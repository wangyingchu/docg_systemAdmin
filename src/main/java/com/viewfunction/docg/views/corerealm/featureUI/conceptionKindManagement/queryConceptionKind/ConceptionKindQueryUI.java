package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

public class ConceptionKindQueryUI extends VerticalLayout {

    private String conceptionKindName;

    public ConceptionKindQueryUI(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;

        VerticalLayout queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);
        queryFieldsContainer.setHeight(500, Unit.PIXELS);
        queryFieldsContainer.add(new Label("la"));

        VerticalLayout queryResultContainer= new VerticalLayout();
        queryResultContainer.setPadding(false);
        queryResultContainer.setSpacing(false);
        queryResultContainer.setMargin(false);
        queryResultContainer.add(new Label("lb"));
        queryFieldsContainer.setMinWidth(200,Unit.PIXELS);
        queryFieldsContainer.setMaxWidth(400,Unit.PIXELS);

        SplitLayout splitLayout = new SplitLayout(queryFieldsContainer, queryResultContainer);
        splitLayout.setSplitterPosition(10);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);

    }



}
