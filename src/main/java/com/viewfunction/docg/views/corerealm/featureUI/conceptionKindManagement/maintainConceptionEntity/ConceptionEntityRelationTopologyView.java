package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntityRelationTopologyView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private int conceptionEntityRelationInfoViewHeightOffset;
    private ConceptionEntityRelationsChart conceptionEntityRelationsChart;

    public ConceptionEntityRelationTopologyView(String conceptionKind,String conceptionEntityUID,int conceptionEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityRelationInfoViewHeightOffset = conceptionEntityIntegratedInfoViewHeightOffset+100;
        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(), "关联关系总量", "-");
        inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_LEFT.create(), "关系入度", "-");
        outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(), "关系出度", "-");
        isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.BULLSEYE.create(), "是否稠密实体", "-");

        Button reloadConceptionEntitiesInfoButton = new Button("关联概念实体类型分布");
        reloadConceptionEntitiesInfoButton.setIcon(VaadinIcon.PIE_CHART.create());
        reloadConceptionEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadConceptionEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(reloadConceptionEntitiesInfoButton);

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.RANDOM.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关联关系概要: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        this.conceptionEntityRelationsChart = new ConceptionEntityRelationsChart();
        add(this.conceptionEntityRelationsChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        super.onDetach(detachEvent);
    }

    public void loadEntityRelationNetworks(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null) {
            try {
                ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
                if (targetEntity != null) {
                    List<RelationEntity> totalKindsRelationEntitiesList = new ArrayList<>();
                    List<String> attachedRelationKinds = targetEntity.listAttachedRelationKinds();
                    QueryParameters relationshipQueryParameters = new QueryParameters();
                    relationshipQueryParameters.setStartPage(1);
                    relationshipQueryParameters.setEndPage(2);
                    relationshipQueryParameters.setPageSize(10);
                    for (String currentRelationKind : attachedRelationKinds) {
                        relationshipQueryParameters.setEntityKind(currentRelationKind);
                        List<RelationEntity> currentKindTargetRelationEntityList = targetEntity.getSpecifiedRelations(relationshipQueryParameters, RelationDirection.TWO_WAY);
                        totalKindsRelationEntitiesList.addAll(currentKindTargetRelationEntityList);
                    }

                    this.conceptionEntityRelationsChart.setData(totalKindsRelationEntitiesList);



                }else{
                    CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }
}
