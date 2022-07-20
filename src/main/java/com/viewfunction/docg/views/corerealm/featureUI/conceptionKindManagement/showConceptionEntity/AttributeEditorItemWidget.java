package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AttributeEditorItemWidget extends HorizontalLayout {

    public AttributeEditorItemWidget(){

        this.setWidth(100, Unit.PERCENTAGE);
        //this.setMinWidth(248,Unit.PIXELS);

        this.add(new Label("##########"));


        //HorizontalLayout spaceDivLayout = new HorizontalLayout();
        //spaceDivLayout.setWidth(100, Unit.PERCENTAGE);
        //spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        //add(spaceDivLayout);


        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);


        this.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
    }





}
