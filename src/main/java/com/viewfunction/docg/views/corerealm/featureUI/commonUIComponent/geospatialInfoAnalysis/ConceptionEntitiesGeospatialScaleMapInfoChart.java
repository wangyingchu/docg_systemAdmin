package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialCalculateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@JavaScript("./visualization/feature/entitiesGeospatialScaleMapInfoChart-connector.js")
public class ConceptionEntitiesGeospatialScaleMapInfoChart extends VerticalLayout {
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;
    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;
    private String kindName;
    private int randomEntityCount = 100;
    public ConceptionEntitiesGeospatialScaleMapInfoChart(String kindName, GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel, ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        //link to download latest leaflet build js: https://unpkg.com/leaflet
        UI.getCurrent().getPage().addStyleSheet("js/leaflet/1.9.3/dist/leaflet.css");
        UI.getCurrent().getPage().addJavaScript("js/leaflet/1.9.3/dist/leaflet.js");

        this.spatialScaleLevel = spatialScaleLevel;
        this.conceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;
        this.kindName = kindName;

        this.setWidthFull();
        this.setHeight(700, Unit.PIXELS);
    }


    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_EntitiesGeospatialScaleMapInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void renderMapAndSpatialInfo(){
        initConnector();
        renderEntities();
    }

    private void renderEntities(){
        List<String> conceptionEntitiesUIDList = getRandomEntitiesUID(randomEntityCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues());
        if(conceptionEntitiesUIDList != null){

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            try {
                List<ConceptionEntity> targetConceptionEntities = crossKindDataOperator.getConceptionEntitiesByUIDs(conceptionEntitiesUIDList);

                if(targetConceptionEntities!= null){
                    String centroidPointWKT = null;
                    String interiorPointWKT = null;
                    String envelopeAreaWKT = null;
                    String geometryContentWKT = null;
                    String conceptionKindName = null;
                    String conceptionEntityUID = null;

                    for(ConceptionEntity conceptionEntity:targetConceptionEntities){

                        GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType = conceptionEntity.getGeometryType();
                        conceptionKindName = conceptionEntity.getConceptionKindName();
                        conceptionEntityUID = conceptionEntity.getConceptionEntityUID();

                        if(_WKTGeometryType != null) {
                            try {
                                centroidPointWKT = conceptionEntity.getEntitySpatialCentroidPointWKTGeometryContent(this.spatialScaleLevel);
                                interiorPointWKT = conceptionEntity.getEntitySpatialInteriorPointWKTGeometryContent(this.spatialScaleLevel);
                                envelopeAreaWKT = conceptionEntity.getEntitySpatialEnvelopeWKTGeometryContent(this.spatialScaleLevel);

                                geometryContentWKT = null;
                                String geometryCRSAID = null;
                                switch (this.spatialScaleLevel) {
                                    case Local:
                                        geometryContentWKT = conceptionEntity.getLLGeometryContent();
                                        geometryCRSAID = conceptionEntity.getLocalCRSAID();
                                        break;
                                    case Global:
                                        geometryContentWKT = conceptionEntity.getGLGeometryContent();
                                        geometryCRSAID = conceptionEntity.getGlobalCRSAID();
                                        break;
                                    case Country:
                                        geometryContentWKT = conceptionEntity.getCLGeometryContent();
                                        geometryCRSAID = conceptionEntity.getCountryCRSAID();
                                }

                                switch (_WKTGeometryType) {
                                    case POINT:
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                        break;
                                    case LINESTRING:
                                        if(envelopeAreaWKT != null){
                                            renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                        }
                                        if(centroidPointWKT != null){
                                            renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                        }
                                        if(interiorPointWKT != null){
                                            renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                        }
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                        break;
                                    case POLYGON:
                                        if(envelopeAreaWKT != null){
                                            renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                        }
                                        if(interiorPointWKT != null){
                                            renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                        }
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                        break;
                                    case MULTIPOINT:
                                        if(envelopeAreaWKT != null){
                                            renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                        }
                                        if(centroidPointWKT != null){
                                            renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                        }
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                        break;
                                    case MULTILINESTRING:
                                        if(envelopeAreaWKT != null){
                                            renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                        }
                                        if(centroidPointWKT != null){
                                            renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                        }
                                        if(interiorPointWKT != null){
                                            renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                        }
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                        break;
                                    case MULTIPOLYGON:
                                        if(envelopeAreaWKT != null){
                                            renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                        }
                                        if(centroidPointWKT != null){
                                            renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                        }
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                        break;
                                    case GEOMETRYCOLLECTION:
                                        if(envelopeAreaWKT != null){
                                            renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                        }
                                        if(centroidPointWKT != null){
                                            renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                        }
                                        if(interiorPointWKT != null){
                                            renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                        }
                                        renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),conceptionKindName,conceptionEntityUID);
                                    }
                            } catch (CoreRealmServiceRuntimeException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }finally {
                coreRealm.closeGlobalSession();
            }
        }
    }

    private List<String> getRandomEntitiesUID(int targetEntitiesCount,List<ConceptionEntityValue> conceptionEntityValueList){
        List<String> targetUIDList = new ArrayList<>();
        while(targetUIDList.size() < targetEntitiesCount){
            int currentIdx = new Random().nextInt(conceptionEntityValueList.size());
            targetUIDList.add(conceptionEntityValueList.get(currentIdx).getConceptionEntityUID());
        }
        return targetUIDList;
    }

    private String getGeoJsonFromWKTContent(String geometryCRSAID,String wktContent){
        String geoJsonContent = GeospatialCalculateUtil.getGeoJsonFromWTK(wktContent);
        if(geoJsonContent != null){
            String resultGeoJson ="{\"type\": \"FeatureCollection\",\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \""+geometryCRSAID+"\" } },\"features\": [{ \"type\": \"Feature\", \"geometry\": "+ geoJsonContent+" }]}";
            return resultGeoJson;
        }
        return null;
    }

    public void renderCentroidPoint(String centroidPointGeoJson){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderCentroidPoint", new Serializable[]{(new ObjectMapper()).writeValueAsString(centroidPointGeoJson)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void renderInteriorPoint(String interiorPointGeoJson){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderInteriorPoint", new Serializable[]{(new ObjectMapper()).writeValueAsString(interiorPointGeoJson)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void renderEnvelope(String envelopeGeoJson){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderEnvelope", new Serializable[]{(new ObjectMapper()).writeValueAsString(envelopeGeoJson)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void renderEntityContent(GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType,String entityContentGeoJson,String conceptionKindName,String conceptionEntityUID){
        switch (_WKTGeometryType){
            case POINT:
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPointEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(conceptionEntityUID)
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
                                (new ObjectMapper()).writeValueAsString(conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(conceptionEntityUID)
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
                                (new ObjectMapper()).writeValueAsString(conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(conceptionEntityUID)
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
                                (new ObjectMapper()).writeValueAsString(conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(conceptionEntityUID)
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
                                (new ObjectMapper()).writeValueAsString(conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(conceptionEntityUID)
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
                                (new ObjectMapper()).writeValueAsString(conceptionKindName),
                                (new ObjectMapper()).writeValueAsString(conceptionEntityUID)
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
