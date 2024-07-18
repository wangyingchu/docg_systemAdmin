package com.viewfunction.docg.element.externalTechFeature.relatedConceptionEntitiesDandelionGraph;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@NpmPackage(value = "react-colorful", version = "5.6.1")
@JsModule("./externalTech/flow/integration/react/rgba-color-picker.tsx")
@Tag("rgba-color-picker")
public class RelatedConceptionEntitiesDandelionGraphChart extends ReactAdapterComponent {
}
