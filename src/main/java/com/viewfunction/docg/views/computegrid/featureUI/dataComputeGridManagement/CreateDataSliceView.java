package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionKindCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;

public class CreateDataSliceView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField conceptionKindNameField;
    private TextField conceptionKindDescField;
    private DataSlicePropertiesConfigView dataSlicePropertiesConfigView;

    public CreateDataSliceView(){
        this.setWidthFull();
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle1 = new H6("数据切片信息");
        messageContainerLayout.add(viewTitle1);

        this.conceptionKindNameField = new TextField("数据切片名称 - Data Slice Name");
        this.conceptionKindNameField.setWidthFull();
        this.conceptionKindNameField.setRequired(true);
        this.conceptionKindNameField.setRequiredIndicatorVisible(true);
        this.conceptionKindNameField.setTitle("请输入数据切片名称");
        add(conceptionKindNameField);

        this.conceptionKindDescField = new TextField("数据切片分组 - Data Slice Group");
        this.conceptionKindDescField.setWidthFull();
        this.conceptionKindDescField.setRequired(true);
        this.conceptionKindDescField.setRequiredIndicatorVisible(true);
        this.conceptionKindDescField.setTitle("请输入数据切片分组");
        add(conceptionKindDescField);

        HorizontalLayout storageModeLayout = new HorizontalLayout();
        storageModeLayout.setMargin(false);
        add(storageModeLayout);

        RadioButtonGroup<String> dataStorageModeRadioButtonGroupLabel = new RadioButtonGroup<>("数据存储类型");
        storageModeLayout.add(dataStorageModeRadioButtonGroupLabel);
        RadioButtonGroup<String> dataStorageModeRadioButtonGroup = new RadioButtonGroup<>();
        dataStorageModeRadioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_HELPER_ABOVE_FIELD);
        List<String> dataStorageModeList = new ArrayList<>();
        dataStorageModeList.add("Grid");
        dataStorageModeList.add("PerUnit");
        dataStorageModeRadioButtonGroup.setItems(dataStorageModeList);
        dataStorageModeRadioButtonGroup.setValue("Grid");
        storageModeLayout.add(dataStorageModeRadioButtonGroup);

        H6 viewTitle2 = new H6("数据切片属性");
        add(viewTitle2);

        this.dataSlicePropertiesConfigView = new DataSlicePropertiesConfigView();
        add(dataSlicePropertiesConfigView);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidthFull();
        spaceDivLayout1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout1);

        Button confirmButton = new Button("确认创建数据切片",new Icon(VaadinIcon.CHECK_CIRCLE));
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
        }
        if(conceptionKindDesc.equals("")){
            inputValidateResult = false;
            this.conceptionKindDescField.setInvalid(true);
        }
        if(inputValidateResult){
            hideErrorMessage();
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKindName);
            if(targetConceptionKind != null){
                this.conceptionKindNameField.setInvalid(true);
                showErrorMessage("概念类型 "+conceptionKindName+" 已经存在");
            }else{
                targetConceptionKind = coreRealm.createConceptionKind(conceptionKindName,conceptionKindDesc);
                if(targetConceptionKind != null){
                    ConceptionKindCreatedEvent conceptionKindCreatedEvent = new ConceptionKindCreatedEvent();
                    conceptionKindCreatedEvent.setConceptionKindName(targetConceptionKind.getConceptionKindName());
                    conceptionKindCreatedEvent.setConceptionKindDesc(targetConceptionKind.getConceptionKindDesc());
                    conceptionKindCreatedEvent.setCreateDateTime(targetConceptionKind.getCreateDateTime());
                    conceptionKindCreatedEvent.setLastModifyDateTime(targetConceptionKind.getLastModifyDateTime());
                    conceptionKindCreatedEvent.setCreatorId(targetConceptionKind.getCreatorId());
                    conceptionKindCreatedEvent.setDataOrigin(targetConceptionKind.getDataOrigin());
                    ResourceHolder.getApplicationBlackboard().fire(conceptionKindCreatedEvent);
                    if(this.containerDialog != null){
                        this.containerDialog.close();
                    }
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKindName+" 创建成功", NotificationVariant.LUMO_SUCCESS);
                }
            }
        }else{
            showErrorMessage("请输入概念类型名称和概念类型描述");
            CommonUIOperationUtil.showPopupNotification("概念类型信息输入错误",NotificationVariant.LUMO_ERROR);
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
