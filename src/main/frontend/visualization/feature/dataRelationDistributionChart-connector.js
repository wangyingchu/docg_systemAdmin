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
                let option = {
                    name: 'concentric',
                    fit: true, // whether to fit the viewport to the graph
                    padding: 30, // the padding on fit
                    startAngle: 3 / 2 * Math.PI, // where nodes start in radians
                    sweep: undefined, // how many radians should be between the first and last node (defaults to full circle)
                    clockwise: true, // whether the layout should go clockwise (true) or counterclockwise/anticlockwise (false)
                    equidistant: false, // whether levels have an equal radial distance betwen them, may cause bounding box overflow
                    minNodeSpacing: 10, // min spacing between outside of nodes (used for radius adjustment)
                    boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
                    avoidOverlap: true, // prevents node overlap, may overflow boundingBox if not enough space
                    nodeDimensionsIncludeLabels: false, // Excludes the label when calculating node bounding boxes for the layout algorithm
                    height: undefined, // height of layout area (overrides container height)
                    width: undefined, // width of layout area (overrides container width)
                    spacingFactor: undefined, // Applies a multiplicative factor (>0) to expand or compress the overall area that the nodes take up
                    concentric: function( node ){ // returns numeric value for each node, placing higher nodes in levels towards the centre
                        return node.degree();
                    },
                    levelWidth: function( nodes ){ // the variation of concentric values in each level
                        return nodes.maxDegree() / 4;
                    },
                    animate: true, // whether to transition the node positions
                    animationDuration: 800, // duration of animation in ms if enabled
                    animationEasing: undefined, // easing of animation if enabled
                    animateFilter: function ( node, i ){ return true; }, // a function that determines whether the node should be animated.  All nodes animated by default on animate enabled.  Non-animated nodes are positioned immediately when the layout starts
                    ready: undefined, // callback on layoutready
                    stop: undefined, // callback on layoutstop
                    transform: function (node, position ){ return position; } // transform a given node position. Useful for changing flow direction in discrete layouts
                };

                /*
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
                */
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
                    'font-family': 'Montserrat',
                    'font-weight': 'bold',
                    'background-color': 'data(background_color)',
                    'content': 'data(desc)',
                    "text-wrap": "wrap",
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
                    'text-rotation': 'autorotate',
                    'font-size': 0.8,
                    'font-family': 'Montserrat',
                    'color': '#666666',
                    'target-arrow-shape': 'vee',
                    'source-arrow-shape': 'tee',
                    'line-opacity': 'data(lineOpacity)'
                })
                .selector('node:selected')
                .style({
                    'border-color': '#1E90CC',
                    'border-width': "1px",
                    'width':6,'height':6
                })
                .selector('edge:selected')
                .style({
                    'line-color': '#1E90CC',
                    'width': 0.4,
                    'source-arrow-color':'#1E90CC',
                    'target-arrow-color':'#1E90CC',
                    'color': '#333333'
                })
            ,
            elements: {
                nodes: [],
                edges: []
            },
            layout: {
                name: 'cose'
            }
        });

        cy.on('select', 'node', function(evt){
            let node = evt.target;
            node.connectedEdges();
            cy.style()
                .selector(node.connectedEdges())
                .style({
                    'line-color': '#1E90CC',
                    'width': 0.4,
                    'source-arrow-color':'#1E90CC',
                    'target-arrow-color':'#1E90CC',
                    'color': '#333333'
                })
                .update();
        });

        cy.on('unselect', 'node', function(evt){
            let node = evt.target;
            cy.style()
                .selector(node.connectedEdges())
                .style({
                    'content': 'data(type)',
                    'width': 'data(lineWidth)',
                    'line-color': 'data(lineColor)',
                    'source-arrow-color':'data(sourceArrowColor)',
                    'target-arrow-color':'data(targetArrowColor)',
                    'arrow-scale': 0.1,
                    'line-style': 'data(lineStyle)',
                    'curve-style': 'data(curveStyle)',
                    'text-rotation': 'autorotate',
                    'font-size': 0.8,
                    'color': '#666666',
                    'target-arrow-shape': 'vee',
                    'source-arrow-shape': 'tee',
                    'line-opacity': 'data(lineOpacity)'
                })
                .update();
        });

        cy.on('select', 'edge', function(evt){
            let edge = evt.target;
            cy.style()
                .selector(edge.connectedNodes())
                .style({
                    'border-color': '#1E90CC',
                    'border-width': "1px",
                    'width':6,'height':6
                })
                .update();
        });

        cy.on('unselect', 'edge', function(evt){
            let edge = evt.target;
            cy.style()
                .selector(edge.connectedNodes())
                .style({
                    'border-width': "0px",
                })
                .update();
        });

    }
}