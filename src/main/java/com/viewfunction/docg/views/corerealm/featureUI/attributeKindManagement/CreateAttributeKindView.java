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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

public class CreateAttributeKindView extends VerticalLayout {
    private String attributeKindName;
    private String attributeKindDesc;
    private AttributeDataType attributeDataType;
    private TextField attributeKindNameField;
    private TextField attributeKindDescField;
    private TextField conceptionKindNameField;
    private TextField conceptionKindDescField;
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

        H6 viewTitle = new H6("概念类型信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.conceptionKindNameField = new TextField("属性类型名称 - AttributeKind Name");
        this.conceptionKindNameField.setWidthFull();
        this.conceptionKindNameField.setRequired(true);
        this.conceptionKindNameField.setRequiredIndicatorVisible(true);
        this.conceptionKindNameField.setTitle("请输入属性类型名称");
        add(conceptionKindNameField);

        this.attributeDataTypeFilterSelect = new ComboBox("属性类型数据类型 - AttributeKind Data Type");
        this.attributeDataTypeFilterSelect.setWidthFull();
        this.attributeDataTypeFilterSelect.setPageSize(30);
        this.attributeDataTypeFilterSelect.setPlaceholder("属性类型的数据类型");

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

        this.conceptionKindDescField = new TextField("属性类型描述 - AttributeKind Description");
        this.conceptionKindDescField.setWidthFull();
        this.conceptionKindDescField.setRequired(true);
        this.conceptionKindDescField.setRequiredIndicatorVisible(true);
        this.conceptionKindDescField.setTitle("请输入属性类型描述");
        add(conceptionKindDescField);

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
                //doCreateNewConceptionKind();
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setAndLockAttributeKindName(String attributeKindName){
        this.conceptionKindNameField.setValue(attributeKindName);
        this.conceptionKindNameField.setReadOnly(true);
    }

    public void setAndLockAttributeKindDataType(AttributeDataType attributeDataType){
        this.attributeDataTypeFilterSelect.setValue(attributeDataType);
        this.attributeDataTypeFilterSelect.setReadOnly(true);
    }
}
