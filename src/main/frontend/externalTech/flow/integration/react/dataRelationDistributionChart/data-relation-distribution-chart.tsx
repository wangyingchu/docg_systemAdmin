import ReactDOM from 'react-dom';
import CytoscapeComponent from 'react-cytoscapejs';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement} from "react";

class DataRelationDistributionChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {
        const elements = [
            { data: { id: 'one', label: 'Node 1' }, position: { x: 0, y: 0 } },
            { data: { id: 'two', label: 'Node 2' }, position: { x: 100, y: 0 } },
            { data: { source: 'one', target: 'two', label: 'Edge from Node1 to Node2' } }
        ];

        return <CytoscapeComponent elements={elements} style={ { width: '600px', height: '600px' } } />;
    }
}

customElements.define(
    "data-relation-distribution-chart",
    DataRelationDistributionChartElement
);