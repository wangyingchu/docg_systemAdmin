package com.viewfunction.docg.views.corerealm.featureUI;

import ch.carnet.kasparscherrer.VerticalScrollLayout;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.CheckSystemRuntimeInfoEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.*;
import com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.exchangeCoreRealmEntities.DownloadARROWFormatCoreRealmEntitiesView;
import com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis.IntelligentAnalysisView;

import java.util.ArrayList;
import java.util.List;

public class CoreRealmDataUI extends VerticalLayout implements CheckSystemRuntimeInfoEvent.CheckSystemRuntimeInfoListener{

    private Registration listener;

    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout rightSideContentContainerLayout;
    private DataRelationDistributionWidget dataRelationDistributionWidget;
    private SystemRuntimeInfoWidget systemRuntimeInfoWidget;
    private RelationAndConceptionKindAttachInfoWidget relationAndConceptionKindAttachInfoWidget;
    private ConceptionKindInfoWidget conceptionKindInfoWidget;
    private RelationKindInfoWidget relationKindInfoWidget;
    private ClassificationInfoWidget classificationInfoWidget;
    private GeospatialRegionInfoWidget geospatialRegionInfoWidget;
    private TimeFlowInfoWidget timeFlowInfoWidget;
    private AttributeViewKindInfoWidget attributeViewKindInfoWidget;
    private AttributeKindInfoWidget attributeKindInfoWidget;
    private Button show3DChartButton;
    private Button show2DChartButton;
    public CoreRealmDataUI(){

        Button AIDataAnalyisisButton = new Button("领域模型智能分析",LineAwesomeIconsSvg.BRAIN_SOLID.create());
        AIDataAnalyisisButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        AIDataAnalyisisButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        AIDataAnalyisisButton.addClickListener((ClickEvent<Button> click) ->{
            renderIntelligentAnalysisView();
        });

        Button refreshDataButton = new Button("刷新领域数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            CheckSystemRuntimeInfoEvent checkSystemRuntimeInfoEvent = new CheckSystemRuntimeInfoEvent();
            checkSystemRuntimeInfoEvent.setCoreRealmName("Default CoreRealm");
            ResourceHolder.getApplicationBlackboard().fire(checkSystemRuntimeInfoEvent);
        });

