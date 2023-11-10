package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
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
    //private int entitiesDisplayBatchSize = 100;
    private int entitiesDisplayBatchSize = 5;
    private int currentLastDisplayEntityIndex = 0;
    private List<TimeScaleEntity> timeScaleEntityList;
    private TimeFlow.TimeScaleGrade initQueryTimeScaleGrade;
    private String initTimeArea;
    private List<TimeScaleEntity> initTimeScaleEntityList;
    private int initDisplayEntitiesCount;
    private Button addMoreTimeFlowEntitiesInfoButton;

    public TimeFlowCorrelationExploreView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        numberFormat = NumberFormat.getInstance();

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

        this.timeFlowCorrelationInfoChart = new TimeFlowCorrelationInfoChart();
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
            this.currentDisplayCountDisplayValue .setText(""+currentDisplayEntitiesCount);
            this.timeFlowCorrelationInfoChart.renderTimeFlowCorrelationData(this.timeScaleEntityList.subList(0,currentLastDisplayEntityIndex-1));
        }
    }

    public void renderMoreTimeFlowCorrelationData(){
        int newDisplayEntityIndex = (this.currentLastDisplayEntityIndex-1 + this.entitiesDisplayBatchSize) <= (this.timeScaleEntityList.size()-1) ?
                (this.currentLastDisplayEntityIndex-1 + this.entitiesDisplayBatchSize) : (this.timeScaleEntityList.size()-1);
        List<TimeScaleEntity> newAddedTimeScaleEntityList = this.timeScaleEntityList.subList(this.currentLastDisplayEntityIndex,newDisplayEntityIndex);
        this.currentLastDisplayEntityIndex = newDisplayEntityIndex;
        this.currentDisplayCountDisplayValue.setText(""+currentLastDisplayEntityIndex+1);
        this.timeFlowCorrelationInfoChart.renderMoreTimeFlowCorrelationData(newAddedTimeScaleEntityList);
    }

    private void cleanCurrentTimeFlowEntitiesData(){
        this.timeFlowCorrelationInfoChart.emptyGraph();
        this.currentDisplayCountDisplayValue .setText(""+0);
    }

    private void refreshInitTimeFlowEntitiesInfo(){
        this.timeFlowCorrelationInfoChart.emptyGraph();
        this.currentDisplayCountDisplayValue .setText(""+initDisplayEntitiesCount);
        renderInitTimeFlowCorrelationData(this.initQueryTimeScaleGrade,this.initTimeArea, this.initTimeScaleEntityList);
    }
}
