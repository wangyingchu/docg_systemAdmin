package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.github.appreciated.apexcharts.ApexCharts;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConceptionEntityRelationInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private VerticalLayout relationKindsInfoLayout;
    private Grid<RelationEntity> relationEntitiesGrid;
    private Registration listener;
    private int conceptionEntityRelationInfoViewHeightOffset;

    public ConceptionEntityRelationInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntityIntegratedInfoViewHeightOffset) {
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

        Button createRelationButton = new Button("新建实体关联");
        createRelationButton.setIcon(VaadinIcon.LINK.create());
        createRelationButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        createRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderShowMetaInfoUI();
            }
        });

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddNewAttributeUI();
            }
        });

        actionComponentsList.add(createRelationButton);
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.RANDOM.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关联关系概要: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        relationKindsInfoLayout = new VerticalLayout();
        relationKindsInfoLayout.setPadding(false);
        Scroller relationKindsInfoScroller = new Scroller(relationKindsInfoLayout);
        relationKindsInfoScroller.setWidth(430,Unit.PIXELS);
        relationKindsInfoScroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        relationEntitiesDetailLayout.add(relationKindsInfoScroller);

        SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.PIE_CHART.create(), "关系类型分布");
        relationKindsInfoLayout.add(secondaryIconTitle);

        VerticalLayout relationEntitiesListContainerLayout = new VerticalLayout();
        relationEntitiesListContainerLayout.setPadding(false);

        SecondaryIconTitle secondaryIconTitle2 = new SecondaryIconTitle(VaadinIcon.MENU.create(), "关系实体列表");
        relationEntitiesListContainerLayout.add(secondaryIconTitle2);

        relationEntitiesGrid = new Grid<>();
        relationEntitiesGrid.setWidth(100, Unit.PERCENTAGE);
        relationEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_COLUMN_BORDERS);
        relationEntitiesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        relationEntitiesGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        relationEntitiesGrid.addColumn(RelationEntity::getRelationEntityUID).setHeader("关系实体 UID").setKey("idx_2").setFlexGrow(0).setWidth("130px").setResizable(false);
        relationEntitiesGrid.addColumn(RelationEntity::getRelationKindName).setHeader("关系实体类型名称").setKey("idx_3").setResizable(true);
        relationEntitiesGrid.addComponentColumn(new RelatedConceptionEntityValueProvider()).setHeader("相关概念实体").setKey("idx_4").setFlexGrow(1).setResizable(true);
        relationEntitiesGrid.addComponentColumn(new RelationEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_1").setFlexGrow(0).setWidth("110px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        relationEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.KEY_O,"关系实体 UID");
        relationEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CONNECT_O,"关系实体类型名称");
        relationEntitiesGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(FontAwesome.Solid.STETHOSCOPE.create(),"相关概念实体");
        relationEntitiesGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(false);

        relationEntitiesListContainerLayout.add(relationEntitiesGrid);
        relationEntitiesDetailLayout.add(relationEntitiesListContainerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadEntityRelationsInfo();
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            relationEntitiesGrid.setHeight(event.getHeight()-this.conceptionEntityRelationInfoViewHeightOffset, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            relationEntitiesGrid.setHeight(browserHeight-this.conceptionEntityRelationInfoViewHeightOffset,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadEntityRelationsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                long allRelationsCount = targetEntity.countAllRelations();
                try {
                    long inDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.TO);
                    long outDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.FROM);
                    inDegreeDisplayItem.updateDisplayValue(""+inDegree);
                    outDegreeDisplayItem.updateDisplayValue(""+outDegree);
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
                relationCountDisplayItem.updateDisplayValue(""+allRelationsCount);
                isDenseDisplayItem.updateDisplayValue(""+targetEntity.isDense());

                Map<String,Long> attachedRelationKindCountInfo = targetEntity.countAttachedRelationKinds();
                ApexCharts entityAttachedRelationKindsCountChart = new EntityAttachedRelationKindsCountChart(attachedRelationKindCountInfo)
                        .withColors("#03a9f4","#76b852","#00d1b2","#ced7df","#ee4f4f","#0288d1","#ffc107","#d32f2f","#168eea","#323b43","#59626a").build();
                entityAttachedRelationKindsCountChart.setWidth(250,Unit.PIXELS);
                entityAttachedRelationKindsCountChart.setHeight(130,Unit.PIXELS);
                entityAttachedRelationKindsCountChart.getStyle().set("padding-left","30px");
                relationKindsInfoLayout.add(entityAttachedRelationKindsCountChart);

                Set<String> relationKindsSet = attachedRelationKindCountInfo.keySet();
                for(String relationKindName : relationKindsSet){
                    HorizontalLayout relationKindInfoItem = new HorizontalLayout();
                    relationKindInfoItem.setSpacing(false);
                    relationKindsInfoLayout.add(relationKindInfoItem);

                    Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
                    relationKindIcon.setSize("10px");
                    relationKindInfoItem.add(relationKindIcon);
                    relationKindInfoItem.setVerticalComponentAlignment(Alignment.CENTER,relationKindIcon);
                    Button currentRelationKindButton = new Button(relationKindName);
                    currentRelationKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
                    relationKindInfoItem.add(currentRelationKindButton);

                    Span relationEntityCountSpan = new Span(""+attachedRelationKindCountInfo.get(relationKindName).toString());
                    relationEntityCountSpan.getStyle().set("font-size","var(--lumo-font-size-xxs)").set("font-weight","bold");
                    relationEntityCountSpan.setHeight(20,Unit.PIXELS);
                    relationEntityCountSpan.getElement().getThemeList().add("badge contrast");
                    relationKindInfoItem.add(relationEntityCountSpan);
                }

                List<RelationEntity> relationEntityList = targetEntity.getAllRelations();
                relationEntitiesGrid.setItems(relationEntityList);
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<RelationEntity,Icon>{
        @Override
        public Icon apply(RelationEntity relationEntity) {
            Icon relationDirectionIcon = null;
            String fromConceptionEntityUID = relationEntity.getFromConceptionEntityUID();
            String toConceptionEntityUID = relationEntity.getToConceptionEntityUID();
            if(conceptionEntityUID.equals(fromConceptionEntityUID)){
                relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
            }
            if(conceptionEntityUID.equals(toConceptionEntityUID)){
                relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
            }
            return relationDirectionIcon;
        }
    }
    private class RelationEntityActionButtonsValueProvider implements ValueProvider<RelationEntity,HorizontalLayout>{
        @Override
        public HorizontalLayout apply(RelationEntity relationEntity) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(showDetailButton, "显示关联概念实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntity != null){
                        //renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });

            Button addToProcessListButton = new Button();
            addToProcessListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addToProcessListButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            addToProcessListButton.setIcon(VaadinIcon.INBOX.create());
            Tooltips.getCurrent().setTooltip(addToProcessListButton, "将关联概念实体加入待处理数据列表");
            actionButtonContainerLayout.add(addToProcessListButton);
            addToProcessListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntity != null){
                        //addConceptionEntityToProcessingList(conceptionEntityValue);
                    }
                }
            });

            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR );
            deleteButton.setIcon(VaadinIcon.UNLINK.create());
            Tooltips.getCurrent().setTooltip(deleteButton, "删除实体关联");
            actionButtonContainerLayout.add(deleteButton);
            deleteButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntity != null){
                        //deleteConceptionEntity(conceptionEntityValue);
                    }
                }
            });
            return actionButtonContainerLayout;
        }
    }

    private class RelatedConceptionEntityValueProvider implements ValueProvider<RelationEntity,HorizontalLayout>{
        @Override
        public HorizontalLayout apply(RelationEntity relationEntity) {
            String fromConceptionEntityUID = relationEntity.getFromConceptionEntityUID();
            String toConceptionEntityUID = relationEntity.getToConceptionEntityUID();

            Icon conceptionKindIcon = VaadinIcon.CUBE.create();
            conceptionKindIcon.setSize("12px");
            conceptionKindIcon.getStyle().set("padding-right","3px");
            Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
            conceptionEntityIcon.setSize("18px");
            conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
            List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,"NOT-KNOWN-KIND"));
            if(conceptionEntityUID.equals(fromConceptionEntityUID)){
                footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,toConceptionEntityUID));
            }
            if(conceptionEntityUID.equals(toConceptionEntityUID)){
                footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,fromConceptionEntityUID));
            }
            FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList,true);

            return entityInfoFootprintMessageBar;
        }
    }
}
