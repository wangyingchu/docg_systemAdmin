package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.SectionWallContainer;
import com.viewfunction.docg.element.commonComponent.SectionWallTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.SystemRuntimeInfoWidget;

import java.util.ArrayList;
import java.util.List;

@Route("coreRealmIntelligentAnalysis/")
public class IntelligentAnalysisView  extends VerticalLayout {

    private VerticalLayout leftSideContainerLayout;

    public IntelligentAnalysisView() {
        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        //leftSideContainerLayout.setPadding(false);
        //leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(500, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");








        List<Component> sectionAction2ComponentsList = new ArrayList<>();
        SectionActionBar sectionActionBar2 = new SectionActionBar(LineAwesomeIconsSvg.CHART_LINE_SOLID.create(),"全域数据实时分布",sectionAction2ComponentsList);
        leftSideContainerLayout.add(sectionActionBar2);


        SystemRuntimeInfoWidget systemRuntimeInfoWidget = new SystemRuntimeInfoWidget();
        leftSideContainerLayout.add(systemRuntimeInfoWidget);

        Icon conceptionKindInfoTitleIcon = new Icon(VaadinIcon.CUBE);
        conceptionKindInfoTitleIcon.setSize("18px");
        NativeLabel conceptionKindInfoTitleLabel = new NativeLabel("ConceptionKind(概念类型) 数据分布");
        conceptionKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle conceptionKindInfoSectionWallTitle = new SectionWallTitle(conceptionKindInfoTitleIcon,conceptionKindInfoTitleLabel);
        SectionWallContainer conceptionKindInfoSectionWallContainer = new SectionWallContainer(conceptionKindInfoSectionWallTitle,new VerticalLayout());
        leftSideContainerLayout.add(conceptionKindInfoSectionWallContainer);

        Icon relationKindInfoTitleIcon = new Icon(VaadinIcon.CONNECT_O);
        relationKindInfoTitleIcon.setSize("18px");
        NativeLabel relationKindInfoTitleLabel = new NativeLabel("RelationKind(关系类型) 数据分布");
        relationKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle relationKindInfoSectionWallTitle = new SectionWallTitle(relationKindInfoTitleIcon,relationKindInfoTitleLabel);
        SectionWallContainer relationKindInfoSectionWallContainer = new SectionWallContainer(relationKindInfoSectionWallTitle,new VerticalLayout());
        leftSideContainerLayout.add(relationKindInfoSectionWallContainer);

        Icon dataRelationInfoTitleIcon = FontAwesome.Solid.CODE_FORK.create();
        relationKindInfoTitleIcon.setSize("18px");
        NativeLabel dataRelationInfoTitleLabel = new NativeLabel("全域数据实时关联 数据分布");
        dataRelationInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle dataRelationInfoSectionWallTitle = new SectionWallTitle(dataRelationInfoTitleIcon,dataRelationInfoTitleLabel);
        SectionWallContainer dataRelationInfoSectionWallContainer = new SectionWallContainer(dataRelationInfoSectionWallTitle,new VerticalLayout());
        leftSideContainerLayout.add(dataRelationInfoSectionWallContainer);

        Span name = new Span("Sophia Williams");
        Span email = new Span("sophia.williams@company.com");
        Span phone = new Span("(501) 555-9128");

        VerticalLayout content = new VerticalLayout(name, email, phone);
        content.setSpacing(false);
        content.setPadding(false);

        Details details = new Details("Contact information", content);
        details.setOpened(true);

        leftSideContainerLayout.add(details);



    }
}
