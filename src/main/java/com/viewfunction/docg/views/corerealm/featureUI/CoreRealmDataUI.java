package com.viewfunction.docg.views.corerealm.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.config.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.Zoom;
import com.github.appreciated.apexcharts.config.grid.Row;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.helper.Series;
import com.github.appreciated.card.Card;
import com.github.appreciated.card.ClickableCard;
import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.card.action.ActionButton;
import com.github.appreciated.card.action.Actions;
import com.github.appreciated.card.content.IconItem;
import com.github.appreciated.card.content.Item;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;

import com.storedobject.chart.SOChart;
import com.storedobject.chart.Title;
import com.storedobject.chart.TreeChart;
import com.storedobject.chart.TreeData;
import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.theme.lumo.Lumo;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.data.Dataset;
import org.vaadin.addons.chartjs.data.LineDataset;
import org.vaadin.addons.chartjs.options.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CoreRealmDataUI extends VerticalLayout {

    public CoreRealmDataUI(){

        Button refreshDataButton = new Button("刷新领域数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        //Button plusButton3 = new Button("-",new Icon(VaadinIcon.PLUS));
        //plusButton3.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Label coreRealmNameLabel = new Label(" [ Default CoreRealm ]");
        coreRealmNameLabel.getStyle().set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-secondary-text-color)");
        secTitleElementsList.add(coreRealmNameLabel);

        Label coreRealmTechLabel = new Label(" NEO4J 实现");
        //coreRealmTechLabel.getStyle()
        //        .set("font-size","var(--lumo-font-size-xxs)");
        coreRealmTechLabel.addClassName("text-2xs");
        secTitleElementsList.add(coreRealmTechLabel);
        coreRealmTechLabel.getElement().getThemeList().add("badge success");


        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Core Realm 领域模型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        VerticalLayout leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setWidth(400, Unit.PIXELS);
        leftSideContentContainerLayout.setHeight(600,Unit.PIXELS);
        leftSideContentContainerLayout.addClassNames("border-r","border-contrast-20");
        contentContainerLayout.add(leftSideContentContainerLayout);

        HorizontalLayout coreRealmInfoContainerLayout = new HorizontalLayout();
        coreRealmInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);

        //Icon icon = new Icon("lumo", "photo");
        Icon icon = new Icon(VaadinIcon.AUTOMATION);

        //Icon icon2 =
                //FontAwesome.Solid.ADDRESS_CARD.create();
        //leftSideContentContainerLayout.add(FontAwesome.Solid.ADDRESS_CARD.create());

        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据概览信息",null);
        leftSideContentContainerLayout.add(sectionActionBar);



        HorizontalLayout conceptionKindInfoContainerLayout = new HorizontalLayout();
        conceptionKindInfoContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon icon001 = new Icon(VaadinIcon.CUBE);
        icon001.setSize("20px");
        conceptionKindInfoContainerLayout.add(icon001);

        Label lb01 = new Label("ConceptionKind-概念类型");
        lb01.addClassNames("text-xs","font-semibold");
        conceptionKindInfoContainerLayout.add(lb01);








        BarChartConfig config = new BarChartConfig();
        config
                .data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)"))
                .and();

        config.
                options()
                .responsive(true)
                .title()
                .display(true)
                .position(Position.LEFT)
                .text("Chart.js Combo Bar Line Chart")
                .and()
                .done();

        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
            }

            if (ds instanceof BarDataset) {
                BarDataset bds = (BarDataset) ds;
                bds.dataAsList(data);
            }

            if (ds instanceof LineDataset) {
                LineDataset lds = (LineDataset) ds;
                lds.dataAsList(data);
            }
        }

        ChartJs chart1 = new ChartJs(config);
        chart1.setSizeFull();


        //chart.setHeight(200,Unit.PIXELS);
        //chart.setWidth(400,Unit.PIXELS);



        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setWidth(350,Unit.PIXELS);
        verticalLayout1.setHeight(400,Unit.PIXELS);
        //verticalLayout1.add(chart1);











        // Our Apex chart
        ApexCharts apexChart = new ApexCharts();

        // Series
        Series<Integer> series = new Series<Integer>();
        series.setData(new Integer[] {10, 41, 35, 51, 49, 62, 69, 91, 148});
        series.setName("Desktops");

        // Chart
        Chart chart = new Chart();
        chart.setHeight("350");
        chart.setType(Type.line);
        Zoom zoom = new Zoom();
        zoom.setEnabled(false);
        chart.setZoom(zoom);

        // Labels
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);

        // Stroke
        Stroke stroke = new Stroke();
        stroke.setCurve(Curve.straight);

        // Title
        TitleSubtitle titleSubtilte = new TitleSubtitle();
        titleSubtilte.setText("Product Trends by Month");
        titleSubtilte.setAlign(Align.left);

        // Grid
        Grid grid = new Grid();
        Row row = new Row();
        row.setColors(Arrays.asList(new String[] {"#f3f3f3", "transparent"}));
        row.setOpacity(0.5);
        grid.setRow(row);

        // Xaxis
        XAxis xaxis = new XAxis();
        xaxis.setCategories(Arrays.asList(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep"}));

        // Tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.setEnabled(false);

        // Include them all
        apexChart.setSeries(series);
        apexChart.setChart(chart);
        apexChart.setDataLabels(dataLabels);
        apexChart.setStroke(stroke);
        apexChart.setTitle(titleSubtilte);
        apexChart.setGrid(grid);
        apexChart.setXaxis(xaxis);
        apexChart.setTooltip(tooltip);

        // Render them and include into the content
        //leftSideContentContainerLayout.add(apexChart); // AppLayout




        verticalLayout1.add(apexChart);








        leftSideContentContainerLayout.add(verticalLayout1);



        Details component = new Details(conceptionKindInfoContainerLayout,
                verticalLayout1);
        //component.setEnabled(false);

        //component.addOpenedChangeListener(e ->
        //        Notification.show(e.isOpened() ? "Opened" : "Closed"));
        component.getStyle().set("width","100%");
        component.setOpened(true);
        component.addThemeVariants(DetailsVariant.FILLED);
        //component.addClassNames("shadow-xs");

        leftSideContentContainerLayout.add(component);

        Details component2 = new Details("[Conception Kind] 概念类型 ",
                new Text("Toggle using mouse, Enter and Space keys."));
        //component.addOpenedChangeListener(e ->
        //        Notification.show(e.isOpened() ? "Opened" : "Closed"));
        component2.getStyle().set("width","100%");
        component2.setOpened(true);
        component2.addThemeVariants(DetailsVariant.FILLED);
        component2.addClassNames("shadow-xs");
        leftSideContentContainerLayout.add(component2);



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
        leftSideContentContainerLayout.add(soChart);




        RippleClickableCard rcard = new RippleClickableCard(
                onClick -> {/* Handle Card click */},
                new TitleLabel("Example Title") // Follow up with more Components ...
        );

        ClickableCard ccard = new ClickableCard(
                onClick -> {/* Handle Card click */},
                new TitleLabel("Example Title") // Follow up with more Components ...
        );




        leftSideContentContainerLayout.add(rcard);
        leftSideContentContainerLayout.add(ccard);






    }
}
