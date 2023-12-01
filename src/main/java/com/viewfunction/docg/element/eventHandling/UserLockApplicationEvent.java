package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class UserLockApplicationEvent implements Event {

    public interface UserApplicationLogoutListener extends Listener {
        public void receivedUserLockApplicationEvent(final UserLockApplicationEvent event);
    }
}
