package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.*;

import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.ConceptionKindQueryUI;

import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.ConceptionKind;

@Route("conceptionKindDetailInfo/:conceptionKind")
public class ConceptionKindDetailUI extends VerticalLayout implements BeforeEnterObserver {
    private String conceptionKind;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private int conceptionKindDetailViewHeightOffset = 135;
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

    public ConceptionKindDetailUI(){}

    public ConceptionKindDetailUI(String conceptionKind){
        this.conceptionKind = conceptionKind;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.conceptionKind = beforeEnterEvent.getRouteParameters().get("conceptionKind").get();
        this.conceptionKindDetailViewHeightOffset = 70;
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
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            kindCorrelationInfoTabSheet.setHeight(currentBrowserHeight-conceptionKindDetailViewHeightOffset-290,Unit.PIXELS);
        }));
        renderKindCorrelationInfoTabContent();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderConceptionKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();

        Label conceptionKindNameLabel = new Label(this.conceptionKind);
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
        Label relationInfoLabel = new Label(" 概念关联实时分布");
        relationInfoSpan.add(relationInfoIcon,relationInfoLabel);
        conceptionRealTimeInfoTab.add(relationInfoSpan);

        conceptionRealTimeChartTab = kindCorrelationInfoTabSheet.add("",conceptionKindCorrelationInfoChartContainer);
        Span chartInfoSpan =new Span();
        Icon chartInfoIcon = new Icon(VaadinIcon.SPLIT);
        chartInfoIcon.setSize("12px");
        Label chartInfoLabel = new Label(" 概念关联实时分布网络图");
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

        ConceptionKindEntitiesConfigurationView conceptionKindEntitiesConfigurationView = new ConceptionKindEntitiesConfigurationView(this.conceptionKind);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.SPARK_LINE,"概念类型运行时配置"),conceptionKindEntitiesConfigurationView);
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TREE_TABLE,"关联关系规则配置"),new HorizontalLayout());
        kindConfigurationTabSheet.add(generateKindConfigurationTabTitle(VaadinIcon.TASKS,"属性视图配置"),new HorizontalLayout());
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
        Label configTabLabel = new Label(" "+tabTitleTxt);
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
            if(!this.conceptionRealTimeInfoGridFirstLoaded){
                int chartHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset - 340;
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
                Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
                initConceptionRelationRealtimeInfoGrid(conceptionKindCorrelationInfoSet);
                this.conceptionRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
                this.conceptionKindCorrelationInfoGridContainer.add(this.conceptionRelationRealtimeInfoGrid);
                this.conceptionRealTimeInfoGridFirstLoaded = true;
            }
        }else if(conceptionRealTimeChartTab.isSelected()){
            if(!this.conceptionRealTimeChartFirstLoaded){
                int chartHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset - 340;
                conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(chartHeight);
                conceptionKindCorrelationInfoChartContainer.add(conceptionKindCorrelationInfoChart);
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
                Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
                conceptionKindCorrelationInfoChart.clearData();
                conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,conceptionKind);
                this.conceptionRealTimeChartFirstLoaded = true;
            }
        }else{}
    }

    private void initConceptionRelationRealtimeInfoGrid(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        this.conceptionRelationRealtimeInfoGrid = new Grid<>();
        this.conceptionRelationRealtimeInfoGrid.setWidth(100,Unit.PERCENTAGE);
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
            Label displayConceptionKind = null;
            if(conceptionKind.equals(fromConceptionKind)){
                displayConceptionKind = new Label(toConceptionKind);
            }
            if(conceptionKind.equals(toConceptionKind)){
                displayConceptionKind = new Label(fromConceptionKind);
            }
            conceptionKindContainerLayout.add(displayConceptionKind);
            return conceptionKindContainerLayout;
        }
    }

    private void renderConceptionKindQueryUI(){
        ConceptionKindQueryUI conceptionKindQueryUI = new ConceptionKindQueryUI(this.conceptionKind);
        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(conceptionKindIcon);
        Label conceptionKindName = new Label(this.conceptionKind);
        actionComponentList.add(conceptionKindName);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念类型实体数据查询",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionKindQueryUI);
        fullScreenWindow.show();
    }

    private void renderShowMetaInfoUI(){
        ConceptionKindMetaInfoView conceptionKindMetaInfoView = new ConceptionKindMetaInfoView(this.conceptionKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"概念类型元数据信息",null,true,500,380,false);
        fixSizeWindow.setWindowContent(conceptionKindMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}