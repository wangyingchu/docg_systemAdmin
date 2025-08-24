package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionAction;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain.KindActionsDateView;

import java.util.Set;

public class ConceptionKindActionsConfigurationView extends VerticalLayout {

    private String conceptionKind;
    private KindActionsDateView kindActionsDateView;
    private int conceptionActionsDataViewHeightOffset;

    public ConceptionKindActionsConfigurationView(String conceptionKindName,int conceptionKindDetailIntegratedInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();
        this.conceptionKind = conceptionKindName;
        this.conceptionActionsDataViewHeightOffset = conceptionKindDetailIntegratedInfoViewHeightOffset +106;
        this.kindActionsDateView = new KindActionsDateView(KindActionsDateView.KindType.ConceptionKind,conceptionKind,conceptionActionsDataViewHeightOffset);
        add(this.kindActionsDateView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderActionAccessUI();
    }

    private void renderActionAccessUI(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        try {
            if(targetConceptionKind != null){
                Set<ConceptionAction> actionSet = targetConceptionKind.getActions();
                kindActionsDateView.renderConceptionActionDataUI(actionSet);
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }  finally {
            coreRealm.closeGlobalSession();
        }
    }
}
