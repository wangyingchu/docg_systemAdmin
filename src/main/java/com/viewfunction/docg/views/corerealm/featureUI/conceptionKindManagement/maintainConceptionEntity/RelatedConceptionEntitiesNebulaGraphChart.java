package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.List;

public class RelatedConceptionEntitiesNebulaGraphChart extends VerticalLayout {
    private String mainConceptionEntityUID;
    private List<ConceptionEntity> conceptionEntityList;
    private List<RelationEntity> relationEntityList;
    public RelatedConceptionEntitiesNebulaGraphChart(String mainConceptionEntityUID, List<ConceptionEntity> conceptionEntityList,List<RelationEntity> relationEntityList,int chartHeight){
        this.mainConceptionEntityUID = mainConceptionEntityUID;
        this.conceptionEntityList = conceptionEntityList;
        this.relationEntityList = relationEntityList;

        IFrame _IFrame = new IFrame();
        _IFrame.getStyle().set("border","0");
        _IFrame.setSrc("https://vasturiano.github.io/3d-force-graph/example/large-graph/");
        _IFrame.setHeight(chartHeight, Unit.PIXELS);
        _IFrame.setWidth(100, Unit.PERCENTAGE);
        add(_IFrame);
    }
}
