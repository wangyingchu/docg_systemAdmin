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
    private TextField relationAttachKindNameField;
    private TextField relationAttachKindDescField;
    private ComboBox<String> sourceConceptionKindFilterSelect;
    private ComboBox<String> relationKindFilterSelect;
    private ComboBox<String> targetConceptionKindFilterSelect;
    private Checkbox allowRepeatableRelationKindCheckbox;

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

        this.relationAttachKindNameField = new TextField("关系附着规则类型名称 - RelationAttachKind Name");
        this.relationAttachKindNameField.setWidthFull();
        this.relationAttachKindNameField.setRequired(true);
        this.relationAttachKindNameField.setRequiredIndicatorVisible(true);
        this.relationAttachKindNameField.setTitle("请输入关系附着规则类型名称");
        add(relationAttachKindNameField);

        this.relationAttachKindDescField = new TextField("关系附着规则类型描述 - RelationAttachKind Description");
        this.relationAttachKindDescField.setWidthFull();
        this.relationAttachKindDescField.setRequired(true);
        this.relationAttachKindDescField.setRequiredIndicatorVisible(true);
        this.relationAttachKindDescField.setTitle("请输入关系附着规则类型描述");
        add(relationAttachKindDescField);

        this.sourceConceptionKindFilterSelect = new ComboBox("源概念类型名称 - Source ConceptionKind Name");
        this.sourceConceptionKindFilterSelect.setPageSize(30);
        this.sourceConceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.sourceConceptionKindFilterSelect.setRequiredIndicatorVisible(true);
        add(this.sourceConceptionKindFilterSelect);

        this.relationKindFilterSelect = new ComboBox("关系类型名称 - RelationKind Name");
        this.relationKindFilterSelect.setPageSize(30);
        this.relationKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.relationKindFilterSelect.setRequiredIndicatorVisible(true);
        add(this.relationKindFilterSelect);

        this.targetConceptionKindFilterSelect = new ComboBox("目标概念类型名称 - Target ConceptionKind Name");
        this.targetConceptionKindFilterSelect.setPageSize(30);
        this.targetConceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.targetConceptionKindFilterSelect.setRequiredIndicatorVisible(true);
        add(this.targetConceptionKindFilterSelect);

        this.allowRepeatableRelationKindCheckbox = new Checkbox("允许重复创建相同类型的关系实例");
        this.allowRepeatableRelationKindCheckbox.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        add(this.allowRepeatableRelationKindCheckbox);

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
                if(sourceConceptionKindFilterSelect.getValue()==null){
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
