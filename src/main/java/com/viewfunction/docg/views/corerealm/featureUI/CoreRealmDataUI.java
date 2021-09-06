package com.viewfunction.docg.views.corerealm.featureUI;

import ch.carnet.kasparscherrer.VerticalScrollLayout;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.element.commonComponent.SectionWallContainer;
import com.viewfunction.docg.element.commonComponent.SectionWallTitle;
import com.viewfunction.docg.element.commonComponent.chart.ChartGenerator;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class CoreRealmDataUI extends VerticalLayout {

    private Registration listener;

    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout sectionContentContainerLayout;

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

        leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setSpacing(false);
        leftSideContentContainerLayout.setWidth(400, Unit.PIXELS);
        leftSideContentContainerLayout.addClassNames("border-r","border-contrast-20");
        contentContainerLayout.add(leftSideContentContainerLayout);

        HorizontalLayout coreRealmInfoContainerLayout = new HorizontalLayout();
        coreRealmInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        Icon icon = new Icon(VaadinIcon.AUTOMATION); //Icon icon = new Icon("lumo", "photo");
        //leftSideContentContainerLayout.add(FontAwesome.Solid.ADDRESS_CARD.create());
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据概览信息",null);
        leftSideContentContainerLayout.add(sectionActionBar);

        VerticalScrollLayout leftSideSectionContainerScrollLayout = new VerticalScrollLayout();
        leftSideContentContainerLayout.add(leftSideSectionContainerScrollLayout);

        Icon conceptionKindInfoTitleIcon = new Icon(VaadinIcon.CUBE);
        conceptionKindInfoTitleIcon.setSize("20px");
        Label conceptionKindInfoTitleLabel = new Label("ConceptionKind-概念类型");
        SectionWallTitle conceptionKindInfoSectionWallTitle = new SectionWallTitle(conceptionKindInfoTitleIcon,conceptionKindInfoTitleLabel);
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setWidth(100,Unit.PERCENTAGE);
        verticalLayout1.setHeight(400,Unit.PIXELS);
        verticalLayout1.add(ChartGenerator.generateApexChartsLineChart());
        SectionWallContainer sectionWallContainer1 = new SectionWallContainer(conceptionKindInfoSectionWallTitle,verticalLayout1);
        leftSideSectionContainerScrollLayout.add(sectionWallContainer1);


        Icon relationKindInfoTitleIcon = new Icon(VaadinIcon.CONNECT_O);
        relationKindInfoTitleIcon.setSize("20px");
        Label relationKindInfoTitleLabel = new Label("RelationKind-关系类型");
        SectionWallTitle relationKindInfoSectionWallTitle = new SectionWallTitle(relationKindInfoTitleIcon,relationKindInfoTitleLabel);
        SectionWallContainer sectionWallContainer2 = new SectionWallContainer(relationKindInfoSectionWallTitle,ChartGenerator.generateSOChartTreeChart());
        leftSideSectionContainerScrollLayout.add(sectionWallContainer2);


        VerticalLayout _VerticalLayout = new VerticalLayout();
        _VerticalLayout.add(ChartGenerator.generateChartJSBarChart());
        leftSideSectionContainerScrollLayout.add(_VerticalLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-185,Unit.PIXELS);
            //this.sectionContentContainerLayout.setHeight(event.getHeight()-200,Unit.PIXELS);

        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-185,Unit.PIXELS);
            //this.sectionContentContainerLayout.setHeight(browserHeight-200,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
