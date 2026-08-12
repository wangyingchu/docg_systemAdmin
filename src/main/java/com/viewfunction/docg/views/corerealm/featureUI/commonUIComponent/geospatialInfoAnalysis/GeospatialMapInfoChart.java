package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

/**
 * Vaadin ReactAdapterComponent wrapping the MapInfoChart ReactAdapterElement.
 */
@Tag("map-info-chart")
@NpmPackage(value = "@turf/bbox", version = "7.4.0")
@NpmPackage(value = "jspdf", version = "4.2.1")
@NpmPackage(value = "maplibre-gl", version = "6.1.0")
@JsModule("./externalTech/flow/integration/react/geospatialScaleMapInfoChart/map-info-chart.tsx")
public class GeospatialMapInfoChart extends ReactAdapterComponent {

    public GeospatialMapInfoChart() {
        // Vaadin binds to the <flow-content-container name="content">
        // rendered by hooks.useContent("content") in the React element.
    }

    // ── Public API for the Vaadin sidebar ──

    public void renderEntityContent(String wkt, String label) {
        callJs("renderEntityContent", wkt, label);
    }

    public void renderEnvelope(String wkt, String label) {
        callJs("renderEnvelope", wkt, label);
    }

    public void renderInteriorPoint(String wkt, String label) {
        callJs("renderInteriorPoint", wkt, label);
    }

    public void renderCentroidPoint(String wkt, String label) {
        callJs("renderCentroidPoint", wkt, label);
    }

    public void clearMap() {
        getElement().executeJs(
                "window.__mapInstances && window.__mapInstances['map-main'] &&" +
                        " window.__mapInstances['map-main'].clearMap()");
    }

    private void callJs(String method, String wkt, String label) {
        //添加500毫秒延时，确保在添加图层要素之前map已经加载完毕
        int waitTime = 500;
        String commandContentStr = "window.__mapInstances && window.__mapInstances['map-main'] &&" +
        " window.__mapInstances['map-main']." + method + "($0, $1)";
        getElement().executeJs(
                "setTimeout(() => {\n"+
                        commandContentStr +
                "}, $2);"
                , wkt, label != null ? label : "", waitTime);
        /*
        getElement().executeJs(
                "window.__mapInstances && window.__mapInstances['map-main'] &&" +
                        " window.__mapInstances['map-main']." + method + "($0, $1)",
                wkt, label != null ? label : "");
        */
    }
}