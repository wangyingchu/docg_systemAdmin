window.Vaadin.Flow.feature_ConceptionKindsCorrelationInfoSummaryChart = {
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

                if(dynamicRenderingEdgeWidth){
                    let linksData = data.links;
                    linksData.forEach(function (link) {
                        link.lineStyle = {};
                        link.lineStyle.width = link.weight;
                        link.lineStyle.opacity = 0.5;
                    });
                }

                c.$connector.option.legend[0].data = categories.map(function (a) {
                    return a.name;
                });

                this.option.series[0].data = nodesData;
                this.option.series[0].links = data.links;

                if (c.$connector.option && typeof c.$connector.option === "object") {
                    c.$connector.myChart.setOption(c.$connector.option, true);
                }
            },
            hideLegend: function () {
                this.option.legend[0].show = false;
            },
            showLegend: function () {
                this.option.legend[0].show = true;
            },
            enableDynamicRenderingEdgeWidth:function(){
                dynamicRenderingEdgeWidth = true;
            },
            disableDynamicRenderingEdgeWidth:function(){
                dynamicRenderingEdgeWidth = false;
            }
        };
        c.$connector.myChart = echarts.init(c);

        let categories = [];
        let data = [];
        let links=[];
        let dynamicRenderingEdgeWidth = false

        c.$connector.option = {
            title: {},
            tooltip: {
                formatter: function (params) {
                    var labelData = params.data;
                    if(params.dataType==='node'){
                        var res='<div> 概念类型: '+ labelData.name+'( '+labelData.desc+' )</div>';
                        if(labelData.weight) {
                            res = res + '<div> 实体数量: '+labelData.data.entityCount+'</div>';
                        }
                    }
                    if(params.dataType==='edge'){
                        var res=
                            '<div> 关系类型: '+labelData.name+'( '+labelData.desc+' )</div>'+
                            '<div> 关联的概念: '+ labelData.data.sourceConceptionKind+' -> '+labelData.data.targetConceptionKind+'</div>';
                    }
                    return res;
                }
            },
            legend: [{
                data: categories.map(function (a) {
                    return a.name;
                }),
                show: true
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
                        borderColor: '#FFF',
                        borderWidth: 0.5,
                        shadowBlur: 2,
                        shadowColor: 'rgba(0, 0, 0, 0.1)'
                    },
                    label: {
                        position: 'right',
                        formatter: '{b}',
                        fontSize: 10
                    },
                    lineStyle: {
                        color: 'source',
                        curveness: 0.3
                    },
                    emphasis: {
                        lineStyle: {
                            width: 5
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
                        fontSize: 7
                    }
                }
            ]
        };
    }
}