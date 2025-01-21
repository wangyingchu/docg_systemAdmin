package com.viewfunction.docg.views.dataAnalysis.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.analysisProvider.client.AnalysisProviderAdminClient;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.FeatureRunningInfo;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.FunctionalFeatureInfo;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.chart.TimeSequenceBarChart;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProviderAnalysisFeatureConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem analysisFeatureCountDisplayItem;
    private SecondaryTitleActionBar analysisFeatureInfoActionBar;
    private Registration listener;
    private Grid<FunctionalFeatureInfo> functionalFeatureInfoGrid;
    private GridListDataView<FunctionalFeatureInfo> functionalFeatureInfoDataView;

    private SecondaryKeyValueDisplayItem totalRunTimesDisplayItem;
    private SecondaryKeyValueDisplayItem finishedRunTimeDisplayItem;
    private SecondaryKeyValueDisplayItem unfinishedRunTimeDisplayItem;
    private SecondaryKeyValueDisplayItem firstRunTimeDisplayItem;
    private SecondaryKeyValueDisplayItem lastRunTimeDisplayItem;
    private SecondaryKeyValueDisplayItem totalRunningDurationDisplayItem;
    private SecondaryKeyValueDisplayItem avgRunningDurationDisplayItem;
    private SecondaryKeyValueDisplayItem medianRunningDurationDisplayItem;
    private SecondaryKeyValueDisplayItem shortestRunningDurationDisplayItem;
    private SecondaryKeyValueDisplayItem longestRunningDurationDisplayItem;

    private TextField functionalFeatureNameFilterField;
    private TextField functionalFeatureDescFilterField;
    private Grid<FeatureRunningInfo> analysisFeatureRunningInfoGrid;
    private final int ANALYSIS_CLIENT_HOST_PORT = Integer.parseInt(SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_PORT))+2;
    private final String ANALYSIS_CLIENT_HOST_NAME =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_NAME);
    private List<FeatureRunningInfo> currentFeatureRunningInfoList;
    private FunctionalFeatureInfo lastSelectedFunctionalFeatureInfo;
    private HorizontalLayout functionalFeaturesInfoContainerLayout;
    private DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
    private Button registerFeatureButton;
    private Popover registerFunctionalFeatureOperationButtonPopover;
    private RegisterFunctionalFeatureView registerFunctionalFeatureView;
    private Button refreshDataButton;
    private TimeSequenceBarChart featureRunningInfoTimeSequenceBarChart;

    public ProviderAnalysisFeatureConfigurationView() {
        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();

        this.analysisFeatureCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create()," 分析功能特性数量:","-");

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30, Unit.PIXELS);
        infoContainer.add(horSpaceDiv);

        registerFeatureButton = new Button("注册分析功能特性");
        registerFeatureButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        registerFeatureButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(registerFeatureButton);
        registerFeatureButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                registerFunctionalFeatureOperationButtonPopover.open();
            }
        });

        registerFunctionalFeatureView = new RegisterFunctionalFeatureView(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
        registerFunctionalFeatureOperationButtonPopover = new Popover();
        registerFunctionalFeatureOperationButtonPopover.setTarget(registerFeatureButton);
        registerFunctionalFeatureOperationButtonPopover.setWidth("450px");
        registerFunctionalFeatureOperationButtonPopover.setHeight("230px");
        registerFunctionalFeatureOperationButtonPopover.addThemeVariants(PopoverVariant.ARROW);
        registerFunctionalFeatureOperationButtonPopover.setPosition(PopoverPosition.BOTTOM);
        registerFunctionalFeatureOperationButtonPopover.add(registerFunctionalFeatureView);
        registerFunctionalFeatureOperationButtonPopover.setAutofocus(true);
        registerFunctionalFeatureOperationButtonPopover.setModal(true);
        registerFunctionalFeatureView.setContainerPopover(registerFunctionalFeatureOperationButtonPopover);

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"分析功能特性定义:",null);
        add(sectionActionBar);

        functionalFeaturesInfoContainerLayout = new HorizontalLayout();
        functionalFeaturesInfoContainerLayout.setPadding(false);
        functionalFeaturesInfoContainerLayout.setMargin(false);
        add(functionalFeaturesInfoContainerLayout);

        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setPadding(false);
        leftSideLayout.setMargin(false);
        leftSideLayout.setWidth(950,Unit.PIXELS);
        functionalFeaturesInfoContainerLayout.add(leftSideLayout);

        HorizontalLayout functionalFeaturesSearchElementsContainerLayout = new HorizontalLayout();
        functionalFeaturesSearchElementsContainerLayout.setSpacing(false);
        functionalFeaturesSearchElementsContainerLayout.setMargin(false);
        leftSideLayout.add(functionalFeaturesSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        functionalFeaturesSearchElementsContainerLayout.add(filterTitle);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        functionalFeatureNameFilterField = new TextField();
        functionalFeatureNameFilterField.setPlaceholder("分析功能特性名称");
        functionalFeatureNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        functionalFeatureNameFilterField.setWidth(150,Unit.PIXELS);
        functionalFeaturesSearchElementsContainerLayout.add(functionalFeatureNameFilterField);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, functionalFeatureNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        functionalFeaturesSearchElementsContainerLayout.add(plusIcon);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        functionalFeatureDescFilterField = new TextField();
        functionalFeatureDescFilterField.setPlaceholder("分析功能特性描述");
        functionalFeatureDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        functionalFeatureDescFilterField.setWidth(150,Unit.PIXELS);
        functionalFeaturesSearchElementsContainerLayout.add(functionalFeatureDescFilterField);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, functionalFeatureDescFilterField);

        Button searchFunctionalFeaturesButton = new Button("查找分析功能特性",new Icon(VaadinIcon.SEARCH));
        searchFunctionalFeaturesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchFunctionalFeaturesButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        functionalFeaturesSearchElementsContainerLayout.add(searchFunctionalFeaturesButton);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchFunctionalFeaturesButton);
        searchFunctionalFeaturesButton.setWidth(130,Unit.PIXELS);
        searchFunctionalFeaturesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterFunctionalFeatures();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        functionalFeaturesSearchElementsContainerLayout.add(divIcon);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        functionalFeaturesSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        functionalFeaturesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterFunctionalFeatures();
            }
        });

        functionalFeatureInfoGrid = new Grid<>();
        functionalFeatureInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        functionalFeatureInfoGrid.addColumn(FunctionalFeatureInfo::getFunctionalFeatureName).setHeader("分析功能特性名称").setKey("idx_0").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<FunctionalFeatureInfo>() {
            @Override
            public String apply(FunctionalFeatureInfo functionalFeatureInfo) {
                return functionalFeatureInfo.getFunctionalFeatureName();
            }
        });
        functionalFeatureInfoGrid.addColumn(FunctionalFeatureInfo::getFunctionalFeatureDescription).setHeader("分析功能特性描述").setKey("idx_1").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<FunctionalFeatureInfo>() {
            @Override
            public String apply(FunctionalFeatureInfo functionalFeatureInfo) {
                return functionalFeatureInfo.getFunctionalFeatureDescription();
            }
        });
        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分析功能特性名称");
        functionalFeatureInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"分析功能特性描述");
        functionalFeatureInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        functionalFeatureInfoGrid.addSelectionListener(new SelectionListener<Grid<FunctionalFeatureInfo>, FunctionalFeatureInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<FunctionalFeatureInfo>, FunctionalFeatureInfo> selectionEvent) {
                Set<FunctionalFeatureInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    functionalFeatureInfoGrid.select(lastSelectedFunctionalFeatureInfo);
                }else{
                    FunctionalFeatureInfo selectedEntityStatisticsInfo = selectedItemSet.iterator().next();
                    renderFunctionalFeatureRunningOverview(selectedEntityStatisticsInfo);
                    lastSelectedFunctionalFeatureInfo = selectedEntityStatisticsInfo;
                }
            }
        });

        leftSideLayout.add(functionalFeatureInfoGrid);

        RegisterFunctionalFeatureView.RegisterFunctionalFeatureSuccessCallback registerFunctionalFeatureSuccessCallback = new RegisterFunctionalFeatureView.RegisterFunctionalFeatureSuccessCallback() {
            @Override
            public void onExecutionSuccess(String functionalFeatureName, String functionalFeatureDesc) {
                FunctionalFeatureInfo newFunctionalFeatureInfo = new FunctionalFeatureInfo(functionalFeatureName,functionalFeatureDesc);
                ListDataProvider dtaProvider=(ListDataProvider)functionalFeatureInfoGrid.getDataProvider();
                Collection<FunctionalFeatureInfo> functionalFeaturesList = dtaProvider.getItems();
                functionalFeaturesList.add(newFunctionalFeatureInfo);
                functionalFeatureInfoGrid.getDataProvider().refreshAll();
            }
        };
        registerFunctionalFeatureView.setRegisterFunctionalFeatureSuccessCallback(registerFunctionalFeatureSuccessCallback);

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setMargin(false);
        functionalFeaturesInfoContainerLayout.add(rightSideLayout);
        rightSideLayout.getStyle().set("left","0px").set("top","-2px").set("position","relative");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"分析功能特性概览");
        rightSideLayout.add(filterTitle2);

        refreshDataButton = new Button(new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.setTooltipText("刷新分析功能特性运行数据");
        refreshDataButton.setEnabled(false);
        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);
        refreshDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshFunctionalFeatureRunningStatus();
            }
        });

        analysisFeatureInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,buttonList);
        analysisFeatureInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideLayout.add(analysisFeatureInfoActionBar);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.DASHBOARD),"分析功能特性指标");
        rightSideLayout.add(infoTitle2);

        HorizontalLayout statusDisplayContainer = new HorizontalLayout();
        statusDisplayContainer.setSpacing(false);
        statusDisplayContainer.setMargin(false);
        statusDisplayContainer.setPadding(false);
        statusDisplayContainer.setWidthFull();
        rightSideLayout.add(statusDisplayContainer);

        VerticalLayout displayItemsContainerLayout = new VerticalLayout();
        displayItemsContainerLayout.setMinWidth(490,Unit.PIXELS);
        displayItemsContainerLayout.setMaxWidth(490,Unit.PIXELS);
        displayItemsContainerLayout.setSpacing(true);
        displayItemsContainerLayout.setMargin(false);
        displayItemsContainerLayout.setPadding(false);
        statusDisplayContainer.add(displayItemsContainerLayout);

        VerticalLayout chartContainerLayout = new VerticalLayout();
        chartContainerLayout.setSpacing(false);
        chartContainerLayout.setMargin(false);
        chartContainerLayout.setPadding(false);
        statusDisplayContainer.add(chartContainerLayout);
        featureRunningInfoTimeSequenceBarChart = new TimeSequenceBarChart(400,150);
        featureRunningInfoTimeSequenceBarChart.setBarColor("#FF4500");
        featureRunningInfoTimeSequenceBarChart.setPropertyDesc("运行时长（秒）");
        chartContainerLayout.add(featureRunningInfoTimeSequenceBarChart);

        HorizontalLayout displayItemContainer_a = new HorizontalLayout();
        displayItemContainer_a.getStyle().set("padding-left","5px");
        displayItemsContainerLayout.add(displayItemContainer_a);
        totalRunTimesDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_a, VaadinIcon.BOLT.create(),"总运行次数:","-");

        HorizontalLayout spaceDivLayouta0 = new HorizontalLayout();
        spaceDivLayouta0.setWidth(3,Unit.PIXELS);
        displayItemContainer_a.add(spaceDivLayouta0);

        Icon successRunningIcon = VaadinIcon.BOLT.create();
        successRunningIcon.getStyle().set("color", "var(--lumo-success-color)");
        finishedRunTimeDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_a,successRunningIcon ,"运行成功次数:","-");

        HorizontalLayout spaceDivLayouta1 = new HorizontalLayout();
        spaceDivLayouta1.setWidth(3,Unit.PIXELS);
        displayItemContainer_a.add(spaceDivLayouta1);

        Icon unsuccessRunningIcon = VaadinIcon.BOLT.create();
        unsuccessRunningIcon.getStyle().set("color", "var(--lumo-error-color)");
        unfinishedRunTimeDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_a, unsuccessRunningIcon,"未完成运行次数:","-");

        HorizontalLayout displayItemContainer_b = new HorizontalLayout();
        displayItemContainer_b.getStyle().set("padding-left","5px");
        displayItemsContainerLayout.add(displayItemContainer_b);
        firstRunTimeDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_b, VaadinIcon.CALENDAR.create(),"首次运行时间:","-");

        HorizontalLayout spaceDivLayoutb1 = new HorizontalLayout();
        spaceDivLayoutb1.setWidth(5,Unit.PIXELS);
        displayItemContainer_b.add(spaceDivLayoutb1);
        lastRunTimeDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_b, VaadinIcon.CALENDAR.create(),"末次运行时间:","-");

        HorizontalLayout displayItemContainer_c = new HorizontalLayout();
        displayItemContainer_c.getStyle().set("padding-left","5px");
        displayItemsContainerLayout.add(displayItemContainer_c);
        totalRunningDurationDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_c, VaadinIcon.TIME_FORWARD.create(),"成功运行总时长:","-");

        HorizontalLayout spaceDivLayoutc1 = new HorizontalLayout();
        spaceDivLayoutc1.setWidth(5,Unit.PIXELS);
        displayItemContainer_c.add(spaceDivLayoutc1);
        avgRunningDurationDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_c, VaadinIcon.CALC.create(),"成功运行平均时长:","-");

        HorizontalLayout displayItemContainer_d = new HorizontalLayout();
        displayItemContainer_d.getStyle().set("padding-left","5px");
        displayItemsContainerLayout.add(displayItemContainer_d);
        medianRunningDurationDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_d, VaadinIcon.ELLIPSIS_DOTS_H.create(),"成功运行中位数时长:","-");

        HorizontalLayout displayItemContainer_e = new HorizontalLayout();
        displayItemContainer_e.getStyle().set("padding-left","5px");
        displayItemsContainerLayout.add(displayItemContainer_e);
        Icon shortestRunningDurationIcon = VaadinIcon.STOPWATCH.create();
        shortestRunningDurationIcon.getStyle().set("color", "var(--lumo-success-color)");
        shortestRunningDurationDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_e, shortestRunningDurationIcon,"成功运行最短时长:","-");

        HorizontalLayout spaceDivLayoutd1 = new HorizontalLayout();
        spaceDivLayoutd1.setWidth(5,Unit.PIXELS);
        displayItemContainer_e.add(spaceDivLayoutd1);
        Icon longestRunningDurationIcon = VaadinIcon.STOPWATCH.create();
        longestRunningDurationIcon.getStyle().set("color", "var(--lumo-primary-color)");
        longestRunningDurationDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer_e, longestRunningDurationIcon,"成功运行最长时长:","-");

        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create(),"分析功能特性运行记录");
        displayItemsContainerLayout.add(infoTitle);

        Comparator requestTimeComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((FeatureRunningInfo)o1).getRequestTime()!= null && ((FeatureRunningInfo)o2).getRequestTime()!= null &&
                        ((FeatureRunningInfo)o1).getRequestTime() instanceof LocalDateTime &&
                        ((FeatureRunningInfo)o2).getRequestTime() instanceof LocalDateTime){
                    if(((FeatureRunningInfo)o1).getRequestTime().isBefore(((FeatureRunningInfo)o2).getRequestTime())){
                        return -1;
                    }if(((FeatureRunningInfo)o1).getRequestTime().isAfter(((FeatureRunningInfo)o2).getRequestTime())){
                        return 1;
                    }
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        Comparator runningStartTimeComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((FeatureRunningInfo)o1).getRunningStartTime()!= null && ((FeatureRunningInfo)o2).getRunningStartTime()!= null &&
                        ((FeatureRunningInfo)o1).getRunningStartTime() instanceof LocalDateTime &&
                        ((FeatureRunningInfo)o2).getRunningStartTime() instanceof LocalDateTime){
                    if(((FeatureRunningInfo)o1).getRunningStartTime().isBefore(((FeatureRunningInfo)o2).getRunningStartTime())){
                        return -1;
                    }if(((FeatureRunningInfo)o1).getRunningStartTime().isAfter(((FeatureRunningInfo)o2).getRunningStartTime())){
                        return 1;
                    }
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        Comparator runningFinishTimeComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((FeatureRunningInfo)o1).getRunningFinishTime()!= null && ((FeatureRunningInfo)o2).getRunningFinishTime()!= null &&
                        ((FeatureRunningInfo)o1).getRunningFinishTime() instanceof LocalDateTime &&
                        ((FeatureRunningInfo)o2).getRunningFinishTime() instanceof LocalDateTime){
                    if(((FeatureRunningInfo)o1).getRunningFinishTime().isBefore(((FeatureRunningInfo)o2).getRunningFinishTime())){
                        return -1;
                    }if(((FeatureRunningInfo)o1).getRunningFinishTime().isAfter(((FeatureRunningInfo)o2).getRunningFinishTime())){
                        return 1;
                    }
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        ComponentRenderer _runningStatusComponentRenderer = new ComponentRenderer<>(featureRunningInfo -> {
            Icon statusIcon = null;
            if(featureRunningInfo instanceof FeatureRunningInfo){
                //"ACCEPT" "EXECUTING" "FINISHED"
                String featureRunningStatus = ((FeatureRunningInfo) featureRunningInfo).getFeatureRunningStatus();
                if("ACCEPT".equals(featureRunningStatus)){
                    statusIcon = LineAwesomeIconsSvg.PLUG_SOLID.create();
                    statusIcon.setSize("12px");
                }else if("EXECUTING".equals(featureRunningStatus)){
                    statusIcon = LineAwesomeIconsSvg.CLOCK_SOLID.create();
                    statusIcon.setSize("20px");
                    statusIcon.getStyle().set("color", "var(--lumo-error-color)");
                }else if("FINISHED".equals(featureRunningStatus)){
                    statusIcon = VaadinIcon.CHECK_CIRCLE_O.create();
                    statusIcon.setSize("11px");
                    statusIcon.getStyle().set("color", "var(--lumo-success-color)");
                }else{
                    statusIcon = VaadinIcon.QUESTION_CIRCLE_O.create();
                    statusIcon.setSize("12px");
                }
                statusIcon.setTooltipText(featureRunningStatus);
            }

            HorizontalLayout spacingLayout = new HorizontalLayout();
            spacingLayout.setWidth(8,Unit.PIXELS);
            spacingLayout.setPadding(false);
            spacingLayout.setSpacing(false);
            spacingLayout.setMargin(false);

            HorizontalLayout buttons = new HorizontalLayout(spacingLayout,statusIcon);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(20,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        ComponentRenderer _returnDataFormatComponentRenderer = new ComponentRenderer<>(featureRunningInfo -> {
            if(featureRunningInfo instanceof FeatureRunningInfo){
                NativeLabel returnDataFormatLabel = new NativeLabel(((FeatureRunningInfo) featureRunningInfo).getResponseDataForm());
                returnDataFormatLabel.getStyle().set("font-size","var(--lumo-font-size-xxs)");
                returnDataFormatLabel.getElement().getThemeList().add("badge contrast");
                return returnDataFormatLabel;
            }
            return null;
        });

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(featureRunningInfo -> {
            Icon queryIcon = LineAwesomeIconsSvg.POLL_H_SOLID.create();
            queryIcon.setSize("18px");
            Button listExecutionLogButton = new Button(queryIcon, event -> {
                if(featureRunningInfo instanceof FeatureRunningInfo){

                }
            });
            listExecutionLogButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            listExecutionLogButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            listExecutionLogButton.setTooltipText("显示分析运行日志");

            HorizontalLayout buttons = new HorizontalLayout(listExecutionLogButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(60,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        analysisFeatureRunningInfoGrid = new Grid<>();
        analysisFeatureRunningInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        analysisFeatureRunningInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        analysisFeatureRunningInfoGrid.addColumn(_runningStatusComponentRenderer).setHeader("状态").setKey("idx_1").setWidth("30px").setComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((FeatureRunningInfo)o1).getFeatureRunningStatus().compareTo(((FeatureRunningInfo)o2).getFeatureRunningStatus());
            }
        }).setResizable(false);
        analysisFeatureRunningInfoGrid.addColumn(_returnDataFormatComponentRenderer).setHeader("返回数据形式").setKey("idx_2").setWidth("80px").setComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((FeatureRunningInfo)o1).getResponseDataForm().compareTo(((FeatureRunningInfo)o2).getResponseDataForm());
            }
        }).setResizable(true);
        analysisFeatureRunningInfoGrid.addColumn(new LocalDateTimeRenderer<>(FeatureRunningInfo::getRequestTime,"yyyy-MM-dd HH:mm:ss")).setHeader("请求时间").setKey("idx_3").setResizable(true)
                .setTooltipGenerator(featureRunningInfo -> featureRunningInfo.getRequestTime() != null ? featureRunningInfo.getRequestTime().toString(): null).setComparator(requestTimeComparator);
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getRequestUUID).setHeader("请求 UID").setKey("idx_4").setResizable(true).setTooltipGenerator(item -> item.getRequestUUID()).setWidth("80px");
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getResponseUUID).setHeader("响应 UID").setKey("idx_5").setResizable(true).setTooltipGenerator(item -> item.getResponseUUID()).setWidth("80px");
        analysisFeatureRunningInfoGrid.addColumn(new LocalDateTimeRenderer<>(FeatureRunningInfo::getRunningStartTime,"yyyy-MM-dd HH:mm:ss")).setHeader("分析开始时间").setKey("idx_6").setResizable(true)
                .setTooltipGenerator(featureRunningInfo -> featureRunningInfo.getRunningStartTime() != null ? featureRunningInfo.getRunningStartTime().toString(): null).setComparator(runningStartTimeComparator);
        analysisFeatureRunningInfoGrid.addColumn(new LocalDateTimeRenderer<>(FeatureRunningInfo::getRunningFinishTime,"yyyy-MM-dd HH:mm:ss")).setHeader("分析结束时间").setKey("idx_7").setResizable(true)
                .setTooltipGenerator(featureRunningInfo -> featureRunningInfo.getRunningFinishTime() != null ? featureRunningInfo.getRunningFinishTime().toString(): null).setComparator(runningFinishTimeComparator);
        analysisFeatureRunningInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_8")
                .setFlexGrow(0).setWidth("50px").setResizable(false);

        LightGridColumnHeader gridColumnHeader2_idx1 = new LightGridColumnHeader(LineAwesomeIconsSvg.BOLT_SOLID.create(),"状态");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader2_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader2_idx2 = new LightGridColumnHeader(VaadinIcon.FORM,"返回数据形式");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader2_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader2_idx3 = new LightGridColumnHeader(VaadinIcon.TIMER,"请求时间");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader2_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader2_idx4 = new LightGridColumnHeader(VaadinIcon.KEY,"请求 UID");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader2_idx4).setSortable(true);
        LightGridColumnHeader gridColumnHeader2_idx5 = new LightGridColumnHeader(VaadinIcon.KEY_O,"响应 UID");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader2_idx5).setSortable(true);
        LightGridColumnHeader gridColumnHeader2_idx6 = new LightGridColumnHeader(VaadinIcon.FLIGHT_TAKEOFF,"分析开始时间");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_6").setHeader(gridColumnHeader2_idx6).setSortable(true);
        LightGridColumnHeader gridColumnHeader2_idx7 = new LightGridColumnHeader(VaadinIcon.FLIGHT_LANDING,"分析结束时间");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_7").setHeader(gridColumnHeader2_idx7).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx8 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        analysisFeatureRunningInfoGrid.getColumnByKey("idx_8").setHeader(gridColumnHeader_idx8);

        rightSideLayout.add(analysisFeatureRunningInfoGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            functionalFeatureInfoGrid.setHeight(event.getHeight()-360,Unit.PIXELS);
            analysisFeatureRunningInfoGrid.setHeight(event.getHeight()-590,Unit.PIXELS);
            functionalFeaturesInfoContainerLayout.setWidth(event.getWidth()-350,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            functionalFeatureInfoGrid.setHeight(browserHeight-360,Unit.PIXELS);
            analysisFeatureRunningInfoGrid.setHeight(browserHeight-590,Unit.PIXELS);
            functionalFeaturesInfoContainerLayout.setWidth(browserWidth-350,Unit.PIXELS);
        }));
        renderFunctionalFeatureInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderFunctionalFeatureInfo(){
        lastSelectedFunctionalFeatureInfo = null;
        refreshDataButton.setEnabled(false);
        totalRunTimesDisplayItem.updateDisplayValue("-");
        finishedRunTimeDisplayItem.updateDisplayValue("-");
        unfinishedRunTimeDisplayItem.updateDisplayValue("-");
        firstRunTimeDisplayItem.updateDisplayValue("-");
        lastRunTimeDisplayItem.updateDisplayValue("-");
        totalRunningDurationDisplayItem.updateDisplayValue("-");
        avgRunningDurationDisplayItem.updateDisplayValue("-");
        medianRunningDurationDisplayItem.updateDisplayValue("-");
        shortestRunningDurationDisplayItem.updateDisplayValue("-");
        longestRunningDurationDisplayItem.updateDisplayValue("-");
        analysisFeatureRunningInfoGrid.setItems(new ArrayList<>());
        analysisFeatureInfoActionBar.updateTitleContent("-");

        AnalysisProviderAdminClient analysisProviderAdminClient = new AnalysisProviderAdminClient(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
        UI currentUI = UI.getCurrent();
        AnalysisProviderAdminClient.ListFunctionalFeaturesCallback listFunctionalFeaturesCallback = new AnalysisProviderAdminClient.ListFunctionalFeaturesCallback() {
            @Override
            public void onExecutionSuccess(List<FunctionalFeatureInfo> functionalFeatureInfoList) {
                if(functionalFeatureInfoList != null){
                    currentUI.access(() -> {
                        analysisFeatureCountDisplayItem.updateDisplayValue(""+functionalFeatureInfoList.size());
                        functionalFeatureInfoDataView = functionalFeatureInfoGrid.setItems(functionalFeatureInfoList);
                        functionalFeatureInfoDataView.addFilter(item->{
                            String featureName = item.getFunctionalFeatureName();
                            String featureDesc = item.getFunctionalFeatureDescription();

                            boolean featureNameFilterResult = true;
                            if(!functionalFeatureNameFilterField.getValue().trim().equals("")){
                                if(featureName.toUpperCase().contains(functionalFeatureNameFilterField.getValue().trim().toUpperCase())){
                                    featureNameFilterResult = true;
                                }else{
                                    featureNameFilterResult = false;
                                }
                            }

                            boolean featureDescFilterResult = true;
                            if(!functionalFeatureDescFilterField.getValue().trim().equals("")){
                                if(featureDesc.toUpperCase().contains(functionalFeatureDescFilterField.getValue().trim().toUpperCase())){
                                    featureDescFilterResult = true;
                                }else{
                                    featureDescFilterResult = false;
                                }
                            }
                            return featureNameFilterResult & featureDescFilterResult;
                        });
                        queryFeatureRunningStatusInfo(analysisProviderAdminClient,currentUI);
                    });
                }
            }

            @Override
            public void onExecutionFail() {
                currentUI.access(() -> {
                    CommonUIOperationUtil.showPopupNotification("未获取到分析功能特性列表信息", NotificationVariant.LUMO_ERROR,-1, Notification.Position.BOTTOM_START);
                });
            }
        };
        analysisProviderAdminClient.listFunctionalFeatures(listFunctionalFeaturesCallback,3);
    }

    private void queryFeatureRunningStatusInfo(AnalysisProviderAdminClient analysisProviderAdminClient,UI currentUI){
        AnalysisProviderAdminClient.ListFeatureRunningStatusCallback listFeatureRunningStatusCallback = new AnalysisProviderAdminClient.ListFeatureRunningStatusCallback() {
            @Override
            public void onExecutionSuccess(List<FeatureRunningInfo> featureRunningInfo) {
                if(featureRunningInfo != null){
                    currentFeatureRunningInfoList = featureRunningInfo;
                }else{
                    currentUI.access(() -> {
                        CommonUIOperationUtil.showPopupNotification("未获取到分析功能特性运行状态信息", NotificationVariant.LUMO_ERROR,-1, Notification.Position.BOTTOM_START);
                    });
                }
            }

            @Override
            public void onExecutionFail() {
                currentUI.access(() -> {
                    CommonUIOperationUtil.showPopupNotification("未获取到分析功能特性运行状态信息", NotificationVariant.LUMO_ERROR,-1, Notification.Position.BOTTOM_START);
                });
            }
        };
        analysisProviderAdminClient.listFeatureRunningStatus(listFeatureRunningStatusCallback,3);
    }

    private void filterFunctionalFeatures(){
        String featureNameFilterValue = functionalFeatureNameFilterField.getValue().trim();
        String featureDescFilterValue = functionalFeatureDescFilterField.getValue().trim();
        if(featureNameFilterValue.equals("")&featureDescFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入分析功能特性名称 和/或 分析功能特性描述", NotificationVariant.LUMO_ERROR);
        }else{
            this.functionalFeatureInfoDataView.refreshAll();
        }
    }

    private void cancelFilterFunctionalFeatures(){
        functionalFeatureNameFilterField.setValue("");
        functionalFeatureDescFilterField.setValue("");
        this.functionalFeatureInfoDataView.refreshAll();
    }

    private void renderFunctionalFeatureRunningOverview(FunctionalFeatureInfo functionalFeatureInfo){
        analysisFeatureInfoActionBar.updateTitleContent(functionalFeatureInfo.getFunctionalFeatureName().trim()+
                " ("+ functionalFeatureInfo.getFunctionalFeatureDescription().trim()+")");
        refreshDataButton.setEnabled(true);
        List<FeatureRunningInfo> selectedFeatureRunningInfoList = new ArrayList<>();
        List<Duration> finishedRunningDurationList = new ArrayList<>();
        int finishedRunningTimes = 0;
        Map<LocalDateTime,Long> requestAndDurationMapping = new HashMap<>();
        if(currentFeatureRunningInfoList!= null){
            for(FeatureRunningInfo featureRunningInfo : currentFeatureRunningInfoList) {
                if(featureRunningInfo.getFeatureName().trim().equals(functionalFeatureInfo.getFunctionalFeatureName().trim())){
                    selectedFeatureRunningInfoList.add(featureRunningInfo);
                    if("FINISHED".equals(featureRunningInfo.getFeatureRunningStatus())) {
                        LocalDateTime startDateTime = featureRunningInfo.getRunningStartTime();
                        LocalDateTime finishDateTime = featureRunningInfo.getRunningFinishTime();
                        Duration duration = Duration.between(startDateTime, finishDateTime);
                        finishedRunningDurationList.add(duration);
                        finishedRunningTimes++;
                        requestAndDurationMapping.put(featureRunningInfo.getRequestTime(),duration.toSeconds());
                    }
                }
            }
        }
        selectedFeatureRunningInfoList.sort(new Comparator<FeatureRunningInfo>() {
            @Override
            public int compare(FeatureRunningInfo o1, FeatureRunningInfo o2) {
                return 0 - o1.getRequestTime().compareTo(o2.getRequestTime());
            }
        });
        analysisFeatureRunningInfoGrid.setItems(selectedFeatureRunningInfoList);

        if(selectedFeatureRunningInfoList.size() > 0){
            totalRunTimesDisplayItem.updateDisplayValue(numberFormat.format(selectedFeatureRunningInfoList.size()));
            finishedRunTimeDisplayItem.updateDisplayValue(numberFormat.format(finishedRunningTimes));
            unfinishedRunTimeDisplayItem.updateDisplayValue(numberFormat.format(selectedFeatureRunningInfoList.size()-finishedRunningTimes));
            firstRunTimeDisplayItem.updateDisplayValue(selectedFeatureRunningInfoList.get(0).getRunningStartTime().format(localDateTimeFormatter));
            lastRunTimeDisplayItem.updateDisplayValue(selectedFeatureRunningInfoList.get(selectedFeatureRunningInfoList.size()-1).getRunningStartTime().format(localDateTimeFormatter));
        }else{
            totalRunTimesDisplayItem.updateDisplayValue("0");
            finishedRunTimeDisplayItem.updateDisplayValue("0");
            unfinishedRunTimeDisplayItem.updateDisplayValue("0");
            firstRunTimeDisplayItem.updateDisplayValue("-");
            lastRunTimeDisplayItem.updateDisplayValue("-");
        }

        if(finishedRunningDurationList.size() > 0){
            Duration totalDuration = finishedRunningDurationList.stream().reduce(Duration.ZERO, Duration::plus);
            totalRunningDurationDisplayItem.updateDisplayValue(formatDuration(totalDuration));

            long[] secondNumbers = new long[finishedRunningDurationList.size()];
            long totalTimeInSeconds = totalDuration.getSeconds();
            long shortestTimeInSeconds = finishedRunningDurationList.get(0).getSeconds();
            long longestTimeInSeconds = finishedRunningDurationList.get(0).getSeconds();

            int i = 0;
            for(Duration currentDuration:finishedRunningDurationList){
                secondNumbers[i] = currentDuration.getSeconds();
                i++;
                if(currentDuration.getSeconds() < shortestTimeInSeconds){
                    shortestTimeInSeconds = currentDuration.getSeconds();
                }
                if(currentDuration.getSeconds()> longestTimeInSeconds){
                    longestTimeInSeconds = currentDuration.getSeconds();
                }
            }
            long averageTimeInSeconds = totalTimeInSeconds/finishedRunningDurationList.size();
            Duration avgDuration = Duration.ofSeconds(averageTimeInSeconds);
            avgRunningDurationDisplayItem.updateDisplayValue(formatDuration(avgDuration));

            long medianTimeInSeconds = calculateMedian(secondNumbers);
            Duration medianDuration = Duration.ofSeconds(medianTimeInSeconds);
            medianRunningDurationDisplayItem.updateDisplayValue(formatDuration(medianDuration));

            Duration shortestDuration = Duration.ofSeconds(shortestTimeInSeconds);
            shortestRunningDurationDisplayItem.updateDisplayValue(formatDuration(shortestDuration));

            Duration longestDuration = Duration.ofSeconds(longestTimeInSeconds);
            longestRunningDurationDisplayItem.updateDisplayValue(formatDuration(longestDuration));
        }else{
            totalRunningDurationDisplayItem.updateDisplayValue("-");
            avgRunningDurationDisplayItem.updateDisplayValue("-");
            medianRunningDurationDisplayItem.updateDisplayValue("-");
            shortestRunningDurationDisplayItem.updateDisplayValue("-");
            longestRunningDurationDisplayItem.updateDisplayValue("-");
        }

        featureRunningInfoTimeSequenceBarChart.setDate(requestAndDurationMapping);
    }

    private String formatDuration(Duration duration) {
        // 获取总秒数
        long totalSeconds = duration.getSeconds();
        // 计算小时数
        long hours = totalSeconds / 3600;
        // 计算剩余的秒数
        long remainingSeconds = totalSeconds % 3600;
        // 计算分钟数
        long minutes = remainingSeconds / 60;
        // 计算剩余的秒数
        long seconds = remainingSeconds % 60;
        // 格式化输出
        String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return formattedDuration;
    }

    public long calculateMedian(long[] numbers) {
        // 对数组进行排序
        Arrays.sort(numbers);

        int middle = numbers.length / 2;
        if (numbers.length % 2 == 1) {
            // 如果数组长度是奇数，中位数是中间的数字
            return numbers[middle];
        } else {
            // 如果数组长度是偶数，中位数是中间两个数字的平均值
            return (long) ((numbers[middle - 1] + numbers[middle]) / 2.0);
        }
    }

    private void refreshFunctionalFeatureRunningStatus(){
        if(lastSelectedFunctionalFeatureInfo!= null){
            AnalysisProviderAdminClient analysisProviderAdminClient = new AnalysisProviderAdminClient(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
            List<FeatureRunningInfo> currentRealtimeFeatureRunningInfoList = analysisProviderAdminClient.listFeatureRunningStatus();
            if(currentRealtimeFeatureRunningInfoList != null){
                currentFeatureRunningInfoList = currentRealtimeFeatureRunningInfoList;
                UI currentUI = UI.getCurrent();
                currentUI.access(() -> {
                    renderFunctionalFeatureRunningOverview(lastSelectedFunctionalFeatureInfo);
                });
            }
        }
    }
}
