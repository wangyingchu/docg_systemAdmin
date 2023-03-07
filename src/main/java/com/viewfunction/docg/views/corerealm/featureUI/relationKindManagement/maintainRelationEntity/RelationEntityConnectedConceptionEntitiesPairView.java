package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RelationEntityConnectedConceptionEntitiesPairView extends VerticalLayout {

    private String relationKind;
    private String relationEntityUID;
    public RelationEntityConnectedConceptionEntitiesPairView(String relationKind,String relationEntityUID,int relationEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
    }
}
