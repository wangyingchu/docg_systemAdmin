package com.viewfunction.docg.views.dataAnalysis.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

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
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.analysisProvider.client.AnalysisProviderAdminClient;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.ProviderRunningInfo;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AnalysisProviderRefreshEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataAnalysisCapacityManagementUI extends VerticalLayout implements
        AnalysisProviderRefreshEvent.AnalysisProviderRefreshEventListener {

    private Registration listener;
    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout rightSideContentContainerLayout;
    private final String ANALYSIS_CLIENT_HOST_NAME =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_NAME);
    private final int ANALYSIS_CLIENT_HOST_PORT = Integer.parseInt(SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_PORT));
    private Grid<ProviderRunningInfo> providerRunningInfoGrid;
    private HorizontalLayout runningStatusLayout;
    private HorizontalLayout notRunningStatusLayout;
    private ProviderAnalysisFeatureConfigurationView providerAnalysisFeatureConfigurationView;

    public DataAnalysisCapacityManagementUI() {
        Button refreshDataButton = new Button("刷新分析服务统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            AnalysisProviderRefreshEvent analysisProviderRefreshEvent = new AnalysisProviderRefreshEvent();
            ResourceHolder.getApplicationBlackboard().fire(analysisProviderRefreshEvent);
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();
        NativeLabel dataAnalysisProviderTechLabel = new NativeLabel(" Apache Spark 实现");
        dataAnalysisProviderTechLabel.getStyle().set("font-size","var(--lumo-font-size-xxs)");
        secTitleElementsList.add(dataAnalysisProviderTechLabel);
        dataAnalysisProviderTechLabel.getElement().getThemeList().add("badge success");
        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Analysis Provider 分析服务管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setSpacing(false);
        leftSideContentContainerLayout.setMaxWidth(295, Unit.PIXELS);
        leftSideContentContainerLayout.setMinWidth(295, Unit.PIXELS);
        leftSideContentContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");
        contentContainerLayout.add(leftSideContentContainerLayout);

        rightSideContentContainerLayout = new VerticalLayout();
        rightSideContentContainerLayout.setSpacing(false);
        rightSideContentContainerLayout.setPadding(false);
        rightSideContentContainerLayout.setMargin(false);
        contentContainerLayout.add(rightSideContentContainerLayout);

        List<Component> actionComponentsList = new ArrayList<>();
        Button refreshProviderRuntimeInfoButton = new Button("获取服务运行信息",new Icon(VaadinIcon.REFRESH));
        refreshProviderRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshProviderRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshProviderRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        refreshProviderRuntimeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            //checkAnalysisProviderStatusInfo();
        });
        refreshProviderRuntimeInfoButton.setVisible(false);
        actionComponentsList.add(refreshProviderRuntimeInfoButton);

        Icon providerRuntimeStatusIcon = new Icon(VaadinIcon.SPARK_LINE);

        SecondaryTitleActionBar providerRuntimeStatusActionBar = new SecondaryTitleActionBar(providerRuntimeStatusIcon,"服务运行信息",null,actionComponentsList);
        leftSideContentContainerLayout.add(providerRuntimeStatusActionBar);

        VerticalLayout providerRuntimeStatusInfoLayout = new VerticalLayout();
        leftSideContentContainerLayout.add(providerRuntimeStatusInfoLayout);

        SecondaryIconTitle sectionTitle1 = new SecondaryIconTitle(FontAwesome.Solid.HEART_PULSE.create(),"分析服务运行状态");
        providerRuntimeStatusInfoLayout.add(sectionTitle1);

        runningStatusLayout = new HorizontalLayout();
        providerRuntimeStatusInfoLayout.add(runningStatusLayout);
        runningStatusLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        runningStatusLayout.getStyle().set("margin-left","var(--lumo-space-m)").set("margin-bottom","var(--lumo-space-m)");

        Icon runningStatusIcon = LineAwesomeIconsSvg.FIGHTER_JET_SOLID.create();
        runningStatusIcon.setSize("32px");
        runningStatusIcon.getStyle().set("color", "var(--lumo-success-color)");
        runningStatusLayout.add(runningStatusIcon);

        NativeLabel runningTextLabel = new NativeLabel("运行中");
        runningTextLabel.addClassNames("text-xs","font-semibold","text-secondary");
        runningTextLabel.getStyle().set("color", "var(--lumo-success-color)");
        runningStatusLayout.add(runningTextLabel);
        runningStatusLayout.setVisible(false);

        notRunningStatusLayout = new HorizontalLayout();
        providerRuntimeStatusInfoLayout.add(notRunningStatusLayout);
        notRunningStatusLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        notRunningStatusLayout.getStyle().set("margin-left","var(--lumo-space-m)").set("margin-bottom","var(--lumo-space-m)");

        Icon notRunningStatusIcon = LineAwesomeIconsSvg.FIGHTER_JET_SOLID.create();
        notRunningStatusIcon.setSize("32px");
        notRunningStatusLayout.add(notRunningStatusIcon);

        NativeLabel notRunningTextLabel = new NativeLabel("未运行");
        notRunningTextLabel.addClassNames("text-xs","font-semibold","text-secondary");
        notRunningStatusLayout.add(notRunningTextLabel);

        SecondaryIconTitle sectionTitle2 = new SecondaryIconTitle(LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create(), "服务运行历史记录");
        providerRuntimeStatusInfoLayout.add(sectionTitle2);

        providerRunningInfoGrid = new Grid<>();
        providerRunningInfoGrid.setWidth(265,Unit.PIXELS);
        providerRunningInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        providerRunningInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        providerRunningInfoGrid.addColumn(new LocalDateTimeRenderer<>(ProviderRunningInfo::getProviderStartTime,"yyyy-MM-dd HH:mm:ss")).setHeader("开始时间").setKey("idx_0").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> runtimeRelationAndConceptionKindAttachInfo.getProviderStartTime() != null ? runtimeRelationAndConceptionKindAttachInfo.getProviderStartTime().toString(): null);
        providerRunningInfoGrid.addColumn(new LocalDateTimeRenderer<>(ProviderRunningInfo::getProviderStopTime,"yyyy-MM-dd HH:mm:ss")).setHeader("结束时间").setKey("idx_1").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> runtimeRelationAndConceptionKindAttachInfo.getProviderStopTime() != null ? runtimeRelationAndConceptionKindAttachInfo.getProviderStopTime().toString(): null);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.FLIGHT_TAKEOFF,"开始时间");
        providerRunningInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(false);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.FLIGHT_LANDING,"结束时间");
        providerRunningInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(false);

        providerRuntimeStatusInfoLayout.add(providerRunningInfoGrid);

        TabSheet providerConfigurationTabSheet = new TabSheet();
        providerConfigurationTabSheet.setWidthFull();
        rightSideContentContainerLayout.add(providerConfigurationTabSheet);
        rightSideContentContainerLayout.setFlexGrow(1,providerConfigurationTabSheet);

        providerAnalysisFeatureConfigurationView = new ProviderAnalysisFeatureConfigurationView();
        providerConfigurationTabSheet.add(generateConfigurationTabTitle(LineAwesomeIconsSvg.BONG_SOLID.create(),"分析功能特性配置"), providerAnalysisFeatureConfigurationView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-170,Unit.PIXELS);
            this.providerRunningInfoGrid.setHeight(event.getHeight()-320,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-170,Unit.PIXELS);
            this.providerRunningInfoGrid.setHeight(browserHeight-320,Unit.PIXELS);
        }));
        refreshAnalysisProviderStatus();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedAnalysisProviderRefreshEvent(AnalysisProviderRefreshEvent event) {
        refreshAnalysisProviderStatus();
    }

    private void refreshAnalysisProviderStatus(){
        UI currentUI = UI.getCurrent();
        AnalysisProviderAdminClient analysisProviderAdminClient = new AnalysisProviderAdminClient(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
        List<ProviderRunningInfo> providerRunningInfoList =analysisProviderAdminClient.listProviderRunningStatus();
        if(providerRunningInfoList != null){
            providerRunningInfoList.sort(new Comparator<ProviderRunningInfo>() {
                @Override
                public int compare(ProviderRunningInfo o1, ProviderRunningInfo o2) {
                    return 0 - o1.getProviderStartTime().compareTo(o2.getProviderStartTime());
                }
            });
            runningStatusLayout.setVisible(true);
            notRunningStatusLayout.setVisible(false);
            providerRunningInfoGrid.setItems(providerRunningInfoList);

        }else{
            runningStatusLayout.setVisible(false);
            notRunningStatusLayout.setVisible(true);
            CommonUIOperationUtil.showPopupNotification("未检测到运行中的数据分析服务", NotificationVariant.LUMO_ERROR,-1, Notification.Position.MIDDLE);
        }
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
}
