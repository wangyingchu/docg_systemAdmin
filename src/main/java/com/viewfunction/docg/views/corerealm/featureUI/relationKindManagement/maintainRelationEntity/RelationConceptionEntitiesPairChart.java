package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.io.Serializable;
import java.util.*;

@JavaScript("./visualization/feature/relationConceptionEntitiesPairChart-connector.js")
public class RelationConceptionEntitiesPairChart extends VerticalLayout {

    private List<String> conceptionEntityUIDList;
    private List<String> relationEntityUIDList;
    private Map<String,String> conceptionKindColorMap;
    private int currentQueryPageSize = 10;
    private Map<String,Integer> targetConceptionEntityRelationCurrentQueryPageMap;
    private int colorIndex = 0;
    private String selectedConceptionEntityUID;
    private String selectedConceptionEntityKind;
    private Multimap<String,String> conception_relationEntityUIDMap;
    private String selectedRelationEntityUID;
    private String selectedRelationEntityKind;


    private String relationEntityUID;
    private String relationKind;
    private String relationFromConceptionEntityUID;
    private List<String> fromConceptionKinds;
    private String relationToConceptionEntityUID;
    private List<String> toConceptionKinds;

    public RelationConceptionEntitiesPairChart(){
        this.conceptionEntityUIDList = new ArrayList<>();
        this.relationEntityUIDList = new ArrayList<>();
        this.targetConceptionEntityRelationCurrentQueryPageMap = new HashMap<>();
        this.conceptionKindColorMap = new HashMap<>();
        this.conception_relationEntityUIDMap = ArrayListMultimap.create();
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.23.0/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_RelationConceptionEntitiesPairChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setDate(String relationKind,String relationEntityUID,String relationFromConceptionEntityUID,List<String> fromConceptionKinds,String relationToConceptionEntityUID,List<String> toConceptionKinds){
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
        this.relationFromConceptionEntityUID = relationFromConceptionEntityUID;
        this.fromConceptionKinds = fromConceptionKinds;
        this.relationToConceptionEntityUID = relationToConceptionEntityUID;
        this.toConceptionKinds = toConceptionKinds;

        if(!conception_relationEntityUIDMap.containsKey(this.relationFromConceptionEntityUID)){
            conception_relationEntityUIDMap.put(this.relationFromConceptionEntityUID,relationEntityUID);
        }else{
            if(!conception_relationEntityUIDMap.get(this.relationFromConceptionEntityUID).contains(relationEntityUID)){
                conception_relationEntityUIDMap.put(this.relationFromConceptionEntityUID,relationEntityUID);
            }
        }
        if(!conception_relationEntityUIDMap.containsKey(this.relationToConceptionEntityUID)){
            conception_relationEntityUIDMap.put(this.relationToConceptionEntityUID,relationEntityUID);
        }else{
            if(!conception_relationEntityUIDMap.get(this.relationToConceptionEntityUID).contains(relationEntityUID)){
                conception_relationEntityUIDMap.put(this.relationToConceptionEntityUID,relationEntityUID);
            }
        }

        if(!conceptionEntityUIDList.contains(this.relationFromConceptionEntityUID)){
            conceptionEntityUIDList.add(this.relationFromConceptionEntityUID);
            CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
            cytoscapeNodePayload.getData().put("shape","ellipse");
            cytoscapeNodePayload.getData().put("background_color","#c00");
            cytoscapeNodePayload.getData().put("size","4");
            if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(this.fromConceptionKinds.get(0))!=null){
                cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(this.fromConceptionKinds.get(0)));
            }

            if(fromConceptionKinds.get(0).startsWith(RealmConstant.TimeScaleEventClass)){
                cytoscapeNodePayload.getData().put("shape","round-diamond");
                cytoscapeNodePayload.getData().put("size","3");
                cytoscapeNodePayload.getData().put("background_color","#40E0D0");
            }
            if(fromConceptionKinds.get(0).startsWith(RealmConstant.TimeScaleEntityClass)){
                cytoscapeNodePayload.getData().put("shape","barrel");
                cytoscapeNodePayload.getData().put("background_color","#40E0D0");
            }
            if(fromConceptionKinds.get(0).startsWith(RealmConstant.GeospatialScaleEventClass)){
                cytoscapeNodePayload.getData().put("shape","round-diamond");
                cytoscapeNodePayload.getData().put("size","3");
                cytoscapeNodePayload.getData().put("background_color","#C71585");
            }
            if(fromConceptionKinds.get(0).startsWith(RealmConstant.GeospatialScaleEntityClass)){
                cytoscapeNodePayload.getData().put("shape","barrel");
                cytoscapeNodePayload.getData().put("background_color","#C71585");
            }

            cytoscapeNodePayload.getData().put("id",this.relationFromConceptionEntityUID);
            cytoscapeNodePayload.getData().put("kind",this.fromConceptionKinds.get(0));
            cytoscapeNodePayload.getData().put("desc",this.fromConceptionKinds.get(0)+":\n"+this.relationFromConceptionEntityUID);
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if(!conceptionEntityUIDList.contains(this.relationToConceptionEntityUID)){
            conceptionEntityUIDList.add(this.relationToConceptionEntityUID);
            CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
            cytoscapeNodePayload.getData().put("shape","ellipse");
            cytoscapeNodePayload.getData().put("background_color","#c00");
            cytoscapeNodePayload.getData().put("size","4");
            if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(this.toConceptionKinds.get(0))!=null){
                cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(this.toConceptionKinds.get(0)));
            }

