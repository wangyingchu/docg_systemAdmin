package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;

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
        //需要设定position 为相对位置，否则地图会占满占全部屏幕尺寸
        getStyle().set("position","relative");
        //link to download latest l7 build js: https://unpkg.com/@antv/l7
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




    }
}
