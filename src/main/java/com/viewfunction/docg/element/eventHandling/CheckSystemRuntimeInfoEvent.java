package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class CheckSystemRuntimeInfoEvent implements Event {

    private String coreRealmName;

    public String getCoreRealmName() {
        return coreRealmName;
    }

    public void setCoreRealmName(String coreRealmName) {
        this.coreRealmName = coreRealmName;
    }

    public interface CheckSystemRuntimeInfoListener extends Listener {
        public void receivedCheckSystemRuntimeInfoEvent(final CheckSystemRuntimeInfoEvent event);
    }
}
