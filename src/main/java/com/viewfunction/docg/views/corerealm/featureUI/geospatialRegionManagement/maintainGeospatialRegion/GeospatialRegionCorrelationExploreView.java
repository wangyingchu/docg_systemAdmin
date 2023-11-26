package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEntity;

import java.util.List;

public class GeospatialRegionCorrelationExploreView extends VerticalLayout {
    private String geospatialRegionName;

    public GeospatialRegionCorrelationExploreView(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
    }

    public void renderGeospatialRegionData(List<GeospatialScaleEntity> geospatialScaleEntityList){

        System.out.println("==================");
        System.out.println("==================");
        System.out.println(geospatialScaleEntityList);
        System.out.println("==================");
        System.out.println("==================");



    }
}
