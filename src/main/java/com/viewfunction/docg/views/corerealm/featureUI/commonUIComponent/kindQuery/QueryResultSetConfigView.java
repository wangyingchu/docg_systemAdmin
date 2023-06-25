package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import dev.mett.vaadin.tooltip.Tooltips;

public class QueryResultSetConfigView extends VerticalLayout {

    private Dialog containerDialog;
    private IntegerField pageSizeField;
    private IntegerField startPageField;
    private IntegerField endPageField;
    private IntegerField resultNumberField;
    private QueryParameters queryParameters;
    private Checkbox distinctModeCheckbox;
    public QueryResultSetConfigView(QueryParameters queryParameters){
        this.queryParameters = queryParameters;
        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("结果集配置参数");
        messageContainerLayout.add(viewTitle);

        this.resultNumberField = new IntegerField("查询最大返回结果数 - Result Number");
        this.resultNumberField.setWidthFull();
        this.resultNumberField.setTitle("请输入查询最大返回结果数");
        this.resultNumberField.setMin(0);
        this.resultNumberField.setValue(this.queryParameters.getResultNumber());
        add(resultNumberField);
        Button resetToDefaultButton0 = new Button();
        resetToDefaultButton0.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                resultNumberField.setValue(queryParameters.getResultNumber());
            }
        });
        Tooltips.getCurrent().setTooltip(resetToDefaultButton0, "重置当前输入");
        resetToDefaultButton0.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton0.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon0 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon0.setSize("18px");
        resetToDefaultButton0.setIcon(buttonIcon0);
        this.resultNumberField.setPrefixComponent(resetToDefaultButton0);

        this.distinctModeCheckbox = new Checkbox("不返回重复数据");
        this.distinctModeCheckbox.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        this.distinctModeCheckbox.setValue(this.queryParameters.isDistinctMode());
        add(this.distinctModeCheckbox);

        HorizontalLayout spaceDivLayout0 = new HorizontalLayout();
        spaceDivLayout0.setWidthFull();
        spaceDivLayout0.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        add(spaceDivLayout0);

        HorizontalLayout messageTitleLayout = new HorizontalLayout();
        messageTitleLayout.setPadding(false);
        Icon infoIcon = new Icon(VaadinIcon.INFO_CIRCLE);
        infoIcon.setSize("10px");
        infoIcon.getStyle().set("color","var(--lumo-contrast-80pct)");
        messageTitleLayout.add(infoIcon);
        NativeLabel attributeTypeLabel = new NativeLabel("设置大于0的查询起始页和查询结束页将覆盖最大返回结果数中设置的返回参数值");
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        messageTitleLayout.add(attributeTypeLabel);
        add(messageTitleLayout);

        int initStartPage = this.queryParameters.getStartPage();
        this.startPageField = new IntegerField("查询起始页 - Start Page");
        this.startPageField.setWidthFull();
        this.startPageField.setTitle("请输入查询起始页");
        this.startPageField.setValue(initStartPage);
        this.startPageField.setMin(0);
        add(this.startPageField);
        Button resetToDefaultButton2 = new Button();
        resetToDefaultButton2.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                startPageField.setValue(initStartPage);
            }
        });
        Tooltips.getCurrent().setTooltip(resetToDefaultButton2, "重置当前输入");
        resetToDefaultButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton2.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon2 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon2.setSize("18px");
        resetToDefaultButton2.setIcon(buttonIcon2);
        this.startPageField.setPrefixComponent(resetToDefaultButton2);

        int initEndPage = this.queryParameters.getEndPage();
        this.endPageField = new IntegerField("查询结束页 - End Page");
        this.endPageField.setWidthFull();
        this.endPageField.setTitle("请输入查询结束页");
        this.endPageField.setValue(initEndPage);
        this.endPageField.setMin(0);
        add(this.endPageField);
        Button resetToDefaultButton3 = new Button();
        resetToDefaultButton3.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                endPageField.setValue(initEndPage);
            }
        });
        Tooltips.getCurrent().setTooltip(resetToDefaultButton3, "重置当前输入");
        resetToDefaultButton3.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton3.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon3 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon3.setSize("18px");
        resetToDefaultButton3.setIcon(buttonIcon3);
        this.endPageField.setPrefixComponent(resetToDefaultButton3);

        int initPageSize = this.queryParameters.getPageSize() > 1 ? this.queryParameters.getPageSize() : 100;
        this.pageSizeField = new IntegerField("查询单页返回结果数 - Page Size");
        this.pageSizeField.setWidthFull();
        this.pageSizeField.setTitle("请输入单页包含数据数");
        this.pageSizeField.setValue(initPageSize);
        this.pageSizeField.setMin(0);
        add(pageSizeField);
        Button resetToDefaultButton1 = new Button();
        resetToDefaultButton1.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                pageSizeField.setValue(initPageSize);
            }
        });
        Tooltips.getCurrent().setTooltip(resetToDefaultButton1, "重置当前输入");
        resetToDefaultButton1.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton1.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon.setSize("18px");
        resetToDefaultButton1.setIcon(buttonIcon);
        this.pageSizeField.setPrefixComponent(resetToDefaultButton1);

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
                doConfigQueryParameters();
            }
        });
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doConfigQueryParameters(){
        boolean isValidInput = false;
        if(this.resultNumberField.isInvalid() ||
                this.pageSizeField.isInvalid() ||
                this.startPageField.isInvalid() ||
                this.endPageField.isInvalid()){
            CommonUIOperationUtil.showPopupNotification("请输入合法的数值", NotificationVariant.LUMO_ERROR);
        }else{
            if(this.resultNumberField.getValue() != null){
                this.queryParameters.setResultNumber(this.resultNumberField.getValue());
            }else{
                this.queryParameters.setResultNumber(0);
            }
            if(this.pageSizeField.getValue() != null){
                this.queryParameters.setPageSize(this.pageSizeField.getValue());
            }else{
                this.queryParameters.setPageSize(0);
            }
            if(this.startPageField.getValue() != null){
                this.queryParameters.setStartPage(this.startPageField.getValue());
            }else{
                this.queryParameters.setStartPage(0);
            }
            if(this.endPageField.getValue() != null){
                this.queryParameters.setEndPage(this.endPageField.getValue());
            }else{
                this.queryParameters.setEndPage(0);
            }
            this.queryParameters.setDistinctMode(this.distinctModeCheckbox.getValue());
            Integer pageSize = this.pageSizeField.getValue();
            Integer startPage = this.startPageField.getValue();
            Integer endPage = this.endPageField.getValue();
            isValidInput = true;
            if(startPage >0 ){
                if(endPage <= startPage){
                    CommonUIOperationUtil.showPopupNotification("End Page 必须大于 Start Page", NotificationVariant.LUMO_ERROR);
                    isValidInput = false;
                }
            }
        }
        if(isValidInput){
            if(getContainerDialog() != null){
                getContainerDialog().close();
            }
        }
    }
}
