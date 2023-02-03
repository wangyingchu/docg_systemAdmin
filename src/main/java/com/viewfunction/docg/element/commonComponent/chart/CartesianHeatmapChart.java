package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.Json;
import elemental.json.JsonArray;

@JavaScript("./visualization/common/cartesianHeatmapChart_echarts-connector.js")
public class CartesianHeatmapChart extends Div {

    public CartesianHeatmapChart(int chartWidth,int chartHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        setWidth(chartWidth, Unit.PIXELS);
        setHeight(chartHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    public  void setData(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", ""));
    }

    public void setColorRange(String startColor,String endColor){
        JsonArray dataArray = Json.createArray();
        dataArray.set(0,startColor);
        dataArray.set(1,endColor);
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setColorRange", dataArray));
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_CartesianHeatmapChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
