package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachLinkLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.RelationAttachKindCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RelationAttachKindsConfigurationView extends VerticalLayout implements
        RelationAttachKindCreatedEvent.RelationAttachKindCreatedListener {
    public enum RelatedKindType { ConceptionKind, RelationKind }
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private Grid<RelationAttachKind> relationAttachKindGrid;
    private RelationAttachKind lastSelectedRelationAttachKind;
    private RelatedKindType relatedKindType;
    private String relatedKindName;
    private SecondaryTitleActionBar selectedRelationAttachKindTitleActionBar;
    private SecondaryTitleActionBar selectedRelationAttachKindUIDActionBar;
    private SecondaryTitleActionBar relationKindActionBar;
    private NativeLabel allowRepeatLabel;
    private SecondaryTitleActionBar sourceConceptionKindActionBar;
    private SecondaryTitleActionBar targetConceptionKindActionBar;
    private Grid<RelationAttachLinkLogic> relationAttachLinkLogicGrid;
    private Button addRelationAttachLinkLogicButton;
    private Button executeRelationAttachKindButton;

    public RelationAttachKindsConfigurationView(RelatedKindType relatedKindType,String relatedKindName){
        this.relatedKindName = relatedKindName;
        this.relatedKindType = relatedKindType;
        this.setWidth(100, Unit.PERCENTAGE);

        setSpacing(false);
        setMargin(false);
        setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(800,Unit.PIXELS);
        mainContainerLayout.add(leftSideContainerLayout);

        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button attachAttributesViewKindButton= new Button("创建关联关系附着规则类型");
        attachAttributesViewKindButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        attachAttributesViewKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attachAttributesViewKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateRelationAttachKindViewUI();
            }
        });
        buttonList.add(attachAttributesViewKindButton);

        Button refreshAttributesViewKindsButton = new Button("刷新关联关系附着规则信息",new Icon(VaadinIcon.REFRESH));
        refreshAttributesViewKindsButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindsButton.addClickListener((ClickEvent<Button> click) ->{
            refreshRelationAttachKindsInfo();
        });
        buttonList.add(refreshAttributesViewKindsButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TREE_TABLE),"关系附着规则类型配置管理 ",secTitleElementsList,buttonList);
        leftSideContainerLayout.add(metaConfigItemConfigActionBar);

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
        relationAttachKindGrid.setWidth(100,Unit.PERCENTAGE);
        relationAttachKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationAttachKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationAttachKindName).setHeader("规则类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationAttachKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getSourceConceptionKindName).setHeader("源概念类型名称").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getSourceConceptionKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationKindName).setHeader("关系类型名称").setKey("idx_2").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getTargetConceptionKindName).setHeader("目标概念类型名称").setKey("idx_3").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getTargetConceptionKindName());
        relationAttachKindGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("70px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"规则类型名称");
        relationAttachKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.LEVEL_LEFT,"源概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CONNECT_O.create(),"关系类型名称");
        relationAttachKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.LEVEL_RIGHT,"目标概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationAttachKindGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);

        relationAttachKindGrid.appendFooterRow();
        leftSideContainerLayout.add(relationAttachKindGrid);

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

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(400, Unit.PIXELS);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainContainerLayout.add(rightSideContainerLayout);

        HorizontalLayout spaceDiv01Layout1 = new HorizontalLayout();
        spaceDiv01Layout1.setHeight(10,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout1);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"选中关系附着规则类型概览");
        rightSideContainerLayout.add(filterTitle);

        HorizontalLayout spaceDiv01Layout2 = new HorizontalLayout();
        spaceDiv01Layout2.setHeight(2,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout2);

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
        rightSideContainerLayout.add(selectedRelationAttachKindTitleActionBar);

        HorizontalLayout horizontalContainer01 = new HorizontalLayout();
        horizontalContainer01.setSpacing(false);
        horizontalContainer01.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        rightSideContainerLayout.add(horizontalContainer01);

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

        HorizontalLayout horizontalContainer02 = new HorizontalLayout();
        horizontalContainer02.setSpacing(false);
        horizontalContainer02.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        rightSideContainerLayout.add(horizontalContainer02);

        Icon sourceConceptionKindIcon = new Icon(VaadinIcon.LEVEL_LEFT);
        sourceConceptionKindIcon.setSize("16px");
        sourceConceptionKindIcon.getStyle().set("color","#2e4e7e").set("padding-right", "5px");
        horizontalContainer02.add(sourceConceptionKindIcon);
        sourceConceptionKindActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null,false);
        horizontalContainer02.add(sourceConceptionKindActionBar);

        HorizontalLayout horizontalContainer03 = new HorizontalLayout();
        horizontalContainer03.setSpacing(false);
        horizontalContainer03.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        rightSideContainerLayout.add(horizontalContainer03);

        Icon targetConceptionKindIcon = new Icon(VaadinIcon.LEVEL_RIGHT);
        targetConceptionKindIcon.setSize("16px");
        targetConceptionKindIcon.getStyle().set("color","#2e4e7e").set("padding-right", "5px");
        horizontalContainer03.add(targetConceptionKindIcon);
        targetConceptionKindActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null,false);
        horizontalContainer03.add(targetConceptionKindActionBar);

        relationKindActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONNECT_O),"-",null,null);
        rightSideContainerLayout.add(relationKindActionBar);

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
        rightSideContainerLayout.add(relationAttachLinkLogicInfoSectionActionBar);

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

        rightSideContainerLayout.add(relationAttachLinkLogicGrid);

        HorizontalLayout horizontalContainer04 = new HorizontalLayout();
        horizontalContainer04.setSpacing(false);
        horizontalContainer04.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        rightSideContainerLayout.add(horizontalContainer04);

        addRelationAttachLinkLogicButton.setEnabled(false);
        executeRelationAttachKindButton.setEnabled(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        renderRelationAttachKindsInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    public void setViewHeight(int viewHeight){
        relationAttachKindGrid.setHeight(viewHeight-60,Unit.PIXELS);
    }

    public void setViewWidth(int viewWidth){
        rightSideContainerLayout.setWidth(viewWidth-600,Unit.PIXELS);
    }

    private void renderRelationAttachKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<RelationAttachKind>  relationAttachKindList = coreRealm.getRelationAttachKinds(null,null,null,null,null,null);
        List<RelationAttachKind> filteredRelationAttachKindList = new ArrayList<>();
        for(RelationAttachKind currentRelationAttachKind:relationAttachKindList){
            String sourceConceptionKindName = currentRelationAttachKind.getSourceConceptionKindName();
            String targetConceptionKindName = currentRelationAttachKind.getTargetConceptionKindName();
            String relationKindName = currentRelationAttachKind.getRelationKindName();
            switch(this.relatedKindType){
                case ConceptionKind :
                    if(this.relatedKindName.equals(sourceConceptionKindName) || this.relatedKindName.equals(targetConceptionKindName)){
                        filteredRelationAttachKindList.add(currentRelationAttachKind);
                    }
                case RelationKind:
                    if(this.relatedKindName.equals(relationKindName)){
                        filteredRelationAttachKindList.add(currentRelationAttachKind);
                    }
            }
        }
        relationAttachKindGrid.setItems(filteredRelationAttachKindList);
    }

    public void refreshRelationAttachKindsInfo(){
        renderRelationAttachKindsInfo();
        clearRelationAttachKindDetailInfo();
    }

    private void renderCreateRelationAttachKindViewUI(){
        CreateRelationAttachKindView createRelationAttachKindView = new CreateRelationAttachKindView(this.relatedKindType,this.relatedKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建关系附着规则类型",null,true,490,520,false);
        fixSizeWindow.setWindowContent(createRelationAttachKindView);
        fixSizeWindow.setModel(true);
        createRelationAttachKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    public void receivedRelationAttachKindCreatedEvent(RelationAttachKindCreatedEvent event) {
        if(event.getRelationAttachKind() != null){
            boolean needAddRelationAttachKind = false;
            String relationKind = event.getRelationAttachKind().getRelationKindName();
            String sourceConceptionKind = event.getRelationAttachKind().getSourceConceptionKindName();
            String targetConceptionKind = event.getRelationAttachKind().getTargetConceptionKindName();
            switch(this.relatedKindType){
                case ConceptionKind :
                    if(this.relatedKindName.equals(sourceConceptionKind)||this.relatedKindName.equals(targetConceptionKind)){
                        needAddRelationAttachKind = true;
                    }
                    break;
                case RelationKind:
                    if(this.relatedKindName.equals(relationKind)){
                        needAddRelationAttachKind = true;
                    }
            }
            if(needAddRelationAttachKind){
                ListDataProvider dataProvider=(ListDataProvider)relationAttachKindGrid.getDataProvider();
                dataProvider.getItems().add(event.getRelationAttachKind());
                dataProvider.refreshAll();
            }
        }
    }

    private void renderRelationAttachKindDetailInfo(RelationAttachKind selectedRelationAttachKind){
        selectedRelationAttachKindTitleActionBar.updateTitleContent(
                selectedRelationAttachKind.getRelationAttachKindName()+"("+selectedRelationAttachKind.getRelationAttachKindDesc()+")");
        selectedRelationAttachKindUIDActionBar.updateTitleContent(selectedRelationAttachKind.getRelationAttachKindUID());
        String allowRepeatRelationMessage = selectedRelationAttachKind.isRepeatableRelationKindAllow() ? "允许重复创建相同类型的关系":"不允许重复创建相同类型的关系";
        allowRepeatLabel.setText(allowRepeatRelationMessage);
        relationKindActionBar.updateTitleContent(selectedRelationAttachKind.getRelationKindName());
        sourceConceptionKindActionBar.updateTitleContent(selectedRelationAttachKind.getSourceConceptionKindName());
        targetConceptionKindActionBar.updateTitleContent(selectedRelationAttachKind.getTargetConceptionKindName());
        List<RelationAttachLinkLogic> relationAttachLinkLogicList = selectedRelationAttachKind.getRelationAttachLinkLogic();
        relationAttachLinkLogicGrid.setItems(relationAttachLinkLogicList);
        addRelationAttachLinkLogicButton.setEnabled(true);
        executeRelationAttachKindButton.setEnabled(true);
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

            FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加关系附着逻辑规则",null,true,350,450,false);
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
                "请确认删除关系附着逻辑规则 "+ relationAttachLinkLogic.getRelationAttachLinkLogicUID(),actionButtonList,450,175);
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

    private void renderDeleteRelationAttachKindUI(RelationAttachKind relationAttachKind){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除关系附着规则",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"删除关系附着规则",
                "请确认删除关系附着规则 "+ relationAttachKind.getRelationAttachKindName(),actionButtonList,500,175);
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
                "请确认执行关系附着规则 "+ lastSelectedRelationAttachKind.getRelationAttachKindName()+" 创建新的关系实体",actionButtonList,500,175);
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
}
