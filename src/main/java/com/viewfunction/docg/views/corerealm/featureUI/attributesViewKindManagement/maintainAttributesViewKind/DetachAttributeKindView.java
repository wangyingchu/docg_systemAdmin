package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.AttributeKindDetachedFromAttributesViewKindEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DetachAttributeKindView extends VerticalLayout {
    private String attributesViewKindUID;
    private AttributeKind attributeKind;
    private Dialog containerDialog;

    public DetachAttributeKindView(String attributesViewKindUID, AttributeKind attributeKind){
        this.attributesViewKindUID = attributesViewKindUID;
        this.attributeKind = attributeKind;

        H4 viewTitle = new H4("本操作将从UID 为 "+this.attributesViewKindUID+" 的属性视图类型中移除属性类型 "+this.attributeKind.getAttributeKindName()+"("+this.attributeKind.getAttributeDataType().toString()+")"+" - "+this.attributeKind.getAttributeKindUID()+" ,请确认执行操作。");
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

        Button confirmButton = new Button("确认移除属性类型",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doDetachAttributeKind();
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

    private void doDetachAttributeKind(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind selfAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        if(selfAttributesViewKind != null){
            try {
                boolean detachResult = selfAttributesViewKind.detachAttributeKind(this.attributeKind.getAttributeKindUID());
                if(detachResult){
                    CommonUIOperationUtil.showPopupNotification("从属性视图类型 "+this.attributesViewKindUID+ " 中移除属性类型 "+ attributeKind.getAttributeKindName() +" : "+ attributeKind.getAttributeKindUID() +" 成功", NotificationVariant.LUMO_SUCCESS);
                    if(containerDialog != null){
                        containerDialog.close();
                    }
                    AttributeKindDetachedFromAttributesViewKindEvent attributeKindDetachedFromAttributesViewKindEvent = new AttributeKindDetachedFromAttributesViewKindEvent();
                    attributeKindDetachedFromAttributesViewKindEvent.setAttributesViewKindUID(this.attributesViewKindUID);
                    attributeKindDetachedFromAttributesViewKindEvent.setAttributeKindUID(attributeKind.getAttributeKindUID());
                    ResourceHolder.getApplicationBlackboard().fire(attributeKindDetachedFromAttributesViewKindEvent);
                }else{
                    CommonUIOperationUtil.showPopupNotification("从属性视图类型 "+this.attributesViewKindUID+ " 中移除属性类型 "+ attributeKind.getAttributeKindName() +" : "+ attributeKind.getAttributeKindUID() +" 失败", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        coreRealm.closeGlobalSession();
    }
}
