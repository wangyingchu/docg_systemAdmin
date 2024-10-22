package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesGraph;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityExpandPathEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class ConceptionEntityTopologyTravelableResultView extends VerticalLayout implements ConceptionEntityExpandPathEvent.ConceptionEntityExpandPathListener {

    public ConceptionEntityTopologyTravelableResultView() {
        this.setPadding(false);
        this.setMargin(false);
        this.setPadding(false);
    }

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
        this.removeAll();

        String conceptionKind = event.getConceptionKind();
        String conceptionEntityUID = event.getConceptionEntityUID();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKind);
        if(targetConception != null){
            ConceptionEntity targetConceptionEntity = targetConception.getEntityByUID(conceptionEntityUID);
            if(targetConceptionEntity != null){
                ConceptionEntityTopologyTravelableView.PathExpandType pathExpandType = event.getPathExpandType();
                if(ConceptionEntityTopologyTravelableView.PathExpandType.ExpandPath.equals(pathExpandType)){
                    int minJump = event.getMinJump() != null ? event.getMinJump() : 0;
                    List<EntitiesPath> entitiesPathList = targetConceptionEntity.expandPath(
                            event.getRelationKindMatchLogics(),event.getDefaultDirectionForNoneRelationKindMatch(),event.getConceptionKindMatchLogics(),minJump,event.getMaxJump());
                    ConceptionEntityExpandPathInfoView conceptionEntityExpandPathInfoView = new ConceptionEntityExpandPathInfoView(conceptionKind,conceptionEntityUID,entitiesPathList);
                    add(conceptionEntityExpandPathInfoView);
                }else if(ConceptionEntityTopologyTravelableView.PathExpandType.ExpandGraph.equals(pathExpandType)){
                    EntitiesGraph entitiesGraph = targetConceptionEntity.expandGraph(event.getRelationKindMatchLogics(),event.getDefaultDirectionForNoneRelationKindMatch(),event.getConceptionKindMatchLogics(),event.isContainsSelf(),event.getMaxJump());
                    System.out.println(entitiesGraph);
                    System.out.println(entitiesGraph.getGraphConceptionKindsDataStatistic());
                    ConceptionEntityExpandGraphInfoView conceptionEntityExpandGraphInfoView = new ConceptionEntityExpandGraphInfoView();
                    add(conceptionEntityExpandGraphInfoView);
                }else if(ConceptionEntityTopologyTravelableView.PathExpandType.ExpandSpanningTree.equals(pathExpandType)){
                    ConceptionEntityExpandSpanningTreeInfoView conceptionEntityExpandSpanningTreeInfoView = new ConceptionEntityExpandSpanningTreeInfoView();
                    add(conceptionEntityExpandSpanningTreeInfoView);
                }
            }
        }
    }
}
