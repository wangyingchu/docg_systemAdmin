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
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.AttributesParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.filteringItem.FilteringItem;
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
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;

import static java.util.Arrays.asList;

public class DownloadArrowFormatQueryResultsView extends VerticalLayout {
    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private Button cancelImportButton;
    private Label excelFileName;
    private HorizontalLayout downloaderContainer;
    private String excelDataFileURI;
    private long conceptionEntitiesCount;
    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;

    public DownloadArrowFormatQueryResultsView(String conceptionKindName, ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult, int viewWidth){
        this.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;

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

        excelFileName = new Label();
        excelFileName.addClassNames("text-xs");
        dataFileInfoLayout.add(messageContentLabel, excelFileName);
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
        excelDataFileURI = fileFolder.getAbsolutePath()+"/"+ dataFileName;

        List<ConceptionEntityValue> conceptionEntityValueList = this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();
        List<List<Object>> excelRowDataList = new ArrayList<>();

        Set<String> resultAttributeNamesSet = new HashSet<>();
        QueryParameters queryParameters = this.conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getQueryParameters();
        AttributesParameters attributesParameters = queryParameters.getAttributesParameters();

        if(attributesParameters.getDefaultFilteringItem() != null){
            resultAttributeNamesSet.add(attributesParameters.getDefaultFilteringItem().getAttributeName());
        }

        List<FilteringItem> andFilterList = attributesParameters.getAndFilteringItemsList();
        if(andFilterList != null){
            for(FilteringItem currentFilteringItem:andFilterList){
                String attributeName = currentFilteringItem.getAttributeName();
                resultAttributeNamesSet.add(attributeName);
            }
        }

        List<FilteringItem> orFilterList = attributesParameters.getOrFilteringItemsList();
        if(orFilterList != null){
            for(FilteringItem currentFilteringItem:orFilterList){
                String attributeName = currentFilteringItem.getAttributeName();
                resultAttributeNamesSet.add(attributeName);
            }
        }

        List<String> attributeNameList = new ArrayList<>(resultAttributeNamesSet);

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


        

        String[] headerRow = new String[attributeNameList.size()+1];

        for(int i =0;i<attributeNameList.size();i++){
            headerRow[i] = attributeNameList.get(i);
        }
        headerRow[attributeNameList.size()] = "Entity_UID";

        if(conceptionEntityValueList != null && conceptionEntityValueList.size() >0){
            for(ConceptionEntityValue currentConceptionEntityValue:conceptionEntityValueList){
                ArrayList<Object> currentRowList = new ArrayList();
                excelRowDataList.add(currentRowList);

                Map<String,Object> entityAttributesValueMap = currentConceptionEntityValue.getEntityAttributesValue();
                for(int i =0;i<attributeNameList.size();i++){
                    String attributeName = attributeNameList.get(i);
                    Object currentAttributeValue = entityAttributesValueMap.get(attributeName) != null ?
                            entityAttributesValueMap.get(attributeName): "NULL";
                    currentRowList.add(currentAttributeValue);
                }
                currentRowList.add(currentConceptionEntityValue.getConceptionEntityUID());
            }
        }








        Field age = new Field("age",
                FieldType.nullable(new ArrowType.Int(32, true)),
                /*children*/ null);
        Field name = new Field("name",
                FieldType.nullable(new ArrowType.Utf8()),
                /*children*/ null);
        Schema schema = new Schema(asList(age, name));
        try(
                BufferAllocator allocator = new RootAllocator();
                VectorSchemaRoot root = VectorSchemaRoot.create(schema, allocator);
                IntVector ageVector = (IntVector) root.getVector("age");
                VarCharVector nameVector = (VarCharVector) root.getVector("name");
        ){
            ageVector.allocateNew(3);
            ageVector.set(0, 10);
            ageVector.set(1, 20);
            ageVector.set(2, 30);
            nameVector.allocateNew(3);
            nameVector.set(0, "Dave".getBytes(StandardCharsets.UTF_8));
            nameVector.set(1, "Peter".getBytes(StandardCharsets.UTF_8));
            nameVector.set(2, "Mary".getBytes(StandardCharsets.UTF_8));
            root.setRowCount(3);
            File file = new File(excelDataFileURI);
            try (
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    ArrowFileWriter writer = new ArrowFileWriter(root, /*provider*/ null, fileOutputStream.getChannel());
            ) {
                writer.start();
                writer.writeBatch();
                writer.end();
                System.out.println("Record batches written: " + writer.getRecordBlocks().size()
                        + ". Number of rows written: " + root.getRowCount());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }










        excelFileName.setText(dataFileName);

        Button downloadButton = new Button("点击下载 ARROW 数据文件");
        downloadButton.setIcon(VaadinIcon.DOWNLOAD_ALT.create());

        FileDownloadWrapper arrowFileDownloader = new FileDownloadWrapper(dataFileName,new File(TEMP_FILES_STORAGE_LOCATION));
        arrowFileDownloader.wrapComponent(downloadButton);
        downloaderContainer.add(arrowFileDownloader);
        arrowFileDownloader.setFile(new File(excelDataFileURI));
    }

    private void deleteArrowDataFile(){
        if(excelDataFileURI != null){
            FileUtil.del(excelDataFileURI);
        }
    }


}
