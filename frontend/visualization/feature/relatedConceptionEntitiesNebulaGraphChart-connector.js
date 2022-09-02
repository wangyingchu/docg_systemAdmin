window.Vaadin.Flow.feature_RelatedConceptionEntitiesNebulaGraphChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
        };
        // Random tree
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

        const Graph = ForceGraph3D({
            extraRenderers: [new THREE.CSS2DRenderer()]
        })(c).graphData(gData)
            .backgroundColor('#FFFFFF')
            .width(1850)
            .height(750)
            .nodeRelSize(6)
            .nodeResolution(20)
            .linkOpacity(0.7)
            .linkDirectionalArrowLength(6)
            .linkCurvature(0.01)
            .linkDirectionalArrowRelPos(0.9)
            .linkWidth(1.2)
            .linkThreeObjectExtend(true)
            .linkThreeObject(link => {
                // extend link with text sprite
                const sprite = new SpriteText(`${link.source} > ${link.target}`);
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
                nodeEl.textContent = 'NodeText '+node.id;
                //nodeEl.style.color = node.color;
                nodeEl.className = 'node-label';
                return new THREE.CSS2DObject(nodeEl);
            })
            .nodeThreeObjectExtend(true)
            .onNodeClick(node => {
                // Aim at node from outside it
                const distance = 40;
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
        ;
        // Spread nodes a little wider
        Graph.d3Force('charge').strength(-120);
    }
}