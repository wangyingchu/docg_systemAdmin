package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;

import java.io.Serializable;

@JavaScript("./visualization/feature/conceptionEntitySpatialChart-connector.js")
public class ConceptionEntitySpatialChart extends VerticalLayout {

    private String centroidPointGeoJson;
    private String interiorPointGeoJson;
    private String envelopeGeoJson;
    private String entityContentGeoJson;

    public ConceptionEntitySpatialChart(){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        //link to download latest l7 build js: https://unpkg.com/leaflet
        UI.getCurrent().getPage().addStyleSheet("js/leaflet/1.8.0/dist/leaflet.css");
        UI.getCurrent().getPage().addJavaScript("js/leaflet/1.8.0/dist/leaflet.js");
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntitySpatialChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void renderMapAndSpatialInfo(){
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

    public void renderEntityContent(GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType,String entityContentGeoJson){
        this.entityContentGeoJson = entityContentGeoJson;
        switch (_WKTGeometryType){
            case POINT:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPointEntityContent", new Serializable[]{(new ObjectMapper()).writeValueAsString(entityContentGeoJson)});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case POLYGON:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{(new ObjectMapper()).writeValueAsString(entityContentGeoJson)});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case LINESTRING:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderLineEntityContent", new Serializable[]{(new ObjectMapper()).writeValueAsString(entityContentGeoJson)});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case MULTIPOINT:
                break;
            case MULTIPOLYGON:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{(new ObjectMapper()).writeValueAsString(entityContentGeoJson)});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case MULTILINESTRING:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderLineEntityContent", new Serializable[]{(new ObjectMapper()).writeValueAsString(entityContentGeoJson)});
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
