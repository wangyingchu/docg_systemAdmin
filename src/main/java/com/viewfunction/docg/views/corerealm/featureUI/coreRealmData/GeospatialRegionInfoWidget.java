package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        new PrimaryKeyValueDisplayItem(horizontalLayout,new Icon(VaadinIcon.RHOMBUS),"GeospatialScaleEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout2,"GeospatialScaleEvent 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout2);

        FontAwesome.Regular.Icon ccc= FontAwesome.Regular.SNOWFLAKE.create();
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,ccc,"ContinentEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout3,"Related Events:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout3);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,new Icon(VaadinIcon.RHOMBUS),"CountryRegionEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout4,"Related Events:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout4);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,new Icon(VaadinIcon.RHOMBUS),"ProvinceEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout5,"Related Events:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout5);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,new Icon(VaadinIcon.RHOMBUS),"PrefectureEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout6,"Related Events:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout6);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,new Icon(VaadinIcon.RHOMBUS),"CountyEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout7,"Related Events:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout7);

        HorizontalLayout horizontalLayout8 = new HorizontalLayout();
        horizontalLayout8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout8,new Icon(VaadinIcon.RHOMBUS),"TownshipEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout8,"Related Events:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout8);

        HorizontalLayout horizontalLayout9 = new HorizontalLayout();
        horizontalLayout9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout9,new Icon(VaadinIcon.RHOMBUS),"VillageEntities:","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout9,"Related Events:","1,000,000,000");
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
