package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class SecondaryTitleActionBar extends HorizontalLayout {

    private NativeLabel titleLabel;

    public SecondaryTitleActionBar(Icon titleIcon, String titleContent, List<Component> secondaryTitleComponentsList, List<Component> actionComponentsList){

        this.setSpacing(false);
        this.setMargin(false);

        this.setWidth(100, Unit.PERCENTAGE);
        this.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");

        if(titleIcon != null){
            titleIcon.setSize("14px");
            titleIcon.getStyle()
                    //.set("color","var(--lumo-primary-color)")
                    .set("color","#2e4e7e")
                    .set("padding-right", "5px");
            this.add(titleIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,titleIcon);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();

        titleLabel = new NativeLabel(titleContent);
        titleLabel.getStyle().set("font-size","var(--lumo-font-size-m)")
                .set("color","#2e4e7e");
                //.set("color","var(--lumo-primary-color)");

        titleElementsContainer.add(titleLabel);

        this.add(titleElementsContainer);
        this.setFlexGrow(1,titleElementsContainer);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        if(secondaryTitleComponentsList != null){
            for(Component currentComponent:secondaryTitleComponentsList){
                titleElementsContainer.add(currentComponent);
            }
        }

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                this.add(currentComponent);
                this.setVerticalComponentAlignment(Alignment.CENTER,currentComponent);
                Span spacingSpan = new Span();
                spacingSpan.setWidth(10,Unit.PIXELS);
                this.add(spacingSpan);
            }
        }
    }

    public SecondaryTitleActionBar(Icon titleIcon, String titleContent, List<Component> secondaryTitleComponentsList, List<Component> actionComponentsList,boolean showUnderLine){

        this.setSpacing(false);
        this.setMargin(false);

        this.setWidth(100, Unit.PERCENTAGE);
        if(showUnderLine) {
            this.getStyle()
                    .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                    .set("padding-bottom", "var(--lumo-space-s)");
        }

        if(titleIcon != null){
            titleIcon.setSize("14px");
            titleIcon.getStyle()
                    //.set("color","var(--lumo-primary-color)")
                    .set("color","#2e4e7e")
                    .set("padding-right", "5px");
            this.add(titleIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,titleIcon);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();

        titleLabel = new NativeLabel(titleContent);
        titleLabel.getStyle().set("font-size","var(--lumo-font-size-m)")
                .set("color","#2e4e7e");
        //.set("color","var(--lumo-primary-color)");

        titleElementsContainer.add(titleLabel);

        this.add(titleElementsContainer);
        this.setFlexGrow(1,titleElementsContainer);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        if(secondaryTitleComponentsList != null){
            for(Component currentComponent:secondaryTitleComponentsList){
                titleElementsContainer.add(currentComponent);
            }
        }

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                this.add(currentComponent);
                this.setVerticalComponentAlignment(Alignment.CENTER,currentComponent);
                Span spacingSpan = new Span();
                spacingSpan.setWidth(10,Unit.PIXELS);
                this.add(spacingSpan);
            }
        }
    }

    public void updateTitleContent(String titleContent){
        this.titleLabel.setText(titleContent);
    }
}
