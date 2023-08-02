package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

@Route("attributeKindDetailInfo/:attributeKindUID")
public class AttributeKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String attributeKindUID;
    private int conceptionKindDetailViewHeightOffset = 110;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;

    public AttributeKindDetailUI(){}

    public AttributeKindDetailUI(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributeKindUID = beforeEnterEvent.getRouteParameters().get("attributeKindUID").get();
        this.conceptionKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //renderAttributesViewKindData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int containerHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset;
            //this.containerConceptionKindsConfigView.setHeight(containerHeight);
            //this.containsAttributeKindsConfigView.setHeight(containerHeight);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int containerHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset;
            //this.containerConceptionKindsConfigView.setHeight(containerHeight);
            //this.containsAttributeKindsConfigView.setHeight(containerHeight);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
