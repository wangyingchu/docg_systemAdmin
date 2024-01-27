package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndexMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SearchIndexInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KindIndexConfigView extends VerticalLayout {

    public enum KindIndexType {ConceptionKind,RelationKind}
    private KindIndexType kindIndexType;
    private String kindName;
    private Grid<SearchIndexInfo> searchIndexValueGrid;

    public KindIndexConfigView(KindIndexType kindIndexType,String kindName){
        this.kindIndexType = kindIndexType;
        this.kindName = kindName;
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderKindIndexConfigUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderKindIndexConfigUI(){
        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createKindIndexButton= new Button("创建类型索引");
        createKindIndexButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createKindIndexButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        buttonList.add(createKindIndexButton);
        createKindIndexButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewKindIndexUI();
            }
        });

        Button refreshMetaConfigItemsInfoButton = new Button("刷新类型索引信息",new Icon(VaadinIcon.REFRESH));
        refreshMetaConfigItemsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshMetaConfigItemsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            refreshKindIndex();
        });
        buttonList.add(refreshMetaConfigItemsInfoButton);

        SecondaryTitleActionBar indexConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.ADD_DOCK),"类型索引配置管理 ",secTitleElementsList,buttonList);
        add(indexConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon removeIcon = new Icon(VaadinIcon.ERASER);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {
                if(entityStatisticsInfo instanceof SearchIndexInfo){
                    renderDeleteKindIndexUI((SearchIndexInfo)entityStatisticsInfo);
                }
            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeItemButton.setTooltipText("删除类型索引");

            HorizontalLayout buttons = new HorizontalLayout(removeItemButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        searchIndexValueGrid = new Grid<>();
        searchIndexValueGrid.setWidth(100, Unit.PERCENTAGE);
        searchIndexValueGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        searchIndexValueGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        searchIndexValueGrid.addColumn(SearchIndexInfo::getIndexName).setHeader("索引名称").setKey("idx_0").setFlexGrow(0).setWidth("250px").setResizable(true);
        searchIndexValueGrid.addColumn(SearchIndexInfo::getPopulationPercent).setHeader("总体百分比").setKey("idx_2").setFlexGrow(0).setWidth("120px").setResizable(false);
        searchIndexValueGrid.addColumn(SearchIndexInfo::getIndexedAttributeNames).setHeader("索引包含属性值").setKey("idx_3").setFlexGrow(1).setResizable(true);
        searchIndexValueGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("100px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"索引名称");
        searchIndexValueGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.CALC,"总体百分比");
        searchIndexValueGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.BULLETS,"索引包含属性值");
        searchIndexValueGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        searchIndexValueGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);
        searchIndexValueGrid.setHeight(150,Unit.PIXELS);
        add(searchIndexValueGrid);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<SearchIndexInfo> searchIndexInfoSet = null;
        switch(this.kindIndexType){
            case RelationKind -> searchIndexInfoSet = systemMaintenanceOperator.listRelationKindSearchIndex();
            case ConceptionKind -> searchIndexInfoSet = systemMaintenanceOperator.listConceptionKindSearchIndex();
        }
        List<SearchIndexInfo> kindSearchIndexList = new ArrayList<>();
        for(SearchIndexInfo currentSearchIndexInfo:searchIndexInfoSet){
            String kindName = currentSearchIndexInfo.getSearchKindName();
            if(this.kindName.equals(kindName)){
                kindSearchIndexList.add(currentSearchIndexInfo);
            }
        }
        searchIndexValueGrid.setItems(kindSearchIndexList);
    }

    public void refreshKindIndex(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<SearchIndexInfo> searchIndexInfoSet = null;
        switch(this.kindIndexType){
            case RelationKind -> searchIndexInfoSet = systemMaintenanceOperator.listRelationKindSearchIndex();
            case ConceptionKind -> searchIndexInfoSet = systemMaintenanceOperator.listConceptionKindSearchIndex();
        }
        List<SearchIndexInfo> kindSearchIndexList = new ArrayList<>();
        for(SearchIndexInfo currentSearchIndexInfo:searchIndexInfoSet){
            String kindName = currentSearchIndexInfo.getSearchKindName();
            if(this.kindName.equals(kindName)){
                kindSearchIndexList.add(currentSearchIndexInfo);
            }
        }
        ListDataProvider dtaProvider=(ListDataProvider)searchIndexValueGrid.getDataProvider();
        dtaProvider.getItems().clear();
        dtaProvider.getItems().addAll(kindSearchIndexList);
        dtaProvider.refreshAll();
    }

    private void renderAddNewKindIndexUI(){
        CreateKindIndexView createKindIndexView = new CreateKindIndexView(this.kindIndexType,this.kindName);
        createKindIndexView.setContainerKindIndexConfigView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建类型索引",null,true,550,320,false);
        fixSizeWindow.setWindowContent(createKindIndexView);
        fixSizeWindow.setModel(true);
        createKindIndexView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderDeleteKindIndexUI(SearchIndexInfo searchIndexInfo){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除类型索引",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行删除类型索引 "+searchIndexInfo.getIndexName()+" ("+searchIndexInfo.getSearchKindName()+") 的操作",actionButtonList,650,190);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDeleteKindIndex(searchIndexInfo,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDeleteKindIndex(SearchIndexInfo searchIndexInfo, ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        boolean deleteResult = false;
        try {
            switch(this.kindIndexType){
                case RelationKind -> deleteResult = systemMaintenanceOperator.removeRelationKindSearchIndex(searchIndexInfo.getIndexName());
                case ConceptionKind -> deleteResult = systemMaintenanceOperator.removeConceptionKindSearchIndex(searchIndexInfo.getIndexName());
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
        if(deleteResult){
            CommonUIOperationUtil.showPopupNotification("删除类型索引 "+ searchIndexInfo.getIndexName()+" ("+searchIndexInfo.getSearchKindName()+") 操作" +"成功", NotificationVariant.LUMO_SUCCESS);
            confirmWindow.closeConfirmWindow();
            ListDataProvider dtaProvider=(ListDataProvider)searchIndexValueGrid.getDataProvider();
            dtaProvider.getItems().remove(searchIndexInfo);
            dtaProvider.refreshAll();
        }else{
            CommonUIOperationUtil.showPopupNotification("删除类型索引 "+ searchIndexInfo.getIndexName()+" ("+searchIndexInfo.getSearchKindName()+") 操作" +"失败", NotificationVariant.LUMO_ERROR);
        }
    }
}
