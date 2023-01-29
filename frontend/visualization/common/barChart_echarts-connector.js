window.Vaadin.Flow.common_BarChart_echarts = {
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
            setCenter:function(centerArray){
                c.$connector.option.series[0].center = centerArray;
            },
            enableRightLegend:function(command){
                c.$connector.option.legend = {
                    show:true,
                    type:'scroll',
                    orient: 'vertical',
                    right: 'right'
                };
            },
            enableLeftLegend:function(command){
                c.$connector.option.legend = {
                    show:true,
                    type:'scroll',
                    orient: 'vertical',
                    left: 'left'
                };
            },
            enableTopLegend:function(command){
                c.$connector.option.legend = {
                    show:true,
                    type:'scroll',
                    orient: 'horizontal',
                    top: 'top'
                };
            },
            enableBottomLegend:function(command){
                c.$connector.option.legend = {
                    show:true,
                    type:'scroll',
                    orient: 'horizontal',
                    bottom: 'bottom'
                };
            },
            setData: function (data) {
                c.$connector.option.xAxis.data = data.category;
                c.$connector.option.series[0].data = data.value;
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            }
        };
        c.$connector.myChart = echarts.init(c);
        c.$connector.option = {
            color:["#03a9f4","#76b852","#00d1b2","#ced7df","#ee4f4f","#0288d1","#ffc107","#d32f2f","#168eea","#323b43","#59626a"],
            tooltip: {
                trigger: 'item',
                confine:true,
                show:true,
                showContent:true,
                textStyle: {
                    fontSize: 12
                },
                formatter: '{b0}<br/> <b>{c0}</b>'
            },
            grid: {
                //left: '3%',
                right: '20%',
                //bottom: '3%',
                top:'2%',
                containLabel: false
            },
            xAxis: {
                type: 'category',
                axisLabel:{
                    show:true,
                    inside:false,
                    rotate:30,
                    fontSize:8
                }
            },
            yAxis: {
                type: 'value',
                position:'right',
                max: function (value) {
                    return value.max+100;
                },
                axisLabel:{
                    show:true,
                    inside:false,
                    fontSize:10
                }
            },
            series: [
                {
                    type: 'bar'
                }
            ]
        };
    }
}