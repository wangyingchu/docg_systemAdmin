package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.TimeFlowCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class CreateTimeFlowView extends VerticalLayout {
    private H6 errorMessage;
    private Dialog containerDialog;
    private TextField timeFlowNameField;
    private Button confirmButton;
    private boolean createDefaultTimeFlowMode = false;

    public CreateTimeFlowView() {
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("时间流信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.timeFlowNameField = new TextField("时间流名称 - Time Flow Name");
        this.timeFlowNameField.setWidthFull();
        this.timeFlowNameField.setRequired(true);
        this.timeFlowNameField.setRequiredIndicatorVisible(true);
        this.timeFlowNameField.setTitle("请输入时间流名称");
        add(timeFlowNameField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        confirmButton = new Button("确认创建时间流",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateTimeFlow();
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<TimeFlow> existingTimeFlowList = coreRealm.getTimeFlows();
        if(existingTimeFlowList.size() == 0){
            this.timeFlowNameField.setEnabled(false);
            this.confirmButton.setText("确认创建默认时间流");
            this.createDefaultTimeFlowMode = true;
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        super.onDetach(detachEvent);
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

    private void doCreateTimeFlow(){
        if(createDefaultTimeFlowMode){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            TimeFlow defaultTimeFlow = coreRealm.getOrCreateTimeFlow();
            if(defaultTimeFlow != null){
                TimeFlowCreatedEvent timeFlowCreatedEvent = new TimeFlowCreatedEvent();
                timeFlowCreatedEvent.setTimeFlowName(defaultTimeFlow.getTimeFlowName());
                ResourceHolder.getApplicationBlackboard().fire(timeFlowCreatedEvent);
                if(this.containerDialog != null){
                    this.containerDialog.close();
                }
                CommonUIOperationUtil.showPopupNotification("默认时间流创建成功", NotificationVariant.LUMO_SUCCESS);
            }else{
                CommonUIOperationUtil.showPopupNotification("默认时间流创建失败", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            String targetTimeFlowName = this.timeFlowNameField.getValue();
            boolean inputValid = true;
            if(targetTimeFlowName.equals("")){
                showErrorMessage("请输入时间流名称");
                CommonUIOperationUtil.showPopupNotification("时间流名称输入错误",NotificationVariant.LUMO_ERROR);
                inputValid = false;
            }else{
                List<TimeFlow> existingTimeFlowList = coreRealm.getTimeFlows();
                for(TimeFlow currentTimeFlow : existingTimeFlowList){
                    String currentTimeFlowName = currentTimeFlow.getTimeFlowName();
                    if(targetTimeFlowName.equalsIgnoreCase(currentTimeFlowName)){
                        showErrorMessage("名称为 "+targetTimeFlowName+" 的时间流已经存在");
                        CommonUIOperationUtil.showPopupNotification("名称为 "+targetTimeFlowName+" 的时间流已经存在",NotificationVariant.LUMO_ERROR);
                        inputValid = false;
                        break;
                    }
                }
            }
            if(inputValid){
                hideErrorMessage();
                TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(targetTimeFlowName);
                if(targetTimeFlow != null){
                    TimeFlowCreatedEvent timeFlowCreatedEvent = new TimeFlowCreatedEvent();
                    timeFlowCreatedEvent.setTimeFlowName(targetTimeFlow.getTimeFlowName());
                    ResourceHolder.getApplicationBlackboard().fire(timeFlowCreatedEvent);
                    if(this.containerDialog != null){
                        this.containerDialog.close();
                    }
                    CommonUIOperationUtil.showPopupNotification("时间流 "+targetTimeFlowName+" 创建成功", NotificationVariant.LUMO_SUCCESS);
                }else{
                    CommonUIOperationUtil.showPopupNotification("时间流 "+targetTimeFlowName+" 创建失败", NotificationVariant.LUMO_ERROR);
                }
            }
        }
    }
}
