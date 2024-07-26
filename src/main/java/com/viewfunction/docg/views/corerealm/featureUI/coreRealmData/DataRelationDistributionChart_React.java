package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

//https://www.npmjs.com/package/react-cytoscapejs
//https://www.npmjs.com/package/cytoscape
@NpmPackage(value = "react-cytoscapejs", version = "2.0.0")
@NpmPackage(value = "cytoscape", version = "3.30.1")
@JsModule("./externalTech/flow/integration/react/dataRelationDistributionChart/data-relation-distribution-chart.tsx")
@Tag("data-relation-distribution-chart")
public class DataRelationDistributionChart_React extends ReactAdapterComponent {
}
