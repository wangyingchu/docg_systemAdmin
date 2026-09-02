package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;

import java.util.List;

public class InformationInsightWidget extends VerticalLayout {

    public InformationInsightWidget(String question,
                                    List<String> insightScopeConceptionKindList,
                                    List<String> insightScopeRelationKindList,
                                    List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList,
                                    int insightContentHeight){

        NativeLabel lb = new NativeLabel("洞察");
        add(lb);

    }
}
