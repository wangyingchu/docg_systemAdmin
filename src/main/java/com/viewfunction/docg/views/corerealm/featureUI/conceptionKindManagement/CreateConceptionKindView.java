package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CreateConceptionKindView extends VerticalLayout {

    private TextField conceptionKindNameField;
    private TextField conceptionKindDescField;
    private H6 errorMessage;

    public CreateConceptionKindView(){
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("概念类型信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("请输入概念类型名称和概念类型描述");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.conceptionKindNameField = new TextField("概念类型名称 - ConceptionKind Name");
        this.conceptionKindNameField.setWidthFull();
        this.conceptionKindNameField.setRequired(true);
        this.conceptionKindNameField.setRequiredIndicatorVisible(true);
        this.conceptionKindNameField.setTitle("请输入概念类型名称");
        add(conceptionKindNameField);

        this.conceptionKindDescField = new TextField("概念类型描述 - ConceptionKind Description");
        this.conceptionKindDescField.setWidthFull();
        this.conceptionKindDescField.setRequired(true);
        this.conceptionKindDescField.setRequiredIndicatorVisible(true);
        this.conceptionKindDescField.setTitle("请输入概念类型描述");
        add(conceptionKindDescField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建概念类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateNewConceptionKind();
            }
        });
    }

    private void doCreateNewConceptionKind(){
        String conceptionKindName = this.conceptionKindNameField.getValue();
        String conceptionKindDesc = this.conceptionKindDescField.getValue();
        boolean inputValidateResult = true;
        if(conceptionKindName.equals("")){
            inputValidateResult = false;
            this.conceptionKindNameField.setInvalid(true);

            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            Div text = new Div(new Text("概念类型信息输入错误"));
            Button closeButton = new Button(new Icon("lumo", "cross"));
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            closeButton.addClickListener(event -> {
                notification.close();
            });

            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setAlignItems(Alignment.CENTER);
            notification.add(layout);
            notification.open();
        }

        if(conceptionKindDesc.equals("")){
            inputValidateResult = false;
            this.conceptionKindDescField.setInvalid(true);
        }
        if(inputValidateResult){
            errorMessage.setVisible(false);
        }else{
            errorMessage.setVisible(true);
        }
    }
}
