window.Vaadin.Flow.plotly_3DPointClusteringChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            //// functions
            setData : function(data) {
                this.chart.data = data;
            }
        };

        Plotly.d3.csv('https://raw.githubusercontent.com/plotly/datasets/master/alpha_shape.csv', function(err, rows){

            function unpack(rows, key) {
                return rows.map(function(row) { return row[key]; });
            }

            var data = [{
                x: unpack(rows, 'x'),
                y: unpack(rows, 'y'),
                z: unpack(rows, 'z'),
                mode: 'markers',
                type: 'scatter3d',
                marker: {
                    color: 'rgb(23, 190, 207)',
                    size: 2
                }
            },{
                alphahull: 7,
                opacity: 0.1,
                type: 'mesh3d',
                x: unpack(rows, 'x'),
                y: unpack(rows, 'y'),
                z: unpack(rows, 'z')
            }];

            var layout = {
                autosize: true,
                height: 800,
                scene: {
                    aspectratio: {
                        x: 1,
                        y: 1,
                        z: 1
                    },
                    camera: {
                        center: {
                            x: 0,
                            y: 0,
                            z: 0
                        },
                        eye: {
                            x: 1.25,
                            y: 1.25,
                            z: 1.25
                        },
                        up: {
                            x: 0,
                            y: 0,
                            z: 1
                        }
                    },
                    xaxis: {
                        type: 'linear',
                        zeroline: false
                    },
                    yaxis: {
                        type: 'linear',
                        zeroline: false
                    },
                    zaxis: {
                        type: 'linear',
                        zeroline: false
                    }
                },
                //title: '3d point clustering',
                width: 1500
            };

            Plotly.newPlot(c, data, layout);

        });
    }
}