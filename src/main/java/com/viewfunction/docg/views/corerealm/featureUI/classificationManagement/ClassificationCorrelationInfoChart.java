package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.chart.TreeChart;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsRadialTreeChartPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassificationCorrelationInfoChart extends VerticalLayout {
    private VerticalLayout chartContainerLayout;
    private int chartHeight;
    private String classificationName;
    public ClassificationCorrelationInfoChart(int chartHeight){
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);
        this.chartContainerLayout = new VerticalLayout();
        this.chartContainerLayout.setMargin(false);
        this.chartContainerLayout.setSpacing(false);
        this.chartContainerLayout.setPadding(false);
        this.add(chartContainerLayout);
        this.setHeight(chartHeight, Unit.PIXELS);
        this.chartHeight = chartHeight;
    }

    public void refreshCorrelationInfo(String classificationName){
        clearData();
        this.classificationName = classificationName;

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        Classification targetClassification = coreRealm.getClassification(classificationName);
        EchartsRadialTreeChartPayload currentClassificationPayload = null;
        if(targetClassification != null){
            List<Classification> chartAllClassificationList = new ArrayList<>();
            Map<String,Classification> chartAllClassificationListMap = new HashMap<>();

            chartAllClassificationList.add(targetClassification);
            chartAllClassificationListMap.put(targetClassification.getClassificationName(),targetClassification);
            prepareChildrenClassificationData(null,targetClassification,chartAllClassificationList,chartAllClassificationListMap);
            List<Classification> childrenClassificationList = targetClassification.getChildClassifications();

            currentClassificationPayload = new EchartsRadialTreeChartPayload(this.classificationName);
            List<EchartsRadialTreeChartPayload> childEchartsRadialTreeChartPayloadList = new ArrayList<>();
            currentClassificationPayload.setValue(childrenClassificationList.size());
            currentClassificationPayload.setChildren(childEchartsRadialTreeChartPayloadList);
            for(Classification firstLevelChildClassification:childrenClassificationList){
                prepareChildrenClassificationData(currentClassificationPayload,firstLevelChildClassification,chartAllClassificationList,chartAllClassificationListMap);
            }
        }
        coreRealm.closeGlobalSession();

        TreeChart radialTreeChart = new TreeChart(0,chartHeight);
        //radialTreeChart.setLayout(TreeChart.TreeLayout.radial);
        this.chartContainerLayout.add(radialTreeChart);

        if(currentClassificationPayload != null){
            radialTreeChart.setDate(currentClassificationPayload);
        }
    }

    public void setChartHeight(int chartHeight){
        this.chartHeight = chartHeight;
    }

    public void clearData(){
        this.chartContainerLayout.removeAll();
    }

    private void prepareChildrenClassificationData(EchartsRadialTreeChartPayload parentEchartsRadialTreeChartPayload,
                                                   Classification currentClassification,
                                                   List<Classification> chartAllClassificationList,
                                                   Map<String,Classification> chartAllClassificationListMap){
        EchartsRadialTreeChartPayload currentClassificationPayload = new EchartsRadialTreeChartPayload(currentClassification.getClassificationName());
        List<EchartsRadialTreeChartPayload> childEchartsRadialTreeChartPayloadList = new ArrayList<>();
        currentClassificationPayload.setChildren(childEchartsRadialTreeChartPayloadList);
        if(parentEchartsRadialTreeChartPayload != null){
            parentEchartsRadialTreeChartPayload.getChildren().add(currentClassificationPayload);
        }
        List<Classification> childrenClassificationList = currentClassification.getChildClassifications();
        currentClassificationPayload.setValue(childrenClassificationList.size());

        for(Classification currentChildClassification:childrenClassificationList){
            chartAllClassificationList.add(currentChildClassification);
            chartAllClassificationListMap.put(currentChildClassification.getClassificationName(),currentChildClassification);
            prepareChildrenClassificationData(currentClassificationPayload,currentChildClassification,chartAllClassificationList,chartAllClassificationListMap);
        }
    }
}
