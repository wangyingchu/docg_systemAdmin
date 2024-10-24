package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConceptionEntityExpandPathInfoView extends VerticalLayout {
    private Registration listener;
    private String conceptionKind;
    private String conceptionEntityUID;
    private List<EntitiesPath> entitiesPathList;
    private Grid<EntitiesPath> entitiesPathGrid;
    private HorizontalLayout containerLayout;
    private ConceptionEntityExpandPathsChart conceptionEntityExpandPathsChart;
    private Map<EntitiesPath,Icon> entitiesPathDisplayIconMap;
    private Popover EntitiesPathInfoPopover;

    public ConceptionEntityExpandPathInfoView(String conceptionKind,String conceptionEntityUID,List<EntitiesPath> entitiesPathList) {
        this.setPadding(false);
        this.setMargin(false);
        this.setPadding(false);
        setSizeFull();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.entitiesPathList = entitiesPathList;
        entitiesPathDisplayIconMap = new HashMap<>();
        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entitiesPath -> {
            Icon queryIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
            queryIcon.setSize("16px");
            Button pathInfoDisplayButton = new Button(queryIcon);
            pathInfoDisplayButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    showEntitiesPathInfo((EntitiesPath)entitiesPath,pathInfoDisplayButton);
                }
            });

            pathInfoDisplayButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            pathInfoDisplayButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            pathInfoDisplayButton.setTooltipText("显示路径详情");

            HorizontalLayout buttons = new HorizontalLayout(pathInfoDisplayButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(40,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        containerLayout = new HorizontalLayout();
        add(containerLayout);
        entitiesPathGrid = new Grid<>();
        entitiesPathGrid.setWidth(570,Unit.PIXELS);
        entitiesPathGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        entitiesPathGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        entitiesPathGrid.addComponentColumn(new EntitiesPathDisplayValueProvider()).setHeader("").setKey("idx").setFlexGrow(0).setWidth("35px").setResizable(false);
        entitiesPathGrid.addComponentColumn(new PathStartConceptionEntityValueProvider()).setHeader("起点概念实体").setKey("idx_0").setResizable(true).setFlexGrow(1);
        entitiesPathGrid.addColumn(EntitiesPath::getPathJumps).setHeader("").setKey("idx_1").setResizable(true).setFlexGrow(0).setWidth("50px");
        entitiesPathGrid.addComponentColumn(new PathEndConceptionEntityValueProvider()).setHeader("终点概念实体").setKey("idx_3").setResizable(true).setFlexGrow(1);
        entitiesPathGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("60px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.SUN_RISE,"起点概念实体");
        entitiesPathGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.RUNNING_SOLID.create(),"");
        entitiesPathGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.SUN_DOWN.create(),"终点概念实体");
        entitiesPathGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx4).setSortable(false);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        entitiesPathGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx5);

        entitiesPathGrid.setItems(this.entitiesPathList);
        containerLayout.add(entitiesPathGrid);

        entitiesPathGrid.addSelectionListener(new SelectionListener<Grid<EntitiesPath>, EntitiesPath>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<EntitiesPath>, EntitiesPath> selectionEvent) {
                if(selectionEvent != null){
                    if(selectionEvent.getAllSelectedItems() != null && selectionEvent.getAllSelectedItems().size() > 0){
                        EntitiesPath selectedPath = selectionEvent.getAllSelectedItems().iterator().next();
                        conceptionEntityExpandPathsChart.setData(selectedPath.getPathRelationEntities());
                        if(entitiesPathDisplayIconMap.containsKey(selectedPath)){
                            entitiesPathDisplayIconMap.get(selectedPath).getStyle().set("color","#3D9970");
                        }
                    }
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
        entitiesPathDisplayIconMap.clear();
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

    private class EntitiesPathDisplayValueProvider implements ValueProvider<EntitiesPath,Icon>{
        @Override
        public Icon apply(EntitiesPath entitiesPath) {
            Icon entitiesPathDisplayIcon = VaadinIcon.LAPTOP.create();
            entitiesPathDisplayIcon.setSize("12px");
            entitiesPathDisplayIcon.getStyle().set("color","#CCCCCC");
            entitiesPathDisplayIconMap.put(entitiesPath,entitiesPathDisplayIcon);
            return entitiesPathDisplayIcon;
        }
    }

    private void showEntitiesPathInfo(EntitiesPath entitiesPath,Button popupTarget){
        if(this.EntitiesPathInfoPopover == null){
            this.EntitiesPathInfoPopover = new Popover();
        }
        this.EntitiesPathInfoPopover.setTarget(popupTarget);
        this.EntitiesPathInfoPopover.add(new NativeLabel("1234567"));
        this.EntitiesPathInfoPopover.open();
    }
}
