package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;

public class AttributesViewKindAttachedToConceptionKindEvent implements Event {
    private ConceptionKind conceptionKind;
    private AttributesViewKind attributesViewKind;
    private String conceptionKindName;
    private String attributesViewKindUID;

    public ConceptionKind getConceptionKind() {
        return conceptionKind;
    }

    public void setConceptionKind(ConceptionKind conceptionKind) {
        this.conceptionKind = conceptionKind;
    }

    public AttributesViewKind getAttributesViewKind() {
        return attributesViewKind;
    }

    public void setAttributesViewKind(AttributesViewKind attributesViewKind) {
        this.attributesViewKind = attributesViewKind;
    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public String getAttributesViewKindUID() {
        return attributesViewKindUID;
    }

    public void setAttributesViewKindUID(String attributesViewKindUID) {
        this.attributesViewKindUID = attributesViewKindUID;
    }

    public interface AttributesViewKindAttachedToConceptionKindListener extends Listener {
        public void receivedAttributesViewKindAttachedToConceptionKindEvent(final AttributesViewKindAttachedToConceptionKindEvent event);
    }
}
