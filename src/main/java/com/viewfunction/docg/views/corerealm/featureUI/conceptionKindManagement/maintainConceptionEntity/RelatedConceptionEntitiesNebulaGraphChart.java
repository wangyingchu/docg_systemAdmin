package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@StyleSheet("webApps/relatedConceptionEntitiesNebulaGraphChart/style.css")
@JavaScript("./visualization/feature/relatedConceptionEntitiesNebulaGraphChart-connector.js")
public class RelatedConceptionEntitiesNebulaGraphChart extends VerticalLayout {
    private String mainConceptionKind;
    private String mainConceptionEntityUID;
    private List<ConceptionEntity> conceptionEntityList;
    private List<RelationEntity> relationEntityList;
    private Registration listener;
    private int colorIndex = 0;
    private Map<String,String> conceptionKindColorMap;
    public RelatedConceptionEntitiesNebulaGraphChart(String mainConceptionKind,String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList,List<RelationEntity> relationEntityList){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.mainConceptionEntityUID = mainConceptionEntityUID;
        this.mainConceptionKind = mainConceptionKind;
        this.conceptionEntityList = conceptionEntityList;
        this.relationEntityList = relationEntityList;
        this.conceptionKindColorMap = new HashMap<>();
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/three-spritetext.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/3d-force-graph.min.js");
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_RelatedConceptionEntitiesNebulaGraphChart.initLazy($0)", getElement()));
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
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void generateGraph(int height,int width){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Object> valueMap =new HashMap<>();
                List<Map<String,String>> nodeInfoList = new ArrayList<>();
                Map<String,String> centerNodeInfo = new HashMap<>();
                centerNodeInfo.put("id",this.mainConceptionEntityUID);
                centerNodeInfo.put("entityKind",this.mainConceptionKind);
                centerNodeInfo.put("color","#555555");
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
                for(RelationEntity currentRelationEntity:this.relationEntityList){
                    Map<String,String> currentEdgeInfo = new HashMap<>();
                    currentEdgeInfo.put("source",currentRelationEntity.getFromConceptionEntityUID());
                    currentEdgeInfo.put("target",currentRelationEntity.getToConceptionEntityUID());
                    currentEdgeInfo.put("color","#666666");
                    edgeInfoList.add(currentEdgeInfo);
                }

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
}
