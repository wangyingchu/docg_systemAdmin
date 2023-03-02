package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.List;

public class RelationEntitiesCreatedEvent implements Event {

    private List<RelationEntity> createdRelationEntitiesList;

    public List<RelationEntity> getCreatedRelationEntitiesList() {
        return createdRelationEntitiesList;
    }

    public void setCreatedRelationEntitiesList(List<RelationEntity> createdRelationEntitiesList) {
        this.createdRelationEntitiesList = createdRelationEntitiesList;
    }

    public interface RelationEntitiesCreatedListener extends Listener {
        public void receivedRelationEntitiesCreatedEvent(final RelationEntitiesCreatedEvent event);
    }
}
