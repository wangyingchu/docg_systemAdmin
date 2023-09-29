package com.viewfunction.docg.views.corerealm.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ClassificationRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ClassificationCreatedEvent;
import com.viewfunction.docg.element.eventHandling.ClassificationRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.ClassificationCorrelationInfoChart;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.CreateClassificationView;

import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.RemoveClassificationView;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification.ClassificationDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.*;

public class ClassificationManagementUI extends VerticalLayout implements
        ClassificationCreatedEvent.ClassificationCreatedListener,
        ClassificationRemovedEvent.ClassificationRemovedListener {
    private TextField classificationNameFilterField;
    private TextField classificationDescFilterField;
    private TreeGrid<ClassificationMetaInfo> classificationsMetaInfoTreeGrid;
    private Grid<ClassificationMetaInfo> classificationsMetaInfoFilterGrid;
    private GridListDataView<ClassificationMetaInfo> classificationMetaInfosMetaInfoFilterView;
    private Registration listener;
    private SecondaryTitleActionBar secondaryTitleActionBar;
    private SecondaryKeyValueDisplayItem childClassificationCount;
    private SecondaryKeyValueDisplayItem offendClassificationCount;
    private SecondaryKeyValueDisplayItem conceptionKindCount;
    private SecondaryKeyValueDisplayItem relationKindCount;
    private SecondaryKeyValueDisplayItem attributeKindCount;
    private SecondaryKeyValueDisplayItem attributesViewKindCount;
    private SecondaryKeyValueDisplayItem conceptionEntityCount;
    private VerticalLayout singleClassificationSummaryInfoContainerLayout;
    private ClassificationMetaInfo lastSelectedClassificationMetaInfo;
    private Map<String,ClassificationMetaInfo> classificationMetaInfoMap;
    private String lastSelectedClassificationName;
    private ClassificationCorrelationInfoChart classificationCorrelationInfoChart;
    public ClassificationManagementUI(){

        Button refreshDataButton = new Button("刷新分类数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            refreshClassificationsData();
            resetSingleClassificationSummaryInfoArea();
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

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Classification 分类数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> classificationManagementOperationButtonList = new ArrayList<>();

        Button classificationRelationGuideButton = new Button("分类分布概览",new Icon(VaadinIcon.DASHBOARD));
        classificationRelationGuideButton.setDisableOnClick(true);
        classificationRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        classificationRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationManagementOperationButtonList.add(classificationRelationGuideButton);
        classificationRelationGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindsCorrelationInfoSummaryUI(classificationRelationGuideButton);
            }
        });

        Button createClassificationButton = new Button("创建分类",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createClassificationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createClassificationButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationManagementOperationButtonList.add(createClassificationButton);
        createClassificationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateClassificationUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"分类数据:",classificationManagementOperationButtonList);
        add(sectionActionBar);

        HorizontalLayout classificationsInfoContainerLayout = new HorizontalLayout();
        classificationsInfoContainerLayout.setSpacing(false);
        classificationsInfoContainerLayout.setMargin(false);
        classificationsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(classificationsInfoContainerLayout);

        VerticalLayout classificationMetaInfoGridContainerLayout = new VerticalLayout();
        classificationMetaInfoGridContainerLayout.setSpacing(true);
        classificationMetaInfoGridContainerLayout.setMargin(false);
        classificationMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout classificationsSearchElementsContainerLayout = new HorizontalLayout();
        classificationsSearchElementsContainerLayout.setSpacing(false);
        classificationsSearchElementsContainerLayout.setMargin(false);
        classificationMetaInfoGridContainerLayout.add(classificationsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        classificationsSearchElementsContainerLayout.add(filterTitle);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        classificationNameFilterField = new TextField();
        classificationNameFilterField.setPlaceholder("分类名称");
        classificationNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationNameFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationNameFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        classificationsSearchElementsContainerLayout.add(plusIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        classificationDescFilterField = new TextField();
        classificationDescFilterField.setPlaceholder("分类描述");
        classificationDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationDescFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationDescFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationDescFilterField);

        Button searchClassificationsButton = new Button("查找分类",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(searchClassificationsButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.setWidth(90,Unit.PIXELS);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterClassifications();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        classificationsSearchElementsContainerLayout.add(divIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterClassifications();
            }
        });

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(classificationMetaInfo -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configClassification = new Button(configIcon, event -> {
                if(classificationMetaInfo instanceof ClassificationMetaInfo){
                    renderClassificationConfigurationUI((ClassificationMetaInfo)classificationMetaInfo);
                }
            });
            configClassification.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configClassification.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configClassification, "配置分类定义");

            Icon deleteClassificationIcon = new Icon(VaadinIcon.TRASH);
            deleteClassificationIcon.setSize("21px");
            Button removeClassification = new Button(deleteClassificationIcon, event -> {});
            removeClassification.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeClassification.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeClassification.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeClassification, "删除分类");
            removeClassification.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(classificationMetaInfo instanceof ClassificationMetaInfo){
                        renderRemoveClassificationUI((ClassificationMetaInfo)classificationMetaInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configClassification,removeClassification);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15, Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        classificationsMetaInfoTreeGrid = new TreeGrid<>();
        classificationsMetaInfoTreeGrid.setWidth(1300,Unit.PIXELS);
        classificationsMetaInfoTreeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_ROW_BORDERS);
        classificationsMetaInfoTreeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        classificationsMetaInfoTreeGrid.addHierarchyColumn(ClassificationMetaInfo::getClassificationName).setKey("idx_0").setHeader("分类名称");
        classificationsMetaInfoTreeGrid.addColumn(ClassificationMetaInfo::getClassificationDesc).setKey("idx_1").setHeader("分类描述");
        classificationsMetaInfoTreeGrid.addColumn(ClassificationMetaInfo::getChildClassificationCount).setKey("idx_2").setHeader("子分类数量").setFlexGrow(0).setWidth("110px").setResizable(false);
        classificationsMetaInfoTreeGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("110px").setResizable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分类名称");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"分类描述");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.ROAD_BRANCHES,"子分类数量");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);
        classificationsMetaInfoTreeGrid.appendFooterRow();
        classificationMetaInfoGridContainerLayout.add(classificationsMetaInfoTreeGrid);
        classificationsMetaInfoTreeGrid.addSelectionListener(new SelectionListener<Grid<ClassificationMetaInfo>, ClassificationMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ClassificationMetaInfo>, ClassificationMetaInfo> selectionEvent) {
                Set<ClassificationMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    classificationsMetaInfoTreeGrid.select(lastSelectedClassificationMetaInfo);
                }else{
                    ClassificationMetaInfo selectedClassificationMetaInfo = selectedItemSet.iterator().next();
                    renderClassificationOverview(selectedClassificationMetaInfo);
                    lastSelectedClassificationMetaInfo = selectedClassificationMetaInfo;
                    classificationsMetaInfoFilterGrid.select(lastSelectedClassificationMetaInfo);
                }
            }
        });

        classificationsMetaInfoFilterGrid = new Grid<>();
        classificationsMetaInfoFilterGrid.setWidth(1300,Unit.PIXELS);
        classificationsMetaInfoFilterGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_ROW_BORDERS);
        classificationsMetaInfoFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getClassificationName).setKey("idx_0").setHeader("分类名称");
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getClassificationDesc).setKey("idx_1").setHeader("分类描述");
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getChildClassificationCount).setKey("idx_2").setHeader("子分类数量").setFlexGrow(0).setWidth("110px").setResizable(false);
        classificationsMetaInfoFilterGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("110px").setResizable(false);
        GridColumnHeader gridColumnHeader_idx0A = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分类名称");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1A = new GridColumnHeader(VaadinIcon.DESKTOP,"分类描述");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2A = new GridColumnHeader(VaadinIcon.ROAD_BRANCHES,"子分类数量");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3A = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3A);
        classificationsMetaInfoFilterGrid.appendFooterRow();
        classificationMetaInfoGridContainerLayout.add(classificationsMetaInfoFilterGrid);
        classificationsMetaInfoFilterGrid.addSelectionListener(new SelectionListener<Grid<ClassificationMetaInfo>, ClassificationMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ClassificationMetaInfo>, ClassificationMetaInfo> selectionEvent) {
                Set<ClassificationMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    classificationsMetaInfoFilterGrid.select(lastSelectedClassificationMetaInfo);
                }else{
                    ClassificationMetaInfo selectedClassificationMetaInfo = selectedItemSet.iterator().next();
                    renderClassificationOverview(selectedClassificationMetaInfo);
                    lastSelectedClassificationMetaInfo = selectedClassificationMetaInfo;
                    classificationsMetaInfoTreeGrid.select(lastSelectedClassificationMetaInfo);
                }
            }
        });

        this.classificationsMetaInfoTreeGrid.setVisible(true);
        this.classificationsMetaInfoFilterGrid.setVisible(false);
        classificationsInfoContainerLayout.add(classificationMetaInfoGridContainerLayout);

        singleClassificationSummaryInfoContainerLayout = new VerticalLayout();
        singleClassificationSummaryInfoContainerLayout.setSpacing(true);
        singleClassificationSummaryInfoContainerLayout.setMargin(true);
        singleClassificationSummaryInfoContainerLayout.setPadding(false);
        classificationsInfoContainerLayout.add(singleClassificationSummaryInfoContainerLayout);
        classificationsInfoContainerLayout.setFlexGrow(1, singleClassificationSummaryInfoContainerLayout);

        HorizontalLayout singleClassificationInfoElementsContainerLayout = new HorizontalLayout();
        singleClassificationInfoElementsContainerLayout.setSpacing(false);
        singleClassificationInfoElementsContainerLayout.setMargin(false);
        singleClassificationInfoElementsContainerLayout.setHeight("30px");
        singleClassificationSummaryInfoContainerLayout.add(singleClassificationInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"分类概览");
        singleClassificationInfoElementsContainerLayout.add(filterTitle2);
        singleClassificationInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"-",null,null,false);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        singleClassificationSummaryInfoContainerLayout.add(secondaryTitleActionBar);

        HorizontalLayout displayItemContainer5 = new HorizontalLayout();
        displayItemContainer5.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer5);
        childClassificationCount = new SecondaryKeyValueDisplayItem(displayItemContainer5, VaadinIcon.TAG.create(),"Child Classification-子分类数量:","-");

        HorizontalLayout displayItemContainer6 = new HorizontalLayout();
        displayItemContainer6.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer6);
        offendClassificationCount = new SecondaryKeyValueDisplayItem(displayItemContainer6, FontAwesome.Solid.TAGS.create(),"Offspring Classification-后代分类数量:","-");

        HorizontalLayout displayItemContainer1 = new HorizontalLayout();
        displayItemContainer1.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer1);
        conceptionKindCount = new SecondaryKeyValueDisplayItem(displayItemContainer1, VaadinIcon.CUBE.create(),"相关 ConceptionKind-概念类型数量:","-");

        HorizontalLayout displayItemContainer2 = new HorizontalLayout();
        displayItemContainer2.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer2);
        relationKindCount = new SecondaryKeyValueDisplayItem(displayItemContainer2, VaadinIcon.CONNECT_O.create(),"相关 RelationKind-关系类型数量:","-");

        HorizontalLayout displayItemContainer3 = new HorizontalLayout();
        displayItemContainer3.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer3);
        attributeKindCount = new SecondaryKeyValueDisplayItem(displayItemContainer3, VaadinIcon.INPUT.create(),"相关 AttributeKind-属性类型数量:","-");

        HorizontalLayout displayItemContainer4 = new HorizontalLayout();
        displayItemContainer4.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer4);
        attributesViewKindCount = new SecondaryKeyValueDisplayItem(displayItemContainer4, VaadinIcon.TASKS.create(),"相关 AttributesViewKind-属性视图类型数量:","-");

        HorizontalLayout displayItemContainer7 = new HorizontalLayout();
        displayItemContainer7.getStyle().set("padding-left","10px");
        singleClassificationSummaryInfoContainerLayout.add(displayItemContainer7);
        conceptionEntityCount= new SecondaryKeyValueDisplayItem(displayItemContainer7, VaadinIcon.STOCK.create(),"相关 ConceptionEntity-概念实体数量:","-");

        HorizontalLayout divLayout = new HorizontalLayout();
        divLayout.setWidthFull();
        divLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        singleClassificationSummaryInfoContainerLayout.add(divLayout);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create(),"分类及三代内后代分类分布");
        singleClassificationSummaryInfoContainerLayout.add(infoTitle2);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        loadClassificationsData();
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            classificationsMetaInfoTreeGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
            classificationsMetaInfoFilterGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            classificationsMetaInfoTreeGrid.setHeight(browserHeight-250,Unit.PIXELS);
            classificationsMetaInfoFilterGrid.setHeight(browserHeight-250,Unit.PIXELS);
            classificationCorrelationInfoChart = new ClassificationCorrelationInfoChart(browserHeight-520);
            singleClassificationSummaryInfoContainerLayout.add(classificationCorrelationInfoChart);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadClassificationsData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<ClassificationMetaInfo> classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            TreeDataProvider<ClassificationMetaInfo> dataProvider = (TreeDataProvider<ClassificationMetaInfo>)classificationsMetaInfoTreeGrid.getDataProvider();
            TreeData<ClassificationMetaInfo> gridTreeData = dataProvider.getTreeData();

            classificationMetaInfoMap = new HashMap<>();
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                classificationMetaInfoMap.put(currentClassificationMetaInfo.getClassificationName(),currentClassificationMetaInfo);
            }
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                gridTreeData.addItem(null,currentClassificationMetaInfo);
            }
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                if(!currentClassificationMetaInfo.isRootClassification()){
                    String parentClassificationName = currentClassificationMetaInfo.getParentClassificationName();
                    gridTreeData.setParent(currentClassificationMetaInfo,classificationMetaInfoMap.get(parentClassificationName));
                }
            }
            dataProvider.refreshAll();
            this.classificationsMetaInfoTreeGrid.expand(classificationsMetaInfoList);
            this.classificationMetaInfosMetaInfoFilterView = classificationsMetaInfoFilterGrid.setItems(classificationsMetaInfoList);
            //logic to filter AttributeKinds already loaded from server
            this.classificationMetaInfosMetaInfoFilterView.addFilter(item->{
                String entityKindName = item.getClassificationName();
                String entityKindDesc = item.getClassificationDesc();
                boolean attributeKindNameFilterResult = true;
                if(!classificationNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(classificationNameFilterField.getValue().trim())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }
                boolean attributeKindDescFilterResult = true;
                if(!classificationDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(classificationDescFilterField.getValue().trim())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }
                return attributeKindNameFilterResult & attributeKindDescFilterResult;
            });

        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshClassificationsData(){
        if(this.lastSelectedClassificationMetaInfo != null){
            this.lastSelectedClassificationName = this.lastSelectedClassificationMetaInfo.getClassificationName();
        }

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<ClassificationMetaInfo> classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            TreeDataProvider<ClassificationMetaInfo> dataProvider = (TreeDataProvider<ClassificationMetaInfo>)classificationsMetaInfoTreeGrid.getDataProvider();
            TreeData<ClassificationMetaInfo> gridTreeData = dataProvider.getTreeData();
            gridTreeData.clear();

            classificationMetaInfoMap.clear();
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                classificationMetaInfoMap.put(currentClassificationMetaInfo.getClassificationName(),currentClassificationMetaInfo);
            }
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                gridTreeData.addItem(null,currentClassificationMetaInfo);
            }
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                if(!currentClassificationMetaInfo.isRootClassification()){
                    String parentClassificationName = currentClassificationMetaInfo.getParentClassificationName();
                    gridTreeData.setParent(currentClassificationMetaInfo,classificationMetaInfoMap.get(parentClassificationName));
                }
            }

            dataProvider.refreshAll();
            this.classificationsMetaInfoTreeGrid.expand(classificationsMetaInfoList);

            ListDataProvider listDataProvider = (ListDataProvider)classificationsMetaInfoFilterGrid.getDataProvider();
            listDataProvider.getItems().clear();
            listDataProvider.getItems().addAll(classificationsMetaInfoList);
            listDataProvider.refreshAll();
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void filterClassifications(){
        String classificationFilterValue = classificationNameFilterField.getValue().trim();
        String classificationDescFilterValue = classificationDescFilterField.getValue().trim();
        if(classificationFilterValue.equals("")&classificationDescFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入分类名称 和/或 分类描述", NotificationVariant.LUMO_ERROR);
        }else{
            this.classificationsMetaInfoTreeGrid.setVisible(false);
            this.classificationsMetaInfoFilterGrid.setVisible(true);
            this.classificationMetaInfosMetaInfoFilterView.refreshAll();
        }
    }

    private void cancelFilterClassifications(){
        this.classificationsMetaInfoTreeGrid.setVisible(true);
        this.classificationsMetaInfoFilterGrid.setVisible(false);
        classificationNameFilterField.setValue("");
        classificationDescFilterField.setValue("");
        this.classificationMetaInfosMetaInfoFilterView.refreshAll();
    }

    private void renderCreateClassificationUI(){
        CreateClassificationView createClassificationView = new CreateClassificationView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建分类",null,true,500,350,false);
        fixSizeWindow.setWindowContent(createClassificationView);
        createClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderClassificationOverview(ClassificationMetaInfo classificationMetaInfo){
        String classificationName = classificationMetaInfo.getClassificationName();
        String classificationDesc = classificationMetaInfo.getClassificationDesc() != null ?
                classificationMetaInfo.getClassificationDesc():"未设置描述信息";
        String attributeNameText = classificationName +" ( "+classificationDesc+" )";
        this.secondaryTitleActionBar.updateTitleContent(attributeNameText);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        Classification selectedClassification = coreRealm.getClassification(classificationName);
        if(selectedClassification != null){
            ClassificationRuntimeStatistics classificationRuntimeStatistics = selectedClassification.getClassificationRuntimeStatistics();
            this.childClassificationCount.updateDisplayValue(""+classificationRuntimeStatistics.getChildClassificationsCount());
            this.offendClassificationCount.updateDisplayValue(""+classificationRuntimeStatistics.getOffspringClassificationsCount());
            this.conceptionKindCount.updateDisplayValue(""+classificationRuntimeStatistics.getRelatedConceptionKindCount());
            this.relationKindCount.updateDisplayValue(""+classificationRuntimeStatistics.getRelatedRelationKindCount());
            this.attributeKindCount.updateDisplayValue(""+classificationRuntimeStatistics.getRelatedAttributeKindCount());
            this.attributesViewKindCount.updateDisplayValue(""+classificationRuntimeStatistics.getRelatedAttributesViewKindCount());
            this.conceptionEntityCount.updateDisplayValue(""+classificationRuntimeStatistics.getRelatedConceptionEntityCount());
        }
        coreRealm.closeGlobalSession();
        this.classificationCorrelationInfoChart.refreshCorrelationInfo(classificationMetaInfo.getClassificationName());
    }

    @Override
    public void receivedClassificationCreatedEvent(ClassificationCreatedEvent event) {
        refreshClassificationsData();
        if(this.lastSelectedClassificationName != null){
            this.lastSelectedClassificationMetaInfo = classificationMetaInfoMap.get(this.lastSelectedClassificationName);
        }
        this.classificationsMetaInfoFilterGrid.select(this.lastSelectedClassificationMetaInfo);
        this.classificationsMetaInfoTreeGrid.select(this.lastSelectedClassificationMetaInfo);
    }

    @Override
    public void receivedClassificationRemovedEvent(ClassificationRemovedEvent event) {
        refreshClassificationsData();
        if(event.getClassificationName() != null && event.getClassificationName().equals(this.lastSelectedClassificationName)){
            resetSingleClassificationSummaryInfoArea();
        }
        if(this.lastSelectedClassificationName != null){
            this.lastSelectedClassificationMetaInfo = classificationMetaInfoMap.get(this.lastSelectedClassificationName);
        }
        this.classificationsMetaInfoFilterGrid.select(this.lastSelectedClassificationMetaInfo);
        this.classificationsMetaInfoTreeGrid.select(this.lastSelectedClassificationMetaInfo);
    }

    private void resetSingleClassificationSummaryInfoArea(){
        this.lastSelectedClassificationMetaInfo = null;
        this.secondaryTitleActionBar.updateTitleContent("-");
        this.childClassificationCount.updateDisplayValue("-");
        this.offendClassificationCount.updateDisplayValue("-");
        this.conceptionKindCount.updateDisplayValue("-");
        this.relationKindCount.updateDisplayValue("-");
        this.attributeKindCount.updateDisplayValue("-");
        this.attributesViewKindCount.updateDisplayValue("-");
        this.conceptionEntityCount.updateDisplayValue("-");
        this.classificationCorrelationInfoChart.clearData();
    }

    private void renderRemoveClassificationUI(ClassificationMetaInfo classificationMetaInfo){
        RemoveClassificationView removeClassificationView = new RemoveClassificationView(classificationMetaInfo);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除分类",null,true,600,240,false);
        fixSizeWindow.setWindowContent(removeClassificationView);
        fixSizeWindow.setModel(true);
        removeClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderClassificationConfigurationUI(ClassificationMetaInfo classificationMetaInfo){
        ClassificationDetailUI classificationDetailUI = new ClassificationDetailUI(classificationMetaInfo.getClassificationName());
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

        Icon attributesViewKindIcon = VaadinIcon.TAGS.create();
        attributesViewKindIcon.setSize("10px");
        titleDetailLayout.add(attributesViewKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributesViewKindName = new NativeLabel(classificationMetaInfo.getClassificationName());
        titleDetailLayout.add(attributesViewKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"分类配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(classificationDetailUI);
        fullScreenWindow.show();
    }
}
