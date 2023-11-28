package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntitySpatialAttributeView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import java.text.NumberFormat;
import java.util.List;

public class GeospatialRegionCorrelationExploreView extends VerticalLayout {
    private String geospatialRegionName;
    public enum RenderType {Single,List_SameLevel,List_SubLevel}
    private int viewHeight;
    private int viewWidth;
    private Grid<AttributeValue> entityAttributesInfoGrid;
    private SecondaryTitleActionBar geospatialScaleEntityTitleActionBar;
    private NativeLabel entityUID;
    private PrimaryKeyValueDisplayItem relatedConceptionEntitiesCountDisplayItem;
    private NumberFormat numberFormat;
    private VerticalLayout entityInfoContainerLayout;
    public GeospatialRegionCorrelationExploreView(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.numberFormat = NumberFormat.getInstance();

        HorizontalLayout singleGeospatialRegionElementsContainerLayout = new HorizontalLayout();
        singleGeospatialRegionElementsContainerLayout.setSpacing(false);
        singleGeospatialRegionElementsContainerLayout.setMargin(false);
        singleGeospatialRegionElementsContainerLayout.setPadding(false);
        singleGeospatialRegionElementsContainerLayout.setHeight("17px");
        add(singleGeospatialRegionElementsContainerLayout);

        SecondaryIconTitle geospatialScaleEntityTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"地理空间区域尺度实体概览");
        singleGeospatialRegionElementsContainerLayout.add(geospatialScaleEntityTitle);

        HorizontalLayout singleGeospatialRegionElementsFootprintMessageLayout = new HorizontalLayout();
        add(singleGeospatialRegionElementsFootprintMessageLayout);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        singleGeospatialRegionElementsFootprintMessageLayout.add(footPrintStartIcon);

