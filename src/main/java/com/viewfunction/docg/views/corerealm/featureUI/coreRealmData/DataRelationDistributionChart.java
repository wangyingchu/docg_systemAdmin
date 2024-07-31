package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.CytoscapeNodePayload;

import java.text.NumberFormat;
import java.util.*;

//https://www.npmjs.com/package/react-cytoscapejs
//https://www.npmjs.com/package/cytoscape
//https://www.npmjs.com/package/cytoscape-cose-bilkent
//https://www.npmjs.com/package/cytoscape-cola
//https://www.npmjs.com/package/cytoscape-euler
//https://www.npmjs.com/package/cytoscape-fcose
@NpmPackage(value = "react-cytoscapejs", version = "2.0.0")
@NpmPackage(value = "cytoscape", version = "3.30.1")
@NpmPackage(value = "cytoscape-cose-bilkent", version = "4.1.0")
@NpmPackage(value = "cytoscape-cola", version = "2.5.1")
@NpmPackage(value = "cytoscape-euler", version = "1.2.2")
@NpmPackage(value = "cytoscape-fcose", version = "2.2.0")
@JsModule("./externalTech/flow/integration/react/dataRelationDistributionChart/data-relation-distribution-chart.tsx")
@Tag("data-relation-distribution-chart")
public class DataRelationDistributionChart extends ReactAdapterComponent {

    private Map<String,String> conceptionKindColorMap;
    private int colorIndex = 0;
    private NumberFormat numberFormat;

