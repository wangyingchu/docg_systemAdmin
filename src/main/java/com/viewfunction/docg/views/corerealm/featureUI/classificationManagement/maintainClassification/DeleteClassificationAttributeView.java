package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ClassificationAttributeDeletedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DeleteClassificationAttributeView extends VerticalLayout {
    private Dialog containerDialog;
    private String classificationName;
    private String attributeName;

    public DeleteClassificationAttributeView(String classificationName, String attributeName){
        this.classificationName = classificationName;
        this.attributeName = attributeName;

        H4 viewTitle = new H4("本操作将删除分类 "+this.classificationName +" 的属性 "+this.attributeName+",请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-m)").set("color","var(--lumo-error-color)");
        add(viewTitle);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认删除分类属性",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doRemoveClassificationProperty();
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

    private void doRemoveClassificationProperty() {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        if(targetClassification == null){
            CommonUIOperationUtil.showPopupNotification("分类 "+ classificationName +" 不存在", NotificationVariant.LUMO_ERROR);
        }else{
            if(!targetClassification.hasAttribute(attributeName)){
                    CommonUIOperationUtil.showPopupNotification("名称为 "+ classificationName +" 的分类中不存在属性 "+attributeName, NotificationVariant.LUMO_ERROR);
            }else{
                try {
                    boolean removeResult = targetClassification.removeAttribute(attributeName);
                    if(removeResult){
                        CommonUIOperationUtil.showPopupNotification("在名称为 "+ classificationName +" 的分类中删除属性 "+attributeName+" 成功", NotificationVariant.LUMO_SUCCESS);
                        ClassificationAttributeDeletedEvent classificationAttributeDeletedEvent = new ClassificationAttributeDeletedEvent();
                        classificationAttributeDeletedEvent.setClassificationName(classificationName);
                        classificationAttributeDeletedEvent.setAttributeName(attributeName);
                        ResourceHolder.getApplicationBlackboard().fire(classificationAttributeDeletedEvent);
                        if(containerDialog != null){
                            containerDialog.close();
                        }
                    }else{
                        CommonUIOperationUtil.showPopupNotification("在名称为 "+ classificationName +" 的分类中删除属性 "+ attributeName+" 失败", NotificationVariant.LUMO_ERROR);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    CommonUIOperationUtil.showPopupNotification("在名称为 "+ classificationName +" 的分类中删除属性 "+ attributeName+" 失败", NotificationVariant.LUMO_ERROR);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
