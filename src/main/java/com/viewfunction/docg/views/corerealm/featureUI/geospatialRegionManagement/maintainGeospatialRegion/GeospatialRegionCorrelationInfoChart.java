package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.JsonSerializable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEntity;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.AttributesCorrelationInfoSummaryChart;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.io.Serializable;
import java.util.List;

@JavaScript("./visualization/feature/attributesCorrelationInfoSummaryChart-connector.js")
public class GeospatialRegionCorrelationInfoChart extends VerticalLayout {
    private String geospatialRegionName;
    public GeospatialRegionCorrelationInfoChart(String geospatialRegionName){
        //https://www.amcharts.com/demos/rectangular-voronoi-tree-map/
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();
        this.setHeight(470, Unit.PIXELS);
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/index.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/hierarchy.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/themes/Animated.js");
        initConnector();
        this.geospatialRegionName = geospatialRegionName;
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


    public void renderEntitiesSpatialInfo(List<GeospatialScaleEntity> geospatialScaleEntityList){}



}
