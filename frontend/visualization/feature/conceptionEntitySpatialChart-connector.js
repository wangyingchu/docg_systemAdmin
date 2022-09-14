window.Vaadin.Flow.feature_ConceptionEntitySpatialChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            renderPointEntityContent : function(geoJsonStr) {

                console.log(geoJsonStr);
                console.log(geoJsonStr);
                const geoJsonData =JSON.parse(geoJsonStr);

                console.log(eval("(" + geoJsonStr + ")"));
                    //eval("(" + geoJsonStr + ")")

                console.log(geoJsonData);
                console.log(geoJsonData);
                console.log(geoJsonData);
                console.log(geoJsonData);
                console.log(geoJsonData);
                console.log(geoJsonData);

                const pointLayer = new L7.PointLayer({})
                    .source(eval("(" + geoJsonStr + ")"))
                    .shape('simple')
                    .size(18)
                    .color('#0099FF')
                    .active(true)
                    .style({
                        opacity: 0.9,
                        strokeWidth: 1
                    });
                scene.addLayer(pointLayer);
            }
        };
        const scene = new L7.Scene({
            id: c,
            map: new L7.GaodeMap({
                style: 'light',
                center: [-122.29346695102016,47.615982606514976],
                pitch: 0,
                zoom: 11
            }),
            logoVisible: false
        });
        /*
        scene.on('loaded', () => {
            fetch(
                'https://gw.alipayobjects.com/os/basement_prod/d3564b06-670f-46ea-8edb-842f7010a7c6.json'
            ).then(res => res.json()).then(data => {
                    const pointLayer = new L7.PointLayer({})
                        .source(data)
                        .shape('simple')
                        .size(15)
                        .color('mag', mag => {
                            return mag > 4.5 ? '#5B8FF9' : '#5CCEA1';
                        })
                        .active(true)
                        .style({
                            opacity: 0.6,
                            strokeWidth: 3
                        });
                    scene.addLayer(pointLayer);
            });
        });
        */

/*
            const data = {
                "type": "FeatureCollection",
                //"crs": { "type": "name", "properties": { "name": "urn:ogc:def:crs:OGC:1.3:CRS84" } },
                "crs": { "type": "name", "properties": { "name": "EPSG:4326" } },
                "features": [
                    { "type": "Feature", "properties": { "id": "ak16821829", "mag": 1.5, "time": 1504834613166, "felt": null, "tsunami": 0 }, "geometry": { "type": "Point", "coordinates": [ 123.230989, 41.721006, 6.1 ] } },
                    { "type": "Feature", "properties": { "id": "hv61900626", "mag": 2.91, "time": 1504833891990, "felt": null, "tsunami": 0 }, "geometry": { "type": "Point", "coordinates": [ 121.309668, 31.066417, 2.609 ] } }
                ]
            };
            const pointLayer = new L7.PointLayer({})
                .source(data)
                .shape('simple')
                .size(8)
               // .color('mag', mag => {
               //     return mag > 4.5 ? '#5B8FF9' : '#5CCEA1';
               // })
                .color('#0099FF'
                )
                .active(true)
                .style({
                    opacity: 0.9,
                    strokeWidth: 1
                });
            scene.addLayer(pointLayer);
*/
    }
}