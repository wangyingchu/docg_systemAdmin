package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class QueryResultSetConfigView extends VerticalLayout {

    private Dialog containerDialog;
    TextField conceptionKindNameField;
    TextField conceptionKindDescField;
  //  TextField conceptionKindNameField;

    public QueryResultSetConfigView(){

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("结果集配置参数");
        messageContainerLayout.add(viewTitle);

        H6 errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);


        HorizontalLayout messageTitleLayout = new HorizontalLayout();
        messageTitleLayout.setPadding(false);
        Icon infoIcon = new Icon(VaadinIcon.INFO_CIRCLE);
        infoIcon.setSize("10px");
        infoIcon.getStyle().set("color","var(--lumo-contrast-80pct)");
        messageTitleLayout.add(infoIcon);
        Label attributeTypeLabel = new Label("设置查询最大返回结果数将忽略查询起始页和查询结束页中的设置");
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        messageTitleLayout.add(attributeTypeLabel);
        add(messageTitleLayout);

        this.conceptionKindNameField = new TextField("查询单页返回结果数 - Page Size");
        this.conceptionKindNameField.setPreventInvalidInput(true);
        this.conceptionKindNameField.setSuffixComponent(new Label("HAHAH"));
        //this.conceptionKindNameField.addThemeVariants(TextFieldVariant.LUMO_HELPER_ABOVE_FIELD);

        this.conceptionKindNameField.setWidthFull();
        this.conceptionKindNameField.setRequired(true);
        this.conceptionKindNameField.setRequiredIndicatorVisible(true);
        this.conceptionKindNameField.setTitle("请输入概念类型名称");
        this.conceptionKindNameField.setValue("100");
        add(conceptionKindNameField);

        this.conceptionKindDescField = new TextField("查询起始页 - Start Page");
        this.conceptionKindDescField.setWidthFull();
        this.conceptionKindDescField.setRequired(true);
        this.conceptionKindDescField.setRequiredIndicatorVisible(true);
        this.conceptionKindDescField.setTitle("请输入概念类型描述");
        this.conceptionKindDescField.setValue("1");
        add(conceptionKindDescField);

        TextField conceptionKindDescField2 = new TextField("查询结束页 - End Page");
        conceptionKindDescField2.setWidthFull();
        conceptionKindDescField2.setRequired(true);
        conceptionKindDescField2.setRequiredIndicatorVisible(true);
        conceptionKindDescField2.setTitle("请输入概念类型描述");
        conceptionKindDescField2.setValue("101");
        add(conceptionKindDescField2);

        TextField conceptionKindDescField3 = new TextField("查询最大返回结果数 - Result Number");
        conceptionKindDescField3.setWidthFull();
        conceptionKindDescField3.setRequired(true);
        conceptionKindDescField3.setRequiredIndicatorVisible(true);
        conceptionKindDescField3.setTitle("请输入概念类型描述");
        add(conceptionKindDescField3);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //doCreateNewConceptionKind();
            }
        });
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
