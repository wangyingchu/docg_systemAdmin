window.Vaadin.Flow.feature_DataRelationDistributionChart = {
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
                let option = {name: 'cose',
                    // Called on `layoutready`
                    ready: function(){},
                    // Called on `layoutstop`
                    stop: function(){},
                    // Whether to animate while running the layout
                    // true : Animate continuously as the layout is running
                    // false : Just show the end result
                    // 'end' : Animate with the end result, from the initial positions to the end positions
                    animate: true,
                    // Easing of the animation for animate:'end'
                    animationEasing: undefined,
                    // The duration of the animation for animate:'end'
                    animationDuration: undefined,
                    // A function that determines whether the node should be animated
                    // All nodes animated by default on animate enabled
                    // Non-animated nodes are positioned immediately when the layout starts
                    animateFilter: function ( node, i ){ return true; },
                    // The layout animates only after this many milliseconds for animate:true
                    // (prevents flashing on fast runs)
                    animationThreshold: 250,
                    // Number of iterations between consecutive screen positions update
                    refresh: 20,
                    // Whether to fit the network view after when done
                    fit: true,
                    // Padding on fit
                    padding: 5,
                    // Constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
                    boundingBox: undefined,
                    // Excludes the label when calculating node bounding boxes for the layout algorithm
                    nodeDimensionsIncludeLabels: false,
                    // Randomize the initial positions of the nodes (true) or use existing positions (false)
                    randomize: false,
                    // Extra spacing between components in non-compound graphs
                    componentSpacing: 40,
                    // Node repulsion (non overlapping) multiplier
                    nodeRepulsion: function( node ){ return 2048; },
                    // Node repulsion (overlapping) multiplier
                    nodeOverlap: 4,
                    // Ideal edge (non nested) length
                    idealEdgeLength: function( edge ){ return 32; },
                    // Divisor to compute edge forces
                    edgeElasticity: function( edge ){ return 32; },
                    // Nesting factor (multiplier) to compute ideal edge length for nested edges
                    nestingFactor: 1.2,
                    // Gravity force (constant)
                    gravity: 1,
                    // Maximum number of iterations to perform
                    numIter: 1000,
                    // Initial temperature (maximum node displacement)
                    initialTemp: 1000,
                    // Cooling factor (how the temperature is reduced between consecutive iterations
                    coolingFactor: 0.99,
                    // Lower temperature threshold (below this point the layout will end)
                    minTemp: 1.0
                };

                let layout = cy.layout(option);
                layout.on("layoutready", () => {})
                layout.run();
                cy.fit();
                cy.center();
                cy.minZoom(0.5);
                cy.maxZoom(30)
            }
        };

        let cy = cytoscape({
            container: c,
            style: cytoscape.stylesheet()
                .selector('parent')
                .css({
                    'background-opacity': 0.1
                })
                .selector('node')
                .css({
                    'font-size': 1.1,
                    'font-weight': 'bold',
                    'background-color': 'data(background_color)',
                    'content': 'data(desc)',
                    'text-valign': 'center',
                    'color': '#333333',
                    'shape': 'data(shape)',
                    'text-outline-width': 0.15 ,
                    'text-outline-color': '#EEE',
                    'width': 'data(size)',
                    'height': 'data(size)',
                    'background-opacity': 1
                })
                .selector('edge')
                .css({
                    'content': 'data(type)',
                    'width': 'data(lineWidth)',
                    'line-color': 'data(lineColor)',
                    'source-arrow-color':'data(sourceArrowColor)',
                    'target-arrow-color':'data(targetArrowColor)',
                    'arrow-scale': 0.1,
                    'line-style': 'data(lineStyle)',
                    'curve-style': 'data(curveStyle)',
                    //'curve-style': 'unbundled-bezier',
                    //'curve-style': 'segments',
                    //'curve-style': 'unbundled-bezier(multiple)',
                    //'curve-style': 'taxi',
                    //'curve-style': 'straight',
                    //'curve-style': 'haystack',
                    //'curve-style': 'loop',
                    'text-rotation': 'autorotate',
                    'font-size': 0.8,
                    'font-family': 'Georgia',
                    //'font-weight': 'bold',
                    'color': '#666666',
                    'target-arrow-shape': 'vee',
                    'source-arrow-shape': 'tee',
                    'line-opacity': 'data(lineOpacity)'
                })
                .selector('node:selected')
                .style({
                    'border-color': '#1E90CC',
                    'border-width': "1px",
                    'width':6,'height':6})
                .selector('edge:selected')
                .style({
                    'line-color': '#1E90CC',
                    'width': 0.4,
                    'source-arrow-color':'#1E90CC',
                    'target-arrow-color':'#1E90CC',
                    'color': '#333333'})
            ,
            elements: {
                nodes: [],
                edges: []
            },
            layout: {
                name: 'cose'
            }
        });
    }
}