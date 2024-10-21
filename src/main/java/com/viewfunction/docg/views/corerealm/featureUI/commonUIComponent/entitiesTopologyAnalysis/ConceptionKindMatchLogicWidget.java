package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ConceptionKindMatchLogic;

import java.util.ArrayList;
import java.util.List;

public class ConceptionKindMatchLogicWidget extends VerticalLayout {

    public interface ConceptionKindMatchLogicWidgetHelper {
        public void executeCancelConceptionKindMatchLogic(ConceptionKindMatchLogicWidget conceptionKindMatchLogicWidget);
    }

    private String conceptionKindName;
    private String conceptionKindDesc;
    private ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule;
    private NativeLabel conceptionKindDescLabel;
    private NativeLabel conceptionKindNameLabel;
    private ComboBox<ConceptionKindMatchLogic.ConceptionKindExistenceRule> conceptionKindExistenceRuleComboBox;
    private ConceptionKindMatchLogicWidgetHelper conceptionKindMatchLogicWidgetHelper;

    public ConceptionKindMatchLogicWidget(){
        setSizeFull();
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(true);

        HorizontalLayout conceptionKindMetaLayout = new HorizontalLayout();
        conceptionKindMetaLayout.setSpacing(false);
        conceptionKindMetaLayout.setMargin(false);
        conceptionKindMetaLayout.setPadding(false);
        conceptionKindMetaLayout.setWidth(285, Unit.PIXELS);

        Scroller queryConditionItemScroller = new Scroller(conceptionKindMetaLayout);
        add(queryConditionItemScroller);

        VerticalLayout conceptionKindMetaInfoContainer = new VerticalLayout();
        conceptionKindMetaInfoContainer.setSpacing(false);
        conceptionKindMetaInfoContainer.setMargin(false);
        conceptionKindMetaInfoContainer.setPadding(false);

        conceptionKindMetaLayout.add(conceptionKindMetaInfoContainer);

        HorizontalLayout conceptionKindDescInfoContainer = new HorizontalLayout();
        conceptionKindDescInfoContainer.setWidth(100,Unit.PERCENTAGE);
        conceptionKindMetaInfoContainer.add(conceptionKindDescInfoContainer);

        conceptionKindDescLabel = new NativeLabel("-");
        conceptionKindDescLabel.getStyle().set("font-size","0.75rem").set("font-weight","bold").set("padding-right","5px");
        conceptionKindDescInfoContainer.add(conceptionKindDescLabel);
        conceptionKindDescInfoContainer.setFlexGrow(1, conceptionKindDescLabel);

        HorizontalLayout conceptionKindNameContainer = new HorizontalLayout();
        conceptionKindNameContainer.setPadding(false);
        conceptionKindNameContainer.setMargin(false);
        conceptionKindNameContainer.setSpacing(false);
        conceptionKindMetaInfoContainer.add(conceptionKindNameContainer);

        conceptionKindNameLabel = new NativeLabel("-");
        conceptionKindNameLabel.addClassNames("text-tertiary");
        conceptionKindNameLabel.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-70pct)").set("padding-left","1px");
        conceptionKindNameContainer.add(conceptionKindNameLabel);

        conceptionKindMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        conceptionKindExistenceRuleComboBox = new ComboBox();
        conceptionKindExistenceRuleComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        List<ConceptionKindMatchLogic.ConceptionKindExistenceRule> conceptionKindExistenceRuleList = new ArrayList<>();
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.MUST_HAVE);
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.END_WITH);
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.NOT_ALLOW);
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.TERMINATE_AT);
        conceptionKindExistenceRuleComboBox.setItems(conceptionKindExistenceRuleList);
        conceptionKindExistenceRuleComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        conceptionKindExistenceRuleComboBox.setWidth(75, Unit.PIXELS);
        conceptionKindExistenceRuleComboBox.setValue(ConceptionKindMatchLogic.ConceptionKindExistenceRule.MUST_HAVE);
        conceptionKindExistenceRuleComboBox.setAllowCustomValue(false);
        conceptionKindExistenceRuleComboBox.getStyle().set("font-size","8px");
        controlButtonsContainer.add(conceptionKindExistenceRuleComboBox);

        Button clearMatchingLogicButton = new Button();
        clearMatchingLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        clearMatchingLogicButton.setIcon(VaadinIcon.ERASER.create());
        clearMatchingLogicButton.setTooltipText("撤销此匹配规则");
        clearMatchingLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                clearMatchingLogic();
            }
        });
        controlButtonsContainer.add(clearMatchingLogicButton);

        controlButtonsContainer.setVerticalComponentAlignment(Alignment.START,clearMatchingLogicButton);
        conceptionKindMetaLayout.add(controlButtonsContainer);
        conceptionKindMetaLayout.setVerticalComponentAlignment(Alignment.START,controlButtonsContainer);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidth(100,Unit.PERCENTAGE);
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);
    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
        this.conceptionKindNameLabel.setText(conceptionKindName);
    }

    public ConceptionKindMatchLogic.ConceptionKindExistenceRule getConceptionKindExistenceRule() {
        return conceptionKindExistenceRule;
    }

    public void setConceptionKindExistenceRule(ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule) {
        this.conceptionKindExistenceRule = conceptionKindExistenceRule;
        this.conceptionKindExistenceRuleComboBox.setValue(conceptionKindExistenceRule);
    }

    public String getConceptionKindDesc() {
        return conceptionKindDesc;
    }

    public void setConceptionKindDesc(String conceptionKindDesc) {
        this.conceptionKindDesc = conceptionKindDesc;
        this.conceptionKindDescLabel.setText(conceptionKindDesc);
    }

    private void clearMatchingLogic() {
        if(conceptionKindMatchLogicWidgetHelper != null){
            conceptionKindMatchLogicWidgetHelper.executeCancelConceptionKindMatchLogic(this);
        }
    }

    public void setRelationKindMatchLogicWidgetHelper(ConceptionKindMatchLogicWidgetHelper conceptionKindMatchLogicWidgetHelper) {
        this.conceptionKindMatchLogicWidgetHelper = conceptionKindMatchLogicWidgetHelper;
    }
}
