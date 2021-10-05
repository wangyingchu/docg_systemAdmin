package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

public class TimeFlowInfoWidget extends VerticalLayout {

    public TimeFlowInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");
        this.setWidth(100, Unit.PERCENTAGE);

        Accordion accordion = new Accordion();
        accordion.setWidth(100, Unit.PERCENTAGE);

        VerticalLayout timeFlowInformationLayout = new VerticalLayout();
        timeFlowInformationLayout.setPadding(false);

        HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
        timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        Span fromYear = new Span("1990");
        fromYear.getElement().getThemeList().add("badge pill");
        fromYear.addClassNames("text-xl","font-bold");
        Span toYear = new Span("2050");
        toYear.getElement().getThemeList().add("badge pill");
        toYear.addClassNames("text-xl","font-bold");
        Span yearDiv = new Span(" - ");
        timeHorizontalLayout.add(fromYear);
        timeHorizontalLayout.add(yearDiv);
        timeHorizontalLayout.add(toYear);
        timeFlowInformationLayout.add(timeHorizontalLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout, FontAwesome.Solid.CLOCK.create(),"TimeScaleEntity 数量:","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new PrimaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.BEZIER_CURVE.create(),"TimeScaleEvent 数量:","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout2);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label3 = new Label("Year Entities:");
        label3.getElement().getThemeList().add("badge success small");
        horizontalLayout3.add(label3);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout3);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label4 = new Label("Month Entities:");
        label4.getElement().getThemeList().add("badge success small");
        horizontalLayout4.add(label4);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout4);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label5 = new Label("Day Entities:");
        label5.getElement().getThemeList().add("badge success small");
        horizontalLayout5.add(label5);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout5);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label6 = new Label("Hour Entities:");
        label6.getElement().getThemeList().add("badge success small");
        horizontalLayout6.add(label6);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout6);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label label7 = new Label("Minute Entities:");
        label7.getElement().getThemeList().add("badge success small");
        horizontalLayout7.add(label7);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout7);

        AccordionPanel timeFlowInfoPanel1 =accordion.add("DefaultTimeFlow", timeFlowInformationLayout);
        timeFlowInfoPanel1.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);

        add(accordion);
    }
}
