package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;

public class SectionWallContainer extends Details {

    public SectionWallContainer(Component summary, Component content){
        super(summary,content);
        this.getStyle().set("width","100%");
        this.setOpened(true);
        this.addThemeVariants(DetailsVariant.FILLED);
        this.addClassNames("shadow-xs");
    }

}
