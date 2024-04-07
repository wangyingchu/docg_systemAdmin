package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;

import com.viewfunction.docg.dataCompute.computeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceDetailInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.computeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncConceptionEntitiesToExistingDataSliceView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private HorizontalLayout doesNotDetectDataGridInfoMessage;
    private VerticalLayout contentContainer;
    private Grid<DataSliceMetaInfo> dataSliceMetaInfoGrid;
    private EntityAttributeNamesMappingView entityAttributeNamesMappingView;

    private class DataSlicePropertyDefinitionVO{
        private String propertyName;
        private DataSlicePropertyType dataSlicePropertyType;
        private boolean isPrimaryKey;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public DataSlicePropertyType getDataSlicePropertyType() {
            return dataSlicePropertyType;
        }

        public void setDataSlicePropertyType(DataSlicePropertyType dataSlicePropertyType) {
            this.dataSlicePropertyType = dataSlicePropertyType;
        }

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            isPrimaryKey = primaryKey;
        }
    }

    public SyncConceptionEntitiesToExistingDataSliceView(String conceptionKindName){
        this.setWidthFull();
        this.conceptionKindName = conceptionKindName;

        this.doesNotDetectDataGridInfoMessage = new HorizontalLayout();
        this.doesNotDetectDataGridInfoMessage.setSpacing(true);
        this.doesNotDetectDataGridInfoMessage.setPadding(true);
        this.doesNotDetectDataGridInfoMessage.setMargin(true);
        this.doesNotDetectDataGridInfoMessage.setWidth(100,Unit.PERCENTAGE);
        Icon messageLogo = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        messageLogo.getStyle()
                .set("color","#ce0000").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 未检测到运行中的数据计算网格");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#ce0000");
        this.doesNotDetectDataGridInfoMessage.add(messageLogo,messageLabel);
        add(this.doesNotDetectDataGridInfoMessage);
        this.doesNotDetectDataGridInfoMessage.setVisible(false);

        this.contentContainer = new VerticalLayout();
        this.contentContainer.setWidthFull();
        this.contentContainer.setSpacing(false);
        this.contentContainer.setPadding(false);
        this.contentContainer.setMargin(false);
        add(this.contentContainer);

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        this.contentContainer.add(entityInfoFootprintMessageBar);
        this.contentContainer.setVisible(true);

        HorizontalLayout syncOperationContentContainer = new HorizontalLayout();
        syncOperationContentContainer.setSpacing(false);
        syncOperationContentContainer.setPadding(false);
        syncOperationContentContainer.setMargin(false);
        this.contentContainer.add(syncOperationContentContainer);

        VerticalLayout existingDataSliceInfoLayout = new VerticalLayout();
        existingDataSliceInfoLayout.setWidth(500,Unit.PIXELS);
        existingDataSliceInfoLayout.setSpacing(false);
        existingDataSliceInfoLayout.setPadding(false);
        existingDataSliceInfoLayout.setMargin(false);
        VerticalLayout conceptionKindAttributesInfoLayout = new VerticalLayout();
        conceptionKindAttributesInfoLayout.setWidth(300,Unit.PIXELS);
        conceptionKindAttributesInfoLayout.setSpacing(false);
        conceptionKindAttributesInfoLayout.setPadding(false);
        conceptionKindAttributesInfoLayout.setMargin(false);
        VerticalLayout syncDataSliceDataControllerLayout = new VerticalLayout();
        syncDataSliceDataControllerLayout.setWidth(200,Unit.PIXELS);
        syncDataSliceDataControllerLayout.setSpacing(false);
        syncDataSliceDataControllerLayout.setPadding(false);
        syncDataSliceDataControllerLayout.setMargin(false);
        syncOperationContentContainer.add(existingDataSliceInfoLayout,conceptionKindAttributesInfoLayout,syncDataSliceDataControllerLayout);

        ThirdLevelIconTitle dataSlicesInfoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.CLONE.create(),"已有数据切片");
        dataSlicesInfoTitle.getStyle().set("padding-bottom","5px");
        dataSlicesInfoTitle.getStyle().set("padding-top","10px");
        existingDataSliceInfoLayout.add(dataSlicesInfoTitle);

        this.dataSliceMetaInfoGrid = new Grid<>();
        this.dataSliceMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        existingDataSliceInfoLayout.add(this.dataSliceMetaInfoGrid);

        this.dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getDataSliceName).setHeader("切片名称").setKey("idx_0").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<DataSliceMetaInfo>() {
            @Override
            public String apply(DataSliceMetaInfo dataSliceMetaInfo) {
                return dataSliceMetaInfo.getDataSliceName();
            }
        });
        this.dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getSliceGroupName).setHeader("切片分组").setKey("idx_1").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<DataSliceMetaInfo>() {
            @Override
            public String apply(DataSliceMetaInfo dataSliceMetaInfo) {
                return dataSliceMetaInfo.getSliceGroupName();
            }
        });
        this.dataSliceMetaInfoGrid.addColumn(new NumberRenderer<>(DataSliceMetaInfo::getPrimaryDataCount, NumberFormat.getIntegerInstance())).setHeader("切片数据量").setKey("idx_3").setWidth("130px").setFlexGrow(0);

        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"切片名称");
        this.dataSliceMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(VaadinIcon.ARCHIVES,"切片分组");
        this.dataSliceMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.STOCK,"切片数据量");
        this.dataSliceMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);

        this.dataSliceMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<DataSliceMetaInfo>, DataSliceMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<DataSliceMetaInfo>, DataSliceMetaInfo> selectionEvent) {
                Set<DataSliceMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    clearDataSlicePropertiesMappingContent();
                }else{
                    DataSliceMetaInfo selectedDataSliceMetaInfo = selectedItemSet.iterator().next();
                    renderDataSlicePropertiesMappingContent(selectedDataSliceMetaInfo);
                }
            }
        });

        ThirdLevelIconTitle dataPropertiesMappingInfoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ARROWS_LONG_H),"切片数据属性映射");
        dataPropertiesMappingInfoTitle.getStyle().set("padding-bottom","5px");
        dataPropertiesMappingInfoTitle.getStyle().set("padding-top","10px");
        dataPropertiesMappingInfoTitle.getStyle().set("padding-left","5px");
        conceptionKindAttributesInfoLayout.add(dataPropertiesMappingInfoTitle);

        this.entityAttributeNamesMappingView = new EntityAttributeNamesMappingView(null,null);
        this.entityAttributeNamesMappingView.setHeight(460,Unit.PIXELS);
        this.entityAttributeNamesMappingView.setWidth(300,Unit.PIXELS);
        this.entityAttributeNamesMappingView.getStyle().set("padding-left","15px");

        Scroller queryConditionItemsScroller = new Scroller(this.entityAttributeNamesMappingView);
        queryConditionItemsScroller.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)")
                .set("border-top", "1px solid var(--lumo-contrast-20pct)")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        queryConditionItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        conceptionKindAttributesInfoLayout.add(queryConditionItemsScroller);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        checkComputeGridStatusInfo();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void checkComputeGridStatusInfo(){
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        try {
            //Set<DataComputeUnitMetaInfo> dataComputeUnitMetaInfoSet =
            targetComputeGrid.listDataComputeUnit();
            Set<DataSliceMetaInfo> dataSliceMetaInfoSet = targetComputeGrid.listDataSlice();
            this.dataSliceMetaInfoGrid.setItems(dataSliceMetaInfoSet);
            doesNotDetectDataGridInfoMessage.setVisible(false);
            contentContainer.setVisible(true);
        } catch (ComputeGridException e) {
            doesNotDetectDataGridInfoMessage.setVisible(true);
            contentContainer.setVisible(false);
        }
    }

    private void clearDataSlicePropertiesMappingContent(){
        this.entityAttributeNamesMappingView.refreshEntityAttributeNamesMappingInfo(null,null);
    }

    private void renderDataSlicePropertiesMappingContent(DataSliceMetaInfo dataSliceMetaInfo){
        if(dataSliceMetaInfo != null){
            String dataSliceName = dataSliceMetaInfo.getDataSliceName();
            ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
            try {
                DataSliceDetailInfo dataSliceDetailInfo = targetComputeGrid.getDataSliceDetail(dataSliceName);
                if(dataSliceDetailInfo != null){
                    List<String> sliceAttributesNameList = new ArrayList<>();
                    sliceAttributesNameList.add("A001");
                    sliceAttributesNameList.add("A002");
                    sliceAttributesNameList.add("A003");
                    sliceAttributesNameList.add("A004");
                    sliceAttributesNameList.add("A005");
                    sliceAttributesNameList.add("A006");
                    sliceAttributesNameList.add("A007");
                    sliceAttributesNameList.add("A008");
                    sliceAttributesNameList.add("A009");
                    sliceAttributesNameList.add("A0010");

                    List<String> conceptionKindPropertiesNameList = new ArrayList<>();
                    conceptionKindPropertiesNameList.add("YYYUUU_SOOOP");

                    Set<String> primaryKeyPropertiesNames = dataSliceDetailInfo.getPrimaryKeyPropertiesNames();
                    Map<String, DataSlicePropertyType> dataSlicePropertiesMap = dataSliceDetailInfo.getPropertiesDefinition();
                    List<DataSlicePropertyDefinitionVO> dataSlicePropertyDefinitionVOList = new ArrayList<>();
                    Set<String> propertyNameSet = dataSlicePropertiesMap.keySet();
                    for(String currentName : propertyNameSet){
                        DataSlicePropertyDefinitionVO currentDataSlicePropertyDefinitionVO = new DataSlicePropertyDefinitionVO();
                        currentDataSlicePropertyDefinitionVO.setPropertyName(currentName);
                        currentDataSlicePropertyDefinitionVO.setDataSlicePropertyType(dataSlicePropertiesMap.get(currentName));
                        if(primaryKeyPropertiesNames.contains(currentName)){
                            currentDataSlicePropertyDefinitionVO.setPrimaryKey(true);
                        }else{
                            currentDataSlicePropertyDefinitionVO.setPrimaryKey(false);
                        }
                        dataSlicePropertyDefinitionVOList.add(currentDataSlicePropertyDefinitionVO);

                        sliceAttributesNameList.add(currentName);
                    }
                    this.entityAttributeNamesMappingView.refreshEntityAttributeNamesMappingInfo(sliceAttributesNameList,conceptionKindPropertiesNameList);
                }
            } catch (ComputeGridException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
