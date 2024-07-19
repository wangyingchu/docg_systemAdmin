package com.viewfunction.docg.element.externalTechFeature.conceptionEntitiesRelationsChart;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

//@NpmPackage(value = "@neo4j-nvl/base", version = "0.3.1")
@NpmPackage(value = "@neo4j-nvl/react", version = "0.3.1")
@JsModule("./externalTech/flow/integration/react/conceptionEntitiesRelationsChart/conception-entities-relations-chart.tsx")
@Tag("conception-entities-relations-chart")
public class ConceptionEntitiesRelationsChart extends ReactAdapterComponent {
}
