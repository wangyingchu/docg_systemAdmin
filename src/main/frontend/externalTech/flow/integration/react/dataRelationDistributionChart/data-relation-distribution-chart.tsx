import ReactDOM from 'react-dom';
import CytoscapeComponent from 'react-cytoscapejs';
// @ts-ignore
import Cytoscape from 'cytoscape';
// @ts-ignore
import COSEBilkent from 'cytoscape-cose-bilkent';
// @ts-ignore
import cola from 'cytoscape-cola';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement, useEffect, useRef} from "react";

//Cytoscape.use(COSEBilkent);
Cytoscape.use(cola);
class DataRelationDistributionChartElement extends ReactAdapterElement {
    //https://github.com/plotly/react-cytoscapejs
    protected override render(hooks: RenderHooks): ReactElement | null {
        const [chartWidth, setWidth] = hooks.useState<number>("chartWidth");
        const [chartHeight, setHeight] = hooks.useState<number>("chartHeight");
        const [chartData, setChartData] = hooks.useState<any>("chartData");

        const widthValueStr=''+chartWidth+'px';
        const heightValueStr=''+chartHeight+'px';
        const fgRef = useRef();

        useEffect(() => {});

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

        //const layout = { name: 'cose-bilkent',fit: true };
        const layout = { name: 'cola',


            animate: false, // whether to show the layout as it's running
            refresh: 1, // number of ticks per frame; higher is faster but more jerky
            maxSimulationTime: 4000, // max length in ms to run the layout
            ungrabifyWhileSimulating: false, // so you can't drag nodes during layout
            fit: true, // on every layout reposition of nodes, fit the viewport
            padding: 30, // padding around the simulation
            boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
            nodeDimensionsIncludeLabels: false, // whether labels should be included in determining the space used by a node

            // layout event callbacks
            ready: function(){}, // on layoutready
            stop: function(){}, // on layoutstop

            // positioning options
            randomize: false, // use random node positions at beginning of layout
            avoidOverlap: true, // if true, prevents overlap of node bounding boxes
            handleDisconnected: true, // if true, avoids disconnected components from overlapping
            convergenceThreshold: 0.01, // when the alpha value (system energy) falls below this value, the layout stops

            // @ts-ignore
            nodeSpacing: function( node ){ return 6; }, // extra spacing around nodes
            flow: undefined, // use DAG/tree flow layout if specified, e.g. { axis: 'y', minSeparation: 30 }
            alignment: undefined, // relative alignment constraints on nodes, e.g. {vertical: [[{node: node1, offset: 0}, {node: node2, offset: 5}]], horizontal: [[{node: node3}, {node: node4}], [{node: node5}, {node: node6}]]}
            gapInequalities: undefined, // list of inequality constraints for the gap between the nodes, e.g. [{"axis":"y", "left":node1, "right":node2, "gap":25}]
            centerGraph: true, // adjusts the node positions initially to center the graph (pass false if you want to start the layout from the current position)

            // different methods of specifying edge length
            // each can be a constant numerical value or a function like `function( edge ){ return 2; }`
            edgeLength: 4, // sets edge length directly in simulation
            edgeSymDiffLength: undefined, // symmetric diff edge length in simulation
            edgeJaccardLength: undefined, // jaccard edge length in simulation

            // iterations of cola algorithm; uses default values on undefined
            unconstrIter: undefined, // unconstrained initial layout iterations
            userConstIter: undefined, // initial layout iterations with user-specified constraints
            allConstIter: undefined, // initial layout iterations with all constraints including non-overlap
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
            minZoom={0.5}
            maxZoom={30}
        />;
    }
}

customElements.define(
    "data-relation-distribution-chart",
    DataRelationDistributionChartElement
);