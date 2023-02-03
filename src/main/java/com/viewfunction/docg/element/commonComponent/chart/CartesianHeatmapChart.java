package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

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

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_CartesianHeatmapChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
