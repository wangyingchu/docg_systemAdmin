package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;

import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InformationExplorationWidget extends VerticalLayout {

    public InformationExplorationWidget(String question,String explorationQuery){
        this.setWidthFull();

        Icon operationIcon = LineAwesomeIconsSvg.SQUARE.create();
        operationIcon.setSize("12px");
        operationIcon.getStyle().set("padding-right","1px");

        Span explorationQuestionSpan = new Span(question);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now =LocalDateTime.now();
        Span timeSpan = new Span("["+now.format(formatter)+ "] ");

        Icon reRunIcon = new Icon(VaadinIcon.REFRESH);
        reRunIcon.setSize("16px");
        Button reRunButton = new Button(reRunIcon, event -> {
            //renderConceptionKindConfigurationUI(conceptionKindName);
        });
        reRunButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        reRunButton.setTooltipText("重新执行探索");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.add(operationIcon,timeSpan,explorationQuestionSpan);

        Details informationExplorationResultDetails = new Details(horizontalLayout);
        informationExplorationResultDetails.addThemeVariants(DetailsVariant.REVERSE);
        informationExplorationResultDetails.setWidthFull();

        informationExplorationResultDetails.setOpened(true);
        add(informationExplorationResultDetails);
        informationExplorationResultDetails.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        HorizontalLayout explorationQueryControlLayout = new HorizontalLayout();
        explorationQueryControlLayout.setHeight(20, Unit.PIXELS);
        explorationQueryControlLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        explorationQueryControlLayout.getStyle().set("background-color", "var(--lumo-contrast-5pct)");

        Icon explorationQueryIcon = LineAwesomeIconsSvg.DIGITAL_TACHOGRAPH_SOLID.create();
        explorationQueryIcon.getStyle().set("padding-left","5px");
        explorationQueryControlLayout.add(explorationQueryIcon,reRunButton);

        informationExplorationResultDetails.add(explorationQueryControlLayout);

        Popover popover = new Popover();
        popover.setTarget(explorationQueryIcon);
        popover.setWidth("500px");
        popover.setHeight("280px");
        popover.addThemeVariants(PopoverVariant.ARROW,PopoverVariant.LUMO_NO_PADDING);
        popover.setPosition(PopoverPosition.BOTTOM_START);
        popover.setModal(true);
        popover.add(new ExplorationQueryInfoWidget(explorationQuery));
    }
}
