package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmFunctionNotSupportedException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.RelationAttachKindCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain.RelationAttachKindsConfigurationView.RelatedKindType;
import java.util.List;

public class CreateRelationAttachKindView extends VerticalLayout {

    private Dialog containerDialog;
    private NativeLabel errorMessage;
    private TextField relationAttachKindNameField;
    private TextField relationAttachKindDescField;
    private ComboBox<KindMetaInfo> sourceConceptionKindFilterSelect;
    private ComboBox<KindMetaInfo> relationKindFilterSelect;
    private ComboBox<KindMetaInfo> targetConceptionKindFilterSelect;
    private Checkbox allowRepeatableRelationKindCheckbox;
    private RelatedKindType relatedKindType;
    private String relatedKindName;

    public CreateRelationAttachKindView(RelatedKindType relatedKindType,String relatedKindName){
        this.relatedKindType = relatedKindType;
        this.relatedKindName = relatedKindName;

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("新建关系附着规则类型信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        this.relationAttachKindNameField = new TextField("关系附着规则类型名称 - RelationAttachKind Name");
        this.relationAttachKindNameField.setWidthFull();
        this.relationAttachKindNameField.setRequired(true);
        this.relationAttachKindNameField.setRequiredIndicatorVisible(true);
        this.relationAttachKindNameField.setTitle("请输入关系附着规则类型名称");
        add(relationAttachKindNameField);

        this.relationAttachKindDescField = new TextField("关系附着规则类型描述 - RelationAttachKind Description");
        this.relationAttachKindDescField.setWidthFull();
        this.relationAttachKindDescField.setRequired(true);
        this.relationAttachKindDescField.setRequiredIndicatorVisible(true);
        this.relationAttachKindDescField.setTitle("请输入关系附着规则类型描述");
        add(relationAttachKindDescField);

        this.sourceConceptionKindFilterSelect = new ComboBox("源概念类型名称 - Source ConceptionKind Name");
        this.sourceConceptionKindFilterSelect.setPageSize(30);
        this.sourceConceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.sourceConceptionKindFilterSelect.setRequiredIndicatorVisible(true);
        this.sourceConceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        this.sourceConceptionKindFilterSelect.setRenderer(createRenderer());
        add(this.sourceConceptionKindFilterSelect);

        this.relationKindFilterSelect = new ComboBox("关系类型名称 - RelationKind Name");
        this.relationKindFilterSelect.setPageSize(30);
        this.relationKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.relationKindFilterSelect.setRequiredIndicatorVisible(true);
        this.relationKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        this.relationKindFilterSelect.setRenderer(createRenderer());
        add(this.relationKindFilterSelect);

        this.targetConceptionKindFilterSelect = new ComboBox("目标概念类型名称 - Target ConceptionKind Name");
        this.targetConceptionKindFilterSelect.setPageSize(30);
        this.targetConceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.targetConceptionKindFilterSelect.setRequiredIndicatorVisible(true);
        this.targetConceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        this.targetConceptionKindFilterSelect.setRenderer(createRenderer());
        add(this.targetConceptionKindFilterSelect);

        this.allowRepeatableRelationKindCheckbox = new Checkbox("允许重复创建相同类型的关系实例");
        this.allowRepeatableRelationKindCheckbox.getStyle().set("font-size","0.75rem").set("color","var(--lumo-contrast-80pct)");
        add(this.allowRepeatableRelationKindCheckbox);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定创建关系附着规则类型",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if(relationAttachKindNameField.getValue().equals("")){
                    errorMessage.setText("请输入关系附着规则类型名称");
                    errorMessage.setVisible(true);
                }else if(relationAttachKindDescField.getValue().equals("")){
                    errorMessage.setText("请输入关系附着规则类型描述");
                    errorMessage.setVisible(true);
                }else if(sourceConceptionKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择源概念类型名称");
                    errorMessage.setVisible(true);
                }else if(relationKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择关系类型名称");
                    errorMessage.setVisible(true);
                }else if(targetConceptionKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择目标概念类型名称");
                    errorMessage.setVisible(true);
                }else{
                    doCreateRelationAttachKind();
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            coreRealm.openGlobalSession();
            List<KindMetaInfo> runtimeConceptionKindMetaInfoList = coreRealm.getConceptionKindsMetaInfo();
            this.sourceConceptionKindFilterSelect.setItems(runtimeConceptionKindMetaInfoList);
            this.targetConceptionKindFilterSelect.setItems(runtimeConceptionKindMetaInfoList);
            List<KindMetaInfo> runtimeRelationKindMetaInfoList = coreRealm.getRelationKindsMetaInfo();
            this.relationKindFilterSelect.setItems(runtimeRelationKindMetaInfoList);

            if(this.relatedKindType.equals(RelatedKindType.RelationKind)){
                for(KindMetaInfo currentKindMetaInfo:runtimeRelationKindMetaInfoList){
                    if(currentKindMetaInfo.getKindName().equals(this.relatedKindName)){
                        this.relationKindFilterSelect.setValue(currentKindMetaInfo);
                        break;
                    }
                }
                this.relationKindFilterSelect.setReadOnly(true);
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        } finally {
            coreRealm.closeGlobalSession();
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private Renderer<KindMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-xl); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeKindDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindMetaInfo>of(tpl.toString())
                .withProperty("attributeKindName", KindMetaInfo::getKindName)
                .withProperty("attributeKindDesc", KindMetaInfo::getKindDesc);
    }

    private void doCreateRelationAttachKind(){
        boolean isInValidInput = false;
        errorMessage.setVisible(false);
        switch(this.relatedKindType){
            case RelationKind :
                if(!this.relationKindFilterSelect.getValue().getKindName().equals(this.relatedKindName)){
                    isInValidInput = true;
                    errorMessage.setText("关系类型必须为: "+this.relatedKindName);
                    errorMessage.setVisible(true);
                }
                break;
            case ConceptionKind :
                if(!this.sourceConceptionKindFilterSelect.getValue().getKindName().equals(this.relatedKindName) &
                        !this.targetConceptionKindFilterSelect.getValue().getKindName().equals(this.relatedKindName)){
                    isInValidInput = true;
                    errorMessage.setText("源概念类型与目标概念类型至少一项必须为: "+this.relatedKindName);
                    errorMessage.setVisible(true);
                }
        }
        if(!isInValidInput){
            String relationAttachKindName = relationAttachKindNameField.getValue();
            String relationAttachKindDesc = relationAttachKindDescField.getValue();
            String sourceConceptionKind  = sourceConceptionKindFilterSelect.getValue().getKindName();
            String targetConceptionName = targetConceptionKindFilterSelect.getValue().getKindName();
            String relationKind = relationKindFilterSelect.getValue().getKindName();
            boolean allowRepeatRelationKind = allowRepeatableRelationKindCheckbox.getValue();

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            try {
                RelationAttachKind targetRelationAttachKind = coreRealm.createRelationAttachKind(relationAttachKindName,
                        relationAttachKindDesc,sourceConceptionKind,targetConceptionName,relationKind,allowRepeatRelationKind);
                if(targetRelationAttachKind != null){
                    CommonUIOperationUtil.showPopupNotification("新建关系附着规则类型 "+relationAttachKindName+ " : "+ relationAttachKindDesc +" 成功", NotificationVariant.LUMO_SUCCESS);
                    if(containerDialog != null){
                        containerDialog.close();
                    }
                    RelationAttachKindCreatedEvent relationAttachKindCreatedEvent = new RelationAttachKindCreatedEvent();
                    relationAttachKindCreatedEvent.setRelationAttachKind(targetRelationAttachKind);
                    relationAttachKindCreatedEvent.setRelationAttachKindName(targetRelationAttachKind.getRelationAttachKindName());
                    relationAttachKindCreatedEvent.setRelationAttachKindUID(targetRelationAttachKind.getRelationAttachKindUID());
                    ResourceHolder.getApplicationBlackboard().fire(relationAttachKindCreatedEvent);
                }else{
                    CommonUIOperationUtil.showPopupNotification("新建关系附着规则类型 "+relationAttachKindName+ " : "+ relationAttachKindDesc +" 失败", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmFunctionNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
