package com.viewfunction.docg.views.dataAnalysis.featureUI;

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
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

public class RegisterFunctionalFeatureView extends VerticalLayout {

    private H6 errorMessage;
    private TextField functionalFeatureNameField;
    private TextField functionalFeatureDescField;

    public RegisterFunctionalFeatureView(){
        this.setWidthFull();
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle1 = new H6("分析功能特性信息");
        messageContainerLayout.add(viewTitle1);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        messageContainerLayout.add(errorMessage);

        this.functionalFeatureNameField = new TextField("分析功能特性名称 - functional Feature Name");
        this.functionalFeatureNameField.setWidthFull();
        this.functionalFeatureNameField.setRequired(true);
        this.functionalFeatureNameField.setRequiredIndicatorVisible(true);
        this.functionalFeatureNameField.setTitle("请输入分析功能特性名称");
        add(functionalFeatureNameField);

        this.functionalFeatureDescField = new TextField("分析功能特性描述 - functionalFeature Description");
        this.functionalFeatureDescField.setWidthFull();
        this.functionalFeatureDescField.setRequired(true);
        this.functionalFeatureDescField.setRequiredIndicatorVisible(true);
        this.functionalFeatureDescField.setTitle("请输入分析功能特性描述");
        add(functionalFeatureDescField);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidthFull();
        spaceDivLayout1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout1);

        Button confirmButton = new Button("确认注册分析功能特性",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doRegisterFunctionalFeature();
            }
        });
    }

    private void doRegisterFunctionalFeature(){
        String dataSliceName = this.functionalFeatureNameField.getValue();
        String dataSliceGroup = this.functionalFeatureDescField.getValue();

        boolean inputValidateResult = true;
        if(dataSliceName.equals("")){
            inputValidateResult = false;
            this.functionalFeatureNameField.setInvalid(true);
        }
        if(dataSliceGroup.equals("")){
            inputValidateResult = false;
            this.functionalFeatureDescField.setInvalid(true);
        }

        if(inputValidateResult){
            hideErrorMessage();

            /*
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
                CommonUIOperationUtil.showPopupNotification("创建数据切片异常: "+e.getMessage(), NotificationVariant.LUMO_ERROR);
                throw new RuntimeException(e);
            }
            */
        }else{
            showErrorMessage("请输入分析功能特性名称和描述信息");
            CommonUIOperationUtil.showPopupNotification("分析功能特性信息输入错误",NotificationVariant.LUMO_ERROR);
        }
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void hideErrorMessage(){
        this.errorMessage.setVisible(false);
    }
}
