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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributesViewKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.*;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.AttributesViewKindsCorrelationInfoSummaryView;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.CreateAttributesViewKindView;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.RemoveAttributesViewKindView;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.AttributesViewKindDetailUI;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class AttributesViewKindManagementUI extends VerticalLayout implements
        AttributesViewKindCreatedEvent.AttributesViewKindCreatedListener,
        AttributesViewKindRemovedEvent.AttributesViewKindRemovedListener,
        AttributesViewKindDescriptionUpdatedEvent.AttributesViewKindDescriptionUpdatedListener,
        AttributeKindAttachedToAttributesViewKindEvent.AttributeKindAttachedToAttributesViewKindListener,
        AttributeKindDetachedFromAttributesViewKindEvent.AttributeKindDetachedFromAttributesViewKindListener,
        AttributesViewKindAttachedToConceptionKindEvent.AttributesViewKindAttachedToConceptionKindListener,
        AttributesViewKindDetachedFromConceptionKindEvent.AttributesViewKindDetachedFromConceptionKindListener{
    private Grid<AttributesViewKindMetaInfo> attributesViewKindMetaInfoGrid;
    private GridListDataView<AttributesViewKindMetaInfo> attributesViewKindsMetaInfoView;
    final ZoneId id = ZoneId.systemDefault();
    private TextField attributesViewKindNameFilterField;
    private TextField attributesViewKindDescFilterField;
    private ComboBox<AttributesViewKind.AttributesViewKindDataForm> viewKindDataFormFilterSelect;
    private SecondaryTitleActionBar secondaryTitleActionBar;
    private SecondaryTitleActionBar secondaryTitleActionBar2;
    private Grid<AttributeKind> attributeKindAttributesInfoGrid;
    private Grid<ConceptionKind> conceptionKindAttributesInfoGrid;
    private VerticalLayout singleAttributesViewKindSummaryInfoContainerLayout;
    private Registration listener;
    private AttributesViewKindMetaInfo lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo;

    public AttributesViewKindManagementUI(){
        Button refreshDataButton = new Button("刷新属性视图类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            loadAttributesViewKindsInfo();
            resetSingleAttributeKindSummaryInfoArea();
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        String coreRealmName = coreRealm.getCoreRealmName();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span(coreRealmName));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Attributes View Kind 属性视图类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> attributeKindManagementOperationButtonList = new ArrayList<>();

        Button attributeKindRuntimeStatusGuideButton = new Button("属性视图类型关联分布概览",new Icon(VaadinIcon.DASHBOARD));
        attributeKindRuntimeStatusGuideButton.setDisableOnClick(true);
        attributeKindRuntimeStatusGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        attributeKindRuntimeStatusGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindManagementOperationButtonList.add(attributeKindRuntimeStatusGuideButton);
        attributeKindRuntimeStatusGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttributesViewKindsCorrelationInfoSummaryUI(attributeKindRuntimeStatusGuideButton);
            }
        });

        Button createAttributeKindButton = new Button("创建属性视图类型",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindManagementOperationButtonList.add(createAttributeKindButton);
        createAttributeKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddAttributesViewKindView();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"属性视图类型定义:",attributeKindManagementOperationButtonList);
        add(sectionActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributeKindMetaInfo -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributeKind = new Button(configIcon, event -> {
                if(attributeKindMetaInfo instanceof AttributesViewKindMetaInfo){
                    renderAttributesViewKindConfigurationUI((AttributesViewKindMetaInfo)attributeKindMetaInfo);
                }
            });
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configAttributeKind.setTooltipText("配置属性视图类型定义");

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKind = new Button(deleteKindIcon, event -> {});
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeAttributeKind.setTooltipText("删除属性视图类型定义");
            removeAttributeKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributeKindMetaInfo instanceof AttributesViewKindMetaInfo){
                        renderRemoveAttributesViewKindUI((AttributesViewKindMetaInfo)attributeKindMetaInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configAttributeKind,removeAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15, Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        ComponentRenderer _createDateComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            if(entityStatisticsInfo instanceof AttributesViewKindMetaInfo && ((AttributesViewKindMetaInfo)entityStatisticsInfo).getCreateDate() != null){
                ZonedDateTime createZonedDateTime = ((AttributesViewKindMetaInfo)entityStatisticsInfo).getCreateDate();
                return new NativeLabel(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new NativeLabel("-");
            }
        });

        Comparator createDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((AttributesViewKindMetaInfo)o1).getCreateDate()!= null && ((AttributesViewKindMetaInfo)o2).getCreateDate()!= null &&
                        ((AttributesViewKindMetaInfo)o1).getCreateDate() instanceof ZonedDateTime &&
                        ((AttributesViewKindMetaInfo)o2).getCreateDate() instanceof ZonedDateTime){
                    if(((AttributesViewKindMetaInfo)o1).getCreateDate().isBefore(((AttributesViewKindMetaInfo)o2).getCreateDate())){
                        return -1;
                    }if(((AttributesViewKindMetaInfo)o1).getCreateDate().isAfter(((AttributesViewKindMetaInfo)o2).getCreateDate())){
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
            if(entityStatisticsInfo instanceof AttributesViewKindMetaInfo && ((AttributesViewKindMetaInfo)entityStatisticsInfo).getLastModifyDate() != null){
                ZonedDateTime createZonedDateTime = ((AttributesViewKindMetaInfo)entityStatisticsInfo).getLastModifyDate();
                return new NativeLabel(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new NativeLabel("-");
            }
        });

        Comparator lastUpdateDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((AttributesViewKindMetaInfo)o1).getLastModifyDate()!= null && ((AttributesViewKindMetaInfo)o2).getLastModifyDate()!= null &&
                        ((AttributesViewKindMetaInfo)o1).getLastModifyDate() instanceof ZonedDateTime &&
                        ((AttributesViewKindMetaInfo)o2).getLastModifyDate() instanceof ZonedDateTime){
                    if(((AttributesViewKindMetaInfo)o1).getLastModifyDate().isBefore(((AttributesViewKindMetaInfo)o2).getLastModifyDate())){
                        return -1;
                    }if(((AttributesViewKindMetaInfo)o1).getLastModifyDate().isAfter(((AttributesViewKindMetaInfo)o2).getLastModifyDate())){
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

        attributesViewKindMetaInfoGrid = new Grid<>();
        attributesViewKindMetaInfoGrid.setWidth(1300,Unit.PIXELS);
        attributesViewKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributesViewKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributesViewKindMetaInfoGrid.addColumn(AttributesViewKindMetaInfo::getKindName).setHeader("属性视图类型名称").setKey("idx_0").setFlexGrow(1);
        attributesViewKindMetaInfoGrid.addColumn(AttributesViewKindMetaInfo::getKindDesc).setHeader("属性视图类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getKindDesc());
        attributesViewKindMetaInfoGrid.addColumn(AttributesViewKindMetaInfo::getViewKindDataForm).setHeader("属性视图存储结构").setKey("idx_2")
                .setFlexGrow(0).setWidth("140px").setResizable(false);
        attributesViewKindMetaInfoGrid.addColumn(AttributesViewKindMetaInfo::getKindUID).setHeader("属性视图类型 UID").setKey("idx_3")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        attributesViewKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_4")
                .setComparator(createDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        attributesViewKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_5")
                .setComparator(lastUpdateDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        attributesViewKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_6").setFlexGrow(0).setWidth("90px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性视图类型名称");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"属性视图类型描述");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.ELLIPSIS_H,"属性视图存储结构");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.KEY_O,"属性视图类型 UID");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5).setSortable(true);
        GridColumnHeader gridColumnHeader_idx6 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_6").setHeader(gridColumnHeader_idx6);

        attributesViewKindMetaInfoGrid.appendFooterRow();

        attributesViewKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<AttributesViewKindMetaInfo>, AttributesViewKindMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<AttributesViewKindMetaInfo>, AttributesViewKindMetaInfo> selectionEvent) {
                Set<AttributesViewKindMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    attributesViewKindMetaInfoGrid.select(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo);
                }else{
                    AttributesViewKindMetaInfo selectedAttributesViewKindMetaInfo = selectedItemSet.iterator().next();
                    renderAttributesViewKindOverview(selectedAttributesViewKindMetaInfo);
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo = selectedAttributesViewKindMetaInfo;
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

        attributesViewKindNameFilterField = new TextField();
        attributesViewKindNameFilterField.setPlaceholder("属性视图类型名称");
        attributesViewKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributesViewKindNameFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributesViewKindNameFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributesViewKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        attributesViewKindDescFilterField = new TextField();
        attributesViewKindDescFilterField.setPlaceholder("属性视图类型描述");
        attributesViewKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributesViewKindDescFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributesViewKindDescFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributesViewKindDescFilterField);

        Icon plusIcon2 = new Icon(VaadinIcon.PLUS);
        plusIcon2.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon2);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon2);

        viewKindDataFormFilterSelect = new ComboBox();
        viewKindDataFormFilterSelect.setPlaceholder("视图类型数据存储结构");
        viewKindDataFormFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        viewKindDataFormFilterSelect.setWidth(150,Unit.PIXELS);
        viewKindDataFormFilterSelect.setPageSize(30);

        AttributesViewKind.AttributesViewKindDataForm[] attributesViewDataDataFormArray = new AttributesViewKind.AttributesViewKindDataForm[]{
                AttributesViewKind.AttributesViewKindDataForm.SINGLE_VALUE,
                AttributesViewKind.AttributesViewKindDataForm.LIST_VALUE,
                AttributesViewKind.AttributesViewKindDataForm.RELATED_VALUE,
                AttributesViewKind.AttributesViewKindDataForm.EXTERNAL_VALUE
        };
        this.viewKindDataFormFilterSelect.setItems(attributesViewDataDataFormArray);

        attributeKindsSearchElementsContainerLayout.add(viewKindDataFormFilterSelect);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, viewKindDataFormFilterSelect);

        Button searchAttributeKindsButton = new Button("查找属性视图类型",new Icon(VaadinIcon.SEARCH));
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindsSearchElementsContainerLayout.add(searchAttributeKindsButton);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchAttributeKindsButton);
        searchAttributeKindsButton.setWidth(130,Unit.PIXELS);
        searchAttributeKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterAttributesViewKinds();
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
                cancelFiltersAttributesViewKinds();
            }
        });

        attributeKindMetaInfoGridContainerLayout.add(attributesViewKindMetaInfoGrid);
        attributeKindsInfoContainerLayout.add(attributeKindMetaInfoGridContainerLayout);

        singleAttributesViewKindSummaryInfoContainerLayout = new VerticalLayout();
        singleAttributesViewKindSummaryInfoContainerLayout.setSpacing(true);
        singleAttributesViewKindSummaryInfoContainerLayout.setMargin(true);
        singleAttributesViewKindSummaryInfoContainerLayout.setPadding(false);
        attributeKindsInfoContainerLayout.add(singleAttributesViewKindSummaryInfoContainerLayout);
        attributeKindsInfoContainerLayout.setFlexGrow(1, singleAttributesViewKindSummaryInfoContainerLayout);

        HorizontalLayout singleAttributeKindInfoElementsContainerLayout = new HorizontalLayout();
        singleAttributeKindInfoElementsContainerLayout.setSpacing(false);
        singleAttributeKindInfoElementsContainerLayout.setMargin(false);
        singleAttributeKindInfoElementsContainerLayout.setHeight("30px");
        singleAttributesViewKindSummaryInfoContainerLayout.add(singleAttributeKindInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"属性视图类型概览");
        singleAttributeKindInfoElementsContainerLayout.add(filterTitle2);
        singleAttributeKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"-",null,null,false);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        singleAttributesViewKindSummaryInfoContainerLayout.add(secondaryTitleActionBar);

        secondaryTitleActionBar2 = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        singleAttributesViewKindSummaryInfoContainerLayout.add(secondaryTitleActionBar2);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBE),"包含本属性视图类型的概念类型");
        singleAttributesViewKindSummaryInfoContainerLayout.add(infoTitle2);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        conceptionKindAttributesInfoGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        LightGridColumnHeader gridColumnHeader0_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader0_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader1_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader1_idx1).setSortable(true);

        singleAttributesViewKindSummaryInfoContainerLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.INPUT),"包含属性类型");
        singleAttributesViewKindSummaryInfoContainerLayout.add(infoTitle1);

        attributeKindAttributesInfoGrid = new Grid<>();
        attributeKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        attributeKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        attributeKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeKindName).setHeader("属性类型名称").setKey("idx_0");
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeKindUID).setHeader("属性类型 UID").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeDataType).setHeader("数据数据类型").setKey("idx_2").setFlexGrow(0).setWidth("150px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"数据数据类型");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);

        singleAttributesViewKindSummaryInfoContainerLayout.add(attributeKindAttributesInfoGrid);

        add(attributeKindsInfoContainerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        loadAttributesViewKindsInfo();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            attributesViewKindMetaInfoGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
            attributeKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            attributesViewKindMetaInfoGrid.setHeight(browserHeight-250,Unit.PIXELS);
            attributeKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadAttributesViewKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<AttributesViewKindMetaInfo> gridAttributeKindMetaInfoList = new ArrayList<>();
        try {
            List<AttributesViewKindMetaInfo> kindMetaInfoList = coreRealm.getAttributesViewKindsMetaInfo();
            gridAttributeKindMetaInfoList.addAll(kindMetaInfoList);
            this.attributesViewKindNameFilterField.setValue("");
            this.attributesViewKindDescFilterField.setValue("");
            this.viewKindDataFormFilterSelect.setValue(null);
            this.attributesViewKindsMetaInfoView = attributesViewKindMetaInfoGrid.setItems(gridAttributeKindMetaInfoList);
            //logic to filter AttributeKinds already loaded from server
            this.attributesViewKindsMetaInfoView.addFilter(item->{
                String entityKindName = item.getKindName().toUpperCase();
                String entityKindDesc = item.getKindDesc().toUpperCase();
                String entityDataType = item.getViewKindDataForm();
                boolean attributeKindNameFilterResult = true;
                if(!attributesViewKindNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(attributesViewKindNameFilterField.getValue().trim().toUpperCase())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }

                boolean attributeKindDescFilterResult = true;
                if(!attributesViewKindDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(attributesViewKindDescFilterField.getValue().trim().toUpperCase())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }

                boolean attributeDataTypeFilterResult = true;
                if(viewKindDataFormFilterSelect.getValue() != null){
                    if(entityDataType.equals(viewKindDataFormFilterSelect.getValue().toString())){
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

    private void filterAttributesViewKinds(){
        String attributesViewKindFilterValue = attributesViewKindNameFilterField.getValue().trim();
        String attributesViewKindDescFilterValue = attributesViewKindDescFilterField.getValue().trim();
        AttributesViewKind.AttributesViewKindDataForm dataTForm= viewKindDataFormFilterSelect.getValue();
        if(attributesViewKindFilterValue.equals("")&attributesViewKindDescFilterValue.equals("")&dataTForm == null){
            CommonUIOperationUtil.showPopupNotification("请输入属性视图类型名称 和/或 属性视图类型描述 和/或 视图类型数据存储结构", NotificationVariant.LUMO_ERROR);
        }else{
            this.attributesViewKindsMetaInfoView.refreshAll();
        }
    }

    private void cancelFiltersAttributesViewKinds(){
        attributesViewKindNameFilterField.setValue("");
        attributesViewKindDescFilterField.setValue("");
        viewKindDataFormFilterSelect.setValue(null);
        this.attributesViewKindsMetaInfoView.refreshAll();
    }

    private void renderAddAttributesViewKindView(){
        CreateAttributesViewKindView createAttributesViewKindView = new CreateAttributesViewKindView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建属性视图类型",null,true,500,350,false);
        fixSizeWindow.setWindowContent(createAttributesViewKindView);
        createAttributesViewKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    @Override
    public void receivedAttributesViewKindCreatedEvent(AttributesViewKindCreatedEvent event) {
        String kindName = event.getAttributesViewKindName();
        String kindDesc = event.getAttributesViewKindDesc();
        String kindUID = event.getAttributesViewKindUID();
        AttributesViewKind.AttributesViewKindDataForm viewKindDataForm = event.getAttributesViewKindDataForm();
        Date createDateTime = event.getCreateDateTime();
        Date lastModifyDateTime = event.getLastModifyDateTime();
        String creatorId = event.getCreatorId();
        String dataOrigin = event.getDataOrigin();
        ListDataProvider dtaProvider = (ListDataProvider) attributesViewKindMetaInfoGrid.getDataProvider();
        AttributesViewKindMetaInfo attributesViewKindMetaInfo= new AttributesViewKindMetaInfo(kindName,kindDesc,kindUID,
                viewKindDataForm.toString(),ZonedDateTime.ofInstant(createDateTime.toInstant(), id),
                ZonedDateTime.ofInstant(lastModifyDateTime.toInstant(), id),
                creatorId,dataOrigin);
        dtaProvider.getItems().add(attributesViewKindMetaInfo);
        dtaProvider.refreshAll();
    }

    private void resetSingleAttributeKindSummaryInfoArea(){
        this.attributeKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.conceptionKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.secondaryTitleActionBar.updateTitleContent(" - ");
        this.secondaryTitleActionBar2.updateTitleContent(" - ");
    }

    private void renderRemoveAttributesViewKindUI(AttributesViewKindMetaInfo attributesViewKindMetaInfo){
        RemoveAttributesViewKindView removeAttributesViewKindView = new RemoveAttributesViewKindView(attributesViewKindMetaInfo);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除属性视图类型",null,true,600,220,false);
        fixSizeWindow.setWindowContent(removeAttributesViewKindView);
        fixSizeWindow.setModel(true);
        removeAttributesViewKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderAttributesViewKindOverview(AttributesViewKindMetaInfo attributesViewKindMetaInfo){
        String attributesViewKindName = attributesViewKindMetaInfo.getKindName();
        String attributesViewKindDataType = attributesViewKindMetaInfo.getViewKindDataForm();
        String attributesViewKindDesc = attributesViewKindMetaInfo.getKindDesc() != null ?
                attributesViewKindMetaInfo.getKindDesc():"未设置描述信息";
        String attributesViewKindUID = attributesViewKindMetaInfo.getKindUID();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind selectedAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
        List<ConceptionKind> conceptionKindList = selectedAttributesViewKind.getContainerConceptionKinds();
        if(conceptionKindList != null){
            conceptionKindAttributesInfoGrid.setItems(conceptionKindList);
        }
        List<AttributeKind> containsAttributeKindList = selectedAttributesViewKind.getContainsAttributeKinds();
        if(containsAttributeKindList != null){
            attributeKindAttributesInfoGrid.setItems(containsAttributeKindList);
        }
        coreRealm.closeGlobalSession();

        String attributeNameText = attributesViewKindName +" ( "+attributesViewKindDesc+" )";
        this.secondaryTitleActionBar.updateTitleContent(attributeNameText);
        String attributeKindIdText = attributesViewKindUID+ " - "+attributesViewKindDataType;
        this.secondaryTitleActionBar2.updateTitleContent(attributeKindIdText);
    }

    @Override
    public void receivedAttributesViewKindRemovedEvent(AttributesViewKindRemovedEvent event) {
        if(event.getAttributesViewKindUID() != null){
            ListDataProvider dtaProvider=(ListDataProvider) attributesViewKindMetaInfoGrid.getDataProvider();
            Collection<AttributesViewKindMetaInfo> attributeKindMetaInfoList = dtaProvider.getItems();
            AttributesViewKindMetaInfo removeTargetElement = null;
            for(AttributesViewKindMetaInfo currentAttributeKindMetaInfo:attributeKindMetaInfoList){
                if(currentAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                    removeTargetElement = currentAttributeKindMetaInfo;
                }
            }
            if(removeTargetElement != null){
                dtaProvider.getItems().remove(removeTargetElement);
            }
            dtaProvider.refreshAll();

            if(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                resetSingleAttributeKindSummaryInfoArea();
            }
        }
    }

    private void renderAttributesViewKindConfigurationUI(AttributesViewKindMetaInfo attributesViewKindMetaInfo){
        AttributesViewKindDetailUI attributesViewKindDetailUI = new AttributesViewKindDetailUI(attributesViewKindMetaInfo.getKindUID());
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

        Icon attributesViewKindIcon = VaadinIcon.TASKS.create();
        attributesViewKindIcon.setSize("10px");
        titleDetailLayout.add(attributesViewKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributesViewKindName = new NativeLabel(attributesViewKindMetaInfo.getKindName()+" ( ");
        titleDetailLayout.add(attributesViewKindName);

        Icon _UIDIcon = VaadinIcon.KEY_O.create();
        _UIDIcon.setSize("10px");
        titleDetailLayout.add(_UIDIcon);
        NativeLabel attributesViewKindUID = new NativeLabel(" "+attributesViewKindMetaInfo.getKindUID()+")");
        titleDetailLayout.add(attributesViewKindUID);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"属性视图类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(attributesViewKindDetailUI);
        fullScreenWindow.show();
    }

    @Override
    public void receivedAttributesViewKindDescriptionUpdatedEvent(AttributesViewKindDescriptionUpdatedEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributesViewKindDesc() != null){
            ListDataProvider dtaProvider=(ListDataProvider)attributesViewKindMetaInfoGrid.getDataProvider();
            Collection<AttributesViewKindMetaInfo> entityStatisticsInfoList = dtaProvider.getItems();
            for(AttributesViewKindMetaInfo currentAttributesViewKindMetaInfo:entityStatisticsInfoList){
                if(currentAttributesViewKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                    currentAttributesViewKindMetaInfo.setKindDesc(event.getAttributesViewKindDesc());
                }
            }
            dtaProvider.refreshAll();
            if(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                String attributeNameText = lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindName() +" ( "+event.getAttributesViewKindDesc()+" )";
                this.secondaryTitleActionBar.updateTitleContent(attributeNameText);
            }
        }
    }

    @Override
    public void receivedAttributeKindAttachedToAttributesViewKindEvent(AttributeKindAttachedToAttributesViewKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
            if(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                ListDataProvider dtaProvider=(ListDataProvider)attributeKindAttributesInfoGrid.getDataProvider();
                dtaProvider.getItems().add(event.getAttributeKind());
                dtaProvider.refreshAll();
            }
        }
    }

    @Override
    public void receivedAttributeKindDetachedFromAttributesViewKindEvent(AttributeKindDetachedFromAttributesViewKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
            if(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                ListDataProvider dataProvider=(ListDataProvider)attributeKindAttributesInfoGrid.getDataProvider();
                Collection<AttributeKind> itemsCollection = dataProvider.getItems();
                if(itemsCollection != null){
                    for(AttributeKind currentAttributeKind:itemsCollection){
                        if(event.getAttributeKindUID().equals(currentAttributeKind.getAttributeKindUID())){
                            itemsCollection.remove(currentAttributeKind);
                            break;
                        }
                    }
                    dataProvider.refreshAll();
                }
            }
        }
    }

    @Override
    public void receivedAttributesViewKindAttachedToConceptionKindEvent(AttributesViewKindAttachedToConceptionKindEvent event) {
        if(event.getConceptionKind() != null && event.getAttributesViewKindUID() != null){
            if(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                ListDataProvider dtaProvider=(ListDataProvider)conceptionKindAttributesInfoGrid.getDataProvider();
                dtaProvider.getItems().add(event.getConceptionKind());
                dtaProvider.refreshAll();
            }
        }
    }

    @Override
    public void receivedAttributesViewKindDetachedFromConceptionKindEvent(AttributesViewKindDetachedFromConceptionKindEvent event) {
        if(event.getConceptionKindName() != null && event.getAttributesViewKindUID() != null){
            if(lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo != null &&
                    lastSelectedAttributesViewKindMetaInfoGridAttributeKindMetaInfo.getKindUID().equals(event.getAttributesViewKindUID())){
                ListDataProvider dataProvider=(ListDataProvider)conceptionKindAttributesInfoGrid.getDataProvider();
                Collection<ConceptionKind> itemsCollection = dataProvider.getItems();
                if(itemsCollection != null){
                    for(ConceptionKind currentConceptionKind:itemsCollection){
                        if(event.getConceptionKindName().equals(currentConceptionKind.getConceptionKindName())){
                            itemsCollection.remove(currentConceptionKind);
                            break;
                        }
                    }
                    dataProvider.refreshAll();
                }
            }
        }
    }

    private void renderAttributesViewKindsCorrelationInfoSummaryUI(Button launchButton){
        AttributesViewKindsCorrelationInfoSummaryView attributesViewKindsCorrelationInfoSummaryView = new AttributesViewKindsCorrelationInfoSummaryView(1180,800);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DASHBOARD),"属性视图类型关联分布概览",null,true,1200,900,false);
        fixSizeWindow.setWindowContent(attributesViewKindsCorrelationInfoSummaryView);
        fixSizeWindow.show();
        fixSizeWindow.addDetachListener(new ComponentEventListener<DetachEvent>() {
            @Override
            public void onComponentEvent(DetachEvent detachEvent) {
                launchButton.setEnabled(true);
            }
        });
    }
}
