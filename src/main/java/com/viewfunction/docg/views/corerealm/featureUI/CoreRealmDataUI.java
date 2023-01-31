package com.viewfunction.docg.views.corerealm.featureUI;

import ch.carnet.kasparscherrer.VerticalScrollLayout;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.*;

import java.util.ArrayList;
import java.util.List;

public class CoreRealmDataUI extends VerticalLayout {

    private Registration listener;

    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout rightSideContentContainerLayout;
    private DataRelationDistributionWidget dataRelationDistributionWidget;

    public CoreRealmDataUI(){

        Button refreshDataButton = new Button("刷新领域数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span("Default CoreRealm"));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

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
        leftSideContentContainerLayout.setWidth(550, Unit.PIXELS);
        leftSideContentContainerLayout.addClassNames("border-r","border-contrast-20");
        contentContainerLayout.add(leftSideContentContainerLayout);

        rightSideContentContainerLayout = new VerticalLayout();
        rightSideContentContainerLayout.setSpacing(false);
        contentContainerLayout.add(rightSideContentContainerLayout);

        HorizontalLayout coreRealmInfoContainerLayout = new HorizontalLayout();
        coreRealmInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        Icon icon = new Icon(VaadinIcon.AUTOMATION);
        //leftSideContentContainerLayout.add(FontAwesome.Solid.ADDRESS_CARD.create());
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"数据概览信息",null);
        leftSideContentContainerLayout.add(sectionActionBar);

        VerticalScrollLayout leftSideSectionContainerScrollLayout = new VerticalScrollLayout();
        leftSideContentContainerLayout.add(leftSideSectionContainerScrollLayout);

        Icon conceptionKindInfoTitleIcon = new Icon(VaadinIcon.CUBE);
        conceptionKindInfoTitleIcon.setSize("18px");
        Label conceptionKindInfoTitleLabel = new Label("ConceptionKind-概念类型");
        SectionWallTitle conceptionKindInfoSectionWallTitle = new SectionWallTitle(conceptionKindInfoTitleIcon,conceptionKindInfoTitleLabel);
        ConceptionKindInfoWidget conceptionKindInfoWidget = new ConceptionKindInfoWidget();
        SectionWallContainer conceptionKindInfoSectionWallContainer = new SectionWallContainer(conceptionKindInfoSectionWallTitle,conceptionKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(conceptionKindInfoSectionWallContainer);
        conceptionKindInfoSectionWallContainer.setOpened(false);

        Icon relationKindInfoTitleIcon = new Icon(VaadinIcon.CONNECT_O);
        relationKindInfoTitleIcon.setSize("18px");
        Label relationKindInfoTitleLabel = new Label("RelationKind-关系类型");
        SectionWallTitle relationKindInfoSectionWallTitle = new SectionWallTitle(relationKindInfoTitleIcon,relationKindInfoTitleLabel);
        RelationKindInfoWidget relationKindInfoWidget = new RelationKindInfoWidget();
        SectionWallContainer relationKindInfoSectionWallContainer = new SectionWallContainer(relationKindInfoSectionWallTitle,relationKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(relationKindInfoSectionWallContainer);
        relationKindInfoSectionWallContainer.setOpened(false);

        Icon classificationInfoTitleIcon = new Icon(VaadinIcon.TAGS);
        classificationInfoTitleIcon.setSize("18px");
        Label classificationInfoTitleLabel = new Label("Classification-分类");
        SectionWallTitle classificationInfoSectionWallTitle = new SectionWallTitle(classificationInfoTitleIcon,classificationInfoTitleLabel);
        ClassificationInfoWidget classificationInfoWidget = new ClassificationInfoWidget();
        SectionWallContainer classificationInSectionWallContainer = new SectionWallContainer(classificationInfoSectionWallTitle,classificationInfoWidget);
        leftSideSectionContainerScrollLayout.add(classificationInSectionWallContainer);
        classificationInSectionWallContainer.setOpened(false);

        Icon geospatialRegionInfoTitleIcon = new Icon(VaadinIcon.GLOBE_WIRE);
        geospatialRegionInfoTitleIcon.setSize("18px");
        Label geospatialRegionInfoTitleLabel = new Label("GeospatialRegion-地理空间区域");
        SectionWallTitle geospatialRegionInfoSectionWallTitle = new SectionWallTitle(geospatialRegionInfoTitleIcon,geospatialRegionInfoTitleLabel);
        GeospatialRegionInfoWidget geospatialRegionInfoWidget = new GeospatialRegionInfoWidget();
        SectionWallContainer geospatialRegionInSectionWallContainer = new SectionWallContainer(geospatialRegionInfoSectionWallTitle,geospatialRegionInfoWidget);
        leftSideSectionContainerScrollLayout.add(geospatialRegionInSectionWallContainer);
        geospatialRegionInSectionWallContainer.setOpened(false);

        Icon timeFlowInfoTitleIcon = new Icon(VaadinIcon.TIMER);
        timeFlowInfoTitleIcon.setSize("18px");
        Label timeFlowInfoTitleLabel = new Label("TimeFlow-时间流");
        SectionWallTitle timeFlowInfoSectionWallTitle = new SectionWallTitle(timeFlowInfoTitleIcon,timeFlowInfoTitleLabel);
        TimeFlowInfoWidget timeFlowInfoWidget = new TimeFlowInfoWidget();
        SectionWallContainer timeFlowInSectionWallContainer = new SectionWallContainer(timeFlowInfoSectionWallTitle,timeFlowInfoWidget);
        leftSideSectionContainerScrollLayout.add(timeFlowInSectionWallContainer);
        timeFlowInSectionWallContainer.setOpened(false);

        Icon attributesViewKindInfoTitleIcon = new Icon(VaadinIcon.TASKS);
        attributesViewKindInfoTitleIcon.setSize("18px");
        Label attributesViewKindInfoTitleLabel = new Label("AttributesViewKind-属性视图类型");
        SectionWallTitle attributesViewKindInfoSectionWallTitle = new SectionWallTitle(attributesViewKindInfoTitleIcon,attributesViewKindInfoTitleLabel);
        AttributeViewKindInfoWidget attributeViewKindInfoWidget = new AttributeViewKindInfoWidget();
        SectionWallContainer attributesViewKindInSectionWallContainer = new SectionWallContainer(attributesViewKindInfoSectionWallTitle, attributeViewKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(attributesViewKindInSectionWallContainer);
        attributesViewKindInSectionWallContainer.setOpened(false);

        Icon attributesKindInfoTitleIcon = new Icon(VaadinIcon.INPUT);
        attributesKindInfoTitleIcon.setSize("18px");
        Label attributesKindInfoTitleLabel = new Label("AttributesKind-属性类型");
        SectionWallTitle attributesKindInfoSectionWallTitle = new SectionWallTitle(attributesKindInfoTitleIcon,attributesKindInfoTitleLabel);
        AttributeKindInfoWidget attributeKindInfoWidget = new AttributeKindInfoWidget();
        SectionWallContainer attributesKindInSectionWallContainer = new SectionWallContainer(attributesKindInfoSectionWallTitle, attributeKindInfoWidget);
        leftSideSectionContainerScrollLayout.add(attributesKindInSectionWallContainer);
        attributesKindInSectionWallContainer.setOpened(false);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(20,Unit.PIXELS);
        leftSideSectionContainerScrollLayout.add(spaceDivLayout);

        Icon icon3 = new Icon(VaadinIcon.SPARK_LINE);
        SecondaryTitleActionBar sectionActionBar3 = new SecondaryTitleActionBar(icon3,"系统运行信息",null,null);
        leftSideSectionContainerScrollLayout.add(sectionActionBar3);

        SystemRuntimeInfoWidget systemRuntimeInfoWidget = new SystemRuntimeInfoWidget();
        leftSideSectionContainerScrollLayout.add(systemRuntimeInfoWidget);

        Icon icon2 = new Icon(VaadinIcon.GRID_SMALL_O);
        SectionActionBar sectionActionBar2 = new SectionActionBar(icon2,"全域数据关联分布",null);
        rightSideContentContainerLayout.add(sectionActionBar2);

        dataRelationDistributionWidget = new DataRelationDistributionWidget();
        rightSideContentContainerLayout.add(dataRelationDistributionWidget);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-185,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(event.getWidth()-580,Unit.PIXELS);
            this.dataRelationDistributionWidget.setHeight(event.getHeight()-220,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-185,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(browserWidth-580,Unit.PIXELS);
            this.dataRelationDistributionWidget.setHeight(browserHeight-220,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
