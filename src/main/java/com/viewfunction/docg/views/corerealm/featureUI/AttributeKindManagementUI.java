package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.NativeLabel;
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
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AttributeKindAttachedToAttributesViewKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributeKindCreatedEvent;
import com.viewfunction.docg.element.eventHandling.AttributeKindRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.CreateAttributeKindView;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.RemoveAttributeKindView;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind.AttributeKindDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;

import dev.mett.vaadin.tooltip.Tooltips;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class AttributeKindManagementUI extends VerticalLayout implements
        AttributeKindCreatedEvent.AttributeKindCreatedListener,
        AttributeKindRemovedEvent.AttributeKindRemovedListener,
        AttributeKindAttachedToAttributesViewKindEvent.AttributeKindAttachedToAttributesViewKindListener{
    private Grid<AttributeKindMetaInfo> attributeKindMetaInfoGrid;
    private GridListDataView<AttributeKindMetaInfo> attributeKindsMetaInfoView;
    private Registration listener;
    private SecondaryTitleActionBar secondaryTitleActionBar;
    private SecondaryTitleActionBar secondaryTitleActionBar2;
    private Grid<AttributesViewKind> attributeKindAttributesInfoGrid;
    private ConceptionKindCorrelationInfoChart conceptionKindCorrelationInfoChart;
    private VerticalLayout singleAttributeKindSummaryInfoContainerLayout;
    private AttributeKindMetaInfo lastSelectedAttributeKindMetaInfoGridAttributeKindMetaInfo;
    final ZoneId id = ZoneId.systemDefault();
    private TextField attributeKindNameFilterField;
    private TextField attributeKindDescFilterField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;
    public AttributeKindManagementUI(){

        Button refreshDataButton = new Button("刷新属性类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            loadAttributeKindsInfo();
            resetSingleAttributeKindSummaryInfoArea();
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

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Attribute Kind 属性类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> attributeKindManagementOperationButtonList = new ArrayList<>();

        Button attributeKindRuntimeStatusGuideButton = new Button("实体属性分布概览",new Icon(VaadinIcon.DASHBOARD));
        attributeKindRuntimeStatusGuideButton.setDisableOnClick(true);
        attributeKindRuntimeStatusGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        attributeKindRuntimeStatusGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindManagementOperationButtonList.add(attributeKindRuntimeStatusGuideButton);
        attributeKindRuntimeStatusGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindsCorrelationInfoSummaryUI(attributeKindRuntimeStatusGuideButton);
            }
        });

        Button createAttributeKindButton = new Button("创建属性类型",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindManagementOperationButtonList.add(createAttributeKindButton);
        createAttributeKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddAttributeKindView();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"属性类型定义:",attributeKindManagementOperationButtonList);
        add(sectionActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributeKindMetaInfo -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributeKind = new Button(configIcon, event -> {
                if(attributeKindMetaInfo instanceof AttributeKindMetaInfo){
                    renderAttributeKindConfigurationUI((AttributeKindMetaInfo)attributeKindMetaInfo);
                }
            });
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configAttributeKind, "配置属性类型定义");

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKind = new Button(deleteKindIcon, event -> {});
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeAttributeKind, "删除属性类型定义");
            removeAttributeKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributeKindMetaInfo instanceof AttributeKindMetaInfo){
                        renderRemoveAttributeKindEntitiesUI((AttributeKindMetaInfo)attributeKindMetaInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configAttributeKind,removeAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        ComponentRenderer _createDateComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            if(entityStatisticsInfo instanceof AttributeKindMetaInfo && ((AttributeKindMetaInfo)entityStatisticsInfo).getCreateDate() != null){
                ZonedDateTime createZonedDateTime = ((AttributeKindMetaInfo)entityStatisticsInfo).getCreateDate();
                return new NativeLabel(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new NativeLabel("-");
            }
        });

        Comparator createDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((AttributeKindMetaInfo)o1).getCreateDate()!= null && ((AttributeKindMetaInfo)o2).getCreateDate()!= null &&
                        ((AttributeKindMetaInfo)o1).getCreateDate() instanceof ZonedDateTime &&
                        ((AttributeKindMetaInfo)o2).getCreateDate() instanceof ZonedDateTime){
                    if(((AttributeKindMetaInfo)o1).getCreateDate().isBefore(((AttributeKindMetaInfo)o2).getCreateDate())){
                        return -1;
                    }if(((AttributeKindMetaInfo)o1).getCreateDate().isAfter(((AttributeKindMetaInfo)o2).getCreateDate())){
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
            if(entityStatisticsInfo instanceof AttributeKindMetaInfo && ((AttributeKindMetaInfo)entityStatisticsInfo).getLastModifyDate() != null){
                ZonedDateTime createZonedDateTime = ((AttributeKindMetaInfo)entityStatisticsInfo).getLastModifyDate();
                return new NativeLabel(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new NativeLabel("-");
            }
        });

        Comparator lastUpdateDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((AttributeKindMetaInfo)o1).getLastModifyDate()!= null && ((AttributeKindMetaInfo)o2).getLastModifyDate()!= null &&
                        ((AttributeKindMetaInfo)o1).getLastModifyDate() instanceof ZonedDateTime &&
                        ((AttributeKindMetaInfo)o2).getLastModifyDate() instanceof ZonedDateTime){
                    if(((AttributeKindMetaInfo)o1).getLastModifyDate().isBefore(((AttributeKindMetaInfo)o2).getLastModifyDate())){
                        return -1;
                    }if(((AttributeKindMetaInfo)o1).getLastModifyDate().isAfter(((AttributeKindMetaInfo)o2).getLastModifyDate())){
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

        attributeKindMetaInfoGrid = new Grid<>();
        attributeKindMetaInfoGrid.setWidth(1300,Unit.PIXELS);
        attributeKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributeKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindName).setHeader("属性类型名称").setKey("idx_0").setFlexGrow(1);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindDesc).setHeader("属性类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getKindDesc());
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getAttributeDataType).setHeader("属性数据类型").setKey("idx_2")
                .setFlexGrow(0).setWidth("130px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindUID).setHeader("属性类型 UID").setKey("idx_3")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_4")
                .setComparator(createDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_5")
                .setComparator(lastUpdateDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_6").setFlexGrow(0).setWidth("90px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"属性类型描述");
        attributeKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"属性数据类型");
        attributeKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        attributeKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        attributeKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5).setSortable(true);
        GridColumnHeader gridColumnHeader_idx6 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributeKindMetaInfoGrid.getColumnByKey("idx_6").setHeader(gridColumnHeader_idx6);

        attributeKindMetaInfoGrid.appendFooterRow();

        attributeKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<AttributeKindMetaInfo>, AttributeKindMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<AttributeKindMetaInfo>, AttributeKindMetaInfo> selectionEvent) {
                Set<AttributeKindMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    attributeKindMetaInfoGrid.select(lastSelectedAttributeKindMetaInfoGridAttributeKindMetaInfo);
                }else{
                    AttributeKindMetaInfo selectedEntityStatisticsInfo = selectedItemSet.iterator().next();
                    renderAttributeKindOverview(selectedEntityStatisticsInfo);
                    lastSelectedAttributeKindMetaInfoGridAttributeKindMetaInfo = selectedEntityStatisticsInfo;
                }
            }
        });

        HorizontalLayout attributeKindsInfoContainerLayout = new HorizontalLayout();
        attributeKindsInfoContainerLayout.setSpacing(false);
        attributeKindsInfoContainerLayout.setMargin(false);
        attributeKindsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);

        VerticalLayout attributeKindMetaInfoGridContainerLayout = new VerticalLayout();
        attributeKindMetaInfoGridContainerLayout.setSpacing(true);
        attributeKindMetaInfoGridContainerLayout.setMargin(false);
        attributeKindMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout attributeKindsSearchElementsContainerLayout = new HorizontalLayout();
        attributeKindsSearchElementsContainerLayout.setSpacing(false);
        attributeKindsSearchElementsContainerLayout.setMargin(false);
        attributeKindMetaInfoGridContainerLayout.add(attributeKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        attributeKindsSearchElementsContainerLayout.add(filterTitle);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        attributeKindNameFilterField = new TextField();
        attributeKindNameFilterField.setPlaceholder("属性类型名称");
        attributeKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributeKindNameFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributeKindNameFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributeKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        attributeKindDescFilterField = new TextField();
        attributeKindDescFilterField.setPlaceholder("属性类型描述");
        attributeKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributeKindDescFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributeKindDescFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributeKindDescFilterField);

        Icon plusIcon2 = new Icon(VaadinIcon.PLUS);
        plusIcon2.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon2);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon2);

        attributeDataTypeFilterSelect = new ComboBox();
        attributeDataTypeFilterSelect.setPlaceholder("属性类型数据类型");
        attributeDataTypeFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        attributeKindDescFilterField.setWidth(170,Unit.PIXELS);
        attributeDataTypeFilterSelect.setPageSize(30);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeDataType[] attributeDataTypesArray = coreRealm.getStorageImplTech().equals(CoreRealmStorageImplTech.NEO4J) ?
                new AttributeDataType[]{
                        AttributeDataType.BOOLEAN,
                        AttributeDataType.LONG,
                        AttributeDataType.DOUBLE,
                        AttributeDataType.TIMESTAMP,
                        AttributeDataType.DATE,
                        AttributeDataType.DATETIME,
                        AttributeDataType.TIME,
                        AttributeDataType.STRING
                }
                :
                new AttributeDataType[]{
                        AttributeDataType.BOOLEAN,
                        AttributeDataType.INT,
                        AttributeDataType.SHORT,
                        AttributeDataType.LONG,
                        AttributeDataType.FLOAT,
                        AttributeDataType.DOUBLE,
                        AttributeDataType.TIMESTAMP,
                        AttributeDataType.DATE,
                        AttributeDataType.DATETIME,
                        AttributeDataType.TIME,
                        AttributeDataType.STRING,
                        AttributeDataType.BYTE,
                        AttributeDataType.DECIMAL
        };
        this.attributeDataTypeFilterSelect.setItems(attributeDataTypesArray);

        attributeKindsSearchElementsContainerLayout.add(attributeDataTypeFilterSelect);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeDataTypeFilterSelect);

        Button searchAttributeKindsButton = new Button("查找属性类型",new Icon(VaadinIcon.SEARCH));
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindsSearchElementsContainerLayout.add(searchAttributeKindsButton);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchAttributeKindsButton);
        searchAttributeKindsButton.setWidth(115,Unit.PIXELS);
        searchAttributeKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterAttributeKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        attributeKindsSearchElementsContainerLayout.add(divIcon);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterAttributeKinds();
            }
        });

        attributeKindMetaInfoGridContainerLayout.add(attributeKindMetaInfoGrid);
        attributeKindsInfoContainerLayout.add(attributeKindMetaInfoGridContainerLayout);

        singleAttributeKindSummaryInfoContainerLayout = new VerticalLayout();
        singleAttributeKindSummaryInfoContainerLayout.setSpacing(true);
        singleAttributeKindSummaryInfoContainerLayout.setMargin(true);
        singleAttributeKindSummaryInfoContainerLayout.setPadding(false);
        attributeKindsInfoContainerLayout.add(singleAttributeKindSummaryInfoContainerLayout);
        attributeKindsInfoContainerLayout.setFlexGrow(1, singleAttributeKindSummaryInfoContainerLayout);

        HorizontalLayout singleAttributeKindInfoElementsContainerLayout = new HorizontalLayout();
        singleAttributeKindInfoElementsContainerLayout.setSpacing(false);
        singleAttributeKindInfoElementsContainerLayout.setMargin(false);
        singleAttributeKindInfoElementsContainerLayout.setHeight("30px");
        singleAttributeKindSummaryInfoContainerLayout.add(singleAttributeKindInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"属性类型概览");
        singleAttributeKindInfoElementsContainerLayout.add(filterTitle2);
        singleAttributeKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"-",null,null,false);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        singleAttributeKindSummaryInfoContainerLayout.add(secondaryTitleActionBar);

        secondaryTitleActionBar2 = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        singleAttributeKindSummaryInfoContainerLayout.add(secondaryTitleActionBar2);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"所属属性视图类型");
        singleAttributeKindSummaryInfoContainerLayout.add(infoTitle1);

        attributeKindAttributesInfoGrid = new Grid<>();
        attributeKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        attributeKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        attributeKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindName).setHeader("属性视图名称").setKey("idx_0");
        attributeKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindUID).setHeader("属性视图 UID").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindDataForm).setHeader("数据存储结构").setKey("idx_2").setFlexGrow(0).setWidth("150px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性视图类型名称");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.KEY_O,"属性视图类型 UID");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.COMBOBOX,"视图数据存储结构");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        attributeKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        singleAttributeKindSummaryInfoContainerLayout.add(attributeKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.SCATTER_CHART),"属性类型实体数据分布");
        singleAttributeKindSummaryInfoContainerLayout.add(infoTitle2);
        add(attributeKindsInfoContainerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        loadAttributeKindsInfo();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            attributeKindMetaInfoGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            attributeKindMetaInfoGrid.setHeight(browserHeight-250,Unit.PIXELS);
            //conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(browserHeight-600);
            //singleConceptionKindSummaryInfoContainerLayout.add(conceptionKindCorrelationInfoChart);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderAddAttributeKindView(){
        CreateAttributeKindView createAttributeKindView = new CreateAttributeKindView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建属性类型",null,true,500,350,false);
        fixSizeWindow.setWindowContent(createAttributeKindView);
        createAttributeKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void loadAttributeKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<AttributeKindMetaInfo> gridAttributeKindMetaInfoList = new ArrayList<>();
        try {
            List<AttributeKindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getAttributeKindsMetaInfo();
            gridAttributeKindMetaInfoList.addAll(runtimeAttributeKindMetaInfoList);
            this.attributeKindNameFilterField.setValue("");
            this.attributeKindDescFilterField.setValue("");
            this.attributeDataTypeFilterSelect.setValue(null);
            this.attributeKindsMetaInfoView = attributeKindMetaInfoGrid.setItems(gridAttributeKindMetaInfoList);
            //logic to filter AttributeKinds already loaded from server
            this.attributeKindsMetaInfoView.addFilter(item->{
                String entityKindName = item.getKindName();
                String entityKindDesc = item.getKindDesc();
                String entityDataType = item.getAttributeDataType();
                boolean attributeKindNameFilterResult = true;
                if(!attributeKindNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(attributeKindNameFilterField.getValue().trim())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }

                boolean attributeKindDescFilterResult = true;
                if(!attributeKindDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(attributeKindDescFilterField.getValue().trim())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }

                boolean attributeDataTypeFilterResult = true;
                if(attributeDataTypeFilterSelect.getValue() != null){
                    if(entityDataType.equals(attributeDataTypeFilterSelect.getValue().toString())){
                        attributeDataTypeFilterResult = true;
                    }else{
                        attributeDataTypeFilterResult = false;
                    }
                }
                return attributeKindNameFilterResult & attributeKindDescFilterResult & attributeDataTypeFilterResult;
            });
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void filterAttributeKinds(){
        String attributeKindFilterValue = attributeKindNameFilterField.getValue().trim();
        String attributeKindDescFilterValue = attributeKindDescFilterField.getValue().trim();
        AttributeDataType dataType= attributeDataTypeFilterSelect.getValue();
        if(attributeKindFilterValue.equals("")&attributeKindDescFilterValue.equals("")&dataType == null){
            CommonUIOperationUtil.showPopupNotification("请输入属性类型名称 和/或 属性类型描述 和/或 属性数据类型", NotificationVariant.LUMO_ERROR);
        }else{
            this.attributeKindsMetaInfoView.refreshAll();
        }
    }

    private void cancelFilterAttributeKinds(){
        attributeKindNameFilterField.setValue("");
        attributeKindDescFilterField.setValue("");
        attributeDataTypeFilterSelect.setValue(null);
        this.attributeKindsMetaInfoView.refreshAll();
    }

    @Override
    public void receivedAttributeKindCreatedEvent(AttributeKindCreatedEvent event) {
        String kindName = event.getAttributeKindName();
        String kindDesc = event.getAttributeKindDesc();
        String kindUID = event.getAttributeKindUID();
        String attributeKindDataType = event.getAttributeKindDataType().toString();
        Date createDateTime = event.getCreateDateTime();
        Date lastModifyDateTime = event.getLastModifyDateTime();
        String creatorId = event.getCreatorId();
        String dataOrigin = event.getDataOrigin();

        ListDataProvider dtaProvider = (ListDataProvider)attributeKindMetaInfoGrid.getDataProvider();

        AttributeKindMetaInfo attributeKindMetaInfo= new AttributeKindMetaInfo(kindName,kindDesc,kindUID,
                attributeKindDataType,ZonedDateTime.ofInstant(createDateTime.toInstant(), id),
                ZonedDateTime.ofInstant(lastModifyDateTime.toInstant(), id),
                creatorId,dataOrigin);
        dtaProvider.getItems().add(attributeKindMetaInfo);
        dtaProvider.refreshAll();
    }

    @Override
    public void receivedAttributeKindRemovedEvent(AttributeKindRemovedEvent event) {
        if(event.getAttributeKindUID() != null){
            ListDataProvider dtaProvider=(ListDataProvider)attributeKindMetaInfoGrid.getDataProvider();
            Collection<AttributeKindMetaInfo> attributeKindMetaInfoList = dtaProvider.getItems();
            AttributeKindMetaInfo removeTargetElement = null;
            for(AttributeKindMetaInfo currentAttributeKindMetaInfo:attributeKindMetaInfoList){
                if(currentAttributeKindMetaInfo.getKindUID().equals(event.getAttributeKindUID())){
                    removeTargetElement = currentAttributeKindMetaInfo;
                }
            }
            if(removeTargetElement != null){
                dtaProvider.getItems().remove(removeTargetElement);
            }
            dtaProvider.refreshAll();

            if(lastSelectedAttributeKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributeKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributeKindUID())){
                resetSingleAttributeKindSummaryInfoArea();
            }
        }
    }

    private void renderRemoveAttributeKindEntitiesUI(AttributeKindMetaInfo attributeKindMetaInfo){
        RemoveAttributeKindView removeAttributeKindView = new RemoveAttributeKindView(attributeKindMetaInfo);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除属性类型",null,true,600,210,false);
        fixSizeWindow.setWindowContent(removeAttributeKindView);
        fixSizeWindow.setModel(true);
        removeAttributeKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void resetSingleAttributeKindSummaryInfoArea(){
        this.attributeKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.secondaryTitleActionBar.updateTitleContent(" - ");
        this.secondaryTitleActionBar2.updateTitleContent(" - ");
        //this.conceptionKindCorrelationInfoChart.clearData();
    }

    private void renderAttributeKindOverview(AttributeKindMetaInfo attributeKindMetaInfo){
        String attributeKindName = attributeKindMetaInfo.getKindName();
        String attributeDataType = attributeKindMetaInfo.getAttributeDataType();
        String attributeKindDesc = attributeKindMetaInfo.getKindDesc() != null ?
                attributeKindMetaInfo.getKindDesc():"未设置描述信息";
        String attributeKindUID = attributeKindMetaInfo.getKindUID();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        List<AttributesViewKind> containerAttributesViewKindsList = new ArrayList<>();
                AttributeKind attributeKind = coreRealm.getAttributeKind(attributeKindMetaInfo.getKindUID());
        if(attributeKind != null){
            containerAttributesViewKindsList.addAll(attributeKind.getContainerAttributesViewKinds());
        }
        coreRealm.closeGlobalSession();

        attributeKindAttributesInfoGrid.setItems(containerAttributesViewKindsList);
        //conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
        //conceptionKindCorrelationInfoChart.clearData();
        //conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,attributeKindName);

        String attributeNameText = attributeKindName +" ( "+attributeKindDesc+" )";
        this.secondaryTitleActionBar.updateTitleContent(attributeNameText);
        String attributeKindIdText = attributeKindUID+ " - "+attributeDataType;
        this.secondaryTitleActionBar2.updateTitleContent(attributeKindIdText);
    }

    private void renderAttributeKindConfigurationUI(AttributeKindMetaInfo attributeKindMetaInfo){
        AttributeKindDetailUI attributeKindDetailUI = new AttributeKindDetailUI(attributeKindMetaInfo.getKindUID());
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

        Icon attributeKindIcon = VaadinIcon.INPUT.create();
        attributeKindIcon.setSize("10px");
        titleDetailLayout.add(attributeKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributeKindName = new NativeLabel(attributeKindMetaInfo.getKindName()+" ( ");
        titleDetailLayout.add(attributeKindName);

        Icon _UIDIcon = VaadinIcon.KEY_O.create();
        _UIDIcon.setSize("10px");
        titleDetailLayout.add(_UIDIcon);
        NativeLabel attributeKindUID = new NativeLabel(" "+attributeKindMetaInfo.getKindUID()+")");
        titleDetailLayout.add(attributeKindUID);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"属性类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(attributeKindDetailUI);
        fullScreenWindow.show();
    }

    @Override
    public void receivedAttributeKindAttachedToAttributesViewKindEvent(AttributeKindAttachedToAttributesViewKindEvent event) {

    }
}
