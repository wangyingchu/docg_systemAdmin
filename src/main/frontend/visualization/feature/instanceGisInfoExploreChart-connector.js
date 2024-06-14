window.Vaadin.Flow.feature_InstanceGisInfoExploreChart = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(data,coordinateObj,infoObjectType,infoObjectRID) {
                let geoJsonObject =  eval('(' + data + ')');
                const mbUrl = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
                let grayscale = L.tileLayer(mbUrl, {id: 'mapbox/light-v9'}), streets  = L.tileLayer(mbUrl, {id: 'mapbox/streets-v11'});

                let map = L.map(c ,{
                    zoom: 10,
                    maxZoom: 18,
                    layers: [grayscale]
                }).setView([coordinateObj.y, coordinateObj.x], 13);
                let baseLayers = {
                    "Grayscale": grayscale,
                    "Streets": streets
                };

                L.control.layers(baseLayers).addTo(map);

                function onEachFeature(feature, layer) {
                    let popupMessage = "<div>数据实例： "+infoObjectType+" ["+infoObjectRID+"]</div><hr/>"+
                        "<div>实例ID： "+feature.properties.ID+"</div>";
                    if(feature.properties["NAME"]){
                        popupMessage += "<div>实例名称： "+feature.properties["NAME"]+"</div> ";
                    }
                    for (let key in feature.properties) {
                        if(key != "ID" && key !="NAME"){
                            popupMessage += "<div>"+key+"： "+feature.properties[key]+"</div>";
                        }
                    }
                    layer.bindPopup(popupMessage);
                }

                L.geoJSON(geoJsonObject, {
                    style: function (feature) {
                        let geometryType = feature.geometry.type;
                        if(geometryType == "Point"){}
                        if(geometryType == "MultiPoint"){}
                        if(geometryType == "LineString"){
                            let lineStyle ={
                                weight: 2,
                                color: globalColorsLoopArray[1],
                                opacity: 1
                            };
                            return lineStyle;
                        }
                        if(geometryType == "MultiLineString"){
                            let lineStyle ={
                                weight: 2,
                                color: globalColorsLoopArray[1],
                                opacity: 1
                            };
                            return lineStyle;
                        }
                        if(geometryType == "Polygon"){
                            let areaStyle ={
                                weight: 1,
                                color: "#000",
                                opacity: 0.5,
                                fillColor: globalColorsLoopArray[1],
                                fillOpacity: 0.8
                            };
                            return areaStyle;
                        }
                        if(geometryType == "MultiPolygon"){
                            let areaStyle ={
                                weight: 1,
                                color: "#000",
                                opacity: 0.5,
                                fillColor:  globalColorsLoopArray[1],
                                fillOpacity: 0.8
                            };
                            return areaStyle;
                        }
                    },
                    pointToLayer: function (feature, latlng) {
                        return L.circleMarker(latlng, {
                            radius: 8,
                            fillColor: globalColorsLoopArray[1],
                            color: "#000",
                            weight: 1,
                            opacity: 1,
                            fillOpacity: 0.8
                        });
                    },
                    onEachFeature: onEachFeature
                }).addTo(map);
            }
        };

        const globalColorsLoopArray= [
            '#058DC7', '#50B432', '#ED561B', '#24CBE5',
            '#cc241d', '#98971a', '#d79921', '#458588',
            '#b16286', '#689d6a', '#d65d0e', '#665c54',
            '#90ed7d', '#f7a35c', '#8085e9', '#f15c80',
            '#c23531', '#2f4554', '#61a0a8', '#d48265',
            '#e4d354', '#8085e8', '#8d4653', '#91e8e1',
            '#91c7ae', '#749f83', '#ca8622', '#bda29a',
            '#CE0000', '#64E572', '#FF9655', '#FFF263',
            '#6AF9C4', '#EC2500', '#7cb5ec', '#434348',
            '#ECE100', '#EC9800', '#9EDE00', '#DDDF00',
            '#6e7074', '#546570', '#c4ccd3', '#bdae93'
        ];
    }
}