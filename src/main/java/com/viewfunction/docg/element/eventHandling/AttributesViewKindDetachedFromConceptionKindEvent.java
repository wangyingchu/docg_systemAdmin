package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AttributesViewKindDetachedFromConceptionKindEvent implements Event {
    private String attributesViewKindUID;
    private String conceptionKindName;

    public String getAttributesViewKindUID() {
        return attributesViewKindUID;
    }

    public void setAttributesViewKindUID(String attributesViewKindUID) {
        this.attributesViewKindUID = attributesViewKindUID;
    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public interface AttributesViewKindDetachedFromConceptionKindListener extends Listener {
        public void receivedAttributesViewKindDetachedFromConceptionKindEvent(final AttributesViewKindDetachedFromConceptionKindEvent event);
    }
}
