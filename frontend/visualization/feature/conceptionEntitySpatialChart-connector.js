window.Vaadin.Flow.feature_ConceptionEntitySpatialChart = {
    initLazy: function (c) {

        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {

        };


        console.log(c);

        const scene = new L7.Scene({
            id: c,
            map: new L7.GaodeMap({
                style: 'light',
                center: [110.770672, 34.159869],
                pitch: 45,
            }),
            logoVisible: false
        });


        console.log("=====================================");
        console.log("=====================================");
        console.log("=====================================");
        console.log("=====================================");
        console.log(scene);

        console.log("=====================================");
        console.log("=====================================");
    }
}