package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.LocalDate;

//@Theme(value = Lumo.class, variant = Lumo.DARK)
public class RelationKindManagementUI extends VerticalLayout {

    public RelationKindManagementUI(){
        add(new Label("RelationKindManagementUI"));




        H3 personalTitle = new H3("Personal information");
        personalTitle.setId("PERSONAL_TITLE_ID");

        TextField firstName = new TextField("First name");
        firstName.setWidthFull();

        TextField lastName = new TextField("Last name");
        lastName.setWidthFull();

        DatePicker birthDate = new DatePicker("Birthdate");
        birthDate.setInitialPosition(LocalDate.of(1990, 1, 1));
        birthDate.setWidthFull();

        Section personalInformation = new Section(personalTitle, firstName, lastName, birthDate);
        personalInformation.getElement().setAttribute("aria-labelledby", "PERSONAL_TITLE_ID");

        add(personalInformation);

// Employment information
        H3 employementTitle = new H3("Employment information");
        employementTitle.setId("EMPLOYMENT_TITLE_ID");

        TextField position = new TextField("Position");
        position.setWidthFull();

        TextArea additionalInformation = new TextArea("Additional Information");
        additionalInformation.setWidthFull();

        Section employmentInformation = new Section(employementTitle, position, additionalInformation);
        employmentInformation.getElement().setAttribute("aria-labelledby", "EMPLOYMENT_TITLE_ID");

        add(employmentInformation);

        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
        add(new Label("RelationKindManagementUI"));
    }
}
