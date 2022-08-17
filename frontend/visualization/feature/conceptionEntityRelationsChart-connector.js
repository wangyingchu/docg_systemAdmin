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
                    name: 'concentric',
                    fit:false,
                    padding: 30, // the padding on fit
                    startAngle:4/ 2 * Math.PI, // where nodes start in radians
                    sweep: undefined, // how many radians should be between the first and last node (defaults to full circle)
                    clockwise: true, // whether the layout should go clockwise (true) or counterclockwise/anticlockwise (false)
                    equidistant: false, // whether levels have an equal radial distance betwen them, may cause bounding box overflow
                    minNodeSpacing: 8 // min spacing between outside of nodes (used for radius adjustment)
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
                   // 'curve-style': 'segments',
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
        cy.on('click', 'node', function(evt){
            var node = evt.target;
            c.$server.addConceptionEntityRelations(node.data().kind,node.id());
        });

        cy.on('dblclick', 'node', function(evt){
            var node = evt.target;
            console.log( 'dblclicked ' + node.id() );
        });
    }
}