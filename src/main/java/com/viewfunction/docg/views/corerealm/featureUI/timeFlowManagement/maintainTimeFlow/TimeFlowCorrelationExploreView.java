package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.List;

public class TimeFlowCorrelationExploreView extends VerticalLayout {

    private int viewHeight;
    private int viewWidth;

    private TimeFlowCorrelationInfoChart timeFlowCorrelationInfoChart;

    public TimeFlowCorrelationExploreView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        HorizontalLayout toolbarActionsContainerLayout = new HorizontalLayout();
        toolbarActionsContainerLayout.setSpacing(false);
        toolbarActionsContainerLayout.setMargin(false);
        toolbarActionsContainerLayout.setPadding(false);

        toolbarActionsContainerLayout.setWidthFull();
        toolbarActionsContainerLayout.setHeight(20, Unit.PIXELS);
        toolbarActionsContainerLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(toolbarActionsContainerLayout);

        SecondaryIconTitle filterTitle3 = new SecondaryIconTitle(FontAwesome.Solid.CLOCK.create(),"时间尺度实体检索结果");
        filterTitle3.getStyle().set("padding-left","10px");
        toolbarActionsContainerLayout.add(filterTitle3);

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

    public void renderTimeFlowCorrelationData(List<TimeScaleEntity> timeScaleEntityList){
        this.timeFlowCorrelationInfoChart.renderTimeFlowCorrelationData(timeScaleEntityList);
    }
}
