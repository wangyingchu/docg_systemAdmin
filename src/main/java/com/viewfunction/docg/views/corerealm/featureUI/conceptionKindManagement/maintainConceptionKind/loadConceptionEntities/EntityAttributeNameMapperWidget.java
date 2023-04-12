package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;

public class EntityAttributeNameMapperWidget extends VerticalLayout {

    public EntityAttributeNameMapperWidget(String attributeName, List<String> existingKindAttributesList){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout attributeOriginalNameInfo = new HorizontalLayout();
        attributeOriginalNameInfo.setSpacing(false);
        attributeOriginalNameInfo.setMargin(false);
        attributeOriginalNameInfo.setPadding(false);
        attributeOriginalNameInfo.getStyle()
                .set("padding-top","5px");
        attributeOriginalNameInfo.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(attributeOriginalNameInfo);

        Icon icon = LineAwesomeIconsSvg.CIRCLE.create();
        icon.setSize("10px");
        attributeOriginalNameInfo.add(icon);

        Label attributeNameLabel = new Label(attributeName);
        attributeNameLabel.getStyle()
                .set("padding-left","5px")
                .set("font-size","0.8rem")
                .set("font-weight","bold");
        attributeOriginalNameInfo.add(attributeNameLabel);

        ComboBox<String> existingKindAttributesSelect = new ComboBox();
        existingKindAttributesSelect.setWidth(98,Unit.PERCENTAGE);
        existingKindAttributesSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        existingKindAttributesSelect.setItems(existingKindAttributesList);

        existingKindAttributesSelect.setRequired(false);
        existingKindAttributesSelect.setAllowCustomValue(true);
        existingKindAttributesSelect.setPageSize(10);
        add(existingKindAttributesSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(10,Unit.PIXELS);
        spaceDivLayout.setWidth(98,Unit.PERCENTAGE);
        spaceDivLayout.getStyle()
                .set("border-top", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);
    }
}
