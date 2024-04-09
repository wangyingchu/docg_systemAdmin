package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.TemporalScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.KindScopeAttributeAddedEvent;
import com.viewfunction.docg.element.eventHandling.RelationEntitiesCountRefreshEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindCleanedEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindConfigurationInfoRefreshEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain.AttributesValueListView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain.DuplicateAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeTypeConvert.ConvertEntityAttributeToTemporalTypeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain.RelationAttachKindsConfigurationView;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.RelationKindCorrelationInfoChart;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.queryRelationKind.RelationKindQueryUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.RelationKind;

@Route("relationKindDetailInfo/:relationKind")
public class RelationKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver,
        RelationKindConfigurationInfoRefreshEvent.RelationKindConfigurationInfoRefreshListener,
        RelationKindCleanedEvent.RelationKindCleanedListener,
        RelationEntitiesCountRefreshEvent.RelationEntitiesCountRefreshListener,
        KindScopeAttributeAddedEvent.KindScopeAttributeAddedListener {

    private String relationKind;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private int relationKindDetailViewHeightOffset = 110;
    private Registration listener;
    private int currentBrowserHeight = 0;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private Grid<KindEntityAttributeRuntimeStatistics> relationKindAttributesInfoGrid;
    private TabSheet kindConfigurationTabSheet;
    private TabSheet kindCorrelationInfoTabSheet;
    private Tab conceptionRealTimeInfoTab;
    private Tab conceptionRealTimeChartTab;
    private VerticalLayout conceptionKindCorrelationInfoGridContainer;
    private VerticalLayout conceptionKindCorrelationInfoChartContainer;
    private RelationKindCorrelationInfoChart relationKindCorrelationInfoChart;
    private Grid<ConceptionKindCorrelationInfo> relationKindRelationRealtimeInfoGrid;
    private boolean conceptionRealTimeInfoGridFirstLoaded = false;
    private boolean conceptionRealTimeChartFirstLoaded = false;
    private RelationAttachKindsConfigurationView relationAttachKindsConfigurationView;

    public RelationKindDetailUI(){}

    public RelationKindDetailUI(String relationKind){
        this.relationKind = relationKind;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.relationKind = beforeEnterEvent.getRouteParameters().get("relationKind").get();
        this.relationKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderRelationKindData();
        loadRelationKindInfoData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int chartHeight = currentBrowserHeight - relationKindDetailViewHeightOffset - 340;
            this.relationKindRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
            this.relationAttachKindsConfigurationView.setViewWidth(event.getWidth()-820);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            kindCorrelationInfoTabSheet.setHeight(currentBrowserHeight-relationKindDetailViewHeightOffset-290,Unit.PIXELS);
            relationAttachKindsConfigurationView.setViewHeight(currentBrowserHeight-relationKindDetailViewHeightOffset-70);
            relationAttachKindsConfigurationView.setViewWidth(receiver.getBodyClientWidth()-820);
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

    private void renderRelationKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();

        NativeLabel relationKindNameLabel = new NativeLabel(this.relationKind);
        relationKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(relationKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.relationKind,RelationKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button queryRelationKindButton= new Button("关系类型实体数据查询");
        queryRelationKindButton.setIcon(VaadinIcon.RECORDS.create());
        queryRelationKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        queryRelationKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelationKindQueryUI();
            }
        });
        buttonList.add(queryRelationKindButton);

        HorizontalLayout queryEntityByUIDContainerLayout = new HorizontalLayout();
        TextField uidSearchTextField = new TextField();
        uidSearchTextField.setTooltipText("输入唯一值ID检索关系实体");
        uidSearchTextField.setWidth(140,Unit.PIXELS);
        uidSearchTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        Icon uidSearchIcon = VaadinIcon.KEY_O.create();
        uidSearchIcon.addClassNames("text-tertiary");
        uidSearchTextField.setPrefixComponent(uidSearchIcon);
        Button queryRelationEntityByUIDButton= new Button();
        queryRelationEntityByUIDButton.setIcon(VaadinIcon.SEARCH.create());
        queryRelationEntityByUIDButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        queryRelationEntityByUIDButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                searchRelationEntityByUID(uidSearchTextField.getValue());
            }
        });
        uidSearchTextField.setSuffixComponent(queryRelationEntityByUIDButton);
        uidSearchTextField.setPlaceholder("Entity UID");
        queryEntityByUIDContainerLayout.add(uidSearchTextField);
        buttonList.add(queryEntityByUIDContainerLayout);

        Icon divIcon0 = VaadinIcon.LINE_V.create();
        divIcon0.setSize("8px");
        buttonList.add(divIcon0);

        Button relationKindMetaInfoButton= new Button("关系类型元数据");
        relationKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        relationKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        relationKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });
        buttonList.add(relationKindMetaInfoButton);

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        buttonList.add(divIcon);

        Button refreshRelationKindConfigInfoButton= new Button("刷新关系类型配置信息");
        refreshRelationKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshRelationKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshRelationKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                RelationKindConfigurationInfoRefreshEvent relationKindConfigurationInfoRefreshEvent = new RelationKindConfigurationInfoRefreshEvent();
                relationKindConfigurationInfoRefreshEvent.setRelationKindName(relationKind);
                ResourceHolder.getApplicationBlackboard().fire(relationKindConfigurationInfoRefreshEvent);
            }
        });
        buttonList.add(refreshRelationKindConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONNECT_O),"Relation Kind 关系类型  ",secTitleElementsList,buttonList);
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

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"关系类型属性分布 (实体概略采样数 "+10000+")");
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

            actionOptionsSubItems.addSeparator();

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
                    renderDeleteRelationKindAttributeView(attributeInfo.getAttributeName());
                }
            });

            HorizontalLayout action2_1Layout = new HorizontalLayout();
            action2_1Layout.setPadding(false);
            action2_1Layout.setSpacing(false);
            action2_1Layout.setMargin(false);
            action2_1Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action2_1Icon = new Icon((VaadinIcon.COPY));
            action2_1Icon.setSize("10px");
            Span action2_Space = new Span();
            action2_Space.setWidth(6,Unit.PIXELS);
            NativeLabel action2_1Label = new NativeLabel("复制属性");
            action2_1Label.addClassNames("text-xs","font-semibold","text-secondary");
            action2_1Layout.add(action2_1Icon,action2_Space,action2_1Label);
            MenuItem action2_1Item = actionOptionsSubItems.addItem(action2_1Layout);
            action2_1Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    renderDuplicateRelationKindAttributeView(attributeInfo.getAttributeName());
                }
            });

            actionOptionsSubItems.addSeparator();

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
                NativeLabel action3Label = new NativeLabel("转为 LONG 类型");
                action3Label.addClassNames("text-xs","font-semibold","text-secondary");
                action3Layout.add(action3Icon,action3Space,action3Label);
                MenuItem action3Item = containerAction3Item.getSubMenu().addItem(action3Layout);
                action3Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        renderConvertAttributeToIntView(attributeInfo.getAttributeName());
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
                NativeLabel action4Label = new NativeLabel("转为 DOUBLE 类型");
                action4Label.addClassNames("text-xs","font-semibold","text-secondary");
                action4Layout.add(action4Icon,action4Space,action4Label);
                MenuItem action4Item = containerAction3Item.getSubMenu().addItem(action4Layout);
                action4Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        renderConvertAttributeToFloatView(attributeInfo.getAttributeName());
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
                        renderConvertAttributeToBooleanView(attributeInfo.getAttributeName());
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
                        renderConvertAttributeToStringView(attributeInfo.getAttributeName());
                    }
                });
            }

            if(attributeDataType.equals(AttributeDataType.STRING)){
                if(!attributeDataType.equals(AttributeDataType.DATE)){
                    HorizontalLayout action6Layout = new HorizontalLayout();
                    action6Layout.setPadding(false);
                    action6Layout.setSpacing(false);
                    action6Layout.setMargin(false);
                    action6Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    Icon action6Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                    action6Icon.setSize("10px");
                    Span action6Space = new Span();
                    action6Space.setWidth(6,Unit.PIXELS);
                    NativeLabel action6Label = new NativeLabel("转为 DATE 类型");
                    action6Label.addClassNames("text-xs","font-semibold","text-secondary");
                    action6Layout.add(action6Icon,action6Space,action6Label);
                    MenuItem action6Item = containerAction3Item.getSubMenu().addItem(action6Layout);
                    action6Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                        @Override
                        public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                            renderConvertAttributeToDateView(attributeInfo.getAttributeName());
                        }
                    });
                }
                if(!attributeDataType.equals(AttributeDataType.TIME)){
                    HorizontalLayout action6Layout = new HorizontalLayout();
                    action6Layout.setPadding(false);
                    action6Layout.setSpacing(false);
                    action6Layout.setMargin(false);
                    action6Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    Icon action6Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                    action6Icon.setSize("10px");
                    Span action6Space = new Span();
                    action6Space.setWidth(6,Unit.PIXELS);
                    NativeLabel action6Label = new NativeLabel("转为 TIME 类型");
                    action6Label.addClassNames("text-xs","font-semibold","text-secondary");
                    action6Layout.add(action6Icon,action6Space,action6Label);
                    MenuItem action6Item = containerAction3Item.getSubMenu().addItem(action6Layout);
                    action6Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                        @Override
                        public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                            renderConvertAttributeToTimeView(attributeInfo.getAttributeName());
                        }
                    });
                }
                if(!attributeDataType.equals(AttributeDataType.DATETIME)){
                    HorizontalLayout action6Layout = new HorizontalLayout();
                    action6Layout.setPadding(false);
                    action6Layout.setSpacing(false);
                    action6Layout.setMargin(false);
                    action6Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    Icon action6Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                    action6Icon.setSize("10px");
                    Span action6Space = new Span();
                    action6Space.setWidth(6,Unit.PIXELS);
                    NativeLabel action6Label = new NativeLabel("转为 DATETIME 类型");
                    action6Label.addClassNames("text-xs","font-semibold","text-secondary");
                    action6Layout.add(action6Icon,action6Space,action6Label);
                    MenuItem action6Item = containerAction3Item.getSubMenu().addItem(action6Layout);
                    action6Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                        @Override
                        public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                            renderConvertAttributeToDateTimeView(attributeInfo.getAttributeName());
                        }
                    });
                }
                if(!attributeDataType.equals(AttributeDataType.TIMESTAMP)){
                    HorizontalLayout action6Layout = new HorizontalLayout();
                    action6Layout.setPadding(false);
                    action6Layout.setSpacing(false);
                    action6Layout.setMargin(false);
                    action6Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    Icon action6Icon = LineAwesomeIconsSvg.FIRSTDRAFT.create();
                    action6Icon.setSize("10px");
                    Span action6Space = new Span();
                    action6Space.setWidth(6,Unit.PIXELS);
                    NativeLabel action6Label = new NativeLabel("转为 TIMESTAMP 类型");
                    action6Label.addClassNames("text-xs","font-semibold","text-secondary");
                    action6Layout.add(action6Icon,action6Space,action6Label);
                    MenuItem action6Item = containerAction3Item.getSubMenu().addItem(action6Layout);
                    action6Item.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                        @Override
                        public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                            renderConvertAttributeToTimeStampView(attributeInfo.getAttributeName());
                        }
                    });
                }
            }
            return actionsMenuBar;
        });

        relationKindAttributesInfoGrid = new Grid<>();
        relationKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        relationKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        relationKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        relationKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        relationKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);
        relationKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getSampleCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getSampleCount() - entityStatisticsInfo2.getSampleCount()))
                .setHeader("属性采样数").setKey("idx_2")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        relationKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getAttributeHitCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性命中数").setKey("idx_3")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        relationKindAttributesInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("60px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        relationKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> getAttributeName(kindEntityAttributeRuntimeStatistics));
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        relationKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.EYEDROPPER,"属性采样数");
        relationKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        relationKindAttributesInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationKindAttributesInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);

        relationKindAttributesInfoGrid.setHeight(218,Unit.PIXELS);
        leftSideContainerLayout.add(relationKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.RANDOM),"关系类型实体关联流向");
        infoTitle2.getStyle().set("padding-top","20px");

        leftSideContainerLayout.add(infoTitle2);

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
        NativeLabel relationInfoLabel = new NativeLabel(" 关联流向实时分布");
        relationInfoSpan.add(relationInfoIcon,relationInfoLabel);
        conceptionRealTimeInfoTab.add(relationInfoSpan);

        conceptionRealTimeChartTab = kindCorrelationInfoTabSheet.add("",conceptionKindCorrelationInfoChartContainer);
        Span chartInfoSpan =new Span();
        Icon chartInfoIcon = new Icon(VaadinIcon.ROAD_BRANCHES);
        chartInfoIcon.setSize("12px");
        NativeLabel chartInfoLabel = new NativeLabel(" 关联流向实时分布桑基图");
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

        RelationKindEntitiesConfigurationView relationKindEntitiesConfigurationView = new RelationKindEntitiesConfigurationView(this.relationKind);
        relationAttachKindsConfigurationView = new RelationAttachKindsConfigurationView(RelationAttachKindsConfigurationView.RelatedKindType.RelationKind,this.relationKind);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.SPARK_LINE,"关系类型运行时配置"),relationKindEntitiesConfigurationView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TREE_TABLE,"关联关系规则配置"),relationAttachKindsConfigurationView);
        //kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TASKS,"属性视图配置"),new HorizontalLayout());
    }

    private void loadRelationKindInfoData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetRelationKind.statisticEntityAttributesDistribution(10000);
        coreRealm.closeGlobalSession();
        relationKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
    }

    private String getAttributeName(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics){
        return kindEntityAttributeRuntimeStatistics.getAttributeName();
    }

    private void renderKindCorrelationInfoTabContent() {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(relationKind);
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoList = targetRelationKind.getConceptionKindsRelationStatistics();
        if (conceptionRealTimeInfoTab.isSelected()) {
            if(!this.conceptionRealTimeInfoGridFirstLoaded){
                int chartHeight = currentBrowserHeight - relationKindDetailViewHeightOffset - 340;
                initConceptionRelationRealtimeInfoGrid(conceptionKindCorrelationInfoList);
                this.relationKindRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
                this.conceptionKindCorrelationInfoGridContainer.add(this.relationKindRelationRealtimeInfoGrid);
                this.conceptionRealTimeInfoGridFirstLoaded = true;
            }else{
                refreshConceptionRelationRealtimeInfoGrid(conceptionKindCorrelationInfoList);
            }
        } else if (conceptionRealTimeChartTab.isSelected()) {
            if(!this.conceptionRealTimeChartFirstLoaded){
                int chartHeight = currentBrowserHeight - this.relationKindDetailViewHeightOffset - 340;
                relationKindCorrelationInfoChart = new RelationKindCorrelationInfoChart(chartHeight);
                conceptionKindCorrelationInfoChartContainer.add(relationKindCorrelationInfoChart);
                relationKindCorrelationInfoChart.clearData();
                relationKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoList);
                this.conceptionRealTimeChartFirstLoaded = true;
            }else{
                relationKindCorrelationInfoChart.clearData();
                relationKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoList);
            }
        }else{}
    }

    private void initConceptionRelationRealtimeInfoGrid(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        this.relationKindRelationRealtimeInfoGrid = new Grid<>();
        this.relationKindRelationRealtimeInfoGrid.setWidth(99,Unit.PERCENTAGE);
        this.relationKindRelationRealtimeInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.relationKindRelationRealtimeInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        this.relationKindRelationRealtimeInfoGrid.addColumn(ConceptionKindCorrelationInfo::getSourceConceptionKindName).setHeader("源概念类型").setKey("idx_0");
        this.relationKindRelationRealtimeInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_1").setFlexGrow(0).setWidth("35px").setResizable(false);
        this.relationKindRelationRealtimeInfoGrid.addColumn(ConceptionKindCorrelationInfo::getTargetConceptionKindName).setHeader("目标概念类型").setKey("idx_2");
        this.relationKindRelationRealtimeInfoGrid.addColumn(ConceptionKindCorrelationInfo::getRelationEntityCount).setHeader("关系实体数量").setKey("idx_3");
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CUBE,"源概念类型");
        relationKindRelationRealtimeInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true)
                .setTooltipGenerator(ConceptionKindCorrelationInfo::getSourceConceptionKindName);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CUBE,"目标概念类型");
        relationKindRelationRealtimeInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true)
                .setTooltipGenerator(ConceptionKindCorrelationInfo::getTargetConceptionKindName);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.STOCK,"关系实体数量");
        relationKindRelationRealtimeInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        this.relationKindRelationRealtimeInfoGrid.setItems(conceptionKindCorrelationInfoSet);
    }

    private void refreshConceptionRelationRealtimeInfoGrid(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        ListDataProvider dtaProvider= (ListDataProvider)this.relationKindRelationRealtimeInfoGrid.getDataProvider();
        dtaProvider.getItems().clear();
        dtaProvider.getItems().addAll(conceptionKindCorrelationInfoSet);
        dtaProvider.refreshAll();
    }

    @Override
    public void receivedRelationKindConfigurationInfoRefreshEvent(RelationKindConfigurationInfoRefreshEvent event) {
        relationAttachKindsConfigurationView.refreshRelationAttachKindsInfo();
        loadRelationKindInfoData();
        renderKindCorrelationInfoTabContent();
    }

    @Override
    public void receivedRelationKindCleanedEvent(RelationKindCleanedEvent event) {

    }

    @Override
    public void receivedRelationEntitiesCountRefreshEvent(RelationEntitiesCountRefreshEvent event) {

    }

    @Override
    public void receivedKindScopeAttributeAddedEvent(KindScopeAttributeAddedEvent event) {
        if(this.relationKind.equals(event.getKindName()) && AddEntityAttributeView.KindType.RelationKind.equals(event.getKindType())){
            loadRelationKindInfoData();
        }
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<ConceptionKindCorrelationInfo,Icon> {
        @Override
        public Icon apply(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo) {
            conceptionKindCorrelationInfo.getSourceConceptionKindName();
            Icon relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
            relationDirectionIcon.setSize("14px");
            return relationDirectionIcon;
        }
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

    private void renderRelationKindQueryUI(){
        RelationKindQueryUI relationKindQueryUI = new RelationKindQueryUI(this.relationKind);
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

        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("10px");
        titleDetailLayout.add(relationKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel relationKindName = new NativeLabel(this.relationKind);
        titleDetailLayout.add(relationKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系类型实体数据查询",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindQueryUI);
        fullScreenWindow.show();
    }

    private void renderShowMetaInfoUI(){
        RelationKindMetaInfoView relationKindMetaInfoView = new RelationKindMetaInfoView(this.relationKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"关系类型元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(relationKindMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderSampleRandomAttributesView(String attributeName){
        AttributesValueListView attributesValueListView = new AttributesValueListView(AttributesValueListView.AttributeKindType.RelationKind,this.relationKind,attributeName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.EYE_DROPPER_SOLID.create(),"属性值随机采样 (100项)",null,true,500,510,false);
        fixSizeWindow.setWindowContent(attributesValueListView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderDeleteRelationKindAttributeView(String attributeName){
        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认删除属性",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行删除属性操作，该操作将从关系类型 "+this.relationKind+" 的所有实体中删除属性 "+attributeName,actionButtonList,500,200);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDeleteRelationKindAttribute(attributeName);
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDeleteRelationKindAttribute(String attributeName){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        Set<String> attributeForRemoveSet = new HashSet<>();
        attributeForRemoveSet.add(attributeName);
        try {
            EntitiesOperationStatistics resultEntitiesOperationStatistics = targetRelationKind.removeEntityAttributes(attributeForRemoveSet);
            String notificationMessage = "从关系类型 "+this.relationKind+" 中删除实体属性 "+attributeName+" 操作成功";
            showPopupNotification(notificationMessage,resultEntitiesOperationStatistics, NotificationVariant.LUMO_SUCCESS);
            loadRelationKindInfoData();
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPopupNotification(String notificationMessage,EntitiesOperationStatistics conceptionEntitiesAttributesRetrieveResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text(notificationMessage));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setFlexGrow(1,text);
        notification.add(layout);

        VerticalLayout notificationMessageContainer = new VerticalLayout();
        notificationMessageContainer.add(new Div(new Text("操作成功实体数: "+conceptionEntitiesAttributesRetrieveResult.getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作失败实体数: "+conceptionEntitiesAttributesRetrieveResult.getFailItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+conceptionEntitiesAttributesRetrieveResult.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+conceptionEntitiesAttributesRetrieveResult.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(10000);
        notification.open();
    }

    private void renderConvertAttributeToIntView(String attributeName){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认转换数据类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行转换属性数据类型操作，该操作将关系类型 "+this.relationKind+" 所有实体的属性 "+attributeName +" 转换为 LONG 类型,类型无法转换的属性将被删除",actionButtonList,550,200);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConvertAttributeToInt(attributeName);
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doConvertAttributeToInt(String attributeName){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        EntitiesOperationStatistics entitiesOperationStatistics = targetRelationKind.convertEntityAttributeToIntType(attributeName);
        String notificationMessage = "将关系类型 "+this.relationKind+" 的实体属性 "+attributeName+" 转换为 LONG 类型操作成功";
        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
        loadRelationKindInfoData();
    }

    private void renderConvertAttributeToFloatView(String attributeName){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认转换数据类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行转换属性数据类型操作，该操作将关系类型 "+this.relationKind+" 所有实体的属性 "+attributeName +" 转换为 DOUBLE 类型,类型无法转换的属性将被删除",actionButtonList,550,200);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConvertAttributeToFloat(attributeName);
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doConvertAttributeToFloat(String attributeName){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        EntitiesOperationStatistics entitiesOperationStatistics = targetRelationKind.convertEntityAttributeToFloatType(attributeName);
        String notificationMessage = "将关系类型 "+this.relationKind+" 的实体属性 "+attributeName+" 转换为 DOUBLE 类型操作成功";
        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
        loadRelationKindInfoData();
    }

    private void renderConvertAttributeToBooleanView(String attributeName){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认转换数据类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行转换属性数据类型操作，该操作将关系类型 "+this.relationKind+" 所有实体的属性 "+attributeName +" 转换为 BOOLEAN 类型,类型无法转换的属性将被删除",actionButtonList,550,200);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConvertAttributeToBoolean(attributeName);
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doConvertAttributeToBoolean(String attributeName){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        EntitiesOperationStatistics entitiesOperationStatistics = targetRelationKind.convertEntityAttributeToBooleanType(attributeName);
        String notificationMessage = "将关系类型 "+this.relationKind+" 的实体属性 "+attributeName+" 转换为 BOOLEAN 类型操作成功";
        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
        loadRelationKindInfoData();
    }

    private void renderConvertAttributeToStringView(String attributeName){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认转换数据类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行转换属性数据类型操作，该操作将关系类型 "+this.relationKind+" 所有实体的属性 "+attributeName +" 转换为 STRING 类型,类型无法转换的属性将被删除",actionButtonList,550,200);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConvertAttributeToString(attributeName);
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doConvertAttributeToString(String attributeName){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        EntitiesOperationStatistics entitiesOperationStatistics = targetRelationKind.convertEntityAttributeToStringType(attributeName);
        String notificationMessage = "将关系类型 "+this.relationKind+" 的实体属性 "+attributeName+" 转换为 STRING 类型操作成功";
        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
        loadRelationKindInfoData();
    }

    private void renderConvertAttributeToDateView(String attributeName){
        ConvertEntityAttributeToTemporalTypeView convertEntityAttributeToTemporalTypeView = new ConvertEntityAttributeToTemporalTypeView(
                ConvertEntityAttributeToTemporalTypeView.KindType.RelationKind,this.relationKind,attributeName, TemporalScaleCalculable.TemporalScaleLevel.Date);

        ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback =
                new ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback() {
                    @Override
                    public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics) {
                        String notificationMessage = "将关系类型 "+ relationKind+ " 的实体属性 "+attributeName+" 转换为 DATE 类型操作成功";
                        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
                        loadRelationKindInfoData();
                    }
                };
        convertEntityAttributeToTemporalTypeView.setConvertEntityAttributeToTemporalTypeCallback(convertEntityAttributeToTemporalTypeCallback);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关系类型实体属性类型转换 String -> DATE",null,true,500,255,false);
        fixSizeWindow.setWindowContent(convertEntityAttributeToTemporalTypeView);
        convertEntityAttributeToTemporalTypeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderConvertAttributeToTimeView(String attributeName){
        ConvertEntityAttributeToTemporalTypeView convertEntityAttributeToTemporalTypeView = new ConvertEntityAttributeToTemporalTypeView(
                ConvertEntityAttributeToTemporalTypeView.KindType.RelationKind,this.relationKind,attributeName,TemporalScaleCalculable.TemporalScaleLevel.Time);

        ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback =
                new ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback() {
                    @Override
                    public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics) {
                        String notificationMessage = "将关系类型 "+ relationKind+ " 的实体属性 "+attributeName+" 转换为 TIME 类型操作成功";
                        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
                        loadRelationKindInfoData();
                    }
                };
        convertEntityAttributeToTemporalTypeView.setConvertEntityAttributeToTemporalTypeCallback(convertEntityAttributeToTemporalTypeCallback);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关系类型实体属性类型转换 String -> TIME",null,true,500,255,false);
        fixSizeWindow.setWindowContent(convertEntityAttributeToTemporalTypeView);
        convertEntityAttributeToTemporalTypeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderConvertAttributeToDateTimeView(String attributeName){
        ConvertEntityAttributeToTemporalTypeView convertEntityAttributeToTemporalTypeView = new ConvertEntityAttributeToTemporalTypeView(
                ConvertEntityAttributeToTemporalTypeView.KindType.RelationKind,this.relationKind,attributeName,TemporalScaleCalculable.TemporalScaleLevel.Datetime);

        ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback =
                new ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback() {
                    @Override
                    public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics) {
                        String notificationMessage = "将关系类型 "+ relationKind+ " 的实体属性 "+attributeName+" 转换为 DATETIME 类型操作成功";
                        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
                        loadRelationKindInfoData();
                    }
                };
        convertEntityAttributeToTemporalTypeView.setConvertEntityAttributeToTemporalTypeCallback(convertEntityAttributeToTemporalTypeCallback);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关系类型实体属性类型转换 String -> DATETIME",null,true,500,255,false);
        fixSizeWindow.setWindowContent(convertEntityAttributeToTemporalTypeView);
        convertEntityAttributeToTemporalTypeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderConvertAttributeToTimeStampView(String attributeName){
        ConvertEntityAttributeToTemporalTypeView convertEntityAttributeToTemporalTypeView = new ConvertEntityAttributeToTemporalTypeView(
                ConvertEntityAttributeToTemporalTypeView.KindType.RelationKind,this.relationKind,attributeName,TemporalScaleCalculable.TemporalScaleLevel.Timestamp);

        ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback =
                new ConvertEntityAttributeToTemporalTypeView.ConvertEntityAttributeToTemporalTypeCallback() {
                    @Override
                    public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics) {
                        String notificationMessage = "将关系类型 "+ relationKind+ " 的实体属性 "+attributeName+" 转换为 TIMESTAMP 类型操作成功";
                        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
                        loadRelationKindInfoData();
                    }
                };
        convertEntityAttributeToTemporalTypeView.setConvertEntityAttributeToTemporalTypeCallback(convertEntityAttributeToTemporalTypeCallback);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关系类型实体属性类型转换 String -> TIMESTAMP",null,true,500,255,false);
        fixSizeWindow.setWindowContent(convertEntityAttributeToTemporalTypeView);
        convertEntityAttributeToTemporalTypeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderDuplicateRelationKindAttributeView(String attributeName){
        DuplicateAttributeView duplicateAttributeView = new DuplicateAttributeView(
                DuplicateAttributeView.KindType.RelationKind,
                this.relationKind,attributeName);
        DuplicateAttributeView.DuplicateAttributeCallback duplicateAttributeCallback = new DuplicateAttributeView.DuplicateAttributeCallback() {
            @Override
            public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics) {
                String notificationMessage = "复制关系类型 "+ relationKind+ " 的实体属性 "+attributeName+" 操作成功";
                showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
                loadRelationKindInfoData();
            }
        };
        duplicateAttributeView.setDuplicateAttributeCallback(duplicateAttributeCallback);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"复制关系类型实体属性",null,true,550,210,false);
        fixSizeWindow.setWindowContent(duplicateAttributeView);
        duplicateAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void searchRelationEntityByUID(String relationEntityUID){
        if(relationEntityUID == null || relationEntityUID.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入关系实体唯一值ID",NotificationVariant.LUMO_WARNING,0, Notification.Position.TOP_END);
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
            RelationEntity targetRelationEntity = targetRelationKind.getEntityByUID(relationEntityUID);
            coreRealm.closeGlobalSession();
            if(targetRelationEntity == null){
                CommonUIOperationUtil.showPopupNotification("关系类型 "+this.relationKind+" 中不存在UID为 "+relationEntityUID+" 的关系实体",NotificationVariant.LUMO_ERROR,0, Notification.Position.TOP_END);
            }else{
                RelationEntityDetailUI relationEntityDetailUI = new RelationEntityDetailUI(this.relationKind,relationEntityUID);

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

                Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
                relationKindIcon.setSize("10px");
                titleDetailLayout.add(relationKindIcon);
                HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
                spaceDivLayout2.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout2);
                NativeLabel conceptionKindNameLabel = new NativeLabel(this.relationKind);
                titleDetailLayout.add(conceptionKindNameLabel);

                HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
                spaceDivLayout3.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout3);

                Icon divIcon = VaadinIcon.ITALIC.create();
                divIcon.setSize("8px");
                titleDetailLayout.add(divIcon);

                HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
                spaceDivLayout4.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout4);

                Icon relationEntityIcon = VaadinIcon.KEY_O.create();
                relationEntityIcon.setSize("10px");
                titleDetailLayout.add(relationEntityIcon);

                HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
                spaceDivLayout5.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout5);
                NativeLabel relationEntityUIDLabel = new NativeLabel(relationEntityUID);
                titleDetailLayout.add(relationEntityUIDLabel);

                actionComponentList.add(titleDetailLayout);

                FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系实体详情",actionComponentList,null,true);
                fullScreenWindow.setWindowContent(relationEntityDetailUI);
                relationEntityDetailUI.setContainerDialog(fullScreenWindow);
                fullScreenWindow.show();
            }
        }
    }
}
