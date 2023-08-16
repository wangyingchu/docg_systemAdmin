package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.AttributeKindAttachedToAttributesViewKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributeKindDetachedFromAttributesViewKindEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

import java.util.ArrayList;
import java.util.List;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.AttributesViewKind;

@Route("attributesViewKindDetailInfo/:attributesViewKindUID")
public class AttributesViewKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver,
        AttributeKindAttachedToAttributesViewKindEvent.AttributeKindAttachedToAttributesViewKindListener,
        AttributeKindDetachedFromAttributesViewKindEvent.AttributeKindDetachedFromAttributesViewKindListener{
    private String attributesViewKindUID;
    private int attributesViewKindDetailViewHeightOffset = 40;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private ContainerConceptionKindsConfigView containerConceptionKindsConfigView;
    private ContainsAttributeKindsConfigView containsAttributeKindsConfigView;
    private AttributesViewKindRuntimeConfigurationView attributesViewKindRuntimeConfigurationView;
    private TabSheet kindConfigurationTabSheet;
    private AttributesViewKindCorrelationInfoChart attributesViewKindCorrelationInfoChart;
    private AttributesViewKindCorrelationInfoChartInitThread attributesViewKindCorrelationInfoChartInitThread;
    private int attributesViewKindCorrelationInfoChartHeight = 500;

    public AttributesViewKindDetailUI(){}

    public AttributesViewKindDetailUI(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributesViewKindUID = beforeEnterEvent.getRouteParameters().get("attributesViewKindUID").get();
        this.attributesViewKindDetailViewHeightOffset = -10;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        renderAttributesViewKindData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int containerHeight = currentBrowserHeight - attributesViewKindDetailViewHeightOffset;
            this.containerConceptionKindsConfigView.setHeight(containerHeight-10);
            this.containsAttributeKindsConfigView.setHeight(containerHeight-10);
            this.attributesViewKindRuntimeConfigurationView.setViewHeight(containerHeight-60);
            this.attributesViewKindCorrelationInfoChartHeight = containerHeight -110;
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int containerHeight = currentBrowserHeight - attributesViewKindDetailViewHeightOffset;
            this.containerConceptionKindsConfigView.setHeight(containerHeight-10);
            this.containsAttributeKindsConfigView.setHeight(containerHeight-10);
            this.attributesViewKindRuntimeConfigurationView.setViewHeight(containerHeight-60);
            this.attributesViewKindCorrelationInfoChartHeight = containerHeight -110;

            this.attributesViewKindCorrelationInfoChartInitThread = new AttributesViewKindCorrelationInfoChartInitThread(attachEvent.getUI(), this);
            this.attributesViewKindCorrelationInfoChartInitThread.start();
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
        attributesViewKindCorrelationInfoChartInitThread.interrupt();
        attributesViewKindCorrelationInfoChartInitThread = null;
    }

    private void renderAttributesViewKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();
        String attributesViewKindDisplayInfo = this.attributesViewKindUID;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        if(targetAttributesViewKind != null){
            attributesViewKindDisplayInfo = targetAttributesViewKind.getAttributesViewKindName() +" ( "+this.attributesViewKindUID+" )";
        }
        coreRealm.closeGlobalSession();
        NativeLabel attributesViewKindNameLabel = new NativeLabel(attributesViewKindDisplayInfo);
        attributesViewKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(attributesViewKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.attributesViewKindUID,AttributesViewKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button attributesViewKindMetaInfoButton= new Button("属性视图类型元数据");
        attributesViewKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        attributesViewKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attributesViewKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });
        buttonList.add(attributesViewKindMetaInfoButton);

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        buttonList.add(divIcon);

        Button refreshAttributesViewKindConfigInfoButton= new Button("刷新属性视图类型配置信息");
        refreshAttributesViewKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshAttributesViewKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                containerConceptionKindsConfigView.refreshConceptionKindsInfo();
                containsAttributeKindsConfigView.refreshAttributeTypesInfo();
                refreshAttributesViewKindCorrelationInfoChart();
            }
        });
        buttonList.add(refreshAttributesViewKindConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"Attributes View Kind 属性视图类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(750, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.COGS),"属性视图类型配置概览");
        leftSideContainerLayout.add(infoTitle);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setPadding(false);
        rightSideContainerLayout.setMargin(false);
        mainContainerLayout.add(rightSideContainerLayout);

        kindConfigurationTabSheet = new TabSheet();
        kindConfigurationTabSheet.setWidthFull();
        rightSideContainerLayout.add(kindConfigurationTabSheet);
        rightSideContainerLayout.setFlexGrow(1,kindConfigurationTabSheet);

        attributesViewKindRuntimeConfigurationView = new AttributesViewKindRuntimeConfigurationView(this.attributesViewKindUID);
        containsAttributeKindsConfigView = new ContainsAttributeKindsConfigView(this.attributesViewKindUID);
        containerConceptionKindsConfigView = new ContainerConceptionKindsConfigView(this.attributesViewKindUID);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.SPARK_LINE,"属性视图类型运行时配置"),attributesViewKindRuntimeConfigurationView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.INPUT,"属性类型定义配置"), containsAttributeKindsConfigView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.CUBE,"概念类型定义配置"), containerConceptionKindsConfigView);
    }

    private void renderShowMetaInfoUI(){
        AttributesViewKindMetaInfoView attributesViewKindMetaInfoView = new AttributesViewKindMetaInfoView(this.attributesViewKindUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"属性视图类型元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(attributesViewKindMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private HorizontalLayout generateKindConfigurationTabTitle(VaadinIcon tabIcon,String tabTitleTxt){
        HorizontalLayout  kindConfigTabLayout = new HorizontalLayout();
        kindConfigTabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kindConfigTabLayout.setHeight(26,Unit.PIXELS);
        Icon configTabIcon = new Icon(tabIcon);
        configTabIcon.setSize("12px");
        NativeLabel configTabLabel = new NativeLabel(" "+tabTitleTxt);
        configTabLabel.getStyle()
                . set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        kindConfigTabLayout.add(configTabIcon,configTabLabel);
        return kindConfigTabLayout;
    }

    private void initAttributesViewKindCorrelationInfoChart(){
        attributesViewKindCorrelationInfoChart = new AttributesViewKindCorrelationInfoChart(attributesViewKindCorrelationInfoChartHeight);
        leftSideContainerLayout.add(attributesViewKindCorrelationInfoChart);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
        List<AttributeKind> containsAttributeKindsList = null;
        List<ConceptionKind> containerConceptionKindsList = null;
        if(targetAttributesViewKind != null){
            containsAttributeKindsList = targetAttributesViewKind.getContainsAttributeKinds();
            containerConceptionKindsList = targetAttributesViewKind.getContainerConceptionKinds();
        }
        coreRealm.closeGlobalSession();
        attributesViewKindCorrelationInfoChart.setData(targetAttributesViewKind,containerConceptionKindsList,containsAttributeKindsList);
    }

    private void refreshAttributesViewKindCorrelationInfoChart(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
        List<AttributeKind> containsAttributeKindsList = null;
        List<ConceptionKind> containerConceptionKindsList = null;
        if(targetAttributesViewKind != null){
            containsAttributeKindsList = targetAttributesViewKind.getContainsAttributeKinds();
            containerConceptionKindsList = targetAttributesViewKind.getContainerConceptionKinds();
        }
        coreRealm.closeGlobalSession();
        attributesViewKindCorrelationInfoChart.clearData();
        attributesViewKindCorrelationInfoChart.setData(targetAttributesViewKind,containerConceptionKindsList,containsAttributeKindsList);
    }

    private static class AttributesViewKindCorrelationInfoChartInitThread extends Thread {
        private final UI ui;
        private AttributesViewKindDetailUI attributesViewKindDetailUI;

        public AttributesViewKindCorrelationInfoChartInitThread(UI ui, AttributesViewKindDetailUI attributesViewKindDetailUI) {
            this.ui = ui;
            this.attributesViewKindDetailUI = attributesViewKindDetailUI;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(50);
                ui.access(() -> attributesViewKindDetailUI.initAttributesViewKindCorrelationInfoChart());
                this.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receivedAttributeKindAttachedToAttributesViewKindEvent(AttributeKindAttachedToAttributesViewKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                refreshAttributesViewKindCorrelationInfoChart();
            }
        }
    }

    @Override
    public void receivedAttributeKindDetachedFromAttributesViewKindEvent(AttributeKindDetachedFromAttributesViewKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                refreshAttributesViewKindCorrelationInfoChart();
            }
        }
    }
}
