package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Label;
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

        VerticalLayout personalInformationLayout = new VerticalLayout();
        personalInformationLayout.setPadding(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout,FontAwesome.Solid.MAP.create(),"GeospatialScaleEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.MAP_MARKER_ALT.create(),"GeospatialScaleEvent 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout2);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label3 = new Label("ContinentEntities:");
        label3.getElement().getThemeList().add("badge success small");
        horizontalLayout3.add(label3);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout3);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label4 = new Label("CountryRegionEntities:");
        label4.getElement().getThemeList().add("badge success small");
        horizontalLayout4.add(label4);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout4);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label5 = new Label("ProvinceEntities:");
        label5.getElement().getThemeList().add("badge success small");
        horizontalLayout5.add(label5);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout5);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label6 = new Label("PrefectureEntities:");
        label6.getElement().getThemeList().add("badge success small");
        horizontalLayout6.add(label6);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout6);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label7 = new Label("CountyEntities:");
        label7.getElement().getThemeList().add("badge success small");
        horizontalLayout7.add(label7);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout7);

        HorizontalLayout horizontalLayout8 = new HorizontalLayout();
        horizontalLayout8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label8 = new Label("TownshipEntities:");
        label8.getElement().getThemeList().add("badge success small");
        horizontalLayout8.add(label8);
        new SecondaryKeyValueDisplayItem(horizontalLayout8,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout8,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout8);

        HorizontalLayout horizontalLayout9 = new HorizontalLayout();
        horizontalLayout9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label9 = new Label("VillageEntities:");
        label9.getElement().getThemeList().add("badge success small");
        horizontalLayout9.add(label9);
        new SecondaryKeyValueDisplayItem(horizontalLayout9,FontAwesome.Solid.MAP.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout9,FontAwesome.Solid.MAP_MARKER_ALT.create(),"","1,000,000,000");
        personalInformationLayout.add(horizontalLayout9);

        AccordionPanel personalInfoPanel1 =accordion.add("DefaultGeospatialRegion", personalInformationLayout);
        personalInfoPanel1.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        AccordionPanel personalInfoPanel2 =accordion.add("Geospatial Region 2", new VerticalLayout());
        personalInfoPanel2.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        AccordionPanel personalInfoPanel3 =accordion.add("Geospatial Region 3", new VerticalLayout());
        personalInfoPanel3.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        add(accordion);
    }

}
