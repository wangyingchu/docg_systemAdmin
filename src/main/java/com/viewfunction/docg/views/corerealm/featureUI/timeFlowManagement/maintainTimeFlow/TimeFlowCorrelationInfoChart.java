package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleRelationsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.io.Serializable;
import java.util.*;

@StyleSheet("webApps/timeFlowCorrelationInfoChart/style.css")
@JavaScript("./visualization/feature/timeFlowCorrelationInfoChart-connector.js")
public class TimeFlowCorrelationInfoChart extends VerticalLayout {

    private Map<String,String> conceptionKindColorMap;
    private int graphHeight;
    private int graphWidth;
    private List<String> allTimeScaleEntityUIDList;
    private List<String> allTimeEntitiesRelationUIDList;
    private boolean isFirstLoad = true;
    private String timeFlowName;
    private List<String> timeFlowEntityExpendedList;
    private int timeScaleRelationsRetrieveBatchSize = 5;
    private Map<String,Integer> timeScaleEntityRelationsRetrieveIndexMap;
    private TimeFlowCorrelationExploreView containerTimeFlowCorrelationExploreView;

    public TimeFlowCorrelationInfoChart(String timeFlowName){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.timeFlowName = timeFlowName;
        this.conceptionKindColorMap = new HashMap<>();
        this.conceptionKindColorMap.put("YEAR","#3D9970");
        this.conceptionKindColorMap.put("MONTH","#FF851B");
        this.conceptionKindColorMap.put("DAY","#0074D9");
        this.conceptionKindColorMap.put("HOUR","#FF4136");
        this.conceptionKindColorMap.put("MINUTE","#7FDBFF");

        this.allTimeScaleEntityUIDList = new ArrayList<>();
        this.allTimeEntitiesRelationUIDList = new ArrayList<>();
        this.timeFlowEntityExpendedList = new ArrayList<>();
        this.timeScaleEntityRelationsRetrieveIndexMap = new HashMap<>();
        //link to download latest 3d-force-graph build js: https://unpkg.com/3d-force-graph
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.3/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.3/dist/three-spritetext.min.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.3/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.3/dist/3d-force-graph.min.js");
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_TimeFlowCorrelationInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.emptyGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString("")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        super.onDetach(detachEvent);
    }

    public void renderTimeFlowCorrelationData(List<TimeScaleEntity> timeScaleEntityList){
        if(timeScaleEntityList != null){
            allTimeScaleEntityUIDList.clear();
            allTimeEntitiesRelationUIDList.clear();
            if(timeScaleEntityList.size() > 0){
                for(TimeScaleEntity currentTimeScaleEntity:timeScaleEntityList){
                    allTimeScaleEntityUIDList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
                }
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
                try {
                    List<RelationEntity> timeEntitiesPairRelationList = allTimeScaleEntityUIDList.size() > 1 ?
                            crossKindDataOperator.getRelationsOfConceptionEntityPair(allTimeScaleEntityUIDList) : new ArrayList<>();
                    for(RelationEntity currentRelationEntity : timeEntitiesPairRelationList){
                        allTimeEntitiesRelationUIDList.add(currentRelationEntity.getRelationEntityUID());
                    }
                    if(isFirstLoad){
                        generateGraph(timeScaleEntityList, timeEntitiesPairRelationList);
                        isFirstLoad = false;
                    }else{
                        emptyGraph();
                        insertInGenerateGraph(timeScaleEntityList,timeEntitiesPairRelationList);
                    }
                } catch (CoreRealmServiceEntityExploreException e) {
                    throw new RuntimeException(e);
                }
            }else{
                emptyGraph();
            }
        }
    }

