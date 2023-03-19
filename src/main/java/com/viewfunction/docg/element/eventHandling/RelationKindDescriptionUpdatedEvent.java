package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationKindDescriptionUpdatedEvent implements Event {

    private String relationKindName;
    private String relationKindDesc;

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public String getRelationKindDesc() {
        return relationKindDesc;
    }

    public void setRelationKindDesc(String relationKindDesc) {
        this.relationKindDesc = relationKindDesc;
    }

    public interface RelationKindDescriptionUpdatedListener extends Listener {
        public void receivedRelationKindDescriptionUpdatedEvent(final RelationKindDescriptionUpdatedEvent event);
    }

}
