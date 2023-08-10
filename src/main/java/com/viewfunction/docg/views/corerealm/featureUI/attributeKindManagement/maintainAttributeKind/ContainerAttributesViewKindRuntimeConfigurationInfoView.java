package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.SectionActionBar;

public class ContainerAttributesViewKindRuntimeConfigurationInfoView extends HorizontalLayout {

    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private ContainerAttributesViewKindsConfigView containerAttributesViewKindsConfigView;

    private String attributeKindUID;
    public ContainerAttributesViewKindRuntimeConfigurationInfoView(String attributeKindUID){
        setSpacing(false);
        setMargin(false);
        setPadding(false);

        this.attributeKindUID = attributeKindUID;

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        add(leftSideContainerLayout);

        containerAttributesViewKindsConfigView = new ContainerAttributesViewKindsConfigView(this.attributeKindUID);
        leftSideContainerLayout.add(containerAttributesViewKindsConfigView);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(rightSideContainerLayout);
        Icon icon2 = new Icon(VaadinIcon.INPUT);
        SectionActionBar sectionActionBar2 = new SectionActionBar(icon2,"包含属性类型配置管理",null);
        rightSideContainerLayout.add(sectionActionBar2);
    }
}
