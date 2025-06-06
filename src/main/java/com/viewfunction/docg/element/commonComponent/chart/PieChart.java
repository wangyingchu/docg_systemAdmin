package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsPieChartPayload;
import elemental.json.Json;
import elemental.json.JsonArray;

@JavaScript("./visualization/common/pieChart_echarts-connector.js")
public class PieChart extends Div {

    public PieChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        setWidth(windowWidth, Unit.PIXELS);
        setHeight(windowHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    public void setDate(String[] nameArray,Double[] valueArray){
        JsonArray dataArray = Json.createArray();
        for(int i = 0; i < nameArray.length; i++){
            String dataName = nameArray[i];
            Double dataValue = valueArray[i];
            EchartsPieChartPayload currentEchartsPieChartPayload = new EchartsPieChartPayload(dataName,dataValue);
            dataArray.set(i,currentEchartsPieChartPayload.toJson());
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", dataArray));
    }

    public void setColor(String[] colorArray){
        JsonArray dataArray = Json.createArray();
        for(int i = 0; i < colorArray.length; i++){
            String currentColor = colorArray[i];
            dataArray.set(i,currentColor);
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setColor", dataArray));
    }

    public void setCenter(int xOffset,int yOffset){
        if(xOffset>0 && xOffset<=100 && yOffset>0 && yOffset<=100){
            JsonArray dataArray = Json.createArray();
            String xOffsetValue = ""+xOffset+"%";
            String yOffsetValue = ""+yOffset+"%";
            dataArray.set(0,xOffsetValue);
            dataArray.set(1,yOffsetValue);
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setCenter", dataArray));
        }
    }

    public void setRadius(int radius){
        if(radius>0 && radius<=100){
            String radiusValue = ""+radius+"%";
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setRadius", radiusValue));
        }
    }

    public void enableRightLegend(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.enableRightLegend", ""));
    }

    public void enableLeftLegend(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.enableLeftLegend", ""));
    }

    public void enableTopLegend(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.enableTopLegend", ""));
    }

    public void enableBottomLegend(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.enableBottomLegend", ""));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_PieChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