        MenuBar advancedConfigItemsMenuBar = new MenuBar();
        //advancedConfigItemsMenuBar.setTooltipText(null, "高级配置管理");
        advancedConfigItemsMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL,MenuBarVariant.LUMO_CONTRAST);
        MenuItem advancedConfigMenu = createIconItem(advancedConfigItemsMenuBar, VaadinIcon.AUTOMATION, null, null);
        SubMenu advancedConfigMenuItems = advancedConfigMenu.getSubMenu();
        MenuItem arrowFormatDataExportItem = advancedConfigMenuItems.addItem("导出 ARROW 格式领域模型全量数据");
        arrowFormatDataExportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderDownloadARROWFormatCoreRealmEntitiesView();
            }
        });
        advancedConfigItemsMenuBar.setEnabled(false);
        List<Component> buttonList = new ArrayList<>();
        buttonList.add(AIDataAnalyisisButton);
        buttonList.add(refreshDataButton);
        buttonList.add(advancedConfigItemsMenuBar);

        List<Component> secTitleElementsList = new ArrayList<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        String implTech = coreRealm.getStorageImplTech().name();
        String coreRealmName = coreRealm.getCoreRealmName();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span(coreRealmName));
        realmNameSpan.getStyle().set("font-size","var(--lumo-font-size-xs)");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        NativeLabel coreRealmTechLabel = new NativeLabel(" "+implTech+" 实现");
        coreRealmTechLabel.getStyle().set("font-size","var(--lumo-font-size-xxs)");
        secTitleElementsList.add(coreRealmTechLabel);
        coreRealmTechLabel.getElement().getThemeList().add("badge success");

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Core Realm 领域模型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setSpacing(false);
        leftSideContentContainerLayout.setWidth(550, Unit.PIXELS);
        leftSideContentContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");
        contentContainerLayout.add(leftSideContentContainerLayout);

        rightSideContentContainerLayout = new VerticalLayout();
        rightSideContentContainerLayout.setSpacing(false);
        rightSideContentContainerLayout.setPadding(false);

        contentContainerLayout.add(rightSideContentContainerLayout);

        HorizontalLayout coreRealmInfoContainerLayout = new HorizontalLayout();
        coreRealmInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        Icon icon = new Icon(VaadinIcon.AUTOMATION);
        //leftSideContentContainerLayout.add(FontAwesome.Solid.ADDRESS_CARD.create());
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据概览信息",null);
        leftSideContentContainerLayout.add(sectionActionBar);

        VerticalScrollLayout leftSideSectionContainerScrollLayout = new VerticalScrollLayout();
        leftSideContentContainerLayout.add(leftSideSectionContainerScrollLayout);

        Icon conceptionKindInfoTitleIcon = new Icon(VaadinIcon.CUBE);
        conceptionKindInfoTitleIcon.setSize("18px");
        NativeLabel conceptionKindInfoTitleLabel = new NativeLabel("ConceptionKind-概念类型");
        conceptionKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle conceptionKindInfoSectionWallTitle = new SectionWallTitle(conceptionKindInfoTitleIcon,conceptionKindInfoTitleLabel);
        conceptionKindInfoWidget = new ConceptionKindInfoWidget();
        SectionWallContainer conceptionKindInfoSectionWallContainer = new SectionWallContainer(conceptionKindInfoSectionWallTitle,conceptionKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(conceptionKindInfoSectionWallContainer);
        conceptionKindInfoSectionWallContainer.setOpened(false);
        conceptionKindInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                conceptionKindInfoWidget.loadWidgetContent();
            }
        });

        Icon relationKindInfoTitleIcon = new Icon(VaadinIcon.CONNECT_O);
        relationKindInfoTitleIcon.setSize("18px");
        NativeLabel relationKindInfoTitleLabel = new NativeLabel("RelationKind-关系类型");
        relationKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle relationKindInfoSectionWallTitle = new SectionWallTitle(relationKindInfoTitleIcon,relationKindInfoTitleLabel);
        relationKindInfoWidget = new RelationKindInfoWidget();
        SectionWallContainer relationKindInfoSectionWallContainer = new SectionWallContainer(relationKindInfoSectionWallTitle,relationKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(relationKindInfoSectionWallContainer);
        relationKindInfoSectionWallContainer.setOpened(false);
        relationKindInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                relationKindInfoWidget.loadWidgetContent();
            }
        });

        Icon classificationInfoTitleIcon = new Icon(VaadinIcon.TAGS);
        classificationInfoTitleIcon.setSize("18px");
        NativeLabel classificationInfoTitleLabel = new NativeLabel("Classification-分类");
        classificationInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle classificationInfoSectionWallTitle = new SectionWallTitle(classificationInfoTitleIcon,classificationInfoTitleLabel);
        classificationInfoWidget = new ClassificationInfoWidget();
        SectionWallContainer classificationInSectionWallContainer = new SectionWallContainer(classificationInfoSectionWallTitle,classificationInfoWidget);
        leftSideSectionContainerScrollLayout.add(classificationInSectionWallContainer);
        classificationInSectionWallContainer.setOpened(false);
        classificationInSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                classificationInfoWidget.loadWidgetContent();
            }
        });

        Icon geospatialRegionInfoTitleIcon = new Icon(VaadinIcon.GLOBE_WIRE);
        geospatialRegionInfoTitleIcon.setSize("18px");
        NativeLabel geospatialRegionInfoTitleLabel = new NativeLabel("GeospatialRegion-地理空间区域");
        geospatialRegionInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle geospatialRegionInfoSectionWallTitle = new SectionWallTitle(geospatialRegionInfoTitleIcon,geospatialRegionInfoTitleLabel);
        geospatialRegionInfoWidget = new GeospatialRegionInfoWidget();
        SectionWallContainer geospatialRegionInSectionWallContainer = new SectionWallContainer(geospatialRegionInfoSectionWallTitle,geospatialRegionInfoWidget);
        leftSideSectionContainerScrollLayout.add(geospatialRegionInSectionWallContainer);
        geospatialRegionInSectionWallContainer.setOpened(false);
        geospatialRegionInSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                geospatialRegionInfoWidget.loadWidgetContent();
            }
        });

        Icon timeFlowInfoTitleIcon = new Icon(VaadinIcon.TIMER);
        timeFlowInfoTitleIcon.setSize("18px");
        NativeLabel timeFlowInfoTitleLabel = new NativeLabel("TimeFlow-时间流");
        timeFlowInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle timeFlowInfoSectionWallTitle = new SectionWallTitle(timeFlowInfoTitleIcon,timeFlowInfoTitleLabel);
        timeFlowInfoWidget = new TimeFlowInfoWidget();
        SectionWallContainer timeFlowInSectionWallContainer = new SectionWallContainer(timeFlowInfoSectionWallTitle,timeFlowInfoWidget);
        leftSideSectionContainerScrollLayout.add(timeFlowInSectionWallContainer);
        timeFlowInSectionWallContainer.setOpened(false);
        timeFlowInSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                timeFlowInfoWidget.loadWidgetContent();
            }
        });

        Icon attributesViewKindInfoTitleIcon = new Icon(VaadinIcon.TASKS);
        attributesViewKindInfoTitleIcon.setSize("18px");
        NativeLabel attributesViewKindInfoTitleLabel = new NativeLabel("AttributesViewKind-属性视图类型");
        attributesViewKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle attributesViewKindInfoSectionWallTitle = new SectionWallTitle(attributesViewKindInfoTitleIcon,attributesViewKindInfoTitleLabel);
        attributeViewKindInfoWidget = new AttributeViewKindInfoWidget();
        SectionWallContainer attributesViewKindInSectionWallContainer = new SectionWallContainer(attributesViewKindInfoSectionWallTitle, attributeViewKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(attributesViewKindInSectionWallContainer);
        attributesViewKindInSectionWallContainer.setOpened(false);
        attributesViewKindInSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                attributeViewKindInfoWidget.loadWidgetContent();
            }
        });

        Icon attributesKindInfoTitleIcon = new Icon(VaadinIcon.INPUT);
        attributesKindInfoTitleIcon.setSize("18px");
        NativeLabel attributesKindInfoTitleLabel = new NativeLabel("AttributesKind-属性类型");
        attributesKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle attributesKindInfoSectionWallTitle = new SectionWallTitle(attributesKindInfoTitleIcon,attributesKindInfoTitleLabel);
        attributeKindInfoWidget = new AttributeKindInfoWidget();
        SectionWallContainer attributesKindInSectionWallContainer = new SectionWallContainer(attributesKindInfoSectionWallTitle, attributeKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(attributesKindInSectionWallContainer);
        attributesKindInSectionWallContainer.setOpened(false);
        attributesKindInSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                attributeKindInfoWidget.loadWidgetContent();
            }
        });

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(10,Unit.PIXELS);
        leftSideSectionContainerScrollLayout.add(spaceDivLayout);

        List<Component> actionComponentsList = new ArrayList<>();
        Button refreshSystemRuntimeInfoButton = new Button("获取系统运行信息",new Icon(VaadinIcon.REFRESH));
        refreshSystemRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshSystemRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshSystemRuntimeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        refreshSystemRuntimeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            systemRuntimeInfoWidget.refreshSystemRuntimeInfo();
        });
        actionComponentsList.add(refreshSystemRuntimeInfoButton);

        Icon icon3 = new Icon(VaadinIcon.SPARK_LINE);
        SecondaryTitleActionBar sectionActionBar3 = new SecondaryTitleActionBar(icon3,"系统运行信息",null,actionComponentsList);
        leftSideSectionContainerScrollLayout.add(sectionActionBar3);

        systemRuntimeInfoWidget = new SystemRuntimeInfoWidget();
        leftSideSectionContainerScrollLayout.add(systemRuntimeInfoWidget);

        List<Component> sectionAction2ComponentsList = new ArrayList<>();

        show2DChartButton = new Button();
        show2DChartButton.setTooltipText("显示 2维 全域数据关联分布图");
        Icon _2DSwitchIcon = LineAwesomeIconsSvg.STOP_SOLID.create();;
        _2DSwitchIcon.setSize("15px");
        show2DChartButton.setHeight(19,Unit.PIXELS);
        show2DChartButton.setIcon(_2DSwitchIcon);
        show2DChartButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        show2DChartButton.addClickListener((ClickEvent<Button> click) -> {
            dataRelationDistributionWidget.show2DChart();
            show3DChartButton.setEnabled(true);
            show2DChartButton.setEnabled(false);
        });
        sectionAction2ComponentsList.add(show2DChartButton);
        show2DChartButton.setEnabled(false);

        show3DChartButton = new Button();
        show3DChartButton.setTooltipText("显示 3维 全域数据关联分布图");
        Icon _3DSwitchIcon = LineAwesomeIconsSvg.CUBE_SOLID.create();
        _3DSwitchIcon.setSize("15px");
        show3DChartButton.setHeight(19,Unit.PIXELS);
        show3DChartButton.setIcon(_3DSwitchIcon);
        show3DChartButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        show3DChartButton.addClickListener((ClickEvent<Button> click) -> {
            dataRelationDistributionWidget.show3DChart();
            show3DChartButton.setEnabled(false);
            show2DChartButton.setEnabled(true);
        });
        sectionAction2ComponentsList.add(show3DChartButton);

        SectionActionBar sectionActionBar2 = new SectionActionBar(FontAwesome.Solid.CODE_FORK.create(),"全域数据关联分布",sectionAction2ComponentsList);
        rightSideContentContainerLayout.add(sectionActionBar2);

        HorizontalLayout widgetDivLayout = new HorizontalLayout();
        widgetDivLayout.setMargin(false);
        widgetDivLayout.setSpacing(false);
        widgetDivLayout.setPadding(false);
        rightSideContentContainerLayout.add(widgetDivLayout);

        dataRelationDistributionWidget = new DataRelationDistributionWidget();
        dataRelationDistributionWidget.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-10pct)");

        widgetDivLayout.add(dataRelationDistributionWidget);

        relationAndConceptionKindAttachInfoWidget = new RelationAndConceptionKindAttachInfoWidget();
        relationAndConceptionKindAttachInfoWidget.setWidth(400,Unit.PIXELS);
        widgetDivLayout.add(relationAndConceptionKindAttachInfoWidget);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-175,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(event.getWidth()-580,Unit.PIXELS);
            this.dataRelationDistributionWidget.setHeight(event.getHeight()-205,Unit.PIXELS);
            this.dataRelationDistributionWidget.setWidth(event.getWidth()-550-420-50,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-175,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(browserWidth-580,Unit.PIXELS);
            this.dataRelationDistributionWidget.setHeight(browserHeight-205,Unit.PIXELS);
            this.dataRelationDistributionWidget.setWidth(browserWidth-550-420-50,Unit.PIXELS);
        }));
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedCheckSystemRuntimeInfoEvent(CheckSystemRuntimeInfoEvent event) {
        systemRuntimeInfoWidget.refreshSystemRuntimeInfo();
        dataRelationDistributionWidget.refreshDataRelationDistributionData();
        relationAndConceptionKindAttachInfoWidget.renderRelationAndConceptionKindAttachInfo();
        conceptionKindInfoWidget.reloadWidgetContent();
        relationKindInfoWidget.reloadWidgetContent();
        classificationInfoWidget.reloadWidgetContent();
        geospatialRegionInfoWidget.reloadWidgetContent();
        timeFlowInfoWidget.reloadWidgetContent();
        attributeViewKindInfoWidget.reloadWidgetContent();
        attributeKindInfoWidget.reloadWidgetContent();
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);
        icon.setSize("16px");
        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }
        MenuItem item = menu.addItem(icon, e -> {
        });
        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }
        if (label != null) {
            item.add(new Text(label));
        }
        return item;
    }

    private void renderDownloadARROWFormatCoreRealmEntitiesView(){
        DownloadARROWFormatCoreRealmEntitiesView downloadARROWFormatCoreRealmEntitiesView = new DownloadARROWFormatCoreRealmEntitiesView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 ARROW 格式领域模型全量数据",null,true,550,290,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadARROWFormatCoreRealmEntitiesView);
        fixSizeWindow.setModel(true);
        downloadARROWFormatCoreRealmEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderIntelligentAnalysisView(){
        IntelligentAnalysisView intelligentAnalysisView = new IntelligentAnalysisView();
        FullScreenWindow fullSizeWindow = new FullScreenWindow(LineAwesomeIconsSvg.BRAIN_SOLID.create(),"领域模型智能分析",null,null,true);
        fullSizeWindow.setModel(true);
        fullSizeWindow.setWindowContent(intelligentAnalysisView);
        fullSizeWindow.show();
        fullSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {}
        });
    }
}
