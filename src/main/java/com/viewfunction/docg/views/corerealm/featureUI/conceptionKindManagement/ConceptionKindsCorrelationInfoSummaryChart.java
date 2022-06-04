package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.visualizationComponent.payload.common.RelationshipEdgeData;
import com.viewfunction.docg.element.visualizationComponent.payload.common.RelationshipNodeData;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JavaScript("./visualization/widget/echarts/circulargraphchart-connector.js")
public class ConceptionKindsCorrelationInfoSummaryChart extends Div {

    public ConceptionKindsCorrelationInfoSummaryChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.3.2/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.3.2/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.3.2/dist/extension/bmap.min.js");
        setWidth(windowWidth,Unit.PIXELS);
        setHeight(windowHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.eCharts_CircularGraphChartConnector.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet) {
        JsonObject obj = Json.createObject();
        JsonArray linkDataArray = Json.createArray();
        JsonArray nodeDataArray = Json.createArray();
        int idx_relation = 0;
        int idx_node = 0;
        List<String> conceptionKindList = new ArrayList<>();
        for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
            String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
            String targetKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
            String relationKindName = currentConceptionKindCorrelationInfo.getRelationKindName();
            long relationCount = currentConceptionKindCorrelationInfo.getRelationEntityCount();
            RelationshipEdgeData currentRelationshipEdgeData = new RelationshipEdgeData(relationKindName,"",relationKindName+"_ID",sourceKindName+"_ID",targetKindName+"_ID",(int)relationCount);
            JsonObject childJsonObject = currentRelationshipEdgeData.toJson();
            linkDataArray.set(idx_relation, childJsonObject);
            idx_relation++;

            if(!conceptionKindList.contains(sourceKindName)){
                RelationshipNodeData currentRelationshipNodeData = new RelationshipNodeData(sourceKindName,"",sourceKindName+"_ID","",1);
                JsonObject childJsonObject2 = currentRelationshipNodeData.toJson();
                nodeDataArray.set(idx_node, childJsonObject2);
                idx_node++;
                conceptionKindList.add(sourceKindName);
            }

            if(!conceptionKindList.contains(targetKindName)){
                RelationshipNodeData currentRelationshipNodeData = new RelationshipNodeData(targetKindName,"",targetKindName+"_ID","",1);
                JsonObject childJsonObject2 = currentRelationshipNodeData.toJson();
                nodeDataArray.set(idx_node, childJsonObject2);
                idx_node++;
                conceptionKindList.add(targetKindName);
            }
        }
        obj.put("links", linkDataArray);
        obj.put("nodes", nodeDataArray);
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", obj));
    }

    public void setNodeNameLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setNodeNameLabel", labelValue));
    }

    public void setNodeDescLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setNodeDescLabel", labelValue));
    }

    public void setNodeWeightLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setNodeWeightLabel", labelValue));
    }

    public void setEdgeDescLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setEdgeDescLabel", labelValue));
    }

    public void setEdgeNameLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setEdgeNameLabel", labelValue));
    }

    public void setEdgeIdLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setEdgeIdLabel", labelValue));
    }

    public void setEdgeSourceLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setEdgeSourceLabel", labelValue));
    }

    public void setEdgeTargetLabel(String labelValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setEdgeTargetLabel", labelValue));
    }
}
