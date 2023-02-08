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

    public void setXAxisLabel(String[] xAxisLabel){
        JsonArray dataArray = Json.createArray();
        for(int i = 0; i < xAxisLabel.length; i++){
            String currentLabel = xAxisLabel[i];
            dataArray.set(i,currentLabel);
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setXAxisLabel", dataArray));
    }

    public void setYAxisLabel(String[] yAxisLabel){
        JsonArray dataArray = Json.createArray();
        for(int i = 0; i < yAxisLabel.length; i++){
            String currentLabel = yAxisLabel[i];
            dataArray.set(i,currentLabel);
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setYAxisLabel", dataArray));
    }

    public  void setData(JsonArray dataArray){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", dataArray));
    }

    public void setName(String chartName){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setName", chartName));
    }

    public void setTooltipPosition(String positionValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setTooltipPosition", positionValue));
    }

    public void hideLabels(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.hideLabels", ""));
    }

    public void setMinMapValue(int minValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setMinMapValue", minValue));
    }

    public void setMaxMapValue(int maxValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setMaxMapValue", maxValue));
    }

    public void hideMapValues(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.hideMapValues", ""));
    }

    public void setColorRange(String startColor,String endColor){
        JsonArray dataArray = Json.createArray();
        dataArray.set(0,startColor);
        dataArray.set(1,endColor);
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setColorRange", dataArray));
    }

    public void setTopMargin(int marginValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setTopMargin", marginValue));
    }

    public void setRightMargin(int marginValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setRightMargin", marginValue));
    }

    public void setLeftMargin(int marginValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setLeftMargin", marginValue));
    }

    public void setBottomMargin(int marginValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setBottomMargin", marginValue));
    }

    //public void setXAxisLabel

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_CartesianHeatmapChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
