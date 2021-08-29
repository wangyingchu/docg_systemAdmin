package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class SectionActionBar extends HorizontalLayout {

    public SectionActionBar(Icon sectionIcon, String sectionTitle, List<Component> actionComponentsList){

        this.setWidth(100, Unit.PERCENTAGE);
        getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "5px");

        Label sectionTitleLabel = new Label(sectionTitle);
        sectionTitleLabel.getStyle().set("font-size","var(--lumo-font-size-s)");

        if(sectionIcon != null){
            sectionIcon.setSize("16px");
            this.add(sectionIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,sectionIcon);
        }

        add(sectionTitleLabel);

        this.setFlexGrow(1,sectionTitleLabel);

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                this.add(currentComponent);
                this.setVerticalComponentAlignment(Alignment.START,currentComponent);
                Span spacingSpan = new Span();
                spacingSpan.setWidth(10,Unit.PIXELS);
                this.add(spacingSpan);
            }
        }
    }
}
