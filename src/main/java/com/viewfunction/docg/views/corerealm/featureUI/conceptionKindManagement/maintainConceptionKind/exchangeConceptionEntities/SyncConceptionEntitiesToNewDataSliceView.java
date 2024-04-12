package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.dataCompute.computeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.computeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SyncConceptionEntitiesToNewDataSliceView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private HorizontalLayout doesNotDetectDataGridInfoMessage;
    private VerticalLayout contentContainer;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;
    private EntityAttributeNamesMappingView entityAttributeNamesMappingView;
    private SecondaryTitleActionBar selectedDataSliceNameInfoActionBar;
    private SecondaryTitleActionBar selectedDataSliceGroupInfoActionBar;
    private String selectedDataSliceName;
    private String selectedDataSliceGroup;
    private Button syncToDataSliceButton;

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

        VerticalLayout existingDataSliceInfoLayout = new VerticalLayout();
        existingDataSliceInfoLayout.setWidth(400,Unit.PIXELS);
        existingDataSliceInfoLayout.setSpacing(false);
        existingDataSliceInfoLayout.setPadding(false);
        existingDataSliceInfoLayout.setMargin(false);

        /*
        VerticalLayout conceptionKindAttributesInfoLayout = new VerticalLayout();
        conceptionKindAttributesInfoLayout.setWidth(500,Unit.PIXELS);
        conceptionKindAttributesInfoLayout.setSpacing(false);
        conceptionKindAttributesInfoLayout.setPadding(false);
        conceptionKindAttributesInfoLayout.setMargin(false);
        */

        VerticalLayout syncDataSliceDataControllerLayout = new VerticalLayout();
        syncDataSliceDataControllerLayout.setWidth(200,Unit.PIXELS);
        syncDataSliceDataControllerLayout.setSpacing(false);
        syncDataSliceDataControllerLayout.setPadding(false);
        syncDataSliceDataControllerLayout.setMargin(false);
        //syncOperationContentContainer.add(existingDataSliceInfoLayout,conceptionKindAttributesInfoLayout,syncDataSliceDataControllerLayout);
        syncOperationContentContainer.add(existingDataSliceInfoLayout,syncDataSliceDataControllerLayout);

        ThirdLevelIconTitle dataSlicesInfoTitle = new ThirdLevelIconTitle(VaadinIcon.ALIGN_LEFT.create(),"概念类型属性");
        dataSlicesInfoTitle.getStyle().set("padding-bottom","5px");
        dataSlicesInfoTitle.getStyle().set("padding-top","10px");
        existingDataSliceInfoLayout.add(dataSlicesInfoTitle);

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

        existingDataSliceInfoLayout.add(conceptionKindAttributesInfoGrid);






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

        TextField dataSliceNameField = new TextField();
        dataSliceNameField.setWidth(180, Unit.PIXELS);
        dataSliceNameField.setPlaceholder("数据切片名称");
        dataSliceNameField.setRequired(true);
        dataSliceNameField.setRequiredIndicatorVisible(true);
        dataSliceNameField.setPrefixComponent(LineAwesomeIconsSvg.CLONE.create());
        syncDataSliceDataControllerContentContainer.add(dataSliceNameField);

        TextField dataSliceGroupField = new TextField();
        dataSliceGroupField.setWidth(180, Unit.PIXELS);
        dataSliceGroupField.setPlaceholder("数据切片分组");
        dataSliceGroupField.setRequired(true);
        dataSliceGroupField.setRequiredIndicatorVisible(true);
        dataSliceGroupField.setPrefixComponent(VaadinIcon.ARCHIVES.create());
        syncDataSliceDataControllerContentContainer.add(dataSliceGroupField);

        /*
        selectedDataSliceNameInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,null,false);
        selectedDataSliceNameInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        syncDataSliceDataControllerContentContainer.add(selectedDataSliceNameInfoActionBar);

        selectedDataSliceGroupInfoActionBar = new SecondaryTitleActionBar(VaadinIcon.ARCHIVES.create(), "-",null,null);
        selectedDataSliceGroupInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        syncDataSliceDataControllerContentContainer.add(selectedDataSliceGroupInfoActionBar);
        */


        syncToDataSliceButton = new Button("导出至数据切片",LineAwesomeIconsSvg.MEMORY_SOLID.create());
        syncToDataSliceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        syncDataSliceDataControllerContentContainer.add(syncToDataSliceButton);
        syncToDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //syncToDataSlice();
            }
        });
        this.syncToDataSliceButton.setEnabled(false);







/*
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
        queryConditionItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        conceptionKindAttributesInfoLayout.add(queryConditionItemsScroller);
        */

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
}
