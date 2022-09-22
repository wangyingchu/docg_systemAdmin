window.Vaadin.Flow.feature_ConceptionEntitySpatialChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            renderPointEntityContent : function(geoJsonStr) {
                c.$connector.renderEntityContent(geoJsonStr);
            },
            renderPolygonEntityContent: function(geoJsonStr) {

                /*
                let reformattedGeoJsonStr = geoJsonStr.replaceAll('\\','');
                if(reformattedGeoJsonStr.startsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.replace('"','');
                }
                if(reformattedGeoJsonStr.endsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.split("").reverse().join('').replace('"','').split('').reverse().join('');
                }
                let geoJsonObject = eval("(" + reformattedGeoJsonStr + ")");
                L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.marker(latlng, {icon: baseballIcon});
                    },
                    onEachFeature: onEachFeature
                }).addTo(map);

                function onEachFeature(feature, layer) {
                    let popupContent = '<p>I started out as a GeoJSON ' +
                        feature.geometry.type + ', but now I\'m a Leaflet vector!</p>';

                    if (feature.properties && feature.properties.popupContent) {
                        popupContent += feature.properties.popupContent;
                    }

                    layer.bindPopup(popupContent);
                }
                */
                c.$connector.renderEntityContent(geoJsonStr);

            },
            renderLineEntityContent: function(geoJsonStr) {
                c.$connector.renderEntityContent(geoJsonStr);
            },


            renderEntityContent: function(geoJsonStr) {
                let reformattedGeoJsonStr = geoJsonStr.replaceAll('\\','');
                if(reformattedGeoJsonStr.startsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.replace('"','');
                }
                if(reformattedGeoJsonStr.endsWith('"')){
                    reformattedGeoJsonStr = reformattedGeoJsonStr.split("").reverse().join('').replace('"','').split('').reverse().join('');
                }
                const geoJsonObject = eval("(" + reformattedGeoJsonStr + ")");


                var myStyle = {
                    "color": "#ff7800",
                    "weight": 5,
                    "opacity": 0.65
                };



                L.geoJSON(geoJsonObject, {
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            radius: 8,
                            fillColor: '#749f83',
                            color: "#000",
                            weight: 1,
                            opacity: 1,
                            fillOpacity: 0.8
                        });
                    },
                    style:myStyle,
                    /*
                    style:{weight: 1,
                        color: "#000",
                        opacity: 0.5,
                        fillColor: '#749f83',
                        fillOpacity: 0.8},

                    style: function (feature) {

                        console.log(feature);
                        console.log(feature);
                        console.log(feature);

                        let geometryType = feature.geometry.type;
                        if(geometryType == "Point"){}
                        if(geometryType == "MultiPoint"){}
                        if(geometryType == "LineString"){
                            let lineStyle ={
                                weight: 2,
                                color: '#749f83',
                                opacity: 1
                            };
                            return lineStyle;
                        }
                        if(geometryType == "MultiLineString"){
                            let lineStyle ={
                                weight: 2,
                                color: '#749f83',
                                opacity: 1
                            };
                            return lineStyle;
                        }
                        if(geometryType == "Polygon"){
                            let areaStyle ={
                                weight: 1,
                                color: "#000",
                                opacity: 0.5,
                                fillColor: '#749f83',
                                fillOpacity: 0.8
                            };
                            return areaStyle;
                        }
                        if(geometryType == "MultiPolygon"){
                            let areaStyle ={
                                weight: 1,
                                color: "#000",
                                opacity: 0.5,
                                fillColor:  '#749f83',
                                fillOpacity: 0.8
                            };
                            return areaStyle;
                        }
                    },

*/
                    onEachFeature: onEachFeature
                }).addTo(map);

                function onEachFeature(feature, layer) {
                    let popupContent = '<p>I started out as a GeoJSON ' + feature.geometry.type + ', but now I\'m a Leaflet vector!</p>';
                    if (feature.properties && feature.properties.popupContent) {
                        popupContent += feature.properties.popupContent;
                    }
                    layer.bindPopup(popupContent);
                }

            }


        };
        const mbUrl = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
        const grayscale = L.tileLayer(mbUrl, {id: 'mapbox/light-v10'}), streets  = L.tileLayer(mbUrl, {id: 'mapbox/streets-v11'});
        const satellite = L.tileLayer(mbUrl, {id: 'mapbox/satellite-v9', tileSize: 512, zoomOffset: -1});
        const map = L.map(c,{
            attributionControl:false,
            //layers: [streets]
            layers: [grayscale]
        }).setView([39.74739, -105], 13);

        let baseLayers = {
            "<span style='color: gray'>Grayscale</span>": grayscale,
            "Streets": streets,
            "Satellite": satellite
        };
        L.control.layers(baseLayers).addTo(map);
    }
}