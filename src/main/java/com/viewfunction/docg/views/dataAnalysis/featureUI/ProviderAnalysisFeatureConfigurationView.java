package com.viewfunction.docg.views.dataAnalysis.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.analysisProvider.client.AnalysisProviderAdminClient;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.FeatureRunningInfo;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.FunctionalFeatureInfo;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProviderAnalysisFeatureConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem analysisFeatureCountDisplayItem;
    private SecondaryTitleActionBar analysisFeatureInfoActionBar;
    private Registration listener;
    private Grid<FunctionalFeatureInfo> functionalFeatureInfoGrid;
    private GridListDataView<FunctionalFeatureInfo> functionalFeatureInfoDataView;
    private SecondaryKeyValueDisplayItem groupNameDisplayItem;
    private SecondaryKeyValueDisplayItem primaryDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem backupDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem sliceAtomicityDisplayItem;
    private SecondaryKeyValueDisplayItem backupNumberDisplayItem;
    private SecondaryKeyValueDisplayItem sliceStorageModeDisplayItem;
    private TextField functionalFeatureNameFilterField;
    private TextField functionalFeatureDescFilterField;
    private Grid<FeatureRunningInfo> analysisFeatureRunningInfoGrid;
    private final int ANALYSIS_CLIENT_HOST_PORT = Integer.parseInt(SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_PORT))+2;
    private final String ANALYSIS_CLIENT_HOST_NAME =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_NAME);
    private List<FeatureRunningInfo> currentFeatureRunningInfoList;
    private FunctionalFeatureInfo lastSelectedFunctionalFeatureInfo;
    private HorizontalLayout functionalFeaturesInfoContainerLayout;

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

        Button registerFeatureButton= new Button("注册分析功能特性");
        registerFeatureButton.setIcon(LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create());
        registerFeatureButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(registerFeatureButton);
        registerFeatureButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateDataSliceView();
            }
        });

        List<Component> dataAnalysisProviderManagementOperationButtonList = new ArrayList<>();

        Button registerAnalysisFunctionalFeatureButton = new Button("注册分析功能特性", LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create());
        registerAnalysisFunctionalFeatureButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        registerAnalysisFunctionalFeatureButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        dataAnalysisProviderManagementOperationButtonList.add(registerAnalysisFunctionalFeatureButton);
        registerAnalysisFunctionalFeatureButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateConceptionKindUI();
            }
        });

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

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setMargin(false);
        functionalFeaturesInfoContainerLayout.add(rightSideLayout);
        rightSideLayout.getStyle().set("left","0px").set("top","-2px").set("position","relative");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"分析功能特性概览");
        rightSideLayout.add(filterTitle2);

        analysisFeatureInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,null);
        analysisFeatureInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideLayout.add(analysisFeatureInfoActionBar);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.DASHBOARD),"分析功能特性指标");
        rightSideLayout.add(infoTitle2);

        HorizontalLayout displayItemContainer4 = new HorizontalLayout();
        displayItemContainer4.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer4);
        groupNameDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer4, VaadinIcon.ARCHIVES.create(),"切片组名称:","-");

        HorizontalLayout displayItemContainer1 = new HorizontalLayout();
        displayItemContainer1.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer1);
        primaryDataCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer1, VaadinIcon.STOCK.create(),"主数据量:","-");

        HorizontalLayout spaceDivLayout01 = new HorizontalLayout();
        spaceDivLayout01.setWidth(5,Unit.PIXELS);
        displayItemContainer1.add(spaceDivLayout01);
        backupDataCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer1, VaadinIcon.STOCK.create(),"备份数据量:","-");

        HorizontalLayout displayItemContainer3 = new HorizontalLayout();
        displayItemContainer3.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer3);
        totalDataCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer3, VaadinIcon.STOCK.create(),"总数据量:","-");

        HorizontalLayout spaceDivLayout02 = new HorizontalLayout();
        spaceDivLayout02.setWidth(5,Unit.PIXELS);
        displayItemContainer3.add(spaceDivLayout02);
        backupNumberDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer3, VaadinIcon.FLIP_H.create(),"切片备份数:","-");

        HorizontalLayout displayItemContainer5 = new HorizontalLayout();
        displayItemContainer5.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer5);
        sliceAtomicityDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer5, VaadinIcon.RHOMBUS.create(),"原子类型模式:","-");

        HorizontalLayout spaceDivLayout03 = new HorizontalLayout();
        spaceDivLayout03.setWidth(5,Unit.PIXELS);
        displayItemContainer5.add(spaceDivLayout03);

        sliceStorageModeDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer5, VaadinIcon.SERVER.create(),"数据存储类型:","-");

        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"数据切片属性定义");
        rightSideLayout.add(infoTitle);

        analysisFeatureRunningInfoGrid = new Grid<>();
        analysisFeatureRunningInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        analysisFeatureRunningInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);

        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getFeatureRunningStatus).setHeader("执行状态").setKey("idx_1").setResizable(true).setWidth("60px");
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getResponseDataForm).setHeader("返回数据形式").setKey("idx_2").setResizable(true).setWidth("100px");
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getRequestTime).setHeader("请求时间").setKey("idx_3").setResizable(true).setTooltipGenerator(item -> "请求时间: "+item.getRequestTime());
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getRequestUUID).setHeader("请求 UID").setKey("idx_4").setResizable(true).setTooltipGenerator(item -> "请求 UID: "+item.getRequestUUID());
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getResponseUUID).setHeader("响应 UID").setKey("idx_5").setResizable(true).setTooltipGenerator(item -> "响应 UID: "+item.getResponseUUID());
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getRunningStartTime).setHeader("分析开始时间").setKey("idx_6").setResizable(true).setTooltipGenerator(item -> "分析开始时间: "+item.getRunningStartTime());
        analysisFeatureRunningInfoGrid.addColumn(FeatureRunningInfo::getRunningFinishTime).setHeader("分析结束时间").setKey("idx_7").setResizable(true).setTooltipGenerator(item -> "分析结束时间: "+item.getRunningFinishTime());

        LightGridColumnHeader gridColumnHeader2_idx1 = new LightGridColumnHeader(LineAwesomeIconsSvg.BOLT_SOLID.create(),"执行状态");
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

        rightSideLayout.add(analysisFeatureRunningInfoGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            functionalFeatureInfoGrid.setHeight(event.getHeight()-360,Unit.PIXELS);
            analysisFeatureRunningInfoGrid.setHeight(event.getHeight()-570,Unit.PIXELS);
            functionalFeaturesInfoContainerLayout.setWidth(event.getWidth()-350,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            functionalFeatureInfoGrid.setHeight(browserHeight-360,Unit.PIXELS);
            analysisFeatureRunningInfoGrid.setHeight(browserHeight-570,Unit.PIXELS);
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
        AnalysisProviderAdminClient analysisProviderAdminClient = new AnalysisProviderAdminClient(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
        UI currentUI = UI.getCurrent();
        //dataSlicePropertyDefinitionsGrid.setItems(new ArrayList<>());
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

        List<FeatureRunningInfo> selectedFeatureRunningInfoList = new ArrayList<>();
        if(currentFeatureRunningInfoList!= null){
            for(FeatureRunningInfo featureRunningInfo : currentFeatureRunningInfoList) {
                if(featureRunningInfo.getFeatureName().trim().equals(functionalFeatureInfo.getFunctionalFeatureName().trim())){
                    selectedFeatureRunningInfoList.add(featureRunningInfo);
                }
            }
        }
        analysisFeatureRunningInfoGrid.setItems(selectedFeatureRunningInfoList);
    }
}
