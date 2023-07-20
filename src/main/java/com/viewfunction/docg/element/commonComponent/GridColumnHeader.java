package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GridColumnHeader extends HorizontalLayout {
    public GridColumnHeader(VaadinIcon titleIcon, String titleContent){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon headerTitleIcon = new Icon(titleIcon);
        headerTitleIcon.setSize("14px");
        this.add(headerTitleIcon);
        NativeLabel headerTitleLabel = new NativeLabel(titleContent);
        headerTitleLabel.addClassNames("text-xs","font-semibold");
        this.add(headerTitleLabel);
    }

    public GridColumnHeader(com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.Icon titleIcon, String titleContent){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleIcon.setSize("12px");
        this.add(titleIcon);
        NativeLabel headerTitleLabel = new NativeLabel(titleContent);
        headerTitleLabel.addClassNames("text-xs","font-semibold");
        this.add(headerTitleLabel);
    }

    public GridColumnHeader(com.vaadin.flow.component.icon.Icon titleIcon, String titleContent){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleIcon.setSize("12px");
        this.add(titleIcon);
        NativeLabel headerTitleLabel = new NativeLabel(titleContent);
        headerTitleLabel.addClassNames("text-xs","font-semibold");
        this.add(headerTitleLabel);
    }
}
