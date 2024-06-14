window.Vaadin.Flow.amCharts_SunburstChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(data) {
                c.$connector.chart.data = data;
            }
        };
        /* Chart code */
        am4core.ready(function() {

        // Themes begin
        am4core.useTheme(am4themes_animated);
        // Themes end

        // create chart
        c.$connector.chart = am4core.create(c, am4plugins_sunburst.Sunburst);
            c.$connector.chart.padding(0,0,0,0);
            c.$connector.chart.radius = am4core.percent(98);

            c.$connector.chart.colors.step = 2;
            c.$connector.chart.fontSize = 11;
            c.$connector.chart.innerRadius = am4core.percent(10);

            // define data fields
            c.$connector.chart.dataFields.value = "value";
            c.$connector.chart.dataFields.name = "name";
            c.$connector.chart.dataFields.children = "children";

            let level0SeriesTemplate = new am4plugins_sunburst.SunburstSeries();
            level0SeriesTemplate.hiddenInLegend = false;
            c.$connector.chart.seriesTemplates.setKey("0", level0SeriesTemplate);

            // this makes labels to be hidden if they don't fit
            level0SeriesTemplate.labels.template.truncate = true;
            level0SeriesTemplate.labels.template.hideOversized = true;

            level0SeriesTemplate.labels.template.adapter.add("rotation", function(rotation, target) {
                target.maxWidth = target.dataItem.slice.radius - target.dataItem.slice.innerRadius - 10;
                target.maxHeight = Math.abs(target.dataItem.slice.arc * (target.dataItem.slice.innerRadius + target.dataItem.slice.radius) / 2 * am4core.math.RADIANS);
                return rotation;
            });

            let level1SeriesTemplate = level0SeriesTemplate.clone();
            c.$connector.chart.seriesTemplates.setKey("1", level1SeriesTemplate);
            level1SeriesTemplate.fillOpacity = 0.95;
            level1SeriesTemplate.hiddenInLegend = true;

            let level2SeriesTemplate = level0SeriesTemplate.clone();
            c.$connector.chart.seriesTemplates.setKey("2", level2SeriesTemplate);
            level2SeriesTemplate.fillOpacity = 0.85;
            level2SeriesTemplate.hiddenInLegend = true;

            let level3SeriesTemplate = level0SeriesTemplate.clone();
            c.$connector.chart.seriesTemplates.setKey("3", level3SeriesTemplate);
            level3SeriesTemplate.fillOpacity = 0.70;
            level3SeriesTemplate.hiddenInLegend = true;

            let level4SeriesTemplate = level0SeriesTemplate.clone();
            c.$connector.chart.seriesTemplates.setKey("4", level4SeriesTemplate);
            level4SeriesTemplate.fillOpacity = 0.60;
            level4SeriesTemplate.hiddenInLegend = true;

            let level5SeriesTemplate = level0SeriesTemplate.clone();
            c.$connector.chart.seriesTemplates.setKey("5", level5SeriesTemplate);
            level5SeriesTemplate.fillOpacity = 0.45;
            level5SeriesTemplate.hiddenInLegend = true;

            c.$connector.chart.legend = new am4charts.Legend();
        });
    }
}