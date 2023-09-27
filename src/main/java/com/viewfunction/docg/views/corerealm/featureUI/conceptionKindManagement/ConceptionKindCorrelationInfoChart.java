package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JavaScript("./visualization/feature/conceptionKindCorrelationInfoChart-connector.js")
public class ConceptionKindCorrelationInfoChart extends VerticalLayout {

    private List<String> conceptionKindIdList;

    public ConceptionKindCorrelationInfoChart(int chartHeight){
        conceptionKindIdList = new ArrayList<>();
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.23.0/dist/cytoscape.min.js");
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
                if(!sourceConceptionKindId.equals(RealmConstant.ConceptionKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.AttributesViewKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.AttributeKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.RelationKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !sourceConceptionKindId.equals(RealmConstant.ClassificationClass)){
                    if(!conceptionKindIdList.contains(sourceConceptionKindId)){
                        CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                        cytoscapeNodePayload.getData().put("shape","round-octagon");
                        cytoscapeNodePayload.getData().put("background_color","#c00");
                        if(targetConceptionKind.equals(sourceConceptionKindId)){
                            cytoscapeNodePayload.getData().put("shape","pentagon");
                            cytoscapeNodePayload.getData().put("background_color","#777777");
                        }

                        if(sourceConceptionKindId.startsWith("DOCG_TS_")){
                            cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                            cytoscapeNodePayload.getData().put("shape","round-tag");
                        }
                        if(sourceConceptionKindId.startsWith("DOCG_GS_")){
                            cytoscapeNodePayload.getData().put("background_color","#C71585");
                            cytoscapeNodePayload.getData().put("shape","round-octagon");
                        }
                        if(sourceConceptionKindId.startsWith(RealmConstant.TimeScaleEventClass)){
                            cytoscapeNodePayload.getData().put("shape","round-diamond");
                            cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                        }
                        if(sourceConceptionKindId.startsWith(RealmConstant.TimeScaleEntityClass)){
                            cytoscapeNodePayload.getData().put("shape","barrel");
                            cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                        }
                        if(sourceConceptionKindId.startsWith(RealmConstant.GeospatialScaleEventClass)){
                            cytoscapeNodePayload.getData().put("shape","round-diamond");
                            cytoscapeNodePayload.getData().put("background_color","#C71585");
                        }
                        if(sourceConceptionKindId.startsWith(RealmConstant.GeospatialScaleEntityClass)){
                            cytoscapeNodePayload.getData().put("shape","barrel");
                            cytoscapeNodePayload.getData().put("background_color","#C71585");
                        }

                        cytoscapeNodePayload.getData().put("id",sourceConceptionKindId);
                        runBeforeClientResponse(ui -> {
                            try {
                                getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        conceptionKindIdList.add(sourceConceptionKindId);
                    }
                }

                if(!targetConceptionKindId.equals(RealmConstant.ConceptionKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.AttributesViewKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.AttributeKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.RelationKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !targetConceptionKindId.equals(RealmConstant.ClassificationClass)){
                    if(!conceptionKindIdList.contains(targetConceptionKindId)){
                        CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                        cytoscapeNodePayload.getData().put("shape","round-octagon");
                        cytoscapeNodePayload.getData().put("background_color","#c00");
                        if(targetConceptionKind.equals(targetConceptionKindId)){
                            cytoscapeNodePayload.getData().put("shape","pentagon");
                            cytoscapeNodePayload.getData().put("background_color","#777777");
                        }

                        if(targetConceptionKindId.startsWith("DOCG_TS_")){
                            cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                            cytoscapeNodePayload.getData().put("shape","round-tag");
                        }
                        if(targetConceptionKindId.startsWith("DOCG_GS_")){
                            cytoscapeNodePayload.getData().put("background_color","#C71585");
                            cytoscapeNodePayload.getData().put("shape","round-octagon");
                        }
                        if(targetConceptionKindId.startsWith(RealmConstant.TimeScaleEventClass)){
                            cytoscapeNodePayload.getData().put("shape","round-diamond");
                            cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                        }
                        if(targetConceptionKindId.startsWith(RealmConstant.TimeScaleEntityClass)){
                            cytoscapeNodePayload.getData().put("shape","barrel");
                            cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                        }
                        if(targetConceptionKindId.startsWith(RealmConstant.GeospatialScaleEventClass)){
                            cytoscapeNodePayload.getData().put("shape","round-diamond");
                            cytoscapeNodePayload.getData().put("background_color","#C71585");
                        }
                        if(targetConceptionKindId.startsWith(RealmConstant.GeospatialScaleEntityClass)){
                            cytoscapeNodePayload.getData().put("shape","barrel");
                            cytoscapeNodePayload.getData().put("background_color","#C71585");
                        }

                        cytoscapeNodePayload.getData().put("id",targetConceptionKindId);
                        runBeforeClientResponse(ui -> {
                            try {
                                getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        conceptionKindIdList.add(targetConceptionKindId);
                    }
                }

                CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                cytoscapeEdgePayload.getData().put("type", currentConceptionKindCorrelationInfo.getRelationKindName());
                cytoscapeEdgePayload.getData().put("source", sourceConceptionKindId);
                cytoscapeEdgePayload.getData().put("target", targetConceptionKindId);
                if(!sourceConceptionKindId.equals(RealmConstant.ConceptionKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.AttributesViewKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.AttributeKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.RelationKindClass)
                        && !sourceConceptionKindId.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !sourceConceptionKindId.equals(RealmConstant.ClassificationClass)

                        && !targetConceptionKindId.equals(RealmConstant.ConceptionKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.AttributesViewKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.AttributeKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.RelationKindClass)
                        && !targetConceptionKindId.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !targetConceptionKindId.equals(RealmConstant.ClassificationClass)
                ){
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
