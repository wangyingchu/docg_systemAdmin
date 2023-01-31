window.Vaadin.Flow.feature_DataRelationDistributionChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            lockGraph:function(data){
                cy.nodes().forEach(node => {
                    node.lock();
                })
            },
            setData : function(data) {
                cy.add(eval("(" + data + ")"));
            },
            clearData : function() {
                cy.remove(cy.elements());
            },
            deleteNode : function(data) {
                let targetNode = cy.filter('[id = '+data+']');
                cy.remove(targetNode.union(targetNode.connectedEdges()));
            },
            deleteNodeWithOneDegreeConnection : function(data) {
                let dataObj = eval("(" + data + ")");
                let targetNodeIDValue = '"'+dataObj.targetNodeId+'"';
                let ignoreNodeIDValue = '"'+dataObj.ignoreNodeId+'"';
                let targetNode = cy.filter('[id = '+ targetNodeIDValue+']');
                let relationsToDelete = targetNode.connectedEdges();
                let nodesToDelete = relationsToDelete.connectedNodes();
                let finalNodesToDelete = nodesToDelete.filter('[id != '+ignoreNodeIDValue+']');
                let nodesToDeletesRelation = finalNodesToDelete.connectedEdges();
                cy.remove(targetNode.union(relationsToDelete).union(finalNodesToDelete).union(nodesToDeletesRelation));
            },
            deleteOneDegreeConnectionNodes : function(data) {
                let dataObj = eval("(" + data + ")");
                let targetNodeIDValue = '"'+dataObj.targetNodeId+'"';
                let ignoreNodeIDValue = '"'+dataObj.ignoreNodeId+'"';
                let targetNode = cy.filter('[id = '+ targetNodeIDValue+']');
                let relationsToDelete = targetNode.connectedEdges();
                let nodesToDelete = relationsToDelete.connectedNodes();
                let finalNodesToDeleteMiddle = nodesToDelete.filter('[id != '+ignoreNodeIDValue+']');
                let finalNodesToDelete = finalNodesToDeleteMiddle.filter('[id != '+targetNodeIDValue+']');
                let nodesToDeletesRelation = finalNodesToDelete.connectedEdges();
                cy.remove(finalNodesToDelete.union(nodesToDeletesRelation));
            },
            initLayoutGraph: function(){
                let layout = cy.layout({
                    name: 'cose',
                    fit:true,
                    padding: 10, // the padding on fit
                });
                layout.run();
                cy.fit();
                cy.center();
            },
            layoutGraph: function(){
                let layout = cy.layout({
                    name: 'cose',
                    // Whether to animate while running the layout
                    // true : Animate continuously as the layout is running
                    // false : Just show the end result
                    // 'end' : Animate with the end result, from the initial positions to the end positions
                    animate: false,
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
                    padding: 10,
                    // Constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
                    boundingBox: undefined,
                    // Excludes the label when calculating node bounding boxes for the layout algorithm
                    nodeDimensionsIncludeLabels: false,
                    // Randomize the initial positions of the nodes (true) or use existing positions (false)
                    randomize: true,
                    // Extra spacing between components in non-compound graphs
                    componentSpacing: 40,
                    // Node repulsion (non overlapping) multiplier
                    nodeRepulsion: function( node ){ return 2048; },
                    // Node repulsion (overlapping) multiplier
                    nodeOverlap: 1,
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
                });
                layout.on("layoutready", () => {
                    cy.nodes().forEach(node => {
                        node.unlock();
                    })
                })
                layout.run();
                // cy.fit();
                // cy.center();
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
                    'text-outline-width': 0.15 ,
                    'text-outline-color': '#EEE',
                    'width': 'data(size)',
                    'height': 'data(size)'
                })
                .selector('edge')
                .css({
                    'content': 'data(type)',
                    'width': 0.3,
                    'line-color': '#DDDDDD',
                    'source-arrow-color':'#DDDDDD',
                    'target-arrow-color':'#DDDDDD',
                    'arrow-scale': 0.1,
                    'line-style': 'solid',
                    //'curve-style': 'unbundled-bezier',
                    //'curve-style': 'segments',
                    'curve-style': 'straight',
                    'text-rotation': 'autorotate',
                    'font-size': 0.8,
                    'font-family': 'Georgia',
                    //'font-weight': 'bold',
                    'color': '#666666',
                    'target-arrow-shape': 'vee',
                    'source-arrow-shape': 'tee'
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
        cy.on('dblclick', 'node', function(evt){
            let node = evt.target;
            c.$server.addConceptionEntityRelations(node.data().kind,node.id());
        });
        cy.on('unselect', 'node', function(evt){
            let node = evt.target;
            c.$server.unselectConceptionEntity(node.data().kind,node.id());
        });
        cy.on('select', 'node', function(evt){
            let node = evt.target;
            c.$server.selectConceptionEntity(node.data().kind,node.id());
        });
        cy.on('unselect', 'edge', function(evt){
            let edge = evt.target;
            c.$server.unselectRelationEntity(edge.data().type);
        });
        cy.on('select', 'edge', function(evt){
            let edge = evt.target;
            c.$server.selectRelationEntity(edge.data().type);
        });
    }
}