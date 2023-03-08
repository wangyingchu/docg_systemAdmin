package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
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

public class RelationEntityConnectedConceptionEntitiesPairView extends VerticalLayout {

    private String relationKind;
    private String relationEntityUID;
    public RelationEntityConnectedConceptionEntitiesPairView(String relationKind,String relationEntityUID,int relationEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;

        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        SecondaryKeyValueDisplayItem relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(), "FROM 概念实体", "-");

        Icon divIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
        divIcon.setSize("10px");
        titleLayout.add(divIcon);
        titleLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        SecondaryKeyValueDisplayItem isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.BULLSEYE.create(), "TO 概念实体", "-");

        Button addToProcessingDataListButton = new Button("加入待处理数据列表");
        addToProcessingDataListButton.setIcon(VaadinIcon.INBOX.create());
        addToProcessingDataListButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addToProcessingDataListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //addConceptionEntityToProcessingList(conceptionKind,conceptionEntityUID);
            }
        });

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //refreshRelationsInfo();
            }
        });

        actionComponentsList.add(addToProcessingDataListButton);
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.INFO_CIRCLE_O.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关系实体概要: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);
    }


}
