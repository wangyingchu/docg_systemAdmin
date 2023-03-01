package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JavaScript("./visualization/feature/relationKindCorrelationInfoChart-connector.js")
public class RelationKindCorrelationInfoChart extends VerticalLayout {

    public RelationKindCorrelationInfoChart(int chartHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(chartHeight, Unit.PIXELS);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_RelationKindCorrelationInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void clearData(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.clearData", ""));
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet){
        if(conceptionKindCorrelationInfoSet != null && conceptionKindCorrelationInfoSet.size() > 0){
            JsonObject obj = Json.createObject();
            JsonArray linkDataArray = Json.createArray();
            JsonArray dataDataArray = Json.createArray();
            int idx_link = 0;
            int idx_data = 0;
            List<String> conceptionKindList = new ArrayList<>();
            List<String> sourceConceptionKindList = new ArrayList<>();

            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                if(!conceptionKindList.contains(sourceKindName)){
                    JsonObject jsonObject = Json.createObject();
                    jsonObject.put("name",sourceKindName);
                    dataDataArray.set(idx_data, jsonObject);
                    idx_data ++;
                    conceptionKindList.add(sourceKindName);
                    sourceConceptionKindList.add(sourceKindName);
                }
            }
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String targetKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                String targetConceptionKindRealName = sourceConceptionKindList.contains(targetKindName) ? targetKindName+"(1)":targetKindName;
                if(!conceptionKindList.contains(targetConceptionKindRealName)){
                    JsonObject jsonObject = Json.createObject();
                    jsonObject.put("name",targetConceptionKindRealName);
                    dataDataArray.set(idx_data, jsonObject);
                    idx_data ++;
                    conceptionKindList.add(targetConceptionKindRealName);
                }
            }

            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                long relationCount = currentConceptionKindCorrelationInfo.getRelationEntityCount();
                String targetConceptionKindRealName = sourceConceptionKindList.contains(targetKindName) ? targetKindName+"(1)":targetKindName;
                JsonObject linkJsonObject = Json.createObject();
                linkJsonObject.put("source",sourceKindName);
                if(sourceKindName.equals(targetKindName)){
                    linkJsonObject.put("target",sourceKindName+"(1)");
                }else{
                    linkJsonObject.put("target",targetConceptionKindRealName);
                }
                linkJsonObject.put("value",relationCount);
                linkDataArray.set(idx_link, linkJsonObject);
                idx_link++;
            }
            obj.put("links", linkDataArray);
            obj.put("data", dataDataArray);
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", obj));
        }else{
            clearData();
        }
    }
}
