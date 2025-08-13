package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.externalValueViewKindConfig;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;

import java.util.ArrayList;
import java.util.List;

public class CustomizedDataSourceConfigView extends VerticalLayout {

    private AttributesViewKind attributesViewKind;
    private TextField implementationClassFullNameField;
    private Dialog containerDialog;
    private H6 errorMessage;
    private MetaConfigItemsConfigView relatedMetaConfigItemsConfigView;

    public CustomizedDataSourceConfigView(AttributesViewKind attributesViewKind){
        this.setWidthFull();
        this.attributesViewKind = attributesViewKind;

        Icon attributesViewKindIcon = VaadinIcon.TASKS.create();
        attributesViewKindIcon.setSize("12px");
        attributesViewKindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributesViewKindIcon,this.attributesViewKind.getAttributesViewKindName()+" ( "+this.attributesViewKind.getAttributesViewKindUID()+" ) "));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("外部自定义实现类信息");
        messageContainerLayout.add(viewTitle);
        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        implementationClassFullNameField = new TextField("Implementation Class Full Name");
        implementationClassFullNameField.setRequired(true);
        implementationClassFullNameField.setRequiredIndicatorVisible(true);
        implementationClassFullNameField.setHelperText(RealmConstant.ExternalAttributesValueAccessProcessorID);
        implementationClassFullNameField.setWidthFull();
        add(implementationClassFullNameField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认配置外部自定义实现类",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConfigExternalDataSource();
            }
        });
    }

    private void doConfigExternalDataSource(){
        String dbHostField = this.implementationClassFullNameField.getValue();

        boolean inputValidateResult = true;
        if(dbHostField.equals("")){
            inputValidateResult = false;
            this.implementationClassFullNameField.setInvalid(true);
        }

        if(inputValidateResult){
            hideErrorMessage();
            attributesViewKind.addOrUpdateMetaConfigItem(RealmConstant.ExternalAttributesValueAccessProcessorID,dbHostField);
            if(containerDialog != null){
                containerDialog.close();
            }
            CommonUIOperationUtil.showPopupNotification("外部自定义实现类配置成功", NotificationVariant.LUMO_SUCCESS);
            if(relatedMetaConfigItemsConfigView != null){
                relatedMetaConfigItemsConfigView.refreshMetaConfigItemsInfo();
            }
        }else{
            showErrorMessage("请输入全部外部自定义实现类配置项");
            CommonUIOperationUtil.showPopupNotification("外部自定义实现类配置信息输入错误", NotificationVariant.LUMO_ERROR);
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

    public void setRelatedMetaConfigItemsConfigView(MetaConfigItemsConfigView relatedMetaConfigItemsConfigView) {
        this.relatedMetaConfigItemsConfigView = relatedMetaConfigItemsConfigView;
    }
}
