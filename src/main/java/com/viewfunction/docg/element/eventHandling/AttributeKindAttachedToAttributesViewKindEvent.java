package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;

public class AttributeKindAttachedToAttributesViewKindEvent implements Event {
    private String attributeKindUID;
    private String attributesViewKindUID;
    private AttributeKindMetaInfo attributeKindMetaInfo;
    private AttributeKind attributeKind;
    private AttributesViewKind attributesViewKind;

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

    public AttributeKindMetaInfo getAttributeKindMetaInfo() {
        return attributeKindMetaInfo;
    }

    public void setAttributeKindMetaInfo(AttributeKindMetaInfo attributeKindMetaInfo) {
        this.attributeKindMetaInfo = attributeKindMetaInfo;
    }

    public AttributeKind getAttributeKind() {
        return attributeKind;
    }

    public void setAttributeKind(AttributeKind attributeKind) {
        this.attributeKind = attributeKind;
    }

    public AttributesViewKind getAttributesViewKind() {
        return attributesViewKind;
    }

    public void setAttributesViewKind(AttributesViewKind attributesViewKind) {
        this.attributesViewKind = attributesViewKind;
    }

    public interface AttributeKindAttachedToAttributesViewKindListener extends Listener {
        public void receivedAttributeKindAttachedToAttributesViewKindEvent(final AttributeKindAttachedToAttributesViewKindEvent event);
    }
}
