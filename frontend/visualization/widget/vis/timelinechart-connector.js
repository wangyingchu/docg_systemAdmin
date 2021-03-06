window.Vaadin.Flow.vis_TimelineChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            //// functions
            setData : function(data) {
                //this.chart.data = data;
            }
        };

        // get selected item count from url parameter
        var count = 1000;

        // create groups
        var groups = new vis.DataSet([
            {id: 1, content: 'Truck&nbsp;1'},
            {id: 2, content: 'Truck&nbsp;2'},
            {id: 3, content: 'Truck&nbsp;3'},
            {id: 4, content: 'Truck&nbsp;4'},
            {id: 5, content: 'Truck&nbsp;5'},
            {id: 6, content: 'Truck&nbsp;6'},
            {id: 7, content: 'Truck&nbsp;7'},
            {id: 8, content: 'Truck&nbsp;8'},
            {id: 9, content: 'Truck&nbsp;9'},
            {id: 10, content: 'Truck&nbsp;10'},
            {id: 11, content: 'Truck&nbsp;11'},
            {id: 12, content: 'Truck&nbsp;12'},
            {id: 13, content: 'Truck&nbsp;13'},
            {id: 14, content: 'Truck&nbsp;14'},
            {id: 15, content: 'Truck&nbsp;15'},
            {id: 16, content: 'Truck&nbsp;16'},
            {id: 17, content: 'Truck&nbsp;17'},
            {id: 18, content: 'Truck&nbsp;18'},
            {id: 19, content: 'Truck&nbsp;19'},
            {id: 20, content: 'Truck&nbsp;20'},
            {id: 21, content: 'Truck&nbsp;21'},
            {id: 22, content: 'Truck&nbsp;22'},
            {id: 23, content: 'Truck&nbsp;23'},
            {id: 24, content: 'Truck&nbsp;24'},
            {id: 25, content: 'Truck&nbsp;25'},
        ]);

        // create items
        var items = new vis.DataSet();

        //var types = [ 'box', 'point', 'range', 'background']
        var types = [ 'point']
        var order = 1;
        var truck = 1;
        for (var j = 0; j < 25; j++) {
            var date = new Date();
            for (var i = 0; i < count/25; i++) {
                date.setHours(date.getHours() +  4 * (Math.random() < 0.2));
                var start = new Date(date);

                date.setHours(date.getHours() + 2 + Math.floor(Math.random()*4));
                var end = new Date(date);

                var type = types[Math.floor(4 * Math.random())]

                items.add({
                    id: order,
                    group: truck,
                    start: start,
                    end: end,
                    type: type,
                    content: 'Order ' + order
                });

                order++;
            }
            truck++;
        }

        // specify options
        var options = {
            stack: true,
            maxHeight: 750,
            start: new Date(),
            end: new Date(1000*60*60*24 + (new Date()).valueOf()),
        };

        // create a Timeline
        //var container = document.getElementById('visualization');
        var timeline = new vis.Timeline(c, null, options);
        timeline.setGroups(groups);
        timeline.setItems(items);
    }
}