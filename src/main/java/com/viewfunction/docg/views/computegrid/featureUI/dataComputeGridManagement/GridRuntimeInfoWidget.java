package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.dataCompute.applicationCapacity.dataCompute.dataComputeGrid.ComputeGridRealtimeMetrics;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class GridRuntimeInfoWidget extends VerticalLayout {

    private SecondaryKeyValueDisplayItem realmStartDateDisplayItem;
    private SecondaryKeyValueDisplayItem infoSampleDateDisplayItem;
    private SecondaryKeyValueDisplayItem totalRequestDisplayItem;
    private SecondaryKeyValueDisplayItem peakRequestDisplayItem;
    private SecondaryKeyValueDisplayItem currentRequestDisplayItem;
    private SecondaryKeyValueDisplayItem totalDiskDisplayItem;
    private SecondaryKeyValueDisplayItem freeDiskDisplayItem;
    private SecondaryKeyValueDisplayItem freeDiskPercentDisplayItem;
    private SecondaryKeyValueDisplayItem usableDiskDisplayItem;
    private NumberFormat nt;
    private PieChart pieChart;
    final ZoneId id = ZoneId.systemDefault();
    private SecondaryKeyValueDisplayItem gridStartDatetimeDisplayItem;
    public GridRuntimeInfoWidget(){
        this.setWidthFull();

        nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(1);


        HorizontalLayout statusInfoContainer1 = new HorizontalLayout();
        statusInfoContainer1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer1);
        gridStartDatetimeDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer1,
                VaadinIcon.GOLF.create(),"计算网格启动时间:", "-");

        HorizontalLayout statusInfoContainer2 = new HorizontalLayout();
        statusInfoContainer2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer2);
        realmStartDateDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer2, VaadinIcon.FLIGHT_TAKEOFF.create(),"领域启动时间:",
                "-");










    }

    public void refreshRuntimeInfo(ComputeGridRealtimeMetrics targetComputeGridRealtimeMetrics){
        if(targetComputeGridRealtimeMetrics != null) {
            if(targetComputeGridRealtimeMetrics.getGridStartTime() != null){
                gridStartDatetimeDisplayItem.updateDisplayValue(targetComputeGridRealtimeMetrics.getGridStartTime().format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }

        }


    }
}
