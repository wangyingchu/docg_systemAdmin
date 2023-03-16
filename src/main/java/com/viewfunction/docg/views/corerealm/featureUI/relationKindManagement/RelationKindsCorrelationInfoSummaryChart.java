package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RuntimeRelationAndConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;

import elemental.json.Json;
import elemental.json.JsonArray;

import java.util.*;

public class RelationKindsCorrelationInfoSummaryChart extends VerticalLayout {

    private CartesianHeatmapChart inDegreeCartesianHeatmapChart;
    private CartesianHeatmapChart outDegreeCartesianHeatmapChart;

    public RelationKindsCorrelationInfoSummaryChart(int windowWidth, int windowHeight){
        int cartesianHeatmapChartWidth = windowWidth/2 -50;
        HorizontalLayout heatMapsContainerLayout = new HorizontalLayout();

        inDegreeCartesianHeatmapChart = new CartesianHeatmapChart(cartesianHeatmapChartWidth,windowHeight-100);
        inDegreeCartesianHeatmapChart.setColorRange("#ABDCFF","#0396FF");
        inDegreeCartesianHeatmapChart.setName("领域概念与关系实体入度统计");
        inDegreeCartesianHeatmapChart.setTooltipPosition("right");
        inDegreeCartesianHeatmapChart.setXAxisLabelRotateDegree(45);
        inDegreeCartesianHeatmapChart.setTopMargin(40);
        inDegreeCartesianHeatmapChart.setLeftMargin(50);
        inDegreeCartesianHeatmapChart.displayYAxisLabelInside(true);
        inDegreeCartesianHeatmapChart.setXAxisLabelFontSize(9);
        inDegreeCartesianHeatmapChart.setYAxisLabelFontSize(14);
        heatMapsContainerLayout.add(inDegreeCartesianHeatmapChart);

        outDegreeCartesianHeatmapChart = new CartesianHeatmapChart(cartesianHeatmapChartWidth,windowHeight-100);
        outDegreeCartesianHeatmapChart.setColorRange("#FCCF31","#F55555");
        outDegreeCartesianHeatmapChart.setName("领域概念与关系实体出度统计");
        outDegreeCartesianHeatmapChart.setTooltipPosition("left");
        outDegreeCartesianHeatmapChart.setXAxisLabelRotateDegree(45);
        outDegreeCartesianHeatmapChart.setYAxisPosition("right");
        outDegreeCartesianHeatmapChart.setTopMargin(40);
        outDegreeCartesianHeatmapChart.setLeftMargin(50);
        outDegreeCartesianHeatmapChart.setRightMargin(20);
        outDegreeCartesianHeatmapChart.displayYAxisLabelInside(true);
        outDegreeCartesianHeatmapChart.setXAxisLabelFontSize(9);
        outDegreeCartesianHeatmapChart.setYAxisLabelFontSize(14);
        heatMapsContainerLayout.add(outDegreeCartesianHeatmapChart);
        add(heatMapsContainerLayout);
    }

