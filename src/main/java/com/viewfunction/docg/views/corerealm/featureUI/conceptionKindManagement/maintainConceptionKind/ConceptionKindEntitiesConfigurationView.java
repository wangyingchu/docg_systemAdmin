package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.Text;
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
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;

import java.text.NumberFormat;

public class ConceptionKindEntitiesConfigurationView extends VerticalLayout {

    private String conceptionKindName;

    public ConceptionKindEntitiesConfigurationView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
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

        Button addConceptionEntityButton= new Button("添加概念类型实体");
        addConceptionEntityButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        addConceptionEntityButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_LARGE,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addConceptionEntityButton);

        Button addConceptionKindScopeAttributeButton= new Button("添加概念类型全局属性");
        addConceptionKindScopeAttributeButton.setIcon(VaadinIcon.TEXT_INPUT.create());
        addConceptionKindScopeAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_LARGE,ButtonVariant.LUMO_TERTIARY);
        infoContainer.add(addConceptionKindScopeAttributeButton);

        Icon divIcon = VaadinIcon.LINE_V.create();
        infoContainer.add(divIcon);

        MenuBar importMenuBar = new MenuBar();
        importMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_LARGE);
        MenuItem importDataMenu = createIconItem(importMenuBar, VaadinIcon.DOWNLOAD, "导入概念实体数据", null);
        SubMenu importSubItems = importDataMenu.getSubMenu();
        MenuItem csvImportItem = importSubItems.addItem("CSV 格式数据");
        MenuItem arrowImportItem = importSubItems.addItem("ARROW 格式数据");
        infoContainer.add(importMenuBar);

        MenuBar exportMenuBar = new MenuBar();
        exportMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_LARGE);
        MenuItem exportDataMenu = createIconItem(exportMenuBar, VaadinIcon.UPLOAD, "导出概念实体数据", null);
        SubMenu exportSubItems = exportDataMenu.getSubMenu();
        MenuItem csvExportItem = exportSubItems.addItem("CSV 格式数据");
        MenuItem arrowExportItem = exportSubItems.addItem("ARROW 格式数据");
        infoContainer.add(exportMenuBar);
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
}
