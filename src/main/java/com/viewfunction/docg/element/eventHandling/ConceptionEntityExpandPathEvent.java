package com.viewfunction.docg.element.eventHandling;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ConceptionKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.RelationKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.ConceptionEntityPathTravelableView.PathExpandType;
import java.util.List;

public class ConceptionEntityExpandPathEvent implements Event {

    private String conceptionEntityUID;
    private String conceptionKind;
    private PathExpandType pathExpandType;
    private List<RelationKindMatchLogic> relationKindMatchLogics;
    private RelationDirection defaultDirectionForNoneRelationKindMatch;
    private List<ConceptionKindMatchLogic> conceptionKindMatchLogics;
    private Integer minJump;
    private Integer maxJump;
    private boolean containsSelf;

    public String getConceptionEntityUID() {
        return conceptionEntityUID;
    }

    public void setConceptionEntityUID(String conceptionEntityUID) {
        this.conceptionEntityUID = conceptionEntityUID;
    }

    public String getConceptionKind() {
        return conceptionKind;
    }

    public void setConceptionKind(String conceptionKind) {
        this.conceptionKind = conceptionKind;
    }

    public PathExpandType getPathExpandType() {
        return pathExpandType;
    }

    public void setPathExpandType(PathExpandType pathExpandType) {
        this.pathExpandType = pathExpandType;
    }

    public List<RelationKindMatchLogic> getRelationKindMatchLogics() {
        return relationKindMatchLogics;
    }

    public void setRelationKindMatchLogics(List<RelationKindMatchLogic> relationKindMatchLogics) {
        this.relationKindMatchLogics = relationKindMatchLogics;
    }

    public RelationDirection getDefaultDirectionForNoneRelationKindMatch() {
        return defaultDirectionForNoneRelationKindMatch;
    }

    public void setDefaultDirectionForNoneRelationKindMatch(RelationDirection defaultDirectionForNoneRelationKindMatch) {
        this.defaultDirectionForNoneRelationKindMatch = defaultDirectionForNoneRelationKindMatch;
    }

    public List<ConceptionKindMatchLogic> getConceptionKindMatchLogics() {
        return conceptionKindMatchLogics;
    }

    public void setConceptionKindMatchLogics(List<ConceptionKindMatchLogic> conceptionKindMatchLogics) {
        this.conceptionKindMatchLogics = conceptionKindMatchLogics;
    }

    public Integer getMinJump() {
        return minJump;
    }

    public void setMinJump(Integer minJump) {
        this.minJump = minJump;
    }

    public Integer getMaxJump() {
        return maxJump;
    }

    public void setMaxJump(Integer maxJump) {
        this.maxJump = maxJump;
    }

    public boolean isContainsSelf() {
        return containsSelf;
    }

    public void setContainsSelf(boolean containsSelf) {
        this.containsSelf = containsSelf;
    }

    public interface ConceptionEntityExpandPathListener extends Listener {
        public void receivedConceptionEntityExpandPathEvent(final ConceptionEntityExpandPathEvent event);
    }
}
