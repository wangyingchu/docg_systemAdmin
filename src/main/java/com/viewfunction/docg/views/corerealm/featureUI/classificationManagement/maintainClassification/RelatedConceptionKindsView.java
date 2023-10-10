package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.List;

public class RelatedConceptionKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionKindCountDisplayItem;
    private Grid<ConceptionKind> conceptionKindMetaInfoGrid;
    private ClassificationRelatedDataQueryCriteriaView classificationRelatedDataQueryCriteriaView;

    public RelatedConceptionKindsView(String classificationName){
        this.setPadding(false);
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关概念类型运行时信息");

        filterTitle1.getStyle().set("padding-top","10px");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
               // .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关概念类型数量:","-");

        TabSheet kindCorrelationInfoTabSheet = new TabSheet();
        kindCorrelationInfoTabSheet.setWidthFull();



        VerticalLayout conceptionKindCorrelationInfoGridContainer = new VerticalLayout();
        conceptionKindCorrelationInfoGridContainer.setPadding(false);
        conceptionKindCorrelationInfoGridContainer.setSpacing(false);
        conceptionKindCorrelationInfoGridContainer.setMargin(false);

        VerticalLayout conceptionKindCorrelationInfoChartContainer = new VerticalLayout();
        conceptionKindCorrelationInfoChartContainer.setPadding(false);
        conceptionKindCorrelationInfoChartContainer.setSpacing(false);
        conceptionKindCorrelationInfoChartContainer.setMargin(false);




        Tab conceptionRealTimeInfoTab = kindCorrelationInfoTabSheet.add("",conceptionKindCorrelationInfoGridContainer);
        Span relationInfoSpan =new Span();
        Icon relationInfoIcon = new Icon(VaadinIcon.BULLETS);
        relationInfoIcon.setSize("12px");
        NativeLabel relationInfoLabel = new NativeLabel(" 概念关联实时分布");
        relationInfoSpan.add(relationInfoIcon,relationInfoLabel);
        conceptionRealTimeInfoTab.add(relationInfoSpan);

        Tab conceptionRealTimeChartTab = kindCorrelationInfoTabSheet.add("",conceptionKindCorrelationInfoChartContainer);
        Span chartInfoSpan =new Span();
        Icon chartInfoIcon = new Icon(VaadinIcon.SPLIT);
        chartInfoIcon.setSize("12px");
        NativeLabel chartInfoLabel = new NativeLabel(" 概念关联实时分布网络图");
        chartInfoSpan.add(chartInfoIcon,chartInfoLabel);
        conceptionRealTimeChartTab.add(chartInfoSpan);

        kindCorrelationInfoTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {
                //renderKindCorrelationInfoTabContent();
            }
        });

        add(kindCorrelationInfoTabSheet);








        HorizontalLayout mainContentContainerLayout = new HorizontalLayout();
        mainContentContainerLayout.setWidthFull();
        conceptionKindCorrelationInfoGridContainer.add(mainContentContainerLayout);

        VerticalLayout leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(700,Unit.PIXELS);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        VerticalLayout rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setMargin(true);
        rightSideContainerLayout.setPadding(false);

        mainContentContainerLayout.add(leftSideContainerLayout);
        mainContentContainerLayout.add(rightSideContainerLayout);

        ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper =
                new ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper() {
            @Override
            public void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel) {
                queryRelatedConceptionKind(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            }
        };
        classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        leftSideContainerLayout.add(classificationRelatedDataQueryCriteriaView);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon queryIcon = new Icon(VaadinIcon.RECORDS);
            queryIcon.setSize("20px");
            Button queryConceptionKind = new Button(queryIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            queryConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            queryConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(queryConceptionKind, "查询概念类型实体");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindConfigurationUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configConceptionKind, "配置概念类型定义");

            Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
            cleanKindIcon.setSize("21px");
            Button cleanConceptionKind = new Button(cleanKindIcon, event -> {});
            cleanConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cleanConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(cleanConceptionKind, "清除概念类型所有实例");
            cleanConceptionKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        //renderCleanConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeConceptionKind = new Button(deleteKindIcon, event -> {});
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeConceptionKind, "删除概念类型");
            removeConceptionKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        //renderRemoveConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(queryConceptionKind,configConceptionKind, cleanConceptionKind,removeConceptionKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });











        conceptionKindMetaInfoGrid = new Grid<>();
        conceptionKindMetaInfoGrid.setWidth(700,Unit.PIXELS);
        conceptionKindMetaInfoGrid.setHeight(600,Unit.PIXELS);
        conceptionKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindMetaInfoGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindMetaInfoGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        conceptionKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5")
                .setFlexGrow(0).setWidth("170px").setResizable(false);
        /*
        //conceptionKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_2")
        //        .setComparator(createDateComparator)
        //        .setFlexGrow(0).setWidth("210px").setResizable(false);
        //conceptionKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_3")
        //        .setComparator(lastUpdateDateComparator)
        //        .setFlexGrow(0).setWidth("210px").setResizable(false);
        conceptionKindMetaInfoGrid.addColumn(new NumberRenderer<>(EntityStatisticsInfo::getEntitiesCount,NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getEntitiesCount() - entityStatisticsInfo2.getEntitiesCount()))
                .setHeader("类型包含实体数量").setKey("idx_4")
                .setFlexGrow(0).setWidth("150px").setResizable(false);

        */
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        /*
        conceptionKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.STOCK,"类型包含实体数量");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5);
        */
        conceptionKindMetaInfoGrid.appendFooterRow();

        leftSideContainerLayout.add(conceptionKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LINK),"分类关联信息");
        rightSideContainerLayout.add(filterTitle2);
    }

    public void setHeight(int viewHeight){
        this.conceptionKindMetaInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.conceptionKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    private void queryRelatedConceptionKind(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        try {
            List<ConceptionKind>  conceptionKindList = targetClassification.getRelatedConceptionKinds(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            conceptionKindMetaInfoGrid.setItems(conceptionKindList);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
