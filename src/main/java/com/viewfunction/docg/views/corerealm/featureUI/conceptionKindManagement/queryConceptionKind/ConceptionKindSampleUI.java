package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConceptionKindSampleUI extends VerticalLayout {

    private String conceptionKindName;
    private int sampleCount;

    public ConceptionKindSampleUI(String conceptionKindName,int sampleCount) {
        this.conceptionKindName = conceptionKindName;
        this.sampleCount = sampleCount;
    }
}