    public void renderMoreTimeFlowCorrelationData(List<TimeScaleEntity> timeScaleEntityList){
        if(timeScaleEntityList != null && timeScaleEntityList.size() > 0){
            for(TimeScaleEntity currentTimeScaleEntity:timeScaleEntityList){
                allTimeScaleEntityUIDList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
            }
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            try {
                List<RelationEntity> timeEntitiesRelationList = crossKindDataOperator.getRelationsOfConceptionEntityPair(allTimeScaleEntityUIDList);
                List<RelationEntity> needAddTimeEntitiesRelationList = new ArrayList<>();
                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    if(!allTimeEntitiesRelationUIDList.contains(currentRelationEntity.getRelationEntityUID())){
                        if(allTimeScaleEntityUIDList.contains(currentRelationEntity.getFromConceptionEntityUID()) &&
                               allTimeScaleEntityUIDList.contains(currentRelationEntity.getToConceptionEntityUID())) {
                           needAddTimeEntitiesRelationList.add(currentRelationEntity);
                           allTimeEntitiesRelationUIDList.add(currentRelationEntity.getRelationEntityUID());
                       }
                    }
                }
                insertInGenerateGraph(timeScaleEntityList,needAddTimeEntitiesRelationList);
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void emptyGraph(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.emptyGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString("")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        this.timeFlowEntityExpendedList.clear();
    }

    private void generateGraph(List<TimeScaleEntity> timeScaleEntityList,List<RelationEntity> timeEntitiesRelationList){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Object> valueMap =new HashMap<>();
                List<Map<String,String>> nodeInfoList = new ArrayList<>();

                for(TimeScaleEntity currentConceptionEntity:timeScaleEntityList){
                    Map<String,String> currentNodeInfo = new HashMap<>();
                    currentNodeInfo.put("id",currentConceptionEntity.getTimeScaleEntityUID());
                    currentNodeInfo.put("entityKind",currentConceptionEntity.getTimeScaleGrade().toString());
                    currentNodeInfo.put("entityDesc",currentConceptionEntity.getTimeScaleEntityDesc());
                    switch(currentConceptionEntity.getTimeScaleGrade()){
                        case YEAR -> currentNodeInfo.put("size","60");
                        case MONTH -> currentNodeInfo.put("size","40");
                        case DAY -> currentNodeInfo.put("size","30");
                        case HOUR -> currentNodeInfo.put("size","20");
                        case MINUTE -> currentNodeInfo.put("size","12");
                    }
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionEntity.getTimeScaleGrade().toString())!=null){
                        currentNodeInfo.put("color",this.conceptionKindColorMap.get(currentConceptionEntity.getTimeScaleGrade().toString()));
                    }else{
                        currentNodeInfo.put("color","#0099FF");
                    }
                    nodeInfoList.add(currentNodeInfo);
                }

                List<Map<String,String>> edgeInfoList = new ArrayList<>();

                List<String> attachedRelationKinds = new ArrayList<>();
                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    if(!attachedRelationKinds.contains(currentRelationEntity.getRelationKindName())){
                        attachedRelationKinds.add(currentRelationEntity.getRelationKindName());
                    }
                }

                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    Map<String,String> currentEdgeInfo = new HashMap<>();
                    currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                    currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                    currentEdgeInfo.put("entityKind",currentRelationEntity.getRelationKindName());
                    currentEdgeInfo.put("color","#AAAAAA");
                    edgeInfoList.add(currentEdgeInfo);
                }

                valueMap.put("graphHeight",graphHeight);
                valueMap.put("graphWidth",graphWidth);
                valueMap.put("nodesInfo",nodeInfoList);
                valueMap.put("edgesInfo",edgeInfoList);
                getElement().callJsFunction("$connector.generateGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString(valueMap)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void insertInGenerateGraph(List<TimeScaleEntity> timeScaleEntityList,List<RelationEntity> timeEntitiesRelationList){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Object> valueMap =new HashMap<>();
                List<Map<String,String>> nodeInfoList = new ArrayList<>();

                for(TimeScaleEntity currentConceptionEntity:timeScaleEntityList){
                    Map<String,String> currentNodeInfo = new HashMap<>();
                    currentNodeInfo.put("id",currentConceptionEntity.getTimeScaleEntityUID());
                    currentNodeInfo.put("entityKind",currentConceptionEntity.getTimeScaleGrade().toString());
                    currentNodeInfo.put("entityDesc",currentConceptionEntity.getTimeScaleEntityDesc());
                    switch(currentConceptionEntity.getTimeScaleGrade()){
                        case YEAR -> currentNodeInfo.put("size","60");
                        case MONTH -> currentNodeInfo.put("size","40");
                        case DAY -> currentNodeInfo.put("size","30");
                        case HOUR -> currentNodeInfo.put("size","20");
                        case MINUTE -> currentNodeInfo.put("size","12");
                    }
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionEntity.getTimeScaleGrade().toString())!=null){
                        currentNodeInfo.put("color",this.conceptionKindColorMap.get(currentConceptionEntity.getTimeScaleGrade().toString()));
                    }else{
                        currentNodeInfo.put("color","#0099FF");
                    }
                    nodeInfoList.add(currentNodeInfo);
                }

                List<Map<String,String>> edgeInfoList = new ArrayList<>();

                List<String> attachedRelationKinds = new ArrayList<>();
                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    if(!attachedRelationKinds.contains(currentRelationEntity.getRelationKindName())){
                        attachedRelationKinds.add(currentRelationEntity.getRelationKindName());
                    }
                }

                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    Map<String,String> currentEdgeInfo = new HashMap<>();
                    currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                    currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                    currentEdgeInfo.put("entityKind",currentRelationEntity.getRelationKindName());
                    currentEdgeInfo.put("color","#AAAAAA");
                    edgeInfoList.add(currentEdgeInfo);
                }

                valueMap.put("graphHeight",graphHeight);
                valueMap.put("graphWidth",graphWidth);
                valueMap.put("nodesInfo",nodeInfoList);
                valueMap.put("edgesInfo",edgeInfoList);
                getElement().callJsFunction("$connector.insertGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString(valueMap)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void insertInGenerateGraph(List<TimeScaleRelationsInfo> timeScaleRelationsInfoList){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Object> valueMap =new HashMap<>();
                List<Map<String,String>> nodeInfoList = new ArrayList<>();
                List<Map<String,String>> edgeInfoList = new ArrayList<>();

                for(TimeScaleRelationsInfo currentTimeScaleRelationsInfo:timeScaleRelationsInfoList){
                    Map<String,String> currentEventNodeInfo = new HashMap<>();
                    currentEventNodeInfo.put("id",currentTimeScaleRelationsInfo.getTimeScaleEventUID());
                    currentEventNodeInfo.put("entityKind","TimeScaleEvent");
                    currentEventNodeInfo.put("entityDesc",currentTimeScaleRelationsInfo.getTimeScaleEventComment());
                    currentEventNodeInfo.put("size","10");
                    currentEventNodeInfo.put("color","#85144B");
                    nodeInfoList.add(currentEventNodeInfo);

                    Map<String,String> currentConceptionEntityNodeInfo = new HashMap<>();
                    currentConceptionEntityNodeInfo.put("id",currentTimeScaleRelationsInfo.getConceptionEntityUID());
                    currentConceptionEntityNodeInfo.put("entityKind",currentTimeScaleRelationsInfo.getConceptionKindName());
                    currentConceptionEntityNodeInfo.put("entityDesc",currentTimeScaleRelationsInfo.getConceptionEntityUID());
                    currentConceptionEntityNodeInfo.put("size","80");
                    currentConceptionEntityNodeInfo.put("color","#39CCCC");
                    nodeInfoList.add(currentConceptionEntityNodeInfo);

                    Map<String,String> currentTimeReferToEdgeInfo = new HashMap<>();
                    currentTimeReferToEdgeInfo.put("source",currentTimeScaleRelationsInfo.getTimeScaleEntityUID());
                    currentTimeReferToEdgeInfo.put("target",currentTimeScaleRelationsInfo.getTimeScaleEventUID());
                    currentTimeReferToEdgeInfo.put("entityKind","TimeReferTo");
                    currentTimeReferToEdgeInfo.put("color","#85144B");
                    edgeInfoList.add(currentTimeReferToEdgeInfo);

                    Map<String,String> currentAttachToTimeScaleEdgeInfo = new HashMap<>();
                    currentAttachToTimeScaleEdgeInfo.put("source",currentTimeScaleRelationsInfo.getConceptionEntityUID());
                    currentAttachToTimeScaleEdgeInfo.put("target",currentTimeScaleRelationsInfo.getTimeScaleEventUID());
                    currentAttachToTimeScaleEdgeInfo.put("entityKind","AttachToTimeScale");
                    currentAttachToTimeScaleEdgeInfo.put("color","#39CCCC");
                    edgeInfoList.add(currentAttachToTimeScaleEdgeInfo);
                }

                valueMap.put("graphHeight",graphHeight);
                valueMap.put("graphWidth",graphWidth);
                valueMap.put("nodesInfo",nodeInfoList);
                valueMap.put("edgesInfo",edgeInfoList);
                getElement().callJsFunction("$connector.insertGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString(valueMap)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(int graphHeight) {
        this.graphHeight = graphHeight;
    }

    public int getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth;
    }

    @ClientCallable
    public void expendTimeFlowEntity(String entityType,String entityUID) {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(this.timeFlowName);
        TimeScaleEntity targetTimeScaleEntity = targetTimeFlow.getTimeScaleEntityByUID(entityUID);
        if(targetTimeScaleEntity != null){
            if(!this.timeFlowEntityExpendedList.contains(entityUID)){
                LinkedList<TimeScaleEntity> childTimeScaleEntityList = targetTimeScaleEntity.getChildEntities();
                List<String> targetRelationCheckEntityList = new ArrayList<>();
                for(TimeScaleEntity currentTimeScaleEntity:childTimeScaleEntityList){
                    targetRelationCheckEntityList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
                }
                targetRelationCheckEntityList.add(entityUID);
                renderMoreTimeFlowCorrelationData(childTimeScaleEntityList);
                this.timeFlowEntityExpendedList.add(entityUID);
            }
            try {
                int currentIndexSize;
                if(timeScaleEntityRelationsRetrieveIndexMap.containsKey(entityUID)){
                    currentIndexSize = timeScaleEntityRelationsRetrieveIndexMap.get(entityUID);
                }else{
                    currentIndexSize = 0;
                }
                List<TimeScaleRelationsInfo> timeScaleRelationsInfoList = targetTimeScaleEntity.sampleSelfAttachedTimeScaleRelationsInfos(timeScaleRelationsRetrieveBatchSize,currentIndexSize);
                if(timeScaleRelationsInfoList.size()>0){
                    timeScaleEntityRelationsRetrieveIndexMap.put(entityUID,currentIndexSize+timeScaleRelationsRetrieveBatchSize);
                    insertInGenerateGraph(timeScaleRelationsInfoList);
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
        coreRealm.closeGlobalSession();
        if(this.containerTimeFlowCorrelationExploreView != null){
            this.containerTimeFlowCorrelationExploreView.hideEntityDetail();
        }
    }

    @ClientCallable
    public void showEntityDetail(String entityType,String entityUID){
        String timeFlowRelatedEntityKind = entityType;
        if(this.containerTimeFlowCorrelationExploreView != null){
            if(entityType.equals("YEAR")){ timeFlowRelatedEntityKind = RealmConstant.TimeScaleYearEntityClass;}
            if(entityType.equals("MONTH")){ timeFlowRelatedEntityKind = RealmConstant.TimeScaleMonthEntityClass;}
            if(entityType.equals("DAY")){ timeFlowRelatedEntityKind = RealmConstant.TimeScaleDayEntityClass;}
            if(entityType.equals("HOUR")){ timeFlowRelatedEntityKind = RealmConstant.TimeScaleHourEntityClass;}
            if(entityType.equals("MINUTE")){ timeFlowRelatedEntityKind = RealmConstant.TimeScaleMinuteEntityClass;}
            if(entityType.equals("TimeScaleEvent")){ timeFlowRelatedEntityKind = RealmConstant.TimeScaleEventClass;}
            this.containerTimeFlowCorrelationExploreView.showEntityDetail(timeFlowRelatedEntityKind,entityUID);
        }
    }

    public void setContainerTimeFlowCorrelationExploreView(TimeFlowCorrelationExploreView containerTimeFlowCorrelationExploreView) {
        this.containerTimeFlowCorrelationExploreView = containerTimeFlowCorrelationExploreView;
    }
}
