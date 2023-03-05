package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.QueryConditionItemWidget;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiValuePropertyInputWidget extends HorizontalLayout {
    private VerticalLayout valuesDisplayContainer;
    private QueryConditionItemWidget queryConditionItemWidget;
    private AbstractField currentValueInputField;
    private List<Object> valuesList;
    private Map<Object,HorizontalLayout> valueDisplaySectionMap;

    public MultiValuePropertyInputWidget(int valueDisplayFieldWidth){
        this.valuesList=new ArrayList<Object>();
        this.valueDisplaySectionMap=new HashMap<Object,HorizontalLayout>();

        Button addNewValueButton = new Button();
        addNewValueButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        addNewValueButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        Tooltips.getCurrent().setTooltip(addNewValueButton, "添加查询值");
        addNewValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                displayNewValueInput();
            }
        });
        add(addNewValueButton);

        this.valuesDisplayContainer = new VerticalLayout();
        this.valuesDisplayContainer.setMargin(false);
        this.valuesDisplayContainer.setPadding(false);
        this.valuesDisplayContainer.setSpacing(false);
        this.valuesDisplayContainer.setWidth(valueDisplayFieldWidth,Unit.PIXELS);

        Scroller queryConditionItemScroller = new Scroller(this.valuesDisplayContainer);
        queryConditionItemScroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        add(queryConditionItemScroller);
    }

    private void displayNewValueInput(){
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setSpacing(false);
        containerLayout.setMargin(false);

        HorizontalLayout valueInputFieldLayout=new HorizontalLayout();
        containerLayout.add(valueInputFieldLayout);
        this.currentValueInputField = (AbstractField)this.getQueryConditionItemWidget().generateSingleQueryValueTextField(200);
        valueInputFieldLayout.add(this.currentValueInputField);

        HorizontalLayout spacingDiv=new HorizontalLayout();
        spacingDiv.setWidth(5,Unit.PIXELS);
        valueInputFieldLayout.add(spacingDiv);

        Button addNewButton=new Button("确定");
        addNewButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        addNewButton.setIcon(VaadinIcon.CHECK_SQUARE.create());
        addNewButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                boolean currentEditorInValidateResult = false;
                if(currentValueInputField instanceof TextField){
                    currentEditorInValidateResult = ((TextField)currentValueInputField).isInvalid();
                }
                if(currentEditorInValidateResult){
                    CommonUIOperationUtil.showPopupNotification("数据校验错误,当前输入值非法", NotificationVariant.LUMO_ERROR);
                }else{
                    addNewValue();
                }
            }
        });
        valueInputFieldLayout.add(addNewButton);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"添加自定义查询值",null,true,350,140,false);
        fixSizeWindow.setWindowContent(containerLayout);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    public QueryConditionItemWidget getQueryConditionItemWidget() {
        return this.queryConditionItemWidget;
    }

    public void setQueryConditionItemWidget(QueryConditionItemWidget queryConditionItemWidget) {
        this.queryConditionItemWidget = queryConditionItemWidget;
    }

    private void addNewValue(){
        AttributeDataType attributeDataType = this.getQueryConditionItemWidget().getAttributeDataType();
        Object propertyValueObj=null;
        switch(attributeDataType){
            case STRING:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case BOOLEAN:
                propertyValueObj=((ComboBox)this.currentValueInputField).getValue();
                break;
            case DATE:
                propertyValueObj=((DatePicker)this.currentValueInputField).getValue();
                break;
            case INT:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case LONG:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case DOUBLE:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case FLOAT:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case SHORT:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case TIME:
                propertyValueObj=((TimePicker)this.currentValueInputField).getValue();
                break;
            case DATETIME:
                propertyValueObj=((DateTimePicker)this.currentValueInputField).getValue();
                break;
            case TIMESTAMP:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
            case DECIMAL:
                propertyValueObj=((TextField)this.currentValueInputField).getValue();
                break;
        }
        if(!this.valuesList.contains(propertyValueObj)){
            this.valuesList.add(propertyValueObj);
            this.valuesDisplayContainer.add(generateValueDisplaySection(propertyValueObj));
        }
    }

    private HorizontalLayout generateValueDisplaySection(final Object valueObject){
        HorizontalLayout displayContainer = new HorizontalLayout();
        displayContainer.setSpacing(false);

        Span propertyValueSpan = new Span(new Span(valueObject.toString()));
        propertyValueSpan.addClassName("text-2xs");
        propertyValueSpan.getElement().getThemeList().add("badge contrast");

        Button removeValueButton = new Button();
        removeValueButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        Icon removeIcon = VaadinIcon.DEL_A.create();
        removeIcon.setSize("18px");
        removeValueButton.setIcon(removeIcon);
        Tooltips.getCurrent().setTooltip(removeValueButton, "撤销该值");
        removeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                removeValue(valueObject);
            }
        });

        displayContainer.add(propertyValueSpan);
        displayContainer.add(removeValueButton);
        displayContainer.setVerticalComponentAlignment(Alignment.CENTER,propertyValueSpan);
        this.valueDisplaySectionMap.put(valueObject,displayContainer);
        return displayContainer;
    }

    private void removeValue(Object valueToDelete){
        HorizontalLayout layoutToDelete=this.valueDisplaySectionMap.get(valueToDelete);
        if(layoutToDelete!=null){
            this.valuesDisplayContainer.remove(layoutToDelete);
            this.valueDisplaySectionMap.remove(valueToDelete);
        }
        if(this.valuesList.contains(valueToDelete)){
            this.valuesList.remove(valueToDelete);
        }
    }

    public List<Object> getMultiValueList(){
        return this.valuesList;
    }
}
