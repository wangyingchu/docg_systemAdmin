package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class SectionWallTitle extends HorizontalLayout {

    public SectionWallTitle(Component iconComponent, Label sectionWallTitleLabel){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.add(iconComponent);
        sectionWallTitleLabel.addClassNames("text-xs","font-semibold");
        this.add(sectionWallTitleLabel);
    }
}
