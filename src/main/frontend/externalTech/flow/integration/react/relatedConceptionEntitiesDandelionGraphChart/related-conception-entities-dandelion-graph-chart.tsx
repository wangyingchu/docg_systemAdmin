import { ForceGraph2D, ForceGraph3D, ForceGraphVR, ForceGraphAR } from 'react-force-graph';
import * as THREE from 'three'
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement, useEffect, useRef, useState} from "react";
import SpriteText from 'three-spritetext';
import { CSS2DRenderer,CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer.js';

class RelatedConceptionEntitiesDandelionGraphChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {
        const [chartWidth, setWidth] = hooks.useState<number>("chartWidth");
        const [chartHeight, setHeight] = hooks.useState<number>("chartHeight");
        const [chartData, setChartData] = hooks.useState<any>("charData");

        const simulateGraphData = {
            nodes: [...Array(1000).keys()].map(i => ({id: i})),
            links: [...Array(1000).keys()]
                .filter(id => id)
                .map(id => ({
                    ['target' ]: id,
                    [ 'source' ]: Math.round(Math.random() * (id - 1))
                }))
        };

        const getChartGraphData = () =>{
            return {
                nodes:chartData.nodesInfo,
                links:chartData.edgesInfo
            }
        }

        const fgRef = useRef();

        useEffect(() => {
            const fg = fgRef.current;
            // Spread nodes a little wider
            // @ts-ignore
            fgRef.current.d3Force('charge').strength(-300);
        });

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

        let forceGraph3D = <ForceGraph3D
            // @ts-ignore
            graphData={getChartGraphData()}
            width={chartWidth}
            height={chartHeight}
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
    }
}

customElements.define(
    "related-conception-entities-dandelion-graph-chart",
    RelatedConceptionEntitiesDandelionGraphChartElement
);