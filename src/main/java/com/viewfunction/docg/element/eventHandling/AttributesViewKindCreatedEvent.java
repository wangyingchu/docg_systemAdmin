package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;

import java.util.Date;

public class AttributesViewKindCreatedEvent implements Event {

    private String attributesViewKindName;
    private String attributesViewKindDesc;
    private AttributesViewKind.AttributesViewKindDataForm attributesViewKindDataForm;
    private String attributesViewKindUID;
    private Date createDateTime;
    private Date lastModifyDateTime;
    private String creatorId;
    private String dataOrigin;

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

    public AttributesViewKind.AttributesViewKindDataForm getAttributesViewKindDataForm() {
        return attributesViewKindDataForm;
    }

    public void setAttributesViewKindDataForm(AttributesViewKind.AttributesViewKindDataForm attributesViewKindDataForm) {
        this.attributesViewKindDataForm = attributesViewKindDataForm;
    }

    public String getAttributesViewKindUID() {
        return attributesViewKindUID;
    }

    public void setAttributesViewKindUID(String attributesViewKindUID) {
        this.attributesViewKindUID = attributesViewKindUID;
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

    public interface AttributesViewKindCreatedListener extends Listener {
        public void receivedAttributesViewKindCreatedEvent(final AttributesViewKindCreatedEvent event);
    }
}
