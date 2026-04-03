package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.*;

@JavaScript("./visualization/feature/relationKindCorrelationInfoChart-connector.js")
public class RelationKindCorrelationInfoChart extends VerticalLayout {

    private static final ObjectMapper mapper = new ObjectMapper();

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
            ObjectNode obj = mapper.createObjectNode();
            ArrayNode linkDataArray = mapper.createArrayNode();
            ArrayNode dataDataArray = mapper.createArrayNode();
            int idx_link = 0;
            int idx_data = 0;
            List<String> conceptionKindList = new ArrayList<>();
            List<String> sourceConceptionKindList = new ArrayList<>();
            List<String> target_SourceKindNameMappingList = new ArrayList<>();
            List<String> conceptionKindsInRelation = new ArrayList<>();
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                if(!sourceKindName.equals(RealmConstant.ConceptionKindClass)
                        && !sourceKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !sourceKindName.equals(RealmConstant.AttributeKindClass)
                        && !sourceKindName.equals(RealmConstant.RelationKindClass)
                        && !sourceKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        //&& !sourceKindName.equals(RealmConstant.ClassificationClass)

                        && !targetKindName.equals(RealmConstant.ConceptionKindClass)
                        && !targetKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !targetKindName.equals(RealmConstant.AttributeKindClass)
                        && !targetKindName.equals(RealmConstant.RelationKindClass)
                        && !targetKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        //&& !targetKindName.equals(RealmConstant.ClassificationClass)
                )
                {
                    conceptionKindsInRelation.add(sourceKindName);
                    conceptionKindsInRelation.add(targetKindName);
                    target_SourceKindNameMappingList.add(targetKindName+"<-"+sourceKindName);
                }
            }
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                if(conceptionKindsInRelation.contains(sourceKindName)){
                    if(!sourceKindName.equals(RealmConstant.ConceptionKindClass)
                            && !sourceKindName.equals(RealmConstant.AttributesViewKindClass)
                            && !sourceKindName.equals(RealmConstant.AttributeKindClass)
                            && !sourceKindName.equals(RealmConstant.RelationKindClass)
                            && !sourceKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                            //&& !sourceKindName.equals(RealmConstant.ClassificationClass)
                    )
                    {
                        if(!conceptionKindList.contains(sourceKindName)){
                            ObjectNode jsonObject = mapper.createObjectNode();
                            jsonObject.put("name",sourceKindName);
                            dataDataArray.insert(idx_data, jsonObject);
                            idx_data ++;
                            conceptionKindList.add(sourceKindName);
                            sourceConceptionKindList.add(sourceKindName);
                        }
                    }
                }
            }
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String targetKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                if(conceptionKindsInRelation.contains(targetKindName)){
                    if(! targetKindName.equals(RealmConstant.ConceptionKindClass)
                            && !targetKindName.equals(RealmConstant.AttributesViewKindClass)
                            && !targetKindName.equals(RealmConstant.AttributeKindClass)
                            && !targetKindName.equals(RealmConstant.RelationKindClass)
                            && !targetKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                            //&& !targetKindName.equals(RealmConstant.ClassificationClass)
                    )
                    {
                        if(target_SourceKindNameMappingList.contains(targetKindName+"<-"+sourceKindName)){
                            String targetConceptionKindRealName = sourceConceptionKindList.contains(targetKindName) ? targetKindName+"(1)":targetKindName;
                            if(!conceptionKindList.contains(targetConceptionKindRealName)){
                                ObjectNode jsonObject = mapper.createObjectNode();
                                jsonObject.put("name",targetConceptionKindRealName);
                                dataDataArray.insert(idx_data, jsonObject);
                                idx_data ++;
                                conceptionKindList.add(targetConceptionKindRealName);
                            }
                        }
                    }
                }
            }

            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                if(!sourceKindName.equals(RealmConstant.ConceptionKindClass)
                        && !sourceKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !sourceKindName.equals(RealmConstant.AttributeKindClass)
                        && !sourceKindName.equals(RealmConstant.RelationKindClass)
                        && !sourceKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        //&& !sourceKindName.equals(RealmConstant.ClassificationClass)

                        && !targetKindName.equals(RealmConstant.ConceptionKindClass)
                        && !targetKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !targetKindName.equals(RealmConstant.AttributeKindClass)
                        && !targetKindName.equals(RealmConstant.RelationKindClass)
                        && !targetKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        //&& !targetKindName.equals(RealmConstant.ClassificationClass)
                )
                {
                    long relationCount = currentConceptionKindCorrelationInfo.getRelationEntityCount();
                    String targetConceptionKindRealName = sourceConceptionKindList.contains(targetKindName) ? targetKindName+"(1)":targetKindName;
                    ObjectNode linkJsonObject = mapper.createObjectNode();
                    linkJsonObject.put("source",sourceKindName);
                    if(sourceKindName.equals(targetKindName)){
                        linkJsonObject.put("target",sourceKindName+"(1)");
                    }else{
                        linkJsonObject.put("target",targetConceptionKindRealName);
                    }
                    linkJsonObject.put("value",relationCount);
                    linkDataArray.insert(idx_link, linkJsonObject);
                    idx_link++;
                }
            }
            obj.set("links", linkDataArray);
            obj.set("data", dataDataArray);
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", obj));
        }else{
            clearData();
        }
    }
}
