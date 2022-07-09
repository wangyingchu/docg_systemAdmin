package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import dev.mett.vaadin.tooltip.Tooltips;

public class QueryResultSetConfigView extends VerticalLayout {

    private Dialog containerDialog;
    private IntegerField pageSizeField;
    private IntegerField startPageField;
    private IntegerField endPageField;
    private IntegerField resultNumberField;

    public QueryResultSetConfigView(){
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("结果集配置参数");
        messageContainerLayout.add(viewTitle);

        H6 errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        HorizontalLayout messageTitleLayout = new HorizontalLayout();
        messageTitleLayout.setPadding(false);
        Icon infoIcon = new Icon(VaadinIcon.INFO_CIRCLE);
        infoIcon.setSize("10px");
        infoIcon.getStyle().set("color","var(--lumo-contrast-80pct)");
        messageTitleLayout.add(infoIcon);
        Label attributeTypeLabel = new Label("设置查询最大返回结果数将忽略查询起始页和查询结束页中的设置");
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        messageTitleLayout.add(attributeTypeLabel);
        add(messageTitleLayout);

        this.pageSizeField = new IntegerField("查询单页返回结果数 - Page Size");
        this.pageSizeField.setWidthFull();
        this.pageSizeField.setTitle("请输入单页包含数据数");
        this.pageSizeField.setValue(100);
        this.pageSizeField.setMin(10);
        add(pageSizeField);
        Button resetToDefaultButton1 = new Button();
        Tooltips.getCurrent().setTooltip(resetToDefaultButton1, "重置为默认值");
        resetToDefaultButton1.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton1.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon.setSize("18px");
        resetToDefaultButton1.setIcon(buttonIcon);
        this.pageSizeField.setPrefixComponent(resetToDefaultButton1);

        this.startPageField = new IntegerField("查询起始页 - Start Page");
        this.startPageField.setWidthFull();
        this.startPageField.setTitle("请输入查询起始页");
        this.startPageField.setValue(1);
        this.startPageField.setMin(1);
        add(this.startPageField);
        Button resetToDefaultButton2 = new Button();
        Tooltips.getCurrent().setTooltip(resetToDefaultButton2, "重置为默认值");
        resetToDefaultButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton2.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon2 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon2.setSize("18px");
        resetToDefaultButton2.setIcon(buttonIcon2);
        this.startPageField.setPrefixComponent(resetToDefaultButton2);

        this.endPageField = new IntegerField("查询结束页 - End Page");
        this.endPageField.setWidthFull();
        this.endPageField.setTitle("请输入查询结束页");
        this.endPageField.setValue(101);
        add(this.endPageField);
        Button resetToDefaultButton3 = new Button();
        Tooltips.getCurrent().setTooltip(resetToDefaultButton3, "重置为默认值");
        resetToDefaultButton3.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton3.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon3 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon3.setSize("18px");
        resetToDefaultButton3.setIcon(buttonIcon3);
        this.endPageField.setPrefixComponent(resetToDefaultButton3);

        this.resultNumberField = new IntegerField("查询最大返回结果数 - Result Number");
        this.resultNumberField.setWidthFull();
        this.resultNumberField.setClearButtonVisible(true);
        this.resultNumberField.setTitle("请输入查询最大返回结果数");
        this.resultNumberField.setMin(1);
        add(resultNumberField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //doCreateNewConceptionKind();
            }
        });
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
