package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.JsonSerializable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonObject;

import java.io.Serializable;
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















        @Override
        public JsonObject toJson() {
            return null;
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
    }

    public void renderSummaryData(List<RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo> conceptionAttributesDistributionInfoList,
                                  List<RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo> relationAttributesDistributionInfoList){


        System.out.println(conceptionAttributesDistributionInfoList);
        System.out.println(conceptionAttributesDistributionInfoList);

        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderVoronoiTreemapEntities", new Serializable[]{(new ObjectMapper()).writeValueAsString("null")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }



}
