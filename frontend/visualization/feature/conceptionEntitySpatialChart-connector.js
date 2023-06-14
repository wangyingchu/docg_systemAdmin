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

            renderPointEntityContent : function(geoJsonStr,conceptionKindName,conceptionEntityUID) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject,conceptionKindName,conceptionEntityUID);
                const pointLocation = geoJsonObject.features[0].geometry.coordinates;
                map.setView([pointLocation[1],pointLocation[0]], 17);
            },

            renderPolygonEntityContent: function(geoJsonStr,conceptionKindName,conceptionEntityUID) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject,conceptionKindName,conceptionEntityUID);
            },

            renderLineEntityContent: function(geoJsonStr,conceptionKindName,conceptionEntityUID) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                c.$connector.renderEntityContent(geoJsonObject,conceptionKindName,conceptionEntityUID);
            },

            renderEntityContent: function(geoJsonObject,conceptionKindName,conceptionEntityUID) {
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
                    let popupContent = '<p> '+ conceptionKindName+' - '+conceptionEntityUID+' ('+feature.geometry.type +')</p>';
                    if (feature.properties && feature.properties.popupContent) {
                        popupContent += feature.properties.popupContent;
                    }
                    layer.bindPopup(popupContent,{closeButton:false});
                }
            },

            renderCentroidPoint : function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
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
                    }
                }).addTo(map);
                const pointLocation = geoJsonObject.features[0].geometry.coordinates;
                map.setView([pointLocation[1],pointLocation[0]], 15);
            },

            renderInteriorPoint : function(geoJsonStr) {
                const geoJsonObject = c.$connector.getGeoJsonObject(geoJsonStr);
                L.geoJSON(geoJsonObject, {
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
        /* access_token doesn't work anymore,so stop use mapbox
        const mbUrl = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
        const grayscale = L.tileLayer(mbUrl, {id: 'mapbox/light-v10'}), streets  = L.tileLayer(mbUrl, {id: 'mapbox/streets-v11'});
        const satellite = L.tileLayer(mbUrl, {id: 'mapbox/satellite-v9', tileSize: 512, zoomOffset: -1});
         let baseLayers = {
            "灰度模式": grayscale,
            "道路模式": streets,
            "卫星模式": satellite
        };
        */

        /* use opencyclemap for most tile layer
          https://www.thunderforest.com/maps/opencyclemap/
        */
        const atlas = L.tileLayer('https://{s}.tile.thunderforest.com/atlas/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const cycle = L.tileLayer('https://{s}.tile.thunderforest.com/cycle/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const transport = L.tileLayer('https://{s}.tile.thunderforest.com/transport/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const transport_dark = L.tileLayer('https://{s}.tile.thunderforest.com/transport-dark/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const landscape = L.tileLayer('https://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const outdoors = L.tileLayer('https://{s}.tile.thunderforest.com/outdoors/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const neighbourhood = L.tileLayer('https://{s}.tile.thunderforest.com/neighbourhood/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const mobile_atlas = L.tileLayer('https://{s}.tile.thunderforest.com/mobile-atlas/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const pioneer = L.tileLayer('https://{s}.tile.thunderforest.com/pioneer/{z}/{x}/{y}@2x.jpg90?apikey=e011577877b94f269e42a09b6905fdb1',
            {maxZoom: 19}
        );
        const osmHOT = L.tileLayer('https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Tiles style by <a href="https://www.hotosm.org/" target="_blank">Humanitarian OpenStreetMap Team</a> hosted by <a href="https://openstreetmap.fr/" target="_blank">OpenStreetMap France</a>'
        });


        //const tdtu = L.tileLayer('http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={x}&TILECOL={y}&tk=8d790e1213b4d044f195e0710f7842eb',
        //    {maxZoom: 19}
       // );
        //https://segmentfault.com/a/1190000021467375?sort=votes
        const tdtu = L.tileLayer('http://t1.tianditu.com/vec_c/wmts?layer=vec&style=default&tilematrixset=c&Service=WMTS&Request=GetTile&Version=1.0.0&Format=tiles&TileMatrix={z}&TileCol={x}&TileRow={y}&tk=8d790e1213b4d044f195e0710f7842eb',
            {maxZoom: 19}
        );

        const map = L.map(c,{
            attributionControl:false,
            layers: [atlas],
            //crs: L.CRS.EPSG4326
            crs: L.CRS.EPSG3857
        });

        const baseLayers = {
            'Atlas': atlas,
            'Cycle': cycle,
            'Transport': transport,
            'Transport Dark': transport_dark,
            'Landscape': landscape,
            'Outdoors': outdoors,
            'Neighbourhood': neighbourhood,
            'Mobile Atlas': mobile_atlas,
            'Pioneer': pioneer,
            'OpenStreetMap': osmHOT,
            'tdtu':tdtu
        };
        L.control.layers(baseLayers).addTo(map);
    }
}