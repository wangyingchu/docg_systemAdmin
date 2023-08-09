package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

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
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.AttributeInConceptionKindDistributionInfoChart;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain.AttributesValueListView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route("attributeKindDetailInfo/:attributeKindUID")
public class AttributeKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String attributeKindUID;
    private String attributeKindName;
    private AttributeDataType attributeDataType;
    private int attributeKindDetailViewHeightOffset = 110;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private Grid<AttributeInConceptionKindDistributionInfo> conceptionKindContainsAttributeInfoGrid;
    private AttributeInConceptionKindDistributionInfoChart attributeInConceptionKindDistributionInfoChart;
    private TabSheet kindConfigurationTabSheet;

    private class AttributeInConceptionKindDistributionInfo{
        private String conceptionKindName;
        private String conceptionKindDesc;
        private long conceptionEntityCount;
        private long propertyExistInEntityCount;

        public String getConceptionKindName() {
            return conceptionKindName;
        }

        public void setConceptionKindName(String conceptionKindName) {
            this.conceptionKindName = conceptionKindName;
        }

        public String getConceptionKindDesc() {
            return conceptionKindDesc;
        }

        public void setConceptionKindDesc(String conceptionKindDesc) {
            this.conceptionKindDesc = conceptionKindDesc;
        }

        public long getConceptionEntityCount() {
            return conceptionEntityCount;
        }

        public void setConceptionEntityCount(long conceptionEntityCount) {
            this.conceptionEntityCount = conceptionEntityCount;
        }

        public long getPropertyExistInEntityCount() {
            return propertyExistInEntityCount;
        }

        public void setPropertyExistInEntityCount(long propertyExistInEntityCount) {
            this.propertyExistInEntityCount = propertyExistInEntityCount;
        }
    }

    public AttributeKindDetailUI(){}

    public AttributeKindDetailUI(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributeKindUID = beforeEnterEvent.getRouteParameters().get("attributeKindUID").get();
        this.attributeKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderAttributeKindData();
        loadAttributeKindInfoData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int containerHeight = currentBrowserHeight - attributeKindDetailViewHeightOffset;
            this.conceptionKindContainsAttributeInfoGrid.setHeight(containerHeight-480,Unit.PIXELS);
            //this.containerConceptionKindsConfigView.setHeight(containerHeight);
            //this.containsAttributeKindsConfigView.setHeight(containerHeight);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int containerHeight = currentBrowserHeight - attributeKindDetailViewHeightOffset;
            this.conceptionKindContainsAttributeInfoGrid.setHeight(containerHeight-480,Unit.PIXELS);
            //this.containerConceptionKindsConfigView.setHeight(containerHeight);
            //this.containsAttributeKindsConfigView.setHeight(containerHeight);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderAttributeKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();
        String attributesViewKindDisplayInfo = this.attributeKindUID;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        if(targetAttributeKind != null){
            this.attributeKindName = targetAttributeKind.getAttributeKindName();
            this.attributeDataType = targetAttributeKind.getAttributeDataType();
            attributesViewKindDisplayInfo = this.attributeKindName +" ( "+this.attributeKindUID+" )"+ " - " + this.attributeDataType;
        }
        NativeLabel attributesViewKindNameLabel = new NativeLabel(attributesViewKindDisplayInfo);
        attributesViewKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(attributesViewKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.attributeKindUID, KindDescriptionEditorItemWidget.KindType.AttributeKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button metaConfigItemConfigInfoButton= new Button("元属性配置管理");
        metaConfigItemConfigInfoButton.setIcon(VaadinIcon.BOOKMARK.create());
        metaConfigItemConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        metaConfigItemConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderMetaConfigItemConfigInfoUI();
            }
        });
        buttonList.add(metaConfigItemConfigInfoButton);

        Button classificationConfigInfoButton= new Button("分类配置管理");
        classificationConfigInfoButton.setIcon(VaadinIcon.TAGS.create());
        classificationConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        classificationConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderClassificationConfigInfoUI();
            }
        });
        buttonList.add(classificationConfigInfoButton);

        Button conceptionKindMetaInfoButton= new Button("属性类型元数据");
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

        Button refreshConceptionKindConfigInfoButton= new Button("刷新属性类型配置信息");
        refreshConceptionKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshConceptionKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshConceptionKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //containerConceptionKindsConfigView.refreshConceptionKindsInfo();
                //containsAttributeKindsConfigView.refreshAttributeTypesInfo();
            }
        });
        buttonList.add(refreshConceptionKindConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"Attribute Kind 属性类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(850, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBE),"包含本属性类型的概念类型实体属性分布");
        infoTitle1.getStyle().set("padding-bottom","5px");
        leftSideContainerLayout.add(infoTitle1);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            AttributeInConceptionKindDistributionInfo attributeInfo = (AttributeInConceptionKindDistributionInfo)entityStatisticsInfo;
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
                    renderSampleRandomAttributesView(attributeInfo.conceptionKindName);
                }
            });

            /*
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
                    //renderAddAttributeKindView(attributeInfo);
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
                    //System.out.println(attributeInfo.getAttributeName());
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
            */
            return actionsMenuBar;
        });

        conceptionKindContainsAttributeInfoGrid = new Grid<>();
        conceptionKindContainsAttributeInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindContainsAttributeInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindContainsAttributeInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindContainsAttributeInfoGrid.addColumn(AttributeInConceptionKindDistributionInfo::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindContainsAttributeInfoGrid.addColumn(AttributeInConceptionKindDistributionInfo::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        conceptionKindContainsAttributeInfoGrid.addColumn(new NumberRenderer<>(AttributeInConceptionKindDistributionInfo::getConceptionEntityCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getConceptionEntityCount() - entityStatisticsInfo2.getConceptionEntityCount()))
                .setHeader("类型包含实体数量").setKey("idx_2")
                .setFlexGrow(0).setWidth("120px").setResizable(false);
        conceptionKindContainsAttributeInfoGrid.addColumn(new NumberRenderer<>(AttributeInConceptionKindDistributionInfo::getPropertyExistInEntityCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getPropertyExistInEntityCount() - entityStatisticsInfo2.getPropertyExistInEntityCount()))
                .setHeader("包含当前属性实体数量").setKey("idx_3")
                .setFlexGrow(0).setWidth("120px").setResizable(false);
        conceptionKindContainsAttributeInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("60px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"概念类型名称");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> kindEntityAttributeRuntimeStatistics.conceptionKindName);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"概念类型显示名称");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> kindEntityAttributeRuntimeStatistics.conceptionKindDesc);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.STOCK,"类型实体数量");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性类型命中数量");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);
        conceptionKindContainsAttributeInfoGrid.setHeight(350,Unit.PIXELS);
        leftSideContainerLayout.add(conceptionKindContainsAttributeInfoGrid);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(20,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivLayout1);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.BAR_CHART_H),"属性类型在概念实体中的数据分布");
        leftSideContainerLayout.add(infoTitle2);

        attributeInConceptionKindDistributionInfoChart = new AttributeInConceptionKindDistributionInfoChart();
        attributeInConceptionKindDistributionInfoChart.setChartSize(600,400);
        leftSideContainerLayout.add(attributeInConceptionKindDistributionInfoChart);

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

        AttributeKindRuntimeConfigurationView attributeKindRuntimeConfigurationView = new AttributeKindRuntimeConfigurationView(this.attributeKindUID);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.SPARK_LINE,"属性类型运行时配置"),attributeKindRuntimeConfigurationView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TASKS,"属性视图配置"),new HorizontalLayout());

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+10000+")");
        infoTitle3.getStyle().set("padding-bottom","5px");
        rightSideContainerLayout.add(infoTitle3);
    }

    private void loadAttributeKindInfoData() {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        Map<String,Long> distributionMap = targetAttributeKind.getAttributeInConceptionKindDistributionStatistics();
        Set<String> conceptionNameSet = distributionMap.keySet();
        List<AttributeInConceptionKindDistributionInfo> attributeInConceptionKindDistributionInfoList = new ArrayList<>();
        try {
            List<EntityStatisticsInfo> conceptionKindInfolist = coreRealm.getConceptionEntitiesStatistics();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:conceptionKindInfolist){
                String conceptionKindName = currentEntityStatisticsInfo.getEntityKindName();
                if(conceptionNameSet.contains(conceptionKindName)){
                    AttributeInConceptionKindDistributionInfo currentAttributeInConceptionKindDistributionInfo = new AttributeInConceptionKindDistributionInfo();
                    currentAttributeInConceptionKindDistributionInfo.setConceptionKindName(currentEntityStatisticsInfo.getEntityKindName());
                    currentAttributeInConceptionKindDistributionInfo.setConceptionKindDesc(currentEntityStatisticsInfo.getEntityKindDesc());
                    currentAttributeInConceptionKindDistributionInfo.setConceptionEntityCount(currentEntityStatisticsInfo.getEntitiesCount());
                    currentAttributeInConceptionKindDistributionInfo.setPropertyExistInEntityCount(distributionMap.get(conceptionKindName));
                    attributeInConceptionKindDistributionInfoList.add(currentAttributeInConceptionKindDistributionInfo);
                }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
        conceptionKindContainsAttributeInfoGrid.setItems(attributeInConceptionKindDistributionInfoList);
        attributeInConceptionKindDistributionInfoChart.refreshDistributionInfo(distributionMap);
    }

    private void renderShowMetaInfoUI(){
        AttributeKindMetaInfoView attributeKindMetaInfoView = new AttributeKindMetaInfoView(this.attributeKindUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"属性类型元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(attributeKindMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderMetaConfigItemConfigInfoUI(){
        MetaConfigItemsConfigView metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.AttributeKind,this.attributeKindUID);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.BOOKMARK),"属性类型元属性配置管理",null,true,750,280,false);
        fixSizeWindow.setWindowContent(metaConfigItemsConfigView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderSampleRandomAttributesView(String conceptionKindName){
        AttributesValueListView attributesValueListView = new AttributesValueListView(AttributesValueListView.AttributeKindType.ConceptionKind,conceptionKindName,this.attributeKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.EYE_DROPPER_SOLID.create(),"属性值随机采样 (100项)",null,true,500,510,false);
        fixSizeWindow.setWindowContent(attributesValueListView);
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
}
