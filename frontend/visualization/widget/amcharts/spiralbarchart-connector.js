window.Vaadin.Flow.amCharts_SpiralBarChartConnector = {
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

        /* Chart code */
        // Themes begin
        //am4core.useTheme(am4themes_kelly);
        am4core.useTheme(am4themes_animated);
        // Themes end

        c.$connector.chart = am4core.create(c, am4plugins_timeline.SpiralChart);
        c.$connector.chart.levelCount = 3;
        c.$connector.chart.inversed = true;
        c.$connector.chart.endAngle = -135;
        c.$connector.chart.yAxisInnerRadius = 0;
        c.$connector.chart.yAxisRadius = am4core.percent(70);
        c.$connector.chart.padding(0,0,0,0)

        c.$connector.chart.data = [
            { category: "" },
            { category: "Chandler", value: 47 },
            { category: "Joey", value: 45 },
            { category: "Monica", value: 204 },
            { category: "Phoebe", value: 62 },
            { category: "Rachel", value: 154 },
            { category: "Ross", value: 120 }];

        c.$connector.chart.fontSize = 11;

        let categoryAxis = c.$connector.chart.yAxes.push(new am4charts.CategoryAxis());
        categoryAxis.dataFields.category = "category";
        categoryAxis.renderer.grid.template.disabled = true;
        categoryAxis.renderer.minGridDistance = 6;
        categoryAxis.cursorTooltipEnabled = false;

        let categoryAxisLabelTemplate = categoryAxis.renderer.labels.template;
        categoryAxisLabelTemplate.paddingLeft = 20;
        categoryAxisLabelTemplate.horizontalCenter = "left";
        categoryAxisLabelTemplate.adapter.add("rotation", function (rotation, target) {
            let position = valueAxis.valueToPosition(valueAxis.min);
            return valueAxis.renderer.positionToAngle(position) + 90;
        })

        let valueAxis = c.$connector.chart.xAxes.push(new am4charts.ValueAxis());
        valueAxis.renderer.minGridDistance = 70;

        valueAxis.renderer.line.strokeDasharray = "1,4";
        valueAxis.renderer.line.strokeOpacity = 0.7;
        valueAxis.renderer.grid.template.disabled = true;
        valueAxis.zIndex = 100;
        valueAxis.cursorTooltipEnabled = false;
        valueAxis.min = 0;

        let labelTemplate = valueAxis.renderer.labels.template;
        labelTemplate.verticalCenter = "middle";
        labelTemplate.fillOpacity = 0.7;

        let series = c.$connector.chart.series.push(new am4plugins_timeline.CurveColumnSeries());
        series.dataFields.valueX = "value";
        series.dataFields.categoryY = "category";

        let columnTemplate = series.columns.template;
        series.tooltipText = "{categoryY}: {valueX} kisses";
        columnTemplate.adapter.add("fill", function (fill, target) {
            return c.$connector.chart.colors.getIndex(target.dataItem.index * 2);
        })
        columnTemplate.strokeOpacity = 0;
        columnTemplate.fillOpacity = 0.8;

        let hoverState = columnTemplate.states.create("hover")
        hoverState.properties.fillOpacity = 1;

        c.$connector.chart.scrollbarX = new am4core.Scrollbar();
        c.$connector.chart.scrollbarX.align = "center"
        c.$connector.chart.scrollbarX.width = am4core.percent(70);
        c.$connector.chart.isMeasured = false;

        let cursor = new am4plugins_timeline.CurveCursor();
        c.$connector.chart.cursor = cursor;
        cursor.xAxis = valueAxis;
        cursor.yAxis = categoryAxis;
        cursor.lineY.disabled = true;
        cursor.lineX.strokeDasharray = "1,4";
        cursor.lineX.strokeOpacity = 1;

        let label = c.$connector.chart.plotContainer.createChild(am4core.Label);
        label.text = "Biggest kissers in Friends"
        label.fontSize = 15;
        label.x = am4core.percent(80);
        label.y = am4core.percent(80);
        label.horizontalCenter = "right";
    }
}