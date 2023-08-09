package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

public class AttributeKindRuntimeConfigurationView extends VerticalLayout {
    private String attributeKindUID;
    public AttributeKindRuntimeConfigurationView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.CUBES),"概念类型实体配置");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);




        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);


        //this.numberFormat = NumberFormat.getInstance();
        //this.conceptionEntitiesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"概念实体数量:",targetAttributeKind.getAttributeKindName());

        HorizontalLayout horSpaceDiv = new HorizontalLayout();
        horSpaceDiv.setWidth(30, Unit.PIXELS);
        infoContainer.add(horSpaceDiv);



    }
}
