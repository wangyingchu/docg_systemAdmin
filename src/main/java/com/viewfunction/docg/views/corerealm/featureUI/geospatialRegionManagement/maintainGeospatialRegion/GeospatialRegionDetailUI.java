package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.util.ResourceHolder;

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
        //renderTimeFlowBasicInfo();
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
        leftSideContainerLayout.setWidth(270, Unit.PIXELS);
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

        HorizontalLayout action0Layout = new HorizontalLayout();
        action0Layout.setPadding(false);
        action0Layout.setSpacing(false);
        action0Layout.setMargin(false);
        action0Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon action0Icon = VaadinIcon.ARROWS_LONG_H.create();
        action0Icon.setSize("10px");
        Span action0Space = new Span();
        action0Space.setWidth(6,Unit.PIXELS);
        NativeLabel action0Label = new NativeLabel("扩展时间流年跨度");
        action0Label.addClassNames("text-xs","font-semibold","text-secondary");
        action0Layout.add(action0Icon,action0Space,action0Label);
        MenuItem expandFlowActionItem = operationSubItems.addItem(action0Layout);
        expandFlowActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                //renderExpendTimeFlowYearsUI();
            }
        });

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
