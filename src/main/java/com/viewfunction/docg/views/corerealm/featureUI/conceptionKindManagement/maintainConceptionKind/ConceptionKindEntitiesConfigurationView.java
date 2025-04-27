package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCreatedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindCleanedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindConfigurationInfoRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndexMaintain.KindIndexConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.CleanConceptionKindEntitiesView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.CleanConceptionKindExclusiveEntitiesView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion.AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion.AttachConceptionKindEntitiesToGeospatialRegionByGeoPropertyView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion.AttachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion.AttachConceptionKindEntitiesToTimeFlowBySingleTimePropertyView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities.*;

import java.text.NumberFormat;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView.KindType.ConceptionKind;

public class ConceptionKindEntitiesConfigurationView extends VerticalLayout implements
        ConceptionEntitiesCreatedEvent.ConceptionEntitiesCreatedListener,
        ConceptionEntitiesCountRefreshEvent.ConceptionEntitiesCountRefreshListener,
        ConceptionKindCleanedEvent.ConceptionKindCleanedListener,
        ConceptionKindConfigurationInfoRefreshEvent.ConceptionKindConfigurationInfoRefreshListener{

    private String conceptionKindName;
    private long conceptionEntitiesCount;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionEntitiesCountDisplayItem;
    private MetaConfigItemsConfigView metaConfigItemsConfigView;
    private KindIndexConfigView kindIndexConfigView;
    private ClassificationConfigView classificationConfigView;
    private Button addConceptionKindScopeAttributeButton;

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

        addConceptionKindScopeAttributeButton= new Button("添加概念类型全局属性");
        addConceptionKindScopeAttributeButton.setIcon(VaadinIcon.TEXT_INPUT.create());
        addConceptionKindScopeAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addConceptionKindScopeAttributeButton);
        addConceptionKindScopeAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddConceptionKindGlobalAttributeView();
            }
        });
        if(conceptionEntitiesCount == 0){
            addConceptionKindScopeAttributeButton.setEnabled(false);
        }

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        infoContainer.add(divIcon);

        MenuBar importMenuBar = new MenuBar();
        importMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem importDataMenu = createIconItem(importMenuBar, VaadinIcon.UPLOAD, "导入概念实体数据", null);
        Icon downArrowIcon1 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon1.setSize("14px");
        importDataMenu.add(downArrowIcon1);

        SubMenu importSubItems = importDataMenu.getSubMenu();

        HorizontalLayout importFromCSVwActionLayout = generateActionLayout(VaadinIcon.FILE_TEXT_O.create(), "CSV 格式数据");
        MenuItem csvImportItem = importSubItems.addItem(importFromCSVwActionLayout);
        csvImportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderLoadCSVFormatConceptionEntitiesView();
            }
        });

        HorizontalLayout importFromArrowActionLayout = generateActionLayout(VaadinIcon.CURSOR_O.create(),"ARROW 格式数据");
        MenuItem arrowImportItem = importSubItems.addItem(importFromArrowActionLayout);
        arrowImportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderLoadARROWFormatConceptionEntitiesView();
            }
        });

        HorizontalLayout importFromShpActionLayout = generateActionLayout(VaadinIcon.MAP_MARKER.create(),"SHP 格式数据");
        MenuItem shpImportItem = importSubItems.addItem(importFromShpActionLayout);
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
        Icon downArrowIcon2 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon2.setSize("14px");
        exportDataMenu.add(downArrowIcon2);

        SubMenu exportSubItems = exportDataMenu.getSubMenu();

        HorizontalLayout exportToCSVwActionLayout = generateActionLayout(VaadinIcon.FILE_TEXT_O.create(), "CSV 格式数据");
        MenuItem csvExportItem = exportSubItems.addItem(exportToCSVwActionLayout);
        csvExportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderDownloadCSVFormatConceptionEntitiesView();
            }
        });

        HorizontalLayout exportToArrowActionLayout = generateActionLayout(VaadinIcon.CURSOR_O.create(),"ARROW 格式数据");
        MenuItem arrowExportItem = exportSubItems.addItem(exportToArrowActionLayout);
        arrowExportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderDownloadARROWFormatConceptionEntitiesView();
            }
        });

        exportSubItems.addSeparator();

        HorizontalLayout syncToDataSliceActionLayout = generateActionLayout(LineAwesomeIconsSvg.BUFFER.create(),"Data Slice 数据切片");
        MenuItem syncToDataSliceItem = exportSubItems.addItem(syncToDataSliceActionLayout);
        SubMenu syncToDataSliceSubItems = syncToDataSliceItem.getSubMenu();

        HorizontalLayout syncToExistingDataSliceActionLayout = generateActionLayout(LineAwesomeIconsSvg.CLONE.create(),"导出至已有数据切片");
        MenuItem syncToExistingDataSliceActionItem = syncToDataSliceSubItems.addItem(syncToExistingDataSliceActionLayout);
        syncToExistingDataSliceActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderSyncConceptionKindEntitiesToExistingDataSliceView();
            }
        });

        HorizontalLayout syncToNewDataSliceActionLayout = generateActionLayout(VaadinIcon.PLUS_SQUARE_O.create(),"导出至新建数据切片");
        MenuItem syncToNewDataSliceActionItem = syncToDataSliceSubItems.addItem(syncToNewDataSliceActionLayout);
        syncToNewDataSliceActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderSyncConceptionKindEntitiesToNewDataSliceView();
            }
        });

        infoContainer.add(exportMenuBar);

        Icon divIcon2 = VaadinIcon.LINE_V.create();
        divIcon2.setSize("8px");
        infoContainer.add(divIcon2);

        MenuBar linkTimpalAndSpitalInfoMenuBar = new MenuBar();
        linkTimpalAndSpitalInfoMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem linkDataMenu = createIconItem(linkTimpalAndSpitalInfoMenuBar, VaadinIcon.LINK, "链接时空信息", null);
        Icon downArrowIcon3 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon3.setSize("14px");
        linkDataMenu.add(downArrowIcon3);

        SubMenu linkDataSubItems = linkDataMenu.getSubMenu();

        HorizontalLayout action0Layout = new HorizontalLayout();
        action0Layout.setPadding(false);
        action0Layout.setSpacing(false);
        action0Layout.setMargin(false);
        action0Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon action0Icon = VaadinIcon.GLOBE_WIRE.create();
        action0Icon.setSize("10px");
        Span action0Space = new Span();
        action0Space.setWidth(6,Unit.PIXELS);
        NativeLabel action0Label = new NativeLabel("链接至地理空间区域");
        action0Layout.add(action0Icon,action0Space,action0Label);

        MenuItem linkGeoItem = linkDataSubItems.addItem(action0Layout);

        HorizontalLayout linkByGeoPropertyActionLayout = new HorizontalLayout();
        linkByGeoPropertyActionLayout.setPadding(false);
        linkByGeoPropertyActionLayout.setSpacing(false);
        linkByGeoPropertyActionLayout.setMargin(false);
        linkByGeoPropertyActionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon actionAIcon = VaadinIcon.INPUT.create();
        actionAIcon.setSize("10px");
        Span action3Space = new Span();
        action3Space.setWidth(6,Unit.PIXELS);
        NativeLabel action3Label = new NativeLabel("根据地理空间属性编码链接");
        action3Label.addClassNames("text-xs","font-semibold","text-secondary");
        linkByGeoPropertyActionLayout.add(actionAIcon,action3Space,action3Label);
        MenuItem linkByGeoPropertyActionItem = linkGeoItem.getSubMenu().addItem(linkByGeoPropertyActionLayout);
        linkByGeoPropertyActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderAttachToGeospatialRegionByGeoPropertyView();
            }
        });

        HorizontalLayout linkByGeoComputeActionLayout = new HorizontalLayout();
        linkByGeoComputeActionLayout.setPadding(false);
        linkByGeoComputeActionLayout.setSpacing(false);
        linkByGeoComputeActionLayout.setMargin(false);
        linkByGeoComputeActionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon actionBIcon = VaadinIcon.CALC.create();
        actionBIcon.setSize("10px");
        Span actionBSpace = new Span();
        actionBSpace.setWidth(6,Unit.PIXELS);
        NativeLabel actionBLabel = new NativeLabel("根据概念实体地理空间计算链接");
        actionBLabel.addClassNames("text-xs","font-semibold","text-secondary");
        linkByGeoComputeActionLayout.add(actionBIcon,actionBSpace,actionBLabel);
        MenuItem linkByGeoComputeActionItem = linkGeoItem.getSubMenu().addItem(linkByGeoComputeActionLayout);
        linkByGeoComputeActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderAttachToGeospatialRegionByGeoComputeView();
            }
        });

        HorizontalLayout action1Layout = new HorizontalLayout();
        action1Layout.setPadding(false);
        action1Layout.setSpacing(false);
        action1Layout.setMargin(false);
        action1Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon action1Icon = VaadinIcon.TIMER.create();
        action1Icon.setSize("10px");
        Span action1Space = new Span();
        action1Space.setWidth(6,Unit.PIXELS);
        NativeLabel action1Label = new NativeLabel("链接至时间流");
        action1Layout.add(action1Icon,action1Space,action1Label);

        MenuItem linkTimeFlowItem = linkDataSubItems.addItem(action1Layout);

        HorizontalLayout linkBySingleTimePropertyActionLayout = new HorizontalLayout();
        linkBySingleTimePropertyActionLayout.setPadding(false);
        linkBySingleTimePropertyActionLayout.setSpacing(false);
        linkBySingleTimePropertyActionLayout.setMargin(false);
        linkBySingleTimePropertyActionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon actionCIcon = VaadinIcon.INPUT.create();
        actionCIcon.setSize("10px");
        Span actionCSpace = new Span();
        actionCSpace.setWidth(6,Unit.PIXELS);
        NativeLabel actionCLabel = new NativeLabel("根据单一时间属性链接");
        actionCLabel.addClassNames("text-xs","font-semibold","text-secondary");
        linkBySingleTimePropertyActionLayout.add(actionCIcon,actionCSpace,actionCLabel);
        MenuItem linkBySingleTimePropertyActionItem = linkTimeFlowItem.getSubMenu().addItem(linkBySingleTimePropertyActionLayout);
        linkBySingleTimePropertyActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderAttachToTimeFlowBySingleTimePropertyView();
            }
        });

        HorizontalLayout linkByMultiTimePropertyActionLayout = new HorizontalLayout();
        linkByMultiTimePropertyActionLayout.setPadding(false);
        linkByMultiTimePropertyActionLayout.setSpacing(false);
        linkByMultiTimePropertyActionLayout.setMargin(false);
        linkByMultiTimePropertyActionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon actionDIcon = VaadinIcon.INPUT.create();
        actionDIcon.setSize("10px");
        Span actionDSpace = new Span();
        actionDSpace.setWidth(6,Unit.PIXELS);
        NativeLabel actionDLabel = new NativeLabel("根据复合时间属性链接");
        actionDLabel.addClassNames("text-xs","font-semibold","text-secondary");
        linkByMultiTimePropertyActionLayout.add(actionDIcon,actionDSpace,actionDLabel);
        MenuItem linkByMultiTimePropertyActionItem = linkTimeFlowItem.getSubMenu().addItem(linkByMultiTimePropertyActionLayout);
        linkByMultiTimePropertyActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderAttachToTimeFlowByMultiTimePropertyView();
            }
        });
        infoContainer.add(linkTimpalAndSpitalInfoMenuBar);

        Icon divIcon3 = VaadinIcon.LINE_V.create();
        divIcon3.setSize("8px");
        infoContainer.add(divIcon3);

        MenuBar cleanConceptionKindsMenuBar = new MenuBar();
        cleanConceptionKindsMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem cleanConceptionKindDataMenu = createIconItem(cleanConceptionKindsMenuBar, VaadinIcon.RECYCLE, "清除概念类型实例", null);
        Icon cleanConceptionKindsIcon3 = new Icon(VaadinIcon.CHEVRON_DOWN);
        cleanConceptionKindsIcon3.setSize("14px");
        cleanConceptionKindDataMenu.add(cleanConceptionKindsIcon3);
        SubMenu cleanConceptionKindDataSubItems = cleanConceptionKindDataMenu.getSubMenu();

        HorizontalLayout action2Layout = new HorizontalLayout();
        action2Layout.setPadding(false);
        action2Layout.setSpacing(false);
        action2Layout.setMargin(false);
        action2Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon action2Icon = VaadinIcon.RECYCLE.create();
        action2Icon.setSize("10px");
        Span action2Space = new Span();
        action2Space.setWidth(6,Unit.PIXELS);
        NativeLabel action2Label = new NativeLabel("清除概念类型所有实例");
        action2Layout.add(action2Icon,action2Space,action2Label);

        MenuItem cleanAllEntitiesItem = cleanConceptionKindDataSubItems.addItem(action2Layout);
        cleanAllEntitiesItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderCleanConceptionKindEntitiesUI();
            }
        });

        HorizontalLayout action3Layout = new HorizontalLayout();
        action3Layout.setPadding(false);
        action3Layout.setSpacing(false);
        action3Layout.setMargin(false);
        action3Layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon action3Icon = VaadinIcon.RECYCLE.create();
        action3Icon.setSize("10px");
        Span action3ASpace = new Span();
        action3ASpace.setWidth(6,Unit.PIXELS);
        NativeLabel action3ALabel = new NativeLabel("清除概念类型独享实例");
        action3Layout.add(action3Icon,action3ASpace,action3ALabel);

        MenuItem cleanExclusiveEntitiesItem = cleanConceptionKindDataSubItems.addItem(action3Layout);
        cleanExclusiveEntitiesItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderCleanConceptionKindExclusiveEntitiesUI();
            }
        });

        infoContainer.add(cleanConceptionKindsMenuBar);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"概念类型组件运行时配置");
        filterTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(filterTitle2);

        metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.ConceptionKind,this.conceptionKindName);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(metaConfigItemsConfigView);

        kindIndexConfigView = new KindIndexConfigView(KindIndexConfigView.KindIndexType.ConceptionKind,this.conceptionKindName);
        kindIndexConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(kindIndexConfigView);

        classificationConfigView = new ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType.ConceptionKind,this.conceptionKindName);
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
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加概念类型实体",null,true,500,670,false);
        fixSizeWindow.setWindowContent(addConceptionEntityView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAddConceptionKindGlobalAttributeView(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.conceptionKindName,null,ConceptionKind);
        addEntityAttributeView.setKindScopeAttribute(true);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TEXT_INPUT),"添加概念类型全局属性",null,true,480,200,false);
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

    private void renderCleanConceptionKindExclusiveEntitiesUI(){
        CleanConceptionKindExclusiveEntitiesView cleanConceptionKindExclusiveEntitiesView = new CleanConceptionKindExclusiveEntitiesView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.RECYCLE),"清除概念类型独享实例",null,true,600,220,false);
        fixSizeWindow.setWindowContent(cleanConceptionKindExclusiveEntitiesView);
        fixSizeWindow.setModel(true);
        cleanConceptionKindExclusiveEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderLoadCSVFormatConceptionEntitiesView(){
        LoadCSVFormatConceptionEntitiesView loadCSVFormatConceptionEntitiesView = new LoadCSVFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.UPLOAD),"导入 CSV 格式概念实体数据",null,true,550,540,false);
        fixSizeWindow.setWindowContent(loadCSVFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        loadCSVFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderLoadSHPFormatConceptionEntitiesView(){
        LoadSHPFormatConceptionEntitiesView loadShpFormatConceptionEntitiesView = new LoadSHPFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.UPLOAD),"导入 SHP 格式概念实体数据",null,true,550,540,false);
        fixSizeWindow.setWindowContent(loadShpFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        loadShpFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderLoadARROWFormatConceptionEntitiesView(){
        LoadARROWFormatConceptionEntitiesView loadARROWFormatConceptionEntitiesView = new LoadARROWFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.UPLOAD),"导入 ARROW 格式概念实体数据",null,true,550,540,false);
        fixSizeWindow.setWindowContent(loadARROWFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        loadARROWFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderDownloadARROWFormatConceptionEntitiesView(){
        DownloadARROWFormatConceptionEntitiesView downloadARROWFormatConceptionEntitiesView = new DownloadARROWFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 ARROW 格式概念实体数据",null,true,550,270,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadARROWFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        downloadARROWFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderDownloadCSVFormatConceptionEntitiesView(){
        DownloadCSVFormatConceptionEntitiesView downloadCSVFormatConceptionEntitiesView = new DownloadCSVFormatConceptionEntitiesView(this.conceptionKindName,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 CSV 格式概念实体数据",null,true,550,270,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadCSVFormatConceptionEntitiesView);
        fixSizeWindow.setModel(true);
        downloadCSVFormatConceptionEntitiesView.setContainerDialog(fixSizeWindow);
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
                if(this.conceptionEntitiesCount > 0){
                    this.addConceptionKindScopeAttributeButton.setEnabled(true);
                }else{
                    this.addConceptionKindScopeAttributeButton.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void receivedConceptionKindCleanedEvent(ConceptionKindCleanedEvent event) {
        if(event.getConceptionKindName() != null){
            if(this.conceptionKindName.equals(event.getConceptionKindName())){
                this.conceptionEntitiesCount = 0;
                this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.conceptionEntitiesCount));
                this.addConceptionKindScopeAttributeButton.setEnabled(false);
            }
        }
    }

    @Override
    public void receivedConceptionEntitiesCountRefreshEvent(ConceptionEntitiesCountRefreshEvent event) {
        if(event.getConceptionKindName() != null){
            if(this.conceptionKindName.equals(event.getConceptionKindName())){
                this.conceptionEntitiesCount = event.getConceptionEntitiesCount();
                this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.conceptionEntitiesCount));
                if(this.conceptionEntitiesCount > 0){
                    this.addConceptionKindScopeAttributeButton.setEnabled(true);
                }else{
                    this.addConceptionKindScopeAttributeButton.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void receivedConceptionKindConfigurationInfoRefreshEvent(ConceptionKindConfigurationInfoRefreshEvent event) {
        if(event.getConceptionKindName() != null){
            if(this.conceptionKindName.equals(event.getConceptionKindName())){
                metaConfigItemsConfigView.refreshMetaConfigItemsInfo();
                kindIndexConfigView.refreshKindIndex();
                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                try {
                    this.conceptionEntitiesCount = targetConceptionKind.countConceptionEntities();
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
                this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(this.conceptionEntitiesCount));
            }
        }
    }

    public void setViewHeight(int viewHeight){
        classificationConfigView.setHeight(viewHeight-500,Unit.PIXELS);
    }

    private void renderAttachToGeospatialRegionByGeoPropertyView(){
        AttachConceptionKindEntitiesToGeospatialRegionByGeoPropertyView attachConceptionKindEntitiesToGeospatialRegionByGeoPropertyView =
                new AttachConceptionKindEntitiesToGeospatialRegionByGeoPropertyView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.GLOBE_WIRE.create(),"链接概念类型实体至地理空间区域",null,true,500,695,false);
        fixSizeWindow.setWindowContent(attachConceptionKindEntitiesToGeospatialRegionByGeoPropertyView);
        attachConceptionKindEntitiesToGeospatialRegionByGeoPropertyView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAttachToGeospatialRegionByGeoComputeView(){
        AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView attachConceptionKindEntitiesToGeospatialRegionByGeoComputeView =
                new AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.GLOBE_WIRE.create(),"链接概念类型实体至地理空间区域",null,true,500,695,false);
        fixSizeWindow.setWindowContent(attachConceptionKindEntitiesToGeospatialRegionByGeoComputeView);
        attachConceptionKindEntitiesToGeospatialRegionByGeoComputeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAttachToTimeFlowBySingleTimePropertyView(){
        AttachConceptionKindEntitiesToTimeFlowBySingleTimePropertyView attachConceptionKindEntitiesToTimeFlowBySingleTimePropertyView =
                new AttachConceptionKindEntitiesToTimeFlowBySingleTimePropertyView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.TIMER.create(),"链接概念类型实体至时间流",null,true,500,695,false);
        fixSizeWindow.setWindowContent(attachConceptionKindEntitiesToTimeFlowBySingleTimePropertyView);
        attachConceptionKindEntitiesToTimeFlowBySingleTimePropertyView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAttachToTimeFlowByMultiTimePropertyView(){
        AttachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView attachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView =
                new AttachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.TIMER.create(),"链接概念类型实体至时间流",null,true,500,860,false);
        fixSizeWindow.setWindowContent(attachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView);
        attachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private HorizontalLayout generateActionLayout(Icon actionIcon,String actionText){
        HorizontalLayout actionLayout = new HorizontalLayout();
        actionLayout.setPadding(false);
        actionLayout.setSpacing(false);
        actionLayout.setMargin(false);
        actionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        actionIcon.setSize("10px");
        Span actionSpace = new Span();
        actionSpace.setWidth(6,Unit.PIXELS);
        NativeLabel actionLabel = new NativeLabel(actionText);
        actionLayout.add(actionIcon,actionSpace,actionLabel);
        return actionLayout;
    }

    private void renderSyncConceptionKindEntitiesToExistingDataSliceView(){
        SyncConceptionEntitiesToExistingDataSliceView syncConceptionEntitiesToExistingDataSliceView = new SyncConceptionEntitiesToExistingDataSliceView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.BUFFER.create(),"导出概念类型实体至 Data Slice 数据切片",null,true,1050,605,false);
        fixSizeWindow.setWindowContent(syncConceptionEntitiesToExistingDataSliceView);
        fixSizeWindow.setModel(true);
        syncConceptionEntitiesToExistingDataSliceView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderSyncConceptionKindEntitiesToNewDataSliceView(){
        SyncConceptionEntitiesToNewDataSliceView syncConceptionEntitiesToNewDataSliceView = new SyncConceptionEntitiesToNewDataSliceView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.BUFFER.create(),"导出概念类型实体至 Data Slice 数据切片",null,true,730,540,false);
        fixSizeWindow.setWindowContent(syncConceptionEntitiesToNewDataSliceView);
        fixSizeWindow.setModel(true);
        syncConceptionEntitiesToNewDataSliceView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
