package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import static com.viewfunction.docg.element.userInterfaceUtil.CommonConstant.*;

public class AddCustomQueryCriteriaUI extends VerticalLayout {

    private Dialog containerDialog;
    private H6 errorMessage;

    public AddCustomQueryCriteriaUI(){
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("自定义查询/显示条件信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        HorizontalLayout criteriaFieldContainerLayout = new HorizontalLayout();
        add(criteriaFieldContainerLayout);

        TextField conceptionKindNameField = new TextField();
        criteriaFieldContainerLayout.add(conceptionKindNameField);
        conceptionKindNameField.setPlaceholder("属性名称");
        conceptionKindNameField.setWidth(150,Unit.PIXELS);

        ComboBox queryCriteriaFilterSelect = new ComboBox();
        queryCriteriaFilterSelect.setPageSize(30);
        queryCriteriaFilterSelect.setPlaceholder("属性数据类型");
        queryCriteriaFilterSelect.setMinWidth(70, Unit.PIXELS);
        queryCriteriaFilterSelect.setItems(
                PropertyTypeClassification_BOOLEAN,
                PropertyTypeClassification_INT,
                PropertyTypeClassification_SHORT,
                PropertyTypeClassification_LONG,
                PropertyTypeClassification_FLOAT,
                PropertyTypeClassification_DOUBLE,
                PropertyTypeClassification_DATE,
                PropertyTypeClassification_TIMESTAMP,
                PropertyTypeClassification_STRING,
                PropertyTypeClassification_BINARY,
                PropertyTypeClassification_BYTE,
                PropertyTypeClassification_DECIMAL
        );
        criteriaFieldContainerLayout.add(queryCriteriaFilterSelect);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        criteriaFieldContainerLayout.add(confirmButton);
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
