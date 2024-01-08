package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;

public class RelationAttachKindCreatedEvent implements Event {

    private String relationAttachKindName;
    private String relationAttachKindUID;
    private RelationAttachKind relationAttachKind;

    public String getRelationAttachKindName() {
        return relationAttachKindName;
    }

    public void setRelationAttachKindName(String relationAttachKindName) {
        this.relationAttachKindName = relationAttachKindName;
    }

    public String getRelationAttachKindUID() {
        return relationAttachKindUID;
    }

    public void setRelationAttachKindUID(String relationAttachKindUID) {
        this.relationAttachKindUID = relationAttachKindUID;
    }

    public RelationAttachKind getRelationAttachKind() {
        return relationAttachKind;
    }

    public void setRelationAttachKind(RelationAttachKind relationAttachKind) {
        this.relationAttachKind = relationAttachKind;
    }

    public interface RelationAttachKindCreatedListener extends Listener {
        public void receivedRelationAttachKindCreatedEvent(final RelationAttachKindCreatedEvent event);
    }
}
