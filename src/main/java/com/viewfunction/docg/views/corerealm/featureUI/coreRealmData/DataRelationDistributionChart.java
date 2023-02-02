package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.io.Serializable;
import java.util.*;

@JavaScript("./visualization/feature/dataRelationDistributionChart-connector.js")
public class DataRelationDistributionChart extends VerticalLayout {

    private Map<String,String> conceptionKindColorMap;
    private int colorIndex = 0;

    public DataRelationDistributionChart(){
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.22.1/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.conceptionKindColorMap = new HashMap<>();
        this.setHeight(100, Unit.PERCENTAGE);
        initConnector();
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet, Map<String, Long> conceptionKindsDataCount,Map<String, Long> relationKindsDataCount){
        if(conceptionKindsDataCount != null){
            Set<String> conceptionKindNameSet = conceptionKindsDataCount.keySet();
            generateConceptionKindColorMap(conceptionKindNameSet);

            CytoscapeNodePayload gs_cytoscapeNodePayload =new CytoscapeNodePayload();
            gs_cytoscapeNodePayload.getData().put("shape","ellipse");
            //gs_cytoscapeNodePayload.getData().put("background_color","#c00");
            gs_cytoscapeNodePayload.getData().put("id","GS");
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(gs_cytoscapeNodePayload)});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            CytoscapeNodePayload ts_cytoscapeNodePayload =new CytoscapeNodePayload();
            ts_cytoscapeNodePayload.getData().put("shape","ellipse");
            //gs_cytoscapeNodePayload.getData().put("background_color","#c00");
            ts_cytoscapeNodePayload.getData().put("id","TS");
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(ts_cytoscapeNodePayload)});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            for(String currentConceptionKindName:conceptionKindNameSet){
                if(!currentConceptionKindName.equals(RealmConstant.ConceptionKindClass)
                && !currentConceptionKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.AttributeKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.RelationKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !currentConceptionKindName.equals(RealmConstant.ClassificationClass)){
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    cytoscapeNodePayload.getData().put("size", ""+Math.log10(conceptionKindsDataCount.get(currentConceptionKindName)));
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionKindName)!=null){
                        cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(currentConceptionKindName));
                    }
                    if(currentConceptionKindName.startsWith("DOCG_TS_")){
                        cytoscapeNodePayload.getData().put("background_color","#40E0D0");

                        cytoscapeNodePayload.getData().put("shape","round-tag");
                        cytoscapeNodePayload.getData().put("parent","TS");
                    }

                    if(currentConceptionKindName.startsWith("DOCG_GS_")){
                        cytoscapeNodePayload.getData().put("background_color","#C71585");
                        cytoscapeNodePayload.getData().put("shape","round-octagon");
                        cytoscapeNodePayload.getData().put("parent","GS");
                    }

                    if(currentConceptionKindName.startsWith(RealmConstant.TimeScaleEventClass)){
                        cytoscapeNodePayload.getData().put("shape","round-diamond");
                        cytoscapeNodePayload.getData().put("size","3");
                        cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.TimeScaleEntityClass)){
                        cytoscapeNodePayload.getData().put("shape","barrel");
                        cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.GeospatialScaleEventClass)){
                        cytoscapeNodePayload.getData().put("shape","round-diamond");
                        cytoscapeNodePayload.getData().put("size","3");
                        cytoscapeNodePayload.getData().put("background_color","#C71585");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.GeospatialScaleEntityClass)){
                        cytoscapeNodePayload.getData().put("shape","barrel");
                        cytoscapeNodePayload.getData().put("background_color","#C71585");
                    }
                    cytoscapeNodePayload.getData().put("id",currentConceptionKindName);
                    cytoscapeNodePayload.getData().put("kind",currentConceptionKindName);
                    cytoscapeNodePayload.getData().put("desc",currentConceptionKindName);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
        if(conceptionKindCorrelationInfoSet != null){
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceConceptionKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetConceptionKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                String relationKindName = currentConceptionKindCorrelationInfo.getRelationKindName();
                long relationEntityCount = currentConceptionKindCorrelationInfo.getRelationEntityCount();

                CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                cytoscapeEdgePayload.getData().put("type", relationKindName);
                cytoscapeEdgePayload.getData().put("source", sourceConceptionKindName);
                cytoscapeEdgePayload.getData().put("target", targetConceptionKindName);
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

    private Map<String,String> generateConceptionKindColorMap(Set<String> attachedConceptionKindsSet){
        List<String> attachedConceptionKinds = new ArrayList<String>();
        attachedConceptionKinds.addAll(attachedConceptionKindsSet);

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

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_DataRelationDistributionChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
