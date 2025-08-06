package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData.ConceptionEntityExternalDataView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionEntityExternalAttributesAccessView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntityExternalDataViewHeightOffset;
    private HorizontalLayout doesNotContainsSpatialInfoMessage;
    private ConceptionEntityExternalDataView conceptionEntityExternalDataView;

    public ConceptionEntityExternalAttributesAccessView(String conceptionKind, String conceptionEntityUID, int conceptionEntityIntegratedInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

        String emptyMessage = " 当前概念实体所属概念类型中未配置 External Value 类型属性视图";
        if(this.conceptionEntityUID == null) {
            //conceptionEntityUID 为空，表示当前操作对象为概念类型的外部属性视图对象
            emptyMessage = " 当前概念类型中未配置 External Value 类型属性视图";
        }
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityIntegratedInfoViewHeightOffset +106;

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
        NativeLabel messageLabel = new NativeLabel(emptyMessage);
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsSpatialInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsSpatialInfoMessage);

        conceptionEntityExternalDataView = new ConceptionEntityExternalDataView(this.conceptionKind,this.conceptionEntityUID,this.conceptionEntityExternalDataViewHeightOffset);
        conceptionEntityExternalDataView.setContainerExternalAttributesAccessView(this);
        add(conceptionEntityExternalDataView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderExternalDataAccessUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderExternalDataAccessUI(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        try {
            if(targetConceptionKind != null){
                Set<AttributesViewKind> attributesViewKindSet = targetConceptionKind.getAvailableExternalValueAttributesViewKinds();
                List<AttributesViewKind> conceptionKindExternalAttributeViewList = new ArrayList<>();
                conceptionKindExternalAttributeViewList.addAll(attributesViewKindSet);
                if(conceptionKindExternalAttributeViewList.size() == 0){
                    conceptionEntityExternalDataView.setVisible(false);
                    doesNotContainsSpatialInfoMessage.setVisible(true);
                }else{
                    doesNotContainsSpatialInfoMessage.setVisible(false);
                    conceptionEntityExternalDataView.setVisible(true);
                    conceptionEntityExternalDataView.renderExternalAttributesAccessDataUI(conceptionKindExternalAttributeViewList);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }  finally {
            coreRealm.closeGlobalSession();
        }
    }
}
