package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        renderTimelineEntities(
                generateTimelineTemporalDataStructure(timeScaleDataPairList)
        );
    }

    public void renderTimelineEntities(List<Map> timelineEntities){
        runBeforeClientResponse(ui -> {
            try {
                getElement().callJsFunction("$connector.renderTimelineEntities", new Serializable[]{(new ObjectMapper()).writeValueAsString(timelineEntities)});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<Map> generateTimelineTemporalDataStructure(List<TimeScaleDataPair> timeScaleDataPairList){
        List<Map> timelineDataStructure = new ArrayList<>();
        if(timeScaleDataPairList != null){
            for(int i=0; i<timeScaleDataPairList.size();i++){
                TimeScaleDataPair timeScaleDataPair = timeScaleDataPairList.get(i);
                TimeScaleEvent timeScaleEvent = timeScaleDataPair.getTimeScaleEvent();

                LocalDateTime referTime = timeScaleEvent.getReferTime();
                TimeFlow.TimeScaleGrade timeScaleGrade = timeScaleEvent.getTimeScaleGrade();
                String eventComment = timeScaleEvent.getEventComment();

                String referTimeString = "";
                switch(timeScaleGrade){
                    case YEAR:
                        referTimeString = ""+referTime.getYear()+"-01";
                        break;
                    case MONTH:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonth().getValue();
                        break;
                    case DAY:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth();
                        break;
                    case HOUR:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour()+":00";
                        break;
                    case MINUTE:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour()+":"+referTime.getMinute();
                        break;
                    case SECOND:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour()+":"+referTime.getMinute()+":"+referTime.getSecond();
                }
                HashMap<String,Object> timeScaleData = new HashMap<>();
                timeScaleData.put("id",i+1);
                timeScaleData.put("content",eventComment+"<br/>"+referTimeString+"<br/>"+timeScaleGrade+"-"+timeScaleEvent.getTimeScaleEventUID());
                timeScaleData.put("start",referTimeString);
                timelineDataStructure.add(timeScaleData);
            }
        }
        return timelineDataStructure;
    }

}
