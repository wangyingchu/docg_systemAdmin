package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;

public class RelationKindMatchLogicWidget extends HorizontalLayout {

    private String relationKindName;
    private RelationDirection relationDirection;

    public RelationKindMatchLogicWidget(){
        setSizeFull();

        NativeLabel defaultRelationDirectionFilterText = new NativeLabel("默认全局关系方向:");
        defaultRelationDirectionFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        add(defaultRelationDirectionFilterText);

    }

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public RelationDirection getRelationDirection() {
        return relationDirection;
    }

    public void setRelationDirection(RelationDirection relationDirection) {
        this.relationDirection = relationDirection;
    }
}
