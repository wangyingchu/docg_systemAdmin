package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("attributeKindDetailInfo/:attributeKindUID")
public class AttributeKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String attributeKindUID;
    private int conceptionKindDetailViewHeightOffset = 110;

    public AttributeKindDetailUI(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributeKindUID = beforeEnterEvent.getRouteParameters().get("attributeKindUID").get();
        this.conceptionKindDetailViewHeightOffset = 45;
    }
}
