window.Vaadin.Flow.feature_AttributesViewKindCorrelationInfoChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(data) {
                cy.add(eval("(" + data + ")"));
            },
            clearData : function() {
                cy.remove(cy.elements());
            },
            layoutGraph: function(){
                let layout = cy.layout({
                    name: 'circle'
                });
                layout.run();
                cy.fit();
                cy.center();
            }
        };
        let cy = cytoscape({
            container: c,
            style: cytoscape.stylesheet()
                .selector('node')
                .css({
                    'font-size': 10,
                    'font-weight': 'bold',
                    'font-family': 'Montserrat',
                    'background-color': 'data(background_color)',
                    'content': 'data(desc)',
                    'text-valign': 'center',
                    "text-wrap": "wrap",
                    'color': '#333333',
                    'shape': 'data(shape)',
                    'text-outline-width': 2 ,
                    'text-outline-color': '#EEE',
                    'width': 40,
                    'height': 40
                })
                .selector('edge')
                .css({
                    'content': 'data(type)',
                    'width': 3,
                    'line-color': '#CCCCCC',
                    'arrow-scale': 2,
                    'line-style': 'solid',
                    'curve-style': 'unbundled-bezier',
                    'text-rotation': 'autorotate',
                    'font-size': 8,
                    'font-family': 'Montserrat',
                    'font-weight': 'bold',
                    'color': '#555555',
                    'target-arrow-shape': 'vee',
                    'source-arrow-shape': 'tee'
                }),
            elements: {
                nodes: [],
                edges: []
            },
            layout: {
                name: 'circle'
            }
        });
    }
}