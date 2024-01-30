package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;

public class KindScopeAttributeAddedEvent implements Event {
    private AddEntityAttributeView.KindType kindType;
    private String kindName;

    public AddEntityAttributeView.KindType getKindType() {
        return kindType;
    }

    public void setKindType(AddEntityAttributeView.KindType kindType) {
        this.kindType = kindType;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public interface KindScopeAttributeAddedListener extends Listener {
        public void receivedKindScopeAttributeAddedEvent(final KindScopeAttributeAddedEvent event);
    }
}
