package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JavaScript("./visualization/feature/conceptionEntityRelationsChart-connector.js")
public class ConceptionEntityRelationsChart extends VerticalLayout {

    private List<String> conceptionEntityUIDList;
    private List<String> relationEntityUIDList;
    private String conceptionEntityUID;
    private String conceptionKind;
    private Map<String,String> conceptionKindColorMap;
    private int currentRelationQueryPage = 1;
    private int currentQueryPageSize = 10;
    private Map<String,Integer> additionalTargetConceptionEntityRelationCurrentQueryPageMap;
    private boolean graphChartLoaded = false;

    public ConceptionEntityRelationsChart(String conceptionKind,String conceptionEntityUID){
        this.conceptionEntityUIDList = new ArrayList<>();
        this.relationEntityUIDList = new ArrayList<>();
        this.additionalTargetConceptionEntityRelationCurrentQueryPageMap = new HashMap<>();
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionKind = conceptionKind;
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.22.1/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(750, Unit.PIXELS);
        //this.setHeight(chartHeight, Unit.PIXELS);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntityRelationsChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void clearData(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.clearData", "null"));
    }

    public void setData(List<RelationEntity> conceptionEntityRelationEntityList){
        if(conceptionEntityRelationEntityList != null){
            for(RelationEntity currentRelationEntity:conceptionEntityRelationEntityList){
                String relationKind = currentRelationEntity.getRelationKindName();
                String relationEntityUID = currentRelationEntity.getRelationEntityUID();

                List<String> fromConceptionEntityKind = currentRelationEntity.getFromConceptionEntityKinds();
                String fromConceptionEntityUID = currentRelationEntity.getFromConceptionEntityUID();

                List<String> toConceptionEntityKind = currentRelationEntity.getToConceptionEntityKinds();
                String toConceptionEntityUID = currentRelationEntity.getToConceptionEntityUID();

                if(!conceptionEntityUIDList.contains(fromConceptionEntityUID)){
                    conceptionEntityUIDList.add(fromConceptionEntityUID);
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(fromConceptionEntityKind.get(0))!=null){
                        cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(fromConceptionEntityKind.get(0)));
                    }
                    if(this.conceptionEntityUID.equals(fromConceptionEntityUID)){
                        cytoscapeNodePayload.getData().put("shape","pentagon");
                        cytoscapeNodePayload.getData().put("background_color","#555555");
                    }
                    if(fromConceptionEntityKind.get(0).startsWith("DOCG_")){
                        cytoscapeNodePayload.getData().put("shape","diamond");
                        cytoscapeNodePayload.getData().put("background_color","#FF8C00");
                    }
                    cytoscapeNodePayload.getData().put("id",fromConceptionEntityUID);
                    cytoscapeNodePayload.getData().put("kind",fromConceptionEntityKind.get(0));
                    cytoscapeNodePayload.getData().put("desc",fromConceptionEntityKind.get(0)+":"+fromConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if(!conceptionEntityUIDList.contains(toConceptionEntityUID)){
                    conceptionEntityUIDList.add(toConceptionEntityUID);
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(toConceptionEntityKind.get(0))!=null){
                        cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(toConceptionEntityKind.get(0)));
                    }
                    if(this.conceptionEntityUID.equals(toConceptionEntityUID)){
                        cytoscapeNodePayload.getData().put("shape","pentagon");
                        cytoscapeNodePayload.getData().put("background_color","#555555");
                    }
                    if(toConceptionEntityKind.get(0).startsWith("DOCG_")){
                        cytoscapeNodePayload.getData().put("shape","diamond");
                        cytoscapeNodePayload.getData().put("background_color","#FF8C00");
                    }
                    cytoscapeNodePayload.getData().put("id",toConceptionEntityUID);
                    cytoscapeNodePayload.getData().put("kind",toConceptionEntityKind.get(0));
                    cytoscapeNodePayload.getData().put("desc",toConceptionEntityKind.get(0)+":"+toConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if(!relationEntityUIDList.contains(relationEntityUID)){
                    relationEntityUIDList.add(relationEntityUID);
                    CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                    cytoscapeEdgePayload.getData().put("type", relationKind+":"+relationEntityUID);
                    cytoscapeEdgePayload.getData().put("source", fromConceptionEntityUID);
                    cytoscapeEdgePayload.getData().put("target", toConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeEdgePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
    }











    public void insertData(List<RelationEntity> conceptionEntityRelationEntityList){
        if(conceptionEntityRelationEntityList != null){


            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.lockGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            for(RelationEntity currentRelationEntity:conceptionEntityRelationEntityList){
                String relationKind = currentRelationEntity.getRelationKindName();
                String relationEntityUID = currentRelationEntity.getRelationEntityUID();

                List<String> fromConceptionEntityKind = currentRelationEntity.getFromConceptionEntityKinds();
                String fromConceptionEntityUID = currentRelationEntity.getFromConceptionEntityUID();

                List<String> toConceptionEntityKind = currentRelationEntity.getToConceptionEntityKinds();
                String toConceptionEntityUID = currentRelationEntity.getToConceptionEntityUID();

                if(!conceptionEntityUIDList.contains(fromConceptionEntityUID)){
                    conceptionEntityUIDList.add(fromConceptionEntityUID);
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(fromConceptionEntityKind.get(0))!=null){
                        cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(fromConceptionEntityKind.get(0)));
                    }
                    if(this.conceptionEntityUID.equals(fromConceptionEntityUID)){
                        cytoscapeNodePayload.getData().put("shape","pentagon");
                        cytoscapeNodePayload.getData().put("background_color","#555555");
                    }
                    if(fromConceptionEntityKind.get(0).startsWith("DOCG_")){
                        cytoscapeNodePayload.getData().put("shape","diamond");
                        cytoscapeNodePayload.getData().put("background_color","#FF8C00");
                    }
                    cytoscapeNodePayload.getData().put("id",fromConceptionEntityUID);
                    cytoscapeNodePayload.getData().put("kind",fromConceptionEntityKind.get(0));
                    cytoscapeNodePayload.getData().put("desc",fromConceptionEntityKind.get(0)+":"+fromConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if(!conceptionEntityUIDList.contains(toConceptionEntityUID)){
                    conceptionEntityUIDList.add(toConceptionEntityUID);
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(toConceptionEntityKind.get(0))!=null){
                        cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(toConceptionEntityKind.get(0)));
                    }
                    if(this.conceptionEntityUID.equals(toConceptionEntityUID)){
                        cytoscapeNodePayload.getData().put("shape","pentagon");
                        cytoscapeNodePayload.getData().put("background_color","#555555");
                    }
                    if(toConceptionEntityKind.get(0).startsWith("DOCG_")){
                        cytoscapeNodePayload.getData().put("shape","diamond");
                        cytoscapeNodePayload.getData().put("background_color","#FF8C00");
                    }
                    cytoscapeNodePayload.getData().put("id",toConceptionEntityUID);
                    cytoscapeNodePayload.getData().put("kind",toConceptionEntityKind.get(0));
                    cytoscapeNodePayload.getData().put("desc",toConceptionEntityKind.get(0)+":"+toConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if(!relationEntityUIDList.contains(relationEntityUID)){
                    relationEntityUIDList.add(relationEntityUID);
                    CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                    cytoscapeEdgePayload.getData().put("type", relationKind+":"+relationEntityUID);
                    cytoscapeEdgePayload.getData().put("source", fromConceptionEntityUID);
                    cytoscapeEdgePayload.getData().put("target", toConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeEdgePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }

/*
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.unlockGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
  */

        }
    }



















    private void layoutGraph(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.layoutGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void layoutGraph2(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.layoutGraph2", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setConceptionKindColorMap(Map<String, String> conceptionKindColorMap) {
        this.conceptionKindColorMap = conceptionKindColorMap;
    }

    @ClientCallable
    public void addConceptionEntityRelations(String entityType,String entityUID) {
        System.out.println("addConceptionEntityRelations, " + entityUID);
        if(this.conceptionEntityUID.equals(entityUID)){
            loadTargetConceptionEntityRelationData();
        }else{
            loadAdditionalTargetConceptionEntityRelationData(entityType,entityUID);
        }
    }

    public void loadTargetConceptionEntityRelationData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null) {
            try {
                ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                if (targetEntity != null) {
                    List<RelationEntity> totalKindsRelationEntitiesList = new ArrayList<>();
                    List<String> attachedRelationKinds = targetEntity.listAttachedRelationKinds();
                    List<String> attachedConceptionKinds = targetEntity.listAttachedConceptionKinds();
                    setConceptionKindColorMap(generateConceptionKindColorMap(attachedConceptionKinds));
                    QueryParameters relationshipQueryParameters = new QueryParameters();
                    relationshipQueryParameters.setStartPage(currentRelationQueryPage);
                    relationshipQueryParameters.setEndPage(currentRelationQueryPage+1);
                    relationshipQueryParameters.setPageSize(currentQueryPageSize);
                    for (String currentRelationKind : attachedRelationKinds) {
                        relationshipQueryParameters.setEntityKind(currentRelationKind);
                        List<RelationEntity> currentKindTargetRelationEntityList = targetEntity.getSpecifiedRelations(relationshipQueryParameters, RelationDirection.TWO_WAY);
                        totalKindsRelationEntitiesList.addAll(currentKindTargetRelationEntityList);
                    }
                    if(totalKindsRelationEntitiesList.size()>0){
                        setData(totalKindsRelationEntitiesList);
                        if(!graphChartLoaded){
                            graphChartLoaded = true;
                            layoutGraph();
                        }
                        currentRelationQueryPage++;
                    }
                }else{
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }

    public void loadAdditionalTargetConceptionEntityRelationData(String conceptionKind,String conceptionEntityUID){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
        if(targetConceptionKind != null) {
            try {
                ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(conceptionEntityUID);
                if (targetEntity != null) {
                    int currentEntityQueryPage = 1;
                    if(additionalTargetConceptionEntityRelationCurrentQueryPageMap.containsKey(conceptionEntityUID)){
                        currentEntityQueryPage = additionalTargetConceptionEntityRelationCurrentQueryPageMap.get(conceptionEntityUID);
                    }
                    List<RelationEntity> totalKindsRelationEntitiesList = new ArrayList<>();
                    List<String> attachedRelationKinds = targetEntity.listAttachedRelationKinds();
                    List<String> attachedConceptionKinds = targetEntity.listAttachedConceptionKinds();
                    setConceptionKindColorMap(generateConceptionKindColorMap(attachedConceptionKinds));
                    QueryParameters relationshipQueryParameters = new QueryParameters();
                    relationshipQueryParameters.setStartPage(currentEntityQueryPage);
                    relationshipQueryParameters.setEndPage(currentEntityQueryPage+1);
                    relationshipQueryParameters.setPageSize(currentQueryPageSize);
                    for (String currentRelationKind : attachedRelationKinds) {
                        relationshipQueryParameters.setEntityKind(currentRelationKind);
                        List<RelationEntity> currentKindTargetRelationEntityList = targetEntity.getSpecifiedRelations(relationshipQueryParameters, RelationDirection.TWO_WAY);
                        totalKindsRelationEntitiesList.addAll(currentKindTargetRelationEntityList);
                    }
                    if(totalKindsRelationEntitiesList.size()>0){
                        insertData(totalKindsRelationEntitiesList);
                        currentEntityQueryPage++;
                        additionalTargetConceptionEntityRelationCurrentQueryPageMap.put(conceptionEntityUID,currentEntityQueryPage);
                        layoutGraph2();
                    }
                }else{
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }

    private Map<String,String> generateConceptionKindColorMap(List<String> attachedConceptionKinds){
        String[] colorList =new String[]{
                "#EA2027","#006266","#1B1464","#5758BB","#6F1E51","#EE5A24","#009432","##0652DD","#9980FA","#833471",
                "#F79F1F","#A3CB38","#1289A7","#D980FA","#B53471","#FFC312","#C4E538","#12CBC4","#FDA7DF","#ED4C67"
        };
        Map<String,String> conceptionKindColorMap = new HashMap<>();
        int colorIndex = 0;
        for(int i=0;i<attachedConceptionKinds.size();i++){
            if(colorIndex>=colorList.length){
                colorIndex = 0;
            }
            String currentConceptionKindName = attachedConceptionKinds.get(i);
            conceptionKindColorMap.put(currentConceptionKindName,colorList[colorIndex]);
            colorIndex++;
        }
        return conceptionKindColorMap;
    }
}