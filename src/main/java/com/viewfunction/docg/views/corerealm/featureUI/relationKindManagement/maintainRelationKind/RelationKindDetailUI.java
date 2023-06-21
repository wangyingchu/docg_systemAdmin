package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.RelationKindConfigurationInfoRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.queryRelationKind.RelationKindQueryUI;

import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.RelationKind;

public class RelationKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String relationKind;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private int relationKindDetailViewHeightOffset = 110;
    private Registration listener;
    private int currentBrowserHeight = 0;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private Grid<KindEntityAttributeRuntimeStatistics> relationKindAttributesInfoGrid;
    private TabSheet kindConfigurationTabSheet;

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
            //conceptionKindCorrelationInfoChart.setHeight(chartHeight, Unit.PIXELS);
            //this.conceptionRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            //kindCorrelationInfoTabSheet.setHeight(currentBrowserHeight-relationKindDetailViewHeightOffset-290,Unit.PIXELS);
        }));
       // renderKindCorrelationInfoTabContent();
        //ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderRelationKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();

        Label relationKindNameLabel = new Label(this.relationKind);
        relationKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(relationKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.relationKind,RelationKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

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
            Icon queryIcon = new Icon(VaadinIcon.INPUT);
            queryIcon.setSize("20px");
            Button addAsAttributeKind = new Button(queryIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            addAsAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addAsAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(addAsAttributeKind, "添加为属性类型");

            HorizontalLayout buttons = new HorizontalLayout(addAsAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        relationKindAttributesInfoGrid = new Grid<>();
        relationKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        relationKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        relationKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        relationKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        relationKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);
        relationKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getSampleCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
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

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT),"概念类型实体关联分布");
        infoTitle2.getStyle().set("padding-top","20px");













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
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.SPARK_LINE,"概念类型运行时配置"),relationKindEntitiesConfigurationView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TREE_TABLE,"关联关系规则配置"),new HorizontalLayout());
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TASKS,"属性视图配置"),new HorizontalLayout());
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

    private HorizontalLayout generateKindConfigurationTabTitle(VaadinIcon tabIcon,String tabTitleTxt){
        HorizontalLayout  kindConfigTabLayout = new HorizontalLayout();
        kindConfigTabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kindConfigTabLayout.setHeight(26,Unit.PIXELS);
        Icon configTabIcon = new Icon(tabIcon);
        configTabIcon.setSize("12px");
        Label configTabLabel = new Label(" "+tabTitleTxt);
        configTabLabel.getStyle()
                . set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        kindConfigTabLayout.add(configTabIcon,configTabLabel);
        return kindConfigTabLayout;
    }

    private void renderRelationKindQueryUI(){
        RelationKindQueryUI relationKindQueryUI = new RelationKindQueryUI(this.relationKind);
        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("12px");
        relationKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(relationKindIcon);
        Label relationKindName = new Label(this.relationKind);
        actionComponentList.add(relationKindName);

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
}
