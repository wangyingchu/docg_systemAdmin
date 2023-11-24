package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialRegionRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialRegionSummaryStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Route("timeFlowDetailInfo/:geospatialRegion")
public class GeospatialRegionDetailUI extends VerticalLayout implements
        BeforeEnterObserver {
    private String geospatialRegionName;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout middleContainerLayout;
    private HorizontalLayout rightSideContainerLayout;
    private int contentContainerHeightOffset;
    private Registration listener;
    private SecondaryKeyValueDisplayItem totalGeospatialScaleEntityCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalGeospatialScaleEventCountDisplayItem;
    private SecondaryKeyValueDisplayItem continentEntityCountItem;
    private SecondaryKeyValueDisplayItem continentEventCountItem;
    private SecondaryKeyValueDisplayItem countryRegionEntityCountItem;
    private SecondaryKeyValueDisplayItem countryRegionEventCountItem;
    private SecondaryKeyValueDisplayItem provinceEntityCountItem;
    private SecondaryKeyValueDisplayItem provinceEventCountItem;
    private SecondaryKeyValueDisplayItem prefectureEntityCountItem;
    private SecondaryKeyValueDisplayItem prefectureEventCountItem;
    private SecondaryKeyValueDisplayItem countyEntityCountItem;
    private SecondaryKeyValueDisplayItem countyEventCountItem;
    private SecondaryKeyValueDisplayItem townshipEntityCountItem;
    private SecondaryKeyValueDisplayItem townshipEventCountItem;
    private SecondaryKeyValueDisplayItem villageEntityCountItem;
    private SecondaryKeyValueDisplayItem villageEventCountItem;
    private ComboBox<String> continentValueTextField;
    private ComboBox<String> countryRegionTextField;
    private ComboBox<String> provinceValueTextField;
    private ComboBox<String> prefectureValueTextField;
    private ComboBox<String> countyValueTextField;
    private ComboBox<String> townshipValueTextField;
    private ComboBox<String> villageValueTextField;
    private boolean timeFlowRuntimeStatisticsQueried;
    private NumberFormat numberFormat;
    private Grid<GeospatialScaleEntity> geospatialScaleEntitiesGrid;
    private NativeLabel resultNumberValue;
    private RadioButtonGroup<String> geospatialPropertyRadioGroup;
    private List<String> continentEntityCodeList;
    private List<String> continentEntityCNameList;
    private List<String> continentEntityENameList;
    private GeospatialRegion.GeospatialProperty currentGeospatialProperty;
    private Button querySelectedContinentEntitiesButton;
    private Button querySelectedCountryRegionEntitiesButton;
    private Button querySelectedProvinceEntitiesButton;
    private Button querySelectedPrefectureEntitiesButton;
    private Button querySelectedCountyEntitiesButton;
    private Button querySelectedTownshipEntitiesButton;
    private Button querySelectedVillageEntitiesButton;
    public GeospatialRegionDetailUI(){
        this.contentContainerHeightOffset = 265;
        this.numberFormat = NumberFormat.getInstance();
    }

    public GeospatialRegionDetailUI(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.contentContainerHeightOffset = 265;
        this.numberFormat = NumberFormat.getInstance();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.geospatialRegionName = beforeEnterEvent.getRouteParameters().get("geospatialRegion").get();
        this.contentContainerHeightOffset = 30;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderGeospatialRegionData();
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            leftSideContainerLayout.setHeight(event.getHeight() - contentContainerHeightOffset, Unit.PIXELS);
            middleContainerLayout.setHeight(event.getHeight() - contentContainerHeightOffset,Unit.PIXELS);
            rightSideContainerLayout.setHeight(event.getHeight() - contentContainerHeightOffset,Unit.PIXELS);
            //timeFlowCorrelationExploreView.setViewWidth(event.getWidth() - 600);
            //timeFlowCorrelationExploreView.setViewHeight(event.getHeight() - contentContainerHeightOffset);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            leftSideContainerLayout.setHeight(browserHeight - contentContainerHeightOffset,Unit.PIXELS);
            middleContainerLayout.setHeight(browserHeight - contentContainerHeightOffset,Unit.PIXELS);
            rightSideContainerLayout.setHeight(browserHeight - contentContainerHeightOffset,Unit.PIXELS);
            //timeFlowCorrelationExploreView.setViewWidth(browserWidth - 600);
            //timeFlowCorrelationExploreView.setViewHeight(browserHeight - contentContainerHeightOffset);
        }));
        renderGeospatialRegionBasicInfo();
        //ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderGeospatialRegionData(){
        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(410, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");

        VerticalLayout geospatialRegionInformationLayout = new VerticalLayout();
        geospatialRegionInformationLayout.setMargin(false);
        geospatialRegionInformationLayout.setPadding(false);

        leftSideContainerLayout.add(geospatialRegionInformationLayout);
        VerticalScrollLayout leftSideSectionContainerScrollLayout = new VerticalScrollLayout();
        leftSideContainerLayout.add(leftSideSectionContainerScrollLayout);

        MenuBar geospatialRegionOperationMenuBar = new MenuBar();
        geospatialRegionOperationMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem geospatialRegionOperationMenu = createIconItem(geospatialRegionOperationMenuBar, VaadinIcon.COG, "配置", null);
        geospatialRegionOperationMenu.getStyle().set("font-size","0.7rem");
        Icon downArrowIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon.setSize("10px");
        geospatialRegionOperationMenu.add(downArrowIcon);

        SubMenu operationSubItems = geospatialRegionOperationMenu.getSubMenu();

        HorizontalLayout action1Layout = new HorizontalLayout();
        action1Layout.setPadding(false);
        action1Layout.setSpacing(false);
        action1Layout.setMargin(false);
        action1Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon action1Icon = VaadinIcon.ROCKET.create();
        action1Icon.setSize("10px");
        Span action1Space = new Span();
        action1Space.setWidth(6,Unit.PIXELS);
        NativeLabel action1Label = new NativeLabel("生成检索加速索引");
        action1Label.addClassNames("text-xs","font-semibold","text-secondary");
        action1Layout.add(action1Icon,action1Space,action1Label);
        MenuItem generateIndexesActionItem = operationSubItems.addItem(action1Layout);
        generateIndexesActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                //renderGenerateTimeFlowIndexesUI();
            }
        });

        List<Component> actionComponentsList = new ArrayList<>();
        actionComponentsList.add(geospatialRegionOperationMenuBar);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.LAPTOP),"地理空间区域概览",null,actionComponentsList);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        geospatialRegionInformationLayout.add(secondaryTitleActionBar);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        totalGeospatialScaleEntityCountDisplayItem = new SecondaryKeyValueDisplayItem(horizontalLayout, FontAwesome.Solid.MAP.create(),"GeospatialScaleEntity 数量:","-");
        geospatialRegionInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        totalGeospatialScaleEventCountDisplayItem = new SecondaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.MAP_LOCATION.create(),"GeospatialScaleEvent 数量:","-");
        geospatialRegionInformationLayout.add(horizontalLayout2);

        VerticalLayout geospatialRegionInfoWallContainerLayout = new VerticalLayout();
        geospatialRegionInfoWallContainerLayout.setMargin(false);
        geospatialRegionInfoWallContainerLayout.setPadding(false);
        geospatialRegionInfoWallContainerLayout.setSpacing(false);
        geospatialRegionInfoWallContainerLayout.setWidth(95,Unit.PERCENTAGE);
        geospatialRegionInformationLayout.add(geospatialRegionInfoWallContainerLayout);

        HorizontalLayout continentEntitiesInfoLayout = new HorizontalLayout();
        continentEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel continentEntitiesLabel = new NativeLabel("Continent Entities:");
        continentEntityCountItem = new SecondaryKeyValueDisplayItem(continentEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        continentEventCountItem = new SecondaryKeyValueDisplayItem(continentEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon continentInfoTitleIcon = LineAwesomeIconsSvg.GLOBE_AFRICA_SOLID.create();
        continentInfoTitleIcon.setSize("10px");
        SectionWallTitle yearInfoSectionWallTitle = new SectionWallTitle(continentInfoTitleIcon,continentEntitiesLabel);
        SectionWallContainer continentInfoSectionWallContainer = new SectionWallContainer(yearInfoSectionWallTitle,continentEntitiesInfoLayout);
        continentInfoSectionWallContainer.setOpened(false);
        continentInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(continentInfoSectionWallContainer);

        HorizontalLayout countryRegionEntitiesInfoLayout = new HorizontalLayout();
        countryRegionEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel countryRegionEntitiesLabel = new NativeLabel("Country_Region Entities:");
        countryRegionEntityCountItem = new SecondaryKeyValueDisplayItem(countryRegionEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        countryRegionEventCountItem = new SecondaryKeyValueDisplayItem(countryRegionEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon countryRegionInfoTitleIcon = new Icon(VaadinIcon.INSTITUTION);
        countryRegionInfoTitleIcon.setSize("10px");
        SectionWallTitle countryRegionInfoSectionWallTitle = new SectionWallTitle(countryRegionInfoTitleIcon,countryRegionEntitiesLabel);
        SectionWallContainer countryRegionInfoSectionWallContainer = new SectionWallContainer(countryRegionInfoSectionWallTitle,countryRegionEntitiesInfoLayout);
        countryRegionInfoSectionWallContainer.setOpened(false);
        countryRegionInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(countryRegionInfoSectionWallContainer);

        HorizontalLayout provinceEntitiesInfoLayout = new HorizontalLayout();
        provinceEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel provinceEntitiesLabel = new NativeLabel("Province Entities:");
        provinceEntityCountItem = new SecondaryKeyValueDisplayItem(provinceEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        provinceEventCountItem = new SecondaryKeyValueDisplayItem(provinceEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon provinceInfoTitleIcon = new Icon(VaadinIcon.OFFICE);
        provinceInfoTitleIcon.setSize("10px");
        SectionWallTitle provinceInfoSectionWallTitle = new SectionWallTitle(provinceInfoTitleIcon,provinceEntitiesLabel);
        SectionWallContainer provinceInfoSectionWallContainer = new SectionWallContainer(provinceInfoSectionWallTitle,provinceEntitiesInfoLayout);
        provinceInfoSectionWallContainer.setOpened(false);
        provinceInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(provinceInfoSectionWallContainer);

        HorizontalLayout prefectureEntitiesInfoLayout = new HorizontalLayout();
        prefectureEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel prefectureEntitiesLabel = new NativeLabel("Prefecture Entities:");
        prefectureEntityCountItem = new SecondaryKeyValueDisplayItem(prefectureEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        prefectureEventCountItem = new SecondaryKeyValueDisplayItem(prefectureEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon prefectureInfoTitleIcon = new Icon(VaadinIcon.BUILDING);
        prefectureInfoTitleIcon.setSize("10px");
        SectionWallTitle prefectureInfoSectionWallTitle = new SectionWallTitle(prefectureInfoTitleIcon,prefectureEntitiesLabel);
        SectionWallContainer prefectureInfoSectionWallContainer = new SectionWallContainer(prefectureInfoSectionWallTitle,prefectureEntitiesInfoLayout);
        prefectureInfoSectionWallContainer.setOpened(false);
        prefectureInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(prefectureInfoSectionWallContainer);

        HorizontalLayout countyEntitiesInfoLayout = new HorizontalLayout();
        countyEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel countyEntitiesLabel = new NativeLabel("County Entities:");
        countyEntityCountItem = new SecondaryKeyValueDisplayItem(countyEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        countyEventCountItem = new SecondaryKeyValueDisplayItem(countyEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon countyInfoTitleIcon = new Icon(VaadinIcon.FACTORY);
        countyInfoTitleIcon.setSize("10px");
        SectionWallTitle countyInfoSectionWallTitle = new SectionWallTitle(countyInfoTitleIcon,countyEntitiesLabel);
        SectionWallContainer countyInfoSectionWallContainer = new SectionWallContainer(countyInfoSectionWallTitle,countyEntitiesInfoLayout);
        countyInfoSectionWallContainer.setOpened(false);
        countyInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(countyInfoSectionWallContainer);

        HorizontalLayout townshipEntitiesInfoLayout = new HorizontalLayout();
        townshipEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel townshipEntitiesLabel = new NativeLabel("Township Entities:");
        townshipEntityCountItem = new SecondaryKeyValueDisplayItem(townshipEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        townshipEventCountItem = new SecondaryKeyValueDisplayItem(townshipEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon townshipInfoTitleIcon = new Icon(VaadinIcon.SHOP);
        townshipInfoTitleIcon.setSize("10px");
        SectionWallTitle townshipInfoSectionWallTitle = new SectionWallTitle(townshipInfoTitleIcon,townshipEntitiesLabel);
        SectionWallContainer townshipInfoSectionWallContainer = new SectionWallContainer(townshipInfoSectionWallTitle,townshipEntitiesInfoLayout);
        townshipInfoSectionWallContainer.setOpened(false);
        townshipInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(townshipInfoSectionWallContainer);

        HorizontalLayout villageEntitiesInfoLayout = new HorizontalLayout();
        villageEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel villageEntitiesLabel = new NativeLabel("Village Entities:");
        villageEntityCountItem = new SecondaryKeyValueDisplayItem(villageEntitiesInfoLayout,FontAwesome.Solid.MAP.create(),"","-");
        villageEventCountItem = new SecondaryKeyValueDisplayItem(villageEntitiesInfoLayout,FontAwesome.Solid.MAP_LOCATION.create(),"","-");
        Icon villageInfoTitleIcon = new Icon(VaadinIcon.HOME);
        villageInfoTitleIcon.setSize("10px");
        SectionWallTitle villageInfoSectionWallTitle = new SectionWallTitle(villageInfoTitleIcon,villageEntitiesLabel);
        SectionWallContainer villageInfoSectionWallContainer = new SectionWallContainer(villageInfoSectionWallTitle,villageEntitiesInfoLayout);
        villageInfoSectionWallContainer.setOpened(false);
        villageInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                setupTimeFlowRuntimeStatisticInfo();
            }
        });
        geospatialRegionInfoWallContainerLayout.add(villageInfoSectionWallContainer);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"地理空间区域尺度实体检索");
        leftSideSectionContainerScrollLayout.add(filterTitle2);

        HorizontalLayout heightSpaceDiv06 = new HorizontalLayout();
        heightSpaceDiv06.setWidth(95,Unit.PERCENTAGE);
        leftSideSectionContainerScrollLayout.add(heightSpaceDiv06);
        heightSpaceDiv06.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        HorizontalLayout checkBoxesContainer1 = new HorizontalLayout();
        checkBoxesContainer1.setWidth(280,Unit.PIXELS);
        checkBoxesContainer1.getStyle().set("padding-top", "var(--lumo-space-m)");
        leftSideSectionContainerScrollLayout.add(checkBoxesContainer1);

        NativeLabel geospatialPropertyFilterText = new NativeLabel("检索属性:");
        geospatialPropertyFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(geospatialPropertyFilterText);
        checkBoxesContainer1.setVerticalComponentAlignment(Alignment.CENTER,geospatialPropertyFilterText);

        geospatialPropertyRadioGroup = new RadioButtonGroup<>();
        //<theme-editor-local-classname>
        geospatialPropertyRadioGroup.addClassName("geospatial-region-detail-ui-radio-group-1");
        geospatialPropertyRadioGroup.setItems("地理空间编码", "中文名称", "英文名称");
        checkBoxesContainer1.add(geospatialPropertyRadioGroup);
        checkBoxesContainer1.setVerticalComponentAlignment(Alignment.CENTER,geospatialPropertyRadioGroup);
        geospatialPropertyRadioGroup.setValue("中文名称");
        currentGeospatialProperty = GeospatialRegion.GeospatialProperty.ChineseName;
        geospatialPropertyRadioGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> radioButtonGroupStringComponentValueChangeEvent) {
                String newValue = radioButtonGroupStringComponentValueChangeEvent.getValue();
                switchGeospatialRegionSearchElementsInfo(newValue);
            }
        });

        HorizontalLayout continentValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(continentValueContainer);
        NativeLabel continentFilterText = new NativeLabel("洲际 :");
        continentFilterText.setWidth(50,Unit.PIXELS);
        continentFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        continentValueContainer.add(continentFilterText);
        continentValueContainer.setVerticalComponentAlignment(Alignment.CENTER,continentFilterText);
        Button listContinentEntitiesButton = new Button();
        listContinentEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listContinentEntitiesButton.setTooltipText("检索全部洲际尺度实体");
        listContinentEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listContinentEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.CONTINENT);
            }
        });
        continentValueContainer.add(listContinentEntitiesButton);
        continentValueTextField = new ComboBox();
        continentValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        continentValueTextField.setWidth(180,Unit.PIXELS);
        continentValueContainer.add(continentValueTextField);
        continentValueTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
                if(continentValueTextField.getValue() != null){
                    querySelectedContinentEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.CONTINENT);
                }else{
                    querySelectedContinentEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer1 = new HorizontalLayout();
        buttonsGroupContainer1.setSpacing(false);
        buttonsGroupContainer1.setMargin(false);
        buttonsGroupContainer1.setPadding(false);
        continentValueTextField.setPrefixComponent(buttonsGroupContainer1);

        Button clearContinentEntitiesQueryInputButton = new Button();
        clearContinentEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearContinentEntitiesQueryInputButton.setTooltipText("清除洲际及下级尺度实体检索条件");
        clearContinentEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearContinentEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                continentValueTextField.clear();
                countryRegionTextField.clear();
                provinceValueTextField.clear();
                prefectureValueTextField.clear();
                countyValueTextField.clear();
                townshipValueTextField.clear();
                villageValueTextField.clear();

                countryRegionTextField.setEnabled(false);
                provinceValueTextField.setEnabled(false);
                prefectureValueTextField.setEnabled(false);
                countyValueTextField.setEnabled(false);
                townshipValueTextField.setEnabled(false);
                villageValueTextField.setEnabled(false);
            }
        });
        buttonsGroupContainer1.add(clearContinentEntitiesQueryInputButton);

        querySelectedContinentEntitiesButton = new Button();
        querySelectedContinentEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedContinentEntitiesButton.setTooltipText("显示当前选择洲际实体");
        querySelectedContinentEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer1.add(querySelectedContinentEntitiesButton);

        HorizontalLayout countryRegionValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(countryRegionValueContainer);
        NativeLabel countryRegionFilterText = new NativeLabel("国家地区 :");
        countryRegionFilterText.setWidth(50,Unit.PIXELS);
        countryRegionFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        countryRegionValueContainer.add(countryRegionFilterText);
        countryRegionValueContainer.setVerticalComponentAlignment(Alignment.CENTER,countryRegionFilterText);
        Button listCountryRegionEntitiesButton = new Button();
        listCountryRegionEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listCountryRegionEntitiesButton.setTooltipText("检索全部国家地区尺度实体");
        listCountryRegionEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listCountryRegionEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.COUNTRY_REGION);
            }
        });
        countryRegionValueContainer.add(listCountryRegionEntitiesButton);
        countryRegionTextField = new ComboBox();
        countryRegionTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        countryRegionTextField.setWidth(180,Unit.PIXELS);
        countryRegionValueContainer.add(countryRegionTextField);
        countryRegionTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
                if(countryRegionTextField.getValue() != null){
                    querySelectedCountryRegionEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.COUNTRY_REGION);
                }else{
                    querySelectedCountryRegionEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer2 = new HorizontalLayout();
        buttonsGroupContainer2.setSpacing(false);
        buttonsGroupContainer2.setMargin(false);
        buttonsGroupContainer2.setPadding(false);
        countryRegionTextField.setPrefixComponent(buttonsGroupContainer2);

        Button clearCountryRegionEntitiesQueryInputButton = new Button();
        clearCountryRegionEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearCountryRegionEntitiesQueryInputButton.setTooltipText("清除国家地区级及下级尺度实体检索条件");
        clearCountryRegionEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearCountryRegionEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                countryRegionTextField.clear();
                provinceValueTextField.clear();
                prefectureValueTextField.clear();
                countyValueTextField.clear();
                townshipValueTextField.clear();
                villageValueTextField.clear();

                provinceValueTextField.setEnabled(false);
                prefectureValueTextField.setEnabled(false);
                countyValueTextField.setEnabled(false);
                townshipValueTextField.setEnabled(false);
                villageValueTextField.setEnabled(false);
            }
        });
        buttonsGroupContainer2.add(clearCountryRegionEntitiesQueryInputButton);

        querySelectedCountryRegionEntitiesButton = new Button();
        querySelectedCountryRegionEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedCountryRegionEntitiesButton.setTooltipText("显示当前选择国家地区实体");
        querySelectedCountryRegionEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer2.add(querySelectedCountryRegionEntitiesButton);

        HorizontalLayout provinceValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(provinceValueContainer);
        NativeLabel provinceFilterText = new NativeLabel("省级 :");
        provinceFilterText.setWidth(50,Unit.PIXELS);
        provinceFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        provinceValueContainer.add(provinceFilterText);
        provinceValueContainer.setVerticalComponentAlignment(Alignment.CENTER,provinceFilterText);
        Button listProvinceEntitiesButton = new Button();
        listProvinceEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listProvinceEntitiesButton.setTooltipText("检索全部省级尺度实体");
        listProvinceEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listProvinceEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.PROVINCE);
            }
        });
        provinceValueContainer.add(listProvinceEntitiesButton);
        provinceValueTextField = new ComboBox();
        provinceValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        provinceValueTextField.setWidth(180,Unit.PIXELS);
        provinceValueContainer.add(provinceValueTextField);
        provinceValueTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {

                if(provinceValueTextField.getValue() != null){
                    querySelectedProvinceEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.PROVINCE);
                }else{
                    querySelectedProvinceEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer3 = new HorizontalLayout();
        buttonsGroupContainer3.setSpacing(false);
        buttonsGroupContainer3.setMargin(false);
        buttonsGroupContainer3.setPadding(false);
        provinceValueTextField.setPrefixComponent(buttonsGroupContainer3);

        Button clearProvinceEntitiesQueryInputButton = new Button();
        clearProvinceEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearProvinceEntitiesQueryInputButton.setTooltipText("清除省级及下级尺度实体检索条件");
        clearProvinceEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearProvinceEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                provinceValueTextField.clear();
                prefectureValueTextField.clear();
                countyValueTextField.clear();
                townshipValueTextField.clear();
                villageValueTextField.clear();

                prefectureValueTextField.setEnabled(false);
                countyValueTextField.setEnabled(false);
                townshipValueTextField.setEnabled(false);
                villageValueTextField.setEnabled(false);
            }
        });
        buttonsGroupContainer3.add(clearProvinceEntitiesQueryInputButton);

        querySelectedProvinceEntitiesButton = new Button();
        querySelectedProvinceEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedProvinceEntitiesButton.setTooltipText("显示当前选择省级实体");
        querySelectedProvinceEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer3.add(querySelectedProvinceEntitiesButton);

        HorizontalLayout prefectureValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(prefectureValueContainer);
        NativeLabel prefectureFilterText = new NativeLabel("地级 :");
        prefectureFilterText.setWidth(50,Unit.PIXELS);
        prefectureFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        prefectureValueContainer.add(prefectureFilterText);
        prefectureValueContainer.setVerticalComponentAlignment(Alignment.CENTER,prefectureFilterText);

        Button listPrefectureEntitiesButton = new Button();
        listPrefectureEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listPrefectureEntitiesButton.setTooltipText("检索全部地级尺度实体");
        listPrefectureEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listPrefectureEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.PREFECTURE);
            }
        });
        prefectureValueContainer.add(listPrefectureEntitiesButton);

        prefectureValueTextField = new ComboBox();
        prefectureValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        prefectureValueTextField.setWidth(180,Unit.PIXELS);
        prefectureValueContainer.add(prefectureValueTextField);
        prefectureValueTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
                if(prefectureValueTextField.getValue() != null){
                    querySelectedPrefectureEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.PREFECTURE);
                }else{
                    querySelectedPrefectureEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer4 = new HorizontalLayout();
        buttonsGroupContainer4.setSpacing(false);
        buttonsGroupContainer4.setMargin(false);
        buttonsGroupContainer4.setPadding(false);
        prefectureValueTextField.setPrefixComponent(buttonsGroupContainer4);

        Button clearPrefectureEntitiesQueryInputButton = new Button();
        clearPrefectureEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearPrefectureEntitiesQueryInputButton.setTooltipText("清除地级及下级尺度实体检索条件");
        clearPrefectureEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearPrefectureEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                prefectureValueTextField.clear();
                countyValueTextField.clear();
                townshipValueTextField.clear();
                villageValueTextField.clear();

                countyValueTextField.setEnabled(false);
                townshipValueTextField.setEnabled(false);
                villageValueTextField.setEnabled(false);
            }
        });
        buttonsGroupContainer4.add(clearPrefectureEntitiesQueryInputButton);

        querySelectedPrefectureEntitiesButton = new Button();
        querySelectedPrefectureEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedPrefectureEntitiesButton.setTooltipText("显示当前选择地级实体");
        querySelectedPrefectureEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer4.add(querySelectedPrefectureEntitiesButton);

        HorizontalLayout countyValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(countyValueContainer);
        NativeLabel countyFilterText = new NativeLabel("县级 :");
        countyFilterText.setWidth(50,Unit.PIXELS);
        countyFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        countyValueContainer.add(countyFilterText);
        countyValueContainer.setVerticalComponentAlignment(Alignment.CENTER,countyFilterText);
        Button listCountyEntitiesButton = new Button();
        listCountyEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listCountyEntitiesButton.setTooltipText("检索全部县级尺度实体");
        listCountyEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listCountyEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.COUNTY);
            }
        });
        countyValueContainer.add(listCountyEntitiesButton);
        countyValueTextField = new ComboBox();
        countyValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        countyValueTextField.setWidth(180,Unit.PIXELS);
        countyValueContainer.add(countyValueTextField);
        countyValueTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
                if(countyValueTextField.getValue() != null){
                    querySelectedCountyEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.COUNTY);
                }else{
                    querySelectedCountyEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer5 = new HorizontalLayout();
        buttonsGroupContainer5.setSpacing(false);
        buttonsGroupContainer5.setMargin(false);
        buttonsGroupContainer5.setPadding(false);
        countyValueTextField.setPrefixComponent(buttonsGroupContainer5);

        Button clearCountyEntitiesQueryInputButton = new Button();
        clearCountyEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearCountyEntitiesQueryInputButton.setTooltipText("清除县级及下级尺度实体检索条件");
        clearCountyEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearCountyEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                countyValueTextField.clear();
                townshipValueTextField.clear();
                villageValueTextField.clear();

                townshipValueTextField.setEnabled(false);
                villageValueTextField.setEnabled(false);
            }
        });
        buttonsGroupContainer5.add(clearCountyEntitiesQueryInputButton);

        querySelectedCountyEntitiesButton = new Button();
        querySelectedCountyEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedCountyEntitiesButton.setTooltipText("显示当前选择县级实体");
        querySelectedCountyEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer5.add(querySelectedCountyEntitiesButton);

        HorizontalLayout townshipValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(townshipValueContainer);
        NativeLabel townshipFilterText = new NativeLabel("乡级 :");
        townshipFilterText.setWidth(50,Unit.PIXELS);
        townshipFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        townshipValueContainer.add(townshipFilterText);
        townshipValueContainer.setVerticalComponentAlignment(Alignment.CENTER,townshipFilterText);
        Button listTownshipEntitiesButton = new Button();
        listTownshipEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listTownshipEntitiesButton.setTooltipText("检索全部乡级尺度实体");
        listTownshipEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listTownshipEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.TOWNSHIP);
            }
        });
        townshipValueContainer.add(listTownshipEntitiesButton);
        townshipValueTextField = new ComboBox();
        townshipValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        townshipValueTextField.setWidth(180,Unit.PIXELS);
        townshipValueContainer.add(townshipValueTextField);
        townshipValueTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
                if(townshipValueTextField.getValue() != null){
                    querySelectedTownshipEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.TOWNSHIP);
                }else{
                    querySelectedTownshipEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer6 = new HorizontalLayout();
        buttonsGroupContainer6.setSpacing(false);
        buttonsGroupContainer6.setMargin(false);
        buttonsGroupContainer6.setPadding(false);
        townshipValueTextField.setPrefixComponent(buttonsGroupContainer6);

        Button clearTownshipEntitiesQueryInputButton = new Button();
        clearTownshipEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearTownshipEntitiesQueryInputButton.setTooltipText("清除乡级及下级尺度实体检索条件");
        clearTownshipEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearTownshipEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                townshipValueTextField.clear();
                villageValueTextField.clear();

                villageValueTextField.setEnabled(false);
            }
        });
        buttonsGroupContainer6.add(clearTownshipEntitiesQueryInputButton);

        querySelectedTownshipEntitiesButton = new Button();
        querySelectedTownshipEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedTownshipEntitiesButton.setTooltipText("显示当前选择乡级实体");
        querySelectedTownshipEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer6.add(querySelectedTownshipEntitiesButton);

        HorizontalLayout villageValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(villageValueContainer);
        NativeLabel villageFilterText = new NativeLabel("村级 :");
        villageFilterText.setWidth(50,Unit.PIXELS);
        villageFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        villageValueContainer.add(villageFilterText);
        villageValueContainer.setVerticalComponentAlignment(Alignment.CENTER,villageFilterText);
        Button listVillageEntitiesButton = new Button();
        listVillageEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        listVillageEntitiesButton.setTooltipText("检索全部村级尺度实体");
        listVillageEntitiesButton.setIcon(VaadinIcon.BULLETS.create());
        listVillageEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade.VILLAGE);
            }
        });
        villageValueContainer.add(listVillageEntitiesButton);
        villageValueTextField = new ComboBox();
        villageValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        villageValueTextField.setWidth(180,Unit.PIXELS);
        villageValueContainer.add(villageValueTextField);
        villageValueTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
                if(villageValueTextField.getValue() != null){
                    querySelectedVillageEntitiesButton.setEnabled(true);
                    setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade.VILLAGE);
                }else{
                    querySelectedVillageEntitiesButton.setEnabled(false);
                }
            }
        });

        HorizontalLayout buttonsGroupContainer7 = new HorizontalLayout();
        buttonsGroupContainer7.setSpacing(false);
        buttonsGroupContainer7.setMargin(false);
        buttonsGroupContainer7.setPadding(false);
        villageValueTextField.setPrefixComponent(buttonsGroupContainer7);

        Button clearVillageEntitiesQueryInputButton = new Button();
        clearVillageEntitiesQueryInputButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        clearVillageEntitiesQueryInputButton.setTooltipText("清除村级尺度实体检索条件");
        clearVillageEntitiesQueryInputButton.setIcon(VaadinIcon.REFRESH.create());
        clearVillageEntitiesQueryInputButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                villageValueTextField.clear();
            }
        });
        buttonsGroupContainer7.add(clearVillageEntitiesQueryInputButton);

        querySelectedVillageEntitiesButton = new Button();
        querySelectedVillageEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        querySelectedVillageEntitiesButton.setTooltipText("显示当前选择村级实体");
        querySelectedVillageEntitiesButton.setIcon(FontAwesome.Solid.MAP.create());
        buttonsGroupContainer7.add(querySelectedVillageEntitiesButton);

        HorizontalLayout heightSpaceDiv2 = new HorizontalLayout();
        heightSpaceDiv2.setWidth(10,Unit.PIXELS);
        heightSpaceDiv2.setHeight(5,Unit.PIXELS);
        leftSideSectionContainerScrollLayout.add(heightSpaceDiv2);

        Button executeQueryButton = new Button("检索地理空间区域尺度实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //queryTimeScaleEntities();
            }
        });
        leftSideSectionContainerScrollLayout.add(executeQueryButton);

        countryRegionTextField.setEnabled(false);
        provinceValueTextField.setEnabled(false);
        prefectureValueTextField.setEnabled(false);
        countyValueTextField.setEnabled(false);
        townshipValueTextField.setEnabled(false);
        villageValueTextField.setEnabled(false);

        querySelectedContinentEntitiesButton.setEnabled(false);
        querySelectedCountryRegionEntitiesButton.setEnabled(false);
        querySelectedProvinceEntitiesButton.setEnabled(false);
        querySelectedPrefectureEntitiesButton.setEnabled(false);
        querySelectedCountyEntitiesButton.setEnabled(false);
        querySelectedTownshipEntitiesButton.setEnabled(false);
        querySelectedVillageEntitiesButton.setEnabled(false);

        middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);

        mainContainerLayout.add(middleContainerLayout);
        middleContainerLayout.setWidth(510, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");

        resultNumberValue = new NativeLabel("");
        resultNumberValue.addClassNames("text-xs","font-bold");
        resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterTitle3 = new SecondaryIconTitle(FontAwesome.Solid.MAP.create(),"地理空间尺度实体检索结果",resultNumberValue);
        filterTitle3.getStyle().set("padding-left","10px");
        middleContainerLayout.add(filterTitle3);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(geospatialScaleEntity -> {
            Icon eyeIcon = new Icon(VaadinIcon.EYE);
            eyeIcon.setSize("20px");
            Button timeScaleEntityDetailButton = new Button(eyeIcon, event -> {
                if(geospatialScaleEntity instanceof GeospatialScaleEntity){
                    renderGeospatialScaleEntityDetailUI((GeospatialScaleEntity)geospatialScaleEntity);
                }
            });
            timeScaleEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            timeScaleEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            timeScaleEntityDetailButton.setTooltipText("显示地理空间区域尺度实体详情");

            HorizontalLayout buttons = new HorizontalLayout(timeScaleEntityDetailButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        geospatialScaleEntitiesGrid = new Grid<>();

        geospatialScaleEntitiesGrid.setWidth(500,Unit.PIXELS);
        geospatialScaleEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        geospatialScaleEntitiesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
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


        rightSideContainerLayout = new HorizontalLayout();
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);

        mainContainerLayout.add(rightSideContainerLayout);
        rightSideContainerLayout.setWidthFull();
        rightSideContainerLayout.setHeight(600,Unit.PIXELS);
    }

    private void renderGeospatialRegionBasicInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        GeospatialRegion geospatialRegion = coreRealm.getOrCreateGeospatialRegion(this.geospatialRegionName);
        GeospatialRegionSummaryStatistics geospatialRegionSummaryStatistics = geospatialRegion.getGeospatialRegionSummaryStatistics();
        totalGeospatialScaleEntityCountDisplayItem.updateDisplayValue(this.numberFormat.format(geospatialRegionSummaryStatistics.getContainsTotalGeospatialScaleEntityCount()));
        totalGeospatialScaleEventCountDisplayItem.updateDisplayValue(this.numberFormat.format(geospatialRegionSummaryStatistics.getRefersTotalGeospatialScaleEventCount()));

        List<GeospatialScaleEntity> allContinentEntityList = geospatialRegion.listContinentEntities();
        continentEntityCodeList = new ArrayList<>();
        continentEntityCNameList = new ArrayList<>();
        continentEntityENameList = new ArrayList<>();

        for(GeospatialScaleEntity currentGeospatialScaleEntity : allContinentEntityList){
            continentEntityCodeList.add(currentGeospatialScaleEntity.getGeospatialCode());
            continentEntityCNameList.add(currentGeospatialScaleEntity.getChineseName());
            continentEntityENameList.add(currentGeospatialScaleEntity.getEnglishName());
        }

        String filterPropertyName = geospatialPropertyRadioGroup.getValue();
        if("地理空间编码".equals(filterPropertyName)){
            continentValueTextField.setItems(continentEntityCodeList);
        }else if ("中文名称".equals(filterPropertyName)) {
            continentValueTextField.setItems(continentEntityCNameList);
        }else if ("英文名称".equals(filterPropertyName)) {
            continentValueTextField.setItems(continentEntityENameList);
        }
    }

    private void switchGeospatialRegionSearchElementsInfo(String propertyName){
        continentValueTextField.clear();
        continentValueTextField.setItems(new ArrayList<>());

        countryRegionTextField.setEnabled(false);
        provinceValueTextField.setEnabled(false);
        prefectureValueTextField.setEnabled(false);
        countyValueTextField.setEnabled(false);
        townshipValueTextField.setEnabled(false);
        villageValueTextField.setEnabled(false);

        if("地理空间编码".equals(propertyName)){
            continentValueTextField.setItems(continentEntityCodeList);
            currentGeospatialProperty = GeospatialRegion.GeospatialProperty.GeospatialCode;
        }else if ("中文名称".equals(propertyName)) {
            continentValueTextField.setItems(continentEntityCNameList);
            currentGeospatialProperty = GeospatialRegion.GeospatialProperty.ChineseName;
        }else if ("英文名称".equals(propertyName)) {
            continentValueTextField.setItems(continentEntityENameList);
            currentGeospatialProperty = GeospatialRegion.GeospatialProperty.EnglishName;
        }
    }

    private void setupGeospatialScaleQueryWElements(GeospatialRegion.GeospatialScaleGrade geospatialScaleGrade){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        GeospatialRegion geospatialRegion = coreRealm.getOrCreateGeospatialRegion(this.geospatialRegionName);
        String currentContinentValue;
        String currentCountryRegionValue;
        String currentProvinceValue;
        String currentPrefectureValue;
        String currentCountyValue;
        String currentTownshipValue;
        try {
            switch(geospatialScaleGrade){
                case CONTINENT:
                    currentContinentValue = continentValueTextField.getValue();
                    List<String> currentCountryRegionValueSelect =
                            getPropertyValueList(geospatialRegion.listCountryRegionEntities(currentGeospatialProperty,currentContinentValue,null));
                    countryRegionTextField.setItems(currentCountryRegionValueSelect);
                    countryRegionTextField.setEnabled(true);
                    break;
                case COUNTRY_REGION:
                    currentCountryRegionValue = countryRegionTextField.getValue();
                    List<String> currentProvinceValueSelect = getPropertyValueList(geospatialRegion.listProvinceEntities(currentGeospatialProperty,currentCountryRegionValue,null));
                    provinceValueTextField.setItems(currentProvinceValueSelect);
                    provinceValueTextField.setEnabled(true);
                    break;
                case PROVINCE:
                    currentCountryRegionValue = countryRegionTextField.getValue();
                    currentProvinceValue = provinceValueTextField.getValue();
                    List<String> currentPrefectureValueSelect = getPropertyValueList(geospatialRegion.listPrefectureEntities(currentGeospatialProperty,currentCountryRegionValue,currentProvinceValue,null));
                    prefectureValueTextField.setItems(currentPrefectureValueSelect);
                    prefectureValueTextField.setEnabled(true);
                    break;
                case PREFECTURE:
                    currentCountryRegionValue = countryRegionTextField.getValue();
                    currentProvinceValue = provinceValueTextField.getValue();
                    currentPrefectureValue = prefectureValueTextField.getValue();
                    List<String> currentCountyValueSelect = getPropertyValueList(geospatialRegion.listCountyEntities(currentGeospatialProperty,currentCountryRegionValue,currentProvinceValue,currentPrefectureValue,null));
                    countyValueTextField.setItems(currentCountyValueSelect);
                    countyValueTextField.setEnabled(true);
                    break;
                case COUNTY:
                    currentCountryRegionValue = countryRegionTextField.getValue();
                    currentProvinceValue = provinceValueTextField.getValue();
                    currentPrefectureValue = prefectureValueTextField.getValue();
                    currentCountyValue = countyValueTextField.getValue();
                    List<String> currentTownshipValueSelect = getPropertyValueList(geospatialRegion.listTownshipEntities(currentGeospatialProperty,currentCountryRegionValue,currentProvinceValue,currentPrefectureValue,currentCountyValue,null));
                    townshipValueTextField.setItems(currentTownshipValueSelect);
                    townshipValueTextField.setEnabled(true);
                    break;
                case TOWNSHIP:
                    currentCountryRegionValue = countryRegionTextField.getValue();
                    currentProvinceValue = provinceValueTextField.getValue();
                    currentPrefectureValue = prefectureValueTextField.getValue();
                    currentCountyValue = countyValueTextField.getValue();
                    currentTownshipValue = townshipValueTextField.getValue();
                    List<String> currentVillageValueSelect = getPropertyValueList(geospatialRegion.listVillageEntities(currentGeospatialProperty,currentCountryRegionValue,currentProvinceValue,currentPrefectureValue,currentCountyValue,currentTownshipValue,null));
                    villageValueTextField.setItems(currentVillageValueSelect);
                    villageValueTextField.setEnabled(true);
                    break;
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getPropertyValueList(List<GeospatialScaleEntity> currentCountryRegionList){
        List<String> resultValueList = new ArrayList<>();
        if(currentCountryRegionList != null){
            for(GeospatialScaleEntity currentGeospatialScaleEntity:currentCountryRegionList){
                switch (currentGeospatialProperty){
                    case GeospatialCode -> resultValueList.add(currentGeospatialScaleEntity.getGeospatialCode());
                    case ChineseName -> resultValueList.add(currentGeospatialScaleEntity.getChineseName());
                    case EnglishName -> resultValueList.add(currentGeospatialScaleEntity.getEnglishName());
                }
            }
        }
        return resultValueList;
    }

    private void setupTimeFlowRuntimeStatisticInfo(){
        if(!timeFlowRuntimeStatisticsQueried){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            GeospatialRegion geospatialRegion = coreRealm.getOrCreateGeospatialRegion(this.geospatialRegionName);
            GeospatialRegionRuntimeStatistics geospatialRegionRuntimeStatistics = geospatialRegion.getGeospatialRegionRuntimeStatistics();
            timeFlowRuntimeStatisticsQueried = true;

            continentEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsContinentScaleTimeScaleEntityCount()));
            continentEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersContinentScaleTimeScaleEventCount()));
            countryRegionEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsCountry_RegionScaleTimeScaleEntityCount()));
            countryRegionEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersCountry_RegionScaleTimeScaleEventCount()));
            provinceEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsProvinceScaleTimeScaleEntityCount()));
            provinceEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersProvinceScaleTimeScaleEventCount()));
            prefectureEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsPrefectureScaleTimeScaleEntityCount()));
            prefectureEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersPrefectureScaleTimeScaleEventCount()));
            countyEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsCountyScaleTimeScaleEntityCount()));
            countyEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersCountyScaleTimeScaleEventCount()));
            townshipEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsTownshipScaleTimeScaleEntityCount()));
            townshipEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersTownshipScaleTimeScaleEventCount()));
            villageEntityCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getContainsVillageScaleTimeScaleEntityCount()));
            villageEventCountItem.updateDisplayValue(this.numberFormat.format(geospatialRegionRuntimeStatistics.getRefersVillageScaleTimeScaleEventCount()));
        }
    }

    private void renderGeospatialScaleEntityList(GeospatialRegion.GeospatialScaleGrade geospatialScaleGrade) {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        GeospatialRegion geospatialRegion = coreRealm.getOrCreateGeospatialRegion(this.geospatialRegionName);
        List<GeospatialScaleEntity> geospatialScaleEntityList = null;
        geospatialScaleEntitiesGrid.setItems(new ArrayList<>());
        try {
            switch (geospatialScaleGrade) {
                case CONTINENT:
                    geospatialScaleEntityList = geospatialRegion.listContinentEntities();
                    break;
                case COUNTRY_REGION:
                    geospatialScaleEntityList = geospatialRegion.listCountryRegionEntities(null,null);
                    break;
                case PROVINCE:
                    geospatialScaleEntityList = geospatialRegion.listProvinceEntities(null,null,null);
                    break;
                case PREFECTURE:
                    geospatialScaleEntityList = geospatialRegion.listPrefectureEntities(null,null,null,null);
                    break;
                case COUNTY:
                    geospatialScaleEntityList = geospatialRegion.listCountyEntities(null,null,null,null,null);
                    break;
                case TOWNSHIP:
                    geospatialScaleEntityList = geospatialRegion.listTownshipEntities(null,null,null,null,null,null);
                    break;
                case VILLAGE:
                    geospatialScaleEntityList = geospatialRegion.listVillageEntities(null,null,null,null,null,null,null);
                    break;
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
        geospatialScaleEntitiesGrid.setItems(geospatialScaleEntityList);
        CommonUIOperationUtil.showPopupNotification("地理空间区域粒度实体查询操作成功，查询返回实体数: "+geospatialScaleEntityList.size(),
                NotificationVariant.LUMO_SUCCESS,3000, Notification.Position.BOTTOM_START);
        resultNumberValue.setText("实体总量："+this.numberFormat.format(geospatialScaleEntityList.size()));
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
        MenuItem item = menu.addItem(icon, e -> {});
        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }
        if (label != null) {
            item.add(new Text(label));
        }
        return item;
    }
}
