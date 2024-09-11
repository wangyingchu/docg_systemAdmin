package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;

import java.util.ArrayList;
import java.util.List;

public class RelationKindMatchLogicWidget extends VerticalLayout {

    private String relationKindName;
    private RelationDirection relationDirection;

    public RelationKindMatchLogicWidget(){
        setSizeFull();
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(true);

        HorizontalLayout attributeMetaLayout = new HorizontalLayout();
        attributeMetaLayout.setSpacing(false);
        attributeMetaLayout.setMargin(false);
        attributeMetaLayout.setPadding(false);
        attributeMetaLayout.setWidth(285, Unit.PIXELS);

        Scroller queryConditionItemScroller = new Scroller(attributeMetaLayout);
        add(queryConditionItemScroller);

        VerticalLayout attributeMetaInfoContainer = new VerticalLayout();
        attributeMetaInfoContainer.setSpacing(false);
        attributeMetaInfoContainer.setMargin(false);
        attributeMetaInfoContainer.setPadding(false);

        attributeMetaLayout.add(attributeMetaInfoContainer);

        HorizontalLayout attributeNameInfoContainer = new HorizontalLayout();
        attributeNameInfoContainer.setWidth(100,Unit.PERCENTAGE);
        attributeMetaInfoContainer.add(attributeNameInfoContainer);

        NativeLabel attributeNameLabel = new NativeLabel("共享航班已执行执飞");
        attributeNameLabel.getStyle().set("font-size","0.75rem").set("font-weight","bold").set("padding-right","5px");
        attributeNameInfoContainer.add(attributeNameLabel);
        attributeNameInfoContainer.setFlexGrow(1,attributeNameLabel);

        HorizontalLayout conditionStatusContainer = new HorizontalLayout();
        conditionStatusContainer.setPadding(false);
        conditionStatusContainer.setMargin(false);
        conditionStatusContainer.setSpacing(false);
        attributeMetaInfoContainer.add(conditionStatusContainer);

        NativeLabel attributeTypeLabel = new NativeLabel("AlreadyServicedShareFlightExecution");
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-70pct)").set("padding-left","1px");
        conditionStatusContainer.add(attributeTypeLabel);



        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        ComboBox<RelationDirection> provinceValueTextField = new ComboBox();
        provinceValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        List<RelationDirection> relationDirectionList = new ArrayList<>();
        relationDirectionList.add(RelationDirection.FROM);
        relationDirectionList.add(RelationDirection.TO);
        relationDirectionList.add(RelationDirection.TWO_WAY);
        provinceValueTextField.setItems(relationDirectionList);
        provinceValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        provinceValueTextField.setWidth(75, Unit.PIXELS);
        provinceValueTextField.setValue(RelationDirection.TWO_WAY);
        provinceValueTextField.setAllowCustomValue(false);
        provinceValueTextField.getStyle().set("font-size","9px");
        controlButtonsContainer.add(provinceValueTextField);

        Button clearFilteringLogicButton = new Button();
        clearFilteringLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        clearFilteringLogicButton.setIcon(VaadinIcon.ERASER.create());
        clearFilteringLogicButton.setTooltipText("撤销此过滤（显示）条件");

        clearFilteringLogicButton = new Button();
        clearFilteringLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        clearFilteringLogicButton.setIcon(VaadinIcon.ERASER.create());
        clearFilteringLogicButton.setTooltipText("撤销此过滤（显示）条件");
        clearFilteringLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //removeCurrentConditionLogic();
            }
        });
        controlButtonsContainer.add(clearFilteringLogicButton);

        controlButtonsContainer.setVerticalComponentAlignment(Alignment.START,clearFilteringLogicButton);
        attributeMetaLayout.add(controlButtonsContainer);
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.START,controlButtonsContainer);








        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(100,Unit.PERCENTAGE);
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);
    }

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public RelationDirection getRelationDirection() {
        return relationDirection;
    }

    public void setRelationDirection(RelationDirection relationDirection) {
        this.relationDirection = relationDirection;
    }
}
