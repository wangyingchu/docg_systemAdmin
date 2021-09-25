package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;

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
        if(icon != null){
            icon.setSize("10px");
            containComponent.add(icon);
        }
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-medium","text-secondary");
        containComponent.add(conceptionEntityNumberText);
        Label conceptionEntityNumberValue = new Label(valueText);
        conceptionEntityNumberValue.addClassNames("text-s","text-primary","font-extrabold","border-b","border-contrast-20");
        containComponent.add(conceptionEntityNumberValue);
    }
}
