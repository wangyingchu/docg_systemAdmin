package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RealtimeAttributesCorrelationInfoSummaryView extends HorizontalLayout {

    class AttributesDistributionInfo{
        private String kindsName;
        private long attributeCount;

        public AttributesDistributionInfo(String kindsName,long attributeCount){
            setKindsName(kindsName);
            setAttributeCount(attributeCount);
        }

        public String getKindsName() {
            return kindsName;
        }

        public void setKindsName(String kindsName) {
            this.kindsName = kindsName;
        }

        public long getAttributeCount() {
            return attributeCount;
        }

        public void setAttributeCount(long attributeCount) {
            this.attributeCount = attributeCount;
        }
    }

    private Grid<AttributesDistributionInfo> conceptionKindAttributesInfoGrid;
    private Grid<AttributesDistributionInfo> relationKindAttributesInfoGrid;
    private AttributesCorrelationInfoSummaryChart attributesCorrelationInfoSummaryChart;
    private SecondaryTitleActionBar attributeNameActionBar;

    public RealtimeAttributesCorrelationInfoSummaryView(int viewHeight){
        this.setWidthFull();
        this.setHeight(viewHeight,Unit.PIXELS);

        VerticalLayout leftSideContainer = new VerticalLayout();
        leftSideContainer.setSpacing(false);
        leftSideContainer.setWidth(510,Unit.PIXELS);

        VerticalLayout rightSideContainer = new VerticalLayout();
        rightSideContainer.setWidthFull();
        add(leftSideContainer,rightSideContainer);

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"领域实时属性概览");
        filterTitle1.getStyle().set("padding-bottom", "var(--lumo-space-s)");
        leftSideContainer.add(filterTitle1);

        Div attributesContainerDiv = new Div();
        attributesContainerDiv.setWidth(500, Unit.PIXELS);

        Scroller scroller = new Scroller(attributesContainerDiv);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("border-top", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainer.add(scroller);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Set<String> realtimeAttributesSet = coreRealm.getSystemMaintenanceOperator().getRealtimeAttributesStatistics();
        for(String currentAttribute:realtimeAttributesSet){
            Button showDetailButton = new Button(currentAttribute);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderAttributeRealtimeInfo(currentAttribute);
                }
            });

            attributesContainerDiv.add(showDetailButton);
            Span pendingSpan = new Span(" ");
            pendingSpan.setWidth("10px");
            attributesContainerDiv.add(pendingSpan);
        }

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.SCATTER_CHART),"被选择属性数据分布");
        filterTitle2.setWidth(830,Unit.PIXELS);
        filterTitle2.getStyle().set("padding-bottom", "var(--lumo-space-s)");
        filterTitle2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        rightSideContainer.add(filterTitle2);

        attributeNameActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"-",null,null,false);
        rightSideContainer.add(attributeNameActionBar);

        HorizontalLayout entityAttributesCountInfoGridLayout = new HorizontalLayout();
        entityAttributesCountInfoGridLayout.setWidthFull();
        rightSideContainer.add(entityAttributesCountInfoGridLayout);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(410,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(AttributesDistributionInfo::getKindsName).setHeader("概念类型名称集合").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(AttributesDistributionInfo::getKindsName);
        conceptionKindAttributesInfoGrid.addColumn(AttributesDistributionInfo::getAttributeCount).setHeader("属性值数量").setKey("idx_1").setFlexGrow(0).setWidth("100px");
        GridColumnHeader gridColumnHeader_idx00 = new GridColumnHeader(VaadinIcon.CUBE,"概念类型名称集合");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx00).setSortable(true);
        GridColumnHeader gridColumnHeader_idx01 = new GridColumnHeader(VaadinIcon.STOCK,"属性值数量");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx01).setSortable(true);
        conceptionKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);

        relationKindAttributesInfoGrid = new Grid<>();
        relationKindAttributesInfoGrid.setWidth(410,Unit.PERCENTAGE);
        relationKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        relationKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationKindAttributesInfoGrid.addColumn(AttributesDistributionInfo::getKindsName).setHeader("关系类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(AttributesDistributionInfo::getKindsName);
        relationKindAttributesInfoGrid.addColumn(AttributesDistributionInfo::getAttributeCount).setHeader("属性值数量").setKey("idx_1").setFlexGrow(0).setWidth("100px");
        GridColumnHeader gridColumnHeader_idx10 = new GridColumnHeader(VaadinIcon.CONNECT_O,"关系类型名称");
        relationKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx10).setSortable(true);
        GridColumnHeader gridColumnHeader_idx11 = new GridColumnHeader(VaadinIcon.STOCK,"属性值数量");
        relationKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx11).setSortable(true);
        relationKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);

        entityAttributesCountInfoGridLayout.add(conceptionKindAttributesInfoGrid,relationKindAttributesInfoGrid);

        attributesCorrelationInfoSummaryChart = new AttributesCorrelationInfoSummaryChart();
        rightSideContainer.add(attributesCorrelationInfoSummaryChart);
    }

    private void renderAttributeRealtimeInfo(String attributeName){
        attributeNameActionBar.updateTitleContent(attributeName);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Map<Set<String>, Long> conceptionAttributeValueDistributionMap = systemMaintenanceOperator.getConceptionAttributeValueDistributionStatistic(attributeName);
        Map<String, Long> relationAttributeValueDistributionMap = systemMaintenanceOperator.getRelationAttributeValueDistributionStatistic(attributeName);
        coreRealm.closeGlobalSession();

        List<AttributesDistributionInfo> conceptionAttributesDistributionInfoList = new ArrayList<>();
        Set<Set<String>> keySet = conceptionAttributeValueDistributionMap.keySet();
        for(Set<String> currentNamesSet : keySet){
            String namesSet = currentNamesSet.toString();
            long attributeCount = conceptionAttributeValueDistributionMap.get(currentNamesSet);
            conceptionAttributesDistributionInfoList.add(new AttributesDistributionInfo(namesSet,attributeCount));
        }
        conceptionKindAttributesInfoGrid.setItems(conceptionAttributesDistributionInfoList);

        List<AttributesDistributionInfo> relationAttributesDistributionInfoList = new ArrayList<>();
        Set<String> keySet2 = relationAttributeValueDistributionMap.keySet();
        for(String currentKindName:keySet2){
            long attributeCount = relationAttributeValueDistributionMap.get(currentKindName);
            relationAttributesDistributionInfoList.add(new AttributesDistributionInfo(currentKindName,attributeCount));
        }
        relationKindAttributesInfoGrid.setItems(relationAttributesDistributionInfoList);
        attributesCorrelationInfoSummaryChart.setSummaryData(conceptionAttributesDistributionInfoList,relationAttributesDistributionInfoList);
    }
}
