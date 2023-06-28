package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind;

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
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndexMaintain.KindIndexConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities.*;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.CleanRelationKindEntitiesView;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView.KindType.RelationKind;

public class RelationKindEntitiesConfigurationView extends VerticalLayout {
    private String relationKindName;

    private String conceptionKindName;
    private long conceptionEntitiesCount;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionEntitiesCountDisplayItem;
    private MetaConfigItemsConfigView metaConfigItemsConfigView;
    private KindIndexConfigView kindIndexConfigView;
    private ClassificationConfigView classificationConfigView;
    private Button addConceptionKindScopeAttributeButton;

    public RelationKindEntitiesConfigurationView(String relationKindName){
        this.conceptionKindName = relationKindName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(LineAwesomeIconsSvg.BATTLE_NET.create(),"关系类型实体配置");
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
            conceptionEntitiesCount = coreRealm.getRelationKind(this.conceptionKindName).countRelationEntities();
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionEntitiesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"关系实体数量:",this.numberFormat.format(conceptionEntitiesCount));

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30, Unit.PIXELS);
        infoContainer.add(horSpaceDiv);

        addConceptionKindScopeAttributeButton= new Button("添加关系类型全局属性");
        addConceptionKindScopeAttributeButton.setIcon(VaadinIcon.TEXT_INPUT.create());
        addConceptionKindScopeAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addConceptionKindScopeAttributeButton);
        addConceptionKindScopeAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddRelationKindGlobalAttributeView();
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
        MenuItem importDataMenu = createIconItem(importMenuBar, VaadinIcon.UPLOAD, "导入关系实体数据", null);
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
        MenuItem exportDataMenu = createIconItem(exportMenuBar, VaadinIcon.DOWNLOAD, "导出关系实体数据", null);
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

        Icon cleanKindIcon = new Icon(VaadinIcon.RECYCLE);
        cleanKindIcon.setSize("21px");
        Button cleanConceptionKindButton = new Button("清除关系类型实例",cleanKindIcon, event -> {});
        cleanConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cleanConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        Tooltips.getCurrent().setTooltip(cleanConceptionKindButton, "清除关系类型所有实例");
        cleanConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCleanConceptionKindEntitiesUI();
            }
        });
        infoContainer.add(cleanConceptionKindButton);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"关系类型组件运行时配置");
        filterTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(filterTitle2);

        metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.RelationKind,this.conceptionKindName);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(metaConfigItemsConfigView);

        kindIndexConfigView = new KindIndexConfigView(KindIndexConfigView.KindIndexType.RelationKind,this.conceptionKindName);
        kindIndexConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(kindIndexConfigView);

        classificationConfigView = new ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType.RelationKind,this.conceptionKindName);
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

    private void renderAddRelationKindGlobalAttributeView(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.conceptionKindName,null,RelationKind);
        addEntityAttributeView.setKindScopeAttribute(true);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TEXT_INPUT),"添加关系类型全局属性",null,true,480,200,false);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderCleanConceptionKindEntitiesUI(){
        CleanRelationKindEntitiesView cleanRelationKindEntitiesView = new CleanRelationKindEntitiesView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.RECYCLE),"清除关系类型所有实例",null,true,600,210,false);
        fixSizeWindow.setWindowContent(cleanRelationKindEntitiesView);
        fixSizeWindow.setModel(true);
        cleanRelationKindEntitiesView.setContainerDialog(fixSizeWindow);
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
        //ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }



}
