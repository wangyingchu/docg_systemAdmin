package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;

@JavaScript("./visualization/feature/conceptionKindCorrelationInfoChart-connector.js")
public class ConceptionKindCorrelationInfoChart extends Div {

    public ConceptionKindCorrelationInfoChart(int chartHeight){
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.21.1/dist/cytoscape.min.js");

        setWidth(500, Unit.PIXELS);
        //setHeight(700,Unit.PIXELS);


        this.setHeight(chartHeight,Unit.PIXELS);

        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionKindCorrelationInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
