package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis.ConceptionEntitiesGeospatialInfoAnalysisView;

import java.util.Set;

public class ConceptionEntitiesListView extends VerticalLayout {

    private Grid<ConceptionEntity> displayedConceptionEntitiesGrid;

    public ConceptionEntitiesListView() {

        this.displayedConceptionEntitiesGrid = new Grid<>();
        this.displayedConceptionEntitiesGrid.setWidth(100, Unit.PERCENTAGE);
        this.displayedConceptionEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_COMPACT);
        this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionKindName).setHeader("").setKey("idx_0").setFlexGrow(1).setResizable(false);
        this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionEntityUID).setHeader("").setKey("idx_1").setFlexGrow(0).setWidth("100px").setResizable(false);
        //this.displayedConceptionEntitiesGrid.addComponentColumn(new ConceptionEntitiesGeospatialInfoAnalysisView.ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("60px").setResizable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CUBE,"概念类型");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        //GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        //this.displayedConceptionEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false);
        this.displayedConceptionEntitiesGrid.addSelectionListener(new SelectionListener<Grid<ConceptionEntity>, ConceptionEntity>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ConceptionEntity>, ConceptionEntity> selectionEvent) {
                Set<ConceptionEntity> selectedEntities = selectionEvent.getAllSelectedItems();
                if(!selectedEntities.isEmpty()){
                    //conceptionEntitiesGeospatialScaleMapInfoChart.flyToPointedConceptionEntities(selectedEntities);
                }
            }
        });

        add(this.displayedConceptionEntitiesGrid);

    }



}
