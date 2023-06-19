package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class RelationKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String relationKind;
    private int relationKindDetailViewHeightOffset = 110;

    public RelationKindDetailUI(){}

    public RelationKindDetailUI(String relationKind){
        this.relationKind = relationKind;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.relationKind = beforeEnterEvent.getRouteParameters().get("relationKind").get();
        this.relationKindDetailViewHeightOffset = 45;
    }






}
