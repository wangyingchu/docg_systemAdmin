package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.data.validator.*;
import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import dev.mett.vaadin.tooltip.Tooltips;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AttributeEditorItemWidget extends VerticalLayout {

    private Label propertyNameLabel;
    //private PropertyTypeVO propertyTypeVO;
    private Button updatePropertyValueButton;
    private Button confirmUpdateButton;
    private Button cancelUpdateButton;
    private Button deletePropertyValueButton;

    private Component valueEditor;
    private Object propertyValue;

    private AttributeValue attributeValue;
    private String attributeName;
    private AttributeDataType attributeDataType;
    private Button updateAttributeValueButton;
    private Button cancelUpdateValueButton;
    private Button confirmUpdateAttributeValueButton;
    private Button deleteAttributeButton;
    private Binder<String> attributeValueDataBinder;
    public AttributeEditorItemWidget(AttributeValue attributeValue){
        this.attributeValue = attributeValue;
        this.attributeName = attributeValue.getAttributeName();
        this.attributeDataType = attributeValue.getAttributeDataType();
        //this.binder = binder;
        this.attributeValueDataBinder = new Binder<>();

        this.setPadding(true);
        this.setMargin(false);
        this.setSpacing(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout attributeMetaLayout = new HorizontalLayout();
        attributeMetaLayout.setSpacing(false);
        attributeMetaLayout.setMargin(false);
        attributeMetaLayout.setPadding(false);
        attributeMetaLayout.setWidth(100, Unit.PERCENTAGE);
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
        attributeStatusContainer.add(attributeTypeLabel);
        attributeStatusContainer.setVerticalComponentAlignment(Alignment.CENTER);

        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        updateAttributeValueButton = new Button();
        updateAttributeValueButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        Icon plusIcon = VaadinIcon.EDIT.create();
        plusIcon.setSize("18px");
        updateAttributeValueButton.setIcon(plusIcon);
        Tooltips.getCurrent().setTooltip(updateAttributeValueButton, "更新属性值");
        updateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                enableEditAttributeValue();
            }
        });
        controlButtonsContainer.add(updateAttributeValueButton);

        confirmUpdateAttributeValueButton = new Button();
        confirmUpdateAttributeValueButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        Icon multiIcon = VaadinIcon.CHECK_CIRCLE_O.create();
        multiIcon.setSize("20px");
        confirmUpdateAttributeValueButton.setIcon(multiIcon);
        Tooltips.getCurrent().setTooltip(confirmUpdateAttributeValueButton, "确认更新");
        confirmUpdateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelEditAttributeValue();
            }
        });
        controlButtonsContainer.add(confirmUpdateAttributeValueButton);
        confirmUpdateAttributeValueButton.setVisible(false);

        cancelUpdateValueButton = new Button();
        cancelUpdateValueButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        Icon notIcon = VaadinIcon.ARROW_BACKWARD.create();
        notIcon.setSize("20px");
        cancelUpdateValueButton.setIcon(notIcon);
        Tooltips.getCurrent().setTooltip(cancelUpdateValueButton, "取消更新");
        cancelUpdateValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelEditAttributeValue();
            }
        });
        controlButtonsContainer.add(cancelUpdateValueButton);
        cancelUpdateValueButton.setVisible(false);

        deleteAttributeButton = new Button();
        deleteAttributeButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        deleteAttributeButton.setIcon(VaadinIcon.ERASER.create());
        Tooltips.getCurrent().setTooltip(deleteAttributeButton, "删除属性值");
        deleteAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //removeCurrentConditionLogic();
            }
        });
        controlButtonsContainer.add(deleteAttributeButton);

        controlButtonsContainer.setVerticalComponentAlignment(Alignment.START,updateAttributeValueButton,confirmUpdateAttributeValueButton,cancelUpdateValueButton,deleteAttributeButton);
        attributeMetaLayout.add(controlButtonsContainer);
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.START,controlButtonsContainer);

        HorizontalLayout attributeValueInfoContainerLayout = new HorizontalLayout();
        attributeValueInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        Icon inputIcon = VaadinIcon.INPUT.create();
        inputIcon.setSize("10px");
        attributeValueInfoContainerLayout.add(inputIcon);

        this.valueEditor = generateValueEditorTextField();
        attributeValueInfoContainerLayout.add(valueEditor);
        this.add(attributeValueInfoContainerLayout);

        this.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
    }

    private Component generateValueEditorTextField() {
        if (this.attributeValue != null && this.attributeDataType != null) {
            Component currentConditionValueEditor = null;
            switch (this.attributeDataType) {
                case STRING:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
                    break;
                case BOOLEAN:
                    currentConditionValueEditor = new ComboBox();
                    ((ComboBox)currentConditionValueEditor).addThemeVariants(ComboBoxVariant.LUMO_SMALL);
                    ((ComboBox)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((ComboBox)currentConditionValueEditor).setAllowCustomValue(false);
                    ((ComboBox)currentConditionValueEditor).setItems("-","true","false");
                    ((ComboBox)currentConditionValueEditor).setValue("-");
                    if(this.attributeValue != null){
                        if((Boolean)this.attributeValue.getAttributeValue()){
                            ((ComboBox)currentConditionValueEditor).setValue("true");
                        }else{
                            ((ComboBox)currentConditionValueEditor).setValue("false");
                        }
                    }
                    break;
                case DATE:
                    currentConditionValueEditor = new DatePicker();
                    ((DatePicker)currentConditionValueEditor).addThemeVariants(DatePickerVariant.LUMO_SMALL);
                    ((DatePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);





                    if(this.attributeValue != null){
                        Date valueDate = (Date)this.attributeValue.getAttributeValue();
                        LocalDateTime ldt = valueDate.toInstant().atZone( ZoneId.systemDefault()).toLocalDateTime();
                       // ((DatePicker)currentConditionValueEditor).setValue(valueDate);
                    }
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
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
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
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
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
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
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
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
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
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
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
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
                    break;
                case BYTE:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
                    break;
                case BINARY:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((TextField)currentConditionValueEditor).setValue(this.attributeValue.getAttributeValue().toString());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
                    break;
            }
            return currentConditionValueEditor;
        }
        return null;
    }

    private void enableEditAttributeValue(){
        updateAttributeValueButton.setVisible(false);
        cancelUpdateValueButton.setVisible(true);
        confirmUpdateAttributeValueButton.setVisible(true);
        ((AbstractField)valueEditor).setReadOnly(false);

    }

    private void cancelEditAttributeValue(){
        updateAttributeValueButton.setVisible(true);
        cancelUpdateValueButton.setVisible(false);
        confirmUpdateAttributeValueButton.setVisible(false);
        ((AbstractField)valueEditor).setReadOnly(true);
        ((AbstractField)valueEditor).setValue(this.attributeValue.getAttributeValue().toString());
    }
}
