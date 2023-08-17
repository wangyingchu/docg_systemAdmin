package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

public class RealtimeAttributesCorrelationInfoSummaryChart extends VerticalLayout {

    public RealtimeAttributesCorrelationInfoSummaryChart(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.getSystemMaintenanceOperator().getRealtimeAttributesStatistics();
    }
}
