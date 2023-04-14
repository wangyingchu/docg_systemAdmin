package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCreatedEvent;

import com.viewfunction.docg.element.eventHandling.ConceptionKindCleanedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndexMaintain.KindIndexConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.CleanConceptionKindEntitiesView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities.LoadCSVFormatConceptionEntitiesView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities.LoadShpFormatConceptionEntitiesView;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView.KindType.ConceptionKind;

public class ConceptionKindEntitiesConfigurationView extends VerticalLayout implements
        ConceptionEntitiesCreatedEvent.ConceptionEntitiesCreatedListener,
        ConceptionEntitiesCountRefreshEvent.ConceptionEntitiesCountRefreshListener,
        ConceptionKindCleanedEvent.ConceptionKindCleanedListener{

    private String conceptionKindName;
    private long conceptionEntitiesCount;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionEntitiesCountDisplayItem;

    public ConceptionKindEntitiesConfigurationView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.CUBES),"概念类型实体配置");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        conceptionEntitiesCount = 0;
        try {
            conceptionEntitiesCount = coreRealm.getConceptionKind(this.conceptionKindName).countConceptionEntities();
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionEntitiesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"概念实体数量:",this.numberFormat.format(conceptionEntitiesCount));

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30,Unit.PIXELS);
        infoContainer.add(horSpaceDiv);

        Button addConceptionEntityButton= new Button("添加概念类型实体");
        addConceptionEntityButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        addConceptionEntityButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addConceptionEntityButton);
        addConceptionEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddConceptionEntityView();
            }
        });

        Button addConceptionKindScopeAttributeButton= new Button("添加概念类型全局属性");
        addConceptionKindScopeAttributeButton.setIcon(VaadinIcon.TEXT_INPUT.create());
        addConceptionKindScopeAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addConceptionKindScopeAttributeButton);
        addConceptionKindScopeAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddConceptionKindGlobalAttributeView();
            }
        });

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        infoContainer.add(divIcon);

        MenuBar importMenuBar = new MenuBar();
        importMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem importDataMenu = createIconItem(importMenuBar, VaadinIcon.UPLOAD, "导入概念实体数据", null);
        SubMenu importSubItems = importDataMenu.getSubMenu();
        MenuItem csvImportItem = importSubItems.addItem("CSV 格式数据");
        csvImportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderLoadCSVFormatConceptionEntitiesView();
            }
        });
        MenuItem arrowImportItem = importSubItems.addItem("ARROW 格式数据");
        MenuItem shpImportItem = importSubItems.addItem("SHP 格式数据");
        shpImportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderLoadSHPFormatConceptionEntitiesView();
            }
        });

        infoContainer.add(importMenuBar);

        MenuBar exportMenuBar = new MenuBar();
        exportMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem exportDataMenu = createIconItem(exportMenuBar, VaadinIcon.DOWNLOAD, "导出概念实体数据", null);
        SubMenu exportSubItems = exportDataMenu.getSubMenu();
        MenuItem csvExportItem = exportSubItems.addItem("CSV 格式数据");
        MenuItem arrowExportItem = exportSubItems.addItem("ARROW 格式数据");
        infoContainer.add(exportMenuBar);

        Icon divIcon2 = VaadinIcon.LINE_V.create();
        divIcon2.setSize("8px");
        infoContainer.add(divIcon2);

        Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
        cleanKindIcon.setSize("21px");
        Button cleanConceptionKindButton = new Button("清除概念类型实例",cleanKindIcon, event -> {});
        cleanConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cleanConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Tooltips.getCurrent().setTooltip(cleanConceptionKindButton, "清除概念类型所有实例");
        cleanConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCleanConceptionKindEntitiesUI();
            }
        });
        infoContainer.add(cleanConceptionKindButton);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"概念类型组件运行时配置");
        filterTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(filterTitle2);

        MetaConfigItemsConfigView metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.ConceptionKind,this.conceptionKindName);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(metaConfigItemsConfigView);

        KindIndexConfigView kindIndexConfigView = new KindIndexConfigView(KindIndexConfigView.KindIndexType.ConceptionKind,this.conceptionKindName);
        kindIndexConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(kindIndexConfigView);

        ClassificationConfigView classificationConfigView = new ClassificationConfigView();
        classificationConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(classificationConfigView);
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

    private void renderAddConceptionEntityView(){
        AddConceptionEntityView addConceptionEntityView = new AddConceptionEntityView(this.conceptionKindName,410,650);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加概念类型实体",null,true,500,700,false);
        fixSizeWindow.setWindowContent(addConceptionEntityView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAddConceptionKindGlobalAttributeView(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.conceptionKindName,null,ConceptionKind);
        addEntityAttributeView.setKindScopeAttribute(true);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TEXT_INPUT),"添加概念类型全局属性",null,true,480,240,false);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderCleanConceptionKindEntitiesUI(){
        CleanConceptionKindEntitiesView cleanConceptionKindEntitiesView = new CleanConceptionKindEntitiesView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.RECYCLE),"清除概念类型所有实例",null,true,600,210,false);
        fixSizeWindow.setWindowContent(cleanConceptionKindEntitiesView);
        fixSizeWindow.setModel(true);
        cleanConceptionKindEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderLoadCSVFormatConceptionEntitiesView(){
        LoadCSVFormatConceptionEntitiesView loadCSVFormatConceptionEntitiesView = new LoadCSVFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.UPLOAD),"导入 CSV 格式概念实体数据",null,true,550,580,false);
        fixSizeWindow.setWindowContent(loadCSVFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        loadCSVFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderLoadSHPFormatConceptionEntitiesView(){
        LoadShpFormatConceptionEntitiesView loadShpFormatConceptionEntitiesView = new LoadShpFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.UPLOAD),"导入 SHP 格式概念实体数据",null,true,550,580,false);
        fixSizeWindow.setWindowContent(loadShpFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        loadShpFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedConceptionEntitiesCreatedEvent(ConceptionEntitiesCreatedEvent event) {
        if(event.getConceptionKindName() != null){
            if(this.conceptionKindName.equals(event.getConceptionKindName())){
                this.conceptionEntitiesCount = this.conceptionEntitiesCount + event.getNewConceptionEntitiesCount();
                this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.conceptionEntitiesCount));
            }
        }
    }

    @Override
    public void receivedConceptionKindCleanedEvent(ConceptionKindCleanedEvent event) {
        if(event.getConceptionKindName() != null){
            if(this.conceptionKindName.equals(event.getConceptionKindName())){
                this.conceptionEntitiesCount = 0;
                this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.conceptionEntitiesCount));
            }
        }
    }

    @Override
    public void receivedConceptionEntitiesCountRefreshEvent(ConceptionEntitiesCountRefreshEvent event) {
        if(event.getConceptionKindName() != null){
            if(this.conceptionKindName.equals(event.getConceptionKindName())){
                this.conceptionEntitiesCount = event.getConceptionEntitiesCount();
                this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.conceptionEntitiesCount));
            }
        }
    }
}
