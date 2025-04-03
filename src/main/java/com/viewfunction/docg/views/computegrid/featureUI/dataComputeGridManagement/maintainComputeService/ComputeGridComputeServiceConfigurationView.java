package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainComputeService;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.dataCompute.dataComputeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.ComputeFunctionMetaInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.util.ResourceHolder;

import java.text.NumberFormat;
import java.util.Set;

public class ComputeGridComputeServiceConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem gridDataSlicesCountDisplayItem;
    private TextField dataSliceNameFilterField;
    private TextField dataSliceGroupFilterField;
    private SecondaryTitleActionBar dataSliceInfoActionBar;
    private Registration listener;
    private Grid<ComputeFunctionMetaInfo> computeFunctionMetaInfoGrid;
    private GridListDataView<ComputeFunctionMetaInfo> computeFunctionMetaInfoView;
    private ComputeFunctionMetaInfo lastSelectedComputeFunctionMetaInfo;
    private Grid<ComputeFunctionPropertyDefinitionVO> dataSlicePropertyDefinitionsGrid;
    private SecondaryKeyValueDisplayItem groupNameDisplayItem;
    private SecondaryKeyValueDisplayItem primaryDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem backupDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem sliceAtomicityDisplayItem;
    private SecondaryKeyValueDisplayItem backupNumberDisplayItem;
    private SecondaryKeyValueDisplayItem sliceStorageModeDisplayItem;
    private int currentDataSliceCount;

    private class ComputeFunctionPropertyDefinitionVO{

        private String functionName;
        private String functionImplementation;
        private int maxFunctionPerUnit;
        private int totalRunningCount;

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public String getFunctionImplementation() {
            return functionImplementation;
        }

        public void setFunctionImplementation(String functionImplementation) {
            this.functionImplementation = functionImplementation;
        }

        public int getMaxFunctionPerUnit() {
            return maxFunctionPerUnit;
        }

        public void setMaxFunctionPerUnit(int maxFunctionPerUnit) {
            this.maxFunctionPerUnit = maxFunctionPerUnit;
        }

        public int getTotalRunningCount() {
            return totalRunningCount;
        }

        public void setTotalRunningCount(int totalRunningCount) {
            this.totalRunningCount = totalRunningCount;
        }
    }

    public ComputeGridComputeServiceConfigurationView(){
        SecondaryIconTitle sectionTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.COG_SOLID.create(),"计算服务配置");
        add(sectionTitle);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();

        this.gridDataSlicesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create()," 网格数据切片数量:","-");

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30, Unit.PIXELS);
        infoContainer.add(horSpaceDiv);

        Button deployComputeServiceButton= new Button("部署计算函数");
        deployComputeServiceButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        deployComputeServiceButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(deployComputeServiceButton);
        deployComputeServiceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateDataSliceView();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"计算函数定义:",null);
        add(sectionActionBar);

        HorizontalLayout dataSlicesInfoContainerLayout = new HorizontalLayout();
        dataSlicesInfoContainerLayout.setPadding(false);
        dataSlicesInfoContainerLayout.setMargin(false);
        add(dataSlicesInfoContainerLayout);

        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setPadding(false);
        leftSideLayout.setMargin(false);
        dataSlicesInfoContainerLayout.add(leftSideLayout);

        HorizontalLayout dataSlicesSearchElementsContainerLayout = new HorizontalLayout();
        dataSlicesSearchElementsContainerLayout.setSpacing(false);
        dataSlicesSearchElementsContainerLayout.setMargin(false);
        leftSideLayout.add(dataSlicesSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        dataSlicesSearchElementsContainerLayout.add(filterTitle);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        dataSliceNameFilterField = new TextField();
        dataSliceNameFilterField.setPlaceholder("计算函数名称");
        dataSliceNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dataSliceNameFilterField.setWidth(150,Unit.PIXELS);
        dataSlicesSearchElementsContainerLayout.add(dataSliceNameFilterField);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,dataSliceNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        dataSlicesSearchElementsContainerLayout.add(plusIcon);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        dataSliceGroupFilterField = new TextField();
        dataSliceGroupFilterField.setPlaceholder("数据切片分组");
        dataSliceGroupFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dataSliceGroupFilterField.setWidth(150,Unit.PIXELS);
        dataSlicesSearchElementsContainerLayout.add(dataSliceGroupFilterField);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,dataSliceGroupFilterField);

        Button searchDataSlicesButton = new Button("查找数据切片",new Icon(VaadinIcon.SEARCH));
        searchDataSlicesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchDataSlicesButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        dataSlicesSearchElementsContainerLayout.add(searchDataSlicesButton);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchDataSlicesButton);
        searchDataSlicesButton.setWidth(115,Unit.PIXELS);
        searchDataSlicesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterDataSlices();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        dataSlicesSearchElementsContainerLayout.add(divIcon);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        dataSlicesSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelFilterDataSlices();
            }
        });

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(dataSliceMetaInfo -> {

            /*
            Icon queryIcon = new Icon(VaadinIcon.RECORDS);
            queryIcon.setSize("20px");
            Button queryDataSliceButton = new Button(queryIcon, event -> {
                //renderQueryDataSliceUI((DataSliceMetaInfo)dataSliceMetaInfo);
            });
            queryDataSliceButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            queryDataSliceButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            queryDataSliceButton.setTooltipText("查询数据切片");

            Icon cleanIcon = new Icon(VaadinIcon.RECYCLE);
            cleanIcon.setSize("21px");
            Button cleanDataSliceButton = new Button(cleanIcon, event -> {});
            cleanDataSliceButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cleanDataSliceButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            cleanDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    //renderEmptyDataSliceUI((DataSliceMetaInfo)dataSliceMetaInfo);
                }
            });
            cleanDataSliceButton.setTooltipText("清除数据切片所有数据");
            */
            Icon deleteDataSliceIcon = new Icon(VaadinIcon.TRASH);
            deleteDataSliceIcon.setSize("21px");
            Button removeDataSliceButton = new Button(deleteDataSliceIcon, event -> {});
            removeDataSliceButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeDataSliceButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeDataSliceButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    //renderRemoveDataSliceUI((DataSliceMetaInfo)dataSliceMetaInfo);
                }
            });
            removeDataSliceButton.setTooltipText("取消计算函数部署");

            HorizontalLayout buttons = new HorizontalLayout(removeDataSliceButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        computeFunctionMetaInfoGrid = new Grid<>();
        computeFunctionMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        leftSideLayout.add(computeFunctionMetaInfoGrid);

        computeFunctionMetaInfoGrid.addColumn(ComputeFunctionMetaInfo::getFunctionName).setHeader("计算函数名称").setKey("idx_0").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<ComputeFunctionMetaInfo>() {
            @Override
            public String apply(ComputeFunctionMetaInfo dataSliceMetaInfo) {
                return dataSliceMetaInfo.getFunctionName();
            }
        });
        computeFunctionMetaInfoGrid.addColumn(ComputeFunctionMetaInfo::getMaxFunctionPerUnit).setHeader("单计算单元最大允许运行数").setKey("idx_1").setWidth("80px").setResizable(false);
        computeFunctionMetaInfoGrid.addColumn(ComputeFunctionMetaInfo::getTotalRunningCount).setHeader("网格中总运行数").setKey("idx_2").setWidth("80px").setResizable(false);
        computeFunctionMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"计算函数名称");
        computeFunctionMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(LineAwesomeIconsSvg.OBJECT_UNGROUP.create(),"单计算单元最大允许运行数");
        computeFunctionMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.OBJECT_GROUP.create(),"网格中总运行数");
        computeFunctionMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        computeFunctionMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);

        computeFunctionMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<ComputeFunctionMetaInfo>, ComputeFunctionMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ComputeFunctionMetaInfo>, ComputeFunctionMetaInfo> selectionEvent) {
                Set<ComputeFunctionMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    computeFunctionMetaInfoGrid.select(lastSelectedComputeFunctionMetaInfo);
                }else{
                    //singleConceptionKindSummaryInfoContainerLayout.setVisible(true);
                    ComputeFunctionMetaInfo selectedDataSliceMetaInfo = selectedItemSet.iterator().next();
                    //renderDataSliceOverview(selectedDataSliceMetaInfo);
                    lastSelectedComputeFunctionMetaInfo = selectedDataSliceMetaInfo;
                }
            }
        });

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setMargin(false);
        rightSideLayout.setWidth(590,Unit.PIXELS);
        dataSlicesInfoContainerLayout.add(rightSideLayout);
        rightSideLayout.getStyle().set("left","0px").set("top","-2px").set("position","relative");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"数据切片概览");
        rightSideLayout.add(filterTitle2);

        dataSliceInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,null);
        dataSliceInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideLayout.add(dataSliceInfoActionBar);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.DASHBOARD),"数据切片指标");
        rightSideLayout.add(infoTitle2);

        HorizontalLayout displayItemContainer4 = new HorizontalLayout();
        displayItemContainer4.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer4);
        groupNameDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer4, VaadinIcon.ARCHIVES.create(),"切片组名称:","-");

        HorizontalLayout displayItemContainer1 = new HorizontalLayout();
        displayItemContainer1.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer1);
        primaryDataCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer1, VaadinIcon.STOCK.create(),"主数据量:","-");

        HorizontalLayout spaceDivLayout01 = new HorizontalLayout();
        spaceDivLayout01.setWidth(5,Unit.PIXELS);
        displayItemContainer1.add(spaceDivLayout01);
        backupDataCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer1, VaadinIcon.STOCK.create(),"备份数据量:","-");

        HorizontalLayout displayItemContainer3 = new HorizontalLayout();
        displayItemContainer3.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer3);
        totalDataCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer3, VaadinIcon.STOCK.create(),"总数据量:","-");

        HorizontalLayout spaceDivLayout02 = new HorizontalLayout();
        spaceDivLayout02.setWidth(5,Unit.PIXELS);
        displayItemContainer3.add(spaceDivLayout02);
        backupNumberDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer3, VaadinIcon.FLIP_H.create(),"切片备份数:","-");

        HorizontalLayout displayItemContainer5 = new HorizontalLayout();
        displayItemContainer5.getStyle().set("padding-left","5px");
        rightSideLayout.add(displayItemContainer5);
        sliceAtomicityDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer5, VaadinIcon.RHOMBUS.create(),"原子类型模式:","-");

        HorizontalLayout spaceDivLayout03 = new HorizontalLayout();
        spaceDivLayout03.setWidth(5,Unit.PIXELS);
        displayItemContainer5.add(spaceDivLayout03);

        sliceStorageModeDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer5, VaadinIcon.SERVER.create(),"数据存储类型:","-");

        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"数据切片属性定义");
        rightSideLayout.add(infoTitle);

        dataSlicePropertyDefinitionsGrid = new Grid<>();
        dataSlicePropertyDefinitionsGrid.setWidth(100,Unit.PERCENTAGE);
        dataSlicePropertyDefinitionsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        dataSlicePropertyDefinitionsGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        dataSlicePropertyDefinitionsGrid.addColumn(ComputeFunctionPropertyDefinitionVO::getFunctionName).setHeader("属性名称").setKey("idx_0");
        dataSlicePropertyDefinitionsGrid.addColumn(ComputeFunctionPropertyDefinitionVO::getMaxFunctionPerUnit).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("100px").setResizable(false);
        dataSlicePropertyDefinitionsGrid.addColumn(ComputeFunctionPropertyDefinitionVO::getMaxFunctionPerUnit).setHeader("切片主键").setKey("idx_2").setFlexGrow(0).setWidth("80px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        dataSlicePropertyDefinitionsGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        dataSlicePropertyDefinitionsGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.KEY_SOLID.create(),"切片主键");
        dataSlicePropertyDefinitionsGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        rightSideLayout.add(dataSlicePropertyDefinitionsGrid);

        VerticalLayout spaceHolderLayout = new VerticalLayout();
        rightSideLayout.add(spaceHolderLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            computeFunctionMetaInfoGrid.setHeight(event.getHeight()-385,Unit.PIXELS);
            dataSlicePropertyDefinitionsGrid.setHeight(event.getHeight()-650,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            computeFunctionMetaInfoGrid.setHeight(browserHeight-385,Unit.PIXELS);
            dataSlicePropertyDefinitionsGrid.setHeight(browserHeight-650,Unit.PIXELS);
        }));
        renderGridComputeFunctionsInfo();
    }

    private void renderGridComputeFunctionsInfo(){
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        try {
            Set<ComputeFunctionMetaInfo> computeFunctionMetaInfoSet = targetComputeGrid.listComputeFunction();
            this.computeFunctionMetaInfoView = this.computeFunctionMetaInfoGrid.setItems(computeFunctionMetaInfoSet);
            this.computeFunctionMetaInfoView.addFilter(item->{
                String dataSliceName = item.getFunctionName();
                boolean dataSliceNameFilterResult = true;
                if(!dataSliceNameFilterField.getValue().trim().equals("")){
                    if(dataSliceName.contains(dataSliceNameFilterField.getValue().trim())){
                        dataSliceNameFilterResult = true;
                    }else{
                        dataSliceNameFilterResult = false;
                    }
                }
                return dataSliceNameFilterResult;
            });
            this.currentDataSliceCount = computeFunctionMetaInfoSet.size();
            this.gridDataSlicesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.currentDataSliceCount));
        } catch (ComputeGridException e) {
            //throw new RuntimeException(e);
        }
    }
}
