package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.react.ReactAdapterComponent;

import java.util.Map;

@JsModule("./externalTech/flow/integration/react/explorationResultGraphChart/exploration-result-graph-chart.tsx")
@Tag("exploration-result-graph-chart")
public class ExplorationResultGraphChart extends ReactAdapterComponent {

    public ExplorationResultGraphChart(){
    }

    public void setChartWidth(int width){
        setState("chartWidth", width);
    }

    public void setChartHeight(int height){
        setState("chartHeight", height);
    }

    public void setChartData(Map<String,Object> valueMap){
        setState("chartData", valueMap);
    }

}
