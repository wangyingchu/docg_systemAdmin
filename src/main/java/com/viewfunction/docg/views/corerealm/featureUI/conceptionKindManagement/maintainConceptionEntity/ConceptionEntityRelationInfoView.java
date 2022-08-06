package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.github.appreciated.apexcharts.ApexCharts;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConceptionEntityRelationInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private VerticalLayout relationKindsInfoLayout;
    public ConceptionEntityRelationInfoView(String conceptionKind,String conceptionEntityUID){
        this.setPadding(false);
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"关联关系总量","-");
        inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_LEFT.create(),"关系入度","-");
        outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(),"关系出度","-");
        isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.BULLSEYE.create(),"是否稠密实体","-");

        Button createRelationButton= new Button("新建数据关联");
        createRelationButton.setIcon(VaadinIcon.LINK.create());
        createRelationButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderShowMetaInfoUI();
            }
        });

        Button reloadRelationInfoButton= new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddNewAttributeUI();
            }
        });

        actionComponentsList.add(createRelationButton);
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.RANDOM.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon,"关联关系概要: ",secondaryTitleComponentsList,actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        add(relationEntitiesDetailLayout);

        relationKindsInfoLayout = new VerticalLayout();
        relationKindsInfoLayout.setHeight(600,Unit.PIXELS);
        relationEntitiesDetailLayout.add(relationKindsInfoLayout);

        SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.CONNECT_O.create(),"关系类型分布");
        relationKindsInfoLayout.add(secondaryIconTitle);

        VerticalLayout relationEntitiesListContainerLayout = new VerticalLayout();
        relationEntitiesListContainerLayout.add(new Label("relationEntities list"));
        relationEntitiesDetailLayout.add(relationEntitiesListContainerLayout);


        //ApexCharts  conceptionEntityCountChart = new ConceptionEntityCountChart().build();
        //conceptionEntityCountChart.setWidth("300");
        //relationKindsInfoLayout.add(conceptionEntityCountChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadEntityRelationsInfo();
        /*
        ResourceHolder.getApplicationBlackboard().addListener(this);

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionKindMetaInfoGrid.setHeight(event.getHeight()-280, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionKindMetaInfoGrid.setHeight(browserHeight-280,Unit.PIXELS);
            conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(browserHeight-600);
            singleConceptionKindSummaryInfoContainerLayout.add(conceptionKindCorrelationInfoChart);
        }));
        */

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        //listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void loadEntityRelationsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                //List<RelationEntity> relationEntityList = targetEntity.getAllRelations();
                long allRelationsCount = targetEntity.countAllRelations();
                try {
                    long inDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.TO);
                    long outDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.FROM);
                    inDegreeDisplayItem.updateDisplayValue(""+inDegree);
                    outDegreeDisplayItem.updateDisplayValue(""+outDegree);
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
                relationCountDisplayItem.updateDisplayValue(""+allRelationsCount);
                isDenseDisplayItem.updateDisplayValue(""+targetEntity.isDense());

                Map<String,Long> attachedRelationKindCountInfo = targetEntity.countAttachedRelationKinds();
                ApexCharts entityAttachedRelationKindsCountChart = new EntityAttachedRelationKindsCountChart(attachedRelationKindCountInfo)
                        .withColors("#168eea", "#ee4f4f", "#03a9f4", "#76b852", "#323b43", "#59626a", "#0288d1", "#ffc107", "#d32f2f", "#00d1b2","#ced7df").build();
                entityAttachedRelationKindsCountChart.setWidth(250,Unit.PIXELS);
                entityAttachedRelationKindsCountChart.setHeight(130,Unit.PIXELS);
                relationKindsInfoLayout.add(entityAttachedRelationKindsCountChart);

                Set<String> relationKindsSet = attachedRelationKindCountInfo.keySet();
                for(String relationKindName : relationKindsSet){
                    HorizontalLayout relationKindInfoItem = new HorizontalLayout();
                    relationKindInfoItem.setSpacing(false);
                    relationKindsInfoLayout.add(relationKindInfoItem);
                    Button currentRelationKindButton = new Button(relationKindName);
                    currentRelationKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
                    relationKindInfoItem.add(currentRelationKindButton);

                    Span relationEntityCountSpan = new Span(""+attachedRelationKindCountInfo.get(relationKindName).toString());
                    relationEntityCountSpan.getStyle().set("font-size","var(--lumo-font-size-xxs)");
                    relationEntityCountSpan.setHeight(20,Unit.PIXELS);
                    relationEntityCountSpan.getElement().getThemeList().add("badge contrast");
                    relationKindInfoItem.add(relationEntityCountSpan);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }
}
