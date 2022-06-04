package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.Set;

@Route("conceptionKindsCorrelationInfoSummary")
public class ConceptionKindsCorrelationInfoSummaryView extends VerticalLayout {

    private ConceptionKindsCorrelationInfoSummaryChart conceptionKindsCorrelationInfoSummaryChart;

    public ConceptionKindsCorrelationInfoSummaryView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setSizeFull();
        conceptionKindsCorrelationInfoSummaryChart = new ConceptionKindsCorrelationInfoSummaryChart(1140,800);
        add(conceptionKindsCorrelationInfoSummaryChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = systemMaintenanceOperator.
                getSystemConceptionKindsRelationDistributionStatistics();
        conceptionKindsCorrelationInfoSummaryChart.setData(conceptionKindCorrelationInfoSet);
    }

}
