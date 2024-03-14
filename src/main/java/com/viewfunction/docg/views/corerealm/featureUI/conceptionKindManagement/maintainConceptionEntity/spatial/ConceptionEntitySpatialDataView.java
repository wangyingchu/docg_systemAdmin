package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialScaleEventsMaintain.AttachGeospatialScaleEventsOfConceptionEntityView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList.ProcessingDataListView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConceptionEntitySpatialDataView extends VerticalLayout {

    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<GeospatialScaleDataPair> geospatialScaleDataPairList;
    private Accordion accordion;
    private Scroller scroller;
    private Registration listener;

    public ConceptionEntitySpatialDataView(){

        Button attachGeoScalaEntityButton = new Button();
        attachGeoScalaEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        attachGeoScalaEntityButton.getStyle().set("font-size","12px");
        Icon buttonIcon0 = LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create();
        buttonIcon0.setSize("16px");
        attachGeoScalaEntityButton.setIcon(buttonIcon0);
        attachGeoScalaEntityButton.setTooltipText("关联地理空间区域事件");
        attachGeoScalaEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachGeospatialScaleEventsOfConceptionEntityView();
            }
        });

        Button refreshTemporalEventAttributesInfoButton = new Button();
        refreshTemporalEventAttributesInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        refreshTemporalEventAttributesInfoButton.getStyle().set("font-size","12px");
        Icon buttonIcon = VaadinIcon.REFRESH.create();
        buttonIcon.setSize("16px");
        refreshTemporalEventAttributesInfoButton.setIcon(buttonIcon);
        refreshTemporalEventAttributesInfoButton.setTooltipText("刷新关联地理空间区域事件信息");
        refreshTemporalEventAttributesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshSpatialEventAttributesInfo();
            }
        });

        List<Component> actionComponentList = new ArrayList<>();
        actionComponentList.add(attachGeoScalaEntityButton);
        actionComponentList.add(refreshTemporalEventAttributesInfoButton);
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.LIST_SELECT.create(), "关联地理空间区域事件信息",null,actionComponentList);
        add(secondaryTitleActionBar);

        accordion = new Accordion();
        accordion.setWidth(100, Unit.PERCENTAGE);
        scroller = new Scroller(accordion);
        scroller.setWidth(100,Unit.PERCENTAGE);
        add(scroller);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            scroller.setHeight(event.getHeight()-200, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            scroller.setHeight(browserHeight-200,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderSpatialDataInfo(List<GeospatialScaleDataPair> geospatialScaleDataPairList, String conceptionKindName, String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.geospatialScaleDataPairList = geospatialScaleDataPairList;

        if(this.geospatialScaleDataPairList != null && this.geospatialScaleDataPairList.size() >0){
            for(GeospatialScaleDataPair currentGeospatialScaleDataPair :this.geospatialScaleDataPairList){
                GeospatialScaleEvent currentGeospatialScaleEvent = currentGeospatialScaleDataPair.getGeospatialScaleEvent();
                GeospatialScaleEntity currentGeospatialScaleEntity = currentGeospatialScaleDataPair.getGeospatialScaleEntity();
                GeospatialEventSummaryWidget currentGeospatialEventSummaryWidget = new GeospatialEventSummaryWidget(currentGeospatialScaleEvent, currentGeospatialScaleEntity);
                currentGeospatialEventSummaryWidget.setConceptionEntitySpatialDataView(this);
                GeospatialEventDetailWidget currentGeospatialEventDetailWidget = new GeospatialEventDetailWidget(currentGeospatialScaleEvent, currentGeospatialScaleEntity);
                AccordionPanel accordionPanel = new AccordionPanel(currentGeospatialEventSummaryWidget,currentGeospatialEventDetailWidget);
                accordionPanel.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
                    @Override
                    public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                        boolean isOpened = openedChangeEvent.isOpened();
                        if(isOpened){
                            currentGeospatialEventDetailWidget.renderEntityMapInfo();
                        }
                    }
                });
                accordion.add(accordionPanel);
            }
        }
    }

    public void refreshSpatialEventAttributesInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
        ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);

        this.geospatialScaleDataPairList = targetConceptionEntity.getAttachedGeospatialScaleDataPairs();
        accordion.getChildren().forEach(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                accordion.remove(component);
            }
        });
        if(this.geospatialScaleDataPairList != null && this.geospatialScaleDataPairList.size() >0) {
            for (GeospatialScaleDataPair currentGeospatialScaleDataPair : this.geospatialScaleDataPairList) {
                GeospatialScaleEvent currentGeospatialScaleEvent = currentGeospatialScaleDataPair.getGeospatialScaleEvent();
                GeospatialScaleEntity currentGeospatialScaleEntity = currentGeospatialScaleDataPair.getGeospatialScaleEntity();
                GeospatialEventSummaryWidget currentGeospatialEventSummaryWidget = new GeospatialEventSummaryWidget(currentGeospatialScaleEvent, currentGeospatialScaleEntity);
                currentGeospatialEventSummaryWidget.setConceptionEntitySpatialDataView(this);
                GeospatialEventDetailWidget currentGeospatialEventDetailWidget = new GeospatialEventDetailWidget(currentGeospatialScaleEvent, currentGeospatialScaleEntity);
                AccordionPanel accordionPanel = new AccordionPanel(currentGeospatialEventSummaryWidget, currentGeospatialEventDetailWidget);
                accordionPanel.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
                    @Override
                    public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                        boolean isOpened = openedChangeEvent.isOpened();
                        if (isOpened) {
                            currentGeospatialEventDetailWidget.renderEntityMapInfo();
                        }
                    }
                });
                accordion.add(accordionPanel);
            }
        }
        coreRealm.closeGlobalSession();
    }

    private void renderAttachGeospatialScaleEventsOfConceptionEntityView(){
        AttachGeospatialScaleEventsOfConceptionEntityView attachGeospatialScaleEventsOfConceptionEntityView = new AttachGeospatialScaleEventsOfConceptionEntityView(this.conceptionKindName,this.conceptionEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create(),"关联地理空间区域事件",null,true,1090,580,false);
        fixSizeWindow.setWindowContent(attachGeospatialScaleEventsOfConceptionEntityView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
