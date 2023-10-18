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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ClassificationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind.RelationKindDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelatedRelationKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem relationKindCountDisplayItem;
    private TextField attachedRelationKindNameField;
    private ComboBox<String> relationDirectionSelect;
    private TextField relationKindNameField;
    private Grid<RelationKind> relationKindMetaInfoGrid;
    private Grid<RelationKindAttachInfoVO> directRelatedRelationKindInfoGrid;
    private GridListDataView<RelationKindAttachInfoVO> directRelatedRelationKindInfoGridListDataView;
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

    private class RelationKindAttachInfoVO {
        private  String relationKindName;
        private String attachedRelationKindName;
        private RelationDirection relationDirection;
        private Map<String, Object> relationData;
        private String relationEntityUID;
        public RelationKindAttachInfoVO(RelationKindAttachInfo relationKindAttachInfo){
            this.relationKindName = relationKindAttachInfo.getAttachedRelationKind().getRelationKindName();
            this.attachedRelationKindName = relationKindAttachInfo.getRelationAttachInfo().getRelationKind();
            this.relationData = relationKindAttachInfo.getRelationAttachInfo().getRelationData();
            this.relationDirection = relationKindAttachInfo.getRelationAttachInfo().getRelationDirection();
            this.relationEntityUID = relationKindAttachInfo.getRelationAttachInfo().getRelationEntityUID();
        }

        public String getRelationKindName() {
            return relationKindName;
        }

        public String getAttachedRelationKindName() {
            return attachedRelationKindName;
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
    }

    public RelatedRelationKindsView(String classificationName){
        this.setPadding(false);
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关关系类型运行时信息");
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
        this.relationKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关关系类型数量:","-");

        TabSheet componentsSwitchTabSheet = new TabSheet();
        componentsSwitchTabSheet.setWidthFull();

        VerticalLayout directRelatedRelationKindsContainer = new VerticalLayout();
        directRelatedRelationKindsContainer.setPadding(false);
        directRelatedRelationKindsContainer.setSpacing(false);
        directRelatedRelationKindsContainer.setMargin(false);

        VerticalLayout advancedRelationKindsQueryContainer = new VerticalLayout();
        advancedRelationKindsQueryContainer.setPadding(false);
        advancedRelationKindsQueryContainer.setSpacing(false);
        advancedRelationKindsQueryContainer.setMargin(false);

        Tab directRelatedRelationKindsInfoTab = componentsSwitchTabSheet.add("",directRelatedRelationKindsContainer);
        Span info1Span =new Span();
        Icon info1Icon = new Icon(VaadinIcon.ALIGN_JUSTIFY);
        info1Icon.setSize("12px");
        NativeLabel info1Label = new NativeLabel(" 全量直接关联关系类型");
        info1Span.add(info1Icon,info1Label);
        directRelatedRelationKindsInfoTab.add(info1Span);

        Tab advancedRelationKindsQueryTab = componentsSwitchTabSheet.add("",advancedRelationKindsQueryContainer);
        Span info2Span =new Span();
        Icon info2Icon = new Icon(VaadinIcon.CONTROLLER);
        info2Icon.setSize("12px");
        NativeLabel info2Label = new NativeLabel(" 关联关系类型自定义条件查询");
        info2Span.add(info2Icon,info2Label);
        advancedRelationKindsQueryTab.add(info2Span);

        componentsSwitchTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {}
        });

        add(componentsSwitchTabSheet);

        VerticalLayout directRelatedRelationKindMainContentContainerLayout = new VerticalLayout();
        directRelatedRelationKindMainContentContainerLayout.setWidthFull();
        directRelatedRelationKindMainContentContainerLayout.setPadding(false);
        directRelatedRelationKindMainContentContainerLayout.setMargin(false);
        directRelatedRelationKindsContainer.add(directRelatedRelationKindMainContentContainerLayout);

        HorizontalLayout directRelatedRelationKindFilterControlLayout = new HorizontalLayout();
        directRelatedRelationKindFilterControlLayout.setSpacing(false);
        directRelatedRelationKindFilterControlLayout.setMargin(false);
        directRelatedRelationKindMainContentContainerLayout.add(directRelatedRelationKindFilterControlLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        directRelatedRelationKindFilterControlLayout.add(filterTitle);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        attachedRelationKindNameField = new TextField();
        attachedRelationKindNameField.setPlaceholder("关联关系类型名称");
        attachedRelationKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attachedRelationKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedRelationKindFilterControlLayout.add(attachedRelationKindNameField);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, attachedRelationKindNameField);

        Icon plusIcon0 = new Icon(VaadinIcon.PLUS);
        plusIcon0.setSize("12px");
        directRelatedRelationKindFilterControlLayout.add(plusIcon0);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon0);

        relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("关联关系方向");
        relationDirectionSelect.setWidth(120,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        directRelatedRelationKindFilterControlLayout.add(relationDirectionSelect);

        Icon plusIcon1 = new Icon(VaadinIcon.PLUS);
        plusIcon1.setSize("12px");
        directRelatedRelationKindFilterControlLayout.add(plusIcon1);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon1);

        relationKindNameField = new TextField();
        relationKindNameField.setPlaceholder("相关关系类型名称");
        relationKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedRelationKindFilterControlLayout.add(relationKindNameField);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindNameField);

        Button searchRelationKindsButton = new Button("查找关系类型关联",new Icon(VaadinIcon.SEARCH));
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedRelationKindFilterControlLayout.add(searchRelationKindsButton);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationKindsButton);
        searchRelationKindsButton.setWidth(140,Unit.PIXELS);
        searchRelationKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterRelationKindLinks();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        directRelatedRelationKindFilterControlLayout.add(divIcon);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedRelationKindFilterControlLayout.add(clearSearchCriteriaButton);
        directRelatedRelationKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterRelationKindLinks();
            }
        });

        ComponentRenderer _toolBarComponentRenderer0 = new ComponentRenderer<>(currentRelationKindAttachInfoVO -> {
            Icon queryIcon = LineAwesomeIconsSvg.COG_SOLID.create();
            queryIcon.setSize("18px");
            Button configAttachedRelationKind = new Button(queryIcon, event -> {
                RelationKindAttachInfoVO relationKindAttachInfoVO = (RelationKindAttachInfoVO)currentRelationKindAttachInfoVO;
                renderRelationKindDetailUI(relationKindAttachInfoVO.getAttachedRelationKindName());
            });
            configAttachedRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttachedRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configAttachedRelationKind.setTooltipText("关联关系类型定义配置");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configRelationKind = new Button(configIcon, event -> {
                RelationKindAttachInfoVO relationKindAttachInfoVO = (RelationKindAttachInfoVO)currentRelationKindAttachInfoVO;
                renderRelationKindDetailUI(relationKindAttachInfoVO.getRelationKindName());
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("相关关系类型定义配置");

            Icon linkIcon = LineAwesomeIconsSvg.LINK_SOLID.create();
            linkIcon.setSize("17px");
            Button editLinkProperties = new Button(linkIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("关联链接信息");
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    RelationKindAttachInfoVO currentRelationKindAttachInfoVOAlis = (RelationKindAttachInfoVO) currentRelationKindAttachInfoVO;
                    renderRelationEntityDetailUI(currentRelationKindAttachInfoVOAlis.getAttachedRelationKindName(),
                            currentRelationKindAttachInfoVOAlis.getRelationEntityUID());
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
                    renderDetachRelationKindUI((RelationKindAttachInfoVO)currentRelationKindAttachInfoVO);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configAttachedRelationKind,configRelationKind, editLinkProperties,removeClassificationLink);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        directRelatedRelationKindInfoGrid = new Grid<>();
        directRelatedRelationKindInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        directRelatedRelationKindInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        directRelatedRelationKindInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        directRelatedRelationKindInfoGrid.addColumn(RelationKindAttachInfoVO::getAttachedRelationKindName).setHeader("关联关系类型名称").setKey("idx_1");
        directRelatedRelationKindInfoGrid.addColumn(RelationKindAttachInfoVO::getRelationKindName).setHeader("相关关系类型名称").setKey("idx_2");
        directRelatedRelationKindInfoGrid.addColumn(RelationKindAttachInfoVO::getRelationData).setHeader("关联关系属性").setKey("idx_3");
        directRelatedRelationKindInfoGrid.addColumn(_toolBarComponentRenderer0).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("150px").setResizable(false);
        directRelatedRelationKindInfoGrid.appendFooterRow();

        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT,"关联关系类型名称");
        directRelatedRelationKindInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.CONNECT_O,"相关关系类型名称");
        directRelatedRelationKindInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.INPUT,"关联关系属性");
        directRelatedRelationKindInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        directRelatedRelationKindInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4);

        directRelatedRelationKindMainContentContainerLayout.add(directRelatedRelationKindInfoGrid);

        HorizontalLayout advancedRelationKindsQueryMainContentContainerLayout = new HorizontalLayout();
        advancedRelationKindsQueryMainContentContainerLayout.setWidthFull();
        advancedRelationKindsQueryContainer.add(advancedRelationKindsQueryMainContentContainerLayout);

        VerticalLayout advancedRelationKindsQueryLeftSideContainerLayout = new VerticalLayout();
        advancedRelationKindsQueryLeftSideContainerLayout.setWidth(700,Unit.PIXELS);
        advancedRelationKindsQueryLeftSideContainerLayout.setMargin(false);
        advancedRelationKindsQueryLeftSideContainerLayout.setPadding(false);
        VerticalLayout advancedRelationKindsQueryRightSideContainerLayout = new VerticalLayout();
        advancedRelationKindsQueryRightSideContainerLayout.setMargin(true);
        advancedRelationKindsQueryRightSideContainerLayout.setPadding(false);

        advancedRelationKindsQueryMainContentContainerLayout.add(advancedRelationKindsQueryLeftSideContainerLayout);
        advancedRelationKindsQueryMainContentContainerLayout.add(advancedRelationKindsQueryRightSideContainerLayout);

        ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper =
                new ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper() {
                    @Override
                    public void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel) {
                        currentAdvanceQueryRelationKindName = relationKindName;
                        currentAdvanceQueryRelationDirection = relationDirection;
                        currentAdvanceQueryIncludeOffspringClassifications = includeOffspringClassifications;
                        currentAdvanceQueryOffspringLevel = offspringLevel;
                        clearClassificationLinkRelationInfo();
                        queryRelatedRelationKind(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
                    }
                };
        classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        advancedRelationKindsQueryLeftSideContainerLayout.add(classificationRelatedDataQueryCriteriaView);

        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(relationKind -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configRelationKind = new Button(configIcon, event -> {
                if(relationKind instanceof RelationKind){
                    RelationKind currentRelationKind = (RelationKind)relationKind;
                    renderRelationKindDetailUI(currentRelationKind.getRelationKindName());
                }
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("关联关系类型定义配置");

            HorizontalLayout buttons = new HorizontalLayout(configRelationKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        relationKindMetaInfoGrid = new Grid<>();
        relationKindMetaInfoGrid.setWidth(700,Unit.PIXELS);
        relationKindMetaInfoGrid.setHeight(600,Unit.PIXELS);
        relationKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        relationKindMetaInfoGrid.addColumn(RelationKind::getRelationKindName).setHeader("关系类型名称").setKey("idx_0");
        relationKindMetaInfoGrid.addColumn(RelationKind::getRelationKindDesc).setHeader("关系类型显示名称").setKey("idx_1");
        relationKindMetaInfoGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_2_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"关系类型名称");
        relationKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_2_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"关系类型显示名称");
        relationKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_2_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx2 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_2_idx2);
        relationKindMetaInfoGrid.appendFooterRow();
        relationKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<RelationKind>, RelationKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<RelationKind>, RelationKind> selectionEvent) {
                Set<RelationKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    //unselected item, reset link detail info
                    clearClassificationLinkRelationInfo();
                }else{
                    RelationKind selectedRelationKind = selectedItemSet.iterator().next();
                    renderSelectedRelationKindLinkInfo(selectedRelationKind);
                }
            }
        });

        advancedRelationKindsQueryLeftSideContainerLayout.add(relationKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(LineAwesomeIconsSvg.LINK_SOLID.create(),"关联链接信息");
        advancedRelationKindsQueryRightSideContainerLayout.add(filterTitle2);

        HorizontalLayout classificationLinkNameInfoContainer = new HorizontalLayout();
        advancedRelationKindsQueryRightSideContainerLayout.add(classificationLinkNameInfoContainer);
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
        advancedRelationKindsQueryRightSideContainerLayout.add(classificationLinkEntityUIDInfoContainer);
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
        advancedRelationKindsQueryRightSideContainerLayout.add(infoTitle1);
        HorizontalLayout horizontalDiv02 = new HorizontalLayout();
        horizontalDiv02.setWidthFull();
        horizontalDiv02.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        advancedRelationKindsQueryRightSideContainerLayout.add(horizontalDiv02);

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
        advancedRelationKindsQueryRightSideContainerLayout.add(classificationLinkEntityAttributesInfoGrid);
    }

    public void setHeight(int viewHeight){
        this.relationKindMetaInfoGrid.setHeight(viewHeight-125, Unit.PIXELS);
        this.directRelatedRelationKindInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.relationKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    public void loadDirectRelatedRelationKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        List<RelationKindAttachInfo> directRelatedRelationKindList = targetClassification.getAllDirectRelatedRelationKindsInfo();
        List<RelationKindAttachInfoVO> relationKindAttachInfoVOList = new ArrayList<>();
        for(RelationKindAttachInfo currentRelationKindAttachInfo:directRelatedRelationKindList){
            relationKindAttachInfoVOList.add(new RelationKindAttachInfoVO(currentRelationKindAttachInfo));
        }
        this.directRelatedRelationKindInfoGridListDataView = this.directRelatedRelationKindInfoGrid.setItems(relationKindAttachInfoVOList);
        this.directRelatedRelationKindInfoGridListDataView.addFilter(item->{
            String relationKindName = item.getRelationKindName();
            String attachedRelationKindName = item.getAttachedRelationKindName();
            RelationDirection relationDirection = item.getRelationDirection();

            boolean attachedRelationKindNameFilterResult = true;
            if(!attachedRelationKindNameField.getValue().trim().equals("")){
                if(attachedRelationKindName.contains(attachedRelationKindNameField.getValue().trim())){
                    attachedRelationKindNameFilterResult = true;
                }else{
                    attachedRelationKindNameFilterResult = false;
                }
            }

            boolean relationKindNameFilterResult = true;
            if(!relationKindNameField.getValue().trim().equals("")){
                if(relationKindName.contains(relationKindNameField.getValue().trim())){
                    relationKindNameFilterResult = true;
                }else{
                    relationKindNameFilterResult = false;
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
            return attachedRelationKindNameFilterResult & relationKindNameFilterResult & relationDirectionFilterResult;
        });
    }

    private void queryRelatedRelationKind(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        try {
            List<RelationKind>  relationKindList = targetClassification.getRelatedRelationKinds(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            relationKindMetaInfoGrid.setItems(relationKindList);
            CommonUIOperationUtil.showPopupNotification("查询关联关系类型成功,查询返回 "+relationKindList.size()+" 项关联关系类型", NotificationVariant.LUMO_SUCCESS);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderSelectedRelationKindLinkInfo(RelationKind selectedRelationKind){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification currentClassification = coreRealm.getClassification(this.classificationName);
        RelationAttachInfo targetRelationAttachInfo = null;
        if(!currentAdvanceQueryIncludeOffspringClassifications){
            List<RelationKindAttachInfo> relationKindAttachInfoList = currentClassification.getAllDirectRelatedRelationKindsInfo();
            for(RelationKindAttachInfo currentRelationKindAttachInfo:relationKindAttachInfoList){
                RelationAttachInfo relationAttachInfo = currentRelationKindAttachInfo.getRelationAttachInfo();
                RelationKind relationKind = currentRelationKindAttachInfo.getAttachedRelationKind();
                if(selectedRelationKind.getRelationKindName().equals(relationKind.getRelationKindName()) &&
                        currentAdvanceQueryRelationKindName.equals(relationAttachInfo.getRelationKind()) &&
                        currentAdvanceQueryRelationDirection.toString().equals(relationAttachInfo.getRelationDirection().toString())
                ){
                    targetRelationAttachInfo = relationAttachInfo;
                }
            }
        }else{
            RelationKind targetRelationKind = coreRealm.getRelationKind(selectedRelationKind.getRelationKindName());
            List<ClassificationAttachInfo> classificationAttachInfoList = targetRelationKind.getAllAttachedClassificationsInfo();
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

    private void clearClassificationLinkRelationInfo(){
        classificationLinkRelationKindLabel.setText("-");
        classificationLinkEntityUIDLabel.setText("-");
        showClassificationLinkDetailButton.setEnabled(false);
        List<AttributeValueVO> attributeValueVOList = new ArrayList<>();
        classificationLinkEntityAttributesInfoGrid.setItems(attributeValueVOList);
        currentSelectedClassificationLinkEntityKind = null;
        currentSelectedClassificationLinkEntityUID = null;
    }

    private void filterRelationKindLinks(){
        String attachedRelationKindFilterValue = attachedRelationKindNameField.getValue().trim();
        String relationKindFilterValue = relationKindNameField.getValue().trim();
        if(attachedRelationKindFilterValue.equals("") & relationKindFilterValue.equals("") & relationDirectionSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请输入或选择关系类型名称 和/或 关联关系方向", NotificationVariant.LUMO_WARNING);
        }else{
            this.directRelatedRelationKindInfoGridListDataView.refreshAll();
        }
    }

    private void cancelFilterRelationKindLinks(){
        attachedRelationKindNameField.setValue("");
        relationKindNameField.setValue("");
        relationDirectionSelect.setValue(null);
        this.directRelatedRelationKindInfoGridListDataView.refreshAll();
    }

    private void renderRelationKindDetailUI(String targetRelationKindName){
        RelationKindDetailUI relationKindDetailUI = new RelationKindDetailUI(targetRelationKindName);
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
        NativeLabel relationKindName = new NativeLabel(targetRelationKindName);
        titleDetailLayout.add(relationKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"关系类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindDetailUI);
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
        NativeLabel relationKindNameLabel = new NativeLabel(relationKindName);
        titleDetailLayout.add(relationKindNameLabel);

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

    private void renderDetachRelationKindUI(RelationKindAttachInfoVO relationKindAttachInfoVO){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除分类关联",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行删除分类关联 "+ this.classificationName+" - ["+relationKindAttachInfoVO.getAttachedRelationKindName()+"] -"+relationKindAttachInfoVO.relationKindName+" 的操作",actionButtonList,500,200);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDetachClassificationLink(relationKindAttachInfoVO,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDetachClassificationLink(RelationKindAttachInfoVO relationKindAttachInfoVO, ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        RelationKind targetRelationKind = coreRealm.getRelationKind(relationKindAttachInfoVO.relationKindName);
        RelationDirection relationKindViewRelationDirection = RelationDirection.TWO_WAY;
        switch(relationKindAttachInfoVO.relationDirection){
            case FROM -> relationKindViewRelationDirection = RelationDirection.TO;
            case TO -> relationKindViewRelationDirection = RelationDirection.FROM;
        }
        try {
            boolean detachResult =targetRelationKind.detachClassification(this.classificationName,relationKindAttachInfoVO.getAttachedRelationKindName(),relationKindViewRelationDirection);
            if(detachResult){
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+relationKindAttachInfoVO.getAttachedRelationKindName()+"] -"+relationKindAttachInfoVO.relationKindName +" 成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                ListDataProvider dataProvider=(ListDataProvider) directRelatedRelationKindInfoGrid.getDataProvider();
                dataProvider.getItems().remove(relationKindAttachInfoVO);
                dataProvider.refreshAll();
            }else{
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+relationKindAttachInfoVO.getAttachedRelationKindName()+"] -"+relationKindAttachInfoVO.relationKindName +" 失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private class RelationDirectionIconValueProvider implements ValueProvider<RelationKindAttachInfoVO,Icon> {
        @Override
        public Icon apply(RelationKindAttachInfoVO relationKindAttachInfoVO) {
            Icon relationDirectionIcon = null;
            switch (relationKindAttachInfoVO.relationDirection){
                case FROM -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
                case TO -> relationDirectionIcon = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
            }
            if(relationDirectionIcon != null){
                relationDirectionIcon.setSize("14px");
                switch (relationKindAttachInfoVO.relationDirection){
                    case FROM -> relationDirectionIcon.setTooltipText("From Classification");
                    case TO -> relationDirectionIcon.setTooltipText("To Classification");
                }
            }
            return relationDirectionIcon;
        }
    }
}
