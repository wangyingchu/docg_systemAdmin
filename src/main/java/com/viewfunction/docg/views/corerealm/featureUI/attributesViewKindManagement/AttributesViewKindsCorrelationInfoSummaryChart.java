package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributesViewKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JavaScript("./visualization/feature/attributesViewKindCorrelationInfoChart-connector.js")
public class AttributesViewKindsCorrelationInfoSummaryChart extends Div {
    private List<String> conceptionKindIdList;
    private List<String> attributeKindUIdList;
    public AttributesViewKindsCorrelationInfoSummaryChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.23.0/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setHeight(windowHeight, Unit.PIXELS);
        this.setWidth(windowWidth, Unit.PIXELS);

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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadAttributesViewKindsCorrelationInfoSummaryChartData();
    }

    private void loadAttributesViewKindsCorrelationInfoSummaryChartData(){
        List<AttributesViewKind> attributesViewKindList = new ArrayList<>();
        Map<String,List<ConceptionKind>> containerConceptionKindListMap = new HashMap<>();
        Map<String,List<AttributeKind>> containsAttributeKindListMap = new HashMap<>();
        try {
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            List<AttributesViewKindMetaInfo> attributesViewKindMetaInfoList = coreRealm.getAttributesViewKindsMetaInfo();
            if(attributesViewKindMetaInfoList != null){
                for(AttributesViewKindMetaInfo currentAttributesViewKindMetaInfo:attributesViewKindMetaInfoList){
                    String currentAttributesViewKindUID = currentAttributesViewKindMetaInfo.getKindUID();
                    AttributesViewKind currentAttributesViewKind = coreRealm.getAttributesViewKind(currentAttributesViewKindUID);
                    if(currentAttributesViewKind != null){
                        attributesViewKindList.add(currentAttributesViewKind);
                        List<AttributeKind> attributeKindList = currentAttributesViewKind.getContainsAttributeKinds();
                        if(attributeKindList != null){
                            containsAttributeKindListMap.put(currentAttributesViewKindUID,attributeKindList);
                        }
                        List<ConceptionKind> conceptionKindList = currentAttributesViewKind.getContainerConceptionKinds();
                        if(conceptionKindList != null){
                            containerConceptionKindListMap.put(currentAttributesViewKindUID,conceptionKindList);
                        }
                    }
                }
            }
            coreRealm.closeGlobalSession();
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        for(AttributesViewKind targetAttributesViewKind:attributesViewKindList){
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
            List<ConceptionKind> containerConceptionKindsList = containerConceptionKindListMap.get(targetAttributesViewKind.getAttributesViewKindUID());
            if(containerConceptionKindsList != null){
                for(ConceptionKind currentConceptionKind:containerConceptionKindsList){
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
                    }
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
            List<AttributeKind> containsAttributeKindList = containsAttributeKindListMap.get(targetAttributesViewKind.getAttributesViewKindUID());
            if(containsAttributeKindList != null){
                for(AttributeKind currentAttributeKind : containsAttributeKindList){
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
                    }
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
