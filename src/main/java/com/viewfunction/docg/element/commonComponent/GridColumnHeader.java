package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GridColumnHeader extends HorizontalLayout {
    public GridColumnHeader(VaadinIcon titleIcon, String titleContent){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon headerTitleIcon = new Icon(titleIcon);
        headerTitleIcon.setSize("14px");
        this.add(headerTitleIcon);
        Label headerTitleLabel = new Label(titleContent);
        headerTitleLabel.addClassNames("text-xs","font-semibold");
        this.add(headerTitleLabel);
    }
}
