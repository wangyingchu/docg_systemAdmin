package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class ThirdLevelTitleActionBar extends HorizontalLayout {
    private Label titleLabel;

    public ThirdLevelTitleActionBar(Icon titleIcon, String titleContent, List<Component> thirdLevelTitleComponentsList, List<Component> actionComponentsList){

        this.setSpacing(false);
        this.setMargin(false);

        this.setWidth(100, Unit.PERCENTAGE);
        if(titleIcon != null){
            titleIcon.setSize("14px");
            titleIcon.getStyle()
                    .set("color","#2e4e7e")
                    .set("padding-right", "5px");
            this.add(titleIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,titleIcon);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();

        titleLabel = new Label(titleContent);
        titleLabel.addClassNames("text-xs","text-tertiary");
        titleElementsContainer.add(titleLabel);

        this.add(titleElementsContainer);
        this.setFlexGrow(1,titleElementsContainer);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        if(thirdLevelTitleComponentsList != null){
            for(Component currentComponent:thirdLevelTitleComponentsList){
                titleElementsContainer.add(currentComponent);
            }
        }

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

    public void updateTitleContent(String titleContent){
        this.titleLabel.setText(titleContent);
    }
}