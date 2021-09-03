package com.viewfunction.docg.views.corerealm.featureUI;

import com.github.appreciated.card.ClickableCard;
import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.card.label.TitleLabel;

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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.SectionWallContainer;
import com.viewfunction.docg.element.commonComponent.SectionWallTitle;
import com.viewfunction.docg.element.commonComponent.chart.ChartGenerator;
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

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Label coreRealmNameLabel = new Label(" [ Default CoreRealm ]");
        coreRealmNameLabel.getStyle().set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-secondary-text-color)");
        secTitleElementsList.add(coreRealmNameLabel);

        Label coreRealmTechLabel = new Label(" NEO4J 实现");
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

        Icon icon = new Icon(VaadinIcon.AUTOMATION); //Icon icon = new Icon("lumo", "photo");
        //leftSideContentContainerLayout.add(FontAwesome.Solid.ADDRESS_CARD.create());
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据概览信息",null);
        leftSideContentContainerLayout.add(sectionActionBar);

        Icon conceptionKindInfoTitleIcon = new Icon(VaadinIcon.CUBE);
        conceptionKindInfoTitleIcon.setSize("20px");
        Label conceptionKindInfoTitleLabel = new Label("ConceptionKind-概念类型");
        SectionWallTitle conceptionKindInfoSectionWallTitle = new SectionWallTitle(conceptionKindInfoTitleIcon,conceptionKindInfoTitleLabel);

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setWidth(100,Unit.PERCENTAGE);
        verticalLayout1.setHeight(400,Unit.PIXELS);
        verticalLayout1.add(ChartGenerator.generateApexChartsLineChart());
        leftSideContentContainerLayout.add(verticalLayout1);

        SectionWallContainer sectionWallContainer1 = new SectionWallContainer(conceptionKindInfoSectionWallTitle,verticalLayout1);
        leftSideContentContainerLayout.add(sectionWallContainer1);

        //Details component2 = new Details("[Conception Kind] 概念类型 ",
                //new Text("Toggle using mouse, Enter and Space keys."));
        Details component2 = new Details("[Conception Kind] 概念类型 ",
                ChartGenerator.generateSOChartTreeChart());

        component2.getStyle().set("width","100%");
        component2.setOpened(true);
        component2.addThemeVariants(DetailsVariant.FILLED);
        component2.addClassNames("shadow-xs");
        leftSideContentContainerLayout.add(component2);


        RippleClickableCard rcard = new RippleClickableCard(
                onClick -> {/* Handle Card click */},
                new TitleLabel("Example Title") // Follow up with more Components ...
        );

        ClickableCard ccard = new ClickableCard(
                onClick -> {/* Handle Card click */},
                new TitleLabel("Example Title") // Follow up with more Components ...
        );


        leftSideContentContainerLayout.add(ChartGenerator.generateChartJSBarChart());
        leftSideContentContainerLayout.add(ccard);
    }
}
