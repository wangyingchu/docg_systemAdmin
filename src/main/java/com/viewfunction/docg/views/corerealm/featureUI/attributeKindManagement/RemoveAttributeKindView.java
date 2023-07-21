package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.AttributeKindRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class RemoveAttributeKindView extends VerticalLayout {
    private AttributeKindMetaInfo attributeKindMetaInfo;
    private Dialog containerDialog;

    public RemoveAttributeKindView(AttributeKindMetaInfo attributeKindMetaInfo){
        this.attributeKindMetaInfo = attributeKindMetaInfo;

        H4 viewTitle = new H4("本操作将删除 UID 为 "+this.attributeKindMetaInfo.getKindUID()+" 的属性类型: "+this.attributeKindMetaInfo.getKindName()+"["+this.attributeKindMetaInfo.getAttributeDataType()+"]"+"("+this.attributeKindMetaInfo.getKindDesc()+") ,请确认执行操作。");
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

        Button confirmButton = new Button("确认删除属性类型",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doRemoveAttributeKind();
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

    private void doRemoveAttributeKind(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            boolean operationResult = coreRealm.removeAttributeKind(this.attributeKindMetaInfo.getKindUID());
            if(operationResult){
                AttributeKindRemovedEvent attributeKindRemovedEvent = new AttributeKindRemovedEvent();
                attributeKindRemovedEvent.setAttributeKindName(this.attributeKindMetaInfo.getKindName());
                attributeKindRemovedEvent.setAttributeKindDesc(this.attributeKindMetaInfo.getKindDesc());
                attributeKindRemovedEvent.setAttributeKindUID(this.attributeKindMetaInfo.getKindUID());
                attributeKindRemovedEvent.setAttributeKindDataType(this.attributeKindMetaInfo.getAttributeDataType());
                ResourceHolder.getApplicationBlackboard().fire(attributeKindRemovedEvent);

                CommonUIOperationUtil.showPopupNotification("属性类型 "+this.attributeKindMetaInfo.getKindUID()+" 删除操作成功", NotificationVariant.LUMO_SUCCESS);
            }else{
                CommonUIOperationUtil.showPopupNotification("属性类型 "+this.attributeKindMetaInfo.getKindUID()+" 删除操作失败",NotificationVariant.LUMO_ERROR);
            }
            if(containerDialog != null){
                containerDialog.close();
            }
        } catch (CoreRealmServiceRuntimeException e) {
            CommonUIOperationUtil.showPopupNotification("属性类型 "+this.attributeKindMetaInfo.getKindUID()+" 删除操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        }
    }
}
