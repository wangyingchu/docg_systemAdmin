package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEvent;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.List;

public class ConceptionEntitySpatialDataView extends VerticalLayout {

    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<GeospatialScaleDataPair> geospatialScaleDataPairList;
    private Accordion accordion;

    public ConceptionEntitySpatialDataView(){
        //this.getStyle().set("padding-left","100px");
        SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.LIST_SELECT.create(), "关联地理空间区域事件信息");
        add(secondaryIconTitle);

        accordion = new Accordion();
        accordion.setWidth(100, Unit.PERCENTAGE);
        Scroller scroller = new Scroller(accordion);
        scroller.setWidth(100,Unit.PERCENTAGE);
        add(scroller);
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
                GeospatialEventDetailWidget currentGeospatialEventDetailWidget = new GeospatialEventDetailWidget(currentGeospatialScaleEvent, currentGeospatialScaleEntity);
                AccordionPanel accordionPanel = new AccordionPanel(currentGeospatialEventSummaryWidget,currentGeospatialEventDetailWidget);
                accordion.add(accordionPanel);
            }
        }
    }
}
