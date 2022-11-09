package com.viewfunction.docg.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.element.eventHandling.*;
import com.viewfunction.docg.util.ResourceHolder;
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

    public static class MenuItemInfo {

        private String text;
        private String iconClass;
        private Class<? extends Component> view;

        public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
            this.text = text;
            this.iconClass = iconClass;
            this.view = view;
        }

        public String getText() {
            return text;
        }

        public String getIconClass() {
            return iconClass;
        }

        public Class<? extends Component> getView() {
            return view;
        }

    }

    private final Tabs menu;
    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
        //close the left side drawer by default
        this.setDrawerOpened(false);
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setClassName("sidemenu-header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);

        Avatar avatar = new Avatar();
        avatar.addClassNames("ms-auto", "me-m");
        layout.add(avatar);

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
        //logoLayout.add(new H1(" SAC"));
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
        MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
               // new MenuItemInfo("概览 [ General Information ]", "la la-chalkboard", GeneralInformationView.class), //

                new MenuItemInfo("核心领域模型 [ Core Realm ]", "la la-cubes", CoreRealmView.class), //

                new MenuItemInfo("计算网格 [ Compute Grid ]", "la la-calculator", ComputeGridView.class), //

                new MenuItemInfo("数据分析 [ Data Analysis ]", "la la-brain", DataAnalysisView.class), //

                new MenuItemInfo("知识融合 [ Knowledge Fusion ]", "lab la-mendeley", KnowledgeFusionView.class), //

                new MenuItemInfo("关于 [ About ]", "la la-info-circle", AboutView.class), //

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
        iconElement.addClassNames("text-l", "pr-s");
        if (!menuItemInfo.getIconClass().isEmpty()) {
            iconElement.addClassNames(menuItemInfo.getIconClass());
        }
        link.add(iconElement, new Text(menuItemInfo.getText()));
        tab.add(link);
        ComponentUtil.setData(tab, Class.class, menuItemInfo.getView());
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
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
        _BLACKBOARD.register(ConceptionEntityAttributeAddedEvent.ConceptionEntityAttributeAddedListener.class,
                ConceptionEntityAttributeAddedEvent.class);
        _BLACKBOARD.register(ConceptionEntityAttributeUpdatedEvent.ConceptionEntityAttributeUpdatedListener.class,
                ConceptionEntityAttributeUpdatedEvent.class);
        _BLACKBOARD.register(ConceptionEntityAttributeDeletedEvent.ConceptionEntityAttributeDeletedListener.class,
                ConceptionEntityAttributeDeletedEvent.class);
        _BLACKBOARD.register(RelationEntityDeletedEvent.RelationEntityDeletedListener.class,
                RelationEntityDeletedEvent.class);

        ResourceHolder.setApplicationBlackboard(_BLACKBOARD);
    }
}
