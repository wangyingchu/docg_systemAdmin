package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachLinkLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.RelationAttachKindCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain.CreateNewRelationAttachLinkLogicView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain.CreateRelationAttachKindView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RelationAttachKindManagementUI extends VerticalLayout implements
        RelationAttachKindCreatedEvent.RelationAttachKindCreatedListener{

    private Grid<RelationAttachKind> relationAttachKindGrid;
    private GridListDataView<RelationAttachKind> relationAttachKindInfoView;
    private RelationAttachKind lastSelectedRelationAttachKind;
    private VerticalLayout singleRelationAttachKindSummaryInfoContainerLayout;
    private VerticalLayout selectKindPromptInfoContainerLayout;
    private Registration listener;
    private SecondaryTitleActionBar selectedRelationAttachKindTitleActionBar;
    private SecondaryTitleActionBar selectedRelationAttachKindUIDActionBar;
    private SecondaryTitleActionBar relationKindActionBar;
    private NativeLabel allowRepeatLabel;
    private SecondaryTitleActionBar sourceConceptionKindActionBar;
    private SecondaryTitleActionBar targetConceptionKindActionBar;
    private Grid<RelationAttachLinkLogic> relationAttachLinkLogicGrid;
    private Button addRelationAttachLinkLogicButton;
    private Button executeRelationAttachKindButton;
    private NativeLabel isActiveLabel;
    private TextField relationAttachKindNameFilterField;
    private TextField relationAttachKindDescFilterField;
    private TextField sourceConceptionKindNameFilterField;
    private TextField targetConceptionKindNameFilterField;
    private TextField relationKindNameFilterField;

    public RelationAttachKindManagementUI() {
        Button refreshDataButton = new Button("刷新关系附着规则类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            refreshRelationAttachKindsInfo();
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

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Relation Attach Kind 关系附着规则类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> relationAttachKindManagementOperationButtonList = new ArrayList<>();

        Button createRelationAttachKindButton = new Button("创建关系附着规则",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationAttachKindManagementOperationButtonList.add(createRelationAttachKindButton);
        createRelationAttachKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateRelationAttachKindViewUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"关系附着规则类型:",relationAttachKindManagementOperationButtonList);
        add(sectionActionBar);

        HorizontalLayout relationAttachKindsInfoContainerLayout = new HorizontalLayout();
        relationAttachKindsInfoContainerLayout.setSpacing(false);
        relationAttachKindsInfoContainerLayout.setMargin(false);
        relationAttachKindsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(relationAttachKindsInfoContainerLayout);

        VerticalLayout relationAttachKindMetaInfoGridContainerLayout = new VerticalLayout();
        relationAttachKindMetaInfoGridContainerLayout.setSpacing(true);
        relationAttachKindMetaInfoGridContainerLayout.setMargin(false);
        relationAttachKindMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout relationAttachKindsSearchElementsContainerLayout = new HorizontalLayout();
        relationAttachKindsSearchElementsContainerLayout.setSpacing(false);
        relationAttachKindsSearchElementsContainerLayout.setMargin(false);
        relationAttachKindMetaInfoGridContainerLayout.add(relationAttachKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        relationAttachKindsSearchElementsContainerLayout.add(filterTitle);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        relationAttachKindNameFilterField = new TextField();
        relationAttachKindNameFilterField.setPlaceholder("关系附着规则类型名称");
        relationAttachKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationAttachKindNameFilterField.setWidth(150,Unit.PIXELS);
        relationAttachKindsSearchElementsContainerLayout.add(relationAttachKindNameFilterField);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,relationAttachKindNameFilterField);

        Icon plusIcon0 = new Icon(VaadinIcon.PLUS);
        plusIcon0.setSize("12px");
        relationAttachKindsSearchElementsContainerLayout.add(plusIcon0);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon0);

        relationAttachKindDescFilterField = new TextField();
        relationAttachKindDescFilterField.setPlaceholder("关系附着规则类型显示名称");
        relationAttachKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationAttachKindDescFilterField.setWidth(160,Unit.PIXELS);
        relationAttachKindsSearchElementsContainerLayout.add(relationAttachKindDescFilterField);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,relationAttachKindDescFilterField);

        Icon plusIcon1 = new Icon(VaadinIcon.PLUS);
        plusIcon1.setSize("12px");
        relationAttachKindsSearchElementsContainerLayout.add(plusIcon1);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon1);

        sourceConceptionKindNameFilterField = new TextField();
        sourceConceptionKindNameFilterField.setPlaceholder("源概念类型名称");
        sourceConceptionKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        sourceConceptionKindNameFilterField.setWidth(150,Unit.PIXELS);
        relationAttachKindsSearchElementsContainerLayout.add(sourceConceptionKindNameFilterField);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,sourceConceptionKindNameFilterField);

        Icon plusIcon2 = new Icon(VaadinIcon.PLUS);
        plusIcon2.setSize("12px");
        relationAttachKindsSearchElementsContainerLayout.add(plusIcon2);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon2);

        relationKindNameFilterField = new TextField();
        relationKindNameFilterField.setPlaceholder("关系类型名称");
        relationKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindNameFilterField.setWidth(150,Unit.PIXELS);
        relationAttachKindsSearchElementsContainerLayout.add(relationKindNameFilterField);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,relationKindNameFilterField);

        Icon plusIcon3 = new Icon(VaadinIcon.PLUS);
        plusIcon3.setSize("12px");
        relationAttachKindsSearchElementsContainerLayout.add(plusIcon3);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon3);

        targetConceptionKindNameFilterField = new TextField();
        targetConceptionKindNameFilterField.setPlaceholder("目标概念类型名称");
        targetConceptionKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        targetConceptionKindNameFilterField.setWidth(150,Unit.PIXELS);
        relationAttachKindsSearchElementsContainerLayout.add(targetConceptionKindNameFilterField);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,targetConceptionKindNameFilterField);

        Button searchRelationAttachKindsButton = new Button("查找关系附着规则类型",new Icon(VaadinIcon.SEARCH));
        searchRelationAttachKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationAttachKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationAttachKindsSearchElementsContainerLayout.add(searchRelationAttachKindsButton);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationAttachKindsButton);
        searchRelationAttachKindsButton.setWidth(150,Unit.PIXELS);
        searchRelationAttachKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterRelationAttachKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        relationAttachKindsSearchElementsContainerLayout.add(divIcon);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationAttachKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        relationAttachKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterRelationAttachKinds();
            }
        });

        relationAttachKindsInfoContainerLayout.add(relationAttachKindMetaInfoGridContainerLayout);

        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(relationAttachKind -> {
            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeRelationAttachKindButton = new Button(deleteKindIcon, event -> {});
            removeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeRelationAttachKindButton.setTooltipText("删除关系附着规则类型");
            removeRelationAttachKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderDeleteRelationAttachKindUI((RelationAttachKind)relationAttachKind);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(removeRelationAttachKindButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        relationAttachKindGrid = new Grid<>();
        relationAttachKindGrid.setWidth(1300,Unit.PIXELS);
        relationAttachKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationAttachKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationAttachKindName).setHeader("规则类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationAttachKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationAttachKindDesc).setHeader("规则类型显示名称").setKey("idx_0A").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationAttachKindDesc());
        relationAttachKindGrid.addColumn(RelationAttachKind::getSourceConceptionKindName).setHeader("源概念类型名称").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getSourceConceptionKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationKindName).setHeader("关系类型名称").setKey("idx_2").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getTargetConceptionKindName).setHeader("目标概念类型名称").setKey("idx_3").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getTargetConceptionKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::isRepeatableRelationKindAllow).setHeader("允许重复创建").setKey("idx_4").setFlexGrow(0).setWidth("110px").setResizable(false)
                .setTooltipGenerator(attributeKindMetaInfoData -> ""+attributeKindMetaInfoData.isRepeatableRelationKindAllow());
        relationAttachKindGrid.addColumn(RelationAttachKind::isActive).setHeader("启用状态").setKey("idx_5").setFlexGrow(0).setWidth("110px").setResizable(false)
                .setTooltipGenerator(attributeKindMetaInfoData -> ""+attributeKindMetaInfoData.isActive());
        relationAttachKindGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_6").setFlexGrow(0).setWidth("70px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"规则类型名称");
        relationAttachKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx0A = new GridColumnHeader(VaadinIcon.DESKTOP,"规则类型显示名称");
        relationAttachKindGrid.getColumnByKey("idx_0A").setHeader(gridColumnHeader_idx0A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.LEVEL_LEFT,"源概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CONNECT_O.create(),"关系类型名称");
        relationAttachKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.LEVEL_RIGHT,"目标概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.RANDOM,"可重复创建");
        relationAttachKindGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.BOLT,"启用状态");
        relationAttachKindGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5).setSortable(true);
        GridColumnHeader gridColumnHeader_idx6 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationAttachKindGrid.getColumnByKey("idx_6").setHeader(gridColumnHeader_idx6);
        relationAttachKindGrid.appendFooterRow();
        relationAttachKindMetaInfoGridContainerLayout.add(relationAttachKindGrid);

        relationAttachKindGrid.addSelectionListener(new SelectionListener<Grid<RelationAttachKind>, RelationAttachKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<RelationAttachKind>, RelationAttachKind> selectionEvent) {
                Set<RelationAttachKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    relationAttachKindGrid.select(lastSelectedRelationAttachKind);
                }else{
                    RelationAttachKind selectedRelationAttachKind = selectedItemSet.iterator().next();
                    lastSelectedRelationAttachKind = selectedRelationAttachKind;
                    renderRelationAttachKindDetailInfo(selectedRelationAttachKind);
                }
            }
        });

        singleRelationAttachKindSummaryInfoContainerLayout = new VerticalLayout();
        singleRelationAttachKindSummaryInfoContainerLayout.setSpacing(true);
        singleRelationAttachKindSummaryInfoContainerLayout.setMargin(true);
        singleRelationAttachKindSummaryInfoContainerLayout.setPadding(false);
        relationAttachKindsInfoContainerLayout.add(singleRelationAttachKindSummaryInfoContainerLayout);
        relationAttachKindsInfoContainerLayout.setFlexGrow(1, singleRelationAttachKindSummaryInfoContainerLayout);

        HorizontalLayout singleConceptionKindInfoElementsContainerLayout = new HorizontalLayout();
        singleConceptionKindInfoElementsContainerLayout.setSpacing(false);
        singleConceptionKindInfoElementsContainerLayout.setMargin(false);
        singleConceptionKindInfoElementsContainerLayout.setHeight("30px");
        singleRelationAttachKindSummaryInfoContainerLayout.add(singleConceptionKindInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"概念类型概览");
        singleConceptionKindInfoElementsContainerLayout.add(filterTitle2);
        singleConceptionKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        List<Component> actionComponentsList = new ArrayList<>();
        executeRelationAttachKindButton = new Button("执行关系附着规则",VaadinIcon.PLAY.create());
        executeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        actionComponentsList.add(executeRelationAttachKindButton);
        executeRelationAttachKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderExecuteRelationAttachKindUI();
            }
        });

        selectedRelationAttachKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TREE_TABLE),"-",null,actionComponentsList,false);
        selectedRelationAttachKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        singleRelationAttachKindSummaryInfoContainerLayout.add(selectedRelationAttachKindTitleActionBar);

        HorizontalLayout horizontalContainer01 = new HorizontalLayout();
        horizontalContainer01.setSpacing(false);
        horizontalContainer01.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        singleRelationAttachKindSummaryInfoContainerLayout.add(horizontalContainer01);

        selectedRelationAttachKindUIDActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null,false);
        selectedRelationAttachKindUIDActionBar.setWidth(90,Unit.PIXELS);
        horizontalContainer01.add(selectedRelationAttachKindUIDActionBar);

        Icon allowRepeatIcon = new Icon(VaadinIcon.RANDOM);
        allowRepeatIcon.setSize("14px");
        allowRepeatIcon.getStyle().set("color","#2e4e7e").set("padding-right", "5px");
        horizontalContainer01.add(allowRepeatIcon);
        allowRepeatLabel = new NativeLabel("-");
        allowRepeatLabel.getStyle().set("color","#2e4e7e");
        allowRepeatLabel.addClassNames("text-xs");
        horizontalContainer01.add(allowRepeatLabel);

        Button updateAllowRepeatAttributeValueButton = new Button();
        updateAllowRepeatAttributeValueButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon plusIconA = VaadinIcon.EDIT.create();
        plusIconA.setSize("16px");
        updateAllowRepeatAttributeValueButton.setIcon(plusIconA);
        updateAllowRepeatAttributeValueButton.setTooltipText("更新 AllowRepeat 属性值");
        updateAllowRepeatAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
               // enableEditAttributeValue();
            }
        });
        horizontalContainer01.add(updateAllowRepeatAttributeValueButton);

        HorizontalLayout horizontalContainer04 = new HorizontalLayout();
        horizontalContainer04.setSpacing(false);
        horizontalContainer04.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        singleRelationAttachKindSummaryInfoContainerLayout.add(horizontalContainer04);

        Icon isActiveIcon = new Icon(VaadinIcon.BOLT);
        isActiveIcon.setSize("14px");
        isActiveIcon.getStyle().set("color","#2e4e7e").set("padding-right", "5px");
        horizontalContainer04.add(isActiveIcon);
        isActiveLabel = new NativeLabel("-");
        isActiveLabel.getStyle().set("color","#2e4e7e");
        isActiveLabel.addClassNames("text-xs");
        horizontalContainer04.add(isActiveLabel);

        Button updateActiveAttributeValueButton = new Button();
        updateActiveAttributeValueButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon plusIconB = VaadinIcon.EDIT.create();
        plusIconB.setSize("16px");
        updateActiveAttributeValueButton.setIcon(plusIconB);
        updateActiveAttributeValueButton.setTooltipText("更新 Active 属性值");
        updateActiveAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                // enableEditAttributeValue();
            }
        });
        horizontalContainer04.add(updateActiveAttributeValueButton);

        HorizontalLayout horizontalContainer02 = new HorizontalLayout();
        horizontalContainer02.setSpacing(false);
        horizontalContainer02.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        singleRelationAttachKindSummaryInfoContainerLayout.add(horizontalContainer02);

        Icon sourceConceptionKindIcon = new Icon(VaadinIcon.LEVEL_LEFT);
        sourceConceptionKindIcon.setSize("16px");
        sourceConceptionKindIcon.getStyle().set("color","#2e4e7e").set("padding-right", "5px");
        horizontalContainer02.add(sourceConceptionKindIcon);
        sourceConceptionKindActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null,false);
        horizontalContainer02.add(sourceConceptionKindActionBar);

        HorizontalLayout horizontalContainer03 = new HorizontalLayout();
        horizontalContainer03.setSpacing(false);
        horizontalContainer03.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        singleRelationAttachKindSummaryInfoContainerLayout.add(horizontalContainer03);

        Icon targetConceptionKindIcon = new Icon(VaadinIcon.LEVEL_RIGHT);
        targetConceptionKindIcon.setSize("16px");
        targetConceptionKindIcon.getStyle().set("color","#2e4e7e").set("padding-right", "5px");
        horizontalContainer03.add(targetConceptionKindIcon);
        targetConceptionKindActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null,false);
        horizontalContainer03.add(targetConceptionKindActionBar);

        relationKindActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONNECT_O),"-",null,null);
        singleRelationAttachKindSummaryInfoContainerLayout.add(relationKindActionBar);

        List<Component> actionComponentsList2 = new ArrayList<>();
        addRelationAttachLinkLogicButton = new Button("添加关系附着逻辑规则");
        addRelationAttachLinkLogicButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        addRelationAttachLinkLogicButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addRelationAttachLinkLogicButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        addRelationAttachLinkLogicButton.addClickListener((ClickEvent<Button> click) ->{
            renderAddNewRelationAttachLinkLogic();
        });
        actionComponentsList2.add(addRelationAttachLinkLogicButton);

        Icon relationAttachLinkLogicInfoIcon = new Icon(VaadinIcon.WRENCH);
        SecondaryTitleActionBar relationAttachLinkLogicInfoSectionActionBar = new SecondaryTitleActionBar(relationAttachLinkLogicInfoIcon,"关系附着逻辑规则",null,actionComponentsList2,false);
        singleRelationAttachKindSummaryInfoContainerLayout.add(relationAttachLinkLogicInfoSectionActionBar);

        ComponentRenderer _toolBarComponentRenderer2 = new ComponentRenderer<>(relationAttachLinkLogic -> {
            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeRelationAttachLogicButton = new Button(deleteKindIcon, event -> {});
            removeRelationAttachLogicButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeRelationAttachLogicButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeRelationAttachLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeRelationAttachLogicButton.setTooltipText("删除关系附着逻辑规则");
            removeRelationAttachLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderDeleteRelationAttachLinkLogicUI((RelationAttachLinkLogic)relationAttachLinkLogic);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(removeRelationAttachLogicButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        relationAttachLinkLogicGrid = new Grid<>();
        relationAttachLinkLogicGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getLinkLogicType).setHeader("逻辑类别").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(relationAttachLinkLogic -> relationAttachLinkLogic.getLinkLogicType().toString());
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getSourceEntityLinkAttributeName).setHeader("源属性名称").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(RelationAttachLinkLogic::getSourceEntityLinkAttributeName);
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getLinkLogicCondition).setHeader("逻辑条件").setKey("idx_2").setFlexGrow(1)
                .setTooltipGenerator(relationAttachLinkLogic -> relationAttachLinkLogic.getLinkLogicCondition().toString());
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getTargetEntitiesLinkAttributeName).setHeader("目标属性名称").setKey("idx_3").setFlexGrow(1)
                .setTooltipGenerator(RelationAttachLinkLogic::getTargetEntitiesLinkAttributeName);
        relationAttachLinkLogicGrid.addColumn(_toolBarComponentRenderer2).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.CALC,"逻辑类别");
        relationAttachLinkLogicGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.LEVEL_LEFT,"源属性名称");
        relationAttachLinkLogicGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.CREATIVE_COMMONS_ND.create(),"逻辑条件");
        relationAttachLinkLogicGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.LEVEL_RIGHT.create(),"目标属性名称");
        relationAttachLinkLogicGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationAttachLinkLogicGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4);

        singleRelationAttachKindSummaryInfoContainerLayout.add(relationAttachLinkLogicGrid);

        HorizontalLayout horizontalContainer05 = new HorizontalLayout();
        horizontalContainer05.setSpacing(false);
        horizontalContainer05.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        singleRelationAttachKindSummaryInfoContainerLayout.add(horizontalContainer05);

        addRelationAttachLinkLogicButton.setEnabled(false);
        executeRelationAttachKindButton.setEnabled(false);

        selectKindPromptInfoContainerLayout = new VerticalLayout();
        selectKindPromptInfoContainerLayout.getStyle().set("padding-top", "50px");
        relationAttachKindsInfoContainerLayout.add(selectKindPromptInfoContainerLayout);

        HorizontalLayout selectKindInfoMessage = new HorizontalLayout();
        selectKindInfoMessage.setSpacing(true);
        selectKindInfoMessage.setPadding(true);
        selectKindInfoMessage.setMargin(true);
        selectKindInfoMessage.setWidth(100,Unit.PERCENTAGE);
        selectKindInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 请从左侧列表选择关系附着规则类型显示详情");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        selectKindInfoMessage.add(messageLogo,messageLabel);
        selectKindPromptInfoContainerLayout.add(selectKindInfoMessage);

        singleRelationAttachKindSummaryInfoContainerLayout.setVisible(false);
        selectKindPromptInfoContainerLayout.setVisible(true);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            relationAttachKindGrid.setHeight(event.getHeight()-250,Unit.PIXELS);

        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            relationAttachKindGrid.setHeight(browserHeight-250,Unit.PIXELS);
        }));
        renderRelationAttachKindsInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderRelationAttachKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<RelationAttachKind>  relationAttachKindList = coreRealm.getRelationAttachKinds(null,null,null,null,null,null);
        List<RelationAttachKind> filteredRelationAttachKindList = new ArrayList<>();
        for(RelationAttachKind currentRelationAttachKind:relationAttachKindList){
            filteredRelationAttachKindList.add(currentRelationAttachKind);
        }
        this.relationAttachKindInfoView = relationAttachKindGrid.setItems(filteredRelationAttachKindList);
        //logic to filter RelationAttachKinds already loaded from server
        this.relationAttachKindInfoView.addFilter(item->{
            String relationAttachKindName = item.getRelationAttachKindName().toUpperCase();
            String relationAttachKindDesc = item.getRelationAttachKindDesc().toUpperCase();
            String sourceConceptionKindName = item.getSourceConceptionKindName().toUpperCase();
            String targetConceptionKindName = item.getTargetConceptionKindName().toUpperCase();
            String relationKindName = item.getRelationKindName().toUpperCase();
            boolean relationAttachKindNameFilterResult = true;
            if(!relationAttachKindNameFilterField.getValue().trim().equals("")){
                if(relationAttachKindName.contains(relationAttachKindNameFilterField.getValue().trim().toUpperCase())){
                    relationAttachKindNameFilterResult = true;
                }else{
                    relationAttachKindNameFilterResult = false;
                }
            }

            boolean relationAttachKindDescFilterResult = true;
            if(!relationAttachKindDescFilterField.getValue().trim().equals("")){
                if(relationAttachKindDesc.contains(relationAttachKindDescFilterField.getValue().trim().toUpperCase())){
                    relationAttachKindDescFilterResult = true;
                }else{
                    relationAttachKindDescFilterResult = false;
                }
            }

            boolean sourceConceptionKindNameFilterResult = true;
            if(!sourceConceptionKindNameFilterField.getValue().trim().equals("")){
                if(sourceConceptionKindName.contains(sourceConceptionKindNameFilterField.getValue().trim().toUpperCase())){
                    sourceConceptionKindNameFilterResult = true;
                }else{
                    sourceConceptionKindNameFilterResult = false;
                }
            }

            boolean targetConceptionKindNameFilterResult = true;
            if(!targetConceptionKindNameFilterField.getValue().trim().equals("")){
                if(targetConceptionKindName.contains(targetConceptionKindNameFilterField.getValue().trim().toUpperCase())){
                    targetConceptionKindNameFilterResult = true;
                }else{
                    targetConceptionKindNameFilterResult = false;
                }
            }

            boolean relationKindNameFilterResult = true;
            if(!relationKindNameFilterField.getValue().trim().equals("")){
                if(relationKindName.contains(relationKindNameFilterField.getValue().trim().toUpperCase())){
                    relationKindNameFilterResult = true;
                }else{
                    relationKindNameFilterResult = false;
                }
            }
            return relationAttachKindNameFilterResult & relationAttachKindDescFilterResult &
                    sourceConceptionKindNameFilterResult & targetConceptionKindNameFilterResult & relationKindNameFilterResult;
        });
    }

    public void refreshRelationAttachKindsInfo(){
        renderRelationAttachKindsInfo();
        clearRelationAttachKindDetailInfo();
    }

    private void renderRelationAttachKindDetailInfo(RelationAttachKind selectedRelationAttachKind){
        singleRelationAttachKindSummaryInfoContainerLayout.setVisible(true);
        selectKindPromptInfoContainerLayout.setVisible(false);

        selectedRelationAttachKindTitleActionBar.updateTitleContent(
                selectedRelationAttachKind.getRelationAttachKindName()+"("+selectedRelationAttachKind.getRelationAttachKindDesc()+")");
        selectedRelationAttachKindUIDActionBar.updateTitleContent(selectedRelationAttachKind.getRelationAttachKindUID());
        String allowRepeatRelationMessage = selectedRelationAttachKind.isRepeatableRelationKindAllow() ? "允许重复创建相同类型的关系":"不允许重复创建相同类型的关系";
        allowRepeatLabel.setText(allowRepeatRelationMessage);
        String isActiveMessage = selectedRelationAttachKind.isActive() ? "启用中":"未启用";
        isActiveLabel.setText(isActiveMessage);
        relationKindActionBar.updateTitleContent(selectedRelationAttachKind.getRelationKindName());
        sourceConceptionKindActionBar.updateTitleContent(selectedRelationAttachKind.getSourceConceptionKindName());
        targetConceptionKindActionBar.updateTitleContent(selectedRelationAttachKind.getTargetConceptionKindName());
        List<RelationAttachLinkLogic> relationAttachLinkLogicList = selectedRelationAttachKind.getRelationAttachLinkLogic();
        relationAttachLinkLogicGrid.setItems(relationAttachLinkLogicList);
        addRelationAttachLinkLogicButton.setEnabled(true);
        executeRelationAttachKindButton.setEnabled(selectedRelationAttachKind.isActive());
    }

    private void clearRelationAttachKindDetailInfo(){
        selectedRelationAttachKindTitleActionBar.updateTitleContent("-");
        selectedRelationAttachKindUIDActionBar.updateTitleContent("-");
        allowRepeatLabel.setText("-");
        relationKindActionBar.updateTitleContent("-");
        sourceConceptionKindActionBar.updateTitleContent("-");
        targetConceptionKindActionBar.updateTitleContent("-");
        List<RelationAttachLinkLogic> relationAttachLinkLogicList = new ArrayList<>();
        relationAttachLinkLogicGrid.setItems(relationAttachLinkLogicList);
        addRelationAttachLinkLogicButton.setEnabled(false);
        executeRelationAttachKindButton.setEnabled(false);

        singleRelationAttachKindSummaryInfoContainerLayout.setVisible(false);
        selectKindPromptInfoContainerLayout.setVisible(true);
    }

    private void renderCreateRelationAttachKindViewUI(){
        CreateRelationAttachKindView createRelationAttachKindView = new CreateRelationAttachKindView(CreateRelationAttachKindView.RelatedKindType.FromBlank,null);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建关系附着规则类型",null,true,490,540,false);
        fixSizeWindow.setWindowContent(createRelationAttachKindView);
        fixSizeWindow.setModel(true);
        createRelationAttachKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    public void receivedRelationAttachKindCreatedEvent(RelationAttachKindCreatedEvent event) {
        if(event.getRelationAttachKind() != null){
            ListDataProvider dataProvider=(ListDataProvider)relationAttachKindGrid.getDataProvider();
            dataProvider.getItems().add(event.getRelationAttachKind());
            dataProvider.refreshAll();
        }
    }

    private void renderDeleteRelationAttachKindUI(RelationAttachKind relationAttachKind){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除关系附着规则",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"删除关系附着规则",
                "请确认删除关系附着规则 "+ relationAttachKind.getRelationAttachKindName(),actionButtonList,500,185);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                coreRealm.openGlobalSession();
                try {
                    boolean removeResult = coreRealm.removeRelationAttachKind(relationAttachKind.getRelationAttachKindUID());
                    if(removeResult){
                        ListDataProvider<RelationAttachKind> dataProvider=(ListDataProvider<RelationAttachKind>)relationAttachKindGrid.getDataProvider();
                        dataProvider.getItems().remove(relationAttachKind);
                        dataProvider.refreshAll();
                        CommonUIOperationUtil.showPopupNotification("删除关系附着规则成功", NotificationVariant.LUMO_SUCCESS);
                        confirmWindow.closeConfirmWindow();
                        if(lastSelectedRelationAttachKind != null &&
                                lastSelectedRelationAttachKind.getRelationAttachKindUID().equals(relationAttachKind.getRelationAttachKindUID())){
                            clearRelationAttachKindDetailInfo();
                        }
                    }else{
                        CommonUIOperationUtil.showPopupNotification("删除关系附着规则失败", NotificationVariant.LUMO_ERROR);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }finally {
                    coreRealm.closeGlobalSession();
                }
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void renderAddNewRelationAttachLinkLogic(){
        if(lastSelectedRelationAttachKind != null){
            CreateNewRelationAttachLinkLogicView createNewRelationAttachLinkLogicView = new CreateNewRelationAttachLinkLogicView(lastSelectedRelationAttachKind);
            createNewRelationAttachLinkLogicView.setCreateRelationAttachLinkLogicCallback(new CreateNewRelationAttachLinkLogicView.CreateRelationAttachLinkLogicCallback() {
                @Override
                public void onSuccess(RelationAttachLinkLogic resultRelationAttachLinkLogic) {
                    ListDataProvider<RelationAttachLinkLogic> dataProvider=(ListDataProvider<RelationAttachLinkLogic>)relationAttachLinkLogicGrid.getDataProvider();
                    dataProvider.getItems().add(resultRelationAttachLinkLogic);
                    dataProvider.refreshAll();
                }
            });

            FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加关系附着逻辑规则",null,true,350,470,false);
            fixSizeWindow.setWindowContent(createNewRelationAttachLinkLogicView);
            fixSizeWindow.setModel(true);
            createNewRelationAttachLinkLogicView.setContainerDialog(fixSizeWindow);
            fixSizeWindow.show();
        }
    }

    private void renderDeleteRelationAttachLinkLogicUI(RelationAttachLinkLogic relationAttachLinkLogic){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除关系附着逻辑规则",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"删除关系附着逻辑规则",
                "请确认删除关系附着逻辑规则 "+ relationAttachLinkLogic.getRelationAttachLinkLogicUID(),actionButtonList,450,185);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                coreRealm.openGlobalSession();
                RelationAttachKind targetRelationAttachKind = coreRealm.getRelationAttachKind(lastSelectedRelationAttachKind.getRelationAttachKindUID());
                try {
                    boolean removeResult = targetRelationAttachKind.removeRelationAttachLinkLogic(relationAttachLinkLogic.getRelationAttachLinkLogicUID());
                    if(removeResult){
                        ListDataProvider<RelationAttachLinkLogic> dataProvider=(ListDataProvider<RelationAttachLinkLogic>)relationAttachLinkLogicGrid.getDataProvider();
                        dataProvider.getItems().remove(relationAttachLinkLogic);
                        dataProvider.refreshAll();
                        CommonUIOperationUtil.showPopupNotification("删除关系附着逻辑规则成功", NotificationVariant.LUMO_SUCCESS);
                        confirmWindow.closeConfirmWindow();
                    }else{
                        CommonUIOperationUtil.showPopupNotification("删除关系附着逻辑规则失败", NotificationVariant.LUMO_ERROR);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }finally {
                    coreRealm.closeGlobalSession();
                }
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void renderExecuteRelationAttachKindUI(){
        boolean hasDefaultLogic = false;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        RelationAttachKind targetRelationAttachKind = coreRealm.getRelationAttachKind(lastSelectedRelationAttachKind.getRelationAttachKindUID());
        List<RelationAttachLinkLogic> relationAttachLinkLogicList = targetRelationAttachKind.getRelationAttachLinkLogic();
        if(relationAttachLinkLogicList != null){
            for(RelationAttachLinkLogic currentRelationAttachLinkLogic : relationAttachLinkLogicList){
                RelationAttachKind.LinkLogicType currentLogicLinkLogicType =currentRelationAttachLinkLogic.getLinkLogicType();
                if(currentLogicLinkLogicType.equals(RelationAttachKind.LinkLogicType.DEFAULT)){
                    hasDefaultLogic = true;
                    break;
                }
            }
        }
        if(!hasDefaultLogic){
            CommonUIOperationUtil.showPopupNotification("执行关系附着规则要求必须包含 "+RelationAttachKind.LinkLogicType.DEFAULT +" 类型的关系实体匹配逻辑", NotificationVariant.LUMO_ERROR,5000, Notification.Position.MIDDLE);
            return;
        }
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认执行关系附着规则",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"删除关系附着规则",
                "请确认执行关系附着规则 "+ lastSelectedRelationAttachKind.getRelationAttachKindName()+" 创建新的关系实体",actionButtonList,500,185);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                coreRealm.openGlobalSession();
                try {
                    RelationAttachKind targetRelationAttachKind = coreRealm.getRelationAttachKind(lastSelectedRelationAttachKind.getRelationAttachKindUID());
                    EntitiesOperationResult entitiesOperationResult = targetRelationAttachKind.newUniversalRelationEntities(null);
                    showPopupNotification(targetRelationAttachKind.getRelationAttachKindName(),entitiesOperationResult,NotificationVariant.LUMO_SUCCESS);
                    confirmWindow.closeConfirmWindow();
                }catch(CoreRealmServiceRuntimeException e){
                    e.printStackTrace();
                } finally {
                    coreRealm.closeGlobalSession();
                }
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void showPopupNotification(String relationAttachKindName,EntitiesOperationResult entitiesOperationResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("关系附着规则 "+relationAttachKindName+" 执行操作成功"));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setFlexGrow(1,text);
        notification.add(layout);

        VerticalLayout notificationMessageContainer = new VerticalLayout();
        notificationMessageContainer.add(new Div(new Text("执行创建实体数: "+entitiesOperationResult.getOperationStatistics().getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("执行开始时间: "+entitiesOperationResult.getOperationStatistics().getStartTime())));
        notificationMessageContainer.add(new Div(new Text("执行结束时间: "+entitiesOperationResult.getOperationStatistics().getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }

    private void filterRelationAttachKinds(){
        String relationAttachKindNameFilterValue = relationAttachKindNameFilterField.getValue().trim();
        String relationAttachKindDescFilterValue = relationAttachKindDescFilterField.getValue().trim();
        String sourceConceptionKindNameFilterValue = sourceConceptionKindNameFilterField.getValue().trim();
        String targetConceptionKindNameFilterValue = targetConceptionKindNameFilterField.getValue().trim();
        String relationKindNameFilterFieldValue = relationKindNameFilterField.getValue().trim();
        if(relationAttachKindNameFilterValue.equals("")&relationAttachKindDescFilterValue.equals("")&
                sourceConceptionKindNameFilterValue.equals("")&targetConceptionKindNameFilterValue.equals("")&
                relationKindNameFilterFieldValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入至少一项过滤条件", NotificationVariant.LUMO_ERROR);
        }else{
            this.relationAttachKindInfoView.refreshAll();
        }
    }

    private void cancelFilterRelationAttachKinds(){
        relationAttachKindNameFilterField.setValue("");
        relationAttachKindDescFilterField.setValue("");
        sourceConceptionKindNameFilterField.setValue("");
        targetConceptionKindNameFilterField.setValue("");
        relationKindNameFilterField.setValue("");
        this.relationAttachKindInfoView.refreshAll();
    }
}
