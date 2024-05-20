package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceMetaInfo;

public class DataSliceCreatedEvent implements Event {

    private String dataSliceName;
    private String dataSliceGroup;
    private DataSliceMetaInfo dataSliceMetaInfo;

    public String getDataSliceName() {
        return dataSliceName;
    }

    public void setDataSliceName(String dataSliceName) {
        this.dataSliceName = dataSliceName;
    }

    public String getDataSliceGroup() {
        return dataSliceGroup;
    }

    public void setDataSliceGroup(String dataSliceGroup) {
        this.dataSliceGroup = dataSliceGroup;
    }

    public DataSliceMetaInfo getDataSliceMetaInfo() {
        return dataSliceMetaInfo;
    }

    public void setDataSliceMetaInfo(DataSliceMetaInfo dataSliceMetaInfo) {
        this.dataSliceMetaInfo = dataSliceMetaInfo;
    }

    public interface DataSliceCreatedListener extends Listener{
        public void receivedDataSliceCreatedEvent(final DataSliceCreatedEvent event);
    }
}
