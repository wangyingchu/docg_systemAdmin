package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.timeScaleEventsMaintain.AttachTimeScaleEventsOfConceptionEntityView;
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
    private HorizontalLayout doesNotContainsTemporalInfoMessage;
    public ConceptionEntityTemporalInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntityTemporalInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityTemporalInfoViewHeightOffset = conceptionEntityTemporalInfoViewHeightOffset+106;

        doesNotContainsTemporalInfoMessage = new HorizontalLayout();
        doesNotContainsTemporalInfoMessage.setSpacing(true);
        doesNotContainsTemporalInfoMessage.setPadding(true);
        doesNotContainsTemporalInfoMessage.setMargin(true);
        doesNotContainsTemporalInfoMessage.setWidth(100,Unit.PERCENTAGE);
        doesNotContainsTemporalInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前概念实体中不包含时间序列相关信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");

        Button attachTimeScalaEntityButton = new Button();
        attachTimeScalaEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_LARGE);
        Icon buttonIcon0 = LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create();
        buttonIcon0.setSize("18px");
        attachTimeScalaEntityButton.setIcon(buttonIcon0);
        attachTimeScalaEntityButton.setTooltipText("关联时间序列事件");
        attachTimeScalaEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachTimeScaleEventsOfConceptionEntityView();
            }
        });
        attachTimeScalaEntityButton.getStyle().set("top","-10px").set("position","relative");
        doesNotContainsTemporalInfoMessage.add(messageLogo,messageLabel,attachTimeScalaEntityButton);
        add(doesNotContainsTemporalInfoMessage);

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

        temporalEntityAndChartContainer.setVisible(false);
        conceptionEntityTemporalTimelineChart.setVisible(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            temporalEntityAndChartContainer.setHeight(event.getHeight()-this.conceptionEntityTemporalInfoViewHeightOffset-140, Unit.PIXELS);
            conceptionEntityTemporalSunburstChart.setHeight(event.getHeight()-this.conceptionEntityTemporalInfoViewHeightOffset-180, Unit.PIXELS);
            conceptionEntityTemporalDataView.setWidth(event.getWidth() - 820,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            temporalEntityAndChartContainer.setHeight(browserHeight-this.conceptionEntityTemporalInfoViewHeightOffset-140,Unit.PIXELS);
            conceptionEntityTemporalSunburstChart.setHeight(browserHeight-this.conceptionEntityTemporalInfoViewHeightOffset-180,Unit.PIXELS);
            conceptionEntityTemporalDataView.setWidth(browserWidth - 820,Unit.PIXELS);
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
                    if(timeScaleDataPairList == null || timeScaleDataPairList.size() == 0){
                        conceptionEntityTemporalTimelineChart.setVisible(false);
                        conceptionEntityTemporalDataView.setVisible(false);
                        conceptionEntityTemporalSunburstChart.setVisible(false);
                        CommonUIOperationUtil.showPopupNotification("UID 为 "+conceptionEntityUID+" 的概念实体中不包含时间序列相关信息", NotificationVariant.LUMO_CONTRAST,5000, Notification.Position.BOTTOM_START);
                    }else{
                        doesNotContainsTemporalInfoMessage.setVisible(false);
                        temporalEntityAndChartContainer.setVisible(true);
                        conceptionEntityTemporalTimelineChart.setVisible(true);

                        conceptionEntityTemporalTimelineChart.renderTemporalTimelineInfo(timeScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                        conceptionEntityTemporalDataView.renderTemporalDataInfo(timeScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                        conceptionEntityTemporalSunburstChart.renderTemporalSunburstInfo(timeScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                    }
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

    private void renderAttachTimeScaleEventsOfConceptionEntityView(){
        AttachTimeScaleEventsOfConceptionEntityView attachTimeScaleEventsOfConceptionEntityView = new AttachTimeScaleEventsOfConceptionEntityView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create(),"关联时间序列事件",null,true,760,670,false);
        fixSizeWindow.setWindowContent(attachTimeScaleEventsOfConceptionEntityView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
