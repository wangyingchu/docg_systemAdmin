package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;

import java.util.List;

@JavaScript("./visualization/feature/conceptionEntityTemporalTimelineChart-connector.js")
public class ConceptionEntityTemporalTimelineChart extends VerticalLayout {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<TimeScaleDataPair> timeScaleDataPairList;
    public ConceptionEntityTemporalTimelineChart(){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidth(100, Unit.PERCENTAGE);
        //link to download latest vis-timeline build js: https://unpkg.com/browse/vis-timeline/
        UI.getCurrent().getPage().addStyleSheet("js/vis/timeline/7.7.0/dist/vis-timeline-graph2d.min.css");
        UI.getCurrent().getPage().addJavaScript("js/vis/timeline/7.7.0/dist/vis-timeline-graph2d.min.js");
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntityTemporalTimelineChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void renderTemporalTimelineInfo(List<TimeScaleDataPair> timeScaleDataPairList,String conceptionKindName, String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.timeScaleDataPairList = timeScaleDataPairList;
        initConnector();
    }
}
