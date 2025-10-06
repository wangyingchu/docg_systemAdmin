package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ConceptionDataCorrelationRealtimeInfoWidget extends VerticalLayout {

    private Grid<ConceptionKindCorrelationInfo> runtimeConceptionKindCorrelationInfoGrid;
    private IntelligentAnalysisView containerIntelligentAnalysisView;

    public ConceptionDataCorrelationRealtimeInfoWidget(IntelligentAnalysisView containerIntelligentAnalysisView){
        this.getStyle().set("background-color", "white");
        this.setSpacing(false);
        this.containerIntelligentAnalysisView = containerIntelligentAnalysisView;

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(conceptionKindCorrelationInfo -> {
            Icon configIcon = LineAwesomeIconsSvg.AUDIBLE.create();
            configIcon.setSize("16px");
            Button addToInsightScope = new Button(configIcon, event -> {
                containerIntelligentAnalysisView.addConceptionKindCorrelationToInsightScope((ConceptionKindCorrelationInfo)conceptionKindCorrelationInfo);
            });
            addToInsightScope.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_CONTRAST);
            addToInsightScope.setTooltipText("加入知识洞察范围");
            HorizontalLayout buttons = new HorizontalLayout(addToInsightScope);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15, Unit.PIXELS);
            buttons.setWidth(50,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        runtimeConceptionKindCorrelationInfoGrid = new Grid<>();
        runtimeConceptionKindCorrelationInfoGrid.setWidthFull();
        runtimeConceptionKindCorrelationInfoGrid.setHeight(600,Unit.PIXELS);
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
        runtimeConceptionKindCorrelationInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5")
                .setFlexGrow(0).setWidth("50px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.CUBE,"源概念类型");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT_O,"关系类型");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CUBE,"目标概念类型");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"关系数量");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx5 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        runtimeConceptionKindCorrelationInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_1_idx5);

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
            List<ConceptionKindCorrelationInfo> filteredList = new ArrayList<>();
            for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:conceptionKindCorrelationInfoList){
                if(currentConceptionKindCorrelationInfo.getSourceConceptionKindName().startsWith(RealmConstant.RealmInnerTypePerFix)&
                currentConceptionKindCorrelationInfo.getTargetConceptionKindName().startsWith(RealmConstant.RealmInnerTypePerFix)){
                }else{
                    filteredList.add(currentConceptionKindCorrelationInfo);
                }
            }
            runtimeConceptionKindCorrelationInfoGrid.setItems(filteredList);
        }
    }
}
