package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import java.time.ZonedDateTime;
import java.util.Date;

public class ClassificationCreatedEvent implements Event {
    private String classificationName;
    private String classificationDesc;
    private ZonedDateTime createDate;
    private ZonedDateTime lastModifyDate;
    private String creatorId;
    private String dataOrigin;
    private String parentClassificationName;
    private int childClassificationCount;
    private boolean isRootClassification;

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

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(ZonedDateTime lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDataOrigin() {
        return dataOrigin;
    }

    public void setDataOrigin(String dataOrigin) {
        this.dataOrigin = dataOrigin;
    }

    public String getParentClassificationName() {
        return parentClassificationName;
    }

    public void setParentClassificationName(String parentClassificationName) {
        this.parentClassificationName = parentClassificationName;
    }

    public int getChildClassificationCount() {
        return childClassificationCount;
    }

    public void setChildClassificationCount(int childClassificationCount) {
        this.childClassificationCount = childClassificationCount;
    }

    public boolean isRootClassification() {
        return isRootClassification;
    }

    public void setRootClassification(boolean rootClassification) {
        isRootClassification = rootClassification;
    }

    public interface ClassificationCreatedListener extends Listener {
        public void receivedClassificationCreatedEvent(final ClassificationCreatedEvent event);
    }
}
