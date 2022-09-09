package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConceptionEntitySpatialDetailView extends VerticalLayout {

    private VerticalLayout mapContainer;
    public ConceptionEntitySpatialDetailView(){
        mapContainer = new VerticalLayout();
        mapContainer.setWidth(300, Unit.PIXELS);
        mapContainer.setHeight(400,Unit.PIXELS);
        //add(mapContainer);
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        //renderEntitySpatialInfo();

        Div continerDiv = new Div();
        continerDiv.setWidth("300px");
        continerDiv.setHeight("300px");
        ConceptionEntitySpatialChart conceptionEntitySpatialChart = new ConceptionEntitySpatialChart();
        continerDiv.add(conceptionEntitySpatialChart);
        add(continerDiv);
    }
}
