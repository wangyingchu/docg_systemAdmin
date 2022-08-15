package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JavaScript("./visualization/feature/conceptionEntityRelationsChart-connector.js")
public class ConceptionEntityRelationsChart extends VerticalLayout {

    List<String> conceptionEntityUIDList;
    List<String> relationEntityUIDList;

    public ConceptionEntityRelationsChart(){
        conceptionEntityUIDList = new ArrayList<>();
        relationEntityUIDList = new ArrayList<>();
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.22.1/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(700, Unit.PIXELS);
        //this.setHeight(chartHeight, Unit.PIXELS);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntityRelationsChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void clearData(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.clearData", "null"));
    }

    public void setData(List<RelationEntity> conceptionEntityRelationEntityList){
        if(conceptionEntityRelationEntityList != null){
            for(RelationEntity currentRelationEntity:conceptionEntityRelationEntityList){
                String relationKind = currentRelationEntity.getRelationKindName();
                String relationEntityUID = currentRelationEntity.getRelationEntityUID();

                List<String> fromConceptionEntityKind = currentRelationEntity.getFromConceptionEntityKinds();
                String fromConceptionEntityUID = currentRelationEntity.getFromConceptionEntityUID();

                List<String> toConceptionEntityKind = currentRelationEntity.getToConceptionEntityKinds();
                String toConceptionEntityUID = currentRelationEntity.getToConceptionEntityUID();

                if(!conceptionEntityUIDList.contains(fromConceptionEntityUID)){
                    conceptionEntityUIDList.add(fromConceptionEntityUID);
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                   // if(targetConceptionKind.equals(sourceConceptionKindId)){
                    //    cytoscapeNodePayload.getData().put("shape","pentagon");
                   //     cytoscapeNodePayload.getData().put("background_color","#777777");
                  //  }
                   // if(sourceConceptionKindId.startsWith("DOCG_")){
                   //     cytoscapeNodePayload.getData().put("shape","diamond");
                   //     cytoscapeNodePayload.getData().put("background_color","#FF8C00");
                   // }
                    cytoscapeNodePayload.getData().put("id",fromConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if(!conceptionEntityUIDList.contains(toConceptionEntityUID)){
                    conceptionEntityUIDList.add(toConceptionEntityUID);
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayload.getData().put("shape","ellipse");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    // if(targetConceptionKind.equals(sourceConceptionKindId)){
                    //    cytoscapeNodePayload.getData().put("shape","pentagon");
                    //     cytoscapeNodePayload.getData().put("background_color","#777777");
                    //  }
                    // if(sourceConceptionKindId.startsWith("DOCG_")){
                    //     cytoscapeNodePayload.getData().put("shape","diamond");
                    //     cytoscapeNodePayload.getData().put("background_color","#FF8C00");
                    // }
                    cytoscapeNodePayload.getData().put("id",toConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeNodePayload)});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if(!relationEntityUIDList.contains(relationEntityUID)){
                    relationEntityUIDList.add(relationEntityUID);
                    CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                    cytoscapeEdgePayload.getData().put("type", relationKind);
                    cytoscapeEdgePayload.getData().put("source", fromConceptionEntityUID);
                    cytoscapeEdgePayload.getData().put("target", toConceptionEntityUID);
                    runBeforeClientResponse(ui -> {
                        try {
                            getElement().callJsFunction("$connector.setData", new Serializable[]{(new ObjectMapper()).writeValueAsString(cytoscapeEdgePayload)});
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
}
