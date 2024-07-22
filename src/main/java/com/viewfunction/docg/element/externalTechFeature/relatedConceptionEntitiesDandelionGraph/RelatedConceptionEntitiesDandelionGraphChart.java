package com.viewfunction.docg.element.externalTechFeature.relatedConceptionEntitiesDandelionGraph;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@NpmPackage(value = "react-force-graph", version = "1.44.4")
@NpmPackage(value = "three", version = "r166")
@JsModule("./externalTech/flow/integration/react/relatedConceptionEntitiesDandelionGraphChart/related-conception-entities-dandelion-graph-chart.tsx")
@Tag("related-conception-entities-dandelion-graph-chart")
public class RelatedConceptionEntitiesDandelionGraphChart extends ReactAdapterComponent {
}
