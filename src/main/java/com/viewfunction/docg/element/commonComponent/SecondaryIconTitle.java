package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SecondaryIconTitle extends HorizontalLayout {

    public SecondaryIconTitle(Icon titleIcon,String titleText){
        if(titleIcon != null){
            titleIcon.setSize("10px");
            add(titleIcon);
        }
        Label titleTextLabel = new Label(titleText+":");
        titleTextLabel.addClassNames("text-xs","font-semibold","text-secondary");
        add(titleTextLabel);
    }

    public SecondaryIconTitle(Icon titleIcon, String titleText, Component actionComponent){
        this.setWidth(100, Unit.PERCENTAGE);
        if(titleIcon != null){
            titleIcon.setSize("10px");
            add(titleIcon);
        }
        Label titleTextLabel = new Label(titleText+":");
        titleTextLabel.addClassNames("text-xs","font-semibold","text-secondary");
        add(titleTextLabel);
        add(actionComponent);
        setFlexGrow(1,titleTextLabel);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }
}
