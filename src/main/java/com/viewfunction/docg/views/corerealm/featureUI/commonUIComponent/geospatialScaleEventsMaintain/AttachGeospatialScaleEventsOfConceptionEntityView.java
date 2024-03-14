package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialScaleEventsMaintain;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public AttachGeospatialScaleEventsOfConceptionEntityView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

        this.setSpacing(false);
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
        entitiesSearchQueryElementsTabSheet.add("属性匹配检索",searchByPropertyMatchContainerLayout);
        entitiesSearchQueryElementsTabSheet.add("空间计算检索",searchBySpaceCalculateContainerLayout);

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
                GeospatialRegion.GeospatialScaleGrade.CONTINENT,
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
                //queryTimeScaleEntities();
            }
        });
        leftSideSectionContainerLayout.add(executeQueryButton);

        VerticalLayout middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);
        mainLayout.add(middleContainerLayout);

        middleContainerLayout.setWidth(315, Unit.PIXELS);
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












        VerticalLayout rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainLayout.add(rightSideContainerLayout);

        rightSideContainerLayout.setWidth(470,Unit.PIXELS);
        rightSideContainerLayout.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle eventCommentInfoTitle = new SecondaryIconTitle(VaadinIcon.CALENDAR_CLOCK.create(), "事件信息",resultNumberValue);
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
                //attachConceptionEntityToTimeFlow();
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

}
