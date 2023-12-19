package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.dataCompute.computeServiceCore.payload.ComputeGridRealtimeStatisticsInfo;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.chart.BulletChart;
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
    private SecondaryKeyValueDisplayItem totalAllocatedMemoryCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalUsedMemoryCountDisplayItemDisplayItem;
    private SecondaryKeyValueDisplayItem freeMemoryPercentDisplayItem;
    private NumberFormat nt;
    private NumberFormat nt2;
    final ZoneId id = ZoneId.systemDefault();
    private SecondaryKeyValueDisplayItem gridStartDatetimeDisplayItem;
    private SecondaryKeyValueDisplayItem firstComputeUnitDisplayItem;
    private SecondaryKeyValueDisplayItem lastComputeUnitDisplayItem;
    private SecondaryKeyValueDisplayItem maxAvailableMemoryCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalBusyTimeInMinuteDisplayItem;
    private SecondaryKeyValueDisplayItem totalIdelTimeInMinuteDisplayItem;
    private BulletChart bulletChart;
    private HorizontalLayout bulletChartContainer;
    public GridRuntimeInfoWidget(){
        this.setWidthFull();

        nt = NumberFormat.getIntegerInstance();
        nt.setMinimumFractionDigits(0);

        nt2 = NumberFormat.getPercentInstance();
        nt2.setMinimumFractionDigits(2);

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

        HorizontalLayout statusInfoContainer2_A = new HorizontalLayout();
        statusInfoContainer2_A.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer2_A);

        totalBusyTimeInMinuteDisplayItem =  new SecondaryKeyValueDisplayItem(statusInfoContainer2_A, VaadinIcon.FLIGHT_TAKEOFF.create(),"网格当前运算总耗时(秒):",
                "-");

        HorizontalLayout horizSpaceDivLayout02_A = new HorizontalLayout();
        horizSpaceDivLayout02_A.setWidth(20, Unit.PIXELS);
        statusInfoContainer2_A.add(horizSpaceDivLayout02_A);

        totalIdelTimeInMinuteDisplayItem =  new SecondaryKeyValueDisplayItem(statusInfoContainer2_A, VaadinIcon.FLIGHT_LANDING.create(),"网格总空闲时间(秒):",
                "-");

        HorizontalLayout statusInfoContainer3 = new HorizontalLayout();
        statusInfoContainer3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer3);

        firstComputeUnitDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer3,VaadinIcon.SERVER.create(),"最早启动计算单元:",
                "-");

        Button showFirstStartedUnitInfoButton = new Button();
        showFirstStartedUnitInfoButton.setIcon(VaadinIcon.EYE.create());
        showFirstStartedUnitInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_ICON);
        statusInfoContainer3.add(showFirstStartedUnitInfoButton);

        HorizontalLayout statusInfoContainer4 = new HorizontalLayout();
        statusInfoContainer4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer4);
        lastComputeUnitDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer4,VaadinIcon.SERVER.create(),"最后启动计算单元:",
                "-");

        Button showLastStartedUnitInfoButton = new Button();
        showLastStartedUnitInfoButton.setIcon(VaadinIcon.EYE.create());
        showLastStartedUnitInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_ICON);
        statusInfoContainer4.add(showLastStartedUnitInfoButton);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(3, Unit.PIXELS);
        add(spaceDivLayout1);

        ThirdLevelIconTitle memoryStatusInfoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.MEMORY_SOLID.create(), "网格内存资源概览");
        add(memoryStatusInfoTitle);

        HorizontalLayout memorySpaceInfoLayout = new HorizontalLayout();
        memorySpaceInfoLayout.setDefaultVerticalComponentAlignment(Alignment.START);
        add(memorySpaceInfoLayout);

        VerticalLayout heapMemoryInfoLeftLayout = new VerticalLayout();
        heapMemoryInfoLeftLayout.setPadding(false);
        heapMemoryInfoLeftLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
        memorySpaceInfoLayout.add(heapMemoryInfoLeftLayout);

        HorizontalLayout statusInfoContainer7 = new HorizontalLayout();
        statusInfoContainer7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        heapMemoryInfoLeftLayout.add(statusInfoContainer7);
        totalAllocatedMemoryCountDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer7, VaadinIcon.HARDDRIVE_O.create(),"已分配内存总量:","-");

        HorizontalLayout statusInfoContainer8 = new HorizontalLayout();
        statusInfoContainer8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        heapMemoryInfoLeftLayout.add(statusInfoContainer8);
        totalUsedMemoryCountDisplayItemDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer8, VaadinIcon.HARDDRIVE.create(),"已用内存总量:","-");

        HorizontalLayout statusInfoContainer9 = new HorizontalLayout();
        statusInfoContainer9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        heapMemoryInfoLeftLayout.add(statusInfoContainer9);
        maxAvailableMemoryCountDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer9, VaadinIcon.STORAGE.create(),"网格最大可用内存总量:","-");

        HorizontalLayout statusInfoContainer10 = new HorizontalLayout();
        statusInfoContainer10.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        heapMemoryInfoLeftLayout.add(statusInfoContainer10);
        freeMemoryPercentDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer10, VaadinIcon.PIE_CHART.create(),"网格未用内存空间占比:","-");

        bulletChartContainer = new HorizontalLayout();
        bulletChartContainer.setSpacing(false);
        bulletChartContainer.setPadding(false);
        bulletChartContainer.setMargin(false);

        memorySpaceInfoLayout.add(bulletChartContainer);
    }

    public void refreshRuntimeInfo(ComputeGridRealtimeStatisticsInfo computeGridRealtimeStatisticsInfo){
        if(computeGridRealtimeStatisticsInfo != null) {
            if(computeGridRealtimeStatisticsInfo.getGridStartTime() != null){
                gridStartDatetimeDisplayItem.updateDisplayValue(computeGridRealtimeStatisticsInfo.getGridStartTime().format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
                gridUpTimeInSecondDisplayItem.updateDisplayValue(""+computeGridRealtimeStatisticsInfo.getGridUpTimeInMinute());
                totalAvailableCPUCoresDisplayItem.updateDisplayValue(""+computeGridRealtimeStatisticsInfo.getTotalAvailableCPUCores());
                totalComputeUnitCountDisplayItem.updateDisplayValue(""+computeGridRealtimeStatisticsInfo.getDataComputeUnitsAmount());
                firstComputeUnitDisplayItem.updateDisplayValue(computeGridRealtimeStatisticsInfo.getOldestUnitId());
                lastComputeUnitDisplayItem.updateDisplayValue(computeGridRealtimeStatisticsInfo.getYoungestUnitId());

                long maxMemory = computeGridRealtimeStatisticsInfo.getMaxAvailableMemoryInMB();
                long usedMemory = computeGridRealtimeStatisticsInfo.getUsedMemoryInMB();
                double freePercent = (double)(maxMemory-usedMemory)/(double)maxMemory;
                long assignedMemory = computeGridRealtimeStatisticsInfo.getAssignedMemoryInMB();
                double usedPercent = (double)(usedMemory)/(double)maxMemory;
                double waterMarkPercent = (double)(assignedMemory)/(double)maxMemory;

                totalAllocatedMemoryCountDisplayItem.updateDisplayValue(nt.format(computeGridRealtimeStatisticsInfo.getAssignedMemoryInMB()/1024)+"GB"+" ("+nt2.format(waterMarkPercent)+")");
                totalUsedMemoryCountDisplayItemDisplayItem.updateDisplayValue(nt.format(computeGridRealtimeStatisticsInfo.getUsedMemoryInMB()/1024)+"GB"+" ("+nt2.format(usedPercent)+")");
                maxAvailableMemoryCountDisplayItem.updateDisplayValue(nt.format(computeGridRealtimeStatisticsInfo.getMaxAvailableMemoryInMB()/1024)+"GB");
                freeMemoryPercentDisplayItem.updateDisplayValue(nt2.format(freePercent));
                totalBusyTimeInMinuteDisplayItem.updateDisplayValue(nt.format(computeGridRealtimeStatisticsInfo.getTotalBusyTimeInSecond()));
                totalIdelTimeInMinuteDisplayItem.updateDisplayValue(nt.format(computeGridRealtimeStatisticsInfo.getTotalIdleTimeInSecond()));

                if(bulletChart != null){
                    bulletChartContainer.removeAll();
                    bulletChart = null;
                }

                bulletChart = new BulletChart();
                bulletChart.setHeight(100,Unit.PIXELS);
                bulletChart.setWidth(360,Unit.PIXELS);
                bulletChartContainer.add(bulletChart);
                bulletChart.getStyle().set("left","40px").set("top","-5px").set("position","relative");
                bulletChart.setData("Mem Consumption",Double.valueOf(nt2.format(usedPercent).replace("%","")),Double.valueOf(nt2.format(waterMarkPercent).replace("%","")));
            }
            infoSampleDateDisplayItem.updateDisplayValue(new Date().toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
        }
    }
}
