package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.NodePayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JavaScript("./visualization/feature/conceptionKindCorrelationInfoChart-connector.js")
public class ConceptionKindCorrelationInfoChart extends VerticalLayout {

    private List<String> conceptionKindIdList;

    public ConceptionKindCorrelationInfoChart(int chartHeight){
        conceptionKindIdList = new ArrayList<>();
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.21.1/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(chartHeight,Unit.PIXELS);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionKindCorrelationInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void clearData(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.clearData", "null"));
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet, String targetConceptionKind){
        conceptionKindIdList.clear();
        if(conceptionKindCorrelationInfoSet!= null){
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceConceptionKindId = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetConceptionKindId = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                if(!conceptionKindIdList.contains(sourceConceptionKindId)){
                    NodePayload nodePayload=new NodePayload();
                    nodePayload.getData().put("shape","ellipse");
                    nodePayload.getData().put("background_color","#c00");
                    if(targetConceptionKind.equals(sourceConceptionKindId)){
                        nodePayload.getData().put("shape","pentagon");
                        nodePayload.getData().put("background_color","#777777");
                    }
                    if(sourceConceptionKindId.startsWith("DOCG_")){
                        nodePayload.getData().put("shape","diamond");
                        nodePayload.getData().put("background_color","#FF8C00");
                    }
                    nodePayload.getData().put("id",sourceConceptionKindId);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(nodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    conceptionKindIdList.add(sourceConceptionKindId);
                }
                if(!conceptionKindIdList.contains(targetConceptionKindId)){
                    NodePayload nodePayload=new NodePayload();
                    nodePayload.getData().put("shape","ellipse");
                    nodePayload.getData().put("background_color","#c00");
                    if(targetConceptionKind.equals(targetConceptionKindId)){
                        nodePayload.getData().put("shape","pentagon");
                        nodePayload.getData().put("background_color","#777777");
                    }
                    if(targetConceptionKindId.startsWith("DOCG_")){
                        nodePayload.getData().put("shape","diamond");
                        nodePayload.getData().put("background_color","#FF8C00");
                    }
                    nodePayload.getData().put("id",targetConceptionKindId);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(nodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    conceptionKindIdList.add(targetConceptionKindId);
                }
                EdgePayload edgePayload=new EdgePayload();
                edgePayload.getData().put("type", currentConceptionKindCorrelationInfo.getRelationKindName());
                edgePayload.getData().put("source", sourceConceptionKindId);
                edgePayload.getData().put("target", targetConceptionKindId);
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(edgePayload)});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.layoutGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
