package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.xinblue.cytoscape.Cytoscape;
import de.xinblue.cytoscape.model.Edge;
import de.xinblue.cytoscape.model.Node;
import de.xinblue.cytoscape.styles.GeneralGraphStyles;
import de.xinblue.cytoscape.styles.GraphStyles;

import java.util.Iterator;

public class ConceptionKindCorrelationInfoChart extends VerticalLayout {

    private void createDashBoardContent() {
        //Create Cytoscape Canvas
        Cytoscape cy=new Cytoscape("ID0001");
        cy.setWidth("100%");
        cy.setHeight("300px");
        cy.addClassName("cy");

        //Add edge handling
        cy.registerStandardEdgeHandling();

        //Create context menu for nodes and edges. Tap on an edge or node (left mouse down for 1 second)
        cy.createContextMenu("node",new String[]{"DeleteNode","EditNode"});
        cy.createContextMenu("edge",new String[]{"ShowEdge"});

        cy.registerEvent("mouseover", "node");
        cy.addCustomEventListener(e -> {
            try {
                JsonNode event = new ObjectMapper().readTree(e.getMessage());

                JsonNode ja=event.get("data");
                Iterator<JsonNode> iterator = ja.iterator();
                while (iterator.hasNext()) {
                    JsonNode js=iterator.next().at("/data/id");
                    String id=js.asText();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        cy.addDragListener(e -> {
            try {
                JsonNode dragObject = new ObjectMapper().readTree(e.getMessage());
                Node node=new Node();
                node.getPosition().put("x", dragObject.get("dragXCoordinate").asInt());
                node.getPosition().put("y", dragObject.get("dragYCoordinate").asInt());
                node.getData().put("type", dragObject.get("dragObject").asText());
                cy.addNode(node);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        DropTarget<Div> dropTarget = DropTarget.create(cy);

        dropTarget.addDropListener(event -> {
            event.getDragData().ifPresent(data -> {

            });
        });

        //Define an array of styles for different selectors
        GraphStyles gs=new GraphStyles();

        //Define the style for nodes
        GeneralGraphStyles node=new GeneralGraphStyles.Builder().background_color("#c00").label("data(id)").build();
        gs.addStyle("node", node);

        //Define the style for edges
        GeneralGraphStyles edge=new GeneralGraphStyles.Builder().width("2").line_color("#00c").line_style("dotted").build();
        gs.addStyle("edge", edge);

        //Define the style for selected nodes
        GeneralGraphStyles node_selected=new GeneralGraphStyles.Builder().background_color("#0c0").build();
        gs.addStyle("node:selected", node_selected);

        //Define the style for edgeHandling
        GeneralGraphStyles eh_handle=new GeneralGraphStyles.Builder().background_color("#00C").width("8").height("8").text_opacity("0").build();
        gs.addStyle(".eh-handle", eh_handle);

        //Set the styles
        cy.loadStyle(gs.getJson(true));

        //Load demo graph
        cy.loadGraph(cy.getDemoGraph2());
        this.add(cy);
    }

    public ConceptionKindCorrelationInfoChart() {
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        createDashBoardContent();
    }
}
