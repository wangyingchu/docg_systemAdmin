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
                /*

                const geoStyle = {
                    "color": '#001F3F',
                    "weight": 2,
                    "opacity": 0.8,
                    "fillColor": '#0079D4',
                    "fillOpacity": 0.65
                };

                let contentLayer = L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            radius: 6,
                            fillColor: '#0079D4',
                            color: "#001F3F",
                            weight: 1,
                            opacity: 0.8,
                            fillOpacity: 0.5
                        });
                    },
                    style:geoStyle,
                    onEachFeature: onEachFeature
                }).addTo(map);
                assetsLayersArray.push(contentLayer);

                function onEachFeature(feature, layer) {
                    let popupContent = '<p> '+ entityChineseName+' - '+entityCode+' ('+entityType +')</p>';
                    if (feature.properties && feature.properties.popupContent) {
                        popupContent += feature.properties.popupContent;
                    }
                    layer.bindPopup(popupContent,{closeButton:false});
                }
                */

                if(isMapLoaded){
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

                        new maplibregl.Popup({
                            closeButton: false,
                            closeOnClick: true
                        })
                            .setLngLat(e.lngLat)
                            .setHTML('<p> '+ entityChineseName+' - '+entityCode+' ('+entityType +')</p>')
                            .addTo(map);
                    });

                    // Change the cursor to a pointer when the mouse is over the places layer.
                    map.on('mouseenter', entityCode+"Layer", () => {
                        map.getCanvas().style.cursor = 'pointer';
                    });

                    // Change it back to a pointer when it leaves.
                    map.on('mouseleave', entityCode+"Layer", () => {
                        map.getCanvas().style.cursor = '';
                    });

                }else{
                    map.on('load', () => {
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

                            new maplibregl.Popup({
                                closeButton: false,
                                closeOnClick: true
                            })
                                .setLngLat(e.lngLat)
                                .setHTML('<p> '+ entityChineseName+' - '+entityCode+' ('+entityType +')</p>')
                                .addTo(map);
                        });

                        // Change the cursor to a pointer when the mouse is over the places layer.
                        map.on('mouseenter', entityCode+"Layer", () => {
                            map.getCanvas().style.cursor = 'pointer';
                        });

                        // Change it back to a pointer when it leaves.
                        map.on('mouseleave', entityCode+"Layer", () => {
                            map.getCanvas().style.cursor = '';
                        });

                    });
                }
            },

            innerRenderEntityContent: function(geoJsonObject,entityChineseName,entityCode,entityType) {

            },

            renderCentroidPoint : function(geoJsonStr,zoomLevel) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                /*
                let centroidPointLayer = L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            dashArray: 5,
                            radius: 3,
                            fillColor: '#CCCCCC',
                            color: "#444444",
                            weight: 1,
                            opacity: 0.8,
                            fillOpacity: 0.5
                        });
                    }
                }).addTo(map);
                assetsLayersArray.push(centroidPointLayer);
                */

                const pointLocation = geoJsonObject.features[0].geometry.coordinates;
                map.flyTo({
                    center: [pointLocation[0], pointLocation[1]],
                    zoom: zoomLevel
                    //,bearing: 90        // Optional: Bearing in degrees (rotation)
                    //,pitch: 45           // Optional: Pitch in degrees (tilt)
                });
            },

            renderInteriorPoint : function(geoJsonStr) {
                /*
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                let interiorPointLayer = L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            dashArray: 5,
                            radius: 3,
                            fillColor: '#444444',
                            color: "#000000",
                            weight: 1,
                            opacity: 0.8,
                            fillOpacity: 0.5
                        });
                    }
                }).addTo(map);
                assetsLayersArray.push(interiorPointLayer);
                */

            },

            renderEnvelope: function(geoJsonStr) {
                /*
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                const geoStyle = {
                    "dashArray": 5,
                    "color": '#666666',
                    "weight": 1,
                    "opacity": 0.4,
                    "fillColor": '#AAAAAA',
                    "fillOpacity": 0.2
                };

                let envelopeLayer = L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            radius: 6,
                            fillColor: '#0079D4',
                            color: "#001F3F",
                            weight: 1,
                            opacity: 0.9,
                            fillOpacity: 0.65
                        });
                    },
                    style:geoStyle
                }).addTo(map);
                assetsLayersArray.push(envelopeLayer);
                */
            },

            clearMap:function(){
                if(isMapLoaded){
                    layersIDArray.forEach(function(layerID){
                        map.removeLayer(layerID);
                    });

                    sourcesIDArray.forEach(function(sourceID){
                        map.removeSource(sourceID);
                    });

                    layersIDArray.splice(0, layersIDArray.length);
                    sourcesIDArray.splice(0, sourcesIDArray.length);
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
            style: 'https://tiles.openfreemap.org/styles/liberty',
           // center: [-68.13734351262877, 45.137451890638886],
            zoom: 5
        });

        const layersIDArray = [];
        const sourcesIDArray = [];
        let isMapLoaded = false;
    }
}