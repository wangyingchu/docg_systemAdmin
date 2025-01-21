import * as echarts from 'echarts';
window.Vaadin.Flow.common_TimeSequenceBarChart_echarts = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {

        };

        c.$connector.myChart = echarts.init(c);
        c.$connector.option;

        const upColor = '#ec0000';
        const upBorderColor = '#8A0000';
        const downColor = '#00da3c';
        const downBorderColor = '#008F28';
        const dataCount = 600;
        const data = generateOHLC(dataCount);
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
                    start: 10,
                    end: 100
                },
                {
                    show: true,
                    xAxisIndex: [0, 1],
                    type: 'slider',
                    bottom: 10,
                    start: 10,
                    end: 100
                }
            ],
            visualMap: {
                show: false,
                seriesIndex: 1,
                dimension: 6,
                pieces: [
                    {
                        value: 1,
                        color: upColor
                    },
                    {
                        value: -1,
                        color: downColor
                    }
                ]
            },
            series: [
                {
                    name: 'Volumn',
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
        function generateOHLC(count) {
            let data = [];
            let xValue = +new Date(2011, 0, 1);
            let minute = 60 * 1000;
            let baseValue = Math.random() * 12000;
            let boxVals = new Array(4);
            let dayRange = 12;
            for (let i = 0; i < count; i++) {
                baseValue = baseValue + Math.random() * 20 - 10;
                for (let j = 0; j < 4; j++) {
                    boxVals[j] = (Math.random() - 0.5) * dayRange + baseValue;
                }
                boxVals.sort();
                let openIdx = Math.round(Math.random() * 3);
                let closeIdx = Math.round(Math.random() * 2);
                if (closeIdx === openIdx) {
                    closeIdx++;
                }
                let volumn = boxVals[3] * (1000 + Math.random() * 500);
                // ['open', 'close', 'lowest', 'highest', 'volumn']
                // [1, 4, 3, 2]
                data[i] = [
                    echarts.format.formatTime('yy/MM/dd\nhh:mm:ss', (xValue += minute)),
                    +boxVals[openIdx].toFixed(2),
                    +boxVals[3].toFixed(2),
                    +boxVals[0].toFixed(2),
                    +boxVals[closeIdx].toFixed(2),
                    +volumn.toFixed(0),
                    getSign(data, i, +boxVals[openIdx], +boxVals[closeIdx], 4) // sign
                ];
            }
            return data;
            function getSign(data, dataIndex, openVal, closeVal, closeDimIdx) {
                var sign;
                if (openVal > closeVal) {
                    sign = -1;
                } else if (openVal < closeVal) {
                    sign = 1;
                } else {
                    sign =
                        dataIndex > 0
                            ? // If close === open, compare with close of last record
                            data[dataIndex - 1][closeDimIdx] <= closeVal
                                ? 1
                                : -1
                            : // No record of previous, set to be positive
                            1;
                }
                return sign;
            }
        }

        c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
    }
}