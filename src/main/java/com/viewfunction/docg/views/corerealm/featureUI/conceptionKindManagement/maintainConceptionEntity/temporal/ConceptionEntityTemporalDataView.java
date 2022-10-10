package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.List;

public class ConceptionEntityTemporalDataView extends VerticalLayout {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<TimeScaleDataPair> timeScaleDataPairList;
    private Accordion accordion;

    public ConceptionEntityTemporalDataView(){
        this.getStyle().set("padding-left","50px");
        SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.LIST_SELECT.create(), "关联时间序列事件信息");
        add(secondaryIconTitle);

        accordion = new Accordion();
        accordion.setWidth(100,Unit.PERCENTAGE);
        Scroller scroller = new Scroller(accordion);
        scroller.setWidth(100,Unit.PERCENTAGE);
        add(scroller);
    }

    public void renderTemporalDataInfo(List<TimeScaleDataPair> timeScaleDataPairList, String conceptionKindName, String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.timeScaleDataPairList = timeScaleDataPairList;

        if(this.timeScaleDataPairList != null && this.timeScaleDataPairList.size() >0){
            for(TimeScaleDataPair currentTimeScaleDataPair :this.timeScaleDataPairList){
                TimeScaleEvent currentTimeScaleEvent = currentTimeScaleDataPair.getTimeScaleEvent();
                TimeScaleEntity currentTimeScaleEntity = currentTimeScaleDataPair.getTimeScaleEntity();
                TemporalEventSummaryWidget currentTemporalEventSummaryWidget = new TemporalEventSummaryWidget(currentTimeScaleEvent, currentTimeScaleEntity);
                AccordionPanel accordionPanel = new AccordionPanel(currentTemporalEventSummaryWidget, new VerticalLayout());
                accordion.add(accordionPanel);
            }
        }
    }
}
