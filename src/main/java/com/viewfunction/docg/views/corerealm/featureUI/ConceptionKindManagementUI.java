package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;

public class ConceptionKindManagementUI extends VerticalLayout {

    public ConceptionKindManagementUI(){
        add(new Label("ConceptionKindManagementUI"));




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

// Employment information
        H3 employementTitle = new H3("Employment information");
        employementTitle.setId("EMPLOYMENT_TITLE_ID");

        TextField position = new TextField("Position");
        position.setWidthFull();

        TextArea additionalInformation = new TextArea("Additional Information");
        additionalInformation.setWidthFull();

        Section employmentInformation = new Section(employementTitle, position, additionalInformation);
        employmentInformation.getElement().setAttribute("aria-labelledby", "EMPLOYMENT_TITLE_ID");

// NOTE
// We are using inline styles here to keep the example simple.
// We recommend placing CSS in a separate style sheet and to
// encapsulating the styling in a new component.
        Scroller scroller = new Scroller(new Div(personalInformation, employmentInformation));
        scroller.setHeight(150, Unit.PIXELS);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding", "var(--lumo-space-m)");
        add(scroller);






    }
}
