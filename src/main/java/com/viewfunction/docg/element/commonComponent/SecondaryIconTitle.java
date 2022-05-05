package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SecondaryIconTitle extends HorizontalLayout {

    public SecondaryIconTitle(Icon titleIcon,String titleText){
        titleIcon.setSize("10px");
        add(titleIcon);
        Label conceptionEntityNumberText = new Label(titleText+":");
        conceptionEntityNumberText.addClassNames("text-xs","font-semibold","text-secondary");
        add(conceptionEntityNumberText);
    }
}
