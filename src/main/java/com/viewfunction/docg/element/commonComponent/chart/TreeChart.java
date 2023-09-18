package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsRadialTreeChartPayload;

@JavaScript("./visualization/common/treeChart_echarts-connector.js")
public class TreeChart extends Div {

    public enum TreeLayout {orthogonal,radial}
    public TreeChart(int windowWidth, int windowHeight){
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/echarts.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/dataTool.min.js");
        UI.getCurrent().getPage().addJavaScript("js/echarts/5.4.1/dist/extension/bmap.min.js");
        if(windowWidth ==0){
            setWidth(100, Unit.PERCENTAGE);
        }else{
            setWidth(windowWidth, Unit.PIXELS);
        }
        setHeight(windowHeight,Unit.PIXELS);
        initConnector(getElement());
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_TreeChart_echarts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setDate(EchartsRadialTreeChartPayload echartsRadialTreeChartPayload){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setData", echartsRadialTreeChartPayload.toJson()));
    }

    public void setLayout(TreeLayout treeLayout){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setLayout", treeLayout.toString()));
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

    public void setColor(String colorArray){
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setColor", colorArray));
    }
}
