package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

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
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ConceptionKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.RelationKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityExpandTopologyEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis.AddRelationMatchLogicUI.AddRelationMatchLogicHelper;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis.AddConceptionMatchLogicUI.AddConceptionMatchLogicHelper;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis.ConceptionEntityTopologyTravelableView.TopologyExpandType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConceptionEntityExpandTopologyCriteriaView extends VerticalLayout {

    private VerticalLayout relationMatchLogicCriteriaItemsContainer;
    private VerticalLayout conceptionMatchLogicCriteriaItemsContainer;
    private RadioButtonGroup<RelationDirection> defaultRelationDirectionRadioGroup;
    private Checkbox includeSelfCheckBox;
    private IntegerField minJumpField;
    private IntegerField maxJumpField;

    private String conceptionEntityUID;
    private String conceptionKind;
    private TopologyExpandType topologyExpandType;

    private Button addRelationMatchLogicButton;
    private Popover addRelationMatchLogicButtonPopover;
    private Button addConceptionMatchLogicButton;
    private Popover addConceptionMatchLogicButtonPopover;
    private Button resultSetConfigButton;
    private Popover resultSetConfigButtonPopover;
    private ExpandParameters expandParameters;

    public ConceptionEntityExpandTopologyCriteriaView(String conceptionKind, String conceptionEntityUID, TopologyExpandType topologyExpandType) {
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.topologyExpandType = topologyExpandType;
        this.expandParameters = new ExpandParameters();

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

        addRelationMatchLogicButton = new Button("添加关系类型匹配逻辑");
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

        addConceptionMatchLogicButton = new Button("添加概念类型匹配逻辑");
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
        configCriteriaContainerLayout.add(queryConditionItemsScroller2);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setMargin(false);
        buttonsContainerLayout.setSpacing(true);
        buttonsContainerLayout.setPadding(false);
        add(buttonsContainerLayout);

        Select<String> commandSelect = new Select<>();
        commandSelect.addThemeVariants(SelectVariant.LUMO_SMALL);
        commandSelect.setWidth(95,Unit.PIXELS);
        commandSelect.setItems("拓展路径", "拓展子图", "拓展生成树");
        commandSelect.setValue("拓展路径");
        buttonsContainerLayout.add(commandSelect);

        Button executExpandButton = new Button("执行拓展");
        executExpandButton.setIcon(VaadinIcon.PLAY.create());
        executExpandButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SMALL);
        buttonsContainerLayout.add(executExpandButton);
        executExpandButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                String expandCommand = commandSelect.getValue();
                if("拓展路径".equals(expandCommand)){
                    executeConceptionEntityExpand(TopologyExpandType.ExpandPath);
                }
                if("拓展子图".equals(expandCommand)){
                    executeConceptionEntityExpand(TopologyExpandType.ExpandGraph);
                }
                if("拓展生成树".equals(expandCommand)){
                    executeConceptionEntityExpand(TopologyExpandType.ExpandSpanningTree);
                }
            }
        });

        resultSetConfigButton = new Button("设置运行参数",new Icon(VaadinIcon.COG_O));
        resultSetConfigButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        resultSetConfigButton.getStyle().set("font-size","10px");
        buttonsContainerLayout.add(resultSetConfigButton);
        resultSetConfigButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderResultSetConfig();
            }
        });

        Button expandPathButton = new Button("拓展路径");
        expandPathButton.setIcon(LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create());
        expandPathButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeConceptionEntityExpand(TopologyExpandType.ExpandPath);
            }
        });
        //buttonsContainerLayout.add(expandPathButton);

        Button expandSubGraphButton = new Button("拓展子图");
        expandSubGraphButton.setIcon(LineAwesomeIconsSvg.SHARE_ALT_SOLID.create());
        expandSubGraphButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeConceptionEntityExpand(TopologyExpandType.ExpandGraph);
            }
        });
        //buttonsContainerLayout.add(expandSubGraphButton);

        Button expandSpanTreeButton = new Button("拓展生成树");
        expandSpanTreeButton.setIcon(LineAwesomeIconsSvg.SITEMAP_SOLID.create());
        expandSpanTreeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeConceptionEntityExpand(TopologyExpandType.ExpandSpanningTree);
            }
        });
        //buttonsContainerLayout.add(expandSpanTreeButton);

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
        if(addRelationMatchLogicButtonPopover == null){
            AddRelationMatchLogicUI addRelationMatchLogicUI = new AddRelationMatchLogicUI();
            addRelationMatchLogicUI.setAddRelationMatchLogicHelper(addRelationMatchLogicHelper);
            addRelationMatchLogicButtonPopover = new Popover();
            addRelationMatchLogicButtonPopover.setTarget(addRelationMatchLogicButton);
            addRelationMatchLogicButtonPopover.setWidth("500px");
            addRelationMatchLogicButtonPopover.setHeight("220px");
            addRelationMatchLogicButtonPopover.addThemeVariants(PopoverVariant.ARROW);
            addRelationMatchLogicButtonPopover.setPosition(PopoverPosition.BOTTOM);
            addRelationMatchLogicButtonPopover.add(addRelationMatchLogicUI);
            addRelationMatchLogicButtonPopover.setAutofocus(true);
            addRelationMatchLogicButtonPopover.setModal(true,true);
        }
        addRelationMatchLogicButtonPopover.open();

        /*
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加关系类型匹配逻辑",null,true,470,280,false);
        fixSizeWindow.setWindowContent(addRelationMatchLogicUI);
        fixSizeWindow.setModel(true);
        addRelationMatchLogicUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
        */
    }

    private void renderAddConceptionMatchLogicUI(){
        if(addConceptionMatchLogicButtonPopover == null){
            AddConceptionMatchLogicUI addConceptionMatchLogicUI = new AddConceptionMatchLogicUI();
            addConceptionMatchLogicUI.setAddConceptionMatchLogicHelper(addConceptionMatchLogicHelper);
            addConceptionMatchLogicButtonPopover = new Popover();
            addConceptionMatchLogicButtonPopover.setTarget(addConceptionMatchLogicButton);
            addConceptionMatchLogicButtonPopover.setWidth("500px");
            addConceptionMatchLogicButtonPopover.setHeight("220px");
            addConceptionMatchLogicButtonPopover.addThemeVariants(PopoverVariant.ARROW);
            addConceptionMatchLogicButtonPopover.setPosition(PopoverPosition.TOP);
            addConceptionMatchLogicButtonPopover.add(addConceptionMatchLogicUI);
            addConceptionMatchLogicButtonPopover.setAutofocus(true);
            addConceptionMatchLogicButtonPopover.setModal(true,true);
        }
        addConceptionMatchLogicButtonPopover.open();

        /*
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加概念类型匹配逻辑",null,true,470,280,false);
        fixSizeWindow.setWindowContent(addConceptionMatchLogicUI);
        fixSizeWindow.setModel(true);
        addConceptionMatchLogicUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
        */
    }

    private void renderResultSetConfig(){
        if(resultSetConfigButtonPopover == null){
            resultSetConfigButtonPopover = new Popover();
            ExpandResultSetConfigView expandResultSetConfigView = new ExpandResultSetConfigView(expandParameters);
            expandResultSetConfigView.setContainerPopover(resultSetConfigButtonPopover);
            resultSetConfigButtonPopover.setTarget(resultSetConfigButton);
            resultSetConfigButtonPopover.setWidth("250px");
            resultSetConfigButtonPopover.setHeight("300px");
            resultSetConfigButtonPopover.addThemeVariants(PopoverVariant.ARROW);
            resultSetConfigButtonPopover.setPosition(PopoverPosition.TOP);
            resultSetConfigButtonPopover.add(expandResultSetConfigView);
            resultSetConfigButtonPopover.setAutofocus(true);
            resultSetConfigButtonPopover.setModal(true,true);
        }
        resultSetConfigButtonPopover.open();
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

    private void executeConceptionEntityExpand(TopologyExpandType topologyExpandType){
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

        ConceptionEntityExpandTopologyEvent conceptionEntityExpandTopologyEvent =new ConceptionEntityExpandTopologyEvent();
        conceptionEntityExpandTopologyEvent.setConceptionEntityUID(this.conceptionEntityUID);
        conceptionEntityExpandTopologyEvent.setConceptionKind(this.conceptionKind);
        conceptionEntityExpandTopologyEvent.setExpandParameters(this.expandParameters);

        if(this.topologyExpandType != null){
            conceptionEntityExpandTopologyEvent.setPathExpandType(this.topologyExpandType);
        }else{
            conceptionEntityExpandTopologyEvent.setPathExpandType(topologyExpandType);
        }
        conceptionEntityExpandTopologyEvent.setRelationKindMatchLogics(relationKindMatchLogicList);
        conceptionEntityExpandTopologyEvent.setDefaultDirectionForNoneRelationKindMatch(defaultRelationDirection);
        conceptionEntityExpandTopologyEvent.setConceptionKindMatchLogics(conceptionKindMatchLogicList);
        conceptionEntityExpandTopologyEvent.setContainsSelf(containsSelf);
        conceptionEntityExpandTopologyEvent.setMaxJump(maxJump);
        conceptionEntityExpandTopologyEvent.setMinJump(minJump);
        ResourceHolder.getApplicationBlackboard().fire(conceptionEntityExpandTopologyEvent);
    }
}
