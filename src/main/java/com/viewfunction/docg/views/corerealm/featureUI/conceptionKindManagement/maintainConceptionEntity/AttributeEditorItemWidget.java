package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
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
import com.vaadin.flow.component.notification.NotificationVariant;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityAttributeUpdatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import dev.mett.vaadin.tooltip.Tooltips;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class AttributeEditorItemWidget extends VerticalLayout {
    private Component valueEditor;
    private AttributeValue attributeValue;
    private String attributeName;
    private AttributeDataType attributeDataType;
    private Button updateAttributeValueButton;
    private Button cancelUpdateValueButton;
    private Button confirmUpdateAttributeValueButton;
    private Button deleteAttributeButton;
    private Binder<String> attributeValueDataBinder;
    private String conceptionKind;
    private String conceptionEntityUID;
    public AttributeEditorItemWidget(String conceptionKind,String conceptionEntityUID,AttributeValue attributeValue){
        this.attributeValue = attributeValue;
        this.attributeName = attributeValue.getAttributeName();
        this.attributeDataType = attributeValue.getAttributeDataType();
        this.attributeValueDataBinder = new Binder<>();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

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
        Tooltips.getCurrent().setTooltip(updateAttributeValueButton, "???????????????");
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
        Tooltips.getCurrent().setTooltip(confirmUpdateAttributeValueButton, "????????????");
        confirmUpdateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                updateAttributeValue();
            }
        });
        controlButtonsContainer.add(confirmUpdateAttributeValueButton);
        confirmUpdateAttributeValueButton.setVisible(false);

        cancelUpdateValueButton = new Button();
        cancelUpdateValueButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        Icon notIcon = VaadinIcon.ARROW_BACKWARD.create();
        notIcon.setSize("20px");
        cancelUpdateValueButton.setIcon(notIcon);
        Tooltips.getCurrent().setTooltip(cancelUpdateValueButton, "????????????");
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
        Tooltips.getCurrent().setTooltip(deleteAttributeButton, "???????????????");
        deleteAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                deleteAttribute();
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

    public String getAttributeName(){
        return this.attributeName;
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
                    ((ComboBox)currentConditionValueEditor).setItems("true","false");
                    if(this.attributeValue != null){
                        if((Boolean)this.attributeValue.getAttributeValue()){
                            ((ComboBox)currentConditionValueEditor).setValue("true");
                        }else{
                            ((ComboBox)currentConditionValueEditor).setValue("false");
                        }
                    }
                    ((ComboBox)currentConditionValueEditor).setReadOnly(true);
                    break;
                case DATE:
                    currentConditionValueEditor = new DatePicker();
                    ((DatePicker)currentConditionValueEditor).addThemeVariants(DatePickerVariant.LUMO_SMALL);
                    ((DatePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((DatePicker)currentConditionValueEditor).setValue((LocalDate)this.attributeValue.getAttributeValue());
                    ((DatePicker)currentConditionValueEditor).setReadOnly(true);
                    break;
                case TIME:
                    currentConditionValueEditor = new TimePicker();
                    ((TimePicker)currentConditionValueEditor).addThemeVariants(TimePickerVariant.LUMO_SMALL);
                    ((TimePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((TimePicker)currentConditionValueEditor).setValue((LocalTime)this.attributeValue.getAttributeValue());
                    ((TimePicker)currentConditionValueEditor).setReadOnly(true);
                    break;
                case DATETIME:
                    currentConditionValueEditor = new DateTimePicker();
                    ((DateTimePicker)currentConditionValueEditor).addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
                    ((DateTimePicker)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((DateTimePicker)currentConditionValueEditor).setValue((LocalDateTime)this.attributeValue.getAttributeValue());
                    ((DateTimePicker)currentConditionValueEditor).setReadOnly(true);
                    break;
                case TIMESTAMP:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    ((TextField)currentConditionValueEditor).setValue(""+((Date)this.attributeValue.getAttributeValue()).getTime());
                    ((TextField)currentConditionValueEditor).setReadOnly(true);
                    break;
                case INT:
                    currentConditionValueEditor = new TextField();
                    ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    ((TextField)currentConditionValueEditor).setWidth(100,Unit.PERCENTAGE);
                    attributeValueDataBinder.forField((TextField)currentConditionValueEditor).withConverter(new StringToIntegerConverter("????????????????????????INT??????"))
                            .withValidator(new IntegerRangeValidator("????????????????????????INT??????", null, null))
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
                                    new StringToLongConverter("????????????????????????LONG??????"))
                            .withValidator(new LongRangeValidator("????????????????????????LONG??????", null, null))
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
                                    new StringToDoubleConverter("????????????????????????DOUBLE??????"))
                            .withValidator(new DoubleRangeValidator("????????????????????????DOUBLE??????", null, null))
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
                                    new StringToFloatConverter("????????????????????????FLOAT??????"))
                            .withValidator(new FloatRangeValidator("????????????????????????FLOAT??????", null, null))
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
                                    new StringToBigDecimalConverter("????????????????????????DECIMAL??????"))
                            .withValidator(new BigDecimalRangeValidator("????????????????????????DECIMAL??????", null, null))
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
                                    new StringToIntegerConverter("????????????????????????SHORT??????"))
                            .withValidator(new IntegerRangeValidator("????????????????????????SHORT??????", null, null))
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
        AttributeDataType attributeDataType = this.attributeValue.getAttributeDataType();
        if(AttributeDataType.DATETIME.equals(attributeDataType)){
            LocalDateTime orgDateTime = (LocalDateTime)this.attributeValue.getAttributeValue();
            ((DateTimePicker)valueEditor).setValue(orgDateTime);
        }else if(AttributeDataType.DATE.equals(attributeDataType)){
            LocalDate orgDate = (LocalDate)this.attributeValue.getAttributeValue();
            ((DatePicker)valueEditor).setValue(orgDate);
        }else if(AttributeDataType.TIME.equals(attributeDataType)){
            LocalTime orgTime = (LocalTime)this.attributeValue.getAttributeValue();
            ((TimePicker)valueEditor).setValue(orgTime);
        }else if(AttributeDataType.TIMESTAMP.equals(attributeDataType)){
            Date orgTimestamp = (Date)this.attributeValue.getAttributeValue();
            ((TextField)valueEditor).setValue(""+orgTimestamp.getTime());
        }else{
            ((AbstractField)valueEditor).setValue(this.attributeValue.getAttributeValue().toString());
        }
    }

    private void updateAttributeValue(){
        if(validateAttributeValue()){
            //show errorMessage
        }else{
            //do update logic
            updateEntityAttribute();
        }
    }

    private void updateEntityAttribute(){
        String attributeValueString = null;
        Object newEntityAttributeValue = null;

        if (valueEditor != null) {
            Component currentConditionValueEditor = valueEditor;
            AttributeDataType attributeDataType = this.attributeValue.getAttributeDataType();
            switch (attributeDataType) {
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

            if(newEntityAttributeValue != null) {
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
                if (targetConceptionKind == null) {
                    CommonUIOperationUtil.showPopupNotification("???????????? " + conceptionKind + " ?????????", NotificationVariant.LUMO_ERROR);
                } else {
                    ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                    if (targetConceptionEntity == null) {
                        CommonUIOperationUtil.showPopupNotification("???????????? " + conceptionKind + " ???????????? UID ???" + conceptionEntityUID + " ???????????????", NotificationVariant.LUMO_ERROR);
                    } else {
                        if (!targetConceptionEntity.hasAttribute(attributeName)) {
                            CommonUIOperationUtil.showPopupNotification("UID ??? " + conceptionEntityUID + " ????????????????????????????????? " + attributeName, NotificationVariant.LUMO_ERROR);
                        } else {
                            try {
                                AttributeValue attributeValue = null;
                                if (newEntityAttributeValue instanceof Boolean) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Boolean) newEntityAttributeValue).booleanValue());
                                } else if (newEntityAttributeValue instanceof Integer) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Integer) newEntityAttributeValue).intValue());
                                } else if (newEntityAttributeValue instanceof Short) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Short) newEntityAttributeValue).shortValue());
                                } else if (newEntityAttributeValue instanceof Long) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Long) newEntityAttributeValue).longValue());
                                } else if (newEntityAttributeValue instanceof Float) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Float) newEntityAttributeValue).floatValue());
                                } else if (newEntityAttributeValue instanceof Double) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Double) newEntityAttributeValue).doubleValue());
                                } else if (newEntityAttributeValue instanceof Byte) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, ((Byte) newEntityAttributeValue).byteValue());
                                } else if (newEntityAttributeValue instanceof Date) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, (Date) newEntityAttributeValue);
                                } else if (newEntityAttributeValue instanceof LocalDateTime) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, (LocalDateTime) newEntityAttributeValue);
                                } else if (newEntityAttributeValue instanceof LocalDate) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, (LocalDate) newEntityAttributeValue);
                                } else if (newEntityAttributeValue instanceof LocalTime) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, (LocalTime) newEntityAttributeValue);
                                } else if (newEntityAttributeValue instanceof BigDecimal) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, (BigDecimal) newEntityAttributeValue);
                                } else if (newEntityAttributeValue instanceof String) {
                                    attributeValue = targetConceptionEntity.updateAttribute(attributeName, newEntityAttributeValue.toString());
                                }
                                if (attributeValue != null) {
                                    ConceptionEntityAttributeUpdatedEvent conceptionEntityAttributeUpdatedEvent = new ConceptionEntityAttributeUpdatedEvent();
                                    conceptionEntityAttributeUpdatedEvent.setConceptionEntityUID(this.conceptionEntityUID);
                                    conceptionEntityAttributeUpdatedEvent.setConceptionKindName(this.conceptionKind);
                                    conceptionEntityAttributeUpdatedEvent.setAttributeValue(attributeValue);
                                    ResourceHolder.getApplicationBlackboard().fire(conceptionEntityAttributeUpdatedEvent);
                                    updateAttributeValueButton.setVisible(true);
                                    cancelUpdateValueButton.setVisible(false);
                                    confirmUpdateAttributeValueButton.setVisible(false);
                                    ((AbstractField)valueEditor).setReadOnly(true);
                                    CommonUIOperationUtil.showPopupNotification("??? UID ??? " + conceptionEntityUID + " ?????????????????????????????? " + attributeName + " ??????", NotificationVariant.LUMO_SUCCESS);
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

    private boolean validateAttributeValue() {
        if (valueEditor != null) {
            Component currentConditionValueEditor = valueEditor;
            AttributeDataType attributeDataType = this.attributeValue.getAttributeDataType();
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

    private void deleteAttribute(){
        DeleteConceptionEntityAttributeView deleteConceptionEntityAttributeView = new DeleteConceptionEntityAttributeView(this.conceptionKind,this.conceptionEntityUID,this.attributeName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.ERASER),"????????????????????????",null,true,550,210,false);
        fixSizeWindow.setWindowContent(deleteConceptionEntityAttributeView);
        fixSizeWindow.setModel(true);
        deleteConceptionEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
