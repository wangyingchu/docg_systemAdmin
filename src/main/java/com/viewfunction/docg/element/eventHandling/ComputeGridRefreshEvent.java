package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ComputeGridRefreshEvent implements Event {

    public interface ComputeGridRefreshEventListener extends Listener {
        public void receivedComputeGridRefreshEvent(final ComputeGridRefreshEvent event);
    }
}
