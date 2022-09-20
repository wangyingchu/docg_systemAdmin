window.Vaadin.Flow.feature_ConceptionEntitySpatialChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            renderPointEntityContent : function(geoJsonStr) {
                let reformattedGeoJsonStr = geoJsonStr.replaceAll('\\','');
                if(reformattedGeoJsonStr.startsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.replace('"','');
                }
                if(reformattedGeoJsonStr.endsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.split("").reverse().join('').replace('"','').split('').reverse().join('');
                }
                const geoJsonObject = eval("(" + reformattedGeoJsonStr + ")");
                const pointLayer = new L7.PointLayer({})
                    .source(geoJsonObject)
                    .shape('simple')
                    .size(8)
                    .color('#CE0000')
                    .active(true)
                    .style({
                        opacity: 1,
                        strokeWidth: 1,
                        stroke: '#333333',
                    });
                const centerPoint = geoJsonObject.features[0].geometry.coordinates;
                scene.on('loaded', () => {
                    scene.addLayer(pointLayer);
                    scene.setZoomAndCenter(15,centerPoint);
                });
            },
            renderPolygonEntityContent: function(geoJsonStr) {
                let reformattedGeoJsonStr = geoJsonStr.replaceAll('\\','');
                if(reformattedGeoJsonStr.startsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.replace('"','');
                }
                if(reformattedGeoJsonStr.endsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.split("").reverse().join('').replace('"','').split('').reverse().join('');
                }
                reformattedGeoJsonStr = reformattedGeoJsonStr.replaceAll('MultiPolygon','Polygon');
                let geoJsonObject = eval("(" + reformattedGeoJsonStr + ")");


                console.log(geoJsonObject)

                geoJsonObject = {"type": "FeatureCollection",
                    "features": [{ "type": "Feature", "geometry": {"type":"Polygon",
                            "coordinates":[
                                [[-122.2882691893278,47.70625938501865],[-122.2882717548172,47.70627959740429],[-122.28828741146341,47.706301401080225],
                                    [-122.28829308834608,47.70632502451352],[-122.28828898078696,47.70633286501066],[-122.28828055792175,47.706348944552495],
                                    [-122.28827880620925,47.706352288159],[-122.28827837894721,47.706352479276354],[-122.28829178247325,47.70632689115468],
                                    [-122.28828610559096,47.706303267721296],[-122.28827045002362,47.70628146543528],[-122.2882691893278,47.70625938501865]]]}
                    }]
                }

                geoJsonObject = {
                    "type": "Polygon",
                    "coordinates": [
                        [
                            [-170.0, 10.0],
                            [170.0, 10.0],
                            [170.0, -10.0],
                            [-170.0, -10.0],
                            [-170.0, 10.0]
                        ],
                        [
                            [175.0, 5.0],
                            [-175.0, 5.0],
                            [-175.0, -5.0],
                            [175.0, -5.0],
                            [175.0, 5.0]
                        ]
                    ]
                };


                geoJsonObject={"type": "FeatureCollection","crs": { "type": "name", "properties": { "name": "EPSG:4326" } },"features": [{ "type": "Feature", "geometry":
   /*
                            {
            "type": "Polygon",
            "coordinates": [
                [
                    [-170.0, 10.0],
                    [170.0, 10.0],
                    [170.0, -10.0],
                    [-170.0, -10.0],
                    [-170.0, 10.0]
                ],
                [
                    [175.0, 5.0],
                    [-175.0, 5.0],
                    [-175.0, -5.0],
                    [175.0, -5.0],
                    [175.0, 5.0]
                ]
            ]
        }

*/


/*
                        {
                            "type": "MultiPolygon",
                            "coordinates": [
                                [
                                    [
                                        [102.0, 2.0],
                                        [103.0, 2.0],
                                        [103.0, 3.0],
                                        [102.0, 3.0],
                                        [102.0, 2.0]
                                    ]
                                ],
                                [
                                    [
                                        [100.0, 0.0],
                                        [101.0, 0.0],
                                        [101.0, 1.0],
                                        [100.0, 1.0],
                                        [100.0, 0.0]
                                    ],
                                    [
                                        [100.2, 0.2],
                                        [100.8, 0.2],
                                        [100.8, 0.8],
                                        [100.2, 0.8],
                                        [100.2, 0.2]
                                    ]
                                ]
                            ]
                        }
*/



                        {
                            "type": "MultiPolygon",
                            "coordinates": [
                                [
                                    [
                                        //[-122.391,47.691],
                                        //[-122.392,47.692],
                                        //[-122.393,47.693],
                                        //[-122.394,47.694],
                                       // [-122.391,47.691]


                                        /*
                                        [-122.29701312510166, 2.011],
                                        [-123.29701312510166, 2.011],
                                        [-123.29701312510166, 3.011],
                                        [-122.29701312510166, 3.011],
                                        [-122.29701312510166, 2.011]
                                        */

                                        /*
                                        [-122.29701312510166,47.6396932920516],
                                        [-123.29701312510166,47.6396932920516],
                                        [-123.29701312510166,48.6396932920516],
                                        [-122.29701312510166,48.6396932920516],
                                        [-122.29701312510166,47.6396932920516]
                                        */




                                        [-122.29696300204908,47.63979512043487],
                                        [-122.29701312510166,47.6396932920516],
                                        [-122.29798196178889,47.64972948324296],
                                        [-122.29696669789605,47.64975928516926],
                                        [-122.29696300204908,47.63979512043487]







                                    ]
                                ]
                            ]
                        }
                }]}


                scene.on('loaded', () => {
                    const layer = new L7.PolygonLayer({})
                        .source(geoJsonObject)
                        /*
                        .color(
                            'unit_price',
                            [
                                '#1A4397',
                                '#2555B7',
                                '#3165D1',
                                '#467BE8',
                                '#6296FE',
                                '#7EA6F9',
                                '#98B7F7',
                                '#BDD0F8',
                                '#DDE6F7',
                                '#F2F5FC'
                            ].reverse()
                        )
                        */
                        .color('#CE0000')
                        .shape('fill')
                        .active(true)
                        .style({
                            opacity: 1
                        });

                    scene.addLayer(layer);
                });


                /*
                const pointLayer = new L7.PolygonLayer({})
                    .source(geoJsonObject)
                   // .shape('simple')
                  //  .size(8)
                    .color('#CE0000')
                    .active(true)
                    .style({
                        opacity: 1,
                        strokeWidth: 1,
                        stroke: '#333333',
                    });
                //const centerPoint = geoJsonObject.features[0].geometry.coordinates;
                scene.on('loaded', () => {
                    scene.addLayer(pointLayer);
                    //scene.setZoomAndCenter(15,centerPoint);
                });
                */
            },
            renderLineEntityContent: function(geoJsonStr) {
                let reformattedGeoJsonStr = geoJsonStr.replaceAll('\\','');
                if(reformattedGeoJsonStr.startsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.replace('"','');
                }
                if(reformattedGeoJsonStr.endsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.split("").reverse().join('').replace('"','').split('').reverse().join('');
                }
                const geoJsonObject = eval("(" + reformattedGeoJsonStr + ")");

                console.log(geoJsonObject)




                scene.on('loaded', () => {
                    fetch(
                        'https://gw.alipayobjects.com/os/basement_prod/1d27c363-af3a-469e-ab5b-7a7e1ce4f311.json'
                    )
                        .then(res => res.json())
                        .then(data => {
                            const layer = new L7.PolygonLayer({})
                                .source(data)
                                .color(
                                    'unit_price',
                                    [
                                        '#1A4397',
                                        '#2555B7',
                                        '#3165D1',
                                        '#467BE8',
                                        '#6296FE',
                                        '#7EA6F9',
                                        '#98B7F7',
                                        '#BDD0F8',
                                        '#DDE6F7',
                                        '#F2F5FC'
                                    ].reverse()
                                )
                                .shape('fill')
                                .active(true)
                                .style({
                                    opacity: 1
                                });
                            const layer2 = new L7.LineLayer({
                                zIndex: 2
                            })
                                .source(data)
                                .color('#fff')
                                .size(0.8)
                                .style({
                                    opacity: 1
                                });

                            scene.addLayer(layer);
                            scene.addLayer(layer2);
                        });
                });







                /*
                const pointLayer = new L7.LineLayer({})
                    .source(geoJsonObject)
                   // .shape('simple')
                   // .size(8)
                    .color('#CE0000')
                    .active(true)
                    .style({
                        opacity: 1,
                        strokeWidth: 1,
                        stroke: '#333333',
                    });
                //const centerPoint = geoJsonObject.features[0].geometry.coordinates;
                scene.on('loaded', () => {
                    scene.addLayer(pointLayer);
                    //scene.setZoomAndCenter(15,centerPoint);
                });
                */
            }
        };
        const scene = new L7.Scene({
            id: c,
            /*
            map: new L7.GaodeMap({
                style: 'light',
                pitch: 0,
                zoom: 11
            }),
            */
            map: new L7.Mapbox({

                center: [-122.29696300204908,47.63979512043487],

                style: 'light',
                pitch: 0,
                zoom: 11,
                accessToken:'pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw'
            }),

            logoVisible: false
        });

















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