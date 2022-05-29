package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class FixSizeWindow extends Dialog {

    private SecondaryTitleActionBar titleActionBar;

    public FixSizeWindow(SecondaryTitleActionBar titleActionBar,int windowWidth,int windowHeight){
        this.setModal(false);
        this.setResizable(true);
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        if(windowWidth == 0 || windowHeight == 0){
            this.setSizeFull();
        }else{
            this.setWidth(windowWidth,Unit.PIXELS);
            this.setHeight(windowHeight, Unit.PIXELS);
        }
        this.addThemeVariants(DialogVariant.LUMO_NO_PADDING);




        HorizontalLayout titleContentContainer = new HorizontalLayout();
        Button cancelButton = new Button("Cancel", e -> this.close());
        titleContentContainer.add(cancelButton);
        this.add(titleContentContainer);


        //this.add(titleActionBar);
    }

    public void show(){
        this.open();
    }
}
