package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.eventHandling.RelationEntitiesCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ConceptionEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList.ProcessingConceptionEntityListView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.AddEntityAttributeView;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.AttributeEditorItemWidget;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.*;

public class CreateRelationEntityView extends VerticalLayout {

    private ComboBox<KindMetaInfo> relationKindSelect;
    private RadioButtonGroup<String> relationDirectionRadioGroup;
    private ProcessingConceptionEntityListView processingConceptionEntityListView;
    private Checkbox allowDupTypeRelationCheckbox;
    private String conceptionKind;
    private String conceptionEntityUID;
    private VerticalLayout relationEntityAttributesContainer;
    private Map<String,AttributeEditorItemWidget> relationAttributeEditorsMap;
    private Button clearAttributeButton;

    public CreateRelationEntityView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        add(mainContainerLayout);

        VerticalLayout basicInfoContainerLayout = new VerticalLayout();
        basicInfoContainerLayout.setWidth(350,Unit.PIXELS);
        mainContainerLayout.add(basicInfoContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择关系类型");
        basicInfoContainerLayout.add(infoTitle1);

        relationKindSelect = new ComboBox();
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(340,Unit.PIXELS);

        relationKindSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo kindMetaInfo) {
                String itemLabelValue = kindMetaInfo.getKindName()+ " ("+
                        kindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        basicInfoContainerLayout.add(relationKindSelect);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXCHANGE),"选择关系方向");
        basicInfoContainerLayout.add(infoTitle2);

        relationDirectionRadioGroup = new RadioButtonGroup<>();
        relationDirectionRadioGroup.setItems("FROM", "TO", "BOTH");
        relationDirectionRadioGroup.setValue("FROM");
        basicInfoContainerLayout.add(relationDirectionRadioGroup);

        HorizontalLayout addRelationAttributesUIContainerLayout = new HorizontalLayout();
        addRelationAttributesUIContainerLayout.setSpacing(false);
        addRelationAttributesUIContainerLayout.setMargin(false);
        addRelationAttributesUIContainerLayout.setPadding(false);
        basicInfoContainerLayout.add(addRelationAttributesUIContainerLayout);

        addRelationAttributesUIContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定关系属性");
        addRelationAttributesUIContainerLayout.add(infoTitle3);

        Button addAttributeButton = new Button();
        Tooltips.getCurrent().setTooltip(addAttributeButton, "添加关系实体属性");
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addAttributeButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewAttributeUI();
            }
        });
        addRelationAttributesUIContainerLayout.add(addAttributeButton);

        clearAttributeButton = new Button();
        clearAttributeButton.setEnabled(false);
        Tooltips.getCurrent().setTooltip(clearAttributeButton, "清除已设置关系实体属性");
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        clearAttributeButton.setIcon(VaadinIcon.RECYCLE.create());
        clearAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cleanRelationAttributes();
            }
        });
        addRelationAttributesUIContainerLayout.add(clearAttributeButton);

        relationEntityAttributesContainer = new VerticalLayout();
        relationEntityAttributesContainer.setMargin(false);
        relationEntityAttributesContainer.setSpacing(false);
        relationEntityAttributesContainer.setPadding(false);
        relationEntityAttributesContainer.setWidth(100,Unit.PERCENTAGE);

        Scroller relationEntityAttributesScroller = new Scroller(relationEntityAttributesContainer);
        relationEntityAttributesScroller.setHeight(300,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        basicInfoContainerLayout.add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        basicInfoContainerLayout.add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        basicInfoContainerLayout.add(buttonsContainerLayout);

        Button executeCreateRelationButton = new Button("创建实体关联");
        executeCreateRelationButton.setIcon(new Icon(VaadinIcon.LINK));
        executeCreateRelationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeCreateRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                creatConceptionEntityRelations();
            }
        });
        buttonsContainerLayout.add(executeCreateRelationButton);

        allowDupTypeRelationCheckbox = new Checkbox();
        buttonsContainerLayout.add(allowDupTypeRelationCheckbox);
        Label chechboxDescLabel = new Label("允许重复创建同类型关系");
        chechboxDescLabel.addClassNames("text-xs","text-tertiary");
        chechboxDescLabel.setWidth(80,Unit.PIXELS);
        buttonsContainerLayout.add(chechboxDescLabel);

        VerticalLayout targetConceptionEntitiesInfoContainerLayout = new VerticalLayout();
        targetConceptionEntitiesInfoContainerLayout.setSpacing(false);
        mainContainerLayout.add(targetConceptionEntitiesInfoContainerLayout);

        ThirdLevelIconTitle infoTitle4 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBES),"选择目标概念实体");
        targetConceptionEntitiesInfoContainerLayout.add(infoTitle4);

        processingConceptionEntityListView = new ProcessingConceptionEntityListView(500);
        targetConceptionEntitiesInfoContainerLayout.add(processingConceptionEntityListView);

        this.relationAttributeEditorsMap = new HashMap<>();
    }

    private Dialog containerDialog;

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<KindMetaInfo> reltionKindsList = coreRealm.getRelationKindsMetaInfo();
            relationKindSelect.setItems(reltionKindsList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void creatConceptionEntityRelations(){
        KindMetaInfo selectedRelationKind = relationKindSelect.getValue();
        if(selectedRelationKind == null){
            CommonUIOperationUtil.showPopupNotification("请选择关系类型定义", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        Set<ConceptionEntityResourceHolderVO> targetConceptionEntitiesInfoSet = processingConceptionEntityListView.getSelectedConceptionEntitiesInProcessingList();
        if(targetConceptionEntitiesInfoSet.size() == 0){
            CommonUIOperationUtil.showPopupNotification("请选择至少一个关联目标概念实体", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认创建关系实体",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行创建关系实体操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateRelationEntities();
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doCreateRelationEntities(){
        KindMetaInfo selectedRelationKind = relationKindSelect.getValue();
        String relationDirection = relationDirectionRadioGroup.getValue();
        boolean allowDupRelation = allowDupTypeRelationCheckbox.getValue();
        String relationKind = selectedRelationKind.getKindName();
        Set<ConceptionEntityResourceHolderVO> targetConceptionEntitiesInfoSet = processingConceptionEntityListView.getSelectedConceptionEntitiesInProcessingList();

        CoreRealm coreRealm = null;
        try {
            coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            List<String> conceptionEntityUIDList = new ArrayList<>();
            conceptionEntityUIDList.add(this.conceptionEntityUID);
            List<ConceptionEntity> conceptionEntityList = crossKindDataOperator.getConceptionEntitiesByUIDs(conceptionEntityUIDList);
            ConceptionEntity sourceConceptionEntity = conceptionEntityList.get(0);
            int successRelationCount = 0;
            List<RelationEntity> relationEntityInfoList = new ArrayList<>();

            Map<String, Object> relationAttributesMap = null;
            if(relationAttributeEditorsMap.size() > 0){
                relationAttributesMap = new HashMap<>();
                for(String currentAttributeName:relationAttributeEditorsMap.keySet()){
                    AttributeValue currentAttributeValue = relationAttributeEditorsMap.get(currentAttributeName).getAttributeValue();
                    relationAttributesMap.put(currentAttributeValue.getAttributeName(),currentAttributeValue.getAttributeValue());
                }
            }

            for(ConceptionEntityResourceHolderVO currentConceptionEntityResourceHolderVO:targetConceptionEntitiesInfoSet){
                String targetConceptionEntityUID = currentConceptionEntityResourceHolderVO.getConceptionEntityUID();
                if(relationDirection.equals("FROM")){
                    RelationEntity resultRelationEntity = sourceConceptionEntity.attachFromRelation(targetConceptionEntityUID,relationKind,relationAttributesMap,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                }else if(relationDirection.equals("TO")){
                    RelationEntity resultRelationEntity = sourceConceptionEntity.attachToRelation(targetConceptionEntityUID,relationKind,relationAttributesMap,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                }else{
                    //BOTH
                    RelationEntity resultRelationEntity = sourceConceptionEntity.attachFromRelation(targetConceptionEntityUID,relationKind,relationAttributesMap,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                    resultRelationEntity = sourceConceptionEntity.attachToRelation(targetConceptionEntityUID,relationKind,relationAttributesMap,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                }
            }

            showPopupNotification(successRelationCount,NotificationVariant.LUMO_SUCCESS);
            if(successRelationCount > 0){
                RelationEntitiesCreatedEvent relationEntitiesCreatedEvent = new RelationEntitiesCreatedEvent();
                relationEntitiesCreatedEvent.setCreatedRelationEntitiesList(relationEntityInfoList);
                ResourceHolder.getApplicationBlackboard().fire(relationEntitiesCreatedEvent);
            }
        } catch (CoreRealmServiceEntityExploreException | CoreRealmServiceRuntimeException e) {
            CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionKind+" / "+conceptionEntityUID+" 新建实体关联操作错误",NotificationVariant.LUMO_ERROR);
            throw new RuntimeException(e);
        }finally {
            if(coreRealm != null){
                coreRealm.closeGlobalSession();
            }
        }
    }

    private void showPopupNotification(int relationCount, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念实体 "+conceptionKind+" / "+conceptionEntityUID+" 新建实体关联操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("新建关系实体数: "+relationCount)));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(null,null);
        AttributeValueOperateHandler attributeValueOperateHandlerForDelete = new AttributeValueOperateHandler(){
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(relationAttributeEditorsMap.containsKey(attributeName)){
                    relationEntityAttributesContainer.remove(relationAttributeEditorsMap.get(attributeName));
                }
                relationAttributeEditorsMap.remove(attributeName);
                if(relationAttributeEditorsMap.size()==0) {
                    clearAttributeButton.setEnabled(false);
                }
            }
        };
        AttributeValueOperateHandler attributeValueOperateHandlerForAdd = new AttributeValueOperateHandler() {
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(relationAttributeEditorsMap.containsKey(attributeName)){
                    CommonUIOperationUtil.showPopupNotification("已经设置了名称为 "+attributeName+" 的关系属性",NotificationVariant.LUMO_ERROR);
                }else{
                    AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(null,null,attributeValue);
                    attributeEditorItemWidget.setWidth(350,Unit.PIXELS);
                    relationEntityAttributesContainer.add(attributeEditorItemWidget);
                    attributeEditorItemWidget.setAttributeValueOperateHandler(attributeValueOperateHandlerForDelete);
                    relationAttributeEditorsMap.put(attributeName,attributeEditorItemWidget);
                }
                if(relationAttributeEditorsMap.size()>0){
                    clearAttributeButton.setEnabled(true);
                }
            }
        };
        addEntityAttributeView.setAttributeValueOperateHandler(attributeValueOperateHandlerForAdd);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加关系实体属性",null,true,480,210,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void cleanRelationAttributes(){
        relationEntityAttributesContainer.removeAll();
        relationAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
    }
}
