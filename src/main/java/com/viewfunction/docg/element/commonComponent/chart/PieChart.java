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
