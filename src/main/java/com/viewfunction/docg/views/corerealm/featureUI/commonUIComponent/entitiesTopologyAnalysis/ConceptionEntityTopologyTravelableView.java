package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;

import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntityTopologyTravelableView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;
    public enum PathExpandType {ExpandPath,ExpandGraph,ExpandSpanningTree}

    public ConceptionEntityTopologyTravelableView(String conceptionKind, String conceptionEntityUID){

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,this.conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,this.conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);

        ConceptionEntityExpandTopologyCriteriaView conceptionEntityExpandTopologyCriteriaView =
                new ConceptionEntityExpandTopologyCriteriaView(this.conceptionKind,this.conceptionEntityUID,null);
        queryFieldsContainer.add(conceptionEntityExpandTopologyCriteriaView);

        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        if(webBrowser.isChrome()){
            queryFieldsContainer.setMinWidth(320, Unit.PIXELS);
            queryFieldsContainer.setMaxWidth(320,Unit.PIXELS);
        }else{
            queryFieldsContainer.setMinWidth(310,Unit.PIXELS);
            queryFieldsContainer.setMaxWidth(310,Unit.PIXELS);
        }

        queryResultContainer= new VerticalLayout();
        queryResultContainer.setPadding(false);
        queryResultContainer.setSpacing(false);
        queryResultContainer.setMargin(false);

        ConceptionEntityTopologyTravelableResultView conceptionEntityTopologyTravelableResultView = new ConceptionEntityTopologyTravelableResultView();
        queryResultContainer.add(conceptionEntityTopologyTravelableResultView);

        SplitLayout splitLayout = new SplitLayout(queryFieldsContainer, queryResultContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
    }
}
