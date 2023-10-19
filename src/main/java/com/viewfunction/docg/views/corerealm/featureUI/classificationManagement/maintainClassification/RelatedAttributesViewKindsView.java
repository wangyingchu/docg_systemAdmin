package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributesViewKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ClassificationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.AttributesViewKindDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind.RelationKindDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelatedAttributesViewKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem attributesViewKindCountDisplayItem;
    private TextField relationKindNameField;
    private ComboBox<String> relationDirectionSelect;
    private TextField attributesViewKindNameField;
    private Grid<AttributesViewKindAttachInfoVO> directRelatedAttributesViewKindInfoGrid;
    private Grid<AttributesViewKind> attributesViewKindMetaInfoGrid;
    private GridListDataView<AttributesViewKindAttachInfoVO> directRelatedAttributesViewKindInfoGridListDataView;
    private ClassificationRelatedDataQueryCriteriaView classificationRelatedDataQueryCriteriaView;
    private String currentAdvanceQueryRelationKindName;
    private RelationDirection currentAdvanceQueryRelationDirection;
    private boolean currentAdvanceQueryIncludeOffspringClassifications;
    private int currentAdvanceQueryOffspringLevel;
    private NativeLabel classificationLinkRelationKindLabel;
    private NativeLabel classificationLinkEntityUIDLabel;
    private Button showClassificationLinkDetailButton;
    private Grid<AttributeValueVO> classificationLinkEntityAttributesInfoGrid;
    private String currentSelectedClassificationLinkEntityKind;
    private String currentSelectedClassificationLinkEntityUID;

    private class AttributesViewKindAttachInfoVO {
        private  String attributesViewKindName;
        private  String attributesViewKindUID;
        private String relationKindName;
        private RelationDirection relationDirection;
        private Map<String, Object> relationData;
        private String relationEntityUID;

        public AttributesViewKindAttachInfoVO(AttributesViewKindAttachInfo attributesViewKindAttachInfo){
            this.attributesViewKindName = attributesViewKindAttachInfo.getAttachedAttributesViewKind().getAttributesViewKindName();
            this.attributesViewKindUID = attributesViewKindAttachInfo.getAttachedAttributesViewKind().getAttributesViewKindUID();
            this.relationKindName = attributesViewKindAttachInfo.getRelationAttachInfo().getRelationKind();
            this.relationData = attributesViewKindAttachInfo.getRelationAttachInfo().getRelationData();
            this.relationDirection = attributesViewKindAttachInfo.getRelationAttachInfo().getRelationDirection();
            this.relationEntityUID = attributesViewKindAttachInfo.getRelationAttachInfo().getRelationEntityUID();
        }

        public String getAttributesViewKindName() {
            return attributesViewKindName;
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

        public String getRelationEntityUID() {
            return relationEntityUID;
        }

        public void setRelationEntityUID(String relationEntityUID) {
            this.relationEntityUID = relationEntityUID;
        }

        public String getAttributesViewKindUID() {
            return attributesViewKindUID;
        }
    }

    public RelatedAttributesViewKindsView(String classificationName){
        this.setPadding(false);
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关属性视图类型运行时信息");
        filterTitle1.getStyle().set("padding-top","10px");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.attributesViewKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关属性视图类型数量:","-");

        TabSheet componentsSwitchTabSheet = new TabSheet();
        componentsSwitchTabSheet.setWidthFull();

        VerticalLayout directRelatedAttributesViewKindsContainer = new VerticalLayout();
        directRelatedAttributesViewKindsContainer.setPadding(false);
        directRelatedAttributesViewKindsContainer.setSpacing(false);
        directRelatedAttributesViewKindsContainer.setMargin(false);

        VerticalLayout advancedAttributesViewKindsQueryContainer = new VerticalLayout();
        advancedAttributesViewKindsQueryContainer.setPadding(false);
        advancedAttributesViewKindsQueryContainer.setSpacing(false);
        advancedAttributesViewKindsQueryContainer.setMargin(false);

        Tab directRelatedAttributesViewKindsInfoTab = componentsSwitchTabSheet.add("",directRelatedAttributesViewKindsContainer);
        Span info1Span =new Span();
        Icon info1Icon = new Icon(VaadinIcon.ALIGN_JUSTIFY);
        info1Icon.setSize("12px");
        NativeLabel info1Label = new NativeLabel(" 全量直接关联属性视图类型");
        info1Span.add(info1Icon,info1Label);
        directRelatedAttributesViewKindsInfoTab.add(info1Span);

        Tab advancedAttributesViewKindsQueryTab = componentsSwitchTabSheet.add("",advancedAttributesViewKindsQueryContainer);
        Span info2Span =new Span();
        Icon info2Icon = new Icon(VaadinIcon.CONTROLLER);
        info2Icon.setSize("12px");
        NativeLabel info2Label = new NativeLabel(" 关联属性视图类型自定义条件查询");
        info2Span.add(info2Icon,info2Label);
        advancedAttributesViewKindsQueryTab.add(info2Span);

        componentsSwitchTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {}
        });

        add(componentsSwitchTabSheet);

        VerticalLayout directRelatedAttributesViewKindMainContentContainerLayout = new VerticalLayout();
        directRelatedAttributesViewKindMainContentContainerLayout.setWidthFull();
        directRelatedAttributesViewKindMainContentContainerLayout.setPadding(false);
        directRelatedAttributesViewKindMainContentContainerLayout.setMargin(false);
        directRelatedAttributesViewKindsContainer.add(directRelatedAttributesViewKindMainContentContainerLayout);

        HorizontalLayout directRelatedAttributesViewKindFilterControlLayout = new HorizontalLayout();
        directRelatedAttributesViewKindFilterControlLayout.setSpacing(false);
        directRelatedAttributesViewKindFilterControlLayout.setMargin(false);
        directRelatedAttributesViewKindMainContentContainerLayout.add(directRelatedAttributesViewKindFilterControlLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        directRelatedAttributesViewKindFilterControlLayout.add(filterTitle);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        relationKindNameField = new TextField();
        relationKindNameField.setPlaceholder("关联关系类型名称");
        relationKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedAttributesViewKindFilterControlLayout.add(relationKindNameField);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindNameField);

        Icon plusIcon0 = new Icon(VaadinIcon.PLUS);
        plusIcon0.setSize("12px");
        directRelatedAttributesViewKindFilterControlLayout.add(plusIcon0);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon0);

        relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("关联关系方向");
        relationDirectionSelect.setWidth(120,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        directRelatedAttributesViewKindFilterControlLayout.add(relationDirectionSelect);

        Icon plusIcon1 = new Icon(VaadinIcon.PLUS);
        plusIcon1.setSize("12px");
        directRelatedAttributesViewKindFilterControlLayout.add(plusIcon1);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon1);

        attributesViewKindNameField = new TextField();
        attributesViewKindNameField.setPlaceholder("相关属性视图类型名称");
        attributesViewKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributesViewKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedAttributesViewKindFilterControlLayout.add(attributesViewKindNameField);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, attributesViewKindNameField);

        Button searchRelationKindsButton = new Button("查找属性视图类型关联",new Icon(VaadinIcon.SEARCH));
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedAttributesViewKindFilterControlLayout.add(searchRelationKindsButton);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationKindsButton);
        searchRelationKindsButton.setWidth(140,Unit.PIXELS);
        searchRelationKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterAttributesViewKindLinks();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        directRelatedAttributesViewKindFilterControlLayout.add(divIcon);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedAttributesViewKindFilterControlLayout.add(clearSearchCriteriaButton);
        directRelatedAttributesViewKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterAttributesViewKindLinks();
            }
        });

        ComponentRenderer _toolBarComponentRenderer0 = new ComponentRenderer<>(attributesViewKindAttachInfoVO -> {
            Icon queryIcon = LineAwesomeIconsSvg.COG_SOLID.create();
            queryIcon.setSize("18px");
            Button configRelationKind = new Button(queryIcon, event -> {
                renderRelationKindDetailUI((AttributesViewKindAttachInfoVO)attributesViewKindAttachInfoVO);
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("关联关系类型定义配置");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributesViewKindAttachInfoVO_Kind = new Button(configIcon, event -> {
                renderAttributesViewKindDetailUI(((AttributesViewKindAttachInfoVO)attributesViewKindAttachInfoVO).getAttributesViewKindName(),
                        ((AttributesViewKindAttachInfoVO)attributesViewKindAttachInfoVO).getAttributesViewKindUID());
            });
            configAttributesViewKindAttachInfoVO_Kind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributesViewKindAttachInfoVO_Kind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configAttributesViewKindAttachInfoVO_Kind.setTooltipText("相关属性视图类型定义配置");

            Icon linkIcon = LineAwesomeIconsSvg.LINK_SOLID.create();
            linkIcon.setSize("17px");
            Button editLinkProperties = new Button(linkIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("关联链接信息");
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderRelationEntityDetailUI(((AttributesViewKindAttachInfoVO) attributesViewKindAttachInfoVO).relationKindName,
                            ((AttributesViewKindAttachInfoVO) attributesViewKindAttachInfoVO).getRelationEntityUID());
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
                    renderDetachAttributesViewKindUI((AttributesViewKindAttachInfoVO)attributesViewKindAttachInfoVO);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configRelationKind,configAttributesViewKindAttachInfoVO_Kind, editLinkProperties,removeClassificationLink);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        directRelatedAttributesViewKindInfoGrid = new Grid<>();
        directRelatedAttributesViewKindInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        directRelatedAttributesViewKindInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        directRelatedAttributesViewKindInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        directRelatedAttributesViewKindInfoGrid.addColumn(AttributesViewKindAttachInfoVO::getRelationKindName).setHeader("关联关系类型名称").setKey("idx_1");
        directRelatedAttributesViewKindInfoGrid.addColumn(AttributesViewKindAttachInfoVO::getAttributesViewKindName).setHeader("相关属性视图类型名称").setKey("idx_2");
        directRelatedAttributesViewKindInfoGrid.addColumn(AttributesViewKindAttachInfoVO::getRelationData).setHeader("关联关系属性").setKey("idx_3");
        directRelatedAttributesViewKindInfoGrid.addColumn(_toolBarComponentRenderer0).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("150px").setResizable(false);
        directRelatedAttributesViewKindInfoGrid.appendFooterRow();

        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT,"关联关系类型名称");
        directRelatedAttributesViewKindInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.TASKS,"相关属性视图类型名称");
        directRelatedAttributesViewKindInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关联关系属性");
        directRelatedAttributesViewKindInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        directRelatedAttributesViewKindInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4);

        directRelatedAttributesViewKindMainContentContainerLayout.add(directRelatedAttributesViewKindInfoGrid);

        HorizontalLayout advancedAttributesViewKindAttachInfoVO_KindsQueryMainContentContainerLayout = new HorizontalLayout();
        advancedAttributesViewKindAttachInfoVO_KindsQueryMainContentContainerLayout.setWidthFull();
        advancedAttributesViewKindsQueryContainer.add(advancedAttributesViewKindAttachInfoVO_KindsQueryMainContentContainerLayout);

        VerticalLayout advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout = new VerticalLayout();
        advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout.setWidth(700,Unit.PIXELS);
        advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout.setMargin(false);
        advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout.setPadding(false);
        VerticalLayout advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout = new VerticalLayout();
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.setMargin(true);
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.setPadding(false);

        advancedAttributesViewKindAttachInfoVO_KindsQueryMainContentContainerLayout.add(advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout);
        advancedAttributesViewKindAttachInfoVO_KindsQueryMainContentContainerLayout.add(advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout);

        ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper =
                new ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper() {
                    @Override
                    public void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel) {
                        currentAdvanceQueryRelationKindName = relationKindName;
                        currentAdvanceQueryRelationDirection = relationDirection;
                        currentAdvanceQueryIncludeOffspringClassifications = includeOffspringClassifications;
                        currentAdvanceQueryOffspringLevel = offspringLevel;
                        clearClassificationLinkRelationInfo();
                        queryRelatedAttributesViewKind(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
                    }
                };
        classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout.add(classificationRelatedDataQueryCriteriaView);

        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(attributesViewKind -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributesViewKind = new Button(configIcon, event -> {
                if(attributesViewKind instanceof AttributesViewKind){
                    renderAttributesViewKindDetailUI(((AttributesViewKind)attributesViewKind).getAttributesViewKindName(),
                            ((AttributesViewKind)attributesViewKind).getAttributesViewKindUID());
                }
            });
            configAttributesViewKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributesViewKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configAttributesViewKind.setTooltipText("关联属性视图类型定义配置");

            HorizontalLayout buttons = new HorizontalLayout(configAttributesViewKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        attributesViewKindMetaInfoGrid = new Grid<>();
        attributesViewKindMetaInfoGrid.setWidth(700,Unit.PIXELS);
        attributesViewKindMetaInfoGrid.setHeight(600,Unit.PIXELS);
        attributesViewKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributesViewKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributesViewKindMetaInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindName).setHeader("属性视图类型名称").setKey("idx_0");
        attributesViewKindMetaInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindDesc).setHeader("属性视图类型显示名称").setKey("idx_1");
        attributesViewKindMetaInfoGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_2_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性视图类型名称");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_2_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"属性视图类型显示名称");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_2_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx2 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributesViewKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_2_idx2);
        attributesViewKindMetaInfoGrid.appendFooterRow();
        attributesViewKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<AttributesViewKind>, AttributesViewKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<AttributesViewKind>, AttributesViewKind> selectionEvent) {
                Set<AttributesViewKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    //unselected item, reset link detail info
                    clearClassificationLinkRelationInfo();
                }else{
                    AttributesViewKind selectedAttributesViewKind = selectedItemSet.iterator().next();
                    renderSelectedAttributesViewKindLinkInfo(selectedAttributesViewKind);
                }
            }
        });

        advancedAttributesViewKindAttachInfoVO_KindsQueryLeftSideContainerLayout.add(attributesViewKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(LineAwesomeIconsSvg.LINK_SOLID.create(),"关联链接信息");
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(filterTitle2);

        HorizontalLayout classificationLinkNameInfoContainer = new HorizontalLayout();
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(classificationLinkNameInfoContainer);
        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("16px");
        relationKindIcon.getStyle().set("padding-right","3px");
        classificationLinkNameInfoContainer.add(relationKindIcon);
        classificationLinkNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,relationKindIcon);
        classificationLinkRelationKindLabel = new NativeLabel("-");
        classificationLinkRelationKindLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        classificationLinkRelationKindLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        classificationLinkNameInfoContainer.getStyle().set("padding-top","15px");
        classificationLinkNameInfoContainer.add(classificationLinkRelationKindLabel);
        classificationLinkNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,classificationLinkRelationKindLabel);

        HorizontalLayout classificationLinkEntityUIDInfoContainer = new HorizontalLayout();
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(classificationLinkEntityUIDInfoContainer);
        Icon linkRelationEntityIcon = VaadinIcon.KEY_O.create();
        linkRelationEntityIcon.setSize("18px");
        linkRelationEntityIcon.getStyle().set("padding-right","3px");
        classificationLinkEntityUIDInfoContainer.add(linkRelationEntityIcon);
        classificationLinkEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,linkRelationEntityIcon);

        classificationLinkEntityUIDLabel = new NativeLabel("-");
        classificationLinkEntityUIDLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        classificationLinkEntityUIDLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        classificationLinkEntityUIDInfoContainer.getStyle().set("padding-top","5px");
        classificationLinkEntityUIDInfoContainer.add(classificationLinkEntityUIDLabel);
        classificationLinkEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,classificationLinkEntityUIDLabel);

        showClassificationLinkDetailButton = new Button("显示关系实体详情");
        showClassificationLinkDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showClassificationLinkDetailButton.getStyle().set("font-size","12px");
        showClassificationLinkDetailButton.setIcon(VaadinIcon.EYE.create());
        Tooltips.getCurrent().setTooltip(showClassificationLinkDetailButton, "显示关系实体详情");
        showClassificationLinkDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelationEntityDetailUI(currentSelectedClassificationLinkEntityKind,currentSelectedClassificationLinkEntityUID);
            }
        });
        classificationLinkEntityUIDInfoContainer.add(showClassificationLinkDetailButton);
        classificationLinkEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,showClassificationLinkDetailButton);
        classificationLinkEntityUIDInfoContainer.getStyle().set("padding-bottom", "3px");
        showClassificationLinkDetailButton.setEnabled(false);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"关系实体属性");
        infoTitle1.getStyle().set("padding-top","15px");
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(infoTitle1);
        HorizontalLayout horizontalDiv02 = new HorizontalLayout();
        horizontalDiv02.setWidthFull();
        horizontalDiv02.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(horizontalDiv02);

        classificationLinkEntityAttributesInfoGrid = new Grid<>();
        classificationLinkEntityAttributesInfoGrid.setWidth(100, Unit.PERCENTAGE);
        classificationLinkEntityAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        classificationLinkEntityAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        classificationLinkEntityAttributesInfoGrid.addColumn(AttributeValueVO::getAttributeName).setHeader("属性名称").setKey("idx_0");
        classificationLinkEntityAttributesInfoGrid.addColumn(AttributeValueVO::getAttributeValue).setHeader("属性值").setKey("idx_1").setFlexGrow(1).setResizable(true);
        LightGridColumnHeader gridColumnHeader_3_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        classificationLinkEntityAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_3_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_3_idx1 = new LightGridColumnHeader(VaadinIcon.INPUT,"属性值");
        classificationLinkEntityAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_3_idx1).setSortable(true);
        classificationLinkEntityAttributesInfoGrid.setHeight(400,Unit.PIXELS);
        advancedAttributesViewKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(classificationLinkEntityAttributesInfoGrid);
    }

    public void setHeight(int viewHeight){
        this.attributesViewKindMetaInfoGrid.setHeight(viewHeight-125, Unit.PIXELS);
        this.directRelatedAttributesViewKindInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.attributesViewKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    public void loadDirectRelatedAttributesViewKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        List<AttributesViewKindAttachInfo> directRelatedAttributesViewKindList = targetClassification.getAllDirectRelatedAttributesViewKindsInfo();
        List<AttributesViewKindAttachInfoVO> attributesViewKindAttachInfoVOList = new ArrayList<>();
        for(AttributesViewKindAttachInfo currentAttributesViewKindAttachInfo:directRelatedAttributesViewKindList){
            attributesViewKindAttachInfoVOList.add(new AttributesViewKindAttachInfoVO(currentAttributesViewKindAttachInfo));
        }
        this.directRelatedAttributesViewKindInfoGridListDataView = this.directRelatedAttributesViewKindInfoGrid.setItems(attributesViewKindAttachInfoVOList);
        this.directRelatedAttributesViewKindInfoGridListDataView.addFilter(item->{
            String attributesViewKindName = item.attributesViewKindName;
            String relationKindName = item.getRelationKindName();
            RelationDirection relationDirection = item.getRelationDirection();

            boolean relationKindNameFilterResult = true;
            if(!relationKindNameField.getValue().trim().equals("")){
                if(relationKindName.contains(relationKindNameField.getValue().trim())){
                    relationKindNameFilterResult = true;
                }else{
                    relationKindNameFilterResult = false;
                }
            }

            boolean attributesViewKindNameFilterResult = true;
            if(!attributesViewKindNameField.getValue().trim().equals("")){
                if(attributesViewKindName.contains(attributesViewKindNameField.getValue().trim())){
                    attributesViewKindNameFilterResult = true;
                }else{
                    attributesViewKindNameFilterResult = false;
                }
            }

            boolean relationDirectionFilterResult = true;
            String selectedRelationDirectionValue = relationDirectionSelect.getValue();
            if(selectedRelationDirectionValue != null){
                if(!selectedRelationDirectionValue.equals("TWO_WAY")){
                    if(relationDirection.toString().equals(selectedRelationDirectionValue)){
                        relationDirectionFilterResult = true;
                    }else{
                        relationDirectionFilterResult = false;
                    }
                }
            }
            return relationKindNameFilterResult & attributesViewKindNameFilterResult & relationDirectionFilterResult;
        });
    }

    private void filterAttributesViewKindLinks(){
        String relationKindFilterValue = relationKindNameField.getValue().trim();
        String attributesViewKindFilterValue = attributesViewKindNameField.getValue().trim();
        if(relationKindFilterValue.equals("") & attributesViewKindFilterValue.equals("") & relationDirectionSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请输入或选择关系类型名称 和/或 属性视图类型名称 和/或 关联关系方向", NotificationVariant.LUMO_WARNING);
        }else{
            this.directRelatedAttributesViewKindInfoGridListDataView.refreshAll();
        }
    }

    private void cancelFilterAttributesViewKindLinks(){
        relationKindNameField.setValue("");
        attributesViewKindNameField.setValue("");
        relationDirectionSelect.setValue(null);
        this.directRelatedAttributesViewKindInfoGridListDataView.refreshAll();
    }

    private void renderRelationKindDetailUI(AttributesViewKindAttachInfoVO attributesViewKindAttachInfoVO){
        RelationKindDetailUI relationKindDetailUI = new RelationKindDetailUI(attributesViewKindAttachInfoVO.getRelationKindName());
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
        NativeLabel relationKindName = new NativeLabel(attributesViewKindAttachInfoVO.getRelationKindName());
        titleDetailLayout.add(relationKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"关系类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindDetailUI);
        fullScreenWindow.show();
    }

    private void renderAttributesViewKindDetailUI(String attributesViewKindStr, String attributesViewKindUIDStr){
        AttributesViewKindDetailUI attributesViewKindDetailUI = new AttributesViewKindDetailUI(attributesViewKindUIDStr);
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

        Icon attributesViewKindIcon = VaadinIcon.TASKS.create();
        attributesViewKindIcon.setSize("10px");
        titleDetailLayout.add(attributesViewKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributesViewKindName = new NativeLabel(attributesViewKindStr+" ( ");
        titleDetailLayout.add(attributesViewKindName);

        Icon _UIDIcon = VaadinIcon.KEY_O.create();
        _UIDIcon.setSize("10px");
        titleDetailLayout.add(_UIDIcon);
        NativeLabel attributesViewKindUID = new NativeLabel(" "+attributesViewKindUIDStr+")");
        titleDetailLayout.add(attributesViewKindUID);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"属性视图类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(attributesViewKindDetailUI);
        fullScreenWindow.show();
    }

    private void renderRelationEntityDetailUI(String relationKindName,String relationEntityUID){
        RelationEntityDetailUI relationEntityDetailUI = new RelationEntityDetailUI(relationKindName,relationEntityUID);

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
        NativeLabel attributesViewKindNameLabel = new NativeLabel(relationKindName);
        titleDetailLayout.add(attributesViewKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon relationEntityIcon = VaadinIcon.KEY_O.create();
        relationEntityIcon.setSize("10px");
        titleDetailLayout.add(relationEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel relationEntityUIDLabel = new NativeLabel(relationEntityUID);
        titleDetailLayout.add(relationEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationEntityDetailUI);
        relationEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void renderDetachAttributesViewKindUI(AttributesViewKindAttachInfoVO attributesViewKindAttachInfoVO){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除分类关联",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行删除分类关联 "+ this.classificationName+" - ["+ attributesViewKindAttachInfoVO.relationKindName+"] -"+
                        attributesViewKindAttachInfoVO.getAttributesViewKindName()+" 的操作",actionButtonList,500,180);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDetachClassificationLink(attributesViewKindAttachInfoVO,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDetachClassificationLink(AttributesViewKindAttachInfoVO attributesViewKindAttachInfoVO, ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindAttachInfoVO.getAttributesViewKindUID());
        RelationDirection attributesViewKindViewRelationDirection = RelationDirection.TWO_WAY;
        switch(attributesViewKindAttachInfoVO.relationDirection){
            case FROM -> attributesViewKindViewRelationDirection = RelationDirection.TO;
            case TO -> attributesViewKindViewRelationDirection = RelationDirection.FROM;
        }
        try {
            boolean detachResult = targetAttributesViewKind.detachClassification(this.classificationName,attributesViewKindAttachInfoVO.getRelationKindName(),attributesViewKindViewRelationDirection);
            if(detachResult){
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+attributesViewKindAttachInfoVO.relationKindName+"] -"+attributesViewKindAttachInfoVO.getAttributesViewKindName() +" 成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                ListDataProvider dataProvider=(ListDataProvider) directRelatedAttributesViewKindInfoGrid.getDataProvider();
                dataProvider.getItems().remove(attributesViewKindAttachInfoVO);
                dataProvider.refreshAll();
            }else{
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+attributesViewKindAttachInfoVO.relationKindName+"] -"+attributesViewKindAttachInfoVO.getAttributesViewKindName() +" 失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearClassificationLinkRelationInfo(){
        classificationLinkRelationKindLabel.setText("-");
        classificationLinkEntityUIDLabel.setText("-");
        showClassificationLinkDetailButton.setEnabled(false);
        List<AttributeValueVO> attributeValueVOList = new ArrayList<>();
        classificationLinkEntityAttributesInfoGrid.setItems(attributeValueVOList);
        currentSelectedClassificationLinkEntityKind = null;
        currentSelectedClassificationLinkEntityUID = null;
    }

    private void queryRelatedAttributesViewKind(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        try {
            List<AttributesViewKind>  attributesViewKindList = targetClassification.getRelatedAttributesViewKinds(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            attributesViewKindMetaInfoGrid.setItems(attributesViewKindList);
            CommonUIOperationUtil.showPopupNotification("查询关联概念类型成功,查询返回 "+attributesViewKindList.size()+" 项关联属性类型", NotificationVariant.LUMO_SUCCESS);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderSelectedAttributesViewKindLinkInfo(AttributesViewKind selectedAttributesViewKind){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification currentClassification = coreRealm.getClassification(this.classificationName);
        RelationAttachInfo targetRelationAttachInfo = null;
        if(!currentAdvanceQueryIncludeOffspringClassifications){
            List<AttributesViewKindAttachInfo> attributesViewKindAttachInfoList = currentClassification.getAllDirectRelatedAttributesViewKindsInfo();
            for(AttributesViewKindAttachInfo currentAttributesViewKindAttachInfo:attributesViewKindAttachInfoList){
                RelationAttachInfo relationAttachInfo = currentAttributesViewKindAttachInfo.getRelationAttachInfo();
                AttributesViewKind attributesViewKind = currentAttributesViewKindAttachInfo.getAttachedAttributesViewKind();
                if(selectedAttributesViewKind.getAttributesViewKindUID().equals(attributesViewKind.getAttributesViewKindUID()) &&
                        currentAdvanceQueryRelationKindName.equals(relationAttachInfo.getRelationKind()) &&
                        currentAdvanceQueryRelationDirection.toString().equals(relationAttachInfo.getRelationDirection().toString())
                ){
                    targetRelationAttachInfo = relationAttachInfo;
                }
            }
        }else{
            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(selectedAttributesViewKind.getAttributesViewKindUID());
            List<ClassificationAttachInfo> classificationAttachInfoList = targetAttributesViewKind.getAllAttachedClassificationsInfo();
            for(ClassificationAttachInfo currentClassificationAttachInfo:classificationAttachInfoList){
                RelationAttachInfo relationAttachInfo = currentClassificationAttachInfo.getRelationAttachInfo();
                Classification relatedClassification = currentClassificationAttachInfo.getAttachedClassification();
                RelationDirection classificationSideRelationDirection = RelationDirection.TWO_WAY;
                switch (relationAttachInfo.getRelationDirection()){
                    case FROM -> classificationSideRelationDirection = RelationDirection.TO;
                    case TO -> classificationSideRelationDirection = RelationDirection.FROM;
                }
                if(relationAttachInfo.getRelationKind().equals(currentAdvanceQueryRelationKindName) &&
                        classificationSideRelationDirection.toString().equals(currentAdvanceQueryRelationDirection.toString()) &&
                        relatedClassification.getClassificationName().equals(this.classificationName)){
                    targetRelationAttachInfo = relationAttachInfo;
                }
            }
        }
        if(targetRelationAttachInfo != null){
            classificationLinkRelationKindLabel.setText(targetRelationAttachInfo.getRelationKind());
            classificationLinkEntityUIDLabel.setText(targetRelationAttachInfo.getRelationEntityUID());
            showClassificationLinkDetailButton.setEnabled(true);
            Map<String,Object> relationData = targetRelationAttachInfo.getRelationData();
            Set<String> attributeNameSet = relationData.keySet();
            List<AttributeValueVO> attributeValueVOList = new ArrayList<>();
            for(String currentKey:attributeNameSet){
                AttributeValueVO currentAttributeValueVO = new AttributeValueVO(currentKey,relationData.get(currentKey));
                attributeValueVOList.add(currentAttributeValueVO);
            }
            classificationLinkEntityAttributesInfoGrid.setItems(attributeValueVOList);
            currentSelectedClassificationLinkEntityKind = targetRelationAttachInfo.getRelationKind();
            currentSelectedClassificationLinkEntityUID = targetRelationAttachInfo.getRelationEntityUID();
        }else{
            classificationLinkRelationKindLabel.setText("-");
            classificationLinkEntityUIDLabel.setText("-");
            showClassificationLinkDetailButton.setEnabled(false);
            List<AttributeValueVO> attributeValueVOList = new ArrayList<>();
            classificationLinkEntityAttributesInfoGrid.setItems(attributeValueVOList);
            currentSelectedClassificationLinkEntityKind = null;
            currentSelectedClassificationLinkEntityUID = null;
        }
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<AttributesViewKindAttachInfoVO,Icon> {
        @Override
        public Icon apply(AttributesViewKindAttachInfoVO attributesViewKindAttachInfoVO) {
            Icon relationDirectionIcon = null;
            switch (attributesViewKindAttachInfoVO.relationDirection){
                case FROM -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
                case TO -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
                switch (attributesViewKindAttachInfoVO.relationDirection){
                    case FROM -> relationDirectionIcon.setTooltipText("From Classification");
                    case TO -> relationDirectionIcon.setTooltipText("To Classification");
                }
            }
            return relationDirectionIcon;
        }
    }
}
