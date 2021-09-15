package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.github.appreciated.apexcharts.ApexCharts;

import com.vaadin.flow.component.Unit;
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

        new PrimaryKeyValueDisplayItem(leftComponentContainer,"概念类型数量:","500");

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,"概念实体数量:","1,000,000,000");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        ApexCharts apexCharts = new ConceptionEntityCountChart()
                .withColors("#0288d1", "#b3e5fc", "#03a9f4", "#f44336", "#ffc107", "#212121", "#757575", "#BDBDBD", "#d32f2f", "#4caf50").build();
        //https://www.materialpalette.com/
        apexCharts.setWidth("330");
        rightComponentContainer.add(apexCharts);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,apexCharts);
    }
}
