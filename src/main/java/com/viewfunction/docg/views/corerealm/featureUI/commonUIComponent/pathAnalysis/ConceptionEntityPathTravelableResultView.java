package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.eventHandling.ConceptionEntityExpandPathEvent;
import com.viewfunction.docg.util.ResourceHolder;

public class ConceptionEntityPathTravelableResultView extends VerticalLayout implements ConceptionEntityExpandPathEvent.ConceptionEntityExpandPathListener{


    public ConceptionEntityPathTravelableResultView() {}


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        /*
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryResultGrid.setHeight(event.getHeight()-140, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryResultGrid.setHeight(browserHeight-140,Unit.PIXELS);
        }));
        */
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        //listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedConceptionEntityExpandPathEvent(ConceptionEntityExpandPathEvent event) {



        System.out.println(event.getConceptionEntityUID());
        System.out.println(event.getConceptionKind());
        System.out.println(event.getPathExpandType());
        System.out.println(event.getDefaultDirectionForNoneRelationKindMatch());
        System.out.println(event.getRelationKindMatchLogics());
        System.out.println(event.getMaxJump());
        System.out.println(event.getMinJump());
        System.out.println(event.isContainsSelf());
        System.out.println(event.getRelationKindMatchLogics());


    }
}
