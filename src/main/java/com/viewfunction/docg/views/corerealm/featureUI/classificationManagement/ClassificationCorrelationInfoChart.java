package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.InheritanceTree;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

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
        this.add(chartContainerLayout);
    }

    public void refreshCorrelationInfo(String classificationName){
        this.classificationName = classificationName;

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        Classification targetClassification = coreRealm.getClassification(classificationName);
        if(targetClassification != null){
            InheritanceTree<Classification> offspringClassificationsTree = targetClassification.getOffspringClassifications();





            System.out.println(offspringClassificationsTree.size());
            System.out.println(offspringClassificationsTree.size());
            System.out.println(offspringClassificationsTree.size());

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
}
