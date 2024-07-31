import ReactDOM from 'react-dom';
import CytoscapeComponent from 'react-cytoscapejs';
// @ts-ignore
import Cytoscape from 'cytoscape';
// @ts-ignore
import COSEBilkent from 'cytoscape-cose-bilkent';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement, useEffect, useRef} from "react";

Cytoscape.use(COSEBilkent);
class DataRelationDistributionChartElement extends ReactAdapterElement {
    //https://github.com/plotly/react-cytoscapejs
    protected override render(hooks: RenderHooks): ReactElement | null {
        const [chartWidth, setWidth] = hooks.useState<number>("chartWidth");
        const [chartHeight, setHeight] = hooks.useState<number>("chartHeight");
        const [chartData, setChartData] = hooks.useState<any>("chartData");
        const layout = { name: 'cose-bilkent' };
        const widthValueStr=''+chartWidth+'px';
        const heightValueStr=''+chartHeight+'px';
        const fgRef = useRef();

        // @ts-ignore
        const setListeners = (cy) => {
            // @ts-ignore
            cy.on('select', 'node', function(evt){
                let node = evt.target;
                node.connectedEdges();
                // @ts-ignore
                cy.style()
                    .selector(node.connectedEdges())
                    .style({
                        'line-color': '#1E90CC',
                        'width': 0.4,
                        'source-arrow-color':'#1E90CC',
                        'target-arrow-color':'#1E90CC',
                        'color': '#333333'
                    })
                    .update();
            });

            // @ts-ignore
            cy.on('unselect', 'node', function(evt){
                let node = evt.target;
                cy.style()
                    .selector(node.connectedEdges())
                    .style({
                        'content': 'data(type)',
                        'width': 'data(lineWidth)',
                        'line-color': 'data(lineColor)',
                        'source-arrow-color':'data(sourceArrowColor)',
                        'target-arrow-color':'data(targetArrowColor)',
                        'arrow-scale': 0.1,
                        'line-style': 'data(lineStyle)',
                        'curve-style': 'data(curveStyle)',
                        'text-rotation': 'autorotate',
                        'font-size': 0.8,
                        'color': '#666666',
                        'target-arrow-shape': 'vee',
                        'source-arrow-shape': 'tee',
                        'line-opacity': 'data(lineOpacity)'
                    })
                    .update();
            });

            // @ts-ignore
            cy.on('select', 'edge', function(evt){
                let edge = evt.target;
                cy.style()
                    .selector(edge.connectedNodes())
                    .style({
                        'border-color': '#1E90CC',
                        'border-width': "1px",
                        'width':6,'height':6
                    })
                    .update();
            });

            // @ts-ignore
            cy.on('unselect', 'edge', function(evt){
                let edge = evt.target;
                cy.style()
                    .selector(edge.connectedNodes())
                    .style({
                        'border-width': "0px",
                    })
                    .update();
            });
        };

        return <CytoscapeComponent
            //need set ref for useEffect function
            ref={fgRef}
            // @ts-ignore
            cy={(cy) => {
                setListeners(cy);
            }}
            elements={CytoscapeComponent.normalizeElements(chartData)}
            style={
                { width: widthValueStr, height: heightValueStr }
            }
            stylesheet={[
                {
                    selector: 'node',
                    style: {
                        'font-size': 1.1,
                        'font-family': 'Montserrat',
                        'font-weight': 'bold',
                        'background-color': 'data(background_color)',
                        'content': 'data(desc)',
                        "text-wrap": "wrap",
                        'text-valign': 'center',
                        'color': '#333333',
                        'shape': 'data(shape)',
                        'text-outline-width': 0.15 ,
                        'text-outline-color': '#EEE',
                        'width': 'data(size)',
                        'height': 'data(size)',
                        'background-opacity': 1
                    }
                },
                {
                    selector: 'edge',
                    style: {
                        'content': 'data(type)',
                        'width': 'data(lineWidth)',
                        'line-color': 'data(lineColor)',
                        'source-arrow-color':'data(sourceArrowColor)',
                        'target-arrow-color':'data(targetArrowColor)',
                        'arrow-scale': 0.1,
                        'line-style': 'data(lineStyle)',
                        'curve-style': 'data(curveStyle)',
                        'text-rotation': 'autorotate',
                        'font-size': 0.8,
                        'font-family': 'Montserrat',
                        'color': '#666666',
                        'target-arrow-shape': 'vee',
                        'source-arrow-shape': 'tee',
                        'line-opacity': 'data(lineOpacity)'
                    }
                },
                {
                    selector: 'node:selected',
                    style: {
                        'border-color': '#1E90CC',
                        'border-width': "1px",
                        'width':6,
                        'height':6
                    }
                },
                {
                    selector: 'edge:selected',
                    style: {
                        'line-color': '#1E90CC',
                        'width': 0.4,
                        'source-arrow-color':'#1E90CC',
                        'target-arrow-color':'#1E90CC',
                        'color': '#333333'
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