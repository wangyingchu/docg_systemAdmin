package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.eventHandling.AttributeKindCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;

public class CreateAttributeKindView extends VerticalLayout {
    private TextField attributeKindNameField;
    private TextField attributeKindDescField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;
    private H6 errorMessage;
    private Dialog containerDialog;

    public CreateAttributeKindView(){
        renderAttributeKindViewUI();
    }

    private void renderAttributeKindViewUI(){

        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("属性类型信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.attributeKindNameField = new TextField("属性类型名称 - AttributeKind Name");
        this.attributeKindNameField.setWidthFull();
        this.attributeKindNameField.setRequired(true);
        this.attributeKindNameField.setRequiredIndicatorVisible(true);
        this.attributeKindNameField.setTitle("请输入属性类型名称");
        add(this.attributeKindNameField);

        this.attributeDataTypeFilterSelect = new ComboBox("属性类型数据类型 - AttributeKind Data Type");
        this.attributeDataTypeFilterSelect.setRequired(true);
        this.attributeDataTypeFilterSelect.setWidthFull();
        this.attributeDataTypeFilterSelect.setPageSize(30);
        this.attributeDataTypeFilterSelect.setPlaceholder("请选择属性类型的数据类型");

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeDataType[] attributeDataTypesArray = coreRealm.getStorageImplTech().equals(CoreRealmStorageImplTech.NEO4J) ?
                new AttributeDataType[]{
                        AttributeDataType.BOOLEAN,
                        AttributeDataType.LONG,
                        AttributeDataType.DOUBLE,
                        AttributeDataType.TIMESTAMP,
                        AttributeDataType.DATE,
                        AttributeDataType.DATETIME,
                        AttributeDataType.TIME,
                        AttributeDataType.STRING
                        /*,
                        AttributeDataType.BOOLEAN_ARRAY,
                        AttributeDataType.INT_ARRAY,
                        AttributeDataType.SHORT_ARRAY,
                        AttributeDataType.LONG_ARRAY,
                        AttributeDataType.FLOAT_ARRAY,
                        AttributeDataType.DOUBLE_ARRAY,
                        AttributeDataType.TIMESTAMP_ARRAY,
                        AttributeDataType.DATE_ARRAY,
                        AttributeDataType.DATETIME_ARRAY,
                        AttributeDataType.TIME_ARRAY,
                        AttributeDataType.STRING_ARRAY,
                        AttributeDataType.BYTE_ARRAY,
                        AttributeDataType.DECIMAL_ARRAY
                        //,AttributeDataType.BINARY
                        */
                }
                :
                new AttributeDataType[]{
                        AttributeDataType.BOOLEAN,
                        AttributeDataType.INT,
                        AttributeDataType.SHORT,
                        AttributeDataType.LONG,
                        AttributeDataType.FLOAT,
                        AttributeDataType.DOUBLE,
                        AttributeDataType.TIMESTAMP,
                        AttributeDataType.DATE,
                        AttributeDataType.DATETIME,
                        AttributeDataType.TIME,
                        AttributeDataType.STRING,
                        AttributeDataType.BYTE,
                        AttributeDataType.DECIMAL
                        /* ,
                        AttributeDataType.BOOLEAN_ARRAY,
                        AttributeDataType.INT_ARRAY,
                        AttributeDataType.SHORT_ARRAY,
                        AttributeDataType.LONG_ARRAY,
                        AttributeDataType.FLOAT_ARRAY,
                        AttributeDataType.DOUBLE_ARRAY,
                        AttributeDataType.TIMESTAMP_ARRAY,
                        AttributeDataType.DATE_ARRAY,
                        AttributeDataType.DATETIME_ARRAY,
                        AttributeDataType.TIME_ARRAY,
                        AttributeDataType.STRING_ARRAY,
                        AttributeDataType.BYTE_ARRAY,
                        AttributeDataType.DECIMAL_ARRAY
                        //,AttributeDataType.BINARY
                        */
                };
        this.attributeDataTypeFilterSelect.setItems(attributeDataTypesArray);
        add(this.attributeDataTypeFilterSelect);

        this.attributeKindDescField = new TextField("属性类型描述 - AttributeKind Description");
        this.attributeKindDescField.setWidthFull();
        this.attributeKindDescField.setRequired(true);
        this.attributeKindDescField.setRequiredIndicatorVisible(true);
        this.attributeKindDescField.setTitle("请输入属性类型描述");
        add(attributeKindDescField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建属性类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateNewAttributeKind();
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setAndLockAttributeKindName(String attributeKindName){
        this.attributeKindNameField.setValue(attributeKindName);
        this.attributeKindNameField.setReadOnly(true);
    }

    public void setAndLockAttributeKindDataType(AttributeDataType attributeDataType){
        this.attributeDataTypeFilterSelect.setValue(attributeDataType);
        this.attributeDataTypeFilterSelect.setReadOnly(true);
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void doCreateNewAttributeKind(){
        String attributeKindName = this.attributeKindNameField.getValue();
        String attributeKindDesc = this.attributeKindDescField.getValue();
        AttributeDataType attributeDataType = this.attributeDataTypeFilterSelect.getValue();

        if(attributeKindName.equals("")||attributeKindDesc.equals("")||attributeDataType== null){
            showErrorMessage("请输入全部属性类型定义信息");
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            List<AttributeKind> attributeKindList = coreRealm.getAttributeKinds(attributeKindName,attributeKindDesc,attributeDataType);
            if(attributeKindList != null && attributeKindList.size() > 0){
                List<Button> actionButtonList = new ArrayList<>();
                Button confirmButton = new Button("确认创建属性类型",new Icon(VaadinIcon.CHECK_CIRCLE));
                confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("取消操作");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
                actionButtonList.add(confirmButton);
                actionButtonList.add(cancelButton);

                ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","相同定义内容的属性类型已经存在，请确认是否继续执行创建操作",actionButtonList,650,190);
                confirmWindow.open();
                confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        doCreateAttributeKind(attributeKindName,attributeKindDesc,attributeDataType,confirmWindow);
                    }
                });
                cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        confirmWindow.closeConfirmWindow();
                    }
                });
            }else{
                doCreateAttributeKind(attributeKindName,attributeKindDesc,attributeDataType,null);
            }
        }
    }

    private void doCreateAttributeKind(String attributeKindName,String attributeKindDesc,AttributeDataType attributeDataType,ConfirmWindow confirmWindow){
        if(confirmWindow != null){
            confirmWindow.closeConfirmWindow();
        }
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeKind targetAttributeKind = coreRealm.createAttributeKind(attributeKindName,attributeKindDesc,attributeDataType);
        if(targetAttributeKind != null){
            AttributeKindCreatedEvent attributeKindCreatedEvent = new AttributeKindCreatedEvent();
            attributeKindCreatedEvent.setAttributeKindName(targetAttributeKind.getAttributeKindName());
            attributeKindCreatedEvent.setAttributeKindDesc(targetAttributeKind.getAttributeKindDesc());
            attributeKindCreatedEvent.setAttributeKindDataType(targetAttributeKind.getAttributeDataType());
            attributeKindCreatedEvent.setAttributeKindUID(targetAttributeKind.getAttributeKindUID());
            attributeKindCreatedEvent.setCreateDateTime(targetAttributeKind.getCreateDateTime());
            attributeKindCreatedEvent.setLastModifyDateTime(targetAttributeKind.getLastModifyDateTime());
            attributeKindCreatedEvent.setCreatorId(targetAttributeKind.getCreatorId());
            attributeKindCreatedEvent.setDataOrigin(targetAttributeKind.getDataOrigin());
            ResourceHolder.getApplicationBlackboard().fire(attributeKindCreatedEvent);

            CommonUIOperationUtil.showPopupNotification("属性类型 "+attributeKindName+"["+attributeDataType+"]"+"("+attributeKindDesc+") 创建成功,属性类型 UID 为:"+targetAttributeKind.getAttributeKindUID(), NotificationVariant.LUMO_SUCCESS);
            if(this.containerDialog != null){
                this.containerDialog.close();
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("属性类型 "+attributeKindName+"["+attributeDataType+"]"+"("+attributeKindDesc+") 创建失败", NotificationVariant.LUMO_ERROR);
        }
    }
}
