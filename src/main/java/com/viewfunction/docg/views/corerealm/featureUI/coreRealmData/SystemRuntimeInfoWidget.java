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

import java.util.Date;

public class SystemRuntimeInfoWidget extends VerticalLayout {

    public SystemRuntimeInfoWidget(){
        this.setWidthFull();

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
        new SecondaryKeyValueDisplayItem(statusInfoContainer2, VaadinIcon.FLIGHT_TAKEOFF.create(),"系统启动时间:",systemStatusSnapshotInfo.getSystemStartupTime().toString());

        HorizontalLayout statusInfoContainer3 = new HorizontalLayout();
        statusInfoContainer3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer3);
        new SecondaryKeyValueDisplayItem(statusInfoContainer3, VaadinIcon.CAMERA.create(),"指标采样时间:",new Date(systemStatusSnapshotInfo.getSnapshotTookTime()).toString());

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(6, Unit.PIXELS);
        add(spaceDivLayout1);

        HorizontalLayout statusInfoContainer4 = new HorizontalLayout();
        statusInfoContainer4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer4);
        new SecondaryKeyValueDisplayItem(statusInfoContainer4, VaadinIcon.AREA_SELECT.create(),"系统服务请求总量:",""+systemStatusSnapshotInfo.getTotalAcceptedRequestCount());

        HorizontalLayout statusInfoContainer5 = new HorizontalLayout();
        statusInfoContainer5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer5);
        new SecondaryKeyValueDisplayItem(statusInfoContainer5, VaadinIcon.AREA_SELECT.create(),"系统服务请求峰值:",""+systemStatusSnapshotInfo.getPeakRequestCount());

        HorizontalLayout statusInfoContainer6 = new HorizontalLayout();
        statusInfoContainer6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer6);
        new SecondaryKeyValueDisplayItem(statusInfoContainer6, VaadinIcon.AREA_SELECT.create(),"当前系统服务请求量:",""+systemStatusSnapshotInfo.getCurrentAcceptedRequestCount());

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(6, Unit.PIXELS);
        add(spaceDivLayout2);

        HorizontalLayout statusInfoContainer7 = new HorizontalLayout();
        statusInfoContainer7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer7);
        new SecondaryKeyValueDisplayItem(statusInfoContainer7, VaadinIcon.DASHBOARD.create(),"系统磁盘空间总量:",""+systemStatusSnapshotInfo.getTotalDiskSpaceSize());

        HorizontalLayout statusInfoContainer8 = new HorizontalLayout();
        statusInfoContainer8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer8);
        new SecondaryKeyValueDisplayItem(statusInfoContainer8, VaadinIcon.HARDDRIVE.create(),"系统未用磁盘空间总量:",""+systemStatusSnapshotInfo.getFreeDiskSpaceSize());

        HorizontalLayout statusInfoContainer9 = new HorizontalLayout();
        statusInfoContainer9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer9);
        new SecondaryKeyValueDisplayItem(statusInfoContainer9, VaadinIcon.HARDDRIVE_O.create(),"系统可用磁盘空间总量:",""+systemStatusSnapshotInfo.getUsableDiskSpaceSize());

        HorizontalLayout statusInfoContainer10 = new HorizontalLayout();
        statusInfoContainer10.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(statusInfoContainer10);
        new SecondaryKeyValueDisplayItem(statusInfoContainer10, VaadinIcon.PIE_CHART.create(),"系统未用磁盘空间占比:",""+systemStatusSnapshotInfo.getFreeDiskPercent());
    }
}
