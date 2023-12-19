package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;

import java.text.NumberFormat;

public class ComputeGridDataSliceConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem gridDataSlicesCountDisplayItem;
    private SecondaryTitleActionBar dataSliceInfoActionBar;
    private Registration listener;
    private Grid<DataSliceMetaInfo> dataSliceMetaInfoGrid;

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
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create()," 网格数据切片数量:",this.numberFormat.format(100000));

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30, Unit.PIXELS);
        infoContainer.add(horSpaceDiv);

        Button addGridDataSliceButton= new Button("添加网格数据切片");
        addGridDataSliceButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        addGridDataSliceButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addGridDataSliceButton);
        addGridDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddConceptionEntityView();
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

        TextField dataSliceNameFilterField = new TextField();
        dataSliceNameFilterField.setPlaceholder("数据切片名称");
        dataSliceNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dataSliceNameFilterField.setWidth(150,Unit.PIXELS);
        dataSlicesSearchElementsContainerLayout.add(dataSliceNameFilterField);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,dataSliceNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        dataSlicesSearchElementsContainerLayout.add(plusIcon);
        dataSlicesSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        TextField dataSliceGroupFilterField = new TextField();
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
                //filterConceptionKinds();
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
                //cancelFilterConceptionKinds();
            }
        });

        dataSliceMetaInfoGrid = new Grid<>();
        dataSliceMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        leftSideLayout.add(dataSliceMetaInfoGrid);

        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getDataSliceName).setHeader("切片名称").setKey("idx_0");
        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getSliceGroupName).setHeader("切片分组").setKey("idx_1");
        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getDataStoreMode).setHeader("存储模式").setKey("idx_2");
        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getStoreBackupNumber).setHeader("备份数量").setKey("idx_3");
        dataSliceMetaInfoGrid.addColumn(DataSliceMetaInfo::getPrimaryDataCount).setHeader("数据量").setKey("idx_4");

        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"切片名称");
        dataSliceMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setSpacing(true);
        rightSideLayout.setMargin(true);
        rightSideLayout.setWidth(545,Unit.PIXELS);
        dataSlicesInfoContainerLayout.add(rightSideLayout);
        rightSideLayout.getStyle().set("left","0px").set("top","-8px").set("position","relative");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"数据切片概览");
        rightSideLayout.add(filterTitle2);

        dataSliceInfoActionBar = new SecondaryTitleActionBar(LineAwesomeIconsSvg.CLONE.create(),"-",null,null);
        dataSliceInfoActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideLayout.add(dataSliceInfoActionBar);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+5000+")");
        rightSideLayout.add(infoTitle1);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            dataSliceMetaInfoGrid.setHeight(event.getHeight()-375,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            dataSliceMetaInfoGrid.setHeight(browserHeight-375,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
