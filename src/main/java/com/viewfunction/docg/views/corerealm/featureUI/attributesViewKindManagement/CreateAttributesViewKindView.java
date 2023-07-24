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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;

import java.util.ArrayList;
import java.util.List;

public class CreateAttributesViewKindView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField attributeViewKindNameField;
    private TextField attributeViewKindDescField;
    private ComboBox<AttributesViewKind.AttributesViewKindDataForm> attributeViewKindDataformSelect;
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

        this.attributeViewKindNameField = new TextField("属性视图类型名称 - AttributesViewKind Name");
        this.attributeViewKindNameField.setWidthFull();
        this.attributeViewKindNameField.setRequired(true);
        this.attributeViewKindNameField.setRequiredIndicatorVisible(true);
        this.attributeViewKindNameField.setTitle("请输入属性视图类型名称");
        add(this.attributeViewKindNameField);

        this.attributeViewKindDescField = new TextField("属性视图类型描述 - AttributesViewKind Description");
        this.attributeViewKindDescField.setWidthFull();
        this.attributeViewKindDescField.setRequired(true);
        this.attributeViewKindDescField.setRequiredIndicatorVisible(true);
        this.attributeViewKindDescField.setTitle("请输入属性视图类型描述");
        add(attributeViewKindDescField);

        this.attributeViewKindDataformSelect = new ComboBox("属性视图类型数据存储结构 - AttributesViewKind Data Form");
        this.attributeViewKindDataformSelect.setRequired(true);
        this.attributeViewKindDataformSelect.setWidthFull();
        this.attributeViewKindDataformSelect.setPageSize(30);
        this.attributeViewKindDataformSelect.setPlaceholder("请选择属性视图类型数据存储结构");

        AttributesViewKind.AttributesViewKindDataForm[] attributeDataTypesArray =
                new AttributesViewKind.AttributesViewKindDataForm[]{
                        AttributesViewKind.AttributesViewKindDataForm.SINGLE_VALUE,
                        AttributesViewKind.AttributesViewKindDataForm.LIST_VALUE,
                        AttributesViewKind.AttributesViewKindDataForm.RELATED_VALUE,
                        AttributesViewKind.AttributesViewKindDataForm.EXTERNAL_VALUE
                };
        this.attributeViewKindDataformSelect.setItems(attributeDataTypesArray);
        this.attributeViewKindDataformSelect.setValue(AttributesViewKind.AttributesViewKindDataForm.SINGLE_VALUE);
        add(this.attributeViewKindDataformSelect);

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
                doCreateNewAttributesViewKind();
            }
        });
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void doCreateNewAttributesViewKind(){
        String attributeViewKindName = this.attributeViewKindNameField.getValue();
        String attributeViewKindDesc = this.attributeViewKindDescField.getValue();
        AttributesViewKind.AttributesViewKindDataForm attributesViewKindDataForm = this.attributeViewKindDataformSelect.getValue();

        if(attributeViewKindName.equals("")||attributeViewKindDesc.equals("")||attributesViewKindDataForm== null){
            showErrorMessage("请输入全部属性视图类型定义信息");
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            List<AttributesViewKind> attributesViewKindList = coreRealm.getAttributesViewKinds(attributeViewKindName,attributeViewKindDesc,attributesViewKindDataForm);
            if(attributesViewKindList != null && attributesViewKindList.size() > 0){
                List<Button> actionButtonList = new ArrayList<>();
                Button confirmButton = new Button("确认创建属性视图类型",new Icon(VaadinIcon.CHECK_CIRCLE));
                confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("取消操作");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
                actionButtonList.add(confirmButton);
                actionButtonList.add(cancelButton);

                ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","相同定义内容的属性视图类型已经存在，请确认是否继续执行创建操作",actionButtonList,650,190);
                confirmWindow.open();
                confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        doCreateAttributeKind(attributeViewKindName,attributeViewKindDesc,attributesViewKindDataForm,confirmWindow);
                    }
                });
                cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        confirmWindow.closeConfirmWindow();
                    }
                });
            }else{
                doCreateAttributeKind(attributeViewKindName,attributeViewKindDesc,attributesViewKindDataForm,null);
            }
        }
    }

    private void doCreateAttributeKind(String attributeViewKindName,String attributeViewKindDesc,
                                       AttributesViewKind.AttributesViewKindDataForm attributesViewKindDataForm,ConfirmWindow confirmWindow){
        if(confirmWindow != null){
            confirmWindow.closeConfirmWindow();
        }
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributesViewKind attributesViewKind = coreRealm.createAttributesViewKind(attributeViewKindName,attributeViewKindDesc,attributesViewKindDataForm);
        if(attributesViewKind != null){

        }
    }
}
