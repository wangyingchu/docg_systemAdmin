package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AttributeKindDetachedFromAttributesViewKindEvent implements Event {
    private String attributeKindUID;
    private String attributesViewKindUID;

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
    }

    public String getAttributesViewKindUID() {
        return attributesViewKindUID;
    }

    public void setAttributesViewKindUID(String attributesViewKindUID) {
        this.attributesViewKindUID = attributesViewKindUID;
    }

    public interface AttributeKindDetachedFromAttributesViewKindListener extends Listener {
        public void receivedAttributeKindDetachedFromAttributesViewKindEvent(final AttributeKindDetachedFromAttributesViewKindEvent event);
    }
}
