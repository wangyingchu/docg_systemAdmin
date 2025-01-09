package com.viewfunction.docg.views.dataAnalysis.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.ComputeGridDataSliceConfigurationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ProviderAnalysisFeatureConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem gridDataSlicesCountDisplayItem;


    private SecondaryTitleActionBar dataSliceInfoActionBar;
    private Registration listener;
    private Grid<DataSliceMetaInfo> dataSliceMetaInfoGrid;
    private GridListDataView<DataSliceMetaInfo> dataSliceMetaInfoView;
    private DataSliceMetaInfo lastSelectedDataSliceMetaInfo;
    //private Grid<ComputeGridDataSliceConfigurationView.DataSlicePropertyDefinitionVO> dataSlicePropertyDefinitionsGrid;
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

    public ProviderAnalysisFeatureConfigurationView() {






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





        Button addGridDataSliceButton= new Button("注册分析功能特性");
        addGridDataSliceButton.setIcon(LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create());
        addGridDataSliceButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addGridDataSliceButton);
        addGridDataSliceButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateDataSliceView();
            }
        });








        List<Component> dataAnalysisProviderManagementOperationButtonList = new ArrayList<>();

        Button registerAnalysisFunctionalFeatureButton = new Button("注册分析功能特性", LineAwesomeIconsSvg.CLIPBOARD_LIST_SOLID.create());
        registerAnalysisFunctionalFeatureButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        registerAnalysisFunctionalFeatureButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        dataAnalysisProviderManagementOperationButtonList.add(registerAnalysisFunctionalFeatureButton);
        registerAnalysisFunctionalFeatureButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateConceptionKindUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
         SectionActionBar sectionActionBar = new SectionActionBar(icon,"分析功能特性定义:",null);
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






    }
}
