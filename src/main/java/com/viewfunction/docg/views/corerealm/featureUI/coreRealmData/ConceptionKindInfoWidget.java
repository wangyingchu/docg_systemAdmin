package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.chart.ChartGenerator;

public class ConceptionKindInfoWidget extends HorizontalLayout {

    public ConceptionKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(350,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);


        Label conceptionKindNumberText = new Label("概念类型数量:");
        conceptionKindNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        leftComponentContainer.add(conceptionKindNumberText);
        Label conceptionKindNumberValue = new Label("500");
        conceptionKindNumberValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-30");
        leftComponentContainer.add(conceptionKindNumberValue);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);


        Label conceptionEntityNumberText = new Label("概念实体数量:");
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        leftComponentContainer.add(conceptionEntityNumberText);
        Label conceptionEntityNumberValue = new Label("1,000,000,000");
        conceptionEntityNumberValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-30");
        leftComponentContainer.add(conceptionEntityNumberValue);

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setWidth(350,Unit.PIXELS);
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);

        this.setFlexGrow(1,rightComponentContainer);
        rightComponentContainer.add(ChartGenerator.generateApexChartsLineChart());
    }
}
