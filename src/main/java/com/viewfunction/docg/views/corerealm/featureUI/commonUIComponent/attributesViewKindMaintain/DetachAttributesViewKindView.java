package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributesViewKindMaintain;

import com.vaadin.flow.component.AttachEvent;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.AttributeKindDetachedFromAttributesViewKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDetachedFromConceptionKindEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DetachAttributesViewKindView extends VerticalLayout {
    public enum RelatedKindType { ConceptionKind, AttributeKind }
    private String attributesViewKindUID;
    private String attributeKindUID;
    private String conceptionKindName;
    private Dialog containerDialog;
    private RelatedKindType relatedKindType;

    public DetachAttributesViewKindView(String attributesViewKindUID,RelatedKindType relatedKindType){
        this.attributesViewKindUID = attributesViewKindUID;
        this.relatedKindType = relatedKindType;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        String detachMessage = null;
        switch (this.relatedKindType){
            case AttributeKind :
                AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
                detachMessage = "本操作将从 UID 为 "+this.attributesViewKindUID+" 的属性视图类型中移除属性类型 "+targetAttributeKind.getAttributeKindName()+"("+targetAttributeKind.getAttributeDataType().toString()+")"+" - "+targetAttributeKind.getAttributeKindUID()+" ,请确认执行操作。";
                break;
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                detachMessage = "本操作将 UID 为 "+this.attributesViewKindUID+" 的属性视图类型从概念类型 "+targetConceptionKind.getConceptionKindName()+"("+targetConceptionKind.getConceptionKindDesc()+")"+"中移除 ,请确认执行操作。";
        }

        H4 viewTitle = new H4(detachMessage);
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

        Button confirmButton = new Button("确认移除属性视图类型",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doDetachAttributesViewKind();
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

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    private void doDetachAttributesViewKind(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        switch (this.relatedKindType){
            case AttributeKind :
                AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
                AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
                try {
                    boolean detachResult = targetAttributesViewKind.detachAttributeKind(this.attributeKindUID);
                    if(detachResult){
                        CommonUIOperationUtil.showPopupNotification("从属性视图类型 "+this.attributesViewKindUID+ " 中移除属性类型 "+ targetAttributeKind.getAttributeKindName() +" : "+ targetAttributeKind.getAttributeKindUID() +" 成功", NotificationVariant.LUMO_SUCCESS);
                        if(containerDialog != null){
                            containerDialog.close();
                        }
                        AttributeKindDetachedFromAttributesViewKindEvent attributeKindDetachedFromAttributesViewKindEvent = new AttributeKindDetachedFromAttributesViewKindEvent();
                        attributeKindDetachedFromAttributesViewKindEvent.setAttributesViewKindUID(this.attributesViewKindUID);
                        attributeKindDetachedFromAttributesViewKindEvent.setAttributeKindUID(targetAttributeKind.getAttributeKindUID());
                        ResourceHolder.getApplicationBlackboard().fire(attributeKindDetachedFromAttributesViewKindEvent);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("从属性视图类型 "+this.attributesViewKindUID+ " 中移除属性类型 "+ targetAttributeKind.getAttributeKindName() +" : "+ targetAttributeKind.getAttributeKindUID() +" 失败", NotificationVariant.LUMO_ERROR);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                try {
                    boolean detachResult = targetConceptionKind.detachAttributesViewKind(this.attributesViewKindUID);
                    if(detachResult){
                        CommonUIOperationUtil.showPopupNotification("从概念类型 "+targetConceptionKind.getConceptionKindName()+ " 中移除属性视图类型 "+ this.attributesViewKindUID +" 成功", NotificationVariant.LUMO_SUCCESS);
                        if(containerDialog != null){
                            containerDialog.close();
                        }
                        AttributesViewKindDetachedFromConceptionKindEvent attributesViewKindDetachedFromConceptionKindEvent = new AttributesViewKindDetachedFromConceptionKindEvent();
                        attributesViewKindDetachedFromConceptionKindEvent.setAttributesViewKindUID(this.attributesViewKindUID);
                        attributesViewKindDetachedFromConceptionKindEvent.setConceptionKindName(targetConceptionKind.getConceptionKindName());
                        ResourceHolder.getApplicationBlackboard().fire(attributesViewKindDetachedFromConceptionKindEvent);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("从概念类型 "+targetConceptionKind.getConceptionKindName()+ " 中移除属性视图类型 "+ this.attributesViewKindUID +" 失败", NotificationVariant.LUMO_ERROR);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
