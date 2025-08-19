package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;

public class RegisterKindActionView extends VerticalLayout {

    private String kindName;
    private KindActionsDateView.KindType kindType;
    private TextField actionNameField;
    private TextField actionDescField;
    private TextField actionFullClassNameField;
    private H6 errorMessage;
    private Popover containerPopover;
    private KindActionsDateView parentKindActionsDateView;

    public RegisterKindActionView(KindActionsDateView.KindType kindType, String kindName){
        this.kindName = kindName;
        this.kindType = kindType;
        this.setMargin(false);
        this.setWidthFull();

        Icon kindIcon = null;
        switch (this.kindType){
            case ConceptionKind ->kindIcon = VaadinIcon.CUBE.create();
            case RelationKind ->kindIcon = VaadinIcon.CONNECT_O.create();
        }

        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.kindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);
        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.actionNameField = new TextField("自定义动作名称 - Action Name");
        this.actionNameField.setWidthFull();
        this.actionNameField.setRequired(true);
        this.actionNameField.setRequiredIndicatorVisible(true);
        this.actionNameField.setTitle("请输入自定义动作名称");
        add(actionNameField);

        this.actionDescField = new TextField("自定义动作名称描述 - Action Description");
        this.actionDescField.setWidthFull();
        this.actionDescField.setRequired(true);
        this.actionDescField.setRequiredIndicatorVisible(true);
        this.actionDescField.setTitle("请输入自定义动作名称");
        add(actionDescField);

        this.actionFullClassNameField = new TextField("自定义动作类全名 - Action Implementation Class Full Name");
        this.actionFullClassNameField.setWidthFull();
        this.actionFullClassNameField.setRequired(true);
        this.actionFullClassNameField.setRequiredIndicatorVisible(true);
        this.actionFullClassNameField.setTitle("请输入自定义动作类全名");
        add(actionFullClassNameField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认注册自定义动作",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doRegisterKindAction();
            }
        });
    }

    public void setContainerPopover(Popover containerPopover) {
        this.containerPopover = containerPopover;
    }

    private void doRegisterKindAction(){
        String actionName = this.actionNameField.getValue();
        String actionDesc = this.actionDescField.getValue();
        String actionFullClassName = this.actionFullClassNameField.getValue();

        boolean inputValidateResult = true;
        if(actionName.equals("")){
            inputValidateResult = false;
            this.actionNameField.setInvalid(true);
        }
        if(actionDesc.equals("")){
            inputValidateResult = false;
            this.actionDescField.setInvalid(true);
        }
        if(actionFullClassName.equals("")){
            inputValidateResult = false;
            this.actionFullClassNameField.setInvalid(true);
        }

        if(inputValidateResult){
            hideErrorMessage();
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            switch (this.kindType){
                case ConceptionKind :
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
                    if(targetConceptionKind.getAction(actionName) != null){
                        this.actionNameField.setInvalid(true);
                        showErrorMessage("概念类型 "+kindName+" 中已经注册了自定义动作 "+actionName);
                    }else{
                        try {
                            boolean registerResult = targetConceptionKind.registerAction(actionName,actionDesc,actionFullClassName);
                            if(registerResult){
                                if(this.containerPopover != null){
                                    this.containerPopover.close();
                                }
                                CommonUIOperationUtil.showPopupNotification("自定义动作 "+actionName+" 注册成功", NotificationVariant.LUMO_SUCCESS);
                                if(this.parentKindActionsDateView != null){
                                    this.parentKindActionsDateView.refreshKindActionsInfo();
                                }
                            }
                        } catch (CoreRealmServiceRuntimeException e) {
                            e.printStackTrace();
                            CommonUIOperationUtil.showPopupNotification("自定义动作信息注册错误",NotificationVariant.LUMO_ERROR);
                        }
                    }
                    break;
                case RelationKind : break;
            }
            coreRealm.closeGlobalSession();
        }else{
            showErrorMessage("请输入全部必要信息");
            CommonUIOperationUtil.showPopupNotification("自定义动作信息输入错误",NotificationVariant.LUMO_ERROR);
        }
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void hideErrorMessage(){
        this.errorMessage.setVisible(false);
    }

    public void setParentKindActionsDateView(KindActionsDateView parentKindActionsDateView) {
        this.parentKindActionsDateView = parentKindActionsDateView;
    }
}