        Button _CONTINENTButton = new Button("亚洲");
        _CONTINENTButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_CONTINENTButton);

        Icon divIcon0 = VaadinIcon.ITALIC.create();
        divIcon0.setSize("12px");
        divIcon0.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon0);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon0);

        Button _COUNTRY_REGIONButton = new Button("中国");
        _COUNTRY_REGIONButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_COUNTRY_REGIONButton);

        Icon divIcon1 = VaadinIcon.ITALIC.create();
        divIcon1.setSize("12px");
        divIcon1.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon1);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon1);

        Button _PROVINCEButton = new Button("新疆维吾尔自治区");
        _PROVINCEButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_PROVINCEButton);

        Icon divIcon2 = VaadinIcon.ITALIC.create();
        divIcon2.setSize("12px");
        divIcon2.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon2);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon2);

        Button _PREFECTUREButton = new Button("克孜勒苏柯尔克孜自治州");
        _PREFECTUREButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_PREFECTUREButton);

        Icon divIcon3 = VaadinIcon.ITALIC.create();
        divIcon3.setSize("12px");
        divIcon3.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon3);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon3);

        Button _COUNTYButton = new Button("阿合奇县");
        _COUNTYButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_COUNTYButton);

        Icon divIcon4 = VaadinIcon.ITALIC.create();
        divIcon4.setSize("12px");
        divIcon4.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon4);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon4);

        Button _TOWNSHIPButton = new Button("库兰萨日克乡");
        _TOWNSHIPButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_TOWNSHIPButton);

        Icon divIcon5 = VaadinIcon.ITALIC.create();
        divIcon5.setSize("12px");
        divIcon5.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon5);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon5);

        NativeLabel villaggeName = new NativeLabel("阿克特克提尔村委会");
        singleGeospatialRegionElementsFootprintMessageLayout.add(villaggeName);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,villaggeName);

        Icon divIcon6 = VaadinIcon.KEY_O.create();
        divIcon6.setSize("10px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon6);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon6);
        entityUID = new NativeLabel("-");
        singleGeospatialRegionElementsFootprintMessageLayout.add(entityUID);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,entityUID);

        this.geospatialScaleEntityTitleActionBar = new SecondaryTitleActionBar(FontAwesome.Solid.MAP.create(),"-",null,null);
        this.geospatialScaleEntityTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        add(this.geospatialScaleEntityTitleActionBar);

        HorizontalLayout geospatialScaleEntityInfoContainerLayout = new HorizontalLayout();
        geospatialScaleEntityInfoContainerLayout.setSpacing(false);
        geospatialScaleEntityInfoContainerLayout.setMargin(false);
        geospatialScaleEntityInfoContainerLayout.setPadding(false);
        add(geospatialScaleEntityInfoContainerLayout);

        entityInfoContainerLayout = new VerticalLayout();
        entityInfoContainerLayout.setSpacing(false);
        entityInfoContainerLayout.setMargin(false);
        entityInfoContainerLayout.setPadding(false);
        entityInfoContainerLayout.setWidth(500,Unit.PIXELS);
        geospatialScaleEntityInfoContainerLayout.add(entityInfoContainerLayout);
        entityInfoContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        HorizontalLayout summaryInfoContainer = new HorizontalLayout();
        summaryInfoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        summaryInfoContainer.setWidthFull();
        summaryInfoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        entityInfoContainerLayout.add(summaryInfoContainer);

        relatedConceptionEntitiesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(summaryInfoContainer, FontAwesome.Solid.CIRCLE.create(),"关联概念实体数量:","-");

        entityAttributesInfoGrid = new Grid<>();
        entityAttributesInfoGrid.setWidth(300, Unit.PIXELS);
        entityAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        entityAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeName).setHeader("属性名称").setKey("idx_0");
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeValue).setHeader("属性值").setKey("idx_1").setFlexGrow(1).setResizable(true);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        entityAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.INPUT,"属性值");
        entityAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        geospatialScaleEntityInfoContainerLayout.add(entityAttributesInfoGrid);
        geospatialScaleEntityInfoContainerLayout.setVerticalComponentAlignment(Alignment.START,entityAttributesInfoGrid);
    }

    private void renderSingleGeospatialRegionEntity(GeospatialScaleEntity targetGeospatialScaleEntity){
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

        String entityEngliseName = targetGeospatialScaleEntity.getEnglishName() != null ? targetGeospatialScaleEntity.getEnglishName() : "-";
        this.geospatialScaleEntityTitleActionBar.updateTitleContent( currentGeospatialScaleGrade.toString() + " : "+targetGeospatialScaleEntity.getChineseName()+" ("+entityEngliseName+") ");
        this.entityUID.setText(currentGeospatialScaleEntityUID);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        GeospatialRegion geospatialRegion = coreRealm.getOrCreateGeospatialRegion(this.geospatialRegionName);
        targetGeospatialScaleEntity = geospatialRegion.getEntityByGeospatialCode(targetGeospatialScaleEntity.getGeospatialCode());

        Long relatedConceptionEntitiesCount = targetGeospatialScaleEntity.countAttachedConceptionEntities(GeospatialScaleEntity.GeospatialScaleLevel.SELF);
        targetGeospatialScaleEntity.getChildEntities();
        targetGeospatialScaleEntity.getParentEntity();
        targetGeospatialScaleEntity.getFellowEntities();

        relatedConceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(relatedConceptionEntitiesCount));

        ConceptionKind targetGeoConceptionKind = coreRealm.getConceptionKind(geospatialScaleEntityKindName);
        ConceptionEntity targetConceptionEntity = targetGeoConceptionKind.getEntityByUID(currentGeospatialScaleEntityUID);
        List<AttributeValue> allAttributesList = targetConceptionEntity.getAttributes();
        entityAttributesInfoGrid.setItems(allAttributesList);

        coreRealm.closeGlobalSession();


    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewWidth(int value){
        this.viewWidth = value;
        this.entityInfoContainerLayout.setWidth(this.viewWidth-310, Unit.PIXELS);
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewHeight(int value){
        this.viewHeight = value;
        this.entityAttributesInfoGrid.setHeight(this.viewHeight-100,Unit.PIXELS);
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

    private void renderSingleGeospatialRegionEntity0(GeospatialScaleEntity targetGeospatialScaleEntity){
        //this.geospatialChartContainer.removeAll();
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
        //this.geospatialChartContainer.add(conceptionEntitySpatialAttributeView);
        conceptionEntitySpatialAttributeView.renderEntitySpatialInfo();
    }

    private void renderSameLevelGeospatialRegionEntityList(List<GeospatialScaleEntity> geospatialScaleEntityList){
        //this.geospatialChartContainer.removeAll();
        //GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(this.geospatialRegionName);
        //this.geospatialChartContainer.add(geospatialRegionCorrelationInfoChart);
        //geospatialRegionCorrelationInfoChart.renderEntitiesSpatialInfo(geospatialScaleEntityList);
    }

    private void renderSubLevelGeospatialRegionEntityList(List<GeospatialScaleEntity> geospatialScaleEntityList){
        //this.geospatialChartContainer.removeAll();
        //GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(this.geospatialRegionName);
       // this.geospatialChartContainer.add(geospatialRegionCorrelationInfoChart);
        //geospatialRegionCorrelationInfoChart.renderEntitiesSpatialInfo(geospatialScaleEntityList);
    }
}
