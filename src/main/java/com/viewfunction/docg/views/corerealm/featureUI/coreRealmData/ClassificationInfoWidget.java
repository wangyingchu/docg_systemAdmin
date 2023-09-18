package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GlobalClassificationsRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.chart.TreeChart;
import com.viewfunction.docg.element.visualizationComponent.payload.common.EchartsTreeChartPayload;

import java.util.ArrayList;
import java.util.List;

public class ClassificationInfoWidget extends HorizontalLayout {

    public ClassificationInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        List<ClassificationMetaInfo> classificationsMetaInfoList = null;

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();

        try {
            classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();

            GlobalClassificationsRuntimeStatistics globalClassificationsRuntimeStatistics =
                    coreRealm.getSystemMaintenanceOperator().getGlobalClassificationsRuntimeStatistics();

        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        coreRealm.closeGlobalSession();

        String classificationCount = classificationsMetaInfoList != null ? ""+classificationsMetaInfoList.size() :"-";

        new PrimaryKeyValueDisplayItem(leftComponentContainer, FontAwesome.Solid.TAG.create(),"分类数量:",classificationCount);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关概念类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout2);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关关系类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout3);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Solid.CIRCLE.create(),"相关概念实体:","1,000,000,000");

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout4);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关属性视图类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout5);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关属性类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout6 = new HorizontalLayout();
        spaceDivLayout6.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout6);

        NativeLabel messageText = new NativeLabel("Top 2 Levels Classifications ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        TreeChart treeChart = new TreeChart(330,400);
        treeChart.setLayout(TreeChart.TreeLayout.radial);
        treeChart.setLeftMargin(1);
        treeChart.setRightMargin(25);
        treeChart.setTopMargin(1);
        treeChart.setBottomMargin(1);
        treeChart.setColor("#CE0000");
        rightComponentContainer.add(treeChart);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START, treeChart);

        EchartsTreeChartPayload vRootClassificationPayload = new EchartsTreeChartPayload("Root");

        if(classificationsMetaInfoList != null && classificationsMetaInfoList.size()>0){
            List<EchartsTreeChartPayload> rootEchartsTreeChartPayloadList = new ArrayList<>();
            for(ClassificationMetaInfo rootLevelEchartsTreeChartPayload:classificationsMetaInfoList){
                if(rootLevelEchartsTreeChartPayload.isRootClassification()){
                    EchartsTreeChartPayload currentRootClassificationPayload = new EchartsTreeChartPayload(rootLevelEchartsTreeChartPayload.getClassificationName());
                    rootEchartsTreeChartPayloadList.add(currentRootClassificationPayload);
                }
            }
            vRootClassificationPayload.setChildren(rootEchartsTreeChartPayloadList);
            vRootClassificationPayload.setValue(rootEchartsTreeChartPayloadList.size());

            for(EchartsTreeChartPayload currentRootEchartsTreeChartPayload:rootEchartsTreeChartPayloadList){
                String currentRootClassificationName = currentRootEchartsTreeChartPayload.getName();
                List<EchartsTreeChartPayload> currentRootEchartsTreeChartPayloadList = new ArrayList<>();
                currentRootEchartsTreeChartPayload.setChildren(currentRootEchartsTreeChartPayloadList);

                for(ClassificationMetaInfo currentLevelEchartsTreeChartPayload:classificationsMetaInfoList){
                    String parentClassificationName = currentLevelEchartsTreeChartPayload.getParentClassificationName();
                    if(currentRootClassificationName.equals(parentClassificationName)){
                        EchartsTreeChartPayload currentFirstLevelClassificationPayload = new EchartsTreeChartPayload(currentLevelEchartsTreeChartPayload.getClassificationName());
                        currentRootEchartsTreeChartPayloadList.add(currentFirstLevelClassificationPayload);
                    }
                }
                currentRootEchartsTreeChartPayload.setValue(currentRootEchartsTreeChartPayloadList.size());
            }
        }

        treeChart.setDate(vRootClassificationPayload);
    }
}
