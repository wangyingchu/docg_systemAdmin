package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ClassificationRemovedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class RemoveClassificationView extends VerticalLayout {

    private ClassificationMetaInfo classificationMetaInfo;
    private Dialog containerDialog;
    private Checkbox removeWithOffspringFlagCheckbox;

    public RemoveClassificationView(ClassificationMetaInfo classificationMetaInfo){
        this.classificationMetaInfo = classificationMetaInfo;

        H4 viewTitle = new H4("本操作将删除分类 "+classificationMetaInfo.getClassificationName()+" 以及与其相连的所有关联信息,请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","var(--lumo-error-color)");
        viewTitle.getStyle().set("padding-bottom", "10px").
                set("padding-top", "20px").
                set("padding-left", "5px").
                set("padding-right", "5px");
        add(viewTitle);

        this.removeWithOffspringFlagCheckbox = new Checkbox("同时删除所有后代分类");
        this.removeWithOffspringFlagCheckbox.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        this.setHorizontalComponentAlignment(Alignment.END,this.removeWithOffspringFlagCheckbox);
        add(this.removeWithOffspringFlagCheckbox);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认删除分类",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doRemoveClassification();
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

    private void doRemoveClassification(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Boolean removeWithOffspring = this.removeWithOffspringFlagCheckbox.getValue();
        boolean removeResult = false;
        try {
            if(removeWithOffspring){
                removeResult = coreRealm.removeClassificationWithOffspring(this.classificationMetaInfo.getClassificationName());
            }else{
                removeResult = coreRealm.removeClassification(this.classificationMetaInfo.getClassificationName());
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }

        if(removeResult){
            ClassificationRemovedEvent classificationRemovedEvent = new ClassificationRemovedEvent();
            classificationRemovedEvent.setClassificationName(this.classificationMetaInfo.getClassificationName());
            ResourceHolder.getApplicationBlackboard().fire(classificationRemovedEvent);
            CommonUIOperationUtil.showPopupNotification("分类 "+this.classificationMetaInfo.getClassificationName()+" 删除操作成功", NotificationVariant.LUMO_SUCCESS);
            if(containerDialog != null){
                containerDialog.close();
            }
        }
    }
}
