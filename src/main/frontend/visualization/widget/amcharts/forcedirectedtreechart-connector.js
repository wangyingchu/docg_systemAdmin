window.Vaadin.Flow.amCharts_ForceDirectedTreeChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(data) {
                this.chart.data = data;
            }
        };

        /* Chart code */
        // Themes begin
        am4core.useTheme(am4themes_animated);
        // Themes end

        c.$connector.chart = am4core.create(c, am4plugins_forceDirected.ForceDirectedTree);
        c.$connector.networkSeries = c.$connector.chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries());

        c.$connector.chart.data = [
            {
                name: "defaultRoot",
                value:1,
                children: [
                    {
                        name: "defaultChildren0",
                        value:1
                    }
                    ]
            }
        ];

        c.$connector.networkSeries.dataFields.value = "value";
        c.$connector.networkSeries.dataFields.name = "name";
        c.$connector.networkSeries.dataFields.children = "children";
        c.$connector.networkSeries.nodes.template.tooltipText = "{name}({desc}):{value}";
        c.$connector.networkSeries.nodes.template.label.text = "{name}";
        c.$connector.networkSeries.nodes.template.fillOpacity = 1;
        c.$connector.networkSeries.manyBodyStrength = -20;
        c.$connector.networkSeries.links.template.strength = 0.8;
        c.$connector.networkSeries.minRadius = am4core.percent(2);
        c.$connector.networkSeries.fontSize = 10;
    }
}