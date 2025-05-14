package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDescriptionUpdatedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.externalValueViewKindConfig.ObjectStoreDataSourceConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.externalValueViewKindConfig.RelationDBDataSourceConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;

public class AttributesViewKindRuntimeConfigurationView extends VerticalLayout implements
        AttributesViewKindDescriptionUpdatedEvent.AttributesViewKindDescriptionUpdatedListener{
    private String attributesViewKindUID;
    private MetaConfigItemsConfigView metaConfigItemsConfigView;
    private ClassificationConfigView classificationConfigView;
    private PrimaryKeyValueDisplayItem attributesViewKindDescTxt;
    private AttributesViewKind targetAttributesViewKind;

    public AttributesViewKindRuntimeConfigurationView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.COG_O),"属性视图类型定义配置");
        add(filterTitle1);
        HorizontalLayout infoContainer0 = new HorizontalLayout();
        infoContainer0.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer0.setWidthFull();
        add(infoContainer0);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);

        new PrimaryKeyValueDisplayItem(infoContainer0, VaadinIcon.INFO_CIRCLE_O.create(),"属性视图类型名称:",targetAttributesViewKind.getAttributesViewKindName());
        HorizontalLayout horSpaceDiv0 = new HorizontalLayout();
        horSpaceDiv0.setWidth(20, Unit.PIXELS);
        infoContainer0.add(horSpaceDiv0);
        attributesViewKindDescTxt = new PrimaryKeyValueDisplayItem(infoContainer0, VaadinIcon.DESKTOP.create(),"属性视图类型描述:",targetAttributesViewKind.getAttributesViewKindDesc());

        HorizontalLayout infoContainer1 = new HorizontalLayout();
        infoContainer1.setDefaultVerticalComponentAlignment(Alignment.END);
        infoContainer1.setWidthFull();
        infoContainer1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer1);
        new PrimaryKeyValueDisplayItem(infoContainer1, VaadinIcon.ELLIPSIS_H.create(),"属性视图存储结构:",targetAttributesViewKind.getAttributesViewKindDataForm().toString());
        HorizontalLayout horSpaceDiv1 = new HorizontalLayout();
        horSpaceDiv1.setWidth(20,Unit.PIXELS);
        infoContainer1.add(horSpaceDiv1);

        new PrimaryKeyValueDisplayItem(infoContainer1, VaadinIcon.KEY_O.create(),"属性视图类型 UID:",targetAttributesViewKind.getAttributesViewKindUID());

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"属性视图类型组件运行时配置");
        filterTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(filterTitle2);

        metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.AttributesViewKind,this.attributesViewKindUID);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        if(AttributesViewKind.AttributesViewKindDataForm.EXTERNAL_VALUE.equals(targetAttributesViewKind.getAttributesViewKindDataForm())){
            MenuBar setupExternalDataSourceMenuBar = new MenuBar();
            setupExternalDataSourceMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
            MenuItem setupExternalDataSourceDataMenu = createIconItem(setupExternalDataSourceMenuBar, LineAwesomeIconsSvg.HDD.create(), " 配置 External Value 属性视图数据源", null);
            Icon downArrowIcon1 = new Icon(VaadinIcon.CHEVRON_DOWN);
            downArrowIcon1.setSize("12px");
            setupExternalDataSourceDataMenu.add(downArrowIcon1);

            SubMenu setupExternalDataSourceSubItems = setupExternalDataSourceDataMenu.getSubMenu();
            HorizontalLayout action0Layout = new HorizontalLayout();
            action0Layout.setPadding(false);
            action0Layout.setSpacing(false);
            action0Layout.setMargin(false);
            action0Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action0Icon = VaadinIcon.DATABASE.create();
            action0Icon.setSize("10px");
            Span action0Space = new Span();
            action0Space.setWidth(6,Unit.PIXELS);
            NativeLabel action0Label = new NativeLabel(" 配置 RelationDB");
            action0Layout.add(action0Icon,action0Space,action0Label);

            MenuItem setupRelationDBItem = setupExternalDataSourceSubItems.addItem(action0Layout);
            setupRelationDBItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    renderRelationDBDataSourceConfigView();
                }
            });

            HorizontalLayout action1Layout = new HorizontalLayout();
            action1Layout.setPadding(false);
            action1Layout.setSpacing(false);
            action1Layout.setMargin(false);
            action1Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action1Icon = LineAwesomeIconsSvg.BOX_SOLID.create();
            action1Icon.setSize("12px");
            Span action1Space = new Span();
            action1Space.setWidth(6,Unit.PIXELS);
            NativeLabel action1Label = new NativeLabel(" 配置 ObjectStore");
            action1Layout.add(action1Icon,action1Space,action1Label);

            MenuItem setupObjectStoreItem = setupExternalDataSourceSubItems.addItem(action1Layout);
            setupObjectStoreItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    renderObjectStoreDataSourceConfigView();
                }
            });

            HorizontalLayout action2Layout = new HorizontalLayout();
            action2Layout.setPadding(false);
            action2Layout.setSpacing(false);
            action2Layout.setMargin(false);
            action2Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            Icon action2Icon = VaadinIcon.ALARM.create();
            action2Icon.setSize("10px");
            Span action2Space = new Span();
            action2Space.setWidth(6,Unit.PIXELS);
            NativeLabel action2Label = new NativeLabel(" 配置 TimeSeriesDB");
            action2Layout.add(action2Icon,action2Space,action2Label);

            MenuItem setupTimeSeriesDBItem = setupExternalDataSourceSubItems.addItem(action2Layout);
            setupTimeSeriesDBItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
                @Override
                public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                    //renderRelationDBDataSourceConfigView();
                }
            });

            metaConfigItemsConfigView.setCustomizeComponent(setupExternalDataSourceMenuBar);
        }
        add(metaConfigItemsConfigView);

        classificationConfigView = new ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType.AttributesViewKind,this.attributesViewKindUID);
        classificationConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(classificationConfigView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        metaConfigItemsConfigView.setViewHeight(350);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedAttributesViewKindDescriptionUpdatedEvent(AttributesViewKindDescriptionUpdatedEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributesViewKindDesc() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                attributesViewKindDescTxt.updateDisplayValue(event.getAttributesViewKindDesc());
            }
        }
    }

    public void setViewHeight(int viewHeight){
        this.classificationConfigView.setHeight(viewHeight - 575,Unit.PIXELS);
    }

    private MenuItem createIconItem(HasMenuItems menu, Icon icon, String label, String ariaLabel) {
        return createIconItem(menu, icon, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, Icon icon,String label, String ariaLabel, boolean isChild) {
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

    private void renderRelationDBDataSourceConfigView(){
        RelationDBDataSourceConfigView relationDBDataSourceConfigView = new RelationDBDataSourceConfigView(targetAttributesViewKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.HDD.create(),"配置 RelationDB 数据源",null,true,400,670,false);
        fixSizeWindow.setWindowContent(relationDBDataSourceConfigView);
        relationDBDataSourceConfigView.setContainerDialog(fixSizeWindow);
        relationDBDataSourceConfigView.setRelatedMetaConfigItemsConfigView(metaConfigItemsConfigView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderObjectStoreDataSourceConfigView(){
        ObjectStoreDataSourceConfigView ObjectStoreDataSourceConfigView = new ObjectStoreDataSourceConfigView(targetAttributesViewKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.BOX_SOLID.create(),"配置 ObjectStore 数据源",null,true,400,670,false);
        fixSizeWindow.setWindowContent(ObjectStoreDataSourceConfigView);
        ObjectStoreDataSourceConfigView.setContainerDialog(fixSizeWindow);
        ObjectStoreDataSourceConfigView.setRelatedMetaConfigItemsConfigView(metaConfigItemsConfigView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
