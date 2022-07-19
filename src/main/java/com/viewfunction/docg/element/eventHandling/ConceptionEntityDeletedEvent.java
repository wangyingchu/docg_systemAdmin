package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import java.util.List;

public class ConceptionEntityDeletedEvent implements Event {

    private String conceptionEntityUID;
    private List<String> entityAllConceptionKindNames;

    public String getConceptionEntityUID() {
        return conceptionEntityUID;
    }

    public void setConceptionEntityUID(String conceptionEntityUID) {
        this.conceptionEntityUID = conceptionEntityUID;
    }

    public List<String> getEntityAllConceptionKindNames() {
        return entityAllConceptionKindNames;
    }

    public void setEntityAllConceptionKindNames(List<String> entityAllConceptionKindNames) {
        this.entityAllConceptionKindNames = entityAllConceptionKindNames;
    }

    public interface ConceptionEntityDeletedListener extends Listener {
        public void receivedConceptionEntityDeletedEvent(final ConceptionEntityDeletedEvent event);
    }
}
