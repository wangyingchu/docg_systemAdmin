package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SecondaryKeyValueDisplayItem {

    private NativeLabel displayValue;

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, String keyText, String valueText){
        NativeLabel conceptionEntityNumberText = new NativeLabel(keyText);
        conceptionEntityNumberText.getStyle().set("font-size","var(--lumo-font-size-xs)");
        containComponent.add(conceptionEntityNumberText);
        displayValue = new NativeLabel(valueText);
        displayValue.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight","bolder")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("color","#2e4e7e");
        containComponent.add(displayValue);
    }

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, Icon icon,String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("8px");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(3, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }
        addDisplayItemContent(containComponent,horizontalLayout,keyText,valueText);
    }

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.Icon icon, String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("8px");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(3, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }
        addDisplayItemContent(containComponent,horizontalLayout,keyText,valueText);
    }

    private void addDisplayItemContent(HasComponents containComponent,HorizontalLayout keyHorizontalLayout,String keyText, String valueText){
        NativeLabel conceptionEntityNumberText = new NativeLabel(keyText);
        conceptionEntityNumberText.getStyle().set("font-size","var(--lumo-font-size-s)");
        keyHorizontalLayout.add(conceptionEntityNumberText);
        containComponent.add(keyHorizontalLayout);
        displayValue = new NativeLabel(valueText);
        displayValue.getStyle()
                .set("font-size","var(--lumo-font-size-m)")
                .set("font-weight","bolder")
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("color","#2e4e7e");
        containComponent.add(displayValue);
    }

    public void updateDisplayValue(String newValue){
        displayValue.setText(newValue);
    }
}
