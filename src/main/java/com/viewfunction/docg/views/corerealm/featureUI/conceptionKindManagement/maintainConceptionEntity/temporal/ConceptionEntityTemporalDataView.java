package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.timeScaleEventsMaintain.AttachTimeScaleEventsOfConceptionEntityView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityTemporalInfoView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConceptionEntityTemporalDataView extends VerticalLayout {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<TimeScaleDataPair> timeScaleDataPairList;
    private Accordion accordion;
    private ConceptionEntityTemporalInfoView containerConceptionEntityTemporalInfoView;

    public ConceptionEntityTemporalDataView(){
        this.getStyle().set("padding-left","100px");

        Button attachTimeScalaEntityButton = new Button();
        attachTimeScalaEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        attachTimeScalaEntityButton.getStyle().set("font-size","12px");
        Icon buttonIcon0 = LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create();
        buttonIcon0.setSize("16px");
        attachTimeScalaEntityButton.setIcon(buttonIcon0);
        attachTimeScalaEntityButton.setTooltipText("关联时间序列事件");
        attachTimeScalaEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachTimeScaleEventsOfConceptionEntityView();
            }
        });

        Button refreshTemporalEventAttributesInfoButton = new Button();
        refreshTemporalEventAttributesInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        refreshTemporalEventAttributesInfoButton.getStyle().set("font-size","12px");
        Icon buttonIcon = VaadinIcon.REFRESH.create();
        buttonIcon.setSize("16px");
        refreshTemporalEventAttributesInfoButton.setIcon(buttonIcon);
        refreshTemporalEventAttributesInfoButton.setTooltipText("刷新关联时间序列事件信息");
        refreshTemporalEventAttributesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshTemporalEventAttributesInfo();
            }
        });

        List<Component> actionComponentList = new ArrayList<>();
        actionComponentList.add(attachTimeScalaEntityButton);
        actionComponentList.add(refreshTemporalEventAttributesInfoButton);
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.LIST_SELECT.create(), "关联时间序列事件信息",null,actionComponentList);
        add(secondaryTitleActionBar);

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
                currentTemporalEventSummaryWidget.setContainerConceptionEntityTemporalDataView(this);
                TemporalEventDetailWidget currentTemporalEventDetailWidget = new TemporalEventDetailWidget(currentTimeScaleEvent, currentTimeScaleEntity);
                AccordionPanel accordionPanel = new AccordionPanel(currentTemporalEventSummaryWidget,currentTemporalEventDetailWidget);
                accordion.add(accordionPanel);
            }
        }
    }

    public void refreshTemporalEventAttributesInfo(){
        accordion.getChildren().forEach(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                accordion.remove(component);
            }
        });

        if(containerConceptionEntityTemporalInfoView != null){
            containerConceptionEntityTemporalInfoView.renderEntityTemporalInfo();
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
            ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            this.timeScaleDataPairList = targetConceptionEntity.getAttachedTimeScaleDataPairs();
            if(this.timeScaleDataPairList != null && this.timeScaleDataPairList.size() >0){
                for(TimeScaleDataPair currentTimeScaleDataPair :this.timeScaleDataPairList){
                    TimeScaleEvent currentTimeScaleEvent = currentTimeScaleDataPair.getTimeScaleEvent();
                    TimeScaleEntity currentTimeScaleEntity = currentTimeScaleDataPair.getTimeScaleEntity();
                    TemporalEventSummaryWidget currentTemporalEventSummaryWidget = new TemporalEventSummaryWidget(currentTimeScaleEvent, currentTimeScaleEntity);
                    currentTemporalEventSummaryWidget.setContainerConceptionEntityTemporalDataView(this);
                    TemporalEventDetailWidget currentTemporalEventDetailWidget = new TemporalEventDetailWidget(currentTimeScaleEvent, currentTimeScaleEntity);
                    AccordionPanel accordionPanel = new AccordionPanel(currentTemporalEventSummaryWidget,currentTemporalEventDetailWidget);
                    accordion.add(accordionPanel);
                }
            }
            coreRealm.closeGlobalSession();
        }
    }

    private void renderAttachTimeScaleEventsOfConceptionEntityView(){
        AttachTimeScaleEventsOfConceptionEntityView.AttachTimeScaleEventsOfConceptionEntityCallback attachTimeScaleEventsOfConceptionEntityCallback = new AttachTimeScaleEventsOfConceptionEntityView.AttachTimeScaleEventsOfConceptionEntityCallback() {
            @Override
            public void onSuccess(List<TimeScaleEvent> resultEventList) {
                refreshTemporalEventAttributesInfo();
            }
        };
        AttachTimeScaleEventsOfConceptionEntityView attachTimeScaleEventsOfConceptionEntityView = new AttachTimeScaleEventsOfConceptionEntityView(this.conceptionKindName,this.conceptionEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create(),"关联时间序列事件",null,true,1090,580,false);
        fixSizeWindow.setWindowContent(attachTimeScaleEventsOfConceptionEntityView);
        attachTimeScaleEventsOfConceptionEntityView.setContainerDialog(fixSizeWindow);
        attachTimeScaleEventsOfConceptionEntityView.setAttachTimeScaleEventsOfConceptionEntityCallback(attachTimeScaleEventsOfConceptionEntityCallback);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    public void setContainerConceptionEntityTemporalInfoView(ConceptionEntityTemporalInfoView containerConceptionEntityTemporalInfoView) {
        this.containerConceptionEntityTemporalInfoView = containerConceptionEntityTemporalInfoView;
    }
}
