package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.TimeFlowInfoWidget;

@Route("timeFlowDetailInfo/:timeFlow")
public class TimeFlowDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String timeFlowName;
    private VerticalLayout leftSideContainerLayout;
    public TimeFlowDetailUI(){}

    public TimeFlowDetailUI(String timeFlowName){
        this.timeFlowName = timeFlowName;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.timeFlowName = beforeEnterEvent.getRouteParameters().get("timeFlow").get();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderTimeFlowData();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderTimeFlowData(){
        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setPadding(false);
        //mainContainerLayout.setMargin(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(280, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"时间流概览");
        leftSideContainerLayout.add(filterTitle2);
        //singleConceptionKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);


        HorizontalLayout singleConceptionKindInfoElementsContainerLayout = new HorizontalLayout();
        singleConceptionKindInfoElementsContainerLayout.setSpacing(false);
        singleConceptionKindInfoElementsContainerLayout.setMargin(false);
        singleConceptionKindInfoElementsContainerLayout.setHeight(10,Unit.PIXELS);
        leftSideContainerLayout.add(singleConceptionKindInfoElementsContainerLayout);


        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TIMER),this.timeFlowName,
                null,null);
        secondaryTitleActionBar.setWidth(95,Unit.PERCENTAGE);
        leftSideContainerLayout.add(secondaryTitleActionBar);




        VerticalLayout timeFlowInformationLayout = new VerticalLayout();
        //timeFlowInformationLayout.setPadding(false);
        leftSideContainerLayout.add(timeFlowInformationLayout);

        HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
        timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);



/*
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
      //  if(icon != null){
       //     icon.setSize("10px");
       //     icon.addClassNames("text-secondary");
      //      horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(5, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
     //   }
*/
        NativeLabel conceptionEntityNumberText = new NativeLabel("时间跨度:");
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        timeHorizontalLayout.add(conceptionEntityNumberText);

        Span fromYear = new Span();

        NativeLabel displayValue = new NativeLabel("1990");
        displayValue.addClassNames("text-xl","font-extrabold");
        fromYear.add(displayValue);

        fromYear.getElement().getThemeList().add("badge pill");
        fromYear.addClassNames("text-xl","text-primary","font-extrabold");

        fromYear.getStyle().set("color","#2e4e7e");
        Span toYear = new Span("2050");
        toYear.getElement().getThemeList().add("badge pill");
        toYear.addClassNames("text-xl","font-bold");
        toYear.getStyle().set("color","#2e4e7e");
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
        NativeLabel label3 = new NativeLabel("Year Entities:");
        label3.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label3);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout3);

        HorizontalLayout heightSpaceDiv01 = new HorizontalLayout();
        timeFlowInformationLayout.add(heightSpaceDiv01);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label4 = new NativeLabel("Month Entities:");
        label4.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label4);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout4);

        HorizontalLayout heightSpaceDiv02 = new HorizontalLayout();
        timeFlowInformationLayout.add(heightSpaceDiv02);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label5 = new NativeLabel("Day Entities:");
        label5.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label5);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout5);

        HorizontalLayout heightSpaceDiv03 = new HorizontalLayout();
        timeFlowInformationLayout.add(heightSpaceDiv03);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label6 = new NativeLabel("Hour Entities:");
        label6.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label6);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout6);

        HorizontalLayout heightSpaceDiv04 = new HorizontalLayout();
        timeFlowInformationLayout.add(heightSpaceDiv04);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label7 = new NativeLabel("Minute Entities:");
        label7.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label7);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout7);

    }
}
