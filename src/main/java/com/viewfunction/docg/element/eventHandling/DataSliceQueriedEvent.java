package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.dataCompute.computeServiceCore.analysis.query.QueryParameters;

public class DataSliceQueriedEvent implements Event {

    private String dataSliceName;

    private QueryParameters queryParameters;

    public String getDataSliceName() {
        return dataSliceName;
    }

    public void setDataSliceName(String dataSliceName) {
        this.dataSliceName = dataSliceName;
    }

    public QueryParameters getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
    }

    public interface DataSliceQueriedListener extends Listener {
        public void receivedDataSliceQueriedEvent(final DataSliceQueriedEvent event);
    }
}
