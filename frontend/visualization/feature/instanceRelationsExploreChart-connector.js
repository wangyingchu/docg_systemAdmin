window.Vaadin.Flow.feature_InstanceRelationsExploreChartConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }
        c.$connector = {
            // functions
            setData : function(data) {
                let nodesArray = data.nodes;
                let edgesArray = data.edges;
                let focusDataRid = data.focusDataRID;

                nodeDataRelationRetrievePageIndexMapping[focusDataRid] = 1;

                for(let nodeArrayIdx in nodesArray){
                    let currentNodeData = nodesArray[nodeArrayIdx];

                    if(!checkInitNodeExistence(currentNodeData.dataRID)){
                        let nodeShape = 'dot';
                        if(currentNodeData.dataClassify == 'DIMENSION'){
                            nodeShape = 'hexagon';
                        }
                        if(currentNodeData.dataRID == focusDataRid){
                            nodeShape = 'diamond';
                        }
                        let currentNodeColor = retrieveTypeColor(currentNodeData.dataTypeName);

                        nodeData.push({
                            id: currentNodeData.dataRID,
                            title: "类型名称："+currentNodeData.dataTypeName+'<br/> 类型描述：'+currentNodeData.dataTypeDesc+'<br/> 对象 RID：'+currentNodeData.dataRID,
                            label: currentNodeData.dataTypeName +' ['+ currentNodeData.dataRID+']',
                            shape: nodeShape,
                            color: { background: currentNodeColor },
                            chosen: { label: false, node: changeChosenNodeSize }
                        });

                        if(currentNodeData.dataRID ==focusDataRid){
                            nodeData[nodeData.length-1].color = { background: "#444444"};
                            nodeData[nodeData.length-1].value = 10000;
                            nodeData[nodeData.length-1].x = 0;
                            nodeData[nodeData.length-1].y = 0;
                            nodeData[nodeData.length-1].font = { multi: true};
                            nodeData[nodeData.length-1].label = '<b>'+currentNodeData.dataTypeName+'</b> ['+currentNodeData.dataRID+']\n<i>'+currentNodeData.dataTypeDesc+'</i>';
                        }
                    }
                };
                c.$connector.nodes.add(nodeData);

                for(let edgeArrayIdx in edgesArray){
                    let currentEdgeData = edgesArray[edgeArrayIdx];
                    if(!checkInitEdgeExistence(currentEdgeData.relationRID)){
                        edgeData.push({
                            id:currentEdgeData.relationRID,
                            from: currentEdgeData.sourceDataRID,
                            to: currentEdgeData.targetDataRID,
                            arrows:'to',
                            title: "类型名称："+currentEdgeData.relationTypeName +
                                '<br/> 类型描述：'+currentEdgeData.relationTypeDesc+
                                '<br/> 关系 RID：'+currentEdgeData.relationRID +
                                '<br/> 源数据对象 RID：'+currentEdgeData.sourceDataRID +
                                '<br/> 目标数据对象 RID：'+currentEdgeData.targetDataRID ,
                            label:currentEdgeData.relationTypeName+' ['+ currentEdgeData.relationRID+']',
                            font: { align: 'middle'},
                            chosen: { label: false, edge: changeChosenEdgeWidthSize}
                        });
                    }
                };
                c.$connector.edges.add(edgeData);
            },
            addData : function(data) {
                let nodesArray = data.nodes;
                let edgesArray = data.edges;

                for(let nodeArrayIdx in nodesArray){
                    let currentNodeData = nodesArray[nodeArrayIdx];

                    if(!checkNetworkNodeDataExistence(currentNodeData.dataRID)){
                        let nodeShape = 'dot';
                        if(currentNodeData.dataClassify == 'DIMENSION'){
                            nodeShape = 'hexagon';
                        }
                        let currentNodeColor = retrieveTypeColor(currentNodeData.dataTypeName);
                        let positionObj = c.$connector.network.getPositions([currentSelectedDataRID]);
                        c.$connector.nodes.add({
                            id: currentNodeData.dataRID,
                            title: "类型名称："+currentNodeData.dataTypeName+'<br/> 类型描述：'+currentNodeData.dataTypeDesc+'<br/> 对象 RID：'+currentNodeData.dataRID,
                            label: currentNodeData.dataTypeName +' ['+ currentNodeData.dataRID+']',
                            shape: nodeShape,
                            color: { background: currentNodeColor },
                            chosen: { label: false, node: changeChosenNodeSize },
                            x:positionObj[currentSelectedDataRID].x,
                            y:positionObj[currentSelectedDataRID].y
                        });
                    }
                };

                for(let edgeArrayIdx in edgesArray){
                    let currentEdgeData = edgesArray[edgeArrayIdx];
                    if(!checkNetworkEdgeDataExistence(currentEdgeData.relationRID)){
                        c.$connector.edges.add({
                            id:currentEdgeData.relationRID,
                            from: currentEdgeData.sourceDataRID,
                            to: currentEdgeData.targetDataRID,
                            arrows:'to',
                            title: "类型名称："+currentEdgeData.relationTypeName +
                                '<br/> 类型描述：'+currentEdgeData.relationTypeDesc+
                                '<br/> 关系 RID：'+currentEdgeData.relationRID +
                                '<br/> 源数据对象 RID：'+currentEdgeData.sourceDataRID +
                                '<br/> 目标数据对象 RID：'+currentEdgeData.targetDataRID ,
                            label:currentEdgeData.relationTypeName+' ['+ currentEdgeData.relationRID+']',
                            font: { align: 'middle'},
                            chosen: { label: false, edge: changeChosenEdgeWidthSize}
                        });
                    }
                };
            }
        };

        let dataTypeList=[];
        let nodeData = [];
        let edgeData = [];
        let currentSelectedDataRID;
        let nodeDataRelationRetrievePageIndexMapping = [];

        function checkInitNodeExistence(nodeId){
            let nodeExistenceResult = false;
            nodeData.forEach(function(value,index,array){
                if(value.id==nodeId){
                    nodeExistenceResult=true;
                    return nodeExistenceResult;
                }
            });
            return nodeExistenceResult;
        }

        function checkInitEdgeExistence(edgeId){
            let existFlag=false;
            edgeData.forEach(function(value,index,array){
                if(value.id==edgeId){
                    existFlag=true;
                    return existFlag;
                }
            });
            return existFlag;
        }

        function checkNetworkNodeDataExistence(nodeDataId){
            let queryResult = c.$connector.nodes.get([nodeDataId]);
            if(queryResult.length>0){
                return true;
            }else{
                return false;
            }
        }

        function checkNetworkEdgeDataExistence(edgeDataId){
            let queryResult = c.$connector.edges.get([edgeDataId]);
            if(queryResult.length>0){
                return true;
            }else{
                return false;
            }
        }

        function retrieveTypeColor(typeName){
            let currentTypeIndex = dataTypeList.indexOf(typeName);
            if(currentTypeIndex == -1){
                dataTypeList.push(typeName);
                currentTypeIndex = dataTypeList.length -1;
            }
            return getCurrentGlobalColor(currentTypeIndex);
        }

        const changeChosenEdgeWidthSize =
            function(values, id, selected, hovering) {
                values.width = values.width+3;
            };

        const changeChosenNodeSize =
            function(values, id, selected, hovering) {
                values.size = values.size + 3;
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

        function getCurrentGlobalColor(colorIndex){
            if(colorIndex<globalColorsLoopArray.length){
                return globalColorsLoopArray[colorIndex];
            }else{
                return globalColorsLoopArray[colorIndex%globalColorsLoopArray.length];
            }
        }

        c.$connector.itemPerPage = 20;
        c.$connector.nodes = new vis.DataSet([]);
        c.$connector.edges = new vis.DataSet([]);

        // create a network
        c.$connector.data = {
            nodes: c.$connector.nodes,
            edges: c.$connector.edges
        };
        c.$connector.options = {
            nodes: {
                shape: 'dot',
                size: 10,
                font: {
                    size: 11
                },
                color: {
                    border: '#dddddd',
                    background: '#666666',
                    highlight:{background:'#222222',border:'#dddddd'},
                    hover:{background:'#444444',border:'#dddddd'}
                },
                borderWidth: 1,
                shadow:true
            },
            edges: {
                width: 1,
                shadow:false,
                color: '#bbbbbb',
                font: {
                    size: 10
                },
                /* method1 */
                smooth: {
                    type: "discrete",
                    forceDirection: "none",
                    roundness: 0
                }
                /* method2 */
                /*
               smooth: {
                   type: "vertical",
                   forceDirection: "none"
               }
               */
            },
            /* method1 */
            physics: {
                barnesHut: {
                    gravitationalConstant: -15000,
                    centralGravity: 0.1,
                    springLength: 145,
                    damping: 0.6,
                    avoidOverlap: 1
                },
                maxVelocity:50,
                minVelocity:3
            },
            /* method2 */
            /*
            physics:{
                barnesHut:{gravitationalConstant:-30000},
                stabilization: {
                    iterations:1500,
                    fit:true

                },
                minVelocity:5,
                maxVelocity:50,
                adaptiveTimestep:true
            },
            */
            interaction:{hover:true}
        };
        c.$connector.network = new vis.Network(c, c.$connector.data, c.$connector.options);
        /*
        c.$connector.network.on("selectNode", function (params) {
            currentSelectedDataRID = params.nodes[0];
            let newPageIndex = 1;
            if(nodeDataRelationRetrievePageIndexMapping[currentSelectedDataRID] != undefined){
                newPageIndex = nodeDataRelationRetrievePageIndexMapping[currentSelectedDataRID]+1;
            }
            nodeDataRelationRetrievePageIndexMapping[currentSelectedDataRID] = newPageIndex;
            c.$server.clientSideNodeSelectedHandler(params.nodes[0],newPageIndex,c.$connector.itemPerPage);
        });
        */
        c.$connector.network.on("click", function (params) {
            if(params.nodes.length>0){
                currentSelectedDataRID = params.nodes[0];
                let newPageIndex = 1;
                if(nodeDataRelationRetrievePageIndexMapping[currentSelectedDataRID] != undefined){
                    newPageIndex = nodeDataRelationRetrievePageIndexMapping[currentSelectedDataRID]+1;
                }
                nodeDataRelationRetrievePageIndexMapping[currentSelectedDataRID] = newPageIndex;
                c.$server.clientSideNodeSelectedHandler(params.nodes[0],newPageIndex,c.$connector.itemPerPage);
            }
        });
    }
}