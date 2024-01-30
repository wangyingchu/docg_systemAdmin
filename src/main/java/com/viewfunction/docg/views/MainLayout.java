package com.viewfunction.docg.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.*;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import com.viewfunction.docg.views.corerealm.CoreRealmView;
import com.viewfunction.docg.views.computegrid.ComputeGridView;
import com.viewfunction.docg.views.dataAnalysis.DataAnalysisView;
import com.viewfunction.docg.views.knowledgefusion.KnowledgeFusionView;
import com.viewfunction.docg.views.about.AboutView;
import com.vaadin.flow.component.avatar.Avatar;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    private final String SESSION_MAX_INACTIVE_INTERVAL_IN_SECOND =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.SESSION_MAX_INACTIVE_INTERVAL_IN_SECOND);
    private final String ENABLE_USER_LOCK_APPLICATION = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ENABLE_USER_LOCK_APPLICATION);

    public static class MenuItemInfo {

        private String text;
        private Class<? extends Component> view;
        private Icon linkIcon;

        public MenuItemInfo(Icon linkIcon,String text, Class<? extends Component> view) {
            this.linkIcon = linkIcon;
            this.text = text;
            this.view = view;
        }

        public String getText() {
            return text;
        }

        public Class<? extends Component> getView() {
            return view;
        }
    }

    private final Tabs menu;
    private HorizontalLayout viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
        //close the left side drawer by default
        this.setDrawerOpened(false);

        initBlackboard();

        if(SESSION_MAX_INACTIVE_INTERVAL_IN_SECOND != null){
            int maxInactiveInterval = Integer.parseInt(SESSION_MAX_INACTIVE_INTERVAL_IN_SECOND);
            //be default session MaxInactiveInterval is 1800s = 30min
            VaadinSession.getCurrent().getSession().setMaxInactiveInterval(maxInactiveInterval);
        }
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setClassName("sidemenu-header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        DrawerToggle drawerToggle = new DrawerToggle();
        //drawerToggle.setEnabled(false);
        layout.add(drawerToggle);
        viewTitle = new HorizontalLayout();
        layout.add(viewTitle);

        Avatar avatar = new Avatar("系统用户");
        avatar.setAbbreviation("USR");

        MenuBar menuBar = new MenuBar();
        menuBar.addClassNames("ms-auto", "me-m");
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        MenuItem menuItem = menuBar.addItem(avatar);
        SubMenu subMenu = menuItem.getSubMenu();

        HorizontalLayout actionLayout = new HorizontalLayout();
        actionLayout.setPadding(false);
        actionLayout.setSpacing(false);
        actionLayout.setMargin(false);
        actionLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Icon action1Icon = VaadinIcon.LOCK.create();
        action1Icon.setSize("10px");
        Span action1Space = new Span();
        action1Space.setWidth(6,Unit.PIXELS);
        NativeLabel action1Label = new NativeLabel("锁定系统");
        action1Label.addClassNames("text-xs","font-semibold","text-secondary");
        actionLayout.add(action1Icon,action1Space,action1Label);

        if(ENABLE_USER_LOCK_APPLICATION != null & Boolean.parseBoolean(ENABLE_USER_LOCK_APPLICATION)){
            MenuItem exitSystemActionItem = subMenu.addItem(actionLayout);
            exitSystemActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    UserLockApplicationEvent userLockApplicationEvent = new UserLockApplicationEvent();
                    ResourceHolder.getApplicationBlackboard().fire(userLockApplicationEvent);
                }
            });
        }

        layout.add(menuBar);

        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("sidemenu-menu");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image image = new Image("images/logo.png","DOCG-SAC logo");
        image.setHeight(39, Unit.PIXELS);
        image.setWidth(128, Unit.PIXELS);
        logoLayout.add(image);
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        for (Tab menuTab : createMenuItems()) {
            tabs.add(menuTab);
        }
        return tabs;
    }

    private List<Tab> createMenuItems() {
        MenuItemInfo[] menuItems = new MenuItemInfo[]{
                new MenuItemInfo(LineAwesomeIconsSvg.CONNECTDEVELOP.create(),"核心领域模型 [Core Realm]", CoreRealmView.class),
                new MenuItemInfo(LineAwesomeIconsSvg.NETWORK_WIRED_SOLID.create(),"计算网格 [Compute Grid]", ComputeGridView.class),
                new MenuItemInfo(LineAwesomeIconsSvg.FLASK_SOLID.create(),"数据分析 [Data Analysis]", DataAnalysisView.class),
                new MenuItemInfo(LineAwesomeIconsSvg.WINDOW_RESTORE.create(),"知识融合 [Knowledge Fusion]", KnowledgeFusionView.class),
                new MenuItemInfo(LineAwesomeIconsSvg.FINGERPRINT_SOLID.create(),"关于 [About]",  AboutView.class),
        };
        List<Tab> tabs = new ArrayList<>();
        for (MenuItemInfo menuItemInfo : menuItems) {
            tabs.add(createTab(menuItemInfo));
        }
        return tabs;
    }

    private static Tab createTab(MenuItemInfo menuItemInfo) {
        Tab tab = new Tab();
        RouterLink link = new RouterLink();
        link.setRoute(menuItemInfo.getView());
        Span iconElement = new Span();
        Icon linkIcon =menuItemInfo.linkIcon;
        if(linkIcon != null){
            iconElement.add(linkIcon);
            linkIcon.setSize("18px");
            linkIcon.getStyle().set("padding-right","5px");
        }

        Text linkText = new Text(menuItemInfo.getText());
        link.add(iconElement, linkText);
        tab.add(link);
        ComponentUtil.setData(tab, Class.class, menuItemInfo.getView());
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.removeAll();
        String currentPageTitle = getCurrentPageTitle();
        if(currentPageTitle.equals("数海云图 - 核心领域模型 [ Core Realm ]")){
            viewTitle.add(getTitleViewComponent(LineAwesomeIconsSvg.CONNECTDEVELOP.create(),"核心领域模型 [ Core Realm ]"));
        }
        if(currentPageTitle.equals("数海云图 - 计算网格 [ Compute Grid ]")){
            viewTitle.add(getTitleViewComponent(LineAwesomeIconsSvg.NETWORK_WIRED_SOLID.create(),"计算网格 [ Compute Grid ]"));
        }
        if(currentPageTitle.equals("数海云图 - 数据分析 [ Data Analysis ]")){
            viewTitle.add(getTitleViewComponent(LineAwesomeIconsSvg.FLASK_SOLID.create(),"数据分析 [ Data Analysis ]"));
        }
        if(currentPageTitle.equals("数海云图 - 知识融合 [ Knowledge Fusion ]")){
            viewTitle.add(getTitleViewComponent(LineAwesomeIconsSvg.WINDOW_RESTORE.create(),"知识融合 [ Knowledge Fusion ]"));
        }
        if(currentPageTitle.equals("数海云图 - 关于 [ About ]")){
            viewTitle.add(getTitleViewComponent(LineAwesomeIconsSvg.FINGERPRINT_SOLID.create(),"关于 [ About ]"));
        }
    }

    private HorizontalLayout getTitleViewComponent(Icon viewIcon,String viewName){
        HorizontalLayout titleViewLayout = new HorizontalLayout();
        titleViewLayout.setSpacing(false);
        titleViewLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        viewIcon.setSize("26px");
        H1 viewNameHeader = new H1(viewName);
        viewNameHeader.getStyle().set("padding-left","5px");
        titleViewLayout.add(viewIcon,viewNameHeader);
        return titleViewLayout;
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void initBlackboard(){
        Blackboard _BLACKBOARD = new Blackboard();
        _BLACKBOARD.enableLogging();

        _BLACKBOARD.register(ConceptionKindCreatedEvent.ConceptionKindCreatedListener.class,
                ConceptionKindCreatedEvent.class);
        _BLACKBOARD.register(ConceptionKindCleanedEvent.ConceptionKindCleanedListener.class,
                ConceptionKindCleanedEvent.class);
        _BLACKBOARD.register(ConceptionKindRemovedEvent.ConceptionKindRemovedListener.class,
                ConceptionKindRemovedEvent.class);
        _BLACKBOARD.register(ConceptionKindQueriedEvent.ConceptionKindQueriedListener.class,
                ConceptionKindQueriedEvent.class);
        _BLACKBOARD.register(ConceptionEntityDeletedEvent.ConceptionEntityDeletedListener.class,
                ConceptionEntityDeletedEvent.class);
        _BLACKBOARD.register(ConceptionEntitiesCreatedEvent.ConceptionEntitiesCreatedListener.class,
                ConceptionEntitiesCreatedEvent.class);
        _BLACKBOARD.register(ConceptionEntityAttributeAddedEvent.ConceptionEntityAttributeAddedListener.class,
                ConceptionEntityAttributeAddedEvent.class);
        _BLACKBOARD.register(ConceptionEntityAttributeUpdatedEvent.ConceptionEntityAttributeUpdatedListener.class,
                ConceptionEntityAttributeUpdatedEvent.class);
        _BLACKBOARD.register(ConceptionEntityAttributeDeletedEvent.ConceptionEntityAttributeDeletedListener.class,
                ConceptionEntityAttributeDeletedEvent.class);
        _BLACKBOARD.register(RelationEntityDeletedEvent.RelationEntityDeletedListener.class,
                RelationEntityDeletedEvent.class);
        _BLACKBOARD.register(CheckSystemRuntimeInfoEvent.CheckSystemRuntimeInfoListener.class,
                CheckSystemRuntimeInfoEvent.class);
        _BLACKBOARD.register(RelationKindCreatedEvent.RelationKindCreatedListener.class,
                RelationKindCreatedEvent.class);
        _BLACKBOARD.register(RelationKindRemovedEvent.RelationKindRemovedListener.class,
                RelationKindRemovedEvent.class);
        _BLACKBOARD.register(RelationKindCleanedEvent.RelationKindCleanedListener.class,
                RelationKindCleanedEvent.class);
        _BLACKBOARD.register(RelationEntitiesCreatedEvent.RelationEntitiesCreatedListener.class,
                RelationEntitiesCreatedEvent.class);
        _BLACKBOARD.register(RelationKindQueriedEvent.RelationKindQueriedListener.class,
                RelationKindQueriedEvent.class);
        _BLACKBOARD.register(RelationEntityAttributeAddedEvent.RelationEntityAttributeAddedListener.class,
                RelationEntityAttributeAddedEvent.class);
        _BLACKBOARD.register(RelationEntityAttributeDeletedEvent.RelationEntityAttributeDeletedListener.class,
                RelationEntityAttributeDeletedEvent.class);
        _BLACKBOARD.register(RelationEntityAttributeUpdatedEvent.RelationEntityAttributeUpdatedListener.class,
                RelationEntityAttributeUpdatedEvent.class);
        _BLACKBOARD.register(ConceptionKindDescriptionUpdatedEvent.ConceptionKindDescriptionUpdatedListener.class,
                ConceptionKindDescriptionUpdatedEvent.class);
        _BLACKBOARD.register(RelationKindDescriptionUpdatedEvent.RelationKindDescriptionUpdatedListener.class,
                RelationKindDescriptionUpdatedEvent.class);
        _BLACKBOARD.register(ConceptionEntitiesCountRefreshEvent.ConceptionEntitiesCountRefreshListener.class,
                ConceptionEntitiesCountRefreshEvent.class);
        _BLACKBOARD.register(ConceptionKindConfigurationInfoRefreshEvent.ConceptionKindConfigurationInfoRefreshListener.class,
                ConceptionKindConfigurationInfoRefreshEvent.class);
        _BLACKBOARD.register(RelationKindConfigurationInfoRefreshEvent.RelationKindConfigurationInfoRefreshListener.class,
                RelationKindConfigurationInfoRefreshEvent.class);
        _BLACKBOARD.register(RelationEntitiesCountRefreshEvent.RelationEntitiesCountRefreshListener.class,
                RelationEntitiesCountRefreshEvent.class);
        _BLACKBOARD.register(AttributeKindCreatedEvent.AttributeKindCreatedListener.class,
                AttributeKindCreatedEvent.class);
        _BLACKBOARD.register(AttributeKindRemovedEvent.AttributeKindRemovedListener.class,
                AttributeKindRemovedEvent.class);
        _BLACKBOARD.register(AttributesViewKindCreatedEvent.AttributesViewKindCreatedListener.class,
                AttributesViewKindCreatedEvent.class);
        _BLACKBOARD.register(AttributesViewKindRemovedEvent.AttributesViewKindRemovedListener.class,
                AttributesViewKindRemovedEvent.class);
        _BLACKBOARD.register(AttributeKindDescriptionUpdatedEvent.AttributeKindDescriptionUpdatedListener.class,
                AttributeKindDescriptionUpdatedEvent.class);
        _BLACKBOARD.register(AttributesViewKindDescriptionUpdatedEvent.AttributesViewKindDescriptionUpdatedListener.class,
                AttributesViewKindDescriptionUpdatedEvent.class);
        _BLACKBOARD.register(AttributeKindAttachedToAttributesViewKindEvent.AttributeKindAttachedToAttributesViewKindListener.class,
                AttributeKindAttachedToAttributesViewKindEvent.class);
        _BLACKBOARD.register(AttributeKindDetachedFromAttributesViewKindEvent.AttributeKindDetachedFromAttributesViewKindListener.class,
                AttributeKindDetachedFromAttributesViewKindEvent.class);
        _BLACKBOARD.register(AttributesViewKindAttachedToConceptionKindEvent.AttributesViewKindAttachedToConceptionKindListener.class,
                AttributesViewKindAttachedToConceptionKindEvent.class);
        _BLACKBOARD.register(AttributesViewKindDetachedFromConceptionKindEvent.AttributesViewKindDetachedFromConceptionKindListener.class,
                AttributesViewKindDetachedFromConceptionKindEvent.class);
        _BLACKBOARD.register(ClassificationCreatedEvent.ClassificationCreatedListener.class,
                ClassificationCreatedEvent.class);
        _BLACKBOARD.register(ClassificationRemovedEvent.ClassificationRemovedListener.class,
                ClassificationRemovedEvent.class);
        _BLACKBOARD.register(ClassificationDescriptionUpdatedEvent.ClassificationDescriptionUpdatedListener.class,
                ClassificationDescriptionUpdatedEvent.class);
        _BLACKBOARD.register(ClassificationAttributeAddedEvent.ClassificationAttributeAddedListener.class,
                ClassificationAttributeAddedEvent.class);
        _BLACKBOARD.register(ClassificationAttributeDeletedEvent.ClassificationAttributeDeletedListener.class,
                ClassificationAttributeDeletedEvent.class);
        _BLACKBOARD.register(ClassificationAttributeUpdatedEvent.ClassificationAttributeUpdatedListener.class,
                ClassificationAttributeUpdatedEvent.class);
        _BLACKBOARD.register(ClassificationDetachedEvent.ClassificationDetachedListener.class,
                ClassificationDetachedEvent.class);
        _BLACKBOARD.register(ClassificationAttachedEvent.ClassificationAttachedListener.class,
                ClassificationAttachedEvent.class);
        _BLACKBOARD.register(TimeFlowRefreshEvent.TimeFlowRefreshEventListener.class,
                TimeFlowRefreshEvent.class);
        _BLACKBOARD.register(GeospatialRegionRefreshEvent.GeospatialRegionRefreshEventListener.class,
                GeospatialRegionRefreshEvent.class);
        _BLACKBOARD.register(UserLockApplicationEvent.UserApplicationLogoutListener.class,
                UserLockApplicationEvent.class);
        _BLACKBOARD.register(DataSliceCreatedEvent.DataSliceCreatedListener.class,
                DataSliceCreatedEvent.class);
        _BLACKBOARD.register(ComputeGridRefreshEvent.ComputeGridRefreshEventListener.class,
                ComputeGridRefreshEvent.class);
        _BLACKBOARD.register(RelationAttachKindCreatedEvent.RelationAttachKindCreatedListener.class,
                RelationAttachKindCreatedEvent.class);
        _BLACKBOARD.register(GeospatialRegionCreatedEvent.GeospatialRegionCreatedListener.class,
                GeospatialRegionCreatedEvent.class);
        _BLACKBOARD.register(TimeFlowCreatedEvent.TimeFlowCreatedListener.class,
                TimeFlowCreatedEvent.class);
        _BLACKBOARD.register(KindScopeAttributeAddedEvent.KindScopeAttributeAddedListener.class,
                KindScopeAttributeAddedEvent.class);

        ResourceHolder.setApplicationBlackboard(_BLACKBOARD);
    }
}
