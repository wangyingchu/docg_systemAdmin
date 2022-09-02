package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ResultEntitiesParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class RelatedConceptionEntitiesNebulaGraphInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    public RelatedConceptionEntitiesNebulaGraphInfoView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,this.conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,this.conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadRelatedConceptionEntitiesCollRelationsInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void loadRelatedConceptionEntitiesCollRelationsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        CrossKindDataOperator crossKindDataOperator =coreRealm.getCrossKindDataOperator();
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                try {
                    ResultEntitiesParameters resultEntitiesParameters = new ResultEntitiesParameters();
                    resultEntitiesParameters.setDistinctMode(true);
                    resultEntitiesParameters.setResultNumber(100000000);
                    List<ConceptionEntity> relatedConceptionEntityList =
                            targetEntity.getRelatedConceptionEntities(null,null, RelationDirection.TWO_WAY,1,null,null,resultEntitiesParameters);
                    List<String> conceptionEntityUIDList = new ArrayList<>();
                    for(ConceptionEntity currentConceptionEntity:relatedConceptionEntityList){
                        conceptionEntityUIDList.add(currentConceptionEntity.getConceptionEntityUID());
                    }
                    //above query does not contain this.conceptionEntityUID, need add it
                    conceptionEntityUIDList.add(this.conceptionEntityUID);
                    List<RelationEntity> relationEntityList = crossKindDataOperator.getRelationsOfConceptionEntityPair(conceptionEntityUIDList);

                    RelatedConceptionEntitiesNebulaGraphChart relatedConceptionEntitiesNebulaGraphChart =
                            new RelatedConceptionEntitiesNebulaGraphChart(this.conceptionEntityUID,relatedConceptionEntityList,relationEntityList);
                    add(relatedConceptionEntitiesNebulaGraphChart);
                } catch (CoreRealmServiceEntityExploreException e) {
                    throw new RuntimeException(e);
                } finally {
                    coreRealm.closeGlobalSession();
                }
            }
        }
    }
}
