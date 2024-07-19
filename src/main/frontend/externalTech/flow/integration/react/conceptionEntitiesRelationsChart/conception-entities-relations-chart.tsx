//import type { HitTargets, Node, Relationship } from '@neo4j-nvl/core'
import { BasicNvlWrapper } from '@neo4j-nvl/react';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement} from "react";

class ConceptionEntitiesRelationsChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {


        const nodes = [
            { id: '1', label: 'Node 1', color: '#e04141' },
            { id: '2', label: 'Node 2', color: '#e09c41' }
        ]

        const relationships = [
            { id: '12', from: '1', to: '2' }
        ]

        const optionA = {
            layout: 'forceDirected',
            initialZoom: 0.5
        }

        const callbacks = {
            onLayoutDone: () => console.log('Layout done')
        }







        return <BasicNvlWrapper
            nodes={nodes}
            rels={relationships}



        />; // (3)
    }
}

customElements.define(
    "conception-entities-relations-chart",
    ConceptionEntitiesRelationsChartElement
); // (4)