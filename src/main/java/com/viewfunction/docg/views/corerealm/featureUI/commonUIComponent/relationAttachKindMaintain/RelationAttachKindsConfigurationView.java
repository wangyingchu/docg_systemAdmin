package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RelationAttachKindsConfigurationView extends VerticalLayout {
    public enum RelatedKindType { ConceptionKind, RelationKind }
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private Grid<RelationAttachKind> relationAttachKindGrid;
    private RelationAttachKind lastSelectedRelationAttachKind;
    private RelatedKindType relatedKindType;
    private String relatedKindName;
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

        SecondaryTitleActionBar selectedAttributesViewKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TREE_TABLE),"-",null,null,false);
        selectedAttributesViewKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindTitleActionBar);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        renderRelationAttachKindsInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    public void setViewHeight(int viewHeight){
        relationAttachKindGrid.setHeight(viewHeight-60,Unit.PIXELS);
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
}