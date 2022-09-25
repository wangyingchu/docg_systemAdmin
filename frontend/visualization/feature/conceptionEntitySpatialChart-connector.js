window.Vaadin.Flow.feature_ConceptionEntitySpatialChart = {
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

            renderPointEntityContent : function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject);
                const pointLocation = geoJsonObject.features[0].geometry.coordinates;
                map.setView([pointLocation[1],pointLocation[0]], 17);
            },

            renderPolygonEntityContent: function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject);
            },

            renderLineEntityContent: function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject);
            },

            renderEntityContent: function(geoJsonObject) {
                const geoStyle = {
                    "color": '#003472',
                    "weight": 2,
                    "opacity": 0.9,
                    "fillColor": '#1685a9',
                    "fillOpacity": 0.65
                };
                L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            radius: 6,
                            fillColor: '#1685a9',
                            color: "#003472",
                            weight: 1,
                            opacity: 0.9,
                            fillOpacity: 0.65
                        });
                    },
                    style:geoStyle,
                    onEachFeature: onEachFeature
                }).addTo(map);

                function onEachFeature(feature, layer) {
                    let popupContent = '<p> '+ feature.geometry.type +'</p>';
                    if (feature.properties && feature.properties.popupContent) {
                        popupContent += feature.properties.popupContent;
                    }
                    layer.bindPopup(popupContent,{closeButton:false});
                }
            },
            renderCentroidPoint : function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                const geoStyle = {
                    "dashArray": 5,
                    "color": '#666666',
                    "weight": 1,
                    "opacity": 0.9,
                    "fillColor": '#AAAAAA',
                    "fillOpacity": 0.7
                };
                L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            dashArray: 5,
                            radius: 3,
                            fillColor: '#AAAAAA',
                            color: "#666666",
                            weight: 1,
                            opacity: 0.8,
                            fillOpacity: 0.5
                        });
                    },
                    style:geoStyle
                }).addTo(map);
                const pointLocation = geoJsonObject.features[0].geometry.coordinates;
                map.setView([pointLocation[1],pointLocation[0]], 15);
            },
            renderEnvelope: function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                const geoStyle = {
                    "dashArray": 5,
                    "color": '#666666',
                    "weight": 1,
                    "opacity": 0.4,
                    "fillColor": '#AAAAAA',
                    "fillOpacity": 0.2
                };

                L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            radius: 6,
                            fillColor: '#1685a9',
                            color: "#003472",
                            weight: 1,
                            opacity: 0.9,
                            fillOpacity: 0.65
                        });
                    },
                    style:geoStyle
                }).addTo(map);


            }
        };
        const mbUrl = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
        const grayscale = L.tileLayer(mbUrl, {id: 'mapbox/light-v10'}), streets  = L.tileLayer(mbUrl, {id: 'mapbox/streets-v11'});
        const satellite = L.tileLayer(mbUrl, {id: 'mapbox/satellite-v9', tileSize: 512, zoomOffset: -1});
        const map = L.map(c,{
            attributionControl:false,
            //layers: [streets]
            layers: [grayscale]
        });

        let baseLayers = {
            "灰度模式": grayscale,
            "道路模式": streets,
            "卫星模式": satellite
        };
        L.control.layers(baseLayers).addTo(map);
    }
}