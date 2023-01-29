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
            setTopMargin:function(marginValue){
                c.$connector.option.grid.top = marginValue;
            },
            setLeftMargin:function(marginValue){
                c.$connector.option.grid.left = marginValue;
            },
            setRightMargin:function(marginValue){
                c.$connector.option.grid.right = marginValue;
            },
            setBottomMargin:function(marginValue){
                c.$connector.option.grid.bottom = marginValue;
            },
            setData: function (data) {
                c.$connector.option.xAxis.data = data.category;
                c.$connector.option.series[0].data = data.value;
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            }
        };
        c.$connector.myChart = echarts.init(c);
        c.$connector.option = {
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
                //right: '20%',
                //bottom: '3%',
                //top:'2%',
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
                    return value.max+10;
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