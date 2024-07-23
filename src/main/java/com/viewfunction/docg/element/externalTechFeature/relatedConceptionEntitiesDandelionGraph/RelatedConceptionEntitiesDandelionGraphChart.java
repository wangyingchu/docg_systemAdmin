package com.viewfunction.docg.element.externalTechFeature.relatedConceptionEntitiesDandelionGraph;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@NpmPackage(value = "react-force-graph", version = "1.44.4")
@NpmPackage(value = "three", version = "0.166.1")
//@NpmPackage(value = "three-css2drenderer", version = "1.0.0")
@NpmPackage(value = "three-spritetext", version = "1.8.2")



@JsModule("./externalTech/flow/integration/react/relatedConceptionEntitiesDandelionGraphChart/related-conception-entities-dandelion-graph-chart.tsx")
@Tag("related-conception-entities-dandelion-graph-chart")
public class RelatedConceptionEntitiesDandelionGraphChart extends ReactAdapterComponent {
}
