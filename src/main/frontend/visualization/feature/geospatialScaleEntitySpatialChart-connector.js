window.Vaadin.Flow.feature_GeospatialScaleEntitySpatialChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            getGeoJsonObject : function(geoJsonStr){
                let reformattedGeoJsonStr = geoJsonStr.replaceAll('\\','');
                if(reformattedGeoJsonStr.startsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.replace('"','');
                }
                if(reformattedGeoJsonStr.endsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.split("").reverse().join('').replace('"','').split('').reverse().join('');
                }
                const geoJsonObject = eval("(" + reformattedGeoJsonStr + ")");
                return geoJsonObject;
            },

            renderPolygonEntityContent: function(geoJsonStr,entityChineseName,entityCode,entityType) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject,entityChineseName,entityCode,entityType);
            },

            renderEntityContent: function(geoJsonObject,entityChineseName,entityCode,entityType) {
                if(isMapLoaded){
                    c.$connector.innerRenderEntityContent(geoJsonObject,entityChineseName,entityCode,entityType);
                }else{
                    map.on('load', () => {
                        c.$connector. innerRenderEntityContent(geoJsonObject,entityChineseName,entityCode,entityType);
                    });
                }
            },

            innerRenderEntityContent: function(geoJsonObject,entityChineseName,entityCode,entityType) {
                map.addSource(entityCode, {
                    'type': 'geojson',
                    'data': geoJsonObject
                });
                map.addLayer({
                    'id': entityCode+"Layer",
                    'type': 'fill',
                    'source': entityCode,
                    'layout': {},
                    'paint': {
                        'fill-color': '#0079D4',
                        'fill-opacity': 0.65
                    }
                });
                layersIDArray.push(entityCode+"Layer");
                sourcesIDArray.push(entityCode);
                isMapLoaded = true;

                // 添加边框图层
                map.addLayer({
                    id: entityCode+'borderLayer',
                    type: 'line',
                    source: entityCode, // 使用相同的数据源
                    paint: {
                        'line-color': '#001F3F',
                        'line-width': 2,
                        'line-opacity': 0.8
                    }
                });
                layersIDArray.push(entityCode+'borderLayer');

                // When a click event occurs on a feature in the places layer, open a popup at the
                // location of the feature, with description HTML from its properties.
                map.on('click', entityCode+"Layer", (e) => {
                    const coordinates = e.features[0].geometry.coordinates.slice();
                    // Ensure that if the map is zoomed out such that multiple
                    // copies of the feature are visible, the popup appears
                    // over the copy being pointed to.
                    while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                        coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
                    }
                    const popup = new maplibregl.Popup({closeButton: false,closeOnClick: true})
                        .setLngLat(e.lngLat)
                        .setHTML('<p> '+ entityChineseName+' - '+entityCode+' ('+entityType +')</p>')
                        .addTo(map);
                    popupsArray.push(popup);
                });

                // Change the cursor to a pointer when the mouse is over the places layer.
                map.on('mouseenter', entityCode+"Layer", () => {
                    map.getCanvas().style.cursor = 'pointer';
                });

                // Change it back to a pointer when it leaves.
                map.on('mouseleave', entityCode+"Layer", () => {
                    map.getCanvas().style.cursor = '';
                });
            },

            renderCentroidPoint : function(geoJsonStr,zoomLevel) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                if(isMapLoaded){
                    map.addSource('CentroidPointSource', {
                        'type': 'geojson',
                        'data':geoJsonObject
                    });
                    map.addLayer({
                        'id': 'CentroidPoint',
                        'type': 'circle',
                        'source': 'CentroidPointSource',
                        'paint': {
                            'circle-radius': 4,
                            'circle-color': '#CCCCCC',
                            'circle-opacity': 0.5,
                            'circle-stroke-color': '#444444',
                            'circle-stroke-opacity': 0.8,
                            'circle-stroke-width': 1
                        }
                    });
                    layersIDArray.push("CentroidPoint");
                    sourcesIDArray.push("CentroidPointSource");
                }else{
                    map.on('load', () => {
                        map.addSource('CentroidPointSource', {
                            'type': 'geojson',
                            'data':geoJsonObject
                        });
                        map.addLayer({
                            'id': 'CentroidPoint',
                            'type': 'circle',
                            'source': 'CentroidPointSource',
                            'paint': {
                                'circle-radius': 4,
                                'circle-color': '#CCCCCC',
                                'circle-opacity': 0.5,
                                'circle-stroke-color': '#444444',
                                'circle-stroke-opacity': 0.8,
                                'circle-stroke-width': 1
                            }
                        });
                        layersIDArray.push("CentroidPoint");
                        sourcesIDArray.push("CentroidPointSource");
                        isMapLoaded = true;
                    });
                }

                const pointLocation = geoJsonObject.features[0].geometry.coordinates;
                map.flyTo({
                    center: [pointLocation[0], pointLocation[1]],
                    zoom: zoomLevel
                    //,bearing: 90        // Optional: Bearing in degrees (rotation)
                    //,pitch: 45          // Optional: Pitch in degrees (tilt)
                });
            },

            renderInteriorPoint : function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                if(isMapLoaded){
                    // Add a data source for the X markers
                    map.addSource('InteriorPointSource', {
                        'type': 'geojson',
                        'data': geoJsonObject
                    });
                    // Add the X text layer
                    map.addLayer({
                        'id': 'InteriorPoint',
                        'type': 'symbol',
                        'source': 'InteriorPointSource',
                        'layout': {
                            'text-field': '+',  // Unicode multiplication sign (+)
                            'text-size': 10,
                            'text-allow-overlap': true,
                            'text-anchor': 'center',
                            'text-justify': 'center'
                        },
                        'paint': {
                            'text-color': '#CCCCCC',
                            'text-halo-color': '#444444',
                            'text-halo-width': 1,
                            'text-opacity': 0.6
                        }
                    });
                    layersIDArray.push("InteriorPoint");
                    sourcesIDArray.push("InteriorPointSource");
                }else{
                    map.on('load', function() {
                        // Add a data source for the X markers
                        map.addSource('InteriorPointSource', {
                            'type': 'geojson',
                            'data': geoJsonObject
                        });
                        // Add the X text layer
                        map.addLayer({
                            'id': 'InteriorPoint',
                            'type': 'symbol',
                            'source': 'InteriorPointSource',
                            'layout': {
                                'text-field': '+',  // Unicode multiplication sign (X)
                                'text-size': 10,
                                'text-allow-overlap': true,
                                'text-anchor': 'center',
                                'text-justify': 'center'
                            },
                            'paint': {
                                'text-color': '#CCCCCC',
                                'text-halo-color': '#444444',
                                'text-halo-width': 1,
                                'text-opacity': 0.6
                            }
                        });
                        layersIDArray.push("InteriorPoint");
                        sourcesIDArray.push("InteriorPointSource");
                        isMapLoaded = true;
                    });
                }
            },

            renderEnvelope: function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                if(isMapLoaded){
                    map.addSource("EnvelopeSource", {
                        'type': 'geojson',
                        'data': geoJsonObject
                    });
                    map.addLayer({
                        'id': "Envelope-border",
                        'type': 'line',
                        'source': "EnvelopeSource",
                        'layout': {},
                        'paint': {
                            'line-color': '#666666',
                            'line-width': 1,
                            'line-opacity': 0.4,
                            'line-dasharray': [10, 5, 2, 5]
                        }
                    });
                    map.addLayer({
                        'id': "Envelope-fill",
                        'type': 'fill',
                        'source': "EnvelopeSource",
                        'layout': {},
                        'paint': {
                            'fill-color': '#AAAAAA',
                            'fill-opacity': 0.2
                        }
                    });
                    layersIDArray.push("Envelope-border");
                    layersIDArray.push("Envelope-fill");
                    sourcesIDArray.push("EnvelopeSource");
                }else{
                    map.on('load', () => {
                        map.addSource("EnvelopeSource", {
                            'type': 'geojson',
                            'data': geoJsonObject
                        });
                        map.addLayer({
                            'id': "Envelope-border",
                            'type': 'line',
                            'source': "EnvelopeSource",
                            'layout': {},
                            'paint': {
                                'line-color': '#666666',
                                'line-width': 1,
                                'line-opacity': 0.4,
                                'line-dasharray': [10, 5, 2, 5]
                            }
                        });
                        map.addLayer({
                            'id': "Envelope-fill",
                            'type': 'fill',
                            'source': "EnvelopeSource",
                            'layout': {},
                            'paint': {
                                'fill-color': '#AAAAAA',
                                'fill-opacity': 0.2
                            }
                        });
                        layersIDArray.push("Envelope-border");
                        layersIDArray.push("Envelope-fill");
                        sourcesIDArray.push("EnvelopeSource");
                        isMapLoaded = true;
                    });
                }
            },

            clearMap:function(){
                if(isMapLoaded){
                    layersIDArray.forEach(function(layerID){
                        map.removeLayer(layerID);
                    });
                    sourcesIDArray.forEach(function(sourceID){
                        map.removeSource(sourceID);
                    });
                    popupsArray.forEach(popup => popup.remove());

                    layersIDArray.splice(0, layersIDArray.length);
                    sourcesIDArray.splice(0, sourcesIDArray.length);
                    popupsArray.splice(0, popupsArray.length);
                }
            }
        };


        /* Using open free map https://openfreemap.org */
      /*
        const positron = L.maplibreGL({
            style: 'https://tiles.openfreemap.org/styles/positron',
        });
        const bright = L.maplibreGL({
            style: 'https://tiles.openfreemap.org/styles/bright',
        });
        const dark = L.maplibreGL({
            style: 'https://tiles.openfreemap.org/styles/dark',
        });
        const liberty = L.maplibreGL({
            style: 'https://tiles.openfreemap.org/styles/liberty',
        });
        const map = L.map(c,{
            attributionControl:false,
            layers: [liberty],
            //crs: L.CRS.EPSG4326
            crs: L.CRS.EPSG3857
        });

        const baseLayers = {
            'Positron': positron,
            'Bright': bright,
            'Dark': dark,
            'Liberty': liberty
        };
        L.control.layers(baseLayers).addTo(map);

        const assetsLayersArray = [];

*/

        const map = new maplibregl.Map({
            container: c,
            //style: 'https://demotiles.maplibre.org/style.json',
            style:'https://tiles.openfreemap.org/styles/positron',
            //style: 'https://tiles.openfreemap.org/styles/liberty',
            zoom: 5
        });

        // Add zoom and rotation controls to the map.
        map.addControl(new maplibregl.NavigationControl({
            visualizePitch: true,
            visualizeRoll: true,
            showZoom: true,
            showCompass: true
        }));

        const layersIDArray = [];
        const sourcesIDArray = [];
        const popupsArray = [];
        let isMapLoaded = false;
    }
}