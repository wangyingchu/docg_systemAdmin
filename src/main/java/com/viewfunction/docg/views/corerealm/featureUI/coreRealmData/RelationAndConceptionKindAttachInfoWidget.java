package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DataStatusSnapshotInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RuntimeRelationAndConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;

import dev.mett.vaadin.tooltip.Tooltips;
import elemental.json.Json;
import elemental.json.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationAndConceptionKindAttachInfoWidget extends VerticalLayout {
    private Registration listener;
    private Map<String,Integer> conceptionKindIndexMap;
    private Map<String,Integer> relationKindIndexMap;
    private String[] conceptionKindsLabel_x;
    private String[] relationKindsLabel_y;
    private CartesianHeatmapChart inDegreeCartesianHeatmapChart;
    private CartesianHeatmapChart outDegreeCartesianHeatmapChart;
    private Grid<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoGrid;

    public RelationAndConceptionKindAttachInfoWidget(){
        conceptionKindIndexMap = new HashMap<>();
        relationKindIndexMap = new HashMap<>();

        List<Component> actionComponentsList = new ArrayList<>();
        Button outDegreeInfoButton = new Button("出度统计",new Icon(VaadinIcon.EXPAND_SQUARE));
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        outDegreeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            renderRelationAndConceptionKindAttachInfoHeatMapUI();
        });
        Tooltips.getCurrent().setTooltip(outDegreeInfoButton,"概念与关系实体出度统计 HeatMap");
        actionComponentsList.add(outDegreeInfoButton);

        Button inDegreeInfoButton = new Button("入度统计",new Icon(VaadinIcon.COMPRESS_SQUARE));
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        inDegreeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            //systemRuntimeInfoWidget.refreshSystemRuntimeInfo();
        });
        Tooltips.getCurrent().setTooltip(inDegreeInfoButton,"概念与关系实体入度统计 HeatMap");
        actionComponentsList.add(inDegreeInfoButton);

        SecondaryTitleActionBar sectionActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.GRID),"概念与关系实体统计",null,actionComponentsList);
        sectionActionBar.setWidth(410,Unit.PIXELS);
        add(sectionActionBar);

        runtimeRelationAndConceptionKindAttachInfoGrid = new Grid<>();
        runtimeRelationAndConceptionKindAttachInfoGrid.setWidth(410,Unit.PIXELS);
        runtimeRelationAndConceptionKindAttachInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        runtimeRelationAndConceptionKindAttachInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        runtimeRelationAndConceptionKindAttachInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        runtimeRelationAndConceptionKindAttachInfoGrid.addColumn(RuntimeRelationAndConceptionKindAttachInfo::getConceptionKind).setHeader("概念类型").setKey("idx_0").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> getConceptionKindName(runtimeRelationAndConceptionKindAttachInfo));
        runtimeRelationAndConceptionKindAttachInfoGrid.addColumn(RuntimeRelationAndConceptionKindAttachInfo::getRelationKind).setHeader("关系类型").setKey("idx_1").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> getRelationKindName(runtimeRelationAndConceptionKindAttachInfo));
        runtimeRelationAndConceptionKindAttachInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_2").setFlexGrow(0).setWidth("35px").setResizable(false)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> getRelationDirection(runtimeRelationAndConceptionKindAttachInfo));
        runtimeRelationAndConceptionKindAttachInfoGrid.addColumn(RuntimeRelationAndConceptionKindAttachInfo::getRelationEntityCount).setHeader("关系数量").setKey("idx_3").setWidth("50px");

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.CUBE,"概念类型");
        runtimeRelationAndConceptionKindAttachInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT_O,"关系类型");
        runtimeRelationAndConceptionKindAttachInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"关系数量");
        runtimeRelationAndConceptionKindAttachInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        add(runtimeRelationAndConceptionKindAttachInfoGrid);
    }

    private String getConceptionKindName(RuntimeRelationAndConceptionKindAttachInfo runtimeRelationAndConceptionKindAttachInfo){
        return runtimeRelationAndConceptionKindAttachInfo.getConceptionKind();
    }

    private String getRelationKindName(RuntimeRelationAndConceptionKindAttachInfo runtimeRelationAndConceptionKindAttachInfo){
        return runtimeRelationAndConceptionKindAttachInfo.getRelationKind();
    }

    private String getRelationDirection(RuntimeRelationAndConceptionKindAttachInfo runtimeRelationAndConceptionKindAttachInfo){
        return runtimeRelationAndConceptionKindAttachInfo.getRelationDirection().toString();
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<RuntimeRelationAndConceptionKindAttachInfo,Icon> {
        @Override
        public Icon apply(RuntimeRelationAndConceptionKindAttachInfo runtimeRelationAndConceptionKindAttachInfo) {
            Icon relationDirectionIcon = null;
            RelationDirection relationDirection = runtimeRelationAndConceptionKindAttachInfo.getRelationDirection();
            switch(relationDirection){
                case FROM -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
                case TO -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
            }
            return relationDirectionIcon;
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.runtimeRelationAndConceptionKindAttachInfoGrid.setHeight(event.getHeight()-295,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            this.runtimeRelationAndConceptionKindAttachInfoGrid.setHeight(browserHeight-295,Unit.PIXELS);
        }));
        renderRelationAndConceptionKindAttachInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderRelationAndConceptionKindAttachInfo(){
        runtimeRelationAndConceptionKindAttachInfoGrid.setItems(new ArrayList<>());
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
        List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();
        runtimeRelationAndConceptionKindAttachInfoGrid.setItems(runtimeRelationAndConceptionKindAttachInfoList);
    }

    private void renderRelationAndConceptionKindAttachInfoHeatMapUI(){
        FullScreenWindow fullSizeWindow = new FullScreenWindow(new Icon(VaadinIcon.SITEMAP),"概念与关系实体度统计概览",null,null,true);
        fullSizeWindow.setModel(true);
        HorizontalLayout heatMapsContainerLayout = new HorizontalLayout();

        inDegreeCartesianHeatmapChart = new CartesianHeatmapChart(1850,800);
        inDegreeCartesianHeatmapChart.setColorRange("WhiteSmoke","#4682B4");
        inDegreeCartesianHeatmapChart.setName("领域概念与关系实体入度统计");
        inDegreeCartesianHeatmapChart.hideLabels();
        //inDegreeCartesianHeatmapChart.hideMapValues();
        inDegreeCartesianHeatmapChart.setTopMargin(20);
        inDegreeCartesianHeatmapChart.setTooltipPosition("right");
        inDegreeCartesianHeatmapChart.setLeftMargin(20);
        inDegreeCartesianHeatmapChart.setBottomMargin(20);
        inDegreeCartesianHeatmapChart.setRightMargin(20);
        heatMapsContainerLayout.add(inDegreeCartesianHeatmapChart);

        outDegreeCartesianHeatmapChart = new CartesianHeatmapChart(750,800);
        outDegreeCartesianHeatmapChart.setColorRange("WhiteSmoke","#323b43");
        outDegreeCartesianHeatmapChart.setName("领域概念与关系实体出度统计");
        //outDegreeCartesianHeatmapChart.hideLabels();
        outDegreeCartesianHeatmapChart.hideMapValues();
        outDegreeCartesianHeatmapChart.setTopMargin(20);
        outDegreeCartesianHeatmapChart.setTooltipPosition("left");
        outDegreeCartesianHeatmapChart.setRightMargin(200);
        //heatMapsContainerLayout.add(outDegreeCartesianHeatmapChart);

        conceptionKindIndexMap.clear();
        relationKindIndexMap.clear();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        JsonArray outDegreeDataArray = Json.createArray();
        JsonArray inDegreeDataArray = Json.createArray();
        long inDegreeMaxRelationCount = 0;
        long outDegreeMaxRelationCount = 0;
        try {
            List<EntityStatisticsInfo> conceptionEntityStatisticsInfoList = coreRealm.getConceptionEntitiesStatistics();
            conceptionKindsLabel_x = new String[conceptionEntityStatisticsInfoList.size()];
            for(int i =0 ;i<conceptionEntityStatisticsInfoList.size();i++){
                String conceptionKindName = conceptionEntityStatisticsInfoList.get(i).getEntityKindName();
                conceptionKindIndexMap.put(conceptionKindName,i);
                conceptionKindsLabel_x[i]=conceptionKindName;
            }

            List<EntityStatisticsInfo> relationEntityStatisticsInfoList = coreRealm.getRelationEntitiesStatistics();
            relationKindsLabel_y = new String[relationEntityStatisticsInfoList.size()];
            for(int i =0 ;i<relationEntityStatisticsInfoList.size();i++){
                String relationKindName =  relationEntityStatisticsInfoList.get(i).getEntityKindName();
                relationKindIndexMap.put(relationKindName,i);
                relationKindsLabel_y[i] = relationKindName;
            }

            int inDegreeDataArrayIdx = 0;
            int outDegreeDataArrayIdx = 0;

            SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
            DataStatusSnapshotInfo dataStatusSnapshotInfo = systemMaintenanceOperator.getDataStatusSnapshot();
            List<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoList = dataStatusSnapshotInfo.getRelationAndConceptionKindAttachInfo();

            for(RuntimeRelationAndConceptionKindAttachInfo currentRuntimeRelationAndConceptionKindAttachInfo:runtimeRelationAndConceptionKindAttachInfoList){
                RelationDirection relationDirection = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationDirection();
                String conceptionKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getConceptionKind();
                String relationKindName = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationKind();
                long relationEntityCount = currentRuntimeRelationAndConceptionKindAttachInfo.getRelationEntityCount();

                if(conceptionKindIndexMap.get(conceptionKindName) != null && relationKindIndexMap.get(relationKindName) != null){
                    JsonArray dataArray = Json.createArray();
                    dataArray.set(0,conceptionKindIndexMap.get(conceptionKindName));
                    dataArray.set(1,relationKindIndexMap.get(relationKindName));
                    dataArray.set(2,relationEntityCount);

                    switch (relationDirection){
                        case FROM -> {
                            outDegreeDataArray.set(outDegreeDataArrayIdx,dataArray);
                            outDegreeDataArrayIdx++;
                            if(relationEntityCount > outDegreeMaxRelationCount){
                                outDegreeMaxRelationCount = relationEntityCount;
                            }
                            break;
                        }
                        case TO -> {
                            inDegreeDataArray.set(inDegreeDataArrayIdx,dataArray);
                            inDegreeDataArrayIdx++;
                            if(relationEntityCount > inDegreeMaxRelationCount){
                                inDegreeMaxRelationCount = relationEntityCount;
                            }
                        }
                    }
                }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        inDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        inDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        inDegreeCartesianHeatmapChart.setMaxMapValue((int)inDegreeMaxRelationCount);
        inDegreeCartesianHeatmapChart.setData(inDegreeDataArray);

        outDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        outDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        outDegreeCartesianHeatmapChart.setMaxMapValue((int)outDegreeMaxRelationCount);
        outDegreeCartesianHeatmapChart.setData(outDegreeDataArray);

        fullSizeWindow.setWindowContent(heatMapsContainerLayout);
        fullSizeWindow.show();
        fullSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {
                //conceptionKindRelationGuideButton.setEnabled(true);
            }
        });
    }
}
