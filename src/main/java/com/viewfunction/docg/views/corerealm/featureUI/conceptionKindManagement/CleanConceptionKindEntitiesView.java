package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

public class CleanConceptionKindEntitiesView extends VerticalLayout {

    private String conceptionKind;

    private Dialog containerDialog;

    public CleanConceptionKindEntitiesView(String conceptionKind){
        this.conceptionKind = conceptionKind;

        H4 viewTitle = new H4("本操作将清除概念类型 "+conceptionKind+" 中的所有概念实体,请确认执行操作。");
        viewTitle.getStyle().set("font-size","var(--lumo-font-size-m)").set("color","var(--lumo-error-color)");
        add(viewTitle);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认清除概念实体",new Icon(VaadinIcon.CHECK_CIRCLE));
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
                doCleanConceptionKindEntities();
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

    private void doCleanConceptionKindEntities(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConceptionKind =coreRealm.getConceptionKind(this.conceptionKind);
        try {
            EntitiesOperationResult entitiesOperationResult =targetConceptionKind.purgeAllEntities();
            EntitiesOperationStatistics entitiesOperationStatistics = entitiesOperationResult.getOperationStatistics();
            entitiesOperationStatistics.getStartTime();
            entitiesOperationStatistics.getFinishTime();
            entitiesOperationStatistics.getOperationSummary();
            entitiesOperationStatistics.getSuccessItemsCount();
            entitiesOperationStatistics.getFailItemsCount();
            if(containerDialog != null){
                containerDialog.close();
            }
            showPopupNotification(entitiesOperationResult,NotificationVariant.LUMO_SUCCESS);
        } catch (CoreRealmServiceRuntimeException e) {
            showPopupNotification("概念类型 "+conceptionKind+" 实例清除操作发生服务器端错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        }
    }

    private void showPopupNotification(EntitiesOperationResult entitiesOperationResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念类型 "+conceptionKind+" 实例清除操作成功"));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setFlexGrow(1,text);
        notification.add(layout);

        VerticalLayout notificationMessageContainer = new VerticalLayout();
        notificationMessageContainer.add(new Div(new Text("操作摘要: "+entitiesOperationResult.getOperationStatistics().getOperationSummary())));
        notificationMessageContainer.add(new Div(new Text("删除成功实体数: "+entitiesOperationResult.getOperationStatistics().getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("删除失败实体数: "+entitiesOperationResult.getOperationStatistics().getFailItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+entitiesOperationResult.getOperationStatistics().getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+entitiesOperationResult.getOperationStatistics().getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.open();
    }

    private void showPopupNotification(String notificationMessage,NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text(notificationMessage));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);
        notification.add(layout);
        notification.open();
    }
}
