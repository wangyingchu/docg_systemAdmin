package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class SectionActionBar extends HorizontalLayout {

    public SectionActionBar(Icon sectionIcon, String sectionTitle, List<Component> actionComponentsList){

        this.setWidth(100, Unit.PERCENTAGE);
        getStyle().set("padding-bottom", "5px");
        this.addClassNames("border-contrast-20","border-b");

        if(sectionIcon != null){
            sectionIcon.setSize("16px");
            this.add(sectionIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,sectionIcon);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        Label sectionTitleLabel = new Label(sectionTitle);
        sectionTitleLabel.addClassNames("text-s","font-semibold");

        titleElementsContainer.add(sectionTitleLabel);

        this.add(titleElementsContainer);
        this.setFlexGrow(1,titleElementsContainer);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                this.add(currentComponent);
                this.setVerticalComponentAlignment(Alignment.START,currentComponent);
                Icon divIcon = new Icon(VaadinIcon.LINE_V);
                divIcon.setSize("8px");
                this.add(divIcon);
                this.setVerticalComponentAlignment(Alignment.CENTER,divIcon);
            }
        }
    }
}
