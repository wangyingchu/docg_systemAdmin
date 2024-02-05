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
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion.AttachConceptionKindEntitiesToGeospatialRegionView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion.AttachConceptionKindEntitiesToTimeFlowView;
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
        MenuItem csvImportItem = importSubItems.addItem("CSV 格式数据");
        csvImportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderLoadCSVFormatConceptionEntitiesView();
            }
        });
        MenuItem arrowImportItem = importSubItems.addItem("ARROW 格式数据");
        arrowImportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderLoadARROWFormatConceptionEntitiesView();
            }
        });
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
        Icon downArrowIcon2 = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon2.setSize("14px");
        exportDataMenu.add(downArrowIcon2);

        SubMenu exportSubItems = exportDataMenu.getSubMenu();
        MenuItem csvExportItem = exportSubItems.addItem("CSV 格式数据");
        csvExportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderDownloadCSVFormatConceptionEntitiesView();
            }
        });

        MenuItem arrowExportItem = exportSubItems.addItem("ARROW 格式数据");
        arrowExportItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderDownloadARROWFormatConceptionEntitiesView();
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
        //action0Label.addClassNames("text-xs","font-semibold","text-secondary");
        action0Layout.add(action0Icon,action0Space,action0Label);

        MenuItem linkTimeItem = linkDataSubItems.addItem(action0Layout);
        linkTimeItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderAttachToGeospatialRegionView();
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
        //action1Label.addClassNames("text-xs","font-semibold","text-secondary");
        action1Layout.add(action1Icon,action1Space,action1Label);

        MenuItem linkGeoItem = linkDataSubItems.addItem(action1Layout);
        linkGeoItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderAttachToTimeFlowView();
            }
        });

        infoContainer.add(linkTimpalAndSpitalInfoMenuBar);

        Icon divIcon3 = VaadinIcon.LINE_V.create();
        divIcon3.setSize("8px");
        infoContainer.add(divIcon3);

        Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
        cleanKindIcon.setSize("21px");
        Button cleanConceptionKindButton = new Button("清除概念类型实例",cleanKindIcon, event -> {});
        cleanConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cleanConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        cleanConceptionKindButton.setTooltipText("清除概念类型所有实例");
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
                //classificationConfigView;
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

    private void renderAttachToGeospatialRegionView(){
        AttachConceptionKindEntitiesToGeospatialRegionView attachConceptionKindEntitiesToGeospatialRegionView =
                new AttachConceptionKindEntitiesToGeospatialRegionView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.GLOBE_WIRE.create(),"链接概念类型实体至地理空间区域",null,true,500,630,false);
        fixSizeWindow.setWindowContent(attachConceptionKindEntitiesToGeospatialRegionView);
        attachConceptionKindEntitiesToGeospatialRegionView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderAttachToTimeFlowView(){
        AttachConceptionKindEntitiesToTimeFlowView attachConceptionKindEntitiesToTimeFlowView =
                new AttachConceptionKindEntitiesToTimeFlowView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.TIMER.create(),"链接概念类型实体至时间流",null,true,500,630,false);
        fixSizeWindow.setWindowContent(attachConceptionKindEntitiesToTimeFlowView);
        attachConceptionKindEntitiesToTimeFlowView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
