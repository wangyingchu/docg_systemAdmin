package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionKindConfigurationInfoRefreshEvent implements Event {
    private String conceptionKindName;
    public String getConceptionKindName() {
        return conceptionKindName;
    }
    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public interface ConceptionKindConfigurationInfoRefreshListener extends Listener {
        public void receivedConceptionKindConfigurationInfoRefreshEvent(final ConceptionKindConfigurationInfoRefreshEvent event);
    }
}
