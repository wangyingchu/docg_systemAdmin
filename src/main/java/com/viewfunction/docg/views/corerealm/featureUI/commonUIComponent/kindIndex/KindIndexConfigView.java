package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndex;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SearchIndexInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import dev.mett.vaadin.tooltip.Tooltips;

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
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

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

        SecondaryTitleActionBar indexConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONTROLLER),"类型索引配置管理 ",secTitleElementsList,buttonList);
        add(indexConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon removeIcon = new Icon(VaadinIcon.ERASER);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {
                if(entityStatisticsInfo instanceof SearchIndexInfo){
                    //renderDeleteConfigItemUI((MetaConfigItemsConfigView.MetaConfigItemValueObject)entityStatisticsInfo);
                }
            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeItemButton, "删除类型索引");

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
        searchIndexValueGrid.addColumn(SearchIndexInfo::getSearchIndexType).setHeader("索引类型").setKey("idx_1").setFlexGrow(0).setWidth("180px").setResizable(false);
        searchIndexValueGrid.addColumn(SearchIndexInfo::getPopulationPercent).setHeader("总体百分比").setKey("idx_2").setFlexGrow(0).setWidth("120px").setResizable(false);
        searchIndexValueGrid.addColumn(SearchIndexInfo::getIndexedAttributeNames).setHeader("索引包含属性值").setKey("idx_3").setFlexGrow(1).setResizable(true);
        searchIndexValueGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("100px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"索引名称");
        searchIndexValueGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.OPTIONS,"索引类型");
        searchIndexValueGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.CALC,"总体百分比");
        searchIndexValueGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.BULLETS,"索引包含属性值");
        searchIndexValueGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        searchIndexValueGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);
        searchIndexValueGrid.setHeight(100,Unit.PIXELS);
        add(searchIndexValueGrid);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<SearchIndexInfo> searchIndexInfoSet = systemMaintenanceOperator.listConceptionKindSearchIndex();
        for(SearchIndexInfo currentSearchIndexInfo:searchIndexInfoSet){
            System.out.println(currentSearchIndexInfo.getIndexName());
            System.out.println(currentSearchIndexInfo.getSearchIndexType());
            System.out.println(currentSearchIndexInfo.getSearchKindName());
            System.out.println(currentSearchIndexInfo.getIndexedAttributeNames());
            System.out.println(currentSearchIndexInfo.getPopulationPercent());
        }
        List<AttributeSystemInfo> attributeSystemInfoList = systemMaintenanceOperator.getConceptionKindAttributesSystemInfo(this.kindName);
    }

    private void renderAddNewKindIndexUI(){
        CreateKindIndexView createKindIndexView = new CreateKindIndexView(KindIndexType.ConceptionKind,this.kindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建类型索引",null,true,550,440,false);
        fixSizeWindow.setWindowContent(createKindIndexView);
        fixSizeWindow.setModel(true);
        createKindIndexView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
