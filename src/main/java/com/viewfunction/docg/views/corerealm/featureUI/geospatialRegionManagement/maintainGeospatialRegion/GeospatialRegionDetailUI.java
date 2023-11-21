package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialRegionRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialRegionSummaryStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

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
    private boolean timeFlowRuntimeStatisticsQueried;
    private NumberFormat numberFormat;

    public GeospatialRegionDetailUI(){
        this.contentContainerHeightOffset = 265;
    }

    public GeospatialRegionDetailUI(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        this.contentContainerHeightOffset = 265;
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
        checkBoxesContainer1.getStyle().set("padding-top", "var(--lumo-space-m)");
        leftSideSectionContainerScrollLayout.add(checkBoxesContainer1);

        NativeLabel geospatialPropertyFilterText = new NativeLabel("检索属性:");
        geospatialPropertyFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(geospatialPropertyFilterText);
        checkBoxesContainer1.setVerticalComponentAlignment(Alignment.CENTER,geospatialPropertyFilterText);

        RadioButtonGroup<String> geospatialPropertyRadioGroup = new RadioButtonGroup<>();
        //<theme-editor-local-classname>
        geospatialPropertyRadioGroup.addClassName("geospatial-region-detail-ui-radio-group-1");
        geospatialPropertyRadioGroup.setItems("地理空间编码", "中文名称", "英文名称");
        checkBoxesContainer1.add(geospatialPropertyRadioGroup);
        checkBoxesContainer1.setVerticalComponentAlignment(Alignment.CENTER,geospatialPropertyRadioGroup);
        geospatialPropertyRadioGroup.setValue("中文名称");
        geospatialPropertyRadioGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> radioButtonGroupStringComponentValueChangeEvent) {
                String newValue = radioButtonGroupStringComponentValueChangeEvent.getValue();
                //setupTimeScaleGradeSearchElements(newValue);
            }
        });

        HorizontalLayout continentValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(continentValueContainer);
        NativeLabel continentFilterText = new NativeLabel("洲际 :");
        continentFilterText.setWidth(50,Unit.PIXELS);
        continentFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        continentValueContainer.add(continentFilterText);
        continentValueContainer.setVerticalComponentAlignment(Alignment.CENTER,continentFilterText);
        TextField continentValueTextField = new TextField();
        continentValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        continentValueTextField.setWidth(200,Unit.PIXELS);
        continentValueContainer.add(continentValueTextField);

        HorizontalLayout countryRegionValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(countryRegionValueContainer);
        NativeLabel countryRegionFilterText = new NativeLabel("国家地区 :");
        countryRegionFilterText.setWidth(50,Unit.PIXELS);
        countryRegionFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        countryRegionValueContainer.add(countryRegionFilterText);
        countryRegionValueContainer.setVerticalComponentAlignment(Alignment.CENTER,countryRegionFilterText);
        TextField countryRegionTextField = new TextField();
        countryRegionTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        countryRegionTextField.setWidth(200,Unit.PIXELS);
        countryRegionValueContainer.add(countryRegionTextField);

        HorizontalLayout provinceValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(provinceValueContainer);
        NativeLabel provinceFilterText = new NativeLabel("省级 :");
        provinceFilterText.setWidth(50,Unit.PIXELS);
        provinceFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        provinceValueContainer.add(provinceFilterText);
        provinceValueContainer.setVerticalComponentAlignment(Alignment.CENTER,provinceFilterText);
        TextField provinceValueTextField = new TextField();
        provinceValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        provinceValueTextField.setWidth(200,Unit.PIXELS);
        provinceValueContainer.add(provinceValueTextField);

        HorizontalLayout prefectureValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(prefectureValueContainer);
        NativeLabel prefectureFilterText = new NativeLabel("地级 :");
        prefectureFilterText.setWidth(50,Unit.PIXELS);
        prefectureFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        prefectureValueContainer.add(prefectureFilterText);
        prefectureValueContainer.setVerticalComponentAlignment(Alignment.CENTER,prefectureFilterText);
        TextField prefectureValueTextField = new TextField();
        prefectureValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        prefectureValueTextField.setWidth(200,Unit.PIXELS);
        prefectureValueContainer.add(prefectureValueTextField);

        HorizontalLayout countyValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(countyValueContainer);
        NativeLabel countyFilterText = new NativeLabel("县级 :");
        countyFilterText.setWidth(50,Unit.PIXELS);
        countyFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        countyValueContainer.add(countyFilterText);
        countyValueContainer.setVerticalComponentAlignment(Alignment.CENTER,countyFilterText);
        TextField countyValueTextField = new TextField();
        countyValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        countyValueTextField.setWidth(200,Unit.PIXELS);
        countyValueContainer.add(countyValueTextField);

        HorizontalLayout townshipValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(townshipValueContainer);
        NativeLabel townshipFilterText = new NativeLabel("乡级 :");
        townshipFilterText.setWidth(50,Unit.PIXELS);
        townshipFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        townshipValueContainer.add(townshipFilterText);
        townshipValueContainer.setVerticalComponentAlignment(Alignment.CENTER,townshipFilterText);
        TextField townshipValueTextField = new TextField();
        townshipValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        townshipValueTextField.setWidth(200,Unit.PIXELS);
        townshipValueContainer.add(townshipValueTextField);

        HorizontalLayout villageValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(villageValueContainer);
        NativeLabel villageFilterText = new NativeLabel("村级 :");
        villageFilterText.setWidth(50,Unit.PIXELS);
        villageFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        villageValueContainer.add(villageFilterText);
        villageValueContainer.setVerticalComponentAlignment(Alignment.CENTER,villageFilterText);
        TextField villageValueTextField = new TextField();
        villageValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        villageValueTextField.setWidth(200,Unit.PIXELS);
        villageValueContainer.add(villageValueTextField);

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










        middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);

        mainContainerLayout.add(middleContainerLayout);
        middleContainerLayout.setWidth(300, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle filterTitle3 = new SecondaryIconTitle(FontAwesome.Solid.CLOCK.create(),"地理空间尺度实体检索结果");
        filterTitle3.getStyle().set("padding-left","10px");
        middleContainerLayout.add(filterTitle3);

        HorizontalLayout heightSpaceDivM0 = new HorizontalLayout();
        heightSpaceDivM0.setWidth(100,Unit.PERCENTAGE);
        middleContainerLayout.add(heightSpaceDivM0);
        heightSpaceDivM0.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-left", "var(--lumo-space-l)")
                .set("padding-right", "var(--lumo-space-l)")
                .set("padding-bottom", "var(--lumo-space-s)");

















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
        this.numberFormat = NumberFormat.getInstance();
        totalGeospatialScaleEntityCountDisplayItem.updateDisplayValue(this.numberFormat.format(geospatialRegionSummaryStatistics.getContainsTotalGeospatialScaleEntityCount()));
        totalGeospatialScaleEventCountDisplayItem.updateDisplayValue(this.numberFormat.format(geospatialRegionSummaryStatistics.getRefersTotalGeospatialScaleEventCount()));
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
