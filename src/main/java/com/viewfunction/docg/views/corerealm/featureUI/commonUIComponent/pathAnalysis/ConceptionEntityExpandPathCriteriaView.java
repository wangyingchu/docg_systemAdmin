package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;

import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ConceptionKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.AddRelationMatchLogicUI.AddRelationMatchLogicHelper;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.AddConceptionMatchLogicUI.AddConceptionMatchLogicHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConceptionEntityExpandPathCriteriaView extends VerticalLayout {

    private VerticalLayout relationMatchLogicCriteriaItemsContainer;
    private VerticalLayout conceptionMatchLogicCriteriaItemsContainer;

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
        addRelationMatchLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddRelationMatchLogicUI();
            }
        });

        relationMatchLogicCriteriaItemsContainer= new VerticalLayout();
        relationMatchLogicCriteriaItemsContainer.setMargin(false);
        relationMatchLogicCriteriaItemsContainer.setSpacing(false);
        relationMatchLogicCriteriaItemsContainer.setPadding(false);
        relationMatchLogicCriteriaItemsContainer.setWidth(295,Unit.PIXELS);

        Scroller queryConditionItemsScroller = new Scroller(relationMatchLogicCriteriaItemsContainer);
        queryConditionItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        queryConditionItemsScroller.setHeight(250,Unit.PIXELS);
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
        minJumpField.setWidth(40,Unit.PIXELS);
        pathJumpConfigSetupContainer.add(minJumpField);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,minJumpField);

        NativeLabel maxJumpConfigText = new NativeLabel("最大跳数:");
        maxJumpConfigText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        pathJumpConfigSetupContainer.add(maxJumpConfigText);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,maxJumpConfigText);

        IntegerField maxJumpField = new IntegerField();
        maxJumpField.setMin(1);
        maxJumpField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        maxJumpField.setWidth(40,Unit.PIXELS);
        pathJumpConfigSetupContainer.add(maxJumpField);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,maxJumpField);

        HorizontalLayout conceptionMatchLogicConfigSetupContainer = new HorizontalLayout();
        configCriteriaContainerLayout.add(conceptionMatchLogicConfigSetupContainer);

        NativeLabel conceptionMatchLogicConfigText = new NativeLabel("概念类型匹配逻辑:");
        conceptionMatchLogicConfigText.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        conceptionMatchLogicConfigSetupContainer.add(conceptionMatchLogicConfigText);
        conceptionMatchLogicConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionMatchLogicConfigText);

        Button addConceptionMatchLogicButton = new Button("添加概念类型匹配逻辑");
        addConceptionMatchLogicButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addConceptionMatchLogicButton.setIcon(VaadinIcon.PLUS.create());
        conceptionMatchLogicConfigSetupContainer.add(addConceptionMatchLogicButton);
        conceptionMatchLogicConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,addConceptionMatchLogicButton);
        addConceptionMatchLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddConceptionMatchLogicUI();
            }
        });

        conceptionMatchLogicCriteriaItemsContainer = new VerticalLayout();
        conceptionMatchLogicCriteriaItemsContainer.setMargin(false);
        conceptionMatchLogicCriteriaItemsContainer.setSpacing(false);
        conceptionMatchLogicCriteriaItemsContainer.setPadding(false);
        conceptionMatchLogicCriteriaItemsContainer.setWidth(295,Unit.PIXELS);

        Scroller queryConditionItemsScroller2 = new Scroller(conceptionMatchLogicCriteriaItemsContainer);
        queryConditionItemsScroller2.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        queryConditionItemsScroller2.setHeight(245,Unit.PIXELS);
        //scroller.getStyle().set("padding", "var(--lumo-space-m)");
        configCriteriaContainerLayout.add(queryConditionItemsScroller2);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setMargin(false);
        buttonsContainerLayout.setSpacing(false);
        buttonsContainerLayout.setPadding(false);
        add(buttonsContainerLayout);

        Button expandPathButton = new Button("执行路径拓展");
        expandPathButton.setIcon(LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create());
        expandPathButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        expandPathButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //queryConceptionEntities();
            }
        });
        buttonsContainerLayout.add(expandPathButton);
    }

    RelationKindMatchLogicWidget.RelationKindMatchLogicWidgetHelper relationKindMatchLogicWidgetHelper = new RelationKindMatchLogicWidget.RelationKindMatchLogicWidgetHelper(){
        @Override
        public void executeCancelRelationKindMatchLogic(RelationKindMatchLogicWidget relationKindMatchLogicWidget) {
            relationMatchLogicCriteriaItemsContainer.remove(relationKindMatchLogicWidget);
        }
    };

    AddRelationMatchLogicHelper addRelationMatchLogicHelper = new AddRelationMatchLogicHelper(){
        @Override
        public void executeAddRelationMatchLogic(String relationKindName, String relationKindDesc, RelationDirection relationDirection) {
            if(checkRelationKindMatchLogicNotExist(relationKindName,relationDirection)){
                RelationKindMatchLogicWidget newRelationKindMatchLogicWidget = new RelationKindMatchLogicWidget();
                newRelationKindMatchLogicWidget.setRelationKindMatchLogicWidgetHelper(relationKindMatchLogicWidgetHelper);
                newRelationKindMatchLogicWidget.setRelationKindName(relationKindName);
                newRelationKindMatchLogicWidget.setRelationKindDesc(relationKindDesc);
                newRelationKindMatchLogicWidget.setRelationDirection(relationDirection);
                relationMatchLogicCriteriaItemsContainer.add(newRelationKindMatchLogicWidget);
            }else{
                CommonUIOperationUtil.showPopupNotification("关系类型匹配逻辑已经存在", NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            }
        }
    };

    ConceptionKindMatchLogicWidget.ConceptionKindMatchLogicWidgetHelper conceptionKindMatchLogicWidgetHelper = new ConceptionKindMatchLogicWidget.ConceptionKindMatchLogicWidgetHelper(){
        @Override
        public void executeCancelConceptionKindMatchLogic(ConceptionKindMatchLogicWidget conceptionKindMatchLogicWidget) {
            conceptionMatchLogicCriteriaItemsContainer.remove(conceptionKindMatchLogicWidget);
        }
    };

    AddConceptionMatchLogicHelper addConceptionMatchLogicHelper = new AddConceptionMatchLogicUI.AddConceptionMatchLogicHelper(){
        @Override
        public void executeAddConceptionMatchLogic(String conceptionKindName, String conceptionKindDesc, ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule) {
            if(checkConceptionKindMatchLogicNotExist(conceptionKindName,conceptionKindExistenceRule)){
                ConceptionKindMatchLogicWidget conceptionKindMatchLogicWidget = new ConceptionKindMatchLogicWidget();
                conceptionKindMatchLogicWidget.setRelationKindMatchLogicWidgetHelper(conceptionKindMatchLogicWidgetHelper);
                conceptionKindMatchLogicWidget.setConceptionKindName(conceptionKindName);
                conceptionKindMatchLogicWidget.setConceptionKindDesc(conceptionKindDesc);
                conceptionKindMatchLogicWidget.setConceptionKindExistenceRule(conceptionKindExistenceRule);
                conceptionMatchLogicCriteriaItemsContainer.add(conceptionKindMatchLogicWidget);
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型匹配逻辑已经存在", NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            }
        }
    };

    private void renderAddRelationMatchLogicUI(){
        AddRelationMatchLogicUI addRelationMatchLogicUI = new AddRelationMatchLogicUI();
        addRelationMatchLogicUI.setAddRelationMatchLogicHelper(addRelationMatchLogicHelper);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加关系类型匹配逻辑",null,true,470,280,false);
        fixSizeWindow.setWindowContent(addRelationMatchLogicUI);
        fixSizeWindow.setModel(true);
        addRelationMatchLogicUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderAddConceptionMatchLogicUI(){
        AddConceptionMatchLogicUI addConceptionMatchLogicUI = new AddConceptionMatchLogicUI();
        addConceptionMatchLogicUI.setAddConceptionMatchLogicHelper(addConceptionMatchLogicHelper);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加概念类型匹配逻辑",null,true,470,280,false);
        fixSizeWindow.setWindowContent(addConceptionMatchLogicUI);
        fixSizeWindow.setModel(true);
        addConceptionMatchLogicUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private boolean checkConceptionKindMatchLogicNotExist(String conceptionKindName, ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule){
        Stream<Component> existConceptionMatchLogicWidgets = conceptionMatchLogicCriteriaItemsContainer.getChildren();
        List<Boolean> haveExistConceptionMatchLogicList = new ArrayList<>();
        existConceptionMatchLogicWidgets.forEach(
                existConceptionMatchLogicWidget -> {
                    ConceptionKindMatchLogicWidget currentConceptionKindMatchLogicWidget = (ConceptionKindMatchLogicWidget) existConceptionMatchLogicWidget;
                    if(conceptionKindName.equals(currentConceptionKindMatchLogicWidget.getConceptionKindName()) &
                            conceptionKindExistenceRule.equals(currentConceptionKindMatchLogicWidget.getConceptionKindExistenceRule())
                    ){
                        haveExistConceptionMatchLogicList.add(true);
                    }
                }
        );
        if(haveExistConceptionMatchLogicList.size() == 0){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkRelationKindMatchLogicNotExist(String relationKindName, RelationDirection relationDirection){
        Stream<Component> existRelationMatchLogicWidgets = relationMatchLogicCriteriaItemsContainer.getChildren();
        List<Boolean> haveExistRelationMatchLogicList = new ArrayList<>();
        existRelationMatchLogicWidgets.forEach(
                existRelationMatchLogicWidget -> {
                    RelationKindMatchLogicWidget currentRelationKindMatchLogicWidget = (RelationKindMatchLogicWidget) existRelationMatchLogicWidget;
                    if(relationKindName.equals(currentRelationKindMatchLogicWidget.getRelationKindName()) &
                            relationDirection.equals(currentRelationKindMatchLogicWidget.getRelationDirection())
                    ){
                        haveExistRelationMatchLogicList.add(true);
                    }
                }
        );
        if(haveExistRelationMatchLogicList.size() == 0){
            return true;
        }else{
            return false;
        }
    }
}
