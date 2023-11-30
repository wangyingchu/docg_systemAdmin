package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialRegionRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

import java.text.NumberFormat;
import java.util.List;

public class GeospatialRegionInfoWidget extends VerticalLayout {

    private boolean contentAlreadyLoaded = false;
    private NumberFormat numberFormat;
    private Accordion accordion;

    public GeospatialRegionInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");
        this.setWidth(100, Unit.PERCENTAGE);
        this.numberFormat = NumberFormat.getInstance();

        accordion = new Accordion();
        accordion.setWidth(100, Unit.PERCENTAGE);
        add(accordion);
    }

    public void loadWidgetContent(){
        if(!this.contentAlreadyLoaded){
            this.contentAlreadyLoaded = true;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            List<GeospatialRegion> existingGeospatialRegionList =  coreRealm.getGeospatialRegions();
            if(existingGeospatialRegionList != null){
                for(GeospatialRegion currentGeospatialRegion : existingGeospatialRegionList){
                    String geospatialRegionName = currentGeospatialRegion.getGeospatialRegionName();
                    GeospatialRegionRuntimeStatistics geospatialRegionRuntimeStatistics = currentGeospatialRegion.getGeospatialRegionRuntimeStatistics();

                    VerticalLayout geoSpatialInformationLayout = new VerticalLayout();
                    geoSpatialInformationLayout.setPadding(false);

                    HorizontalLayout horizontalLayout = new HorizontalLayout();
                    horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    new PrimaryKeyValueDisplayItem(horizontalLayout,FontAwesome.Solid.MAP.create(),"GeospatialScaleEntity 数量:",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsTotalGeospatialScaleEntityCount()));
                    geoSpatialInformationLayout.add(horizontalLayout);

                    HorizontalLayout horizontalLayout2 = new HorizontalLayout();
                    horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    new PrimaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.MAP_LOCATION.create(),"GeospatialScaleEvent 数量:",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersTotalGeospatialScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout2);

                    HorizontalLayout horizontalLayout3 = new HorizontalLayout();
                    horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label3 = new NativeLabel("ContinentEntities:");
                    label3.getElement().getThemeList().add("badge success small");
                    horizontalLayout3.add(label3);
                    new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsContinentScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersContinentScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout3);

                    HorizontalLayout horizontalLayout4 = new HorizontalLayout();
                    horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label4 = new NativeLabel("CountryRegionEntities:");
                    label4.getElement().getThemeList().add("badge success small");
                    horizontalLayout4.add(label4);
                    new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsCountry_RegionScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersCountry_RegionScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout4);

                    HorizontalLayout horizontalLayout5 = new HorizontalLayout();
                    horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label5 = new NativeLabel("ProvinceEntities:");
                    label5.getElement().getThemeList().add("badge success small");
                    horizontalLayout5.add(label5);
                    new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsProvinceScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersProvinceScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout5);

                    HorizontalLayout horizontalLayout6 = new HorizontalLayout();
                    horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label6 = new NativeLabel("PrefectureEntities:");
                    label6.getElement().getThemeList().add("badge success small");
                    horizontalLayout6.add(label6);
                    new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsPrefectureScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersPrefectureScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout6);

                    HorizontalLayout horizontalLayout7 = new HorizontalLayout();
                    horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label7 = new NativeLabel("CountyEntities:");
                    label7.getElement().getThemeList().add("badge success small");
                    horizontalLayout7.add(label7);
                    new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsCountyScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersCountyScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout7);

                    HorizontalLayout horizontalLayout8 = new HorizontalLayout();
                    horizontalLayout8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label8 = new NativeLabel("TownshipEntities:");
                    label8.getElement().getThemeList().add("badge success small");
                    horizontalLayout8.add(label8);
                    new SecondaryKeyValueDisplayItem(horizontalLayout8,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsTownshipScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout8,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersTownshipScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout8);

                    HorizontalLayout horizontalLayout9 = new HorizontalLayout();
                    horizontalLayout9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label9 = new NativeLabel("VillageEntities:");
                    label9.getElement().getThemeList().add("badge success small");
                    horizontalLayout9.add(label9);
                    new SecondaryKeyValueDisplayItem(horizontalLayout9,FontAwesome.Solid.MAP.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsVillageScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout9,FontAwesome.Solid.MAP_LOCATION.create(),"",
                            this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersVillageScaleTimeScaleEventCount()));
                    geoSpatialInformationLayout.add(horizontalLayout9);

                    AccordionPanel geoSpatialRegionInfoPanel1 =accordion.add(geospatialRegionName, geoSpatialInformationLayout);
                    geoSpatialRegionInfoPanel1.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);
                }
            }
            coreRealm.closeGlobalSession();
        }
    }
}
