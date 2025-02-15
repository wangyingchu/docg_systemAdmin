package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FullScreenWindow extends Dialog {

    private VerticalLayout windowsContentContainerLayout;
    private Button closeButton;
    private int closeWindowDelayInMicroSecond; //default 0 seconds
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public interface CloseFullScreenWindowListener {
        void beforeCloseWindow();
    }

    private CloseFullScreenWindowListener closeFullScreenWindowListener;

    public FullScreenWindow(Icon titleIcon, String titleContent,List<Component> titleComponentsList, List<Component> actionComponentsList, boolean displayCloseButton){
        this.setModal(false);
        this.setResizable(false);
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        this.setDraggable(false);
        this.setSizeFull();
        this.addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        HorizontalLayout firstSpaceDivContainer = new HorizontalLayout();
        firstSpaceDivContainer.setWidth(5,Unit.PIXELS);
        titleElementsContainer.add(firstSpaceDivContainer);
        titleElementsContainer.setWidth(100, Unit.PERCENTAGE);
        titleElementsContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)")
                .set("padding-top", "var(--lumo-space-s)");

        if(titleIcon != null){
            titleIcon.setSize("16px");
            //titleIcon.getStyle().set("color","var(--lumo-primary-color)");
            titleIcon.getStyle().set("color","#2e4e7e");
            titleElementsContainer.add(titleIcon);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,titleIcon);
        }

        HorizontalLayout titleElementsLeftContainer = new HorizontalLayout();
        titleElementsLeftContainer.setSpacing(false);
        titleElementsLeftContainer.setPadding(false);
        titleElementsLeftContainer.setMargin(false);

        NativeLabel titleLabel = new NativeLabel(titleContent);
        titleLabel.getStyle().
                set("font-size","var(--lumo-font-size-m)").
                //set("color","var(--lumo-primary-color)").
                        set("color","#2e4e7e").
                set("padding-right","10px").
                set("font-weight","bold");
        titleElementsLeftContainer.add(titleLabel);

        titleElementsContainer.add(titleElementsLeftContainer);
        titleElementsContainer.setFlexGrow(1,titleElementsLeftContainer);
        titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,titleElementsLeftContainer);

        if(titleComponentsList != null){
            for(Component currentComponent:titleComponentsList){
                titleElementsLeftContainer.add(currentComponent);
                titleElementsLeftContainer.setVerticalComponentAlignment(FlexComponent.Alignment.STRETCH,currentComponent);
            }
        }

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                titleElementsContainer.add(currentComponent);
                titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE,currentComponent);
                Span spacingSpan = new Span();
                spacingSpan.setWidth(10,Unit.PIXELS);
                titleElementsContainer.add(spacingSpan);
            }
        }
        if(displayCloseButton){
            Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
            this.closeButton = new Button(closeIcon, e -> this.close());
            this.closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            this.closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            titleElementsContainer.add(this.closeButton);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.START,this.closeButton);
        }
        this.add(titleElementsContainer);

        this.windowsContentContainerLayout = new VerticalLayout();
        this.windowsContentContainerLayout.setWidth(100,Unit.PERCENTAGE);
        this.windowsContentContainerLayout.setSpacing(false);
        this.windowsContentContainerLayout.setPadding(false);
        this.windowsContentContainerLayout.setMargin(false);

        this.add(this.windowsContentContainerLayout);
    }

    public FullScreenWindow(Icon titleIcon, String titleContent,List<Component> titleComponentsList, List<Component> actionComponentsList, CloseFullScreenWindowListener closeFullScreenWindowListener){
        UI currentUI = UI.getCurrent();
        this.setModal(false);
        this.setResizable(false);
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        this.setDraggable(false);
        this.setSizeFull();
        this.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        this.closeFullScreenWindowListener = closeFullScreenWindowListener;

        HorizontalLayout titleElementsContainer = new HorizontalLayout();
        HorizontalLayout firstSpaceDivContainer = new HorizontalLayout();
        firstSpaceDivContainer.setWidth(5,Unit.PIXELS);
        titleElementsContainer.add(firstSpaceDivContainer);
        titleElementsContainer.setWidth(100, Unit.PERCENTAGE);
        titleElementsContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)")
                .set("padding-top", "var(--lumo-space-s)");

        if(titleIcon != null){
            titleIcon.setSize("16px");
            //titleIcon.getStyle().set("color","var(--lumo-primary-color)");
            titleIcon.getStyle().set("color","#2e4e7e");
            titleElementsContainer.add(titleIcon);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,titleIcon);
        }

        HorizontalLayout titleElementsLeftContainer = new HorizontalLayout();
        titleElementsLeftContainer.setSpacing(false);
        titleElementsLeftContainer.setPadding(false);
        titleElementsLeftContainer.setMargin(false);

        NativeLabel titleLabel = new NativeLabel(titleContent);
        titleLabel.getStyle().
                set("font-size","var(--lumo-font-size-m)").
                //set("color","var(--lumo-primary-color)").
                        set("color","#2e4e7e").
                set("padding-right","10px").
                set("font-weight","bold");
        titleElementsLeftContainer.add(titleLabel);

        titleElementsContainer.add(titleElementsLeftContainer);
        titleElementsContainer.setFlexGrow(1,titleElementsLeftContainer);
        titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,titleElementsLeftContainer);

        if(titleComponentsList != null){
            for(Component currentComponent:titleComponentsList){
                titleElementsLeftContainer.add(currentComponent);
                titleElementsLeftContainer.setVerticalComponentAlignment(FlexComponent.Alignment.STRETCH,currentComponent);
            }
        }

        if(actionComponentsList != null){
            for(Component currentComponent:actionComponentsList){
                titleElementsContainer.add(currentComponent);
                titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE,currentComponent);
                Span spacingSpan = new Span();
                spacingSpan.setWidth(10,Unit.PIXELS);
                titleElementsContainer.add(spacingSpan);
            }
        }
        if(this.closeFullScreenWindowListener != null){
            Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
            this.closeButton = new Button(closeIcon);
            this.closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            this.closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            this.closeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(closeWindowDelayInMicroSecond <= 0){
                        closeFullScreenWindowListener.beforeCloseWindow();
                        close();
                    }else{
                        closeFullScreenWindowListener.beforeCloseWindow();
                        executorService.schedule(() -> {
                            currentUI.access(() -> {
                                close();
                            });

                        }, closeWindowDelayInMicroSecond, TimeUnit.MICROSECONDS);
                    }
                };
            });
            titleElementsContainer.add(this.closeButton);
            titleElementsContainer.setVerticalComponentAlignment(FlexComponent.Alignment.START,this.closeButton);
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

    public void setCloseWindowDelayInMicroSecond(int closeWindowDelayInMicroSecond) {
        this.closeWindowDelayInMicroSecond = closeWindowDelayInMicroSecond;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        if(executorService != null){
            executorService.shutdown();
        }
    }
}
