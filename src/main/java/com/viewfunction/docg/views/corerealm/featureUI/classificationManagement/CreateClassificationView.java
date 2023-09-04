package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

public class CreateClassificationView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField attributeViewKindNameField;
    private TextField attributeViewKindDescField;

    public CreateClassificationView(){
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("分类信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.attributeViewKindNameField = new TextField("分类名称 - Classification Name");
        this.attributeViewKindNameField.setWidthFull();
        this.attributeViewKindNameField.setRequired(true);
        this.attributeViewKindNameField.setRequiredIndicatorVisible(true);
        this.attributeViewKindNameField.setTitle("请输入分类名称");
        add(this.attributeViewKindNameField);

        this.attributeViewKindDescField = new TextField("分类描述 - Classification Description");
        this.attributeViewKindDescField.setWidthFull();
        this.attributeViewKindDescField.setRequired(true);
        this.attributeViewKindDescField.setRequiredIndicatorVisible(true);
        this.attributeViewKindDescField.setTitle("请输入分类描述");
        add(attributeViewKindDescField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建分类",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateNewClassification();
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

    private void doCreateNewClassification(){
        String classificationName = this.attributeViewKindNameField.getValue();
        String classificationDesc = this.attributeViewKindDescField.getValue();
        if(classificationName.equals("")||classificationDesc.equals("")){
            showErrorMessage("请输入全部分类定义信息");
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            Classification targetClassification = coreRealm.getClassification(classificationName);
            if(targetClassification != null){
                CommonUIOperationUtil.showPopupNotification("分类 "+classificationName+"["+classificationDesc+"]"+" 已经存在", NotificationVariant.LUMO_WARNING);
            }else{
                doCreateClassification(classificationName,classificationDesc);
            }
        }
    }

    private void doCreateClassification(String classificationName,String classificationDesc){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification classification = coreRealm.createClassification(classificationName,classificationDesc);
        if(classification != null){
            CommonUIOperationUtil.showPopupNotification("分类 "+classification.getClassificationName()+"["+classification.getClassificationDesc()+"]"+" 创建成功", NotificationVariant.LUMO_SUCCESS);
            if(this.containerDialog != null){
                this.containerDialog.close();
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("分类 "+classification.getClassificationName()+"["+classification.getClassificationDesc()+"]"+" 创建失败", NotificationVariant.LUMO_ERROR);
        }
    }
}
