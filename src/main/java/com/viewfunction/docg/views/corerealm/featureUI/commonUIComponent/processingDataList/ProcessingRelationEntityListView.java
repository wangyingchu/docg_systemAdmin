package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;
import com.viewfunction.docg.util.RelationEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcessingRelationEntityListView extends VerticalLayout {

    private Grid<RelationEntityResourceHolderVO> relationEntityProcessingDataGrid;
    private HorizontalLayout controllerToolbar;
    private Button removeSelectedButton;

    public ProcessingRelationEntityListView(int processingListHeight){

        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        controllerToolbar = new HorizontalLayout();
        controllerToolbar.setVisible(false);

        List<Component> actionComponentsList = new ArrayList<>();
        removeSelectedButton = new Button("清除选中待处理数据",new Icon(VaadinIcon.DEL));
        removeSelectedButton.setEnabled(false);
        removeSelectedButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        removeSelectedButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        removeSelectedButton.addClickListener((ClickEvent<Button> click) ->{
            removeSelectedData();
        });
        actionComponentsList.add(removeSelectedButton);

        Button clearAllButton = new Button("清除全部待处理数据",new Icon(VaadinIcon.RECYCLE));
        clearAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearAllButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        clearAllButton.addClickListener((ClickEvent<Button> click) ->{
            clearAllData();
        });
        actionComponentsList.add(clearAllButton);

        ThirdLevelTitleActionBar sectionActionBar = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.LIST),"待处理概念实体",null,actionComponentsList);
        sectionActionBar.setWidth(710, Unit.PIXELS);
        controllerToolbar.add(sectionActionBar);
        add(controllerToolbar);

        relationEntityProcessingDataGrid = new Grid<>();
        relationEntityProcessingDataGrid.setWidth(710, Unit.PIXELS);
        relationEntityProcessingDataGrid.setHeight(processingListHeight, Unit.PIXELS);
        relationEntityProcessingDataGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationEntityProcessingDataGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        relationEntityProcessingDataGrid.addColumn(RelationEntityResourceHolderVO::getRelationKind).setHeader("关系类型名称").setWidth("380px").setFlexGrow(0).setKey("idx_0");
        relationEntityProcessingDataGrid.addColumn(RelationEntityResourceHolderVO::getRelationEntityUID).setHeader("关系实体唯一值ID").setWidth("200px").setFlexGrow(0).setResizable(false).setKey("idx_1");

        relationEntityProcessingDataGrid.addComponentColumn(new RelationEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("80px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CONNECT_O,"关系类型名称");
        relationEntityProcessingDataGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.KEY,"关系实体唯一值ID");
        relationEntityProcessingDataGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        relationEntityProcessingDataGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false);

        relationEntityProcessingDataGrid.addSelectionListener(new SelectionListener<Grid<RelationEntityResourceHolderVO>, RelationEntityResourceHolderVO>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<RelationEntityResourceHolderVO>, RelationEntityResourceHolderVO> selectionEvent) {
                int selectedEntityNumber =  selectionEvent.getAllSelectedItems().size();
                if(selectedEntityNumber == 0){
                    removeSelectedButton.setEnabled(false);
                }else{
                    removeSelectedButton.setEnabled(true);
                }
            }
        });

        add(relationEntityProcessingDataGrid);
    }

    private class RelationEntityActionButtonsValueProvider implements ValueProvider<RelationEntityResourceHolderVO,HorizontalLayout> {
        @Override
        public HorizontalLayout apply(RelationEntityResourceHolderVO relationEntityValue) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(showDetailButton, "显示关系实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntityValue != null){
                        //renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });

            Icon commentIcon = VaadinIcon.COMMENT.create();
            if(relationEntityValue.getComment() != null){
                commentIcon.setTooltipText(relationEntityValue.getComment());
                commentIcon.setColor("#AAAAAA");
            }else{
                commentIcon.setColor("#DEDEDE");
            }
            commentIcon.setSize("12px");
            actionButtonContainerLayout.add(commentIcon);
            actionButtonContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,commentIcon);
            return actionButtonContainerLayout;
        }
    }

    public void showControllerToolbar(boolean onOff){
        this.controllerToolbar.setVisible(onOff);
    }

    private void clearAllData(){
        ResourceHolder.clearConceptionEntityProcessingList();
        ListDataProvider dataProvider=(ListDataProvider) relationEntityProcessingDataGrid.getDataProvider();
        dataProvider.getItems().clear();
        dataProvider.refreshAll();
        removeSelectedButton.setEnabled(false);
    }

    private void removeSelectedData(){
        Set<RelationEntityResourceHolderVO> selectedEntitySet = relationEntityProcessingDataGrid.getSelectedItems();
        ListDataProvider dataProvider=(ListDataProvider) relationEntityProcessingDataGrid.getDataProvider();
        dataProvider.getItems().removeAll(selectedEntitySet);
        dataProvider.refreshAll();
        removeSelectedButton.setEnabled(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        List<RelationEntityResourceHolderVO> relationEntitiesProcessingDataList = ResourceHolder.getRelationEntityProcessingDataList();
        relationEntityProcessingDataGrid.setItems(relationEntitiesProcessingDataList);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
