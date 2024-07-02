package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
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
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionEntitiesListView extends VerticalLayout {

    public interface SelectConceptionEntityListener {
        void onSelectConceptionEntity(ConceptionEntity conceptionEntity);
        void onUnSelectConceptionEntity();
    }

    private Grid<ConceptionEntity> displayedConceptionEntitiesGrid;
    private boolean showConceptionKindName;
    private SelectConceptionEntityListener selectConceptionEntityListener;

    public ConceptionEntitiesListView(boolean showConceptionKindName) {
        this.showConceptionKindName = showConceptionKindName;
        this.displayedConceptionEntitiesGrid = new Grid<>();
        this.displayedConceptionEntitiesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.displayedConceptionEntitiesGrid.setWidth(100, Unit.PERCENTAGE);
        this.displayedConceptionEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_COMPACT);
        if(this.showConceptionKindName){
            this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionKindName).setHeader("").setKey("idx_0").setFlexGrow(1).setResizable(false);
            this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionEntityUID).setHeader("").setKey("idx_1").setFlexGrow(0).setWidth("100px").setResizable(false);
        }else{
            this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionEntityUID).setHeader("").setKey("idx_1").setFlexGrow(1).setResizable(false);
        }
        this.displayedConceptionEntitiesGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("60px").setResizable(false);
        if(this.showConceptionKindName) {
            GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CUBE, "概念类型");
            this.displayedConceptionEntitiesGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        }
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false);
        this.displayedConceptionEntitiesGrid.addSelectionListener(new SelectionListener<Grid<ConceptionEntity>, ConceptionEntity>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ConceptionEntity>, ConceptionEntity> selectionEvent) {
                Set<ConceptionEntity> selectedEntities = selectionEvent.getAllSelectedItems();
                if(!selectedEntities.isEmpty()){
                    if(selectConceptionEntityListener != null){
                        ConceptionEntity selectedConceptionEntity = selectionEvent.getFirstSelectedItem().get();
                        selectConceptionEntityListener.onSelectConceptionEntity(selectedConceptionEntity);
                    }
                }else{
                    if(selectConceptionEntityListener != null){
                        selectConceptionEntityListener.onUnSelectConceptionEntity();
                    }
                }
            }
        });
        add(this.displayedConceptionEntitiesGrid);
    }

    public void renderConceptionEntitiesList(Set<ConceptionEntity> conceptionEntitiesSet){
        ListDataProvider dataProvider=(ListDataProvider)displayedConceptionEntitiesGrid.getDataProvider();
        dataProvider.getItems().clear();
        this.displayedConceptionEntitiesGrid.setItems(conceptionEntitiesSet);
        dataProvider.refreshAll();
    }

    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntity, HorizontalLayout> {
        @Override
        public HorizontalLayout apply(ConceptionEntity conceptionEntityValue) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            showDetailButton.setTooltipText("显示概念实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });
            return actionButtonContainerLayout;
        }
    }

    private void renderConceptionEntityUI(ConceptionEntity conceptionEntityValue){
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionEntityValue.getConceptionKindName(),conceptionEntityValue.getConceptionEntityUID());

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionEntityValue.getConceptionKindName());
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityValue.getConceptionEntityUID());
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    public SelectConceptionEntityListener getSelectConceptionEntityListener() {
        return selectConceptionEntityListener;
    }

    public void setSelectConceptionEntityListener(SelectConceptionEntityListener selectConceptionEntityListener) {
        this.selectConceptionEntityListener = selectConceptionEntityListener;
    }
}
