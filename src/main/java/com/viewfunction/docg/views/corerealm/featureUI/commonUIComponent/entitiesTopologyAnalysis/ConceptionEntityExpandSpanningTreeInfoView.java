package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesSpanningTree;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.EntitySyntheticAbstractInfoView;

public class ConceptionEntityExpandSpanningTreeInfoView extends VerticalLayout implements ConceptionEntityExpandTopologyChart.ConceptionEntityExpandTopologyChartOperationHandler {

    private Registration listener;
    private String conceptionKind;
    private String conceptionEntityUID;
    private EntitiesSpanningTree entitiesSpanningTree;
    private ConceptionEntityExpandTopologyChart conceptionEntityExpandTopologyChart;
    private EntitySyntheticAbstractInfoView entitySyntheticAbstractInfoView;
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
        containerLayout.setPadding(false);
        containerLayout.setSpacing(false);
        containerLayout.setMargin(false);
        containerLayout.setWidthFull();
        add(containerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(this.entitiesSpanningTree != null){
            conceptionEntityExpandTopologyChart = new ConceptionEntityExpandTopologyChart(this.conceptionKind,this.conceptionEntityUID);
            conceptionEntityExpandTopologyChart.setConceptionEntityExpandTopologyChartOperationHandler(this);
            conceptionEntityExpandTopologyChart.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
            containerLayout.add(conceptionEntityExpandTopologyChart);
            conceptionEntityExpandTopologyChart.setData(this.entitiesSpanningTree.getTreeRelationEntities(),this.entitiesSpanningTree.getTreeConceptionEntities());
            entitySyntheticAbstractInfoView = new EntitySyntheticAbstractInfoView(330);
            entitySyntheticAbstractInfoView.getStyle().set("padding-left", "8px");
            entitySyntheticAbstractInfoView.setWidth(350,Unit.PIXELS);
            containerLayout.add(this.entitySyntheticAbstractInfoView);
            containerLayout.setFlexGrow(1,this.conceptionEntityExpandTopologyChart);
        }

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            if(conceptionEntityExpandTopologyChart != null){
                conceptionEntityExpandTopologyChart.setHeight(event.getHeight()-120, Unit.PIXELS);
                entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(event.getHeight()-320);
            }
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            if(conceptionEntityExpandTopologyChart != null){
                conceptionEntityExpandTopologyChart.setHeight(browserHeight-120,Unit.PIXELS);
                entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(browserHeight-320);
            }
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    @Override
    public void selectConceptionEntity(String entityType, String entityUID) {
        if(entitySyntheticAbstractInfoView != null){
            entitySyntheticAbstractInfoView.renderConceptionEntitySyntheticAbstractInfo(entityType,entityUID);
        }
    }

    @Override
    public void unselectConceptionEntity(String entityType, String entityUID) {
        if(entitySyntheticAbstractInfoView != null){
            entitySyntheticAbstractInfoView.cleanAbstractInfo();
        }
    }

    @Override
    public void selectRelationEntity(String entityType, String entityUID) {
        if(entitySyntheticAbstractInfoView != null){
            entitySyntheticAbstractInfoView.renderRelationEntitySyntheticAbstractInfo(entityType,entityUID);
        }
    }

    @Override
    public void unselectRelationEntity(String entityType, String entityUID) {
        if(entitySyntheticAbstractInfoView != null){
            entitySyntheticAbstractInfoView.cleanAbstractInfo();
        }
    }
}
