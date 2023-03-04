package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;
import com.viewfunction.docg.util.ConceptionEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailView;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcessingConceptionEntityListView extends VerticalLayout {

    private Grid<ConceptionEntityResourceHolderVO> conceptionEntityProcessingDataGrid;
    private HorizontalLayout controllerToolbar;
    private Button removeSelectedButton;

    public ProcessingConceptionEntityListView(int processingListHeight){

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
        sectionActionBar.setWidth(710,Unit.PIXELS);
        controllerToolbar.add(sectionActionBar);
        add(controllerToolbar);

        conceptionEntityProcessingDataGrid = new Grid<>();
        conceptionEntityProcessingDataGrid.setWidth(710, Unit.PIXELS);
        conceptionEntityProcessingDataGrid.setHeight(processingListHeight, Unit.PIXELS);
        conceptionEntityProcessingDataGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionEntityProcessingDataGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        conceptionEntityProcessingDataGrid.addColumn(ConceptionEntityResourceHolderVO::getConceptionKind).setHeader("概念类型名称").setWidth("380px").setFlexGrow(0).setKey("idx_0");
        conceptionEntityProcessingDataGrid.addColumn(ConceptionEntityResourceHolderVO::getConceptionEntityUID).setHeader("概念实体唯一值ID").setWidth("200px").setFlexGrow(0).setResizable(false).setKey("idx_1");

        conceptionEntityProcessingDataGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("80px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CUBE,"概念类型名称");
        conceptionEntityProcessingDataGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.KEY,"概念实体唯一值ID");
        conceptionEntityProcessingDataGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        conceptionEntityProcessingDataGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false);

        conceptionEntityProcessingDataGrid.addSelectionListener(new SelectionListener<Grid<ConceptionEntityResourceHolderVO>, ConceptionEntityResourceHolderVO>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ConceptionEntityResourceHolderVO>, ConceptionEntityResourceHolderVO> selectionEvent) {
                int selectedEntityNumber =  selectionEvent.getAllSelectedItems().size();
                if(selectedEntityNumber == 0){
                    removeSelectedButton.setEnabled(false);
                }else{
                    removeSelectedButton.setEnabled(true);
                }
            }
        });

        add(conceptionEntityProcessingDataGrid);
    }

    public Set<ConceptionEntityResourceHolderVO> getSelectedConceptionEntitiesInProcessingList(){
        return conceptionEntityProcessingDataGrid.getSelectedItems();
    }

    public void showControllerToolbar(boolean onOff){
        this.controllerToolbar.setVisible(onOff);
    }

    private void clearAllData(){
        ResourceHolder.clearConceptionEntityProcessingList();
        ListDataProvider dataProvider=(ListDataProvider)conceptionEntityProcessingDataGrid.getDataProvider();
        dataProvider.getItems().clear();
        dataProvider.refreshAll();
        removeSelectedButton.setEnabled(false);
    }

    private void removeSelectedData(){
        Set<ConceptionEntityResourceHolderVO> selectedEntitySet = conceptionEntityProcessingDataGrid.getSelectedItems();
        ListDataProvider dataProvider=(ListDataProvider)conceptionEntityProcessingDataGrid.getDataProvider();
        dataProvider.getItems().removeAll(selectedEntitySet);
        dataProvider.refreshAll();
        removeSelectedButton.setEnabled(false);
    }

    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntityResourceHolderVO,HorizontalLayout> {
        @Override
        public HorizontalLayout apply(ConceptionEntityResourceHolderVO conceptionEntityValue) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(showDetailButton, "显示概念实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                       renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });

            Icon commentIcon = VaadinIcon.COMMENT.create();
            if(conceptionEntityValue.getComment() != null){
                commentIcon.setTooltipText(conceptionEntityValue.getComment());
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

    private void renderConceptionEntityUI(ConceptionEntityResourceHolderVO conceptionEntityValue){
        ConceptionEntityDetailView conceptionEntityDetailView = new ConceptionEntityDetailView(conceptionEntityValue.getConceptionKind(),conceptionEntityValue.getConceptionEntityUID());

        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(conceptionKindIcon);
        Label conceptionKindNameLabel = new Label(conceptionEntityValue.getConceptionKind());
        actionComponentList.add(conceptionKindNameLabel);
        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("12px");
        divIcon.getStyle().set("padding-left","5px");
        actionComponentList.add(divIcon);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        actionComponentList.add(conceptionEntityIcon);

        Label conceptionEntityUIDLabel = new Label(conceptionEntityValue.getConceptionEntityUID());
        actionComponentList.add(conceptionEntityUIDLabel);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailView);
        conceptionEntityDetailView.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        List<ConceptionEntityResourceHolderVO> conceptionEntitiesProcessingDataList = ResourceHolder.getConceptionEntityProcessingDataList();
        conceptionEntityProcessingDataGrid.setItems(conceptionEntitiesProcessingDataList);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }
}
