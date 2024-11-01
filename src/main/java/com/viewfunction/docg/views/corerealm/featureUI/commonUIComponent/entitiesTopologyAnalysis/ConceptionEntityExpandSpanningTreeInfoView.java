package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesSpanningTree;

public class ConceptionEntityExpandSpanningTreeInfoView extends VerticalLayout {

    private Registration listener;
    private String conceptionKind;
    private String conceptionEntityUID;
    private EntitiesSpanningTree entitiesSpanningTree;
    private ConceptionEntityExpandTopologyChart conceptionEntityExpandTopologyChart;
    private HorizontalLayout containerLayout;

    public ConceptionEntityExpandSpanningTreeInfoView(String conceptionKind, String conceptionEntityUID, EntitiesSpanningTree entitiesSpanningTree) {
        this.setPadding(false);
        this.setMargin(false);
        this.setPadding(false);
        setSizeFull();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.entitiesSpanningTree = entitiesSpanningTree;
        this.containerLayout = new HorizontalLayout();
        add(containerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(this.entitiesSpanningTree != null){
            conceptionEntityExpandTopologyChart = new ConceptionEntityExpandTopologyChart(this.conceptionKind,this.conceptionEntityUID);
            containerLayout.add(conceptionEntityExpandTopologyChart);
            conceptionEntityExpandTopologyChart.setData(this.entitiesSpanningTree.getTreeRelationEntities());
        }

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            if(conceptionEntityExpandTopologyChart != null){
                conceptionEntityExpandTopologyChart.setHeight(event.getHeight()-120, Unit.PIXELS);
                conceptionEntityExpandTopologyChart.setWidth(event.getWidth()-940, Unit.PIXELS);
            }
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            if(conceptionEntityExpandTopologyChart != null){
                conceptionEntityExpandTopologyChart.setHeight(browserHeight-120,Unit.PIXELS);
                conceptionEntityExpandTopologyChart.setWidth(browserWidth-940, Unit.PIXELS);
            }
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
