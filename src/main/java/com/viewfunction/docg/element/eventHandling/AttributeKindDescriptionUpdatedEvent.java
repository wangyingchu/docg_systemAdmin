package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AttributeKindDescriptionUpdatedEvent implements Event {
    private String attributeKindUID;
    private String attributeKindDesc;

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
    }

    public String getAttributeKindDesc() {
        return attributeKindDesc;
    }

    public void setAttributeKindDesc(String attributeKindDesc) {
        this.attributeKindDesc = attributeKindDesc;
    }

    public interface AttributeKindDescriptionUpdatedListener extends Listener {
        public void receivedAttributeKindDescriptionUpdatedEvent(final AttributeKindDescriptionUpdatedEvent event);
    }
}
