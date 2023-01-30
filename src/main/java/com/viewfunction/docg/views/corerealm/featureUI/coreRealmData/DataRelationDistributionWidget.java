package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RuntimeRelationAndConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SystemStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.List;

public class DataRelationDistributionWidget extends HorizontalLayout {

    public DataRelationDistributionWidget(){
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.getStyle().set("background-color","#EEEEEE");
    }




    private void generateDataRelationDistributionMap(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        //SystemStatusSnapshotInfo systemStatusSnapshotInfo = systemMaintenanceOperator.getSystemStatusSnapshot();
        DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();

        System.out.println(dataStatusSnapshotInfo.getRelationKindsDataCount());
        System.out.println(dataStatusSnapshotInfo.getConceptionKindsDataCount());

        List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();



        for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
            currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
            currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
            currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();
            System.out.println(currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind()+" - "+currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind()+":"+currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount());


        }


    }
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        generateDataRelationDistributionMap();
    }
}
