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

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.geospatial.GeospatialCalculateUtil;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeospatialRegionCorrelationExploreView extends VerticalLayout {

    private String geospatialRegionName;
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
    private MenuBar sameLevelEntitiesMenuBar;
    private MenuBar childrenLvelMenuBar;
    private Button displayCurrentEntityDetailButton;
    private Button _CONTINENTButton;
    private Icon divIcon0;
    private Button _COUNTRY_REGIONButton;
    private Icon divIcon1;
    private Button _PROVINCEButton;
    private Icon divIcon2;
    private Button _PREFECTUREButton;
    private Icon divIcon3;
    private Button _COUNTYButton;
    private Icon divIcon4;
    private Button _TOWNSHIPButton;
    private Icon divIcon5;
    private Button _VILLAGEButton;
    private Icon divIcon6;
    private Map<String,GeospatialScaleEntity> navagationBarEntitieesMap;
    private GeospatialScaleEntityMapInfoChart geospatialScaleEntityMapInfoChart;

    public GeospatialRegionCorrelationExploreView(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.numberFormat = NumberFormat.getInstance();
        this.navagationBarEntitieesMap = new HashMap<>();

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

        _CONTINENTButton = new Button("");
        _CONTINENTButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _CONTINENTButton.getStyle().set("font-size","0.8rem");
        _CONTINENTButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_CONTINENTButton);

        divIcon0 = VaadinIcon.ITALIC.create();
        divIcon0.setSize("12px");
        divIcon0.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon0);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon0);

        _COUNTRY_REGIONButton = new Button("");
        _COUNTRY_REGIONButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _COUNTRY_REGIONButton.getStyle().set("font-size","0.8rem");
        _COUNTRY_REGIONButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_COUNTRY_REGIONButton);

        divIcon1 = VaadinIcon.ITALIC.create();
        divIcon1.setSize("12px");
        divIcon1.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon1);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon1);

        _PROVINCEButton = new Button("");
        _PROVINCEButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _PROVINCEButton.getStyle().set("font-size","0.8rem");
        _PROVINCEButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_PROVINCEButton);

        divIcon2 = VaadinIcon.ITALIC.create();
        divIcon2.setSize("12px");
        divIcon2.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon2);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon2);

        _PREFECTUREButton = new Button("");
        _PREFECTUREButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _PREFECTUREButton.getStyle().set("font-size","0.8rem");
        _PREFECTUREButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_PREFECTUREButton);

        divIcon3 = VaadinIcon.ITALIC.create();
        divIcon3.setSize("12px");
        divIcon3.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon3);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon3);

        _COUNTYButton = new Button("");
        _COUNTYButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _COUNTYButton.getStyle().set("font-size","0.8rem");
        _COUNTYButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_COUNTYButton);

        divIcon4 = VaadinIcon.ITALIC.create();
        divIcon4.setSize("12px");
        divIcon4.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon4);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon4);

        _TOWNSHIPButton = new Button("");
        _TOWNSHIPButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _TOWNSHIPButton.getStyle().set("font-size","0.8rem");
        _TOWNSHIPButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_TOWNSHIPButton);

        divIcon5 = VaadinIcon.ITALIC.create();
        divIcon5.setSize("12px");
        divIcon5.getStyle().set("padding-left","5px");
        singleGeospatialRegionElementsFootprintMessageLayout.add(divIcon5);
        singleGeospatialRegionElementsFootprintMessageLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon5);

        _VILLAGEButton = new Button("");
        _VILLAGEButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        _VILLAGEButton.getStyle().set("font-size","0.8rem");
        _VILLAGEButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderSingleGeospatialRegionEntity(buttonClickEvent.getSource().getText());
            }
        });
        singleGeospatialRegionElementsFootprintMessageLayout.add(_VILLAGEButton);

        divIcon6 = VaadinIcon.KEY_O.create();
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

        sameLevelEntitiesMenuBar = new MenuBar();
        sameLevelEntitiesMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        sameLevelEntitiesDataMenu = createIconItem(sameLevelEntitiesMenuBar, VaadinIcon.ARROWS_LONG_H, "同级地理空间区域实体", null);
        sameLevelEntitiesDataMenu.getStyle().set("font-size","0.75rem");
        Icon downArrowIcon2 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon2.setSize("10px");
        sameLevelEntitiesDataMenu.add(downArrowIcon2);
        summaryInfoContainer.add(sameLevelEntitiesMenuBar);

        childrenLvelMenuBar = new MenuBar();
        childrenLvelMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        childrenEntitiesDataMenu = createIconItem(childrenLvelMenuBar, VaadinIcon.ARROW_LONG_DOWN, "下级地理空间区域实体", null);
        childrenEntitiesDataMenu.getStyle().set("font-size","0.75rem");
        Icon downArrowIcon1 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon1.setSize("10px");
        childrenEntitiesDataMenu.add(downArrowIcon1);
        summaryInfoContainer.add(childrenLvelMenuBar);

        Icon divIcon8 = VaadinIcon.LINE_V.create();
        divIcon8.setSize("8px");
        summaryInfoContainer.add(divIcon8);

        displayCurrentEntityDetailButton = new Button("显示实体详情");
        displayCurrentEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        displayCurrentEntityDetailButton.setIcon(VaadinIcon.EYE.create());
        displayCurrentEntityDetailButton.getStyle().set("font-size","0.75rem");
        displayCurrentEntityDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(currentDisplayingGeospatialScaleEntity != null){
                    renderGeospatialScaleEntityDetailUI(currentDisplayingGeospatialScaleEntity);
                }
            }
        });
        summaryInfoContainer.add(displayCurrentEntityDetailButton);

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

        sameLevelEntitiesMenuBar.setEnabled(false);
        childrenLvelMenuBar.setEnabled(false);
        displayCurrentEntityDetailButton.setEnabled(false);

        hideEntityNavigationBarElements();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change

        this.geospatialScaleEntityMapInfoChart = new GeospatialScaleEntityMapInfoChart();
        this.geospatialScaleEntityMapInfoChart.setWidth(100,Unit.PERCENTAGE);
        this.geospatialScaleEntityMapInfoChart.setHeight(300,Unit.PIXELS);
        this.entityInfoContainerLayout.add(this.geospatialScaleEntityMapInfoChart);

        /*
        conceptionEntitySpatialChart = new ConceptionEntitySpatialChart();
        conceptionEntitySpatialChart.setWidth(100,Unit.PERCENTAGE);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionEntitySpatialChart.setHeight(event.getHeight()-this.conceptionEntitySpatialInfoViewHeightOffset+20, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionEntitySpatialChart.setHeight(browserHeight-this.conceptionEntitySpatialInfoViewHeightOffset+20,Unit.PIXELS);
        }));
        add(conceptionEntitySpatialChart);
        */

        this.geospatialScaleEntityMapInfoChart.setVisible(false);
    }

    private void hideEntityNavigationBarElements(){
        _CONTINENTButton.setVisible(false);
        divIcon0.setVisible(false);
        _COUNTRY_REGIONButton.setVisible(false);
        divIcon1.setVisible(false);
        _PROVINCEButton.setVisible(false);
        divIcon2.setVisible(false);
        _PREFECTUREButton.setVisible(false);
        divIcon3.setVisible(false);
        _COUNTYButton.setVisible(false);
        divIcon4.setVisible(false);
        _TOWNSHIPButton.setVisible(false);
        divIcon5.setVisible(false);
        _VILLAGEButton.setVisible(false);
        divIcon6.setVisible(false);
    }

    private void renderSingleGeospatialRegionEntity(String targetGeospatialScaleEntityChineseName){
        if(this.navagationBarEntitieesMap.containsKey(targetGeospatialScaleEntityChineseName)){
            renderSingleGeospatialRegionEntity(this.navagationBarEntitieesMap.get(targetGeospatialScaleEntityChineseName));
        }
    }

    public void renderSingleGeospatialRegionEntity(GeospatialScaleEntity targetGeospatialScaleEntity){
        this.currentDisplayingGeospatialScaleEntity = targetGeospatialScaleEntity;
        this.navagationBarEntitieesMap.clear();

        hideEntityNavigationBarElements();
        this.sameLevelEntitiesMenuBar.setEnabled(true);
        this.childrenLvelMenuBar.setEnabled(true);
        this.displayCurrentEntityDetailButton.setEnabled(true);

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

        setupEntityNavigationBar(targetGeospatialScaleEntity);
        divIcon6.setVisible(true);

        Long relatedConceptionEntitiesCount = targetGeospatialScaleEntity.countAttachedConceptionEntities(GeospatialScaleEntity.GeospatialScaleLevel.SELF);
        relatedConceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(relatedConceptionEntitiesCount));

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

        ConceptionKind targetGeoConceptionKind = coreRealm.getConceptionKind(geospatialScaleEntityKindName);
        ConceptionEntity targetConceptionEntity = targetGeoConceptionKind.getEntityByUID(currentGeospatialScaleEntityUID);
        List<AttributeValue> allAttributesList = targetConceptionEntity.getAttributes();
        entityAttributesInfoGrid.setItems(allAttributesList);

        this.geospatialScaleEntityMapInfoChart.setVisible(true);
        this.geospatialScaleEntityMapInfoChart.renderMapAndSpatialInfo(geospatialScaleEntityKindName,currentGeospatialScaleEntityUID);
        renderEntityMapInfo(targetConceptionEntity);
        coreRealm.closeGlobalSession();
    }

    private void renderEntityMapInfo(ConceptionEntity targetConceptionEntity){
        GeospatialScaleFeatureSupportable.WKTGeometryType _WKTGeometryType = targetConceptionEntity.getGeometryType();
        try {
            String centroidPointWKT = targetConceptionEntity.getEntitySpatialCentroidPointWKTGeometryContent(GeospatialScaleCalculable.SpatialScaleLevel.Global);
            String envelopeAreaWKT = targetConceptionEntity.getEntitySpatialEnvelopeWKTGeometryContent(GeospatialScaleCalculable.SpatialScaleLevel.Global);
            String geometryCRSAID = targetConceptionEntity.getGlobalCRSAID();
            String geometryContentWKT = targetConceptionEntity.getGLGeometryContent();
            if(envelopeAreaWKT != null){
                this.geospatialScaleEntityMapInfoChart.renderEnvelope(getGeoJsonFromWKTContent(geometryCRSAID, envelopeAreaWKT));
            }
            if(centroidPointWKT != null){
                this.geospatialScaleEntityMapInfoChart.renderCentroidPoint(getGeoJsonFromWKTContent(geometryCRSAID, centroidPointWKT));
            }
            this.geospatialScaleEntityMapInfoChart.renderEntityContent(_WKTGeometryType,getGeoJsonFromWKTContent(geometryCRSAID, geometryContentWKT));
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupEntityNavigationBar(GeospatialScaleEntity targetGeospatialScaleEntity){
        String entityChineseName = targetGeospatialScaleEntity.getChineseName();
        this.navagationBarEntitieesMap.put(entityChineseName,targetGeospatialScaleEntity);
        GeospatialRegion.GeospatialScaleGrade entityGeospatialScaleGrade =  targetGeospatialScaleEntity.getGeospatialScaleGrade();
        switch(entityGeospatialScaleGrade){
            case CONTINENT :
                _CONTINENTButton.setText(entityChineseName);
                _CONTINENTButton.setVisible(true);
                break;
            case COUNTRY_REGION:
                divIcon0.setVisible(true);
                _COUNTRY_REGIONButton.setText(entityChineseName);
                _COUNTRY_REGIONButton.setVisible(true);
                break;
            case PROVINCE:
                divIcon1.setVisible(true);
                _PROVINCEButton.setText(entityChineseName);
                _PROVINCEButton.setVisible(true);
                break;
            case PREFECTURE:
                divIcon2.setVisible(true);
                _PREFECTUREButton.setText(entityChineseName);
                _PREFECTUREButton.setVisible(true);
                break;
            case COUNTY:
                divIcon3.setVisible(true);
                _COUNTYButton.setText(entityChineseName);
                _COUNTYButton.setVisible(true);
                break;
            case TOWNSHIP:
                divIcon4.setVisible(true);
                _TOWNSHIPButton.setText(entityChineseName);
                _TOWNSHIPButton.setVisible(true);
                break;
            case VILLAGE:
                divIcon5.setVisible(true);
                _VILLAGEButton.setText(entityChineseName);
                _VILLAGEButton.setVisible(true);
                break;
        }
        GeospatialScaleEntity parentGeospatialScaleEntity = targetGeospatialScaleEntity.getParentEntity();
        if(parentGeospatialScaleEntity != null){
            setupEntityNavigationBar(parentGeospatialScaleEntity);
        }
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
        this.geospatialScaleEntityMapInfoChart.setHeight(this.viewHeight-128,Unit.PIXELS);
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

    private String getGeoJsonFromWKTContent(String geometryCRSAID,String wktContent){
        String geoJsonContent = GeospatialCalculateUtil.getGeoJsonFromWTK(wktContent);
        if(geoJsonContent != null){
            String resultGeoJson ="{\"type\": \"FeatureCollection\",\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \""+geometryCRSAID+"\" } },\"features\": [{ \"type\": \"Feature\", \"geometry\": "+ geoJsonContent+" }]}";
            return resultGeoJson;
        }
        return null;
    }
}
