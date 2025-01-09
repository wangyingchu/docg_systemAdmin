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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.analysisProvider.client.AnalysisProviderAdminClient;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.FunctionalFeatureInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ProviderAnalysisFeatureConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem gridDataSlicesCountDisplayItem;
    private SecondaryTitleActionBar dataSliceInfoActionBar;
    private Registration listener;
    private Grid<FunctionalFeatureInfo> dataSliceMetaInfoGrid;
    private GridListDataView<DataSliceMetaInfo> dataSliceMetaInfoView;
    private DataSliceMetaInfo lastSelectedDataSliceMetaInfo;
    private SecondaryKeyValueDisplayItem groupNameDisplayItem;
    private SecondaryKeyValueDisplayItem primaryDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem backupDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem sliceAtomicityDisplayItem;
    private SecondaryKeyValueDisplayItem backupNumberDisplayItem;
    private SecondaryKeyValueDisplayItem sliceStorageModeDisplayItem;
    private TextField dataSliceNameFilterField;
    private TextField dataSliceGroupFilterField;
    private int currentDataSliceCount;
    //private Grid<FunctionalFeatureInfo> dataSlicePropertyDefinitionsGrid;
    private final int ANALYSIS_CLIENT_HOST_PORT = Integer.parseInt(SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_PORT))+2;
    private final String ANALYSIS_CLIENT_HOST_NAME =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_NAME);
    public ProviderAnalysisFeatureConfigurationView() {

        SecondaryIconTitle sectionTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.CLONE.create(),"数据分析功能特性配置");
        add(sectionTitle);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();

        this.gridDataSlicesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create()," 分析功能特性数量:","-");

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30, Unit.PIXELS);
        infoContainer.add(horSpaceDiv);

        Button addGridDataSliceButton= new Button("注册分析功能特性");
        addGridDataSliceButton.setIcon(LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create());
        addGridDataSliceButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addGridDataSliceButton);
        addGridDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
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

        HorizontalLayout dataSlicesInfoContainerLayout = new HorizontalLayout();
        dataSlicesInfoContainerLayout.setPadding(false);
        dataSlicesInfoContainerLayout.setMargin(false);
        add(dataSlicesInfoContainerLayout);

        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setPadding(false);
        leftSideLayout.setMargin(false);
        dataSlicesInfoContainerLayout.add(leftSideLayout);

        HorizontalLayout dataSlicesSearchElementsContainerLayout = new HorizontalLayout();
        dataSlicesSearchElementsContainerLayout.setSpacing(false);
        dataSlicesSearchElementsContainerLayout.setMargin(false);
        leftSideLayout.add(dataSlicesSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        dataSlicesSearchElementsContainerLayout.add(filterTitle);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        dataSliceNameFilterField = new TextField();
        dataSliceNameFilterField.setPlaceholder("分析功能特性名称");
        dataSliceNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dataSliceNameFilterField.setWidth(150,Unit.PIXELS);
        dataSlicesSearchElementsContainerLayout.add(dataSliceNameFilterField);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,dataSliceNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        dataSlicesSearchElementsContainerLayout.add(plusIcon);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        dataSliceGroupFilterField = new TextField();
        dataSliceGroupFilterField.setPlaceholder("分析功能特性描述");
        dataSliceGroupFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dataSliceGroupFilterField.setWidth(150,Unit.PIXELS);
        dataSlicesSearchElementsContainerLayout.add(dataSliceGroupFilterField);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,dataSliceGroupFilterField);

        Button searchDataSlicesButton = new Button("查找分析功能特性",new Icon(VaadinIcon.SEARCH));
        searchDataSlicesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchDataSlicesButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        dataSlicesSearchElementsContainerLayout.add(searchDataSlicesButton);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchDataSlicesButton);
        searchDataSlicesButton.setWidth(130,Unit.PIXELS);
        searchDataSlicesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterDataSlices();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        dataSlicesSearchElementsContainerLayout.add(divIcon);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        dataSlicesSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelFilterDataSlices();
            }
        });

        dataSliceMetaInfoGrid = new Grid<>();
        dataSliceMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        dataSliceMetaInfoGrid.addColumn(FunctionalFeatureInfo::getFunctionalFeatureName).setHeader("分析功能特性名称").setKey("idx_0").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<FunctionalFeatureInfo>() {
            @Override
            public String apply(FunctionalFeatureInfo functionalFeatureInfo) {
                return functionalFeatureInfo.getFunctionalFeatureName();
            }
        });
        dataSliceMetaInfoGrid.addColumn(FunctionalFeatureInfo::getFunctionalFeatureDescription).setHeader("分析功能特性描述").setKey("idx_1").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<FunctionalFeatureInfo>() {
            @Override
            public String apply(FunctionalFeatureInfo functionalFeatureInfo) {
                return functionalFeatureInfo.getFunctionalFeatureDescription();
            }
        });
        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分析功能特性名称");
        dataSliceMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"分析功能特性描述");
        dataSliceMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);

        leftSideLayout.add(dataSliceMetaInfoGrid);

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setMargin(false);
        rightSideLayout.setWidth(590,Unit.PIXELS);
        dataSlicesInfoContainerLayout.add(rightSideLayout);
        rightSideLayout.getStyle().set("left","0px").set("top","-2px").set("position","relative");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"分析功能特性概览");
        rightSideLayout.add(filterTitle2);

        dataSliceInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,null);
        dataSliceInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideLayout.add(dataSliceInfoActionBar);

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

