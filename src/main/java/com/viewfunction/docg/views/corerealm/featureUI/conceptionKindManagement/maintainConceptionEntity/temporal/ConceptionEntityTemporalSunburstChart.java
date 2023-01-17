package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.time.LocalDateTime;
import java.util.List;
@JavaScript("./visualization/feature/conceptionEntityTemporalSunburstChart-connector.js")
public class ConceptionEntityTemporalSunburstChart extends VerticalLayout {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<TimeScaleDataPair> timeScaleDataPairList;

    public ConceptionEntityTemporalSunburstChart(){
        this.setWidth(400,Unit.PIXELS);
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.2.29/index.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.2.29/hierarchy.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.2.29/themes/Animated.js");
        SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.PIE_CHART.create(), "关联时间序列统计信息");
        add(secondaryIconTitle);
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_ConceptionEntityTemporalSunburstChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void renderTemporalSunburstInfo(List<TimeScaleDataPair> timeScaleDataPairList, String conceptionKindName, String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.timeScaleDataPairList = timeScaleDataPairList;
        initConnector();
    }

    private void generateSunburstTemporalDataStructure(List<TimeScaleDataPair> timeScaleDataPairList){
        if(timeScaleDataPairList != null){
            for(TimeScaleDataPair timeScaleDataPair:timeScaleDataPairList){
                TimeScaleEntity timeScaleEntity = timeScaleDataPair.getTimeScaleEntity();
                TimeScaleEvent timeScaleEvent = timeScaleDataPair.getTimeScaleEvent();

                LocalDateTime referTime = timeScaleEvent.getReferTime();
                TimeFlow.TimeScaleGrade timeScaleGrade = timeScaleEvent.getTimeScaleGrade();
                String eventComment = timeScaleEvent.getEventComment();

                switch(timeScaleGrade){
                    case YEAR :
                            referTime.getYear(); System.out.println();break;
                    case MONTH : System.out.println();
                    case DAY : System.out.println();
                    case HOUR : System.out.println();
                    case MINUTE : System.out.println();
                    case SECOND : System.out.println();
                }
            }
        }
    }

    private void setTemporalData(){}
}
