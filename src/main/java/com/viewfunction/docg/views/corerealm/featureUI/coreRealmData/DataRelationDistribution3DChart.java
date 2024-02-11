package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;

import java.text.NumberFormat;
import java.util.*;

@StyleSheet("webApps/timeFlowCorrelationInfoChart/style.css")
@JavaScript("./visualization/feature/dataRelationDistribution3DChart-connector.js")
public class DataRelationDistribution3DChart extends VerticalLayout {

    private Map<String,String> conceptionKindColorMap;
    private int colorIndex = 0;
    private NumberFormat numberFormat;

    public DataRelationDistribution3DChart(){
        //link to download latest 3d-force-graph build js: https://unpkg.com/3d-force-graph
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/three-spritetext.min.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.73.0/dist/3d-force-graph.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.conceptionKindColorMap = new HashMap<>();
        this.setHeight(100, Unit.PERCENTAGE);
        this.numberFormat = NumberFormat.getInstance();
        initConnector();
    }

    public void setData(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet, Map<String, Long> conceptionKindsDataCount, Map<String, Long> relationKindsDataCount){
        if(conceptionKindsDataCount != null){
            Set<String> conceptionKindNameSet = conceptionKindsDataCount.keySet();
            generateConceptionKindColorMap(conceptionKindNameSet);


        }
        if(conceptionKindCorrelationInfoSet != null){


        }
    }

    private Map<String,String> generateConceptionKindColorMap(Set<String> attachedConceptionKindsSet){
        List<String> attachedConceptionKinds = new ArrayList<String>();
        attachedConceptionKinds.addAll(attachedConceptionKindsSet);

        String[] colorList =new String[]{
                "#EA2027","#006266","#1B1464","#6F1E51","#EE5A24","#009432","##0652DD","#9980FA","#833471",
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

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_DataRelationDistribution3DChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
