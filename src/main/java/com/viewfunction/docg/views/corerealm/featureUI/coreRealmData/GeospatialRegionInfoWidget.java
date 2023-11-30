package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

public class GeospatialRegionInfoWidget extends VerticalLayout {

    public GeospatialRegionInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");
        this.setWidth(100, Unit.PERCENTAGE);

        Accordion accordion = new Accordion();
        accordion.setWidth(100, Unit.PERCENTAGE);

        VerticalLayout geoSpatialInformationLayout = new VerticalLayout();
        geoSpatialInformationLayout.setPadding(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout,FontAwesome.Solid.MAP.create(),"GeospatialScaleEntity 数量:","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.MAP_LOCATION.create(),"GeospatialScaleEvent 数量:","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout2);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label3 = new NativeLabel("ContinentEntities:");
        label3.getElement().getThemeList().add("badge success small");
        horizontalLayout3.add(label3);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout3);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label4 = new NativeLabel("CountryRegionEntities:");
        label4.getElement().getThemeList().add("badge success small");
        horizontalLayout4.add(label4);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout4);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label5 = new NativeLabel("ProvinceEntities:");
        label5.getElement().getThemeList().add("badge success small");
        horizontalLayout5.add(label5);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout5);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label6 = new NativeLabel("PrefectureEntities:");
        label6.getElement().getThemeList().add("badge success small");
        horizontalLayout6.add(label6);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout6);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label7 = new NativeLabel("CountyEntities:");
        label7.getElement().getThemeList().add("badge success small");
        horizontalLayout7.add(label7);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout7);

        HorizontalLayout horizontalLayout8 = new HorizontalLayout();
        horizontalLayout8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label8 = new NativeLabel("TownshipEntities:");
        label8.getElement().getThemeList().add("badge success small");
        horizontalLayout8.add(label8);
        new SecondaryKeyValueDisplayItem(horizontalLayout8,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout8,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout8);

        HorizontalLayout horizontalLayout9 = new HorizontalLayout();
        horizontalLayout9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label9 = new NativeLabel("VillageEntities:");
        label9.getElement().getThemeList().add("badge success small");
        horizontalLayout9.add(label9);
        new SecondaryKeyValueDisplayItem(horizontalLayout9,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout9,FontAwesome.Solid.MAP_LOCATION.create(),"","1,000,000,000");
        geoSpatialInformationLayout.add(horizontalLayout9);

        AccordionPanel geoSpatialRegionInfoPanel1 =accordion.add("DefaultGeospatialRegion", geoSpatialInformationLayout);
        geoSpatialRegionInfoPanel1.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        AccordionPanel personalInfoPanel2 =accordion.add("Geospatial Region 2", new VerticalLayout());
        personalInfoPanel2.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        AccordionPanel personalInfoPanel3 =accordion.add("Geospatial Region 3", new VerticalLayout());
        personalInfoPanel3.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        add(accordion);
    }

    public void loadWidgetContent(){}

}
