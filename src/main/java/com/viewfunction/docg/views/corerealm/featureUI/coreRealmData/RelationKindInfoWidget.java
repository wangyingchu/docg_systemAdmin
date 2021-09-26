package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.github.appreciated.apexcharts.ApexCharts;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;

public class RelationKindInfoWidget extends HorizontalLayout {

    public RelationKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(250,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        new PrimaryKeyValueDisplayItem(leftComponentContainer, FontAwesome.Regular.CIRCLE.create(),"关系类型数量:","120");

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Solid.CIRCLE.create(),"关系实体数量:","50,060,034");

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(25,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout2);

        Label messageText = new Label("Top 10 Relation Types with more entities ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        ApexCharts apexCharts = new RelationEntityCountChart().build();
        apexCharts.setWidth("330");
        rightComponentContainer.add(apexCharts);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,apexCharts);
    }
}