/*
        dataSlicePropertyDefinitionsGrid = new Grid<>();
        dataSlicePropertyDefinitionsGrid.setWidth(100,Unit.PERCENTAGE);
        dataSlicePropertyDefinitionsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        dataSlicePropertyDefinitionsGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        dataSlicePropertyDefinitionsGrid.addColumn(FunctionalFeatureInfo::getFunctionalFeatureName).setHeader("属性名称").setKey("idx_0");
       dataSlicePropertyDefinitionsGrid.addColumn(FunctionalFeatureInfo::getFunctionalFeatureDescription).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("100px").setResizable(false);
        //dataSlicePropertyDefinitionsGrid.addColumn(ComputeGridDataSliceConfigurationView.DataSlicePropertyDefinitionVO::isPrimaryKey).setHeader("切片主键").setKey("idx_2").setFlexGrow(0).setWidth("80px").setResizable(false);
*/
        /*
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        dataSlicePropertyDefinitionsGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        dataSlicePropertyDefinitionsGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.KEY_SOLID.create(),"切片主键");
        dataSlicePropertyDefinitionsGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        rightSideLayout.add(dataSlicePropertyDefinitionsGrid);
        */

        VerticalLayout spaceHolderLayout = new VerticalLayout();
        rightSideLayout.add(spaceHolderLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            dataSliceMetaInfoGrid.setHeight(event.getHeight()-385,Unit.PIXELS);
            //dataSlicePropertyDefinitionsGrid.setHeight(event.getHeight()-650,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            dataSliceMetaInfoGrid.setHeight(browserHeight-385,Unit.PIXELS);
            //dataSlicePropertyDefinitionsGrid.setHeight(browserHeight-650,Unit.PIXELS);
        }));
        //renderGridDateSlicesInfo();


        AnalysisProviderAdminClient analysisProviderAdminClient = new AnalysisProviderAdminClient(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
        List<FunctionalFeatureInfo> functionalFeatureInfoList = analysisProviderAdminClient.listFunctionalFeatures();

        if(functionalFeatureInfoList != null){
            dataSliceMetaInfoGrid.setItems(functionalFeatureInfoList);
            this.gridDataSlicesCountDisplayItem.updateDisplayValue(""+functionalFeatureInfoList.size());
        }

        /*
        AnalysisProviderAdminClient.ListProviderRunningStatusCallback listProviderRunningStatusCallback = new AnalysisProviderAdminClient.ListProviderRunningStatusCallback() {
            @Override
            public void onExecutionSuccess(List<ProviderRunningInfo> providerRunningInfoList) {
                if(providerRunningInfoList != null){
                    providerRunningInfoList.sort(new Comparator<ProviderRunningInfo>() {
                        @Override
                        public int compare(ProviderRunningInfo o1, ProviderRunningInfo o2) {
                            return 0 - o1.getProviderStartTime().compareTo(o2.getProviderStartTime());
                        }
                    });

                    currentUI.access(() -> {
                        runningStatusLayout.setVisible(true);
                        notRunningStatusLayout.setVisible(false);
                        providerRunningInfoGrid.setItems(providerRunningInfoList);
                    });
                }else{
                    currentUI.access(() -> {
                        runningStatusLayout.setVisible(false);
                        notRunningStatusLayout.setVisible(true);
                        CommonUIOperationUtil.showPopupNotification("未检测到运行中的数据分析服务", NotificationVariant.LUMO_ERROR,-1, Notification.Position.MIDDLE);
                    });
                }
            }

            @Override
            public void onExecutionFail() {
                currentUI.access(() -> {
                    runningStatusLayout.setVisible(false);
                    notRunningStatusLayout.setVisible(true);
                    CommonUIOperationUtil.showPopupNotification("未检测到运行中的数据分析服务", NotificationVariant.LUMO_ERROR,-1, Notification.Position.MIDDLE);
                });
            }
        };
        analysisProviderAdminClient.listProviderRunningStatus(listProviderRunningStatusCallback,5);
        */


    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }





}
