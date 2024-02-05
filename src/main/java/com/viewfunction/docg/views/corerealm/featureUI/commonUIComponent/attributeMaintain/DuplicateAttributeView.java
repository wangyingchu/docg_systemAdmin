package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;

public class DuplicateAttributeView extends VerticalLayout {
    public enum KindType {ConceptionKind,RelationKind}
    private String kindName;
    private String originalAttributeName;
    private DuplicateAttributeView.KindType kindType;
    private Dialog containerDialog;
    private DuplicateAttributeCallback duplicateAttributeCallback;
    private TextField newAttributeNameField;


    public interface DuplicateAttributeCallback {
        public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics);
    }

    public DuplicateAttributeView(DuplicateAttributeView.KindType kindType, String kindName, String originalAttributeName){
        this.kindType = kindType;
        this.kindName = kindName;
        this.originalAttributeName = originalAttributeName;

        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (kindType){
            case ConceptionKind -> kindIcon = VaadinIcon.CUBE.create();
            case RelationKind ->  kindIcon = VaadinIcon.CONNECT_O.create();
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");

        Icon attributeIcon = VaadinIcon.BULLETS.create();
        attributeIcon.setSize("12px");
        attributeIcon.getStyle().set("padding-left","3px").set("padding-right","2px");

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon,this.kindName));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeIcon,this.originalAttributeName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        this.newAttributeNameField = new TextField("复制目标属性名称");
        this.newAttributeNameField.setWidthFull();
        this.newAttributeNameField.setRequired(true);
        this.newAttributeNameField.setRequiredIndicatorVisible(true);
        this.newAttributeNameField.setTitle("请输入复制目标属性名称");
        add(this.newAttributeNameField);

        Button confirmButton = new Button("确定复制属性",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                duplicateAttributeCallback();
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

    }

    public void setDuplicateAttributeCallback(DuplicateAttributeCallback duplicateAttributeCallback) {
        this.duplicateAttributeCallback = duplicateAttributeCallback;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void duplicateAttributeCallback(){
        String newAttributeName = newAttributeNameField.getValue();
        if(newAttributeName == null || newAttributeName.isEmpty()){
            CommonUIOperationUtil.showPopupNotification("请输入复制目标属性名称", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
        }else{
            EntitiesOperationStatistics entitiesOperationStatistics = null;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            switch(this.kindType){
                case ConceptionKind:
                    com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.kindName);
                    try {
                        entitiesOperationStatistics = targetConceptionKind.duplicateEntityAttribute(this.originalAttributeName,newAttributeName);
                        if(this.containerDialog != null){
                            this.containerDialog.close();
                        }
                        if(this.duplicateAttributeCallback != null){
                            this.duplicateAttributeCallback.onSuccess(entitiesOperationStatistics);
                        }
                    } catch (CoreRealmServiceRuntimeException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case RelationKind:
            }
        }
    }
}
