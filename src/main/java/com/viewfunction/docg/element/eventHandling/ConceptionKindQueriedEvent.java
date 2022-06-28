package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionKindQueriedEvent implements Event {

    private String conceptionKindName;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public interface ConceptionKindQueriedListener extends Listener {
        public void receivedConceptionKindQueriedEvent(final ConceptionKindQueriedEvent event);
    }
}
