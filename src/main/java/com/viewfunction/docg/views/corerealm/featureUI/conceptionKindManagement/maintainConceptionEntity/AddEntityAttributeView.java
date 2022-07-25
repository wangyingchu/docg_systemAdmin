package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class AddEntityAttributeView extends VerticalLayout {

    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField attributeNameField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;
    public AddEntityAttributeView(String conceptionKind,String conceptionEntityUID){
        this.setMargin(false);
        this.setSpacing(false);

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

        Label errorMessage = new Label("-");
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

        HorizontalLayout attributeValueFieldContainerLayout = new HorizontalLayout();
        add(attributeValueFieldContainerLayout);

        TextField attributeValueField = new TextField();
        attributeValueField.setWidth(340,Unit.PIXELS);
        attributeValueFieldContainerLayout.add(attributeValueField);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(attributeNameField.getValue().equals("")|| attributeDataTypeFilterSelect.getValue()==null){
                    errorMessage.setText("属性名称与数据类型是必填项");
                    errorMessage.setVisible(true);
                }else{
                    /*
                    getConceptionKindQueryCriteriaView().addQueryConditionItem(propertyNameField.getValue(),propertyDataTypeFilterSelect.getValue());
                    if(getContainerDialog() != null){
                        getContainerDialog().close();
                    }
                   */
                }
            }
        });
        attributeValueFieldContainerLayout.add(confirmButton);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
