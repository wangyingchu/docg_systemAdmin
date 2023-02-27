package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

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

import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.RelationKindCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class CreateRelationKindView extends VerticalLayout {

    private TextField relationKindNameField;
    private TextField relationKindDescField;
    private H6 errorMessage;
    private Dialog containerDialog;

    public CreateRelationKindView(){
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("关系类型信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.relationKindNameField = new TextField("关系类型名称 - RelationKind Name");
        this.relationKindNameField.setWidthFull();
        this.relationKindNameField.setRequired(true);
        this.relationKindNameField.setRequiredIndicatorVisible(true);
        this.relationKindNameField.setTitle("请输入关系类型名称");
        add(relationKindNameField);

        this.relationKindDescField = new TextField("关系类型描述 - RelationKind Description");
        this.relationKindDescField.setWidthFull();
        this.relationKindDescField.setRequired(true);
        this.relationKindDescField.setRequiredIndicatorVisible(true);
        this.relationKindDescField.setTitle("请输入关系类型描述");
        add(relationKindDescField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建关系类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateNewRelationKind();
            }
        });
    }

    private void doCreateNewRelationKind(){
        String relationKindName = this.relationKindNameField.getValue();
        String relationKindDesc = this.relationKindDescField.getValue();
        boolean inputValidateResult = true;
        if(relationKindName.equals("")){
            inputValidateResult = false;
            this.relationKindNameField.setInvalid(true);
        }
        if(relationKindDesc.equals("")){
            inputValidateResult = false;
            this.relationKindDescField.setInvalid(true);
        }
        if(inputValidateResult){
            hideErrorMessage();
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            RelationKind targetRelationKind = coreRealm.getRelationKind(relationKindName);
            if(targetRelationKind != null){
                this.relationKindNameField.setInvalid(true);
                showErrorMessage("概念类型 "+relationKindName+" 已经存在");
            }else{
                targetRelationKind = coreRealm.createRelationKind(relationKindName,relationKindDesc);
                if(targetRelationKind != null){
                    RelationKindCreatedEvent relationKindCreatedEvent = new RelationKindCreatedEvent();
                    relationKindCreatedEvent.setRelationKindName(targetRelationKind.getRelationKindName());
                    relationKindCreatedEvent.setRelationKindDesc(targetRelationKind.getRelationKindDesc());
                    relationKindCreatedEvent.setCreateDateTime(targetRelationKind.getCreateDateTime());
                    relationKindCreatedEvent.setLastModifyDateTime(targetRelationKind.getLastModifyDateTime());
                    relationKindCreatedEvent.setCreatorId(targetRelationKind.getCreatorId());
                    relationKindCreatedEvent.setDataOrigin(targetRelationKind.getDataOrigin());
                    ResourceHolder.getApplicationBlackboard().fire(relationKindCreatedEvent);
                    if(this.containerDialog != null){
                        this.containerDialog.close();
                    }
                    CommonUIOperationUtil.showPopupNotification("关系类型 "+relationKindName+" 创建成功", NotificationVariant.LUMO_SUCCESS);
                }
            }
        }else{
            showErrorMessage("请输入关系类型名称和概念类型描述");
            CommonUIOperationUtil.showPopupNotification("关系类型信息输入错误",NotificationVariant.LUMO_ERROR);
        }
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void hideErrorMessage(){
        this.errorMessage.setVisible(false);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
