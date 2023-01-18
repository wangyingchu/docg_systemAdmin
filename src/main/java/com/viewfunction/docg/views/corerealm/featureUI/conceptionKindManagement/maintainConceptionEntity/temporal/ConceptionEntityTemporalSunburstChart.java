package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        renderSunburstEntities(
                generateSunburstTemporalDataStructure(timeScaleDataPairList)
        );
    }

    private class TemporalDataEntity{
        private String name;
        private String value;
        private List<TemporalDataEntity> children;

        public TemporalDataEntity(String name,String value){
            this.name = name;
            this.value = value;
        }

        public void addChild(TemporalDataEntity temporalDataEntity){
            if(children == null){
                children = new ArrayList<>();
            }
            children.add(temporalDataEntity);
        }

        public void appendValue(String newValue){
            this.value = this.value +";"+newValue;
        }
    }

    private void renderSunburstEntities(TemporalDataEntity root){

    }

    private TemporalDataEntity generateSunburstTemporalDataStructure(List<TimeScaleDataPair> timeScaleDataPairList){
        TemporalDataEntity rootTemporalDataEntity = new TemporalDataEntity("root",null);
        if(timeScaleDataPairList != null){
            Map<String,TemporalDataEntity> singleTemporalDataEntityMap = new HashMap<>();
            for(TimeScaleDataPair timeScaleDataPair:timeScaleDataPairList){
                TimeScaleEvent timeScaleEvent = timeScaleDataPair.getTimeScaleEvent();

                LocalDateTime referTime = timeScaleEvent.getReferTime();
                TimeFlow.TimeScaleGrade timeScaleGrade = timeScaleEvent.getTimeScaleGrade();
                String eventComment = timeScaleEvent.getEventComment();

                String referTimeString = "";
                switch(timeScaleGrade){
                    case YEAR:
                        referTimeString = ""+referTime.getYear();
                        if(singleTemporalDataEntityMap.containsKey(referTimeString)){
                            singleTemporalDataEntityMap.get(referTimeString).appendValue(eventComment);
                        }else{
                            TemporalDataEntity yearEntity = new TemporalDataEntity(referTimeString,eventComment);
                            singleTemporalDataEntityMap.put(referTimeString,yearEntity);
                            rootTemporalDataEntity.addChild(yearEntity);
                        }
                        break;
                    case MONTH:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonth().getValue();
                        if(singleTemporalDataEntityMap.containsKey(referTimeString)){
                            singleTemporalDataEntityMap.get(referTimeString).appendValue(eventComment);
                        }else{
                            String parentYearString = ""+referTime.getYear();
                            if(!singleTemporalDataEntityMap.containsKey(parentYearString)){
                                TemporalDataEntity yearEntity = new TemporalDataEntity(parentYearString,null);
                                singleTemporalDataEntityMap.put(parentYearString,yearEntity);
                                rootTemporalDataEntity.addChild(yearEntity);
                            }
                            TemporalDataEntity monthEntity = new TemporalDataEntity(referTimeString,eventComment);
                            singleTemporalDataEntityMap.put(referTimeString,monthEntity);
                            singleTemporalDataEntityMap.get(parentYearString).addChild(monthEntity);
                        }
                        break;
                    case DAY:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth();
                        if(singleTemporalDataEntityMap.containsKey(referTimeString)){
                            singleTemporalDataEntityMap.get(referTimeString).appendValue(eventComment);
                        }else{
                            String parentYearString = ""+referTime.getYear();
                            String parentMonthString = ""+referTime.getYear()+"-"+referTime.getMonth().getValue();
                            if(!singleTemporalDataEntityMap.containsKey(parentYearString)){
                                TemporalDataEntity yearEntity = new TemporalDataEntity(parentYearString,null);
                                singleTemporalDataEntityMap.put(parentYearString,yearEntity);
                                rootTemporalDataEntity.addChild(yearEntity);
                            }
                            if(!singleTemporalDataEntityMap.containsKey(parentMonthString)){
                                TemporalDataEntity monthEntity = new TemporalDataEntity(parentMonthString,null);
                                singleTemporalDataEntityMap.put(parentMonthString,monthEntity);
                                singleTemporalDataEntityMap.get(parentYearString).addChild(monthEntity);
                            }
                            TemporalDataEntity dayEntity = new TemporalDataEntity(referTimeString,eventComment);
                            singleTemporalDataEntityMap.put(referTimeString,dayEntity);
                            singleTemporalDataEntityMap.get(parentMonthString).addChild(dayEntity);
                        }
                        break;
                    case HOUR:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour();
                        if(singleTemporalDataEntityMap.containsKey(referTimeString)){
                            singleTemporalDataEntityMap.get(referTimeString).appendValue(eventComment);
                        }else{
                            String parentYearString = ""+referTime.getYear();
                            String parentMonthString = ""+referTime.getYear()+"-"+referTime.getMonth().getValue();
                            String parentDayString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth();
                            if(!singleTemporalDataEntityMap.containsKey(parentYearString)){
                                TemporalDataEntity yearEntity = new TemporalDataEntity(parentYearString,null);
                                singleTemporalDataEntityMap.put(parentYearString,yearEntity);
                                rootTemporalDataEntity.addChild(yearEntity);
                            }
                            if(!singleTemporalDataEntityMap.containsKey(parentMonthString)){
                                TemporalDataEntity monthEntity = new TemporalDataEntity(parentMonthString,null);
                                singleTemporalDataEntityMap.put(parentMonthString,monthEntity);
                                singleTemporalDataEntityMap.get(parentYearString).addChild(monthEntity);
                            }
                            if(!singleTemporalDataEntityMap.containsKey(parentDayString)){
                                TemporalDataEntity dayEntity = new TemporalDataEntity(parentDayString,null);
                                singleTemporalDataEntityMap.put(parentDayString,dayEntity);
                                singleTemporalDataEntityMap.get(parentMonthString).addChild(dayEntity);
                            }
                            TemporalDataEntity hourEntity = new TemporalDataEntity(referTimeString,eventComment);
                            singleTemporalDataEntityMap.put(referTimeString,hourEntity);
                            singleTemporalDataEntityMap.get(parentDayString).addChild(hourEntity);
                        }
                        break;
                    case MINUTE:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour()+":"+referTime.getMinute();
                        if(singleTemporalDataEntityMap.containsKey(referTimeString)){
                            singleTemporalDataEntityMap.get(referTimeString).appendValue(eventComment);
                        }else{
                            String parentYearString = ""+referTime.getYear();
                            String parentMonthString = ""+referTime.getYear()+"-"+referTime.getMonth().getValue();
                            String parentDayString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth();
                            String parentHourString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour();
                            if(!singleTemporalDataEntityMap.containsKey(parentYearString)){
                                TemporalDataEntity yearEntity = new TemporalDataEntity(parentYearString,null);
                                singleTemporalDataEntityMap.put(parentYearString,yearEntity);
                                rootTemporalDataEntity.addChild(yearEntity);
                            }
                            if(!singleTemporalDataEntityMap.containsKey(parentMonthString)){
                                TemporalDataEntity monthEntity = new TemporalDataEntity(parentMonthString,null);
                                singleTemporalDataEntityMap.put(parentMonthString,monthEntity);
                                singleTemporalDataEntityMap.get(parentYearString).addChild(monthEntity);
                            }
                            if(!singleTemporalDataEntityMap.containsKey(parentDayString)){
                                TemporalDataEntity dayEntity = new TemporalDataEntity(parentDayString,null);
                                singleTemporalDataEntityMap.put(parentDayString,dayEntity);
                                singleTemporalDataEntityMap.get(parentMonthString).addChild(dayEntity);
                            }
                            if(!singleTemporalDataEntityMap.containsKey(parentHourString)){
                                TemporalDataEntity hourEntity = new TemporalDataEntity(parentHourString,null);
                                singleTemporalDataEntityMap.put(parentHourString,hourEntity);
                                singleTemporalDataEntityMap.get(parentDayString).addChild(hourEntity);
                            }
                            TemporalDataEntity minuteEntity = new TemporalDataEntity(referTimeString,eventComment);
                            singleTemporalDataEntityMap.put(referTimeString,minuteEntity);
                            singleTemporalDataEntityMap.get(parentHourString).addChild(minuteEntity);
                        }
                        break;
                    case SECOND:
                        referTimeString = ""+referTime.getYear()+"-"+referTime.getMonthValue()+"-"+referTime.getDayOfMonth()+" "+referTime.getHour()+":"+referTime.getMinute()+":"+referTime.getSecond();
                        if(singleTemporalDataEntityMap.containsKey(referTimeString)){
                            singleTemporalDataEntityMap.get(referTimeString).appendValue(eventComment);
                        }
                }
            }
        }
        return rootTemporalDataEntity;
    }
}
