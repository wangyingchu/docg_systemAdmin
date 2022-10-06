package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;

import java.io.Serializable;

@JavaScript("./visualization/feature/conceptionEntitySpatialChart-connector_Abandon.js")
public class ConceptionEntitySpatialChart_L7_Abandon extends VerticalLayout{

    private String centroidPointGeoJson;
    private String interiorPointGeoJson;
    private String envelopeGeoJson;
    private String entityContentGeoJson;

    public ConceptionEntitySpatialChart_L7_Abandon(){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        //需要设定position 为相对位置，否则地图会占满占全部屏幕尺寸
        getStyle().set("position","relative");
        //link to download latest l7 build js: https://unpkg.com/@antv/l7
        //link to download latest mapbox-gl build js:https://unpkg.com/mapbox-gl
        UI.getCurrent().getPage().addJavaScript("js/mapbox-gl/2.10.0/dist/mapbox-gl.js");
        UI.getCurrent().getPage().addJavaScript("js/antv/l7/2.9.26/dist/l7.js");
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
    }

    public void renderInteriorPoint(String interiorPointGeoJson){
        this.interiorPointGeoJson = interiorPointGeoJson;
    }

    public void renderEnvelope(String envelopeGeoJson){
        this.envelopeGeoJson = envelopeGeoJson;
    }

    public void renderEntityContent(GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType,String entityContentGeoJson){
        this.entityContentGeoJson = entityContentGeoJson;



        System.out.println(this.entityContentGeoJson);
        System.out.println(this.entityContentGeoJson);
        System.out.println(this.entityContentGeoJson);


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
