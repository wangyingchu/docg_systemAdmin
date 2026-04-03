package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsPieChartPayload;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

@JavaScript("./visualization/common/pieChart_echarts-connector.js")
public class PieChart extends Div {

    private static final ObjectMapper mapper = new ObjectMapper();

    public PieChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        setWidth(windowWidth, Unit.PIXELS);
        setHeight(windowHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    public void setDate(String[] nameArray,Double[] valueArray){
        ArrayNode arrayNode = mapper.createArrayNode();
        for(int i = 0; i < nameArray.length; i++){
            String dataName = nameArray[i];
            Double dataValue = valueArray[i];
            EchartsPieChartPayload currentEchartsPieChartPayload = new EchartsPieChartPayload(dataName,dataValue);
            arrayNode.insert(i, currentEchartsPieChartPayload.toJson());
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", arrayNode));
    }

    public void setColor(String[] colorArray){
        ArrayNode arrayNode = mapper.createArrayNode();
        for(int i = 0; i < colorArray.length; i++){
            String currentColor = colorArray[i];
            arrayNode.insert(i,currentColor);
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setColor", arrayNode));
    }

    public void setCenter(int xOffset,int yOffset){
        if(xOffset>0 && xOffset<=100 && yOffset>0 && yOffset<=100){
            ArrayNode arrayNode = mapper.createArrayNode();
            String xOffsetValue = ""+xOffset+"%";
            String yOffsetValue = ""+yOffset+"%";
            arrayNode.insert(0,xOffsetValue);
            arrayNode.insert(1,yOffsetValue);
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setCenter", arrayNode));
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
