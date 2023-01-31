package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SystemStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;

import java.text.NumberFormat;
import java.util.Date;

public class SystemRuntimeInfoWidget extends VerticalLayout {

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
    public SystemRuntimeInfoWidget(){
        this.setWidthFull();

        nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(1);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        SystemStatusSnapshotInfo systemStatusSnapshotInfo = systemMaintenanceOperator.getSystemStatusSnapshot();

        HorizontalLayout statusInfoContainer1 = new HorizontalLayout();
        statusInfoContainer1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer1);
        new SecondaryKeyValueDisplayItem(statusInfoContainer1, VaadinIcon.GOLF.create(),"领域创建时间:",systemStatusSnapshotInfo.getSystemCreateTime().toString());

        HorizontalLayout statusInfoContainer2 = new HorizontalLayout();
        statusInfoContainer2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer2);
        realmStartDateDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer2, VaadinIcon.FLIGHT_TAKEOFF.create(),"领域启动时间:",systemStatusSnapshotInfo.getSystemStartupTime().toString());

        HorizontalLayout statusInfoContainer3 = new HorizontalLayout();
        statusInfoContainer3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer3);
        infoSampleDateDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer3, VaadinIcon.CAMERA.create(),"指标采样时间:",new Date(systemStatusSnapshotInfo.getSnapshotTookTime()).toString());

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(6, Unit.PIXELS);
        add(spaceDivLayout1);

        HorizontalLayout statusInfoContainer4 = new HorizontalLayout();
        statusInfoContainer4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer4);
        totalRequestDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer4, VaadinIcon.AREA_SELECT.create(),"领域服务请求总量:",""+systemStatusSnapshotInfo.getTotalAcceptedRequestCount());

        HorizontalLayout statusInfoContainer5 = new HorizontalLayout();
        statusInfoContainer5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer5);
        peakRequestDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer5, VaadinIcon.AREA_SELECT.create(),"领域服务请求峰值:",""+systemStatusSnapshotInfo.getPeakRequestCount());

        HorizontalLayout statusInfoContainer6 = new HorizontalLayout();
        statusInfoContainer6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer6);
        currentRequestDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer6, VaadinIcon.AREA_SELECT.create(),"当前领域服务请求量:",""+systemStatusSnapshotInfo.getCurrentAcceptedRequestCount());

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(6, Unit.PIXELS);
        add(spaceDivLayout2);

        HorizontalLayout diskSpaceInfoLayout = new HorizontalLayout();
        diskSpaceInfoLayout.setDefaultVerticalComponentAlignment(Alignment.START);
        add(diskSpaceInfoLayout);

        VerticalLayout diskInfoLeftLayout = new VerticalLayout();
        diskInfoLeftLayout.setPadding(false);
        diskInfoLeftLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
        diskSpaceInfoLayout.add(diskInfoLeftLayout);

        HorizontalLayout statusInfoContainer7 = new HorizontalLayout();
        statusInfoContainer7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer7);
        totalDiskDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer7, VaadinIcon.DATABASE.create(),"系统磁盘空间总量:",""+(systemStatusSnapshotInfo.getTotalDiskSpaceSize()/1000000000)+" GB");

        HorizontalLayout statusInfoContainer8 = new HorizontalLayout();
        statusInfoContainer8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer8);
        freeDiskDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer8, VaadinIcon.HARDDRIVE.create(),"系统未用磁盘空间总量:",""+(systemStatusSnapshotInfo.getFreeDiskSpaceSize()/1000000000)+" GB");

        HorizontalLayout statusInfoContainer10 = new HorizontalLayout();
        statusInfoContainer10.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer10);
        freeDiskPercentDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer10, VaadinIcon.PIE_CHART.create(),"系统未用磁盘空间占比:",nt.format(systemStatusSnapshotInfo.getFreeDiskPercent()));

        HorizontalLayout statusInfoContainer9 = new HorizontalLayout();
        statusInfoContainer9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer9);
        usableDiskDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer9, VaadinIcon.HARDDRIVE_O.create(),"领域可用磁盘空间总量:",""+(systemStatusSnapshotInfo.getUsableDiskSpaceSize()/1000000000)+" GB");

        pieChart = new PieChart(250,150);
        String[] pieColorArray = new String[]{"#168eea","#323b43"};
        pieChart.setColor(pieColorArray);
        pieChart.setCenter(50,40);
        pieChart.setRadius(50);
        pieChart.setDate(new String[]{"领域可用磁盘空间","系统已用磁盘空间"},new Double[]{
                Double.valueOf(systemStatusSnapshotInfo.getUsableDiskSpaceSize()/1000000000),
                Double.valueOf((systemStatusSnapshotInfo.getTotalDiskSpaceSize()-systemStatusSnapshotInfo.getFreeDiskSpaceSize())/1000000000)
                });
        diskSpaceInfoLayout.add(pieChart);
    }

    public void refreshSystemRuntimeInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        SystemStatusSnapshotInfo systemStatusSnapshotInfo = systemMaintenanceOperator.getSystemStatusSnapshot();
        realmStartDateDisplayItem.updateDisplayValue(systemStatusSnapshotInfo.getSystemStartupTime().toString());
        infoSampleDateDisplayItem.updateDisplayValue(new Date(systemStatusSnapshotInfo.getSnapshotTookTime()).toString());
        totalRequestDisplayItem.updateDisplayValue(""+systemStatusSnapshotInfo.getTotalAcceptedRequestCount());
        peakRequestDisplayItem.updateDisplayValue(""+systemStatusSnapshotInfo.getPeakRequestCount());
        currentRequestDisplayItem .updateDisplayValue(""+systemStatusSnapshotInfo.getCurrentAcceptedRequestCount());
        totalDiskDisplayItem .updateDisplayValue(""+(systemStatusSnapshotInfo.getTotalDiskSpaceSize()/1000000000)+" GB");
        freeDiskDisplayItem .updateDisplayValue(""+(systemStatusSnapshotInfo.getFreeDiskSpaceSize()/1000000000)+" GB");
        freeDiskPercentDisplayItem .updateDisplayValue(nt.format(systemStatusSnapshotInfo.getFreeDiskPercent()));
        usableDiskDisplayItem.updateDisplayValue(""+(systemStatusSnapshotInfo.getUsableDiskSpaceSize()/1000000000)+" GB");
        pieChart.setDate(new String[]{"领域可用磁盘空间","系统已用磁盘空间"},new Double[]{
                Double.valueOf(systemStatusSnapshotInfo.getUsableDiskSpaceSize()/1000000000),
                Double.valueOf((systemStatusSnapshotInfo.getTotalDiskSpaceSize()-systemStatusSnapshotInfo.getFreeDiskSpaceSize())/1000000000)
        });
    }
}
