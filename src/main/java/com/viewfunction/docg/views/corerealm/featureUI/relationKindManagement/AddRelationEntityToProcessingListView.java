package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

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

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationEntityValue;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.RelationEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;

public class AddRelationEntityToProcessingListView extends VerticalLayout {

    private RelationEntityValue relationEntityValue;
    private Dialog containerDialog;
    private String relationKind;
    private TextArea commentTextField;

    public AddRelationEntityToProcessingListView(String relationKind, RelationEntityValue relationEntityValue){
        this.relationKind = relationKind;
        this.relationEntityValue = relationEntityValue;
        H5 viewTitle = new H5("将关系类型 "+this.relationKind+" 中的UID为 "+this.relationEntityValue.getRelationEntityUID()+" 的关系实体添加入待处理数据列表,请确认执行操作。");
        viewTitle.getStyle().set("padding-bottom", "10px").
                set("padding-top", "20px").
                set("padding-left", "5px").
                set("padding-right", "5px");
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
                doAddRelationEntityToProcessingDataList();
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

    private void doAddRelationEntityToProcessingDataList(){
        RelationEntityResourceHolderVO relationEntityResourceHolderVO = new RelationEntityResourceHolderVO(relationKind,relationEntityValue.getRelationEntityUID());
        if(commentTextField.getValue() != null && !commentTextField.getValue().equals("")){
            relationEntityResourceHolderVO.setComment(commentTextField.getValue());
        }
        boolean addResult = ResourceHolder.addRelationEntityToProcessingList(relationEntityResourceHolderVO);
        if(addResult){
            CommonUIOperationUtil.showPopupNotification("关系实体 "+relationKind+" - "+relationEntityValue.getRelationEntityUID()+" 加入待处理数据列表成功", NotificationVariant.LUMO_SUCCESS,5000, Notification.Position.BOTTOM_START);
            if(containerDialog != null){
                containerDialog.close();
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("关系实体 "+relationKind+" - "+relationEntityValue.getRelationEntityUID()+" 加入待处理数据列表失败", NotificationVariant.LUMO_ERROR);
        }
    }
}
