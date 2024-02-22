window.Vaadin.Flow.feature_DataRelationDistribution3DChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            emptyGraph:function(data){
                Graph
                    .width(200)
                    .height(200)
                    .graphData({
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

                const nodeMapping = {};
                gData.nodes.forEach(node =>{
                   //page_info.set(node.entityKind,node);
                    nodeMapping[node.entityKind] = node;

                });

                console.log(nodeMapping);

                const linkMapping = {};
                gData.links.forEach(link => {
                    linkMapping[link.entityKind] = link;
                });


                const highlightNodes = new Set();
                const highlightLinks = new Set();
                let hoverNode = null;

                // cross-link node objects
                gData.links.forEach(link => {
                    //const a = gData.nodes[link.source.entityKind];
                    //const b = gData.nodes[link.target.entityKind];

                    const a = nodeMapping[link.source];
                    const b = nodeMapping[link.target];

                    console.log(link);
                    console.log(gData.nodes);
                    console.log(link.source);
                    //console.log(b);

                    !a.neighbors && (a.neighbors = []);
                    !b.neighbors && (b.neighbors = []);
                    a.neighbors.push(b);
                    b.neighbors.push(a);

                    !a.links && (a.links = []);
                    !b.links && (b.links = []);
                    a.links.push(link);
                    b.links.push(link);
                });







                Graph = ForceGraph3D({
                    extraRenderers: [new THREE.CSS2DRenderer()]
                })(c).graphData(gData)
                    .backgroundColor('#FFFFFF')
                    .nodeVal(node => {
                        return node.size;
                    })
                    .width(200)
                    .height(200)
                    .nodeRelSize(2)
                    .nodeResolution(100)
                    .nodeOpacity(0.85)
                    .linkOpacity(0.7)
                    .linkDirectionalArrowLength(4)
                    .linkCurvature(0.2)
                    .linkDirectionalArrowRelPos(0.5)
                    .linkWidth(0.3)
                    .linkThreeObjectExtend(true)
                    .linkThreeObject(link => {
                        // extend link with text sprite
                        const sprite = new SpriteText(`${link.entityKind} : ${link.source} > ${link.target}`);
                        sprite.color = 'black';
                        sprite.textHeight = 1.5;
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
                        nodeEl.textContent = node.entityKind+' ('+node.entityCount+")";
                        nodeEl.className = 'node-label';
                        return new THREE.CSS2DObject(nodeEl);
                    })
                    .nodeThreeObjectExtend(true)



                    .nodeColor(node => highlightNodes.has(node) ? node === hoverNode ? 'rgb(255,0,0,1)' : 'rgba(255,160,0,0.8)' : 'rgba(0,255,255,0.6)')
                    .linkWidth(link => highlightLinks.has(link) ? 4 : 1)
                    .linkDirectionalParticles(link => highlightLinks.has(link) ? 4 : 0)
                    .linkDirectionalParticleWidth(4)



                    .onNodeHover(node => {

                        console.log(node);

                        // no state change
                        if ((!node && !highlightNodes.size) || (node && hoverNode === node)) return;

                        highlightNodes.clear();
                        highlightLinks.clear();
                        if (node) {
                            highlightNodes.add(node);
                            node.neighbors.forEach(neighbor => highlightNodes.add(neighbor));
                            node.links.forEach(link => highlightLinks.add(link));
                        }

                        hoverNode = node || null;

                        console.log("++++++++++++++++++");
                        console.log("++++++++++++++++++");
                        console.log("++++++++++++++++++");

                        Graph
                            .nodeColor(Graph.nodeColor())
                            .linkWidth(Graph.linkWidth())
                            .linkDirectionalParticles(Graph.linkDirectionalParticles());

                    })
                    .onLinkHover(link => {
                        highlightNodes.clear();
                        highlightLinks.clear();

                        if (link) {
                            highlightLinks.add(link);
                            highlightNodes.add(link.source);
                            highlightNodes.add(link.target);
                        }

                        Graph
                            .nodeColor(Graph.nodeColor())
                            .linkWidth(Graph.linkWidth())
                            .linkDirectionalParticles(Graph.linkDirectionalParticles());
                    })








                    .onNodeDragEnd(node => {
                        node.fx = node.x;
                        node.fy = node.y;
                        node.fz = node.z;
                    })
                    .onNodeClick(node => {
                        // Aim at node from outside it
                        const distance = 100;
                        const distRatio = 1 + distance/Math.hypot(node.x, node.y, node.z);
                        const newPos = node.x || node.y || node.z
                            ? { x: node.x * distRatio, y: node.y * distRatio, z: node.z * distRatio }
                            : { x: 0, y: 0, z: distance }; // special case if node is in (0,0,0)
                        Graph.cameraPosition(
                            newPos, // new position
                            node, // lookAt ({ x, y, z })
                            3000  // ms transition duration
                        );
                    });
                Graph
                    .width(dataObj.graphWidth)
                    .height(dataObj.graphHeight)
                    .graphData(gData)
                    .refresh();
                // Spread nodes a little wider
                Graph.d3Force('charge').strength(-800);
            }
        };

        let Graph;



    }
}