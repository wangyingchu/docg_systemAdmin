package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.BarChart;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AttributeKindInfoWidget extends HorizontalLayout {

    public AttributeKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setWidth(250,Unit.PIXELS);
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        String[] relationKindNameArray = new String[0];
        Double[] kindEntitiesCountArray = new Double[0];

        int attributeKindCount = 0;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<AttributeKindMetaInfo> attributeKindMetaInfoList = coreRealm.getAttributeKindsMetaInfo();
            attributeKindCount = attributeKindMetaInfoList.size();
            Collections.sort(attributeKindMetaInfoList, new Comparator<AttributeKindMetaInfo>() {
                public int compare(AttributeKindMetaInfo o1, AttributeKindMetaInfo o2) {
                    if(o2.getContainerAttributesViewKindCount() > o1.getContainerAttributesViewKindCount()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });

            int topRelationKindNameArraySize = attributeKindCount >= 10 ? 10 : attributeKindCount;
            relationKindNameArray = new String[topRelationKindNameArraySize];
            kindEntitiesCountArray = new Double[topRelationKindNameArraySize];

            for(int i=0;i <topRelationKindNameArraySize;i++){
                AttributeKindMetaInfo currentAttributeKindMetaInfo = attributeKindMetaInfoList.get(i);
                relationKindNameArray[i] = currentAttributeKindMetaInfo.getKindName()+"("+currentAttributeKindMetaInfo.getKindUID()+")";
                kindEntitiesCountArray[i] = Double.valueOf(currentAttributeKindMetaInfo.getContainerAttributesViewKindCount());
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        new PrimaryKeyValueDisplayItem(leftComponentContainer, FontAwesome.Regular.CIRCLE.create(),"属性类型数量:",""+attributeKindCount);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        NativeLabel messageText = new NativeLabel("Top 10 Used AttributeKinds ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        BarChart barChart = new BarChart(330,250);
        rightComponentContainer.add(barChart);

        String[] barColorArray = new String[]{"#FF4136"};
        barChart.setColor(barColorArray);
        barChart.setTopMargin(2);
        barChart.setRightMargin(20);
        barChart.setYAxisMaxOffset(0);

        barChart.setDate(relationKindNameArray,kindEntitiesCountArray);
    }
}
