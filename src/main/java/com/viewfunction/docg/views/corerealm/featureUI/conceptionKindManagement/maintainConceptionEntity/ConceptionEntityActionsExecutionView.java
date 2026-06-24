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

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionAction;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionEntityActionsExecutionView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntityActionsExecutionViewHeightOffset;
    private HorizontalLayout doesNotContainsConceptionActionsMessage;
    private ConceptionEntityActionsDoExecuteView conceptionEntityActionsDoExecuteView;

    public ConceptionEntityActionsExecutionView(String conceptionKind, String conceptionEntityUID, int conceptionEntityIntegratedInfoViewHeightOffset){
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

        String emptyMessage = " 当前概念实体所属概念类型中未配置自定义动作";
        if(this.conceptionEntityUID == null) {
            //conceptionEntityUID 为空，表示当前操作对象为概念类型的外部属性视图对象
            emptyMessage = " 当前概念类型中未配置自定义动作";
        }
        this.conceptionEntityActionsExecutionViewHeightOffset = conceptionEntityIntegratedInfoViewHeightOffset +106;

        doesNotContainsConceptionActionsMessage = new HorizontalLayout();
        doesNotContainsConceptionActionsMessage.setSpacing(true);
        doesNotContainsConceptionActionsMessage.setPadding(true);
        doesNotContainsConceptionActionsMessage.setMargin(true);
        doesNotContainsConceptionActionsMessage.setWidth(100, Unit.PERCENTAGE);
        doesNotContainsConceptionActionsMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(emptyMessage);
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsConceptionActionsMessage.add(messageLogo,messageLabel);
        add(doesNotContainsConceptionActionsMessage);

        conceptionEntityActionsDoExecuteView = new ConceptionEntityActionsDoExecuteView(this.conceptionKind,this.conceptionEntityUID,this.conceptionEntityActionsExecutionViewHeightOffset);
        conceptionEntityActionsDoExecuteView.setContainerExternalAttributesAccessView(this);
        add(conceptionEntityActionsDoExecuteView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderConceptionEntityExecuteActionsUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderConceptionEntityExecuteActionsUI(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        try {
            if(targetConceptionKind != null){
                Set<ConceptionAction> conceptionActionSet = targetConceptionKind.getActions();
                List<ConceptionAction> conceptionActionsList = new ArrayList<>();
                conceptionActionsList.addAll(conceptionActionSet);
                if(conceptionActionsList.size() == 0){
                    conceptionEntityActionsDoExecuteView.setVisible(false);
                    doesNotContainsConceptionActionsMessage.setVisible(true);
                }else{
                    doesNotContainsConceptionActionsMessage.setVisible(false);
                    conceptionEntityActionsDoExecuteView.setVisible(true);
                    conceptionEntityActionsDoExecuteView.renderConceptionEntityActionsUI(conceptionActionsList);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
            }
        }  finally {
            coreRealm.closeGlobalSession();
        }
    }
}
