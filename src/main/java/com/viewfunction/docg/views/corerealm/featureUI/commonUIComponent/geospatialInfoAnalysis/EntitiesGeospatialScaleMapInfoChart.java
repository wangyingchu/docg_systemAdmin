package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;

@JavaScript("./visualization/feature/entitiesGeospatialScaleMapInfoChart-connector.js")
public class EntitiesGeospatialScaleMapInfoChart extends VerticalLayout {
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;
    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;
    public EntitiesGeospatialScaleMapInfoChart(GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel, ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        //link to download latest leaflet build js: https://unpkg.com/leaflet
        UI.getCurrent().getPage().addStyleSheet("js/leaflet/1.9.3/dist/leaflet.css");
        UI.getCurrent().getPage().addJavaScript("js/leaflet/1.9.3/dist/leaflet.js");

        this.spatialScaleLevel = spatialScaleLevel;
        this.conceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;
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
        System.out.println("ASDDDDDDDDDDDDDDDD");
        System.out.println("ASDDDDDDDDDDDDDDDD");
        System.out.println("ASDDDDDDDDDDDDDDDD");
        System.out.println("ASDDDDDDDDDDDDDDDD");
    }

    /*
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderMapAndSpatialInfo();
    }
    */
}
