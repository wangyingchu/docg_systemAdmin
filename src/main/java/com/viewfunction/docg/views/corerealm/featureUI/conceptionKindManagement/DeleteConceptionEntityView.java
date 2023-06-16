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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityDeletedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class DeleteConceptionEntityView extends VerticalLayout {

    private ConceptionEntityValue conceptionEntityValue;
    private Dialog containerDialog;
    private String conceptionKind;
    public DeleteConceptionEntityView(String conceptionKind,ConceptionEntityValue conceptionEntityValue){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityValue = conceptionEntityValue;
        H4 viewTitle = new H4("本操作将删除概念类型 "+conceptionKind+" 中的UID为 "+conceptionEntityValue.getConceptionEntityUID()+" 的概念实体以及与该实体相关的所有关系实体,请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-l)").set("color","var(--lumo-error-color)");
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

        Button confirmButton = new Button("确认删除概念实体",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doRemoveConceptionEntity();
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

    private void doRemoveConceptionEntity(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
            if(targetConceptionKind != null){
                ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(conceptionEntityValue.getConceptionEntityUID());
                if(targetConceptionEntity == null){
                    CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 删除操作失败",NotificationVariant.LUMO_ERROR);
                }else{
                    List<String> entityBelongedConceptionKinds =targetConceptionEntity.getAllConceptionKindNames();
                    boolean operationResult = targetConceptionKind.deleteEntity(conceptionEntityValue.getConceptionEntityUID());
                    if(operationResult){
                        ConceptionEntityDeletedEvent conceptionEntityDeletedEvent = new ConceptionEntityDeletedEvent();
                        conceptionEntityDeletedEvent.setConceptionEntityUID(conceptionEntityValue.getConceptionEntityUID());
                        conceptionEntityDeletedEvent.setEntityAllConceptionKindNames(entityBelongedConceptionKinds);
                        ResourceHolder.getApplicationBlackboard().fire(conceptionEntityDeletedEvent);
                        CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 删除操作成功", NotificationVariant.LUMO_SUCCESS);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 删除操作失败",NotificationVariant.LUMO_ERROR);
                    }
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 删除操作失败",NotificationVariant.LUMO_ERROR);
            }
            if(containerDialog != null){
                containerDialog.close();
            }
        } catch (CoreRealmServiceRuntimeException e) {
            CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 删除操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        }
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
