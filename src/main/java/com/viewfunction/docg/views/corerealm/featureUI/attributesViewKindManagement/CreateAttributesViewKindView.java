package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;

public class CreateAttributesViewKindView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField attributeKindNameField;
    private TextField attributeKindDescField;
    private ComboBox<AttributesViewKind.AttributesViewKindDataForm> attributeDataTypeFilterSelect;
    public CreateAttributesViewKindView(){
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("属性视图类型信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.attributeKindNameField = new TextField("属性视图类型名称 - AttributesViewKind Name");
        this.attributeKindNameField.setWidthFull();
        this.attributeKindNameField.setRequired(true);
        this.attributeKindNameField.setRequiredIndicatorVisible(true);
        this.attributeKindNameField.setTitle("请输入属性视图类型名称");
        add(this.attributeKindNameField);

        this.attributeKindDescField = new TextField("属性视图类型描述 - AttributesViewKind Description");
        this.attributeKindDescField.setWidthFull();
        this.attributeKindDescField.setRequired(true);
        this.attributeKindDescField.setRequiredIndicatorVisible(true);
        this.attributeKindDescField.setTitle("请输入属性视图类型描述");
        add(attributeKindDescField);

        this.attributeDataTypeFilterSelect = new ComboBox("属性视图类型数据存储结构 - AttributesViewKind Data Form");
        this.attributeDataTypeFilterSelect.setRequired(true);
        this.attributeDataTypeFilterSelect.setWidthFull();
        this.attributeDataTypeFilterSelect.setPageSize(30);
        this.attributeDataTypeFilterSelect.setPlaceholder("请选择属性视图类型数据存储结构");

        AttributesViewKind.AttributesViewKindDataForm[] attributeDataTypesArray =
                new AttributesViewKind.AttributesViewKindDataForm[]{
                        AttributesViewKind.AttributesViewKindDataForm.SINGLE_VALUE,
                        AttributesViewKind.AttributesViewKindDataForm.LIST_VALUE,
                        AttributesViewKind.AttributesViewKindDataForm.RELATED_VALUE,
                        AttributesViewKind.AttributesViewKindDataForm.EXTERNAL_VALUE
                };
        this.attributeDataTypeFilterSelect.setItems(attributeDataTypesArray);
        add(this.attributeDataTypeFilterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建属性视图类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //doCreateNewAttributeKind();
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
