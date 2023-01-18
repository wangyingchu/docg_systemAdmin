window.Vaadin.Flow.feature_ConceptionEntityTemporalSunburstChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            renderSunburstEntities: function(sunburstEntitiesStr) {
                const sunburstEntitiesObject = eval("(" + sunburstEntitiesStr + ")")
                let data =[{
                    name: "root",
                    children: [{
                        name: "2021",
                        children: [
                            { name: "2021-1", value: 1 },
                            { name: "A2", value: 1 }
                        ]
                    },
                        { name: "Second",
                            children: [
                                { name: "B1", value: 1 },
                                { name: "B2", value: 1 }
                            ]
                        },
                        { name: "Third",
                            children: [
                                { name: "C1",
                                    children: [
                                        { name: "EE1", value: 1 },
                                        { name: "EE2", value: 1 },
                                        { name: "EE3", value: 1 }
                                    ]
                                },
                                { name: "C2", value: 1 },
                                { name: "C3", children: [
                                        { name: "CC1", value: 1 },
                                        { name: "CC2", value: 1 }
                                    ]
                                },
                                { name: "C4", value: 1 }
                            ]
                        },
                        { name: "Fourth",
                            children: [
                                { name: "D1", value: 1 },
                                { name: "D2", value: 1 },
                                { name: "D3", value: 1 }
                            ]
                        },
                        { name: "Fifth",
                            children: [
                                { name: "E1",
                                    children: [
                                        { name: "EE1", value: 1 },
                                        { name: "EE2", value: 1 },
                                        { name: "EE3", value: 1 }
                                    ]
                                },
                                { name: "E2",value: 1 }
                            ]
                        }]
                }];

                c.$connector.sunburstChart.data.setAll(data);
                //c.$connector.sunburstChart.data.setAll([sunburstEntitiesObject]);

                let legend = c.$connector.container.children.push(am5.Legend.new(c.$connector.root, {
                    centerX: am5.percent(50),
                    x: am5.percent(50),
                    layout: c.$connector.root.gridLayout
                }));
                legend.data.setAll(c.$connector.sunburstChart.dataItems[0].get("children"));
            }
        };
        am5.ready(function() {
            c.$connector.root = am5.Root.new(c);
            // Set themes
            // https://www.amcharts.com/docs/v5/concepts/themes/
            c.$connector.root.setThemes([
                am5themes_Animated.new(c.$connector.root)
            ]);
            // Create wrapper container
            c.$connector.container = c.$connector.root.container.children.push(am5.Container.new(c.$connector.root, {
                width: am5.percent(100),
                height: am5.percent(100),
                layout: c.$connector.root.verticalLayout
            }));
            // Create series
            // https://www.amcharts.com/docs/v5/charts/hierarchy/#Adding
            c.$connector.sunburstChart = c.$connector.container.children.push(am5hierarchy.Sunburst.new(c.$connector.root, {
                singleBranchOnly: true,
                downDepth: 10,
                initialDepth: 10,
                topDepth: 1,
                innerRadius:am5.percent(30),
                valueField: "value",
                categoryField: "name",
                childDataField: "children"
            }));
            c.$connector.sunburstChart.appear(1000, 100);
        });
    }
}