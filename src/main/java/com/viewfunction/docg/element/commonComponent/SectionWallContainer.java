package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class SectionWallContainer extends Details {

    public SectionWallContainer(Component summary, Component content){
        super(summary,content);
        this.getStyle().set("width","100%");
        this.setOpened(true);
        this.addThemeVariants(DetailsVariant.FILLED);
        this.addClassNames(LumoUtility.BorderRadius.SMALL,LumoUtility.BoxShadow.XSMALL);
    }

}
