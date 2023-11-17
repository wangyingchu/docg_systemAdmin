package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class TimeFlowRefreshEvent implements Event {
    private String timeFlowName;

    public String getTimeFlowName() {
        return timeFlowName;
    }

    public void setTimeFlowName(String timeFlowName) {
        this.timeFlowName = timeFlowName;
    }

    public interface TimeFlowRefreshEventListener extends Listener {
        public void receivedTimeFlowRefreshEvent(final TimeFlowRefreshEvent event);
    }
}
