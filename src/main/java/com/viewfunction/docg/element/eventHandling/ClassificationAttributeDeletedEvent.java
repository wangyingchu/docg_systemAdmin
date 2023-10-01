package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ClassificationAttributeDeletedEvent implements Event {
    private String classificationName;
    private String attributeName;

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public interface ClassificationAttributeDeletedListener extends Listener {
        public void receivedClassificationAttributeDeletedEvent(final ClassificationAttributeDeletedEvent event);
    }
}
