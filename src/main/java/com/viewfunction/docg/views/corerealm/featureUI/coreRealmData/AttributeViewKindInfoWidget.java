package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributesViewKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.BarChart;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AttributeViewKindInfoWidget  extends HorizontalLayout {

    public AttributeViewKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(260,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        int attributesViewKindCount = 0;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<AttributesViewKindMetaInfo> attributeKindMetaInfoList = coreRealm.getAttributesViewKindsMetaInfo();
            attributesViewKindCount = attributeKindMetaInfoList.size();
            Collections.sort(attributeKindMetaInfoList, new Comparator<AttributesViewKindMetaInfo>() {
                public int compare(AttributesViewKindMetaInfo o1, AttributesViewKindMetaInfo o2) {
                    if(o2.getContainerConceptionKindCount()+o2.getContainsAttributeKindCount() > o1.getContainerConceptionKindCount()+o1.getContainsAttributeKindCount()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });






        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        new PrimaryKeyValueDisplayItem(leftComponentContainer, FontAwesome.Regular.CIRCLE.create(),"属性视图类型数量:",""+attributesViewKindCount);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        NativeLabel messageText = new NativeLabel("Top 10 Used AttributeViewKinds ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        BarChart barChart = new BarChart(330,250);
        rightComponentContainer.add(barChart);
    }
}
