package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@StyleSheet("webApps/relatedConceptionEntitiesDandelionGraphChart/style.css")
@JavaScript("./visualization/feature/timeFlowCorrelationInfoChart-connector.js")
public class TimeFlowCorrelationInfoChart extends VerticalLayout {

    private Registration listener;
    private int colorIndex = 0;
    private int colorIndex2 = 0;
    private Map<String,String> conceptionKindColorMap;
    private Map<String,String> relationKindColorMap;
    private int graphHeight;
    private int graphWidth;
    private List<String> timeScaleEntityUIDList;
    private List<RelationEntity> timeEntitiesRelationList;

    public TimeFlowCorrelationInfoChart(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        this.conceptionKindColorMap = new HashMap<>();
        this.relationKindColorMap = new HashMap<>();
        this.timeScaleEntityUIDList = new ArrayList<>();
        this.timeEntitiesRelationList = new ArrayList<>();

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
        // 此处调用 listener.remove() 会抛出 java.lang.NullPointerException 异常，
        // 但是此异常的抛出能够阻止在多次打开 3d-force-graph 蒲公英图的场景下系统UI卡顿，停止相应并出现持续性的线程调用无法回收的情况
        //具体原理未知，有待调查
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderTimeFlowCorrelationData(List<TimeScaleEntity> timeScaleEntityList){
        if(timeScaleEntityList != null && timeScaleEntityList.size() > 0){
            timeScaleEntityUIDList.clear();
            timeEntitiesRelationList.clear();
            for(TimeScaleEntity currentTimeScaleEntity:timeScaleEntityList){
                timeScaleEntityUIDList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
            }
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            //coreRealm.openGlobalSession();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            try {
                List<RelationEntity> timeEntitiesPairRelationList = crossKindDataOperator.getRelationsOfConceptionEntityPair(timeScaleEntityUIDList);
                timeEntitiesRelationList.addAll(timeEntitiesPairRelationList);
                generateGraph(timeScaleEntityList,timeEntitiesRelationList);
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
            //coreRealm.closeGlobalSession();
        }
    }

    public void renderMoreTimeFlowCorrelationData(List<TimeScaleEntity> timeScaleEntityList){
        if(timeScaleEntityList != null && timeScaleEntityList.size() > 0){
            List<String> timeScaleEntityUIDList = new ArrayList<>();
            for(TimeScaleEntity currentTimeScaleEntity:timeScaleEntityList){
                timeScaleEntityUIDList.add(currentTimeScaleEntity.getTimeScaleEntityUID());
            }
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            //coreRealm.openGlobalSession();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            try {
                List<RelationEntity> timeEntitiesRelationList = crossKindDataOperator.getRelationsOfConceptionEntityPair(timeScaleEntityUIDList);
                insertInGenerateGraph(timeScaleEntityList,timeEntitiesRelationList);
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
            //coreRealm.closeGlobalSession();
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
                generateConceptionKindColorMap(attachedConceptionKinds);

                for(TimeScaleEntity currentConceptionEntity:timeScaleEntityList){
                    Map<String,String> currentNodeInfo = new HashMap<>();
                    currentNodeInfo.put("id",currentConceptionEntity.getTimeScaleEntityUID());
                    currentNodeInfo.put("entityKind",currentConceptionEntity.getTimeScaleGrade().toString());
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
                    currentEdgeInfo.put("color",this.relationKindColorMap.get(currentRelationEntity.getRelationKindName()));
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
                generateConceptionKindColorMap(attachedConceptionKinds);

                for(TimeScaleEntity currentConceptionEntity:timeScaleEntityList){
                    Map<String,String> currentNodeInfo = new HashMap<>();
                    currentNodeInfo.put("id",currentConceptionEntity.getTimeScaleEntityUID());
                    currentNodeInfo.put("entityKind",currentConceptionEntity.getTimeScaleGrade().toString());
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
                    currentEdgeInfo.put("color",this.relationKindColorMap.get(currentRelationEntity.getRelationKindName()));
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
}
