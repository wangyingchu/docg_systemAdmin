package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class TitleActionBar extends HorizontalLayout {

    public TitleActionBar(Icon titleIcon,String titleContent, List<Component> actionComponentsList){

        this.setSpacing(false);
        this.setMargin(false);

        this.setWidth(100, Unit.PERCENTAGE);
        this.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        if(titleIcon != null){
            titleIcon.setSize("22px");
            titleIcon.getStyle().set("color","var(--lumo-primary-color)")
                    .set("padding-right", "5px");
            this.add(titleIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,titleIcon);
        }

        Label titleLabel = new Label(titleContent);
        titleLabel.getStyle().set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-color)");

        this.add(titleLabel);
        this.setFlexGrow(1,titleLabel);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleLabel);

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
