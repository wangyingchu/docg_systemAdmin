package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial.ConceptionEntitySpatialChart;

import java.util.List;

public class GeospatialRegionCorrelationExploreView extends VerticalLayout {
    private String geospatialRegionName;
    private GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart;
    private ConceptionEntitySpatialChart conceptionEntitySpatialChart;

    public GeospatialRegionCorrelationExploreView(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(geospatialRegionName);
        conceptionEntitySpatialChart = new ConceptionEntitySpatialChart();
        conceptionEntitySpatialChart.setWidth(900, Unit.PIXELS);
        conceptionEntitySpatialChart.setHeight(700,Unit.PIXELS);
        add(this.conceptionEntitySpatialChart);
    }

    public void renderGeospatialRegionData(List<GeospatialScaleEntity> geospatialScaleEntityList){

        if(geospatialScaleEntityList != null & geospatialScaleEntityList.size()>0){
            GeospatialScaleEntity currGeospatialScaleEntity = geospatialScaleEntityList.get(0);

            this.conceptionEntitySpatialChart.renderMapAndSpatialInfo(RealmConstant.GeospatialScaleEntityClass, currGeospatialScaleEntity.getGeospatialScaleEntityUID());
        }

        System.out.println("==================");
        System.out.println("==================");
        System.out.println(geospatialScaleEntityList);
        System.out.println("==================");
        System.out.println("==================");



    }
}
