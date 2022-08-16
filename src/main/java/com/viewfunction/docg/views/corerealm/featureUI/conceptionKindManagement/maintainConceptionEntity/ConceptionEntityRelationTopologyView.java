package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntityRelationTopologyView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private int conceptionEntityRelationInfoViewHeightOffset;
    private ConceptionEntityRelationsChart conceptionEntityRelationsChart;

    public ConceptionEntityRelationTopologyView(String conceptionKind,String conceptionEntityUID,int conceptionEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityRelationInfoViewHeightOffset = conceptionEntityIntegratedInfoViewHeightOffset+100;
        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(), "关联关系总量", "-");
        inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_LEFT.create(), "关系入度", "-");
        outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(), "关系出度", "-");
        isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.BULLSEYE.create(), "是否稠密实体", "-");

        Button reloadConceptionEntitiesInfoButton = new Button("关联概念实体类型分布");
        reloadConceptionEntitiesInfoButton.setIcon(VaadinIcon.PIE_CHART.create());
        reloadConceptionEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadConceptionEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(reloadConceptionEntitiesInfoButton);

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.RANDOM.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关联关系概要: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        this.conceptionEntityRelationsChart = new ConceptionEntityRelationsChart(this.conceptionKind,this.conceptionEntityUID);
        add(this.conceptionEntityRelationsChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        super.onDetach(detachEvent);
    }

    public void loadEntityRelationNetworks(){
        this.conceptionEntityRelationsChart.initLoadRelationData();
    }
}
