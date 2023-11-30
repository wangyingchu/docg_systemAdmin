package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.theme.lumo.LumoUtility;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;
import com.viewfunction.docg.element.eventHandling.CheckSystemRuntimeInfoEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.text.NumberFormat;
import java.util.*;

public class ConceptionKindInfoWidget extends HorizontalLayout {
    private boolean contentAlreadyLoaded = false;
    private VerticalLayout widgetComponentContainer;

    public ConceptionKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames(LumoUtility.Background.BASE);

        widgetComponentContainer = new VerticalLayout();
        widgetComponentContainer.setWidth(250,Unit.PIXELS);
        widgetComponentContainer.setSpacing(false);
        widgetComponentContainer.setMargin(false);
        add(widgetComponentContainer);
    }

    public void loadWidgetContent(){
        if(!this.contentAlreadyLoaded){
            this.contentAlreadyLoaded = true;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            List<EntityStatisticsInfo> entityStatisticsInfoList = null;
            long totalEntitiesCount = 0;
            int totalKindCount = 0;

            Map<String,Long> conceptionKindEntitiesCountMap = new HashMap<>();
            try {
                entityStatisticsInfoList = coreRealm.getConceptionEntitiesStatistics();
                for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                    if(!currentEntityStatisticsInfo.isSystemKind()) {
                        String currentConceptionKind = currentEntityStatisticsInfo.getEntityKindName();
                        long currentKindEntityCount = currentEntityStatisticsInfo.getEntitiesCount();
                        totalEntitiesCount = totalEntitiesCount + currentKindEntityCount;
                        totalKindCount++;
                        conceptionKindEntitiesCountMap.put(currentConceptionKind,currentKindEntityCount);
                    }
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }

            List<Map.Entry<String,Long>> orderedlist = new ArrayList<Map.Entry<String,Long>>(conceptionKindEntitiesCountMap.entrySet());
            Collections.sort(orderedlist, new Comparator<Map.Entry<String,Long>>() {
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            int topConceptionKindNameArraySize = totalKindCount >= 10 ? 11 : totalKindCount+1;
            String[] conceptionKindNameArray = new String[topConceptionKindNameArraySize];
            Double[] kindEntitiesCountArray = new Double[topConceptionKindNameArraySize];
            long top10CountTotal = 0;

            for(int i=0;i <topConceptionKindNameArraySize -1;i++){
                Map.Entry<String,Long> mapping = orderedlist.get(i);
                conceptionKindNameArray[i] = mapping.getKey();
                kindEntitiesCountArray[i] = Double.valueOf(mapping.getValue());
                top10CountTotal = top10CountTotal+mapping.getValue();
            }
            if(topConceptionKindNameArraySize ==11) {
                conceptionKindNameArray[topConceptionKindNameArraySize - 1] = "OTHER";
                kindEntitiesCountArray[topConceptionKindNameArraySize - 1] = Double.valueOf(totalEntitiesCount - top10CountTotal);
            }
            NumberFormat numberFormat = NumberFormat.getInstance();

            new PrimaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Regular.CIRCLE.create(),"概念类型数量:",numberFormat.format(totalKindCount));

            HorizontalLayout spaceDivLayout = new HorizontalLayout();
            spaceDivLayout.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout);

            new PrimaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Solid.CIRCLE.create(),"概念实体数量:",numberFormat.format(totalEntitiesCount));

            HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
            spaceDivLayout2.setHeight(25,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout2);

            NativeLabel messageText = new NativeLabel("Top 10 Conception Types with more entities ->");
            widgetComponentContainer.add(messageText);
            messageText.addClassNames("text-xs","text-tertiary");

            VerticalLayout rightComponentContainer = new VerticalLayout();
            rightComponentContainer.setSpacing(false);
            rightComponentContainer.setMargin(false);
            add(rightComponentContainer);
            this.setFlexGrow(1,rightComponentContainer);

            PieChart pieChart = new PieChart(330,260);
            rightComponentContainer.add(pieChart);
            rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,pieChart);

            String[] pieColorArray = new String[]{"#03a9f4","#76b852","#00d1b2","#ced7df","#ee4f4f","#0288d1","#ffc107","#d32f2f","#168eea","#323b43","#59626a"};
            pieChart.setColor(pieColorArray);
            pieChart.setCenter(50,45);
            pieChart.setRadius(60);
            pieChart.enableBottomLegend();
            pieChart.setDate(conceptionKindNameArray,kindEntitiesCountArray);
        }
    }

    public void reloadWidgetContent(){
        this.widgetComponentContainer.removeAll();
        this.contentAlreadyLoaded = false;
        loadWidgetContent();
    }
}
