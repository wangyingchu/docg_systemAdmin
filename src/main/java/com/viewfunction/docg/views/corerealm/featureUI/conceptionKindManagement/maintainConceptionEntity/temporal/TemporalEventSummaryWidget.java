package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemporalEventSummaryWidget extends HorizontalLayout {

    private TimeScaleEvent timeScaleEvent;
    private TimeScaleEntity timeScaleEntity;

    public TemporalEventSummaryWidget(TimeScaleEvent timeScaleEvent, TimeScaleEntity timeScaleEntity){
        this.timeScaleEvent = timeScaleEvent;
        this.timeScaleEntity = timeScaleEntity;

        LocalDateTime localDateTime = this.timeScaleEvent.getReferTime();
        String eventUID = this.timeScaleEvent.getTimeScaleEventUID();
        TimeFlow.TimeScaleGrade timeScaleGrade = this.timeScaleEvent.getTimeScaleGrade();
        String timeFlowName = this.timeScaleEvent.getTimeFlowName();

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
            case HOUR:
                referTimeString = ""+localDateTime.getYear()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getDayOfMonth()+" "+localDateTime.getHour();
                break;
            case MINUTE:
                referTimeString = ""+localDateTime.getYear()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getDayOfMonth()+" "+localDateTime.getHour()+":"+localDateTime.getMinute();
                break;
            case SECOND:
                referTimeString = ""+localDateTime.getYear()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getDayOfMonth()+" "+localDateTime.getHour()+":"+localDateTime.getMinute()+":"+localDateTime.getSecond();
        }

        HorizontalLayout dateInfoContainer = new HorizontalLayout();
        dateInfoContainer.setSpacing(false);
        dateInfoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(dateInfoContainer);

        Icon summaryIcon = VaadinIcon.CALENDAR.create();
        summaryIcon.setSize("14px");
        summaryIcon.getStyle().set("padding-right","5px");
        dateInfoContainer.add(summaryIcon);

        Label temporalTextLabel = new Label(referTimeString);
        temporalTextLabel.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(temporalTextLabel);

        Icon divIcon1 = VaadinIcon.LINE_V.create();
        divIcon1.setSize("8px");
        dateInfoContainer.add(divIcon1);

        Label timeScaleGradeLabel = new Label(timeScaleGrade.toString());
        timeScaleGradeLabel.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(timeScaleGradeLabel);

        Label eventTitleLabel = new Label(this.timeScaleEvent.getEventComment());
        add(eventTitleLabel);

        Button showEventButton = new Button("事件详情");
        showEventButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showEventButton.getStyle().set("font-size","12px");
        showEventButton.setIcon(VaadinIcon.EYE.create());
        Tooltips.getCurrent().setTooltip(showEventButton, "显示关联时间序列事件详情");
        showEventButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedEventEntityUI();
            }
        });
        add(showEventButton);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private void renderRelatedEventEntityUI(){
        String targetConceptionKind = RealmConstant.TimeScaleEventClass;
        String targetConceptionEntityUID = this.timeScaleEvent.getTimeScaleEventUID();

        List<Component> actionComponentList = new ArrayList<>();
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,targetConceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,targetConceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        actionComponentList.add(entityInfoFootprintMessageBar);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.CALENDAR),"时间关联事件详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }
}
