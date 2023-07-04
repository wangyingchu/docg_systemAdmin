package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationEntitiesCountRefreshEvent implements Event {

    private String relationKindName;
    private long relationEntitiesCount;

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public long getRelationEntitiesCount() {
        return relationEntitiesCount;
    }

    public void setRelationEntitiesCount(long relationEntitiesCount) {
        this.relationEntitiesCount = relationEntitiesCount;
    }

    public interface RelationEntitiesCountRefreshListener extends Listener {
        public void receivedRelationEntitiesCountRefreshEvent(final RelationEntitiesCountRefreshEvent event);
    }
}
