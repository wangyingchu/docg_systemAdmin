package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.SectionWallContainer;
import com.viewfunction.docg.element.commonComponent.SectionWallTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.time.ZonedDateTime;
import java.util.*;

@Route("coreRealmIntelligentAnalysis/")
public class IntelligentAnalysisView extends VerticalLayout implements BeforeEnterObserver {

    private VerticalLayout leftSideContainerLayout;
    private ConceptionDataRealtimeInfoWidget conceptionDataRealtimeInfoWidget;
    private RelationDataRealtimeInfoWidget relationDataRealtimeInfoWidget;
    private ConceptionDataCorrelationRealtimeInfoWidget conceptionDataCorrelationRealtimeInfoWidget;
    private Scroller leftSideScroller;
    private Registration listener;
    private int intelligentAnalysisViewHeightOffset = 110;
    private RealtimeConceptionDataCorrelationChartWidget realtimeConceptionDataCorrelationChartWidget;

    public IntelligentAnalysisView() {
        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setWidth(600, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        mainContainerLayout.add(leftSideContainerLayout);

        List<Component> sectionAction2ComponentsList = new ArrayList<>();
        SectionActionBar sectionActionBar2 = new SectionActionBar(LineAwesomeIconsSvg.CHART_LINE_SOLID.create(),"全域数据实时分布",sectionAction2ComponentsList);
        leftSideContainerLayout.add(sectionActionBar2);

        realtimeConceptionDataCorrelationChartWidget = new RealtimeConceptionDataCorrelationChartWidget(570,380);
        leftSideContainerLayout.add(realtimeConceptionDataCorrelationChartWidget);

        leftSideScroller = new Scroller();
        leftSideScroller.setWidthFull();
        leftSideScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        leftSideContainerLayout.add(leftSideScroller);

        VerticalLayout leftSideComponentsLayout = new VerticalLayout();
        leftSideComponentsLayout.setWidthFull();
        leftSideComponentsLayout.setSpacing(false);
        leftSideScroller.setContent(leftSideComponentsLayout);

        conceptionDataRealtimeInfoWidget = new ConceptionDataRealtimeInfoWidget();
        Icon conceptionKindInfoTitleIcon = new Icon(VaadinIcon.CUBE);
        conceptionKindInfoTitleIcon.setSize("18px");
        NativeLabel conceptionKindInfoTitleLabel = new NativeLabel("ConceptionKind(概念类型) 数据分布");
        conceptionKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle conceptionKindInfoSectionWallTitle = new SectionWallTitle(conceptionKindInfoTitleIcon,conceptionKindInfoTitleLabel);
        SectionWallContainer conceptionKindInfoSectionWallContainer = new SectionWallContainer(conceptionKindInfoSectionWallTitle,conceptionDataRealtimeInfoWidget);
        conceptionKindInfoSectionWallContainer.setOpened(false);
        leftSideComponentsLayout.add(conceptionKindInfoSectionWallContainer);

        relationDataRealtimeInfoWidget = new RelationDataRealtimeInfoWidget();
        Icon relationKindInfoTitleIcon = new Icon(VaadinIcon.CONNECT_O);
        relationKindInfoTitleIcon.setSize("18px");
        NativeLabel relationKindInfoTitleLabel = new NativeLabel("RelationKind(关系类型) 数据分布");
        relationKindInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle relationKindInfoSectionWallTitle = new SectionWallTitle(relationKindInfoTitleIcon,relationKindInfoTitleLabel);
        SectionWallContainer relationKindInfoSectionWallContainer = new SectionWallContainer(relationKindInfoSectionWallTitle,relationDataRealtimeInfoWidget);
        relationKindInfoSectionWallContainer.setOpened(false);
        leftSideComponentsLayout.add(relationKindInfoSectionWallContainer);

        conceptionDataCorrelationRealtimeInfoWidget = new ConceptionDataCorrelationRealtimeInfoWidget();
        Icon dataRelationInfoTitleIcon = FontAwesome.Solid.CODE_FORK.create();
        dataRelationInfoTitleIcon.setSize("18px");
        NativeLabel dataRelationInfoTitleLabel = new NativeLabel("全域数据实时关联 数据分布");
        dataRelationInfoTitleLabel.getStyle().set("font-size","var(--lumo-font-size-m)");
        SectionWallTitle dataRelationInfoSectionWallTitle = new SectionWallTitle(dataRelationInfoTitleIcon,dataRelationInfoTitleLabel);
        SectionWallContainer dataRelationInfoSectionWallContainer = new SectionWallContainer(dataRelationInfoSectionWallTitle,conceptionDataCorrelationRealtimeInfoWidget);
        dataRelationInfoSectionWallContainer.setOpened(false);
        leftSideComponentsLayout.add(dataRelationInfoSectionWallContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.intelligentAnalysisViewHeightOffset = 50;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            int currentBrowserHeight = event.getHeight();
            int leftSideScrollHeight = currentBrowserHeight - intelligentAnalysisViewHeightOffset - 410;
            leftSideScroller.setHeight(leftSideScrollHeight,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int currentBrowserHeight  = receiver.getBodyClientHeight();
            int leftSideScrollHeight = currentBrowserHeight - intelligentAnalysisViewHeightOffset - 410;
            leftSideScroller.setHeight(leftSideScrollHeight,Unit.PIXELS);
        }));

        CoreRealm targetCoreRealm = RealmTermFactory.getDefaultCoreRealm();
        targetCoreRealm.openGlobalSession();
        try {
            SystemMaintenanceOperator systemMaintenanceOperator = targetCoreRealm.getSystemMaintenanceOperator();
            ZonedDateTime currentDateTime = ZonedDateTime.now();
            if(systemMaintenanceOperator != null){
                List<EntityStatisticsInfo> realtimeConceptionList = targetCoreRealm.getConceptionEntitiesStatistics();
                List<EntityStatisticsInfo> realtimeRelationList = targetCoreRealm.getRelationEntitiesStatistics();

                Map<String, List<AttributeSystemInfo>> conceptionKindsAttributesSystemInfo = systemMaintenanceOperator.getPeriodicCollectedConceptionKindsAttributesSystemRuntimeInfo();
                if(conceptionKindsAttributesSystemInfo == null || conceptionKindsAttributesSystemInfo.isEmpty()){
                    conceptionKindsAttributesSystemInfo = systemMaintenanceOperator.getAllConceptionKindsAttributesSystemInfo();
                }else{
                    ZonedDateTime _latestRecordDatetime = conceptionKindsAttributesSystemInfo.values().iterator().next().get(0).getCreateDate();
                    ZonedDateTime oneDayBeforeCurrent = currentDateTime.minusDays(1);
                    if(_latestRecordDatetime.isBefore(oneDayBeforeCurrent)){
                        conceptionKindsAttributesSystemInfo = systemMaintenanceOperator.getAllConceptionKindsAttributesSystemInfo();
                    }
                }

                Map<String, List<AttributeSystemInfo>> relationKindsAttributesSystemInfo =
                        systemMaintenanceOperator.getPeriodicCollectedRelationKindsAttributesSystemRuntimeInfo();
                if(relationKindsAttributesSystemInfo == null || relationKindsAttributesSystemInfo.isEmpty()){
                    relationKindsAttributesSystemInfo = systemMaintenanceOperator.getAllRelationKindsAttributesSystemInfo();
                }else{
                    ZonedDateTime _latestRecordDatetime = relationKindsAttributesSystemInfo.values().iterator().next().get(0).getCreateDate();
                    ZonedDateTime oneDayBeforeCurrent = currentDateTime.minusDays(1);
                    if(_latestRecordDatetime.isBefore(oneDayBeforeCurrent)){
                        relationKindsAttributesSystemInfo = systemMaintenanceOperator.getAllRelationKindsAttributesSystemInfo();
                    }
                }

                List<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoList =
                        systemMaintenanceOperator.
                                getPeriodicCollectedConceptionKindCorrelationRuntimeInfo(SystemMaintenanceOperator.PeriodicCollectedInfoRetrieveLogic.LATEST);
                if(conceptionKindCorrelationInfoList == null || conceptionKindCorrelationInfoList.isEmpty()){
                    conceptionKindCorrelationInfoList = systemMaintenanceOperator.getConceptionKindCorrelationRuntimeInfo(1);
                }else{
                    ZonedDateTime _latestRecordDatetime = conceptionKindCorrelationInfoList.get(0).getCreateDate();
                    ZonedDateTime oneDayBeforeCurrent = currentDateTime.minusDays(1);
                    if(_latestRecordDatetime.isBefore(oneDayBeforeCurrent)){
                        conceptionKindCorrelationInfoList = systemMaintenanceOperator.getConceptionKindCorrelationRuntimeInfo(1);
                    }
                }
                this.conceptionDataRealtimeInfoWidget.renderConceptionDataRealtimeInfo(realtimeConceptionList,conceptionKindsAttributesSystemInfo);
                this.relationDataRealtimeInfoWidget.renderRelationDataRealtimeInfo(realtimeRelationList,relationKindsAttributesSystemInfo);
                this.conceptionDataCorrelationRealtimeInfoWidget.renderConceptionDataCorrelationRealtimeInfo(conceptionKindCorrelationInfoList);

                Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = new HashSet<>(conceptionKindCorrelationInfoList);
                this.realtimeConceptionDataCorrelationChartWidget.renderConceptionDataCorrelationRealtimeInfo(conceptionKindCorrelationInfoSet);
            }
        } catch (CoreRealmServiceEntityExploreException | CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            targetCoreRealm.closeGlobalSession();
        }
    }
}
