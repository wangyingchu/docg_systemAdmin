package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.dataCompute.applicationCapacity.dataCompute.dataComputeUnit.util.CoreRealmOperationUtil;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceDetailInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceOperationResult;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.text.NumberFormat;
import java.util.*;

public class SyncConceptionEntitiesToExistingDataSliceView extends VerticalLayout {

    private String conceptionKindName;
    private Dialog containerDialog;
    private HorizontalLayout doesNotDetectDataGridInfoMessage;
    private VerticalLayout contentContainer;
    private Grid<DataSliceMetaInfo> dataSliceMetaInfoGrid;
    private DataSlicePropertiesMappingView entityAttributeNamesMappingView;
    private List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList;
    private SecondaryTitleActionBar selectedDataSliceNameInfoActionBar;
    private SecondaryTitleActionBar selectedDataSliceGroupInfoActionBar;
    private String selectedDataSliceName;
    private String selectedDataSliceGroup;
    private Button syncToDataSliceButton;

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

        this.entityAttributeNamesMappingView = new DataSlicePropertiesMappingView();
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

        ThirdLevelIconTitle sliceSyncInfoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ARROWS_LONG_H),"切片数据导出配置");
        sliceSyncInfoTitle.getStyle().set("padding-bottom","5px");
        sliceSyncInfoTitle.getStyle().set("padding-top","10px");
        sliceSyncInfoTitle.getStyle().set("padding-left","5px");
        syncDataSliceDataControllerLayout.add(sliceSyncInfoTitle);

        VerticalLayout syncDataSliceDataControllerContentContainer= new VerticalLayout();
        syncDataSliceDataControllerLayout.setWidth(200,Unit.PIXELS);
        syncDataSliceDataControllerContentContainer.setSpacing(true);
        syncDataSliceDataControllerContentContainer.setPadding(true);
        syncDataSliceDataControllerContentContainer.setMargin(true);
        syncDataSliceDataControllerLayout.add(syncDataSliceDataControllerContentContainer);

        selectedDataSliceNameInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,null,false);
        selectedDataSliceNameInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        syncDataSliceDataControllerContentContainer.add(selectedDataSliceNameInfoActionBar);

        selectedDataSliceGroupInfoActionBar = new SecondaryTitleActionBar(VaadinIcon.ARCHIVES.create(), "-",null,null);
        selectedDataSliceGroupInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        syncDataSliceDataControllerContentContainer.add(selectedDataSliceGroupInfoActionBar);

        syncToDataSliceButton = new Button("导出至数据切片",LineAwesomeIconsSvg.MEMORY_SOLID.create());
        syncToDataSliceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        syncDataSliceDataControllerContentContainer.add(syncToDataSliceButton);
        syncToDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                syncToDataSlice();
            }
        });
        this.syncToDataSliceButton.setEnabled(false);
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
        this.entityAttributeNamesMappingView.clearMappingInfo();
        this.selectedDataSliceNameInfoActionBar.updateTitleContent("-");
        this.selectedDataSliceGroupInfoActionBar.updateTitleContent("-");
        this.selectedDataSliceName = null;
        this.selectedDataSliceGroup = null;
        this.syncToDataSliceButton.setEnabled(false);
    }

    private void renderDataSlicePropertiesMappingContent(DataSliceMetaInfo dataSliceMetaInfo){
        if(dataSliceMetaInfo != null){
            String dataSliceName = dataSliceMetaInfo.getDataSliceName();
            ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
            try {
                DataSliceDetailInfo dataSliceDetailInfo = targetComputeGrid.getDataSliceDetail(dataSliceName);
                if(dataSliceDetailInfo != null){
                    this.selectedDataSliceNameInfoActionBar.updateTitleContent(dataSliceName);
                    this.selectedDataSliceGroupInfoActionBar.updateTitleContent(dataSliceDetailInfo.getSliceGroupName());
                    this.selectedDataSliceName = dataSliceName;
                    this.selectedDataSliceGroup = dataSliceDetailInfo.getSliceGroupName();
                    this.syncToDataSliceButton.setEnabled(true);
                    if(this.kindEntityAttributeRuntimeStatisticsList == null){
                        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                        coreRealm.openGlobalSession();
                        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                        kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(1000000);
                        coreRealm.closeGlobalSession();
                    }
                    Set<String> primaryKeyPropertiesNames = dataSliceDetailInfo.getPrimaryKeyPropertiesNames();
                    Map<String, DataSlicePropertyType> dataSlicePropertiesMap = dataSliceDetailInfo.getPropertiesDefinition();
                    this.entityAttributeNamesMappingView.renderDataSlicePropertiesMapping(dataSlicePropertiesMap,primaryKeyPropertiesNames,kindEntityAttributeRuntimeStatisticsList);
                }
            } catch (ComputeGridException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void syncToDataSlice(){
        boolean validResult = this.entityAttributeNamesMappingView.validPropertiesMappingStatus();
        if(!validResult){
            CommonUIOperationUtil.showPopupNotification("请选择全部的数据切片主键类型属性映射。如无主键属性，请选择至少一项常规属性映射", NotificationVariant.LUMO_WARNING,0, Notification.Position.MIDDLE);
        }else{
            Map<String,String> attributeMapping = this.entityAttributeNamesMappingView.getAttributesMapping();
            Map<String,String> finalAttributeMapping = new HashMap<>();
            Set<String> attributeNamesSet = attributeMapping.keySet();
            for(String attributeName : attributeNamesSet){
                String attributeValue = attributeMapping.get(attributeName);
                if(attributeValue != null){
                    finalAttributeMapping.put(attributeName,attributeValue);
                }
            }
            if(finalAttributeMapping.size() == 0){
                CommonUIOperationUtil.showPopupNotification("请选择至少一项属性映射", NotificationVariant.LUMO_WARNING,0, Notification.Position.MIDDLE);
            }else{
                List<Button> actionButtonList = new ArrayList<>();
                Button confirmButton = new Button("确认导出实体数据至数据切片",new Icon(VaadinIcon.CHECK_CIRCLE));
                Button cancelButton = new Button("取消操作");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
                actionButtonList.add(confirmButton);
                actionButtonList.add(cancelButton);

                ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认是否向数据切片 "+this.selectedDataSliceName+" 导出概念类型实体数据",actionButtonList,650,180);
                confirmWindow.open();
                confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        doSyncConceptionEntitiesToDataSlice(finalAttributeMapping,confirmWindow);
                    }
                });
                cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        confirmWindow.closeConfirmWindow();
                    }
                });
            }
        }
    }

    private void doSyncConceptionEntitiesToDataSlice(Map<String,String> finalAttributeMapping,ConfirmWindow confirmWindow){
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setResultNumber(100000000);

        DataSliceOperationResult dataSliceOperationResult =CoreRealmOperationUtil.loadConceptionKindEntitiesToDataSlice(this.conceptionKindName,this.selectedDataSliceName,
                finalAttributeMapping,queryParameters,DataSlicePropertyNameMapperWidget.ConceptionEntity_UID_AliasName);

        if(dataSliceOperationResult != null){
            confirmWindow.closeConfirmWindow();
            if(this.containerDialog != null){
                this.containerDialog.close();
            }
            showPopupNotification(dataSliceOperationResult,NotificationVariant.LUMO_SUCCESS);
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKindName+" 导出实体数据至数据切片操作失败 ", NotificationVariant.LUMO_ERROR,0, Notification.Position.BOTTOM_START);
        }
    }

    private void showPopupNotification(DataSliceOperationResult dataSliceOperationResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念类型 "+conceptionKindName+" 导出实体数据至数据切片操作完成"));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setFlexGrow(1,text);
        notification.add(layout);

        VerticalLayout notificationMessageContainer = new VerticalLayout();
        notificationMessageContainer.add(new Div(new Text("导出成功实体数: "+dataSliceOperationResult.getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("导出失败实体数: "+dataSliceOperationResult.getFailItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+dataSliceOperationResult.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+dataSliceOperationResult.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(0);
        notification.open();
    }
}
