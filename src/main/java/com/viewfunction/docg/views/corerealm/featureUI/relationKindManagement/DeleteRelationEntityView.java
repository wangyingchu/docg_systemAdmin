package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.RelationEntityDeletedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

public class DeleteRelationEntityView extends VerticalLayout {
    private RelationEntity relationEntity;
    private RelationEntityValue relationEntityValue;
    private Dialog containerDialog;
    private String relationKindName;
    private String relationEntityUID;
    private String toConceptionEntityUID;
    private String fromConceptionEntityUID;

    public DeleteRelationEntityView(RelationEntity relationEntity){
        this.relationEntity = relationEntity;
        this.relationKindName = this.relationEntity.getRelationKindName();
        this.relationEntityUID = this.relationEntity.getRelationEntityUID();
        this.fromConceptionEntityUID = this.relationEntity.getFromConceptionEntityUID();
        this.toConceptionEntityUID = this.relationEntity.getToConceptionEntityUID();
        renderUIElement();
    }

    public DeleteRelationEntityView(String relationKindName,RelationEntityValue relationEntityValue){
        this.relationEntityValue = relationEntityValue;
        this.relationKindName = relationKindName;
        this.relationEntityUID = this.relationEntityValue.getRelationEntityUID();
        this.fromConceptionEntityUID = this.relationEntityValue.getFromConceptionEntityUID();
        this.toConceptionEntityUID = this.relationEntityValue.getToConceptionEntityUID();
        renderUIElement();
    }

    private void renderUIElement(){
        H4 viewTitle = new H4("本操作将删除关系类型 "+this.relationKindName+" 中的UID为 "+this.relationEntityUID+" 的关系实体,请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-m)").set("color","var(--lumo-error-color)");
        add(viewTitle);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认删除关系实体",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doRemoveRelationEntity();
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

    private void doRemoveRelationEntity(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKindName);
            if(targetRelationKind != null){
                RelationEntity targetRelationEntity = targetRelationKind.getEntityByUID(this.relationEntityUID);
                if(targetRelationEntity == null){
                    CommonUIOperationUtil.showPopupNotification("关系实体 "+this.relationKindName+" - "+this.relationEntityUID+" 删除操作失败",NotificationVariant.LUMO_ERROR);
                }else{
                    boolean operationResult = targetRelationKind.deleteEntity(targetRelationEntity.getRelationEntityUID());
                    if(operationResult){
                        RelationEntityDeletedEvent relationEntityDeletedEvent = new RelationEntityDeletedEvent();
                        relationEntityDeletedEvent.setRelationEntityUID(this.relationEntityUID);
                        relationEntityDeletedEvent.setRelationKindName(this.relationKindName);
                        relationEntityDeletedEvent.setToConceptionEntityUID(this.toConceptionEntityUID);
                        relationEntityDeletedEvent.setFromConceptionEntityUID(this.fromConceptionEntityUID);
                        ResourceHolder.getApplicationBlackboard().fire(relationEntityDeletedEvent);
                        CommonUIOperationUtil.showPopupNotification("关系实体 "+this.relationKindName+" - "+this.relationEntityUID+" 删除操作成功", NotificationVariant.LUMO_SUCCESS);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("关系实体 "+this.relationKindName+" - "+this.relationEntityUID+" 删除操作失败",NotificationVariant.LUMO_ERROR);
                    }
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("关系实体 "+this.relationKindName+" - "+this.relationEntityUID+" 删除操作失败",NotificationVariant.LUMO_ERROR);
            }
            if(containerDialog != null){
                containerDialog.close();
            }
        } catch (CoreRealmServiceRuntimeException e) {
            CommonUIOperationUtil.showPopupNotification("关系实体 "+this.relationKindName+" - "+this.relationEntityUID+" 删除操作发生服务器端错误", NotificationVariant.LUMO_ERROR);
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
