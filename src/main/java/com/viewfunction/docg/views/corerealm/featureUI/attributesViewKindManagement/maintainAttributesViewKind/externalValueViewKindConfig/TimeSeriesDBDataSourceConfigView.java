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

public class TimeSeriesDBDataSourceConfigView extends VerticalLayout {

    private AttributesViewKind attributesViewKind;
    private TextField dbHostField;
    private TextField dbPortField;
    private TextField databaseNameField;
    private TextField tableNameField;
    private TextField userNameField;
    private TextField userPasswordField;
    private Dialog containerDialog;
    private H6 errorMessage;
    private MetaConfigItemsConfigView relatedMetaConfigItemsConfigView;

    public TimeSeriesDBDataSourceConfigView(AttributesViewKind attributesViewKind){
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

        dbHostField = new TextField("Database Host");
        dbHostField.setRequired(true);
        dbHostField.setRequiredIndicatorVisible(true);
        dbHostField.setHelperText("DOCG_ExternalTimeSeriesDB_Host");
        dbHostField.setWidthFull();
        add(dbHostField);

        dbPortField = new TextField("Database Port");
        dbPortField.setRequired(true);
        dbPortField.setRequiredIndicatorVisible(true);
        dbPortField.setHelperText("DOCG_ExternalTimeSeriesDB_Port");
        dbPortField.setWidthFull();
        add(dbPortField);

        databaseNameField = new TextField("Database Name");
        databaseNameField.setRequired(true);
        databaseNameField.setRequiredIndicatorVisible(true);
        databaseNameField.setHelperText("DOCG_ExternalTimeSeriesDB_DefaultDBName");
        databaseNameField.setWidthFull();
        add(databaseNameField);

        tableNameField = new TextField("Table Name");
        tableNameField.setRequired(true);
        tableNameField.setRequiredIndicatorVisible(true);
        tableNameField.setHelperText("DOCG_ExternalTimeSeriesDB_DefaultTableName");
        tableNameField.setWidthFull();
        add(tableNameField);

        userNameField = new TextField("User Name");
        userNameField.setRequired(true);
        userNameField.setRequiredIndicatorVisible(true);
        userNameField.setHelperText("DOCG_ExternalTimeSeriesDB_UserName");
        userNameField.setWidthFull();
        add(userNameField);

        userPasswordField = new TextField("User Password");
        userPasswordField.setRequired(true);
        userPasswordField.setRequiredIndicatorVisible(true);
        userPasswordField.setHelperText("DOCG_ExternalTimeSeriesDB_UserPWD");
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
        String dbHostField = this.dbHostField.getValue();
        String dbPortField = this.dbPortField.getValue();
        String databaseNameField = this.databaseNameField.getValue();
        String tableNameField = this.tableNameField.getValue();
        String userNameField = this.userNameField.getValue();
        String userPasswordField = this.userPasswordField.getValue();
        boolean inputValidateResult = true;
        if(dbHostField.equals("")){
            inputValidateResult = false;
            this.dbHostField.setInvalid(true);
        }
        if(dbPortField.equals("")){
            inputValidateResult = false;
            this.dbPortField.setInvalid(true);
        }
        if(databaseNameField.equals("")){
            inputValidateResult = false;
            this.databaseNameField.setInvalid(true);
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

            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalTimeSeriesDB_Host",dbHostField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalTimeSeriesDB_UserPWD",userPasswordField);
            attributesViewKind.addOrUpdateMetaConfigItem(RealmConstant.ExternalAttributesValueAccessProcessorID,RealmConstant.DefaultTimeSeriesDBExternalAttributesValueAccessProcessorID);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalTimeSeriesDB_DefaultDBName",databaseNameField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalTimeSeriesDB_UserName",userNameField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalTimeSeriesDB_DefaultTableName",tableNameField);
            attributesViewKind.addOrUpdateMetaConfigItem("DOCG_ExternalTimeSeriesDB_Port",dbPortField);

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
