package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

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

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.File;

import java.text.NumberFormat;
import java.util.*;

public class DownloadExcelFormatQueryResultsView extends VerticalLayout {
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
    private  List<String> queryAttributesList;

    public DownloadExcelFormatQueryResultsView(String conceptionKindName, ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult, List<String> queryAttributesList,int viewWidth){
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
        Label messageContentLabel = new Label("EXCEL 数据文件: ");
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

        cancelImportButton = new Button("取消或结束导出 EXCEL 格式概念实体数据");
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

        generateExcelFromAttributesRetrieveResult();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void generateExcelFromAttributesRetrieveResult(){

        File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
        if(!fileFolder.exists()){
            fileFolder.mkdirs();
        }

        String dataFileName = PinyinUtil.getPinyin(this.conceptionKindName,"")+"_"+System.currentTimeMillis()+"_QUERY_EXPORT.xlsx";
        excelDataFileURI = fileFolder.getAbsolutePath()+"/"+ dataFileName;

        List<ConceptionEntityValue> conceptionEntityValueList = this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();
        List<List<Object>> excelRowDataList = new ArrayList<>();

        List<String> attributeNameList = new ArrayList<>(queryAttributesList);
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
                    currentRowList.add(entityAttributesValueMap.get(attributeName));
                }
                currentRowList.add(currentConceptionEntityValue.getConceptionEntityUID());
            }
        }

        ExcelWriter excelWriter= ExcelUtil.getWriter(excelDataFileURI);
        excelWriter.writeHeadRow(Arrays.asList(headerRow));
        excelWriter.write(excelRowDataList,true);
        excelWriter.close();

        excelFileName.setText(dataFileName);

        Button downloadButton = new Button("点击下载 EXCEL 数据文件");
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
