package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionEntitiesCountRefreshEvent implements Event {
    private String conceptionKindName;
    private long conceptionEntitiesCount;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public long getConceptionEntitiesCount() {
        return conceptionEntitiesCount;
    }

    public void setConceptionEntitiesCount(long conceptionEntitiesCount) {
        this.conceptionEntitiesCount = conceptionEntitiesCount;
    }

    public interface ConceptionEntitiesCountRefreshListener extends Listener {
        public void receivedConceptionEntitiesCountRefreshEvent(final ConceptionEntitiesCountRefreshEvent event);
    }
}
