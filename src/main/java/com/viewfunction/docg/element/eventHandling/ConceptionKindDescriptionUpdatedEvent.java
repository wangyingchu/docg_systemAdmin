package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionKindDescriptionUpdatedEvent implements Event {

    private String conceptionKindName;
    private String conceptionKindDesc;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public String getConceptionKindDesc() {
        return conceptionKindDesc;
    }

    public void setConceptionKindDesc(String conceptionKindDesc) {
        this.conceptionKindDesc = conceptionKindDesc;
    }

    public interface ConceptionKindDescriptionUpdatedListener extends Listener{
        public void receivedConceptionKindDescriptionUpdatedEvent(final ConceptionKindDescriptionUpdatedEvent event);
    }
}
