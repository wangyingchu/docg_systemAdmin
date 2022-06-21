package com.viewfunction.docg.util;

import com.github.wolfie.blackboard.Blackboard;

public class ResourceHolder {

    private static Blackboard applicationBlackboard;

    public static Blackboard getApplicationBlackboard() {
        return applicationBlackboard;
    }

    public static void setApplicationBlackboard(Blackboard applicationBlackboard) {
        ResourceHolder.applicationBlackboard = applicationBlackboard;
    }
}
