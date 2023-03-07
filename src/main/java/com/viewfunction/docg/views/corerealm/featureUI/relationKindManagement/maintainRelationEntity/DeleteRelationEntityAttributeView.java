package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.RelationEntityAttributeDeletedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DeleteRelationEntityAttributeView extends VerticalLayout {
    private Dialog containerDialog;
    private String relationKind;
    private String relationEntityUID;
    private String attributeName;
    public DeleteRelationEntityAttributeView(String relationKind, String relationEntityUID, String attributeName){
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
        this.attributeName = attributeName;

        H4 viewTitle = new H4("本操作将删除关系实体 "+this.relationKind +" - "+ this.relationEntityUID +" 的属性 "+this.attributeName+",请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-m)").set("color","var(--lumo-error-color)");
        add(viewTitle);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认删除关系实体属性",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doRemoveRelationEntityProperty();
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

    private void doRemoveRelationEntityProperty() {
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        if(targetRelationKind == null){
            CommonUIOperationUtil.showPopupNotification("关系类型 "+ relationKind +" 不存在", NotificationVariant.LUMO_ERROR);
        }else{
            RelationEntity targetRelationEntity = targetRelationKind.getEntityByUID(this.relationEntityUID);
            if(targetRelationEntity == null){
                CommonUIOperationUtil.showPopupNotification("关系类型 "+ relationKind +" 中不存在 UID 为"+ relationEntityUID +" 的关系实体", NotificationVariant.LUMO_ERROR);
            }else{
                if(!targetRelationEntity.hasAttribute(attributeName)){
                    CommonUIOperationUtil.showPopupNotification("UID 为 "+ relationEntityUID +" 的关系实体中不存在属性 "+attributeName, NotificationVariant.LUMO_ERROR);
                }else{
                    try {
                        boolean removeResult = targetRelationEntity.removeAttribute(attributeName);
                        if(removeResult){
                            CommonUIOperationUtil.showPopupNotification("在 UID 为 "+ relationEntityUID +" 的关系实体中删除属性 "+attributeName+" 成功", NotificationVariant.LUMO_SUCCESS);
                            RelationEntityAttributeDeletedEvent relationEntityAttributeDeletedEvent = new RelationEntityAttributeDeletedEvent();
                            relationEntityAttributeDeletedEvent.setRelationEntityUID(this.relationEntityUID);
                            relationEntityAttributeDeletedEvent.setRelationKindName(this.relationKind);
                            relationEntityAttributeDeletedEvent.setAttributeName(this.attributeName);
                            ResourceHolder.getApplicationBlackboard().fire(relationEntityAttributeDeletedEvent);
                            if(containerDialog != null){
                                containerDialog.close();
                            }
                        }else{
                            CommonUIOperationUtil.showPopupNotification("在 UID 为 "+ relationEntityUID +" 的关系实体中删除属性 "+attributeName+" 失败", NotificationVariant.LUMO_ERROR);
                        }
                    } catch (CoreRealmServiceRuntimeException e) {
                        CommonUIOperationUtil.showPopupNotification("在 UID 为 "+ relationEntityUID +" 的关系实体中删除属性 "+attributeName+" 失败", NotificationVariant.LUMO_ERROR);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
