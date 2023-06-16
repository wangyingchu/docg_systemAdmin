package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionKindRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class RemoveConceptionKindView extends VerticalLayout {

    private String conceptionKind;

    private Dialog containerDialog;

    public RemoveConceptionKindView(String conceptionKind){
        this.conceptionKind = conceptionKind;

        H4 viewTitle = new H4("本操作将删除概念类型 "+conceptionKind+" 以及该类型包含的所有概念实体,请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","var(--lumo-error-color)");
        viewTitle.getStyle().set("padding-bottom", "10px").
                set("padding-top", "20px").
                set("padding-left", "5px").
                set("padding-right", "5px");
        add(viewTitle);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认删除概念类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        setHorizontalComponentAlignment(Alignment.END,buttonsContainerLayout);
        buttonsContainerLayout.add(confirmButton,cancelButton);
        add(buttonsContainerLayout);

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doRemoveConceptionKind();
            }
        });

        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(containerDialog != null){
                    containerDialog.close();
                }
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doRemoveConceptionKind(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            boolean operationResult = coreRealm.removeConceptionKind(this.conceptionKind,true);
            if(operationResult){
                ConceptionKindRemovedEvent conceptionKindRemovedEvent = new ConceptionKindRemovedEvent();
                conceptionKindRemovedEvent.setConceptionKindName(this.conceptionKind);
                ResourceHolder.getApplicationBlackboard().fire(conceptionKindRemovedEvent);

                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 删除操作成功", NotificationVariant.LUMO_SUCCESS);
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 删除操作失败",NotificationVariant.LUMO_ERROR);
            }
            if(containerDialog != null){
                containerDialog.close();
            }
        } catch (CoreRealmServiceRuntimeException e) {
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 删除操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        }
    }
}
