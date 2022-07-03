package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

import java.util.List;

public class ConceptionKindQueriedEvent implements Event {

    private String conceptionKindName;
    private List<String> resultAttributesList;

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    public List<String> getResultAttributesList() {
        return resultAttributesList;
    }

    public void setResultAttributesList(List<String> resultAttributesList) {
        this.resultAttributesList = resultAttributesList;
    }

    public interface ConceptionKindQueriedListener extends Listener {
        public void receivedConceptionKindQueriedEvent(final ConceptionKindQueriedEvent event);
    }
}
