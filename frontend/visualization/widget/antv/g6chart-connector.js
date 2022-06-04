window.Vaadin.Flow.antv_G6ChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            //// functions
            setData : function(data) {
                this.chart.data = data;
            }
        };

        //console.log(G6.Global.version);

        /*
        const data = {
            nodes: [
                {
                    id: "node1",
                    label: "Circle1",
                    x: 150,
                    y: 150
                },
                {
                    id: "node2",
                    label: "Circle2",
                    x: 400,
                    y: 150
                }
            ],
            edges: [
                {
                    source: "node1",
                    target: "node2"
                }
            ]
        };

        const graph = new G6.Graph({
            container: c,
            width: 500,
            height: 500,
            defaultNode: {
                shape: "circle",
                size: [100],
                color: "#5B8FF9",
                style: {
                    fill: "#9EC9FF",
                    lineWidth: 3
                },
                labelCfg: {
                    style: {
                        fill: "#fff",
                        fontSize: 20
                    }
                }
            },
            defaultEdge: {
                style: {
                    stroke: "#e2e2e2"
                }
            }
        });

        graph.data(data);
        graph.render();
*/






        const width = c.scrollWidth;
        const height = c.scrollHeight || 500;


        const graph = new G6.Graph({
            container: c,
            width,
            height,
            layout: {
                type: 'force'
            },
            defaultNode: {
                size: 15,
                color: '#5B8FF9',
                style: {
                    lineWidth: 2,
                    fill: '#C6E5FF'
                }
            },
            defaultEdge: {
                size: 1,
                color: '#e2e2e2'
            }
        });

        fetch('https://gw.alipayobjects.com/os/antvdemo/assets/data/relations.json')
            .then(res => res.json())
            .then(data => {
                graph.data({
                    nodes: data.nodes,
                    edges: data.edges.map(function(edge, i) {
                        edge.id = 'edge' + i;
                        return Object.assign({}, edge);
                    })
                });
                graph.render();

                graph.on('node:dragstart', function(e) {
                    graph.layout();
                    refreshDragedNodePosition(e);
                });
                graph.on('node:drag', function(e) {
                    refreshDragedNodePosition(e);
                });
                graph.on('node:dragend', function(e) {
                    e.item.get('model').fx = null;
                    e.item.get('model').fy = null;
                });
            });

        function refreshDragedNodePosition(e) {
            const model = e.item.get('model');
            model.fx = e.x;
            model.fy = e.y;
        }
    }
}