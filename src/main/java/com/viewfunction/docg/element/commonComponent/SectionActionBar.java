package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class SectionActionBar extends HorizontalLayout {

    public SectionActionBar(Icon sectionIcon, String sectionTitle, List<Component> actionComponentsList){

        this.setWidth(100, Unit.PERCENTAGE);
        this.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        if(sectionIcon != null){
            sectionIcon.setSize("16px");
            this.add(sectionIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,sectionIcon);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        NativeLabel sectionTitleLabel = new NativeLabel(sectionTitle);
        sectionTitleLabel.addClassNames("text-s","font-semibold");
        sectionTitleLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");

        titleElementsContainer.add(sectionTitleLabel);

        this.add(titleElementsContainer);
        this.setFlexGrow(1,titleElementsContainer);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        if(actionComponentsList != null){
            for(int i =0; i < actionComponentsList.size(); i++){
                Component currentComponent = actionComponentsList.get(i);

                this.add(currentComponent);
                this.setVerticalComponentAlignment(Alignment.START,currentComponent);
                if(i != actionComponentsList.size()-1) {
                    Icon divIcon = new Icon(VaadinIcon.LINE_V);
                    divIcon.setSize("8px");
                    this.add(divIcon);
                    this.setVerticalComponentAlignment(Alignment.CENTER, divIcon);
                }

            }
        }
    }

    public SectionActionBar(com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.Icon sectionIcon, String sectionTitle, List<Component> actionComponentsList){

        this.setWidth(100, Unit.PERCENTAGE);
        this.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        if(sectionIcon != null){
            sectionIcon.setSize("16px");
            this.add(sectionIcon);
            this.setVerticalComponentAlignment(Alignment.CENTER,sectionIcon);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        NativeLabel sectionTitleLabel = new NativeLabel(sectionTitle);
        sectionTitleLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");

        titleElementsContainer.add(sectionTitleLabel);

        this.add(titleElementsContainer);
        this.setFlexGrow(1,titleElementsContainer);
        this.setVerticalComponentAlignment(Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        if(actionComponentsList != null){
            for(int i =0; i < actionComponentsList.size(); i++){
                Component currentComponent = actionComponentsList.get(i);

                this.add(currentComponent);
                this.setVerticalComponentAlignment(Alignment.START,currentComponent);
                if(i != actionComponentsList.size()-1) {
                    Icon divIcon = new Icon(VaadinIcon.LINE_V);
                    divIcon.setSize("8px");
                    this.add(divIcon);
                    this.setVerticalComponentAlignment(Alignment.CENTER, divIcon);
                }

            }
        }
    }
}
