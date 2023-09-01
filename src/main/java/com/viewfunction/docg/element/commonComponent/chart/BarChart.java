package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsBarChartPayload;
import elemental.json.Json;
import elemental.json.JsonArray;

@JavaScript("./visualization/common/barChart_echarts-connector.js")
public class BarChart extends Div {

    public BarChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        setWidth(windowWidth, Unit.PIXELS);
        setHeight(windowHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    public void setDate(String[] nameArray,Double[] valueArray){
        EchartsBarChartPayload echartsBarChartPayload = new EchartsBarChartPayload(nameArray,valueArray);
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", echartsBarChartPayload.toJson()));
    }

    public void setColor(String[] colorArray){
        JsonArray dataArray = Json.createArray();
        for(int i = 0; i < colorArray.length; i++){
            String currentColor = colorArray[i];
            dataArray.set(i,currentColor);
        }
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setColor", dataArray));
    }

    public void setTopMargin(int marginValue){
        if(marginValue>0 && marginValue<=100){
            String marginValueString = ""+marginValue+"%";
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setTopMargin", marginValueString));
        }
    }

    public void setRightMargin(int marginValue){
        if(marginValue>0 && marginValue<=100){
            String marginValueString = ""+marginValue+"%";
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setRightMargin", marginValueString));
        }
    }

    public void setLeftMargin(int marginValue){
        if(marginValue>0 && marginValue<=100){
            String marginValueString = ""+marginValue+"%";
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setLeftMargin", marginValueString));
        }
    }

    public void setBottomMargin(int marginValue){
        if(marginValue>0 && marginValue<=100){
            String marginValueString = ""+marginValue+"%";
            runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setBottomMargin", marginValueString));
        }
    }

    public void setYAxisMaxOffset(int marginValue){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setYAxisMaxOffset", marginValue));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_BarChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
