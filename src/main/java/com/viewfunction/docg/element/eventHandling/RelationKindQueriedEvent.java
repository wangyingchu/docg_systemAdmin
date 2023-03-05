package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;

import java.util.List;

public class RelationKindQueriedEvent implements Event {

    private String relationKindName;
    private List<String> resultAttributesList;
    private QueryParameters queryParameters;
    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public List<String> getResultAttributesList() {
        return resultAttributesList;
    }

    public void setResultAttributesList(List<String> resultAttributesList) {
        this.resultAttributesList = resultAttributesList;
    }

    public QueryParameters getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
    }

    public interface RelationKindQueriedListener extends Listener {
        public void receivedRelationKindQueriedEvent(final RelationKindQueriedEvent event);
    }
}
