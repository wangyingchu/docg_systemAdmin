import { ForceGraph2D, ForceGraph3D, ForceGraphVR, ForceGraphAR } from 'react-force-graph';
import * as THREE from 'three'
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement} from "react";

class RelatedConceptionEntitiesDandelionGraphChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {
        const xx = {
            nodes: [...Array(1000).keys()].map(i => ({id: i})),
            links: [...Array(1000).keys()]
                .filter(id => id)
                .map(id => ({
                    ['target' ]: id,
                    [ 'source' ]: Math.round(Math.random() * (id - 1))
                }))
        };

        return <ForceGraph3D graphData={xx}
                             //extraRenderers={[new THREE.CSS2DRenderer()]}


                              linkThreeObjectExtend={true}

                            linkThreeObject= {(link: { entityKind: any; source: any; target: any; }) => {
                                // extend link with text sprite
                                const sprite = new THREE.SpriteText(`${link.entityKind} : ${link.source} > ${link.target}`);
                                sprite.color = 'black';
                                sprite.textHeight = 1.5;
                                return sprite;
                            }}


                             backgroundColor={'#CE0000'}
                             width={800}
                             height={800}
                             nodeVal={node => {
                                 return node.size;
                             }}
                             nodeRelSize={2}
                             nodeResolution={100}
                             nodeOpacity={0.85}
                             linkOpacity={0.7}
                             linkDirectionalArrowLength={4}
                             linkCurvature={0.2}
                             linkDirectionalArrowRelPos={0.5}
                             linkWidth={0.3}

        />;
    }
}

customElements.define(
    "related-conception-entities-dandelion-graph-chart",
    RelatedConceptionEntitiesDandelionGraphChartElement
);