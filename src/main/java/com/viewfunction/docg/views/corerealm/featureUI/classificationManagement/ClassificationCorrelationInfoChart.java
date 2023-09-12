package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.InheritanceTree;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.chart.RadialTreeChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassificationCorrelationInfoChart extends VerticalLayout {
    private VerticalLayout chartContainerLayout;
    private int chartWidth = 400;
    private int chartHeight = 300;
    private String classificationName;
    public ClassificationCorrelationInfoChart(){
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);
        this.chartContainerLayout = new VerticalLayout();
        this.chartContainerLayout.setMargin(false);
        this.chartContainerLayout.setSpacing(false);
        this.chartContainerLayout.setPadding(false);
        //this.add(chartContainerLayout);

        this.setHeight(350, Unit.PIXELS);
        RadialTreeChart radialTreeChart = new RadialTreeChart(350,350);
        this.add(radialTreeChart);
    }

    public void refreshCorrelationInfo(String classificationName){
        this.classificationName = classificationName;

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        Classification targetClassification = coreRealm.getClassification(classificationName);

        if(targetClassification != null){
            List<Classification> chartAllClassificationList = new ArrayList<>();
            Map<String,Classification> chartAllClassificationListMap = new HashMap<>();

            chartAllClassificationList.add(targetClassification);
            chartAllClassificationListMap.put(targetClassification.getClassificationName(),targetClassification);
            prepareChildrenClassificationData(targetClassification,chartAllClassificationList,chartAllClassificationListMap);
            List<Classification> childrenClassificationList = targetClassification.getChildClassifications();
            for(Classification firstLevelChildClassification:childrenClassificationList){
                prepareChildrenClassificationData(firstLevelChildClassification,chartAllClassificationList,chartAllClassificationListMap);
            }

            /*
            InheritanceTree<Classification> offspringClassificationsTree = targetClassification.getOffspringClassifications();
            System.out.println(offspringClassificationsTree.size());
            System.out.println(offspringClassificationsTree.size());
            System.out.println(offspringClassificationsTree.size());
            */



        }
        coreRealm.closeGlobalSession();


    }

    public void setChartSize(int chartWidth,int chartHeight){
        this.chartWidth = chartWidth;
        this.chartHeight = chartHeight;
    }

    public void clearData(){
        this.chartContainerLayout.removeAll();
    }

    private void prepareChildrenClassificationData(Classification currentClassification,List<Classification> chartAllClassificationList,Map<String,Classification> chartAllClassificationListMap){
        List<Classification> childrenClassificationList = currentClassification.getChildClassifications();
        for(Classification currentChildClassification:childrenClassificationList){
            chartAllClassificationList.add(currentChildClassification);
            chartAllClassificationListMap.put(currentChildClassification.getClassificationName(),currentChildClassification);
        }
    }
}
