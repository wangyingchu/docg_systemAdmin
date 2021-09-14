package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Label;

public class PrimaryKeyValueDisplayItem {

    public PrimaryKeyValueDisplayItem(HasComponents containComponent, String keyText, String valueText){
        Label conceptionEntityNumberText = new Label(keyText);
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        containComponent.add(conceptionEntityNumberText);
        Label conceptionEntityNumberValue = new Label(valueText);
        conceptionEntityNumberValue.addClassNames("text-xl","text-primary","font-extrabold","border-b","border-contrast-20");
        containComponent.add(conceptionEntityNumberValue);
    }
}
