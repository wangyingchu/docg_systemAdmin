package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationKindRemovedEvent implements Event {

    private String relationKindName;

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public interface ConceptionKindRemovedListener extends Listener {
        public void receivedRelationKindRemovedEvent(final RelationKindRemovedEvent event);
    }
}
