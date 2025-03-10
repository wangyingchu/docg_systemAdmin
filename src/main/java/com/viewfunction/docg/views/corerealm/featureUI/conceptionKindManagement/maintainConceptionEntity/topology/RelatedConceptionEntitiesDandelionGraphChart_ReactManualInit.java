package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://www.npmjs.com/package/react-force-graph
@NpmPackage(value = "react-force-graph", version = "1.46.0")
@NpmPackage(value = "three", version = "0.173.0")
@NpmPackage(value = "three-spritetext", version = "1.9.4")
@JsModule("./externalTech/flow/integration/react/relatedConceptionEntitiesDandelionGraphChart/related-conception-entities-dandelion-graph-chart_ReactManualInit.tsx")
@StyleSheet("webApps/relatedConceptionEntitiesDandelionGraphChart/style.css")
@Tag("related-conception-entities-dandelion-graph-chart_ReactManualInit")
public class RelatedConceptionEntitiesDandelionGraphChart_ReactManualInit extends VerticalLayout {

    private String mainConceptionKind;
    private String mainConceptionEntityUID;
    private List<ConceptionEntity> conceptionEntityList;
    private List<RelationEntity> relationEntityList;
    private int colorIndex = 0;
    private int colorIndex2 = 0;
    private Map<String,String> conceptionKindColorMap;
    private Map<String,String> relationKindColorMap;

    public RelatedConceptionEntitiesDandelionGraphChart_ReactManualInit(int width,int height,
            String mainConceptionKind, String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList, List<RelationEntity> relationEntityList){
        this.conceptionKindColorMap = new HashMap<>();
        this.relationKindColorMap = new HashMap<>();
        Map<String,Object> valueMap = getDandelionGraphChartData(mainConceptionKind,mainConceptionEntityUID,conceptionEntityList,relationEntityList);
        try {
            getElement().executeJs("window.relatedConceptionEntitiesDandelionGraphChartConnectorInit($0, $1, $2, $3)", getElement(), width,height,(new ObjectMapper()).writeValueAsString(valueMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public void setChartData(Map<String,Object> valueMap){
        try {
            getElement().executeJs("window.dispatchEvent(new Event('updateRelatedConceptionEntitiesDandelionGraphChartData',$0))",(new ObjectMapper()).writeValueAsString(valueMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    */

    public void destroyReactComponent() {
        getElement().executeJs("window.dispatchEvent(new Event('destroyRelatedConceptionEntitiesDandelionGraphChart'));");
    }

    public Map<String,Object> getDandelionGraphChartData(String mainConceptionKind, String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList, List<RelationEntity> relationEntityList){
        this.mainConceptionEntityUID = mainConceptionEntityUID;
        this.mainConceptionKind = mainConceptionKind;
        this.conceptionEntityList = conceptionEntityList;
        this.relationEntityList = relationEntityList;
        return generateGraphData();
    }

    private Map<String,Object> generateGraphData(){
        Map<String,Object> valueMap =new HashMap<>();
        List<Map<String,String>> nodeInfoList = new ArrayList<>();
        Map<String,String> centerNodeInfo = new HashMap<>();
        centerNodeInfo.put("id",this.mainConceptionEntityUID);
        centerNodeInfo.put("entityKind",this.mainConceptionKind);
        centerNodeInfo.put("color","#888888");
        nodeInfoList.add(centerNodeInfo);

        List<String> attachedConceptionKinds = new ArrayList<>();
        for(ConceptionEntity currentConceptionEntity:this.conceptionEntityList){
            if(!attachedConceptionKinds.contains(currentConceptionEntity.getConceptionKindName())){
                attachedConceptionKinds.add(currentConceptionEntity.getConceptionKindName());
            }
        }
        generateConceptionKindColorMap(attachedConceptionKinds);

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

        List<Map<String,String>> edgeInfoList = new ArrayList<>();

        List<String> attachedRelationKinds = new ArrayList<>();
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
        valueMap.put("nodes",nodeInfoList);
        valueMap.put("links",edgeInfoList);
        return valueMap;
    }

    private void generateConceptionKindColorMap(List<String> attachedConceptionKinds){
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
    }

    private void generateRelationKindColorMap(List<String> attachedRelationKinds){
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
    }
}
