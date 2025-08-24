package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain.KindActionsDateView;

import java.util.Set;

public class RelationKindActionsConfigurationView extends VerticalLayout {

    private String relationKind;
    private KindActionsDateView kindActionsDateView;
    private int relationKindDetailIntegratedInfoViewHeightOffset;

    public RelationKindActionsConfigurationView(String relationKind,int relationKindDetailIntegratedInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();
        this.relationKind = relationKind;
        this.relationKindDetailIntegratedInfoViewHeightOffset = relationKindDetailIntegratedInfoViewHeightOffset +161;
        this.kindActionsDateView = new KindActionsDateView(KindActionsDateView.KindType.RelationKind,relationKind,this.relationKindDetailIntegratedInfoViewHeightOffset);
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
        RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        try {
            if(targetRelationKind != null){
                Set<RelationAction> actionSet = targetRelationKind.getActions();
                kindActionsDateView.renderRelationActionDataUI(actionSet);
            }else{
                CommonUIOperationUtil.showPopupNotification("关系类型 "+relationKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }  finally {
            coreRealm.closeGlobalSession();
        }
    }
}
