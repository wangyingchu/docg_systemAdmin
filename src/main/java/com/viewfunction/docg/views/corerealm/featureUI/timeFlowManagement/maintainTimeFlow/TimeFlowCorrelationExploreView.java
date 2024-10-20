package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TimeFlowCorrelationExploreView extends VerticalLayout {

    private int viewHeight;
    private int viewWidth;
    private TimeFlowCorrelationInfoChart timeFlowCorrelationInfoChart;
    private NativeLabel timeGuLevelDisplayValue;
    private NativeLabel timeAreaDisplayValue;
    private NativeLabel timeEntityCountDisplayValue;
    private NativeLabel currentDisplayCountDisplayValue;
    private NumberFormat numberFormat;
    private int entitiesDisplayBatchSize = 100;
    private int currentLastDisplayEntityIndex = 0;
    private List<TimeScaleEntity> timeScaleEntityList;
    private TimeFlow.TimeScaleGrade initQueryTimeScaleGrade;
    private String initTimeArea;
    private List<TimeScaleEntity> initTimeScaleEntityList;
    private int initDisplayEntitiesCount;
    private Button addMoreTimeFlowEntitiesInfoButton;
    private String timeFlowName;
    private TimeFlowRelatedEntitySummaryInfoView timeFlowRelatedEntitySummaryInfoView;
    private String showingDetailEntityKind;
    private String showingDetailEntityUID;
    private  Button selectedTimeScaleEntityOperationButton;
    private Popover selectedTimeScaleEntityOperationButtonPopover;

    public TimeFlowCorrelationExploreView(String timeFlowName){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.numberFormat = NumberFormat.getInstance();
        this.timeFlowName = timeFlowName;
        HorizontalLayout toolbarActionsContainerLayout = new HorizontalLayout();
        toolbarActionsContainerLayout.setSpacing(false);
        toolbarActionsContainerLayout.setMargin(false);
        toolbarActionsContainerLayout.setPadding(false);

        toolbarActionsContainerLayout.setWidthFull();
        toolbarActionsContainerLayout.setHeight(20, Unit.PIXELS);
        toolbarActionsContainerLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(toolbarActionsContainerLayout);

        SecondaryIconTitle filterTitle3 = new SecondaryIconTitle(LineAwesomeIconsSvg.HUBSPOT.create(),"时间流实体探索");
        filterTitle3.getStyle().set("padding-left","10px");
        toolbarActionsContainerLayout.add(filterTitle3);

        NativeLabel timeGuLevelInfoMessage = new NativeLabel("时间粒度:");
        timeGuLevelInfoMessage.getStyle().set("font-size","10px").set("padding-left","30px");
        timeGuLevelInfoMessage.addClassNames("text-tertiary");
        toolbarActionsContainerLayout.add(timeGuLevelInfoMessage);

        timeGuLevelDisplayValue = new NativeLabel("-");
        timeGuLevelDisplayValue.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight","bolder")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("color","#2e4e7e");
        toolbarActionsContainerLayout.add(timeGuLevelDisplayValue);
        timeGuLevelDisplayValue.getStyle().set("top","-2px").set("position","relative").set("padding-left","5px");

        NativeLabel timeAreaInfoMessage = new NativeLabel("时间范围:");
        timeAreaInfoMessage.getStyle().set("font-size","10px").set("padding-left","20px");
        timeAreaInfoMessage.addClassNames("text-tertiary");
        toolbarActionsContainerLayout.add(timeAreaInfoMessage);

        timeAreaDisplayValue = new NativeLabel("-");
        timeAreaDisplayValue.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight","bolder")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("color","#2e4e7e");
        toolbarActionsContainerLayout.add(timeAreaDisplayValue);
        timeAreaDisplayValue.getStyle().set("top","-2px").set("position","relative").set("padding-left","5px");

        NativeLabel timeEntityCountInfoMessage = new NativeLabel("时间尺度实体数量:");
        timeEntityCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","20px");
        timeEntityCountInfoMessage.addClassNames("text-tertiary");
        toolbarActionsContainerLayout.add(timeEntityCountInfoMessage);

        timeEntityCountDisplayValue = new NativeLabel("-");
        timeEntityCountDisplayValue.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight","bolder")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("color","#2e4e7e");
        toolbarActionsContainerLayout.add(timeEntityCountDisplayValue);
        timeEntityCountDisplayValue.getStyle().set("top","-2px").set("position","relative").set("padding-left","5px");

        NativeLabel currentDisplayCountInfoMessage = new NativeLabel("当前显示实体数量(前):");
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","20px");
        currentDisplayCountInfoMessage.addClassNames("text-tertiary");
        toolbarActionsContainerLayout.add(currentDisplayCountInfoMessage);

        currentDisplayCountDisplayValue = new NativeLabel("-");
        currentDisplayCountDisplayValue.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight","bolder")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("color","#2e4e7e");
        toolbarActionsContainerLayout.add(currentDisplayCountDisplayValue);
        currentDisplayCountDisplayValue.getStyle().set("top","-2px").set("position","relative").set("padding-left","5px");

        HorizontalLayout spaceDiv01 = new HorizontalLayout();
        spaceDiv01.setWidth(10,Unit.PIXELS);
        toolbarActionsContainerLayout.add(spaceDiv01);

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("10px");
        divIcon.getStyle().set("top","1px").set("position","relative");
        toolbarActionsContainerLayout.add(divIcon);

        HorizontalLayout spaceDiv02 = new HorizontalLayout();
        spaceDiv02.setWidth(20,Unit.PIXELS);
        toolbarActionsContainerLayout.add(spaceDiv02);

        addMoreTimeFlowEntitiesInfoButton = new Button();
        addMoreTimeFlowEntitiesInfoButton.setTooltipText("追加显示最多后续100个时间流实体");
        addMoreTimeFlowEntitiesInfoButton.setIcon(VaadinIcon.ARROW_RIGHT.create());
        addMoreTimeFlowEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderMoreTimeFlowCorrelationData();
            }
        });
        addMoreTimeFlowEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        toolbarActionsContainerLayout.add(addMoreTimeFlowEntitiesInfoButton);
        addMoreTimeFlowEntitiesInfoButton.getStyle().set("top","-4px").set("position","relative");

        HorizontalLayout spaceDiv03 = new HorizontalLayout();
        spaceDiv03.setWidth(10,Unit.PIXELS);
        toolbarActionsContainerLayout.add(spaceDiv03);

        Button reloadTimeFlowEntitiesInfoButton = new Button();
        reloadTimeFlowEntitiesInfoButton.setTooltipText("刷新显示初始时间流实体");
        reloadTimeFlowEntitiesInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadTimeFlowEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshInitTimeFlowEntitiesInfo();
            }
        });
        reloadTimeFlowEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        toolbarActionsContainerLayout.add(reloadTimeFlowEntitiesInfoButton);
        reloadTimeFlowEntitiesInfoButton.getStyle().set("top","-4px").set("position","relative");

        HorizontalLayout spaceDiv04 = new HorizontalLayout();
        spaceDiv04.setWidth(10,Unit.PIXELS);
        toolbarActionsContainerLayout.add(spaceDiv04);

        Button clearFlowEntitiesInfoButton = new Button();
        clearFlowEntitiesInfoButton.setTooltipText("清除当前显示时间流实体");
        clearFlowEntitiesInfoButton.setIcon(VaadinIcon.ERASER.create());
        clearFlowEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cleanCurrentTimeFlowEntitiesData();
            }
        });
        clearFlowEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        toolbarActionsContainerLayout.add(clearFlowEntitiesInfoButton);
        clearFlowEntitiesInfoButton.getStyle().set("top","-4px").set("position","relative");

        NativeLabel selectMethodMessage = new NativeLabel("左键单击聚焦选择实体，右键单击拓展显示实体时间关联信息");
        selectMethodMessage.getStyle().set("font-size","10px").set("padding-left","30px");
        selectMethodMessage.addClassNames("text-tertiary");
        toolbarActionsContainerLayout.add(selectMethodMessage);
        selectMethodMessage.getStyle().set("top","0px").set("position","relative");

        this.timeFlowRelatedEntitySummaryInfoView = new TimeFlowRelatedEntitySummaryInfoView();
        //renderConceptionEntityUI(showingDetailEntityKind,showingDetailEntityUID);

        selectedTimeScaleEntityOperationButton = new Button("选中实体信息概览");
        selectedTimeScaleEntityOperationButton.getStyle().set("font-size","10px");
        selectedTimeScaleEntityOperationButton.getStyle().set("top","-8px").set("position","relative");
        selectedTimeScaleEntityOperationButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        selectedTimeScaleEntityOperationButton.setIcon(VaadinIcon.CROSSHAIRS.create());
        selectedTimeScaleEntityOperationButton.setEnabled(false);
        toolbarActionsContainerLayout.add(selectedTimeScaleEntityOperationButton);
        selectedTimeScaleEntityOperationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                selectedTimeScaleEntityOperationButtonPopover.open();
            }
        });

        selectedTimeScaleEntityOperationButtonPopover = new Popover();
        selectedTimeScaleEntityOperationButtonPopover.setTarget(selectedTimeScaleEntityOperationButton);
        selectedTimeScaleEntityOperationButtonPopover.setWidth("300px");
        selectedTimeScaleEntityOperationButtonPopover.setHeight("340px");
        selectedTimeScaleEntityOperationButtonPopover.addThemeVariants(PopoverVariant.ARROW);
        selectedTimeScaleEntityOperationButtonPopover.setPosition(PopoverPosition.BOTTOM);
        selectedTimeScaleEntityOperationButtonPopover.add(timeFlowRelatedEntitySummaryInfoView);
        selectedTimeScaleEntityOperationButtonPopover.setAutofocus(true);
        selectedTimeScaleEntityOperationButtonPopover.setModal(true);

        this.timeFlowCorrelationInfoChart = new TimeFlowCorrelationInfoChart(this.timeFlowName);
        this.timeFlowCorrelationInfoChart.setContainerTimeFlowCorrelationExploreView(this);
        add(this.timeFlowCorrelationInfoChart);
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
        this.timeFlowCorrelationInfoChart.setGraphHeight(this.viewHeight -20);
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
        this.timeFlowCorrelationInfoChart.setGraphWidth(this.viewWidth);
    }

    public void renderInitTimeFlowCorrelationData(TimeFlow.TimeScaleGrade queryTimeScaleGrade, String timeArea, List<TimeScaleEntity> timeScaleEntityList){
        this.initQueryTimeScaleGrade = queryTimeScaleGrade;
        this.initTimeArea = timeArea;
        this.initTimeScaleEntityList = timeScaleEntityList;
        this.timeGuLevelDisplayValue.setText(queryTimeScaleGrade.toString());
        this.timeAreaDisplayValue.setText(timeArea);
        this.timeEntityCountDisplayValue.setText(numberFormat.format(timeScaleEntityList.size()));
        this.timeScaleEntityList = timeScaleEntityList;
        if(this.timeScaleEntityList != null){
            int currentDisplayEntitiesCount = this.timeScaleEntityList.size() <= this.entitiesDisplayBatchSize ? this.timeScaleEntityList.size() : this.entitiesDisplayBatchSize;
            if(this.timeScaleEntityList.size() <= this.entitiesDisplayBatchSize){
                addMoreTimeFlowEntitiesInfoButton.setEnabled(false);
            }else{
                addMoreTimeFlowEntitiesInfoButton.setEnabled(true);
            }
            this.initDisplayEntitiesCount = currentDisplayEntitiesCount;
            this.currentLastDisplayEntityIndex = currentDisplayEntitiesCount;
            this.currentDisplayCountDisplayValue.setText(""+currentDisplayEntitiesCount);
            if(this.timeScaleEntityList.size() > 1){
                this.timeFlowCorrelationInfoChart.renderTimeFlowCorrelationData(this.timeScaleEntityList.subList(0,currentLastDisplayEntityIndex));
            }else{
                this.timeFlowCorrelationInfoChart.renderTimeFlowCorrelationData(this.timeScaleEntityList);
            }
        }
        this.selectedTimeScaleEntityOperationButton.setEnabled(false);
    }

    public void renderMoreTimeFlowCorrelationData(){
        int newDisplayEntityIndex = (this.currentLastDisplayEntityIndex + this.entitiesDisplayBatchSize) <= (this.timeScaleEntityList.size()) ?
                (this.currentLastDisplayEntityIndex + this.entitiesDisplayBatchSize) : (this.timeScaleEntityList.size());
        List<TimeScaleEntity> newAddedTimeScaleEntityList = this.timeScaleEntityList.subList(this.currentLastDisplayEntityIndex,newDisplayEntityIndex);
        this.currentLastDisplayEntityIndex = newDisplayEntityIndex;
        this.currentDisplayCountDisplayValue.setText(""+currentLastDisplayEntityIndex);
        this.timeFlowCorrelationInfoChart.renderMoreTimeFlowCorrelationData(newAddedTimeScaleEntityList);
        this.selectedTimeScaleEntityOperationButton.setEnabled(false);
    }

    public void showEntityDetail(String entityType,String entityUID){
        this.showingDetailEntityKind = entityType;
        this.showingDetailEntityUID = entityUID;
        this.timeFlowRelatedEntitySummaryInfoView.showEntitySummaryInfo(showingDetailEntityKind,showingDetailEntityUID);
        this.selectedTimeScaleEntityOperationButton.setEnabled(true);
    }

    public void hideEntityDetail(){
        this.selectedTimeScaleEntityOperationButton.setEnabled(false);
    }

    private void renderConceptionEntityUI(String conceptionKindName,String conceptionEntityUID){
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionKindName,conceptionEntityUID);

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionKindName);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void cleanCurrentTimeFlowEntitiesData(){
        this.timeFlowCorrelationInfoChart.emptyGraph();
        this.currentDisplayCountDisplayValue .setText(""+0);
        this.selectedTimeScaleEntityOperationButton.setEnabled(false);
    }

    private void refreshInitTimeFlowEntitiesInfo(){
        this.timeFlowCorrelationInfoChart.emptyGraph();
        this.currentDisplayCountDisplayValue .setText(""+initDisplayEntitiesCount);
        renderInitTimeFlowCorrelationData(this.initQueryTimeScaleGrade,this.initTimeArea, this.initTimeScaleEntityList);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);
        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }
        MenuItem item = menu.addItem(icon, e -> {});
        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }
        if (label != null) {
            item.add(new Text(label));
        }
        return item;
    }
}
