package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsTimeSequenceBarChartPayload;

import java.time.LocalDateTime;
import java.util.Map;

@JavaScript("./visualization/common/timeSequenceBarChart_echarts-connector.js")
public class TimeSequenceBarChart extends Div {

    public TimeSequenceBarChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        setWidth(windowWidth, Unit.PIXELS);
        setHeight(windowHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    public void setBarColor(String color){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setBarColor", color));
    }

    public void setPropertyDesc(String propertyDesc){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setPropertyDesc", propertyDesc));
    }

    public void setDate(Map<LocalDateTime,Long> timeAndValueMapping){
        EchartsTimeSequenceBarChartPayload echartsTimeSequenceBarChartPayload = new EchartsTimeSequenceBarChartPayload(timeAndValueMapping);
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", echartsTimeSequenceBarChartPayload.toJson()));
    }

    public void clearData(){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.clearData", "null"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_TimeSequenceBarChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
