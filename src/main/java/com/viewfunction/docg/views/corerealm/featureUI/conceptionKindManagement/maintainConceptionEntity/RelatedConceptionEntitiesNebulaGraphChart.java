package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.List;

public class RelatedConceptionEntitiesNebulaGraphChart extends VerticalLayout {
    private String mainConceptionEntityUID;
    private List<ConceptionEntity> conceptionEntityList;
    private List<RelationEntity> relationEntityList;
    public RelatedConceptionEntitiesNebulaGraphChart(String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList,List<RelationEntity> relationEntityList){
        this.mainConceptionEntityUID = mainConceptionEntityUID;
        this.conceptionEntityList = conceptionEntityList;
        this.relationEntityList = relationEntityList;
    }
}
