package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import java.util.Date;

public class ConceptionKindCreatedEvent implements Event {
    private String conceptionKindName;
    private String conceptionKindDesc;
    private String creatorId;
    private String dataOrigin;
    private Date createDateTime;
    private Date lastModifyDateTime;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public String getConceptionKindDesc() {
        return conceptionKindDesc;
    }

    public void setConceptionKindDesc(String conceptionKindDesc) {
        this.conceptionKindDesc = conceptionKindDesc;
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

    public interface ConceptionKindCreatedListener extends Listener {
        public void receivedConceptionKindCreatedEvent(final ConceptionKindCreatedEvent event);
    }

}
