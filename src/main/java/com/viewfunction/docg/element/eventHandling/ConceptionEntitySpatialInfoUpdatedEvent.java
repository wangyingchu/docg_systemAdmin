package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;

public class ConceptionEntitySpatialInfoUpdatedEvent implements Event {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public String getConceptionEntityUID() {
        return conceptionEntityUID;
    }

    public void setConceptionEntityUID(String conceptionEntityUID) {
        this.conceptionEntityUID = conceptionEntityUID;
    }

    public GeospatialScaleCalculable.SpatialScaleLevel getSpatialScaleLevel() {
        return spatialScaleLevel;
    }

    public void setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel) {
        this.spatialScaleLevel = spatialScaleLevel;
    }

    public interface ConceptionEntitySpatialInfoUpdatedListener extends Listener {
        public void receivedConceptionEntitySpatialInfoUpdatedEvent(final ConceptionEntitySpatialInfoUpdatedEvent event);
    }
}
