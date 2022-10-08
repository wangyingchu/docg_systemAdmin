package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal.ConceptionEntityTemporalDataView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal.ConceptionEntityTemporalSunburstChart;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal.ConceptionEntityTemporalTimelineChart;

public class ConceptionEntityTemporalInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntityTemporalInfoViewHeightOffset;
    private ConceptionEntityTemporalTimelineChart conceptionEntityTemporalTimelineChart;
    private ConceptionEntityTemporalDataView conceptionEntityTemporalDataView;
    private ConceptionEntityTemporalSunburstChart conceptionEntityTemporalSunburstChart;

    public ConceptionEntityTemporalInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntityTemporalInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityTemporalInfoViewHeightOffset = conceptionEntityTemporalInfoViewHeightOffset+106;

        HorizontalLayout temporalEntityAndChartContainer = new HorizontalLayout();
        temporalEntityAndChartContainer.setPadding(false);
        temporalEntityAndChartContainer.setSpacing(false);
        temporalEntityAndChartContainer.setMargin(false);
        add(temporalEntityAndChartContainer);

        conceptionEntityTemporalDataView = new ConceptionEntityTemporalDataView();
        conceptionEntityTemporalSunburstChart = new ConceptionEntityTemporalSunburstChart();
        temporalEntityAndChartContainer.add(conceptionEntityTemporalDataView);
        temporalEntityAndChartContainer.add(conceptionEntityTemporalSunburstChart);

        conceptionEntityTemporalTimelineChart = new ConceptionEntityTemporalTimelineChart();
        add(conceptionEntityTemporalTimelineChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    public void renderEntityTemporalInfo(){
        conceptionEntityTemporalTimelineChart.renderTemporalTimelineInfo(this.conceptionKind,this.conceptionEntityUID);
    }
}
