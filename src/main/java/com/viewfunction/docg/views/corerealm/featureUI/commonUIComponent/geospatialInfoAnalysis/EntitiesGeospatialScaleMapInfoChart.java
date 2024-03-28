package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialCalculateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JavaScript("./visualization/feature/entitiesGeospatialScaleMapInfoChart-connector.js")
public class EntitiesGeospatialScaleMapInfoChart extends VerticalLayout {
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;
    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;
    private Registration listener;
    private String kindName;
    public EntitiesGeospatialScaleMapInfoChart(String kindName,GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel, ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult){
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

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.setHeight(receiver.getBodyClientHeight()-75,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        listener.remove();
        super.onDetach(detachEvent);
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
        List<ConceptionEntityValue>  conceptionEntityValueList = this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();
        if(conceptionEntityValueList != null){
            List<String> entitiesUIDList = new ArrayList<>();
            for(ConceptionEntityValue currentConceptionEntityValue:conceptionEntityValueList){
                String currentEntityUID = currentConceptionEntityValue.getConceptionEntityUID();
                entitiesUIDList.add(currentEntityUID);
            }

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            List<String> queryAttributeNamesList = new ArrayList<>();
            try {
                switch(this.spatialScaleLevel){
                    case Global :
                        queryAttributeNamesList.add(RealmConstant._GeospatialGLGeometryContent);
                        queryAttributeNamesList.add(RealmConstant._GeospatialGlobalCRSAID);
                        queryAttributeNamesList.add(RealmConstant._GeospatialGeometryType);
                        break;
                    case Country :
                        queryAttributeNamesList.add(RealmConstant._GeospatialCLGeometryContent);
                        queryAttributeNamesList.add(RealmConstant._GeospatialCountryCRSAID);
                        queryAttributeNamesList.add(RealmConstant._GeospatialGeometryType);
                        break;
                    case Local :
                        queryAttributeNamesList.add(RealmConstant._GeospatialLLGeometryContent);
                        queryAttributeNamesList.add(RealmConstant._GeospatialLocalCRSAID);
                        queryAttributeNamesList.add(RealmConstant._GeospatialGeometryType);
                }
                List<ConceptionEntityValue> conceptionEntityResultValueList = crossKindDataOperator.getSingleValueConceptionEntityAttributesByUIDs(entitiesUIDList,queryAttributeNamesList);
                if(conceptionEntityResultValueList != null){
                    for(ConceptionEntityValue currentConceptionEntityValue:conceptionEntityResultValueList){
                        String geometryCRSAID = null;
                        String wktContent = null;
                        String geospatialGeometryType = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialGeometryType).toString();
                        switch(this.spatialScaleLevel){
                            case Global :
                                geometryCRSAID = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialGlobalCRSAID).toString();
                                wktContent = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialGLGeometryContent).toString();
                                break;
                            case Country :
                                geometryCRSAID = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialCountryCRSAID).toString();
                                wktContent = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialCLGeometryContent).toString();
                                break;
                            case Local :
                                geometryCRSAID = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialLocalCRSAID).toString();
                                wktContent = currentConceptionEntityValue.getEntityAttributesValue().get(RealmConstant._GeospatialLLGeometryContent).toString();
                        }
                        if(wktContent!= null){
                            renderEntityContent(currentConceptionEntityValue.getConceptionEntityUID(),geospatialGeometryType,getGeoJsonFromWKTContent(geometryCRSAID,wktContent));
                        }
                    }
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getGeoJsonFromWKTContent(String geometryCRSAID,String wktContent){
        String geoJsonContent = GeospatialCalculateUtil.getGeoJsonFromWTK(wktContent);
        if(geoJsonContent != null){
            String resultGeoJson ="{\"type\": \"FeatureCollection\",\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \""+geometryCRSAID+"\" } },\"features\": [{ \"type\": \"Feature\", \"geometry\": "+ geoJsonContent+" }]}";
            return resultGeoJson;
        }
        return null;
    }

    public void renderEntityContent(String entityUID, String _WKTGeometryType, String entityContentGeoJsonStr){
        String entityContentGeoJson = entityContentGeoJsonStr;
        switch (_WKTGeometryType){
            case "POINT":
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPointEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.kindName),
                                (new ObjectMapper()).writeValueAsString(entityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "POLYGON":
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.kindName),
                                (new ObjectMapper()).writeValueAsString(entityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "LINESTRING":
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderLineEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.kindName),
                                (new ObjectMapper()).writeValueAsString(entityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "MULTIPOINT":
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPointEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.kindName),
                                (new ObjectMapper()).writeValueAsString(entityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "MULTIPOLYGON":
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderPolygonEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.kindName),
                                (new ObjectMapper()).writeValueAsString(entityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "MULTILINESTRING":
                runBeforeClientResponse(ui -> {
                    try {
                        getElement().callJsFunction("$connector.renderLineEntityContent", new Serializable[]{
                                (new ObjectMapper()).writeValueAsString(entityContentGeoJson),
                                (new ObjectMapper()).writeValueAsString(this.kindName),
                                (new ObjectMapper()).writeValueAsString(entityUID)
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "GEOMETRYCOLLECTION":
                break;
        }
    }
}
