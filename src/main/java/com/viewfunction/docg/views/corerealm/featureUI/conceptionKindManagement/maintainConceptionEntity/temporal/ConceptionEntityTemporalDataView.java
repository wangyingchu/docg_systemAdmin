package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.time.LocalDateTime;
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

        Span name = new Span("Sophia Williams");
        Span email = new Span("sophia.williams@company.com");
        Span phone = new Span("(501) 555-9128");

        VerticalLayout personalInformationLayout = new VerticalLayout(name,email, phone);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);
        personalInformationLayout.setWidth(100, Unit.PERCENTAGE);
        accordion.add("Personal information", personalInformationLayout);

        VerticalLayout personalInformationLayout2 = new VerticalLayout(new Span("aaa"),new Span("aaa"), new Span("aaa"));
        personalInformationLayout2.setSpacing(false);
        personalInformationLayout2.setPadding(false);
        accordion.add("Personal information2", personalInformationLayout2);
        add(accordion);
    }

    public void renderTemporalDataInfo(List<TimeScaleDataPair> timeScaleDataPairList, String conceptionKindName, String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.timeScaleDataPairList = timeScaleDataPairList;

        if(this.timeScaleDataPairList != null && this.timeScaleDataPairList.size() >0){
            for(TimeScaleDataPair currentTimeScaleDataPair :this.timeScaleDataPairList){
                TimeScaleEvent currentTimeScaleEvent = currentTimeScaleDataPair.getTimeScaleEvent();
                TimeScaleEntity currentTimeScaleEntity = currentTimeScaleDataPair.getTimeScaleEntity();


                LocalDateTime localDateTime = currentTimeScaleEvent.getReferTime();
                currentTimeScaleEvent.getTimeScaleEventUID();
                TimeFlow.TimeScaleGrade timeScaleGrade = currentTimeScaleEvent.getTimeScaleGrade();
                currentTimeScaleEvent.getTimeFlowName();

                String referTimeString = "";
                switch(timeScaleGrade){
                    case YEAR:
                        referTimeString = ""+localDateTime.getYear();
                        break;
                    case MONTH:
                        referTimeString = ""+localDateTime.getYear()+"-"+localDateTime.getMonth().getValue();
                        break;
                    case DAY:
                        referTimeString = ""+localDateTime.getYear()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getDayOfMonth();
                        break;
                    case HOUR:break;
                    case MINUTE:break;
                    case SECOND:break;
                }

                String eventTitle = currentTimeScaleEvent.getEventComment()+" "+referTimeString;
                SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.LIST_SELECT.create(), eventTitle);
                AccordionPanel accordionPanel = new AccordionPanel(secondaryIconTitle,new VerticalLayout());
                accordion.add(accordionPanel);
            }
        }
    }
}
