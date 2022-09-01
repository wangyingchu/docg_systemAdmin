window.Vaadin.Flow.feature_RelatedConceptionEntitiesNebulaGraphChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
        };
        const N = 300;
        const gData = {
            nodes: [...Array(N).keys()].map(i => ({ id: i })),
            links: [...Array(N).keys()]
                .filter(id => id)
                .map(id => ({
                    source: id,
                    target: Math.round(Math.random() * (id-1))
                }))
        };
        const Graph = ForceGraph3D()(c).graphData(gData);
    }
}