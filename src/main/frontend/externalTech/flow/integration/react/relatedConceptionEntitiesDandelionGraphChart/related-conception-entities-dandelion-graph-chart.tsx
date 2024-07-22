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

        const backgroundColor = '#FFFFFF'


        return <ForceGraph3D graphData={xx} backgroundColor={backgroundColor} width={800} height={800}
        />; // (3)
    }
}

customElements.define(
    "related-conception-entities-dandelion-graph-chart",
    RelatedConceptionEntitiesDandelionGraphChartElement
); // (4)