package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import java.util.Date;

public class RelationKindCreatedEvent implements Event {
    private String relationKindName;
    private String relationKindDesc;
    private String creatorId;
    private String dataOrigin;
    private Date createDateTime;
    private Date lastModifyDateTime;

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public String getRelationKindDesc() {
        return relationKindDesc;
    }

    public void setRelationKindDesc(String relationKindDesc) {
        this.relationKindDesc = relationKindDesc;
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

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(Date lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    public interface RelationKindCreatedListener extends Listener {
        public void receivedRelationKindCreatedEvent(final RelationKindCreatedEvent event);
    }
}
