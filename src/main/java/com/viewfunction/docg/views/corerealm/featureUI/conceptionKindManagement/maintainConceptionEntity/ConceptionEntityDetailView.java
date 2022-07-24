package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("conceptionEntityDetailInfo/:conceptionKind/:conceptionEntityUID")
public class ConceptionEntityDetailView extends VerticalLayout implements BeforeEnterObserver {

    private Dialog containerDialog;
    private VerticalLayout entityFieldsContainer;
    private VerticalLayout entityDetailContainer;
    private String conceptionKind;
    private String conceptionEntityUID;

    public ConceptionEntityDetailView(){}

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.conceptionKind = beforeEnterEvent.getRouteParameters().get("conceptionKind").get();
        this.conceptionEntityUID = beforeEnterEvent.getRouteParameters().get("conceptionEntityUID").get();;
    }

    public ConceptionEntityDetailView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderView();
    }

    private void renderView(){
        this.entityFieldsContainer = new VerticalLayout();
        this.entityFieldsContainer.setPadding(false);
        this.entityFieldsContainer.setSpacing(false);
        this.entityFieldsContainer.setMargin(false);
        this.entityFieldsContainer.setMinWidth(350, Unit.PIXELS);
        this.entityFieldsContainer.setMaxWidth(500, Unit.PIXELS);
        this.entityDetailContainer = new VerticalLayout();

        ConceptionEntityAttributesEditorView conceptionEntityAttributesEditorView =
                new ConceptionEntityAttributesEditorView(this.conceptionKind,this.conceptionEntityUID);

        this.entityFieldsContainer.add(conceptionEntityAttributesEditorView);
        this.entityDetailContainer.add(new Label("entityDetailContainer"));

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
