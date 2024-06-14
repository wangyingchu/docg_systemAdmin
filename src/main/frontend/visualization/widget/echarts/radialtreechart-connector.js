window.Vaadin.Flow.eCharts_RadialTreeChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData: function (data) {
                this.option.series[0].data = data;
                if (c.$connector.option && typeof c.$connector.option === "object") {
                    c.$connector.myChart.setOption(c.$connector.option, true);
                }
            },
            setNameLabel: function (nameValue) {
                c.$connector.nameLabel = nameValue;
            },
            setDescLabel: function (descLabel) {
                c.$connector.descLabel = descLabel;
            },
            setValueLabel: function (valueLabel) {
                c.$connector.valueLabel = valueLabel;
            }
        };

        c.$connector.nameLabel = '名称';
        c.$connector.descLabel = '描述';
        c.$connector.valueLabel = '数值';

        c.$connector.myChart = echarts.init(c);

        var data ={};

        c.$connector.option = {
            tooltip: {
                trigger: 'item',
                triggerOn: 'mousemove',
                show: true,

                formatter: function (params) {
                    var labelData = params.data;
                    var res='<div>'+ c.$connector.nameLabel+': '+labelData.name+'</div>'+
                        '<div>'+ c.$connector.descLabel+': '+labelData.desc+'</div>';
                    if(labelData.value) {
                        res = res + '<div>'+ c.$connector.valueLabel+': '+labelData.value+'</div>';
                    }
                    return res;
                }
            },
            series:[
                {
                    leaves: {
                        itemStyle: {
                            borderColor: '#0099FF',
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 0,
                                y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#0099FF' // 0% 处的颜色
                                }, {
                                    offset: 1, color: 'white' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        }
                    },
                    label: {
                        formatter: function (params) {
                            var labelData = params.data;
                            var res=labelData.name;
                            if(labelData.value) {
                                res = res + '('+labelData.value+')';
                            }
                            return res;
                        }
                    },
                    type: 'tree',
                    roam:true,
                    data: [data],
                    top: '18%',
                    bottom: '18%',
                    layout: 'radial',
                    symbol: 'emptyCircle',
                    symbolSize: 8,
                    initialTreeDepth: 1,
                    animationDurationUpdate: 750
                }
            ]
        };
    }
}