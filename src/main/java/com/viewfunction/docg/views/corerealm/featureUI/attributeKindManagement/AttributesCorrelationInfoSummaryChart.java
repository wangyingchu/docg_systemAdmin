package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.JsonSerializable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
@JavaScript("./visualization/feature/attributesCorrelationInfoSummaryChart-connector.js")
public class AttributesCorrelationInfoSummaryChart extends VerticalLayout {

    public AttributesCorrelationInfoSummaryChart(){
        //https://www.amcharts.com/demos/rectangular-voronoi-tree-map/
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();
        this.setHeight(470,Unit.PIXELS);
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/index.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/hierarchy.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/themes/Animated.js");
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_AttributesCorrelationInfoSummaryChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    private class VoronoiTreemapEntity implements JsonSerializable {
        private String id;
        private String name ="";
        private long population = 0;
        private List<VoronoiTreemapEntity> children;

        @Override
        public JsonObject toJson() {
            JsonObject obj = Json.createObject();
            if (getName() != null) {
                obj.put("name", getName());
            }
            if (getId() != null) {
                obj.put("id", getId());
            }
            if(getPopulation() >0){
                obj.put("population", getPopulation());
            }
            if(getChildren() != null && getChildren().size()>0){
                JsonArray childrenArray = Json.createArray();
                for(int i=0; i< getChildren().size();i++ ){
                    VoronoiTreemapEntity currentVoronoiTreemapNodeData = getChildren().get(i);
                    JsonObject childJsonObject = currentVoronoiTreemapNodeData.toJson();
                    childrenArray.set(i, childJsonObject);
                }
                obj.put("children", childrenArray);
            }
            return obj;
        }

        @Override
        public JsonSerializable readJson(JsonObject jsonObject) {
            return null;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPopulation() {
            return population;
        }

        public void setPopulation(long population) {
            this.population = population;
        }

        public List<VoronoiTreemapEntity> getChildren() {
            return children;
        }

        public void setChildren(List<VoronoiTreemapEntity> children) {
            this.children = children;
        }
    }

    public void renderSummaryData(List<RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo> conceptionAttributesDistributionInfoList,
                                  List<RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo> relationAttributesDistributionInfoList){
        VoronoiTreemapEntity rootVoronoiTreemapEntity = new VoronoiTreemapEntity();

        List<VoronoiTreemapEntity> childVoronoiTreemapEntityList = new ArrayList<>();
        rootVoronoiTreemapEntity.setChildren(childVoronoiTreemapEntityList);

        VoronoiTreemapEntity commonConceptionRootEntity = new VoronoiTreemapEntity();
        commonConceptionRootEntity.setName("CommonConceptions");
        commonConceptionRootEntity.setChildren(new ArrayList<>());

        VoronoiTreemapEntity commonRelationRootEntity = new VoronoiTreemapEntity();
        commonRelationRootEntity.setName("CommonRelations");
        commonRelationRootEntity.setChildren(new ArrayList<>());

        VoronoiTreemapEntity innerConceptionRootEntity = new VoronoiTreemapEntity();
        innerConceptionRootEntity.setName("InnerConceptions");
        innerConceptionRootEntity.setChildren(new ArrayList<>());

        VoronoiTreemapEntity innerRelationRootEntity = new VoronoiTreemapEntity();
        innerRelationRootEntity.setName("InnerRelations");
        innerRelationRootEntity.setChildren(new ArrayList<>());

        if(conceptionAttributesDistributionInfoList != null){
            for(RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo currentAttributesDistributionInfo:conceptionAttributesDistributionInfoList){
                String kindName = currentAttributesDistributionInfo.getKindsName().substring(1,currentAttributesDistributionInfo.getKindsName().length()-1);
                long attributeCount = currentAttributesDistributionInfo.getAttributeCount();
                VoronoiTreemapEntity currentEntity = new VoronoiTreemapEntity();
                currentEntity.setName(kindName);
                currentEntity.setPopulation(attributeCount);
                currentEntity.setId(kindName);
                if(kindName.equals("DOCG_")){
                    innerConceptionRootEntity.getChildren().add(currentEntity);
                }else{
                    commonConceptionRootEntity.getChildren().add(currentEntity);
                }
            }
        }

        if(relationAttributesDistributionInfoList != null){
            for(RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo currentAttributesDistributionInfo:relationAttributesDistributionInfoList){
                String kindName = currentAttributesDistributionInfo.getKindsName();
                long attributeCount = currentAttributesDistributionInfo.getAttributeCount();
                VoronoiTreemapEntity currentEntity = new VoronoiTreemapEntity();
                currentEntity.setName(kindName);
                currentEntity.setPopulation(attributeCount);
                currentEntity.setId(kindName);
                if(kindName.equals("DOCG_")){
                    innerRelationRootEntity.getChildren().add(currentEntity);
                }else{
                    commonRelationRootEntity.getChildren().add(currentEntity);
                }
            }
        }

       // if(commonConceptionRootEntity.getChildren().size()>0){
            childVoronoiTreemapEntityList.add(commonConceptionRootEntity);
       // }
      //  if(commonRelationRootEntity.getChildren().size()>0){
            childVoronoiTreemapEntityList.add(commonRelationRootEntity);
      //  }
      //  if(innerConceptionRootEntity.getChildren().size()>0){
            childVoronoiTreemapEntityList.add(innerConceptionRootEntity);
      //  }
      //  if(innerRelationRootEntity.getChildren().size()>0){
            childVoronoiTreemapEntityList.add(innerRelationRootEntity);
      //  }

        runBeforeClientResponse(ui -> {
            try {
                //getElement().callJsFunction("$connector.renderVoronoiTreemapEntities", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
                getElement().callJsFunction("$connector.renderVoronoiTreemapEntities", rootVoronoiTreemapEntity.toJson());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
