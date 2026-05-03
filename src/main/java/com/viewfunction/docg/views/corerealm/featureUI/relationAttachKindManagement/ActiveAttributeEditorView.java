package com.viewfunction.docg.views.corerealm.featureUI.relationAttachKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.select.Select;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.RelationAttachKindManagementUI;

import java.util.ArrayList;
import java.util.List;

public class ActiveAttributeEditorView extends VerticalLayout  {

    private Popover containerPopover;
    private RelationAttachKind relationAttachKind;
    private Select<String> select;
    private RelationAttachKindManagementUI parentRelationAttachKindManagementUI;

    public ActiveAttributeEditorView(RelationAttachKind relationAttachKind) {
        this.relationAttachKind = relationAttachKind;
        Icon kindIcon = VaadinIcon.FLIP_H.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.CODE.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, relationAttachKind.getRelationAttachKindName()));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(entityIcon, "isActive"));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        select = new Select<>();
        select.setItems("启用中","未启用");
        select.setWidth(220, Unit.PIXELS);
        select.setEmptySelectionAllowed(false);
        if(relationAttachKind.isActive()){
            select.setValue("启用中");
        }else{
            select.setValue("未启用");
        }

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);
        controlButtonsContainer.add(select);

        Button updateAttributeValueButton = new Button("更新状态属性");
        updateAttributeValueButton.addThemeVariants(ButtonVariant.PRIMARY);
        Icon plusIcon = VaadinIcon.CHECK_CIRCLE.create();
        plusIcon.setSize("18px");
        updateAttributeValueButton.setIcon(plusIcon);
        updateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                updateAttributeValue();
            }
        });
        controlButtonsContainer.add(updateAttributeValueButton);
        add(controlButtonsContainer);
    }

    public void setContainerPopover(Popover containerPopover) {
        this.containerPopover = containerPopover;
    }

    private void updateAttributeValue() {
        boolean isActive = relationAttachKind.isActive();
        String selectedValue = this.select.getValue();
        if(selectedValue.equals("启用中")){
            isActive = true;
        }else{
            isActive = false;
        }
        relationAttachKind.setActiveStatus(isActive);
        CommonUIOperationUtil.showPopupNotification("isActive 状态更新成功", NotificationVariant.LUMO_SUCCESS);

        if(this.parentRelationAttachKindManagementUI != null){
            this.parentRelationAttachKindManagementUI.updateActiveAttribute(relationAttachKind);
        }
        if(this.containerPopover != null){
            this.containerPopover.close();
        }
    }

    public void setParentRelationAttachKindManagementUI(RelationAttachKindManagementUI parentRelationAttachKindManagementUI) {
        this.parentRelationAttachKindManagementUI = parentRelationAttachKindManagementUI;
    }

    public void updateRelationAttachKind(RelationAttachKind relationAttachKind){
        this.relationAttachKind = relationAttachKind;
        this.removeAll();
        Icon kindIcon = VaadinIcon.FLIP_H.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.CODE.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, relationAttachKind.getRelationAttachKindName()));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(entityIcon, "isRepeatableRelationKindAllow"));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        select = new Select<>();
        select.setItems("启用中","未启用");
        select.setWidth(220, Unit.PIXELS);
        select.setEmptySelectionAllowed(false);
        if(relationAttachKind.isActive()){
            select.setValue("启用中");
        }else{
            select.setValue("未启用");
        }

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);
        controlButtonsContainer.add(select);

        Button updateAttributeValueButton = new Button("更新状态属性");
        updateAttributeValueButton.addThemeVariants(ButtonVariant.PRIMARY);
        Icon plusIcon = VaadinIcon.CHECK_CIRCLE.create();
        plusIcon.setSize("18px");
        updateAttributeValueButton.setIcon(plusIcon);
        updateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                updateAttributeValue();
            }
        });
        controlButtonsContainer.add(updateAttributeValueButton);
        add(controlButtonsContainer);
    }
}
