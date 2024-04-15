package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.dataCompute.applicationCapacity.dataCompute.dataComputeUnit.util.CoreRealmOperationUtil;
import com.viewfunction.docg.dataCompute.computeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.computeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.*;

public class SyncConceptionEntitiesToNewDataSliceView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private HorizontalLayout doesNotDetectDataGridInfoMessage;
    private VerticalLayout contentContainer;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;
    private Button syncToDataSliceButton;
    private TextField dataSliceNameField;
    private TextField dataSliceGroupField;
    private Checkbox useConceptionEntityUIDAsDataSlicePKCheckbox;
    private Checkbox clearExistDataSliceDataCheckbox;

    public SyncConceptionEntitiesToNewDataSliceView(String conceptionKindName) {
        this.setWidthFull();
        this.conceptionKindName = conceptionKindName;

        doesNotDetectDataGridInfoMessage = new HorizontalLayout();
        doesNotDetectDataGridInfoMessage.setSpacing(true);
        doesNotDetectDataGridInfoMessage.setPadding(true);
        doesNotDetectDataGridInfoMessage.setMargin(true);
        doesNotDetectDataGridInfoMessage.setWidth(100, Unit.PERCENTAGE);
        Icon messageLogo = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        messageLogo.getStyle()
                .set("color", "#ce0000").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 未检测到运行中的数据计算网格");
        messageLabel.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("color", "#ce0000");
        doesNotDetectDataGridInfoMessage.add(messageLogo, messageLabel);
        add(doesNotDetectDataGridInfoMessage);
        doesNotDetectDataGridInfoMessage.setVisible(false);

        contentContainer = new VerticalLayout();
        contentContainer.setWidthFull();
        contentContainer.setSpacing(false);
        contentContainer.setPadding(false);
        contentContainer.setMargin(false);
        add(contentContainer);

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right", "3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        contentContainer.add(entityInfoFootprintMessageBar);
        contentContainer.setVisible(true);

        HorizontalLayout syncOperationContentContainer = new HorizontalLayout();
        syncOperationContentContainer.setSpacing(false);
        syncOperationContentContainer.setPadding(false);
        syncOperationContentContainer.setMargin(false);
        this.contentContainer.add(syncOperationContentContainer);

        VerticalLayout conceptionKindAttributesInfoLayout = new VerticalLayout();
        conceptionKindAttributesInfoLayout.setWidth(400,Unit.PIXELS);
        conceptionKindAttributesInfoLayout.setSpacing(false);
        conceptionKindAttributesInfoLayout.setPadding(false);
        conceptionKindAttributesInfoLayout.setMargin(false);

        VerticalLayout syncDataSliceDataControllerLayout = new VerticalLayout();
        syncDataSliceDataControllerLayout.setWidth(200,Unit.PIXELS);
        syncDataSliceDataControllerLayout.setSpacing(false);
        syncDataSliceDataControllerLayout.setPadding(false);
        syncDataSliceDataControllerLayout.setMargin(false);
        syncOperationContentContainer.add(conceptionKindAttributesInfoLayout,syncDataSliceDataControllerLayout);

        ThirdLevelIconTitle dataSlicesInfoTitle = new ThirdLevelIconTitle(VaadinIcon.ALIGN_LEFT.create(),"概念类型属性");
        dataSlicesInfoTitle.getStyle().set("padding-bottom","5px");
        dataSlicesInfoTitle.getStyle().set("padding-top","10px");
        conceptionKindAttributesInfoLayout.add(dataSlicesInfoTitle);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> getAttributeName(kindEntityAttributeRuntimeStatistics));
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        conceptionKindAttributesInfoLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle sliceSyncInfoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ARROWS_LONG_H),"切片数据导出配置");
        sliceSyncInfoTitle.getStyle().set("padding-bottom","5px");
        sliceSyncInfoTitle.getStyle().set("padding-top","10px");
        sliceSyncInfoTitle.getStyle().set("padding-left","5px");
        syncDataSliceDataControllerLayout.add(sliceSyncInfoTitle);

        VerticalLayout syncDataSliceDataControllerContentContainer= new VerticalLayout();
        syncDataSliceDataControllerLayout.setWidth(270,Unit.PIXELS);
        syncDataSliceDataControllerContentContainer.setSpacing(true);
        syncDataSliceDataControllerContentContainer.setPadding(true);
        syncDataSliceDataControllerContentContainer.setMargin(true);
        syncDataSliceDataControllerLayout.add(syncDataSliceDataControllerContentContainer);

        dataSliceNameField = new TextField();
        dataSliceNameField.setWidth(250, Unit.PIXELS);
        dataSliceNameField.setPlaceholder("数据切片名称");
        dataSliceNameField.setRequired(true);
        dataSliceNameField.setRequiredIndicatorVisible(true);
        dataSliceNameField.setPrefixComponent(LineAwesomeIconsSvg.CLONE.create());
        syncDataSliceDataControllerContentContainer.add(dataSliceNameField);

        dataSliceGroupField = new TextField();
        dataSliceGroupField.setWidth(250, Unit.PIXELS);
        dataSliceGroupField.setPlaceholder("数据切片分组");
        dataSliceGroupField.setRequired(true);
        dataSliceGroupField.setRequiredIndicatorVisible(true);
        dataSliceGroupField.setPrefixComponent(VaadinIcon.ARCHIVES.create());
        syncDataSliceDataControllerContentContainer.add(dataSliceGroupField);

        useConceptionEntityUIDAsDataSlicePKCheckbox = new Checkbox();
        useConceptionEntityUIDAsDataSlicePKCheckbox.setLabel("使用概念实体唯一值ID做为数据切片主键");
        useConceptionEntityUIDAsDataSlicePKCheckbox.addClassNames("text-tertiary");
        syncDataSliceDataControllerContentContainer.add(useConceptionEntityUIDAsDataSlicePKCheckbox);

        clearExistDataSliceDataCheckbox = new Checkbox();
        clearExistDataSliceDataCheckbox.setLabel("覆写同名目标数据切片中的已有数据");
        clearExistDataSliceDataCheckbox.addClassNames("text-tertiary");
        syncDataSliceDataControllerContentContainer.add(clearExistDataSliceDataCheckbox);

        syncToDataSliceButton = new Button("导出至数据切片",LineAwesomeIconsSvg.MEMORY_SOLID.create());
        syncToDataSliceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        syncDataSliceDataControllerContentContainer.add(syncToDataSliceButton);
        syncToDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                syncToDataSlice();
            }
        });
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
            doesNotDetectDataGridInfoMessage.setVisible(false);
            contentContainer.setVisible(true);
            loadConceptionKindPropertiesInfo();
        } catch (ComputeGridException e) {
            doesNotDetectDataGridInfoMessage.setVisible(true);
            contentContainer.setVisible(false);
        }
    }

    private String getAttributeName(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics){
        return kindEntityAttributeRuntimeStatistics.getAttributeName();
    }

    private void loadConceptionKindPropertiesInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(1000000);
        coreRealm.closeGlobalSession();
        conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
    }

    private void syncToDataSlice(){
        Set<KindEntityAttributeRuntimeStatistics> selectedConceptionKindAttributesSet =this.conceptionKindAttributesInfoGrid.getSelectedItems();
        if(selectedConceptionKindAttributesSet.size() == 0){
            CommonUIOperationUtil.showPopupNotification("请选择至少一项概念类型属性", NotificationVariant.LUMO_WARNING,0, Notification.Position.MIDDLE);
            return;
        }
        String dataSliceName = dataSliceNameField.getValue();
        if(dataSliceName == null || dataSliceName.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入数据切片名称", NotificationVariant.LUMO_WARNING,0, Notification.Position.MIDDLE);
            return;
        }
        String dataSliceGroupName = dataSliceGroupField.getValue();
        if(dataSliceGroupName == null || dataSliceGroupName.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入数据切片分组名称", NotificationVariant.LUMO_WARNING,0, Notification.Position.MIDDLE);
            return;
        }
        boolean weatherUseUIDAsPk = useConceptionEntityUIDAsDataSlicePKCheckbox.getValue();
        boolean overwriteSameNameDataSliceData = clearExistDataSliceDataCheckbox.getValue();

        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认导出实体数据至数据切片",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认是否向数据切片 "+dataSliceName+" 导出概念类型实体数据",actionButtonList,650,180);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doSyncConceptionEntitiesToDataSlice(confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doSyncConceptionEntitiesToDataSlice(ConfirmWindow confirmWindow){
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setResultNumber(100000000);


        List<String> dataSliceNameList = new ArrayList<>();

        Map<String, DataSlicePropertyType> dataSlicePropertyMap = new HashMap<>();

        Set<KindEntityAttributeRuntimeStatistics> selectedConceptionKindAttributesSet =this.conceptionKindAttributesInfoGrid.getSelectedItems();
        for(KindEntityAttributeRuntimeStatistics currentKindEntityAttributeRuntimeStatistics : selectedConceptionKindAttributesSet){
            dataSliceNameList.add(currentKindEntityAttributeRuntimeStatistics.getAttributeName());

            dataSlicePropertyMap.put(currentKindEntityAttributeRuntimeStatistics.getAttributeName(),DataSlicePropertyType.STRING);
        }



        CoreRealmOperationUtil.syncConceptionKindToDataSlice(this.conceptionKindName,null,null,dataSlicePropertyMap,queryParameters);

        //CoreRealmOperationUtil.loadConceptionKindEntitiesToDataSlice(this.conceptionKindName,dataSliceNameList,queryParameters,dataSliceNameField.getValue(),useConceptionEntityUIDAsDataSlicePKCheckbox.getValue(),10);
        //CoreRealmOperationUtil.refreshDataSliceAndLoadDataFromConceptionKind(dataSliceGroupField.getValue(),dataSliceNameField.getValue(),);


        confirmWindow.closeConfirmWindow();
        if(this.containerDialog != null){
            this.containerDialog.close();
        }

    }


}
