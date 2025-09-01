package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

public class InformationInsightScopeManagementWidget extends VerticalLayout {

    private List<String> insightScopeConceptionKindList;
    private List<String> insightScopeRelationKindList;
    private List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList;
    private MultiSelectListBox<String> conceptionKindSelectListBox;
    private MultiSelectListBox<String> relationKindSelectListBox;
    private MultiSelectListBox<String> conceptionKindCorrelationSelectListBox;

    public InformationInsightScopeManagementWidget(List<String> insightScopeConceptionKindList,
                                                  List<String> insightScopeRelationKindList,
                                                   List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList){
        this.insightScopeConceptionKindList = insightScopeConceptionKindList;
        this.insightScopeRelationKindList = insightScopeRelationKindList;
        this.insightScopeConceptionKindCorrelationList = insightScopeConceptionKindCorrelationList;
        setPadding(true);
        this.setWidthFull();

        List<Component> sectionActionComponentsList = new ArrayList<>();
        SectionActionBar sectionActionBar = new SectionActionBar(LineAwesomeIconsSvg.BUROMOBELEXPERTE.create(),"领域知识洞察范围管理",sectionActionComponentsList);
        add(sectionActionBar);

        HorizontalLayout managementContentContainerLayout = new HorizontalLayout();
        add(managementContentContainerLayout);

        VerticalLayout conceptionKindManagementContainerLayout = new VerticalLayout();
        conceptionKindManagementContainerLayout.setWidth(400, Unit.PIXELS);
        managementContentContainerLayout.add(conceptionKindManagementContainerLayout);

        VerticalLayout relationKindManagementContainerLayout = new VerticalLayout();
        relationKindManagementContainerLayout.setWidth(400, Unit.PIXELS);
        managementContentContainerLayout.add(relationKindManagementContainerLayout);

        VerticalLayout conceptionKindCorrelationManagementContainerLayout = new VerticalLayout();
        conceptionKindCorrelationManagementContainerLayout.setWidth(600, Unit.PIXELS);
        managementContentContainerLayout.add(conceptionKindCorrelationManagementContainerLayout);

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.CUBE),"概念类型范围选择");
        conceptionKindManagementContainerLayout.add(filterTitle1);
        conceptionKindSelectListBox = new MultiSelectListBox<>();
        conceptionKindManagementContainerLayout.add(conceptionKindSelectListBox);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONNECT_O),"关系类型范围选择");
        relationKindManagementContainerLayout.add(filterTitle2);
        relationKindSelectListBox = new MultiSelectListBox<>();
        relationKindManagementContainerLayout.add(relationKindSelectListBox);

        SecondaryIconTitle filterTitle3 = new SecondaryIconTitle(FontAwesome.Solid.CODE_FORK.create(),"概念数据关系范围选择");
        conceptionKindCorrelationManagementContainerLayout.add(filterTitle3);
        conceptionKindCorrelationSelectListBox = new MultiSelectListBox<>();
        conceptionKindCorrelationManagementContainerLayout.add(conceptionKindCorrelationSelectListBox);
    }

    public void refreshScopeManagementUI(){
        conceptionKindSelectListBox.setItems(insightScopeConceptionKindList);
        conceptionKindSelectListBox.select(insightScopeConceptionKindList);
        relationKindSelectListBox.setItems(insightScopeRelationKindList);
        relationKindSelectListBox.select(insightScopeRelationKindList);

        List<String> conceptionRelationList = new ArrayList<>();
        for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:insightScopeConceptionKindCorrelationList){
            String currentRelation =
                    currentConceptionKindCorrelationInfo.getSourceConceptionKindName() + " -["+currentConceptionKindCorrelationInfo.getRelationKindName()+"]-> " + currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
            conceptionRelationList.add(currentRelation);
        }
        conceptionKindCorrelationSelectListBox.setItems(conceptionRelationList);
        conceptionKindCorrelationSelectListBox.select(conceptionRelationList);
    }
}
