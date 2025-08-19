package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    private HorizontalLayout doesNotContainsConceptionActionInfoMessage;
    private KindActionsDateView kindActionsDateView;
    private int conceptionActionsDataViewHeightOffset;

    public ConceptionKindActionsConfigurationView(String conceptionKindName,int conceptionKindDetailIntegratedInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKindName;
        this.conceptionActionsDataViewHeightOffset = conceptionKindDetailIntegratedInfoViewHeightOffset +106;

        String emptyMessage = " 当前概念类型中未配置自定义动作";
        doesNotContainsConceptionActionInfoMessage = new HorizontalLayout();
        doesNotContainsConceptionActionInfoMessage.setSpacing(true);
        doesNotContainsConceptionActionInfoMessage.setPadding(true);
        doesNotContainsConceptionActionInfoMessage.setMargin(true);
        doesNotContainsConceptionActionInfoMessage.setWidth(100, Unit.PERCENTAGE);
        doesNotContainsConceptionActionInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(emptyMessage);
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsConceptionActionInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsConceptionActionInfoMessage);

        kindActionsDateView = new KindActionsDateView(KindActionsDateView.KindType.ConceptionKind,conceptionKind,conceptionActionsDataViewHeightOffset);
        add(kindActionsDateView);
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
                if(actionSet.size() == 0){
                    kindActionsDateView.setVisible(false);
                    doesNotContainsConceptionActionInfoMessage.setVisible(true);
                }else{
                    doesNotContainsConceptionActionInfoMessage.setVisible(false);
                    kindActionsDateView.setVisible(true);
                    kindActionsDateView.renderActionDataUI(actionSet);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }  finally {
            coreRealm.closeGlobalSession();
        }
    }
}
