package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import java.io.Serializable;

@JavaScript("./visualization/feature/geospatialScaleEntitySpatialChart-connector.js")
public class GeospatialScaleEntityMapInfoChart extends VerticalLayout {
    private String geospatialRegionName;
    private String centroidPointGeoJson;
    private String interiorPointGeoJson;
    private String envelopeGeoJson;
    private String entityContentGeoJson;
    public GeospatialScaleEntityMapInfoChart(){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        //link to download latest leaflet build js: https://unpkg.com/leaflet
        UI.getCurrent().getPage().addStyleSheet("js/leaflet/1.9.3/dist/leaflet.css");
        UI.getCurrent().getPage().addJavaScript("js/leaflet/1.9.3/dist/leaflet.js");
        //https://unpkg.com/maplibre-gl/dist/maplibre-gl.css
        //https://unpkg.com/maplibre-gl/dist/maplibre-gl.js
        UI.getCurrent().getPage().addJavaScript("js/maplibre-gl/5.13.0/dist/maplibre-gl.js");
        UI.getCurrent().getPage().addStyleSheet("js/maplibre-gl/5.13.0/dist/maplibre-gl.css");
        //https://unpkg.com/@maplibre/maplibre-gl-leaflet/leaflet-maplibre-gl.js
        UI.getCurrent().getPage().addJavaScript("js/maplibre-gl-leaflet/0.1.3/dist/leaflet-maplibre-gl.js");
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_GeospatialScaleEntitySpatialChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void renderMapAndSpatialInfo(){
        initConnector();
    }

    public void renderCentroidPoint(String centroidPointGeoJson,int zoomLevel){
        this.centroidPointGeoJson = centroidPointGeoJson;
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderCentroidPoint", new Serializable[]{
                        (new ObjectMapper()).writeValueAsString(centroidPointGeoJson),
                        (new ObjectMapper()).writeValueAsString(zoomLevel)
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void renderInteriorPoint(String interiorPointGeoJson){
        this.interiorPointGeoJson = interiorPointGeoJson;
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderInteriorPoint", new Serializable[]{(new ObjectMapper()).writeValueAsString(interiorPointGeoJson)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void renderEnvelope(String envelopeGeoJson){
        this.envelopeGeoJson = envelopeGeoJson;
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderEnvelope", new Serializable[]{(new ObjectMapper()).writeValueAsString(envelopeGeoJson)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void renderEntityContent(String entityContentGeoJson,String entityType,String entityChineseName,String entityCode){
        this.entityContentGeoJson = entityContentGeoJson;
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{
                        (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                        (new ObjectMapper()).writeValueAsString(entityChineseName),
                        (new ObjectMapper()).writeValueAsString(entityCode),
                        (new ObjectMapper()).writeValueAsString(entityType)
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void clearMap(){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.clearMap", new Serializable[]{(new ObjectMapper()).writeValueAsString("-")});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
