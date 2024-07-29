import ReactDOM from 'react-dom';
import CytoscapeComponent from 'react-cytoscapejs';
// @ts-ignore
import Cytoscape from 'cytoscape';
// @ts-ignore
import COSEBilkent from 'cytoscape-cose-bilkent';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement} from "react";

Cytoscape.use(COSEBilkent);
class DataRelationDistributionChartElement extends ReactAdapterElement {
    //https://github.com/plotly/react-cytoscapejs
    protected override render(hooks: RenderHooks): ReactElement | null {
        const [chartWidth, setWidth] = hooks.useState<number>("chartWidth");
        const [chartHeight, setHeight] = hooks.useState<number>("chartHeight");
        const [chartData, setChartData] = hooks.useState<any>("charData");

        const elements = [
            { data: { id: 'one', label: 'Node 1' }, position: { x: 0, y: 0 } },
            { data: { id: 'two', label: 'Node 2' }, position: { x: 100, y: 0 } },
            { data: { source: 'one', target: 'two', label: 'Edge from Node1 to Node2' } }
        ];

        const layout = { name: 'cose-bilkent' };

        const widthValueStr=''+chartWidth+'px';
        const heightValueStr=''+chartHeight+'px';

        return <CytoscapeComponent
            // @ts-ignore
            cy={(cy) => { this.cy = cy }}
            elements={CytoscapeComponent.normalizeElements({
                nodes: [
                    { data: { id: 'one', label: 'Node 1' }, position: { x: 0, y: 0 } },
                    { data: { id: 'two', label: 'Node 2' }, position: { x: 100, y: 0 } }
                ],
                edges: [
                    {
                        data: { source: 'one', target: 'two', label: 'Edge from Node1 to Node2' }
                    }
                ]
            })}
            style={
                { width: widthValueStr, height: heightValueStr }
            }

            stylesheet={[
                {
                    selector: 'node',
                    style: {
                        width: 5,
                        height: 5,
                        shape: 'rectangle'
                    }
                },
                {
                    selector: 'edge',
                    style: {
                        width: 2
                    }
                }
            ]}

            layout={layout}
        />;
    }
}

customElements.define(
    "data-relation-distribution-chart",
    DataRelationDistributionChartElement
);