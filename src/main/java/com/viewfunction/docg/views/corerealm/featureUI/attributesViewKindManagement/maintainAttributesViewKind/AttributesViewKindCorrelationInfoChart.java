package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JavaScript("./visualization/feature/attributesViewKindCorrelationInfoChart-connector.js")
public class AttributesViewKindCorrelationInfoChart extends VerticalLayout {

    private List<String> conceptionKindIdList;
    private List<String> attributeKindUIdList;
    private AttributesViewKind targetAttributesViewKind;

    public AttributesViewKindCorrelationInfoChart(int chartHeight){
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.23.0/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(chartHeight, Unit.PIXELS);

        conceptionKindIdList = new ArrayList<>();
        attributeKindUIdList = new ArrayList<>();

        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_AttributesViewKindCorrelationInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setData(AttributesViewKind targetAttributesViewKind, List<ConceptionKind> containerConceptionKindsList,
                        List<AttributeKind> containsAttributeKindsList){
        conceptionKindIdList.clear();
        attributeKindUIdList.clear();
        this.targetAttributesViewKind = targetAttributesViewKind;
        if(this.targetAttributesViewKind != null){
            CytoscapeNodePayload targetAttributesViewKindCytoscapeNodePayload =new CytoscapeNodePayload();
            targetAttributesViewKindCytoscapeNodePayload.getData().put("shape","round-diamond");
            targetAttributesViewKindCytoscapeNodePayload.getData().put("background_color","#666666");
            targetAttributesViewKindCytoscapeNodePayload.getData().put("id",targetAttributesViewKind.getAttributesViewKindUID());
            targetAttributesViewKindCytoscapeNodePayload.getData().put("desc",targetAttributesViewKind.getAttributesViewKindName()+"\n ( "+targetAttributesViewKind.getAttributesViewKindUID()+" )");
            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(targetAttributesViewKindCytoscapeNodePayload)});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            if(containerConceptionKindsList != null){
                for(ConceptionKind currentConceptionKind : containerConceptionKindsList){
                    if(!conceptionKindIdList.contains(currentConceptionKind.getConceptionKindName())){
                        CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                        cytoscapeNodePayload.getData().put("shape","round-octagon");
                        cytoscapeNodePayload.getData().put("background_color","#c23531");
                        cytoscapeNodePayload.getData().put("id",currentConceptionKind.getConceptionKindName());
                        cytoscapeNodePayload.getData().put("desc",currentConceptionKind.getConceptionKindName());
                        runBeforeClientResponse(ui -> {
                            try {
                                getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        conceptionKindIdList.add(currentConceptionKind.getConceptionKindName());
                        CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                        cytoscapeEdgePayload.getData().put("type", "包含属性视图类型");
                        cytoscapeEdgePayload.getData().put("source", currentConceptionKind.getConceptionKindName());
                        cytoscapeEdgePayload.getData().put("target", targetAttributesViewKind.getAttributesViewKindUID());
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
            if(containsAttributeKindsList != null){
                for(AttributeKind currentAttributeKind : containsAttributeKindsList){
                    if(!attributeKindUIdList.contains(currentAttributeKind.getAttributeKindUID())){
                        CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                        cytoscapeNodePayload.getData().put("shape","rhomboid");
                        cytoscapeNodePayload.getData().put("background_color","#689d6a");
                        cytoscapeNodePayload.getData().put("id",currentAttributeKind.getAttributeKindUID());
                        cytoscapeNodePayload.getData().put("desc",currentAttributeKind.getAttributeKindName()+"\n ( "+currentAttributeKind.getAttributeKindUID()+" )");
                        runBeforeClientResponse(ui -> {
                            try {
                                getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        attributeKindUIdList.add(currentAttributeKind.getAttributeKindUID());
                        CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                        cytoscapeEdgePayload.getData().put("type", "包含属性类型");
                        cytoscapeEdgePayload.getData().put("source", targetAttributesViewKind.getAttributesViewKindUID());
                        cytoscapeEdgePayload.getData().put("target", currentAttributeKind.getAttributeKindUID());
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

            runBeforeClientResponse(ui -> {
                try {
                    getElement().callJsFunction("$connector.layoutGraph", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
