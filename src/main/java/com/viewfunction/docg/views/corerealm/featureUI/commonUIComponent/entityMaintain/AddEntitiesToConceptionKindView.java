package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddEntitiesToConceptionKindView extends VerticalLayout {

    public interface EntitiesAddedToConceptionKindsListener{
        public void entitiesAddedToConceptionsAction(String currentConceptionKind,Set<String> ignoreConceptionKinds, EntitiesOperationResult entitiesOperationResult);
    }

    public enum JoinType {ADD,MOVE}
    private JoinType joinType;
    private Dialog containerDialog;
    private ComboBox<KindMetaInfo> conceptionKindFilterSelect;
    private NativeLabel errorMessage;
    private Set<String> ignoreConceptionKinds;
    private List<String> entityUIDsList;
    private String currentConceptionKind;
    private EntitiesAddedToConceptionKindsListener entitiesAddedToConceptionKindsListener;

    public AddEntitiesToConceptionKindView(JoinType joinType, String currentConceptionKind,Set<String> ignoreConceptionKinds,List<String> entityUIDsList) {
        this.joinType = joinType;
        this.ignoreConceptionKinds = ignoreConceptionKinds;
        this.entityUIDsList = entityUIDsList;
        this.currentConceptionKind = currentConceptionKind;

        this.setWidthFull();
        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("目标概念类型信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        conceptionKindFilterSelect = new ComboBox();
        conceptionKindFilterSelect.setPageSize(30);
        conceptionKindFilterSelect.setPlaceholder("选择要加入的概念类型");
        conceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        conceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {

                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
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

        Button confirmButton = new Button("确定加入概念类型",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if(conceptionKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择概念类型");
                    errorMessage.setVisible(true);
                }else{
                    addToConceptionType(conceptionKindFilterSelect.getValue());
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<KindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getConceptionKindsMetaInfo();
            List<KindMetaInfo> avaliableConceptionKindMetaInfoList = new ArrayList<>();
            runtimeAttributeKindMetaInfoList.forEach(kindMetaInfo -> {
                if(!ignoreConceptionKinds.contains(kindMetaInfo.getKindName())){
                    avaliableConceptionKindMetaInfoList.add(kindMetaInfo);
                }
            });
            conceptionKindFilterSelect.setItems(avaliableConceptionKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
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

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public EntitiesAddedToConceptionKindsListener getEntitiesAddedToConceptionKindsListener() {
        return entitiesAddedToConceptionKindsListener;
    }

    public void setEntitiesAddedToConceptionKindsListener(EntitiesAddedToConceptionKindsListener entitiesAddedToConceptionKindsListener) {
        this.entitiesAddedToConceptionKindsListener = entitiesAddedToConceptionKindsListener;
    }

    private void addToConceptionType(KindMetaInfo targetKindMetaInfo){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认加入概念类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认是否将选定概念实体加入概念类型 "+targetKindMetaInfo.getKindName(),actionButtonList,500,180);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
               doAddToConceptionType(confirmWindow,targetKindMetaInfo.getKindName());
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doAddToConceptionType(ConfirmWindow confirmWindow,String targetConceptionKindName){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
        String[] targetConceptionKindsArray = new String[1];
        targetConceptionKindsArray[0] = targetConceptionKindName;
        try {
            EntitiesOperationResult entitiesOperationResult = crossKindDataOperator.joinEntitiesToConceptionKinds(new HashSet<>(entityUIDsList),targetConceptionKindsArray);

            String messageContent = "";
            String operationType = "";
            switch (this.joinType){
                case MOVE :
                    EntitiesOperationResult entitiesOperationResult2 = crossKindDataOperator.retreatEntitiesFromConceptionKind(new HashSet<>(entityUIDsList),currentConceptionKind);
                    if(getEntitiesAddedToConceptionKindsListener() != null){
                        getEntitiesAddedToConceptionKindsListener().entitiesAddedToConceptionsAction(this.currentConceptionKind,this.ignoreConceptionKinds,entitiesOperationResult2);
                    }
                    messageContent = "概念实体移动到新概念类型操作完成";
                    operationType = "移动";
                break;
                case ADD :
                    messageContent = "概念实体加入新概念类型操作完成";
                    operationType = "加入";
            }

            if(entitiesOperationResult != null){
                confirmWindow.closeConfirmWindow();
                if(this.containerDialog != null){
                    this.containerDialog.close();
                }

                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                Div text = new Div(new Text(messageContent));
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
                notificationMessageContainer.add(new Div(new Text("概念实体"+operationType+"操作成功数: "+entitiesOperationResult.getOperationStatistics().getSuccessItemsCount())));
                notificationMessageContainer.add(new Div(new Text("操作开始时间: "+entitiesOperationResult.getOperationStatistics().getStartTime())));
                notificationMessageContainer.add(new Div(new Text("操作结束时间: "+entitiesOperationResult.getOperationStatistics().getFinishTime())));
                notification.add(notificationMessageContainer);
                notification.setDuration(3000);
                notification.open();
            }

        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }
}
