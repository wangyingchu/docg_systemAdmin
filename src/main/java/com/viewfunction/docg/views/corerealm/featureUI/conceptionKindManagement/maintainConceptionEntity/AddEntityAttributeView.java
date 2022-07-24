package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddEntityAttributeView extends VerticalLayout {

    private Dialog containerDialog;
    private H6 errorMessage;

    public AddEntityAttributeView(String conceptionKind,String conceptionEntityUID){

        HorizontalLayout infoFootPrintContainer = new HorizontalLayout();
        infoFootPrintContainer.setMargin(false);
        infoFootPrintContainer.setSpacing(false);
        infoFootPrintContainer.setPadding(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        infoFootPrintContainer.add(footPrintStartIcon);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        infoFootPrintContainer.add(conceptionKindIcon);
        Label conceptionKindNameLabel = new Label(conceptionKind);
        infoFootPrintContainer.add(conceptionKindNameLabel);
        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("12px");
        divIcon.getStyle().set("padding-left","5px");
        infoFootPrintContainer.add(divIcon);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        infoFootPrintContainer.add(conceptionEntityIcon);

        Label conceptionEntityUIDLabel = new Label(conceptionEntityUID);
        infoFootPrintContainer.add(conceptionEntityUIDLabel);

add(infoFootPrintContainer);








    }






    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
