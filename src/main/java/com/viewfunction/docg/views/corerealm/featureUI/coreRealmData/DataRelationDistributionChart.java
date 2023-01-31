package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

@JavaScript("./visualization/feature/dataRelationDistributionChart-connector.js")
public class DataRelationDistributionChart extends VerticalLayout {

    public DataRelationDistributionChart(){
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.22.1/dist/cytoscape.min.js");
        this.getStyle().set("background-color","#EEEEEE");

        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        this.setHeight(600, Unit.PIXELS);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_DataRelationDistributionChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
