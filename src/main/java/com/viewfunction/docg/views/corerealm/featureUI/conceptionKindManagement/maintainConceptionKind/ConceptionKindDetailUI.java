package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.*;

import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindCleanedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindConfigurationInfoRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.CreateAttributeKindView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributesViewKindMaintain.RelatedAttributesViewKindRuntimeConfigurationInfoView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain.AttributesValueListView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain.RelationAttachKindsConfigurationView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.ConceptionKindQueryUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.ConceptionKind;

@Route("conceptionKindDetailInfo/:conceptionKind")
public class ConceptionKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver,
        ConceptionEntitiesCountRefreshEvent.ConceptionEntitiesCountRefreshListener,
        ConceptionKindCleanedEvent.ConceptionKindCleanedListener,
        ConceptionKindConfigurationInfoRefreshEvent.ConceptionKindConfigurationInfoRefreshListener{
    private String conceptionKind;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private int conceptionKindDetailViewHeightOffset = 110;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;
    private ConceptionKindCorrelationInfoChart conceptionKindCorrelationInfoChart;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private VerticalLayout conceptionKindCorrelationInfoChartContainer;
    private TabSheet kindCorrelationInfoTabSheet;
    private Tab conceptionRealTimeInfoTab;
    private Tab conceptionRealTimeChartTab;
    private boolean conceptionRealTimeChartFirstLoaded = false;
    private Registration listener;
    private int currentBrowserHeight = 0;
    private Grid<ConceptionKindCorrelationInfo> conceptionRelationRealtimeInfoGrid;
    private boolean conceptionRealTimeInfoGridFirstLoaded = false;
    private VerticalLayout conceptionKindCorrelationInfoGridContainer;
    private TabSheet kindConfigurationTabSheet;
    private RelatedAttributesViewKindRuntimeConfigurationInfoView relatedAttributesViewKindRuntimeConfigurationInfoView;
    private ConceptionKindEntitiesConfigurationView conceptionKindEntitiesConfigurationView;
    private RelationAttachKindsConfigurationView relationAttachKindsConfigurationView;

    public ConceptionKindDetailUI(){}

    public ConceptionKindDetailUI(String conceptionKind){
        this.conceptionKind = conceptionKind;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.conceptionKind = beforeEnterEvent.getRouteParameters().get("conceptionKind").get();
        this.conceptionKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderConceptionKindData();
        loadConceptionKindInfoData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int chartHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset - 340;
            conceptionKindCorrelationInfoChart.setHeight(chartHeight,Unit.PIXELS);
            this.conceptionRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
            this.conceptionKindEntitiesConfigurationView.setViewHeight(currentBrowserHeight- conceptionKindDetailViewHeightOffset -100);
            this.relatedAttributesViewKindRuntimeConfigurationInfoView.setViewHeight(currentBrowserHeight- conceptionKindDetailViewHeightOffset -75);
            this.relatedAttributesViewKindRuntimeConfigurationInfoView.setViewWidth(event.getWidth()-820);
            this.relationAttachKindsConfigurationView.setViewHeight(currentBrowserHeight- conceptionKindDetailViewHeightOffset -75);
            this.relationAttachKindsConfigurationView.setViewWidth(event.getWidth()-820);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            kindCorrelationInfoTabSheet.setHeight(currentBrowserHeight-conceptionKindDetailViewHeightOffset-290,Unit.PIXELS);
            this.conceptionKindEntitiesConfigurationView.setViewHeight(currentBrowserHeight- conceptionKindDetailViewHeightOffset -100);
            this.relatedAttributesViewKindRuntimeConfigurationInfoView.setViewHeight(currentBrowserHeight- conceptionKindDetailViewHeightOffset -75);
            this.relatedAttributesViewKindRuntimeConfigurationInfoView.setViewWidth(receiver.getBodyClientWidth()-820);
            this.relationAttachKindsConfigurationView.setViewHeight(currentBrowserHeight- conceptionKindDetailViewHeightOffset -75);
            this.relationAttachKindsConfigurationView.setViewWidth(receiver.getBodyClientWidth()-820);
        }));
        renderKindCorrelationInfoTabContent();
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderConceptionKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();

        NativeLabel conceptionKindNameLabel = new NativeLabel(this.conceptionKind);
        conceptionKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(conceptionKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.conceptionKind,ConceptionKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button queryConceptionKindButton= new Button("概念类型实体数据查询");
        queryConceptionKindButton.setIcon(VaadinIcon.RECORDS.create());
        queryConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        queryConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderConceptionKindQueryUI();
            }
        });
        buttonList.add(queryConceptionKindButton);

        Button conceptionKindMetaInfoButton= new Button("概念类型元数据");
        conceptionKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        conceptionKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        conceptionKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });
        buttonList.add(conceptionKindMetaInfoButton);

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        buttonList.add(divIcon);

        Button refreshConceptionKindConfigInfoButton= new Button("刷新概念类型配置信息");
        refreshConceptionKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshConceptionKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshConceptionKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                ConceptionKindConfigurationInfoRefreshEvent conceptionKindConfigurationInfoRefreshEvent = new ConceptionKindConfigurationInfoRefreshEvent();
                conceptionKindConfigurationInfoRefreshEvent.setConceptionKindName(conceptionKind);
                ResourceHolder.getApplicationBlackboard().fire(conceptionKindConfigurationInfoRefreshEvent);
                relatedAttributesViewKindRuntimeConfigurationInfoView.refreshRelatedAttributesViewKindRuntimeConfigurationInfo();
                relationAttachKindsConfigurationView.refreshRelationAttachKindsInfo();
            }
        });
        buttonList.add(refreshConceptionKindConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"Conception Kind 概念类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(800, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+10000+")");
        infoTitle1.getStyle().set("padding-bottom","5px");

        leftSideContainerLayout.add(infoTitle1);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            KindEntityAttributeRuntimeStatistics attributeInfo = (KindEntityAttributeRuntimeStatistics)entityStatisticsInfo;
            MenuBar actionsMenuBar = new MenuBar();
            actionsMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
            Icon icon = new Icon(VaadinIcon.CHEVRON_DOWN);
            icon.setSize("14px");
            MenuItem dropdownIconMenu = actionsMenuBar.addItem(icon, e -> {});
            SubMenu actionOptionsSubItems = dropdownIconMenu.getSubMenu();

            HorizontalLayout action0Layout = new HorizontalLayout();
            action0Layout.setPadding(false);
            action0Layout.setSpacing(false);
            action0Layout.setMargin(false);
            action0Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action0Icon = LineAwesomeIconsSvg.EYE_DROPPER_SOLID.create();
            action0Icon.setSize("10px");
            Span action0Space = new Span();
            action0Space.setWidth(6,Unit.PIXELS);
            NativeLabel action0Label = new NativeLabel("属性随机采样(100)");
            action0Label.addClassNames("text-xs","font-semibold","text-secondary");
            action0Layout.add(action0Icon,action0Space,action0Label);
            MenuItem action0Item = actionOptionsSubItems.addItem(action0Layout);
            action0Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    renderSampleRandomAttributesView(attributeInfo.getAttributeName());
                }
            });

            HorizontalLayout action1Layout = new HorizontalLayout();
            action1Layout.setPadding(false);
            action1Layout.setSpacing(false);
            action1Layout.setMargin(false);
            action1Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action1Icon = new Icon((VaadinIcon.INPUT));
            action1Icon.setSize("10px");
            Span action1Space = new Span();
            action1Space.setWidth(6,Unit.PIXELS);
            NativeLabel action1Label = new NativeLabel("设定为属性类型");
            action1Label.addClassNames("text-xs","font-semibold","text-secondary");
            action1Layout.add(action1Icon,action1Space,action1Label);
            MenuItem action1Item = actionOptionsSubItems.addItem(action1Layout);
            action1Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    //renderLoadCSVFormatConceptionEntitiesView();
                    renderAddAttributeKindView(attributeInfo);
                }
            });

            HorizontalLayout action2Layout = new HorizontalLayout();
            action2Layout.setPadding(false);
            action2Layout.setSpacing(false);
            action2Layout.setMargin(false);
            action2Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action2Icon = new Icon((VaadinIcon.ERASER));
            action2Icon.setSize("10px");
            Span action2Space = new Span();
            action2Space.setWidth(6,Unit.PIXELS);
            NativeLabel action2Label = new NativeLabel("删除属性");
            action2Label.addClassNames("text-xs","font-semibold","text-secondary");
            action2Layout.add(action2Icon,action2Space,action2Label);
            MenuItem action2Item = actionOptionsSubItems.addItem(action2Layout);
            action2Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    //renderLoadCSVFormatConceptionEntitiesView();
                    System.out.println(attributeInfo.getAttributeName());
                }
            });

            HorizontalLayout containerAction3Layout = new HorizontalLayout();
            containerAction3Layout.setPadding(false);
            containerAction3Layout.setSpacing(false);
            containerAction3Layout.setMargin(false);
            containerAction3Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon containerAction3Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
            containerAction3Icon.setSize("10px");
            Span containerAction3Space = new Span();
            containerAction3Space.setWidth(6,Unit.PIXELS);
            NativeLabel containerAction3Label = new NativeLabel("属性数据类型转换");
            containerAction3Label.addClassNames("text-xs","font-semibold","text-secondary");
            containerAction3Layout.add(containerAction3Icon,containerAction3Space,containerAction3Label);
            MenuItem containerAction3Item = actionOptionsSubItems.addItem(containerAction3Layout);

            AttributeDataType attributeDataType = attributeInfo.getAttributeDataType();

            if(!attributeDataType.equals(AttributeDataType.INT)){
                HorizontalLayout action3Layout = new HorizontalLayout();
                action3Layout.setPadding(false);
                action3Layout.setSpacing(false);
                action3Layout.setMargin(false);
                action3Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                Icon action3Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                action3Icon.setSize("10px");
                Span action3Space = new Span();
                action3Space.setWidth(6,Unit.PIXELS);
                NativeLabel action3Label = new NativeLabel("转为 INT 类型");
                action3Label.addClassNames("text-xs","font-semibold","text-secondary");
                action3Layout.add(action3Icon,action3Space,action3Label);
                MenuItem action3Item = containerAction3Item.getSubMenu().addItem(action3Layout);
                action3Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        //renderLoadCSVFormatConceptionEntitiesView();
                        System.out.println(attributeInfo.getAttributeName());
                    }
                });
            }
            if(!attributeDataType.equals(AttributeDataType.FLOAT)){
                HorizontalLayout action4Layout = new HorizontalLayout();
                action4Layout.setPadding(false);
                action4Layout.setSpacing(false);
                action4Layout.setMargin(false);
                action4Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                Icon action4Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                action4Icon.setSize("10px");
                Span action4Space = new Span();
                action4Space.setWidth(6,Unit.PIXELS);
                NativeLabel action4Label = new NativeLabel("转为 FLOAT 类型");
                action4Label.addClassNames("text-xs","font-semibold","text-secondary");
                action4Layout.add(action4Icon,action4Space,action4Label);
                MenuItem action4Item = containerAction3Item.getSubMenu().addItem(action4Layout);
                action4Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        //renderLoadCSVFormatConceptionEntitiesView();
                        System.out.println(attributeInfo.getAttributeName());
                    }
                });
            }
            if(!attributeDataType.equals(AttributeDataType.BOOLEAN)){
                HorizontalLayout action5Layout = new HorizontalLayout();
                action5Layout.setPadding(false);
                action5Layout.setSpacing(false);
                action5Layout.setMargin(false);
                action5Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                Icon action5Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                action5Icon.setSize("10px");
                Span action5Space = new Span();
                action5Space.setWidth(6,Unit.PIXELS);
                NativeLabel action5Label = new NativeLabel("转为 BOOLEAN 类型");
                action5Label.addClassNames("text-xs","font-semibold","text-secondary");
                action5Layout.add(action5Icon,action5Space,action5Label);
                MenuItem action5Item = containerAction3Item.getSubMenu().addItem(action5Layout);
                action5Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        //renderLoadCSVFormatConceptionEntitiesView();
                        System.out.println(attributeInfo.getAttributeName());
                    }
                });
            }
            if(!attributeDataType.equals(AttributeDataType.STRING)){
                HorizontalLayout action6Layout = new HorizontalLayout();
                action6Layout.setPadding(false);
                action6Layout.setSpacing(false);
                action6Layout.setMargin(false);
                action6Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                Icon action6Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                action6Icon.setSize("10px");
                Span action6Space = new Span();
                action6Space.setWidth(6,Unit.PIXELS);
                NativeLabel action6Label = new NativeLabel("转为 STRING 类型");
                action6Label.addClassNames("text-xs","font-semibold","text-secondary");
                action6Layout.add(action6Icon,action6Space,action6Label);
                MenuItem action6Item = containerAction3Item.getSubMenu().addItem(action6Layout);
                action6Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        //renderLoadCSVFormatConceptionEntitiesView();
                        System.out.println(attributeInfo.getAttributeName());
                    }
                });
            }
            return actionsMenuBar;
        });

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getSampleCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性采样数").setKey("idx_2")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getAttributeHitCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性命中数").setKey("idx_3")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("60px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> getAttributeName(kindEntityAttributeRuntimeStatistics));
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.EYEDROPPER,"属性采样数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);

        conceptionKindAttributesInfoGrid.setHeight(218,Unit.PIXELS);
        leftSideContainerLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT),"概念类型实体关联分布");
        infoTitle2.getStyle().set("padding-top","20px");

        leftSideContainerLayout.add(infoTitle2);
        this.conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(500);

        conceptionKindCorrelationInfoGridContainer = new VerticalLayout();
        conceptionKindCorrelationInfoGridContainer.setPadding(false);
        conceptionKindCorrelationInfoGridContainer.setSpacing(false);
        conceptionKindCorrelationInfoGridContainer.setMargin(false);

        conceptionKindCorrelationInfoChartContainer = new VerticalLayout();
        conceptionKindCorrelationInfoChartContainer.setPadding(false);
        conceptionKindCorrelationInfoChartContainer.setSpacing(false);
        conceptionKindCorrelationInfoChartContainer.setMargin(false);

        kindCorrelationInfoTabSheet = new TabSheet();
        kindCorrelationInfoTabSheet.setWidthFull();

        conceptionRealTimeInfoTab = kindCorrelationInfoTabSheet.add("",conceptionKindCorrelationInfoGridContainer);
        Span relationInfoSpan =new Span();
        Icon relationInfoIcon = new Icon(VaadinIcon.BULLETS);
        relationInfoIcon.setSize("12px");
        NativeLabel relationInfoLabel = new NativeLabel(" 概念关联实时分布");
        relationInfoSpan.add(relationInfoIcon,relationInfoLabel);
        conceptionRealTimeInfoTab.add(relationInfoSpan);

        conceptionRealTimeChartTab = kindCorrelationInfoTabSheet.add("",conceptionKindCorrelationInfoChartContainer);
        Span chartInfoSpan =new Span();
        Icon chartInfoIcon = new Icon(VaadinIcon.SPLIT);
        chartInfoIcon.setSize("12px");
        NativeLabel chartInfoLabel = new NativeLabel(" 概念关联实时分布网络图");
        chartInfoSpan.add(chartInfoIcon,chartInfoLabel);
        conceptionRealTimeChartTab.add(chartInfoSpan);

        kindCorrelationInfoTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {
                renderKindCorrelationInfoTabContent();
            }
        });
        leftSideContainerLayout.add(kindCorrelationInfoTabSheet);

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

        conceptionKindEntitiesConfigurationView = new ConceptionKindEntitiesConfigurationView(this.conceptionKind);
        relatedAttributesViewKindRuntimeConfigurationInfoView = new RelatedAttributesViewKindRuntimeConfigurationInfoView(
                RelatedAttributesViewKindRuntimeConfigurationInfoView.KindTypeOfRelatedPair.ConceptionKind,this.conceptionKind);
        relationAttachKindsConfigurationView = new RelationAttachKindsConfigurationView(RelationAttachKindsConfigurationView.RelatedKindType.ConceptionKind,this.conceptionKind);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.SPARK_LINE,"概念类型运行时配置"),conceptionKindEntitiesConfigurationView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TASKS,"属性视图配置"),relatedAttributesViewKindRuntimeConfigurationInfoView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TREE_TABLE,"关联关系规则配置"),relationAttachKindsConfigurationView);
        //kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.CALC,"统计与评估计算"),new HorizontalLayout());
    }

    private void loadConceptionKindInfoData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(10000);
        coreRealm.closeGlobalSession();
        conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
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

    private String getAttributeName(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics){
        return kindEntityAttributeRuntimeStatistics.getAttributeName();
    }

    private void renderKindCorrelationInfoTabContent(){
        if(conceptionRealTimeInfoTab.isSelected()){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
            Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
            if(!this.conceptionRealTimeInfoGridFirstLoaded){
                int chartHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset - 340;
                initConceptionRelationRealtimeInfoGrid(conceptionKindCorrelationInfoSet);
                this.conceptionRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
                this.conceptionKindCorrelationInfoGridContainer.add(this.conceptionRelationRealtimeInfoGrid);
                this.conceptionRealTimeInfoGridFirstLoaded = true;
            }else{
                refreshConceptionRelationRealtimeInfoGrid(conceptionKindCorrelationInfoSet);
            }
        }else if(conceptionRealTimeChartTab.isSelected()){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
            Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
            if(!this.conceptionRealTimeChartFirstLoaded){
                int chartHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset - 340;
                conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(chartHeight);
                conceptionKindCorrelationInfoChartContainer.add(conceptionKindCorrelationInfoChart);
                conceptionKindCorrelationInfoChart.clearData();
                conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,conceptionKind);
                this.conceptionRealTimeChartFirstLoaded = true;
            }else{
                conceptionKindCorrelationInfoChart.clearData();
                conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,conceptionKind);
            }
        }else{}
    }

    private void initConceptionRelationRealtimeInfoGrid(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        this.conceptionRelationRealtimeInfoGrid = new Grid<>();
        this.conceptionRelationRealtimeInfoGrid.setWidth(99,Unit.PERCENTAGE);
        this.conceptionRelationRealtimeInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.conceptionRelationRealtimeInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        this.conceptionRelationRealtimeInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        this.conceptionRelationRealtimeInfoGrid.addColumn(ConceptionKindCorrelationInfo::getRelationKindName).setHeader("关系类型").setKey("idx_1");
        this.conceptionRelationRealtimeInfoGrid.addComponentColumn(new RelatedConceptionKindValueProvider()).setHeader("关联概念类型").setKey("idx_2");
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.CONNECT_O,"关联关系类型");
        conceptionRelationRealtimeInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(false)
                .setTooltipGenerator(conceptionKindCorrelationInfo -> getRelationKindName(conceptionKindCorrelationInfo));
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CUBE,"关联概念类型");
        conceptionRelationRealtimeInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false)
                .setTooltipGenerator(conceptionKindCorrelationInfo -> geConceptionKindName(conceptionKindCorrelationInfo));;
        this.conceptionRelationRealtimeInfoGrid.setItems(conceptionKindCorrelationInfoSet);
    }

    private void refreshConceptionRelationRealtimeInfoGrid(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        ListDataProvider dtaProvider= (ListDataProvider)this.conceptionRelationRealtimeInfoGrid.getDataProvider();
        dtaProvider.getItems().clear();
        dtaProvider.getItems().addAll(conceptionKindCorrelationInfoSet);
        dtaProvider.refreshAll();
    }

    private String getRelationKindName(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo){
        return conceptionKindCorrelationInfo.getRelationKindName();
    }

    private String geConceptionKindName(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo){
        String fromConceptionKind = conceptionKindCorrelationInfo.getSourceConceptionKindName();
        String toConceptionKind = conceptionKindCorrelationInfo.getTargetConceptionKindName();
        if(conceptionKind.equals(fromConceptionKind)){
            return toConceptionKind;
        }else{
            return fromConceptionKind;
        }
    }

    @Override
    public void receivedConceptionEntitiesCountRefreshEvent(ConceptionEntitiesCountRefreshEvent event) {
        refreshConceptionKindAttributesInfoGrid();
    }

    @Override
    public void receivedConceptionKindCleanedEvent(ConceptionKindCleanedEvent event) {
        refreshConceptionKindAttributesInfoGrid();
        renderKindCorrelationInfoTabContent();
    }

    @Override
    public void receivedConceptionKindConfigurationInfoRefreshEvent(ConceptionKindConfigurationInfoRefreshEvent event) {
        refreshConceptionKindAttributesInfoGrid();
        renderKindCorrelationInfoTabContent();
    }

    private void refreshConceptionKindAttributesInfoGrid(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(10000);
        coreRealm.closeGlobalSession();
        ListDataProvider dataProvider=(ListDataProvider)conceptionKindAttributesInfoGrid.getDataProvider();
        dataProvider.getItems().clear();
        dataProvider.refreshAll();
        conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<ConceptionKindCorrelationInfo,Icon> {
        @Override
        public Icon apply(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo) {
            conceptionKindCorrelationInfo.getSourceConceptionKindName();
            Icon relationDirectionIcon = null;
            String fromConceptionKind = conceptionKindCorrelationInfo.getSourceConceptionKindName();
            String toConceptionKind = conceptionKindCorrelationInfo.getTargetConceptionKindName();
            if(conceptionKind.equals(fromConceptionKind)){
                relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
            }
            if(conceptionKind.equals(toConceptionKind)){
                relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
            }
            return relationDirectionIcon;
        }
    }

    private class RelatedConceptionKindValueProvider implements ValueProvider<ConceptionKindCorrelationInfo,HorizontalLayout> {
        public HorizontalLayout apply(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo) {
            HorizontalLayout conceptionKindContainerLayout = new HorizontalLayout();
            String fromConceptionKind = conceptionKindCorrelationInfo.getSourceConceptionKindName();
            String toConceptionKind = conceptionKindCorrelationInfo.getTargetConceptionKindName();
            NativeLabel displayConceptionKind = null;
            if(conceptionKind.equals(fromConceptionKind)){
                displayConceptionKind = new NativeLabel(toConceptionKind);
            }
            if(conceptionKind.equals(toConceptionKind)){
                displayConceptionKind = new NativeLabel(fromConceptionKind);
            }
            conceptionKindContainerLayout.add(displayConceptionKind);
            return conceptionKindContainerLayout;
        }
    }

    private void renderConceptionKindQueryUI(){
        ConceptionKindQueryUI conceptionKindQueryUI = new ConceptionKindQueryUI(this.conceptionKind);
        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindName = new NativeLabel(this.conceptionKind);
        titleDetailLayout.add(conceptionKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念类型实体数据查询",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionKindQueryUI);
        fullScreenWindow.show();
    }

    private void renderShowMetaInfoUI(){
        ConceptionKindMetaInfoView conceptionKindMetaInfoView = new ConceptionKindMetaInfoView(this.conceptionKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"概念类型元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(conceptionKindMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderSampleRandomAttributesView(String attributeName){
        AttributesValueListView attributesValueListView = new AttributesValueListView(AttributesValueListView.AttributeKindType.ConceptionKind,this.conceptionKind,attributeName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.EYE_DROPPER_SOLID.create(),"属性值随机采样 (100项)",null,true,500,510,false);
        fixSizeWindow.setWindowContent(attributesValueListView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAddAttributeKindView(KindEntityAttributeRuntimeStatistics attributeInfo){
        CreateAttributeKindView createAttributeKindView = new CreateAttributeKindView();
        createAttributeKindView.setAndLockAttributeKindName(attributeInfo.getAttributeName());
        createAttributeKindView.setAndLockAttributeKindDataType(attributeInfo.getAttributeDataType());

        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建属性类型",null,true,500,350,false);
        fixSizeWindow.setWindowContent(createAttributeKindView);
        createAttributeKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
