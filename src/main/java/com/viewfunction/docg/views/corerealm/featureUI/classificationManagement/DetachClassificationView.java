package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ClassificationDetachedEvent;
import com.viewfunction.docg.element.eventHandling.ClassificationRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DetachClassificationView extends VerticalLayout {

    private String parentClassificationName;
    private String childClassificationName;
    private Dialog containerDialog;

    public DetachClassificationView(String parentClassificationName, String childClassificationName){
        this.parentClassificationName = parentClassificationName;
        this.childClassificationName = childClassificationName;

        H4 viewTitle = new H4("本操作将从分类 "+this.parentClassificationName+" 中移除子分类 "+this.childClassificationName+", 请确认执行操作。");
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

        Button confirmButton = new Button("确认移除子分类",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doDetachClassification();
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

    private void doDetachClassification(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        boolean removeResult = false;
        try {
            Classification targetClassification = coreRealm.getClassification(this.parentClassificationName);
            if(targetClassification != null){
                removeResult = targetClassification.detachChildClassification(this.childClassificationName);
            }
        }catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
        if(removeResult){
            ClassificationDetachedEvent classificationDetachedEvent = new ClassificationDetachedEvent();
            classificationDetachedEvent.setParentClassificationName(this.parentClassificationName);
            classificationDetachedEvent.setChildClassificationName(this.childClassificationName);
            ResourceHolder.getApplicationBlackboard().fire(classificationDetachedEvent);
            CommonUIOperationUtil.showPopupNotification("分类 "+this.parentClassificationName+" 移除子分类 "+this.childClassificationName+" 操作成功", NotificationVariant.LUMO_SUCCESS);
            if(containerDialog != null){
                containerDialog.close();
            }
        }
    }
}
