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

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityDeletedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindCleanedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindCreatedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList.ProcessingDataListView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.*;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.ConceptionKindQueryUI;

import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ConceptionKindManagementUI extends VerticalLayout implements
        ConceptionKindCreatedEvent.ConceptionKindCreatedListener,
        ConceptionKindCleanedEvent.ConceptionKindCleanedListener,
        ConceptionKindRemovedEvent.ConceptionKindRemovedListener,
        ConceptionEntityDeletedEvent.ConceptionEntityDeletedListener{

    private Grid<EntityStatisticsInfo> conceptionKindMetaInfoGrid;
    private Registration listener;
    private SecondaryTitleActionBar secondaryTitleActionBar;
    private int entityAttributesDistributionStatisticSampleRatio = 10000;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;
    private ConceptionKindCorrelationInfoChart conceptionKindCorrelationInfoChart;
    private VerticalLayout singleConceptionKindSummaryInfoContainerLayout;
    private EntityStatisticsInfo lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo;
    final ZoneId id = ZoneId.systemDefault();
    private TextField conceptionKindNameFilterField;
    private TextField conceptionKindDescFilterField;
    private GridListDataView<EntityStatisticsInfo> conceptionKindsMetaInfoView;

    public ConceptionKindManagementUI(){
        Button refreshDataButton = new Button("刷新概念类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            loadConceptionKindsInfo();
            resetSingleConceptionKindSummaryInfoArea();
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

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Conception Kind 概念类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> conceptionKindManagementOperationButtonList = new ArrayList<>();

        Button conceptionKindRelationGuideButton = new Button("概念实体关联分布概览",new Icon(VaadinIcon.DASHBOARD));
        conceptionKindRelationGuideButton.setDisableOnClick(true);
        conceptionKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        conceptionKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindManagementOperationButtonList.add(conceptionKindRelationGuideButton);
        conceptionKindRelationGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderConceptionKindsCorrelationInfoSummaryUI(conceptionKindRelationGuideButton);
            }
        });

        Button processingDataListButton = new Button("待处理数据",new Icon(VaadinIcon.MAILBOX));
        processingDataListButton.setDisableOnClick(true);
        processingDataListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        processingDataListButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindManagementOperationButtonList.add(processingDataListButton);
        processingDataListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderProcessingDataListUI(processingDataListButton);
            }
        });

        Button createConceptionKindButton = new Button("创建概念类型",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindManagementOperationButtonList.add(createConceptionKindButton);
        createConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateConceptionKindUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"概念类型定义:",conceptionKindManagementOperationButtonList);
        add(sectionActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon queryIcon = new Icon(VaadinIcon.RECORDS);
            queryIcon.setSize("20px");
            Button queryConceptionKind = new Button(queryIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            queryConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            queryConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(queryConceptionKind, "查询概念类型实体");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    renderConceptionKindConfigurationUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configConceptionKind, "配置概念类型定义");

            Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
            cleanKindIcon.setSize("21px");
            Button cleanConceptionKind = new Button(cleanKindIcon, event -> {});
            cleanConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cleanConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(cleanConceptionKind, "清除概念类型所有实例");
            cleanConceptionKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        renderCleanConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeConceptionKind = new Button(deleteKindIcon, event -> {});
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeConceptionKind, "删除概念类型");
            removeConceptionKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        renderRemoveConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(queryConceptionKind,configConceptionKind, cleanConceptionKind,removeConceptionKind);
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

        conceptionKindMetaInfoGrid = new Grid<>();
        conceptionKindMetaInfoGrid.setWidth(1300,Unit.PIXELS);
        conceptionKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindMetaInfoGrid.addColumn(EntityStatisticsInfo::getEntityKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindMetaInfoGrid.addColumn(EntityStatisticsInfo::getEntityKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        conceptionKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_2")
                .setComparator(createDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        conceptionKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_3")
                .setComparator(lastUpdateDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        conceptionKindMetaInfoGrid.addColumn(new NumberRenderer<>(EntityStatisticsInfo::getEntitiesCount,NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getEntitiesCount() - entityStatisticsInfo2.getEntitiesCount()))
                .setHeader("类型包含实体数量").setKey("idx_4")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        conceptionKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5")
                .setFlexGrow(0).setWidth("170px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.STOCK,"类型包含实体数量");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5);

        conceptionKindMetaInfoGrid.appendFooterRow();

        conceptionKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<EntityStatisticsInfo>, EntityStatisticsInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<EntityStatisticsInfo>, EntityStatisticsInfo> selectionEvent) {
                Set<EntityStatisticsInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    conceptionKindMetaInfoGrid.select(lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo);
                }else{
                    //singleConceptionKindSummaryInfoContainerLayout.setVisible(true);
                    EntityStatisticsInfo selectedEntityStatisticsInfo = selectedItemSet.iterator().next();
                    renderConceptionKindOverview(selectedEntityStatisticsInfo);
                    lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo = selectedEntityStatisticsInfo;
                }
            }
        });

        HorizontalLayout conceptionKindsInfoContainerLayout = new HorizontalLayout();
        conceptionKindsInfoContainerLayout.setSpacing(false);
        conceptionKindsInfoContainerLayout.setMargin(false);
        conceptionKindsInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);

        VerticalLayout conceptionKindMetaInfoGridContainerLayout = new VerticalLayout();
        conceptionKindMetaInfoGridContainerLayout.setSpacing(true);
        conceptionKindMetaInfoGridContainerLayout.setMargin(false);
        conceptionKindMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout conceptionKindsSearchElementsContainerLayout = new HorizontalLayout();
        conceptionKindsSearchElementsContainerLayout.setSpacing(false);
        conceptionKindsSearchElementsContainerLayout.setMargin(false);
        conceptionKindMetaInfoGridContainerLayout.add(conceptionKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        conceptionKindsSearchElementsContainerLayout.add(filterTitle);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        conceptionKindNameFilterField = new TextField();
        conceptionKindNameFilterField.setPlaceholder("概念类型名称");
        conceptionKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindNameFilterField.setWidth(250,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindNameFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        conceptionKindsSearchElementsContainerLayout.add(plusIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        conceptionKindDescFilterField = new TextField();
        conceptionKindDescFilterField.setPlaceholder("概念类型显示名称");
        conceptionKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindDescFilterField.setWidth(250,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindDescFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindDescFilterField);

        Button searchConceptionKindsButton = new Button("查找概念类型",new Icon(VaadinIcon.SEARCH));
        searchConceptionKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchConceptionKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindsSearchElementsContainerLayout.add(searchConceptionKindsButton);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchConceptionKindsButton);
        searchConceptionKindsButton.setWidth(115,Unit.PIXELS);
        searchConceptionKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterConceptionKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        conceptionKindsSearchElementsContainerLayout.add(divIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterConceptionKinds();
            }
        });

        conceptionKindMetaInfoGridContainerLayout.add(conceptionKindMetaInfoGrid);

        conceptionKindsInfoContainerLayout.add(conceptionKindMetaInfoGridContainerLayout);

        singleConceptionKindSummaryInfoContainerLayout = new VerticalLayout();
        singleConceptionKindSummaryInfoContainerLayout.setSpacing(true);
        singleConceptionKindSummaryInfoContainerLayout.setMargin(true);
        singleConceptionKindSummaryInfoContainerLayout.setPadding(false);
        conceptionKindsInfoContainerLayout.add(singleConceptionKindSummaryInfoContainerLayout);
        conceptionKindsInfoContainerLayout.setFlexGrow(1,singleConceptionKindSummaryInfoContainerLayout);

        HorizontalLayout singleConceptionKindInfoElementsContainerLayout = new HorizontalLayout();
        singleConceptionKindInfoElementsContainerLayout.setSpacing(false);
        singleConceptionKindInfoElementsContainerLayout.setMargin(false);
        singleConceptionKindInfoElementsContainerLayout.setHeight("30px");
        singleConceptionKindSummaryInfoContainerLayout.add(singleConceptionKindInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"概念类型概览");
        singleConceptionKindInfoElementsContainerLayout.add(filterTitle2);
        singleConceptionKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        singleConceptionKindSummaryInfoContainerLayout.add(secondaryTitleActionBar);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+entityAttributesDistributionStatisticSampleRatio+")");
        singleConceptionKindSummaryInfoContainerLayout.add(infoTitle1);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);;
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getAttributeHitCount,NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性命中数").setKey("idx_2")
                .setFlexGrow(0).setWidth("100px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        conceptionKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        singleConceptionKindSummaryInfoContainerLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT),"概念类型实体关联分布");
        singleConceptionKindSummaryInfoContainerLayout.add(infoTitle2);
        //singleConceptionKindSummaryInfoContainerLayout.setVisible(false);
        add(conceptionKindsInfoContainerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        loadConceptionKindsInfo();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionKindMetaInfoGrid.setHeight(event.getHeight()-280,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionKindMetaInfoGrid.setHeight(browserHeight-280,Unit.PIXELS);
            conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(browserHeight-600);
            singleConceptionKindSummaryInfoContainerLayout.add(conceptionKindCorrelationInfoChart);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<EntityStatisticsInfo>  entityStatisticsInfoList = null;
        try {
            entityStatisticsInfoList = coreRealm.getConceptionEntitiesStatistics();
            List<EntityStatisticsInfo> conceptionKindEntityStatisticsInfoList = new ArrayList<>();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(!currentEntityStatisticsInfo.isSystemKind()) {
                    conceptionKindEntityStatisticsInfoList.add(currentEntityStatisticsInfo);
                }
            }
            this.conceptionKindNameFilterField.setValue("");
            this.conceptionKindDescFilterField.setValue("");
            this.conceptionKindsMetaInfoView = conceptionKindMetaInfoGrid.setItems(conceptionKindEntityStatisticsInfoList);
            //logic to filter ConceptionKinds already loaded from server
            this.conceptionKindsMetaInfoView.addFilter(item->{
                String entityKindName = item.getEntityKindName();
                String entityKindDesc = item.getEntityKindDesc();

                boolean conceptionKindNameFilterResult = true;
                if(!conceptionKindNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(conceptionKindNameFilterField.getValue().trim())){
                        conceptionKindNameFilterResult = true;
                    }else{
                        conceptionKindNameFilterResult = false;
                    }
                }

                boolean conceptionKindDescFilterResult = true;
                if(!conceptionKindDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(conceptionKindDescFilterField.getValue().trim())){
                        conceptionKindDescFilterResult = true;
                    }else{
                        conceptionKindDescFilterResult = false;
                    }
                }
                return conceptionKindNameFilterResult & conceptionKindDescFilterResult;
            });

        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderConceptionKindOverview(EntityStatisticsInfo conceptionKindStatisticsInfo){
        String conceptionKindName = conceptionKindStatisticsInfo.getEntityKindName();
        String conceptionKindDesc = conceptionKindStatisticsInfo.getEntityKindDesc() != null ?
                conceptionKindStatisticsInfo.getEntityKindDesc():"未设置显示名称";

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);

        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
        coreRealm.closeGlobalSession();

        conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
        conceptionKindCorrelationInfoChart.clearData();
        conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,conceptionKindName);

        String conceptionNameText = conceptionKindName+ " ( "+conceptionKindDesc+" )";
        this.secondaryTitleActionBar.updateTitleContent(conceptionNameText);
    }

    private void resetSingleConceptionKindSummaryInfoArea(){
        this.conceptionKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.secondaryTitleActionBar.updateTitleContent(" - ");
        this.conceptionKindCorrelationInfoChart.clearData();
    }

    private void renderConceptionKindConfigurationUI(EntityStatisticsInfo entityStatisticsInfo){
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

    private void renderConceptionKindQueryUI(EntityStatisticsInfo entityStatisticsInfo){
        ConceptionKindQueryUI conceptionKindQueryUI = new ConceptionKindQueryUI(entityStatisticsInfo.getEntityKindName());
        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(conceptionKindIcon);
        Label conceptionKindName = new Label(entityStatisticsInfo.getEntityKindName());
        actionComponentList.add(conceptionKindName);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念类型实体数据查询",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionKindQueryUI);
        fullScreenWindow.show();
    }

    private void renderConceptionKindsCorrelationInfoSummaryUI(Button conceptionKindRelationGuideButton){
        // Method 1 use IFrame to load
        /*
        UI.getCurrent().getPage().fetchCurrentURL(currentUrl -> {
            // This is your own method that you may do something with the url.
            // Please note that this method runs asynchronously
            String conceptionKindsCorrelationInfoSummaryViewURL = currentUrl.toString()+"conceptionKindsCorrelationInfoSummary";
            IFrame _IFrame = new IFrame();
            _IFrame.getStyle().set("border","0");
            _IFrame.setSrc(conceptionKindsCorrelationInfoSummaryViewURL);
            _IFrame.setHeight(820, Unit.PIXELS);
            _IFrame.setWidth(1160,Unit.PIXELS);

            FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.SITEMAP),"概念类型实体实时关联分布概览",null,true,1200,900,false);
            fixSizeWindow.setWindowContent(_IFrame);
            fixSizeWindow.show();
        });
        */

        // Method 2 direct use chart
        ConceptionKindsCorrelationInfoSummaryChart conceptionKindsCorrelationInfoSummaryChart = new ConceptionKindsCorrelationInfoSummaryChart(1180,800);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = systemMaintenanceOperator.
                getSystemConceptionKindsRelationDistributionStatistics();
        conceptionKindsCorrelationInfoSummaryChart.setData(conceptionKindCorrelationInfoSet);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DASHBOARD),"概念类型实体实时关联分布概览",null,true,1200,900,false);
        fixSizeWindow.setWindowContent(conceptionKindsCorrelationInfoSummaryChart);
        fixSizeWindow.show();
        fixSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {
                conceptionKindRelationGuideButton.setEnabled(true);
            }
        });
    }

    private void renderCreateConceptionKindUI(){
        CreateConceptionKindView createConceptionKindView = new CreateConceptionKindView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建概念类型",null,true,630,335,false);
        fixSizeWindow.setWindowContent(createConceptionKindView);
        fixSizeWindow.setModel(true);
        createConceptionKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    public void receivedConceptionKindCreatedEvent(ConceptionKindCreatedEvent event) {
        Date createDateTime = event.getCreateDateTime();
        Date lastModifyDateTime = event.getLastModifyDateTime();
        EntityStatisticsInfo newConceptionKindEntityStatisticsInfo = new EntityStatisticsInfo(
                event.getConceptionKindName(), EntityStatisticsInfo.kindType.ConceptionKind,false,
                0,event.getConceptionKindDesc(),event.getConceptionKindName(),
                ZonedDateTime.ofInstant(createDateTime.toInstant(), id),ZonedDateTime.ofInstant(lastModifyDateTime.toInstant(), id),
                        event.getCreatorId(),event.getDataOrigin()
        );
        ListDataProvider dtaProvider=(ListDataProvider)conceptionKindMetaInfoGrid.getDataProvider();
        dtaProvider.getItems().add(newConceptionKindEntityStatisticsInfo);
        dtaProvider.refreshAll();
    }

    private void renderCleanConceptionKindEntitiesUI(EntityStatisticsInfo entityStatisticsInfo){
        String conceptionKindName = entityStatisticsInfo.getEntityKindName();
        CleanConceptionKindEntitiesView cleanConceptionKindEntitiesView = new CleanConceptionKindEntitiesView(conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.RECYCLE),"清除概念类型所有实例",null,true,600,210,false);
        fixSizeWindow.setWindowContent(cleanConceptionKindEntitiesView);
        fixSizeWindow.setModel(true);
        cleanConceptionKindEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    public void receivedConceptionKindCleanedEvent(ConceptionKindCleanedEvent event) {
        if(event.getConceptionKindName() != null){
            if(lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo != null &&
                    lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo.getEntityKindName().equals(event.getConceptionKindName())){
                renderConceptionKindOverview(lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo);
            }
            ListDataProvider dtaProvider=(ListDataProvider)conceptionKindMetaInfoGrid.getDataProvider();
            Collection<EntityStatisticsInfo> entityStatisticsInfoList = dtaProvider.getItems();
            EntityStatisticsInfo cleanedTargetElement = null;
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(currentEntityStatisticsInfo.getEntityKindName().equals(event.getConceptionKindName())){
                    cleanedTargetElement = currentEntityStatisticsInfo;
                }
            }
            if(cleanedTargetElement != null){
                cleanedTargetElement.setEntitiesCount(0);
            }
            dtaProvider.refreshAll();
        }
    }

    private void renderRemoveConceptionKindEntitiesUI(EntityStatisticsInfo entityStatisticsInfo){
        String conceptionKindName = entityStatisticsInfo.getEntityKindName();
        RemoveConceptionKindView removeConceptionKindView = new RemoveConceptionKindView(conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除概念类型",null,true,600,210,false);
        fixSizeWindow.setWindowContent(removeConceptionKindView);
        fixSizeWindow.setModel(true);
        removeConceptionKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    public void receivedConceptionKindRemovedEvent(ConceptionKindRemovedEvent event) {
        if(event.getConceptionKindName() != null){
            ListDataProvider dtaProvider=(ListDataProvider)conceptionKindMetaInfoGrid.getDataProvider();
            Collection<EntityStatisticsInfo> entityStatisticsInfoList = dtaProvider.getItems();
            EntityStatisticsInfo removeTargetElement = null;
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(currentEntityStatisticsInfo.getEntityKindName().equals(event.getConceptionKindName())){
                    removeTargetElement = currentEntityStatisticsInfo;
                }
            }
            if(removeTargetElement != null){
                dtaProvider.getItems().remove(removeTargetElement);
            }
            dtaProvider.refreshAll();

            if(lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo != null &&
                    lastSelectedConceptionKindMetaInfoGridEntityStatisticsInfo.getEntityKindName().equals(event.getConceptionKindName())){
                resetSingleConceptionKindSummaryInfoArea();
            }
        }
    }

    private void filterConceptionKinds(){
        String conceptionKindFilterValue = conceptionKindNameFilterField.getValue().trim();
        String conceptionKindDescFilterValue = conceptionKindDescFilterField.getValue().trim();
        if(conceptionKindFilterValue.equals("")&conceptionKindDescFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入概念类型名称 和/或 概念类型显示名称", NotificationVariant.LUMO_ERROR);
        }else{
            this.conceptionKindsMetaInfoView.refreshAll();
        }
    }

    private void cancelFilterConceptionKinds(){
        conceptionKindNameFilterField.setValue("");
        conceptionKindDescFilterField.setValue("");
        this.conceptionKindsMetaInfoView.refreshAll();
    }

    @Override
    public void receivedConceptionEntityDeletedEvent(ConceptionEntityDeletedEvent event) {
        if(event.getEntityAllConceptionKindNames() != null){
            List<String> entityAllConceptionKindNamesList = event.getEntityAllConceptionKindNames();
            ListDataProvider dtaProvider=(ListDataProvider)conceptionKindMetaInfoGrid.getDataProvider();
            Collection<EntityStatisticsInfo> entityStatisticsInfoList = dtaProvider.getItems();
            for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
                if(entityAllConceptionKindNamesList.contains(currentEntityStatisticsInfo.getEntityKindName())){
                    long orgEntitiesCount = currentEntityStatisticsInfo.getEntitiesCount();
                    if(orgEntitiesCount >=1){
                        currentEntityStatisticsInfo.setEntitiesCount(orgEntitiesCount-1);
                    }
                }
            }
            dtaProvider.refreshAll();
        }
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
}
