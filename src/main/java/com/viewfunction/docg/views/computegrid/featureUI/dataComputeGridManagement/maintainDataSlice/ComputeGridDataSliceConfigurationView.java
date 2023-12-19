package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.element.commonComponent.*;

import java.text.NumberFormat;

public class ComputeGridDataSliceConfigurationView extends VerticalLayout {

    private NumberFormat numberFormat;

    public ComputeGridDataSliceConfigurationView(){

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.CUBES),"概念类型实体配置");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();

        PrimaryKeyValueDisplayItem conceptionEntitiesCountDisplayItem =
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
        //dataSlicesInfoContainerLayout.setSpacing(false);
        dataSlicesInfoContainerLayout.setPadding(false);
        dataSlicesInfoContainerLayout.setMargin(false);
        add(dataSlicesInfoContainerLayout);

        VerticalLayout leftSideLayout = new VerticalLayout();
        //leftSideLayout.setSpacing(false);
        leftSideLayout.setPadding(false);
        leftSideLayout.setMargin(false);


        dataSlicesInfoContainerLayout.add(leftSideLayout);


        HorizontalLayout conceptionKindsSearchElementsContainerLayout = new HorizontalLayout();
        conceptionKindsSearchElementsContainerLayout.setSpacing(false);
        conceptionKindsSearchElementsContainerLayout.setMargin(false);
        leftSideLayout.add(conceptionKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        conceptionKindsSearchElementsContainerLayout.add(filterTitle);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        TextField conceptionKindNameFilterField = new TextField();
        conceptionKindNameFilterField.setPlaceholder("概念类型名称");
        conceptionKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindNameFilterField.setWidth(150,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindNameFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        conceptionKindsSearchElementsContainerLayout.add(plusIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        TextField conceptionKindDescFilterField = new TextField();
        conceptionKindDescFilterField.setPlaceholder("概念类型显示名称");
        conceptionKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindDescFilterField.setWidth(150,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindDescFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindDescFilterField);

        Button searchConceptionKindsButton = new Button("查找概念类型",new Icon(VaadinIcon.SEARCH));
        searchConceptionKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchConceptionKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindsSearchElementsContainerLayout.add(searchConceptionKindsButton);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchConceptionKindsButton);
        searchConceptionKindsButton.setWidth(115,Unit.PIXELS);
        searchConceptionKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterConceptionKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        conceptionKindsSearchElementsContainerLayout.add(divIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelFilterConceptionKinds();
            }
        });


        Grid<DataSliceMetaInfo> dataSliceMetaInfoGrid = new Grid<>();
        leftSideLayout.add(dataSliceMetaInfoGrid);


        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setSpacing(true);
        rightSideLayout.setMargin(true);
        rightSideLayout.setWidth(560,Unit.PIXELS);
        //rightSideLayout.setPadding(false);
        dataSlicesInfoContainerLayout.add(rightSideLayout);
        //dataSlicesInfoContainerLayout.setFlexGrow(1,rightSideLayout);

        rightSideLayout.getStyle().set("left","0px").set("top","-8px").set("position","relative");

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"概念类型概览");
        rightSideLayout.add(filterTitle2);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideLayout.add(secondaryTitleActionBar);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+5000+")");
        rightSideLayout.add(infoTitle1);


    }
}
