package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.spreadsheet.shared.ContentMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

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
    private Binder<String> propertyValueDataBinder;
    private Component valueEditor;
    private Object propertyValue;

    private String cimSpaceName;
    private String infoObjectType;
    private String objectInstanceRID;

    public AttributeEditorItemWidget(){
        this.setWidth(100, Unit.PERCENTAGE);
        this.add(new Label("##########"));










        this.propertyValueDataBinder = new Binder<>();
        this.cimSpaceName=cimSpaceName;
        this.infoObjectType=infoObjectType;
        this.objectInstanceRID=objectInstanceRID;

        this.propertyValue = propertyValue;
        setSpacing(true);
        setMargin(true);
        //this.addStyleName("ui_appSection_Top_LightDiv");

        HorizontalLayout queryPropertyAndGroupIngInfoContainerLayout=new HorizontalLayout();
        this.add(queryPropertyAndGroupIngInfoContainerLayout);

        this.propertyNameLabel=new Label("-");
        //propertyNameLabel.addStyleName(ValoTheme.LABEL_BOLD);
        //propertyNameLabel.addStyleName(ValoTheme.LABEL_TINY);
        queryPropertyAndGroupIngInfoContainerLayout.add(propertyNameLabel);

        HorizontalLayout propertyConditionControllerContainerLayout=new HorizontalLayout();
        queryPropertyAndGroupIngInfoContainerLayout.add(propertyConditionControllerContainerLayout);
        //queryPropertyAndGroupIngInfoContainerLayout.setComponentAlignment(propertyConditionControllerContainerLayout, Alignment.MIDDLE_RIGHT);
        //queryPropertyAndGroupIngInfoContainerLayout.setExpandRatio(propertyNameLabel,1.0f);

        this.updatePropertyValueButton=new Button();
        //this.updatePropertyValueButton.setDescription("更新属性值");
        //this.updatePropertyValueButton.setIcon(VaadinIcons.EDIT);
       // this.updatePropertyValueButton.addStyleName(ValoTheme.BUTTON_TINY);
       // this.updatePropertyValueButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        /*
        this.updatePropertyValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                enableEditValue();
            }
        });
        */
        propertyConditionControllerContainerLayout.add(this.updatePropertyValueButton);

        this.confirmUpdateButton=new Button();
        /*
        this.confirmUpdateButton.setDescription("确认更新");
        this.confirmUpdateButton.setIcon(VaadinIcons.CHECK_CIRCLE_O);
        this.confirmUpdateButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.confirmUpdateButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.confirmUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                confirmUpdateValue();
            }
        });
        */
        propertyConditionControllerContainerLayout.add(this.confirmUpdateButton);
        this.confirmUpdateButton.setVisible(false);

        this.cancelUpdateButton=new Button();
        /*
        this.cancelUpdateButton.setDescription("取消更新");
        this.cancelUpdateButton.setIcon(VaadinIcons.ARROW_BACKWARD);
        this.cancelUpdateButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.cancelUpdateButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.cancelUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setPropertyValue(propertyValue);
                disableEditValue();
            }
        });

         */
        propertyConditionControllerContainerLayout.add(this.cancelUpdateButton);
        this.cancelUpdateButton.setVisible(false);

        this.deletePropertyValueButton=new Button();
        /*
        this.deletePropertyValueButton.setDescription("删除属性值");
        this.deletePropertyValueButton.setIcon(VaadinIcons.ERASER);
        this.deletePropertyValueButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deletePropertyValueButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.deletePropertyValueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                removePropertyValue();
            }
        });
        */

        propertyConditionControllerContainerLayout.add(this.deletePropertyValueButton);

        HorizontalLayout conditionValueInfoContainerLayout = new HorizontalLayout();
        Label inputIconLabel=new Label();
        //Label inputIconLabel=new Label(VaadinIcons.INPUT., ContentMode.HTML);
        conditionValueInfoContainerLayout.add(inputIconLabel);

        this.valueEditor = generateValueEditorTextField(320);
        //this.valueEditor.setEnabled(false);
        conditionValueInfoContainerLayout.add(valueEditor);
        this.add(conditionValueInfoContainerLayout);















        this.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
    }








    private Component generateValueEditorTextField(int textFieldWidth) {
        /*
        if (propertyTypeVO != null) {
            String propertyDataType = propertyTypeVO.getPropertyFieldDataClassify();
            AbstractComponent currentConditionValueEditor = null;
            switch (propertyDataType) {
                case PropertyTypeClassification_STRING:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    if(this.propertyValue != null){
                        ((TextField)currentConditionValueEditor).setValue(this.propertyValue.toString());
                    }
                    break;
                case PropertyTypeClassification_BOOLEAN:
                    currentConditionValueEditor = new ComboBox();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((ComboBox) currentConditionValueEditor).setTextInputAllowed(false);
                    ((ComboBox) currentConditionValueEditor).setEmptySelectionAllowed(false);
                    ((ComboBox) currentConditionValueEditor).setItems("-","true","false");
                    ((ComboBox) currentConditionValueEditor).setValue("-");
                    if(this.propertyValue != null){
                        if((Boolean)this.propertyValue){
                            ((ComboBox)currentConditionValueEditor).setValue("true");
                        }else{
                            ((ComboBox)currentConditionValueEditor).setValue("false");
                        }
                    }
                    break;
                case PropertyTypeClassification_DATE:
                    currentConditionValueEditor = new DateTimeField();
                    currentConditionValueEditor.addStyleName(ValoTheme.DATEFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    ((DateTimeField) currentConditionValueEditor).setDateFormat("yyyy-MM-dd hh:mm:ss");
                    ((DateTimeField) currentConditionValueEditor).setTextFieldEnabled(false);

                    if(this.propertyValue != null){
                        Date valueDate = (Date)this.propertyValue;
                        LocalDateTime ldt = valueDate.toInstant().atZone( ZoneId.systemDefault()).toLocalDateTime();
                        ((DateTimeField)currentConditionValueEditor).setValue(ldt);
                    }
                    break;
                case PropertyTypeClassification_INT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    this.propertyValueDataBinder.forField((TextField)currentConditionValueEditor)
                            .withConverter(
                                    new StringToIntegerConverter("该项属性值必须为INT类型"))
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
                    if(this.propertyValue != null){
                        ((TextField)currentConditionValueEditor).setValue(this.propertyValue.toString());
                    }
                    break;
                case PropertyTypeClassification_LONG:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    this.propertyValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                    if(this.propertyValue != null){
                        ((TextField)currentConditionValueEditor).setValue(this.propertyValue.toString());
                    }
                    break;
                case PropertyTypeClassification_DOUBLE:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    this.propertyValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                    if(this.propertyValue != null){
                        ((TextField)currentConditionValueEditor).setValue(this.propertyValue.toString());
                    }
                    break;
                case PropertyTypeClassification_FLOAT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    this.propertyValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                    if(this.propertyValue != null){
                        ((TextField)currentConditionValueEditor).setValue(this.propertyValue.toString());
                    }
                    break;
                case PropertyTypeClassification_DECIMAL:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    this.propertyValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                    if(this.propertyValue != null){
                        BigDecimal valueBigDecimal = (BigDecimal)this.propertyValue;
                        ((TextField)currentConditionValueEditor).setValue(valueBigDecimal.toString());
                    }
                    break;
                case PropertyTypeClassification_SHORT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    this.propertyValueDataBinder.forField((TextField)currentConditionValueEditor)
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
                    if(this.propertyValue != null){
                        ((TextField)currentConditionValueEditor).setValue(this.propertyValue.toString());
                    }
                    break;
                case PropertyTypeClassification_BYTE:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    break;
                case PropertyTypeClassification_BINARY:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(textFieldWidth,Unit.PIXELS);
                    break;
            }
            return currentConditionValueEditor;
        }
        return null;
        */
        return new TextField();
    }
}