            if(this.toConceptionKinds.get(0).startsWith(RealmConstant.TimeScaleEventClass)){
                cytoscapeNodePayload.getData().put("shape","round-diamond");
                cytoscapeNodePayload.getData().put("size","3");
                cytoscapeNodePayload.getData().put("background_color","#40E0D0");
            }
            if(this.toConceptionKinds.get(0).startsWith(RealmConstant.TimeScaleEntityClass)){
                cytoscapeNodePayload.getData().put("shape","barrel");
                cytoscapeNodePayload.getData().put("background_color","#40E0D0");
            }
            if(this.toConceptionKinds.get(0).startsWith(RealmConstant.GeospatialScaleEventClass)){
                cytoscapeNodePayload.getData().put("shape","round-diamond");
                cytoscapeNodePayload.getData().put("size","3");
                cytoscapeNodePayload.getData().put("background_color","#C71585");
            }
            if(this.toConceptionKinds.get(0).startsWith(RealmConstant.GeospatialScaleEntityClass)){
                cytoscapeNodePayload.getData().put("shape","barrel");
                cytoscapeNodePayload.getData().put("background_color","#C71585");
            }
            cytoscapeNodePayload.getData().put("id",this.relationToConceptionEntityUID);
            cytoscapeNodePayload.getData().put("kind",this.toConceptionKinds.get(0));
            cytoscapeNodePayload.getData().put("desc",this.toConceptionKinds.get(0)+":\n"+this.relationToConceptionEntityUID);
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if(!relationEntityUIDList.contains(this.relationEntityUID)){
            relationEntityUIDList.add(this.relationEntityUID);
            CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
            cytoscapeEdgePayload.getData().put("type", this.relationKind+":"+this.relationEntityUID);
            cytoscapeEdgePayload.getData().put("source", this.relationFromConceptionEntityUID);
            cytoscapeEdgePayload.getData().put("target", this.relationToConceptionEntityUID);
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeEdgePayload)});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        initLayoutGraph();
    }










    private void initLayoutGraph(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.initLayoutGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
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

    private void clearGraph(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.clearData", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void lockGraph(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.lockGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @ClientCallable
    public void addConceptionEntityRelations(String entityType,String entityUID) {
        loadAdditionalTargetConceptionEntityRelationData(entityType,entityUID);
    }

    @ClientCallable
    public void unselectConceptionEntity(String entityType,String entityUID) {
        this.selectedConceptionEntityKind = null;
        this.selectedConceptionEntityUID = null;

    }

    @ClientCallable
    public void selectConceptionEntity(String entityType,String entityUID) {
        this.selectedConceptionEntityKind = entityType;
        this.selectedConceptionEntityUID = entityUID;
        /*
        if(containerConceptionEntityRelationTopologyView != null){
            int pageIndex = targetConceptionEntityRelationCurrentQueryPageMap.containsKey(entityUID) ?
                    targetConceptionEntityRelationCurrentQueryPageMap.get(entityUID) : 1 ;
            containerConceptionEntityRelationTopologyView.enableControlActionButtons(pageIndex);
            containerConceptionEntityRelationTopologyView.renderSelectedConceptionEntityAbstractInfo(entityType,entityUID);
        }

         */
    }

    @ClientCallable
    public void unselectRelationEntity(String entityTypeAnUIDStr) {
        String[] entityIDInfoArray = entityTypeAnUIDStr.split(":");
        String relationEntityType = entityIDInfoArray[0];
        String relationEntityUID = entityIDInfoArray[1];
        this.selectedRelationEntityKind = null;
        this.selectedRelationEntityUID = null;
        /*
        if(containerConceptionEntityRelationTopologyView != null){
            containerConceptionEntityRelationTopologyView.clearRelationEntityAbstractInfo();
        }

         */
    }

    @ClientCallable
    public void selectRelationEntity(String entityTypeAnUIDStr) {
        String[] entityIDInfoArray = entityTypeAnUIDStr.split(":");
        String relationEntityType = entityIDInfoArray[0];
        String relationEntityUID = entityIDInfoArray[1];
        this.selectedRelationEntityKind = relationEntityType;
        this.selectedRelationEntityUID = relationEntityUID;
        /*
        if(containerConceptionEntityRelationTopologyView != null){
            containerConceptionEntityRelationTopologyView.renderSelectedRelationEntityAbstractInfo(selectedRelationEntityKind,selectedRelationEntityUID);
        }

         */
    }

    /*
    public void initLoadTargetConceptionEntityRelationData(){
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
                    generateConceptionKindColorMap(attachedConceptionKinds);
                    QueryParameters relationshipQueryParameters = new QueryParameters();
                    int currentEntityQueryPage = 1;
                    if(targetConceptionEntityRelationCurrentQueryPageMap.containsKey(this.conceptionEntityUID)){
                        currentEntityQueryPage = targetConceptionEntityRelationCurrentQueryPageMap.get(this.conceptionEntityUID);
                    }
                    relationshipQueryParameters.setStartPage(currentEntityQueryPage);
                    relationshipQueryParameters.setEndPage(currentEntityQueryPage+1);
                    relationshipQueryParameters.setPageSize(currentQueryPageSize);
                    for (String currentRelationKind : attachedRelationKinds) {
                        relationshipQueryParameters.setEntityKind(currentRelationKind);
                        List<RelationEntity> currentKindTargetRelationEntityList = targetEntity.getSpecifiedRelations(relationshipQueryParameters, RelationDirection.TWO_WAY);
                        totalKindsRelationEntitiesList.addAll(currentKindTargetRelationEntityList);
                    }
                    if(totalKindsRelationEntitiesList.size()>0){
                        setData(totalKindsRelationEntitiesList);
                        initLayoutGraph();
                        currentEntityQueryPage++;
                        targetConceptionEntityRelationCurrentQueryPageMap.put(this.conceptionEntityUID,currentEntityQueryPage);
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
        if(containerConceptionEntityRelationTopologyView != null){
            containerConceptionEntityRelationTopologyView.updateEntitiesMetaStaticInfo(conceptionEntityUIDList.size(),relationEntityUIDList.size());
        }
    }
    */

    private void loadAdditionalTargetConceptionEntityRelationData(String conceptionKind,String conceptionEntityUID){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
        if(targetConceptionKind != null) {
            try {
                ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(conceptionEntityUID);
                if (targetEntity != null) {
                    int currentEntityQueryPage = 1;
                    if(targetConceptionEntityRelationCurrentQueryPageMap.containsKey(conceptionEntityUID)){
                        currentEntityQueryPage = targetConceptionEntityRelationCurrentQueryPageMap.get(conceptionEntityUID);
                    }
                    List<RelationEntity> totalKindsRelationEntitiesList = new ArrayList<>();
                    List<String> attachedRelationKinds = targetEntity.listAttachedRelationKinds();
                    List<String> attachedConceptionKinds = targetEntity.listAttachedConceptionKinds();
                    generateConceptionKindColorMap(attachedConceptionKinds);
                    QueryParameters relationshipQueryParameters = new QueryParameters();
                    relationshipQueryParameters.setStartPage(currentEntityQueryPage);
                    relationshipQueryParameters.setEndPage(currentEntityQueryPage+1);
                    relationshipQueryParameters.setPageSize(currentQueryPageSize);
                    for (String currentRelationKind : attachedRelationKinds) {
                        relationshipQueryParameters.setEntityKind(currentRelationKind);
                        List<RelationEntity> currentKindTargetRelationEntityList = targetEntity.getSpecifiedRelations(relationshipQueryParameters, RelationDirection.TWO_WAY);
                        totalKindsRelationEntitiesList.addAll(currentKindTargetRelationEntityList);
                    }
                    boolean executeGraphIncreaseOperation = false;
                    if(totalKindsRelationEntitiesList.size() == 1){
                        RelationEntity resultRelationEntity = totalKindsRelationEntitiesList.get(0);
                        String relationUID = resultRelationEntity.getRelationEntityUID();
                        if(!relationEntityUIDList.contains(relationUID)){
                            executeGraphIncreaseOperation = true;
                        }
                    }
                    if(totalKindsRelationEntitiesList.size() > 1){
                        executeGraphIncreaseOperation = true;
                    }
                    if(executeGraphIncreaseOperation){
                        lockGraph();

                        //setData(totalKindsRelationEntitiesList);


                        currentEntityQueryPage++;
                        targetConceptionEntityRelationCurrentQueryPageMap.put(conceptionEntityUID,currentEntityQueryPage);
                        layoutGraph();
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

    public void reload(){
        this.conceptionEntityUIDList.clear();
        this.relationEntityUIDList.clear();
        this.targetConceptionEntityRelationCurrentQueryPageMap.clear();
        clearGraph();
        //initLoadTargetConceptionEntityRelationData();
    }

    public void expandSelectedEntityOneDegreeRelations() {
        loadAdditionalTargetConceptionEntityRelationData(this.selectedConceptionEntityKind,this.selectedConceptionEntityUID);
    }

    public void resetConceptionEntityRelationQueryPageIndex(){
        if(this.targetConceptionEntityRelationCurrentQueryPageMap.containsKey(this.selectedConceptionEntityUID)){
            this.targetConceptionEntityRelationCurrentQueryPageMap.remove(this.selectedConceptionEntityUID);
        }
    }

    public void addConceptionEntityRelationQueryPageIndex(){
        if(this.targetConceptionEntityRelationCurrentQueryPageMap.containsKey(this.selectedConceptionEntityUID)){
            int currentValue = this.targetConceptionEntityRelationCurrentQueryPageMap.get(this.selectedConceptionEntityUID);
            this.targetConceptionEntityRelationCurrentQueryPageMap.put(this.selectedConceptionEntityUID,currentValue+1);
        }else{
            this.targetConceptionEntityRelationCurrentQueryPageMap.put(this.selectedConceptionEntityUID,1);
        }
    }

    public void minusConceptionEntityRelationQueryPageIndex(){
        if(this.targetConceptionEntityRelationCurrentQueryPageMap.containsKey(this.selectedConceptionEntityUID)){
            int currentValue = this.targetConceptionEntityRelationCurrentQueryPageMap.get(this.selectedConceptionEntityUID);
            if(currentValue -1 >1){
                this.targetConceptionEntityRelationCurrentQueryPageMap.put(this.selectedConceptionEntityUID,currentValue -1);
            }else{
                this.targetConceptionEntityRelationCurrentQueryPageMap.remove(this.selectedConceptionEntityUID);
            }
        }
    }

















}
