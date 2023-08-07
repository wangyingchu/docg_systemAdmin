package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.chart.BarChart;

import java.util.Map;

public class AttributeInConceptionKindDistributionInfoChart extends VerticalLayout {

    private VerticalLayout chartContainerLayout;
    private String attributeKindUID;
    private int chartWidth = 400;
    private int chartHeight = 300;

    public AttributeInConceptionKindDistributionInfoChart(){
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);
        this.chartContainerLayout = new VerticalLayout();
        this.chartContainerLayout.setMargin(false);
        this.chartContainerLayout.setSpacing(false);
        this.chartContainerLayout.setPadding(false);
        this.add(chartContainerLayout);
    }

    public void refreshDistributionInfo(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;
        this.chartContainerLayout.removeAll();
        BarChart barChart = new BarChart(chartWidth,chartHeight);
        String[] barColorArray = new String[]{"#FF4500"};
        barChart.setColor(barColorArray);
        barChart.setTopMargin(5);
        barChart.setRightMargin(15);
        barChart.setLeftMargin(15);
        this.chartContainerLayout.add(barChart);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();

        AttributeKind attributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        if(attributeKind != null){
            Map<String,Long> entityDistributionMap = attributeKind.getAttributeInConceptionKindDistributionStatistics();
            if(entityDistributionMap != null && entityDistributionMap.size() >0){
                Object[] conceptionKindNameObjectArray = entityDistributionMap.keySet().toArray();
                String[] nameArray = new String[conceptionKindNameObjectArray.length];
                Double[] valueArray = new Double[conceptionKindNameObjectArray.length];
                for(int i=0;i<conceptionKindNameObjectArray.length;i++){
                    nameArray[i] = conceptionKindNameObjectArray[i].toString();
                    valueArray[i] = entityDistributionMap.get(conceptionKindNameObjectArray[i]).doubleValue();
                }
                barChart.setDate(nameArray,valueArray);
            }
        }
    }

    public void setChartSize(int chartWidth,int chartHeight){
        this.chartWidth = chartWidth;
        this.chartHeight = chartHeight;
    }

    public void clearData(){
        this.chartContainerLayout.removeAll();
    }
}
