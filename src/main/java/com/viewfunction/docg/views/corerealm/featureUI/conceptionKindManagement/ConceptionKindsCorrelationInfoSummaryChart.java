package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import de.xinblue.cytoscape.Cytoscape;
import de.xinblue.cytoscape.model.Edge;
import de.xinblue.cytoscape.model.Node;
import de.xinblue.cytoscape.styles.GeneralGraphStyles;
import de.xinblue.cytoscape.styles.GraphStyles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ConceptionKindsCorrelationInfoSummaryChart extends VerticalLayout {

    private Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet;
    private List<String> conceptionKindIdList;
    private Cytoscape cy;
    public ConceptionKindsCorrelationInfoSummaryChart(Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet,
                                                      int windowWidth, int windowHeight){
        this.conceptionKindCorrelationInfoSet = conceptionKindCorrelationInfoSet;
        conceptionKindIdList = new ArrayList<>();




    }

    public void loadConceptionKindCorrelationInfo(){

        cy = new Cytoscape("SystemConceptionKindsCorrelationInfoChart"+new Date().getTime());
        cy.setWidth("100%");
        cy.setHeight("900"+"px");
        cy.addClassName("cy");




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
        if(conceptionKindCorrelationInfoSet!= null){
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoSet){
                String sourceConceptionKindId = currentConceptionKindCorrelationInfo.getSourceConceptionKindName();
                String targetConceptionKindId = currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
                if(!conceptionKindIdList.contains(sourceConceptionKindId)){
                    Node node1=new Node();
                    node1.getData().put("shape","ellipse");
                    node1.getData().put("background_color","#c00");
                    node1.getData().put("shape","pentagon");
                    node1.getData().put("background_color","#777777");
                    if(sourceConceptionKindId.startsWith("DOCG_")){
                        node1.getData().put("shape","diamond");
                        node1.getData().put("background_color","#FF8C00");
                    }
                    node1.getData().put("id",sourceConceptionKindId);
                    cy.addNode(node1);
                    conceptionKindIdList.add(sourceConceptionKindId);
                }
                if(!conceptionKindIdList.contains(targetConceptionKindId)){
                    Node node2=new Node();
                    node2.getData().put("shape","ellipse");
                    node2.getData().put("background_color","#c00");
                    node2.getData().put("shape","pentagon");
                    node2.getData().put("background_color","#777777");
                    if(targetConceptionKindId.startsWith("DOCG_")){
                        node2.getData().put("shape","diamond");
                        node2.getData().put("background_color","#FF8C00");
                    }
                    node2.getData().put("id",targetConceptionKindId);
                    cy.addNode(node2);
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadConceptionKindCorrelationInfo();
    }
}
