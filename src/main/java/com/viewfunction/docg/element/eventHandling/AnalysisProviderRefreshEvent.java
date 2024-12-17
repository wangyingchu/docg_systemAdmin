package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class AnalysisProviderRefreshEvent implements Event {

    public interface AnalysisProviderRefreshEventListener extends Listener {
        public void receivedAnalysisProviderRefreshEvent(final AnalysisProviderRefreshEvent event);
    }
}
