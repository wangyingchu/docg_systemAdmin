package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.dom.Element;
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
        UI.getCurrent().getPage().fetchCurrentURL(currentUrl -> {
            // This is your own method that you may do something with the url.
            // Please note that this method runs asynchronously
            //storeCurrentURL(currentUrl);
        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            // This is your own method that you may do something with the screen width.
            // Please note that this method runs asynchronously
            //handleScreenWidth(details.getScreenWidth());
        });



        IFrame _IFrame = new IFrame();
        _IFrame.getStyle().set("border","0");
        _IFrame.setSrc("http://192.168.3.7:7141/instanceRelationsExplore/U3BhY2VOYW1lW0NJTURhdGFEaXNjb3ZlckVuZ2luZVVUXU9iamVjdFR5cGVbQ2ltT2JqZWN0VHlwZVNUQVRfMDAxXUluc3RhbmNlUklEWyMxNTc6N10=");
        _IFrame.setHeight(550, Unit.PIXELS);
        _IFrame.setWidth(950,Unit.PIXELS);
        add(_IFrame);
    }

    public static void logElementSize(String name,
                                      Element element) {
        Page page = UI.getCurrent().getPage();

        page.executeJs(
                "console.log($0 + ' size:', "
                        + "$1.offsetWidth, $1.offsetHeight)",
                name, element);
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadConceptionKindCorrelationInfo();
    }
}
