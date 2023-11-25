package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class GeospatialRegionRefreshEvent implements Event {
    private String geospatialRegionName;

    public String getGeospatialRegionName() {
        return geospatialRegionName;
    }

    public void setGeospatialRegionName(String geospatialRegionName) {
        this.geospatialRegionName = geospatialRegionName;
    }

    public interface GeospatialRegionRefreshEventListener extends Listener {
        public void receivedGeospatialRegionRefreshEvent(final GeospatialRegionRefreshEvent event);
    }
}
