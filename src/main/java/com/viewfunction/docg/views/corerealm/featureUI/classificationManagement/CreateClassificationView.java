package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
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
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ClassificationCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class CreateClassificationView extends VerticalLayout {
    private Dialog containerDialog;
    private H6 errorMessage;
    private TextField classificationNameField;
    private TextField classificationDescField;
    private ComboBox<ClassificationMetaInfo> parentClassificationSelector;
    private List<ClassificationMetaInfo> classificationsMetaInfoList;

    public CreateClassificationView(){
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("分类信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.classificationNameField = new TextField("分类名称 - Classification Name");
        this.classificationNameField.setWidthFull();
        this.classificationNameField.setRequired(true);
        this.classificationNameField.setRequiredIndicatorVisible(true);
        this.classificationNameField.setTitle("请输入分类名称");
        add(this.classificationNameField);

        this.classificationDescField = new TextField("分类描述 - Classification Description");
        this.classificationDescField.setWidthFull();
        this.classificationDescField.setRequired(true);
        this.classificationDescField.setRequiredIndicatorVisible(true);
        this.classificationDescField.setTitle("请输入分类描述");
        add(classificationDescField);

        this.parentClassificationSelector = new ComboBox("父级分类 - Parent Classification");
        this.parentClassificationSelector.setRequired(true);
        this.parentClassificationSelector.setWidthFull();
        this.parentClassificationSelector.setPageSize(30);
        this.parentClassificationSelector.setPlaceholder("请选择父级分类");
        this.parentClassificationSelector.setItemLabelGenerator(new ItemLabelGenerator<ClassificationMetaInfo>() {
            @Override
            public String apply(ClassificationMetaInfo classificationMetaInfo) {

                String itemLabelValue = classificationMetaInfo.getClassificationName()+ " ("+
                        classificationMetaInfo.getClassificationDesc()+")";
                return itemLabelValue;
            }
        });
        this.parentClassificationSelector.setRenderer(createRenderer());
        add(parentClassificationSelector);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            this.parentClassificationSelector.setItems(classificationsMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建分类",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateNewClassification();
            }
        });
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setParentClassification(String parentClassificationName){
        for(ClassificationMetaInfo currentClassificationMetaInfo : classificationsMetaInfoList){
            if(parentClassificationName.equals(currentClassificationMetaInfo.getClassificationName())){
                this.parentClassificationSelector.setValue(currentClassificationMetaInfo);
                this.parentClassificationSelector.setEnabled(false);
            }
        }
    }

    private void doCreateNewClassification(){
        String classificationName = this.classificationNameField.getValue();
        String classificationDesc = this.classificationDescField.getValue();
        if(classificationName.equals("")||classificationDesc.equals("")){
            showErrorMessage("请输入全部分类定义信息");
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            Classification targetClassification = coreRealm.getClassification(classificationName);
            if(targetClassification != null){
                CommonUIOperationUtil.showPopupNotification("分类 "+classificationName+"["+classificationDesc+"]"+" 已经存在", NotificationVariant.LUMO_WARNING);
            }else{
                ClassificationMetaInfo parentClassificationMetaInfo = this.parentClassificationSelector.getValue();
                doCreateClassification(classificationName,classificationDesc,parentClassificationMetaInfo);
            }
        }
    }

    private void doCreateClassification(String classificationName,String classificationDesc,ClassificationMetaInfo parentClassificationMetaInfo){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification classification;
        if(parentClassificationMetaInfo != null){
            try {
                classification = coreRealm.createClassification(classificationName,classificationDesc,parentClassificationMetaInfo.getClassificationName());
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }else{
            classification = coreRealm.createClassification(classificationName,classificationDesc);
        }
        if(classification != null){
            CommonUIOperationUtil.showPopupNotification("分类 "+classification.getClassificationName()+"["+classification.getClassificationDesc()+"]"+" 创建成功", NotificationVariant.LUMO_SUCCESS);

            ClassificationCreatedEvent ClassificationCreatedEvent= new ClassificationCreatedEvent();

            ClassificationCreatedEvent.setClassificationName(classification.getClassificationName());
            ClassificationCreatedEvent.setClassificationDesc(classification.getClassificationDesc());
            String parentClassificationName = classification.getParentClassification() != null ?classification.getParentClassification().getClassificationName() : null;
            ClassificationCreatedEvent.setParentClassificationName(parentClassificationName);

            LocalDateTime localCreateDateTime = LocalDateTime.ofInstant(classification.getCreateDateTime().toInstant(), ZoneId.systemDefault());
            ZonedDateTime zonedCreateDateTime = ZonedDateTime.of(localCreateDateTime, ZoneId.systemDefault());
            ClassificationCreatedEvent.setCreateDate(zonedCreateDateTime);

            LocalDateTime localLastModifyDateTime = LocalDateTime.ofInstant(classification.getLastModifyDateTime().toInstant(), ZoneId.systemDefault());
            ZonedDateTime zonedLastModifyDateTime = ZonedDateTime.of(localLastModifyDateTime, ZoneId.systemDefault());
            ClassificationCreatedEvent.setLastModifyDate(zonedLastModifyDateTime);

            ClassificationCreatedEvent.setCreatorId(classification.getCreatorId());
            ClassificationCreatedEvent.setDataOrigin(classification.getDataOrigin());
            ClassificationCreatedEvent.setRootClassification(true);
            ClassificationCreatedEvent.setChildClassificationCount(0);
            ResourceHolder.getApplicationBlackboard().fire(ClassificationCreatedEvent);

            if(this.containerDialog != null){
                this.containerDialog.close();
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("分类 "+classification.getClassificationName()+"["+classification.getClassificationDesc()+"]"+" 创建失败", NotificationVariant.LUMO_ERROR);
        }
    }

    private Renderer<ClassificationMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-xl); color: var(--lumo-primary-text-color);\">${item.classificationName}</span>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m);\">[${item.childClassificationCount}]</span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.classificationDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");
        return LitRenderer.<ClassificationMetaInfo>of(tpl.toString())
                .withProperty("classificationName", ClassificationMetaInfo::getClassificationName)
                .withProperty("classificationDesc", ClassificationMetaInfo::getClassificationDesc)
                .withProperty("childClassificationCount",ClassificationMetaInfo::getChildClassificationCount);
    }
}
