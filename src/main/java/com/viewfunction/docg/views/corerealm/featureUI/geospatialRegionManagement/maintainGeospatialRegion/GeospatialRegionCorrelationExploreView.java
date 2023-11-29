package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GeospatialRegionCorrelationExploreView extends VerticalLayout {
    private String geospatialRegionName;
    public enum RenderType {Single,List_SameLevel,List_SubLevel}
    private int viewHeight;
    private int viewWidth;
    private Grid<AttributeValue> entityAttributesInfoGrid;
    private SecondaryTitleActionBar geospatialScaleEntityTitleActionBar;
    private NativeLabel entityUIDLabel;
    private PrimaryKeyValueDisplayItem relatedConceptionEntitiesCountDisplayItem;
    private NumberFormat numberFormat;
    private VerticalLayout entityInfoContainerLayout;
    private MenuItem childrenEntitiesDataMenu;
    private MenuItem sameLevelEntitiesDataMenu;
    private GeospatialScaleEntity currentDisplayingGeospatialScaleEntity;
    public GeospatialRegionCorrelationExploreView(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.numberFormat = NumberFormat.getInstance();

        HorizontalLayout singleGeospatialRegionElementsContainerLayout = new HorizontalLayout();
        singleGeospatialRegionElementsContainerLayout.setSpacing(false);
        singleGeospatialRegionElementsContainerLayout.setMargin(false);
        singleGeospatialRegionElementsContainerLayout.setPadding(false);
        singleGeospatialRegionElementsContainerLayout.setHeight("17px");
        add(singleGeospatialRegionElementsContainerLayout);

        SecondaryIconTitle geospatialScaleEntityTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"地理空间区域尺度实体概览");
        singleGeospatialRegionElementsContainerLayout.add(geospatialScaleEntityTitle);

        HorizontalLayout singleGeospatialRegionElementsFootprintMessageLayout = new HorizontalLayout();
        add(singleGeospatialRegionElementsFootprintMessageLayout);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        singleGeospatialRegionElementsFootprintMessageLayout.add(footPrintStartIcon);

        Button _CONTINENTButton = new Button("亚洲");
        _CONTINENTButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_CONTINENTButton);

        Icon divIcon0 = VaadinIcon.ITALIC.create();
        divIcon0.setSize("12px");
        divIcon0.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon0);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon0);

        Button _COUNTRY_REGIONButton = new Button("中国");
        _COUNTRY_REGIONButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_COUNTRY_REGIONButton);

        Icon divIcon1 = VaadinIcon.ITALIC.create();
        divIcon1.setSize("12px");
        divIcon1.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon1);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon1);

        Button _PROVINCEButton = new Button("新疆维吾尔自治区");
        _PROVINCEButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_PROVINCEButton);

        Icon divIcon2 = VaadinIcon.ITALIC.create();
        divIcon2.setSize("12px");
        divIcon2.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon2);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon2);

        Button _PREFECTUREButton = new Button("克孜勒苏柯尔克孜自治州");
        _PREFECTUREButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_PREFECTUREButton);

        Icon divIcon3 = VaadinIcon.ITALIC.create();
        divIcon3.setSize("12px");
        divIcon3.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon3);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon3);

        Button _COUNTYButton = new Button("阿合奇县");
        _COUNTYButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_COUNTYButton);

        Icon divIcon4 = VaadinIcon.ITALIC.create();
        divIcon4.setSize("12px");
        divIcon4.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon4);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon4);

        Button _TOWNSHIPButton = new Button("库兰萨日克乡");
        _TOWNSHIPButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        singleGeospatialRegionElementsFootprintMessageLayout.add(_TOWNSHIPButton);

        Icon divIcon5 = VaadinIcon.ITALIC.create();
        divIcon5.setSize("12px");
        divIcon5.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon5);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon5);

        NativeLabel villaggeName = new NativeLabel("阿克特克提尔村委会");
        singleGeospatialRegionElementsFootprintMessageLayout.add(villaggeName);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,villaggeName);

        Icon divIcon6 = VaadinIcon.KEY_O.create();
        divIcon6.setSize("10px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon6);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon6);
        entityUIDLabel = new NativeLabel("-");
        singleGeospatialRegionElementsFootprintMessageLayout.add(entityUIDLabel);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER, entityUIDLabel);

        this.geospatialScaleEntityTitleActionBar = new SecondaryTitleActionBar(FontAwesome.Solid.MAP.create(),"-",null,null);
        this.geospatialScaleEntityTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        add(this.geospatialScaleEntityTitleActionBar);

        HorizontalLayout geospatialScaleEntityInfoContainerLayout = new HorizontalLayout();
        geospatialScaleEntityInfoContainerLayout.setSpacing(false);
        geospatialScaleEntityInfoContainerLayout.setMargin(false);
        geospatialScaleEntityInfoContainerLayout.setPadding(false);
        add(geospatialScaleEntityInfoContainerLayout);

        entityInfoContainerLayout = new VerticalLayout();
        entityInfoContainerLayout.setSpacing(false);
        entityInfoContainerLayout.setMargin(false);
        entityInfoContainerLayout.setPadding(false);
        entityInfoContainerLayout.setWidth(500,Unit.PIXELS);
        geospatialScaleEntityInfoContainerLayout.add(entityInfoContainerLayout);
        entityInfoContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        HorizontalLayout summaryInfoContainer = new HorizontalLayout();
        summaryInfoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        summaryInfoContainer.setWidthFull();
        summaryInfoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        entityInfoContainerLayout.add(summaryInfoContainer);

        relatedConceptionEntitiesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(summaryInfoContainer, FontAwesome.Solid.CIRCLE.create(),"地理空间关联概念实体数量:","-");

        Icon divIcon7 = VaadinIcon.LINE_V.create();
        divIcon7.setSize("8px");
        summaryInfoContainer.add(divIcon7);

        MenuBar importMenuBar0 = new MenuBar();
        importMenuBar0.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        sameLevelEntitiesDataMenu = createIconItem(importMenuBar0, VaadinIcon.ARROWS_LONG_H, "同级地理空间区域实体", null);
        sameLevelEntitiesDataMenu.getStyle().set("font-size","0.75rem");
        Icon downArrowIcon2 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon2.setSize("10px");
        sameLevelEntitiesDataMenu.add(downArrowIcon2);
        summaryInfoContainer.add(importMenuBar0);

        MenuBar importMenuBar1 = new MenuBar();
        importMenuBar1.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        childrenEntitiesDataMenu = createIconItem(importMenuBar1, VaadinIcon.ARROW_LONG_DOWN, "下级地理空间区域实体", null);
        childrenEntitiesDataMenu.getStyle().set("font-size","0.75rem");
        Icon downArrowIcon1 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon1.setSize("10px");
        childrenEntitiesDataMenu.add(downArrowIcon1);
        summaryInfoContainer.add(importMenuBar1);

        Icon divIcon8 = VaadinIcon.LINE_V.create();
        divIcon8.setSize("8px");
        summaryInfoContainer.add(divIcon8);

        Button displayCurrentEntityDetail = new Button("显示实体详情");
        displayCurrentEntityDetail.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        displayCurrentEntityDetail.setIcon(VaadinIcon.EYE.create());
        displayCurrentEntityDetail.getStyle().set("font-size","0.75rem");
        displayCurrentEntityDetail.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(currentDisplayingGeospatialScaleEntity != null){
                    renderGeospatialScaleEntityDetailUI(currentDisplayingGeospatialScaleEntity);
                }
            }
        });
        summaryInfoContainer.add(displayCurrentEntityDetail);

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setSpacing(false);
        gridContainer.setMargin(false);
        gridContainer.setPadding(false);
        gridContainer.setWidth(300,Unit.PIXELS);
        gridContainer.setAlignItems(Alignment.START);
        geospatialScaleEntityInfoContainerLayout.add(gridContainer);
        geospatialScaleEntityInfoContainerLayout.setVerticalComponentAlignment(Alignment.START,gridContainer);

        VerticalLayout spaceMarginDiv = new VerticalLayout();
        spaceMarginDiv.setSpacing(false);
        spaceMarginDiv.setMargin(false);
        spaceMarginDiv.setPadding(false);
        spaceMarginDiv.setHeight(5,Unit.PIXELS);
        gridContainer.add(spaceMarginDiv);
        gridContainer.setHorizontalComponentAlignment(Alignment.START,spaceMarginDiv);

        entityAttributesInfoGrid = new Grid<>();
        entityAttributesInfoGrid.setWidth(300, Unit.PIXELS);
        entityAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        entityAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeName).setHeader("属性名称").setKey("idx_0");
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeValue).setHeader("属性值").setKey("idx_1").setFlexGrow(1).setResizable(true);
        entityAttributesInfoGrid.getStyle().set("padding-top", "100px");

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        entityAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.INPUT,"属性值");
        entityAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        gridContainer.add(entityAttributesInfoGrid);
        gridContainer.setHorizontalComponentAlignment(Alignment.START,entityAttributesInfoGrid);
    }

    private void renderSingleGeospatialRegionEntity(GeospatialScaleEntity targetGeospatialScaleEntity){
        this.currentDisplayingGeospatialScaleEntity = targetGeospatialScaleEntity;
        String currentGeospatialScaleEntityUID = targetGeospatialScaleEntity.getGeospatialScaleEntityUID();
        GeospatialRegion.GeospatialScaleGrade currentGeospatialScaleGrade = targetGeospatialScaleEntity.getGeospatialScaleGrade();
        String geospatialScaleEntityKindName = null;

        switch(currentGeospatialScaleGrade){
            case CONTINENT -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleContinentEntityClass;
            case COUNTRY_REGION -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleCountryRegionEntityClass;
            case PROVINCE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleProvinceEntityClass;
            case PREFECTURE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScalePrefectureEntityClass;
            case COUNTY -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleCountyEntityClass;
            case TOWNSHIP -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleTownshipEntityClass;
            case VILLAGE -> geospatialScaleEntityKindName = RealmConstant.GeospatialScaleVillageEntityClass;
        }

        String entityEnglishName = targetGeospatialScaleEntity.getEnglishName() != null ? targetGeospatialScaleEntity.getEnglishName() : "-";
        this.geospatialScaleEntityTitleActionBar.updateTitleContent(currentGeospatialScaleGrade.toString() + " : "+
                targetGeospatialScaleEntity.getChineseName()+" ("+entityEnglishName+") - "+targetGeospatialScaleEntity.getGeospatialCode());
        this.entityUIDLabel.setText(currentGeospatialScaleEntityUID);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        GeospatialRegion geospatialRegion = coreRealm.getOrCreateGeospatialRegion(this.geospatialRegionName);
        targetGeospatialScaleEntity = geospatialRegion.getEntityByGeospatialCode(targetGeospatialScaleEntity.getGeospatialCode());

        Long relatedConceptionEntitiesCount = targetGeospatialScaleEntity.countAttachedConceptionEntities(GeospatialScaleEntity.GeospatialScaleLevel.SELF);

        List<GeospatialScaleEntity> childEntitiesList = targetGeospatialScaleEntity.getChildEntities();
        if(childEntitiesList != null){
            SubMenu childrenEntitiesSubItems = childrenEntitiesDataMenu.getSubMenu();
            childrenEntitiesSubItems.removeAll();
            for(GeospatialScaleEntity currentGeospatialScaleEntity : childEntitiesList){
                String entityName = currentGeospatialScaleEntity.getChineseName();
                String entityCode = currentGeospatialScaleEntity.getGeospatialCode();

                HorizontalLayout actionLayout = new HorizontalLayout();
                actionLayout.setPadding(false);
                actionLayout.setSpacing(false);
                actionLayout.setMargin(false);
                actionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                Icon action0Icon = FontAwesome.Solid.MAP.create();
                action0Icon.setSize("10px");
                Span action0Space = new Span();
                action0Space.setWidth(6,Unit.PIXELS);
                NativeLabel action0Label = new NativeLabel(entityName+"("+entityCode+")");
                action0Label.addClassNames("text-xs","text-secondary");
                actionLayout.add(action0Icon,action0Space,action0Label);

                MenuItem childEntityItem = childrenEntitiesSubItems.addItem(actionLayout);
                childEntityItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                        renderSingleGeospatialRegionEntity(currentGeospatialScaleEntity);
                    }
                });
            }
        }

        List<GeospatialScaleEntity> fellowEntitiesList = targetGeospatialScaleEntity.getFellowEntities();
        if(fellowEntitiesList != null){
            SubMenu fellowEntitiesSubItems = sameLevelEntitiesDataMenu.getSubMenu();
            fellowEntitiesSubItems.removeAll();
            for(GeospatialScaleEntity currentGeospatialScaleEntity : fellowEntitiesList){
                if(!currentGeospatialScaleEntity.getGeospatialCode().equals(targetGeospatialScaleEntity.getGeospatialCode())){
                    String entityName = currentGeospatialScaleEntity.getChineseName();
                    String entityCode = currentGeospatialScaleEntity.getGeospatialCode();

                    HorizontalLayout actionLayout = new HorizontalLayout();
                    actionLayout.setPadding(false);
                    actionLayout.setSpacing(false);
                    actionLayout.setMargin(false);
                    actionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    Icon action0Icon = FontAwesome.Solid.MAP.create();
                    action0Icon.setSize("10px");
                    Span action0Space = new Span();
                    action0Space.setWidth(6,Unit.PIXELS);
                    NativeLabel action0Label = new NativeLabel(entityName+"("+entityCode+")");
                    action0Label.addClassNames("text-xs","text-secondary");
                    actionLayout.add(action0Icon,action0Space,action0Label);

                    MenuItem fellowEntityItem = fellowEntitiesSubItems.addItem(actionLayout);
                    fellowEntityItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                        @Override
                        public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                            renderSingleGeospatialRegionEntity(currentGeospatialScaleEntity);;
                        }
                    });
                }
            }
        }

        targetGeospatialScaleEntity.getParentEntity();

        relatedConceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(relatedConceptionEntitiesCount));

        ConceptionKind targetGeoConceptionKind = coreRealm.getConceptionKind(geospatialScaleEntityKindName);
        ConceptionEntity targetConceptionEntity = targetGeoConceptionKind.getEntityByUID(currentGeospatialScaleEntityUID);
        List<AttributeValue> allAttributesList = targetConceptionEntity.getAttributes();
        entityAttributesInfoGrid.setItems(allAttributesList);

        coreRealm.closeGlobalSession();
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewWidth(int value){
        this.viewWidth = value;
        this.entityInfoContainerLayout.setWidth(this.viewWidth-310, Unit.PIXELS);
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewHeight(int value){
        this.viewHeight = value;
        this.entityAttributesInfoGrid.setHeight(this.viewHeight-100,Unit.PIXELS);
    }

    public void renderGeospatialRegionData(RenderType renderType,List<GeospatialScaleEntity> geospatialScaleEntityList){
        if(geospatialScaleEntityList != null & geospatialScaleEntityList.size() > 0){
            GeospatialScaleEntity currGeospatialScaleEntity = geospatialScaleEntityList.get(0);
            switch(renderType){
                case Single -> renderSingleGeospatialRegionEntity(currGeospatialScaleEntity);
                case List_SameLevel -> renderSameLevelGeospatialRegionEntityList(geospatialScaleEntityList);
                case List_SubLevel -> renderSubLevelGeospatialRegionEntityList(geospatialScaleEntityList);
            }
        }
    }

    private void renderSameLevelGeospatialRegionEntityList(List<GeospatialScaleEntity> geospatialScaleEntityList){
        //this.geospatialChartContainer.removeAll();
        //GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(this.geospatialRegionName);
        //this.geospatialChartContainer.add(geospatialRegionCorrelationInfoChart);
        //geospatialRegionCorrelationInfoChart.renderEntitiesSpatialInfo(geospatialScaleEntityList);
    }

    private void renderSubLevelGeospatialRegionEntityList(List<GeospatialScaleEntity> geospatialScaleEntityList){
        //this.geospatialChartContainer.removeAll();
        //GeospatialRegionCorrelationInfoChart geospatialRegionCorrelationInfoChart = new GeospatialRegionCorrelationInfoChart(this.geospatialRegionName);
       // this.geospatialChartContainer.add(geospatialRegionCorrelationInfoChart);
        //geospatialRegionCorrelationInfoChart.renderEntitiesSpatialInfo(geospatialScaleEntityList);
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

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);
        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }
        MenuItem item = menu.addItem(icon, e -> {
        });
        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }
        if (label != null) {
            item.add(new Text(label));
        }
        return item;
    }
}
