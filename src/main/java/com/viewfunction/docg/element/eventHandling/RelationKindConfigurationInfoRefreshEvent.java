package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationKindConfigurationInfoRefreshEvent implements Event {
    private String relationKindName;
    public String getRelationKindName() {
        return relationKindName;
    }
    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public interface RelationKindConfigurationInfoRefreshListener extends Listener {
        public void receivedRelationKindConfigurationInfoRefreshEvent(final RelationKindConfigurationInfoRefreshEvent event);
    }
}
