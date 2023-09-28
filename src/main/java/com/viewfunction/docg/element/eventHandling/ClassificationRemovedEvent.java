package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ClassificationRemovedEvent implements Event {
    private String classificationName;

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public interface ClassificationRemovedListener extends Listener {
        public void receivedClassificationRemovedEvent(final ClassificationRemovedEvent event);
    }
}
