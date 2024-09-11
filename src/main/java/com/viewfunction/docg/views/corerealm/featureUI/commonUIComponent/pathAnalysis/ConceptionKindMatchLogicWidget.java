package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;

import java.util.ArrayList;
import java.util.List;

public class ConceptionKindMatchLogicWidget extends VerticalLayout {

    private String conceptionKindName;
    private ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule;

    private String relationKindName;
    private String relationKindDesc;
    private RelationDirection relationDirection;
    private NativeLabel relationKindDescLabel;
    private NativeLabel relationKindNameLabel;
    private ComboBox<ConceptionKindMatchLogic.ConceptionKindExistenceRule> relationDirectionComboBox;

    public ConceptionKindMatchLogicWidget(){
        setSizeFull();
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(true);

        HorizontalLayout relationKindMetaLayout = new HorizontalLayout();
        relationKindMetaLayout.setSpacing(false);
        relationKindMetaLayout.setMargin(false);
        relationKindMetaLayout.setPadding(false);
        relationKindMetaLayout.setWidth(285, Unit.PIXELS);

        Scroller queryConditionItemScroller = new Scroller(relationKindMetaLayout);
        add(queryConditionItemScroller);

        VerticalLayout relationKindMetaInfoContainer = new VerticalLayout();
        relationKindMetaInfoContainer.setSpacing(false);
        relationKindMetaInfoContainer.setMargin(false);
        relationKindMetaInfoContainer.setPadding(false);

        relationKindMetaLayout.add(relationKindMetaInfoContainer);

        HorizontalLayout relationKindDescInfoContainer = new HorizontalLayout();
        relationKindDescInfoContainer.setWidth(100,Unit.PERCENTAGE);
        relationKindMetaInfoContainer.add(relationKindDescInfoContainer);

        relationKindDescLabel = new NativeLabel("共享航班已执行执飞");
        relationKindDescLabel.getStyle().set("font-size","0.75rem").set("font-weight","bold").set("padding-right","5px");
        relationKindDescInfoContainer.add(relationKindDescLabel);
        relationKindDescInfoContainer.setFlexGrow(1,relationKindDescLabel);

        HorizontalLayout relationKindNameContainer = new HorizontalLayout();
        relationKindNameContainer.setPadding(false);
        relationKindNameContainer.setMargin(false);
        relationKindNameContainer.setSpacing(false);
        relationKindMetaInfoContainer.add(relationKindNameContainer);

        relationKindNameLabel = new NativeLabel("AlreadyServicedShareFlightExecution");
        relationKindNameLabel.addClassNames("text-tertiary");
        relationKindNameLabel.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-70pct)").set("padding-left","1px");
        relationKindNameContainer.add(relationKindNameLabel);

        relationKindMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,relationKindMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        relationDirectionComboBox = new ComboBox();
        relationDirectionComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        List<ConceptionKindMatchLogic.ConceptionKindExistenceRule> relationDirectionList = new ArrayList<>();
        relationDirectionList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.MUST_HAVE);
        relationDirectionList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.END_WITH);
        relationDirectionList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.NOT_ALLOW);
        relationDirectionList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.TERMINATE_AT);
        relationDirectionComboBox.setItems(relationDirectionList);
        relationDirectionComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationDirectionComboBox.setWidth(75, Unit.PIXELS);
        relationDirectionComboBox.setValue(ConceptionKindMatchLogic.ConceptionKindExistenceRule.MUST_HAVE);
        relationDirectionComboBox.setAllowCustomValue(false);
        relationDirectionComboBox.getStyle().set("font-size","8px");
        controlButtonsContainer.add(relationDirectionComboBox);

        Button clearMatchingLogicButton = new Button();
        clearMatchingLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        clearMatchingLogicButton.setIcon(VaadinIcon.ERASER.create());
        clearMatchingLogicButton.setTooltipText("撤销此匹配规则");
        clearMatchingLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //removeCurrentConditionLogic();
            }
        });
        controlButtonsContainer.add(clearMatchingLogicButton);

        controlButtonsContainer.setVerticalComponentAlignment(Alignment.START,clearMatchingLogicButton);
        relationKindMetaLayout.add(controlButtonsContainer);
        relationKindMetaLayout.setVerticalComponentAlignment(Alignment.START,controlButtonsContainer);

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
    }

    public ConceptionKindMatchLogic.ConceptionKindExistenceRule getConceptionKindExistenceRule() {
        return conceptionKindExistenceRule;
    }

    public void setConceptionKindExistenceRule(ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule) {
        this.conceptionKindExistenceRule = conceptionKindExistenceRule;
    }

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
        this.relationKindNameLabel.setText(relationKindName);
    }

    public RelationDirection getRelationDirection() {
        return relationDirection;
    }

    public void setRelationDirection(ConceptionKindMatchLogic.ConceptionKindExistenceRule relationDirectionComboBox) {
        //this.relationDirection = relationDirection;
        this.relationDirectionComboBox.setValue(relationDirectionComboBox);
    }

    public String getRelationKindDesc() {
        return relationKindDesc;
    }

    public void setRelationKindDesc(String relationKindDesc) {
        this.relationKindDesc = relationKindDesc;
        this.relationKindDescLabel.setText(relationKindDesc);
    }
}
