package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.github.appreciated.apexcharts.ApexCharts;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;

public class ConceptionKindInfoWidget extends HorizontalLayout {

    public ConceptionKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(250,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"概念类型数量:","500");

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Solid.CIRCLE.create(),"概念实体数量:","1,000,000,000");

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(25,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout2);

        Label messageText = new Label("Top 10 Conception Types with more entities ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        ApexCharts apexCharts = new ConceptionEntityCountChart()
                .withColors("#168eea", "#ee4f4f", "#03a9f4", "#76b852", "#323b43", "#59626a", "#0288d1", "#ffc107", "#d32f2f", "#00d1b2","#ced7df").build();
        //https://www.materialpalette.com/
        //https://materialui.co/colors/
        //http://brandcolors.net/
        apexCharts.setWidth("330");
        rightComponentContainer.add(apexCharts);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,apexCharts);
    }
}
