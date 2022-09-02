package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@StyleSheet("webApps/relatedConceptionEntitiesNebulaGraphChart/style.css")
@JavaScript("./visualization/feature/relatedConceptionEntitiesNebulaGraphChart-connector.js")
public class RelatedConceptionEntitiesNebulaGraphChart extends VerticalLayout {
    private String mainConceptionEntityUID;
    private List<ConceptionEntity> conceptionEntityList;
    private List<RelationEntity> relationEntityList;
    private Registration listener;
    public RelatedConceptionEntitiesNebulaGraphChart(String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList,List<RelationEntity> relationEntityList){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.mainConceptionEntityUID = mainConceptionEntityUID;
        this.conceptionEntityList = conceptionEntityList;
        this.relationEntityList = relationEntityList;
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/three-spritetext.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/3d-force-graph.min.js");
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_RelatedConceptionEntitiesNebulaGraphChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            generateGraph(receiver.getBodyClientHeight(),receiver.getBodyClientWidth());
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void generateGraph(int height,int width){
        runBeforeClientResponse(ui -> {
            try {
                Map<String,Integer> valueMap =new HashMap<>();
                valueMap.put("graphHeight",height-120);
                valueMap.put("graphWidth",width- 40);
                getElement().callJsFunction("$connector.generateGraph",
                        new Serializable[]{(new ObjectMapper()).writeValueAsString(valueMap)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
