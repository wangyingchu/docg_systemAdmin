package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionEntitiesCreatedEvent implements Event {
    private String conceptionEntityUID;
    private String conceptionKindName;
    private long newConceptionEntitiesCount;

    public String getConceptionEntityUID() {
        return conceptionEntityUID;
    }

    public void setConceptionEntityUID(String conceptionEntityUID) {
        this.conceptionEntityUID = conceptionEntityUID;
    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public long getNewConceptionEntitiesCount() {
        return newConceptionEntitiesCount;
    }

    public void setNewConceptionEntitiesCount(long newConceptionEntitiesCount) {
        this.newConceptionEntitiesCount = newConceptionEntitiesCount;
    }

    public interface ConceptionEntitiesCreatedListener extends Listener {
        public void receivedConceptionEntitiesCreatedEvent(final ConceptionEntitiesCreatedEvent event);
    }
}
