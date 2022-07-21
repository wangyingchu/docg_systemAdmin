package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AttributeEditorItemWidget extends VerticalLayout {

    public AttributeEditorItemWidget(){
        this.setWidth(100, Unit.PERCENTAGE);
        this.add(new Label("##########"));

        this.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
    }
}
