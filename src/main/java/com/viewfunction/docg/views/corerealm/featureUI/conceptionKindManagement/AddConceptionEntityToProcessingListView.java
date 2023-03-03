package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ConceptionEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;

public class AddConceptionEntityToProcessingListView  extends VerticalLayout {

    private ConceptionEntityValue conceptionEntityValue;
    private Dialog containerDialog;
    private String conceptionKind;
    private TextArea commentTextField;

    public AddConceptionEntityToProcessingListView(String conceptionKind, ConceptionEntityValue conceptionEntityValue){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityValue = conceptionEntityValue;
        H5 viewTitle = new H5("将概念类型 "+conceptionKind+" 中的UID为 "+conceptionEntityValue.getConceptionEntityUID()+" 的概念实体添加入待处理数据列表,请确认执行操作。");
        add(viewTitle);

        commentTextField = new TextArea();
        commentTextField.setPlaceholder("待处理数据备注......");
        commentTextField.setWidth(100, Unit.PERCENTAGE);
        commentTextField.setHeight(80,Unit.PIXELS);
        add(commentTextField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认添加入待处理数据列表",new Icon(VaadinIcon.CHECK_CIRCLE));

        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        setHorizontalComponentAlignment(Alignment.END,buttonsContainerLayout);
        buttonsContainerLayout.add(confirmButton,cancelButton);
        add(buttonsContainerLayout);

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAddConceptionEntityToProcessingDataList();
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

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doAddConceptionEntityToProcessingDataList(){
        ConceptionEntityResourceHolderVO conceptionEntityResourceHolderVO = new ConceptionEntityResourceHolderVO(conceptionKind,conceptionEntityValue.getConceptionEntityUID());
        if(commentTextField.getValue() != null && !commentTextField.getValue().equals("")){
            conceptionEntityResourceHolderVO.setComment(commentTextField.getValue());
        }
        boolean addResult = ResourceHolder.addConceptionEntityToProcessingList(conceptionEntityResourceHolderVO);
        if(addResult){
            CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 加入待处理数据列表成功", NotificationVariant.LUMO_SUCCESS,5000, Notification.Position.BOTTOM_START);
            if(containerDialog != null){
                containerDialog.close();
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" - "+conceptionEntityValue.getConceptionEntityUID()+" 加入待处理数据列表失败", NotificationVariant.LUMO_ERROR);
        }
    }
}
