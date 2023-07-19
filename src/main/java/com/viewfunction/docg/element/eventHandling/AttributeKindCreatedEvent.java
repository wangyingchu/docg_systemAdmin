package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;

public class AttributeKindCreatedEvent implements Event {
    private String attributeKindName;
    private String attributeKindDesc;
    private AttributeDataType attributeKindDataType;
    private String attributeKindUID;

    public String getAttributeKindName() {
        return attributeKindName;
    }

    public void setAttributeKindName(String attributeKindName) {
        this.attributeKindName = attributeKindName;
    }

    public String getAttributeKindDesc() {
        return attributeKindDesc;
    }

    public void setAttributeKindDesc(String attributeKindDesc) {
        this.attributeKindDesc = attributeKindDesc;
    }

    public AttributeDataType getAttributeKindDataType() {
        return attributeKindDataType;
    }

    public void setAttributeKindDataType(AttributeDataType attributeKindDataType) {
        this.attributeKindDataType = attributeKindDataType;
    }

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
    }

    public interface AttributeKindCreatedListener extends Listener {
        public void receivedAttributeKindCreatedEvent(final AttributeKindCreatedEvent event);
    }
}
