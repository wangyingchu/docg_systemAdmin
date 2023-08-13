package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

public class AttributesViewKindRuntimeConfigurationView extends VerticalLayout {
    private String attributesViewKindUID;

    public AttributesViewKindRuntimeConfigurationView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.COG_O),"属性视图类型定义配置");
        add(filterTitle1);
        HorizontalLayout infoContainer0 = new HorizontalLayout();
        infoContainer0.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer0.setWidthFull();
        add(infoContainer0);
    }
}
