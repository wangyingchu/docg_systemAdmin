package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import de.xinblue.cytoscape.model.Edge;
import de.xinblue.cytoscape.model.Node;

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
                    Node node=new Node();
                    node.getData().put("shape","ellipse");
                    node.getData().put("background_color","#c00");
                    if(targetConceptionKind.equals(sourceConceptionKindId)){
                        node.getData().put("shape","pentagon");
                        node.getData().put("background_color","#777777");
                    }
                    if(sourceConceptionKindId.startsWith("DOCG_")){
                        node.getData().put("shape","diamond");
                        node.getData().put("background_color","#FF8C00");
                    }
                    node.getData().put("id",sourceConceptionKindId);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(node)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    conceptionKindIdList.add(sourceConceptionKindId);
                }
                if(!conceptionKindIdList.contains(targetConceptionKindId)){
                    Node node=new Node();
                    node.getData().put("shape","ellipse");
                    node.getData().put("background_color","#c00");
                    if(targetConceptionKind.equals(targetConceptionKindId)){
                        node.getData().put("shape","pentagon");
                        node.getData().put("background_color","#777777");
                    }
                    if(targetConceptionKindId.startsWith("DOCG_")){
                        node.getData().put("shape","diamond");
                        node.getData().put("background_color","#FF8C00");
                    }
                    node.getData().put("id",targetConceptionKindId);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(node)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    conceptionKindIdList.add(targetConceptionKindId);
                }
                Edge currentEdge=new Edge();
                currentEdge.getData().put("type", currentConceptionKindCorrelationInfo.getRelationKindName());
                currentEdge.getData().put("source", sourceConceptionKindId);
                currentEdge.getData().put("target", targetConceptionKindId);
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(currentEdge)});
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
