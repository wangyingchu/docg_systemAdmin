package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationEntityAttributeDeletedEvent implements Event {
    private String relationKindName;
    private String relationEntityUID;
    private String attributeName;

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public String getRelationEntityUID() {
        return relationEntityUID;
    }

    public void setRelationEntityUID(String relationEntityUID) {
        this.relationEntityUID = relationEntityUID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public interface RelationEntityAttributeDeletedListener extends Listener {
        public void receivedRelationEntityAttributeDeletedEvent(final RelationEntityAttributeDeletedEvent event);
    }
}
