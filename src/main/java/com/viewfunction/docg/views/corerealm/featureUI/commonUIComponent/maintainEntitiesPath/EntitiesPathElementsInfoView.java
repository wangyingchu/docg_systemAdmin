package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.maintainEntitiesPath;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.EntitySyntheticAbstractInfoView;

public class EntitiesPathElementsInfoView extends HorizontalLayout implements EntitiesPathTopologyChart.EntitiesPathTopologyChartOperationHandler{

    private EntitiesPath entitiesPath;
    private EntitySyntheticAbstractInfoView entitySyntheticAbstractInfoView;
    private Registration listener;
    private EntitiesPathTopologyChart entitiesPathTopologyChart;

    public EntitiesPathElementsInfoView(EntitiesPath entitiesPath,int viewHeight) {
        this.entitiesPath = entitiesPath;
        entitiesPathTopologyChart =
                new EntitiesPathTopologyChart(this.entitiesPath.getStartConceptionEntityUID(),this.entitiesPath.getEndConceptionEntityUID());
        entitiesPathTopologyChart.setEntitiesPathTopologyChartOperationHandler(this);
        entitiesPathTopologyChart.setHeight(viewHeight-45, Unit.PIXELS);
        entitiesPathTopologyChart.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        entitiesPathTopologyChart.setData(this.entitiesPath.getPathRelationEntities(),this.entitiesPath.getPathConceptionEntities());
        add(entitiesPathTopologyChart);

        entitySyntheticAbstractInfoView = new EntitySyntheticAbstractInfoView(330);
        entitySyntheticAbstractInfoView.setWidth(330, Unit.PIXELS);
        add(entitySyntheticAbstractInfoView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            entitiesPathTopologyChart.setWidth(event.getWidth() - 830, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            entitiesPathTopologyChart.setWidth(receiver.getBodyClientWidth() - 830, Unit.PIXELS);
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

    public void refreshPathTopologyInfo(){
        if(entitiesPathTopologyChart != null) {
            entitiesPathTopologyChart.reload();
            entitiesPathTopologyChart.setData(this.entitiesPath.getPathRelationEntities(), this.entitiesPath.getPathConceptionEntities());
        }
        if(entitySyntheticAbstractInfoView != null){
            entitySyntheticAbstractInfoView.cleanAbstractInfo();
        }
    }
}
