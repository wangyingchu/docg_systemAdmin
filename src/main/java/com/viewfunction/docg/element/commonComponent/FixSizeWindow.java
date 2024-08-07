package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class FixSizeWindow extends Dialog {

    private VerticalLayout windowsContentContainerLayout;
    private Button closeButton;

    public FixSizeWindow(Icon titleIcon, String titleContent, List<Component> actionComponentsList,boolean displayCloseButton,int windowWidth, int windowHeight,boolean isResizable){
        this.setModal(false);
        this.setResizable(isResizable);
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        this.setDraggable(true);
        if(windowWidth == 0 || windowHeight == 0){
            this.setSizeFull();
        }else{
            this.setWidth(windowWidth,Unit.PIXELS);
            this.setHeight(windowHeight, Unit.PIXELS);
        }
        //this.addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        titleElementsContainer.setSpacing(false);
        titleElementsContainer.setMargin(false);
        titleElementsContainer.setWidth(100, Unit.PERCENTAGE);
        titleElementsContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        if(titleIcon != null){
            titleIcon.setSize("18px");
            titleIcon.getStyle()
                    //.set("color","var(--lumo-primary-color)")
                    .set("color","#2e4e7e")
                    .set("padding-right", "5px");
            titleElementsContainer.add(titleIcon);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.AUTO,titleIcon);
        }

        NativeLabel titleLabel = new NativeLabel(titleContent);
        titleLabel.getStyle().set("font-size","var(--lumo-font-size-m)")
                //.set("color","var(--lumo-primary-color)").
                .set("color","#2e4e7e").
                set("font-weight","bold");

        titleElementsContainer.add(titleLabel);
        titleElementsContainer.setFlexGrow(1,titleLabel);
        titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,titleElementsContainer);
        titleElementsContainer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                titleElementsContainer.add(currentComponent);
                titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.START,currentComponent);
                Span spacingSpan = new Span();
                spacingSpan.setWidth(10,Unit.PIXELS);
                titleElementsContainer.add(spacingSpan);
            }
        }
        if(displayCloseButton){
            Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
            closeButton = new Button(closeIcon, e -> this.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            titleElementsContainer.add(closeButton);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.START,closeButton);
        }
        this.add(titleElementsContainer);

        this.windowsContentContainerLayout = new VerticalLayout();
        this.windowsContentContainerLayout.setWidth(100,Unit.PERCENTAGE);
        this.windowsContentContainerLayout.setSpacing(false);
        this.windowsContentContainerLayout.setPadding(false);
        this.windowsContentContainerLayout.setMargin(false);

        this.add(this.windowsContentContainerLayout);
    }

    public void setWindowContent(Component windowContent){
        this.windowsContentContainerLayout.add(windowContent);
        this.windowsContentContainerLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER,windowContent);
    }

    public void setModel(boolean isModelWindow){
        this.setModal(isModelWindow);
    }

    public void show(){
        this.open();
    }

    public void disableCloseButton(){
        if(closeButton != null){
            closeButton.setEnabled(false);
        }
    }

    public void enableCloseButton(){
        if(closeButton != null){
            closeButton.setEnabled(true);
        }
    }
}
