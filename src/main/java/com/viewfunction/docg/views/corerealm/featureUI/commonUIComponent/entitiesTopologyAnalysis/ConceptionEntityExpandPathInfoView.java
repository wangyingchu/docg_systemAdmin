package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
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
        entitiesPathGrid.setWidth(570,Unit.PIXELS);
        entitiesPathGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        entitiesPathGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        entitiesPathGrid.addComponentColumn(new PathStartConceptionEntityValueProvider()).setHeader("起点概念实体").setKey("idx_0").setResizable(true).setFlexGrow(1);
        entitiesPathGrid.addColumn(EntitiesPath::getPathJumps).setHeader("跳数").setKey("idx_1").setResizable(true).setFlexGrow(0).setWidth("80px");
        //entitiesPathGrid.addColumn(EntitiesPath::getPathWeight).setHeader("权重").setKey("idx_2").setResizable(true).setWidth("20px");
        entitiesPathGrid.addComponentColumn(new PathEndConceptionEntityValueProvider()).setHeader("终点概念实体").setKey("idx_3").setResizable(true).setFlexGrow(1);

        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.SUN_RISE,"起点概念实体");
        entitiesPathGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.RUNNING_SOLID.create(),"跳数");
        entitiesPathGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx2).setSortable(true);
        //GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CONNECT_O,"权重");
        //entitiesPathGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.SUN_DOWN.create(),"终点概念实体");
        entitiesPathGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx4).setSortable(false);
        entitiesPathGrid.setItems(this.entitiesPathList);
        containerLayout.add(entitiesPathGrid);

        entitiesPathGrid.addSelectionListener(new SelectionListener<Grid<EntitiesPath>, EntitiesPath>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<EntitiesPath>, EntitiesPath> selectionEvent) {
                //conceptionEntityExpandPathsChart.clearData();
                if(selectionEvent != null){
                    EntitiesPath selectedPath = selectionEvent.getAllSelectedItems().iterator().next();
                    conceptionEntityExpandPathsChart.setData(selectedPath.getPathRelationEntities()
                    );
                }
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(!this.entitiesPathList.isEmpty()){
            conceptionEntityExpandPathsChart = new ConceptionEntityExpandPathsChart(this.conceptionKind,this.conceptionEntityUID);
            containerLayout.add(conceptionEntityExpandPathsChart);

            /*
            int idx = this.entitiesPathList.size()<=10 ? this.entitiesPathList.size()-1:10;
            List<RelationEntity> fullRelationList = new LinkedList<>();
            for(int i=0;i<idx+1;i++){
                EntitiesPath currentEntityPath = this.entitiesPathList.get(i);
                LinkedList<RelationEntity> pathRelationEntitiesList = currentEntityPath.getPathRelationEntities();
                fullRelationList.addAll(pathRelationEntitiesList);
            }
            */
            //conceptionEntityExpandPathsChart.setData(fullRelationList);
        }

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            entitiesPathGrid.setHeight(event.getHeight()-120, Unit.PIXELS);
            if(conceptionEntityExpandPathsChart != null){
                conceptionEntityExpandPathsChart.setHeight(event.getHeight()-120, Unit.PIXELS);
                conceptionEntityExpandPathsChart.setWidth(event.getWidth()-940, Unit.PIXELS);
            }
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            entitiesPathGrid.setHeight(browserHeight-120,Unit.PIXELS);
            if(conceptionEntityExpandPathsChart != null){
                conceptionEntityExpandPathsChart.setHeight(browserHeight-120,Unit.PIXELS);
                conceptionEntityExpandPathsChart.setWidth(browserWidth-940, Unit.PIXELS);
            }
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private class PathStartConceptionEntityValueProvider implements ValueProvider<EntitiesPath,HorizontalLayout> {
        @Override
        public HorizontalLayout apply(EntitiesPath entitiesPath) {
            String startConceptionEntityUID = entitiesPath.getStartConceptionEntityUID();
            String startConceptionEntityType = entitiesPath.getStartConceptionEntityType();

            Icon conceptionKindIcon = VaadinIcon.CUBE.create();
            conceptionKindIcon.setSize("12px");
            conceptionKindIcon.getStyle().set("padding-right","3px");
            Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
            conceptionEntityIcon.setSize("18px");
            conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
            List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,startConceptionEntityType));
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,startConceptionEntityUID));
            FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList,true);
            entityInfoFootprintMessageBar.getStyle().set("font-size","10px");
            return entityInfoFootprintMessageBar;
        }
    }

    private class PathEndConceptionEntityValueProvider implements ValueProvider<EntitiesPath,HorizontalLayout> {
        @Override
        public HorizontalLayout apply(EntitiesPath entitiesPath) {
            String endConceptionEntityUID = entitiesPath.getEndConceptionEntityUID();
            String endConceptionEntityType = entitiesPath.getEndConceptionEntityType();

            Icon conceptionKindIcon = VaadinIcon.CUBE.create();
            conceptionKindIcon.setSize("12px");
            conceptionKindIcon.getStyle().set("padding-right","3px");
            Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
            conceptionEntityIcon.setSize("18px");
            conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
            List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,endConceptionEntityType));
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,endConceptionEntityUID));
            FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList,true);
            entityInfoFootprintMessageBar.getStyle().set("font-size","10px");
            return entityInfoFootprintMessageBar;
        }
    }
}
