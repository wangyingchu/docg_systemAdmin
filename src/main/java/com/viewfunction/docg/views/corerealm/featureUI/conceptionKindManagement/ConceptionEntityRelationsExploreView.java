package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("entityRelationsExplore")
public class ConceptionEntityRelationsExploreView extends VerticalLayout implements HasUrlParameter<String> {

    public ConceptionEntityRelationsExploreView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setSizeFull();

        add(new Label("hello world"));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {

    }
}
