package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;

public class AddDataSlicePropertyView extends VerticalLayout {
    private Dialog containerDialog;
    private NativeLabel errorMessage;
    private TextField propertyNameField;
    private ComboBox<DataSlicePropertyType> propertyDataTypeFilterSelect;
    private Binder<String> binder;
    private DataSlicePropertyOperateHandler dataSlicePropertyOperateHandler;
    private Checkbox isPrimaryKeyCheckbox;

    public AddDataSlicePropertyView(){
        this.setMargin(false);
        this.setSpacing(false);
        this.binder = new Binder<>();

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("新属性信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);

        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        HorizontalLayout attributeMetaInfoFieldContainerLayout = new HorizontalLayout();
        add(attributeMetaInfoFieldContainerLayout);

        propertyNameField = new TextField();
        attributeMetaInfoFieldContainerLayout.add(propertyNameField);
        propertyNameField.setPlaceholder("属性名称");
        propertyNameField.setWidth(250, Unit.PIXELS);

        propertyDataTypeFilterSelect = new ComboBox();
        propertyDataTypeFilterSelect.setPageSize(30);
        propertyDataTypeFilterSelect.setPlaceholder("属性数据类型");
        propertyDataTypeFilterSelect.setWidth(170, Unit.PIXELS);

        DataSlicePropertyType[] dataSlicePropertyTypeArray =
                new DataSlicePropertyType[]{
                        DataSlicePropertyType.STRING,
                        DataSlicePropertyType.BOOLEAN,
                        DataSlicePropertyType.INT,
                        DataSlicePropertyType.SHORT,
                        DataSlicePropertyType.LONG,
                        DataSlicePropertyType.FLOAT,
                        DataSlicePropertyType.DOUBLE,
                        DataSlicePropertyType.DATE,
                        DataSlicePropertyType.TIME,
                        DataSlicePropertyType.TIMESTAMP,
                        DataSlicePropertyType.BYTE,
                        DataSlicePropertyType.DECIMAL,
                        DataSlicePropertyType.BINARY,
                        DataSlicePropertyType.GEOMETRY,
                        DataSlicePropertyType.UUID
        };
        propertyDataTypeFilterSelect.setItems(dataSlicePropertyTypeArray);
        attributeMetaInfoFieldContainerLayout.add(propertyDataTypeFilterSelect);

        HorizontalLayout divLayout = new HorizontalLayout();
        divLayout.setHeight(5,Unit.PIXELS);
        add(divLayout);

        HorizontalLayout attributeMetaInfoFieldContainerLayout2 = new HorizontalLayout();
        add(attributeMetaInfoFieldContainerLayout2);

        isPrimaryKeyCheckbox = new Checkbox();
        isPrimaryKeyCheckbox.setLabel("数据切片主键属性");
        isPrimaryKeyCheckbox.setWidth(340,Unit.PIXELS);
        attributeMetaInfoFieldContainerLayout2.add(isPrimaryKeyCheckbox);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(80,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(propertyNameField.getValue().equals("") || propertyDataTypeFilterSelect.getValue()==null){
                    errorMessage.setText("属性名称与数据类型是必填项");
                    errorMessage.setVisible(true);
                }else{
                    if(dataSlicePropertyOperateHandler != null){
                        String propertyName = propertyNameField.getValue();
                        DataSlicePropertyType dataSlicePropertyType = propertyDataTypeFilterSelect.getValue();
                        boolean isPrimaryKeyFlag = isPrimaryKeyCheckbox.getValue();
                        DataSlicePropertyValueObject dataSlicePropertyValueObject = new DataSlicePropertyValueObject();
                        dataSlicePropertyValueObject.setPropertyName(propertyName);
                        dataSlicePropertyValueObject.setDataSlicePropertyType(dataSlicePropertyType);
                        dataSlicePropertyValueObject.setPrimaryKey(isPrimaryKeyFlag);
                        dataSlicePropertyOperateHandler.handleDataSlicePropertyValue(dataSlicePropertyValueObject);
                    }
                }
            }
        });

        attributeMetaInfoFieldContainerLayout2.add(confirmButton);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setDataSlicePropertyOperateHandler(DataSlicePropertyOperateHandler dataSlicePropertyOperateHandler) {
        this.dataSlicePropertyOperateHandler = dataSlicePropertyOperateHandler;
    }
}
