package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ConceptionEntityAttributeDeletedEvent implements Event {

    private String conceptionKindName;
    private String conceptionEntityUID;
    private String attributeName;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public String getConceptionEntityUID() {
        return conceptionEntityUID;
    }

    public void setConceptionEntityUID(String conceptionEntityUID) {
        this.conceptionEntityUID = conceptionEntityUID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public interface ConceptionEntityAttributeDeletedListener extends Listener {
        public void receivedConceptionEntityAttributeDeletedEvent(final ConceptionEntityAttributeDeletedEvent event);
    }
}
