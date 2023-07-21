package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AttributeKindRemovedEvent implements Event {
    private String attributeKindName;
    private String attributeKindDesc;
    private String attributeKindDataType;
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

    public String getAttributeKindDataType() {
        return attributeKindDataType;
    }

    public void setAttributeKindDataType(String attributeKindDataType) {
        this.attributeKindDataType = attributeKindDataType;
    }

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
    }

    public interface AttributeKindRemovedListener extends Listener {
        public void receivedAttributeKindRemovedEvent(final AttributeKindRemovedEvent event);
    }
}
