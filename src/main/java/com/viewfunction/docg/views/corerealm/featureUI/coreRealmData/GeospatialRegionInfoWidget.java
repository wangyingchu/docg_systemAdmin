package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

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
        //personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout,"GeospatialScaleEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout2,new Icon(VaadinIcon.RHOMBUS),"GeospatialScaleEvent 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout2);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,"ContinentEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout3);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,"CountryRegionEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout4);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,"ProvinceEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout5);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,"PrefectureEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout6);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,"CountyEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout7);

        HorizontalLayout horizontalLayout8 = new HorizontalLayout();
        horizontalLayout8.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout8,"TownshipEntity 数量:","1,000,000,000");
        personalInformationLayout.add(horizontalLayout8);

        HorizontalLayout horizontalLayout9 = new HorizontalLayout();
        horizontalLayout9.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout9,"VillageEntity 数量:","1,000,000,000");
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
