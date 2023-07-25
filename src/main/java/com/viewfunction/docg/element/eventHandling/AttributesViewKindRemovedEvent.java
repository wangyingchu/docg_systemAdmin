package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AttributesViewKindRemovedEvent implements Event {
    private String attributesViewKindName;
    private String attributesViewKindDesc;
    private String viewKindDataForm;
    private String attributesViewKindUID;

    public String getAttributesViewKindName() {
        return attributesViewKindName;
    }

    public void setAttributesViewKindName(String attributesViewKindName) {
        this.attributesViewKindName = attributesViewKindName;
    }

    public String getAttributesViewKindDesc() {
        return attributesViewKindDesc;
    }

    public void setAttributesViewKindDesc(String attributesViewKindDesc) {
        this.attributesViewKindDesc = attributesViewKindDesc;
    }

    public String getViewKindDataForm() {
        return viewKindDataForm;
    }

    public void setViewKindDataForm(String viewKindDataForm) {
        this.viewKindDataForm = viewKindDataForm;
    }

    public String getAttributesViewKindUID() {
        return attributesViewKindUID;
    }

    public void setAttributesViewKindUID(String attributesViewKindUID) {
        this.attributesViewKindUID = attributesViewKindUID;
    }

    public interface AttributesViewKindRemovedListener extends Listener {
        public void receivedAttributesViewKindRemovedEvent(final AttributesViewKindRemovedEvent event);
    }
}
