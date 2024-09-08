package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;

import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

public class ConceptionEntityExpandPathCriteriaView extends VerticalLayout {

    public ConceptionEntityExpandPathCriteriaView() {
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(LineAwesomeIconsSvg.VECTOR_SQUARE_SOLID.create(),"路径扩展条件");
        add(filterTitle1);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        VerticalLayout configCriteriaContainerLayout = new VerticalLayout();
        configCriteriaContainerLayout.setPadding(false);
        configCriteriaContainerLayout.setSpacing(false);
        configCriteriaContainerLayout.setMargin(false);
        add(configCriteriaContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(LineAwesomeIconsSvg.SLASH_SOLID.create(),"设定关系实体匹配规则");
        configCriteriaContainerLayout.add(infoTitle1);

        HorizontalLayout relationDirectionRadioGroupContainer = new HorizontalLayout();
        relationDirectionRadioGroupContainer.setWidth(280,Unit.PIXELS);
        relationDirectionRadioGroupContainer.getStyle().set("padding-top", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(relationDirectionRadioGroupContainer);

        NativeLabel defaultRelationDirectionFilterText = new NativeLabel("默认全局关系方向:");
        defaultRelationDirectionFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        relationDirectionRadioGroupContainer.add(defaultRelationDirectionFilterText);
        relationDirectionRadioGroupContainer.setVerticalComponentAlignment(Alignment.CENTER,defaultRelationDirectionFilterText);
        RadioButtonGroup<RelationDirection> defaultRelationDirectionRadioGroup = new RadioButtonGroup<>();
        defaultRelationDirectionRadioGroup.addClassName("geospatial-region-detail-ui-radio-group-1");
        defaultRelationDirectionRadioGroup.setItems(RelationDirection.TWO_WAY, RelationDirection.FROM, RelationDirection.TO);
        defaultRelationDirectionRadioGroup.setValue(RelationDirection.TWO_WAY);
        relationDirectionRadioGroupContainer.add(defaultRelationDirectionRadioGroup);
        relationDirectionRadioGroupContainer.setVerticalComponentAlignment(Alignment.CENTER,defaultRelationDirectionRadioGroup);

        HorizontalLayout relationMatchLogicConfigSetupContainer = new HorizontalLayout();
        relationMatchLogicConfigSetupContainer.getStyle().set("padding-top", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(relationMatchLogicConfigSetupContainer);

        NativeLabel relationMatchLogicConfigText = new NativeLabel("关系类型匹配逻辑:");
        relationMatchLogicConfigText.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        relationMatchLogicConfigSetupContainer.add(relationMatchLogicConfigText);
        relationMatchLogicConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,relationMatchLogicConfigText);

        Button addRelationMatchLogicButton = new Button("添加关系类型匹配逻辑");
        addRelationMatchLogicButton.setTooltipText("添加关系类型匹配逻辑");
        addRelationMatchLogicButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addRelationMatchLogicButton.setIcon(VaadinIcon.PLUS.create());
        relationMatchLogicConfigSetupContainer.add(addRelationMatchLogicButton);
        relationMatchLogicConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,addRelationMatchLogicButton);

        VerticalLayout criteriaItemsContainer = new VerticalLayout();
        criteriaItemsContainer.setMargin(false);
        criteriaItemsContainer.setSpacing(false);
        criteriaItemsContainer.setPadding(false);
        criteriaItemsContainer.setWidth(100,Unit.PERCENTAGE);
        criteriaItemsContainer.setHeight(150,Unit.PIXELS);

        for(int i=0;i<20;i++){
            criteriaItemsContainer.add(new RelationKindMatchLogicWidget());
        }

        Scroller queryConditionItemsScroller = new Scroller(criteriaItemsContainer);
        queryConditionItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        //scroller.getStyle().set("padding", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(queryConditionItemsScroller);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.getStyle().set("padding-top", "var(--lumo-space-l)");
        configCriteriaContainerLayout.add(spaceDivLayout1);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(LineAwesomeIconsSvg.CIRCLE_SOLID.create(),"设定概念实体匹配规则");
        configCriteriaContainerLayout.add(infoTitle2);

        HorizontalLayout pathJumpConfigSetupContainer = new HorizontalLayout();
        pathJumpConfigSetupContainer.getStyle().set("padding-top", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(pathJumpConfigSetupContainer);

        NativeLabel includeSelfConfigText = new NativeLabel("路径包含自身:");
        includeSelfConfigText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        pathJumpConfigSetupContainer.add(includeSelfConfigText);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,includeSelfConfigText);
        Checkbox includeSelfCheckBox = new Checkbox();
        pathJumpConfigSetupContainer.add(includeSelfCheckBox);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,includeSelfCheckBox);

        NativeLabel minJumpConfigText = new NativeLabel("最小跳数:");
        minJumpConfigText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        pathJumpConfigSetupContainer.add(minJumpConfigText);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,minJumpConfigText);

        IntegerField minJumpField = new IntegerField();
        minJumpField.setMin(0);
        minJumpField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        minJumpField.setWidth(35,Unit.PIXELS);
        pathJumpConfigSetupContainer.add(minJumpField);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,minJumpField);

        NativeLabel maxJumpConfigText = new NativeLabel("最大跳数:");
        maxJumpConfigText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        pathJumpConfigSetupContainer.add(maxJumpConfigText);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,maxJumpConfigText);

        IntegerField maxJumpField = new IntegerField();
        maxJumpField.setMin(1);
        maxJumpField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        maxJumpField.setWidth(35,Unit.PIXELS);
        pathJumpConfigSetupContainer.add(maxJumpField);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,maxJumpField);

        HorizontalLayout conceptionMatchLogicConfigSetupContainer = new HorizontalLayout();
        conceptionMatchLogicConfigSetupContainer.getStyle().set("padding-top", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(conceptionMatchLogicConfigSetupContainer);

        NativeLabel conceptionMatchLogicConfigText = new NativeLabel("概念类型匹配逻辑:");
        conceptionMatchLogicConfigText.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        conceptionMatchLogicConfigSetupContainer.add(conceptionMatchLogicConfigText);
        conceptionMatchLogicConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionMatchLogicConfigText);

        Button addConceptionMatchLogicButton = new Button();
        addConceptionMatchLogicButton.setTooltipText("添加概念类型匹配逻辑");
        addConceptionMatchLogicButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addConceptionMatchLogicButton.setIcon(VaadinIcon.PLUS.create());
        conceptionMatchLogicConfigSetupContainer.add(addConceptionMatchLogicButton);
        conceptionMatchLogicConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,addConceptionMatchLogicButton);

        VerticalLayout criteriaItemsContainer2 = new VerticalLayout();
        criteriaItemsContainer2.setMargin(false);
        criteriaItemsContainer2.setSpacing(false);
        criteriaItemsContainer2.setPadding(false);
        criteriaItemsContainer2.setWidth(100,Unit.PERCENTAGE);
        criteriaItemsContainer2.setHeight(150,Unit.PIXELS);

        Scroller queryConditionItemsScroller2 = new Scroller(criteriaItemsContainer2);
        queryConditionItemsScroller2.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        //scroller.getStyle().set("padding", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(queryConditionItemsScroller2);

        for(int i=0;i<20;i++){
            criteriaItemsContainer2.add(new RelationKindMatchLogicWidget());
        }

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setMargin(false);
        buttonsContainerLayout.setSpacing(false);
        buttonsContainerLayout.setPadding(false);
        add(buttonsContainerLayout);

        Button executeQueryButton = new Button("查询概念实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //queryConceptionEntities();
            }
        });
        buttonsContainerLayout.add(executeQueryButton);
    }
}
