package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class LightGridColumnHeader extends HorizontalLayout {
    public LightGridColumnHeader(VaadinIcon titleIcon, String titleContent){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon headerTitleIcon = new Icon(titleIcon);
        headerTitleIcon.setSize("12px");
        headerTitleIcon.addClassNames("text-tertiary");
        this.add(headerTitleIcon);
        Label headerTitleLabel = new Label(titleContent);
        headerTitleLabel.addClassNames("text-xs","text-tertiary");
        this.add(headerTitleLabel);
    }
}
