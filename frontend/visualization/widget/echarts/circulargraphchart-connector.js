window.Vaadin.Flow.eCharts_CircularGraphChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData: function (data) {
                let nodesData = data.nodes;
                nodesData.forEach(function (node) {
                    node.symbolSize = node.weight + 8;
                    node.symbolSize /= 1.5;
                    node.label = {
                        show: true
                    };
                    node.draggable = true;
                    node.category =  node.name;
                    categories.push({
                        name: node.name
                    })
                });

                c.$connector.option.legend[0].data = categories.map(function (a) {
                    return a.name;
                });

                this.option.series[0].data = nodesData;
                this.option.series[0].links = data.links;

                if (c.$connector.option && typeof c.$connector.option === "object") {
                    c.$connector.myChart.setOption(c.$connector.option, true);
                }
            },
            setNodeNameLabel: function (nodeNameLabel) {
                c.$connector.nodeNameLabel = nodeNameLabel;
            },
            setNodeDescLabel: function (nodeDescLabel) {
                c.$connector.nodeDescLabel = nodeDescLabel;
            },
            setNodeWeightLabel: function (nodeWeightLabel) {
                c.$connector.nodeWeightLabel = nodeWeightLabel;
            },
            setEdgeDescLabel: function (edgeDescLabel) {
                c.$connector.edgeDescLabel = edgeDescLabel;
            },
            setEdgeNameLabel: function (edgeNameLabel) {
                c.$connector.edgeNameLabel = edgeNameLabel;
            },
            setEdgeIdLabel: function (edgeIdLabel) {
                c.$connector.edgeIdLabel = edgeIdLabel;
            },
            setEdgeSourceLabel: function (edgeSourceLabel) {
                c.$connector.edgeSourceLabel = edgeSourceLabel;
            },
            setEdgeTargetLabel: function (edgeTargetLabel) {
                c.$connector.edgeTargetLabel = edgeTargetLabel;
            }
        };
        c.$connector.myChart = echarts.init(c);

        c.$connector.nodeNameLabel = "nodeNameLabel";
        c.$connector.nodeDescLabel = "nodeDescLabel";
        c.$connector.nodeWeightLabel = "nodeWeightLabel";
        c.$connector.edgeDescLabel = "edgeDescLabel";
        c.$connector.edgeNameLabel = "edgeNameLabel";
        //c.$connector.edgeWeightLabel = "";
        c.$connector.edgeIdLabel = "edgeIdLabel";
        c.$connector.edgeSourceLabel = "edgeSourceLabel";
        c.$connector.edgeTargetLabel = "edgeTargetLabel";

        let categories = [];
        let data = [];
        let links=[];

        c.$connector.option = {
            title: {},
            tooltip: {
                formatter: function (params) {
                    var labelData = params.data;
                    if(params.dataType==='node'){
                        var res='<div>'+ c.$connector.nodeNameLabel+': '+labelData.name+'</div>'+
                            '<div>'+ c.$connector.nodeDescLabel+': '+labelData.desc+'</div>';
                        if(labelData.weight) {
                            res = res + '<div>'+ c.$connector.nodeWeightLabel+': '+labelData.weight+'</div>';
                        }
                    }
                    if(params.dataType==='edge'){
                        var res=
                            '<div>'+ c.$connector.edgeDescLabel+': '+labelData.desc+'</div>'+
                            '<div>'+ c.$connector.edgeNameLabel+': '+labelData.name+'</div>'+
                            '<div>'+ c.$connector.edgeIdLabel+': '+labelData.id+'</div>'+
                            '<div>'+ c.$connector.edgeSourceLabel+': '+labelData.source+'</div>'+
                            '<div>'+ c.$connector.edgeTargetLabel+': '+labelData.target+'</div>';
                    }
                    return res;
                }
            },
            legend: [{
                data: categories.map(function (a) {
                    return a.name;
                })
            }],

            animationDuration: 1500,
            animationEasingUpdate: 'quinticInOut',
            series : [
                {
                    name: 'Circular Graph',
                    type: 'graph',
                    layout: 'circular',
                    circular: {
                        rotateLabel: true
                    },
                    categories:categories,
                    nodes:data,
                    links :links,
                    roam: true,
                    focusNodeAdjacency: true,
                    itemStyle: {
                        borderColor: '#fff',
                        borderWidth: 1,
                        shadowBlur: 10,
                        shadowColor: 'rgba(0, 0, 0, 0.3)'
                    },
                    label: {
                        position: 'right',
                        formatter: '{b}'
                    },
                    lineStyle: {
                        color: 'source',
                        curveness: 0.3
                    },
                    emphasis: {
                        lineStyle: {
                            width: 10
                        }
                    },
                    edgeSymbol: ['none', 'arrow'],
                    edgeLabel:{
                        show:true,
                        formatter: function (params) {
                            var labelData = params.data;
                            var res=labelData.desc
                            return res;
                        },
                        fontSize: 8
                    }
                }
            ]
        };
    }
}