package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.CreateClassificationView;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassificationManagementUI extends VerticalLayout {
    private TextField attributesViewKindNameFilterField;
    private TextField attributesViewKindDescFilterField;
    private VerticalLayout singleAttributeKindSummaryInfoContainerLayout;
    private TreeGrid<ClassificationMetaInfo> classificationsMetaInfoTreeGrid;
    private Registration listener;
    public ClassificationManagementUI(){

        Button refreshDataButton = new Button("刷新分类数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            //loadConceptionKindsInfo();
            //resetSingleConceptionKindSummaryInfoArea();
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

        Button classificationRelationGuideButton = new Button("概念实体关联分布概览",new Icon(VaadinIcon.DASHBOARD));
        classificationRelationGuideButton.setDisableOnClick(true);
        classificationRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        classificationRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        //classificationManagementOperationButtonList.add(classificationRelationGuideButton);
        classificationRelationGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindsCorrelationInfoSummaryUI(classificationRelationGuideButton);
            }
        });

        Button processingDataListButton = new Button("待处理数据",new Icon(VaadinIcon.MAILBOX));
        processingDataListButton.setDisableOnClick(true);
        processingDataListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        processingDataListButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        //classificationManagementOperationButtonList.add(processingDataListButton);
        processingDataListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderProcessingDataListUI(processingDataListButton);
            }
        });

        Button createClassificationButton = new Button("创建顶层分类",new Icon(VaadinIcon.PLUS_SQUARE_O));
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

        HorizontalLayout attributeKindsInfoContainerLayout = new HorizontalLayout();
        attributeKindsInfoContainerLayout.setSpacing(false);
        attributeKindsInfoContainerLayout.setMargin(false);
        attributeKindsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(attributeKindsInfoContainerLayout);

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
        attributesViewKindNameFilterField.setPlaceholder("分类名称");
        attributesViewKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributesViewKindNameFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributesViewKindNameFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributesViewKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        attributesViewKindDescFilterField = new TextField();
        attributesViewKindDescFilterField.setPlaceholder("分类描述");
        attributesViewKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributesViewKindDescFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributesViewKindDescFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributesViewKindDescFilterField);

        Button searchAttributeKindsButton = new Button("查找分类",new Icon(VaadinIcon.SEARCH));
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindsSearchElementsContainerLayout.add(searchAttributeKindsButton);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchAttributeKindsButton);
        searchAttributeKindsButton.setWidth(90,Unit.PIXELS);
        searchAttributeKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterAttributesViewKinds();
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
                //cancelFiltersAttributesViewKinds();
            }
        });

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributeKindMetaInfo -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributeKind = new Button(configIcon, event -> {
                if(attributeKindMetaInfo instanceof ClassificationMetaInfo){
                    //renderAttributesViewKindConfigurationUI((AttributesViewKindMetaInfo)attributeKindMetaInfo);
                }
            });
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configAttributeKind, "配置分类定义");

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKind = new Button(deleteKindIcon, event -> {});
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeAttributeKind, "删除分类");
            removeAttributeKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributeKindMetaInfo instanceof ClassificationMetaInfo){
                        //renderRemoveAttributesViewKindUI((AttributesViewKindMetaInfo)attributeKindMetaInfo);
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
        attributeKindMetaInfoGridContainerLayout.add(classificationsMetaInfoTreeGrid);
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
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadClassificationsData();
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            classificationsMetaInfoTreeGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
            //attributeKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            classificationsMetaInfoTreeGrid.setHeight(browserHeight-250,Unit.PIXELS);
            //attributeKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadClassificationsData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<ClassificationMetaInfo> classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            TreeDataProvider<ClassificationMetaInfo> dataProvider = (TreeDataProvider<ClassificationMetaInfo>)classificationsMetaInfoTreeGrid.getDataProvider();
            TreeData<ClassificationMetaInfo> gridTreeData = dataProvider.getTreeData();

            Map<String,ClassificationMetaInfo> classificationMetaInfoMap = new HashMap<>();
            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                classificationMetaInfoMap.put(currentClassificationMetaInfo.getClassificationName(),currentClassificationMetaInfo);
            }

            for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoList){
                if(currentClassificationMetaInfo.isRootClassification()){
                    gridTreeData.addItem(null,currentClassificationMetaInfo);
                }else{
                    String parentClassificationName = currentClassificationMetaInfo.getParentClassificationName();
                    gridTreeData.addItem(classificationMetaInfoMap.get(parentClassificationName),currentClassificationMetaInfo);
                }
            }
            dataProvider.refreshAll();
            classificationsMetaInfoTreeGrid.expandRecursively(classificationsMetaInfoList,1);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderCreateClassificationUI(){
        CreateClassificationView createClassificationView = new CreateClassificationView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建顶层分类",null,true,500,285,false);
        fixSizeWindow.setWindowContent(createClassificationView);
        createClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
