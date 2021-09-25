package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SecondaryKeyValueDisplayItem {

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, String keyText, String valueText){
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-medium","text-secondary");
        containComponent.add(conceptionEntityNumberText);
        Label conceptionEntityNumberValue = new Label(valueText);
        conceptionEntityNumberValue.addClassNames("text-s","text-primary","font-extrabold","border-b","border-contrast-20");
        containComponent.add(conceptionEntityNumberValue);
    }

    public SecondaryKeyValueDisplayItem(HasComponents containComponent, Icon icon,String keyText, String valueText){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        if(icon != null){
            icon.setSize("8px");
            horizontalLayout.add(icon);
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(3, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
        }

        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-medium","text-secondary");
        horizontalLayout.add(conceptionEntityNumberText);
        containComponent.add(horizontalLayout);
        Label conceptionEntityNumberValue = new Label(valueText);
        conceptionEntityNumberValue.addClassNames("text-s","text-primary","font-extrabold","border-b","border-contrast-20");
        containComponent.add(conceptionEntityNumberValue);
    }
}
