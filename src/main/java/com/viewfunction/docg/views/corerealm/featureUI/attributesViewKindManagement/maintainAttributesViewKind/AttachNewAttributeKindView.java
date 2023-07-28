package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.AttributeKindAttachedToAttributesViewKindEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class AttachNewAttributeKindView extends VerticalLayout {
    private String attributesViewKindUID;
    private Dialog containerDialog;
    private ComboBox<AttributeKindMetaInfo> attributeKindFilterSelect;
    private NativeLabel errorMessage;

    public AttachNewAttributeKindView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;

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
        attributeKindFilterSelect.setWidth(100,Unit.PERCENTAGE);
        attributeKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<AttributeKindMetaInfo>() {
            @Override
            public String apply(AttributeKindMetaInfo attributeKindMetaInfo) {

                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getAttributeDataType()+") - "+attributeKindMetaInfo.getKindUID();
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
                    doAttachAttributeType(attributeKindFilterSelect.getValue());
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<AttributeKindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getAttributeKindsMetaInfo();
            attributeKindFilterSelect.setItems(runtimeAttributeKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private Renderer<AttributeKindMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-xl); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m);\">[${item.attributeKindDataType}]</span>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m); color: var(--lumo-secondary-text-color);\"> ${item.attributeKindUID}<span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeKindDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<AttributeKindMetaInfo>of(tpl.toString())
                .withProperty("attributeKindName", AttributeKindMetaInfo::getKindName)
                .withProperty("attributeKindDesc", AttributeKindMetaInfo::getKindDesc)
                .withProperty("attributeKindDataType",AttributeKindMetaInfo::getAttributeDataType)
                .withProperty("attributeKindUID",AttributeKindMetaInfo::getKindUID);
    }

    private void doAttachAttributeType(AttributeKindMetaInfo attributeKindMetaInfo){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try {
            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
            List<AttributeKind> containsAttributeKindsList = targetAttributesViewKind.getContainsAttributeKinds();
            if(containsAttributeKindsList != null){
                for(AttributeKind currentAttributeKind : containsAttributeKindsList){
                    if(currentAttributeKind.getAttributeKindUID().equals(attributeKindMetaInfo.getKindUID())){
                        errorMessage.setText("当前属性视图类型中已经包含 UID 为 "+attributeKindMetaInfo.getKindUID()+" 的属性类型");
                        errorMessage.setVisible(true);
                        return;
                    }
                }
            }

            boolean attachResult = targetAttributesViewKind.attachAttributeKind(attributeKindMetaInfo.getKindUID());
            if(attachResult){
                CommonUIOperationUtil.showPopupNotification("向属性视图类型 "+this.attributesViewKindUID+ " 附加属性类型 "+ attributeKindMetaInfo.getKindName() +" : "+attributeKindMetaInfo.getKindUID() +" 成功", NotificationVariant.LUMO_SUCCESS);
                if(containerDialog != null){
                    containerDialog.close();
                }
                AttributeKind targetAttributeKind = coreRealm.getAttributeKind(attributeKindMetaInfo.getKindUID());
                AttributeKindAttachedToAttributesViewKindEvent attributeKindAttachedToAttributesViewKindEvent = new AttributeKindAttachedToAttributesViewKindEvent();
                attributeKindAttachedToAttributesViewKindEvent.setAttributeKindMetaInfo(attributeKindMetaInfo);
                attributeKindAttachedToAttributesViewKindEvent.setAttributeKindUID(attributeKindMetaInfo.getKindUID());
                attributeKindAttachedToAttributesViewKindEvent.setAttributesViewKindUID(this.attributesViewKindUID);
                attributeKindAttachedToAttributesViewKindEvent.setAttributeKind(targetAttributeKind);
                ResourceHolder.getApplicationBlackboard().fire(attributeKindAttachedToAttributesViewKindEvent);
            }else{
                CommonUIOperationUtil.showPopupNotification("向属性视图类型 "+this.attributesViewKindUID+ " 附加属性类型 "+ attributeKindMetaInfo.getKindName() +" : "+attributeKindMetaInfo.getAttributeDataType() +" 失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
    }
}
