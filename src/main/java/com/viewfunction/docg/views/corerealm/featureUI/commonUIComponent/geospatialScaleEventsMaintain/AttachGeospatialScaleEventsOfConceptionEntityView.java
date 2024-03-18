package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialScaleEventsMaintain;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.*;

public class AttachGeospatialScaleEventsOfConceptionEntityView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private NativeLabel resultNumberValue;
    private Map<String, AttributeEditorItemWidget> eventAttributeEditorsMap;
    private TextField eventCommentField;
    private TextField geospatialAttributeSearchField;
    private VerticalLayout eventEntityAttributesContainer;
    private Button clearAttributeButton;
    private ComboBox<GeospatialRegion.GeospatialScaleGrade> geospatialScaleGradeSelect;
    private ComboBox<GeospatialRegion.GeospatialScaleGrade> geospatialScaleGradeSelect1;
    private ComboBox<GeospatialRegion.GeospatialProperty> eventPropertyGeospatialPropertySelect;
    private ComboBox<GeospatialScaleCalculable.SpatialScaleLevel> spatialScaleLevelSelect;
    private ComboBox<GeospatialScaleCalculable.SpatialPredicateType> spatialPredicateTypeSelect;
    private ComboBox<String> geospatialRegionNameSelect;
    private ComboBox<String> geospatialRegionNameSelect1;
    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
    private NumberFormat numberFormat;
    private Dialog containerDialog;
    private String geospatialScaleEntitiesSearchType = "SearchByPropertyMatch";
    private Grid<GeospatialScaleEntity> geospatialScaleEntitiesGrid;
    private AttachGeospatialScaleEventsOfConceptionEntityCallback attachGeospatialScaleEventsOfConceptionEntityCallback;

    public void setAttachGeospatialScaleEventsOfConceptionEntityCallback(AttachGeospatialScaleEventsOfConceptionEntityCallback attachGeospatialScaleEventsOfConceptionEntityCallback) {
        this.attachGeospatialScaleEventsOfConceptionEntityCallback = attachGeospatialScaleEventsOfConceptionEntityCallback;
    }

    public interface AttachGeospatialScaleEventsOfConceptionEntityCallback {
        public void onSuccess(List<GeospatialScaleEvent> resultEventList);
    }

    public AttachGeospatialScaleEventsOfConceptionEntityView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        this.numberFormat = NumberFormat.getInstance();

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout titleDivLayout = new HorizontalLayout();
        titleDivLayout.setWidthFull();
        titleDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        add(titleDivLayout);

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setWidthFull();
        add(mainLayout);

        VerticalScrollLayout leftSideSectionContainerLayout = new VerticalScrollLayout();
        leftSideSectionContainerLayout.setWidth(245, Unit.PIXELS);
        mainLayout.add(leftSideSectionContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"地理空间区域实体检索");
        leftSideSectionContainerLayout.add(filterTitle2);

        HorizontalLayout heightSpaceDiv01 = new HorizontalLayout();
        heightSpaceDiv01.setWidthFull();
        leftSideSectionContainerLayout.add(heightSpaceDiv01);
        heightSpaceDiv01.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        TabSheet entitiesSearchQueryElementsTabSheet = new TabSheet();
        entitiesSearchQueryElementsTabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL);
        entitiesSearchQueryElementsTabSheet.setWidthFull();
        leftSideSectionContainerLayout.add(entitiesSearchQueryElementsTabSheet);
        leftSideSectionContainerLayout.setFlexGrow(1,entitiesSearchQueryElementsTabSheet);
        VerticalScrollLayout searchByPropertyMatchContainerLayout = new VerticalScrollLayout();
        VerticalScrollLayout searchBySpaceCalculateContainerLayout = new VerticalScrollLayout();
        searchByPropertyMatchContainerLayout.setMargin(false);
        searchByPropertyMatchContainerLayout.setPadding(false);
        searchBySpaceCalculateContainerLayout.setMargin(false);
        searchBySpaceCalculateContainerLayout.setPadding(false);
        Tab searchByPropertyMatchTab = entitiesSearchQueryElementsTabSheet.add("属性匹配检索",searchByPropertyMatchContainerLayout);
        Tab searchBySpaceCalculateTab = entitiesSearchQueryElementsTabSheet.add("空间计算检索",searchBySpaceCalculateContainerLayout);
        entitiesSearchQueryElementsTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {
                Tab selectedTab = selectedChangeEvent.getSelectedTab();
                if(selectedTab == searchByPropertyMatchTab){
                    geospatialScaleEntitiesSearchType = "SearchByPropertyMatch";
                }
                if(selectedTab == searchBySpaceCalculateTab){
                    geospatialScaleEntitiesSearchType = "SearchBySpaceCalculate";
                }
            }
        });

        //SearchByPropertyMatchContent
        geospatialRegionNameSelect = new ComboBox<>("地理空间区域名称");
        geospatialRegionNameSelect.setRequired(true);
        geospatialRegionNameSelect.setRequiredIndicatorVisible(true);
        geospatialRegionNameSelect.setAllowCustomValue(false);
        geospatialRegionNameSelect.setPageSize(5);
        geospatialRegionNameSelect.setPlaceholder("选择地理空间区域名称");
        geospatialRegionNameSelect.setWidthFull();
        searchByPropertyMatchContainerLayout.add(geospatialRegionNameSelect);

        eventPropertyGeospatialPropertySelect = new ComboBox<>("地理空间属性类型");
        eventPropertyGeospatialPropertySelect.setRequired(true);
        eventPropertyGeospatialPropertySelect.setRequiredIndicatorVisible(true);
        eventPropertyGeospatialPropertySelect.setItems(
                GeospatialRegion.GeospatialProperty.GeospatialCode,
                GeospatialRegion.GeospatialProperty.ChineseName);
        eventPropertyGeospatialPropertySelect.setAllowCustomValue(false);
        eventPropertyGeospatialPropertySelect.setPageSize(3);
        eventPropertyGeospatialPropertySelect.setPlaceholder("选择地理空间属性类型");
        eventPropertyGeospatialPropertySelect.setWidthFull();
        searchByPropertyMatchContainerLayout.add(eventPropertyGeospatialPropertySelect);

        geospatialScaleGradeSelect = new ComboBox<>("事件地理空间刻度");
        geospatialScaleGradeSelect.setRequired(true);
        geospatialScaleGradeSelect.setRequiredIndicatorVisible(true);
        geospatialScaleGradeSelect.setItems(
                GeospatialRegion.GeospatialScaleGrade.CONTINENT,GeospatialRegion.GeospatialScaleGrade.COUNTRY_REGION,
                GeospatialRegion.GeospatialScaleGrade.PROVINCE,GeospatialRegion.GeospatialScaleGrade.PREFECTURE,
                GeospatialRegion.GeospatialScaleGrade.COUNTY,GeospatialRegion.GeospatialScaleGrade.TOWNSHIP,
                GeospatialRegion.GeospatialScaleGrade.VILLAGE);
        geospatialScaleGradeSelect.setAllowCustomValue(false);
        geospatialScaleGradeSelect.setPageSize(30);
        geospatialScaleGradeSelect.setPlaceholder("选择事件地理空间刻度");
        geospatialScaleGradeSelect.setWidthFull();
        searchByPropertyMatchContainerLayout.add(geospatialScaleGradeSelect);

        geospatialAttributeSearchField = new TextField("检索属性值");
        geospatialAttributeSearchField.setRequired(true);
        geospatialAttributeSearchField.setRequiredIndicatorVisible(true);
        geospatialAttributeSearchField.setWidthFull();
        searchByPropertyMatchContainerLayout.add(geospatialAttributeSearchField);

        //SearchBySpaceCalculateContent
        geospatialRegionNameSelect1 = new ComboBox<>("地理空间区域名称");
        geospatialRegionNameSelect1.setRequired(true);
        geospatialRegionNameSelect1.setRequiredIndicatorVisible(true);
        geospatialRegionNameSelect1.setAllowCustomValue(false);
        geospatialRegionNameSelect1.setPageSize(5);
        geospatialRegionNameSelect1.setPlaceholder("选择地理空间区域名称");
        geospatialRegionNameSelect1.setWidthFull();
        searchBySpaceCalculateContainerLayout.add(geospatialRegionNameSelect1);

        spatialScaleLevelSelect = new ComboBox<>("地理空间尺度");
        spatialScaleLevelSelect.setRequired(true);
        spatialScaleLevelSelect.setRequiredIndicatorVisible(true);
        spatialScaleLevelSelect.setItems(
                GeospatialScaleCalculable.SpatialScaleLevel.Global,
                GeospatialScaleCalculable.SpatialScaleLevel.Country,
                GeospatialScaleCalculable.SpatialScaleLevel.Local);
        spatialScaleLevelSelect.setAllowCustomValue(false);
        spatialScaleLevelSelect.setPageSize(3);
        spatialScaleLevelSelect.setPlaceholder("选择地理空间尺度参考坐标系");
        spatialScaleLevelSelect.setWidthFull();
        searchBySpaceCalculateContainerLayout.add(spatialScaleLevelSelect);

        spatialPredicateTypeSelect = new ComboBox<>("地理空间计算谓词类型");
        spatialPredicateTypeSelect.setRequired(true);
        spatialPredicateTypeSelect.setRequiredIndicatorVisible(true);
        spatialPredicateTypeSelect.setItems(
                GeospatialScaleCalculable.SpatialPredicateType.Contains,
                GeospatialScaleCalculable.SpatialPredicateType.Intersects,
                GeospatialScaleCalculable.SpatialPredicateType.Within,
                GeospatialScaleCalculable.SpatialPredicateType.Equals,
                GeospatialScaleCalculable.SpatialPredicateType.Crosses,
                GeospatialScaleCalculable.SpatialPredicateType.Touches,
                GeospatialScaleCalculable.SpatialPredicateType.Overlaps,
                GeospatialScaleCalculable.SpatialPredicateType.Disjoint,
                GeospatialScaleCalculable.SpatialPredicateType.Cover,
                GeospatialScaleCalculable.SpatialPredicateType.CoveredBy);
        spatialPredicateTypeSelect.setAllowCustomValue(false);
        spatialPredicateTypeSelect.setPageSize(10);
        spatialPredicateTypeSelect.setPlaceholder("选择空间拓扑关系定义");
        spatialPredicateTypeSelect.setWidthFull();
        searchBySpaceCalculateContainerLayout.add(spatialPredicateTypeSelect);

        geospatialScaleGradeSelect1 = new ComboBox<>("事件地理空间刻度");
        geospatialScaleGradeSelect1.setRequired(true);
        geospatialScaleGradeSelect1.setRequiredIndicatorVisible(true);
        geospatialScaleGradeSelect1.setItems(
                //GeospatialRegion.GeospatialScaleGrade.CONTINENT,
                GeospatialRegion.GeospatialScaleGrade.COUNTRY_REGION,
                GeospatialRegion.GeospatialScaleGrade.PROVINCE,
                GeospatialRegion.GeospatialScaleGrade.PREFECTURE,
                GeospatialRegion.GeospatialScaleGrade.COUNTY,
                GeospatialRegion.GeospatialScaleGrade.TOWNSHIP,
                GeospatialRegion.GeospatialScaleGrade.VILLAGE);
        geospatialScaleGradeSelect1.setAllowCustomValue(false);
        geospatialScaleGradeSelect1.setPageSize(7);
        geospatialScaleGradeSelect1.setPlaceholder("选择事件地理空间刻度");
        geospatialScaleGradeSelect1.setWidthFull();
        searchBySpaceCalculateContainerLayout.add(geospatialScaleGradeSelect1);

        HorizontalLayout heightSpaceDiv1 = new HorizontalLayout();
        heightSpaceDiv1.setWidthFull();
        leftSideSectionContainerLayout.add(heightSpaceDiv1);
        heightSpaceDiv1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("padding-bottom","2px");

        HorizontalLayout heightSpaceDiv2 = new HorizontalLayout();
        heightSpaceDiv2.setWidth(10,Unit.PIXELS);
        heightSpaceDiv2.setHeight(15,Unit.PIXELS);
        leftSideSectionContainerLayout.add(heightSpaceDiv2);

        Button executeQueryButton = new Button("检索地理空间区域实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryGeospatialScaleEntities();
            }
        });
        leftSideSectionContainerLayout.add(executeQueryButton);

        VerticalLayout middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);
        mainLayout.add(middleContainerLayout);

        middleContainerLayout.setWidth(515, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");

        resultNumberValue = new NativeLabel("");
        resultNumberValue.addClassNames("text-xs","font-bold");
        resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterResultTitle = new SecondaryIconTitle(FontAwesome.Solid.CLOCK.create(),"地理空间区域实体检索结果",resultNumberValue);
        filterResultTitle.getStyle().set("padding-left","10px").set("padding-top","9px");
        middleContainerLayout.add(filterResultTitle);

        HorizontalLayout heightSpaceDiv02 = new HorizontalLayout();
        heightSpaceDiv02.setWidthFull();
        middleContainerLayout.add(heightSpaceDiv02);
        heightSpaceDiv02.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(geospatialScaleEntity -> {
            Icon eyeIcon = new Icon(VaadinIcon.EYE);
            eyeIcon.setSize("20px");
            Button geospatialScaleEntityDetailButton = new Button(eyeIcon, event -> {
                if(geospatialScaleEntity instanceof GeospatialScaleEntity){
                    renderGeospatialScaleEntityDetailUI((GeospatialScaleEntity)geospatialScaleEntity);
                }
            });
            geospatialScaleEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            geospatialScaleEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            geospatialScaleEntityDetailButton.setTooltipText("显示地理空间区域尺度实体详情");

            HorizontalLayout buttons = new HorizontalLayout(geospatialScaleEntityDetailButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        geospatialScaleEntitiesGrid = new Grid<>();

        geospatialScaleEntitiesGrid.setWidth(510,Unit.PIXELS);
        geospatialScaleEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        geospatialScaleEntitiesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        geospatialScaleEntitiesGrid.addColumn(GeospatialScaleEntity::getGeospatialScaleGrade).setHeader("粒度").setKey("idx_0").setWidth("60px").setTooltipGenerator(new ItemLabelGenerator<GeospatialScaleEntity>() {
            @Override
            public String apply(GeospatialScaleEntity geospatialScaleEntity) {
                return geospatialScaleEntity.getGeospatialScaleGrade().toString();
            }
        });
        geospatialScaleEntitiesGrid.addColumn(GeospatialScaleEntity::getGeospatialCode).setHeader("空间编码").setKey("idx_1").setWidth("90px").setTooltipGenerator(new ItemLabelGenerator<GeospatialScaleEntity>() {
            @Override
            public String apply(GeospatialScaleEntity geospatialScaleEntity) {
                return geospatialScaleEntity.getGeospatialCode();
            }
        });
        geospatialScaleEntitiesGrid.addColumn(GeospatialScaleEntity::getChineseName).setHeader("中文名称").setKey("idx_2").setTooltipGenerator(new ItemLabelGenerator<GeospatialScaleEntity>() {
            @Override
            public String apply(GeospatialScaleEntity geospatialScaleEntity) {
                return geospatialScaleEntity.getChineseName();
            }
        });
        geospatialScaleEntitiesGrid.addColumn(GeospatialScaleEntity::getEnglishName).setHeader("英文名称").setKey("idx_3").setTooltipGenerator(new ItemLabelGenerator<GeospatialScaleEntity>() {
            @Override
            public String apply(GeospatialScaleEntity geospatialScaleEntity) {
                return geospatialScaleEntity.getEnglishName();
            }
        });
        geospatialScaleEntitiesGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setWidth("60px");

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.LOCATION_ARROW,"粒度");
        geospatialScaleEntitiesGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(false);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CODE,"空间编码");
        geospatialScaleEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.COMMENT_ELLIPSIS,"中文名称");
        geospatialScaleEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.COMMENT_ELLIPSIS_O,"英文名称");
        geospatialScaleEntitiesGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        geospatialScaleEntitiesGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_1_idx4).setSortable(false);

        middleContainerLayout.add(geospatialScaleEntitiesGrid);

        VerticalLayout rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainLayout.add(rightSideContainerLayout);

        rightSideContainerLayout.setWidth(510,Unit.PIXELS);
        rightSideContainerLayout.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle eventCommentInfoTitle = new SecondaryIconTitle(VaadinIcon.CALENDAR_CLOCK.create(), "事件信息");
        eventCommentInfoTitle.getStyle().set("padding-left","10px").set("padding-top","9px");
        rightSideContainerLayout.add(eventCommentInfoTitle);

        HorizontalLayout heightSpaceDiv03 = new HorizontalLayout();
        heightSpaceDiv03.setWidthFull();
        rightSideContainerLayout.add(heightSpaceDiv03);
        heightSpaceDiv03.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        VerticalLayout rightSideContentLayout = new VerticalLayout();
        rightSideContentLayout.setSpacing(true);
        rightSideContainerLayout.add(rightSideContentLayout);

        eventCommentField = new TextField("时间事件备注");
        eventCommentField.setRequired(true);
        eventCommentField.setRequiredIndicatorVisible(true);
        eventCommentField.setWidthFull();
        rightSideContentLayout.add(eventCommentField);

        HorizontalLayout addEventAttributesUIContainerLayout = new HorizontalLayout();
        addEventAttributesUIContainerLayout.setSpacing(false);
        addEventAttributesUIContainerLayout.setMargin(false);
        addEventAttributesUIContainerLayout.setPadding(false);
        rightSideContentLayout.add(addEventAttributesUIContainerLayout);

        addEventAttributesUIContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定时间事件属性");
        addEventAttributesUIContainerLayout.add(infoTitle3);

        Button addAttributeButton = new Button();
        addAttributeButton.setTooltipText("添加时间事件属性");
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addAttributeButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewAttributeUI();
            }
        });
        addEventAttributesUIContainerLayout.add(addAttributeButton);

        clearAttributeButton = new Button();
        clearAttributeButton.setTooltipText("清除已设置时间事件属性");
        clearAttributeButton.setEnabled(false);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        clearAttributeButton.setIcon(VaadinIcon.RECYCLE.create());
        clearAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cleanRelationAttributes();
            }
        });
        addEventAttributesUIContainerLayout.add(clearAttributeButton);

        eventEntityAttributesContainer = new VerticalLayout();
        eventEntityAttributesContainer.setMargin(false);
        eventEntityAttributesContainer.setSpacing(false);
        eventEntityAttributesContainer.setPadding(false);
        eventEntityAttributesContainer.setWidth(100, Unit.PERCENTAGE);

        Scroller relationEntityAttributesScroller = new Scroller(eventEntityAttributesContainer);
        relationEntityAttributesScroller.setHeight(250,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        rightSideContentLayout.add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        rightSideContentLayout.add(spaceDivLayout);

        Button confirmButton = new Button("链接概念实体至地理空间区域",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        rightSideContentLayout.add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                attachConceptionEntityToGeospatialRegion();
            }
        });
        this.eventAttributeEditorsMap = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadGeospatialRegionNameSelect();
    }

    private void loadGeospatialRegionNameSelect(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<GeospatialRegion> geospatialRegionList = coreRealm.getGeospatialRegions();
        if(geospatialRegionList != null){
            List<String> geospatialRegionNameList = new ArrayList<>();
            for(GeospatialRegion currentGeospatialRegion : geospatialRegionList){
                geospatialRegionNameList.add(currentGeospatialRegion.getGeospatialRegionName());
            }
            geospatialRegionNameSelect.setItems(geospatialRegionNameList);
            geospatialRegionNameSelect1.setItems(geospatialRegionNameList);
            if(!geospatialRegionNameList.isEmpty()){
                geospatialRegionNameSelect.setValue(geospatialRegionNameList.get(0));
                geospatialRegionNameSelect1.setValue(geospatialRegionNameList.get(0));
            }
        }
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(null,null, AddEntityAttributeView.KindType.ConceptionKind);
        AttributeValueOperateHandler attributeValueOperateHandlerForDelete = new AttributeValueOperateHandler(){
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(eventAttributeEditorsMap.containsKey(attributeName)){
                    eventEntityAttributesContainer.remove(eventAttributeEditorsMap.get(attributeName));
                }
                eventAttributeEditorsMap.remove(attributeName);
                if(eventAttributeEditorsMap.size()==0) {
                    clearAttributeButton.setEnabled(false);
                }
            }
        };
        AttributeValueOperateHandler attributeValueOperateHandlerForAdd = new AttributeValueOperateHandler() {
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(eventAttributeEditorsMap.containsKey(attributeName)){
                    CommonUIOperationUtil.showPopupNotification("已经设置了名称为 "+attributeName+" 的事件属性", NotificationVariant.LUMO_ERROR);
                }else{
                    AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(null,null,attributeValue, AttributeEditorItemWidget.KindType.RelationKind);
                    attributeEditorItemWidget.setWidth(445,Unit.PIXELS);
                    eventEntityAttributesContainer.add(attributeEditorItemWidget);
                    attributeEditorItemWidget.setAttributeValueOperateHandler(attributeValueOperateHandlerForDelete);
                    eventAttributeEditorsMap.put(attributeName,attributeEditorItemWidget);
                }
                if(eventAttributeEditorsMap.size()>0){
                    clearAttributeButton.setEnabled(true);
                }
            }
        };
        addEntityAttributeView.setAttributeValueOperateHandler(attributeValueOperateHandlerForAdd);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加时间事件属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void cleanRelationAttributes(){
        eventEntityAttributesContainer.removeAll();
        eventAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
    }

    private void queryGeospatialScaleEntities(){
        if(geospatialScaleEntitiesSearchType.equals("SearchByPropertyMatch")){
            String geospatialRegionName = geospatialRegionNameSelect.getValue();
            GeospatialRegion.GeospatialProperty selectedGeospatialProperty = eventPropertyGeospatialPropertySelect.getValue();
            GeospatialRegion.GeospatialScaleGrade selectedGeospatialScaleGrade = geospatialScaleGradeSelect.getValue();
            String geospatialAttributeSearchValue = geospatialAttributeSearchField.getValue();
            if(geospatialRegionName == null){
                CommonUIOperationUtil.showPopupNotification("请选择地理空间区域名称", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            if(selectedGeospatialProperty == null){
                CommonUIOperationUtil.showPopupNotification("请选择地理空间属性类型", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            if(selectedGeospatialScaleGrade == null){
                CommonUIOperationUtil.showPopupNotification("请选择事件地理空间刻度", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            if(geospatialAttributeSearchValue == null || geospatialAttributeSearchValue.equals("")){
                CommonUIOperationUtil.showPopupNotification("请输入检索属性值", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            GeospatialRegion targetGeospatialRegion = coreRealm.getOrCreateGeospatialRegion(geospatialRegionName);
            if(targetGeospatialRegion != null){
                //targetGeospatialRegion









            }


        }
        if(geospatialScaleEntitiesSearchType.equals("SearchBySpaceCalculate")){
            String geospatialRegionName = geospatialRegionNameSelect1.getValue();
            GeospatialRegion.GeospatialScaleGrade selectedGeospatialScaleGrade = geospatialScaleGradeSelect1.getValue();
            GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel = spatialScaleLevelSelect.getValue();
            GeospatialScaleCalculable.SpatialPredicateType spatialPredicateType = spatialPredicateTypeSelect.getValue();
            List<GeospatialScaleEntity> targetGeospatialScaleEntities = null;
            if(geospatialRegionName == null){
                CommonUIOperationUtil.showPopupNotification("请选择地理空间区域名称", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            if(spatialScaleLevel == null){
                CommonUIOperationUtil.showPopupNotification("请选择地理空间尺度参考坐标系", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            if(selectedGeospatialScaleGrade == null){
                CommonUIOperationUtil.showPopupNotification("请选择事件地理空间刻度", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            if(spatialPredicateType == null){
                CommonUIOperationUtil.showPopupNotification("请选择空间拓扑关系定义", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
                return;
            }
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            if(targetConceptionKind != null){
                ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                if(targetConceptionEntity != null){
                    try {
                        targetGeospatialScaleEntities = targetConceptionEntity.getSpatialPredicateMatchedGeospatialScaleEntities(spatialScaleLevel,spatialPredicateType,selectedGeospatialScaleGrade,geospatialRegionName);
                    } catch (CoreRealmServiceRuntimeException e) {
                        throw new RuntimeException(e);
                    } catch (CoreRealmServiceEntityExploreException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            coreRealm.closeGlobalSession();

            geospatialScaleEntitiesGrid.setItems(targetGeospatialScaleEntities);
            CommonUIOperationUtil.showPopupNotification("地理空间刻度实体查询操作成功，查询返回实体数: "+targetGeospatialScaleEntities.size(),
                    NotificationVariant.LUMO_SUCCESS,3000, Notification.Position.BOTTOM_START);
            resultNumberValue.setText("实体总量："+this.numberFormat.format(targetGeospatialScaleEntities.size()));
        }
    }

    private void renderGeospatialScaleEntityDetailUI(GeospatialScaleEntity geospatialScaleEntity){
        String geospatialScaleEntityClassName = RealmConstant.GeospatialScaleEntityClass;
        GeospatialRegion.GeospatialScaleGrade currentGeospatialScaleGrade = geospatialScaleEntity.getGeospatialScaleGrade();
        switch(currentGeospatialScaleGrade){
            case CONTINENT -> geospatialScaleEntityClassName = RealmConstant.GeospatialScaleContinentEntityClass;
            case COUNTRY_REGION -> geospatialScaleEntityClassName = RealmConstant.GeospatialScaleCountryRegionEntityClass;
            case PROVINCE -> geospatialScaleEntityClassName = RealmConstant.GeospatialScaleProvinceEntityClass;
            case PREFECTURE -> geospatialScaleEntityClassName = RealmConstant.GeospatialScalePrefectureEntityClass;
            case COUNTY -> geospatialScaleEntityClassName = RealmConstant.GeospatialScaleCountyEntityClass;
            case TOWNSHIP -> geospatialScaleEntityClassName = RealmConstant.GeospatialScaleTownshipEntityClass;
            case VILLAGE -> geospatialScaleEntityClassName = RealmConstant.GeospatialScaleVillageEntityClass;
        }
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(geospatialScaleEntityClassName,geospatialScaleEntity.getGeospatialScaleEntityUID());

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
        NativeLabel conceptionKindNameLabel = new NativeLabel(geospatialScaleEntityClassName);
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

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(geospatialScaleEntity.getGeospatialScaleEntityUID());
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void attachConceptionEntityToGeospatialRegion(){
        Set<GeospatialScaleEntity> selectedGeospatialScaleEntitySet = geospatialScaleEntitiesGrid.getSelectedItems();
        if(selectedGeospatialScaleEntitySet == null || selectedGeospatialScaleEntitySet.isEmpty()){
            CommonUIOperationUtil.showPopupNotification("请选择至少一项地理空间尺度实体", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        String eventComment = eventCommentField.getValue();
        if(eventComment.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入地理空间事件备注", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认链接概念实体至地理空间区域",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行链接概念实体至地理空间区域操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAttachConceptionEntityToGeospatialRegion();
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doAttachConceptionEntityToGeospatialRegion(){
        Set<GeospatialScaleEntity> selectedGeospatialScaleEntitySet = geospatialScaleEntitiesGrid.getSelectedItems();
        if(selectedGeospatialScaleEntitySet != null & !selectedGeospatialScaleEntitySet.isEmpty()){
            List<GeospatialScaleEvent> resultEventList = new ArrayList<>();
            Map<String, Object> eventData = eventAttributeEditorsMap.isEmpty() ? null : new HashMap<>();
            if(!eventAttributeEditorsMap.isEmpty()){
                Set<String> commentPropertiesNameSet = eventAttributeEditorsMap.keySet();
                for(String currentPropertyName:commentPropertiesNameSet){
                    AttributeEditorItemWidget attributeEditorItemWidget = eventAttributeEditorsMap.get(currentPropertyName);
                    eventData.put(attributeEditorItemWidget.getAttributeName(),attributeEditorItemWidget.getAttributeValue().getAttributeValue());
                }
            }

            String eventComment = eventCommentField.getValue();

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);

            for(GeospatialScaleEntity currentGeospatialScaleEntity : selectedGeospatialScaleEntitySet){
                try {
                    GeospatialScaleEvent resultEvent = targetConceptionEntity.
                            attachGeospatialScaleEventByGeospatialScaleEntityUID(currentGeospatialScaleEntity.getGeospatialScaleEntityUID(),eventComment,eventData);
                    if(resultEvent != null){
                        resultEventList.add(resultEvent);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
            }

            if(this.attachGeospatialScaleEventsOfConceptionEntityCallback != null){
                this.attachGeospatialScaleEventsOfConceptionEntityCallback.onSuccess(resultEventList);
            }

            if(this.containerDialog != null){
                this.containerDialog.close();
            }
            coreRealm.closeGlobalSession();
            showPopupNotification(resultEventList.size(),NotificationVariant.LUMO_SUCCESS);
        }
    }

    private void showPopupNotification(int resultNumber, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("链接概念实体 "+this.conceptionKind+"/"+this.conceptionEntityUID+ " 至地理空间区域操作成功"));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setFlexGrow(1,text);
        notification.add(layout);

        VerticalLayout notificationMessageContainer = new VerticalLayout();
        notificationMessageContainer.add(new Div(new Text("链接实体数: "+resultNumber)));
        notification.setDuration(3000);
        notification.open();
    }
}
