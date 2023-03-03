package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.util.ConceptionEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;
import java.util.Set;

public class ProcessingConceptionEntityListView extends VerticalLayout {

    private Grid<ConceptionEntityResourceHolderVO> conceptionEntityProcessingDataGrid;

    public ProcessingConceptionEntityListView(int processingListHeight){
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
