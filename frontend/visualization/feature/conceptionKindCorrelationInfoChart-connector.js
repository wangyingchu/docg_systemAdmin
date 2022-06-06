window.Vaadin.Flow.feature_ConceptionKindCorrelationInfoChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(data) {
                console.log(cy);
                cy.add(data);
                console.log(cy.nodes());
            },
            clearData : function() {
                console.log(cy);
                cy.removeData();
            },
            layout: function(layoutType){
                cy.loadLayout(layoutType);
            }
        };

        let cy = cytoscape({
            container: c,
            style: cytoscape.stylesheet()
                .selector('node')
                .css({
                    'font-size': 15,
                    'font-weight': 'bold',
                    'background_color': 'data(background_color)',
                    'content': 'data(name)',
                    'text-valign': 'center',
                    'color': '#444444',
                    'shape': 'data(shape)',
                    'text-outline-width': 2 ,
                    'text-outline-color': '#888',
                    'width': 40,
                    'height': 40
                })
                .selector('edge')
                .css({
                    'content': 'data(name)',
                    'width': 3,
                    'line-color': '#CCCCCC',
                    'arrow-scale': 2,
                    'line-style': 'solid',
                    'curve-style': 'unbundled-bezier',
                    'text-rotation': 'autorotate',
                    'font-size': 11,
                    'font-family': 'Georgia',
                    'font-weight': 'bold',
                    'color': '#555555',
                    'target-arrow-shape': 'vee',
                    'source-arrow-shape': 'tee'
                }),

            elements: {
                nodes: [

                    /*
                    {
                        data: { id: 'core', name: 'Core'},
                        position: { x: 0, y: 0 }
                    },

                    {
                        data: { id: 'eles', name: 'Collection'},
                        position: { x: 150, y: 150 }
                    },

                    {
                        data: { id: 'style', name: 'Stylesheet'},
                        position: { x: 0, y: 150 }
                    },

                    {
                        data: { id: 'selector', name: 'Selector'},
                        position: { x: -150, y: 150 }
                    },

                    {
                            data: { id: 'ext', name: 'Extensions'}
                        },

                        {
                            data: { id: 'corefn', name: 'Core Function'},
                            classes: 'ext',
                            position: { x: 350, y: -140 }
                        },

                        {
                            data: { id: 'elesfn', name: 'Collection Function'},
                            classes: 'ext',
                            position: { x: 350, y: 0 }
                        },

                        {
                            data: { id: 'layout', name: 'Layout'},
                            classes: 'ext',
                            position: { x: 350, y: 140 }
                        },

                        {
                            data: { id: 'renderer', name: 'Renderer'},
                            classes: 'ext',
                            position: { x: 350, y: 280 }
                        },

                        {
                            data: { id: 'api', name: 'Core API'}
                        },

                        {
                            data: { id: 'app', name: 'Client' },
                            position: { x: 0, y: 480 }
                        }
                        */
                    ],
                edges: [
                        /*
                        { data: { source: 'core', target: 'eles' } },
                        { data: { source: 'core', target: 'ext' } },
                        { data: { source: 'core', target: 'style' } },
                        { data: { source: 'style', target: 'selector' } },
                        { data: { source: 'core', target: 'selector' } },
                        { data: { source: 'elesfn', target: 'eles' }},
                        { data: { source: 'corefn', target: 'core' }},
                        { data: { source: 'layout', target: 'api' }},
                        { data: { source: 'renderer', target: 'api' }},
                        { data: { source: 'app', target: 'api', name: 'use' }},
                        { data: { source: 'app', target: 'ext', name: 'register' }}
                        */
                    ]
                },
            layout: {
                name: 'breadthfirst'
            }
        });
    }
}