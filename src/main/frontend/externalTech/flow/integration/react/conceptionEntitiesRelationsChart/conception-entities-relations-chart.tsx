import { BasicNvlWrapper } from '@neo4j-nvl/react';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import React, {ReactElement, useRef} from "react";

class ConceptionEntitiesRelationsChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {

        return <BasicNvlWrapper
            nodes={[{ id: '0' }, { id: '1' }]}
            rels={[{ from: '0', to: '1', id: '10' }]}
        />; // (3)
    }
}

customElements.define(
    "conception-entities-relations-chart",
    ConceptionEntitiesRelationsChartElement
); // (4)