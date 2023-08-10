package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;

public class ContainerAttributesViewKindRuntimeConfigurationInfoView extends VerticalLayout {

    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private ContainerAttributesViewKindsConfigView containerAttributesViewKindsConfigView;

    private String attributeKindUID;
    public ContainerAttributesViewKindRuntimeConfigurationInfoView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;

        setSpacing(false);
        setMargin(false);
        setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(750,Unit.PIXELS);
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        mainContainerLayout.add(leftSideContainerLayout);
        containerAttributesViewKindsConfigView = new ContainerAttributesViewKindsConfigView(this.attributeKindUID);
        leftSideContainerLayout.add(containerAttributesViewKindsConfigView);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(400, Unit.PIXELS);
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainContainerLayout.add(rightSideContainerLayout);

        Icon icon2 = new Icon(VaadinIcon.INPUT);
        SectionActionBar sectionActionBar2 = new SectionActionBar(icon2,"包含属性类型配置管理",null);
        rightSideContainerLayout.add(sectionActionBar2);
    }

    public void setViewHeight(int viewHeight){
        containerAttributesViewKindsConfigView.setViewHeight(viewHeight);
    }

    public void setViewWidth(int viewWidth){
        rightSideContainerLayout.setWidth(viewWidth-550,Unit.PIXELS);
    }
}
