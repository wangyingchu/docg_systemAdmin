package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.MyIconsSvg;

public class EntityAttributeNameMapperWidget extends VerticalLayout {

    public EntityAttributeNameMapperWidget(String attributeName){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth(100, Unit.PERCENTAGE);
        Label attributeNameLabel = new Label(attributeName);

        Icon icon = MyIconsSvg._500PX.create();
        add(icon);

        attributeNameLabel.getStyle()
                .set("font-size","0.8rem")
                .set("font-weight","bold")
                .set("padding-top","15px");
        add(attributeNameLabel);

        ComboBox<String > searchIndexTypeSelect = new ComboBox();
        searchIndexTypeSelect.setWidth(98,Unit.PERCENTAGE);
        searchIndexTypeSelect.setRequired(true);
        searchIndexTypeSelect.setRequiredIndicatorVisible(true);
        searchIndexTypeSelect.setPageSize(10);
        add(searchIndexTypeSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

    }
}
