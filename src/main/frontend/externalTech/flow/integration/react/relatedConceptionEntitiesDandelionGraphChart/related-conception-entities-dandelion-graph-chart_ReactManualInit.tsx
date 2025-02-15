import React, {useState,useRef,useEffect } from 'react';
import {createRoot } from 'react-dom/client';
import {ForceGraph3D} from 'react-force-graph';
import SpriteText from 'three-spritetext';
// @ts-ignore
import { CSS2DRenderer,CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer.js';
// @ts-ignore
window.relatedConceptionEntitiesDandelionGraphChartConnectorInit = (element, width,height,chartInitData)  => {
    const root = createRoot(element);
    // to use useState, need a React component
    const Wrapper = () => {
        //const [color, setColor] = useState(initialValue);
        // @ts-ignore
        const [chartWidth, setWidth] = useState<number>(width);
        // @ts-ignore
        const [chartHeight, setHeight] = useState<number>(height);
        // @ts-ignore
        //const [chartData, setChartData] = useState<any>({nodes:[],links:[]});
        const [chartData, setChartData] = useState<any>(JSON.parse(chartInitData));

        const fgRef = useRef();

        useEffect(() => {
            const fg = fgRef.current;
            // Spread nodes a little wider
            // @ts-ignore
            fgRef.current.d3Force('charge').strength(-280);

            const handleDestroy = () => {
                // Logic to unmount the component
                root.unmount();
            };
            window.addEventListener('destroyRelatedConceptionEntitiesDandelionGraphChart', handleDestroy);

            // @ts-ignore
            const updateGraphData = (graphData) =>{
                //setChartData(JSON.parse(graphData.detail.data));
                console.log(graphData);
            }
            window.addEventListener('updateRelatedConceptionEntitiesDandelionGraphChartData', updateGraphData);

            return () => {
                window.removeEventListener('destroyRelatedConceptionEntitiesDandelionGraphChart', handleDestroy);
                window.removeEventListener('updateRelatedConceptionEntitiesDandelionGraphChartData', handleDestroy);
            };
        }, []);

        const onNodeClickFunc = (node:{x:any;y:any;z:any})=>{
            // Aim at node from outside it
            const distance = 40;
            const distRatio = 1 + distance/Math.hypot(node.x, node.y, node.z);
            const newPos = node.x || node.y || node.z
                ? { x: node.x * distRatio, y: node.y * distRatio, z: node.z * distRatio }
                : { x: 0, y: 0, z: distance }; // special case if node is in (0,0,0)
            // @ts-ignore
            fgRef.current.cameraPosition(
                newPos, // new position
                node, // lookAt ({ x, y, z })
                3000  // ms transition duration
            );
        };

        const forceGraph3D = <ForceGraph3D
            graphData = {chartData}
            width = {chartWidth}
            height = {chartHeight}
            ref={fgRef}
            backgroundColor={'#FFFFFF'}
            nodeRelSize={6}
            nodeResolution={15}
            nodeOpacity={0.85}
            linkOpacity={0.7}
            linkDirectionalArrowLength={4}
            linkCurvature={0.06}
            linkDirectionalArrowRelPos={0.9}
            linkWidth={0.8}

            linkThreeObjectExtend={true}
            linkThreeObject={(link: { entityKind: any; source: any; target: any; }) => {
                // extend link with text sprite
                const sprite = new SpriteText(`${link.entityKind} : ${link.source} > ${link.target}`);
                sprite.color = 'black';
                sprite.textHeight = 1.5;
                return sprite;
            }}
            linkPositionUpdate={(sprite, { start, end }) => {
                // @ts-ignore
                const middlePos = Object.assign(...['x', 'y', 'z'].map(c => ({
                    // @ts-ignore
                    [c]: start[c] + (end[c] - start[c]) / 2 // calc middle point
                })));
                // Position sprite
                Object.assign(sprite.position, middlePos);
            }}

            extraRenderers={[new CSS2DRenderer()]}
            nodeThreeObjectExtend={true}
            nodeThreeObject={(node:{entityKind:any;id:any}) => {
                const nodeEl = document.createElement('div');
                nodeEl.textContent = node.entityKind+' '+node.id;
                //nodeEl.style.color = node.color;
                nodeEl.className = 'node-label';
                return new CSS2DObject(nodeEl);
            }}
            onNodeDragEnd={node => {
                node.fx = node.x;
                node.fy = node.y;
                node.fz = node.z;
            }}
            onNodeClick={onNodeClickFunc}
        />;
        return forceGraph3D;
    };
    root.render(<Wrapper/>);
}
/*
// @ts-ignore
window.relatedConceptionEntitiesDandelionGraphChartConnectorInit2 = (data)  => {
    var event = new CustomEvent('updateRelatedConceptionEntitiesDandelionGraphChartData', {
        detail: {
            foo: 'bar',
            baz: 42
        }
    });
    window.dispatchEvent(event);
}
*/