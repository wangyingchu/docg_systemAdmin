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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.io.Serializable;
import java.util.*;

@StyleSheet("webApps/timeFlowCorrelationInfoChart/style.css")
@JavaScript("./visualization/feature/timeFlowCorrelationInfoChart-connector.js")
public class TimeFlowCorrelationInfoChart extends VerticalLayout {

    private int colorIndex = 0;
    private int colorIndex2 = 0;
    private Map<String,String> conceptionKindColorMap;
    private Map<String,String> relationKindColorMap;
    private int graphHeight;
    private int graphWidth;
    private List<String> allTimeScaleEntityUIDList;
    private List<String> allTimeEntitiesRelationUIDList;
    private boolean isFirstLoad = true;
    private String timeFlowName;
    private List<String> timeFlowEntityExpendedList;

    public TimeFlowCorrelationInfoChart(String timeFlowName){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.timeFlowName = timeFlowName;
        this.conceptionKindColorMap = new HashMap<>();
        this.conceptionKindColorMap.put("YEAR","#001F3F");
        this.conceptionKindColorMap.put("MONTH","#FF851B");
        this.conceptionKindColorMap.put("DAY","#0074D9");
        this.conceptionKindColorMap.put("HOUR","#FF4136");
        this.conceptionKindColorMap.put("MINUTE","#7FDBFF");

        this.relationKindColorMap = new HashMap<>();
        this.allTimeScaleEntityUIDList = new ArrayList<>();
        this.allTimeEntitiesRelationUIDList = new ArrayList<>();
        this.timeFlowEntityExpendedList = new ArrayList<>();

        //link to download latest 3d-force-graph build js: https://unpkg.com/3d-force-graph
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three-spritetext.min.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/3d-force-graph.min.js");
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
        if(timeScaleEntityList != null && timeScaleEntityList.size() > 0){
            allTimeScaleEntityUIDList.clear();
            allTimeEntitiesRelationUIDList.clear();
            for(TimeScaleEntity currentTimeScaleEntity:timeScaleEntityList){
                allTimeScaleEntityUIDList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
            }
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            try {
                List<RelationEntity> timeEntitiesPairRelationList = crossKindDataOperator.getRelationsOfConceptionEntityPair(allTimeScaleEntityUIDList);
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

                List<String> attachedConceptionKinds = new ArrayList<>();
                for(TimeScaleEntity currentConceptionEntity:timeScaleEntityList){
                    if(!attachedConceptionKinds.contains(currentConceptionEntity.getTimeScaleGrade().toString())){
                        attachedConceptionKinds.add(currentConceptionEntity.getTimeScaleGrade().toString());
                    }
                }
                //generateConceptionKindColorMap(attachedConceptionKinds);

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
                generateRelationKindColorMap(attachedRelationKinds);

                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    Map<String,String> currentEdgeInfo = new HashMap<>();
                    currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                    currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                    currentEdgeInfo.put("entityKind",currentRelationEntity.getRelationKindName());
                    //currentEdgeInfo.put("color",this.relationKindColorMap.get(currentRelationEntity.getRelationKindName()));
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

                List<String> attachedConceptionKinds = new ArrayList<>();
                for(TimeScaleEntity currentConceptionEntity:timeScaleEntityList){
                    if(!attachedConceptionKinds.contains(currentConceptionEntity.getTimeScaleGrade().toString())){
                        attachedConceptionKinds.add(currentConceptionEntity.getTimeScaleGrade().toString());
                    }
                }
                //generateConceptionKindColorMap(attachedConceptionKinds);

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
                generateRelationKindColorMap(attachedRelationKinds);

                for(RelationEntity currentRelationEntity:timeEntitiesRelationList){
                    Map<String,String> currentEdgeInfo = new HashMap<>();
                    currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                    currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                    currentEdgeInfo.put("entityKind",currentRelationEntity.getRelationKindName());
                    //currentEdgeInfo.put("color",this.relationKindColorMap.get(currentRelationEntity.getRelationKindName()));
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

    private Map<String,String> generateConceptionKindColorMap(List<String> attachedConceptionKinds){
        String[] colorList =new String[]{
                "#EA2027","#006266","#1B1464","#5758BB","#6F1E51","#EE5A24","#009432","##0652DD","#9980FA","#833471",
                "#F79F1F","#A3CB38","#1289A7","#D980FA","#B53471","#FFC312","#C4E538","#12CBC4","#FDA7DF","#ED4C67"
        };

        for(int i=0;i<attachedConceptionKinds.size();i++){
            if(colorIndex>=colorList.length){
                colorIndex = 0;
            }
            String currentConceptionKindName = attachedConceptionKinds.get(i);
            if(!conceptionKindColorMap.containsKey(currentConceptionKindName)){
                conceptionKindColorMap.put(currentConceptionKindName,colorList[colorIndex]);
            }
            colorIndex++;
        }
        return conceptionKindColorMap;
    }

    private Map<String,String> generateRelationKindColorMap(List<String> attachedRelationKinds){
        String[] colorList =new String[]{
                "#F79F1F","#A3CB38","#1289A7","#D980FA","#B53471","#FFC312","#C4E538","#12CBC4","#FDA7DF","#ED4C67",
                "#EA2027","#006266","#1B1464","#5758BB","#6F1E51","#EE5A24","#009432","#0652DD","#9980FA","#833471"
        };

        for(int i=0;i<attachedRelationKinds.size();i++){
            if(colorIndex2>=colorList.length){
                colorIndex2 = 0;
            }
            String currentRelationKindName = attachedRelationKinds.get(i);
            if(!relationKindColorMap.containsKey(currentRelationKindName)){
                relationKindColorMap.put(currentRelationKindName,colorList[colorIndex2]);
            }
            colorIndex2++;
        }
        return relationKindColorMap;
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
        if(!this.timeFlowEntityExpendedList.contains(entityUID)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(this.timeFlowName);
            TimeScaleEntity targetTimeScaleEntity = targetTimeFlow.getTimeScaleEntityByUID(entityUID);
            if(targetTimeScaleEntity != null){
                LinkedList<TimeScaleEntity> childTimeScaleEntityList = targetTimeScaleEntity.getChildEntities();
                List<String> targetRelationCheckEntityList = new ArrayList<>();
                for(TimeScaleEntity currentTimeScaleEntity:childTimeScaleEntityList){
                    targetRelationCheckEntityList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
                }
                targetRelationCheckEntityList.add(entityUID);
                renderMoreTimeFlowCorrelationData(childTimeScaleEntityList);
                this.timeFlowEntityExpendedList.add(entityUID);

                //targetTimeScaleEntity.getAttachedTimeScaleEvents(null, TimeScaleEntity.TimeScaleLevel.SELF);

            }
            coreRealm.closeGlobalSession();
        }
    }
}
