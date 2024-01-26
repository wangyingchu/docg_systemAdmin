package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class GeospatialRegionCreatedEvent implements Event {

    private String geospatialRegionName;

    public String getGeospatialRegionName() {
        return geospatialRegionName;
    }

    public void setGeospatialRegionName(String geospatialRegionName) {
        this.geospatialRegionName = geospatialRegionName;
    }

    public interface GeospatialRegionCreatedListener extends Listener {
        public void receivedGeospatialRegionCreatedEvent(final GeospatialRegionCreatedEvent event);
    }
}
