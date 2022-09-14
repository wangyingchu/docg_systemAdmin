package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.tabs.Tab;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import org.vaadin.tabs.PagedTabs;

public class ConceptionEntitySpatialInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntitySpatialInfoViewHeightOffset;
    private ConceptionEntitySpatialDetailView globalConceptionEntitySpatialDetailView;
    private ConceptionEntitySpatialDetailView countryConceptionEntitySpatialDetailView;
    private ConceptionEntitySpatialDetailView localConceptionEntitySpatialDetailView;
    public ConceptionEntitySpatialInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntitySpatialInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntitySpatialInfoViewHeightOffset = conceptionEntitySpatialInfoViewHeightOffset+100;

        VerticalLayout container = new VerticalLayout();
        container.setPadding(false);
        container.setSpacing(false);
        container.setMargin(false);

        PagedTabs tabs = new PagedTabs(container);
        tabs.getElement().getStyle().set("width","100%");

        globalConceptionEntitySpatialDetailView = new ConceptionEntitySpatialDetailView(this.conceptionEntitySpatialInfoViewHeightOffset);
        Tab tab0 = tabs.add("", globalConceptionEntitySpatialDetailView,false);
        Span globalInfoSpan =new Span();
        Icon globalInfoIcon = new Icon(VaadinIcon.GLOBE_WIRE);
        globalInfoIcon.setSize("14px");
        Label globalInfoLabel = new Label(" 全球坐标系地理空间信息");
        globalInfoSpan.add(globalInfoIcon,globalInfoLabel);
        tab0.add(globalInfoSpan);

        countryConceptionEntitySpatialDetailView = new ConceptionEntitySpatialDetailView(this.conceptionEntitySpatialInfoViewHeightOffset);
        Tab tab1 = tabs.add("", countryConceptionEntitySpatialDetailView,false);
        Span countryInfoSpan =new Span();
        Icon countryInfoIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE);
        countryInfoIcon.setSize("14px");
        Label countryInfoLabel = new Label(" 国家坐标系地理空间信息");
        countryInfoSpan.add(countryInfoIcon,countryInfoLabel);
        tab1.add(countryInfoSpan);

        localConceptionEntitySpatialDetailView = new ConceptionEntitySpatialDetailView(this.conceptionEntitySpatialInfoViewHeightOffset);
        Tab tab2 = tabs.add("", localConceptionEntitySpatialDetailView,false);
        Span localInfoSpan =new Span();
        Icon localInfoIcon = new Icon(VaadinIcon.HOME);
        localInfoIcon.setSize("14px");
        Label localInfoLabel = new Label(" 本地坐标系地理空间信息");
        localInfoSpan.add(localInfoIcon,localInfoLabel);
        tab2.add(localInfoSpan);

        add(tabs,container);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
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
                    String _GLGeometryContent = targetEntity.getGLGeometryContent();
                    String _CLGeometryContent = targetEntity.getCLGeometryContent();
                    String _LLGeometryContent = targetEntity.getLLGeometryContent();
                    if(_GLGeometryContent == null & _CLGeometryContent == null & _LLGeometryContent == null){
                        CommonUIOperationUtil.showPopupNotification("UID 为 "+conceptionEntityUID+" 的概念实体中不包含地理空间信息", NotificationVariant.LUMO_CONTRAST,5000, Notification.Position.MIDDLE);
                    }else{
                        if(_GLGeometryContent != null){
                            globalConceptionEntitySpatialDetailView.setConceptionEntity(targetEntity);
                            globalConceptionEntitySpatialDetailView.setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel.Global);
                            globalConceptionEntitySpatialDetailView.renderEntitySpatialDetailInfo();
                        }
                        if(_CLGeometryContent != null){
                            countryConceptionEntitySpatialDetailView.setConceptionEntity(targetEntity);
                            countryConceptionEntitySpatialDetailView.setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel.Country);
                            countryConceptionEntitySpatialDetailView.renderEntitySpatialDetailInfo();
                        }
                        if(_LLGeometryContent != null){
                            localConceptionEntitySpatialDetailView.setConceptionEntity(targetEntity);
                            localConceptionEntitySpatialDetailView.setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel.Local);
                            localConceptionEntitySpatialDetailView.renderEntitySpatialDetailInfo();
                        }
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
}
