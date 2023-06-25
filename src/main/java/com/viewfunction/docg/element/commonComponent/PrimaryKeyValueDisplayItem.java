package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PrimaryKeyValueDisplayItem {

    private NativeLabel displayValue;

    public PrimaryKeyValueDisplayItem(HasComponents containComponent, String keyText, String valueText){
        NativeLabel conceptionEntityNumberText = new NativeLabel(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        containComponent.add(conceptionEntityNumberText);
        displayValue = new NativeLabel(valueText);
        displayValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-20");
        displayValue.getStyle().set("color","#2e4e7e");
        containComponent.add(displayValue);
    }

    public PrimaryKeyValueDisplayItem(HasComponents containComponent, Icon icon, String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("10px");
            icon.addClassNames("text-secondary");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(5, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }
        addDisplayItemContent(containComponent,horizontalLayout,keyText,valueText);
    }

    public PrimaryKeyValueDisplayItem(HasComponents containComponent, com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.Icon icon, String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("10px");
            icon.addClassNames("text-secondary");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(5, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }
        addDisplayItemContent(containComponent,horizontalLayout,keyText,valueText);
    }

    private void addDisplayItemContent(HasComponents containComponent, HorizontalLayout keyHorizontalLayout, String keyText, String valueText){
        NativeLabel conceptionEntityNumberText = new NativeLabel(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        keyHorizontalLayout.add(conceptionEntityNumberText);
        containComponent.add(keyHorizontalLayout);
        displayValue = new NativeLabel(valueText);
        displayValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-20");
        displayValue.getStyle().set("color","#2e4e7e");
        containComponent.add(displayValue);
    }

    public void updateDisplayValue(String newValue){
        displayValue.setText(newValue);
    }
}
