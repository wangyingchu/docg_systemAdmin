package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.io.Serializable;
import java.util.*;

@StyleSheet("webApps/timeFlowCorrelationInfoChart/style.css")
@JavaScript("./visualization/feature/conceptionEntitiesRelationsChart-connector.js")
public class ConceptionEntitiesRelationsChart extends VerticalLayout {

    private Map<String,String> relationKindColorMap;

    public ConceptionEntitiesRelationsChart() {
        //link to download latest 3d-force-graph build js: https://unpkg.com/3d-force-graph
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three-spritetext.min.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/3d-force-graph.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.relationKindColorMap = new HashMap<>();
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntitiesRelationsChart.initLazy($0)", getElement()));
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

    public void renderConceptionEntitiesList(Set<ConceptionEntity> conceptionEntitiesSet){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
        List<String> conceptionEntityUIDList = new ArrayList<>();
        conceptionEntitiesSet.forEach(conceptionEntity -> {
                    conceptionEntityUIDList.add(conceptionEntity.getConceptionEntityUID());
                }
        );
        try {
            List<RelationEntity> relationEntityList = crossKindDataOperator.getRelationsOfConceptionEntityPair(conceptionEntityUIDList);

            List<Map<String,String>> nodeInfoList = new ArrayList<>();
            List<Map<String,String>> edgeInfoList = new ArrayList<>();

            for(ConceptionEntity currentConceptionEntity:conceptionEntitiesSet){
                Map<String,String> currentNodeInfo = new HashMap<>();
                currentNodeInfo.put("id",currentConceptionEntity.getConceptionEntityUID());
                currentNodeInfo.put("size","14");
                currentNodeInfo.put("color",getRandomColor());
                nodeInfoList.add(currentNodeInfo);
            }
            for(RelationEntity currentRelationEntity:relationEntityList){
                Map<String,String> currentEdgeInfo = new HashMap<>();
                currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                currentEdgeInfo.put("entityKind",currentRelationEntity.getRelationKindName());
                currentEdgeInfo.put("id",currentRelationEntity.getRelationEntityUID());
                currentEdgeInfo.put("color",getRelationKindColor(currentRelationEntity.getRelationKindName()));
                edgeInfoList.add(currentEdgeInfo);
            }

            getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
                generateGraph(receiver.getBodyClientHeight()-20,receiver.getBodyClientWidth()-300, nodeInfoList, edgeInfoList);
            }));
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateGraph(int height, int width, List<Map<String,String>> nodeInfoList, List<Map<String,String>> edgeInfoList){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Object> valueMap =new HashMap<>();
                valueMap.put("graphHeight",height-120);
                valueMap.put("graphWidth",width- 40);
                valueMap.put("nodesInfo",nodeInfoList);
                valueMap.put("edgesInfo",edgeInfoList);
                getElement().callJsFunction("$connector.generateGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString(valueMap)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void clearData(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.emptyGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getRelationKindColor(String relationKindName){
        if(!this.relationKindColorMap.containsKey(relationKindName)){
            this.relationKindColorMap.put(relationKindName,getRandomColor());
        }
        return this.relationKindColorMap.get(relationKindName);
    }

    private String getRandomColor(){
        String[] colorList =new String[]{
                "#EA2027","#006266","#1B1464","#6F1E51","#EE5A24","#009432","#0652DD","#9980FA","#833471",
                "#F79F1F","#A3CB38","#1289A7","#D980FA","#B53471","#FFC312","#C4E538","#12CBC4","#FDA7DF","#ED4C67"
        };
        return colorList[new Random().nextInt(colorList.length)];
    }
}
