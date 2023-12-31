package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.dataCompute.computeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.computeServiceCore.internal.ignite.ComputeGridObserver;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceDetailInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.computeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ComputeGridRefreshEvent;
import com.viewfunction.docg.element.eventHandling.DataSliceCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.CreateDataSliceView;

import java.text.NumberFormat;
import java.util.*;

public class ComputeGridDataSliceConfigurationView extends VerticalLayout implements
        DataSliceCreatedEvent.DataSliceCreatedListener,
        ComputeGridRefreshEvent.ComputeGridRefreshEventListener {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem gridDataSlicesCountDisplayItem;
    private SecondaryTitleActionBar dataSliceInfoActionBar;
    private Registration listener;
    private Grid<DataSliceMetaInfo> dataSliceMetaInfoGrid;
    private GridListDataView<DataSliceMetaInfo> dataSliceMetaInfoView;
    private DataSliceMetaInfo lastSelectedDataSliceMetaInfo;
    private Grid<DataSlicePropertyDefinitionVO> dataSlicePropertyDefinitionsGrid;
    private SecondaryKeyValueDisplayItem groupNameDisplayItem;
    private SecondaryKeyValueDisplayItem primaryDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem backupDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalDataCountDisplayItem;
    private SecondaryKeyValueDisplayItem sliceAtomicityDisplayItem;
    private SecondaryKeyValueDisplayItem backupNumberDisplayItem;
    private SecondaryKeyValueDisplayItem sliceStorageModeDisplayItem;
    private TextField dataSliceNameFilterField;
    private TextField dataSliceGroupFilterField;
    private int currentDataSliceCount;

    private class DataSlicePropertyDefinitionVO{
        private String propertyName;
        private DataSlicePropertyType dataSlicePropertyType;
        private boolean isPrimaryKey;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public DataSlicePropertyType getDataSlicePropertyType() {
            return dataSlicePropertyType;
        }

        public void setDataSlicePropertyType(DataSlicePropertyType dataSlicePropertyType) {
            this.dataSlicePropertyType = dataSlicePropertyType;
        }

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            isPrimaryKey = primaryKey;
        }
    }

    public ComputeGridDataSliceConfigurationView(){
        SecondaryIconTitle sectionTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.CLONE.create(),"数据切片配置");
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

        Button addGridDataSliceButton= new Button("创建网格数据切片");
        addGridDataSliceButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        addGridDataSliceButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addGridDataSliceButton);
        addGridDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateDataSliceView();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据切片定义:",null);
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
        dataSliceNameFilterField.setPlaceholder("数据切片名称");
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
                filterDataSlices();
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
                cancelFilterDataSlices();
            }
        });

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(dataSliceMetaInfo -> {
            Icon queryIcon = new Icon(VaadinIcon.RECORDS);
            queryIcon.setSize("20px");
            Button queryDataSliceButton = new Button(queryIcon, event -> {
                renderQueryDataSliceUI((DataSliceMetaInfo)dataSliceMetaInfo);
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
                    renderEmptyDataSliceUI((DataSliceMetaInfo)dataSliceMetaInfo);
                }
            });
            cleanDataSliceButton.setTooltipText("清除数据切片所有数据");

            Icon deleteDataSliceIcon = new Icon(VaadinIcon.TRASH);
            deleteDataSliceIcon.setSize("21px");
            Button removeDataSliceButton = new Button(deleteDataSliceIcon, event -> {});
            removeDataSliceButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeDataSliceButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeDataSliceButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderRemoveDataSliceUI((DataSliceMetaInfo)dataSliceMetaInfo);
                }
            });
            removeDataSliceButton.setTooltipText("删除数据切片");

            HorizontalLayout buttons = new HorizontalLayout(queryDataSliceButton, cleanDataSliceButton,removeDataSliceButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        dataSliceMetaInfoGrid = new Grid<>();
        dataSliceMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        leftSideLayout.add(dataSliceMetaInfoGrid);

        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getDataSliceName).setHeader("切片名称").setKey("idx_0").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<DataSliceMetaInfo>() {
            @Override
            public String apply(DataSliceMetaInfo dataSliceMetaInfo) {
                return dataSliceMetaInfo.getDataSliceName();
            }
        });
        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getSliceGroupName).setHeader("切片分组").setKey("idx_1").setFlexGrow(1).setTooltipGenerator(new ItemLabelGenerator<DataSliceMetaInfo>() {
            @Override
            public String apply(DataSliceMetaInfo dataSliceMetaInfo) {
                return dataSliceMetaInfo.getSliceGroupName();
            }
        });
        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getStoreBackupNumber).setHeader("备份数量").setKey("idx_2").setWidth("100px").setFlexGrow(0).setResizable(false);
        dataSliceMetaInfoGrid.addColumn(new NumberRenderer<>(DataSliceMetaInfo::getPrimaryDataCount,NumberFormat.getIntegerInstance())).setHeader("切片数据量").setKey("idx_3").setWidth("130px").setFlexGrow(0);
        dataSliceMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("140px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"切片名称");
        dataSliceMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(VaadinIcon.ARCHIVES,"切片分组");
        dataSliceMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx2 = new LightGridColumnHeader(VaadinIcon.FLIP_H,"备份数量");
        dataSliceMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.STOCK,"切片主数据量");
        dataSliceMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        dataSliceMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);

        dataSliceMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<DataSliceMetaInfo>, DataSliceMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<DataSliceMetaInfo>, DataSliceMetaInfo> selectionEvent) {
                Set<DataSliceMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    dataSliceMetaInfoGrid.select(lastSelectedDataSliceMetaInfo);
                }else{
                    //singleConceptionKindSummaryInfoContainerLayout.setVisible(true);
                    DataSliceMetaInfo selectedDataSliceMetaInfo = selectedItemSet.iterator().next();
                    renderDataSliceOverview(selectedDataSliceMetaInfo);
                    lastSelectedDataSliceMetaInfo = selectedDataSliceMetaInfo;
                }
            }
        });

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setMargin(false);
        rightSideLayout.setWidth(545,Unit.PIXELS);
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
        dataSlicePropertyDefinitionsGrid.addColumn(DataSlicePropertyDefinitionVO::getPropertyName).setHeader("属性名称").setKey("idx_0");
        dataSlicePropertyDefinitionsGrid.addColumn(DataSlicePropertyDefinitionVO::getDataSlicePropertyType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("100px").setResizable(false);
        dataSlicePropertyDefinitionsGrid.addColumn(DataSlicePropertyDefinitionVO::isPrimaryKey).setHeader("切片主键").setKey("idx_2").setFlexGrow(0).setWidth("80px").setResizable(false);

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
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            dataSliceMetaInfoGrid.setHeight(event.getHeight()-385,Unit.PIXELS);
            dataSlicePropertyDefinitionsGrid.setHeight(event.getHeight()-650,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            dataSliceMetaInfoGrid.setHeight(browserHeight-385,Unit.PIXELS);
            dataSlicePropertyDefinitionsGrid.setHeight(browserHeight-650,Unit.PIXELS);
        }));
        renderGridDateSlicesInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        ResourceHolder.getApplicationBlackboard().removeListener(this);
        super.onDetach(detachEvent);
    }

    private void renderGridDateSlicesInfo(){
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        try {
            Set<DataSliceMetaInfo> dataSliceMetaInfoSet = targetComputeGrid.listDataSlice();
            this.dataSliceMetaInfoView = this.dataSliceMetaInfoGrid.setItems(dataSliceMetaInfoSet);
            this.dataSliceMetaInfoView.addFilter(item->{
                String dataSliceName = item.getDataSliceName();
                String dataSliceGroup = item.getSliceGroupName();

                boolean dataSliceNameFilterResult = true;
                if(!dataSliceNameFilterField.getValue().trim().equals("")){
                    if(dataSliceName.contains(dataSliceNameFilterField.getValue().trim())){
                        dataSliceNameFilterResult = true;
                    }else{
                        dataSliceNameFilterResult = false;
                    }
                }

                boolean dataSliceGroupFilterResult = true;
                if(!dataSliceGroupFilterField.getValue().trim().equals("")){
                    if(dataSliceGroup.contains(dataSliceGroupFilterField.getValue().trim())){
                        dataSliceGroupFilterResult = true;
                    }else{
                        dataSliceGroupFilterResult = false;
                    }
                }
                return dataSliceNameFilterResult & dataSliceGroupFilterResult;
            });
            this.currentDataSliceCount = dataSliceMetaInfoSet.size();
            this.gridDataSlicesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.currentDataSliceCount));
        } catch (ComputeGridException e) {
            //throw new RuntimeException(e);
        }
    }

    private void renderDataSliceOverview(DataSliceMetaInfo dataSliceMetaInfo){
        if(dataSliceMetaInfo != null){
            String dataSliceName = dataSliceMetaInfo.getDataSliceName();
            dataSliceInfoActionBar.updateTitleContent(dataSliceName);
            ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
            try {
                DataSliceDetailInfo dataSliceDetailInfo = targetComputeGrid.getDataSliceDetail(dataSliceName);
                if(dataSliceDetailInfo != null){
                    groupNameDisplayItem.updateDisplayValue(dataSliceDetailInfo.getSliceGroupName());
                    primaryDataCountDisplayItem.updateDisplayValue(numberFormat.format(dataSliceDetailInfo.getPrimaryDataCount()));
                    backupDataCountDisplayItem.updateDisplayValue(numberFormat.format(dataSliceDetailInfo.getBackupDataCount()));
                    totalDataCountDisplayItem.updateDisplayValue(numberFormat.format(dataSliceDetailInfo.getTotalDataCount()));
                    sliceAtomicityDisplayItem.updateDisplayValue(dataSliceDetailInfo.getAtomicityMode().toString());
                    backupNumberDisplayItem.updateDisplayValue(""+dataSliceDetailInfo.getStoreBackupNumber());
                    sliceStorageModeDisplayItem.updateDisplayValue(dataSliceDetailInfo.getDataStoreMode().toString());

                    Set<String> primaryKeyPropertiesNames = dataSliceDetailInfo.getPrimaryKeyPropertiesNames();

                    Map<String,DataSlicePropertyType> dataSlicePropertiesMap = dataSliceDetailInfo.getPropertiesDefinition();
                    List<DataSlicePropertyDefinitionVO> dataSlicePropertyDefinitionVOList = new ArrayList<>();
                    Set<String> propertyNameSet = dataSlicePropertiesMap.keySet();
                    for(String currentName : propertyNameSet){
                        DataSlicePropertyDefinitionVO currentDataSlicePropertyDefinitionVO = new DataSlicePropertyDefinitionVO();
                        currentDataSlicePropertyDefinitionVO.setPropertyName(currentName);
                        currentDataSlicePropertyDefinitionVO.setDataSlicePropertyType(dataSlicePropertiesMap.get(currentName));
                        if(primaryKeyPropertiesNames.contains(currentName)){
                            currentDataSlicePropertyDefinitionVO.setPrimaryKey(true);
                        }else{
                            currentDataSlicePropertyDefinitionVO.setPrimaryKey(false);
                        }
                        dataSlicePropertyDefinitionVOList.add(currentDataSlicePropertyDefinitionVO);
                    }
                    this.dataSlicePropertyDefinitionsGrid.setItems(dataSlicePropertyDefinitionVOList);
                }
            } catch (ComputeGridException e) {
                throw new RuntimeException(e);
            }
        }else{
            dataSliceInfoActionBar.updateTitleContent("-");
            groupNameDisplayItem.updateDisplayValue("-");
            primaryDataCountDisplayItem.updateDisplayValue("-");
            backupDataCountDisplayItem.updateDisplayValue("-");
            totalDataCountDisplayItem.updateDisplayValue("-");
            sliceAtomicityDisplayItem.updateDisplayValue("-");
            backupNumberDisplayItem.updateDisplayValue("-");
            sliceStorageModeDisplayItem.updateDisplayValue("-");
            this.dataSlicePropertyDefinitionsGrid.setItems(new ArrayList<>());
        }
    }

    private void filterDataSlices(){
        String dataSliceNameFilterValue = dataSliceNameFilterField.getValue().trim();
        String dataSliceGroupFilterValue = dataSliceGroupFilterField.getValue().trim();
        if(dataSliceNameFilterValue.equals("")&dataSliceGroupFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入数据切片名称 和/或 数据切片分组", NotificationVariant.LUMO_ERROR);
        }else{
            this.dataSliceMetaInfoView.refreshAll();
        }
    }

    private void cancelFilterDataSlices(){
        dataSliceNameFilterField.setValue("");
        dataSliceGroupFilterField.setValue("");
        this.dataSliceMetaInfoView.refreshAll();
    }

    private void renderCreateDataSliceView(){
        CreateDataSliceView createDataSliceView = new CreateDataSliceView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建网格数据切片",null,true,630,610,false);
        fixSizeWindow.setWindowContent(createDataSliceView);
        fixSizeWindow.setModel(true);
        createDataSliceView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderQueryDataSliceUI(DataSliceMetaInfo dataSliceMetaInfo){}

    private void renderEmptyDataSliceUI(DataSliceMetaInfo dataSliceMetaInfo){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认清除数据",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"清除数据切片数据",
                "请确认清除数据切片 "+ dataSliceMetaInfo.getDataSliceName()+" 中的所有数据",actionButtonList,550,175);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                try(ComputeGridObserver computeGridObserver = ComputeGridObserver.getObserverInstance()) {
                    computeGridObserver.emptyDataSlice(dataSliceMetaInfo.getDataSliceName());

                    if(lastSelectedDataSliceMetaInfo != null &&
                            lastSelectedDataSliceMetaInfo.getDataSliceName().equals(dataSliceMetaInfo.getDataSliceName())){
                        renderDataSliceOverview(dataSliceMetaInfo);
                        //lastSelectedDataSliceMetaInfo = dataSliceMetaInfo;
                    }

                    CommonUIOperationUtil.showPopupNotification("清除数据切片 "+dataSliceMetaInfo.getDataSliceName()+" 中的所有数据成功", NotificationVariant.LUMO_SUCCESS);
                    confirmWindow.closeConfirmWindow();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void renderRemoveDataSliceUI(DataSliceMetaInfo dataSliceMetaInfo){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除数据切片",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"删除数据切片",
                "请确认删除数据切片 "+ dataSliceMetaInfo.getDataSliceName(),actionButtonList,550,175);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                try(ComputeGridObserver computeGridObserver = ComputeGridObserver.getObserverInstance()) {
                    computeGridObserver.eraseDataSlice(dataSliceMetaInfo.getDataSliceName());

                    currentDataSliceCount--;
                    gridDataSlicesCountDisplayItem.updateDisplayValue(numberFormat.format(currentDataSliceCount));
                    ListDataProvider dataProvider = (ListDataProvider)dataSliceMetaInfoGrid.getDataProvider();
                    dataProvider.getItems().remove(dataSliceMetaInfo);
                    dataProvider.refreshAll();

                    if(lastSelectedDataSliceMetaInfo != null &&
                            lastSelectedDataSliceMetaInfo.getDataSliceName().equals(dataSliceMetaInfo.getDataSliceName())){
                        renderDataSliceOverview(null);
                        lastSelectedDataSliceMetaInfo = null;
                    }

                    CommonUIOperationUtil.showPopupNotification("删除数据切片 "+dataSliceMetaInfo.getDataSliceName()+" 成功", NotificationVariant.LUMO_SUCCESS);
                    confirmWindow.closeConfirmWindow();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    @Override
    public void receivedDataSliceCreatedEvent(DataSliceCreatedEvent event) {
        if(event.getDataSliceMetaInfo() != null){
            ListDataProvider dataProvider = (ListDataProvider)dataSliceMetaInfoGrid.getDataProvider();
            dataProvider.getItems().add(event.getDataSliceMetaInfo());
            dataProvider.refreshAll();
            this.currentDataSliceCount++;
            this.gridDataSlicesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.currentDataSliceCount));
        }
    }

    @Override
    public void receivedComputeGridRefreshEvent(ComputeGridRefreshEvent event) {
        renderGridDateSlicesInfo();
        renderDataSliceOverview(null);
    }
}
