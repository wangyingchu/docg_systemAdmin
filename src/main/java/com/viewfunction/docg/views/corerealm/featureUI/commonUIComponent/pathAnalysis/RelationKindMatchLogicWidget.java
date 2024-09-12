package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;

import java.util.ArrayList;
import java.util.List;

public class RelationKindMatchLogicWidget extends VerticalLayout {

    private String relationKindName;
    private String relationKindDesc;
    private RelationDirection relationDirection;
    private NativeLabel relationKindDescLabel;
    private NativeLabel relationKindNameLabel;
    private ComboBox<RelationDirection> relationDirectionComboBox;

    public RelationKindMatchLogicWidget(){
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
        List<RelationDirection> relationDirectionList = new ArrayList<>();
        relationDirectionList.add(RelationDirection.FROM);
        relationDirectionList.add(RelationDirection.TO);
        relationDirectionList.add(RelationDirection.TWO_WAY);
        relationDirectionComboBox.setItems(relationDirectionList);
        relationDirectionComboBox.setWidth(75, Unit.PIXELS);
        relationDirectionComboBox.setValue(RelationDirection.TWO_WAY);
        relationDirectionComboBox.setAllowCustomValue(false);
        relationDirectionComboBox.getStyle().set("font-size","9px");
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

    public void setRelationDirection(RelationDirection relationDirection) {
        this.relationDirection = relationDirection;
        this.relationDirectionComboBox.setValue(relationDirection);
    }

    public String getRelationKindDesc() {
        return relationKindDesc;
    }

    public void setRelationKindDesc(String relationKindDesc) {
        this.relationKindDesc = relationKindDesc;
        this.relationKindDescLabel.setText(relationKindDesc);
    }
}
