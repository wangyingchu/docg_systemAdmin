package com.viewfunction.docg.views.corerealm.featureUI;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.theme.lumo.Lumo;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
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

        List<Component> secTitleElementsList = new ArrayList<>();

        Label coreRealmNameLabel = new Label(" [ Default CoreRealm ]");
        coreRealmNameLabel.getStyle().set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-secondary-text-color)");
        secTitleElementsList.add(coreRealmNameLabel);

        Label coreRealmTechLabel = new Label(" NEO4J 实现");
        //coreRealmTechLabel.getStyle()
        //        .set("font-size","var(--lumo-font-size-xxs)");
        coreRealmTechLabel.addClassName("text-2xs");
        secTitleElementsList.add(coreRealmTechLabel);
        coreRealmTechLabel.getElement().getThemeList().add("badge success");


        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Core Realm 领域模型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        VerticalLayout leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setWidth(400, Unit.PIXELS);
        leftSideContentContainerLayout.setHeight(600,Unit.PIXELS);
        leftSideContentContainerLayout.addClassNames("border-r","border-contrast-20");
        contentContainerLayout.add(leftSideContentContainerLayout);

        HorizontalLayout coreRealmInfoContainerLayout = new HorizontalLayout();
        coreRealmInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);

        //Icon icon = new Icon("lumo", "photo");
        Icon icon = new Icon(VaadinIcon.AUTOMATION);

        //Icon icon2 =
                //FontAwesome.Solid.ADDRESS_CARD.create();
        //leftSideContentContainerLayout.add(FontAwesome.Solid.ADDRESS_CARD.create());

        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据概览信息",null);
        leftSideContentContainerLayout.add(sectionActionBar);



        HorizontalLayout conceptionKindInfoContainerLayout = new HorizontalLayout();
        conceptionKindInfoContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon icon001 = new Icon(VaadinIcon.CUBE);
        icon001.setSize("20px");
        conceptionKindInfoContainerLayout.add(icon001);

        Label lb01 = new Label("ConceptionKind-概念类型");
        lb01.addClassNames("text-xs","font-semibold");
        conceptionKindInfoContainerLayout.add(lb01);
        Details component = new Details(conceptionKindInfoContainerLayout,
                new Text("Toggle using mouse, Enter and Space keys."));
        //component.addOpenedChangeListener(e ->
        //        Notification.show(e.isOpened() ? "Opened" : "Closed"));
        component.getStyle().set("width","100%");
        component.setOpened(true);
        component.addThemeVariants(DetailsVariant.FILLED);
        component.addClassNames("shadow-xs");

        leftSideContentContainerLayout.add(component);

        Details component2 = new Details("[Conception Kind] 概念类型 ",
                new Text("Toggle using mouse, Enter and Space keys."));
        //component.addOpenedChangeListener(e ->
        //        Notification.show(e.isOpened() ? "Opened" : "Closed"));
        component2.getStyle().set("width","100%");
        component2.setOpened(true);
        component2.addThemeVariants(DetailsVariant.FILLED);

        leftSideContentContainerLayout.add(component2);





    }
}
