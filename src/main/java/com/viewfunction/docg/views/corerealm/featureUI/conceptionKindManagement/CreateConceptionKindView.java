package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CreateConceptionKindView extends VerticalLayout {

    public CreateConceptionKindView(){

        this.setWidthFull();

        H6 viewTitle = new H6("概念类型信息");
        add(viewTitle);

        TextField conceptionKindNameField = new TextField("概念类型名称 - ConceptionKind Name");
        conceptionKindNameField.setWidthFull();
        add(conceptionKindNameField);

        TextField conceptionKindDescField = new TextField("概念类型描述 - ConceptionKind Description");
        conceptionKindDescField.setWidthFull();
        add(conceptionKindDescField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建概念类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
    }
}
