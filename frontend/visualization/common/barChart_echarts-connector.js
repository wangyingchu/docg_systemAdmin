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
                console.log(data);
                /*
                c.$connector.option.series[0].data = [
                    { value: 1048, name: 'Search Engine' },
                    { value: 735, name: 'Direct' },
                    { value: 580, name: 'Email' },
                    { value: 484, name: 'Union Ads' },
                    { value: 300, name: 'Video Ads' }
                ];
                */
                c.$connector.option.xAxis.data = data.category;
                c.$connector.option.series[0].data = data.value;
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            }
        };
        c.$connector.myChart = echarts.init(c);
        c.$connector.option = {
            xAxis: {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [120, 200, 150, 80, 70, 110, 130],
                    type: 'bar'
                }
            ]
        };
    }
}