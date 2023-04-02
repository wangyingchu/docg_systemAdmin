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

import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classification.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndex.KindIndexConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItem.MetaConfigItemsConfigView;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;

public class ConceptionKindEntitiesConfigurationView extends VerticalLayout {

    private String conceptionKindName;

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
        long conceptionEntitiesCount = 0;
        try {
            conceptionEntitiesCount = coreRealm.getConceptionKind(this.conceptionKindName).countConceptionEntities();
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"概念实体数量:",numberFormat.format(conceptionEntitiesCount));

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

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        infoContainer.add(divIcon);

        MenuBar importMenuBar = new MenuBar();
        importMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem importDataMenu = createIconItem(importMenuBar, VaadinIcon.DOWNLOAD, "导入概念实体数据", null);
        SubMenu importSubItems = importDataMenu.getSubMenu();
        MenuItem csvImportItem = importSubItems.addItem("CSV 格式数据");
        MenuItem arrowImportItem = importSubItems.addItem("ARROW 格式数据");
        MenuItem shpImportItem = importSubItems.addItem("SHP 格式数据");
        infoContainer.add(importMenuBar);

        MenuBar exportMenuBar = new MenuBar();
        exportMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem exportDataMenu = createIconItem(exportMenuBar, VaadinIcon.UPLOAD, "导出概念实体数据", null);
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
                //if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                //    renderCleanConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                //}
            }
        });
        infoContainer.add(cleanConceptionKindButton);

        Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
        deleteKindIcon.setSize("21px");
        Button removeConceptionKindButton = new Button("删除概念类型",deleteKindIcon, event -> {});
        removeConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        removeConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        removeConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Tooltips.getCurrent().setTooltip(removeConceptionKindButton, "删除概念类型");
        removeConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                //    renderRemoveConceptionKindEntitiesUI((EntityStatisticsInfo)entityStatisticsInfo);
                //}
            }
        });
        infoContainer.add(removeConceptionKindButton);

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
        AddConceptionEntityView addConceptionEntityView = new AddConceptionEntityView(this.conceptionKindName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加概念类型实体",null,true,480,210,false);
        fixSizeWindow.setWindowContent(addConceptionEntityView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
