window.Vaadin.Flow.feature_RelationKindCorrelationInfoChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(dataObj) {
                c.$connector.option.series.data = dataObj.data;
                c.$connector.option.series.links = dataObj.links;
                if (c.$connector.option && typeof c.$connector.option === "object") {
                    c.$connector.myChart.setOption(c.$connector.option, true);
                }
            },
            clearData : function() {
                c.$connector.option.series.data = [];
                c.$connector.option.series.links = [];
                if (c.$connector.option && typeof c.$connector.option === "object") {
                    c.$connector.myChart.setOption(c.$connector.option, true);
                }
            }
        };

        c.$connector.myChart = echarts.init(c);
        c.$connector.option = {
            series: {
                type: 'sankey',
                layout: 'none',
                emphasis: {
                    focus: 'adjacency'
                },
                data: [
                    /*
                    {
                        name: 'a'
                    },
                    {
                        name: 'b'
                    },
                    {
                        name: 'a1'
                    },
                    {
                        name: 'a2'
                    },
                    {
                        name: 'b1'
                    },
                    {
                        name: 'c'
                    }
                    */
                ],
                links: [
                    /*
                    {
                        source: 'a',
                        target: 'a1',
                        value: 5
                    },
                    {
                        source: 'a',
                        target: 'a2',
                        value: 3
                    },
                    {
                        source: 'b',
                        target: 'b1',
                        value: 8
                    },
                    {
                        source: 'a',
                        target: 'b1',
                        value: 3
                    },
                    {
                        source: 'b1',
                        target: 'a1',
                        value: 1
                    },
                    {
                        source: 'b1',
                        target: 'c',
                        value: 2
                    }
                     */
                ]
            }
        };
    }
}