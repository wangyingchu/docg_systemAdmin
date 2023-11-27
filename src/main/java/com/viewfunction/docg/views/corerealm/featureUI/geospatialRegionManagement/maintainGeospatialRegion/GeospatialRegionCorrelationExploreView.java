package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntitySpatialAttributeView;

import java.util.List;

public class GeospatialRegionCorrelationExploreView extends VerticalLayout {
    private String geospatialRegionName;
    private VerticalLayout geospatialChartContainer;
    public enum RenderType {Single,List_SameLevel,List_SubLevel}
    private int viewHeight;
    private int viewWidth;

    public GeospatialRegionCorrelationExploreView(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);

        this.geospatialChartContainer = new VerticalLayout();
        this.geospatialChartContainer.setMargin(false);

        add(this.geospatialChartContainer);
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewWidth(int value){
        this.viewWidth = value;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewHeight(int value){
        this.viewHeight = value;
    }

    public void renderGeospatialRegionData(RenderType renderType,List<GeospatialScaleEntity> geospatialScaleEntityList){
        if(geospatialScaleEntityList != null & geospatialScaleEntityList.size() > 0){
            GeospatialScaleEntity currGeospatialScaleEntity = geospatialScaleEntityList.get(0);
            switch(renderType){
                case Single -> renderSingleGeospatialRegionEntity(currGeospatialScaleEntity);
                case List_SameLevel -> renderSameLevelGeospatialRegionEntityList(geospatialScaleEntityList);
                case List_SubLevel -> renderSubLevelGeospatialRegionEntityList(geospatialScaleEntityList);
            }
        }
    }

    private void renderSingleGeospatialRegionEntity(GeospatialScaleEntity targetGeospatialScaleEntity){
        this.geospatialChartContainer.removeAll();
        String currentGeospatialScaleEntityUID = targetGeospatialScaleEntity.getGeospatialScaleEntityUID();
        GeospatialRegion.GeospatialScaleGrade currentGeospatialScaleGrade = targetGeospatialScaleEntity.getGeospatialScaleGrade();
        String geospatialScaleEntityKindName = null;
        switch(currentGeospatialScaleGrade){
            case CONTINENT -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleContinentEntityClass;
            case COUNTRY_REGION -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleCountryRegionEntityClass;
            case PROVINCE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleProvinceEntityClass;
            case PREFECTURE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScalePrefectureEntityClass;
            case COUNTY -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleCountyEntityClass;
            case TOWNSHIP -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleTownshipEntityClass;
            case VILLAGE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleVillageEntityClass;
        }

        ConceptionEntitySpatialAttributeView conceptionEntitySpatialAttributeView =
                new ConceptionEntitySpatialAttributeView(geospatialScaleEntityKindName,currentGeospatialScaleEntityUID,265);
        conceptionEntitySpatialAttributeView.setWidth(this.viewWidth, Unit.PIXELS);
        this.geospatialChartContainer.add(conceptionEntitySpatialAttributeView);
        conceptionEntitySpatialAttributeView.renderEntitySpatialInfo();
    }

    private void renderSameLevelGeospatialRegionEntityList(List<GeospatialScaleEntity> geospatialScaleEntityList){
        this.geospatialChartContainer.removeAll();
        GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(this.geospatialRegionName);
        this.geospatialChartContainer.add(geospatialRegionCorrelationInfoChart);
        geospatialRegionCorrelationInfoChart.renderEntitiesSpatialInfo(geospatialScaleEntityList);
    }

    private void renderSubLevelGeospatialRegionEntityList(List<GeospatialScaleEntity> geospatialScaleEntityList){
        this.geospatialChartContainer.removeAll();
        GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(this.geospatialRegionName);
        this.geospatialChartContainer.add(geospatialRegionCorrelationInfoChart);
        geospatialRegionCorrelationInfoChart.renderEntitiesSpatialInfo(geospatialScaleEntityList);
    }
}
