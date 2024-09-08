package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ConceptionKindMatchLogic;

public class ConceptionKindMatchLogicWidget extends HorizontalLayout {

    private String conceptionKindName;
    private ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule;

    public ConceptionKindMatchLogicWidget(){

    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public ConceptionKindMatchLogic.ConceptionKindExistenceRule getConceptionKindExistenceRule() {
        return conceptionKindExistenceRule;
    }

    public void setConceptionKindExistenceRule(ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule) {
        this.conceptionKindExistenceRule = conceptionKindExistenceRule;
    }
}
