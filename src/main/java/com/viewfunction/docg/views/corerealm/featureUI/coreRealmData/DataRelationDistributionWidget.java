package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RuntimeRelationAndConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.List;
import java.util.Set;

public class DataRelationDistributionWidget extends HorizontalLayout {

    public DataRelationDistributionWidget(){
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
    }

    private void generateDataRelationDistributionMap(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
        List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();
        for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
            currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
            currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
            currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();
        }

        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet =  systemMaintenanceOperator.getAllDataRelationDistributionStatistics();
        DataRelationDistributionChart dataRelationDistributionChart = new DataRelationDistributionChart();
        add(dataRelationDistributionChart);
        dataRelationDistributionChart.setData(conceptionKindCorrelationInfoSet,dataStatusSnapshotInfo.getConceptionKindsDataCount(),dataStatusSnapshotInfo.getRelationKindsDataCount());
    }
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        generateDataRelationDistributionMap();
    }
}
