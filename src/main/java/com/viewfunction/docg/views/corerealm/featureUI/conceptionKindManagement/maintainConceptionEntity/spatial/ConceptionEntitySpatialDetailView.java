package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialCalculateUtil;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
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
    private Button centroidPointWKTButton;
    private Button interiorPointWKTButton;
    private Button contentWKTButton;
    private Button envelopeWKTButton;
    private String centroidPointWKT;
    private String interiorPointWKT;
    private String envelopeAreaWKT;
    private String geometryContentWKT;
    private HorizontalLayout doesNotContainsSpatialInfoMessage;

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

        HorizontalLayout wktControllerContainer = new HorizontalLayout();
        wktControllerContainer.setSpacing(false);
        wktControllerContainer.setPadding(false);
        wktControllerContainer.setMargin(false);
        secondaryTitleComponentsList.add(wktControllerContainer);

        contentWKTButton = new Button();
        contentWKTButton.setIcon(VaadinIcon.ELASTIC.create());
        contentWKTButton.setTooltipText("显示实体空间信息数据");
        contentWKTButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderEntitySpatialWKTInfo("实体 WKT 空间信息",geometryContentWKT);
            }
        });
        contentWKTButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        wktControllerContainer.add(contentWKTButton);

        centroidPointWKTButton = new Button();
        centroidPointWKTButton.setIcon(VaadinIcon.BULLSEYE.create());
        centroidPointWKTButton.setTooltipText("显示实体质心空间信息数据");
        centroidPointWKTButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderEntitySpatialWKTInfo("实体质心 WKT 空间信息",centroidPointWKT);
            }
        });
        centroidPointWKTButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        wktControllerContainer.add(centroidPointWKTButton);

        interiorPointWKTButton = new Button();
        interiorPointWKTButton.setIcon(VaadinIcon.DOT_CIRCLE.create());
        interiorPointWKTButton.setTooltipText("显示实体中心点空间信息数据");
        interiorPointWKTButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderEntitySpatialWKTInfo("实体中心点 WKT 空间信息",interiorPointWKT);
            }
        });
        interiorPointWKTButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        wktControllerContainer.add(interiorPointWKTButton);

        envelopeWKTButton = new Button();
        envelopeWKTButton.setIcon(VaadinIcon.ABSOLUTE_POSITION.create());
        envelopeWKTButton.setTooltipText("显示实体包围盒空间信息数据");
        envelopeWKTButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderEntitySpatialWKTInfo("实体包围盒 WKT 空间信息",envelopeAreaWKT);
            }
        });
        envelopeWKTButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        wktControllerContainer.add(envelopeWKTButton);

        contentWKTButton.setEnabled(false);
        interiorPointWKTButton.setEnabled(false);
        centroidPointWKTButton.setEnabled(false);
        envelopeWKTButton.setEnabled(false);

        Icon spatialInfoIcon = VaadinIcon.LOCATION_ARROW_CIRCLE_O.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(spatialInfoIcon, "地理空间信息概要: ", secondaryTitleComponentsList, actionComponentsList);
        secondaryTitleActionBar.getStyle().set("padding-top","10px");
        add(secondaryTitleActionBar);

        doesNotContainsSpatialInfoMessage = new HorizontalLayout();
        doesNotContainsSpatialInfoMessage.setSpacing(true);
        doesNotContainsSpatialInfoMessage.setPadding(true);
        doesNotContainsSpatialInfoMessage.setMargin(true);
        doesNotContainsSpatialInfoMessage.setWidth(100,Unit.PERCENTAGE);
        doesNotContainsSpatialInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前概念实体中不包含本类型地理空间信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsSpatialInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsSpatialInfoMessage);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        conceptionEntitySpatialChart = new ConceptionEntitySpatialChart();
        conceptionEntitySpatialChart.setWidth(100,Unit.PERCENTAGE);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionEntitySpatialChart.setHeight(event.getHeight()-this.conceptionEntitySpatialInfoViewHeightOffset+20, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionEntitySpatialChart.setHeight(browserHeight-this.conceptionEntitySpatialInfoViewHeightOffset+20,Unit.PIXELS);
        }));
        add(conceptionEntitySpatialChart);
        conceptionEntitySpatialChart.setVisible(false);
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
            doesNotContainsSpatialInfoMessage.setVisible(false);
            conceptionEntitySpatialChart.setVisible(true);
            GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType = this.conceptionEntity.getGeometryType();
            if(_WKTGeometryType != null) {
                try {
                    centroidPointWKT = conceptionEntity.getEntitySpatialCentroidPointWKTGeometryContent(this.spatialScaleLevel);
                    interiorPointWKT = conceptionEntity.getEntitySpatialInteriorPointWKTGeometryContent(this.spatialScaleLevel);
                    envelopeAreaWKT = conceptionEntity.getEntitySpatialEnvelopeWKTGeometryContent(this.spatialScaleLevel);

                    _WKTGeometryTypeItem.updateDisplayValue(_WKTGeometryType.name());
                    geometryContentWKT = null;
                    String geometryCRSAID = null;
                    switch (this.spatialScaleLevel) {
                        case Local:
                            geometryContentWKT = this.conceptionEntity.getLLGeometryContent();
                            geometryCRSAID = this.conceptionEntity.getLocalCRSAID();
                            break;
                        case Global:
                            geometryContentWKT = this.conceptionEntity.getGLGeometryContent();
                            geometryCRSAID = this.conceptionEntity.getGlobalCRSAID();
                            break;
                        case Country:
                            geometryContentWKT = this.conceptionEntity.getCLGeometryContent();
                            geometryCRSAID = this.conceptionEntity.getCountryCRSAID();
                    }
                    if(geometryContentWKT != null && geometryCRSAID != null) {
                        _CRSAIDItem.updateDisplayValue(geometryCRSAID);
                    }
                    if(conceptionEntitySpatialChart != null) {
                        conceptionEntitySpatialChart.renderMapAndSpatialInfo(this.conceptionEntity.getConceptionKindName(),this.conceptionEntity.getConceptionEntityUID());
                        switch (_WKTGeometryType) {
                            case POINT:
                                contentWKTButton.setEnabled(true);
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
                                break;
                            case LINESTRING:
                                contentWKTButton.setEnabled(true);
                                if(envelopeAreaWKT != null){
                                    envelopeWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                }
                                if(centroidPointWKT != null){
                                    centroidPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                if(interiorPointWKT != null){
                                    interiorPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                }
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
                                break;
                            case POLYGON:
                                contentWKTButton.setEnabled(true);
                                if(envelopeAreaWKT != null){
                                    envelopeWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                }
                                if(interiorPointWKT != null){
                                    interiorPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                }
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
                                break;
                            case MULTIPOINT:
                                contentWKTButton.setEnabled(true);
                                if(envelopeAreaWKT != null){
                                    envelopeWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                }
                                if(centroidPointWKT != null){
                                    centroidPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
                                break;
                            case MULTILINESTRING:
                                contentWKTButton.setEnabled(true);
                                if(envelopeAreaWKT != null){
                                    envelopeWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                }
                                if(centroidPointWKT != null){
                                    centroidPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                if(interiorPointWKT != null){
                                    interiorPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                }
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
                                break;
                            case MULTIPOLYGON:
                                contentWKTButton.setEnabled(true);
                                if(envelopeAreaWKT != null){
                                    envelopeWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                }
                                if(centroidPointWKT != null){
                                    centroidPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
                                break;
                            case GEOMETRYCOLLECTION:
                                contentWKTButton.setEnabled(true);
                                if(envelopeAreaWKT != null){
                                    envelopeWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
                                }
                                if(centroidPointWKT != null){
                                    centroidPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
                                }
                                if(interiorPointWKT != null){
                                    interiorPointWKTButton.setEnabled(true);
                                    conceptionEntitySpatialChart.renderInteriorPoint(getGeoJsonFromWKTContent(geometryCRSAID, interiorPointWKT));
                                }
                                conceptionEntitySpatialChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
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
        if(geoJsonContent != null){
            String resultGeoJson ="{\"type\": \"FeatureCollection\",\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \""+geometryCRSAID+"\" } },\"features\": [{ \"type\": \"Feature\", \"geometry\": "+ geoJsonContent+" }]}";
            return resultGeoJson;
        }
        return null;
    }

    private void renderEntitySpatialWKTInfo(String wktType,String wktValue){
        Span wktSpan = new Span(wktValue);
        Scroller scroller = new Scroller(wktSpan);
        scroller.setHeight(350,Unit.PIXELS);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.ELASTIC),wktType,null,true,600,430,false);
        fixSizeWindow.setWindowContent(scroller);
        fixSizeWindow.show();
    }
}
