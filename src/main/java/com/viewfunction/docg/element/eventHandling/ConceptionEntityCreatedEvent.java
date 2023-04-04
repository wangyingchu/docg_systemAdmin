package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionEntityCreatedEvent implements Event {
    private String conceptionEntityUID;
    private String conceptionKindName;

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

    public interface ConceptionEntityCreatedListener extends Listener {
        public void receivedConceptionEntityCreatedEvent(final ConceptionEntityCreatedEvent event);
    }
}
