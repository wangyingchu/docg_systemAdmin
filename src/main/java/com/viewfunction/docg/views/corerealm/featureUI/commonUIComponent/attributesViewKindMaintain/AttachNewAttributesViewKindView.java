package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributesViewKindMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributesViewKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.AttributeKindAttachedToAttributesViewKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindAttachedToConceptionKindEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class AttachNewAttributesViewKindView extends VerticalLayout {
    public enum RelatedKindType { ConceptionKind, AttributeKind }
    private String attributeKindUID;
    private String conceptionKindName;
    private Dialog containerDialog;
    private RelatedKindType relatedKindType;
    private ComboBox<AttributesViewKindMetaInfo> attributeKindFilterSelect;
    private NativeLabel errorMessage;

    public AttachNewAttributesViewKindView(RelatedKindType relatedKindType){
        this.relatedKindType = relatedKindType;

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("新附加属性类型信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        attributeKindFilterSelect = new ComboBox();
        attributeKindFilterSelect.setPageSize(30);
        attributeKindFilterSelect.setPlaceholder("选择要附加的属性类型");
        attributeKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        attributeKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<AttributesViewKindMetaInfo>() {
            @Override
            public String apply(AttributesViewKindMetaInfo attributeKindMetaInfo) {

                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+") - "+attributeKindMetaInfo.getKindUID();
                return itemLabelValue;
            }
        });
        attributeKindFilterSelect.setRenderer(createRenderer());
        add(attributeKindFilterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定附加属性类型",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

                errorMessage.setVisible(false);
                if(attributeKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择属性类型");
                    errorMessage.setVisible(true);
                }else{
                    doAttachAttributesViewKind(attributeKindFilterSelect.getValue());
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<AttributesViewKindMetaInfo> runtimeAttributesViewKindMetaInfoList = coreRealm.getAttributesViewKindsMetaInfo();
            attributeKindFilterSelect.setItems(runtimeAttributesViewKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
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

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public String getAttributeKindUID() {
        return attributeKindUID;
    }

    public void setAttributeKindUID(String attributeKindUID) {
        this.attributeKindUID = attributeKindUID;
    }

    public String getConceptionKindName() {
        return conceptionKindName;
    }

    public void setConceptionKindName(String conceptionKindName) {
        this.conceptionKindName = conceptionKindName;
    }

    private void doAttachAttributesViewKind(AttributesViewKindMetaInfo attributesViewKindMetaInfo){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try {
            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindMetaInfo.getKindUID());
            switch (this.relatedKindType){
                case AttributeKind :
                    AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
                    List<AttributeKind> containsAttributeKindsList = targetAttributesViewKind.getContainsAttributeKinds();
                    if(containsAttributeKindsList != null){
                        for(AttributeKind currentAttributeKind : containsAttributeKindsList){
                            if(currentAttributeKind.getAttributeKindUID().equals(this.attributeKindUID)){
                                errorMessage.setText("当前选择的属性视图类型中已经包含 UID 为 "+this.attributeKindUID+" 的属性类型");
                                errorMessage.setVisible(true);
                                return;
                            }
                        }
                    }
                    boolean attachResult = targetAttributesViewKind.attachAttributeKind(this.attributeKindUID);
                    if(attachResult){
                        CommonUIOperationUtil.showPopupNotification("向属性视图类型 "+attributesViewKindMetaInfo.getKindUID()+ " 附加属性类型 "+ targetAttributeKind.getAttributeKindName() +" : "+targetAttributeKind.getAttributeKindUID() +" 成功", NotificationVariant.LUMO_SUCCESS);
                        if(containerDialog != null){
                            containerDialog.close();
                        }
                        AttributeKindAttachedToAttributesViewKindEvent attributeKindAttachedToAttributesViewKindEvent = new AttributeKindAttachedToAttributesViewKindEvent();
                        attributeKindAttachedToAttributesViewKindEvent.setAttributeKindUID(this.attributeKindUID);
                        attributeKindAttachedToAttributesViewKindEvent.setAttributesViewKindUID(attributesViewKindMetaInfo.getKindUID());
                        attributeKindAttachedToAttributesViewKindEvent.setAttributesViewKind(targetAttributesViewKind);
                        attributeKindAttachedToAttributesViewKindEvent.setAttributeKind(targetAttributeKind);
                        ResourceHolder.getApplicationBlackboard().fire(attributeKindAttachedToAttributesViewKindEvent);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("向属性视图类型 "+attributesViewKindMetaInfo.getKindUID()+ " 附加属性类型 "+ targetAttributeKind.getAttributeKindName() +" : "+targetAttributeKind.getAttributeKindUID() +" 失败", NotificationVariant.LUMO_ERROR);
                    }
                    break;
                case ConceptionKind :
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
                    List<AttributesViewKind> containsAttributesViewKindsList = targetConceptionKind.getContainsAttributesViewKinds();
                    if(containsAttributesViewKindsList != null){
                        for(AttributesViewKind currentAttributesViewKind:containsAttributesViewKindsList){
                            if(currentAttributesViewKind.getAttributesViewKindUID().equals(attributesViewKindMetaInfo.getKindUID())){
                                errorMessage.setText("当前选择的属性视图类型已经包含在概念类型 "+this.conceptionKindName+" 中");
                                errorMessage.setVisible(true);
                                return;
                            }
                        }
                    }
                    boolean attachResult2 = targetConceptionKind.attachAttributesViewKind(targetAttributesViewKind.getAttributesViewKindUID());
                    if(attachResult2){
                        CommonUIOperationUtil.showPopupNotification("将属性视图类型 "+attributesViewKindMetaInfo.getKindUID()+ " 附加到概念类型 "+ targetConceptionKind.getConceptionKindName() +" : "+targetConceptionKind.getConceptionKindDesc() +" 成功", NotificationVariant.LUMO_SUCCESS);
                        if(containerDialog != null){
                            containerDialog.close();
                        }
                        AttributesViewKindAttachedToConceptionKindEvent attributesViewKindAttachedToConceptionKindEvent = new AttributesViewKindAttachedToConceptionKindEvent();
                        attributesViewKindAttachedToConceptionKindEvent.setConceptionKindName(conceptionKindName);
                        attributesViewKindAttachedToConceptionKindEvent.setAttributesViewKindUID(targetAttributesViewKind.getAttributesViewKindUID());
                        attributesViewKindAttachedToConceptionKindEvent.setConceptionKind(targetConceptionKind);
                        attributesViewKindAttachedToConceptionKindEvent.setAttributesViewKind(targetAttributesViewKind);
                        ResourceHolder.getApplicationBlackboard().fire(attributesViewKindAttachedToConceptionKindEvent);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("将属性视图类型 "+targetAttributesViewKind.getAttributesViewKindUID()+ " 附加到概念类型 "+ targetConceptionKind.getConceptionKindName() +" : "+targetConceptionKind.getConceptionKindDesc() +" 失败", NotificationVariant.LUMO_ERROR);
                    }
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
    }
}
