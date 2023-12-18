window.Vaadin.Flow.common_BulletChart_amcharts = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
        };

        let bulletType="Memory Consume";
        let currentPercentValue = 15;
        let waterMarkPercentValue = 37.5;

        let colors = [
            am5.color(0x0074d9),
            am5.color(0x7fdbff),
            am5.color(0xff851b),
            am5.color(0xff4136)
        ];

        // Create root element
        // https://www.amcharts.com/docs/v5/getting-started/#Root_element
        let root = am5.Root.new(c);

        // Set themes
        // https://www.amcharts.com/docs/v5/concepts/themes/
        root.setThemes([
            am5themes_Animated.new(root)
        ]);

        // Create chart
        // https://www.amcharts.com/docs/v5/charts/xy-chart/
        let chart = root.container.children.push(
            am5xy.XYChart.new(root, {
                panX: false,
                panY: false,
                wheelX: "none",
                wheelY: "none",
                arrangeTooltips: false
            })
        );

        // Create axes
        // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
        let yAxis = chart.yAxes.push(
            am5xy.CategoryAxis.new(root, {
                categoryField: "category",
                renderer: am5xy.AxisRendererY.new(root, {})
            })
        );

        yAxis.data.setAll([{ category: bulletType }]);

        let xRenderer = am5xy.AxisRendererX.new(root, {});
        xRenderer.grid.template.set("forceHidden", true);

        let xAxis = chart.xAxes.push(
            am5xy.ValueAxis.new(root, {
                renderer: xRenderer,
                min: 0,
                max: 100,
                numberFormat: "#.'%'"
            })
        );

        let rangeDataItem = xAxis.makeDataItem({ value: 0, endValue: 100 });
        let range = xAxis.createAxisRange(rangeDataItem);

        rangeDataItem.get("axisFill").setAll({
            visible: true,
            fillOpacity:1
        });

        let stops = [];
        for (var i = 0; i < colors.length; i++) {
            stops.push({ color: colors[i] });
        }

        let linearGradient = am5.LinearGradient.new(root, {
            rotation: 0,
            stops: stops
        });

        rangeDataItem.get("axisFill").set("fillGradient", linearGradient);

        /*
        let count = colors.length;
        for (var i = 0; i < count; i++) {
            let rangeDataItem = xAxis.makeDataItem({
                value: (i / count) * 100,
                endValue: ((i + 1) / count) * 100
            });
            let range = xAxis.createAxisRange(rangeDataItem);

            rangeDataItem.get("axisFill").setAll({
                visible: true,
                fill: colors[i],
                stroke: colors[i],
                fillOpacity:1
            });
        }
        */

        let series = chart.series.push(
            am5xy.ColumnSeries.new(root, {
                xAxis: xAxis,
                yAxis: yAxis,
                valueXField: "value",
                categoryYField: "category",
                fill: am5.color(0x222222),
                stroke: am5.color(0x222222),
                tooltip: am5.Tooltip.new(root, {
                    pointerOrientation: "left",
                    labelText: "{valueX}%"
                })
            })
        );

        series.columns.template.setAll({
            height: am5.p50
        });

        series.data.setAll([{ category: bulletType, value: currentPercentValue }]);

        let stepSeries = chart.series.push(
            am5xy.StepLineSeries.new(root, {
                xAxis: xAxis,
                yAxis: yAxis,
                valueXField: "value",
                categoryYField: "category",
                stroke: am5.color(0x666666),
                fill: am5.color(0x666666),
                noRisers: true,
                stepWidth: am5.p50,
                tooltip: am5.Tooltip.new(root, {
                    pointerOrientation: "left",
                    labelText: "{valueX}%"
                })
            })
        );

        stepSeries.strokes.template.set("strokeWidth", 3);
        stepSeries.data.setAll([{ category: bulletType, value: waterMarkPercentValue }]);

        // Add cursor
        // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
        let cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
                behavior: "none"
            })
        );
        cursor.lineY.set("visible", false);
        cursor.lineX.set("visible", false);

        // Make stuff animate on load
        // https://www.amcharts.com/docs/v5/concepts/animations/
        chart.appear(1000, 100);

        series.appear();
        stepSeries.appear();
    }
}