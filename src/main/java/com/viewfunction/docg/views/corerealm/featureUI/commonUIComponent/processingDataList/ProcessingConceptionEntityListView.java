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

import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;
import com.viewfunction.docg.util.ConceptionEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcessingConceptionEntityListView extends VerticalLayout {

    private Grid<ConceptionEntityResourceHolderVO> conceptionEntityProcessingDataGrid;
    private HorizontalLayout controllerToolbar;

    public ProcessingConceptionEntityListView(int processingListHeight){

        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        controllerToolbar = new HorizontalLayout();
        controllerToolbar.setVisible(false);

        List<Component> actionComponentsList = new ArrayList<>();
        Button outDegreeInfoButton = new Button("清除选中待处理数据",new Icon(VaadinIcon.DEL));
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        outDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        outDegreeInfoButton.addClickListener((ClickEvent<Button> click) ->{
            //renderRelationAndConceptionKindOutDegreeHeatMapUI();
        });
        Tooltips.getCurrent().setTooltip(outDegreeInfoButton,"概念与关系实体出度统计 HeatMap");
        actionComponentsList.add(outDegreeInfoButton);

        Button inDegreeInfoButton = new Button("清除全部待处理数据",new Icon(VaadinIcon.RECYCLE));
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        inDegreeInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        inDegreeInfoButton.addClickListener((ClickEvent<Button> click) ->{
           // renderRelationAndConceptionKindInDegreeHeatMapUI();
        });
        Tooltips.getCurrent().setTooltip(inDegreeInfoButton,"概念与关系实体入度统计 HeatMap");
        actionComponentsList.add(inDegreeInfoButton);



        ThirdLevelTitleActionBar sectionActionBar = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.LIST),"待处理概念实体",null,actionComponentsList);
        sectionActionBar.setWidth(710,Unit.PIXELS);
        controllerToolbar.add(sectionActionBar);
        add(controllerToolbar);

        conceptionEntityProcessingDataGrid = new Grid<>();
        conceptionEntityProcessingDataGrid.setWidth(710, Unit.PIXELS);
        conceptionEntityProcessingDataGrid.setHeight(processingListHeight, Unit.PIXELS);
        conceptionEntityProcessingDataGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionEntityProcessingDataGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        conceptionEntityProcessingDataGrid.addColumn(ConceptionEntityResourceHolderVO::getConceptionKind).setHeader("概念类型名称").setWidth("400px").setFlexGrow(0).setKey("idx_0");
        conceptionEntityProcessingDataGrid.addColumn(ConceptionEntityResourceHolderVO::getConceptionEntityUID).setHeader("概念实体唯一值ID").setWidth("250px").setFlexGrow(0).setResizable(false).setKey("idx_1");
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CUBE,"概念类型名称");
        conceptionEntityProcessingDataGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.KEY,"概念实体唯一值ID");
        conceptionEntityProcessingDataGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);

        add(conceptionEntityProcessingDataGrid);
    }

    public Set<ConceptionEntityResourceHolderVO> getSelectedConceptionEntitiesInProcessingList(){
        return conceptionEntityProcessingDataGrid.getSelectedItems();
    }

    public void showControllerToolbar(boolean onOff){
        this.controllerToolbar.setVisible(onOff);
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
