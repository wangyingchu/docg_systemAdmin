package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class TimeFlowCreatedEvent implements Event {
    private String timeFlowName;

    public String getTimeFlowName() {
        return timeFlowName;
    }

    public void setTimeFlowName(String timeFlowName) {
        this.timeFlowName = timeFlowName;
    }

    public interface TimeFlowCreatedListener extends Listener {
        public void receivedTimeFlowCreatedEvent(final TimeFlowCreatedEvent event);
    }
}
