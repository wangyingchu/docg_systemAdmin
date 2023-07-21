package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;

import java.util.Date;

public class AttributeKindCreatedEvent implements Event {
    private String attributeKindName;
    private String attributeKindDesc;
    private AttributeDataType attributeKindDataType;
    private String attributeKindUID;
    private Date createDateTime;
    private Date lastModifyDateTime;
    private String creatorId;
    private String dataOrigin;

    public String getAttributeKindName() {
        return attributeKindName;
    }

    public void setAttributeKindName(String attributeKindName) {
        this.attributeKindName = attributeKindName;
    }

    public String getAttributeKindDesc() {
        return attributeKindDesc;
    }

    public void setAttributeKindDesc(String attributeKindDesc) {
        this.attributeKindDesc = attributeKindDesc;
    }

    public AttributeDataType getAttributeKindDataType() {
        return attributeKindDataType;
    }

    public void setAttributeKindDataType(AttributeDataType attributeKindDataType) {
        this.attributeKindDataType = attributeKindDataType;
    }

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
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

    public interface AttributeKindCreatedListener extends Listener {
        public void receivedAttributeKindCreatedEvent(final AttributeKindCreatedEvent event);
    }
}
