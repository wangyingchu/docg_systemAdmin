package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributesViewKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EditKindActionView extends VerticalLayout {
    private String kindName;
    private KindActionsDateView.KindType kindType;
    private TextField actionNameField;
    private TextField actionDescField;
    private TextField actionFullClassNameField;
    private ComboBox<AttributesViewKindMetaInfo> attributeKindFilterSelect;
    private KindActionsDateView parentKindActionsDateView;
    private String actionName;
    private Dialog containerDialog;
    private H6 errorMessage;

    public EditKindActionView(String actionName,KindActionsDateView.KindType kindType, String kindName){
        this.actionName = actionName;
        this.kindName = kindName;
        this.kindType = kindType;
        this.setMargin(false);
        this.setWidthFull();

        Icon kindIcon = null;
        switch (this.kindType){
            case ConceptionKind ->kindIcon = VaadinIcon.CUBE.create();
            case RelationKind ->kindIcon = VaadinIcon.CONNECT_O.create();
        }

        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.kindName));

        Icon actionIcon = VaadinIcon.CONTROLLER.create();
        actionIcon.setSize("12px");
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(actionIcon, this.actionName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);
        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.actionNameField = new TextField("自定义动作名称 - Action Name");
        this.actionNameField.setWidthFull();
        this.actionNameField.setRequired(true);
        this.actionNameField.setRequiredIndicatorVisible(true);
        this.actionNameField.setReadOnly(true);
        this.actionNameField.setTitle("请输入自定义动作名称");
        add(actionNameField);

        this.actionDescField = new TextField("自定义动作名称描述 - Action Description");
        this.actionDescField.setWidthFull();
        this.actionDescField.setRequired(true);
        this.actionDescField.setRequiredIndicatorVisible(true);
        this.actionDescField.setTitle("请输入自定义动作名称");
        add(actionDescField);

        this.actionFullClassNameField = new TextField("自定义动作执行类全名 - Action Logic Executor Implementation Class Full Name");
        this.actionFullClassNameField.setWidthFull();
        this.actionFullClassNameField.setRequired(true);
        this.actionFullClassNameField.setRequiredIndicatorVisible(true);
        this.actionFullClassNameField.setTitle("请输入自定义动作类全名");
        add(actionFullClassNameField);

        this.attributeKindFilterSelect = new ComboBox("引用属性视图类型 - Referenced AttributesViewKind");
        this.attributeKindFilterSelect.setPageSize(30);
        this.attributeKindFilterSelect.setPlaceholder("请选择要引用的属性视图类型");
        this.attributeKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.attributeKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<AttributesViewKindMetaInfo>() {
            @Override
            public String apply(AttributesViewKindMetaInfo attributeKindMetaInfo) {

                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+") - "+attributeKindMetaInfo.getKindUID();
                return itemLabelValue;
            }
        });
        this.attributeKindFilterSelect.setRenderer(createRenderer());
        add(this.attributeKindFilterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认更新自定义动作信息",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doUpdateKindAction();
            }
        });

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<AttributesViewKindMetaInfo> runtimeAttributesViewKindMetaInfoList = coreRealm.getAttributesViewKindsMetaInfo();
            attributeKindFilterSelect.setItems(runtimeAttributesViewKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        switch (this.kindType){
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
                ConceptionAction targetAction = targetConceptionKind.getAction(actionName);
                if(targetAction != null){
                    actionNameField.setValue(targetAction.getActionName());
                    actionDescField.setValue(targetAction.getActionDesc());
                    actionFullClassNameField.setValue(targetAction.getActionImplementationClass());
                    AttributesViewKind referencedAttributesViewKind = targetAction.getReferencedAttributesViewKind();
                    if(referencedAttributesViewKind != null){
                        ListDataProvider dtaProvider=(ListDataProvider)attributeKindFilterSelect.getDataProvider();
                        Collection<AttributesViewKindMetaInfo> attributesViewKindMetaInfoList = dtaProvider.getItems();
                        for(AttributesViewKindMetaInfo attributesViewKindMetaInfo : attributesViewKindMetaInfoList){
                            if(attributesViewKindMetaInfo.getKindUID().equals(referencedAttributesViewKind.getAttributesViewKindUID())){
                                attributeKindFilterSelect.setValue(attributesViewKindMetaInfo);
                            }
                        }
                    }
                }
                break;
            case RelationKind :
                RelationKind targetRelationKind = coreRealm.getRelationKind(kindName);
                RelationAction targetAction1 = targetRelationKind.getAction(actionName);
                if(targetAction1 != null){
                    actionNameField.setValue(targetAction1.getActionName());
                    actionDescField.setValue(targetAction1.getActionDesc());
                    actionFullClassNameField.setValue(targetAction1.getActionImplementationClass());
                    AttributesViewKind referencedAttributesViewKind = targetAction1.getReferencedAttributesViewKind();
                    if(referencedAttributesViewKind != null){
                        ListDataProvider dtaProvider=(ListDataProvider)attributeKindFilterSelect.getDataProvider();
                        Collection<AttributesViewKindMetaInfo> attributesViewKindMetaInfoList = dtaProvider.getItems();
                        for(AttributesViewKindMetaInfo attributesViewKindMetaInfo : attributesViewKindMetaInfoList){
                            if(attributesViewKindMetaInfo.getKindUID().equals(referencedAttributesViewKind.getAttributesViewKindUID())){
                                attributeKindFilterSelect.setValue(attributesViewKindMetaInfo);
                            }
                        }
                    }
                }
                break;
        }
        coreRealm.closeGlobalSession();
    }

    private void doUpdateKindAction(){
        String actionName = this.actionNameField.getValue();
        String actionDesc = this.actionDescField.getValue();
        String actionFullClassName = this.actionFullClassNameField.getValue();

        boolean inputValidateResult = true;
        if(actionName.equals("")){
            inputValidateResult = false;
            this.actionNameField.setInvalid(true);
        }
        if(actionDesc.equals("")){
            inputValidateResult = false;
            this.actionDescField.setInvalid(true);
        }
        if(actionFullClassName.equals("")){
            inputValidateResult = false;
            this.actionFullClassNameField.setInvalid(true);
        }
        if(inputValidateResult){
            hideErrorMessage();
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            switch (this.kindType){
                case ConceptionKind :
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
                    ConceptionAction targetAction = targetConceptionKind.getAction(actionName);
                    if(targetAction == null){
                        this.actionNameField.setInvalid(true);
                        showErrorMessage("概念类型 "+kindName+" 中未注册自定义动作 "+actionName);
                    }else{
                        boolean updateResult1 = targetAction.updateActionDesc(actionDesc);
                        boolean updateResult2 = targetAction.updateActionImplementationClass(actionFullClassName);
                        boolean updateResult3 = true;
                        if(this.attributeKindFilterSelect.getValue() != null){
                            AttributesViewKindMetaInfo attributesViewKindMetaInfo = this.attributeKindFilterSelect.getValue();
                            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindMetaInfo.getKindUID());
                            try {
                                updateResult3 = targetAction.setReferencedAttributesViewKind(targetAttributesViewKind);
                            } catch (CoreRealmServiceRuntimeException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            targetAction.clearReferencedAttributesViewKind();
                        }
                        if(updateResult1 & updateResult2 & updateResult3){
                            CommonUIOperationUtil.showPopupNotification("自定义动作 "+actionName+" 信息更新成功", NotificationVariant.LUMO_SUCCESS);
                            if(this.parentKindActionsDateView != null){
                                this.parentKindActionsDateView.refreshKindActionsInfo();
                            }
                            if(this.containerDialog != null){
                                this.containerDialog.close();
                            }
                        }else{
                            CommonUIOperationUtil.showPopupNotification("自定义动作信息更新错误",NotificationVariant.LUMO_ERROR);
                        }
                    }
                    break;
                case RelationKind :
                    RelationKind targetRelationKind = coreRealm.getRelationKind(kindName);
                    RelationAction targetAction1 = targetRelationKind.getAction(actionName);
                    if(targetAction1 == null){
                        this.actionNameField.setInvalid(true);
                        showErrorMessage("关系类型 "+kindName+" 中未注册自定义动作 "+actionName);
                    }else{
                        boolean updateResult1 = targetAction1.updateActionDesc(actionDesc);
                        boolean updateResult2 = targetAction1.updateActionImplementationClass(actionFullClassName);
                        boolean updateResult3 = true;
                        if(this.attributeKindFilterSelect.getValue() != null){
                            AttributesViewKindMetaInfo attributesViewKindMetaInfo = this.attributeKindFilterSelect.getValue();
                            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindMetaInfo.getKindUID());
                            try {
                                updateResult3 = targetAction1.setReferencedAttributesViewKind(targetAttributesViewKind);
                            } catch (CoreRealmServiceRuntimeException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            targetAction1.clearReferencedAttributesViewKind();
                        }
                        if(updateResult1 & updateResult2 & updateResult3){
                            CommonUIOperationUtil.showPopupNotification("自定义动作 "+actionName+" 信息更新成功", NotificationVariant.LUMO_SUCCESS);
                            if(this.parentKindActionsDateView != null){
                                this.parentKindActionsDateView.refreshKindActionsInfo();
                            }
                            if(this.containerDialog != null){
                                this.containerDialog.close();
                            }
                        }else{
                            CommonUIOperationUtil.showPopupNotification("自定义动作信息更新错误",NotificationVariant.LUMO_ERROR);
                        }
                    }
                    break;
            }
            coreRealm.closeGlobalSession();
        }else{
            showErrorMessage("请输入全部必要信息");
            CommonUIOperationUtil.showPopupNotification("自定义动作信息输入错误",NotificationVariant.LUMO_ERROR);
        }
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void hideErrorMessage(){
        this.errorMessage.setVisible(false);
    }

    public void setParentKindActionsDateView(KindActionsDateView parentKindActionsDateView) {
        this.parentKindActionsDateView = parentKindActionsDateView;
    }

    private Renderer<AttributesViewKindMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-xl); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m);\">[${item.attributeKindDataType}]</span>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m); color: var(--lumo-secondary-text-color);\"> ${item.attributeKindUID}<span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeKindDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");
        return LitRenderer.<AttributesViewKindMetaInfo>of(tpl.toString())
                .withProperty("attributeKindName", AttributesViewKindMetaInfo::getKindName)
                .withProperty("attributeKindDesc", AttributesViewKindMetaInfo::getKindDesc)
                .withProperty("attributeKindDataType",AttributesViewKindMetaInfo::getViewKindDataForm)
                .withProperty("attributeKindUID",AttributesViewKindMetaInfo::getKindUID);
    }
}
