package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class UserApplicationLogoutEvent implements Event {

    public interface UserApplicationLogoutListener extends Listener {
        public void receivedUserApplicationLogoutEvent(final UserApplicationLogoutEvent event);
    }
}
