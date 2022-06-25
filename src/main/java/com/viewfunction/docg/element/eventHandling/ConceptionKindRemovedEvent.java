package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionKindRemovedEvent implements Event {

    private String conceptionKindName;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public interface ConceptionKindRemovedListener extends Listener {
        public void receivedConceptionKindRemovedEvent(final ConceptionKindRemovedEvent event);
    }
}
