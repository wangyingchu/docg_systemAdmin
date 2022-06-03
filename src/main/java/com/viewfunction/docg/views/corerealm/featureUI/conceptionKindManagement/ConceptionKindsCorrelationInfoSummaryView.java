package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("conceptionKindsCorrelationInfoSummary")
public class ConceptionKindsCorrelationInfoSummaryView extends VerticalLayout {

    public ConceptionKindsCorrelationInfoSummaryView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setSizeFull();
        add(new Label("hello world,ConceptionKindsCorrelationInfoSummaryView"));
    }

}
