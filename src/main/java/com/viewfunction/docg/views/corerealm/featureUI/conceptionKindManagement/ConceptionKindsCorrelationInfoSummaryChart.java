package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsRelationshipEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsRelationshipNodePayload;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.*;

@JavaScript("./visualization/feature/conceptionKindsCorrelationInfoSummaryChart-connector.js")
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
                "window.Vaadin.Flow.feature_ConceptionKindsCorrelationInfoSummaryChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet) {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Map<String,Long> conceptionKindDataCountMap = new HashMap<>();
        Map<String,String> conceptionKindDescMap = new HashMap<>();
        try {
            List<EntityStatisticsInfo> entityStatisticsInfoList = coreRealm.getConceptionEntitiesStatistics();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                conceptionKindDataCountMap.put(currentEntityStatisticsInfo.getEntityKindName(),currentEntityStatisticsInfo.getEntitiesCount());
                conceptionKindDescMap.put(currentEntityStatisticsInfo.getEntityKindName(),currentEntityStatisticsInfo.getEntityKindDesc());
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

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
            EchartsRelationshipEdgePayload currentEchartsRelationshipEdgePayload = new EchartsRelationshipEdgePayload(relationKindName,"",relationKindName+"_ID",sourceKindName+"_ID",targetKindName+"_ID",relationCount);
            currentEchartsRelationshipEdgePayload.getData().put("sourceConceptionKind",sourceKindName);
            currentEchartsRelationshipEdgePayload.getData().put("targetConceptionKind",targetKindName);

            JsonObject childJsonObject = currentEchartsRelationshipEdgePayload.toJson();
            linkDataArray.set(idx_relation, childJsonObject);
            idx_relation++;
            if(!conceptionKindList.contains(sourceKindName)){
                if(conceptionKindDataCountMap.containsKey(sourceKindName) && conceptionKindDataCountMap.get(sourceKindName) > 0){
                    String conceptionKindDesc = conceptionKindDescMap.get(sourceKindName) != null ? conceptionKindDescMap.get(sourceKindName):"";
                    long nodeWeight = (long)(Math.log(conceptionKindDataCountMap.get(sourceKindName))*2.5);
                    EchartsRelationshipNodePayload currentEchartsRelationshipNodePayload = new EchartsRelationshipNodePayload(sourceKindName,conceptionKindDesc,sourceKindName+"_ID","",nodeWeight);
                    currentEchartsRelationshipNodePayload.getData().put("entityCount",conceptionKindDataCountMap.get(sourceKindName));
                    JsonObject childJsonObject2 = currentEchartsRelationshipNodePayload.toJson();
                    nodeDataArray.set(idx_node, childJsonObject2);
                    idx_node++;
                    conceptionKindList.add(sourceKindName);
                }
            }

            if(!conceptionKindList.contains(targetKindName)){
                if(conceptionKindDataCountMap.containsKey(targetKindName) && conceptionKindDataCountMap.get(targetKindName) > 0){
                    String conceptionKindDesc = conceptionKindDescMap.get(targetKindName) != null ? conceptionKindDescMap.get(targetKindName):"";
                    long nodeWeight = (long)(Math.log(conceptionKindDataCountMap.get(targetKindName))*2.5);
                    EchartsRelationshipNodePayload currentEchartsRelationshipNodePayload = new EchartsRelationshipNodePayload(targetKindName,conceptionKindDesc,targetKindName+"_ID","",nodeWeight);
                    currentEchartsRelationshipNodePayload.getData().put("entityCount",conceptionKindDataCountMap.get(targetKindName));
                    JsonObject childJsonObject2 = currentEchartsRelationshipNodePayload.toJson();
                    nodeDataArray.set(idx_node, childJsonObject2);
                    idx_node++;
                    conceptionKindList.add(targetKindName);
                }
            }
        }
        obj.put("links", linkDataArray);
        obj.put("nodes", nodeDataArray);

        setNodeNameLabel("概念类型");
        setEdgeNameLabel("关系类型");
        setNodeWeightLabel("实体数量");
        setEdgeSourceLabel("源概念类型");
        setEdgeTargetLabel("目标概念类型");
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
