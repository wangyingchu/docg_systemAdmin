window.Vaadin.Flow.feature_ConceptionEntityRelationsChart = {
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
                    name: 'concentric'
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
                    'font-size': 1.1,
                    'font-weight': 'bold',
                    'background-color': 'data(background_color)',
                    'content': 'data(desc)',
                    'text-valign': 'center',
                    'color': '#333333',
                    'shape': 'data(shape)',
                    'text-outline-width': 0.2 ,
                    'text-outline-color': '#EEE',
                    'width': 4,
                    'height': 4
                })
                .selector('edge')
                .css({
                    'content': 'data(type)',
                    'width': 0.4,
                    'line-color': '#CCCCCC',
                    'arrow-scale': 0.2,
                    'line-style': 'solid',
                    'curve-style': 'unbundled-bezier',
                    'text-rotation': 'autorotate',
                    'font-size': 1.1,
                    'font-family': 'Georgia',
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
                name: 'concentric'
            }
        });
    }
}