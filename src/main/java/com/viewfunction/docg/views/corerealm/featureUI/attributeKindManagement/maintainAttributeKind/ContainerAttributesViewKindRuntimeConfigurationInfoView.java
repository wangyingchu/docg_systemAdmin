package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.ContainerConceptionKindsConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.ContainsAttributeKindsConfigView;

public class ContainerAttributesViewKindRuntimeConfigurationInfoView extends VerticalLayout {

    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;

    private String attributeKindUID;
    public ContainerAttributesViewKindRuntimeConfigurationInfoView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;
        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        mainContainerLayout.add(leftSideContainerLayout);
        Icon icon = new Icon(VaadinIcon.CUBE);
        icon.setSize("8px");
        SectionActionBar sectionActionBar1 = new SectionActionBar(icon,"相关概念类型配置管理",null);
        leftSideContainerLayout.add(sectionActionBar1);
        //containerConceptionKindsConfigView = new ContainerConceptionKindsConfigView(this.attributesViewKindUID);
        //leftSideContainerLayout.add(containerConceptionKindsConfigView);


        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(100, Unit.PERCENTAGE);
        mainContainerLayout.add(rightSideContainerLayout);
        Icon icon2 = new Icon(VaadinIcon.INPUT);
        SectionActionBar sectionActionBar2 = new SectionActionBar(icon2,"包含属性类型配置管理",null);
        rightSideContainerLayout.add(sectionActionBar2);
        //containsAttributeKindsConfigView = new ContainsAttributeKindsConfigView(this.attributesViewKindUID);
        //rightSideContainerLayout.add(containsAttributeKindsConfigView);

        mainContainerLayout.setFlexGrow(0.5,leftSideContainerLayout);
        mainContainerLayout.setFlexGrow(0.5,rightSideContainerLayout);
    }
}
