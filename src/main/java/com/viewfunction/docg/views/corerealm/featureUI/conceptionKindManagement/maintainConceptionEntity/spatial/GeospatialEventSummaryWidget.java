package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.util.ArrayList;
import java.util.List;

public class GeospatialEventSummaryWidget extends HorizontalLayout {
    private GeospatialScaleEvent geospatialScaleEvent;
    private GeospatialScaleEntity geospatialScaleEntity;
    private ConceptionEntitySpatialDataView conceptionEntitySpatialDataView;

    public GeospatialEventSummaryWidget(GeospatialScaleEvent geospatialScaleEvent, GeospatialScaleEntity geospatialScaleEntity){
        this.geospatialScaleEvent = geospatialScaleEvent;
        this.geospatialScaleEntity = geospatialScaleEntity;
        String geospatialRegionName = geospatialScaleEvent.getGeospatialRegionName();

        GeospatialRegion.GeospatialScaleGrade timeScaleGrade = geospatialScaleEvent.getGeospatialScaleGrade();

        HorizontalLayout dateInfoContainer = new HorizontalLayout();
        dateInfoContainer.setSpacing(false);
        dateInfoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(dateInfoContainer);

        Icon summaryIcon = LineAwesomeIconsSvg.MAP_MARKED_ALT_SOLID.create();
        summaryIcon.setSize("16px");
        summaryIcon.getStyle().set("padding-right","5px");
        dateInfoContainer.add(summaryIcon);

        NativeLabel geospatialTextLabel1 = new NativeLabel(this.geospatialScaleEntity.getGeospatialCode());
        geospatialTextLabel1.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(geospatialTextLabel1);

        Icon divIcon1_0 = VaadinIcon.LINE_H.create();
        divIcon1_0.setSize("6px");
        dateInfoContainer.add(divIcon1_0);

        NativeLabel geospatialTextLabel2 = new NativeLabel(this.geospatialScaleEntity.getChineseName());
        geospatialTextLabel2.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(geospatialTextLabel2);

        if(this.geospatialScaleEntity.getEnglishName() != null){
            Icon divIcon1_1 = VaadinIcon.LINE_H.create();
            divIcon1_1.setSize("6px");
            dateInfoContainer.add(divIcon1_1);

            NativeLabel geospatialTextLabel3 = new NativeLabel(this.geospatialScaleEntity.getEnglishName());
            geospatialTextLabel3.addClassNames("text-xs","font-semibold","text-secondary");
            dateInfoContainer.add(geospatialTextLabel3);
        }

        Icon divIcon1_2 = VaadinIcon.LINE_V.create();
        divIcon1_2.setSize("8px");
        dateInfoContainer.add(divIcon1_2);

        NativeLabel geospatialScaleGradeLabel = new NativeLabel(timeScaleGrade.toString());
        geospatialScaleGradeLabel.addClassNames("text-xs","font-semibold","text-secondary");
        dateInfoContainer.add(geospatialScaleGradeLabel);

        Icon infoIcon = VaadinIcon.INFO_CIRCLE_O.create();
        infoIcon.setSize("12px");
        infoIcon.getStyle().set("padding-left","5px");
        infoIcon.setTooltipText(geospatialRegionName);
        dateInfoContainer.add(infoIcon);

        NativeLabel eventTitleLabel = new NativeLabel(this.geospatialScaleEvent.getEventComment());
        add(eventTitleLabel);

        Button showEventButton = new Button("事件详情");
        showEventButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showEventButton.getStyle().set("font-size","12px");
        showEventButton.setIcon(VaadinIcon.EYE.create());
        showEventButton.setTooltipText("显示关联地理空间事件详情");
        showEventButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedEventEntityUI();
            }
        });
        add(showEventButton);

        Icon divIcon2 = VaadinIcon.LINE_V.create();
        divIcon2.setSize("6px");
        add(divIcon2);

        Button showGeospatialEntityButton = new Button("相关地理空间区域实体");
        showGeospatialEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showGeospatialEntityButton.getStyle().set("font-size","12px");
        Icon buttonIcon = VaadinIcon.GLOBE_WIRE.create();
        buttonIcon.setSize("16px");
        showGeospatialEntityButton.setIcon(buttonIcon);
        showGeospatialEntityButton.setTooltipText("显示关联地理空间区域实体");
        showGeospatialEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedGeospatialScaleEntityUI();
            }
        });
        add(showGeospatialEntityButton);

        Icon divIcon3 = VaadinIcon.LINE_V.create();
        divIcon3.setSize("6px");
        add(divIcon3);

        Button deleteEventButton = new Button("删除事件");
        deleteEventButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ERROR);
        deleteEventButton.getStyle().set("font-size","10px");
        deleteEventButton.setIcon(VaadinIcon.TRASH.create());
        deleteEventButton.setTooltipText("删除此地理空间区域关联事件");
        deleteEventButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderDeleteEventUI();
            }
        });
        add(deleteEventButton);

        setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private void renderRelatedEventEntityUI(){
        String targetConceptionKind = RealmConstant.GeospatialScaleEventClass;
        String targetConceptionEntityUID = this.geospatialScaleEvent.getGeospatialScaleEventUID();

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8, Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(targetConceptionKind);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(targetConceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(LineAwesomeIconsSvg.MAP_MARKED_ALT_SOLID.create(),"地理空间关联事件详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void renderRelatedGeospatialScaleEntityUI(){
        String targetConceptionKind = RealmConstant.GeospatialScaleEntityClass;
        String targetConceptionEntityUID = this.geospatialScaleEntity.getGeospatialScaleEntityUID();

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8, Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(targetConceptionKind);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(targetConceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(VaadinIcon.GLOBE_WIRE.create(),"地理空间区域实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void renderDeleteEventUI(){
        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认删除地理空间区域关联事件",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行删除地理空间区域关联事件操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDeleteEvent();
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDeleteEvent(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        String timeScaleEventUID = this.geospatialScaleEvent.getGeospatialScaleEventUID();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(RealmConstant.GeospatialScaleEventClass);
        try {
            boolean deleteResult = targetConceptionKind.deleteEntity(timeScaleEventUID);
            if(deleteResult){
                CommonUIOperationUtil.showPopupNotification("删除地理空间区域关联事件 "+ timeScaleEventUID +" 成功", NotificationVariant.LUMO_SUCCESS);
                if(this.conceptionEntitySpatialDataView != null){
                    this.conceptionEntitySpatialDataView.refreshSpatialEventAttributesInfo();
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("删除地理空间区域关联事件 "+ timeScaleEventUID +" 失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void setConceptionEntitySpatialDataView(ConceptionEntitySpatialDataView conceptionEntitySpatialDataView) {
        this.conceptionEntitySpatialDataView = conceptionEntitySpatialDataView;
    }
}
