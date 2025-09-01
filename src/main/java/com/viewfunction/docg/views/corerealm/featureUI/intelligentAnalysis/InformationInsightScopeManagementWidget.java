package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

public class InformationInsightScopeManagementWidget extends VerticalLayout {

    private List<String> insightScopeConceptionKindList;
    private List<String> insightScopeRelationKindList;
    private List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList;

    public InformationInsightScopeManagementWidget(List<String> insightScopeConceptionKindList,
                                                  List<String> insightScopeRelationKindList,
                                                   List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList){
        this.insightScopeConceptionKindList = insightScopeConceptionKindList;
        this.insightScopeRelationKindList = insightScopeRelationKindList;
        this.insightScopeConceptionKindCorrelationList = insightScopeConceptionKindCorrelationList;
        setPadding(true);
        setMargin(false);
        //setSpacing(true);

        List<Component> sectionAction2ComponentsList = new ArrayList<>();
        SectionActionBar sectionActionBar1 = new SectionActionBar(LineAwesomeIconsSvg.CHART_LINE_SOLID.create(),"全域数据实时分布",sectionAction2ComponentsList);
        add(sectionActionBar1);
    }
}
