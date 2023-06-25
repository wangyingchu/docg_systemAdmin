package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ThirdLevelIconTitle extends HorizontalLayout {

    public ThirdLevelIconTitle(Icon titleIcon, String titleText){
        if(titleIcon != null) {
            titleIcon.setSize("8px");
            titleIcon.addClassNames("text-tertiary");
            add(titleIcon);
        }
        NativeLabel titleTextLabel = new NativeLabel(titleText+":");
        titleTextLabel.addClassNames("text-xs","text-tertiary");
        add(titleTextLabel);
    }
}
