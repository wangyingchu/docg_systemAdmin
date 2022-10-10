package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal.ConceptionEntityTemporalDataView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal.ConceptionEntityTemporalSunburstChart;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal.ConceptionEntityTemporalTimelineChart;

import java.util.List;

public class ConceptionEntityTemporalInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntityTemporalInfoViewHeightOffset;
    private ConceptionEntityTemporalTimelineChart conceptionEntityTemporalTimelineChart;
    private ConceptionEntityTemporalDataView conceptionEntityTemporalDataView;
    private ConceptionEntityTemporalSunburstChart conceptionEntityTemporalSunburstChart;
    private Registration listener;
    private HorizontalLayout temporalEntityAndChartContainer;
    public ConceptionEntityTemporalInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntityTemporalInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityTemporalInfoViewHeightOffset = conceptionEntityTemporalInfoViewHeightOffset+106;

        temporalEntityAndChartContainer = new HorizontalLayout();
        temporalEntityAndChartContainer.setPadding(false);
        temporalEntityAndChartContainer.setSpacing(false);
        temporalEntityAndChartContainer.setMargin(false);
        add(temporalEntityAndChartContainer);

        conceptionEntityTemporalDataView = new ConceptionEntityTemporalDataView();
        conceptionEntityTemporalSunburstChart = new ConceptionEntityTemporalSunburstChart();
        temporalEntityAndChartContainer.add(conceptionEntityTemporalSunburstChart);
        temporalEntityAndChartContainer.add(conceptionEntityTemporalDataView);

        conceptionEntityTemporalTimelineChart = new ConceptionEntityTemporalTimelineChart();
        add(conceptionEntityTemporalTimelineChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            temporalEntityAndChartContainer.setHeight(event.getHeight()-this.conceptionEntityTemporalInfoViewHeightOffset-140, Unit.PIXELS);
            conceptionEntityTemporalSunburstChart.setHeight(event.getHeight()-this.conceptionEntityTemporalInfoViewHeightOffset-180, Unit.PIXELS);
            conceptionEntityTemporalDataView.setWidth(event.getWidth() - 790,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            temporalEntityAndChartContainer.setHeight(browserHeight-this.conceptionEntityTemporalInfoViewHeightOffset-140,Unit.PIXELS);
            conceptionEntityTemporalSunburstChart.setHeight(browserHeight-this.conceptionEntityTemporalInfoViewHeightOffset-180,Unit.PIXELS);
            conceptionEntityTemporalDataView.setWidth(browserWidth - 790,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderEntityTemporalInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        try {
            if(targetConceptionKind != null){
                ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                if(targetEntity != null){
                    List<TimeScaleDataPair> timeScaleDataPairList = targetEntity.getAttachedTimeScaleDataPairs();
                    conceptionEntityTemporalTimelineChart.renderTemporalTimelineInfo(timeScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                    conceptionEntityTemporalDataView.renderTemporalDataInfo(timeScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                    conceptionEntityTemporalSunburstChart.renderTemporalSunburstInfo(timeScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                }else{
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }   finally {
            coreRealm.closeGlobalSession();
        }
    }
}
