package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RuntimeRelationAndConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;

import java.util.List;

public class RelationAndConceptionKindAttachInfoWidget extends VerticalLayout {

    public RelationAndConceptionKindAttachInfoWidget(){


        String[] conceptionKindsLabel_x = new String[]{"ABCDEF", "1avdvdvd", "2avdvddd", "3avddddd", "4a", "5a", "6a",
                "7a"," '8a'", "9a","10a","11a","12p", "1p", "2p", "3p", "4p", "5p","6p", "7p", "8p", "9p", "10p", "GHIJK"};
        String[] relationKindsLabel_y = new String[]{"A_Saturdayvddvdd", "Fridayvdvddd", "Thursdayvdvdvd",
                "Wednesdaydvdvddd", "Tuesdayvdvdd", "Mondayvdvdd", "SundayB_"};

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMPRESS_SQUARE),"概念与关系实体入度统计");
        add(infoTitle1);
        CartesianHeatmapChart cartesianHeatmapChart1 = new CartesianHeatmapChart(380,280);
        cartesianHeatmapChart1.setColorRange("WhiteSmoke","#168eea");
        cartesianHeatmapChart1.setName("领域概念与关系实体入度统计");
        cartesianHeatmapChart1.hideLabels();
        cartesianHeatmapChart1.setXAxisLabel(conceptionKindsLabel_x);
        cartesianHeatmapChart1.setYAxisLabel(relationKindsLabel_y);
        cartesianHeatmapChart1.setData();
        add(cartesianHeatmapChart1);

        HorizontalLayout spaceDiv01 = new HorizontalLayout();
        spaceDiv01.setHeight(5, Unit.PIXELS);
        add(spaceDiv01);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXPAND_SQUARE),"概念与关系实体出度统计");
        add(infoTitle2);
        CartesianHeatmapChart cartesianHeatmapChart2 = new CartesianHeatmapChart(380,280);
        cartesianHeatmapChart2.setColorRange("WhiteSmoke","#323b43");
        cartesianHeatmapChart2.setName("领域概念与关系实体出度统计");
        cartesianHeatmapChart2.hideLabels();
        cartesianHeatmapChart2.setData();
        add(cartesianHeatmapChart2);
    }

    public void refreshRelationAndConceptionKindAttachInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
        List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();
        for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
            currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
            currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
            currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();
        }
    }
}
