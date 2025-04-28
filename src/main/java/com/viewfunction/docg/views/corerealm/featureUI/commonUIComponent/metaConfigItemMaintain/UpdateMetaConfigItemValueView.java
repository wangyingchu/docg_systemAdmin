package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.data.validator.*;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.MetaConfigItemFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.element.userInterfaceUtil.StringToTimeStampConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class UpdateMetaConfigItemValueView extends VerticalLayout {

    private Dialog containerDialog;
    private NativeLabel errorMessage;
    private TextField attributeNameField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;
    private Binder<String> binder;
    private HorizontalLayout attributeValueInputContainer;
    private AttributeValueOperateHandler attributeValueOperateHandler;
    private MetaConfigItemFeatureSupportable metaConfigItemFeatureSupportable;
    private MetaConfigItemsConfigView containerMetaConfigItemsConfigView;

    private String itemName;
    private AttributeDataType orgItemType;
    private Object orgItemValue;
    private boolean isFirstTimeOpenView;

    public UpdateMetaConfigItemValueView(MetaConfigItemFeatureSupportable metaConfigItemFeatureSupportable, String itemName, AttributeDataType orgItemType, Object orgItemValue){
        this.setMargin(false);
        this.setSpacing(false);
        this.binder = new Binder<>();
        this.metaConfigItemFeatureSupportable = metaConfigItemFeatureSupportable;
        this.itemName = itemName;
        this.orgItemType = orgItemType;
        this.orgItemValue = orgItemValue;
        this.isFirstTimeOpenView = true;

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("待更新属性信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);

        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        HorizontalLayout attributeMetaInfoFieldContainerLayout = new HorizontalLayout();
        add(attributeMetaInfoFieldContainerLayout);

        attributeNameField = new TextField();
        attributeMetaInfoFieldContainerLayout.add(attributeNameField);
        attributeNameField.setPlaceholder("属性名称");
        attributeNameField.setWidth(250, Unit.PIXELS);
        attributeNameField.setValue(this.itemName);
        attributeNameField.setReadOnly(true);

        attributeDataTypeFilterSelect = new ComboBox();
        attributeDataTypeFilterSelect.setPageSize(30);
        attributeDataTypeFilterSelect.setPlaceholder("属性数据类型");
        attributeDataTypeFilterSelect.setWidth(170, Unit.PIXELS);

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
        attributeDataTypeFilterSelect.setItems(attributeDataTypesArray);
        attributeMetaInfoFieldContainerLayout.add(attributeDataTypeFilterSelect);

        this.attributeDataTypeFilterSelect.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<AttributeDataType>, AttributeDataType>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<AttributeDataType>, AttributeDataType> comboBoxAttributeDataTypeComponentValueChangeEvent) {
                attributeValueInputContainer.removeAll();
                errorMessage.setVisible(false);
                AttributeDataType changedAttributeDataType = comboBoxAttributeDataTypeComponentValueChangeEvent.getValue();
                if(changedAttributeDataType != null){
                    Component inputComponent = renderAttributeValueInputElement(changedAttributeDataType);
                    attributeValueInputContainer.add(inputComponent);
                }
            }
        });

        HorizontalLayout attributeValueFieldContainerLayout = new HorizontalLayout();
        add(attributeValueFieldContainerLayout);
        attributeValueInputContainer  = new HorizontalLayout();
        attributeValueInputContainer.setSpacing(false);
        attributeValueInputContainer.setMargin(false);
        attributeValueInputContainer.setPadding(false);
        attributeValueFieldContainerLayout.add(attributeValueInputContainer);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(80,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if(attributeNameField.getValue().equals("") || attributeDataTypeFilterSelect.getValue()==null){
                    errorMessage.setText("属性名称与数据类型是必填项");
                    errorMessage.setVisible(true);
                }else{
                    if(validateAttributeValue()){
                        errorMessage.setText("输入的属性值为空或格式不合法");
                        errorMessage.setVisible(true);
                    }else{
                        if(metaConfigItemFeatureSupportable != null){
                            addMetaConfigItem();
                        }
                        if(attributeValueOperateHandler != null){
                            attributeValueOperateHandler.handleAttributeValue(getAttributeValue());
                        }
                    }
                }
            }
        });
        attributeValueFieldContainerLayout.add(confirmButton);
    }

    private Component renderAttributeValueInputElement(AttributeDataType attributeDataType){
        Component currentConditionValueEditor = null;
        switch(attributeDataType){
            case INT:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor).withConverter(new StringToIntegerConverter("该项属性值必须为INT类型"))
                        .withValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null, null))
                        .bind(new ValueProvider<String, Integer>() {
                            @Override
                            public Integer apply(String s) {
                                return new Integer(s);
                            }
                        }, new Setter<String, Integer>() {
                            @Override
                            public void accept(String s, Integer intValue) {}
                        });
                break;
            case BYTE:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DATE:
                currentConditionValueEditor = new DatePicker();
                if(this.isFirstTimeOpenView){
                    ((DatePicker) currentConditionValueEditor).setValue((LocalDate)this.orgItemValue);
                    this.isFirstTimeOpenView = false;
                }
                ((DatePicker)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((DatePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case LONG:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToLongConverter("该项属性值必须为LONG类型"))
                        .withValidator(new LongRangeValidator("该项属性值必须为LONG类型", null, null))
                        .bind(new ValueProvider<String, Long>() {
                            @Override
                            public Long apply(String s) {
                                return new Long(s);
                            }
                        }, new Setter<String, Long>() {
                            @Override
                            public void accept(String s, Long longValue) {}
                        });
                break;
            case FLOAT:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToFloatConverter("该项属性值必须为FLOAT类型"))
                        .withValidator(new FloatRangeValidator("该项属性值必须为FLOAT类型", null, null))
                        .bind(new ValueProvider<String, Float>() {
                            @Override
                            public Float apply(String s) {
                                return new Float(s);
                            }
                        }, new Setter<String, Float>() {
                            @Override
                            public void accept(String s, Float floatValue) {}
                        });
                break;
            case SHORT:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToIntegerConverter("该项属性值必须为SHORT类型"))
                        .withValidator(new IntegerRangeValidator("该项属性值必须为SHORT类型", null, null))
                        .bind(new ValueProvider<String, Integer>() {
                            @Override
                            public Integer apply(String s) {
                                return new Integer(s);
                            }
                        }, new Setter<String, Integer>() {
                            @Override
                            public void accept(String s, Integer shortValue) {}
                        });
                break;
            case BINARY:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DOUBLE:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToDoubleConverter("该项属性值必须为DOUBLE类型"))
                        .withValidator(new DoubleRangeValidator("该项属性值必须为DOUBLE类型", null, null))
                        .bind(new ValueProvider<String, Double>() {
                            @Override
                            public Double apply(String s) {
                                return new Double(s);
                            }
                        }, new Setter<String, Double>() {
                            @Override
                            public void accept(String s, Double doubleValue) {}
                        });
                break;
            case STRING:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case BOOLEAN:
                currentConditionValueEditor = new ComboBox();
                if(this.isFirstTimeOpenView){
                   // ((ComboBox<?>) currentConditionValueEditor).setValue(this.orgItemValue);
                    this.isFirstTimeOpenView = false;
                }
                ((ComboBox)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((ComboBox)currentConditionValueEditor).setAllowCustomValue(false);
                ((ComboBox)currentConditionValueEditor).setItems("true","false");
                ((ComboBox)currentConditionValueEditor).setValue("true");
                ((ComboBox)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DECIMAL:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    ((TextField) currentConditionValueEditor).setValue(this.orgItemValue.toString());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToBigDecimalConverter("该项属性值必须为DECIMAL类型"))
                        .withValidator(new BigDecimalRangeValidator("该项属性值必须为DECIMAL类型", null, null))
                        .bind(new ValueProvider<String, BigDecimal>() {
                            @Override
                            public BigDecimal apply(String s) {
                                return new BigDecimal(s);
                            }
                        }, new Setter<String, BigDecimal>() {
                            @Override
                            public void accept(String s, BigDecimal bigDecimalValue) {}
                        });
                break;
            case TIMESTAMP:
                currentConditionValueEditor = new TextField();
                if(this.isFirstTimeOpenView){
                    System.out.println(this.orgItemValue);
                    System.out.println(this.orgItemValue.getClass());


                    ((TextField)currentConditionValueEditor).setValue(""+((Date)this.orgItemValue).getTime());
                    this.isFirstTimeOpenView = false;
                }
                ((TextField)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToTimeStampConverter())
                        .withValidator((Validator)new DateTimeRangeValidator("该项属性值必须为TIMESTAMP类型",null,null) )
                        .bind(new ValueProvider<String, Long>() {
                            @Override
                            public Long apply(String s) {
                                return new Long(s);
                            }
                        }, new Setter<String, Long>() {
                            @Override
                            public void accept(String s, Long longValue) {}
                        });
                break;
            case TIME:
                currentConditionValueEditor = new TimePicker();
                if(this.isFirstTimeOpenView){
                    ((TimePicker) currentConditionValueEditor).setValue((LocalTime)this.orgItemValue);
                    this.isFirstTimeOpenView = false;
                }
                ((TimePicker)currentConditionValueEditor).setAutoOpen(true);
                ((TimePicker)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((TimePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DATETIME:
                currentConditionValueEditor = new DateTimePicker();
                if(this.isFirstTimeOpenView){
                    ((DateTimePicker) currentConditionValueEditor).setValue((LocalDateTime)this.orgItemValue);
                    this.isFirstTimeOpenView = false;
                }
                ((DateTimePicker)currentConditionValueEditor).setWidth(340,Unit.PIXELS);
                ((DateTimePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
        }
        return currentConditionValueEditor;
    }

    private boolean validateAttributeValue() {
        if (attributeValueInputContainer.getComponentAt(0) != null) {
            Component currentConditionValueEditor = attributeValueInputContainer.getComponentAt(0);
            switch (attributeDataTypeFilterSelect.getValue()) {
                case INT:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case BYTE:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case DATE:
                    if(((DatePicker) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((DatePicker) currentConditionValueEditor).getValue() == null){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case LONG:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case FLOAT:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case SHORT:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case BINARY:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case DOUBLE:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case STRING:
                    return ((TextField) currentConditionValueEditor).isInvalid();
                case BOOLEAN:
                    if(((ComboBox) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((ComboBox) currentConditionValueEditor).getValue() == null){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case DECIMAL:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case TIMESTAMP:
                    if(((TextField) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TextField) currentConditionValueEditor).getValue().equals("")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case TIME:
                    if(((TimePicker) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((TimePicker) currentConditionValueEditor).getValue() == null){
                            return true;
                        }else{
                            return false;
                        }
                    }
                case DATETIME:
                    if(((DateTimePicker) currentConditionValueEditor).isInvalid()){
                        return true;
                    }else{
                        if(((DateTimePicker) currentConditionValueEditor).getValue() == null){
                            return true;
                        }else{
                            return false;
                        }
                    }
            }
        }
        return false;
    }

    private AttributeValue getAttributeValue(){
        String attributeName = attributeNameField.getValue();
        Object newEntityAttributeValue = getAttributeValueObject();
        AttributeDataType attributeDataType = attributeDataTypeFilterSelect.getValue();

        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setAttributeName(attributeName);
        attributeValue.setAttributeValue(newEntityAttributeValue);
        attributeValue.setAttributeDataType(attributeDataType);
        return attributeValue;
    }

    private Object getAttributeValueObject(){
        String attributeValueString = null;
        Object newEntityAttributeValue = null;
        if (attributeValueInputContainer.getComponentAt(0) != null) {
            Component currentConditionValueEditor = attributeValueInputContainer.getComponentAt(0);

            switch (attributeDataTypeFilterSelect.getValue()) {
                case INT:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = Integer.valueOf(attributeValueString);
                    break;
                case BYTE:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = Byte.valueOf(attributeValueString);
                    break;
                case DATE:
                    newEntityAttributeValue = ((DatePicker) currentConditionValueEditor).getValue();
                    break;
                case LONG:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = Long.valueOf(attributeValueString);
                    break;
                case FLOAT:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = Float.valueOf(attributeValueString);
                    break;
                case SHORT:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = Short.valueOf(attributeValueString);
                    break;
                case BINARY:
                    break;
                case DOUBLE:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = Double.valueOf(attributeValueString);
                    break;
                case STRING:
                    newEntityAttributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case BOOLEAN:
                    attributeValueString = ((ComboBox) currentConditionValueEditor).getValue().toString();
                    newEntityAttributeValue = Boolean.valueOf(attributeValueString);
                    break;
                case DECIMAL:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = new BigDecimal(attributeValueString);
                    break;
                case TIMESTAMP:
                    attributeValueString = ((TextField) currentConditionValueEditor).getValue();
                    newEntityAttributeValue = new Date(Long.valueOf(attributeValueString));
                    break;
                case TIME:
                    newEntityAttributeValue = ((TimePicker) currentConditionValueEditor).getValue();
                    break;
                case DATETIME:
                    newEntityAttributeValue = ((DateTimePicker) currentConditionValueEditor).getValue();
                    break;
            }
        }
        return newEntityAttributeValue;
    }

    private void addMetaConfigItem(){
        String attributeName = attributeNameField.getValue();
        Object newEntityAttributeValue = getAttributeValueObject();
        boolean addResult = this.metaConfigItemFeatureSupportable.addOrUpdateMetaConfigItem(attributeName,newEntityAttributeValue);
        if(addResult){
            CommonUIOperationUtil.showPopupNotification("更新元属性 "+ attributeName +" : "+newEntityAttributeValue +" 成功", NotificationVariant.LUMO_SUCCESS);
            if(containerDialog != null){
                containerDialog.close();
            }
            if(containerMetaConfigItemsConfigView != null){
                containerMetaConfigItemsConfigView.refreshMetaConfigItemsInfo();
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("更新元属性 "+ attributeName +" : "+newEntityAttributeValue +" 失败", NotificationVariant.LUMO_ERROR);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attributeDataTypeFilterSelect.setValue(orgItemType);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setAttributeValueOperateHandler(AttributeValueOperateHandler attributeValueOperateHandler) {
        this.attributeValueOperateHandler = attributeValueOperateHandler;
    }

    public void setContainerMetaConfigItemsConfigView(MetaConfigItemsConfigView containerMetaConfigItemsConfigView) {
        this.containerMetaConfigItemsConfigView = containerMetaConfigItemsConfigView;
    }
}
