package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

public class DataSlicePropertiesConfigView extends VerticalLayout {
    private Grid<DataSlicePropertyValueObject> dataSlicePropertyGrid;

    public DataSlicePropertiesConfigView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createMetaConfigItemButton= new Button("添加切片属性");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createMetaConfigItemButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewPropertyUI();
            }
        });
        buttonList.add(createMetaConfigItemButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"数据切片属性配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(dataSlicePropertyValueObject -> {
            Icon removeIcon = new Icon(VaadinIcon.ERASER);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {

            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeItemButton.setTooltipText("删除切片属性");

            HorizontalLayout buttons = new HorizontalLayout(removeItemButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        dataSlicePropertyGrid = new Grid<>();

        dataSlicePropertyGrid.setWidth(100, Unit.PERCENTAGE);
        dataSlicePropertyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        dataSlicePropertyGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        dataSlicePropertyGrid.addColumn(DataSlicePropertyValueObject::getPropertyName).setHeader("属性名称").setKey("idx_0").setFlexGrow(1).setResizable(true);
        dataSlicePropertyGrid.addColumn(DataSlicePropertyValueObject::getDataSlicePropertyType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("150px");
        dataSlicePropertyGrid.addColumn(DataSlicePropertyValueObject::isPrimaryKey).setHeader("是否主键").setKey("idx_2").setFlexGrow(0).setWidth("100px").setResizable(false);
        dataSlicePropertyGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("100px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        dataSlicePropertyGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        dataSlicePropertyGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.KEY_SOLID.create(),"是否主键");
        dataSlicePropertyGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        dataSlicePropertyGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);
        dataSlicePropertyGrid.setHeight(200,Unit.PIXELS);
        add(dataSlicePropertyGrid);
    }

    private void renderAddNewPropertyUI(){
        AddDataSlicePropertyView addDataSlicePropertyView = new AddDataSlicePropertyView();
        addDataSlicePropertyView.setDataSlicePropertyOperateHandler(new DataSlicePropertyOperateHandler() {
            @Override
            public void handleDataSlicePropertyValue(DataSlicePropertyValueObject dataSlicePropertyValueObject) {

            }
        });
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加属性",null,true,480,180,false);
        fixSizeWindow.setWindowContent(addDataSlicePropertyView);
        fixSizeWindow.setModel(true);
        addDataSlicePropertyView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
