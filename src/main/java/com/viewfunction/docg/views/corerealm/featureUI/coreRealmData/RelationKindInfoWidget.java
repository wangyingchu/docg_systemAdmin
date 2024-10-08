package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.BarChart;

import java.text.NumberFormat;
import java.util.*;

public class RelationKindInfoWidget extends HorizontalLayout {
    private boolean contentAlreadyLoaded = false;
    private VerticalLayout widgetComponentContainer;
    private VerticalLayout chartComponentContainer;
    public RelationKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        widgetComponentContainer = new VerticalLayout();
        widgetComponentContainer.setWidth(250,Unit.PIXELS);
        widgetComponentContainer.setSpacing(false);
        widgetComponentContainer.setMargin(false);
        add(widgetComponentContainer);

        chartComponentContainer = new VerticalLayout();
        chartComponentContainer.setSpacing(false);
        chartComponentContainer.setMargin(false);
        add(chartComponentContainer);
        this.setFlexGrow(1, chartComponentContainer);
    }

    public void loadWidgetContent(){
        if(!this.contentAlreadyLoaded){
            this.contentAlreadyLoaded = true;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            List<EntityStatisticsInfo> entityStatisticsInfoList = null;
            long totalEntitiesCount = 0;
            int totalKindCount = 0;

            Map<String,Long> relationKindEntitiesCountMap = new HashMap<>();

            entityStatisticsInfoList = coreRealm.getRelationEntitiesStatistics();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(!currentEntityStatisticsInfo.isSystemKind()) {
                    String currentRelationKind = currentEntityStatisticsInfo.getEntityKindName();
                    long currentKindEntityCount = currentEntityStatisticsInfo.getEntitiesCount();
                    totalEntitiesCount = totalEntitiesCount + currentKindEntityCount;
                    totalKindCount++;
                    relationKindEntitiesCountMap.put(currentRelationKind,currentKindEntityCount);
                }
            }

            List<Map.Entry<String,Long>> orderedlist = new ArrayList<Map.Entry<String,Long>>(relationKindEntitiesCountMap.entrySet());
            Collections.sort(orderedlist, new Comparator<Map.Entry<String,Long>>() {
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            int topRelationKindNameArraySize = totalKindCount >= 10 ? 11 : totalKindCount;
            String[] relationKindNameArray = new String[topRelationKindNameArraySize];
            Double[] kindEntitiesCountArray = new Double[topRelationKindNameArraySize];
            long top10CountTotal = 0;

            for(int i=0;i <topRelationKindNameArraySize;i++){
                Map.Entry<String,Long> mapping = orderedlist.get(i);
                relationKindNameArray[i] = mapping.getKey();
                kindEntitiesCountArray[i] = Double.valueOf(mapping.getValue());
                top10CountTotal = top10CountTotal+mapping.getValue();
            }
            if(topRelationKindNameArraySize == 11){
                relationKindNameArray[topRelationKindNameArraySize-1] = "OTHER";
                kindEntitiesCountArray[topRelationKindNameArraySize-1] =Double.valueOf( totalEntitiesCount - top10CountTotal);
            }

            NumberFormat numberFormat = NumberFormat.getInstance();

            new PrimaryKeyValueDisplayItem(widgetComponentContainer, FontAwesome.Regular.CIRCLE.create(),"关系类型数量:",numberFormat.format(totalKindCount));

            HorizontalLayout spaceDivLayout = new HorizontalLayout();
            spaceDivLayout.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout);

            new PrimaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Solid.CIRCLE.create(),"关系实体数量:",numberFormat.format(totalEntitiesCount));

            HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
            spaceDivLayout2.setHeight(25,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout2);

            NativeLabel messageText = new NativeLabel("Top 10 Relation Types with more entities ->");
            widgetComponentContainer.add(messageText);
            messageText.addClassNames("text-xs","text-tertiary");

            BarChart barChart = new BarChart(330,250);
            chartComponentContainer.add(barChart);

            String[] barColorArray = new String[]{"#76b852"};
            barChart.setColor(barColorArray);
            barChart.setTopMargin(2);
            barChart.setRightMargin(20);

            barChart.setDate(relationKindNameArray,kindEntitiesCountArray);
        }
    }

    public void reloadWidgetContent(){
        this.widgetComponentContainer.removeAll();
        this.chartComponentContainer.removeAll();
        this.contentAlreadyLoaded = false;
        loadWidgetContent();
    }
}
