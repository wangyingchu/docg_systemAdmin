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
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.io.Serializable;
import java.util.List;

@JavaScript("./visualization/feature/geospatialScaleEntitySpatialChart-connector.js")
public class GeospatialScaleEntityMapInfoChart extends VerticalLayout {
    private String geospatialRegionName;
    private String centroidPointGeoJson;
    private String interiorPointGeoJson;
    private String envelopeGeoJson;
    private String entityContentGeoJson;
    private String conceptionKindName;
    private String conceptionEntityUID;

    public GeospatialScaleEntityMapInfoChart(){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        //link to download latest leaflet build js: https://unpkg.com/leaflet
        UI.getCurrent().getPage().addStyleSheet("js/leaflet/1.9.3/dist/leaflet.css");
        UI.getCurrent().getPage().addJavaScript("js/leaflet/1.9.3/dist/leaflet.js");
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_GeospatialScaleEntitySpatialChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void renderMapAndSpatialInfo(String conceptionKindName,String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        initConnector();
    }

    public void renderCentroidPoint(String centroidPointGeoJson){
        this.centroidPointGeoJson = centroidPointGeoJson;
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderCentroidPoint", new Serializable[]{(new ObjectMapper()).writeValueAsString(centroidPointGeoJson)});
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

    public void renderEntityContent(GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType, String entityContentGeoJson){
        this.entityContentGeoJson = entityContentGeoJson;
        switch (_WKTGeometryType){
            case POINT:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPointEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(this.conceptionEntityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case POLYGON:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(this.conceptionEntityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case LINESTRING:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderLineEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(this.conceptionEntityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case MULTIPOINT:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPointEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(this.conceptionEntityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case MULTIPOLYGON:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(this.conceptionEntityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case MULTILINESTRING:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderLineEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(this.conceptionEntityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case GEOMETRYCOLLECTION:
                break;
        }
    }
}
