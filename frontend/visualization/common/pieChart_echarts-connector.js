window.Vaadin.Flow.common_PieChart_echarts = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setColor:function(colorArray){
                c.$connector.option.color = colorArray;
            },
            setRadius:function(radiusValue){
                c.$connector.option.series[0].radius = radiusValue;
            },
            setData: function (data) {
                /*
                c.$connector.option.series[0].data = [
                    { value: 1048, name: 'Search Engine' },
                    { value: 735, name: 'Direct' },
                    { value: 580, name: 'Email' },
                    { value: 484, name: 'Union Ads' },
                    { value: 300, name: 'Video Ads' }
                ];
                */
                c.$connector.option.series[0].data = data;
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            }
        };
        c.$connector.myChart = echarts.init(c);
        c.$connector.option = {
            //color:["#03a9f4","#76b852","#00d1b2","#ced7df","#ee4f4f","#0288d1","#ffc107","#d32f2f","#168eea","#323b43","#59626a"],
            title: {
                //text: 'title text',
                //subtext: 'subtext',
                //left: 'left'
            },
            tooltip: {
                trigger: 'item',
                confine:true,
                show:true,
                showContent:true,
                textStyle: {
                    fontSize: 12
                },
                formatter: '{b0}<br/> <b>{c0}</b>  -  {d}%'
            },
            /*
            legend: {
                //orient: 'vertical',
                //left: 'left'
            },
            */
            series: [
                {
                    type: 'pie',
                    //radius: '70%',
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        scale:true,
                        scaleSize:5
                    }
                }
            ]
        };
    }
}