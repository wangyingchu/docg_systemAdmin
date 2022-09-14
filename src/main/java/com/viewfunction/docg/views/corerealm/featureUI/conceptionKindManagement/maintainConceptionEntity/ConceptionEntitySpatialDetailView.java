package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialCalculateUtil;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntitySpatialDetailView extends VerticalLayout {

    private int conceptionEntitySpatialInfoViewHeightOffset;
    private Registration listener;
    private ConceptionEntity conceptionEntity;
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;
    private SecondaryKeyValueDisplayItem _WKTGeometryTypeItem;
    private SecondaryKeyValueDisplayItem _CRSAIDItem;
    private ConceptionEntitySpatialChart conceptionEntitySpatialChart;
    public ConceptionEntitySpatialDetailView(int conceptionEntitySpatialInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.conceptionEntitySpatialInfoViewHeightOffset = conceptionEntitySpatialInfoViewHeightOffset+5;

        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        _WKTGeometryTypeItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.VIEWPORT.create(), "地理空间元素类型", "-");
        _CRSAIDItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.CROSSHAIRS.create(), "坐标系 CRSAID", "-");

        Icon spatialInfoIcon = VaadinIcon.LOCATION_ARROW_CIRCLE_O.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(spatialInfoIcon, "地理空间信息概要: ", secondaryTitleComponentsList, actionComponentsList);
        secondaryTitleActionBar.getStyle().set("padding-top","10px");
        add(secondaryTitleActionBar);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        conceptionEntitySpatialChart = new ConceptionEntitySpatialChart();
        conceptionEntitySpatialChart.setWidth(100,Unit.PERCENTAGE);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionEntitySpatialChart.setHeight(event.getHeight()-this.conceptionEntitySpatialInfoViewHeightOffset, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionEntitySpatialChart.setHeight(browserHeight-this.conceptionEntitySpatialInfoViewHeightOffset,Unit.PIXELS);
        }));
        add(conceptionEntitySpatialChart);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void setConceptionEntity(ConceptionEntity conceptionEntity) {
        this.conceptionEntity = conceptionEntity;
    }

    public void setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel) {
        this.spatialScaleLevel = spatialScaleLevel;
    }

    public void renderEntitySpatialDetailInfo() {
        if (this.conceptionEntity != null && this.spatialScaleLevel != null) {
            GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType = this.conceptionEntity.getGeometryType();
            if(_WKTGeometryType != null) {
                try {
                    String centroidPointWKT = conceptionEntity.getEntitySpatialCentroidPointWKTGeometryContent(this.spatialScaleLevel);
                    String interiorPointWKT = conceptionEntity.getEntitySpatialInteriorPointWKTGeometryContent(this.spatialScaleLevel);
                    String envelopePointWKT = conceptionEntity.getEntitySpatialEnvelopeWKTGeometryContent(this.spatialScaleLevel);

                    _WKTGeometryTypeItem.updateDisplayValue(_WKTGeometryType.name());
                    String geometryContent = null;
                    String geometryCRSAID = null;
                    switch (this.spatialScaleLevel) {
                        case Local:
                            geometryContent = this.conceptionEntity.getLLGeometryContent();
                            geometryCRSAID = this.conceptionEntity.getLocalCRSAID();
                            break;
                        case Global:
                            geometryContent = this.conceptionEntity.getGLGeometryContent();
                            geometryCRSAID = this.conceptionEntity.getGlobalCRSAID();
                            break;
                        case Country:
                            geometryContent = this.conceptionEntity.getCLGeometryContent();
                            geometryCRSAID = this.conceptionEntity.getCountryCRSAID();
                    }
                    if(geometryContent != null && geometryCRSAID != null) {
                        _CRSAIDItem.updateDisplayValue(geometryCRSAID);
                    }
                    if(conceptionEntitySpatialChart != null) {
                        conceptionEntitySpatialChart.renderMapAndSpatialInfo();
                        switch (_WKTGeometryType) {
                            case POINT:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                break;
                            case LINESTRING:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                if(centroidPointWKT != null){
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                break;
                            case POLYGON:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                if(interiorPointWKT != null){
                                    conceptionEntitySpatialChart.renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                }
                                if(envelopePointWKT != null){
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopePointWKT));
                                }
                                break;
                            case MULTIPOINT:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                if(centroidPointWKT != null){
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                if(envelopePointWKT != null){
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopePointWKT));
                                }
                                break;
                            case MULTILINESTRING:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                if(centroidPointWKT != null){
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                break;
                            case MULTIPOLYGON:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                if(centroidPointWKT != null){
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                if(envelopePointWKT != null){
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopePointWKT));
                                }
                                break;
                            case GEOMETRYCOLLECTION:
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContent));
                                if(centroidPointWKT != null){
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                if(envelopePointWKT != null){
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopePointWKT));
                                }
                        }
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private String getGeoJsonFromWKTContent(String geometryCRSAID,String wktContent){
        String geoJsonContent = GeospatialCalculateUtil.getGeoJsonFromWTK(wktContent);

        System.out.println(geoJsonContent);
        System.out.println(geoJsonContent);
        System.out.println(geoJsonContent);
        System.out.println(geoJsonContent);

        if(geoJsonContent != null){
            String resultGeoJson = "{" +
                    "\"type\": \"FeatureCollection\"," +
                    "\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \""+geometryCRSAID+"\" } }," +
                    "\"features\": [";
            resultGeoJson = resultGeoJson + geoJsonContent;
            resultGeoJson = resultGeoJson +"]" +
                    "}";


           // return "{'type': 'FeatureCollection','crs': { 'type': 'name', 'properties': { 'name': 'EPSG:4326' } },'features': [{'type':'Point','coordinates':[-122.29346695102016,47.615982606514976]}]}";
            //return resultGeoJson;

            return "{\n" +
                    "                \"type\": \"FeatureCollection\",\n" +
                    "                //\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \"urn:ogc:def:crs:OGC:1.3:CRS84\" } },\n" +
                    "                \"crs\": { \"type\": \"name\", \"properties\": { \"name\": \"EPSG:4326\" } },\n" +
                    "                \"features\": [\n" +
                    "                    { \"type\": \"Feature\", \"properties\": { \"id\": \"ak16821829\", \"mag\": 1.5, \"time\": 1504834613166, \"felt\": null, \"tsunami\": 0 }, \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 123.230989, 41.721006, 6.1 ] } },\n" +
                    "                    { \"type\": \"Feature\", \"properties\": { \"id\": \"hv61900626\", \"mag\": 2.91, \"time\": 1504833891990, \"felt\": null, \"tsunami\": 0 }, \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 121.309668, 31.066417, 2.609 ] } }\n" +
                    "                ]\n" +
                    "            }";
        }
        return null;
    }
}
