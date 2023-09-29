package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ClassificationDescriptionUpdatedEvent implements Event {
    private String classificationName;
    private String classificationDesc;

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getClassificationDesc() {
        return classificationDesc;
    }

    public void setClassificationDesc(String classificationDesc) {
        this.classificationDesc = classificationDesc;
    }

    public interface ClassificationDescriptionUpdatedListener extends Listener {
        public void receivedClassificationDescriptionUpdatedEvent(final ClassificationDescriptionUpdatedEvent event);
    }
}
