package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
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
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.KindQueryCriteriaView;

public class AddCustomQueryCriteriaUI extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField propertyNameField;
    private ComboBox<AttributeDataType> propertyDataTypeFilterSelect;
    private KindQueryCriteriaView kindQueryCriteriaView;

    public AddCustomQueryCriteriaUI(){
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("自定义查询/显示条件信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        HorizontalLayout criteriaFieldContainerLayout = new HorizontalLayout();
        add(criteriaFieldContainerLayout);

        propertyNameField = new TextField();
        criteriaFieldContainerLayout.add(propertyNameField);
        propertyNameField.setPlaceholder("属性名称");
        propertyNameField.setWidth(150,Unit.PIXELS);

        propertyDataTypeFilterSelect = new ComboBox();
        propertyDataTypeFilterSelect.setPageSize(30);
        propertyDataTypeFilterSelect.setPlaceholder("属性数据类型");
        propertyDataTypeFilterSelect.setWidth(170, Unit.PIXELS);

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
                        AttributeDataType. FLOAT_ARRAY,
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
                        AttributeDataType. FLOAT_ARRAY,
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
        propertyDataTypeFilterSelect.setItems(attributeDataTypesArray);
        criteriaFieldContainerLayout.add(propertyDataTypeFilterSelect);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(propertyNameField.getValue().equals("")||propertyDataTypeFilterSelect.getValue()==null){
                    errorMessage.setText("属性名称与数据类型是必填项");
                    errorMessage.setVisible(true);
                }else{
                    getKindQueryCriteriaView().addQueryConditionItem(propertyNameField.getValue(),propertyDataTypeFilterSelect.getValue());
                    if(getContainerDialog() != null){
                        getContainerDialog().close();
                    }
                }
            }
        });
        criteriaFieldContainerLayout.add(confirmButton);
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public KindQueryCriteriaView getKindQueryCriteriaView() {
        return kindQueryCriteriaView;
    }

    public void setKindQueryCriteriaView(KindQueryCriteriaView kindQueryCriteriaView) {
        this.kindQueryCriteriaView = kindQueryCriteriaView;
    }
}
