package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiValuePropertyInputWidget extends HorizontalLayout {

    private HorizontalLayout valuesDisplayContainer;
    private QueryConditionItemWidget queryConditionItemWidget;
    private AbstractField currentValueInputField;
    private List<Object> valuesList;

    private Map<Object,HorizontalLayout> valueDisplaySectionMap;

    public MultiValuePropertyInputWidget(int valueDisplayFieldWidth){
        this.valuesList=new ArrayList<Object>();
        this.valueDisplaySectionMap=new HashMap<Object,HorizontalLayout>();

        Button addNewValueButton=new Button();
        //addNewValueButton.setIcon(FontAwesome.PLUS_CIRCLE);
        //addNewValueButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        //addNewValueButton.addStyleName(ValoTheme.BUTTON_TINY);

        addNewValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                displayNewValueInput();
            }
        });

        add(addNewValueButton);

        HorizontalLayout spacingDiv=new HorizontalLayout();
        spacingDiv.setWidth(5, Unit.PIXELS);
        add(spacingDiv);

        //Panel valuesDisplayPanel=new Panel();
        //valuesDisplayPanel.setWidth(valueDisplayFieldWidth,Unit.PIXELS);
        //valuesDisplayPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        //addComponent(valuesDisplayPanel);

        this.valuesDisplayContainer=new HorizontalLayout();
        //valuesDisplayPanel.setContent(this.valuesDisplayContainer);
add(this.valuesDisplayContainer);



    }





    private void displayNewValueInput(){
        /*
        final Window window = new Window();
        window.setWidth(310.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        */

        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setSpacing(true);
        containerLayout.setMargin(true);

        Label inputValueSectionTitle=new Label("添加新的值");
        //inputValueSectionTitle.addStyleName(ValoTheme.LABEL_COLORED);
        //inputValueSectionTitle.addStyleName(ValoTheme.LABEL_BOLD);
        containerLayout.add(inputValueSectionTitle);

        HorizontalLayout valueInputFieldLayout=new HorizontalLayout();
        containerLayout.add(valueInputFieldLayout);
        if(this.currentValueInputField!=null){
            //this.currentValueInputField.discard();
        }
        this.currentValueInputField=(AbstractField)this.getQueryConditionItemWidget().generateSingleQueryValueTextField(170);
        valueInputFieldLayout.add(this.currentValueInputField);

        HorizontalLayout spacingDiv=new HorizontalLayout();
        spacingDiv.setWidth(20,Unit.PIXELS);
        valueInputFieldLayout.add(spacingDiv);

        Button addNewButton=new Button("确定");
        //addNewButton.addStyleName(ValoTheme.BUTTON_SMALL);
        //addNewButton.setIcon(FontAwesome.CHECK_CIRCLE);
        addNewButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //boolean currentEditorValidateResult=currentValueInputField.isValid();

                boolean currentEditorValidateResult = true;
                if(!currentEditorValidateResult){
                    /*
                    Notification errorNotification = new Notification("数据校验错误",
                            "当前输入值非法", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    */
                    return;
                }
                addNewValue();
                //window.close();
            }
        });
        valueInputFieldLayout.add(addNewButton);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"添加自定义查询/显示属性",null,true,310,180,false);
        fixSizeWindow.setWindowContent(containerLayout);
        fixSizeWindow.setModel(true);
        //addCustomQueryCriteriaUI.setContainerDialog(fixSizeWindow);
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
        }
        if(!this.valuesList.contains(propertyValueObj)){
            this.valuesList.add(propertyValueObj);
            this.valuesDisplayContainer.add(generateValueDisplaySection(propertyValueObj));
        }
    }

    private HorizontalLayout generateValueDisplaySection(final Object valueObject){
        HorizontalLayout displayContainer=new HorizontalLayout();
        Label newValueLabel=new Label(valueObject.toString());
        //newValueLabel.addStyleName(ValoTheme.LABEL_BOLD);
        Button removeValueButton=new Button();
        //removeValueButton.setIcon(FontAwesome.REMOVE);
        //removeValueButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        //removeValueButton.addStyleName(ValoTheme.BUTTON_LINK);
       // removeValueButton.addStyleName(ValoTheme.BUTTON_TINY);


        removeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                removeValue(valueObject);
            }
        });

        displayContainer.add(newValueLabel);
        displayContainer.add(removeValueButton);
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
