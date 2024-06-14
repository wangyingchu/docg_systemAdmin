window.Vaadin.Flow.amCharts_XYChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            //// functions
            setData : function(data) {
                this.chart.data = data;
            }
        };
        // Themes begin
        am4core.useTheme(am4themes_animated);
        // Themes end

        c.$connector.chart = am4core.create(c, am4charts.XYChart);
        c.$connector.chart.paddingRight = 20;

        let dateAxis = c.$connector.chart.xAxes.push(new am4charts.DateAxis());
        dateAxis.renderer.grid.template.location = 0;

        let valueAxis = c.$connector.chart.yAxes.push(new am4charts.ValueAxis());
        valueAxis.tooltip.disabled = true;
        valueAxis.renderer.minWidth = 35;

        let series = c.$connector.chart.series.push(new am4charts.LineSeries());
        series.dataFields.dateX = "date";
        series.dataFields.valueY = "value";

        series.tooltipText = "{valueY.value}";
        c.$connector.chart.cursor = new am4charts.XYCursor();

        let scrollbarX = new am4charts.XYChartScrollbar();
        scrollbarX.series.push(series);
        c.$connector.chart.scrollbarX = scrollbarX;
    }
}