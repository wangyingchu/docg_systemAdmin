import { ForceGraph2D, ForceGraph3D, ForceGraphVR, ForceGraphAR } from 'react-force-graph';

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
                             backgroundColor={'#FFFFFF'}
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
                             linkThreeObjectExtend={true}
        />;
    }
}

customElements.define(
    "related-conception-entities-dandelion-graph-chart",
    RelatedConceptionEntitiesDandelionGraphChartElement
);