    private void loadInDegreeChartData(){
        String[] conceptionKindsLabel_x;
        String[] relationKindsLabel_y;
        Map<String,Integer> conceptionKindIndexMap = new HashMap<>();
        Map<String,Integer> relationKindIndexMap = new HashMap<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        JsonArray inDegreeDataArray = Json.createArray();
        List<Long> totalCountList = new ArrayList<>();

        long inDegreeMaxRelationCount = 0;
        try {
            List<EntityStatisticsInfo> conceptionEntityStatisticsInfoList = filterSystemConceptionKinds(coreRealm.getConceptionEntitiesStatistics());
            conceptionKindsLabel_x = new String[conceptionEntityStatisticsInfoList.size()];
            for(int i =0 ;i<conceptionEntityStatisticsInfoList.size();i++){
                String conceptionKindName = conceptionEntityStatisticsInfoList.get(i).getEntityKindName();
                conceptionKindIndexMap.put(conceptionKindName,i);
                conceptionKindsLabel_x[i]=conceptionKindName;
            }

            List<EntityStatisticsInfo> relationEntityStatisticsInfoList = filterSystemRelationKinds(coreRealm.getRelationEntitiesStatistics());
            relationKindsLabel_y = new String[relationEntityStatisticsInfoList.size()];
            for(int i =0 ;i<relationEntityStatisticsInfoList.size();i++){
                String relationKindName =  relationEntityStatisticsInfoList.get(i).getEntityKindName();
                relationKindIndexMap.put(relationKindName,i);
                relationKindsLabel_y[i] = relationKindName;
            }

            int inDegreeDataArrayIdx = 0;

            SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
            DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
            List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();

            for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
                RelationDirection relationDirection = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationDirection();
                String conceptionKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
                String relationKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
                long relationEntityCount = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();

                if(conceptionKindIndexMap.get(conceptionKindName) != null && relationKindIndexMap.get(relationKindName) != null){
                    JsonArray dataArray = Json.createArray();
                    dataArray.set(0,relationKindIndexMap.get(relationKindName));
                    dataArray.set(1,conceptionKindIndexMap.get(conceptionKindName));
                    dataArray.set(2,relationEntityCount);
                    totalCountList.add(relationEntityCount);

                    switch (relationDirection){
                        case TO -> {
                            inDegreeDataArray.set(inDegreeDataArrayIdx,dataArray);
                            inDegreeDataArrayIdx++;
                            if(relationEntityCount > inDegreeMaxRelationCount){
                                inDegreeMaxRelationCount = relationEntityCount;
                            }
                        }
                    }
                }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        inDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        inDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        int maxValue = (int)(inDegreeMaxRelationCount - median(totalCountList))/3;
        inDegreeCartesianHeatmapChart.setMaxMapValue(maxValue);
        inDegreeCartesianHeatmapChart.setMinMapValue((int)min(totalCountList));
        inDegreeCartesianHeatmapChart.setData(inDegreeDataArray);
    }

    private void loadOutDegreeChartData(){
        String[] conceptionKindsLabel_x;
        String[] relationKindsLabel_y;
        Map<String,Integer> conceptionKindIndexMap = new HashMap<>();
        Map<String,Integer> relationKindIndexMap = new HashMap<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        JsonArray outDegreeDataArray = Json.createArray();
        List<Long> totalCountList = new ArrayList<>();

        long outDegreeMaxRelationCount = 0;
        try {
            List<EntityStatisticsInfo> conceptionEntityStatisticsInfoList = filterSystemConceptionKinds(coreRealm.getConceptionEntitiesStatistics());
            conceptionKindsLabel_x = new String[conceptionEntityStatisticsInfoList.size()];
            for(int i =0 ;i<conceptionEntityStatisticsInfoList.size();i++){
                String conceptionKindName = conceptionEntityStatisticsInfoList.get(i).getEntityKindName();
                conceptionKindIndexMap.put(conceptionKindName,i);
                conceptionKindsLabel_x[i]=conceptionKindName;
            }

            List<EntityStatisticsInfo> relationEntityStatisticsInfoList = filterSystemRelationKinds(coreRealm.getRelationEntitiesStatistics());
            relationKindsLabel_y = new String[relationEntityStatisticsInfoList.size()];
            for(int i =0 ;i<relationEntityStatisticsInfoList.size();i++){
                String relationKindName =  relationEntityStatisticsInfoList.get(i).getEntityKindName();
                relationKindIndexMap.put(relationKindName,i);
                relationKindsLabel_y[i] = relationKindName;
            }

            int outDegreeDataArrayIdx = 0;

            SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
            DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
            List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();

            for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
                RelationDirection relationDirection = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationDirection();
                String conceptionKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
                String relationKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
                long relationEntityCount = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();

                if(conceptionKindIndexMap.get(conceptionKindName) != null && relationKindIndexMap.get(relationKindName) != null){
                    JsonArray dataArray = Json.createArray();
                    dataArray.set(0,relationKindIndexMap.get(relationKindName));
                    dataArray.set(1,conceptionKindIndexMap.get(conceptionKindName));
                    dataArray.set(2,relationEntityCount);
                    totalCountList.add(relationEntityCount);
                    switch (relationDirection){
                        case FROM -> {
                            outDegreeDataArray.set(outDegreeDataArrayIdx,dataArray);
                            outDegreeDataArrayIdx++;
                            if(relationEntityCount > outDegreeMaxRelationCount){
                                outDegreeMaxRelationCount = relationEntityCount;
                            }
                        }
                    }
                }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        outDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        outDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        int maxValue = (int)(outDegreeMaxRelationCount - median(totalCountList))/3;
        outDegreeCartesianHeatmapChart.setMaxMapValue(maxValue);
        outDegreeCartesianHeatmapChart.setMinMapValue((int)min(totalCountList));
        outDegreeCartesianHeatmapChart.setData(outDegreeDataArray);
    }

    private static double median(List<Long> total) {
        /*获取数组中位数*/
        double j = 0;
        //集合排序
        Collections.sort(total);
        int size = total.size();
        if(size % 2 == 1){
            j = total.get((size-1)/2);
        }else {
            //加0.0是为了把int转成double类型，否则除以2会算错
            j = (total.get(size/2-1) + total.get(size/2) + 0.0)/2;
        }
        return j;
    }

    private static long min(List<Long> total) {
        /*获取数组最小数*/
        double j = 0;
        //集合排序
        Collections.sort(total);
        return total.get(0);
    }

    private List<EntityStatisticsInfo> filterSystemConceptionKinds(List<EntityStatisticsInfo> entityStatisticsInfo){
        if(entityStatisticsInfo != null){
            ArrayList<EntityStatisticsInfo> resultList = new ArrayList<>();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfo){
                if(currentEntityStatisticsInfo.getEntityKindName().startsWith(RealmConstant.RealmInnerTypePerFix)){
                    if(currentEntityStatisticsInfo.getEntityKindName().equals(RealmConstant.TimeScaleEventClass)){
                        resultList.add(currentEntityStatisticsInfo);
                    }
                    if(currentEntityStatisticsInfo.getEntityKindName().equals(RealmConstant.GeospatialScaleEventClass)){
                        resultList.add(currentEntityStatisticsInfo);
                    }
                }else{
                    resultList.add(currentEntityStatisticsInfo);
                }
            }
            return resultList;
        }
        return null;
    }

    private List<EntityStatisticsInfo> filterSystemRelationKinds(List<EntityStatisticsInfo> entityStatisticsInfo){
        if(entityStatisticsInfo != null){
            ArrayList<EntityStatisticsInfo> resultList = new ArrayList<>();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfo){
                if(currentEntityStatisticsInfo.getEntityKindName().startsWith(RealmConstant.RealmInnerTypePerFix)){
                    if(currentEntityStatisticsInfo.getEntityKindName().equals(RealmConstant.TimeScale_TimeReferToRelationClass)){
                        resultList.add(currentEntityStatisticsInfo);
                    }
                    if(currentEntityStatisticsInfo.getEntityKindName().equals(RealmConstant.GeospatialScale_GeospatialReferToRelationClass)){
                        resultList.add(currentEntityStatisticsInfo);
                    }
                }else{
                    resultList.add(currentEntityStatisticsInfo);
                }
            }
            return resultList;
        }
        return null;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadInDegreeChartData();
        loadOutDegreeChartData();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
