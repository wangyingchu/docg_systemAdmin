package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JavaScript("./visualization/feature/relationConceptionEntitiesPairChart-connector.js")
public class RelationConceptionEntitiesPairChart extends VerticalLayout {

    private List<String> conceptionEntityUIDList;
    private List<String> relationEntityUIDList;
    private String conceptionEntityUID;
    private String conceptionKind;
    private Map<String,String> conceptionKindColorMap;
    private int currentQueryPageSize = 10;
    private Map<String,Integer> targetConceptionEntityRelationCurrentQueryPageMap;
    private int colorIndex = 0;
    private String selectedConceptionEntityUID;
    private String selectedConceptionEntityKind;
    private Multimap<String,String> conception_relationEntityUIDMap;
    private String selectedRelationEntityUID;
    private String selectedRelationEntityKind;

    public RelationConceptionEntitiesPairChart(){

        this.conceptionEntityUIDList = new ArrayList<>();
        this.relationEntityUIDList = new ArrayList<>();
        this.targetConceptionEntityRelationCurrentQueryPageMap = new HashMap<>();
        this.conceptionKindColorMap = new HashMap<>();
        this.conception_relationEntityUIDMap = ArrayListMultimap.create();
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionKind = conceptionKind;
        UI.getCurrent().getPage().addJavaScript("js/cytoscape/3.23.0/dist/cytoscape.min.js");
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_RelationConceptionEntitiesPairChart.initLazy($0)", getElement()));



    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setDate(String relationKind,String relationEntityUID,String relationFromConceptionEntityUID,List<String> fromConceptionKinds,String relationToConceptionEntityUID,List<String> toConceptionKinds){
        add(new Label(relationKind + "window.Vaadin.Flow.feature_RelationConceptionEntitiesPairChart.initLazy($0)"+fromConceptionKinds));

    }
}
