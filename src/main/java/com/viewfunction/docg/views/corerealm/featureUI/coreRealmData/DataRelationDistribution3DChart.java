package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

@StyleSheet("webApps/timeFlowCorrelationInfoChart/style.css")
@JavaScript("./visualization/feature/dataRelationDistribution3DChart-connector.js")
public class DataRelationDistribution3DChart extends VerticalLayout {

    private Map<String,String> conceptionKindColorMap;
    private int colorIndex = 0;
    private NumberFormat numberFormat;

    public DataRelationDistribution3DChart(){
        //link to download latest 3d-force-graph build js: https://unpkg.com/3d-force-graph
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three-spritetext.min.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/3d-force-graph.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.conceptionKindColorMap = new HashMap<>();
        this.setHeight(100, Unit.PERCENTAGE);
        this.numberFormat = NumberFormat.getInstance();
        initConnector();
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet, Map<String, Long> conceptionKindsDataCount, Map<String, Long> relationKindsDataCount){
        if(conceptionKindsDataCount != null){
            Set<String> conceptionKindNameSet = conceptionKindsDataCount.keySet();
            generateConceptionKindColorMap(conceptionKindNameSet);
            for(String currentConceptionKindName:conceptionKindNameSet){
                if(!currentConceptionKindName.equals(RealmConstant.ConceptionKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.AttributeKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.RelationKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !currentConceptionKindName.equals(RealmConstant.ClassificationClass)
                        && !currentConceptionKindName.equals(RealmConstant.TimeScaleEntityClass)
                        && !currentConceptionKindName.equals(RealmConstant.GeospatialScaleEntityClass)
                ){
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionKindName)!=null){

                    }
                    if(currentConceptionKindName.startsWith("DOCG_TS_")){

                    }

                    if(currentConceptionKindName.startsWith("DOCG_GS_")){

                    }

                    if(currentConceptionKindName.startsWith(RealmConstant.TimeScaleEventClass)){

                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.TimeScaleEntityClass)){

                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.GeospatialScaleEventClass)){

                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.GeospatialScaleEntityClass)){

                    }
                    /*
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    */
                }
            }
        }
        if(conceptionKindCorrelationInfoSet != null){
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceConceptionKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetConceptionKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                String relationKindName = currentConceptionKindCorrelationInfo.getRelationKindName();
                long relationEntityCount = currentConceptionKindCorrelationInfo.getRelationEntityCount();
                if(!relationKindName.equals(RealmConstant.Kind_MetaConfigItemsStorageRelationClass)
                        && !relationKindName.equals(RealmConstant.ConceptionKind_AttributesViewKindRelationClass)
                        && !relationKindName.equals(RealmConstant.AttributesViewKind_AttributeKindRelationClass)
                        && !relationKindName.equals(RealmConstant.Classification_ClassificationRelationClass)
                        && !relationKindName.equals(RealmConstant.RelationAttachKind_RelationAttachLinkLogicRelationClass)){
                    boolean linkToTGOrClassification = false;
                    if(sourceConceptionKindName.equals(RealmConstant.TimeScaleEntityClass)
                            ||sourceConceptionKindName.equals(RealmConstant.GeospatialScaleEntityClass)
                            ||sourceConceptionKindName.equals(RealmConstant.ClassificationClass)
                            ||targetConceptionKindName.equals(RealmConstant.TimeScaleEntityClass)
                            ||targetConceptionKindName.equals(RealmConstant.GeospatialScaleEntityClass)
                            ||targetConceptionKindName.equals(RealmConstant.ClassificationClass)){
                        linkToTGOrClassification = true;
                    }
                    if(!linkToTGOrClassification){
                        if(!relationKindName.startsWith("DOCG_TS_NextIs") &&
                                !relationKindName.startsWith("DOCG_TS_FirstChildIs") &&
                                !relationKindName.startsWith("DOCG_TS_LastChildIs")){

                            if(relationKindName.startsWith("DOCG_TS")){

                            }else if(relationKindName.startsWith("DOCG_GS")){

                            }else{

                            }
                            /*
                            runBeforeClientResponse(ui -> {
                                try {
                                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeEdgePayload)});
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            */
                        }
                    }
                }
            }
        }
    }

    private Map<String,String> generateConceptionKindColorMap(Set<String> attachedConceptionKindsSet){
        List<String> attachedConceptionKinds = new ArrayList<String>();
        attachedConceptionKinds.addAll(attachedConceptionKindsSet);

        String[] colorList =new String[]{
                "#EA2027","#006266","#1B1464","#6F1E51","#EE5A24","#009432","##0652DD","#9980FA","#833471",
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

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_DataRelationDistribution3DChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            generateGraph(receiver.getBodyClientHeight(),receiver.getBodyClientWidth());
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }




    private void generateGraph(int height,int width){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Object> valueMap =new HashMap<>();
                List<Map<String,String>> nodeInfoList = new ArrayList<>();
                Map<String,String> centerNodeInfo = new HashMap<>();
                //centerNodeInfo.put("id",this.mainConceptionEntityUID);
                //centerNodeInfo.put("entityKind",this.mainConceptionKind);
                centerNodeInfo.put("color","#888888");
                nodeInfoList.add(centerNodeInfo);

                List<String> attachedConceptionKinds = new ArrayList<>();
                /*
                for(ConceptionEntity currentConceptionEntity:this.conceptionEntityList){
                    if(!attachedConceptionKinds.contains(currentConceptionEntity.getConceptionKindName())){
                        attachedConceptionKinds.add(currentConceptionEntity.getConceptionKindName());
                    }
                }
                */
                //generateConceptionKindColorMap(attachedConceptionKinds);

                /*
                for(ConceptionEntity currentConceptionEntity:this.conceptionEntityList){
                    Map<String,String> currentNodeInfo = new HashMap<>();
                    currentNodeInfo.put("id",currentConceptionEntity.getConceptionEntityUID());
                    currentNodeInfo.put("entityKind",currentConceptionEntity.getConceptionKindName());
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionEntity.getConceptionKindName())!=null){
                        currentNodeInfo.put("color",this.conceptionKindColorMap.get(currentConceptionEntity.getConceptionKindName()));
                    }else{
                        currentNodeInfo.put("color","#0099FF");
                    }
                    nodeInfoList.add(currentNodeInfo);
                }
                */
                List<Map<String,String>> edgeInfoList = new ArrayList<>();

                List<String> attachedRelationKinds = new ArrayList<>();
                /*
                for(RelationEntity currentRelationEntity:this.relationEntityList){
                    if(!attachedRelationKinds.contains(currentRelationEntity.getRelationKindName())){
                        attachedRelationKinds.add(currentRelationEntity.getRelationKindName());
                    }
                }
                generateRelationKindColorMap(attachedRelationKinds);

                for(RelationEntity currentRelationEntity:this.relationEntityList){
                    Map<String,String> currentEdgeInfo = new HashMap<>();
                    currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                    currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                    currentEdgeInfo.put("entityKind",currentRelationEntity.getRelationKindName());
                    currentEdgeInfo.put("color",this.relationKindColorMap.get(currentRelationEntity.getRelationKindName()));
                    edgeInfoList.add(currentEdgeInfo);
                }
                */

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
                getElement().callJsFunction("$connector.clearData", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
