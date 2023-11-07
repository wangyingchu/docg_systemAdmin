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
        timeAreaInfoMessage.getStyle().set("font-size","10px").set("padding-left","30px");
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
        timeEntityCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","30px");
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
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","30px");
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
        spaceDiv02.setWidth(10,Unit.PIXELS);
        toolbarActionsContainerLayout.add(spaceDiv02);

        Button addMoreTimeFlowEntitiesInfoButton = new Button();
        addMoreTimeFlowEntitiesInfoButton.setIcon(VaadinIcon.ARROW_RIGHT.create());
        addMoreTimeFlowEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        addMoreTimeFlowEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        toolbarActionsContainerLayout.add(addMoreTimeFlowEntitiesInfoButton);
        addMoreTimeFlowEntitiesInfoButton.getStyle().set("top","-4px").set("position","relative");

        HorizontalLayout spaceDiv03 = new HorizontalLayout();
        spaceDiv03.setWidth(10,Unit.PIXELS);
        toolbarActionsContainerLayout.add(spaceDiv03);

        Button reloadTimeFlowEntitiesInfoButton = new Button();
        reloadTimeFlowEntitiesInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadTimeFlowEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        reloadTimeFlowEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        toolbarActionsContainerLayout.add(reloadTimeFlowEntitiesInfoButton);
        reloadTimeFlowEntitiesInfoButton.getStyle().set("top","-4px").set("position","relative");

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

    public void renderTimeFlowCorrelationData(TimeFlow.TimeScaleGrade queryTimeScaleGrade,String timeArea, List<TimeScaleEntity> timeScaleEntityList){

        timeGuLevelDisplayValue.setText(queryTimeScaleGrade.toString());
        timeAreaDisplayValue.setText(timeArea);
        timeEntityCountDisplayValue.setText(numberFormat.format(timeScaleEntityList.size()));


        //this.timeFlowCorrelationInfoChart.renderTimeFlowCorrelationData(timeScaleEntityList);
    }
}
