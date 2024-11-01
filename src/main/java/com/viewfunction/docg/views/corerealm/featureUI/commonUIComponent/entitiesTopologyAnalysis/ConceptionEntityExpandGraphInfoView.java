package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesGraph;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.EntitySyntheticAbstractInfoView;

public class ConceptionEntityExpandGraphInfoView extends VerticalLayout {

    private Registration listener;
    private String conceptionKind;
    private String conceptionEntityUID;
    private EntitiesGraph entitiesGraph;
    private ConceptionEntityExpandPathsChart conceptionEntityExpandPathsChart;
    private EntitySyntheticAbstractInfoView entitySyntheticAbstractInfoView;
    private HorizontalLayout containerLayout;

    public ConceptionEntityExpandGraphInfoView(String conceptionKind, String conceptionEntityUID, EntitiesGraph entitiesGraph) {
        this.setPadding(false);
        this.setMargin(false);
        this.setPadding(false);
        setSizeFull();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.entitiesGraph = entitiesGraph;
        containerLayout = new HorizontalLayout();
        containerLayout.setPadding(false);
        containerLayout.setSpacing(false);
        containerLayout.setMargin(false);
        containerLayout.setWidthFull();
        add(containerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(this.entitiesGraph != null){
            conceptionEntityExpandPathsChart = new ConceptionEntityExpandPathsChart(this.conceptionKind,this.conceptionEntityUID);
            containerLayout.add(conceptionEntityExpandPathsChart);
            conceptionEntityExpandPathsChart.setData(this.entitiesGraph.getGraphRelationEntities());
            entitySyntheticAbstractInfoView = new EntitySyntheticAbstractInfoView(330);
            entitySyntheticAbstractInfoView.setWidth(350,Unit.PIXELS);
            containerLayout.add(this.entitySyntheticAbstractInfoView);
            containerLayout.setFlexGrow(1,this.conceptionEntityExpandPathsChart);
        }

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            if(conceptionEntityExpandPathsChart != null){
                conceptionEntityExpandPathsChart.setHeight(event.getHeight()-120, Unit.PIXELS);
            }
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            if(conceptionEntityExpandPathsChart != null){
                conceptionEntityExpandPathsChart.setHeight(browserHeight-120,Unit.PIXELS);
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
