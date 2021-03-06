package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PrimaryKeyValueDisplayItem {

    public PrimaryKeyValueDisplayItem(HasComponents containComponent, String keyText, String valueText){
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        containComponent.add(conceptionEntityNumberText);
        Label conceptionEntityNumberValue = new Label(valueText);
        conceptionEntityNumberValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-20");
        conceptionEntityNumberValue.getStyle().set("color","#2e4e7e");
        containComponent.add(conceptionEntityNumberValue);
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

    public PrimaryKeyValueDisplayItem(HasComponents containComponent, IronIcon icon, String keyText, String valueText){
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
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        keyHorizontalLayout.add(conceptionEntityNumberText);
        containComponent.add(keyHorizontalLayout);
        Label conceptionEntityNumberValue = new Label(valueText);
        conceptionEntityNumberValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-20");
        conceptionEntityNumberValue.getStyle().set("color","#2e4e7e");
        containComponent.add(conceptionEntityNumberValue);
    }
}
