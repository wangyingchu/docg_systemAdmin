package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.RelationEntitiesCreatedEvent;
import com.viewfunction.docg.element.eventHandling.RelationEntityDeletedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.AddConceptionEntityToProcessingListView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.relation.EntityAttachedRelationKindsCountChart;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.CreateRelationEntityView;
import dev.mett.vaadin.tooltip.Tooltips;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.DeleteRelationEntityView;

import java.util.*;

public class ConceptionEntityRelationInfoView extends VerticalLayout implements
        RelationEntityDeletedEvent.RelationEntityDeletedListener,
        RelationEntitiesCreatedEvent.RelationEntitiesCreatedListener {
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
    private GridListDataView<RelationEntity> relationEntitiesView;
    private String currentDisplayRelationKind;
    private List<Button> relationKindFilterButtonList;
    private long allRelationsNumber;
    private long inDegreeCountNumber;
    private long outDegreeCountNumber;
    private Map<String,Long> attachedRelationKindCountInfo;
    private Map<String,Span> relationEntityCountSpanMap;
    private Span relationEntityCountSpan;
    private VerticalLayout chartContainer;

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

        Button addToProcessingDataListButton = new Button("加入待处理数据列表");
        addToProcessingDataListButton.setIcon(VaadinIcon.INBOX.create());
        addToProcessingDataListButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addToProcessingDataListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                addConceptionEntityToProcessingList(conceptionKind,conceptionEntityUID);
            }
        });

        Button createRelationButton = new Button("新建实体关联");
        createRelationButton.setIcon(VaadinIcon.LINK.create());
        createRelationButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        createRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelateConceptionEntityInfoUI();
            }
        });

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshRelationsInfo();
            }
        });

        actionComponentsList.add(addToProcessingDataListButton);
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
        relationEntitiesGrid.addComponentColumn(new RelationEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_1").setFlexGrow(0).setWidth("120px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        relationEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.KEY_O,"关系实体 UID");
        relationEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CONNECT_O,"关系实体类型名称");
        relationEntitiesGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(FontAwesome.Solid.STETHOSCOPE.create(),"相关概念实体");
        relationEntitiesGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(false);

        relationEntitiesGrid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<RelationEntity>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<RelationEntity> relationEntityItemDoubleClickEvent) {
                RelationEntity targetRelationEntity = relationEntityItemDoubleClickEvent.getItem();
                if(targetRelationEntity != null){
                    renderRelatedConceptionEntityUI(targetRelationEntity);
                }
            }
        });

        relationEntitiesListContainerLayout.add(relationEntitiesGrid);
        relationEntitiesDetailLayout.add(relationEntitiesListContainerLayout);

        this.relationKindFilterButtonList = new ArrayList<>();
        this.relationEntityCountSpanMap = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadEntityRelationsInfo();
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            relationEntitiesGrid.setHeight(event.getHeight()-this.conceptionEntityRelationInfoViewHeightOffset+25, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            relationEntitiesGrid.setHeight(browserHeight-this.conceptionEntityRelationInfoViewHeightOffset+25,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadEntityRelationsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                long allRelationsCount = targetEntity.countAllRelations();
                allRelationsNumber = allRelationsCount;
                try {
                    long inDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.TO);
                    long outDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.FROM);
                    inDegreeCountNumber = inDegree;
                    outDegreeCountNumber = outDegree;
                    inDegreeDisplayItem.updateDisplayValue(""+inDegreeCountNumber);
                    outDegreeDisplayItem.updateDisplayValue(""+outDegreeCountNumber);
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
                relationCountDisplayItem.updateDisplayValue(""+allRelationsNumber);
                isDenseDisplayItem.updateDisplayValue(""+targetEntity.isDense());

                attachedRelationKindCountInfo = targetEntity.countAttachedRelationKinds();

                chartContainer = new VerticalLayout();
                chartContainer.setPadding(false);
                chartContainer.setMargin(false);
                chartContainer.setSpacing(false);
                relationKindsInfoLayout.add(chartContainer);

                EntityAttachedRelationKindsCountChart entityAttachedRelationKindsCountChart = new EntityAttachedRelationKindsCountChart(attachedRelationKindCountInfo);
                chartContainer.add(entityAttachedRelationKindsCountChart);

                Icon filterIcon = new Icon(VaadinIcon.FILTER);
                filterIcon.setSize("8px");
                Tooltips.getCurrent().setTooltip(filterIcon, "按类型过滤关系实体");
                relationKindsInfoLayout.add(filterIcon);

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
                    this.relationKindFilterButtonList.add(currentRelationKindButton);
                    currentRelationKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                        @Override
                        public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                            filterRelationEntities(relationKindName,currentRelationKindButton);
                        }
                    });

                    currentRelationKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
                    relationKindInfoItem.add(currentRelationKindButton);

                    Span relationEntityCountSpan = new Span(""+attachedRelationKindCountInfo.get(relationKindName).toString());
                    relationEntityCountSpan.getStyle().set("font-size","var(--lumo-font-size-xxs)").set("font-weight","bold");
                    relationEntityCountSpan.setHeight(20,Unit.PIXELS);
                    relationEntityCountSpan.getElement().getThemeList().add("badge contrast");
                    relationKindInfoItem.add(relationEntityCountSpan);
                    this.relationEntityCountSpanMap.put(relationKindName,relationEntityCountSpan);
                }

                HorizontalLayout relationKindInfoItem = new HorizontalLayout();
                relationKindInfoItem.setSpacing(false);
                relationKindsInfoLayout.add(relationKindInfoItem);

                Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
                relationKindIcon.setSize("10px");
                relationKindInfoItem.add(relationKindIcon);
                relationKindInfoItem.setVerticalComponentAlignment(Alignment.CENTER,relationKindIcon);
                Button currentRelationKindButton = new Button("所有关系类型");
                this.relationKindFilterButtonList.add(currentRelationKindButton);
                currentRelationKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        filterRelationEntities(null,currentRelationKindButton);
                    }
                });

                currentRelationKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
                relationKindInfoItem.add(currentRelationKindButton);
                currentRelationKindButton.setEnabled(false);
                relationEntityCountSpan = new Span(""+allRelationsCount);
                relationEntityCountSpan.getStyle().set("font-size","var(--lumo-font-size-xxs)").set("font-weight","bold");
                relationEntityCountSpan.setHeight(20,Unit.PIXELS);
                relationEntityCountSpan.getElement().getThemeList().add("badge contrast");
                relationKindInfoItem.add(relationEntityCountSpan);

                List<RelationEntity> relationEntityList = targetEntity.getAllRelations();
                this.relationEntitiesView = relationEntitiesGrid.setItems(relationEntityList);
                this.relationEntitiesView.addFilter(item->{
                    if(currentDisplayRelationKind != null){
                        if(currentDisplayRelationKind.equals(item.getRelationKindName())){
                            return true;
                        }else{
                            return false;
                        }
                    }else{
                        return true;
                    }
                });
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }

    @Override
    public void receivedRelationEntityDeletedEvent(RelationEntityDeletedEvent event) {
        String relationEntityUID = event.getRelationEntityUID();
        String relationKindName = event.getRelationKindName();
        if(relationEntityUID != null && relationKindName!= null){
            boolean needUpdateRelationInfo= false;
            ListDataProvider dataProvider=(ListDataProvider)relationEntitiesGrid.getDataProvider();
            Collection<RelationEntity> relationEntityValueList = dataProvider.getItems();
            RelationEntity targetRelationEntityToDelete = null;
            for(RelationEntity currentRelationEntityValue:relationEntityValueList){
                if(currentRelationEntityValue.getRelationEntityUID().equals(relationEntityUID)){
                    targetRelationEntityToDelete = currentRelationEntityValue;
                    break;
                }
            }
            if(targetRelationEntityToDelete != null){
                dataProvider.getItems().remove(targetRelationEntityToDelete);
                dataProvider.refreshAll();
                needUpdateRelationInfo = true;
            }
            if(needUpdateRelationInfo){
                allRelationsNumber--;
                relationCountDisplayItem.updateDisplayValue(""+allRelationsNumber);
                if(this.conceptionEntityUID.equals(event.getFromConceptionEntityUID())){
                    outDegreeCountNumber--;
                    outDegreeDisplayItem.updateDisplayValue(""+outDegreeCountNumber);
                }
                if(this.conceptionEntityUID.equals(event.getToConceptionEntityUID())){
                    inDegreeCountNumber--;
                    inDegreeDisplayItem.updateDisplayValue(""+inDegreeCountNumber);
                }
                relationEntityCountSpan.setText(""+allRelationsNumber);

                Long newRelationEntityCount = attachedRelationKindCountInfo.get(relationKindName) -1;
                attachedRelationKindCountInfo.put(relationKindName,newRelationEntityCount);
                relationEntityCountSpanMap.get(relationKindName).setText(""+newRelationEntityCount.longValue());

                chartContainer.removeAll();

                EntityAttachedRelationKindsCountChart entityAttachedRelationKindsCountChart = new EntityAttachedRelationKindsCountChart(attachedRelationKindCountInfo);
                chartContainer.add(entityAttachedRelationKindsCountChart);
            }
        }
    }

    @Override
    public void receivedRelationEntitiesCreatedEvent(RelationEntitiesCreatedEvent event) {
        refreshRelationsInfo();
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
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(showDetailButton, "显示关联概念实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntity != null){
                        renderRelatedConceptionEntityUI(relationEntity);
                    }
                }
            });

            Button addToProcessListButton = new Button();
            addToProcessListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addToProcessListButton.setIcon(VaadinIcon.INBOX.create());
            Tooltips.getCurrent().setTooltip(addToProcessListButton, "将关联概念实体加入待处理数据列表");
            actionButtonContainerLayout.add(addToProcessListButton);
            addToProcessListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntity != null){
                        String targetConceptionKind = null;
                        String targetConceptionEntityUID = null;
                        if(conceptionEntityUID.equals(relationEntity.getFromConceptionEntityUID())){
                            targetConceptionEntityUID = relationEntity.getToConceptionEntityUID();
                            targetConceptionKind = relationEntity.getToConceptionEntityKinds().get(0);
                        }else{
                            targetConceptionEntityUID= relationEntity.getFromConceptionEntityUID();
                            targetConceptionKind = relationEntity.getFromConceptionEntityKinds().get(0);
                        }
                        addConceptionEntityToProcessingList(targetConceptionKind,targetConceptionEntityUID);
                    }
                }
            });

            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR );
            deleteButton.setIcon(VaadinIcon.UNLINK.create());
            Tooltips.getCurrent().setTooltip(deleteButton, "删除实体关联");
            actionButtonContainerLayout.add(deleteButton);
            deleteButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntity != null){
                        deleteRelationEntity(relationEntity);
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
            List<String> fromConceptionKindList = relationEntity.getFromConceptionEntityKinds();
            List<String> toConceptionKindList = relationEntity.getToConceptionEntityKinds();

            Icon conceptionKindIcon = VaadinIcon.CUBE.create();
            conceptionKindIcon.setSize("12px");
            conceptionKindIcon.getStyle().set("padding-right","3px");
            Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
            conceptionEntityIcon.setSize("18px");
            conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
            List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();

            if(conceptionEntityUID.equals(fromConceptionEntityUID)){
                String conceptionKind = "";
                if(toConceptionKindList !=null &&toConceptionKindList.size()>0){
                    for(int i=0; i<toConceptionKindList.size(); i++){
                        conceptionKind = conceptionKind+toConceptionKindList.get(i);
                        if(i<toConceptionKindList.size()-1){
                            conceptionKind = conceptionKind+" | ";
                        }
                    }
                }
                footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
                footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,toConceptionEntityUID));
            }
            if(!fromConceptionEntityUID.equals(toConceptionEntityUID)){
                if(conceptionEntityUID.equals(toConceptionEntityUID)){
                    String conceptionKind = "";
                    if(fromConceptionKindList !=null && fromConceptionKindList.size()>0){
                        for(int i=0; i<fromConceptionKindList.size(); i++){
                            conceptionKind = conceptionKind+fromConceptionKindList.get(i);
                            if(i<fromConceptionKindList.size()-1){
                                conceptionKind = conceptionKind+" | ";
                            }
                        }
                    }
                    footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
                    footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,fromConceptionEntityUID));
                }
            }
            FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList,true);

            return entityInfoFootprintMessageBar;
        }
    }

    private void filterRelationEntities(String displayRelationKind,Button launchButton){
        this.currentDisplayRelationKind = displayRelationKind;
        this.relationEntitiesView.refreshAll();
        for(Button currentButton:this.relationKindFilterButtonList){
            currentButton.setEnabled(true);
        }
        launchButton.setEnabled(false);
    }

    private void renderRelatedConceptionEntityUI(RelationEntity relationEntity){
        String fromConceptionEntityUID = relationEntity.getFromConceptionEntityUID();
        String toConceptionEntityUID = relationEntity.getToConceptionEntityUID();
        String targetConceptionKind = null;
        String targetConceptionEntityUID = null;
        if(conceptionEntityUID.equals(fromConceptionEntityUID)){
            targetConceptionEntityUID = toConceptionEntityUID;
            targetConceptionKind = relationEntity.getToConceptionEntityKinds().get(0);
        }
        if(conceptionEntityUID.equals(toConceptionEntityUID)){
            targetConceptionEntityUID = fromConceptionEntityUID;
            targetConceptionKind = relationEntity.getFromConceptionEntityKinds().get(0);
        }

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(targetConceptionKind);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(targetConceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void deleteRelationEntity(RelationEntity relationEntity){
        DeleteRelationEntityView deleteRelationEntityView = new DeleteRelationEntityView(relationEntity);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除关系实体",null,true,600,210,false);
        fixSizeWindow.setWindowContent(deleteRelationEntityView);
        fixSizeWindow.setModel(true);
        deleteRelationEntityView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderRelateConceptionEntityInfoUI(){
        CreateRelationEntityView createRelationEntityView = new CreateRelationEntityView(this.conceptionKind,this.conceptionEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.LINK),"新建实体关联",null,true,1140,670,false);
        fixSizeWindow.setWindowContent(createRelationEntityView);
        fixSizeWindow.setModel(true);
        createRelationEntityView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void refreshRelationsInfo(){
        relationKindsInfoLayout.removeAll();
        ListDataProvider dataProvider = (ListDataProvider)relationEntitiesGrid.getDataProvider();
        dataProvider.getItems().clear();
        dataProvider.refreshAll();
        loadEntityRelationsInfo();
    }

    private void addConceptionEntityToProcessingList(String conceptionKind,String conceptionEntityUID){
        ConceptionEntityValue conceptionEntityValue = new ConceptionEntityValue();
        conceptionEntityValue.setConceptionEntityUID(conceptionEntityUID);
        AddConceptionEntityToProcessingListView addConceptionEntityToProcessingListView = new AddConceptionEntityToProcessingListView(conceptionKind,conceptionEntityValue);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INBOX),"待处理数据列表添加概念实例",null,true,600,295,false);
        fixSizeWindow.setWindowContent(addConceptionEntityToProcessingListView);
        fixSizeWindow.setModel(true);
        addConceptionEntityToProcessingListView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
