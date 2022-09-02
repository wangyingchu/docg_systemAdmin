package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.List;

@StyleSheet("./visualization/feature/xxx.css")
@JavaScript("./visualization/feature/relatedConceptionEntitiesNebulaGraphChart-connector.js")
public class RelatedConceptionEntitiesNebulaGraphChart extends VerticalLayout {
    private String mainConceptionEntityUID;
    private List<ConceptionEntity> conceptionEntityList;
    private List<RelationEntity> relationEntityList;
    public RelatedConceptionEntitiesNebulaGraphChart(String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList,List<RelationEntity> relationEntityList,int chartHeight){
        this.mainConceptionEntityUID = mainConceptionEntityUID;
        this.conceptionEntityList = conceptionEntityList;
        this.relationEntityList = relationEntityList;
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/three.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/three-spritetext.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/CSS2DRenderer.js");
        UI.getCurrent().getPage().addJavaScript("js/3d-force-graph/1.70.13/dist/3d-force-graph.min.js");
        initConnector();
/*
        IFrame _IFrame = new IFrame();
        _IFrame.getStyle().set("border","0");
        _IFrame.setSrc("https://vasturiano.github.io/3d-force-graph/example/large-graph/");
        _IFrame.setHeight(chartHeight, Unit.PIXELS);
        _IFrame.setWidth(100, Unit.PERCENTAGE);
        add(_IFrame);

        */
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_RelatedConceptionEntitiesNebulaGraphChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
