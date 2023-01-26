package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.relation.EntityAttachedConceptionKindsCountChart;

import java.util.*;

public class RelatedConceptionEntitiesKindStaticInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private HorizontalLayout conceptionKindsInfoLayout;
    private VerticalLayout conceptionKindsEntityCountLayout;
    public RelatedConceptionEntitiesKindStaticInfoView(String conceptionKind,String conceptionEntityUID){
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

        conceptionKindsInfoLayout = new HorizontalLayout();
        conceptionKindsInfoLayout.setWidthFull();
        add(conceptionKindsInfoLayout);

        conceptionKindsEntityCountLayout = new VerticalLayout();
        conceptionKindsEntityCountLayout.setPadding(true);
        conceptionKindsEntityCountLayout.setMargin(false);
        conceptionKindsEntityCountLayout.setSpacing(false);

        Scroller scroller = new Scroller();
        scroller.setHeight(350,Unit.PIXELS);
        scroller.setWidth(900,Unit.PIXELS);
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
        scroller.setContent(conceptionKindsEntityCountLayout);
        conceptionKindsInfoLayout.add(scroller);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadEntityRelatedConceptionEntitiesKindInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void loadEntityRelatedConceptionEntitiesKindInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                Map<Set<String>,Long> attachedConceptionKindsMap = targetEntity.countAttachedConceptionKinds();
                Map<String,Long> conceptionKindEntityCountMap = new HashMap<>();
                Set<Set<String>> conceptionKindsSet = attachedConceptionKindsMap.keySet();
                for(Set<String> currentConceptionKindSet : conceptionKindsSet){
                    Long currentEntityCount = attachedConceptionKindsMap.get(currentConceptionKindSet);
                    for(String currentConceptionKind :currentConceptionKindSet){
                        if(conceptionKindEntityCountMap.containsKey(currentConceptionKind)){
                            Long newEntityCount = conceptionKindEntityCountMap.get(currentConceptionKind) + currentEntityCount;
                            conceptionKindEntityCountMap.put(currentConceptionKind,newEntityCount);
                        }else{
                            conceptionKindEntityCountMap.put(currentConceptionKind,currentEntityCount);
                        }
                    }
                }

                for(String relationKindName : conceptionKindEntityCountMap.keySet()){
                    HorizontalLayout conceptionKindInfoItem = new HorizontalLayout();
                    conceptionKindInfoItem.setSpacing(false);
                    conceptionKindInfoItem.getStyle().set("padding-bottom","5px");
                    conceptionKindsEntityCountLayout.add(conceptionKindInfoItem);

                    Icon conceptionKindIcon = VaadinIcon.CUBE.create();
                    conceptionKindIcon.addClassNames("text-tertiary");
                    conceptionKindIcon.setSize("8px");
                    conceptionKindInfoItem.add(conceptionKindIcon);
                    conceptionKindInfoItem.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon);
                    Label currentConceptionKind = new Label(relationKindName);
                    currentConceptionKind.getStyle().set("font-size","var(--lumo-font-size-xs)").set("padding-left","5px");
                    currentConceptionKind.addClassNames("text-tertiary");
                    conceptionKindInfoItem.add(currentConceptionKind);
                    conceptionKindInfoItem.setVerticalComponentAlignment(Alignment.CENTER,currentConceptionKind);

                    Span conceptionEntityCountSpan = new Span(""+conceptionKindEntityCountMap.get(relationKindName).toString());
                    conceptionEntityCountSpan.getStyle().set("font-size","var(--lumo-font-size-xxs)").set("font-weight","bold");
                    conceptionEntityCountSpan.setHeight(15,Unit.PIXELS);
                    conceptionEntityCountSpan.getElement().getThemeList().add("badge contrast");
                    conceptionKindInfoItem.add(conceptionEntityCountSpan);
                }

                EntityAttachedConceptionKindsCountChart entityAttachedConceptionKindsCountChart = new EntityAttachedConceptionKindsCountChart(attachedConceptionKindsMap);
                conceptionKindsInfoLayout.add(entityAttachedConceptionKindsCountChart);
            }
        }
        coreRealm.closeGlobalSession();
    }
}
