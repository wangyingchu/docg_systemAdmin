package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RuntimeRelationAndConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;
import elemental.json.Json;
import elemental.json.JsonArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationAndConceptionKindAttachInfoWidget extends VerticalLayout {

    private Map<String,Integer> conceptionKindIndexMap;
    private Map<String,Integer> relationKindIndexMap;
    private String[] conceptionKindsLabel_x;
    private String[] relationKindsLabel_y;
    private CartesianHeatmapChart inDegreeCartesianHeatmapChart;
    private CartesianHeatmapChart outDegreeCartesianHeatmapChart;

    public RelationAndConceptionKindAttachInfoWidget(){
        conceptionKindIndexMap = new HashMap<>();
        relationKindIndexMap = new HashMap<>();

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMPRESS_SQUARE),"概念与关系实体入度统计");
        add(infoTitle1);
        inDegreeCartesianHeatmapChart = new CartesianHeatmapChart(380,280);
        inDegreeCartesianHeatmapChart.setColorRange("WhiteSmoke","#168eea");
        inDegreeCartesianHeatmapChart.setName("领域概念与关系实体入度统计");
        inDegreeCartesianHeatmapChart.hideLabels();
        add(inDegreeCartesianHeatmapChart);

        HorizontalLayout spaceDiv01 = new HorizontalLayout();
        spaceDiv01.setHeight(5, Unit.PIXELS);
        add(spaceDiv01);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXPAND_SQUARE),"概念与关系实体出度统计");
        add(infoTitle2);
        outDegreeCartesianHeatmapChart = new CartesianHeatmapChart(380,280);
        outDegreeCartesianHeatmapChart.setColorRange("WhiteSmoke","#323b43");
        outDegreeCartesianHeatmapChart.setName("领域概念与关系实体出度统计");
        outDegreeCartesianHeatmapChart.hideLabels();
        add(outDegreeCartesianHeatmapChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderRelationAndConceptionKindAttachInfo();
    }

    public void renderRelationAndConceptionKindAttachInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        JsonArray outDegreeDataArray = Json.createArray();
        JsonArray inDegreeDataArray = Json.createArray();
        try {
            List<EntityStatisticsInfo> conceptionEntityStatisticsInfoList = coreRealm.getConceptionEntitiesStatistics();
            conceptionKindsLabel_x = new String[conceptionEntityStatisticsInfoList.size()];
            for(int i =0 ;i<conceptionEntityStatisticsInfoList.size();i++){
                String conceptionKindName = conceptionEntityStatisticsInfoList.get(i).getEntityKindName();
                conceptionKindIndexMap.put(conceptionKindName,i);
                conceptionKindsLabel_x[i]=conceptionKindName;
            }

            List<EntityStatisticsInfo> relationEntityStatisticsInfoList = coreRealm.getRelationEntitiesStatistics();
            relationKindsLabel_y = new String[relationEntityStatisticsInfoList.size()];
            for(int i =0 ;i<relationEntityStatisticsInfoList.size();i++){
                String relationKindName =  relationEntityStatisticsInfoList.get(i).getEntityKindName();
                relationKindIndexMap.put(relationKindName,i);
                relationKindsLabel_y[i] = relationKindName;
            }
            SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
            DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
            List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();

            int inDegreeDataArrayIdx = 0;
            int outDegreeDataArrayIdx = 0;

            for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
                RelationDirection relationDirection = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationDirection();
                String conceptionKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
                String relationKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
                long relationEntityCount = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();

                if(conceptionKindIndexMap.get(conceptionKindName) != null && relationKindIndexMap.get(relationKindName) != null){
                    JsonArray dataArray = Json.createArray();
                    dataArray.set(0,conceptionKindIndexMap.get(conceptionKindName));
                    dataArray.set(1,relationKindIndexMap.get(relationKindName));
                    dataArray.set(2,relationEntityCount);

                    switch (relationDirection){
                        case FROM -> {
                            outDegreeDataArray.set(outDegreeDataArrayIdx,dataArray);
                            outDegreeDataArrayIdx++;
                            break;
                        }
                        case TO -> {
                            inDegreeDataArray.set(inDegreeDataArrayIdx,dataArray);
                            inDegreeDataArrayIdx++;
                        }
                    }
                }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        inDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        inDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        inDegreeCartesianHeatmapChart.setData(inDegreeDataArray);

        outDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        outDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        outDegreeCartesianHeatmapChart.setData(outDegreeDataArray);
    }


    public void refreshRelationAndConceptionKindAttachInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();

    }
}
