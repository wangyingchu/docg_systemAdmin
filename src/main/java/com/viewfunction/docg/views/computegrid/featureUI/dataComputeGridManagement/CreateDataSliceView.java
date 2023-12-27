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

import com.viewfunction.docg.dataCompute.computeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.computeServiceCore.internal.ignite.ComputeGridObserver;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.computeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.eventHandling.DataSliceCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.*;

public class CreateDataSliceView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField dataSliceNameField;
    private TextField dataSliceGroupField;
    private DataSlicePropertiesConfigView dataSlicePropertiesConfigView;
    private RadioButtonGroup<String> dataStorageModeRadioButtonGroup;

    public CreateDataSliceView(){
        this.setWidthFull();
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle1 = new H6("数据切片信息");
        messageContainerLayout.add(viewTitle1);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        messageContainerLayout.add(errorMessage);

        this.dataSliceNameField = new TextField("数据切片名称 - Data Slice Name");
        this.dataSliceNameField.setWidthFull();
        this.dataSliceNameField.setRequired(true);
        this.dataSliceNameField.setRequiredIndicatorVisible(true);
        this.dataSliceNameField.setTitle("请输入数据切片名称");
        add(dataSliceNameField);

        this.dataSliceGroupField = new TextField("数据切片分组 - Data Slice Group");
        this.dataSliceGroupField.setWidthFull();
        this.dataSliceGroupField.setRequired(true);
        this.dataSliceGroupField.setRequiredIndicatorVisible(true);
        this.dataSliceGroupField.setTitle("请输入数据切片分组");
        add(dataSliceGroupField);

        HorizontalLayout storageModeLayout = new HorizontalLayout();
        storageModeLayout.setMargin(false);
        add(storageModeLayout);

        RadioButtonGroup<String> dataStorageModeRadioButtonGroupLabel = new RadioButtonGroup<>("数据存储类型");
        storageModeLayout.add(dataStorageModeRadioButtonGroupLabel);
        dataStorageModeRadioButtonGroup = new RadioButtonGroup<>();
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
        add(this.dataSlicePropertiesConfigView);

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
                doCreateDataSlice();
            }
        });
    }

    private void doCreateDataSlice(){
        String dataSliceName = this.dataSliceNameField.getValue();
        String dataSliceGroup = this.dataSliceGroupField.getValue();
        String storageMode = dataStorageModeRadioButtonGroup.getValue();
        boolean inputValidateResult = true;
        if(dataSliceName.equals("")){
            inputValidateResult = false;
            this.dataSliceNameField.setInvalid(true);
        }
        if(dataSliceGroup.equals("")){
            inputValidateResult = false;
            this.dataSliceGroupField.setInvalid(true);
        }
        Collection<DataSlicePropertyValueObject> dataSlicePropertyValueObjectsCollection = this.dataSlicePropertiesConfigView.getDataSlicePropertyValueObjects();
        if(dataSlicePropertyValueObjectsCollection == null || dataSlicePropertyValueObjectsCollection.size() == 0){
            inputValidateResult = false;
        }

        if(inputValidateResult){
            hideErrorMessage();
            ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
            try {
                Set<DataSliceMetaInfo> dataSliceMetaInfoSet = targetComputeGrid.listDataSlice();
                if(dataSliceMetaInfoSet != null){
                    for(DataSliceMetaInfo currentDataSliceMetaInfo : dataSliceMetaInfoSet){
                        if(dataSliceName.toUpperCase().equals(currentDataSliceMetaInfo.getDataSliceName().toUpperCase())){
                            showErrorMessage("数据切片 "+dataSliceName+" 已经存在");
                            return;
                        }
                    }
                }
            } catch (ComputeGridException e) {
                throw new RuntimeException(e);
            }
            try(ComputeGridObserver computeGridObserver = ComputeGridObserver.getObserverInstance()) {
                Map<String, DataSlicePropertyType> dataSlicePropertyMap = new HashMap<>();
                List<String> pkList = new ArrayList<>();
                for(DataSlicePropertyValueObject currentDataSlicePropertyValueObject:dataSlicePropertyValueObjectsCollection){
                    dataSlicePropertyMap.put(currentDataSlicePropertyValueObject.getPropertyName(),currentDataSlicePropertyValueObject.getDataSlicePropertyType());
                    if(currentDataSlicePropertyValueObject.isPrimaryKey()){
                        pkList.add(currentDataSlicePropertyValueObject.getPropertyName());
                    }
                }

                DataSliceMetaInfo targetDataSlice = null;
                if(storageMode.equals("Grid")){
                    targetDataSlice = computeGridObserver.createGridDataSlice(dataSliceName, dataSliceGroup,dataSlicePropertyMap,pkList);
                }else if(storageMode.equals("PerUnit")){
                    targetDataSlice = computeGridObserver.createPerUnitDataSlice(dataSliceName, dataSliceGroup,dataSlicePropertyMap,pkList);
                }

                if(targetDataSlice != null & targetDataSlice.getDataSliceName().equals(dataSliceName)){
                    DataSliceCreatedEvent dataSliceCreatedEvent = new DataSliceCreatedEvent();
                    dataSliceCreatedEvent.setDataSliceName(targetDataSlice.getDataSliceName());
                    dataSliceCreatedEvent.setDataSliceGroup(targetDataSlice.getSliceGroupName());
                    dataSliceCreatedEvent.setDataSliceMetaInfo(targetDataSlice);
                    ResourceHolder.getApplicationBlackboard().fire(dataSliceCreatedEvent);
                    if(this.containerDialog != null){
                        this.containerDialog.close();
                    }
                    CommonUIOperationUtil.showPopupNotification("数据切片 "+dataSliceName+" 创建成功", NotificationVariant.LUMO_SUCCESS);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            showErrorMessage("请输入数据切片名称，数据切片分组和至少一项数据切片属性");
            CommonUIOperationUtil.showPopupNotification("数据切片信息输入错误",NotificationVariant.LUMO_ERROR);
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
