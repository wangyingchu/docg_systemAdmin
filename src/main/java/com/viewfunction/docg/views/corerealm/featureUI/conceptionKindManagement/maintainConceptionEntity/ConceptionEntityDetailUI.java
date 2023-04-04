package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("conceptionEntityDetailInfo/:conceptionKind/:conceptionEntityUID")
public class ConceptionEntityDetailUI extends VerticalLayout implements BeforeEnterObserver {

    private Dialog containerDialog;
    private VerticalLayout entityFieldsContainer;
    private VerticalLayout entityDetailContainer;
    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntityAttributesEditorHeightOffset = 135;

    public ConceptionEntityDetailUI(){}

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.conceptionKind = beforeEnterEvent.getRouteParameters().get("conceptionKind").get();
        this.conceptionEntityUID = beforeEnterEvent.getRouteParameters().get("conceptionEntityUID").get();
        this.conceptionEntityAttributesEditorHeightOffset = 70;
    }

    public ConceptionEntityDetailUI(String conceptionKind, String conceptionEntityUID){
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
                new ConceptionEntityAttributesEditorView(this.conceptionKind,this.conceptionEntityUID,conceptionEntityAttributesEditorHeightOffset);

        ConceptionEntityIntegratedInfoView conceptionEntityIntegratedInfoView =
                new ConceptionEntityIntegratedInfoView(this.conceptionKind,this.conceptionEntityUID,conceptionEntityAttributesEditorHeightOffset);

        this.entityFieldsContainer.add(conceptionEntityAttributesEditorView);
        this.entityDetailContainer.add(conceptionEntityIntegratedInfoView);

        SplitLayout splitLayout = new SplitLayout(this.entityFieldsContainer, this.entityDetailContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);

        splitLayout.getSecondaryComponent().getElement().getStyle().set("padding-top","0px").set("padding-right","0px");
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
