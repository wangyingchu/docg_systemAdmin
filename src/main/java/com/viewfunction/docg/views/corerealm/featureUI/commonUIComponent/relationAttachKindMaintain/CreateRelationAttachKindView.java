package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CreateRelationAttachKindView extends VerticalLayout {
    private Dialog containerDialog;
    private NativeLabel errorMessage;

    private TextField conceptionKindNameField;
    private TextField conceptionKindDescField;

    private ComboBox<String> attributeKindFilterSelect;
    private ComboBox<String> attributeKindFilterSelect2;
    private ComboBox<String> attributeKindFilterSelect3;

    public CreateRelationAttachKindView(){

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("新建关系附着规则类型信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        this.conceptionKindNameField = new TextField("关系附着规则类型名称 - RelationAttachKind Name");
        this.conceptionKindNameField.setWidthFull();
        this.conceptionKindNameField.setRequired(true);
        this.conceptionKindNameField.setRequiredIndicatorVisible(true);
        this.conceptionKindNameField.setTitle("请输入关系附着规则类型名称");
        add(conceptionKindNameField);

        this.conceptionKindDescField = new TextField("关系附着规则类型描述 - RelationAttachKind Description");
        this.conceptionKindDescField.setWidthFull();
        this.conceptionKindDescField.setRequired(true);
        this.conceptionKindDescField.setRequiredIndicatorVisible(true);
        this.conceptionKindDescField.setTitle("请输入关系附着规则类型描述");
        add(conceptionKindDescField);

        this.attributeKindFilterSelect = new ComboBox("关系附着规则类型描述 - RelationAttachKind Description");
        this.attributeKindFilterSelect.setPageSize(30);
        //this.attributeKindFilterSelect.setPlaceholder("选择要附加的属性类型");
        this.attributeKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.attributeKindFilterSelect.setRequiredIndicatorVisible(true);
        add(this.attributeKindFilterSelect);

        this.attributeKindFilterSelect2 = new ComboBox("关系附着规则类型描述 - RelationAttachKind Description");
        this.attributeKindFilterSelect2.setPageSize(30);
        //this.attributeKindFilterSelect2.setPlaceholder("选择要附加的属性类型");
        this.attributeKindFilterSelect2.setWidth(100, Unit.PERCENTAGE);
        this.attributeKindFilterSelect2.setRequiredIndicatorVisible(true);
        add(this.attributeKindFilterSelect2);

        this.attributeKindFilterSelect3 = new ComboBox("关系附着规则类型描述 - RelationAttachKind Description");
        this.attributeKindFilterSelect3.setPageSize(30);
        //this.attributeKindFilterSelect3.setPlaceholder("选择要附加的属性类型");
        this.attributeKindFilterSelect3.setWidth(100, Unit.PERCENTAGE);
        this.attributeKindFilterSelect3.setRequiredIndicatorVisible(true);
        add(this.attributeKindFilterSelect3);

        Checkbox allowRepeatableRelationKind = new Checkbox("Allow repeat");
        add(allowRepeatableRelationKind);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定创建关系附着规则类型",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

                errorMessage.setVisible(false);
                if(attributeKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择属性类型");
                    errorMessage.setVisible(true);
                }else{
                    //doAttachAttributesViewKind(attributeKindFilterSelect.getValue());
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
