package com.viewfunction.docg.views.dataAnalysis.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.analysisProvider.client.AnalysisProviderAdminClient;
import com.viewfunction.docg.analysisProvider.service.analysisProviderServiceCore.payload.FunctionalFeatureInfo;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AnalysisProviderRefreshEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import java.util.ArrayList;
import java.util.List;

public class DataAnalysisCapacityManagementUI extends VerticalLayout implements
        AnalysisProviderRefreshEvent.AnalysisProviderRefreshEventListener {

    private Registration listener;
    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout rightSideContentContainerLayout;
    private final String ANALYSIS_CLIENT_HOST_NAME =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_NAME);
    private final int ANALYSIS_CLIENT_HOST_PORT = Integer.parseInt(SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ANALYSIS_CLIENT_HOST_PORT));

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
        leftSideContentContainerLayout.setMaxWidth(300, Unit.PIXELS);
        leftSideContentContainerLayout.setMinWidth(300, Unit.PIXELS);
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
        actionComponentsList.add(refreshProviderRuntimeInfoButton);

        Icon providerRuntimeStatusIcon = new Icon(VaadinIcon.SPARK_LINE);

        SecondaryTitleActionBar providerRuntimeStatusActionBar = new SecondaryTitleActionBar(providerRuntimeStatusIcon,"服务运行信息",null,actionComponentsList);
        leftSideContentContainerLayout.add(providerRuntimeStatusActionBar);

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
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"分析功能特性定义:",dataAnalysisProviderManagementOperationButtonList);
        rightSideContentContainerLayout.add(sectionActionBar);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-170,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-170,Unit.PIXELS);
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
        AnalysisProviderAdminClient.PingAnalysisProviderCallback pingAnalysisProviderCallback = new AnalysisProviderAdminClient.PingAnalysisProviderCallback() {
            @Override
            public void onPingSuccess() {
                renderAnalysisProviderInfo();
            }

            @Override
            public void onPingFail() {
                currentUI.access(() -> {
                    CommonUIOperationUtil.showPopupNotification("未检测到运行中的数据分析服务", NotificationVariant.LUMO_ERROR,-1, Notification.Position.MIDDLE);
                });
            }
        };
        analysisProviderAdminClient.pingAnalysisProvider(pingAnalysisProviderCallback,3);
    }

    private void renderAnalysisProviderInfo(){
        System.out.println("renderAnalysisProviderInfo");

    }
}
