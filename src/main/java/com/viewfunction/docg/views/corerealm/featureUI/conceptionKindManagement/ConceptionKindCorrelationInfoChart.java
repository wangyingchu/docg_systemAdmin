package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;

import de.xinblue.cytoscape.Cytoscape;
import de.xinblue.cytoscape.model.Edge;
import de.xinblue.cytoscape.model.Node;
import de.xinblue.cytoscape.styles.GeneralGraphStyles;
import de.xinblue.cytoscape.styles.GraphStyles;

import java.util.*;

public class ConceptionKindCorrelationInfoChart extends VerticalLayout {

    private Cytoscape cy;
    private List<String> conceptionKindIdList;

    public ConceptionKindCorrelationInfoChart(int chartHeight) {
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(chartHeight,Unit.PIXELS);
        loadContent(chartHeight);
    }

    private void loadContent(int chartHeight) {
        //Create Cytoscape Canvas
        cy = new Cytoscape("ConceptionKindCorrelationInfoChart"+new Date().getTime());
        cy.setWidth("100%");
        cy.setHeight(chartHeight+"px");
        cy.addClassName("cy");

        //Add edge handling
        //cy.registerStandardEdgeHandling();

        //Define an array of styles for different selectors
        GraphStyles gs=new GraphStyles();

        //Define the style for nodes
        GeneralGraphStyles node=new GeneralGraphStyles.Builder()
                .background_color("data(background_color)")
                .color("#444444").shape("data(shape)")
                .font_size("15")
                .font_weight("bold")
                .label("data(id)").build();
        gs.addStyle("node", node);

        //Define the style for edges
        GeneralGraphStyles edge=new GeneralGraphStyles.Builder()
                .width("3")
                .line_color("#CCCCCC")
                .line_style("solid")
                .curve_style("unbundled-bezier")
                .target_arrow_shape("vee")
                .source_arrow_shape("tee")
                .arrow_scale("2")
                .label("data(type)")
                .text_rotation("autorotate")
                .font_size("11")
                .font_family("Georgia")
                .font_weight("bold")
                .color("#555555")
                .build();
        gs.addStyle("edge", edge);

        //Define the style for edgeHandling
        GeneralGraphStyles eh_handle=new GeneralGraphStyles.Builder().background_color("#00C").width("8").height("8").text_opacity("0").build();
        gs.addStyle(".eh-handle", eh_handle);

        //Set the styles
        cy.loadStyle(gs.getJson(true));

        this.add(cy);
        conceptionKindIdList = new ArrayList<>();
    }

    public void loadConceptionKindCorrelationInfo(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet,String targetConceptionKind){
        cy.deleteAll();
        conceptionKindIdList.clear();
        if(conceptionKindCorrelationInfoSet!= null){
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceConceptionKindId = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetConceptionKindId = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                if(!conceptionKindIdList.contains(sourceConceptionKindId)){
                    Node node=new Node();
                    node.getData().put("shape","ellipse");
                    node.getData().put("background_color","#c00");
                    if(targetConceptionKind.equals(sourceConceptionKindId)){
                        node.getData().put("shape","pentagon");
                        node.getData().put("background_color","#777777");
                    }
                    if(sourceConceptionKindId.startsWith("DOCG_")){
                        node.getData().put("shape","diamond");
                        node.getData().put("background_color","#FF8C00");
                    }
                    node.getData().put("id",sourceConceptionKindId);
                    cy.addNode(node);
                    conceptionKindIdList.add(sourceConceptionKindId);
                }
                if(!conceptionKindIdList.contains(targetConceptionKindId)){
                    Node node=new Node();
                    node.getData().put("shape","ellipse");
                    node.getData().put("background_color","#c00");
                    if(targetConceptionKind.equals(targetConceptionKindId)){
                        node.getData().put("shape","pentagon");
                        node.getData().put("background_color","#777777");
                    }
                    if(targetConceptionKindId.startsWith("DOCG_")){
                        node.getData().put("shape","diamond");
                        node.getData().put("background_color","#FF8C00");
                    }
                    node.getData().put("id",targetConceptionKindId);
                    cy.addNode(node);
                    conceptionKindIdList.add(targetConceptionKindId);
                }
                Edge currentEdge=new Edge();
                currentEdge.getData().put("type", currentConceptionKindCorrelationInfo.getRelationKindName());
                currentEdge.getData().put("source", sourceConceptionKindId);
                currentEdge.getData().put("target", targetConceptionKindId);
                cy.addEdge(currentEdge);
            }
            cy.loadLayout("breadthfirst");
        }
    }

    public void clearGraph(){
        cy.deleteAll();
        conceptionKindIdList.clear();
        cy.loadLayout("breadthfirst");
    }
}
