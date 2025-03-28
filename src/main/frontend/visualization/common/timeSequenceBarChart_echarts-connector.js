import * as echarts from 'echarts';
window.Vaadin.Flow.common_TimeSequenceBarChart_echarts = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setBarColor:function(barColor){
                c.$connector.option.series[0].itemStyle.color = barColor;
            },
            setPropertyDesc:function(propertyDesc){
                c.$connector.option.series[0].name = propertyDesc;
            },
            setData: function (dataObj) {
                let chartData = [];
                let data = dataObj.data;
                if(data){
                    for(let i=0;i<dataObj.data.length;i++){
                        let xValue = +new Date(data[i]["year"], data[i]["month"], data[i]["day"],data[i]["hour"],data[i]["minute"],data[i]["second"]);
                        chartData[i] = [
                            echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', xValue),
                            data[i]["value"],
                            1
                        ];

                    }
                }
                c.$connector.option.dataset.source = chartData;
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            },
            clearData: function (dataObj){
                let chartData = [];
                c.$connector.option.dataset.source = chartData;
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            }
        };

        c.$connector.myChart = echarts.init(c);
        c.$connector.option;

        const dataCount = 100;
        const data = generateSampleData(dataCount);

        c.$connector.option = {
            dataset: {
                source: data
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'line'
                }
            },
            /*
            toolbox: {
                feature: {
                    dataZoom: {
                        yAxisIndex: false
                    }
                }
            },
            */
            grid: [
                {
                    left: 5,
                    right: 5,
                    bottom: 200
                },
                {
                    left: 5,
                    right: 5,
                    height: 120,
                    bottom: 45
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    // inverse: true,
                    axisLine: { onZero: false },
                    splitLine: { show: false },
                    min: 'dataMin',
                    max: 'dataMax'
                },
                {
                    type: 'category',
                    gridIndex: 1,
                    boundaryGap: false,
                    axisLine: { onZero: false },
                    axisTick: { show: false },
                    splitLine: { show: true },
                    axisLabel: { show: false },
                    min: 'dataMin',
                    max: 'dataMax'
                }
            ],
            yAxis: [
                {
                    scale: true,
                    splitArea: {
                        show: true
                    }
                },
                {
                    scale: true,
                    gridIndex: 1,
                    splitNumber: 2,
                    axisLabel: { show: false },
                    axisLine: { show: false },
                    axisTick: { show: false },
                    splitLine: { show: true }
                }
            ],
            dataZoom: [
                {
                    type: 'inside',
                    xAxisIndex: [0, 1],
                    start: 0,
                    end: 100
                },
                {
                    show: true,
                    xAxisIndex: [0, 1],
                    type: 'slider',
                    bottom: 10,
                    start: 0,
                    end: 100
                }
            ],
            series: [
                {
                    name: '-',
                    type: 'bar',
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    itemStyle: {
                        color: '#0099FF'
                    },
                    large: true,
                    encode: {
                        x: 0,
                        y: 5
                    }
                }
            ]
        };
        function generateSampleData(count) {
            let data = [];
            let xValue = +new Date(2011, 0, 1,6,11,25);
            let minute = 60 * 1000;
            for (let i = 0; i < count; i++) {
                let volumn = 1 * (1000 + Math.random() * 500);
                data[i] = [
                    echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', (xValue += minute)),
                    +volumn.toFixed(0),
                    1
                ];
            }
            return data;
        }
    }
}