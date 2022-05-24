package com.viewfunction.docg.data.vo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
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

public class TestView extends Div {

    private VerticalLayout dashboard=new VerticalLayout();
    private HorizontalLayout hl1=new HorizontalLayout();
    private Label output=new Label();
    private Label rawOutput=new Label();
    private VerticalLayout buttonLayout=new VerticalLayout();
    private VerticalLayout nodeList=new VerticalLayout();

    private void createDashboard() {
        H1 headline=new H1("Cytoscape Vaadin Addon");
        dashboard.add(headline);
        Label headlineLabel=new Label("Use the mousewheel to zoom. Drag icons on the canvas to add nodes. Tap (left mosue down) on a node/edge to access the context menu. Click on the edge handle (blue dot on mouse over) to draw edges between nodes.");
        dashboard.add(headlineLabel);

        hl1.setWidth("100%");
        dashboard.add(hl1);
        dashboard.add(output);
        dashboard.add(rawOutput);

        add(dashboard);

    }

    private void setOutput(String s) {
        output.setText(s);
    }

    private void setRawOutput(String s) {
        rawOutput.setText(s);
    }

    private void processContextMenu(Cytoscape cy, String s) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj;
        try {
            actualObj = mapper.readTree(s);
            if (actualObj.get("action").asText().equalsIgnoreCase("editnode") ) {
                setOutput("Contextmenu Edit Node");
            } else if (actualObj.get("action").asText().equalsIgnoreCase("deletenode") ) {
                setOutput("Contextmenu Delete Node");
                cy.deleteNode(actualObj.get("id").asText());
            } else if (actualObj.get("action").asText().equalsIgnoreCase("showedge") ) {
                setOutput("Contextmenu Edit Edge");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    private void createButtonLayout(Cytoscape cy) {


        Button b1=new Button("Load graph");
        b1.addClickListener( e -> {cy.loadGraph(cy.getDemoGraph2());});
        buttonLayout.add(b1);

        Button b2=new Button("Export graph");
        b2.addClickListener( e -> {cy.exportGraph().then(e2 -> {setOutput("Export Graph");setRawOutput(e2.toJson());});});
        buttonLayout.add(b2);

        Button b3=new Button("Center graph");
        b3.addClickListener( e -> {cy.centerGraph();});
        buttonLayout.add(b3);

        Button b4=new Button("Get selected elements");
        b4.addClickListener( e -> {cy.getSelectedElements().then(e2 -> {setOutput("Export Graph");setRawOutput(e2.toJson());});});
        buttonLayout.add(b4);

        Button b5=new Button("Add predefined nodes and edges dynamically");
        b5.addClickListener( e -> {

            Node node1=new Node();
            node1.getPosition().put("x", 100);
            node1.getPosition().put("y", 100);
            node1.getData().put("id", "x1");
            node1.getData().put("myname", "martin");

            Node node2=new Node();
            node2.getPosition().put("x", 200);
            node2.getPosition().put("y", 200);
            node2.getData().put("id", "x2");

            Edge edge1=new Edge();
            edge1.getData().put("id", "x1-x2");
            edge1.getData().put("source", "x1");
            edge1.getData().put("target", "x2");

            cy.addNode(node1).then(e1 -> cy.addNode(node2).then(e3 -> cy.addEdge(edge1)));
        });
        buttonLayout.add(b5);

        Button b6=new Button("Clear all");
        b6.addClickListener( e -> {cy.deleteAll();});
        buttonLayout.add(b6);


        Button b7=new Button("Delete selected nodes");
        b7.addClickListener( e -> {
            cy.getSelectedElements().then(e2 -> {

                setOutput("Delete selected");setRawOutput(e2.toJson());
                String s=e2.toJson();
                try {
                    JsonNode selectedObject = new ObjectMapper().readTree(s);
                    Iterator<JsonNode> iterator = selectedObject.iterator();

                    System.out.println("delete selected");
                    while (iterator.hasNext()) {
                        JsonNode js=iterator.next().at("/data/id");
                        String id=js.asText();
                        cy.deleteNode(id);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
        });
        buttonLayout.add(b7);

        Button b8=new Button("Set element data");
        b8.addClickListener( e -> {cy.getSelectedElements().then(e2 -> {
            String s=e2.toJson();
            try {
                JsonNode selectedObject = new ObjectMapper().readTree(s);
                Iterator<JsonNode> iterator = selectedObject.iterator();
                while (iterator.hasNext()) {
                    JsonNode js=iterator.next().at("/data/id");
                    String id=js.asText();
                    setOutput("Set data of element with id:");setRawOutput(id);
                    cy.setElementData(id,"{\"myname\":\"martin\"}");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        });
        buttonLayout.add(b8);

    }

    private void createNodeList(Cytoscape cy) {

        nodeList.setWidth("200px");
        Icon logo1 = new Icon(VaadinIcon.CIRCLE);
        logo1.setSize("100px");
        logo1.setColor("orange");

        Icon logo2 = new Icon(VaadinIcon.ELASTIC);
        logo2.setSize("100px");
        logo2.setColor("orange");

        nodeList.add(logo1);
        nodeList.add(logo2);

        //Make icons draggable
        DragSource<Icon> box1DragSource = DragSource.create(logo1);
        box1DragSource.setDragData("MyNode1");

        DragSource<Icon> box2DragSource = DragSource.create(logo2);
        box2DragSource.setDragData("MyNode2");

        box1DragSource.addDragStartListener( e -> cy.setDragObject("Node1"));
        box2DragSource.addDragStartListener( e -> cy.setDragObject("Node2"));

    }


    private void createDashBoardContent() {
        //Create Cytoscape Canvas
        Cytoscape cy=new Cytoscape("ID0001");
        cy.setWidth("100%");
        cy.setHeight("600px");
        cy.addClassName("cy");

        //Add edge handling
        cy.registerStandardEdgeHandling();

        //Create context menu for nodes and edges. Tap on an edge or node (left mouse down for 1 second)
        cy.createContextMenu("node",new String[]{"DeleteNode","EditNode"});
        cy.createContextMenu("edge",new String[]{"ShowEdge"});

        cy.addActionListener(e -> {
            setRawOutput("ActionEvent: "+e.getMessage());
            processContextMenu(cy,e.getMessage());

        });

        cy.registerEvent("mouseover", "node");
        cy.addCustomEventListener(e -> {
            try {
                JsonNode event = new ObjectMapper().readTree(e.getMessage());
                setOutput("CustomEvent:");
                setRawOutput(e.getMessage());
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

        cy.addNodeHasBeenAddedEventListener(e -> {
            setOutput("NodeHasBeenAdded Event");
            setRawOutput(e.getMessage());
        });

        cy.addEdgeHasBeenAddedEventListener(e -> {
            setOutput("EdgeHasBeenAdded Event");
            setRawOutput(e.getMessage());
        });

        cy.addEdgeAddedEventListener(e -> {
            setOutput("EdgeAdded Event");
            setRawOutput(e.getMessage());
        });


        cy.addDragListener(e -> {
            try {
                JsonNode dragObject = new ObjectMapper().readTree(e.getMessage());
                Node node=new Node();
                node.getPosition().put("x", dragObject.get("dragXCoordinate").asInt());
                node.getPosition().put("y", dragObject.get("dragYCoordinate").asInt());
                node.getData().put("type", dragObject.get("dragObject").asText());
                cy.addNode(node);
                setOutput("Drag Event");
                setRawOutput(e.getMessage());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        DropTarget<Div> dropTarget = DropTarget.create(cy);

        dropTarget.addDropListener(event -> {
            event.getDragData().ifPresent(data -> {
                setOutput("Drag target");
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

        createButtonLayout(cy);
        createNodeList(cy);
        hl1.add(buttonLayout,nodeList,cy);
    }

    public TestView() {

        createDashboard();
        createDashBoardContent();

    }
}
