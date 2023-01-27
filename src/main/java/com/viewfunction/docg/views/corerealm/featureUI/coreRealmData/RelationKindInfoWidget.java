package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
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

    public RelationKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(250,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

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

        int topRelationKindNameArraySize = totalKindCount >= 10 ? 11 : totalKindCount+1;
        String[] relationKindNameArray = new String[topRelationKindNameArraySize];
        Double[] kindEntitiesCountArray = new Double[topRelationKindNameArraySize];
        long top10CountTotal = 0;

        for(int i=0;i <topRelationKindNameArraySize -1;i++){
            Map.Entry<String,Long> mapping = orderedlist.get(i);
            relationKindNameArray[i] = mapping.getKey();
            kindEntitiesCountArray[i] = Double.valueOf(mapping.getValue());
            top10CountTotal = top10CountTotal+mapping.getValue();
        }
        relationKindNameArray[topRelationKindNameArraySize-1] = "OTHER";
        kindEntitiesCountArray[topRelationKindNameArraySize-1] =Double.valueOf( totalEntitiesCount - top10CountTotal);
        NumberFormat numberFormat = NumberFormat.getInstance();

        new PrimaryKeyValueDisplayItem(leftComponentContainer, FontAwesome.Regular.CIRCLE.create(),"关系类型数量:",numberFormat.format(totalKindCount));

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Solid.CIRCLE.create(),"关系实体数量:",numberFormat.format(totalEntitiesCount));

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(25,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout2);

        Label messageText = new Label("Top 10 Relation Types with more entities ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);



        BarChart barChart = new BarChart(330,250);
        rightComponentContainer.add(barChart);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,barChart);

        String[] pieColorArray = new String[]{"#03a9f4","#76b852","#00d1b2","#ced7df","#ee4f4f","#0288d1","#ffc107","#d32f2f","#168eea","#323b43","#59626a"};
        //pieChart.setColor(pieColorArray);
        //pieChart.setCenter(50,45);
        //pieChart.setRadius(60);
        //pieChart.enableBottomLegend();
        barChart.setDate(relationKindNameArray,kindEntitiesCountArray);
    }
}
