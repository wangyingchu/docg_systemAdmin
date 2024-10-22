package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.LinkedList;
import java.util.List;

public class ConceptionEntityExpandPathInfoView extends VerticalLayout {
    private Registration listener;
    private String conceptionKind;
    private String conceptionEntityUID;
    private List<EntitiesPath> entitiesPathList;

    private Grid<EntitiesPath> entitiesPathGrid;
    private HorizontalLayout containerLayout;
    private ConceptionEntityExpandPathsChart conceptionEntityExpandPathsChart;

    public ConceptionEntityExpandPathInfoView(String conceptionKind,String conceptionEntityUID,List<EntitiesPath> entitiesPathList) {
        this.setPadding(false);
        this.setMargin(false);
        this.setPadding(false);
        setSizeFull();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.entitiesPathList = entitiesPathList;

        containerLayout = new HorizontalLayout();
        add(containerLayout);
        entitiesPathGrid = new Grid<>();
        entitiesPathGrid.setWidth(410,Unit.PIXELS);
        entitiesPathGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        entitiesPathGrid.setSelectionMode(Grid.SelectionMode.NONE);
        entitiesPathGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        entitiesPathGrid.addColumn(EntitiesPath::getStartConceptionEntityType).setHeader("概念类型").setKey("idx_0").setResizable(true);
        entitiesPathGrid.addColumn(EntitiesPath::getEndConceptionEntityType).setHeader("概念类型").setKey("idx_1").setResizable(true);
        entitiesPathGrid.setItems(this.entitiesPathList);
        containerLayout.add(entitiesPathGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(!this.entitiesPathList.isEmpty()){
            conceptionEntityExpandPathsChart = new ConceptionEntityExpandPathsChart(this.conceptionKind,this.conceptionEntityUID);
            containerLayout.add(conceptionEntityExpandPathsChart);

            int idx = this.entitiesPathList.size()<=10 ? this.entitiesPathList.size()-1:10;
            List<RelationEntity> fullRelationList = new LinkedList<>();
            for(int i=0;i<idx+1;i++){
                EntitiesPath currentEntityPath = this.entitiesPathList.get(i);
                LinkedList<RelationEntity> pathRelationEntitiesList = currentEntityPath.getPathRelationEntities();
                fullRelationList.addAll(pathRelationEntitiesList);

            }
            conceptionEntityExpandPathsChart.setData(fullRelationList);
            System.out.println("fullRelationListfullRelationListfullRelationListfullRelationList");
            System.out.println(fullRelationList);
            System.out.println("fullRelationListfullRelationListfullRelationListfullRelationList");

            for(EntitiesPath currentPath : this.entitiesPathList){
                // LinkedList<ConceptionEntity> pathConceptionEntitiesList =  currentPath.getPathConceptionEntities();
              //  LinkedList<RelationEntity> pathRelationEntitiesList = currentPath.getPathRelationEntities();
              //
                /*
                pathConceptionEntitiesList.forEach(new Consumer<ConceptionEntity>() {
                    @Override
                    public void accept(ConceptionEntity conceptionEntity) {

                    }
                });

                pathRelationEntitiesList.forEach(new Consumer<RelationEntity>() {
                    @Override
                    public void accept(RelationEntity relationEntity) {

                    }
                });
                */

            }
        }

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            entitiesPathGrid.setHeight(event.getHeight()-145, Unit.PIXELS);
            if(conceptionEntityExpandPathsChart != null){
                conceptionEntityExpandPathsChart.setHeight(event.getHeight()-145, Unit.PIXELS);
                conceptionEntityExpandPathsChart.setWidth(event.getWidth()-775, Unit.PIXELS);
            }
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            entitiesPathGrid.setHeight(browserHeight-145,Unit.PIXELS);
            if(conceptionEntityExpandPathsChart != null){
                conceptionEntityExpandPathsChart.setHeight(browserHeight-145,Unit.PIXELS);
                conceptionEntityExpandPathsChart.setWidth(browserWidth-775, Unit.PIXELS);
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
