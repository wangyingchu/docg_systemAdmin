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

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.GeospatialScaleDataPair;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEvent;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialScaleEventsMaintain.AttachGeospatialScaleEventsOfConceptionEntityView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial.ConceptionEntitySpatialDataView;

import java.util.List;

public class ConceptionEntitySpatialInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntitySpatialInfoViewHeightOffset;
    private HorizontalLayout doesNotContainsSpatialInfoMessage;
    private Registration listener;
    private ConceptionEntitySpatialDataView conceptionEntitySpatialDataView;

    public ConceptionEntitySpatialInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntitySpatialInfoViewHeightOffset){

        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntitySpatialInfoViewHeightOffset = conceptionEntitySpatialInfoViewHeightOffset +106;

        doesNotContainsSpatialInfoMessage = new HorizontalLayout();
        doesNotContainsSpatialInfoMessage.setSpacing(true);
        doesNotContainsSpatialInfoMessage.setPadding(true);
        doesNotContainsSpatialInfoMessage.setMargin(true);
        doesNotContainsSpatialInfoMessage.setWidth(100, Unit.PERCENTAGE);
        doesNotContainsSpatialInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前概念实体中不包含地理空间相关信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");

        Button attachGeoScalaEntityButton = new Button();
        attachGeoScalaEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_LARGE);
        Icon buttonIcon0 = LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create();
        buttonIcon0.setSize("18px");
        attachGeoScalaEntityButton.setIcon(buttonIcon0);
        attachGeoScalaEntityButton.setTooltipText("关联地理空间区域事件");
        attachGeoScalaEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachGeospatialScaleEventsOfConceptionEntityView();
            }
        });
        attachGeoScalaEntityButton.getStyle().set("top","-10px").set("position","relative");
        doesNotContainsSpatialInfoMessage.add(messageLogo,messageLabel,attachGeoScalaEntityButton);
        add(doesNotContainsSpatialInfoMessage);

        conceptionEntitySpatialDataView = new ConceptionEntitySpatialDataView();
        add(conceptionEntitySpatialDataView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            //conceptionEntitySpatialDataView.setWidth(event.getWidth() - 820,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            //conceptionEntitySpatialDataView.setWidth(browserWidth - 820,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderEntitySpatialInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        try {
            if(targetConceptionKind != null){
                ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                if(targetEntity != null){
                    List<GeospatialScaleDataPair> geospatialScaleDataPairList = targetEntity.getAttachedGeospatialScaleDataPairs();
                    if(geospatialScaleDataPairList == null || geospatialScaleDataPairList.size() == 0){
                        CommonUIOperationUtil.showPopupNotification("UID 为 "+conceptionEntityUID+" 的概念实体中不包含地理空间相关信息", NotificationVariant.LUMO_CONTRAST,5000, Notification.Position.BOTTOM_START);
                        conceptionEntitySpatialDataView.setVisible(false);
                        doesNotContainsSpatialInfoMessage.setVisible(true);
                    }else{
                        doesNotContainsSpatialInfoMessage.setVisible(false);
                        conceptionEntitySpatialDataView.setVisible(true);
                        conceptionEntitySpatialDataView.renderSpatialDataInfo(geospatialScaleDataPairList,this.conceptionKind,this.conceptionEntityUID);
                    }
                }else{
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }  finally {
            coreRealm.closeGlobalSession();
        }
    }

    private void renderAttachGeospatialScaleEventsOfConceptionEntityView(){
        AttachGeospatialScaleEventsOfConceptionEntityView.AttachGeospatialScaleEventsOfConceptionEntityCallback
                attachGeospatialScaleEventsOfConceptionEntityCallback = new AttachGeospatialScaleEventsOfConceptionEntityView.AttachGeospatialScaleEventsOfConceptionEntityCallback() {
            @Override
            public void onSuccess(List<GeospatialScaleEvent> resultEventList) {
                renderEntitySpatialInfo();
            }
        };
        AttachGeospatialScaleEventsOfConceptionEntityView attachGeospatialScaleEventsOfConceptionEntityView = new AttachGeospatialScaleEventsOfConceptionEntityView(this.conceptionKind,this.conceptionEntityUID);
        attachGeospatialScaleEventsOfConceptionEntityView.setAttachGeospatialScaleEventsOfConceptionEntityCallback(attachGeospatialScaleEventsOfConceptionEntityCallback);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create(),"关联地理空间区域事件",null,true,1270,580,false);
        fixSizeWindow.setWindowContent(attachGeospatialScaleEventsOfConceptionEntityView);
        attachGeospatialScaleEventsOfConceptionEntityView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
