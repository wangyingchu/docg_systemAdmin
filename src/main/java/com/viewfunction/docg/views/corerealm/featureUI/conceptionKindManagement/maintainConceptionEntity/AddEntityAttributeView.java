package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
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

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityAttributeAddedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.element.userInterfaceUtil.StringToTimeStampConverter;
import com.viewfunction.docg.util.ResourceHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddEntityAttributeView extends VerticalLayout {

    private Dialog containerDialog;
    private Label errorMessage;
    private TextField attributeNameField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;
    private Binder<String> binder;
    private HorizontalLayout attributeValueInputContainer;
    private String conceptionKind;
    private String conceptionEntityUID;
    public AddEntityAttributeView(String conceptionKind,String conceptionEntityUID){
        this.setMargin(false);
        this.setSpacing(false);
        this.binder = new Binder<>();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        Label viewTitle = new Label("新属性信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);

        errorMessage = new Label("-");
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

        attributeDataTypeFilterSelect = new ComboBox();
        attributeDataTypeFilterSelect.setPageSize(30);
        attributeDataTypeFilterSelect.setPlaceholder("属性数据类型");
        attributeDataTypeFilterSelect.setWidth(170, Unit.PIXELS);
        attributeDataTypeFilterSelect.setItems(
                AttributeDataType.BINARY,
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
                AttributeDataType.DECIMAL,
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
        );
        attributeMetaInfoFieldContainerLayout.add(attributeDataTypeFilterSelect);

        this.attributeDataTypeFilterSelect.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<AttributeDataType>, AttributeDataType>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<AttributeDataType>, AttributeDataType> comboBoxAttributeDataTypeComponentValueChangeEvent) {
                attributeValueInputContainer.removeAll();
                errorMessage.setVisible(false);
                AttributeDataType changedAttributeDataType = comboBoxAttributeDataTypeComponentValueChangeEvent.getValue();
                if(changedAttributeDataType != null){
                    Component inputComponent = renderAttributeValueInputElement(changedAttributeDataType,340);
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
                        addEntityAttribute();
                    }
                }
            }
        });
        attributeValueFieldContainerLayout.add(confirmButton);
    }

    private Component renderAttributeValueInputElement(AttributeDataType attributeDataType,int textFieldWidth){
        Component currentConditionValueEditor = null;
        switch(attributeDataType){
            case INT:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DATE:
                currentConditionValueEditor = new DatePicker();
                ((DatePicker)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((DatePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case LONG:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DOUBLE:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case BOOLEAN:
                currentConditionValueEditor = new ComboBox();
                ((ComboBox)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((ComboBox)currentConditionValueEditor).setPreventInvalidInput(true);
                ((ComboBox)currentConditionValueEditor).setAllowCustomValue(false);
                ((ComboBox)currentConditionValueEditor).setItems("true","false");
                ((ComboBox)currentConditionValueEditor).setValue("true");
                ((ComboBox)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DECIMAL:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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
                ((TimePicker)currentConditionValueEditor).setAutoOpen(true);
                ((TimePicker)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TimePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DATETIME:
                currentConditionValueEditor = new DateTimePicker();
                ((DateTimePicker)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
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

    private void addEntityAttribute(){
        String attributeValueString = null;
        Object newEntityAttributeValue = null;
        String attributeName = attributeNameField.getValue();
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
            if(newEntityAttributeValue != null){
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
                if(targetConceptionKind == null){
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
                }else{
                    ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                    if(targetConceptionEntity == null){
                        CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
                    }else{
                        if(targetConceptionEntity.hasAttribute(attributeName)){
                            CommonUIOperationUtil.showPopupNotification("UID 为 "+conceptionEntityUID+" 的概念实体中已经存在属性 "+attributeName, NotificationVariant.LUMO_ERROR);
                        }else{
                            try {
                                AttributeValue attributeValue = null;
                                if (newEntityAttributeValue instanceof Boolean) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Boolean) newEntityAttributeValue).booleanValue());
                                }else if (newEntityAttributeValue instanceof Integer) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Integer) newEntityAttributeValue).intValue());
                                }else if (newEntityAttributeValue instanceof Short) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Short) newEntityAttributeValue).shortValue());
                                }else if (newEntityAttributeValue instanceof Long) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Long) newEntityAttributeValue).longValue());
                                }else if (newEntityAttributeValue instanceof Float) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Float) newEntityAttributeValue).floatValue());
                                }else if (newEntityAttributeValue instanceof Double) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Double) newEntityAttributeValue).doubleValue());
                                }else if (newEntityAttributeValue instanceof Byte) {
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,((Byte) newEntityAttributeValue).byteValue());
                                }else if (newEntityAttributeValue instanceof Date){
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,(Date)newEntityAttributeValue);
                                }else if (newEntityAttributeValue instanceof LocalDateTime){
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,(LocalDateTime)newEntityAttributeValue);
                                }else if (newEntityAttributeValue instanceof LocalDate){
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,(LocalDate)newEntityAttributeValue);
                                }else if (newEntityAttributeValue instanceof LocalTime){
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,(LocalTime)newEntityAttributeValue);
                                }else if (newEntityAttributeValue instanceof BigDecimal){
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,(BigDecimal)newEntityAttributeValue);
                                }else if (newEntityAttributeValue instanceof String){
                                    attributeValue = targetConceptionEntity.addAttribute(attributeName,newEntityAttributeValue.toString());
                                }
                                if(attributeValue != null){
                                    ConceptionEntityAttributeAddedEvent conceptionEntityAttributeAddedEvent = new ConceptionEntityAttributeAddedEvent();
                                    conceptionEntityAttributeAddedEvent.setConceptionEntityUID(this.conceptionEntityUID);
                                    conceptionEntityAttributeAddedEvent.setConceptionKindName(this.conceptionKind);
                                    conceptionEntityAttributeAddedEvent.setAttributeValue(attributeValue);
                                    ResourceHolder.getApplicationBlackboard().fire(conceptionEntityAttributeAddedEvent);
                                    CommonUIOperationUtil.showPopupNotification("在 UID 为 "+conceptionEntityUID+" 的概念实体中添加属性 "+attributeName+" 成功", NotificationVariant.LUMO_SUCCESS);
                                    if(containerDialog != null){
                                        containerDialog.close();
                                    }
                                }
                            } catch (CoreRealmServiceRuntimeException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attributeDataTypeFilterSelect.setValue(AttributeDataType.STRING);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
