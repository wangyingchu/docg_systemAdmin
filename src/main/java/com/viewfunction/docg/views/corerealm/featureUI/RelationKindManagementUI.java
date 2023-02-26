package com.viewfunction.docg.views.corerealm.featureUI;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class RelationKindManagementUI extends VerticalLayout {



    final ZoneId id = ZoneId.systemDefault();
    private TextField conceptionKindNameFilterField;
    private TextField conceptionKindDescFilterField;


    public RelationKindManagementUI(){
        Button refreshDataButton = new Button("刷新关系类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            //loadConceptionKindsInfo();
            //resetSingleConceptionKindSummaryInfoArea();
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span("Default CoreRealm"));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Relation Kind 关系类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> relationKindManagementOperationButtonList = new ArrayList<>();

        Button relationKindRelationGuideButton = new Button("关系实体关联分布概览",new Icon(VaadinIcon.SITEMAP));
        relationKindRelationGuideButton.setDisableOnClick(true);
        relationKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        relationKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindManagementOperationButtonList.add(relationKindRelationGuideButton);
        relationKindRelationGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindsCorrelationInfoSummaryUI(conceptionKindRelationGuideButton);
            }
        });

        Button createRelationKindButton = new Button("创建关系类型",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createRelationKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createRelationKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationKindManagementOperationButtonList.add(createRelationKindButton);
        createRelationKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateConceptionKindUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"关系类型定义:",relationKindManagementOperationButtonList);
        add(sectionActionBar);

        HorizontalLayout conceptionKindsInfoContainerLayout = new HorizontalLayout();
        conceptionKindsInfoContainerLayout.setSpacing(false);
        conceptionKindsInfoContainerLayout.setMargin(false);
        conceptionKindsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(conceptionKindsInfoContainerLayout);



        VerticalLayout conceptionKindMetaInfoGridContainerLayout = new VerticalLayout();
        conceptionKindMetaInfoGridContainerLayout.setSpacing(true);
        conceptionKindMetaInfoGridContainerLayout.setMargin(false);
        conceptionKindMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout conceptionKindsSearchElementsContainerLayout = new HorizontalLayout();
        conceptionKindsSearchElementsContainerLayout.setSpacing(false);
        conceptionKindsSearchElementsContainerLayout.setMargin(false);
        conceptionKindMetaInfoGridContainerLayout.add(conceptionKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        conceptionKindsSearchElementsContainerLayout.add(filterTitle);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        conceptionKindNameFilterField = new TextField();
        conceptionKindNameFilterField.setPlaceholder("关系类型名称");
        conceptionKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindNameFilterField.setWidth(250,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindNameFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        conceptionKindsSearchElementsContainerLayout.add(plusIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        conceptionKindDescFilterField = new TextField();
        conceptionKindDescFilterField.setPlaceholder("关系类型显示名称");
        conceptionKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindDescFilterField.setWidth(250,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindDescFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindDescFilterField);

        Button searchConceptionKindsButton = new Button("查找关系类型",new Icon(VaadinIcon.SEARCH));
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

        //conceptionKindMetaInfoGridContainerLayout.add(conceptionKindMetaInfoGrid);

        conceptionKindsInfoContainerLayout.add(conceptionKindMetaInfoGridContainerLayout);



























    }
}
