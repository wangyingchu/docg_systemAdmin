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
                            { name: "2021-3", value: 1 }
                        ]
                    },
                        { name: "2022",
                            children: [
                                { name: "2022-3", value: 1 },
                                { name: "2022-7", value: 1 }
                            ]
                        },
                        { name: "2023",
                            children: [
                                { name: "2023-1",
                                    children: [
                                        { name: "2023-1-1", value: 1 },
                                        { name: "2023-1-17", value: 1 },
                                        { name: "2023-1-22", value: 1 }
                                    ]
                                },
                                { name: "2023-5", value: 1 },
                                { name: "2023-7", children: [
                                        { name: "2023-7-1", value: 1 },
                                        { name: "2023-7-23", value: 1 }
                                    ]
                                },
                                { name: "2023-9", value: 1 }
                            ]
                        },
                        { name: "2024",
                            children: [
                                { name: "2024-3", value: 1 },
                                { name: "2024-5", value: 1 },
                                { name: "2024-9", value: 1 }
                            ]
                        },
                        { name: "2025",
                            children: [
                                { name: "2025-7",
                                    children: [
                                        { name: "2025-7-1", value: 1 },
                                        { name: "2025-7-19", value: 1 },
                                        { name: "2025-7-26", value: 1 }
                                    ]
                                },
                                { name: "2025-9",value: 1 }
                            ]
                        }]
                }];

                console.log(data);
                console.log([sunburstEntitiesObject]);
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