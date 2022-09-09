package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConceptionEntitySpatialDetailView extends VerticalLayout {

    private VerticalLayout mapContainer;
    public ConceptionEntitySpatialDetailView(){
        mapContainer = new VerticalLayout();
        mapContainer.setWidth(300, Unit.PIXELS);
        mapContainer.setHeight(400,Unit.PIXELS);
        add(mapContainer);
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        //renderEntitySpatialInfo();


        ConceptionEntitySpatialChart conceptionEntitySpatialChart = new ConceptionEntitySpatialChart();

        mapContainer.add(conceptionEntitySpatialChart);
    }
}
