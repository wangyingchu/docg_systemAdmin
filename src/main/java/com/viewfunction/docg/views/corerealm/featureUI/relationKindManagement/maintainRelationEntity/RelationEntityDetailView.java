package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("relationEntityDetailInfo/:relationKind/:relationEntityUID")
public class RelationEntityDetailView extends VerticalLayout implements BeforeEnterObserver {

    private Dialog containerDialog;
    private VerticalLayout entityFieldsContainer;
    private VerticalLayout entityDetailContainer;
    private String relationKind;
    private String relationEntityUID;
    private int relationEntityAttributesEditorHeightOffset = 135;

    public RelationEntityDetailView(){}

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.relationKind = beforeEnterEvent.getRouteParameters().get("relationKind").get();
        this.relationEntityUID = beforeEnterEvent.getRouteParameters().get("relationEntityUID").get();
        this.relationEntityAttributesEditorHeightOffset = 70;
    }

    public RelationEntityDetailView(String relationKind, String relationEntityUID){
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
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

        RelationEntityAttributesEditorView conceptionEntityAttributesEditorView =
                new RelationEntityAttributesEditorView(this.relationKind,this.relationEntityUID,relationEntityAttributesEditorHeightOffset);

        RelationEntityIntegratedInfoView relationEntityIntegratedInfoView =
                new RelationEntityIntegratedInfoView(this.relationKind,this.relationEntityUID,relationEntityAttributesEditorHeightOffset);

        this.entityFieldsContainer.add(conceptionEntityAttributesEditorView);
        this.entityDetailContainer.add(relationEntityIntegratedInfoView);

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
