package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionKindCleanedEvent implements Event {

    private String conceptionKindName;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public interface ConceptionKindCleanedListener extends Listener {
        public void receivedConceptionKindCleanedEvent(final ConceptionKindCleanedEvent event);
    }
}
