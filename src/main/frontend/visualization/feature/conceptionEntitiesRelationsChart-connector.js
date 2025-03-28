window.Vaadin.Flow.feature_ConceptionEntitiesRelationsChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            emptyGraph:function(data){
                Graph.graphData({
                    nodes:[],
                    links:[]
                }).refresh();
            },
            generateGraph : function(data) {
                let dataObj = eval("(" + data + ")");
                // Random tree
                /*
                const N = 300;
                const gData = {
                    nodes: [...Array(N).keys()].map(i => ({
                        id: i,
                        color:'#0099FF'

                        //,name:'nodeName'+i
                    })),
                    links: [...Array(N).keys()]
                        .filter(id => id)
                        .map(id => ({
                            source: id,
                            target: Math.round(Math.random() * (id-1)),
                            color:'#666666'
                            //,name:'linkName'+id
                        }))
                };
                */

                const gData = {
                    nodes:dataObj.nodesInfo,
                    links:dataObj.edgesInfo
                }

                Graph = ForceGraph3D({
                    extraRenderers: [new THREE.CSS2DRenderer()]
                })(c).graphData(gData)
                    .backgroundColor('#FFFFFF')
                    .nodeVal(node => {
                        return node.size;
                    })
                    .nodeResolution(15)
                    .nodeOpacity(0.85)
                    .linkOpacity(0.95)
                    .linkDirectionalArrowLength(8)
                    .linkCurvature(0.025)
                    .linkDirectionalArrowRelPos(0.5)
                    .linkWidth(1)
                    .linkThreeObjectExtend(true)
                    .linkThreeObject(link => {
                        // extend link with text sprite
                        const sprite = new SpriteText(`${link.entityKind}`);
                        sprite.color = 'black';
                        sprite.textHeight = 3;
                        return sprite;
                    })
                    .linkPositionUpdate((sprite, { start, end }) => {
                        const middlePos = Object.assign(...['x', 'y', 'z'].map(c => ({
                            [c]: start[c] + (end[c] - start[c]) / 2 // calc middle point
                        })));
                        // Position sprite
                        Object.assign(sprite.position, middlePos);
                    })
                    .nodeThreeObject(node => {
                        const nodeEl = document.createElement('div');
                        nodeEl.textContent = node.id;
                        nodeEl.className = 'node-label';
                        return new THREE.CSS2DObject(nodeEl);
                    })
                    .nodeThreeObjectExtend(true)
                    .onNodeDragEnd(node => {
                        node.fx = node.x;
                        node.fy = node.y;
                        node.fz = node.z;
                    })
                    .onNodeClick(node => {
                        c.$server.showEntityDetail(node.entityKind,node.id);
                        // Aim at node from outside it
                        const distance = 150;
                        const distRatio = 1 + distance/Math.hypot(node.x, node.y, node.z);
                        const newPos = node.x || node.y || node.z
                            ? { x: node.x * distRatio, y: node.y * distRatio, z: node.z * distRatio }
                            : { x: 0, y: 0, z: distance }; // special case if node is in (0,0,0)
                        Graph.cameraPosition(
                            newPos, // new position
                            node, // lookAt ({ x, y, z })
                            3000  // ms transition duration
                        );
                    })
                    .onNodeRightClick(node => {
                        c.$server.expendTimeFlowEntity(node.entityKind,node.id);
                    });

                Graph
                    .width(dataObj.graphWidth)
                    .height(dataObj.graphHeight)
                    .graphData(gData)
                    .refresh();
                // Spread nodes a little wider
                Graph.d3Force('charge').strength(-2000);
            },
            insertGraph : function(data) {
                let dataObj = eval("(" + data + ")");
                const { nodes, links } = Graph.graphData();
                for(var index1 in dataObj.nodesInfo){
                    nodes.push(dataObj.nodesInfo[index1]);
                }
                for(var index2 in dataObj.edgesInfo){
                    links.push(dataObj.edgesInfo[index2]);
                }
                Graph.graphData({
                    nodes: [...nodes],
                    links: [...links]
                });
            }
        };

        let Graph;
    }
}