package com.viewfunction.docg.views.computegrid.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.dataCompute.dataComputeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.ComputeGridRealtimeStatisticsInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataComputeUnitMetaInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ComputeGridRefreshEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.GridRuntimeInfoWidget;
import com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.ComputeGridDataSliceConfigurationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataComputeGridManagementUI extends VerticalLayout implements
        ComputeGridRefreshEvent.ComputeGridRefreshEventListener{

    private Registration listener;
    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout rightSideContentContainerLayout;
    private GridRuntimeInfoWidget gridRuntimeInfoWidget;
    private Grid<DataComputeUnitMetaInfo> computeUnitGrid;
    private ComputeGridDataSliceConfigurationView computeGridDataSliceConfigurationView;

    public DataComputeGridManagementUI(){

        Button refreshDataButton = new Button("刷新计算网格统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            ComputeGridRefreshEvent computeGridRefreshEvent = new ComputeGridRefreshEvent();
            ResourceHolder.getApplicationBlackboard().fire(computeGridRefreshEvent);
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();
        NativeLabel coreRealmTechLabel = new NativeLabel(" Apache Ignite 实现");
        coreRealmTechLabel.getStyle().set("font-size","var(--lumo-font-size-xxs)");
        secTitleElementsList.add(coreRealmTechLabel);
        coreRealmTechLabel.getElement().getThemeList().add("badge success");

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Compute Grid 计算网格管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setSpacing(false);
        leftSideContentContainerLayout.setMaxWidth(630, Unit.PIXELS);
        leftSideContentContainerLayout.setMinWidth(630, Unit.PIXELS);
        leftSideContentContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");
        contentContainerLayout.add(leftSideContentContainerLayout);

        rightSideContentContainerLayout = new VerticalLayout();
        rightSideContentContainerLayout.setSpacing(false);
        rightSideContentContainerLayout.setPadding(false);
        rightSideContentContainerLayout.setMargin(false);
        contentContainerLayout.add(rightSideContentContainerLayout);

        Icon gridUnitsIcon = LineAwesomeIconsSvg.SERVER_SOLID.create();
        SecondaryTitleActionBar gridUnitsActionBar = new SecondaryTitleActionBar(gridUnitsIcon,"网格计算单元",null,null);
        leftSideContentContainerLayout.add(gridUnitsActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(dataComputeUnitMetaInfo -> {

            Icon configIcon = new Icon(VaadinIcon.EYE);
            configIcon.setSize("21px");
            Button showComputeUnitDetail = new Button(configIcon, event -> {
                renderComputeUnitDetailUI((DataComputeUnitMetaInfo)dataComputeUnitMetaInfo);
            });
            showComputeUnitDetail.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showComputeUnitDetail.addThemeVariants(ButtonVariant.LUMO_SMALL);
            showComputeUnitDetail.setTooltipText("显示计算单元详情");

            HorizontalLayout buttons = new HorizontalLayout(showComputeUnitDetail);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(50,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        computeUnitGrid = new Grid<>();

        computeUnitGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        computeUnitGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        computeUnitGrid.setPageSize(5);
        computeUnitGrid.addColumn(DataComputeUnitMetaInfo::getUnitID).setHeader("单元ID").setKey("idx_0").setTooltipGenerator(new ItemLabelGenerator<DataComputeUnitMetaInfo>() {
            @Override
            public String apply(DataComputeUnitMetaInfo dataComputeUnitMetaInfo) {
                return dataComputeUnitMetaInfo.getUnitID();
            }
        });
        computeUnitGrid.addColumn(DataComputeUnitMetaInfo::getUnitHostNames).setHeader("单元主机名").setKey("idx_1").setTooltipGenerator(new ItemLabelGenerator<DataComputeUnitMetaInfo>() {
            @Override
            public String apply(DataComputeUnitMetaInfo dataComputeUnitMetaInfo) {
                return dataComputeUnitMetaInfo.getUnitHostNames().toString();
            }
        });
        computeUnitGrid.addColumn(DataComputeUnitMetaInfo::getUnitIPAddresses).setHeader("单元IP地址").setKey("idx_2").setTooltipGenerator(new ItemLabelGenerator<DataComputeUnitMetaInfo>() {
            @Override
            public String apply(DataComputeUnitMetaInfo dataComputeUnitMetaInfo) {
                return dataComputeUnitMetaInfo.getUnitIPAddresses().toString();
            }
        });
        computeUnitGrid.addColumn(DataComputeUnitMetaInfo::getUnitType).setHeader("单元类型").setKey("idx_3");
        computeUnitGrid.addColumn(DataComputeUnitMetaInfo::getIsClientUnit).setHeader("客户端单元").setKey("idx_4");
        computeUnitGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5")
                .setFlexGrow(0).setWidth("60px").setResizable(false);
        computeUnitGrid.setHeight(300,Unit.PIXELS);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.KEY_O,"单元ID");
        computeUnitGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.LAPTOP,"单元主机名");
        computeUnitGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.MAILBOX,"单元地址");
        computeUnitGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.OPTIONS,"单元类型");
        computeUnitGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.SPECIALIST,"客户端单元");
        computeUnitGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        computeUnitGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5);

        leftSideContentContainerLayout.add(computeUnitGrid);

        VerticalLayout spaceDivLayout1 = new VerticalLayout();
        leftSideContentContainerLayout.add(spaceDivLayout1);

        List<Component> actionComponentsList = new ArrayList<>();
        Button refreshSystemRuntimeInfoButton = new Button("获取网格运行信息",new Icon(VaadinIcon.REFRESH));
        refreshSystemRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshSystemRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshSystemRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        refreshSystemRuntimeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            checkComputeGridStatusInfo();
        });
        actionComponentsList.add(refreshSystemRuntimeInfoButton);

        Icon gridRuntimeStatusIcon = new Icon(VaadinIcon.SPARK_LINE);
        SecondaryTitleActionBar gridRuntimeStatusActionBar = new SecondaryTitleActionBar(gridRuntimeStatusIcon,"网格运行信息",null,actionComponentsList);
        leftSideContentContainerLayout.add(gridRuntimeStatusActionBar);

        gridRuntimeInfoWidget = new GridRuntimeInfoWidget();
        gridRuntimeInfoWidget.setHeight(500,Unit.PIXELS);
        leftSideContentContainerLayout.add(gridRuntimeInfoWidget);

        TabSheet gridConfigurationTabSheet = new TabSheet();
        gridConfigurationTabSheet.setWidthFull();
        rightSideContentContainerLayout.add(gridConfigurationTabSheet);
        rightSideContentContainerLayout.setFlexGrow(1,gridConfigurationTabSheet);

        computeGridDataSliceConfigurationView = new ComputeGridDataSliceConfigurationView();
        gridConfigurationTabSheet.add(generateConfigurationTabTitle(LineAwesomeIconsSvg.BUFFER.create(),"网格数据切片配置"), computeGridDataSliceConfigurationView);
        gridConfigurationTabSheet.add(generateConfigurationTabTitle(LineAwesomeIconsSvg.COGS_SOLID.create(),"网格计算服务配置"),new HorizontalLayout());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-170,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(event.getWidth()-580,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-170,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(browserWidth-580,Unit.PIXELS);
        }));
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        checkComputeGridStatusInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private HorizontalLayout generateConfigurationTabTitle(Icon tabIcon, String tabTitleTxt){
        HorizontalLayout  kindConfigTabLayout = new HorizontalLayout();
        kindConfigTabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kindConfigTabLayout.setHeight(26,Unit.PIXELS);
        tabIcon.setSize("12px");
        NativeLabel configTabLabel = new NativeLabel(" "+tabTitleTxt);
        configTabLabel.getStyle()
                . set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        kindConfigTabLayout.add(tabIcon,configTabLabel);
        return kindConfigTabLayout;
    }

    private void checkComputeGridStatusInfo(){
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        try {
            Set<DataComputeUnitMetaInfo> dataComputeUnitMetaInfoSet = targetComputeGrid.listDataComputeUnit();
            this.computeUnitGrid.setItems(dataComputeUnitMetaInfoSet);

            ComputeGridRealtimeStatisticsInfo computeGridRealtimeStatisticsInfo = targetComputeGrid.getGridRealtimeStatisticsInfo();
            this.gridRuntimeInfoWidget.refreshRuntimeInfo(computeGridRealtimeStatisticsInfo);
        } catch (ComputeGridException e) {
            CommonUIOperationUtil.showPopupNotification("未检测到运行中的数据计算网格", NotificationVariant.LUMO_ERROR,-1, Notification.Position.MIDDLE);
        }
    }

    private void renderComputeUnitDetailUI(DataComputeUnitMetaInfo dataComputeUnitMetaInfo){
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念类型实体数据查询",null,null,true);
        //fullScreenWindow.setWindowContent(conceptionKindQueryUI);
        fullScreenWindow.show();
    }

    @Override
    public void receivedComputeGridRefreshEvent(ComputeGridRefreshEvent event) {
        checkComputeGridStatusInfo();
    }
}
