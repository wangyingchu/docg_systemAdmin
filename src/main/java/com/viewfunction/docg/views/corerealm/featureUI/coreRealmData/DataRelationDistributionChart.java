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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@JavaScript("./visualization/feature/dataRelationDistributionChart-connector.js")
public class DataRelationDistributionChart extends VerticalLayout {

    private Map<String,String> conceptionKindColorMap;

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
            for(String currentConceptionKindName:conceptionKindNameSet){
                CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                cytoscapeNodePayload.getData().put("shape","ellipse");
                cytoscapeNodePayload.getData().put("background_color","#c00");
                cytoscapeNodePayload.getData().put("size","4");
                if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionKindName)!=null){
                    cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(currentConceptionKindName));
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

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_DataRelationDistributionChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
