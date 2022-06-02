package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.vaadin.flow.component.html.IFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionKindsCorrelationInfoSummaryChart extends VerticalLayout {

    private Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet;
    private List<String> conceptionKindIdList;
    public ConceptionKindsCorrelationInfoSummaryChart(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet,
                                                      int windowWidth, int windowHeight){
        this.conceptionKindCorrelationInfoSet = conceptionKindCorrelationInfoSet;
        conceptionKindIdList = new ArrayList<>();
    }

    public void loadConceptionKindCorrelationInfo(){
        //UI.getCurrent().getPage();
        IFrame _IFrame = new IFrame();
        _IFrame.getStyle().set("border","0");
        _IFrame.setSrc("http://192.168.3.7:7141/instanceRelationsExplore/U3BhY2VOYW1lW0NJTURhdGFEaXNjb3ZlckVuZ2luZVVUXU9iamVjdFR5cGVbQ2ltT2JqZWN0VHlwZVNUQVRfMDAxXUluc3RhbmNlUklEWyMxNTc6N10=");
        _IFrame.setHeight(550, Unit.PIXELS);
        _IFrame.setWidth(950,Unit.PIXELS);
        add(_IFrame);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadConceptionKindCorrelationInfo();
    }
}
