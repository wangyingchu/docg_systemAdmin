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

import com.vaadin.flow.data.provider.ListDataProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RemoveEntitiesAttributeView extends VerticalLayout {

    public enum KindType {ConceptionKind,RelationKind,Classification}
    private Dialog containerDialog;
    private List<String> entityUIDsList;
    private String kindName;
    private NativeLabel errorMessage;
    private RemoveEntitiesAttributeView.KindType entityKindType = RemoveEntitiesAttributeView.KindType.ConceptionKind;
    private FootprintMessageBar entityInfoFootprintMessageBar;
    private ComboBox<String> targetAttributeFilterSelect;

    public RemoveEntitiesAttributeView(String kindName, List<String> entityUIDsList, RemoveEntitiesAttributeView.KindType entityKindType){
        this.setMargin(false);
        this.setSpacing(false);

        this.kindName = kindName;
        this.entityKindType = entityKindType;

        if(entityKindType != null){
            this.entityKindType = entityKindType;
        }
        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (this.entityKindType){
            case ConceptionKind ->  kindIcon = VaadinIcon.CUBE.create();
            case RelationKind -> kindIcon = VaadinIcon.CONNECT_O.create();
            case Classification -> kindIcon = VaadinIcon.TAGS.create();
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        String kindNameStr = kindName != null ? kindName : "[...]";
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, kindNameStr));

        entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("待删除实体属性信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);

        errorMessage = new NativeLabel("请选择属性名称");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        targetAttributeFilterSelect = new ComboBox();
        targetAttributeFilterSelect.setPageSize(30);
        targetAttributeFilterSelect.setPlaceholder("选择或输入待删数属性名称");
        targetAttributeFilterSelect.setMinWidth(340, Unit.PIXELS);
        targetAttributeFilterSelect.setAllowCustomValue(true);

        targetAttributeFilterSelect.addCustomValueSetListener(
                event -> targetAttributeFilterSelect.setValue(event.getDetail())
        );

        HorizontalLayout attributeValueFieldContainerLayout = new HorizontalLayout();
        add(attributeValueFieldContainerLayout);
        attributeValueFieldContainerLayout.add(targetAttributeFilterSelect);

        add(attributeValueFieldContainerLayout);
        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(80,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        attributeValueFieldContainerLayout.add(confirmButton);

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                String attributeName = targetAttributeFilterSelect.getValue();
                if(attributeName == null){
                    errorMessage.setVisible(true);
                }else{
                    errorMessage.setVisible(false);
                    deleteResultEntitiesAttributes(attributeName);
                }
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadConceptionKindAttributesComboBox();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void loadConceptionKindAttributesComboBox(){
        if(entityKindType.equals(RemoveEntitiesAttributeView.KindType.ConceptionKind)){
            int entityAttributesDistributionStatisticSampleRatio = 100000;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
            List<String> attributesNameList = new ArrayList<>();
            List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList =
                    targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
            for(KindEntityAttributeRuntimeStatistics currentKindEntityAttributeRuntimeStatistics:kindEntityAttributeRuntimeStatisticsList){
                attributesNameList.add(currentKindEntityAttributeRuntimeStatistics.getAttributeName());
            }
            coreRealm.closeGlobalSession();
            targetAttributeFilterSelect.setItems(attributesNameList);
        }
    }

    private void deleteResultEntitiesAttributes(String attributeName){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除概念实体属性",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认是否删除查询结果概念实体中的属性 "+attributeName,actionButtonList,500,180);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDeleteConceptionEntitiesAttributes(confirmWindow,attributeName);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDeleteConceptionEntitiesAttributes(ConfirmWindow confirmWindow,String attributeName){
        /*
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
        try {
            EntitiesOperationStatistics entitiesOperationStatistics = crossKindDataOperator.deleteConceptionEntitiesByUIDs(this.queryResultEntityUIDList);
            if(entitiesOperationStatistics != null){
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                Div text = new Div(new Text("概念实体删除操作完成"));
                Button closeButton = new Button(new Icon("lumo", "cross"));
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeButton.addClickListener(event -> {
                    notification.close();
                });
                HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                layout.setWidth(100, Unit.PERCENTAGE);
                layout.setFlexGrow(1,text);
                notification.add(layout);

                startTimeDisplayItem.updateDisplayValue("-");
                finishTimeDisplayItem.updateDisplayValue("-");
                dataCountDisplayItem.updateDisplayValue("-");

                ListDataProvider dataProvider = (ListDataProvider)queryResultGrid.getDataProvider();
                if(this.queryResultEntityUIDList.size() == entitiesOperationStatistics.getSuccessItemsCount()){
                    dataProvider.getItems().clear();
                    queryResultOperationMenuBar.setEnabled(false);
                }else {
                    List<ConceptionEntity> conceptionEntityList = crossKindDataOperator.getConceptionEntitiesByUIDs(this.queryResultEntityUIDList);
                    dataCountDisplayItem.updateDisplayValue(""+ numberFormat.format(conceptionEntityList.size()));
                    List<String> existingEntitiesUIDList = new ArrayList<>();
                    conceptionEntityList.forEach(conceptionEntity -> {
                        String conceptionEntityUID = conceptionEntity.getConceptionEntityUID();
                        existingEntitiesUIDList.add(conceptionEntityUID);
                    });

                    Collection<ConceptionEntityValue> conceptionEntityValueList = dataProvider.getItems();
                    for(ConceptionEntityValue currentConceptionEntityValue:conceptionEntityValueList){
                        if(!existingEntitiesUIDList.contains(currentConceptionEntityValue.getConceptionEntityUID())){
                            dataProvider.getItems().remove(currentConceptionEntityValue);
                        }
                    }
                }
                dataProvider.refreshAll();

                VerticalLayout notificationMessageContainer = new VerticalLayout();
                notificationMessageContainer.add(new Div(new Text("查询返回实体数: "+entitiesOperationStatistics.getSuccessItemsCount())));
                notificationMessageContainer.add(new Div(new Text("操作开始时间: "+entitiesOperationStatistics.getStartTime())));
                notificationMessageContainer.add(new Div(new Text("操作结束时间: "+entitiesOperationStatistics.getFinishTime())));
                notification.add(notificationMessageContainer);
                notification.setDuration(3000);
                notification.open();

                confirmWindow.closeConfirmWindow();
            }else{
                CommonUIOperationUtil.showPopupNotification("概念实体删除操作失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        */
    }
}
