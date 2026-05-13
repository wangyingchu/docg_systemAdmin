package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SystemStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.PieChart;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    private SecondaryKeyValueDisplayItem conceptionEntitiesDiskSpaceSizeDisplayItem;
    private SecondaryKeyValueDisplayItem relationEntitiesDiskSpaceSizeDisplayItem;
    private SecondaryKeyValueDisplayItem attributesDiskSpaceSizeDisplayItem;
    private SecondaryKeyValueDisplayItem coreRealmDiskSpaceSizeDisplayItem;

    private NumberFormat nt;
    private PieChart pieChart;
    final ZoneId id = ZoneId.systemDefault();
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
        new SecondaryKeyValueDisplayItem(statusInfoContainer1, VaadinIcon.GOLF.create(),"领域创建时间:",
                systemStatusSnapshotInfo.getSystemCreateTime().toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));

        HorizontalLayout statusInfoContainer2 = new HorizontalLayout();
        statusInfoContainer2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer2);
        realmStartDateDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer2, VaadinIcon.FLIGHT_TAKEOFF.create(),"领域启动时间:",
                systemStatusSnapshotInfo.getSystemStartupTime().toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));

        HorizontalLayout statusInfoContainer3 = new HorizontalLayout();
        statusInfoContainer3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer3);
        infoSampleDateDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer3, VaadinIcon.CAMERA.create(),"指标采样时间:",
                new Date(systemStatusSnapshotInfo.getSnapshotTookTime()).toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        add(spaceDivLayout1);

        HorizontalLayout statusInfoContainer4 = new HorizontalLayout();
        statusInfoContainer4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer4);
        totalRequestDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer4, FontAwesome.Solid.CALCULATOR.create(),"领域服务请求总量:",""+systemStatusSnapshotInfo.getTotalAcceptedRequestCount());

        HorizontalLayout statusInfoContainer5 = new HorizontalLayout();
        statusInfoContainer5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer5);
        peakRequestDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer5, FontAwesome.Solid.HEART_PULSE.create(),"领域服务请求峰值:",""+systemStatusSnapshotInfo.getPeakRequestCount());

        HorizontalLayout statusInfoContainer6 = new HorizontalLayout();
        statusInfoContainer6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer6);
        currentRequestDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer6, FontAwesome.Solid.SERVER.create(),"当前领域服务请求量:",""+systemStatusSnapshotInfo.getCurrentAcceptedRequestCount());

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
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

        HorizontalLayout statusInfoContainer11 = new HorizontalLayout();
        statusInfoContainer11.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer11);
        String conceptionEntitiesDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getConceptionEntitiesDiskSpaceSize()<1000000000){
            conceptionEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getConceptionEntitiesDiskSpaceSize()/1000000)+" MB";
        }else{
            conceptionEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getConceptionEntitiesDiskSpaceSize()/1000000000)+" GB";
        }
        conceptionEntitiesDiskSpaceSizeDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer11, VaadinIcon.CUBE.create(),"概念实体占用磁盘空间:",conceptionEntitiesDiskSpaceSizeText);

        HorizontalLayout statusInfoContainer12 = new HorizontalLayout();
        statusInfoContainer12.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer12);
        String relationEntitiesDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getRelationEntitiesDiskSpaceSize()<1000000000){
            relationEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getRelationEntitiesDiskSpaceSize()/1000000)+" MB";
        }else{
            relationEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getRelationEntitiesDiskSpaceSize()/1000000000)+" GB";
        }
        relationEntitiesDiskSpaceSizeDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer12, VaadinIcon.CONNECT_O.create(),"关系实体占用磁盘空间:",relationEntitiesDiskSpaceSizeText);

        HorizontalLayout statusInfoContainer13 = new HorizontalLayout();
        statusInfoContainer13.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer13);
        String attributesDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getAttributesDiskSpaceSize()<1000000000){
            attributesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getAttributesDiskSpaceSize()/1000000)+" MB";
        }else{
            attributesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getAttributesDiskSpaceSize()/1000000000)+" GB";
        }
        attributesDiskSpaceSizeDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer13, VaadinIcon.INPUT.create(),"属性值占用磁盘空间:",attributesDiskSpaceSizeText);

        HorizontalLayout statusInfoContainer14 = new HorizontalLayout();
        statusInfoContainer14.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        diskInfoLeftLayout.add(statusInfoContainer14);
        String coreRealmUsedDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getCoreRealmUsedDiskSpaceSize()<1000000000){
            coreRealmUsedDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getCoreRealmUsedDiskSpaceSize()/1000000)+" MB";
        }else{
            coreRealmUsedDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getCoreRealmUsedDiskSpaceSize()/1000000000)+" GB";
        }
        coreRealmDiskSpaceSizeDisplayItem = new SecondaryKeyValueDisplayItem(statusInfoContainer14, LineAwesomeIconsSvg.CONNECTDEVELOP.create(),"领域占用总磁盘空间:",coreRealmUsedDiskSpaceSizeText);

        pieChart = new PieChart(250,145);
        String[] pieColorArray = new String[]{"#168eea","#323b43"};
        pieChart.setColor(pieColorArray);
        pieChart.setCenter(50,50);
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
        realmStartDateDisplayItem.updateDisplayValue(systemStatusSnapshotInfo.getSystemStartupTime().
                toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
        infoSampleDateDisplayItem.updateDisplayValue(new Date(systemStatusSnapshotInfo.getSnapshotTookTime()).
                toInstant().atZone(id).format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
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

        String conceptionEntitiesDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getConceptionEntitiesDiskSpaceSize()<1000000000){
            conceptionEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getConceptionEntitiesDiskSpaceSize()/1000000)+" MB";
        }else{
            conceptionEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getConceptionEntitiesDiskSpaceSize()/1000000000)+" GB";
        }
        conceptionEntitiesDiskSpaceSizeDisplayItem.updateDisplayValue(conceptionEntitiesDiskSpaceSizeText);

        String relationEntitiesDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getRelationEntitiesDiskSpaceSize()<1000000000){
            relationEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getRelationEntitiesDiskSpaceSize()/1000000)+" MB";
        }else{
            relationEntitiesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getRelationEntitiesDiskSpaceSize()/1000000000)+" GB";
        }
        relationEntitiesDiskSpaceSizeDisplayItem.updateDisplayValue(relationEntitiesDiskSpaceSizeText);

        String attributesDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getAttributesDiskSpaceSize()<1000000000){
            attributesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getAttributesDiskSpaceSize()/1000000)+" MB";
        }else{
            attributesDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getAttributesDiskSpaceSize()/1000000000)+" GB";
        }
        attributesDiskSpaceSizeDisplayItem.updateDisplayValue(attributesDiskSpaceSizeText);

        String coreRealmUsedDiskSpaceSizeText = "-";
        if(systemStatusSnapshotInfo.getCoreRealmUsedDiskSpaceSize()<1000000000){
            coreRealmUsedDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getCoreRealmUsedDiskSpaceSize()/1000000)+" MB";
        }else{
            coreRealmUsedDiskSpaceSizeText = ""+(systemStatusSnapshotInfo.getCoreRealmUsedDiskSpaceSize()/1000000000)+" GB";
        }
        coreRealmDiskSpaceSizeDisplayItem.updateDisplayValue(coreRealmUsedDiskSpaceSizeText);
    }
}
