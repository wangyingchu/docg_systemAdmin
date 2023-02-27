package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationKindCleanedEvent implements Event {

    private String relationKindName;

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public interface RelationKindCleanedListener extends Listener {
        public void receivedRelationKindCleanedEvent(final RelationKindCleanedEvent event);
    }
}
