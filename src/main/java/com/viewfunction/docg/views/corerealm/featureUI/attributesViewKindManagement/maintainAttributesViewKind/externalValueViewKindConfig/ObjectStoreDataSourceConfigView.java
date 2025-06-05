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

public class ObjectStoreDataSourceConfigView extends VerticalLayout {

    private AttributesViewKind attributesViewKind;
    private MetaConfigItemsConfigView relatedMetaConfigItemsConfigView;
    private Dialog containerDialog;
    private TextField storeHostField;
    private TextField storePortField;
    private TextField storeRootField;
    private TextField baseFolderField;
    private TextField userNameField;
    private TextField userPasswordField;
    private H6 errorMessage;

    public ObjectStoreDataSourceConfigView(AttributesViewKind attributesViewKind){
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

        H6 viewTitle = new H6("外部数据源信息");
        messageContainerLayout.add(viewTitle);
        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        storeHostField = new TextField("Store Host");
        storeHostField.setRequired(true);
        storeHostField.setRequiredIndicatorVisible(true);
        storeHostField.setHelperText("DOCG_ExternalObjectStore_Host");
        storeHostField.setWidthFull();
        add(storeHostField);

        storePortField = new TextField("Store Port");
        storePortField.setRequired(true);
        storePortField.setRequiredIndicatorVisible(true);
        storePortField.setHelperText("DOCG_ExternalObjectStore_Port");
        storePortField.setWidthFull();
        add(storePortField);

        storeRootField = new TextField("Store Root");
        storeRootField.setRequired(true);
        storeRootField.setRequiredIndicatorVisible(true);
        storeRootField.setHelperText("DOCG_ExternalObjectStore_StoreRoot");
        storeRootField.setWidthFull();
        add(storeRootField);

        baseFolderField = new TextField("Base Folder");
        baseFolderField.setRequired(true);
        baseFolderField.setRequiredIndicatorVisible(true);
        baseFolderField.setHelperText("DOCG_ExternalObjectStore_BaseFolder");
        baseFolderField.setWidthFull();
        add(baseFolderField);

        userNameField = new TextField("User Name");
        userNameField.setRequired(true);
        userNameField.setRequiredIndicatorVisible(true);
        userNameField.setHelperText("DOCG_ExternalObjectStore_UserName");
        userNameField.setWidthFull();
        add(userNameField);

        userPasswordField = new TextField("User Password");
        userPasswordField.setRequired(true);
        userPasswordField.setRequiredIndicatorVisible(true);
        userPasswordField.setHelperText("DOCG_ExternalObjectStore_UserPWD");
        userPasswordField.setWidthFull();
        add(userPasswordField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认配置数据源",new Icon(VaadinIcon.CHECK_CIRCLE));
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
        String storeHostField = this.storeHostField.getValue();
        String storePortField = this.storePortField.getValue();
        String storeRootField = this.storeRootField.getValue();
        String baseFolderField = this.baseFolderField.getValue();
        String userNameField = this.userNameField.getValue();
        String userPasswordField = this.userPasswordField.getValue();
        boolean inputValidateResult = true;
        if(storeHostField.equals("")){
            inputValidateResult = false;
            this.storeHostField.setInvalid(true);
        }
        if(storePortField.equals("")){
            inputValidateResult = false;
            this.storePortField.setInvalid(true);
        }
        if(storeRootField.equals("")){
            inputValidateResult = false;
            this.storeRootField.setInvalid(true);
        }
        if(userPasswordField.equals("")){
            inputValidateResult = false;
            this.userPasswordField.setInvalid(true);
        }
        if(userNameField.equals("")){
            inputValidateResult = false;
            this.userNameField.setInvalid(true);
        }
        if(inputValidateResult){
            hideErrorMessage();
            /*
            DOCG_ExternalObjectStore_Host
            DOCG_ExternalObjectStore_Port
            DOCG_ExternalObjectStore_UserName
            DOCG_ExternalObjectStore_UserPWD
            DOCG_ExternalObjectStore_StoreRoot
            DOCG_ExternalObjectStore_BaseFolder
            DOCG_ExternalAttributesValueAccessProcessorID
            */

            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalObjectStore_Host",storeHostField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalObjectStore_UserPWD",userPasswordField);
            attributesViewKind.addOrUpdateMetaConfigItem(RealmConstant.ExternalAttributesValueAccessProcessorID,RealmConstant.DefaultObjectStoreExternalAttributesValueAccessProcessorID);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalObjectStore_StoreRoot",storeRootField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalObjectStore_UserName",userNameField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalObjectStore_BaseFolder",baseFolderField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalObjectStore_Port",storePortField);

            if(containerDialog != null){
                containerDialog.close();
            }
            CommonUIOperationUtil.showPopupNotification("外部数据源配置成功", NotificationVariant.LUMO_SUCCESS);
            if(relatedMetaConfigItemsConfigView != null){
                relatedMetaConfigItemsConfigView.refreshMetaConfigItemsInfo();
            }
        }else{
            showErrorMessage("请输入全部外部数据源配置项");
            CommonUIOperationUtil.showPopupNotification("外部数据源配置信息输入错误", NotificationVariant.LUMO_ERROR);
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