    public DataRelationDistributionChart(){
        this.conceptionKindColorMap = new HashMap<>();
        this.numberFormat = NumberFormat.getInstance();
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet, Map<String, Long> conceptionKindsDataCount, Map<String, Long> relationKindsDataCount){
        List<CytoscapeNodePayload> cytoscapeNodePayloadList = new ArrayList<>();
        List<CytoscapeEdgePayload> cytoscapeEdgePayloadList = new ArrayList<>();
        if(conceptionKindsDataCount != null){
            Set<String> conceptionKindNameSet = conceptionKindsDataCount.keySet();
            generateConceptionKindColorMap(conceptionKindNameSet);
            for(String currentConceptionKindName:conceptionKindNameSet){
                if(!currentConceptionKindName.equals(RealmConstant.ConceptionKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.AttributesViewKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.AttributeKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.RelationKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.MetaConfigItemsStorageClass)
                        && !currentConceptionKindName.equals(RealmConstant.ClassificationClass)
                        && !currentConceptionKindName.equals(RealmConstant.TimeScaleEntityClass)
                        && !currentConceptionKindName.equals(RealmConstant.GeospatialScaleEntityClass)
                        && !currentConceptionKindName.equals(RealmConstant.RelationAttachKindClass)
                        && !currentConceptionKindName.equals(RealmConstant.RelationAttachLinkLogicClass)
                ){
                    CytoscapeNodePayload cytoscapeNodePayload =new CytoscapeNodePayload();
                    cytoscapeNodePayloadList.add(cytoscapeNodePayload);
                    cytoscapeNodePayload.getData().put("shape","round-octagon");
                    cytoscapeNodePayload.getData().put("background_color","#c00");
                    cytoscapeNodePayload.getData().put("size", ""+Math.log10(conceptionKindsDataCount.get(currentConceptionKindName)));
                    if(this.conceptionKindColorMap != null && this.conceptionKindColorMap.get(currentConceptionKindName)!=null){
                        cytoscapeNodePayload.getData().put("background_color",this.conceptionKindColorMap.get(currentConceptionKindName));
                    }
                    if(currentConceptionKindName.startsWith("DOCG_TS_")){
                        cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                        cytoscapeNodePayload.getData().put("shape","round-tag");
                        //cytoscapeNodePayload.getData().put("parent","TS");
                    }
                    if(currentConceptionKindName.startsWith("DOCG_GS_")){
                        cytoscapeNodePayload.getData().put("background_color","#C71585");
                        cytoscapeNodePayload.getData().put("shape","round-octagon");
                        //cytoscapeNodePayload.getData().put("parent","GS");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.TimeScaleEventClass)){
                        cytoscapeNodePayload.getData().put("shape","round-diamond");
                        cytoscapeNodePayload.getData().put("size","3");
                        cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.TimeScaleEntityClass)){
                        cytoscapeNodePayload.getData().put("shape","barrel");
                        cytoscapeNodePayload.getData().put("background_color","#40E0D0");
                        //cytoscapeNodePayload.getData().put("parent","TS");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.GeospatialScaleEventClass)){
                        cytoscapeNodePayload.getData().put("shape","round-diamond");
                        cytoscapeNodePayload.getData().put("size","3");
                        cytoscapeNodePayload.getData().put("background_color","#C71585");
                    }
                    if(currentConceptionKindName.startsWith(RealmConstant.GeospatialScaleEntityClass)){
                        cytoscapeNodePayload.getData().put("shape","barrel");
                        cytoscapeNodePayload.getData().put("background_color","#C71585");
                        //cytoscapeNodePayload.getData().put("parent","GS");
                    }
                    cytoscapeNodePayload.getData().put("id",currentConceptionKindName);
                    cytoscapeNodePayload.getData().put("kind",currentConceptionKindName);
                    cytoscapeNodePayload.getData().put("desc",currentConceptionKindName+"\n ( "+this.numberFormat.format(conceptionKindsDataCount.get(currentConceptionKindName))+" )");
                }
            }
        }
        if(conceptionKindCorrelationInfoSet != null){
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceConceptionKindName = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetConceptionKindName = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                String relationKindName = currentConceptionKindCorrelationInfo.getRelationKindName();
                long relationEntityCount = currentConceptionKindCorrelationInfo.getRelationEntityCount();
                if(!relationKindName.equals(RealmConstant.Kind_MetaConfigItemsStorageRelationClass)
                        && !relationKindName.equals(RealmConstant.ConceptionKind_AttributesViewKindRelationClass)
                        && !relationKindName.equals(RealmConstant.AttributesViewKind_AttributeKindRelationClass)
                        && !relationKindName.equals(RealmConstant.Classification_ClassificationRelationClass)
                        && !relationKindName.equals(RealmConstant.RelationAttachKind_RelationAttachLinkLogicRelationClass)){
                    boolean linkToTGOrClassification = false;
                    if(sourceConceptionKindName.equals(RealmConstant.TimeScaleEntityClass)
                            ||sourceConceptionKindName.equals(RealmConstant.GeospatialScaleEntityClass)
                            ||sourceConceptionKindName.equals(RealmConstant.ClassificationClass)
                            ||targetConceptionKindName.equals(RealmConstant.TimeScaleEntityClass)
                            ||targetConceptionKindName.equals(RealmConstant.GeospatialScaleEntityClass)
                            ||targetConceptionKindName.equals(RealmConstant.ClassificationClass)){
                        linkToTGOrClassification = true;
                    }
                    if(!linkToTGOrClassification){
                        if(!relationKindName.startsWith("DOCG_TS_NextIs") &&
                                !relationKindName.startsWith("DOCG_TS_FirstChildIs") &&
                                !relationKindName.startsWith("DOCG_TS_LastChildIs")){
                            CytoscapeEdgePayload cytoscapeEdgePayload =new CytoscapeEdgePayload();
                            cytoscapeEdgePayloadList.add(cytoscapeEdgePayload);

                            cytoscapeEdgePayload.getData().put("type", relationKindName);
                            cytoscapeEdgePayload.getData().put("source", sourceConceptionKindName);
                            cytoscapeEdgePayload.getData().put("target", targetConceptionKindName);
                            if(relationKindName.startsWith("DOCG_TS")){
                                cytoscapeEdgePayload.getData().put("lineWidth", "0.1");
                                cytoscapeEdgePayload.getData().put("lineColor", "#40E0D0");
                                cytoscapeEdgePayload.getData().put("sourceArrowColor", "#40E0D0");
                                cytoscapeEdgePayload.getData().put("targetArrowColor", "#40E0D0");
                                cytoscapeEdgePayload.getData().put("lineOpacity", "0.6");
                                cytoscapeEdgePayload.getData().put("curveStyle", "segments");
                                cytoscapeEdgePayload.getData().put("lineStyle", "solid");
                            }else if(relationKindName.startsWith("DOCG_GS")){
                                cytoscapeEdgePayload.getData().put("lineWidth", "0.1");
                                cytoscapeEdgePayload.getData().put("lineColor", "#C71585");
                                cytoscapeEdgePayload.getData().put("sourceArrowColor", "#C71585");
                                cytoscapeEdgePayload.getData().put("targetArrowColor", "#C71585");
                                cytoscapeEdgePayload.getData().put("lineOpacity", "0.6");
                                cytoscapeEdgePayload.getData().put("curveStyle", "segments");
                                cytoscapeEdgePayload.getData().put("lineStyle", "solid");
                            }else{
                                cytoscapeEdgePayload.getData().put("lineWidth", "0.2");
                                cytoscapeEdgePayload.getData().put("lineColor", "#AAAAAA");
                                cytoscapeEdgePayload.getData().put("sourceArrowColor", "#AAAAAA");
                                cytoscapeEdgePayload.getData().put("targetArrowColor", "#AAAAAA");
                                cytoscapeEdgePayload.getData().put("lineOpacity", "0.8");
                                cytoscapeEdgePayload.getData().put("curveStyle", "unbundled-bezier");
                                cytoscapeEdgePayload.getData().put("lineStyle", "solid");
                            }
                        }
                    }
                }
            }
        }
        setChartData(cytoscapeNodePayloadList, cytoscapeEdgePayloadList);
    }

    public void setChartData(List<CytoscapeNodePayload> cytoscapeNodePayloadList,List<CytoscapeEdgePayload> cytoscapeEdgePayloadList){
        Map<String,Object>  chartData= new HashMap<>();
        chartData.put("nodes",cytoscapeNodePayloadList);
        chartData.put("edges",cytoscapeEdgePayloadList);
        setState("chartData", chartData);
    }

    public void setChartWidth(int width){
        setState("chartWidth", width);
    }

    public void setChartHeight(int height){
        setState("chartHeight", height);
    }

    private Map<String,String> generateConceptionKindColorMap(Set<String> attachedConceptionKindsSet){
        List<String> attachedConceptionKinds = new ArrayList<String>();
        attachedConceptionKinds.addAll(attachedConceptionKindsSet);

        String[] colorList =new String[]{
                "#EA2027","#006266","#1B1464","#6F1E51","#EE5A24","#009432","#0652DD","#9980FA","#833471",
                "#F79F1F","#A3CB38","#1289A7","#D980FA","#B53471","#FFC312","#C4E538","#12CBC4","#FDA7DF","#ED4C67"
        };

        for(int i=0;i<attachedConceptionKinds.size();i++){
            if(colorIndex>=colorList.length){
                colorIndex = 0;
            }
            String currentConceptionKindName = attachedConceptionKinds.get(i);
            if(!conceptionKindColorMap.containsKey(currentConceptionKindName)){
                conceptionKindColorMap.put(currentConceptionKindName,colorList[colorIndex]);
            }
            colorIndex++;
        }
        return conceptionKindColorMap;
    }
}
