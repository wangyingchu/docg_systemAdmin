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
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.analysisProvider.client.AnalysisProviderAdminClient;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

public class RegisterFunctionalFeatureView extends VerticalLayout {

    private H6 errorMessage;
    private TextField functionalFeatureNameField;
    private TextField functionalFeatureDescField;
    private int ANALYSIS_CLIENT_HOST_PORT;
    private String ANALYSIS_CLIENT_HOST_NAME;
    private Popover containerPopover;
    private RegisterFunctionalFeatureSuccessCallback registerFunctionalFeatureSuccessCallback;

    public interface RegisterFunctionalFeatureSuccessCallback {
        public void onExecutionSuccess(String functionalFeatureName,String functionalFeatureDesc);
    }

    public RegisterFunctionalFeatureView(String ANALYSIS_CLIENT_HOST_NAME,int ANALYSIS_CLIENT_HOST_PORT){
        this.ANALYSIS_CLIENT_HOST_NAME = ANALYSIS_CLIENT_HOST_NAME;
        this.ANALYSIS_CLIENT_HOST_PORT = ANALYSIS_CLIENT_HOST_PORT;
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
        String functionalFeatureName = this.functionalFeatureNameField.getValue();
        String functionalFeatureDesc = this.functionalFeatureDescField.getValue();

        boolean inputValidateResult = true;
        if(functionalFeatureName.equals("")){
            inputValidateResult = false;
            this.functionalFeatureNameField.setInvalid(true);
        }
        if(functionalFeatureDesc.equals("")){
            inputValidateResult = false;
            this.functionalFeatureDescField.setInvalid(true);
        }

        if(inputValidateResult){
            hideErrorMessage();
            AnalysisProviderAdminClient analysisProviderAdminClient = new AnalysisProviderAdminClient(ANALYSIS_CLIENT_HOST_NAME,ANALYSIS_CLIENT_HOST_PORT);
            boolean registerResult = analysisProviderAdminClient.registerFunctionalFeature(functionalFeatureName,functionalFeatureDesc,3);
            if(!registerResult){
                showErrorMessage("分析功能特性 "+functionalFeatureName+" 已经存在");
            }else{
                this.functionalFeatureNameField.setValue("");
                this.functionalFeatureDescField.setValue("");
                hideErrorMessage();
                if(registerFunctionalFeatureSuccessCallback != null){
                    registerFunctionalFeatureSuccessCallback.onExecutionSuccess(functionalFeatureName,functionalFeatureDesc);
                }
                if(this.containerPopover != null){
                    this.containerPopover.close();
                }
                CommonUIOperationUtil.showPopupNotification("分析功能特性 "+functionalFeatureName+" 注册成功", NotificationVariant.LUMO_SUCCESS);
            }
        } else{
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

    public void setContainerPopover(Popover containerPopover) {
        this.containerPopover = containerPopover;
    }

    public void setRegisterFunctionalFeatureSuccessCallback(RegisterFunctionalFeatureSuccessCallback registerFunctionalFeatureSuccessCallback) {
        this.registerFunctionalFeatureSuccessCallback = registerFunctionalFeatureSuccessCallback;
    }
}
