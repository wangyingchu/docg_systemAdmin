package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JoinNewConceptionKindsView extends VerticalLayout {
    private Dialog containerDialog;
    private MultiSelectComboBox<KindMetaInfo> conceptionKindFilterSelect;
    private NativeLabel errorMessage;
    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionEntityMetaInfoView callerConceptionEntityMetaInfoView;

    public JoinNewConceptionKindsView(String conceptionKind,String conceptionEntityUID) {
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top", "3px").set("padding-bottom", "3px");

        NativeLabel viewTitle = new NativeLabel("待加入概念类型信息:");
        viewTitle.getStyle().set("color", "var(--lumo-contrast-50pct)").set("font-size", "0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)").set("font-size", "0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        conceptionKindFilterSelect = new MultiSelectComboBox();
        conceptionKindFilterSelect.setPageSize(30);
        conceptionKindFilterSelect.setPlaceholder("选择要加入的概念类型");
        conceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        conceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {

                String itemLabelValue = attributeKindMetaInfo.getKindName() + " (" +
                        attributeKindMetaInfo.getKindDesc() + ")";
                return itemLabelValue;
            }
        });
        conceptionKindFilterSelect.setRenderer(createRenderer());
        add(conceptionKindFilterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定加入概念类型", new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if (conceptionKindFilterSelect.getValue().isEmpty()) {
                    errorMessage.setText("请选择至少一项概念类型");
                    errorMessage.setVisible(true);
                } else {
                    joinConceptionKinds(conceptionKindFilterSelect.getValue());
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END, confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            ConceptionKind currentConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            ConceptionEntity currentEntity = currentConceptionKind.getEntityByUID(this.conceptionEntityUID);
            List<String> alreadyJoinedKindNameList = currentEntity.getAllConceptionKindNames();
            List<KindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getConceptionKindsMetaInfo();
            List<KindMetaInfo> joinTargetKindMetaInfoList = new ArrayList<>();
            for(KindMetaInfo currentKindMetaInfo:runtimeAttributeKindMetaInfoList){
                if(!alreadyJoinedKindNameList.contains(currentKindMetaInfo.getKindName())){
                    joinTargetKindMetaInfoList.add(currentKindMetaInfo);
                }
            }
            conceptionKindFilterSelect.setItems(joinTargetKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
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

    private void joinConceptionKinds(Set<KindMetaInfo> conceptionKindsInfoSet){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认加入概念类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                "请确认执行将概念实体 "+ this.conceptionKind+" - ["+this.conceptionEntityUID+"] 加入 "+conceptionKindsInfoSet.size()+" 项新概念类型的操作",actionButtonList,600,175);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doJoinConceptionKinds(conceptionKindsInfoSet,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doJoinConceptionKinds(Set<KindMetaInfo> conceptionKindsInfoSet, ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            ConceptionKind currentConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            ConceptionEntity currentEntity = currentConceptionKind.getEntityByUID(this.conceptionEntityUID);
            String[] newConceptionKindNames = new String[conceptionKindsInfoSet.size()];
            int arrayIdx = 0;
            for(KindMetaInfo currentKindMetaInfo:conceptionKindsInfoSet){
                newConceptionKindNames[arrayIdx] = currentKindMetaInfo.getKindName();
                arrayIdx++;
            }
            boolean joinResult = currentEntity.joinConceptionKinds(newConceptionKindNames);
            if(joinResult){
                CommonUIOperationUtil.showPopupNotification("将概念实体 "+ this.conceptionKind+" - ["+this.conceptionEntityUID+"] "+"加入新概念类型操作成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                if(this.containerDialog != null){
                    this.containerDialog.close();
                }
                if(this.callerConceptionEntityMetaInfoView != null){
                    this.callerConceptionEntityMetaInfoView.refreshEntityMetaInfo();
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("将概念实体 "+ this.conceptionKind+" - ["+this.conceptionEntityUID+"] "+"加入新概念类型操作失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCallerConceptionEntityMetaInfoView(ConceptionEntityMetaInfoView callerConceptionEntityMetaInfoView) {
        this.callerConceptionEntityMetaInfoView = callerConceptionEntityMetaInfoView;
    }
}
