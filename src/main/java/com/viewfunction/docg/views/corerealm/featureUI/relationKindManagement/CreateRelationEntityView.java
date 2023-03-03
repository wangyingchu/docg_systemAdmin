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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.eventHandling.RelationEntitiesCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ConceptionEntityResourceHolderVO;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList.ProcessingConceptionEntityListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateRelationEntityView extends VerticalLayout {

    private ComboBox<KindMetaInfo> relationKindSelect;
    private RadioButtonGroup<String> relationDirectionRadioGroup;
    private ProcessingConceptionEntityListView processingConceptionEntityListView;
    private Checkbox allowDupTypeRelationCheckbox;
    private String conceptionKind;
    private String conceptionEntityUID;

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
        mainContainerLayout.add(basicInfoContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择关系类型");
        basicInfoContainerLayout.add(infoTitle1);

        relationKindSelect = new ComboBox();
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(250,Unit.PIXELS);

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

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定关系属性");
        basicInfoContainerLayout.add(infoTitle3);

        VerticalLayout relationEntityAttributesContainer = new VerticalLayout();
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

        String relationDirection = relationDirectionRadioGroup.getValue();
        boolean allowDupRelation = allowDupTypeRelationCheckbox.getValue();
        String relationKind = selectedRelationKind.getKindName();

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
            for(ConceptionEntityResourceHolderVO currentConceptionEntityResourceHolderVO:targetConceptionEntitiesInfoSet){
                String targetConceptionEntityUID = currentConceptionEntityResourceHolderVO.getConceptionEntityUID();
                if(relationDirection.equals("FROM")){
                    RelationEntity resultRelationEntity = sourceConceptionEntity.attachFromRelation(targetConceptionEntityUID,relationKind,null,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                }else if(relationDirection.equals("TO")){
                    RelationEntity resultRelationEntity = sourceConceptionEntity.attachToRelation(targetConceptionEntityUID,relationKind,null,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                }else{
                    //BOTH
                    RelationEntity resultRelationEntity = sourceConceptionEntity.attachFromRelation(targetConceptionEntityUID,relationKind,null,allowDupRelation);
                    if(resultRelationEntity != null){
                        successRelationCount++;
                        relationEntityInfoList.add(resultRelationEntity);
                    }
                    resultRelationEntity = sourceConceptionEntity.attachToRelation(targetConceptionEntityUID,relationKind,null,allowDupRelation);
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
}
