package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConceptionEntityRelationInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    public ConceptionEntityRelationInfoView(String conceptionKind,String conceptionEntityUID){
        this.setPadding(false);
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

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
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon,"关联关系总量:",secondaryTitleComponentsList,actionComponentsList);
        add(secondaryTitleActionBar);
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

        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                List<RelationEntity> relationEntityList = targetEntity.getAllRelations();
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
    }
}
