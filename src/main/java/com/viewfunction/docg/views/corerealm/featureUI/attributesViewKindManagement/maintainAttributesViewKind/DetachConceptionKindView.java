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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDetachedFromConceptionKindEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DetachConceptionKindView extends VerticalLayout {
    private String attributesViewKindUID;
    private ConceptionKind conceptionKind;
    private Dialog containerDialog;

    public DetachConceptionKindView(String attributesViewKindUID,ConceptionKind conceptionKind){
        this.attributesViewKindUID = attributesViewKindUID;
        this.conceptionKind = conceptionKind;
        H4 viewTitle = new H4("本操作将UID 为 "+this.attributesViewKindUID+" 的属性视图类型从概念类型 "+this.conceptionKind.getConceptionKindName()+"("+this.conceptionKind.getConceptionKindDesc()+")"+" 中移除 ,请确认执行操作。");
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

        Button confirmButton = new Button("确认从概念类型中移除",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doDetachConceptionKind();
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

    private void doDetachConceptionKind(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind.getConceptionKindName());
        if(targetConceptionKind != null){
            try {
                boolean detachResult = targetConceptionKind.detachAttributesViewKind(this.attributesViewKindUID);
                if(detachResult){
                    CommonUIOperationUtil.showPopupNotification("从概念类型 "+this.conceptionKind.getConceptionKindName()+ " 中移除属性视图类型 "+ this.attributesViewKindUID +" 成功", NotificationVariant.LUMO_SUCCESS);
                    if(containerDialog != null){
                        containerDialog.close();
                    }
                    AttributesViewKindDetachedFromConceptionKindEvent attributesViewKindDetachedFromConceptionKindEvent = new AttributesViewKindDetachedFromConceptionKindEvent();
                    attributesViewKindDetachedFromConceptionKindEvent.setAttributesViewKindUID(this.attributesViewKindUID);
                    attributesViewKindDetachedFromConceptionKindEvent.setConceptionKindName(this.conceptionKind.getConceptionKindName());
                    ResourceHolder.getApplicationBlackboard().fire(attributesViewKindDetachedFromConceptionKindEvent);
                }else{
                   CommonUIOperationUtil.showPopupNotification("从概念类型 "+this.conceptionKind.getConceptionKindName()+ " 中移除属性视图类型 "+ this.attributesViewKindUID +" 失败", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        coreRealm.closeGlobalSession();
    }
}
