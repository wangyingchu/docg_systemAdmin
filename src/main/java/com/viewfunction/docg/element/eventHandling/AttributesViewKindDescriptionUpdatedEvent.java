package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AttributesViewKindDescriptionUpdatedEvent implements Event {
    private String attributesViewKindUID;
    private String attributesViewKindDesc;

    public String getAttributesViewKindUID() {
        return attributesViewKindUID;
    }

    public void setAttributesViewKindUID(String attributesViewKindUID) {
        this.attributesViewKindUID = attributesViewKindUID;
    }

    public String getAttributesViewKindDesc() {
        return attributesViewKindDesc;
    }

    public void setAttributesViewKindDesc(String attributesViewKindDesc) {
        this.attributesViewKindDesc = attributesViewKindDesc;
    }

    public interface AttributesViewKindDescriptionUpdatedListener extends Listener {
        public void receivedAttributesViewKindDescriptionUpdatedEvent(final AttributesViewKindDescriptionUpdatedEvent event);
    }
}
