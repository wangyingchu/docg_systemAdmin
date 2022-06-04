
//import * as L7 from '@antv/l7';
//import * as GaodeMap from '@antv/l7-maps';

//import { Scene, PointLayer } from '@antv/l7';
//import { GaodeMap } from '@antv/l7-maps';

window.Vaadin.Flow.antv_L7ChartConnector = {
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

        const scene = new L7.Scene({
            id: "SID1234567",
            map: new L7.GaodeMap({
                pitch: 35.210526315789465,
                style: 'dark',
                center: [ 104.288144, 31.239692 ],
                zoom: 4.4
            })
        });

        fetch('https://gw.alipayobjects.com/os/rmsportal/oVTMqfzuuRFKiDwhPSFL.json')
            .then(res => res.json())
            .then(data => {
                const pointLayer = new L7.PointLayer({})
                    .source(data.list, {
                        parser: {
                            type: 'json',
                            x: 'j',
                            y: 'w'
                        }
                    })
                    .shape('cylinder')
                    .size('t', function(level) {
                        return [ 1, 2, level * 2 + 20 ];
                    })
                    .active(true)
                    .color('t', [
                        '#094D4A',
                        '#146968',
                        '#1D7F7E',
                        '#289899',
                        '#34B6B7',
                        '#4AC5AF',
                        '#5FD3A6',
                        '#7BE39E',
                        '#A1EDB8',
                        '#CEF8D6'
                    ])
                    .style({
                        opacity: 1.0
                    });
                scene.addLayer(pointLayer);
            });





    }
}