window.Vaadin.Flow.feature_ConceptionEntityTemporalSunburstChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {

        };
        am5.ready(function() {
            let root = am5.Root.new(c);
            // Set themes
            // https://www.amcharts.com/docs/v5/concepts/themes/
            root.setThemes([
                am5themes_Animated.new(root)
            ]);
            // Create wrapper container
            let container = root.container.children.push(am5.Container.new(root, {
                width: am5.percent(100),
                height: am5.percent(100),
                layout: root.verticalLayout
            }));

            // Create series
            // https://www.amcharts.com/docs/v5/charts/hierarchy/#Adding
            let series = container.children.push(am5hierarchy.Sunburst.new(root, {
                singleBranchOnly: true,
                downDepth: 10,
                initialDepth: 10,
                topDepth: 1,
                innerRadius:am5.percent(30),
                valueField: "value",
                categoryField: "name",
                childDataField: "children"
            }));
            series.data.setAll([{
                name: "root",
                children: [{
                    name: "First",
                    children: [
                        { name: "A1", value: 100 },
                        { name: "A2", value: 60 }
                    ]
                },
                    {
                        name: "Second",
                        children: [
                            { name: "B1", value: 135 },
                            { name: "B2", value: 98 }
                        ]
                    },
                    {
                        name: "Third",
                        children: [
                            {
                                name: "C1",
                                children: [
                                    { name: "EE1", value: 130 },
                                    { name: "EE2", value: 87 },
                                    { name: "EE3", value: 55 }
                                ]
                            },
                            { name: "C2", value: 148 },
                            {
                                name: "C3", children: [
                                    { name: "CC1", value: 53 },
                                    { name: "CC2", value: 30 }
                                ]
                            },
                            { name: "C4", value: 26 }
                        ]
                    },
                    {
                        name: "Fourth",
                        children: [
                            { name: "D1", value: 415 },
                            { name: "D2", value: 148 },
                            { name: "D3", value: 89 }
                        ]
                    },
                    {
                        name: "Fifth",
                        children: [
                            {
                                name: "E1",
                                children: [
                                    { name: "EE1", value: 33 },
                                    { name: "EE2", value: 40 },
                                    { name: "EE3", value: 89 }
                                ]
                            },
                            {
                                name: "E2",
                                value: 148
                            }
                        ]
                    }]
            }]);
            series.appear(1000, 100);
        });
    }
}