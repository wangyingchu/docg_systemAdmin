package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;

import java.text.NumberFormat;
import java.util.List;

public class ConceptionDataCorrelationRealtimeInfoWidget extends VerticalLayout {

    private Grid<ConceptionKindCorrelationInfo> runtimeConceptionKindCorrelationInfoGrid;

    public ConceptionDataCorrelationRealtimeInfoWidget(){
        this.getStyle().set("background-color", "white");
        this.setSpacing(false);
        runtimeConceptionKindCorrelationInfoGrid = new Grid<>();
        runtimeConceptionKindCorrelationInfoGrid.setWidthFull();
        runtimeConceptionKindCorrelationInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        runtimeConceptionKindCorrelationInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        runtimeConceptionKindCorrelationInfoGrid.addColumn(ConceptionKindCorrelationInfo::getSourceConceptionKindName).setHeader("源概念类型").setKey("idx_0").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> runtimeRelationAndConceptionKindAttachInfo.getSourceConceptionKindName());
        runtimeConceptionKindCorrelationInfoGrid.addColumn(ConceptionKindCorrelationInfo::getRelationKindName).setHeader("关系类型").setKey("idx_1").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> runtimeRelationAndConceptionKindAttachInfo.getRelationKindName());
        runtimeConceptionKindCorrelationInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_2").setFlexGrow(0).setWidth("35px").setResizable(false);
        runtimeConceptionKindCorrelationInfoGrid.addColumn(ConceptionKindCorrelationInfo::getSourceConceptionKindName).setHeader("目标概念类型").setKey("idx_3").setResizable(true)
                .setTooltipGenerator(runtimeRelationAndConceptionKindAttachInfo -> runtimeRelationAndConceptionKindAttachInfo.getSourceConceptionKindName());
        runtimeConceptionKindCorrelationInfoGrid.addColumn(new NumberRenderer<>(ConceptionKindCorrelationInfo::getRelationEntityCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getRelationEntityCount() - entityStatisticsInfo2.getRelationEntityCount()))
                .setHeader("关系数量").setKey("idx_4").setWidth("60px");
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.CUBE,"源概念类型");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT_O,"关系类型");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CUBE,"目标概念类型");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"关系数量");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4).setSortable(true);
        add(runtimeConceptionKindCorrelationInfoGrid);
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<ConceptionKindCorrelationInfo, Icon> {
        @Override
        public Icon apply(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo) {
            Icon relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
            relationDirectionIcon.setSize("12px");
            return relationDirectionIcon;
        }
    }

    public void renderConceptionDataCorrelationRealtimeInfo(List<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoList){
        if(conceptionKindCorrelationInfoList != null){
            runtimeConceptionKindCorrelationInfoGrid.setItems(conceptionKindCorrelationInfoList);
        }
    }
}
