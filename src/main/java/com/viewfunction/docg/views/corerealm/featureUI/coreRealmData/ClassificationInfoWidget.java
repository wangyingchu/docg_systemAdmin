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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ClassificationInfoWidget extends HorizontalLayout {

    private boolean contentAlreadyLoaded = false;
    private NumberFormat numberFormat;
    private VerticalLayout widgetComponentContainer;
    private VerticalLayout chartComponentContainer;

    public ClassificationInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        widgetComponentContainer = new VerticalLayout();
        widgetComponentContainer.setSpacing(false);
        widgetComponentContainer.setMargin(false);
        add(widgetComponentContainer);

        chartComponentContainer = new VerticalLayout();
        chartComponentContainer.setSpacing(false);
        chartComponentContainer.setMargin(false);
        add(chartComponentContainer);
        this.setFlexGrow(1, chartComponentContainer);
    }

    public void loadWidgetContent(){
        if(!this.contentAlreadyLoaded){
            this.contentAlreadyLoaded = true;

            List<ClassificationMetaInfo> classificationsMetaInfoList = null;

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            GlobalClassificationsRuntimeStatistics globalClassificationsRuntimeStatistics =
                    coreRealm.getSystemMaintenanceOperator().getGlobalClassificationsRuntimeStatistics();
            try {
                classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
            coreRealm.closeGlobalSession();

            String classificationCount = classificationsMetaInfoList != null ? ""+classificationsMetaInfoList.size() :"-";

            new PrimaryKeyValueDisplayItem(widgetComponentContainer, FontAwesome.Solid.TAG.create(),"分类数量:",classificationCount);

            HorizontalLayout spaceDivLayout = new HorizontalLayout();
            spaceDivLayout.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout);

            this.numberFormat = NumberFormat.getInstance();

            new SecondaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关概念类型:",
                    this.numberFormat.format(globalClassificationsRuntimeStatistics.getRelatedConceptionKindCount()));

            HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
            spaceDivLayout2.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout2);

            new SecondaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关关系类型:",
                    this.numberFormat.format(globalClassificationsRuntimeStatistics.getRelatedRelationKindCount()));

            HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
            spaceDivLayout3.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout3);

            new SecondaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Solid.CIRCLE.create(),"相关概念实体:",
                    this.numberFormat.format(globalClassificationsRuntimeStatistics.getRelatedConceptionEntityCount()));

            HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
            spaceDivLayout4.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout4);

            new SecondaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关属性视图类型:",
                    this.numberFormat.format(globalClassificationsRuntimeStatistics.getRelatedAttributesViewKindCount()));

            HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
            spaceDivLayout5.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout5);

            new SecondaryKeyValueDisplayItem(widgetComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关属性类型:",
                    this.numberFormat.format(globalClassificationsRuntimeStatistics.getRelatedAttributeKindCount()));

            HorizontalLayout spaceDivLayout6 = new HorizontalLayout();
            spaceDivLayout6.setHeight(15,Unit.PIXELS);
            widgetComponentContainer.add(spaceDivLayout6);

            NativeLabel messageText = new NativeLabel("Top 2 Levels Classifications ->");
            widgetComponentContainer.add(messageText);
            messageText.addClassNames("text-xs","text-tertiary");

            TreeChart treeChart = new TreeChart(330,400);
            treeChart.setLayout(TreeChart.TreeLayout.radial);
            treeChart.setLeftMargin(1);
            treeChart.setRightMargin(25);
            treeChart.setTopMargin(1);
            treeChart.setBottomMargin(1);
            treeChart.setColor("#CE0000");
            chartComponentContainer.add(treeChart);
            chartComponentContainer.setHorizontalComponentAlignment(Alignment.START, treeChart);

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

    public void reloadWidgetContent(){
        this.widgetComponentContainer.removeAll();
        this.chartComponentContainer.removeAll();
        this.contentAlreadyLoaded = false;
        loadWidgetContent();
    }
}
