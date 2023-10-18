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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.ConceptionKindDetailUI;
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
    private TextField relationKindNameField;
    private ComboBox<String> relationDirectionSelect;
    private TextField conceptionKindNameField;
    private Grid<RelationKind> conceptionKindMetaInfoGrid;
    private Grid<RelationKindAttachInfoVO> directRelatedConceptionKindInfoGrid;
    private GridListDataView<RelationKindAttachInfoVO> directRelatedConceptionKindInfoGridListDataView;
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
        public RelationKindAttachInfoVO(RelationKindAttachInfo conceptionKindAttachInfo){
            this.relationKindName = conceptionKindAttachInfo.getAttachedRelationKind().getRelationKindName();
            this.attachedRelationKindName = conceptionKindAttachInfo.getRelationAttachInfo().getRelationKind();
            this.relationData = conceptionKindAttachInfo.getRelationAttachInfo().getRelationData();
            this.relationDirection = conceptionKindAttachInfo.getRelationAttachInfo().getRelationDirection();
            this.relationEntityUID = conceptionKindAttachInfo.getRelationAttachInfo().getRelationEntityUID();
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
        NativeLabel info1Label = new NativeLabel(" 全量直接关联关系类型");
        info1Span.add(info1Icon,info1Label);
        directRelatedConceptionKindsInfoTab.add(info1Span);

        Tab advancedConceptionKindsQueryTab = componentsSwitchTabSheet.add("",advancedConceptionKindsQueryContainer);
        Span info2Span =new Span();
        Icon info2Icon = new Icon(VaadinIcon.CONTROLLER);
        info2Icon.setSize("12px");
        NativeLabel info2Label = new NativeLabel(" 关联关系类型自定义条件查询");
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

        relationKindNameField = new TextField();
        relationKindNameField.setPlaceholder("关联关系类型名称");
        relationKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedConceptionKindFilterControlLayout.add(relationKindNameField);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, relationKindNameField);

        Icon plusIcon0 = new Icon(VaadinIcon.PLUS);
        plusIcon0.setSize("12px");
        directRelatedConceptionKindFilterControlLayout.add(plusIcon0);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon0);

        relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("关联关系方向");
        relationDirectionSelect.setWidth(120,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        directRelatedConceptionKindFilterControlLayout.add(relationDirectionSelect);

        Icon plusIcon1 = new Icon(VaadinIcon.PLUS);
        plusIcon1.setSize("12px");
        directRelatedConceptionKindFilterControlLayout.add(plusIcon1);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, plusIcon1);

        conceptionKindNameField = new TextField();
        conceptionKindNameField.setPlaceholder("相关关系类型名称");
        conceptionKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedConceptionKindFilterControlLayout.add(conceptionKindNameField);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, conceptionKindNameField);

        Button searchRelationKindsButton = new Button("查找关系类型关联",new Icon(VaadinIcon.SEARCH));
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedConceptionKindFilterControlLayout.add(searchRelationKindsButton);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationKindsButton);
        searchRelationKindsButton.setWidth(140,Unit.PIXELS);
        searchRelationKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterConceptionKindLinks();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        directRelatedConceptionKindFilterControlLayout.add(divIcon);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedConceptionKindFilterControlLayout.add(clearSearchCriteriaButton);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterConceptionKindLinks();
            }
        });

        ComponentRenderer _toolBarComponentRenderer0 = new ComponentRenderer<>(conceptionKindAttachInfoVO -> {
            Icon queryIcon = LineAwesomeIconsSvg.COG_SOLID.create();
            queryIcon.setSize("18px");
            Button configRelationKind = new Button(queryIcon, event -> {
                renderRelationKindDetailUI((RelationKindAttachInfoVO)conceptionKindAttachInfoVO,"ATTACHED");
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("关联关系类型定义配置");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                renderRelationKindDetailUI((RelationKindAttachInfoVO)conceptionKindAttachInfoVO,"SELF");
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configConceptionKind.setTooltipText("相关关系类型定义配置");

            Icon linkIcon = LineAwesomeIconsSvg.LINK_SOLID.create();
            linkIcon.setSize("17px");
            Button editLinkProperties = new Button(linkIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("关联链接信息");
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    RelationKindAttachInfoVO currentRelationKindAttachInfoVO = (RelationKindAttachInfoVO) conceptionKindAttachInfoVO;
                    renderRelationEntityDetailUI(currentRelationKindAttachInfoVO.getAttachedRelationKindName(),
                            currentRelationKindAttachInfoVO.getRelationEntityUID());
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
                    renderDetachConceptionKindUI((RelationKindAttachInfoVO)conceptionKindAttachInfoVO);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configRelationKind,configConceptionKind, editLinkProperties,removeClassificationLink);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        directRelatedConceptionKindInfoGrid = new Grid<>();
        directRelatedConceptionKindInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        directRelatedConceptionKindInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        directRelatedConceptionKindInfoGrid.addComponentColumn(new RelationDirectionIconValueProvider()).setHeader("").setKey("idx_0").setFlexGrow(0).setWidth("35px").setResizable(false);
        directRelatedConceptionKindInfoGrid.addColumn(RelationKindAttachInfoVO::getAttachedRelationKindName).setHeader("关联关系类型名称").setKey("idx_1");
        directRelatedConceptionKindInfoGrid.addColumn(RelationKindAttachInfoVO::getRelationKindName).setHeader("相关关系类型名称").setKey("idx_2");
        directRelatedConceptionKindInfoGrid.addColumn(RelationKindAttachInfoVO::getRelationData).setHeader("关联关系属性").setKey("idx_3");
        directRelatedConceptionKindInfoGrid.addColumn(_toolBarComponentRenderer0).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("150px").setResizable(false);
        directRelatedConceptionKindInfoGrid.appendFooterRow();

        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT,"关联关系类型名称");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.CONNECT_O,"相关关系类型名称");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.INPUT,"关联关系属性");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4);

        directRelatedConceptionKindMainContentContainerLayout.add(directRelatedConceptionKindInfoGrid);

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
                        currentAdvanceQueryRelationKindName = relationKindName;
                        currentAdvanceQueryRelationDirection = relationDirection;
                        currentAdvanceQueryIncludeOffspringClassifications = includeOffspringClassifications;
                        currentAdvanceQueryOffspringLevel = offspringLevel;
                        clearClassificationLinkRelationInfo();
                        queryRelatedConceptionKind(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
                    }
                };
        classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        advancedConceptionKindsQueryLeftSideContainerLayout.add(classificationRelatedDataQueryCriteriaView);

        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(conceptionKind -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                if(conceptionKind instanceof ConceptionKind){
                    renderConceptionKindDetailUI(((ConceptionKind)conceptionKind).getConceptionKindName());
                }
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configConceptionKind.setTooltipText("配置概念类型定义");

            HorizontalLayout buttons = new HorizontalLayout(configConceptionKind);
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
        conceptionKindMetaInfoGrid.addColumn(RelationKind::getRelationKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindMetaInfoGrid.addColumn(RelationKind::getRelationKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        conceptionKindMetaInfoGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_2_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_2_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_2_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_2_idx2 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_2_idx2);
        conceptionKindMetaInfoGrid.appendFooterRow();
        conceptionKindMetaInfoGrid.addSelectionListener(new SelectionListener<Grid<RelationKind>, RelationKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<RelationKind>, RelationKind> selectionEvent) {
                Set<RelationKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    //unselected item, reset link detail info
                    clearClassificationLinkRelationInfo();
                }else{
                    RelationKind selectedConceptionKind = selectedItemSet.iterator().next();
                    renderSelectedConceptionKindLinkInfo(selectedConceptionKind);
                }
            }
        });

        advancedConceptionKindsQueryLeftSideContainerLayout.add(conceptionKindMetaInfoGrid);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(LineAwesomeIconsSvg.LINK_SOLID.create(),"关联链接信息");
        advancedConceptionKindsQueryRightSideContainerLayout.add(filterTitle2);

        HorizontalLayout classificationLinkNameInfoContainer = new HorizontalLayout();
        advancedConceptionKindsQueryRightSideContainerLayout.add(classificationLinkNameInfoContainer);
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
        advancedConceptionKindsQueryRightSideContainerLayout.add(classificationLinkEntityUIDInfoContainer);
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
                //renderRelationEntityDetailUI(currentSelectedClassificationLinkEntityKind,currentSelectedClassificationLinkEntityUID);
            }
        });
        classificationLinkEntityUIDInfoContainer.add(showClassificationLinkDetailButton);
        classificationLinkEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,showClassificationLinkDetailButton);
        classificationLinkEntityUIDInfoContainer.getStyle().set("padding-bottom", "3px");
        showClassificationLinkDetailButton.setEnabled(false);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"关系实体属性");
        infoTitle1.getStyle().set("padding-top","15px");
        advancedConceptionKindsQueryRightSideContainerLayout.add(infoTitle1);
        HorizontalLayout horizontalDiv02 = new HorizontalLayout();
        horizontalDiv02.setWidthFull();
        horizontalDiv02.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        advancedConceptionKindsQueryRightSideContainerLayout.add(horizontalDiv02);

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
        advancedConceptionKindsQueryRightSideContainerLayout.add(classificationLinkEntityAttributesInfoGrid);
    }

    public void setHeight(int viewHeight){
        this.conceptionKindMetaInfoGrid.setHeight(viewHeight-125, Unit.PIXELS);
        this.directRelatedConceptionKindInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.relationKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    public void loadDirectRelatedRelationKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        List<RelationKindAttachInfo> directRelatedConceptionKindList = targetClassification.getAllDirectRelatedRelationKindsInfo();
        List<RelationKindAttachInfoVO> conceptionKindAttachInfoVOList = new ArrayList<>();
        for(RelationKindAttachInfo currentConceptionKindAttachInfo:directRelatedConceptionKindList){
            conceptionKindAttachInfoVOList.add(new RelationKindAttachInfoVO(currentConceptionKindAttachInfo));
        }
        this.directRelatedConceptionKindInfoGridListDataView = this.directRelatedConceptionKindInfoGrid.setItems(conceptionKindAttachInfoVOList);
        this.directRelatedConceptionKindInfoGridListDataView.addFilter(item->{
            String conceptionKindName = item.getRelationKindName();
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

            boolean conceptionKindNameFilterResult = true;
            if(!conceptionKindNameField.getValue().trim().equals("")){
                if(conceptionKindName.contains(conceptionKindNameField.getValue().trim())){
                    conceptionKindNameFilterResult = true;
                }else{
                    conceptionKindNameFilterResult = false;
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
            return relationKindNameFilterResult & conceptionKindNameFilterResult & relationDirectionFilterResult;
        });
    }

    private void queryRelatedConceptionKind(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        try {
            List<RelationKind>  conceptionKindList = targetClassification.getRelatedRelationKinds(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
            conceptionKindMetaInfoGrid.setItems(conceptionKindList);
            CommonUIOperationUtil.showPopupNotification("查询关联关系类型成功,查询返回 "+conceptionKindList.size()+" 项关联关系类型", NotificationVariant.LUMO_SUCCESS);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderConceptionKindDetailUI(String  conceptionKindName){
        ConceptionKindDetailUI conceptionKindDetailUI = new ConceptionKindDetailUI(conceptionKindName);
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
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionKindName);
        titleDetailLayout.add(conceptionKindNameLabel);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"概念类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionKindDetailUI);
        fullScreenWindow.show();
    }

    private void renderSelectedConceptionKindLinkInfo(RelationKind selectedConceptionKind){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification currentClassification = coreRealm.getClassification(this.classificationName);
        RelationAttachInfo targetRelationAttachInfo = null;
        if(!currentAdvanceQueryIncludeOffspringClassifications){
            List<ConceptionKindAttachInfo> conceptionKindAttachInfoList = currentClassification.getAllDirectRelatedConceptionKindsInfo();
            for(ConceptionKindAttachInfo currentConceptionKindAttachInfo:conceptionKindAttachInfoList){
                RelationAttachInfo relationAttachInfo = currentConceptionKindAttachInfo.getRelationAttachInfo();
                ConceptionKind conceptionKind = currentConceptionKindAttachInfo.getAttachedConceptionKind();
                if(selectedConceptionKind.getRelationKindName().equals(conceptionKind.getConceptionKindName()) &&
                        currentAdvanceQueryRelationKindName.equals(relationAttachInfo.getRelationKind()) &&
                        currentAdvanceQueryRelationDirection.toString().equals(relationAttachInfo.getRelationDirection().toString())
                ){
                    targetRelationAttachInfo = relationAttachInfo;
                }
            }
        }else{
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(selectedConceptionKind.getRelationKindName());
            List<ClassificationAttachInfo> classificationAttachInfoList = targetConceptionKind.getAllAttachedClassificationsInfo();
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

    private void filterConceptionKindLinks(){
        String relationKindFilterValue = relationKindNameField.getValue().trim();
        String conceptionKindFilterValue = conceptionKindNameField.getValue().trim();
        if(relationKindFilterValue.equals("") & conceptionKindFilterValue.equals("") & relationDirectionSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请输入或选择关系类型名称 和/或 关联关系方向", NotificationVariant.LUMO_WARNING);
        }else{
            this.directRelatedConceptionKindInfoGridListDataView.refreshAll();
        }
    }

    private void cancelFilterConceptionKindLinks(){
        relationKindNameField.setValue("");
        conceptionKindNameField.setValue("");
        relationDirectionSelect.setValue(null);
        this.directRelatedConceptionKindInfoGridListDataView.refreshAll();
    }

    private void renderRelationKindDetailUI(RelationKindAttachInfoVO relationKindAttachInfoVO,String kindType){
        String targetRelationKindName = "";
        if("ATTACHED".equals(kindType)){
            targetRelationKindName = relationKindAttachInfoVO.attachedRelationKindName;
        }
        if("SELF".equals(kindType)){
            targetRelationKindName = relationKindAttachInfoVO.getRelationKindName();
        }
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
        NativeLabel conceptionKindNameLabel = new NativeLabel(relationKindName);
        titleDetailLayout.add(conceptionKindNameLabel);

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

    private void renderDetachConceptionKindUI(RelationKindAttachInfoVO relationKindAttachInfoVO){
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
        RelationDirection conceptionKindViewRelationDirection = RelationDirection.TWO_WAY;
        switch(relationKindAttachInfoVO.relationDirection){
            case FROM -> conceptionKindViewRelationDirection = RelationDirection.TO;
            case TO -> conceptionKindViewRelationDirection = RelationDirection.FROM;
        }
        try {
            boolean detachResult =targetRelationKind.detachClassification(this.classificationName,relationKindAttachInfoVO.getAttachedRelationKindName(),conceptionKindViewRelationDirection);
            if(detachResult){
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+relationKindAttachInfoVO.getAttachedRelationKindName()+"] -"+relationKindAttachInfoVO.relationKindName +" 成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                ListDataProvider dataProvider=(ListDataProvider)directRelatedConceptionKindInfoGrid.getDataProvider();
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
