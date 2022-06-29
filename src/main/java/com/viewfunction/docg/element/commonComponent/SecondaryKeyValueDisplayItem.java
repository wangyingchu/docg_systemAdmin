package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SecondaryKeyValueDisplayItem {

    private Label displayValue;

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, String keyText, String valueText){
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-medium","text-secondary");
        containComponent.add(conceptionEntityNumberText);
        displayValue = new Label(valueText);
        displayValue.addClassNames("text-s","text-primary","font-extrabold","border-b","border-contrast-20");
        containComponent.add(displayValue);
    }

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, Icon icon,String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("8px");
            icon.addClassNames("text-secondary");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(3, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }
        addDisplayItemContent(containComponent,horizontalLayout,keyText,valueText);
    }

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, IronIcon icon, String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("8px");
            icon.addClassNames("text-secondary");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(3, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }
        addDisplayItemContent(containComponent,horizontalLayout,keyText,valueText);
    }

    private void addDisplayItemContent(HasComponents containComponent,HorizontalLayout keyHorizontalLayout,String keyText, String valueText){
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-medium","text-secondary");
        keyHorizontalLayout.add(conceptionEntityNumberText);
        containComponent.add(keyHorizontalLayout);
        displayValue = new Label(valueText);
        displayValue.addClassNames("text-s","text-primary","font-extrabold","border-b","border-contrast-20");
        containComponent.add(displayValue);
    }

    public void updateDisplayValue(String newValue){
        displayValue.setText(newValue);
    }
}
