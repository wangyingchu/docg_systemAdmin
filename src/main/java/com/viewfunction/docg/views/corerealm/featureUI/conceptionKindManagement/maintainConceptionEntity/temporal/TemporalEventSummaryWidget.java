package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

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
        summaryIcon.setTooltipText(timeFlowName);
        dateInfoContainer.add(summaryIcon);

        NativeLabel temporalTextLabel = new NativeLabel(referTimeString);
        temporalTextLabel.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(temporalTextLabel);

        Icon divIcon1 = VaadinIcon.LINE_V.create();
        divIcon1.setSize("8px");
        dateInfoContainer.add(divIcon1);

        NativeLabel timeScaleGradeLabel = new NativeLabel(timeScaleGrade.toString());
        timeScaleGradeLabel.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(timeScaleGradeLabel);

        NativeLabel eventTitleLabel = new NativeLabel(this.timeScaleEvent.getEventComment());
        add(eventTitleLabel);

        Button showEventButton = new Button("事件详情");
        showEventButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showEventButton.getStyle().set("font-size","12px");
        showEventButton.setIcon(VaadinIcon.EYE.create());
        showEventButton.setTooltipText("显示关联时间序列事件详情");
        showEventButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedEventEntityUI();
            }
        });
        add(showEventButton);

        Icon divIcon2 = VaadinIcon.LINE_V.create();
        divIcon2.setSize("6px");
        add(divIcon2);

        Button showGeospatialEntityButton = new Button("相关时间流实体");
        showGeospatialEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showGeospatialEntityButton.getStyle().set("font-size","12px");
        Icon buttonIcon = VaadinIcon.TIMER.create();
        buttonIcon.setSize("14px");
        showGeospatialEntityButton.setIcon(buttonIcon);
        showGeospatialEntityButton.setTooltipText("显示关联时间刻度实体");
        showGeospatialEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedTimeScaleEntityUI();
            }
        });
        add(showGeospatialEntityButton);

        setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private void renderRelatedEventEntityUI(){
        String targetConceptionKind = RealmConstant.TimeScaleEventClass;
        String targetConceptionEntityUID = this.timeScaleEvent.getTimeScaleEventUID();

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("16px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8, Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(targetConceptionKind);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(targetConceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.CALENDAR),"时间关联事件详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void renderRelatedTimeScaleEntityUI(){
        String targetConceptionKind = RealmConstant.TimeScaleEntityClass;
        String targetConceptionEntityUID = this.timeScaleEntity.getTimeScaleEntityUID();

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8, Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(targetConceptionKind);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(targetConceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.TIMER),"时间流实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }
}
