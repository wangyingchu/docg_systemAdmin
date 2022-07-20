package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

public class ConceptionEntityDetailView extends VerticalLayout {

    private Dialog containerDialog;

    private VerticalLayout entityFieldsContainer;
    private VerticalLayout entityDetailContainer;

    public ConceptionEntityDetailView(String conceptionKind,String conceptionEntityUID){
        this.entityFieldsContainer = new VerticalLayout();
        this.entityDetailContainer = new VerticalLayout();


        this.entityFieldsContainer.add(new Label("entityFieldsContainer"));
        this.entityDetailContainer.add(new Label("entityDetailContainer"));

        this.entityFieldsContainer.setMinWidth(250, Unit.PIXELS);

        SplitLayout splitLayout = new SplitLayout(this.entityFieldsContainer, this.entityDetailContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
