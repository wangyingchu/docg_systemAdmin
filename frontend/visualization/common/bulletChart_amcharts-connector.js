window.Vaadin.Flow.common_BulletChart_amcharts = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData: function (chartLabel, currentPercentValue, currentWaterMarkPercentValue) {
                c.$connector.bulletType = chartLabel;
                c.$connector.currentPercentValue = currentPercentValue;
                c.$connector.waterMarkPercentValue = currentWaterMarkPercentValue;
                c.$connector.series.data.setAll([{
                    category: c.$connector.bulletType,
                    value: c.$connector.currentPercentValue
                }]);
                c.$connector.stepSeries.data.setAll([{
                    category: c.$connector.bulletType,
                    value: c.$connector.waterMarkPercentValue
                }]);


                alert(c.$connector.bulletType)
                alert(c.$connector.currentPercentValue)
                alert(c.$connector.waterMarkPercentValue)

            }
        };

        am5.ready(function () {


            c.$connector.bulletType = "-";
            c.$connector.currentPercentValue = 0;
            c.$connector.waterMarkPercentValue = 0;

            let colors = [
                am5.color(0x0074d9),
                am5.color(0x7fdbff),
                am5.color(0xff851b),
                am5.color(0xff4136)
            ];

            // Create root element
            // https://www.amcharts.com/docs/v5/getting-started/#Root_element
            c.$connector.root = am5.Root.new(c);

            // Set themes
            // https://www.amcharts.com/docs/v5/concepts/themes/
            c.$connector.root.setThemes([
                am5themes_Animated.new(c.$connector.root)
            ]);

            // Create chart
            // https://www.amcharts.com/docs/v5/charts/xy-chart/
            c.$connector.chart = c.$connector.root.container.children.push(
                am5xy.XYChart.new(c.$connector.root, {
                    panX: false,
                    panY: false,
                    wheelX: "none",
                    wheelY: "none",
                    arrangeTooltips: false
                })
            );

            // Create axes
            // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
            let yAxis = c.$connector.chart.yAxes.push(
                am5xy.CategoryAxis.new(c.$connector.root, {
                    categoryField: "category",
                    renderer: am5xy.AxisRendererY.new(c.$connector.root, {})
                })
            );

            yAxis.data.setAll([{category: c.$connector.bulletType}]);

            let xRenderer = am5xy.AxisRendererX.new(c.$connector.root, {});
            xRenderer.grid.template.set("forceHidden", true);

            let xAxis = c.$connector.chart.xAxes.push(
                am5xy.ValueAxis.new(c.$connector.root, {
                    renderer: xRenderer,
                    min: 0,
                    max: 100,
                    numberFormat: "#.'%'"
                })
            );

            let rangeDataItem = xAxis.makeDataItem({value: 0, endValue: 100});
            let range = xAxis.createAxisRange(rangeDataItem);

            rangeDataItem.get("axisFill").setAll({
                visible: true,
                fillOpacity: 1
            });

            let stops = [];
            for (var i = 0; i < colors.length; i++) {
                stops.push({color: colors[i]});
            }

            let linearGradient = am5.LinearGradient.new(c.$connector.root, {
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

            c.$connector.series = c.$connector.chart.series.push(
                am5xy.ColumnSeries.new(c.$connector.root, {
                    xAxis: xAxis,
                    yAxis: yAxis,
                    valueXField: "value",
                    categoryYField: "category",
                    fill: am5.color(0x222222),
                    stroke: am5.color(0x222222),
                    tooltip: am5.Tooltip.new(c.$connector.root, {
                        pointerOrientation: "left",
                        labelText: "{valueX}%"
                    })
                })
            );

            c.$connector.series.columns.template.setAll({
                height: am5.p50
            });

            c.$connector.series.data.setAll([{
                category: c.$connector.bulletType,
                value: c.$connector.currentPercentValue
            }]);

            c.$connector.stepSeries = c.$connector.chart.series.push(
                am5xy.StepLineSeries.new(c.$connector.root, {
                    xAxis: xAxis,
                    yAxis: yAxis,
                    valueXField: "value",
                    categoryYField: "category",
                    stroke: am5.color(0x666666),
                    fill: am5.color(0x666666),
                    noRisers: true,
                    stepWidth: am5.p50,
                    tooltip: am5.Tooltip.new(c.$connector.root, {
                        pointerOrientation: "left",
                        labelText: "{valueX}%"
                    })
                })
            );

            c.$connector.stepSeries.strokes.template.set("strokeWidth", 3);
            c.$connector.stepSeries.data.setAll([{
                category: c.$connector.bulletType,
                value: c.$connector.waterMarkPercentValue
            }]);

            // Add cursor
            // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
            let cursor = c.$connector.chart.set("cursor", am5xy.XYCursor.new(c.$connector.root, {
                    behavior: "none"
                })
            );
            cursor.lineY.set("visible", false);
            cursor.lineX.set("visible", false);

            // Make stuff animate on load
            // https://www.amcharts.com/docs/v5/concepts/animations/
            c.$connector.chart.appear(1000, 100);

            c.$connector.series.appear();
            c.$connector.stepSeries.appear();


        });


    }


}