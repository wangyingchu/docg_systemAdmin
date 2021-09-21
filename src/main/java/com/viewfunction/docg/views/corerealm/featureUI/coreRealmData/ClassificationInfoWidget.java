package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.github.appreciated.apexcharts.ApexCharts;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.Title;
import com.storedobject.chart.TreeChart;
import com.storedobject.chart.TreeData;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

import java.util.Random;

public class ClassificationInfoWidget extends HorizontalLayout {

    public ClassificationInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(280,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        new PrimaryKeyValueDisplayItem(leftComponentContainer,"分类数量:","300");

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,"相关概念类型数量:","1,000,000,000");

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout2);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,"相关关系类型数量:","1,000,000,000");

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout3);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,"相关概念实体数量:","1,000,000,000");

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout4);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,"相关属性视图类型数量:","1,000,000,000");

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout5);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,"相关属性类型数量:","1,000,000,000");

        HorizontalLayout spaceDivLayout6 = new HorizontalLayout();
        spaceDivLayout6.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout6);

        Label messageText = new Label("Top 10 Conception Types with more entities ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);












        // Creating a chart display area
        SOChart soChart = new SOChart();
        soChart.setSize("800px", "500px");

// Tree chart
// (By default it assumes circular shape. Otherwise, we can set orientation)
// All values are randomly generated
        TreeChart tc = new TreeChart();
        TreeData td = new TreeData("Root", 1000);
        tc.setTreeData(td);
        Random r = new Random();
        for(int i = 1; i < 21; i++) {
            td.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        TreeData td1 = td.get(13);
        td = td.get(9);
        for(int i = 50; i < 56; i++) {
            td.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        for(int i = 30; i < 34; i++) {
            td1.add(new TreeData("Node " + i, r.nextInt(500)));
        }

// Add to the chart display area with a simple title
        soChart.add(tc, new Title("A Circular Tree Chart"));

// Finally, add it to my layout













        ApexCharts apexCharts = new ConceptionEntityCountChart()
                .withColors("#168eea", "#ee4f4f", "#03a9f4", "#76b852", "#323b43", "#59626a", "#0288d1", "#ffc107", "#d32f2f", "#00d1b2","#ced7df").build();
        //https://www.materialpalette.com/
        //https://materialui.co/colors/
        //http://brandcolors.net/
        apexCharts.setWidth("330");
        rightComponentContainer.add(soChart);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,apexCharts);
    }
}
