package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

public class ConceptionKindQueryResultsView extends VerticalLayout implements
        ConceptionKindQueriedEvent.ConceptionKindQueriedListener{

    private String conceptionKindName;

    public ConceptionKindQueryResultsView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
    }

    @Override
    public void receivedConceptionKindQueriedEvent(ConceptionKindQueriedEvent event) {
        String conceptionKindName = event.getConceptionKindName();
        if(conceptionKindName.equals(this.conceptionKindName)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKindName);
            QueryParameters queryParameters = new QueryParameters();
            try {
                ConceptionEntitiesRetrieveResult conceptionEntitiesRetrieveResult = targetConception.getEntities(queryParameters);
                System.out.println(conceptionEntitiesRetrieveResult.getOperationStatistics().getResultEntitiesCount());
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }
}
