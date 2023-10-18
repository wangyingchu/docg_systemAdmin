package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
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
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.text.NumberFormat;
import java.util.Map;

public class RelatedAttributeKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem attributeKindCountDisplayItem;
    private TextField relationKindNameField;
    private ComboBox<String> relationDirectionSelect;
    private TextField conceptionKindNameField;
    private Grid<AttributeKindAttachInfoVO> directRelatedConceptionKindInfoGrid;
    private class AttributeKindAttachInfoVO {
        private  String attributeKindName;
        private String relationKindName;
        private RelationDirection relationDirection;
        private Map<String, Object> relationData;
        private String relationEntityUID;
        public AttributeKindAttachInfoVO(AttributeKindAttachInfo attributeKindAttachInfo){
            this.attributeKindName = attributeKindAttachInfo.getAttachedAttributeKind().getAttributeKindName();
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
        NativeLabel info1Label = new NativeLabel(" 全量直接关联属性类型");
        info1Span.add(info1Icon,info1Label);
        directRelatedConceptionKindsInfoTab.add(info1Span);

        Tab advancedConceptionKindsQueryTab = componentsSwitchTabSheet.add("",advancedConceptionKindsQueryContainer);
        Span info2Span =new Span();
        Icon info2Icon = new Icon(VaadinIcon.CONTROLLER);
        info2Icon.setSize("12px");
        NativeLabel info2Label = new NativeLabel(" 关联属性类型自定义条件查询");
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
        conceptionKindNameField.setPlaceholder("相关属性类型名称");
        conceptionKindNameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindNameField.setWidth(250,Unit.PIXELS);
        directRelatedConceptionKindFilterControlLayout.add(conceptionKindNameField);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER, conceptionKindNameField);

        Button searchRelationKindsButton = new Button("查找属性类型关联",new Icon(VaadinIcon.SEARCH));
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchRelationKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        directRelatedConceptionKindFilterControlLayout.add(searchRelationKindsButton);
        directRelatedConceptionKindFilterControlLayout.setVerticalComponentAlignment(Alignment.CENTER,searchRelationKindsButton);
        searchRelationKindsButton.setWidth(140,Unit.PIXELS);
        searchRelationKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterConceptionKindLinks();
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
                //cancelFilterConceptionKindLinks();
            }
        });

        ComponentRenderer _toolBarComponentRenderer0 = new ComponentRenderer<>(conceptionKindAttachInfoVO -> {
            Icon queryIcon = LineAwesomeIconsSvg.COG_SOLID.create();
            queryIcon.setSize("18px");
            Button configRelationKind = new Button(queryIcon, event -> {
                //renderRelationKindDetailUI((RelatedConceptionKindsView.ConceptionKindAttachInfoVO)conceptionKindAttachInfoVO);
            });
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configRelationKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configRelationKind.setTooltipText("关联关系类型定义配置");

            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("21px");
            Button configConceptionKind = new Button(configIcon, event -> {
                //renderConceptionKindDetailUI(((RelatedConceptionKindsView.ConceptionKindAttachInfoVO)conceptionKindAttachInfoVO).conceptionKindName);
            });
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configConceptionKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configConceptionKind.setTooltipText("相关概念类型定义配置");

            Icon linkIcon = LineAwesomeIconsSvg.LINK_SOLID.create();
            linkIcon.setSize("17px");
            Button editLinkProperties = new Button(linkIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("关联链接信息");
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    //renderRelationEntityDetailUI(((RelatedConceptionKindsView.ConceptionKindAttachInfoVO) conceptionKindAttachInfoVO).relationKindName,
                    //        ((RelatedConceptionKindsView.ConceptionKindAttachInfoVO) conceptionKindAttachInfoVO).getRelationEntityUID());
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
                    //renderDetachConceptionKindUI((RelatedConceptionKindsView.ConceptionKindAttachInfoVO)conceptionKindAttachInfoVO);
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
        directRelatedConceptionKindInfoGrid.addColumn(AttributeKindAttachInfoVO::getRelationKindName).setHeader("关联关系类型名称").setKey("idx_1");
        directRelatedConceptionKindInfoGrid.addColumn(AttributeKindAttachInfoVO::getAttributeKindName).setHeader("相关属性类型名称").setKey("idx_2");
        directRelatedConceptionKindInfoGrid.addColumn(AttributeKindAttachInfoVO::getRelationData).setHeader("关联关系属性").setKey("idx_3");
        directRelatedConceptionKindInfoGrid.addColumn(_toolBarComponentRenderer0).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("150px").setResizable(false);
        directRelatedConceptionKindInfoGrid.appendFooterRow();

        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT,"关联关系类型名称");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.INPUT,"相关属性类型名称");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"关联关系属性");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        directRelatedConceptionKindInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4);

        directRelatedConceptionKindMainContentContainerLayout.add(directRelatedConceptionKindInfoGrid);


    }

    public void setHeight(int viewHeight){
        //this.conceptionKindMetaInfoGrid.setHeight(viewHeight-125, Unit.PIXELS);
        //this.directRelatedConceptionKindInfoGrid.setHeight(viewHeight-125,Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.attributeKindCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    public void loadDirectRelatedAttributeKindsInfo(){

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
