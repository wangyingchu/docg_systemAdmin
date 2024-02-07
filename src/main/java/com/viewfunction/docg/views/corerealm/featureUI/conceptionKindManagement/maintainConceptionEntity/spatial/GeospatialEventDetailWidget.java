package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialCalculateUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.common.AttributeValueInfoWidget;
import com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion.GeospatialScaleEntityMapInfoChart;

import java.util.List;

public class GeospatialEventDetailWidget extends VerticalLayout {
    private GeospatialScaleEvent geospatialScaleEvent;
    private GeospatialScaleEntity geospatialScaleEntity;
    private GeospatialScaleEntityMapInfoChart geospatialScaleEntityMapInfoChart;
    private HorizontalLayout doesNotContainsSpatialInfoMessage;
    private VerticalLayout mapContainerLayout;
    private VerticalLayout attributeContainerLayout;
    private boolean mapNotLoadedFlag = true;

    public GeospatialEventDetailWidget(GeospatialScaleEvent geospatialScaleEvent, GeospatialScaleEntity geospatialScaleEntity){
        this.geospatialScaleEvent = geospatialScaleEvent;
        this.geospatialScaleEntity = geospatialScaleEntity;

        HorizontalLayout geospatialEventDetailElementsContainerLayout = new HorizontalLayout();
        geospatialEventDetailElementsContainerLayout.setWidthFull();
        add(geospatialEventDetailElementsContainerLayout);

        mapContainerLayout = new VerticalLayout();
        mapContainerLayout.setSpacing(false);
        mapContainerLayout.setMargin(false);
        mapContainerLayout.setPadding(false);
        mapContainerLayout.setWidth(550,Unit.PIXELS);
        geospatialEventDetailElementsContainerLayout.add(mapContainerLayout);

        attributeContainerLayout = new VerticalLayout();
        attributeContainerLayout.setWidthFull();
        geospatialEventDetailElementsContainerLayout.add(attributeContainerLayout);
        geospatialEventDetailElementsContainerLayout.setVerticalComponentAlignment(Alignment.START,attributeContainerLayout);

        List<AttributeValue> attributeValueList = geospatialScaleEvent.getAttributes();
        if(attributeValueList != null){
            for(AttributeValue currentAttributeValue:attributeValueList){
                AttributeValueInfoWidget attributeValueInfoWidget = new AttributeValueInfoWidget(currentAttributeValue);
                attributeContainerLayout.add(attributeValueInfoWidget);
            }
        }
    }

    public void renderEntityMapInfo(){
        if(mapNotLoadedFlag){
            String currentGeospatialScaleEntityUID = this.geospatialScaleEntity.getGeospatialScaleEntityUID();
            String entityChineseName = this.geospatialScaleEntity.getChineseName();
            String entityGeospatialCode = this.geospatialScaleEntity.getGeospatialCode();
            GeospatialRegion.GeospatialScaleGrade geospatialScaleGrade = this.geospatialScaleEntity.getGeospatialScaleGrade();
            String geospatialScaleEntityKindName = null;

            switch(geospatialScaleGrade){
                case CONTINENT -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleContinentEntityClass;
                case COUNTRY_REGION -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleCountryRegionEntityClass;
                case PROVINCE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleProvinceEntityClass;
                case PREFECTURE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScalePrefectureEntityClass;
                case COUNTY -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleCountyEntityClass;
                case TOWNSHIP -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleTownshipEntityClass;
                case VILLAGE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleVillageEntityClass;
            }

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetGeoConceptionKind = coreRealm.getConceptionKind(geospatialScaleEntityKindName);
            ConceptionEntity targetConceptionEntity = targetGeoConceptionKind.getEntityByUID(currentGeospatialScaleEntityUID);

            int zoomLevel = 17;
            switch(geospatialScaleGrade){
                case CONTINENT -> zoomLevel = 1;
                case COUNTRY_REGION -> zoomLevel = 3;
                case PROVINCE -> zoomLevel = 5;
                case PREFECTURE -> zoomLevel = 7;
                case COUNTY -> zoomLevel = 9;
                case TOWNSHIP -> zoomLevel = 11;
                case VILLAGE -> zoomLevel = 13;
            }
            GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType = targetConceptionEntity.getGeometryType();
            if(_WKTGeometryType != null){
                this.doesNotContainsSpatialInfoMessage.setVisible(false);
                this.geospatialScaleEntityMapInfoChart.setVisible(true);
                try {
                    String centroidPointWKT = targetConceptionEntity.getEntitySpatialCentroidPointWKTGeometryContent(GeospatialScaleCalculable.SpatialScaleLevel.Global);
                    String envelopeAreaWKT = targetConceptionEntity.getEntitySpatialEnvelopeWKTGeometryContent(GeospatialScaleCalculable.SpatialScaleLevel.Global);
                    String geometryCRSAID = targetConceptionEntity.getGlobalCRSAID();
                    String geometryContentWKT = targetConceptionEntity.getGLGeometryContent();

                    if(envelopeAreaWKT != null){
                        this.geospatialScaleEntityMapInfoChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                    }
                    this.geospatialScaleEntityMapInfoChart.renderEntityContent(getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT),geospatialScaleGrade.toString(),entityChineseName,entityGeospatialCode);
                    if(centroidPointWKT != null){
                        this.geospatialScaleEntityMapInfoChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT),zoomLevel);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
            }else{
                this.doesNotContainsSpatialInfoMessage.setVisible(true);
                this.geospatialScaleEntityMapInfoChart.setVisible(false);
            }

            coreRealm.closeGlobalSession();
            mapNotLoadedFlag = false;
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        this.geospatialScaleEntityMapInfoChart = new GeospatialScaleEntityMapInfoChart();
        this.geospatialScaleEntityMapInfoChart.setWidth(550,Unit.PIXELS);
        this.geospatialScaleEntityMapInfoChart.setHeight(300,Unit.PIXELS);
        mapContainerLayout.add(this.geospatialScaleEntityMapInfoChart);
        this.geospatialScaleEntityMapInfoChart.renderMapAndSpatialInfo();

        this.doesNotContainsSpatialInfoMessage = new HorizontalLayout();
        this.doesNotContainsSpatialInfoMessage.setSpacing(true);
        this.doesNotContainsSpatialInfoMessage.setPadding(true);
        this.doesNotContainsSpatialInfoMessage.setMargin(true);
        this.doesNotContainsSpatialInfoMessage.setWidth(100, Unit.PERCENTAGE);
        this.doesNotContainsSpatialInfoMessage.setHeight(100,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前地理空间区域实体中不包含地理空间信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        this.doesNotContainsSpatialInfoMessage.add(messageLogo,messageLabel);
        mapContainerLayout.add(this.doesNotContainsSpatialInfoMessage);

        this.geospatialScaleEntityMapInfoChart.setVisible(false);
        this.doesNotContainsSpatialInfoMessage.setVisible(false);
    }
}
