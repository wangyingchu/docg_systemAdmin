import { ForceGraph2D, ForceGraph3D, ForceGraphVR, ForceGraphAR } from 'react-force-graph';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement} from "react";

class RelatedConceptionEntitiesDandelionGraphChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {

        return <ForceGraph3D />; // (3)
    }
}

customElements.define(
    "related-conceptionEntities-dandelionGraph-chart",
    RelatedConceptionEntitiesDandelionGraphChartElement
); // (4)