package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import java.util.List;

public class RelationEntitiesCreatedEvent implements Event {

    private List<RelationEntityInfo> createdRelationEntitiesList;

    public List<RelationEntityInfo> getCreatedRelationEntitiesList() {
        return createdRelationEntitiesList;
    }

    public void setCreatedRelationEntitiesList(List<RelationEntityInfo> createdRelationEntitiesList) {
        this.createdRelationEntitiesList = createdRelationEntitiesList;
    }

    public interface RelationEntitiesCreatedListener extends Listener {
        public void receivedRelationEntitiesCreatedEvent(final RelationEntitiesCreatedEvent event);
    }

    public static class RelationEntityInfo{
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
    }
}
