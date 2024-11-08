package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.IntegerField;

public class ExpandResultSetConfigView extends VerticalLayout {

    private IntegerField expandPathResultMaxPathCountField;
    private IntegerField expandGraphResultMaxConceptionEntityCountField;
    private IntegerField expandSpanningTreeResultMaxConceptionEntityCountField;
    private ExpandParameters expandParameters;
    private Popover containerPopover;

    public ExpandResultSetConfigView(ExpandParameters expandParameters){
        this.expandParameters = expandParameters;

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("拓扑扩展配置参数");
        messageContainerLayout.add(viewTitle);

        this.expandPathResultMaxPathCountField = new IntegerField("拓展路径 - 最大返回路径数");
        this.expandPathResultMaxPathCountField.setWidthFull();
        this.expandPathResultMaxPathCountField.setTitle("请输入拓展路径最大返回路径数");
        this.expandPathResultMaxPathCountField.setMin(0);
        this.expandPathResultMaxPathCountField.setValue(this.expandParameters.getExpandPathResultMaxPathCount());
        add(expandPathResultMaxPathCountField);
        Button resetToDefaultButton0 = new Button();
        resetToDefaultButton0.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                expandPathResultMaxPathCountField.setValue(expandParameters.getExpandPathResultMaxPathCount());
            }
        });
        resetToDefaultButton0.setTooltipText("重置当前输入");
        resetToDefaultButton0.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton0.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon0 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon0.setSize("18px");
        resetToDefaultButton0.setIcon(buttonIcon0);
        this.expandPathResultMaxPathCountField.setPrefixComponent(resetToDefaultButton0);

        this.expandGraphResultMaxConceptionEntityCountField = new IntegerField("拓展子图 - 最大返回概念实体数");
        this.expandGraphResultMaxConceptionEntityCountField.setWidthFull();
        this.expandGraphResultMaxConceptionEntityCountField.setTitle("请输入拓展子图最大返回概念实体数");
        this.expandGraphResultMaxConceptionEntityCountField.setMin(0);
        this.expandGraphResultMaxConceptionEntityCountField.setValue(this.expandParameters.getExpandGraphResultMaxConceptionEntityCount());
        add(expandGraphResultMaxConceptionEntityCountField);
        Button resetToDefaultButton1 = new Button();
        resetToDefaultButton0.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                expandGraphResultMaxConceptionEntityCountField.setValue(expandParameters.getExpandGraphResultMaxConceptionEntityCount());
            }
        });
        resetToDefaultButton1.setTooltipText("重置当前输入");
        resetToDefaultButton1.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton1.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon1 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon1.setSize("18px");
        resetToDefaultButton1.setIcon(buttonIcon1);
        this.expandGraphResultMaxConceptionEntityCountField.setPrefixComponent(resetToDefaultButton1);

        this.expandSpanningTreeResultMaxConceptionEntityCountField = new IntegerField("拓展生成树 - 最大返回概念实体数");
        this.expandSpanningTreeResultMaxConceptionEntityCountField.setWidthFull();
        this.expandSpanningTreeResultMaxConceptionEntityCountField.setTitle("请输入拓展生成树最大返回概念实体数");
        this.expandSpanningTreeResultMaxConceptionEntityCountField.setMin(0);
        this.expandSpanningTreeResultMaxConceptionEntityCountField.setValue(this.expandParameters.getExpandSpanningTreeResultMaxConceptionEntityCount());
        add(expandSpanningTreeResultMaxConceptionEntityCountField);
        Button resetToDefaultButton2 = new Button();
        resetToDefaultButton0.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                expandSpanningTreeResultMaxConceptionEntityCountField.setValue(expandParameters.getExpandSpanningTreeResultMaxConceptionEntityCount());
            }
        });
        resetToDefaultButton2.setTooltipText("重置当前输入");
        resetToDefaultButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetToDefaultButton2.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Icon buttonIcon2 = VaadinIcon.ROTATE_LEFT.create();
        buttonIcon2.setSize("18px");
        resetToDefaultButton2.setIcon(buttonIcon2);
        this.expandSpanningTreeResultMaxConceptionEntityCountField.setPrefixComponent(resetToDefaultButton2);

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
                doConfigExpandParameters();
            }
        });
    }

    private void doConfigExpandParameters() {
        expandParameters.setExpandGraphResultMaxConceptionEntityCount(expandGraphResultMaxConceptionEntityCountField.getValue());
        expandParameters.setExpandPathResultMaxPathCount(expandPathResultMaxPathCountField.getValue());
        expandParameters.setExpandSpanningTreeResultMaxConceptionEntityCount(expandSpanningTreeResultMaxConceptionEntityCountField.getValue());
        if(containerPopover != null){
            containerPopover.close();
        }
    }

    public void setContainerPopover(Popover containerPopover) {
        this.containerPopover = containerPopover;
    }
}
