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
                },
            renderPolygonEntityContent: function(geoJsonStr) {
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
            }
        };
        const mbUrl = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
        const grayscale = L.tileLayer(mbUrl, {id: 'mapbox/light-v9'}), streets  = L.tileLayer(mbUrl, {id: 'mapbox/streets-v11'});
        const map = L.map(c,{
            attributionControl:false,
            layers: [grayscale]
        }).setView([39.74739, -105], 13);
    }
}