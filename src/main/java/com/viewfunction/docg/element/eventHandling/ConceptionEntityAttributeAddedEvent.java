package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;

public class ConceptionEntityAttributeAddedEvent implements Event {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private AttributeValue attributeValue;

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

    public AttributeValue getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(AttributeValue attributeValue) {
        this.attributeValue = attributeValue;
    }

    public interface ConceptionEntityAttributeAddedListener extends Listener {
        public void receivedConceptionEntityAttributeAddedEvent(final ConceptionEntityAttributeAddedEvent event);
    }
}
