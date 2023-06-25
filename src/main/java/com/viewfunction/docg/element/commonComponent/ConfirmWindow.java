package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class ConfirmWindow extends Dialog {

    public ConfirmWindow(Icon titleIcon, String titleContent,String messageContent,List<Button> actionButtons,int windowWidth, int windowHeight){
        this.setModal(true);
        this.setResizable(false);
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        this.setDraggable(true);

        if(windowWidth == 0 || windowHeight == 0){
            this.setSizeFull();
        }else{
            this.setWidth(windowWidth, Unit.PIXELS);
            this.setHeight(windowHeight, Unit.PIXELS);
        }

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        titleElementsContainer.setSpacing(false);
        titleElementsContainer.setMargin(false);
        titleElementsContainer.setWidth(100, Unit.PERCENTAGE);
        titleElementsContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        if(titleIcon != null){
            titleIcon.setSize("18px");
            titleIcon.getStyle().set("color","var(--lumo-primary-color)")
                    .set("padding-right", "5px");
            titleElementsContainer.add(titleIcon);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.AUTO,titleIcon);
        }

        NativeLabel titleLabel = new NativeLabel(titleContent);
        titleLabel.getStyle().set("font-size","var(--lumo-font-size-m)").set("color","var(--lumo-primary-color)");

        titleElementsContainer.add(titleLabel);
        titleElementsContainer.setFlexGrow(1,titleLabel);
        titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        Button closeButton = new Button(closeIcon, e -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        titleElementsContainer.add(closeButton);
        titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.START,closeButton);

        this.add(titleElementsContainer);

        H5 viewTitle = new H5(messageContent);
        viewTitle.getStyle().
                set("padding-bottom", "20px").
                set("padding-top", "20px").
                set("padding-left", "5px").
                set("padding-right", "5px");
        add(viewTitle);

        VerticalLayout footerContainer = new VerticalLayout();
        footerContainer.setWidth(100,Unit.PERCENTAGE);
        footerContainer.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        footerContainer.getStyle()
                .set("border-top", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-top", "var(--lumo-space-m)");
        add(footerContainer);

        HorizontalLayout footerElementsContainer = new HorizontalLayout();
        footerContainer.add(footerElementsContainer);
        if(actionButtons != null){
            for(Button currentButton:actionButtons){
                footerElementsContainer.add(currentButton);
                footerElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,currentButton);
            }
        }
    }

    public void closeConfirmWindow(){
            this.close();
    }
}
