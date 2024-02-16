package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.Set;

public class DataRelationDistributionWidget extends HorizontalLayout {

    private DataRelationDistributionChart dataRelationDistributionChart;
    private DataRelationDistribution3DChart dataRelationDistribution3DChart;
    private boolean isIn2DMode = true;

    public DataRelationDistributionWidget(){
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
    }

    private void renderDataRelationDistributionInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();

        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = systemMaintenanceOperator.getAllDataRelationDistributionStatistics();
        if(isIn2DMode){
            dataRelationDistributionChart = new DataRelationDistributionChart();
            add(dataRelationDistributionChart);
            dataRelationDistributionChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
        }else{

        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderDataRelationDistributionInfo();
    }

    public void refreshDataRelationDistributionData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = systemMaintenanceOperator.getAllDataRelationDistributionStatistics();
        if(isIn2DMode){
            dataRelationDistributionChart.clearData();
            dataRelationDistributionChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
        }else{

        }
    }

    public void show2DChart(){
        isIn2DMode = true;
        if(dataRelationDistributionChart != null){
            dataRelationDistributionChart.setVisible(true);
        }
    }

    public void show3DChart(){
        isIn2DMode = false;
        if(dataRelationDistributionChart != null){
            dataRelationDistributionChart.setVisible(false);
        }
    }
}
