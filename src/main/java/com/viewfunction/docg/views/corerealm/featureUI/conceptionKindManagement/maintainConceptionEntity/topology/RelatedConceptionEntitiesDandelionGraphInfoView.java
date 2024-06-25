package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ResultEntitiesParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelatedConceptionEntitiesDandelionGraphInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private String GlobalRelatedConceptionEntitiesDandelionGraphChartKey = "GLOBAL_RelatedConceptionEntitiesDandelionGraphChart";

    public RelatedConceptionEntitiesDandelionGraphInfoView(String conceptionKind, String conceptionEntityUID){
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

        Map<String, Component> globalApplicationComponentsMap = ResourceHolder.getGlobalApplicationComponentsMap();
        RelatedConceptionEntitiesDandelionGraphChart relatedConceptionEntitiesDandelionGraphChart =
                (RelatedConceptionEntitiesDandelionGraphChart)globalApplicationComponentsMap.get(GlobalRelatedConceptionEntitiesDandelionGraphChartKey);
        relatedConceptionEntitiesDandelionGraphChart.removeFromParent();

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
                    if(conceptionEntityUIDList.size()>=2){
                        List<RelationEntity> relationEntityList = crossKindDataOperator.getRelationsOfConceptionEntityPair(conceptionEntityUIDList);

                        Map<String, Component> globalApplicationComponentsMap = ResourceHolder.getGlobalApplicationComponentsMap();
                        boolean isFirstLaunch = false;
                        if(!globalApplicationComponentsMap.containsKey(GlobalRelatedConceptionEntitiesDandelionGraphChartKey)){
                            globalApplicationComponentsMap.put(GlobalRelatedConceptionEntitiesDandelionGraphChartKey,new RelatedConceptionEntitiesDandelionGraphChart());
                            isFirstLaunch = true;
                        }else{
                            isFirstLaunch = false;
                        }
                        RelatedConceptionEntitiesDandelionGraphChart relatedConceptionEntitiesDandelionGraphChart =
                                (RelatedConceptionEntitiesDandelionGraphChart)globalApplicationComponentsMap.get(GlobalRelatedConceptionEntitiesDandelionGraphChartKey);
                        relatedConceptionEntitiesDandelionGraphChart.setDandelionGraphChartData(this.conceptionKind,this.conceptionEntityUID,relatedConceptionEntityList,relationEntityList);

                        //new RelatedConceptionEntitiesDandelionGraphChart(this.conceptionKind,this.conceptionEntityUID,relatedConceptionEntityList,relationEntityList);
                        add(relatedConceptionEntitiesDandelionGraphChart);
                        if(!isFirstLaunch){
                            relatedConceptionEntitiesDandelionGraphChart.generateGraph2(1000,1000);
                        }


                    }
                } catch (CoreRealmServiceEntityExploreException e) {
                    throw new RuntimeException(e);
                } finally {
                    coreRealm.closeGlobalSession();
                }
            }
        }
    }
}
