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
    private TextField attributeNameField;
    private ComboBox<DataSlicePropertyType> attributeDataTypeFilterSelect;
    private Binder<String> binder;
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

        attributeNameField = new TextField();
        attributeMetaInfoFieldContainerLayout.add(attributeNameField);
        attributeNameField.setPlaceholder("属性名称");
        attributeNameField.setWidth(250, Unit.PIXELS);

        attributeDataTypeFilterSelect = new ComboBox();
        attributeDataTypeFilterSelect.setPageSize(30);
        attributeDataTypeFilterSelect.setPlaceholder("属性数据类型");
        attributeDataTypeFilterSelect.setWidth(170, Unit.PIXELS);

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
        attributeDataTypeFilterSelect.setItems(dataSlicePropertyTypeArray);
        attributeMetaInfoFieldContainerLayout.add(attributeDataTypeFilterSelect);

        this.attributeDataTypeFilterSelect.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<DataSlicePropertyType>, DataSlicePropertyType>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<DataSlicePropertyType>, DataSlicePropertyType> comboBoxAttributeDataTypeComponentValueChangeEvent) {
            }
        });

        HorizontalLayout divLayout = new HorizontalLayout();
        divLayout.setHeight(5,Unit.PIXELS);
        add(divLayout);

        HorizontalLayout attributeMetaInfoFieldContainerLayout2 = new HorizontalLayout();
        add(attributeMetaInfoFieldContainerLayout2);

        Checkbox isPromaryKeyCheckbox = new Checkbox();
        isPromaryKeyCheckbox.setLabel("数据切片主键属性");
        isPromaryKeyCheckbox.setWidth(340,Unit.PIXELS);
        attributeMetaInfoFieldContainerLayout2.add(isPromaryKeyCheckbox);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(80,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        attributeMetaInfoFieldContainerLayout2.add(confirmButton);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
