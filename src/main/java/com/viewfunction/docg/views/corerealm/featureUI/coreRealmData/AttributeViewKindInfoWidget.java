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
import com.viewfunction.docg.element.commonComponent.chart.StackedBarChart;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AttributeViewKindInfoWidget  extends HorizontalLayout {

    private boolean contentAlreadyLoaded = false;
    private VerticalLayout widgetComponentContainer;

    public AttributeViewKindInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        widgetComponentContainer = new VerticalLayout();
        widgetComponentContainer.setWidth(260,Unit.PIXELS);
        widgetComponentContainer.setSpacing(false);
        widgetComponentContainer.setMargin(false);
        add(widgetComponentContainer);
    }

    public void loadWidgetContent(){
        if(!this.contentAlreadyLoaded){
            this.contentAlreadyLoaded = true;
            String[] attributeKindNameArray = new String[0];
            Double[] containsAttributeKindCountArray = new Double[0];
            Double[] containerConceptionKindCountArray = new Double[0];

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

                int topAttributesViewKindNameArraySize = attributesViewKindCount >= 10 ? 10 : attributesViewKindCount;
                attributeKindNameArray = new String[topAttributesViewKindNameArraySize];
                containsAttributeKindCountArray = new Double[topAttributesViewKindNameArraySize];
                containerConceptionKindCountArray = new Double[topAttributesViewKindNameArraySize];

                for(int i=0;i <topAttributesViewKindNameArraySize;i++){
                    AttributesViewKindMetaInfo currentAttributeKindMetaInfo = attributeKindMetaInfoList.get(i);
                    attributeKindNameArray[i] = currentAttributeKindMetaInfo.getKindName();
                    containsAttributeKindCountArray[i] = Double.valueOf(currentAttributeKindMetaInfo.getContainsAttributeKindCount());
                    containerConceptionKindCountArray[i] = Double.valueOf(currentAttributeKindMetaInfo.getContainerConceptionKindCount());
                }

            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
            new PrimaryKeyValueDisplayItem(widgetComponentContainer, FontAwesome.Regular.CIRCLE.create(),"属性视图类型数量:",""+attributesViewKindCount);

            HorizontalLayout spaceDivLayout = new HorizontalLayout();
            spaceDivLayout.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout);

            NativeLabel messageText = new NativeLabel("Top 10 AttributeViewKinds used by more container ConceptionKinds and contains AttributeKinds ->");
            widgetComponentContainer.add(messageText);
            messageText.addClassNames("text-xs","text-tertiary");

            VerticalLayout rightComponentContainer = new VerticalLayout();
            rightComponentContainer.setSpacing(false);
            rightComponentContainer.setMargin(false);
            add(rightComponentContainer);
            this.setFlexGrow(1,rightComponentContainer);

            StackedBarChart stackedBarChart = new StackedBarChart(330,250);
            rightComponentContainer.add(stackedBarChart);

            stackedBarChart.setBottomMargin(1);
            stackedBarChart.setLeftMargin(1);
            stackedBarChart.setRightMargin(2);
            String[] barColorArray = new String[]{"#39CCCC","#F012BE"};
            stackedBarChart.setColor(barColorArray);

            stackedBarChart.setYAxisCategory(attributeKindNameArray);
            String[] dataCategoryArray = new String[]{"By AttributeKind","By ConceptionKind"};
            stackedBarChart.setDataCategory(dataCategoryArray);

            stackedBarChart.setDate(containsAttributeKindCountArray);
            stackedBarChart.setDate(containerConceptionKindCountArray);

            stackedBarChart.renderChart();
        }
    }
}
