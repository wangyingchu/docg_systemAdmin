package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ClassificationAttachedEvent implements Event {
    private String parentClassificationName;
    private String childClassificationName;

    public String getParentClassificationName() {
        return parentClassificationName;
    }

    public void setParentClassificationName(String parentClassificationName) {
        this.parentClassificationName = parentClassificationName;
    }

    public String getChildClassificationName() {
        return childClassificationName;
    }

    public void setChildClassificationName(String childClassificationName) {
        this.childClassificationName = childClassificationName;
    }

    public interface ClassificationAttachedListener extends Listener {
        public void receivedClassificationAttachedEvent(final ClassificationAttachedEvent event);
    }
}
