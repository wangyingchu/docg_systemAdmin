package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.*;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.RelationKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityExpandPathEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.AddRelationMatchLogicUI.AddRelationMatchLogicHelper;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.AddConceptionMatchLogicUI.AddConceptionMatchLogicHelper;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.ConceptionEntityPathTravelableView.PathExpandType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConceptionEntityExpandPathCriteriaView extends VerticalLayout {

    private VerticalLayout relationMatchLogicCriteriaItemsContainer;
    private VerticalLayout conceptionMatchLogicCriteriaItemsContainer;
    private RadioButtonGroup<RelationDirection> defaultRelationDirectionRadioGroup;
    private Checkbox includeSelfCheckBox;
    private IntegerField minJumpField;
    private IntegerField maxJumpField;

    private String conceptionEntityUID;
    private String conceptionKind;
    private PathExpandType pathExpandType;

    public ConceptionEntityExpandPathCriteriaView(String conceptionKind,String conceptionEntityUID,PathExpandType pathExpandType) {
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.pathExpandType = pathExpandType;

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
        defaultRelationDirectionRadioGroup = new RadioButtonGroup<>();
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

        relationMatchLogicCriteriaItemsContainer = new VerticalLayout();
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
        includeSelfCheckBox = new Checkbox();
        pathJumpConfigSetupContainer.add(includeSelfCheckBox);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,includeSelfCheckBox);
        includeSelfCheckBox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> checkboxBooleanComponentValueChangeEvent) {
                boolean currentValue = checkboxBooleanComponentValueChangeEvent.getValue();
                if(currentValue){
                    minJumpField.setEnabled(false);
                }else{
                    minJumpField.setEnabled(true);
                }
            }
        });

        NativeLabel minJumpConfigText = new NativeLabel("最小跳数:");
        minJumpConfigText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        pathJumpConfigSetupContainer.add(minJumpConfigText);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,minJumpConfigText);

        minJumpField = new IntegerField();
        minJumpField.setMin(0);
        minJumpField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        minJumpField.setWidth(40,Unit.PIXELS);
        pathJumpConfigSetupContainer.add(minJumpField);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,minJumpField);

        NativeLabel maxJumpConfigText = new NativeLabel("最大跳数:");
        maxJumpConfigText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        pathJumpConfigSetupContainer.add(maxJumpConfigText);
        pathJumpConfigSetupContainer.setVerticalComponentAlignment(Alignment.CENTER,maxJumpConfigText);

        maxJumpField = new IntegerField();
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

        Button expandPathButton1 = new Button("执行路径拓展");
        expandPathButton1.setIcon(LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create());
        expandPathButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        expandPathButton1.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeConceptionEntityExpandPath();
            }
        });
        buttonsContainerLayout.add(expandPathButton1);

        Button expandPathButton2 = new Button("执行路径拓展");
        expandPathButton2.setIcon(LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create());
        expandPathButton2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        expandPathButton2.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeConceptionEntityExpandPath();
            }
        });
        buttonsContainerLayout.add(expandPathButton2);

        Button expandPathButton3 = new Button("执行路径拓展");
        expandPathButton3.setIcon(LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create());
        expandPathButton3.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        expandPathButton3.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeConceptionEntityExpandPath();
            }
        });
        buttonsContainerLayout.add(expandPathButton3);

    }

    RelationKindMatchLogicWidget.RelationKindMatchLogicWidgetHelper relationKindMatchLogicWidgetHelper = new RelationKindMatchLogicWidget.RelationKindMatchLogicWidgetHelper(){
        @Override
        public void executeCancelRelationKindMatchLogic(RelationKindMatchLogicWidget relationKindMatchLogicWidget) {
            relationMatchLogicCriteriaItemsContainer.remove(relationKindMatchLogicWidget);
            setDefaultRelationDirectionRadioGroupStatus();
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
            setDefaultRelationDirectionRadioGroupStatus();
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

    private void setDefaultRelationDirectionRadioGroupStatus(){
        if(relationMatchLogicCriteriaItemsContainer.getComponentCount() > 0){
            defaultRelationDirectionRadioGroup.setEnabled(false);
        }else{
            defaultRelationDirectionRadioGroup.setEnabled(true);
        }
    }

    private void executeConceptionEntityExpandPath(){
        boolean includeSelf = includeSelfCheckBox.getValue();
        if(maxJumpField.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请输入最大跳数", NotificationVariant.LUMO_ERROR);
            return;
        }
        if(!includeSelf){
            if(minJumpField.getValue() == null){
                CommonUIOperationUtil.showPopupNotification("请输入最小跳数", NotificationVariant.LUMO_ERROR);
                return;
            }
            int minJump = minJumpField.getValue();
            int maxJump = maxJumpField.getValue();
            if(minJump>maxJump){
                CommonUIOperationUtil.showPopupNotification("最大跳数不能小于最小跳数", NotificationVariant.LUMO_ERROR);
                return;
            }
        }

        List<RelationKindMatchLogic> relationKindMatchLogicList = new ArrayList<>();
        Stream<Component> existRelationMatchLogicWidgets = relationMatchLogicCriteriaItemsContainer.getChildren();
        existRelationMatchLogicWidgets.forEach(
                existRelationMatchLogicWidget -> {
                    RelationKindMatchLogicWidget currentRelationKindMatchLogicWidget = (RelationKindMatchLogicWidget) existRelationMatchLogicWidget;
                    RelationKindMatchLogic currentRelationKindMatchLogic =
                            new RelationKindMatchLogic(currentRelationKindMatchLogicWidget.getRelationKindName(),currentRelationKindMatchLogicWidget.getRelationDirection());
                    relationKindMatchLogicList.add(currentRelationKindMatchLogic);
                }
        );

        RelationDirection defaultRelationDirection = this.defaultRelationDirectionRadioGroup.getValue();

        List<ConceptionKindMatchLogic> conceptionKindMatchLogicList = new ArrayList<>();
        Stream<Component> existConceptionMatchLogicWidgets = conceptionMatchLogicCriteriaItemsContainer.getChildren();
        existConceptionMatchLogicWidgets.forEach(
                existConceptionMatchLogicWidget -> {
                    ConceptionKindMatchLogicWidget currentConceptionKindMatchLogicWidget = (ConceptionKindMatchLogicWidget) existConceptionMatchLogicWidget;
                    ConceptionKindMatchLogic currentConceptionKindMatchLogic =
                            new ConceptionKindMatchLogic(currentConceptionKindMatchLogicWidget.getConceptionKindName(),currentConceptionKindMatchLogicWidget.getConceptionKindExistenceRule());
                    conceptionKindMatchLogicList.add(currentConceptionKindMatchLogic);

                }
        );

        boolean containsSelf = this.includeSelfCheckBox.getValue();
        Integer maxJump = this.maxJumpField.getValue();
        Integer minJump = this.minJumpField.getValue();

        ConceptionEntityExpandPathEvent conceptionEntityExpandPathEvent =new ConceptionEntityExpandPathEvent();
        conceptionEntityExpandPathEvent.setConceptionEntityUID(this.conceptionEntityUID);
        conceptionEntityExpandPathEvent.setConceptionKind(this.conceptionKind);

        if(this.pathExpandType != null){
            conceptionEntityExpandPathEvent.setPathExpandType(this.pathExpandType);
        }else{
            if(minJump != null){
                conceptionEntityExpandPathEvent.setPathExpandType(ConceptionEntityPathTravelableView.PathExpandType.ExpandPath);
            }else{
                conceptionEntityExpandPathEvent.setPathExpandType(PathExpandType.ExpandGraph);
            }
        }
        conceptionEntityExpandPathEvent.setRelationKindMatchLogics(relationKindMatchLogicList);
        conceptionEntityExpandPathEvent.setDefaultDirectionForNoneRelationKindMatch(defaultRelationDirection);
        conceptionEntityExpandPathEvent.setConceptionKindMatchLogics(conceptionKindMatchLogicList);
        conceptionEntityExpandPathEvent.setContainsSelf(containsSelf);
        conceptionEntityExpandPathEvent.setMaxJump(maxJump);
        conceptionEntityExpandPathEvent.setMinJump(minJump);
        ResourceHolder.getApplicationBlackboard().fire(conceptionEntityExpandPathEvent);
    }
}
