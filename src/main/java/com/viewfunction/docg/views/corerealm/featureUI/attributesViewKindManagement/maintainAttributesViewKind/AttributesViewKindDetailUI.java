package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("attributesViewKindDetailInfo/:attributesViewKindUID")
public class AttributesViewKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String attributesViewKindUID;
    private int conceptionKindDetailViewHeightOffset = 110;

    public AttributesViewKindDetailUI(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributesViewKindUID = beforeEnterEvent.getRouteParameters().get("attributesViewKindUID").get();
        this.conceptionKindDetailViewHeightOffset = 45;
    }
}
