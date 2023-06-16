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
import com.vaadin.flow.data.renderer.NumberRenderer;
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
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;

import dev.mett.vaadin.tooltip.Tooltips;
import elemental.json.Json;
import elemental.json.JsonArray;

import java.text.NumberFormat;
import java.util.*;

public class RelationAndConceptionKindAttachInfoWidget extends VerticalLayout {
    private Registration listener;
    private Grid<RuntimeRelationAndConceptionKindAttachInfo> runtimeRelationAndConceptionKindAttachInfoGrid;

    public RelationAndConceptionKindAttachInfoWidget(){
        List<Component> actionComponentsList = new ArrayList<>();
        Button outDegreeInfoButton = new Button("出度统计",new Icon(VaadinIcon.EXPAND_SQUARE));
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        outDegreeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            renderRelationAndConceptionKindOutDegreeHeatMapUI();
        });
        Tooltips.getCurrent().setTooltip(outDegreeInfoButton,"概念与关系实体出度统计 HeatMap");
        actionComponentsList.add(outDegreeInfoButton);

        Button inDegreeInfoButton = new Button("入度统计",new Icon(VaadinIcon.COMPRESS_SQUARE));
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        inDegreeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            renderRelationAndConceptionKindInDegreeHeatMapUI();
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
        runtimeRelationAndConceptionKindAttachInfoGrid.addColumn(new NumberRenderer<>(RuntimeRelationAndConceptionKindAttachInfo::getRelationEntityCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getRelationEntityCount() - entityStatisticsInfo2.getRelationEntityCount()))
                .setHeader("关系数量").setKey("idx_3").setWidth("50px");
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
            this.runtimeRelationAndConceptionKindAttachInfoGrid.setHeight(event.getHeight()-270,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            this.runtimeRelationAndConceptionKindAttachInfoGrid.setHeight(browserHeight-270,Unit.PIXELS);
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

    private void renderRelationAndConceptionKindInDegreeHeatMapUI(){
        FullScreenWindow fullSizeWindow = new FullScreenWindow(new Icon(VaadinIcon.COMPRESS_SQUARE),"概念与关系实体入度统计概览",null,null,true);
        fullSizeWindow.setModel(true);
        HorizontalLayout heatMapsContainerLayout = new HorizontalLayout();

        CartesianHeatmapChart inDegreeCartesianHeatmapChart = new CartesianHeatmapChart(1850,800);
        inDegreeCartesianHeatmapChart.setColorRange("#ABDCFF","#0396FF");
        //inDegreeCartesianHeatmapChart.setName("领域概念与关系实体入度统计");
        inDegreeCartesianHeatmapChart.setTooltipPosition("left");
        inDegreeCartesianHeatmapChart.setXAxisLabelRotateDegree(30);
        heatMapsContainerLayout.add(inDegreeCartesianHeatmapChart);

        String[] conceptionKindsLabel_x;
        String[] relationKindsLabel_y;
        Map<String,Integer> conceptionKindIndexMap = new HashMap<>();
        Map<String,Integer> relationKindIndexMap = new HashMap<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        JsonArray inDegreeDataArray = Json.createArray();
        List<Long> totalCountList = new ArrayList<>();

        long inDegreeMaxRelationCount = 0;
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
                    dataArray.set(0,relationKindIndexMap.get(relationKindName));
                    dataArray.set(1,conceptionKindIndexMap.get(conceptionKindName));
                    dataArray.set(2,relationEntityCount);
                    totalCountList.add(relationEntityCount);

                    switch (relationDirection){
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
        int maxValue = (int)(inDegreeMaxRelationCount - median(totalCountList))/3;
        inDegreeCartesianHeatmapChart.setMaxMapValue(maxValue);
        inDegreeCartesianHeatmapChart.setMinMapValue((int)min(totalCountList));
        inDegreeCartesianHeatmapChart.setData(inDegreeDataArray);

        fullSizeWindow.setWindowContent(heatMapsContainerLayout);
        fullSizeWindow.show();
        fullSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {}
        });
    }

    private void renderRelationAndConceptionKindOutDegreeHeatMapUI(){
        FullScreenWindow fullSizeWindow = new FullScreenWindow(new Icon(VaadinIcon.EXPAND_SQUARE),"概念与关系实体出度统计概览",null,null,true);
        fullSizeWindow.setModel(true);
        HorizontalLayout heatMapsContainerLayout = new HorizontalLayout();

        CartesianHeatmapChart outDegreeCartesianHeatmapChart = new CartesianHeatmapChart(1850,800);
        outDegreeCartesianHeatmapChart.setColorRange("#FCCF31","#F55555");
        //outDegreeCartesianHeatmapChart.setName("领域概念与关系实体出度统计");
        outDegreeCartesianHeatmapChart.setTooltipPosition("left");
        outDegreeCartesianHeatmapChart.setXAxisLabelRotateDegree(30);
        heatMapsContainerLayout.add(outDegreeCartesianHeatmapChart);

        String[] conceptionKindsLabel_x;
        String[] relationKindsLabel_y;
        Map<String,Integer> conceptionKindIndexMap = new HashMap<>();
        Map<String,Integer> relationKindIndexMap = new HashMap<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        JsonArray outDegreeDataArray = Json.createArray();
        List<Long> totalCountList = new ArrayList<>();

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
                    dataArray.set(0,relationKindIndexMap.get(relationKindName));
                    dataArray.set(1,conceptionKindIndexMap.get(conceptionKindName));
                    dataArray.set(2,relationEntityCount);
                    totalCountList.add(relationEntityCount);
                    switch (relationDirection){
                        case FROM -> {
                            outDegreeDataArray.set(outDegreeDataArrayIdx,dataArray);
                            outDegreeDataArrayIdx++;
                            if(relationEntityCount > outDegreeMaxRelationCount){
                                outDegreeMaxRelationCount = relationEntityCount;
                            }
                        }
                    }
                }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        outDegreeCartesianHeatmapChart.setXAxisLabel(conceptionKindsLabel_x);
        outDegreeCartesianHeatmapChart.setYAxisLabel(relationKindsLabel_y);
        int maxValue = (int)(outDegreeMaxRelationCount - median(totalCountList))/3;
        outDegreeCartesianHeatmapChart.setMaxMapValue(maxValue);
        outDegreeCartesianHeatmapChart.setMinMapValue((int)min(totalCountList));
        outDegreeCartesianHeatmapChart.setData(outDegreeDataArray);

        fullSizeWindow.setWindowContent(heatMapsContainerLayout);
        fullSizeWindow.show();
        fullSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {}
        });
    }

    private static double median(List<Long> total) {
        /*获取数组中位数*/
        double j = 0;
        //集合排序
        Collections.sort(total);
        int size = total.size();
        if(size % 2 == 1){
            j = total.get((size-1)/2);
        }else {
            //加0.0是为了把int转成double类型，否则除以2会算错
            j = (total.get(size/2-1) + total.get(size/2) + 0.0)/2;
        }
        return j;
    }

    private static long min(List<Long> total) {
        /*获取数组最小数*/
        double j = 0;
        //集合排序
        Collections.sort(total);
        return total.get(0);
    }
}
