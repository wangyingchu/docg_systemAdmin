package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.RelationEntityDeletedEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindCleanedEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindCreatedEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList.ProcessingDataListView;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.*;

import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.queryRelationKind.RelationKindQueryUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class RelationKindManagementUI extends VerticalLayout implements
        RelationKindCreatedEvent.RelationKindCreatedListener,
        RelationKindCleanedEvent.RelationKindCleanedListener,
        RelationKindRemovedEvent.RelationKindRemovedListener,
        RelationEntityDeletedEvent.RelationEntityDeletedListener{

    private Grid<EntityStatisticsInfo> relationKindMetaInfoGrid;
    private GridListDataView<EntityStatisticsInfo> relationKindsMetaInfoView;
    private Registration listener;
    final ZoneId id = ZoneId.systemDefault();
    private TextField relationKindNameFilterField;
    private TextField relationKindDescFilterField;
    private EntityStatisticsInfo lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo;
    private Grid<KindEntityAttributeRuntimeStatistics> relationKindAttributesInfoGrid;
    private VerticalLayout singleRelationKindSummaryInfoContainerLayout;
    private RelationKindCorrelationInfoChart relationKindCorrelationInfoChart;
    private SecondaryTitleActionBar secondaryTitleActionBar;
    private int entityAttributesDistributionStatisticSampleRatio = 10000;

    public RelationKindManagementUI(){
        Button refreshDataButton = new Button("刷新关系类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            loadRelationKindsInfo();
            resetSingleRelationKindSummaryInfoArea();
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span("Default CoreRealm"));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Relation Kind 关系类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> relationKindManagementOperationButtonList = new ArrayList<>();

        Button relationKindRelationGuideButton = new Button("关系实体关联分布概览",new Icon(VaadinIcon.SITEMAP));
        relationKindRelationGuideButton.setDisableOnClick(true);
        relationKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        relationKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindManagementOperationButtonList.add(relationKindRelationGuideButton);
        relationKindRelationGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelationKindsCorrelationInfoSummaryUI(relationKindRelationGuideButton);
            }
        });

        Button processingDataListButton = new Button("待处理数据",new Icon(VaadinIcon.MAILBOX));
        processingDataListButton.setDisableOnClick(true);
        processingDataListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        processingDataListButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindManagementOperationButtonList.add(processingDataListButton);
        processingDataListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderProcessingDataListUI(processingDataListButton);
            }
        });

        Button createRelationKindButton = new Button("创建关系类型",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createRelationKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createRelationKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindManagementOperationButtonList.add(createRelationKindButton);
        createRelationKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateRelationKindUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"关系类型定义:",relationKindManagementOperationButtonList);
        add(sectionActionBar);

        HorizontalLayout relationKindsInfoContainerLayout = new HorizontalLayout();
        relationKindsInfoContainerLayout.setSpacing(false);
        relationKindsInfoContainerLayout.setMargin(false);
        relationKindsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(relationKindsInfoContainerLayout);

        VerticalLayout relationKindMetaInfoGridContainerLayout = new VerticalLayout();
        relationKindMetaInfoGridContainerLayout.setSpacing(true);
        relationKindMetaInfoGridContainerLayout.setMargin(false);
        relationKindMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout relationKindsSearchElementsContainerLayout = new HorizontalLayout();
        relationKindsSearchElementsContainerLayout.setSpacing(false);
        relationKindsSearchElementsContainerLayout.setMargin(false);
        relationKindMetaInfoGridContainerLayout.add(relationKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        relationKindsSearchElementsContainerLayout.add(filterTitle);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        relationKindNameFilterField = new TextField();
        relationKindNameFilterField.setPlaceholder("关系类型名称");
        relationKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindNameFilterField.setWidth(250,Unit.PIXELS);
        relationKindsSearchElementsContainerLayout.add(relationKindNameFilterField);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        relationKindsSearchElementsContainerLayout.add(plusIcon);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        relationKindDescFilterField = new TextField();
        relationKindDescFilterField.setPlaceholder("关系类型显示名称");
        relationKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindDescFilterField.setWidth(250,Unit.PIXELS);
        relationKindsSearchElementsContainerLayout.add(relationKindDescFilterField);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindDescFilterField);

        Button searchRelationKindsButton = new Button("查找关系类型",new Icon(VaadinIcon.SEARCH));
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindsSearchElementsContainerLayout.add(searchRelationKindsButton);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationKindsButton);
        searchRelationKindsButton.setWidth(115,Unit.PIXELS);
        searchRelationKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterRelationKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        relationKindsSearchElementsContainerLayout.add(divIcon);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        relationKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterRelationKinds();
            }
        });

        relationKindsInfoContainerLayout.add(relationKindMetaInfoGridContainerLayout);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon queryIcon = new Icon(VaadinIcon.RECORDS);
            queryIcon.setSize("20px");
            Button queryRelationKind = new Button(queryIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    renderRelationKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            queryRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            queryRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(queryRelationKind, "查询关系类型实体");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configRelationKind = new Button(configIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    renderRelationKindConfigurationUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configRelationKind, "配置关系类型定义");

            Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
            cleanKindIcon.setSize("21px");
            Button cleanRelationKind = new Button(cleanKindIcon, event -> {});
            cleanRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cleanRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(cleanRelationKind, "清除关系类型所有实例");
            cleanRelationKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        renderCleanRelationKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeRelationKind = new Button(deleteKindIcon, event -> {});
            removeRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeRelationKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeRelationKind, "删除关系类型");
            removeRelationKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        renderRemoveRelationKindUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(queryRelationKind,configRelationKind, cleanRelationKind,removeRelationKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        ComponentRenderer _createDateComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            if(entityStatisticsInfo instanceof EntityStatisticsInfo && ((EntityStatisticsInfo)entityStatisticsInfo).getCreateDateTime() != null){
                ZonedDateTime createZonedDateTime = ((EntityStatisticsInfo)entityStatisticsInfo).getCreateDateTime();
                return new Label(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new Label("-");
            }
        });

        Comparator createDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((EntityStatisticsInfo)o1).getCreateDateTime()!= null && ((EntityStatisticsInfo)o2).getCreateDateTime()!= null &&
                        ((EntityStatisticsInfo)o1).getLastModifyDateTime() instanceof ZonedDateTime &&
                        ((EntityStatisticsInfo)o2).getLastModifyDateTime() instanceof ZonedDateTime){
                    if(((EntityStatisticsInfo)o1).getCreateDateTime().isBefore(((EntityStatisticsInfo)o2).getCreateDateTime())){
                        return -1;
                    }if(((EntityStatisticsInfo)o1).getCreateDateTime().isAfter(((EntityStatisticsInfo)o2).getCreateDateTime())){
                        return 1;
                    }
                }
                return 0;
            }
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        ComponentRenderer _lastUpdateDateComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            if(entityStatisticsInfo instanceof EntityStatisticsInfo && ((EntityStatisticsInfo)entityStatisticsInfo).getLastModifyDateTime() != null){
                ZonedDateTime createZonedDateTime = ((EntityStatisticsInfo)entityStatisticsInfo).getLastModifyDateTime();
                return new Label(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new Label("-");
            }
        });

        Comparator lastUpdateDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((EntityStatisticsInfo)o1).getLastModifyDateTime()!= null && ((EntityStatisticsInfo)o2).getLastModifyDateTime()!= null &&
                        ((EntityStatisticsInfo)o1).getLastModifyDateTime() instanceof ZonedDateTime &&
                        ((EntityStatisticsInfo)o2).getLastModifyDateTime() instanceof ZonedDateTime){
                    if(((EntityStatisticsInfo)o1).getLastModifyDateTime().isBefore(((EntityStatisticsInfo)o2).getLastModifyDateTime())){
                        return -1;
                    }if(((EntityStatisticsInfo)o1).getLastModifyDateTime().isAfter(((EntityStatisticsInfo)o2).getLastModifyDateTime())){
                        return 1;
                    }
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        relationKindMetaInfoGrid = new Grid<>();
        relationKindMetaInfoGrid.setWidth(1300,Unit.PIXELS);
        relationKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        relationKindMetaInfoGrid.addColumn(EntityStatisticsInfo::getEntityKindName).setHeader("关系念类型名称").setKey("idx_0");
        relationKindMetaInfoGrid.addColumn(EntityStatisticsInfo::getEntityKindDesc).setHeader("关系类型显示名称").setKey("idx_1");
        relationKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_2")
                .setComparator(createDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        relationKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_3")
                .setComparator(lastUpdateDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        relationKindMetaInfoGrid.addColumn(new NumberRenderer<>(EntityStatisticsInfo::getEntitiesCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getEntitiesCount() - entityStatisticsInfo2.getEntitiesCount()))
                .setHeader("类型包含实体数量").setKey("idx_4")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        relationKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5")
                .setFlexGrow(0).setWidth("170px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"关系类型名称");
        relationKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"关系类型显示名称");
        relationKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        relationKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        relationKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.STOCK,"类型包含实体数量");
        relationKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5);

        relationKindMetaInfoGrid.appendFooterRow();

        relationKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<EntityStatisticsInfo>, EntityStatisticsInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<EntityStatisticsInfo>, EntityStatisticsInfo> selectionEvent) {
                Set<EntityStatisticsInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    relationKindMetaInfoGrid.select(lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo);
                }else{
                    EntityStatisticsInfo selectedEntityStatisticsInfo = selectedItemSet.iterator().next();
                    renderRelationKindOverview(selectedEntityStatisticsInfo);
                    lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo = selectedEntityStatisticsInfo;
                }
            }
        });

        relationKindMetaInfoGridContainerLayout.add(relationKindMetaInfoGrid);

        singleRelationKindSummaryInfoContainerLayout = new VerticalLayout();
        singleRelationKindSummaryInfoContainerLayout.setSpacing(true);
        singleRelationKindSummaryInfoContainerLayout.setMargin(true);
        singleRelationKindSummaryInfoContainerLayout.setPadding(false);
        relationKindsInfoContainerLayout.add(singleRelationKindSummaryInfoContainerLayout);
        relationKindsInfoContainerLayout.setFlexGrow(1, singleRelationKindSummaryInfoContainerLayout);

        HorizontalLayout singleRelationKindInfoElementsContainerLayout = new HorizontalLayout();
        singleRelationKindInfoElementsContainerLayout.setSpacing(false);
        singleRelationKindInfoElementsContainerLayout.setMargin(false);
        singleRelationKindInfoElementsContainerLayout.setHeight("30px");
        singleRelationKindSummaryInfoContainerLayout.add(singleRelationKindInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"关系类型概览");
        singleRelationKindInfoElementsContainerLayout.add(filterTitle2);
        singleRelationKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONNECT_O),"-",null,null);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        singleRelationKindSummaryInfoContainerLayout.add(secondaryTitleActionBar);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"关系类型属性分布 (实体概略采样数 "+entityAttributesDistributionStatisticSampleRatio+")");
        singleRelationKindSummaryInfoContainerLayout.add(infoTitle1);

        relationKindAttributesInfoGrid = new Grid<>();
        relationKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        relationKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        relationKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        relationKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        relationKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);;
        relationKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getAttributeHitCount,NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性命中数").setKey("idx_2")
                .setFlexGrow(0).setWidth("100px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        relationKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        relationKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        relationKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        relationKindAttributesInfoGrid.setHeight(150,Unit.PIXELS);
        singleRelationKindSummaryInfoContainerLayout.add(relationKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.RANDOM),"关系类型实体关联流向");
        singleRelationKindSummaryInfoContainerLayout.add(infoTitle2);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        loadRelationKindsInfo();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            relationKindMetaInfoGrid.setHeight(event.getHeight()-280,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            relationKindMetaInfoGrid.setHeight(browserHeight-280,Unit.PIXELS);
            relationKindCorrelationInfoChart = new RelationKindCorrelationInfoChart(browserHeight-550);
            singleRelationKindSummaryInfoContainerLayout.add(relationKindCorrelationInfoChart);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadRelationKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<EntityStatisticsInfo>  entityStatisticsInfoList = null;

        entityStatisticsInfoList = coreRealm.getRelationEntitiesStatistics();
        List<EntityStatisticsInfo> relationKindEntityStatisticsInfoList = new ArrayList<>();
        for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
            if(!currentEntityStatisticsInfo.isSystemKind()) {
                relationKindEntityStatisticsInfoList.add(currentEntityStatisticsInfo);
            }
        }
        this.relationKindNameFilterField.setValue("");
        this.relationKindDescFilterField.setValue("");
        this.relationKindsMetaInfoView = relationKindMetaInfoGrid.setItems(relationKindEntityStatisticsInfoList);
        //logic to filter RelationKinds already loaded from server
        this.relationKindsMetaInfoView.addFilter(item->{
            String entityKindName = item.getEntityKindName();
            String entityKindDesc = item.getEntityKindDesc();

            boolean relationKindNameFilterResult = true;
            if(!relationKindNameFilterField.getValue().trim().equals("")){
                if(entityKindName.contains(relationKindNameFilterField.getValue().trim())){
                    relationKindNameFilterResult = true;
                }else{
                    relationKindNameFilterResult = false;
                }
            }

            boolean relationKindDescFilterResult = true;
            if(!relationKindDescFilterField.getValue().trim().equals("")){
                if(entityKindDesc.contains(relationKindDescFilterField.getValue().trim())){
                    relationKindDescFilterResult = true;
                }else{
                    relationKindDescFilterResult = false;
                }
            }
            return relationKindNameFilterResult & relationKindDescFilterResult;
        });
    }

    private void renderRelationKindOverview(EntityStatisticsInfo relationKindStatisticsInfo){
        String relationKindName = relationKindStatisticsInfo.getEntityKindName();
        String relationKindDesc = relationKindStatisticsInfo.getEntityKindDesc() != null ?
                relationKindStatisticsInfo.getEntityKindDesc():"未设置显示名称";

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        RelationKind targetRelationKind = coreRealm.getRelationKind(relationKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetRelationKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoList = targetRelationKind.getConceptionKindsRelationStatistics();
        coreRealm.closeGlobalSession();
        relationKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
        relationKindCorrelationInfoChart.clearData();
        relationKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoList);
        String relationNameText = relationKindName+ " ( "+relationKindDesc+" )";
        this.secondaryTitleActionBar.updateTitleContent(relationNameText);
    }

    private void resetSingleRelationKindSummaryInfoArea(){
        this.relationKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.secondaryTitleActionBar.updateTitleContent(" - ");
        this.relationKindCorrelationInfoChart.clearData();
    }

    private void filterRelationKinds(){
        String relationKindFilterValue = relationKindNameFilterField.getValue().trim();
        String relationKindDescFilterValue = relationKindDescFilterField.getValue().trim();
        if(relationKindFilterValue.equals("")&relationKindDescFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入关系类型名称 和/或 关系类型显示名称", NotificationVariant.LUMO_ERROR);
        }else{
            this.relationKindsMetaInfoView.refreshAll();
        }
    }

    private void cancelFilterRelationKinds(){
        relationKindNameFilterField.setValue("");
        relationKindDescFilterField.setValue("");
        this.relationKindsMetaInfoView.refreshAll();
    }

    private void renderCreateRelationKindUI(){
        CreateRelationKindView createRelationKindView = new CreateRelationKindView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建关系类型",null,true,630,335,false);
        fixSizeWindow.setWindowContent(createRelationKindView);
        fixSizeWindow.setModel(true);
        createRelationKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderCleanRelationKindEntitiesUI(EntityStatisticsInfo entityStatisticsInfo){
        String relationKindName = entityStatisticsInfo.getEntityKindName();
        CleanRelationKindEntitiesView cleanRelationKindEntitiesView = new CleanRelationKindEntitiesView(relationKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.RECYCLE),"清除关系类型所有实例",null,true,600,210,false);
        fixSizeWindow.setWindowContent(cleanRelationKindEntitiesView);
        fixSizeWindow.setModel(true);
        cleanRelationKindEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderRemoveRelationKindUI(EntityStatisticsInfo entityStatisticsInfo){
        String relationKindName = entityStatisticsInfo.getEntityKindName();
        RemoveRelationKindView removeRelationKindView = new RemoveRelationKindView(relationKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除关系类型",null,true,600,210,false);
        fixSizeWindow.setWindowContent(removeRelationKindView);
        fixSizeWindow.setModel(true);
        removeRelationKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderRelationKindQueryUI(EntityStatisticsInfo entityStatisticsInfo){
        RelationKindQueryUI relationKindQueryUI = new RelationKindQueryUI(entityStatisticsInfo.getEntityKindName());
        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("12px");
        relationKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(relationKindIcon);
        Label relationKindName = new Label(entityStatisticsInfo.getEntityKindName());
        actionComponentList.add(relationKindName);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系类型实体数据查询",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindQueryUI);
        fullScreenWindow.show();
    }

    private void renderRelationKindConfigurationUI(EntityStatisticsInfo entityStatisticsInfo){
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setSizeFull();
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.add(cancelButton);
        dialog.open();
    }

    private void renderProcessingDataListUI(Button processingDataListButton){
        ProcessingDataListView processingDataListView = new ProcessingDataListView(510);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.MAILBOX),"待处理数据",null,true,760,690,false);
        fixSizeWindow.setWindowContent(processingDataListView);
        fixSizeWindow.setModel(false);
        fixSizeWindow.show();
        fixSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {
                processingDataListButton.setEnabled(true);
            }
        });
    }

    @Override
    public void receivedRelationEntityDeletedEvent(RelationEntityDeletedEvent event) {
        if(event.getRelationKindName() != null){
            ListDataProvider dtaProvider=(ListDataProvider) relationKindMetaInfoGrid.getDataProvider();
            Collection<EntityStatisticsInfo> entityStatisticsInfoList = dtaProvider.getItems();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if( event.getRelationKindName().equals(currentEntityStatisticsInfo.getEntityKindName())){
                    long orgEntitiesCount = currentEntityStatisticsInfo.getEntitiesCount();
                    if(orgEntitiesCount >=1){
                        currentEntityStatisticsInfo.setEntitiesCount(orgEntitiesCount-1);
                    }
                }
            }
            dtaProvider.refreshAll();
        }
    }

    @Override
    public void receivedRelationKindCleanedEvent(RelationKindCleanedEvent event) {
        if(event.getRelationKindName() != null){
            if(lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo != null &&
                    lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo.getEntityKindName().equals(event.getRelationKindName())){
                renderRelationKindOverview(lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo);
            }
            ListDataProvider dtaProvider=(ListDataProvider) relationKindMetaInfoGrid.getDataProvider();
            Collection<EntityStatisticsInfo> entityStatisticsInfoList = dtaProvider.getItems();
            EntityStatisticsInfo cleanedTargetElement = null;
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(currentEntityStatisticsInfo.getEntityKindName().equals(event.getRelationKindName())){
                    cleanedTargetElement = currentEntityStatisticsInfo;
                }
            }
            if(cleanedTargetElement != null){
                cleanedTargetElement.setEntitiesCount(0);
            }
            dtaProvider.refreshAll();
        }
    }

    @Override
    public void receivedRelationKindCreatedEvent(RelationKindCreatedEvent event) {
        Date createDateTime = event.getCreateDateTime();
        Date lastModifyDateTime = event.getLastModifyDateTime();
        EntityStatisticsInfo newRelationKindEntityStatisticsInfo = new EntityStatisticsInfo(
                event.getRelationKindName(), EntityStatisticsInfo.kindType.RelationKind,false,
                0,event.getRelationKindDesc(),event.getRelationKindName(),
                ZonedDateTime.ofInstant(createDateTime.toInstant(), id),ZonedDateTime.ofInstant(lastModifyDateTime.toInstant(), id),
                event.getCreatorId(),event.getDataOrigin()
        );
        ListDataProvider dtaProvider=(ListDataProvider) relationKindMetaInfoGrid.getDataProvider();
        dtaProvider.getItems().add(newRelationKindEntityStatisticsInfo);
        dtaProvider.refreshAll();
    }

    @Override
    public void receivedRelationKindRemovedEvent(RelationKindRemovedEvent event) {
        if(event.getRelationKindName() != null){
            ListDataProvider dtaProvider=(ListDataProvider) relationKindMetaInfoGrid.getDataProvider();
            Collection<EntityStatisticsInfo> entityStatisticsInfoList = dtaProvider.getItems();
            EntityStatisticsInfo removeTargetElement = null;
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(currentEntityStatisticsInfo.getEntityKindName().equals(event.getRelationKindName())){
                    removeTargetElement = currentEntityStatisticsInfo;
                }
            }
            if(removeTargetElement != null){
                dtaProvider.getItems().remove(removeTargetElement);
            }
            dtaProvider.refreshAll();

            if(lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo != null &&
                    lastSelectedRelationKindMetaInfoGridEntityStatisticsInfo.getEntityKindName().equals(event.getRelationKindName())){
                resetSingleRelationKindSummaryInfoArea();
            }
        }
    }

    private void renderRelationKindsCorrelationInfoSummaryUI(Button relationKindRelationGuideButton){
        RelationKindsCorrelationInfoSummaryChart relationKindsCorrelationInfoSummaryChart = new RelationKindsCorrelationInfoSummaryChart(1180,800);
        /*
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = systemMaintenanceOperator.
                getSystemConceptionKindsRelationDistributionStatistics();
        relationKindsCorrelationInfoSummaryChart.setData(conceptionKindCorrelationInfoSet);
        */
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.SITEMAP),"关系类型实体实时关联分布概览",null,true,1200,900,false);
        fixSizeWindow.setWindowContent(relationKindsCorrelationInfoSummaryChart);
        fixSizeWindow.show();
        fixSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {
                relationKindRelationGuideButton.setEnabled(true);
            }
        });
    }
}
