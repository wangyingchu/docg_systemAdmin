package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.timepicker.TimePickerVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.data.validator.*;
import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.AddConceptionEntityView;

import dev.mett.vaadin.tooltip.Tooltips;

import java.math.BigDecimal;

public class AttributeCreatorItemWidget extends VerticalLayout {
    private Component valueEditor;
    private String attributeName;
    private AttributeDataType attributeDataType;
    private Binder<String> attributeValueDataBinder;
    private AddConceptionEntityView addConceptionEntityView;

    public AttributeCreatorItemWidget(String attributeName, AttributeDataType attributeDataType,int widgetWidth){
        this.attributeName = attributeName;
        this.attributeDataType = attributeDataType;
        this.attributeValueDataBinder = new Binder<>();

        this.setPadding(true);
        this.setMargin(false);
        this.setSpacing(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout attributeMetaLayout = new HorizontalLayout();
        attributeMetaLayout.setSpacing(false);
        attributeMetaLayout.setMargin(false);
        attributeMetaLayout.setPadding(false);
        attributeMetaLayout.setWidth(widgetWidth, Unit.PIXELS);
        add(attributeMetaLayout);

        VerticalLayout attributeMetaInfoContainer = new VerticalLayout();
        attributeMetaInfoContainer.setSpacing(false);
        attributeMetaInfoContainer.setMargin(false);
        attributeMetaInfoContainer.setPadding(false);
        attributeMetaLayout.add(attributeMetaInfoContainer);

        HorizontalLayout attributeNameInfoContainer = new HorizontalLayout();
        attributeNameInfoContainer.setWidth(100,Unit.PERCENTAGE);
        attributeMetaInfoContainer.add(attributeNameInfoContainer);

        Icon propertyTypeIcon = null;
        if(attributeName.startsWith(RealmConstant.RealmInnerTypePerFix) ||
                attributeName.equals(RealmConstant._createDateProperty) ||
                attributeName.equals(RealmConstant._lastModifyDateProperty) ||
                attributeName.equals(RealmConstant._creatorIdProperty)||
                attributeName.equals(RealmConstant._dataOriginProperty)
        ){
            propertyTypeIcon = VaadinIcon.ELLIPSIS_CIRCLE_O.create();
        }else{
            propertyTypeIcon = VaadinIcon.ELLIPSIS_CIRCLE.create();
        }

        propertyTypeIcon.setSize("12px");
        attributeNameInfoContainer.add(propertyTypeIcon);
        attributeNameInfoContainer.setVerticalComponentAlignment(Alignment.STRETCH,propertyTypeIcon);

        Label attributeNameLabel = new Label(attributeName);
        attributeNameLabel.getStyle().set("font-size","0.75rem").set("font-weight","bold").set("padding-right","5px");
        attributeNameInfoContainer.add(attributeNameLabel);
        attributeNameInfoContainer.setFlexGrow(1,attributeNameLabel);

        HorizontalLayout attributeStatusContainer = new HorizontalLayout();
        attributeStatusContainer.setPadding(false);
        attributeStatusContainer.setMargin(false);
        attributeStatusContainer.setSpacing(false);
        attributeMetaInfoContainer.add(attributeStatusContainer);

        Label attributeTypeLabel = new Label(attributeDataType.toString());
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-70pct)").set("padding-left","20px");
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeMetaInfoContainer);
        attributeMetaLayout.add(attributeTypeLabel);
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeTypeLabel);

        Button clearAttributeButton = new Button();
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY);
        clearAttributeButton.setIcon(VaadinIcon.ERASER.create());
        Tooltips.getCurrent().setTooltip(clearAttributeButton, "撤销此属性");
        attributeMetaLayout.add(clearAttributeButton);
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,clearAttributeButton);

        AttributeCreatorItemWidget that = this;
        clearAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(getAddConceptionEntityView() != null){
                    getAddConceptionEntityView().removeAttributeCreatorItemWidget(that);
                }
            }
        });

        HorizontalLayout attributeValueInfoContainerLayout = new HorizontalLayout();
        attributeValueInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        Icon inputIcon = VaadinIcon.INPUT.create();
        inputIcon.setSize("10px");
        attributeValueInfoContainerLayout.add(inputIcon);

        this.valueEditor = generateValueEditorTextField();
        if(this.valueEditor!= null){
            attributeValueInfoContainerLayout.add(valueEditor);
        }
        this.add(attributeValueInfoContainerLayout);
        this.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
    }

    private Component generateValueEditorTextField() {
        if (this.attributeDataType != null) {
            Component currentConditionValueEditor = null;
            switch (this.attributeDataType) {
                case STRING:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
                case BOOLEAN:
                    currentConditionValueEditor = new ComboBox();
                    ((ComboBox)currentConditionValueEditor).addThemeVariants(ComboBoxVariant.LUMO_SMALL);
                    ((ComboBox)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((ComboBox)currentConditionValueEditor).setAllowCustomValue(false);
                    ((ComboBox)currentConditionValueEditor).setItems("true","false");
                    ((ComboBox)currentConditionValueEditor).setValue("true");
                    break;
                case DATE:
                    currentConditionValueEditor = new DatePicker();
                    ((DatePicker)currentConditionValueEditor).addThemeVariants(DatePickerVariant.LUMO_SMALL);
                    ((DatePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
                case TIME:
                    currentConditionValueEditor = new TimePicker();
                    ((TimePicker)currentConditionValueEditor).addThemeVariants(TimePickerVariant.LUMO_SMALL);
                    ((TimePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
                case DATETIME:
                    currentConditionValueEditor = new DateTimePicker();
                    ((DateTimePicker)currentConditionValueEditor).addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
                    ((DateTimePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
                case TIMESTAMP:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
                case INT:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor).withConverter(new StringToIntegerConverter("该项属性值必须为INT类型"))
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
                case LONG:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                case DOUBLE:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                case FLOAT:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                case DECIMAL:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                case SHORT:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                case BYTE:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
                case BINARY:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    break;
            }
            return currentConditionValueEditor;
        }
        return null;
    }

    private boolean validateAttributeValue() {
        if (valueEditor != null) {
            Component currentConditionValueEditor = valueEditor;
            AttributeDataType attributeDataType = this.attributeDataType;
            switch (attributeDataType) {
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

    public AddConceptionEntityView getAddConceptionEntityView() {
        return addConceptionEntityView;
    }

    public void setAddConceptionEntityView(AddConceptionEntityView addConceptionEntityView) {
        this.addConceptionEntityView = addConceptionEntityView;
    }

    public Object getAttributeValue(){
        if(!validateAttributeValue()){
            Object attributeValue = null;
            Component currentConditionValueEditor = valueEditor;
            AttributeDataType attributeDataType = this.attributeDataType;
            switch (attributeDataType) {
                case INT:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case BYTE:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case DATE:
                    attributeValue = ((DatePicker) currentConditionValueEditor).getValue();
                    break;
                case LONG:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case FLOAT:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case SHORT:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case BINARY:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case DOUBLE:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case STRING:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case BOOLEAN:
                    attributeValue = ((ComboBox) currentConditionValueEditor).getValue();
                    break;
                case DECIMAL:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case TIMESTAMP:
                    attributeValue = ((TextField) currentConditionValueEditor).getValue();
                    break;
                case TIME:
                    attributeValue = ((TimePicker) currentConditionValueEditor).getValue();
                    break;
                case DATETIME:
                    attributeValue = ((DateTimePicker) currentConditionValueEditor).getValue();
            }
            return attributeValue;
        }else{
            return null;
        }
    }

    public String getAttributeName(){
        return this.attributeName;
    }

    public AttributeDataType getAttributeDataType() {
        return attributeDataType;
    }
}
