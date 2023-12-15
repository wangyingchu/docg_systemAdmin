package com.viewfunction.docg.element.commonComponent.chart;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

@JavaScript("./visualization/common/bulletChart_amcharts-connector.js")
public class BulletChart extends Div {

    public BulletChart(){

        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/index.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/hierarchy.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/xy.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/themes/Animated.js");

        initConnector(getElement());
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.common_BulletChart_amcharts.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
