window.Vaadin.Flow.common_StackedBarChart_echarts = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
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
            setYAxisCategory: function (data) {
                c.$connector.YAxisCategories = data.category;
                c.$connector.option.yAxis.data =  data.category;
            },
            setDataCategory: function (data) {
                c.$connector.dataCategories = data.category;
            },
            setData: function (data) {
                c.$connector.option.series[c.$connector.seriesIndex] = {
                    name: c.$connector.dataCategories[c.$connector.seriesIndex],
                    type: 'bar',
                    stack: 'total',
                    label: {
                        show: true
                    },
                    emphasis: {
                        focus: 'series'
                    },
                    data: data.value
                };
                c.$connector.seriesIndex++;
            },
            renderChart:function(){
                c.$connector.option && c.$connector.myChart.setOption(c.$connector.option);
            }
        };
        c.$connector.myChart = echarts.init(c);
        c.$connector.seriesIndex = 0;
        c.$connector.YAxisCategories = [];
        c.$connector.dataCategories = [];

        c.$connector.option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    // Use axis to trigger tooltip
                    type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                }
            },
            legend: {},
            grid: {
                //left: '3%',
                //right: '4%',
                //bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value'
            },
            yAxis: {
                type: 'category',
                data:[],
                axisLabel:{
                    show:true,
                    inside:false,
                    rotate:-30,
                    fontSize:6
                }
            },
            series: []
        };
    }
}