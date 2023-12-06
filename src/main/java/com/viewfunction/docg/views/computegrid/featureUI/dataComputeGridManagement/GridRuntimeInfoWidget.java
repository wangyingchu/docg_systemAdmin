package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.dataCompute.applicationCapacity.dataCompute.dataComputeGrid.ComputeGridRealtimeMetrics;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class GridRuntimeInfoWidget extends VerticalLayout {

    private SecondaryKeyValueDisplayItem gridUpTimeInSecondDisplayItem;
    private SecondaryKeyValueDisplayItem infoSampleDateDisplayItem;
    private SecondaryKeyValueDisplayItem totalAvailableCPUCoresDisplayItem;
    private SecondaryKeyValueDisplayItem totalComputeUnitCountDisplayItem;
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
                VaadinIcon.FLIGHT_TAKEOFF.create(),"计算网格启动时间:", "-");

        HorizontalLayout horizSpaceDivLayout01 = new HorizontalLayout();
        horizSpaceDivLayout01.setWidth(20, Unit.PIXELS);
        statusInfoContainer1.add(horizSpaceDivLayout01);

        infoSampleDateDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer1,
                VaadinIcon.CAMERA.create(),"指标采样时间:", "-");

        HorizontalLayout statusInfoContainer2 = new HorizontalLayout();
        statusInfoContainer2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer2);
        gridUpTimeInSecondDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer2, VaadinIcon.TIMER.create(),"网格运行时长(分钟):",
                "-");

        HorizontalLayout horizSpaceDivLayout02 = new HorizontalLayout();
        horizSpaceDivLayout02.setWidth(20, Unit.PIXELS);
        statusInfoContainer2.add(horizSpaceDivLayout02);

        totalAvailableCPUCoresDisplayItem =  new SecondaryKeyValueDisplayItem(statusInfoContainer2, LineAwesomeIconsSvg.BOLT_SOLID.create(),"可用 CPU 总核数:",
                "-");

        HorizontalLayout horizSpaceDivLayout03 = new HorizontalLayout();
        horizSpaceDivLayout03.setWidth(20, Unit.PIXELS);
        statusInfoContainer2.add(horizSpaceDivLayout03);

        totalComputeUnitCountDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer2, LineAwesomeIconsSvg.SERVER_SOLID.create(),"数据计算单元总数:",
                "-");

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(6, Unit.PIXELS);
        add(spaceDivLayout1);

        ThirdLevelIconTitle memoryStatusInfoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.MEMORY_SOLID.create(), "网格内存资源概览");
        add(memoryStatusInfoTitle);
    }

    public void refreshRuntimeInfo(ComputeGridRealtimeMetrics targetComputeGridRealtimeMetrics){
        if(targetComputeGridRealtimeMetrics != null) {
            if(targetComputeGridRealtimeMetrics.getGridStartTime() != null){
                gridStartDatetimeDisplayItem.updateDisplayValue(targetComputeGridRealtimeMetrics.getGridStartTime().format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
                gridUpTimeInSecondDisplayItem.updateDisplayValue(""+targetComputeGridRealtimeMetrics.getGridUpTimeInMinute());
                totalAvailableCPUCoresDisplayItem.updateDisplayValue(""+targetComputeGridRealtimeMetrics.getAvailableCPUCores());
                totalComputeUnitCountDisplayItem.updateDisplayValue(""+targetComputeGridRealtimeMetrics.getDataComputeUnitsAmount());
            }
            infoSampleDateDisplayItem.updateDisplayValue(new Date().toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
        }


    }
}
