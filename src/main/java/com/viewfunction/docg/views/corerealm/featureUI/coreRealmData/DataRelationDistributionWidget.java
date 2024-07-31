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
    private DataStatusSnapshotInfo dataStatusSnapshotInfo;
    private Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet;
    private int _2DChartWidth=1200;
    private int _2DChartHeight=1200;

    public DataRelationDistributionWidget(){
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
    }

    private void renderDataRelationDistributionInfo(int _2DChartHeight, int _2DChartWidth){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        if(dataStatusSnapshotInfo == null){
            dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
        }
        if(conceptionKindCorrelationInfoSet == null){
            conceptionKindCorrelationInfoSet = systemMaintenanceOperator.getAllDataRelationDistributionStatistics();
        }
        if(isIn2DMode){
            dataRelationDistributionChart = new DataRelationDistributionChart();
            dataRelationDistributionChart.setChartWidth(_2DChartWidth);
            dataRelationDistributionChart.setChartHeight(_2DChartHeight);
            add(dataRelationDistributionChart);
            dataRelationDistributionChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
        }else{
            dataRelationDistribution3DChart = new DataRelationDistribution3DChart();
            add(dataRelationDistribution3DChart);
            dataRelationDistribution3DChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            _2DChartWidth = receiver.getWindowInnerWidth()-1030;
            _2DChartHeight = receiver.getWindowInnerHeight()-205;
            renderDataRelationDistributionInfo(_2DChartHeight,_2DChartWidth);
        }));
    }

    public void refreshDataRelationDistributionData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
        conceptionKindCorrelationInfoSet = systemMaintenanceOperator.getAllDataRelationDistributionStatistics();
        if(dataRelationDistributionChart != null){
            remove(dataRelationDistributionChart);
            dataRelationDistributionChart = new DataRelationDistributionChart();
            dataRelationDistributionChart.setChartWidth(_2DChartWidth);
            dataRelationDistributionChart.setChartHeight(_2DChartHeight);
            add(dataRelationDistributionChart);
            dataRelationDistributionChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
        }
        if(dataRelationDistribution3DChart != null){
            dataRelationDistribution3DChart.clearData();
            dataRelationDistribution3DChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
        }
    }

    public void show2DChart(){
        isIn2DMode = true;
        if(dataRelationDistribution3DChart != null){
            dataRelationDistribution3DChart.setVisible(false);
        }
        if(dataRelationDistributionChart != null){
            dataRelationDistributionChart.setVisible(true);
        }else{
            renderDataRelationDistributionInfo(_2DChartHeight,_2DChartWidth);
        }
    }

    public void show3DChart(){
        isIn2DMode = false;
        if(dataRelationDistributionChart != null){
            dataRelationDistributionChart.setVisible(false);
        }
        if(dataRelationDistribution3DChart != null){
            dataRelationDistribution3DChart.setVisible(true);
        }else{
            renderDataRelationDistributionInfo(_2DChartHeight,_2DChartWidth);
        }
    }
}
