package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class SectionWallTitle extends HorizontalLayout {

    public SectionWallTitle(Component iconComponent, NativeLabel sectionWallTitleLabel){
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.add(iconComponent);
        sectionWallTitleLabel.addClassNames(LumoUtility.FontSize.XSMALL,"font-semibold");
        this.add(sectionWallTitleLabel);
    }
}
