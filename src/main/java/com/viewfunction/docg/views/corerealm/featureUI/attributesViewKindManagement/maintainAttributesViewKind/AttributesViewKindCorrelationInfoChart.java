package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;

import java.util.List;

@JavaScript("./visualization/feature/attributesViewKindCorrelationInfoChart-connector.js")
public class AttributesViewKindCorrelationInfoChart extends VerticalLayout {

    public AttributesViewKindCorrelationInfoChart(int chartHeight){
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.23.0/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(chartHeight, Unit.PIXELS);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_AttributesViewKindCorrelationInfoChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setData(AttributesViewKind targetAttributesViewKind, List<ConceptionKind> containerConceptionKindsList,
                        List<AttributeKind> containsAttributeKindsList){
        System.out.println(targetAttributesViewKind);
        System.out.println(containerConceptionKindsList);
        System.out.println(containsAttributeKindsList);
    }
}
