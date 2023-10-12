package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.ConceptionKindDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind.RelationKindDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelatedConceptionKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionKindCountDisplayItem;
    private Grid<ConceptionKind> conceptionKindMetaInfoGrid;
    private Grid<ConceptionKindAttachInfoVO> directRelatedConceptionKindInfoGrid;
    private ClassificationRelatedDataQueryCriteriaView classificationRelatedDataQueryCriteriaView;

    private class ConceptionKindAttachInfoVO {
        private  String conceptionKindName;
        private String relationKindName;
        private RelationDirection relationDirection;
        private Map<String, Object> relationData;

        public ConceptionKindAttachInfoVO(ConceptionKindAttachInfo conceptionKindAttachInfo){
            this.conceptionKindName = conceptionKindAttachInfo.getAttachedConceptionKind().getConceptionKindName();
            this.relationKindName = conceptionKindAttachInfo.getRelationAttachInfo().getRelationKind();
            this.relationData = conceptionKindAttachInfo.getRelationAttachInfo().getRelationData();
            this.relationDirection = conceptionKindAttachInfo.getRelationAttachInfo().getRelationDirection();
        }

        public String getConceptionKindName() {
            return conceptionKindName;
        }

        public String getRelationKindName() {
            return relationKindName;
        }

        public RelationDirection getRelationDirection() {
            return relationDirection;
        }

        public Map<String, Object> getRelationData() {
            return relationData;
        }
    }

    public RelatedConceptionKindsView(String classificationName){
        this.setPadding(false);
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关概念类型运行时信息");

        filterTitle1.getStyle().set("padding-top","10px");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
               // .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关概念类型数量:","-");

        TabSheet componentsSwitchTabSheet = new TabSheet();
        componentsSwitchTabSheet.setWidthFull();

        VerticalLayout directRelatedConceptionKindsContainer = new VerticalLayout();
        directRelatedConceptionKindsContainer.setPadding(false);
        directRelatedConceptionKindsContainer.setSpacing(false);
        directRelatedConceptionKindsContainer.setMargin(false);

        VerticalLayout advancedConceptionKindsQueryContainer = new VerticalLayout();
        advancedConceptionKindsQueryContainer.setPadding(false);
        advancedConceptionKindsQueryContainer.setSpacing(false);
        advancedConceptionKindsQueryContainer.setMargin(false);

        Tab directRelatedConceptionKindsInfoTab = componentsSwitchTabSheet.add("",directRelatedConceptionKindsContainer);
        Span info1Span =new Span();
        Icon info1Icon = new Icon(VaadinIcon.ALIGN_JUSTIFY);
        info1Icon.setSize("12px");
        NativeLabel info1Label = new NativeLabel(" 全量直接关联概念类型");
        info1Span.add(info1Icon,info1Label);
        directRelatedConceptionKindsInfoTab.add(info1Span);

        Tab advancedConceptionKindsQueryTab = componentsSwitchTabSheet.add("",advancedConceptionKindsQueryContainer);
        Span info2Span =new Span();
        Icon info2Icon = new Icon(VaadinIcon.CONTROLLER);
        info2Icon.setSize("12px");
        NativeLabel info2Label = new NativeLabel(" 关联概念类型自定义条件查询");
        info2Span.add(info2Icon,info2Label);
        advancedConceptionKindsQueryTab.add(info2Span);

        componentsSwitchTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {}
        });

        add(componentsSwitchTabSheet);

        VerticalLayout directRelatedConceptionKindMainContentContainerLayout = new VerticalLayout();
        directRelatedConceptionKindMainContentContainerLayout.setWidthFull();
        directRelatedConceptionKindMainContentContainerLayout.setPadding(false);
        directRelatedConceptionKindMainContentContainerLayout.setMargin(false);
        directRelatedConceptionKindsContainer.add(directRelatedConceptionKindMainContentContainerLayout);

        HorizontalLayout directRelatedConceptionKindFilterControlLayout = new HorizontalLayout();
        directRelatedConceptionKindFilterControlLayout.setSpacing(false);
        directRelatedConceptionKindFilterControlLayout.setMargin(false);
        directRelatedConceptionKindMainContentContainerLayout.add(directRelatedConceptionKindFilterControlLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        directRelatedConceptionKindFilterControlLayout.add(filterTitle);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        ComboBox relationKindSelect = new ComboBox();
        relationKindSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(315,Unit.PIXELS);
        relationKindSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo kindMetaInfo) {
                String itemLabelValue = kindMetaInfo.getKindName()+ " ("+
                        kindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<KindMetaInfo> reltionKindsList = coreRealm.getRelationKindsMetaInfo();
            relationKindSelect.setItems(reltionKindsList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        //relationKindSelect.setRenderer(createRenderer());
        directRelatedConceptionKindFilterControlLayout.add(relationKindSelect);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindSelect);

        HorizontalLayout divLayout1 = new HorizontalLayout();
        divLayout1.setWidth(6,Unit.PIXELS);
        directRelatedConceptionKindFilterControlLayout.add(divLayout1);

        ComboBox relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("选择关系方向");
        relationDirectionSelect.setWidth(100,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        directRelatedConceptionKindFilterControlLayout.add(relationDirectionSelect);

        ComponentRenderer _toolBarComponentRenderer0 = new ComponentRenderer<>(conceptionKindAttachInfoVO -> {
            Icon queryIcon = LineAwesomeIconsSvg.COG_SOLID.create();
            queryIcon.setSize("18px");
            Button configRelationKind = new Button(queryIcon, event -> {
                renderRelationKindQueryUI((ConceptionKindAttachInfoVO)conceptionKindAttachInfoVO);
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("配置关系类型定义");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                renderConceptionKindQueryUI((ConceptionKindAttachInfoVO)conceptionKindAttachInfoVO);
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configConceptionKind.setTooltipText("配置概念类型定义");

            Icon cleanKindIcon = new Icon(VaadinIcon.EDIT);
            cleanKindIcon.setSize("18px");
            Button editLinkProperties = new Button(cleanKindIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("编辑关联属性");
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.UNLINK);
            deleteKindIcon.setSize("21px");
            Button removeClassificationLink = new Button(deleteKindIcon, event -> {});
            removeClassificationLink.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeClassificationLink.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeClassificationLink.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeClassificationLink.setTooltipText("删除分类关联");
            removeClassificationLink.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configRelationKind,configConceptionKind, editLinkProperties,removeClassificationLink);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        directRelatedConceptionKindInfoGrid = new Grid<>();
        directRelatedConceptionKindMainContentContainerLayout.add(directRelatedConceptionKindInfoGrid);

        directRelatedConceptionKindInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        directRelatedConceptionKindInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        directRelatedConceptionKindInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        directRelatedConceptionKindInfoGrid.addColumn(ConceptionKindAttachInfoVO::getRelationKindName).setHeader("关系类型名称").setKey("idx_1");
        directRelatedConceptionKindInfoGrid.addColumn(ConceptionKindAttachInfoVO::getConceptionKindName).setHeader("概念类型名称").setKey("idx_2");
        directRelatedConceptionKindInfoGrid.addColumn(ConceptionKindAttachInfoVO::getRelationData).setHeader("关联关系属性").setKey("idx_3");
        directRelatedConceptionKindInfoGrid.addColumn(_toolBarComponentRenderer0).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("170px").setResizable(false);
        directRelatedConceptionKindInfoGrid.appendFooterRow();

        HorizontalLayout advancedConceptionKindsQueryMainContentContainerLayout = new HorizontalLayout();
        advancedConceptionKindsQueryMainContentContainerLayout.setWidthFull();
        advancedConceptionKindsQueryContainer.add(advancedConceptionKindsQueryMainContentContainerLayout);

        VerticalLayout advancedConceptionKindsQueryLeftSideContainerLayout = new VerticalLayout();
        advancedConceptionKindsQueryLeftSideContainerLayout.setWidth(700,Unit.PIXELS);
        advancedConceptionKindsQueryLeftSideContainerLayout.setMargin(false);
        advancedConceptionKindsQueryLeftSideContainerLayout.setPadding(false);
        VerticalLayout advancedConceptionKindsQueryRightSideContainerLayout = new VerticalLayout();
        advancedConceptionKindsQueryRightSideContainerLayout.setMargin(true);
        advancedConceptionKindsQueryRightSideContainerLayout.setPadding(false);

        advancedConceptionKindsQueryMainContentContainerLayout.add(advancedConceptionKindsQueryLeftSideContainerLayout);
        advancedConceptionKindsQueryMainContentContainerLayout.add(advancedConceptionKindsQueryRightSideContainerLayout);

        ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper =
                new ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper() {
            @Override
            public void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel) {
                queryRelatedConceptionKind(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            }
        };
        classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        advancedConceptionKindsQueryLeftSideContainerLayout.add(classificationRelatedDataQueryCriteriaView);

        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(entityStatisticsInfo -> {
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

        conceptionKindMetaInfoGrid = new Grid<>();
        conceptionKindMetaInfoGrid.setWidth(700,Unit.PIXELS);
        conceptionKindMetaInfoGrid.setHeight(600,Unit.PIXELS);
        conceptionKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindMetaInfoGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindMetaInfoGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        conceptionKindMetaInfoGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_5")
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

        advancedConceptionKindsQueryLeftSideContainerLayout.add(conceptionKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LINK),"分类关联信息");
        advancedConceptionKindsQueryRightSideContainerLayout.add(filterTitle2);
    }

    public void setHeight(int viewHeight){
        this.conceptionKindMetaInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
        this.directRelatedConceptionKindInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.conceptionKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadDirectRelatedConceptionKindsInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void queryRelatedConceptionKind(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        try {
            List<ConceptionKind>  conceptionKindList = targetClassification.getRelatedConceptionKinds(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            conceptionKindMetaInfoGrid.setItems(conceptionKindList);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDirectRelatedConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        List<ConceptionKindAttachInfo> directRelatedConceptionKindList = targetClassification.getAllDirectRelatedConceptionKindsInfo();
        List<ConceptionKindAttachInfoVO> conceptionKindAttachInfoVOList = new ArrayList<>();
        for(ConceptionKindAttachInfo currentConceptionKindAttachInfo:directRelatedConceptionKindList){
            conceptionKindAttachInfoVOList.add(new ConceptionKindAttachInfoVO(currentConceptionKindAttachInfo));
        }
        directRelatedConceptionKindInfoGrid.setItems(conceptionKindAttachInfoVOList);
    }

    private void renderRelationKindQueryUI(ConceptionKindAttachInfoVO conceptionKindAttachInfoVO){
        RelationKindDetailUI relationKindDetailUI = new RelationKindDetailUI(conceptionKindAttachInfoVO.relationKindName);
        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("10px");
        titleDetailLayout.add(relationKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel relationKindName = new NativeLabel(conceptionKindAttachInfoVO.relationKindName);
        titleDetailLayout.add(relationKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"关系类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindDetailUI);
        fullScreenWindow.show();
    }

    private void renderConceptionKindQueryUI(ConceptionKindAttachInfoVO conceptionKindAttachInfoVO){
        ConceptionKindDetailUI conceptionKindDetailUI = new ConceptionKindDetailUI(conceptionKindAttachInfoVO.conceptionKindName);
        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindName = new NativeLabel(conceptionKindAttachInfoVO.conceptionKindName);
        titleDetailLayout.add(conceptionKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"概念类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionKindDetailUI);
        fullScreenWindow.show();
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<ConceptionKindAttachInfoVO,Icon> {
        @Override
        public Icon apply(ConceptionKindAttachInfoVO conceptionKindAttachInfoVO) {
            Icon relationDirectionIcon = null;
            switch (conceptionKindAttachInfoVO.relationDirection){
                case FROM -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
                case TO -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
                switch (conceptionKindAttachInfoVO.relationDirection){
                    case FROM -> relationDirectionIcon.setTooltipText("From Classification");
                    case TO -> relationDirectionIcon.setTooltipText("To Classification");
                }
            }
            return relationDirectionIcon;
        }
    }
}
