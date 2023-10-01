package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;

public class ClassificationAttributeAddedEvent implements Event {
    private String classificationName;
    private AttributeValue attributeValue;

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public AttributeValue getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(AttributeValue attributeValue) {
        this.attributeValue = attributeValue;
    }

    public interface ClassificationAttributeAddedListener extends Listener {
        public void receivedClassificationAttributeAddedEvent(final ClassificationAttributeAddedEvent event);

    }
}
