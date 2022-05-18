package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ThirdLevelIconTitle extends HorizontalLayout {

    public ThirdLevelIconTitle(Icon titleIcon, String titleText){
        titleIcon.setSize("8px");
        add(titleIcon);
        Label conceptionEntityNumberText = new Label(titleText+":");
        conceptionEntityNumberText.addClassNames("text-xxs","font-semibold","text-secondary");
        add(conceptionEntityNumberText);
    }
}
