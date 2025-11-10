package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JoinNewConceptionKindsByAttributeValueGroupView extends VerticalLayout {

    private Dialog containerDialog;
    private String conceptionKind;
    private String attributeName;
    private VerticalLayout valueGroupContainerLayout;

    public JoinNewConceptionKindsByAttributeValueGroupView(String conceptionKind, String attributeName){
        this.conceptionKind = conceptionKind;
        this.attributeName = attributeName;
        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, conceptionKind));

        Icon attributeIcon = VaadinIcon.INPUT.create();
        attributeIcon.setSize("18px");
        attributeIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeIcon, attributeName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        Scroller groupValueScroller = new Scroller();
        groupValueScroller.setWidthFull();
        groupValueScroller.addThemeVariants(ScrollerVariant.LUMO_OVERFLOW_INDICATORS);
        groupValueScroller.setHeight(580,Unit.PIXELS);

        valueGroupContainerLayout = new VerticalLayout();
        valueGroupContainerLayout.setWidthFull();

        groupValueScroller.setContent(valueGroupContainerLayout);
        add(groupValueScroller);

        staticAttributeValueGroupInfo();
    }

    private void staticAttributeValueGroupInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try {
            List<KindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getConceptionKindsMetaInfo();
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.setResultNumber(100000000);
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            Map<Object, Number> resultMap = targetConceptionKind.statisticEntityGroupByAttributeValue(queryParameters,this.attributeName);
            Set<Object> groupValueSet = resultMap.keySet();
            for(Object groupValue : groupValueSet){
                ConceptionEntitiesJoinNewKindWidget conceptionEntitiesJoinNewKindWidget =
                        new ConceptionEntitiesJoinNewKindWidget(this.conceptionKind,this.attributeName,groupValue,resultMap.get(groupValue),runtimeAttributeKindMetaInfoList);
                valueGroupContainerLayout.add(conceptionEntitiesJoinNewKindWidget);
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
