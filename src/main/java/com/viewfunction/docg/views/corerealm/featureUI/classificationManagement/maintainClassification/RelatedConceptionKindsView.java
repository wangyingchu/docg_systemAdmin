package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;

public class RelatedConceptionKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionKindCountDisplayItem;
    public RelatedConceptionKindsView(String classificationName){
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关概念类型运行时信息");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关概念类型数量:",this.numberFormat.format(123456789));

        HorizontalLayout mainContentContainerLayout = new HorizontalLayout();
        mainContentContainerLayout.setWidthFull();
        add(mainContentContainerLayout);

        VerticalLayout leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(650,Unit.PIXELS);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        VerticalLayout rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setMargin(true);
        rightSideContainerLayout.setPadding(false);

        mainContentContainerLayout.add(leftSideContainerLayout);
        mainContentContainerLayout.add(rightSideContainerLayout);

        HorizontalLayout classificationsSearchElementsContainerLayout = new HorizontalLayout();
        classificationsSearchElementsContainerLayout.setSpacing(false);
        classificationsSearchElementsContainerLayout.setMargin(false);
        leftSideContainerLayout.add(classificationsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        classificationsSearchElementsContainerLayout.add(filterTitle);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        TextField classificationNameFilterField = new TextField();
        classificationNameFilterField.setPlaceholder("分类名称");
        classificationNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationNameFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationNameFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        classificationsSearchElementsContainerLayout.add(plusIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        TextField classificationDescFilterField = new TextField();
        classificationDescFilterField.setPlaceholder("分类描述");
        classificationDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationDescFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationDescFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationDescFilterField);

        Button searchClassificationsButton = new Button("查找分类",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(searchClassificationsButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.setWidth(90,Unit.PIXELS);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterClassifications();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        classificationsSearchElementsContainerLayout.add(divIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelFilterClassifications();
            }
        });




        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon queryIcon = new Icon(VaadinIcon.RECORDS);
            queryIcon.setSize("20px");
            Button queryConceptionKind = new Button(queryIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            queryConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            queryConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(queryConceptionKind, "查询概念类型实体");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindConfigurationUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(configConceptionKind, "配置概念类型定义");

            Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
            cleanKindIcon.setSize("21px");
            Button cleanConceptionKind = new Button(cleanKindIcon, event -> {});
            cleanConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cleanConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(cleanConceptionKind, "清除概念类型所有实例");
            cleanConceptionKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        //renderCleanConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeConceptionKind = new Button(deleteKindIcon, event -> {});
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeConceptionKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeConceptionKind, "删除概念类型");
            removeConceptionKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                        //renderRemoveConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(queryConceptionKind,configConceptionKind, cleanConceptionKind,removeConceptionKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });




        Grid<ConceptionKind> conceptionKindMetaInfoGrid = new Grid<>();
        conceptionKindMetaInfoGrid.setWidth(650,Unit.PIXELS);
        conceptionKindMetaInfoGrid.setHeight(600,Unit.PIXELS);
        conceptionKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindMetaInfoGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindMetaInfoGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1");

        conceptionKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5")
                .setFlexGrow(0).setWidth("170px").setResizable(false);


        /*
        //conceptionKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_2")
        //        .setComparator(createDateComparator)
        //        .setFlexGrow(0).setWidth("210px").setResizable(false);
        //conceptionKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_3")
        //        .setComparator(lastUpdateDateComparator)
        //        .setFlexGrow(0).setWidth("210px").setResizable(false);
        conceptionKindMetaInfoGrid.addColumn(new NumberRenderer<>(EntityStatisticsInfo::getEntitiesCount,NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getEntitiesCount() - entityStatisticsInfo2.getEntitiesCount()))
                .setHeader("类型包含实体数量").setKey("idx_4")
                .setFlexGrow(0).setWidth("150px").setResizable(false);

        */
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        /*
        conceptionKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.STOCK,"类型包含实体数量");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5);
        */
        conceptionKindMetaInfoGrid.appendFooterRow();

        leftSideContainerLayout.add(conceptionKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LINK),"分类关联信息");
        rightSideContainerLayout.add(filterTitle2);
    }

    public void renderRelatedConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();

        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        //targetClassification

    }
}
