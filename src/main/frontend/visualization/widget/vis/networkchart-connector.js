window.Vaadin.Flow.vis_NetworkChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            //// functions
            setData : function(data) {
                //this.chart.data = data;
            }
        };

        const changeChosenEdgeWidthSize =
            function(values, id, selected, hovering) {
                values.width = 6;
            };

        const changeChosenNodeSize =
            function(values, id, selected, hovering) {
                values.size = values.size + 3;
            };

        const globalColorsLoopArray= [
            '#058DC7', '#50B432', '#ED561B', '#24CBE5', '#CE0000',
            '#64E572', '#FF9655', '#FFF263', '#6AF9C4', '#EC2500',
            '#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9',
            '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1',
            '#ECE100', '#EC9800', '#9EDE00', '#DDDF00', '#c23531',
            '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83',
            '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'
        ];

        function getCurrentGlobalColor(colorIndex){
            if(colorIndex<globalColorsLoopArray.length){
                return globalColorsLoopArray[colorIndex];
            }else{
                return globalColorsLoopArray[colorIndex%globalColorsLoopArray.length];
            }
        }

        c.$connector.nodes = new vis.DataSet([
            { id: 1,  group: 1,font: { multi: true }, label: '<b>This</b> is a\n<i>default</i> <b><i>multi-</i>font</b> <code>label</code>',chosen: { label: false, node: changeChosenNodeSize }, color: { background: getCurrentGlobalColor(1) }},
            { id: 2, group: 3,font: { multi: 'html', size: 20 }, label: '<b>This</b> is an\n<i>html</i> <b><i>multi-</i>font</b> <code>label</code>',
                color: { background: "#0099ff" },chosen: { label: false, node: changeChosenNodeSize }
            },
            { id: 3, font: { multi: 'md', face: 'georgia' }, label: '*This* is a\n_markdown_ *_multi-_ font* `label`' ,chosen: { label: false, node: changeChosenNodeSize }},
            { id: 4, label: "Node 4" ,chosen: { label: false, node: changeChosenNodeSize }},
            { id: 5, label: "Node 5" ,group: 2,
                color: { background: "#ce0000" },
                chosen: { label: false, node: changeChosenNodeSize }
            },

            {
                id: '6-uj@:',
                title:'12345678\n <br/>hhhaaaa',
                label: "Node: 6\nGroup: 2\nCustom background",
                group: 2,
                color: { background: getCurrentGlobalColor(22) },
                chosen: { label: false, node: changeChosenNodeSize }
            }
        ]);

        // create an array with edges
        c.$connector.edges = new vis.DataSet([
            { from: 1, to: 3 ,arrows:'to', chosen: { label: false, edge: changeChosenEdgeWidthSize}},
            { from: 1, to: 2 ,arrows:'to', chosen: { label: false, edge: changeChosenEdgeWidthSize}},
            { from: 2, to: 4 ,arrows:'to',label: "Node 4 to lll" , chosen: { label: false, edge: changeChosenEdgeWidthSize}},
            { from: 2, to: 5 ,arrows:'to', chosen: { label: false, edge: changeChosenEdgeWidthSize}},
            { from: 3, to: 3 ,arrows:'to' ,
                chosen: { label: false, edge: changeChosenEdgeWidthSize } }

        ]);

        // create a network
        c.$connector.data = {
            nodes: c.$connector.nodes,
            edges: c.$connector.edges
        };
        c.$connector.options = {
            nodes: {
                shape: 'dot',
                size: 10,
                font: {
                    size: 14
                },
                color: {
                    border: '#dddddd',
                    background: '#666666',
                    highlight:{background:'#222222',border:'#dddddd'},
                    hover:{background:'#444444',border:'#dddddd'}
                },
                borderWidth: 1,
                shadow:true
            },
            edges: {
                width: 1,
                shadow:false,
                font: {
                    size: 10
                },
                smooth: {
                    type: "continuous",
                    forceDirection: "none",
                    roundness: 0.09
                }
            },
            physics: {
                barnesHut: {
                    gravitationalConstant: -14000,
                    centralGravity: 1.00,
                    springLength: 100,
                    springConstant: 0,
                    damping: 0.15,
                    avoidOverlap: 0.3
                },
                maxVelocity: 45,
                minVelocity: 0.75
            },
            interaction:{hover:true}
        };
        c.$connector.network = new vis.Network(c, c.$connector.data, c.$connector.options);


        c.$connector.network.on("click", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>Click event:</h2>' + JSON.stringify(params, null, 4);
            console.log('click event, getNodeAt returns: ' + this.getNodeAt(params.pointer.DOM));
        });
        c.$connector.network.on("doubleClick", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>doubleClick event:</h2>' + JSON.stringify(params, null, 4);
        });
        c.$connector.network.on("oncontext", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>oncontext (right click) event:</h2>' + JSON.stringify(params, null, 4);
        });
        c.$connector.network.on("dragStart", function (params) {
            // There's no point in displaying this event on screen, it gets immediately overwritten
            params.event = "[original event]";
            console.log('dragStart Event:', params);
            console.log('dragStart event, getNodeAt returns: ' + this.getNodeAt(params.pointer.DOM));
        });
        c.$connector.network.on("dragging", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>dragging event:</h2>' + JSON.stringify(params, null, 4);
        });
        c.$connector.network.on("dragEnd", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>dragEnd event:</h2>' + JSON.stringify(params, null, 4);
            console.log('dragEnd Event:', params);
            console.log('dragEnd event, getNodeAt returns: ' + this.getNodeAt(params.pointer.DOM));
        });
        c.$connector.network.on("controlNodeDragging", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>control node dragging event:</h2>' + JSON.stringify(params, null, 4);
        });
        c.$connector.network.on("controlNodeDragEnd", function (params) {
            params.event = "[original event]";
            //document.getElementById('eventSpan').innerHTML = '<h2>control node drag end event:</h2>' + JSON.stringify(params, null, 4);
            console.log('controlNodeDragEnd Event:', params);
        });
        c.$connector.network.on("zoom", function (params) {
            //document.getElementById('eventSpan').innerHTML = '<h2>zoom event:</h2>' + JSON.stringify(params, null, 4);
        });
        c.$connector.network.on("showPopup", function (params) {
            //document.getElementById('eventSpan').innerHTML = '<h2>showPopup event: </h2>' + JSON.stringify(params, null, 4);
        });
        c.$connector.network.on("hidePopup", function () {
            console.log('hidePopup Event');
        });
        c.$connector.network.on("select", function (params) {
            console.log('select Event:', params);
        });
        c.$connector.network.on("selectNode", function (params) {
            console.log('selectNode Event:', params);
        });
        c.$connector.network.on("selectEdge", function (params) {
            console.log('selectEdge Event:', params);
        });
        c.$connector.network.on("deselectNode", function (params) {
            console.log('deselectNode Event:', params);
        });
        c.$connector.network.on("deselectEdge", function (params) {
            console.log('deselectEdge Event:', params);
        });
        c.$connector.network.on("hoverNode", function (params) {
            console.log('hoverNode Event:', params);
        });
        c.$connector.network.on("hoverEdge", function (params) {
            console.log('hoverEdge Event:', params);
        });
        c.$connector.network.on("blurNode", function (params) {
            console.log('blurNode Event:', params);
        });
        c.$connector.network.on("blurEdge", function (params) {
            console.log('blurEdge Event:', params);
        });

    }
}