package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.pinyin.PinyinUtil;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import com.viewfunction.docg.util.helper.ArrowOperationHelper;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;

public class DownloadArrowFormatQueryResultsView extends VerticalLayout {
    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private Button cancelImportButton;
    private Label arrowFileName;
    private HorizontalLayout downloaderContainer;
    private String arrowDataFileURI;
    private long conceptionEntitiesCount;
    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;
    private  List<String> queryAttributesList;
    private static Logger logger = LoggerFactory.getLogger(DownloadArrowFormatQueryResultsView.class);
    public DownloadArrowFormatQueryResultsView(String conceptionKindName, ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult, List<String> queryAttributesList,int viewWidth){
        this.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;
        this.queryAttributesList = queryAttributesList;

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        conceptionEntitiesCount = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount();

        NumberFormat numberFormat = NumberFormat.getInstance();
        HorizontalLayout entitiesCountContainer = new HorizontalLayout();
        entitiesCountContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(entitiesCountContainer);
        new PrimaryKeyValueDisplayItem(entitiesCountContainer, FontAwesome.Solid.CIRCLE.create(),"查询结果实体数量:",numberFormat.format(conceptionEntitiesCount));

        HorizontalLayout spaceDiv = new HorizontalLayout();
        spaceDiv.setWidth(20,Unit.PIXELS);
        entitiesCountContainer.add(spaceDiv);

        HorizontalLayout dataFileInfoLayout = new HorizontalLayout();
        dataFileInfoLayout.getStyle().set("padding-top","10px");
        Label messageContentLabel = new Label("ARROW 数据文件: ");
        messageContentLabel.addClassNames("text-xs","text-secondary");

        arrowFileName = new Label();
        arrowFileName.addClassNames("text-xs");
        dataFileInfoLayout.add(messageContentLabel, arrowFileName);
        add(dataFileInfoLayout);

        downloaderContainer = new HorizontalLayout();
        downloaderContainer.setHeight(40,Unit.PIXELS);
        add(downloaderContainer);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonbarLayout = new HorizontalLayout();
        add(buttonbarLayout);
        setHorizontalComponentAlignment(Alignment.END,buttonbarLayout);

        cancelImportButton = new Button("取消或结束导出 ARROW 格式概念实体数据");
        cancelImportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        cancelImportButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(containerDialog != null){
                    containerDialog.close();
                }
                deleteArrowDataFile();
            }
        });
        buttonbarLayout.add(cancelImportButton);

        generateArrowFromAttributesRetrieveResult();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void generateArrowFromAttributesRetrieveResult(){

        File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
        if(!fileFolder.exists()){
            fileFolder.mkdirs();
        }

        String dataFileName = PinyinUtil.getPinyin(this.conceptionKindName,"")+"_"+System.currentTimeMillis()+"_QUERY_EXPORT.arrow";
        arrowDataFileURI = fileFolder.getAbsolutePath()+"/"+ dataFileName;

        List<ConceptionEntityValue> conceptionEntityValueList = this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();

        List<String> attributeNameList = new ArrayList<>(queryAttributesList);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        List<AttributeSystemInfo> attributeSystemInfoList = systemMaintenanceOperator.getConceptionKindAttributesSystemInfo(this.conceptionKindName);
        Map<String,String> attributeDataTypeMapping = new HashMap<>();
        for(AttributeSystemInfo currentAttributeSystemInfo:attributeSystemInfoList){
            attributeDataTypeMapping.put(currentAttributeSystemInfo.getAttributeName(),currentAttributeSystemInfo.getDataType());
        }

        List<Field> headerFieldList = new ArrayList<>();
        for(String currentFieldName : attributeNameList){
            String currentFieldDataType = attributeDataTypeMapping.get(currentFieldName);
            headerFieldList.add(ArrowOperationHelper.getArrowField(currentFieldName,currentFieldDataType));
        }
        headerFieldList.add(ArrowOperationHelper.getArrowField("Entity_UID","STRING"));
        Schema arrorSchema = new Schema(headerFieldList);

        BufferAllocator allocator = new RootAllocator();
        VectorSchemaRoot root = VectorSchemaRoot.create(arrorSchema, allocator);

        //create vectors
        Map<String,FieldVector> fieldVectorMap = new HashMap<>();
        for(String currentFieldName : attributeNameList){
            String currentFieldDataType = attributeDataTypeMapping.get(currentFieldName);
            FieldVector currentFieldVector = ArrowOperationHelper.getArrowVector(root,currentFieldName,currentFieldDataType);
            fieldVectorMap.put(currentFieldName,currentFieldVector);
        }
        FieldVector entityUIDFieldVector = ArrowOperationHelper.getArrowVector(root,"Entity_UID","STRING");
        fieldVectorMap.put("Entity_UID",entityUIDFieldVector);

        //allocateNew vectors
        for(String currentFieldName : attributeNameList){
            String currentFieldDataType = attributeDataTypeMapping.get(currentFieldName);
            FieldVector currentFieldVector = fieldVectorMap.get(currentFieldName);
            ArrowOperationHelper.allocateNewArrowVector(currentFieldVector,currentFieldDataType,(int)conceptionEntitiesCount);
        }
        entityUIDFieldVector = fieldVectorMap.get("Entity_UID");
        ArrowOperationHelper.allocateNewArrowVector(entityUIDFieldVector,"STRING",(int)conceptionEntitiesCount);

        //set vectorsData
        if(conceptionEntityValueList != null){
            VarCharVector entityUIDCharVector = (VarCharVector)fieldVectorMap.get("Entity_UID");
            for(int i=0; i< conceptionEntityValueList.size(); i++){
                ConceptionEntityValue currentConceptionEntityValue = conceptionEntityValueList.get(i);
                Map<String,Object> currentEntityAttributesValue = currentConceptionEntityValue.getEntityAttributesValue();

                Set<String> keySet = currentEntityAttributesValue.keySet();
                for(String currentKey:keySet){
                    String currentAttributeDataType = attributeDataTypeMapping.get(currentKey);
                    if(currentAttributeDataType != null) {
                        switch (currentAttributeDataType) {
                            case "STRING":
                                VarCharVector varCharVector = (VarCharVector)fieldVectorMap.get(currentKey);
                                if(varCharVector != null){
                                    if(currentEntityAttributesValue.get(currentKey) != null){
                                        varCharVector.setSafe(i, currentEntityAttributesValue.get(currentKey).toString().getBytes(StandardCharsets.UTF_8));
                                    }
                                }
                                break;
                        }
                    }
                }
                entityUIDCharVector.setSafe(i, currentConceptionEntityValue.getConceptionEntityUID().getBytes(StandardCharsets.UTF_8));
            }
        }

        root.setRowCount(conceptionEntityValueList.size());
        File file = new File(arrowDataFileURI);
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ArrowFileWriter writer = new ArrowFileWriter(root, /*provider*/ null, fileOutputStream.getChannel());
        ) {
            writer.start();
            writer.writeBatch();
            writer.end();
            logger.debug("ArrowRecord batches written: {}. Number of rows written: {}", writer.getRecordBlocks().size(),root.getRowCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //close vectors
        Collection<FieldVector> fieldVectors = fieldVectorMap.values();
        for(FieldVector currentFieldVector:fieldVectors){
            currentFieldVector.close();
        }
        allocator.close();

        arrowFileName.setText(dataFileName);

        Button downloadButton = new Button("点击下载 ARROW 数据文件");
        downloadButton.setIcon(VaadinIcon.DOWNLOAD_ALT.create());

        FileDownloadWrapper arrowFileDownloader = new FileDownloadWrapper(dataFileName,new File(TEMP_FILES_STORAGE_LOCATION));
        arrowFileDownloader.wrapComponent(downloadButton);
        downloaderContainer.add(arrowFileDownloader);
        arrowFileDownloader.setFile(new File(arrowDataFileURI));
    }

    private void deleteArrowDataFile(){
        if(arrowDataFileURI != null){
            FileUtil.del(arrowDataFileURI);
        }
    }
}
