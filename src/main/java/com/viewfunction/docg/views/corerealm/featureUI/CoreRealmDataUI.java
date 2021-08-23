package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.TitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class CoreRealmDataUI extends VerticalLayout {

    public CoreRealmDataUI(){

        Button refreshDataButton = new Button("刷新领域数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        //Button plusButton3 = new Button("-",new Icon(VaadinIcon.PLUS));
        //plusButton3.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);
        buttonList.add(refreshDataButton);

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Core Realm 领域模型数据管理",buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        VerticalLayout leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setWidth(500, Unit.PIXELS);

        leftSideContentContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");
        contentContainerLayout.add(leftSideContentContainerLayout);

        HorizontalLayout coreRealmInfoContainerLayout = new HorizontalLayout();

        Label coreRealmNameLabel = new Label("Default CoreRealm");
        coreRealmNameLabel.getStyle().set("font-size","var(--lumo-font-size-xxl)")
                .set("color","var(--lumo-secondary-text-color)");
        coreRealmInfoContainerLayout.add(coreRealmNameLabel);

        Label coreRealmTechLabel = new Label(" NEO4J");
        coreRealmTechLabel.getStyle().set("font-size","var(--lumo-font-size-s)")
                .set("color","var(--lumo-body-text-color)");
        coreRealmInfoContainerLayout.add(coreRealmTechLabel);
        coreRealmInfoContainerLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        leftSideContentContainerLayout.add(coreRealmInfoContainerLayout);
    }
}
