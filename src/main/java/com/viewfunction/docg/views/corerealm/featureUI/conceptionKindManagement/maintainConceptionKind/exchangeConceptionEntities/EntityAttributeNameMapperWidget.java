package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;

public class EntityAttributeNameMapperWidget extends VerticalLayout {

    private String attributeName;
    private ComboBox<String> attributeMappingSelect;

    public EntityAttributeNameMapperWidget(String attributeName, List<String> existingKindAttributesList){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth(100, Unit.PERCENTAGE);
        this.attributeName = attributeName;

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

        NativeLabel attributeNameLabel = new NativeLabel(attributeName);
        attributeNameLabel.getStyle()
                .set("padding-left","5px")
                .set("font-size","0.8rem")
                .set("font-weight","bold");
        attributeOriginalNameInfo.add(attributeNameLabel);

        attributeMappingSelect = new ComboBox();
        attributeMappingSelect.setWidth(98,Unit.PERCENTAGE);
        attributeMappingSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        attributeMappingSelect.setItems(existingKindAttributesList);
        attributeMappingSelect.setRequired(false);
        attributeMappingSelect.setAllowCustomValue(true);
        attributeMappingSelect.setPageSize(10);
        attributeMappingSelect.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            ListDataProvider dtaProvider = (ListDataProvider) attributeMappingSelect.getDataProvider();
            dtaProvider.getItems().add(customValue);
            dtaProvider.refreshAll();
            attributeMappingSelect.setValue(customValue);
        });
        add(attributeMappingSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(10,Unit.PIXELS);
        spaceDivLayout.setWidth(98,Unit.PERCENTAGE);
        spaceDivLayout.getStyle()
                .set("border-top", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);
    }

    public String getAttributeName(){
        return this.attributeName;
    }

    public String getAttributeMapping(){
        String currentMapping = attributeMappingSelect.getValue();
        return currentMapping != null ? currentMapping : this.attributeName;
    }
}
