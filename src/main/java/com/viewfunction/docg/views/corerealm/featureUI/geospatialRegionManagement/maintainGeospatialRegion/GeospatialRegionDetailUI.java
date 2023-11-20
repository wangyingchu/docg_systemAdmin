package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialRegionSummaryStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;

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
    private NumberFormat numberFormat;

    public GeospatialRegionDetailUI(){
        //this.binder = new Binder<>();
        this.contentContainerHeightOffset = 265;
    }

    public GeospatialRegionDetailUI(String geospatialRegionName){
        this.geospatialRegionName = geospatialRegionName;
        //this.binder = new Binder<>();
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
        leftSideContainerLayout.setWidth(320, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"地理空间区域概览");
        leftSideContainerLayout.add(filterTitle1);

        HorizontalLayout heightSpaceDiv0 = new HorizontalLayout();
        heightSpaceDiv0.setWidth(95,Unit.PERCENTAGE);
        leftSideContainerLayout.add(heightSpaceDiv0);
        heightSpaceDiv0.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");

        VerticalLayout timeFlowInformationLayout = new VerticalLayout();
        timeFlowInformationLayout.setMargin(false);

        leftSideContainerLayout.add(timeFlowInformationLayout);
        VerticalScrollLayout leftSideSectionContainerScrollLayout = new VerticalScrollLayout();
        leftSideContainerLayout.add(leftSideSectionContainerScrollLayout);

        HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
        timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        MenuBar timeflowOperationMenuBar = new MenuBar();
        timeflowOperationMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem timeFlowOperationMenu = createIconItem(timeflowOperationMenuBar, VaadinIcon.COG, "配置", null);
        timeFlowOperationMenu.getStyle().set("font-size","0.7rem");
        Icon downArrowIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon.setSize("10px");
        timeFlowOperationMenu.add(downArrowIcon);

        SubMenu operationSubItems = timeFlowOperationMenu.getSubMenu();

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
        actionComponentsList.add(timeflowOperationMenuBar);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.GLOBE_WIRE),this.geospatialRegionName,null,actionComponentsList);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        timeFlowInformationLayout.add(secondaryTitleActionBar);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        totalGeospatialScaleEntityCountDisplayItem = new SecondaryKeyValueDisplayItem(horizontalLayout, FontAwesome.Solid.MAP.create(),"GeospatialScaleEntity 数量:","-");
        timeFlowInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        totalGeospatialScaleEventCountDisplayItem = new SecondaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.MAP_LOCATION.create(),"GeospatialScaleEvent 数量:","-");
        timeFlowInformationLayout.add(horizontalLayout2);







        VerticalLayout timeFlowInfoWallContainerLayout = new VerticalLayout();
        timeFlowInfoWallContainerLayout.setMargin(false);
        timeFlowInfoWallContainerLayout.setPadding(false);
        timeFlowInfoWallContainerLayout.setSpacing(false);
        timeFlowInfoWallContainerLayout.setWidth(95,Unit.PERCENTAGE);
        timeFlowInformationLayout.add(timeFlowInfoWallContainerLayout);

        HorizontalLayout yearEntitiesInfoLayout = new HorizontalLayout();
        yearEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel yearEntitiesLabel = new NativeLabel("Year Entities:");
        SecondaryKeyValueDisplayItem yearEntityCountItem = new SecondaryKeyValueDisplayItem(yearEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        SecondaryKeyValueDisplayItem yearEventCountItem = new SecondaryKeyValueDisplayItem(yearEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon yearInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        yearInfoTitleIcon.setSize("10px");
        SectionWallTitle yearInfoSectionWallTitle = new SectionWallTitle(yearInfoTitleIcon,yearEntitiesLabel);
        SectionWallContainer yearInfoSectionWallContainer = new SectionWallContainer(yearInfoSectionWallTitle,yearEntitiesInfoLayout);
        yearInfoSectionWallContainer.setOpened(false);
        yearInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                //setupTimeFlowRuntimeStatisticInfo();
            }
        });
        timeFlowInfoWallContainerLayout.add(yearInfoSectionWallContainer);

        HorizontalLayout monthEntitiesInfoLayout = new HorizontalLayout();
        monthEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel monthEntitiesLabel = new NativeLabel("Month Entities:");
        SecondaryKeyValueDisplayItem monthEntityCountItem = new SecondaryKeyValueDisplayItem(monthEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        SecondaryKeyValueDisplayItem monthEventCountItem = new SecondaryKeyValueDisplayItem(monthEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon monthInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        monthInfoTitleIcon.setSize("10px");
        SectionWallTitle monthInfoSectionWallTitle = new SectionWallTitle(monthInfoTitleIcon,monthEntitiesLabel);
        SectionWallContainer monthInfoSectionWallContainer = new SectionWallContainer(monthInfoSectionWallTitle,monthEntitiesInfoLayout);
        monthInfoSectionWallContainer.setOpened(false);
        monthInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                //setupTimeFlowRuntimeStatisticInfo();
            }
        });
        timeFlowInfoWallContainerLayout.add(monthInfoSectionWallContainer);

        HorizontalLayout dayEntitiesInfoLayout = new HorizontalLayout();
        dayEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel dayEntitiesLabel = new NativeLabel("Day Entities:");
        SecondaryKeyValueDisplayItem dayEntityCountItem = new SecondaryKeyValueDisplayItem(dayEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        SecondaryKeyValueDisplayItem dayEventCountItem = new SecondaryKeyValueDisplayItem(dayEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon dayInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        dayInfoTitleIcon.setSize("10px");
        SectionWallTitle dayInfoSectionWallTitle = new SectionWallTitle(dayInfoTitleIcon,dayEntitiesLabel);
        SectionWallContainer dayInfoSectionWallContainer = new SectionWallContainer(dayInfoSectionWallTitle,dayEntitiesInfoLayout);
        dayInfoSectionWallContainer.setOpened(false);
        dayInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                //setupTimeFlowRuntimeStatisticInfo();
            }
        });
        timeFlowInfoWallContainerLayout.add(dayInfoSectionWallContainer);

        HorizontalLayout hourEntitiesInfoLayout = new HorizontalLayout();
        hourEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel hourEntitiesLabel = new NativeLabel("Hour Entities:");
        SecondaryKeyValueDisplayItem hourEntityCountItem = new SecondaryKeyValueDisplayItem(hourEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        SecondaryKeyValueDisplayItem hourEventCountItem = new SecondaryKeyValueDisplayItem(hourEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon hourInfoTitleIcon = new Icon(VaadinIcon.CLOCK);
        hourInfoTitleIcon.setSize("10px");
        SectionWallTitle hourInfoSectionWallTitle = new SectionWallTitle(hourInfoTitleIcon,hourEntitiesLabel);
        SectionWallContainer hourInfoSectionWallContainer = new SectionWallContainer(hourInfoSectionWallTitle,hourEntitiesInfoLayout);
        hourInfoSectionWallContainer.setOpened(false);
        hourInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                //setupTimeFlowRuntimeStatisticInfo();
            }
        });
        timeFlowInfoWallContainerLayout.add(hourInfoSectionWallContainer);

        HorizontalLayout minuteEntitiesInfoLayout = new HorizontalLayout();
        minuteEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel minuteEntitiesLabel = new NativeLabel("Minute Entities:");
        SecondaryKeyValueDisplayItem minuteEntityCountItem = new SecondaryKeyValueDisplayItem(minuteEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        SecondaryKeyValueDisplayItem minuteEventCountItem = new SecondaryKeyValueDisplayItem(minuteEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon minuteInfoTitleIcon = new Icon(VaadinIcon.CLOCK);
        minuteInfoTitleIcon.setSize("10px");
        SectionWallTitle minuteInfoSectionWallTitle = new SectionWallTitle(minuteInfoTitleIcon,minuteEntitiesLabel);
        SectionWallContainer minuteInfoSectionWallContainer = new SectionWallContainer(minuteInfoSectionWallTitle,minuteEntitiesInfoLayout);
        minuteInfoSectionWallContainer.setOpened(false);
        minuteInfoSectionWallContainer.addOpenedChangeListener(new ComponentEventListener<Details.OpenedChangeEvent>() {
            @Override
            public void onComponentEvent(Details.OpenedChangeEvent openedChangeEvent) {
                //setupTimeFlowRuntimeStatisticInfo();
            }
        });
        timeFlowInfoWallContainerLayout.add(minuteInfoSectionWallContainer);










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
