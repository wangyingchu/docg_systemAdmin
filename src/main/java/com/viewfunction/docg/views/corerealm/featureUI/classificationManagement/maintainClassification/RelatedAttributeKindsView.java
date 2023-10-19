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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ClassificationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind.AttributeKindDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind.RelationKindDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelatedAttributeKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem attributeKindCountDisplayItem;
    private TextField relationKindNameField;
    private ComboBox<String> relationDirectionSelect;
    private TextField attributeKindNameField;
    private Grid<AttributeKindAttachInfoVO> directRelatedAttributeKindInfoGrid;
    private Grid<AttributeKind> attributeKindMetaInfoGrid;
    private GridListDataView<AttributeKindAttachInfoVO> directRelatedAttributeKindInfoGridListDataView;
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

    private class AttributeKindAttachInfoVO {
        private  String attributeKindName;
        private  String attributeKindUID;
        private String relationKindName;
        private RelationDirection relationDirection;
        private Map<String, Object> relationData;
        private String relationEntityUID;
        public AttributeKindAttachInfoVO(AttributeKindAttachInfo attributeKindAttachInfo){
            this.attributeKindName = attributeKindAttachInfo.getAttachedAttributeKind().getAttributeKindName();
            this.attributeKindUID = attributeKindAttachInfo.getAttachedAttributeKind().getAttributeKindUID();
            this.relationKindName = attributeKindAttachInfo.getRelationAttachInfo().getRelationKind();
            this.relationData = attributeKindAttachInfo.getRelationAttachInfo().getRelationData();
            this.relationDirection = attributeKindAttachInfo.getRelationAttachInfo().getRelationDirection();
            this.relationEntityUID = attributeKindAttachInfo.getRelationAttachInfo().getRelationEntityUID();
        }

        public String getAttributeKindName() {
            return attributeKindName;
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

        public String getAttributeKindUID() {
            return attributeKindUID;
        }
    }

    public RelatedAttributeKindsView(String classificationName){
        this.setPadding(false);
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关属性类型运行时信息");
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
        this.attributeKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关属性类型数量:","-");

        TabSheet componentsSwitchTabSheet = new TabSheet();
        componentsSwitchTabSheet.setWidthFull();

        VerticalLayout directRelatedAttributeKindsContainer = new VerticalLayout();
        directRelatedAttributeKindsContainer.setPadding(false);
        directRelatedAttributeKindsContainer.setSpacing(false);
        directRelatedAttributeKindsContainer.setMargin(false);

        VerticalLayout advancedAttributeKindsQueryContainer = new VerticalLayout();
        advancedAttributeKindsQueryContainer.setPadding(false);
        advancedAttributeKindsQueryContainer.setSpacing(false);
        advancedAttributeKindsQueryContainer.setMargin(false);

        Tab directRelatedAttributeKindsInfoTab = componentsSwitchTabSheet.add("",directRelatedAttributeKindsContainer);
        Span info1Span =new Span();
        Icon info1Icon = new Icon(VaadinIcon.ALIGN_JUSTIFY);
        info1Icon.setSize("12px");
        NativeLabel info1Label = new NativeLabel(" 全量直接关联属性类型");
        info1Span.add(info1Icon,info1Label);
        directRelatedAttributeKindsInfoTab.add(info1Span);

        Tab advancedAttributeKindsQueryTab = componentsSwitchTabSheet.add("",advancedAttributeKindsQueryContainer);
        Span info2Span =new Span();
        Icon info2Icon = new Icon(VaadinIcon.CONTROLLER);
        info2Icon.setSize("12px");
        NativeLabel info2Label = new NativeLabel(" 关联属性类型自定义条件查询");
        info2Span.add(info2Icon,info2Label);
        advancedAttributeKindsQueryTab.add(info2Span);

        componentsSwitchTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {}
        });

        add(componentsSwitchTabSheet);

        VerticalLayout directRelatedAttributeKindMainContentContainerLayout = new VerticalLayout();
        directRelatedAttributeKindMainContentContainerLayout.setWidthFull();
        directRelatedAttributeKindMainContentContainerLayout.setPadding(false);
        directRelatedAttributeKindMainContentContainerLayout.setMargin(false);
        directRelatedAttributeKindsContainer.add(directRelatedAttributeKindMainContentContainerLayout);

        HorizontalLayout directRelatedAttributeKindFilterControlLayout = new HorizontalLayout();
        directRelatedAttributeKindFilterControlLayout.setSpacing(false);
        directRelatedAttributeKindFilterControlLayout.setMargin(false);
        directRelatedAttributeKindMainContentContainerLayout.add(directRelatedAttributeKindFilterControlLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        directRelatedAttributeKindFilterControlLayout.add(filterTitle);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        relationKindNameField = new TextField();
        relationKindNameField.setPlaceholder("关联关系类型名称");
        relationKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedAttributeKindFilterControlLayout.add(relationKindNameField);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindNameField);

        Icon plusIcon0 = new Icon(VaadinIcon.PLUS);
        plusIcon0.setSize("12px");
        directRelatedAttributeKindFilterControlLayout.add(plusIcon0);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon0);

        relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("关联关系方向");
        relationDirectionSelect.setWidth(120,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        directRelatedAttributeKindFilterControlLayout.add(relationDirectionSelect);

        Icon plusIcon1 = new Icon(VaadinIcon.PLUS);
        plusIcon1.setSize("12px");
        directRelatedAttributeKindFilterControlLayout.add(plusIcon1);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon1);

        attributeKindNameField = new TextField();
        attributeKindNameField.setPlaceholder("相关属性类型名称");
        attributeKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributeKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedAttributeKindFilterControlLayout.add(attributeKindNameField);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, attributeKindNameField);

        Button searchRelationKindsButton = new Button("查找属性类型关联",new Icon(VaadinIcon.SEARCH));
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedAttributeKindFilterControlLayout.add(searchRelationKindsButton);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationKindsButton);
        searchRelationKindsButton.setWidth(140,Unit.PIXELS);
        searchRelationKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterAttributeKindLinks();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        directRelatedAttributeKindFilterControlLayout.add(divIcon);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedAttributeKindFilterControlLayout.add(clearSearchCriteriaButton);
        directRelatedAttributeKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterAttributeKindLinks();
            }
        });

        ComponentRenderer _toolBarComponentRenderer0 = new ComponentRenderer<>(attributeKindAttachInfoVO -> {
            Icon queryIcon = LineAwesomeIconsSvg.COG_SOLID.create();
            queryIcon.setSize("18px");
            Button configRelationKind = new Button(queryIcon, event -> {
                renderRelationKindDetailUI((AttributeKindAttachInfoVO)attributeKindAttachInfoVO);
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("关联关系类型定义配置");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributeKindAttachInfoVO_Kind = new Button(configIcon, event -> {
                AttributeKindAttachInfoVO attributeKindAttachInfoVO_ = (AttributeKindAttachInfoVO)attributeKindAttachInfoVO;
                renderAttributeKindDetailUI(attributeKindAttachInfoVO_.getAttributeKindName(),attributeKindAttachInfoVO_.getAttributeKindUID());
            });
            configAttributeKindAttachInfoVO_Kind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributeKindAttachInfoVO_Kind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configAttributeKindAttachInfoVO_Kind.setTooltipText("相关属性类型定义配置");

            Icon linkIcon = LineAwesomeIconsSvg.LINK_SOLID.create();
            linkIcon.setSize("17px");
            Button editLinkProperties = new Button(linkIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("关联链接信息");
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderRelationEntityDetailUI(((AttributeKindAttachInfoVO) attributeKindAttachInfoVO).relationKindName,
                            ((AttributeKindAttachInfoVO) attributeKindAttachInfoVO).getRelationEntityUID());
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
                    renderDetachAttributeKindUI((AttributeKindAttachInfoVO)attributeKindAttachInfoVO);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configRelationKind,configAttributeKindAttachInfoVO_Kind, editLinkProperties,removeClassificationLink);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        directRelatedAttributeKindInfoGrid = new Grid<>();
        directRelatedAttributeKindInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        directRelatedAttributeKindInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        directRelatedAttributeKindInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        directRelatedAttributeKindInfoGrid.addColumn(AttributeKindAttachInfoVO::getRelationKindName).setHeader("关联关系类型名称").setKey("idx_1");
        directRelatedAttributeKindInfoGrid.addColumn(AttributeKindAttachInfoVO::getAttributeKindName).setHeader("相关属性类型名称").setKey("idx_2");
        directRelatedAttributeKindInfoGrid.addColumn(AttributeKindAttachInfoVO::getRelationData).setHeader("关联关系属性").setKey("idx_3");
        directRelatedAttributeKindInfoGrid.addColumn(_toolBarComponentRenderer0).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("150px").setResizable(false);
        directRelatedAttributeKindInfoGrid.appendFooterRow();

        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT,"关联关系类型名称");
        directRelatedAttributeKindInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.INPUT,"相关属性类型名称");
        directRelatedAttributeKindInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关联关系属性");
        directRelatedAttributeKindInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        directRelatedAttributeKindInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4);

        directRelatedAttributeKindMainContentContainerLayout.add(directRelatedAttributeKindInfoGrid);

        HorizontalLayout advancedAttributeKindAttachInfoVO_KindsQueryMainContentContainerLayout = new HorizontalLayout();
        advancedAttributeKindAttachInfoVO_KindsQueryMainContentContainerLayout.setWidthFull();
        advancedAttributeKindsQueryContainer.add(advancedAttributeKindAttachInfoVO_KindsQueryMainContentContainerLayout);

        VerticalLayout advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout = new VerticalLayout();
        advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout.setWidth(700,Unit.PIXELS);
        advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout.setMargin(false);
        advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout.setPadding(false);
        VerticalLayout advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout = new VerticalLayout();
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.setMargin(true);
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.setPadding(false);

        advancedAttributeKindAttachInfoVO_KindsQueryMainContentContainerLayout.add(advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout);
        advancedAttributeKindAttachInfoVO_KindsQueryMainContentContainerLayout.add(advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout);

        ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper =
                new ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper() {
                    @Override
                    public void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel) {
                        currentAdvanceQueryRelationKindName = relationKindName;
                        currentAdvanceQueryRelationDirection = relationDirection;
                        currentAdvanceQueryIncludeOffspringClassifications = includeOffspringClassifications;
                        currentAdvanceQueryOffspringLevel = offspringLevel;
                        clearClassificationLinkRelationInfo();
                        queryRelatedAttributeKindAttachInfoVO_Kind(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
                    }
                };
        classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout.add(classificationRelatedDataQueryCriteriaView);

        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(attributeKind -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configAttributeKind = new Button(configIcon, event -> {
                if(attributeKind instanceof AttributeKind){
                    renderAttributeKindDetailUI(((AttributeKind)attributeKind).getAttributeKindName(),((AttributeKind)attributeKind).getAttributeKindUID());
                }
            });
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configAttributeKind.setTooltipText("关联概念类型定义配置");

            HorizontalLayout buttons = new HorizontalLayout(configAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        attributeKindMetaInfoGrid = new Grid<>();
        attributeKindMetaInfoGrid.setWidth(700,Unit.PIXELS);
        attributeKindMetaInfoGrid.setHeight(600,Unit.PIXELS);
        attributeKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributeKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeKindMetaInfoGrid.addColumn(AttributeKind::getAttributeKindName).setHeader("属性类型名称").setKey("idx_0");
        attributeKindMetaInfoGrid.addColumn(AttributeKind::getAttributeKindDesc).setHeader("属性类型显示名称").setKey("idx_1");
        attributeKindMetaInfoGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_2_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_2_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"属性类型显示名称");
        attributeKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_2_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx2 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributeKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_2_idx2);
        attributeKindMetaInfoGrid.appendFooterRow();
        attributeKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<AttributeKind>, AttributeKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<AttributeKind>, AttributeKind> selectionEvent) {
                Set<AttributeKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    //unselected item, reset link detail info
                    clearClassificationLinkRelationInfo();
                }else{
                    AttributeKind selectedAttributeKind = selectedItemSet.iterator().next();
                    renderSelectedAttributeKindLinkInfo(selectedAttributeKind);
                }
            }
        });

        advancedAttributeKindAttachInfoVO_KindsQueryLeftSideContainerLayout.add(attributeKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(LineAwesomeIconsSvg.LINK_SOLID.create(),"关联链接信息");
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(filterTitle2);

        HorizontalLayout classificationLinkNameInfoContainer = new HorizontalLayout();
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(classificationLinkNameInfoContainer);
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
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(classificationLinkEntityUIDInfoContainer);
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
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(infoTitle1);
        HorizontalLayout horizontalDiv02 = new HorizontalLayout();
        horizontalDiv02.setWidthFull();
        horizontalDiv02.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(horizontalDiv02);

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
        advancedAttributeKindAttachInfoVO_KindsQueryRightSideContainerLayout.add(classificationLinkEntityAttributesInfoGrid);
    }

    public void setHeight(int viewHeight){
        this.attributeKindMetaInfoGrid.setHeight(viewHeight-125, Unit.PIXELS);
        this.directRelatedAttributeKindInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.attributeKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    public void loadDirectRelatedAttributeKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        List<AttributeKindAttachInfo> directRelatedAttributeKindList = targetClassification.getAllDirectRelatedAttributeKindsInfo();
        List<AttributeKindAttachInfoVO> attributeKindAttachInfoVOList = new ArrayList<>();
        for(AttributeKindAttachInfo currentAttributeKindAttachInfo:directRelatedAttributeKindList){
            attributeKindAttachInfoVOList.add(new AttributeKindAttachInfoVO(currentAttributeKindAttachInfo));
        }
        this.directRelatedAttributeKindInfoGridListDataView = this.directRelatedAttributeKindInfoGrid.setItems(attributeKindAttachInfoVOList);
        this.directRelatedAttributeKindInfoGridListDataView.addFilter(item->{
            String attributeKindName = item.getAttributeKindName();
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

            boolean attributeKindNameFilterResult = true;
            if(!attributeKindNameField.getValue().trim().equals("")){
                if(attributeKindName.contains(attributeKindNameField.getValue().trim())){
                    attributeKindNameFilterResult = true;
                }else{
                    attributeKindNameFilterResult = false;
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
            return relationKindNameFilterResult & attributeKindNameFilterResult & relationDirectionFilterResult;
        });
    }

    private void filterAttributeKindLinks(){
        String relationKindFilterValue = relationKindNameField.getValue().trim();
        String attributeKindFilterValue = attributeKindNameField.getValue().trim();
        if(relationKindFilterValue.equals("") & attributeKindFilterValue.equals("") & relationDirectionSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请输入或选择关系类型名称 和/或 属性类型名称 和/或 关联关系方向", NotificationVariant.LUMO_WARNING);
        }else{
            this.directRelatedAttributeKindInfoGridListDataView.refreshAll();
        }
    }

    private void cancelFilterAttributeKindLinks(){
        relationKindNameField.setValue("");
        attributeKindNameField.setValue("");
        relationDirectionSelect.setValue(null);
        this.directRelatedAttributeKindInfoGridListDataView.refreshAll();
    }

    private void renderRelationKindDetailUI(AttributeKindAttachInfoVO attributeKindAttachInfoVO){
        RelationKindDetailUI relationKindDetailUI = new RelationKindDetailUI(attributeKindAttachInfoVO.getRelationKindName());
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
        NativeLabel relationKindName = new NativeLabel(attributeKindAttachInfoVO.getRelationKindName());
        titleDetailLayout.add(relationKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"关系类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindDetailUI);
        fullScreenWindow.show();
    }

    private void renderAttributeKindDetailUI(String attributeKindNameStr,String attributeKindUIDStr){
        AttributeKindDetailUI attributeKindDetailUI = new AttributeKindDetailUI(attributeKindUIDStr);
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

        Icon attributeKindIcon = VaadinIcon.INPUT.create();
        attributeKindIcon.setSize("10px");
        titleDetailLayout.add(attributeKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributeKindName = new NativeLabel(attributeKindNameStr+" ( ");
        titleDetailLayout.add(attributeKindName);

        Icon _UIDIcon = VaadinIcon.KEY_O.create();
        _UIDIcon.setSize("10px");
        titleDetailLayout.add(_UIDIcon);
        NativeLabel attributeKindUID = new NativeLabel(" "+attributeKindUIDStr+")");
        titleDetailLayout.add(attributeKindUID);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"属性类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(attributeKindDetailUI);
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
        NativeLabel attributeKindNameLabel = new NativeLabel(relationKindName);
        titleDetailLayout.add(attributeKindNameLabel);

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

    private void renderDetachAttributeKindUI(AttributeKindAttachInfoVO attributeKindAttachInfoVO){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除分类关联",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行删除分类关联 "+ this.classificationName+" - ["+attributeKindAttachInfoVO.relationKindName+"] -"+attributeKindAttachInfoVO.attributeKindName+" 的操作",actionButtonList,500,180);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDetachClassificationLink(attributeKindAttachInfoVO,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDetachClassificationLink(AttributeKindAttachInfoVO attributeKindAttachInfoVO, ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(attributeKindAttachInfoVO.getAttributeKindUID());
        RelationDirection attributeKindViewRelationDirection = RelationDirection.TWO_WAY;
        switch(attributeKindAttachInfoVO.relationDirection){
            case FROM -> attributeKindViewRelationDirection = RelationDirection.TO;
            case TO -> attributeKindViewRelationDirection = RelationDirection.FROM;
        }
        try {
            boolean detachResult = targetAttributeKind.detachClassification(this.classificationName,attributeKindAttachInfoVO.getRelationKindName(),attributeKindViewRelationDirection);
            if(detachResult){
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+attributeKindAttachInfoVO.relationKindName+"] -"+attributeKindAttachInfoVO.getAttributeKindName() +" 成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                ListDataProvider dataProvider=(ListDataProvider) directRelatedAttributeKindInfoGrid.getDataProvider();
                dataProvider.getItems().remove(attributeKindAttachInfoVO);
                dataProvider.refreshAll();
            }else{
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+attributeKindAttachInfoVO.relationKindName+"] -"+attributeKindAttachInfoVO.getAttributeKindName() +" 失败", NotificationVariant.LUMO_ERROR);
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

    private void queryRelatedAttributeKindAttachInfoVO_Kind(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        try {
            List<AttributeKind>  attributeKindList = targetClassification.getRelatedAttributeKinds(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            attributeKindMetaInfoGrid.setItems(attributeKindList);
            CommonUIOperationUtil.showPopupNotification("查询关联概念类型成功,查询返回 "+attributeKindList.size()+" 项关联属性类型", NotificationVariant.LUMO_SUCCESS);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderSelectedAttributeKindLinkInfo(AttributeKind selectedAttributeKind){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification currentClassification = coreRealm.getClassification(this.classificationName);
        RelationAttachInfo targetRelationAttachInfo = null;
        if(!currentAdvanceQueryIncludeOffspringClassifications){
            List<AttributeKindAttachInfo> attributeKindAttachInfoList = currentClassification.getAllDirectRelatedAttributeKindsInfo();
            for(AttributeKindAttachInfo currentAttributeKindAttachInfo:attributeKindAttachInfoList){
                RelationAttachInfo relationAttachInfo = currentAttributeKindAttachInfo.getRelationAttachInfo();
                AttributeKind attributeKind = currentAttributeKindAttachInfo.getAttachedAttributeKind();
                if(selectedAttributeKind.getAttributeKindUID().equals(attributeKind.getAttributeKindUID()) &&
                        currentAdvanceQueryRelationKindName.equals(relationAttachInfo.getRelationKind()) &&
                        currentAdvanceQueryRelationDirection.toString().equals(relationAttachInfo.getRelationDirection().toString())
                ){
                    targetRelationAttachInfo = relationAttachInfo;
                }
            }
        }else{
            AttributeKind targetAttributeKind = coreRealm.getAttributeKind(selectedAttributeKind.getAttributeKindUID());
            List<ClassificationAttachInfo> classificationAttachInfoList = targetAttributeKind.getAllAttachedClassificationsInfo();
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

    private class RelationDirectionIconValueProvider implements ValueProvider<AttributeKindAttachInfoVO,Icon> {
        @Override
        public Icon apply(AttributeKindAttachInfoVO attributeKindAttachInfoVO) {
            Icon relationDirectionIcon = null;
            switch (attributeKindAttachInfoVO.relationDirection){
                case FROM -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
                case TO -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
                switch (attributeKindAttachInfoVO.relationDirection){
                    case FROM -> relationDirectionIcon.setTooltipText("From Classification");
                    case TO -> relationDirectionIcon.setTooltipText("To Classification");
                }
            }
            return relationDirectionIcon;
        }
    }
}
