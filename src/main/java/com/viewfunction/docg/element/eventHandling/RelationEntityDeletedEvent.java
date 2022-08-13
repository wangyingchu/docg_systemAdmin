package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RelationEntityDeletedEvent implements Event {

    private String relationEntityUID;
    private String relationKindName;
    private String fromConceptionEntityUID;
    private String toConceptionEntityUID;

    public String getRelationEntityUID() {
        return relationEntityUID;
    }

    public void setRelationEntityUID(String relationEntityUID) {
        this.relationEntityUID = relationEntityUID;
    }

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public String getFromConceptionEntityUID() {
        return fromConceptionEntityUID;
    }

    public void setFromConceptionEntityUID(String fromConceptionEntityUID) {
        this.fromConceptionEntityUID = fromConceptionEntityUID;
    }

    public String getToConceptionEntityUID() {
        return toConceptionEntityUID;
    }

    public void setToConceptionEntityUID(String toConceptionEntityUID) {
        this.toConceptionEntityUID = toConceptionEntityUID;
    }

    public interface RelationEntityDeletedListener extends Listener {
        public void receivedRelationEntityDeletedEvent(final RelationEntityDeletedEvent event);
    }
}
