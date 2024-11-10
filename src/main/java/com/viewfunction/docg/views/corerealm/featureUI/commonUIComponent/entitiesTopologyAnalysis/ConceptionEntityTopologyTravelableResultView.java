package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesGraph;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesSpanningTree;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityExpandTopologyEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ConceptionEntityTopologyTravelableResultView extends VerticalLayout implements ConceptionEntityExpandTopologyEvent.ConceptionEntityExpandTopologyListener {

    private ConceptionEntityTopologyTravelableView containerConceptionEntityTopologyTravelableView;
    private final ZoneId id = ZoneId.systemDefault();

    public ConceptionEntityTopologyTravelableResultView() {
        this.setPadding(false);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedConceptionEntityExpandTopologyEvent(ConceptionEntityExpandTopologyEvent event) {
        this.removeAll();

        String conceptionKind = event.getConceptionKind();
        String conceptionEntityUID = event.getConceptionEntityUID();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKind);
        if(targetConception != null){
            ConceptionEntity targetConceptionEntity = targetConception.getEntityByUID(conceptionEntityUID);
            if(targetConceptionEntity != null){
                if(this.containerConceptionEntityTopologyTravelableView != null){
                    ZonedDateTime startDateTime = ZonedDateTime.now(id);
                    String startTimeStr = startDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    this.containerConceptionEntityTopologyTravelableView.topologyExpandTypeDisplayItem.updateDisplayValue("-");
                    this.containerConceptionEntityTopologyTravelableView.startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    this.containerConceptionEntityTopologyTravelableView.finishTimeDisplayItem.updateDisplayValue("-");
                    this.containerConceptionEntityTopologyTravelableView.pathDataCountDisplayItem.updateDisplayValue("-");
                    this.containerConceptionEntityTopologyTravelableView.conceptionEntityDataCountDisplayItem.updateDisplayValue("-");
                    this.containerConceptionEntityTopologyTravelableView.relationEntityDataCountDisplayItem.updateDisplayValue("-");
                }

                ConceptionEntityTopologyTravelableView.TopologyExpandType topologyExpandType = event.getPathExpandType();
                ExpandParameters expandParameters = event.getExpandParameters();
                if(ConceptionEntityTopologyTravelableView.TopologyExpandType.ExpandPath.equals(topologyExpandType)){
                    int minJump = event.getMinJump() != null ? event.getMinJump() : 0;
                    List<EntitiesPath> entitiesPathList = targetConceptionEntity.expandPath(event.getRelationKindMatchLogics(),
                            event.getDefaultDirectionForNoneRelationKindMatch(),event.getConceptionKindMatchLogics(),minJump,event.getMaxJump(),expandParameters.getExpandPathResultMaxPathCount());
                    ConceptionEntityExpandPathInfoView conceptionEntityExpandPathInfoView = new ConceptionEntityExpandPathInfoView(conceptionKind,conceptionEntityUID,entitiesPathList);
                    add(conceptionEntityExpandPathInfoView);
                    if(this.containerConceptionEntityTopologyTravelableView != null){
                        this.containerConceptionEntityTopologyTravelableView.topologyExpandTypeDisplayItem.updateDisplayValue("拓展路径");
                        this.containerConceptionEntityTopologyTravelableView.pathDataCountDisplayItem.updateDisplayValue(""+entitiesPathList.size());
                        ZonedDateTime endDateTime = ZonedDateTime.now(id);
                        String endTimeStr = endDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                        this.containerConceptionEntityTopologyTravelableView.finishTimeDisplayItem.updateDisplayValue(endTimeStr);
                    }
                }else if(ConceptionEntityTopologyTravelableView.TopologyExpandType.ExpandGraph.equals(topologyExpandType)){
                    EntitiesGraph entitiesGraph = targetConceptionEntity.expandGraph(event.getRelationKindMatchLogics(),
                            event.getDefaultDirectionForNoneRelationKindMatch(),event.getConceptionKindMatchLogics(),event.isContainsSelf(),event.getMaxJump(),expandParameters.getExpandGraphResultMaxConceptionEntityCount());
                    ConceptionEntityExpandGraphInfoView conceptionEntityExpandGraphInfoView = new ConceptionEntityExpandGraphInfoView(conceptionKind,conceptionEntityUID,entitiesGraph);
                    add(conceptionEntityExpandGraphInfoView);
                    if(this.containerConceptionEntityTopologyTravelableView != null){
                        this.containerConceptionEntityTopologyTravelableView.topologyExpandTypeDisplayItem.updateDisplayValue("拓展子图");
                        this.containerConceptionEntityTopologyTravelableView.conceptionEntityDataCountDisplayItem.updateDisplayValue(""+entitiesGraph.getGraphConceptionEntities().size());
                        this.containerConceptionEntityTopologyTravelableView.relationEntityDataCountDisplayItem.updateDisplayValue(""+entitiesGraph.getGraphRelationEntities().size());
                        ZonedDateTime endDateTime = ZonedDateTime.now(id);
                        String endTimeStr = endDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                        this.containerConceptionEntityTopologyTravelableView.finishTimeDisplayItem.updateDisplayValue(endTimeStr);
                    }
                }else if(ConceptionEntityTopologyTravelableView.TopologyExpandType.ExpandSpanningTree.equals(topologyExpandType)){
                    EntitiesSpanningTree entitiesSpanningTree = targetConceptionEntity.expandSpanningTree(
                            event.getRelationKindMatchLogics(),event.getDefaultDirectionForNoneRelationKindMatch(),event.getConceptionKindMatchLogics(),event.getMaxJump(),expandParameters.getExpandSpanningTreeResultMaxConceptionEntityCount());
                    ConceptionEntityExpandSpanningTreeInfoView conceptionEntityExpandSpanningTreeInfoView = new ConceptionEntityExpandSpanningTreeInfoView(conceptionKind,conceptionEntityUID,entitiesSpanningTree);
                    add(conceptionEntityExpandSpanningTreeInfoView);
                    if(this.containerConceptionEntityTopologyTravelableView != null){
                        this.containerConceptionEntityTopologyTravelableView.topologyExpandTypeDisplayItem.updateDisplayValue("拓展生成树");
                        this.containerConceptionEntityTopologyTravelableView.conceptionEntityDataCountDisplayItem.updateDisplayValue(""+entitiesSpanningTree.getTreeConceptionEntities().size());
                        this.containerConceptionEntityTopologyTravelableView.relationEntityDataCountDisplayItem.updateDisplayValue(""+entitiesSpanningTree.getTreeRelationEntities().size());
                        ZonedDateTime endDateTime = ZonedDateTime.now(id);
                        String endTimeStr = endDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                        this.containerConceptionEntityTopologyTravelableView.finishTimeDisplayItem.updateDisplayValue(endTimeStr);
                    }
                }
            }
        }
    }

    public void setContainerConceptionEntityTopologyTravelableView(ConceptionEntityTopologyTravelableView containerConceptionEntityTopologyTravelableView) {
        this.containerConceptionEntityTopologyTravelableView = containerConceptionEntityTopologyTravelableView;
    }
}
