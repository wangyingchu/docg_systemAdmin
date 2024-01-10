package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachLinkLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.RelationAttachKindCreatedEvent;
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
            //refreshAttributesViewKindsInfo();
        });
        buttonList.add(refreshAttributesViewKindsButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TREE_TABLE),"关系附着规则类型配置管理 ",secTitleElementsList,buttonList);
        leftSideContainerLayout.add(metaConfigItemConfigActionBar);

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

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"规则类型名称");
        relationAttachKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.LEVEL_LEFT,"源概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CONNECT_O.create(),"关系类型名称");
        relationAttachKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.LEVEL_RIGHT,"目标概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);

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
        Button executeRelationAttachKindButton = new Button("执行关系附着规则",VaadinIcon.PLAY.create());
        executeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        actionComponentsList.add(executeRelationAttachKindButton);

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
        //Button refreshSystemRuntimeInfoButton = new Button("获取系统运行信息",new Icon(VaadinIcon.REFRESH));

        Button addRelationAttachLinkLogicButton= new Button("添加关系附着逻辑规则");
        addRelationAttachLinkLogicButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        addRelationAttachLinkLogicButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addRelationAttachLinkLogicButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        addRelationAttachLinkLogicButton.addClickListener((ClickEvent<Button> click) ->{
            //systemRuntimeInfoWidget.refreshSystemRuntimeInfo();
        });
        actionComponentsList2.add(addRelationAttachLinkLogicButton);

        Icon relationAttachLinkLogicInfoIcon = new Icon(VaadinIcon.WRENCH);
        SecondaryTitleActionBar relationAttachLinkLogicInfoSectionActionBar = new SecondaryTitleActionBar(relationAttachLinkLogicInfoIcon,"关系附着逻辑规则",null,actionComponentsList2,false);
        rightSideContainerLayout.add(relationAttachLinkLogicInfoSectionActionBar);

        relationAttachLinkLogicGrid = new Grid<>();
        rightSideContainerLayout.add(relationAttachLinkLogicGrid);
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getLinkLogicType).setHeader("逻辑类别").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(relationAttachLinkLogic -> relationAttachLinkLogic.getLinkLogicType().toString());
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getSourceEntityLinkAttributeName).setHeader("源属性名称").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(RelationAttachLinkLogic::getSourceEntityLinkAttributeName);
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getLinkLogicCondition).setHeader("逻辑条件").setKey("idx_2").setFlexGrow(1)
                .setTooltipGenerator(relationAttachLinkLogic -> relationAttachLinkLogic.getLinkLogicCondition().toString());
        relationAttachLinkLogicGrid.addColumn(RelationAttachLinkLogic::getTargetEntitiesLinkAttributeName).setHeader("目标属性名称").setKey("idx_3").setFlexGrow(1)
                .setTooltipGenerator(RelationAttachLinkLogic::getTargetEntitiesLinkAttributeName);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.CALC,"逻辑类别");
        relationAttachLinkLogicGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.LEVEL_LEFT,"源属性名称");
        relationAttachLinkLogicGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.CREATIVE_COMMONS_ND.create(),"逻辑条件");
        relationAttachLinkLogicGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.LEVEL_RIGHT.create(),"目标属性名称");
        relationAttachLinkLogicGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
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
        relationAttachKindGrid.setItems(relationAttachKindList);
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
    }
}
