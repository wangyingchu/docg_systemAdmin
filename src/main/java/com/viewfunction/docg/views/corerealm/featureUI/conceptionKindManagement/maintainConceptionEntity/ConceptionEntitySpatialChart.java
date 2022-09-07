package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

@JavaScript("./visualization/feature/conceptionEntitySpatialChart-connector.js")
public class ConceptionEntitySpatialChart extends VerticalLayout {

    public ConceptionEntitySpatialChart(){
        setWidth(500, Unit.PIXELS);
        setHeight(500,Unit.PIXELS);

        //link to download latest l7 build js: https://unpkg.com/@antv/l7
        UI.getCurrent().getPage().addJavaScript("js/antv/l7/2.9.26/dist/l7.js");
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntitySpatialChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
