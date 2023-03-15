package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.EntitySyntheticAbstractInfoView;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.AddRelationEntityToProcessingListView;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;

public class RelationEntityConnectedConceptionEntitiesPairView extends VerticalLayout {

    private String relationKind;
    private String relationEntityUID;
    private HorizontalLayout fromConceptionEntityOperateLayout;
    private HorizontalLayout toConceptionEntityOperateLayout;
    private String fromConceptionEntityKind;
    private String fromConceptionEntityUID;
    private String toConceptionEntityKind;
    private String toConceptionEntityUID;
    private Registration listener;
    private EntitySyntheticAbstractInfoView entitySyntheticAbstractInfoView;
    private int relationEntityIntegratedInfoViewHeightOffset;
    private RelationConceptionEntitiesPairChart relationConceptionEntitiesPairChart;

    public RelationEntityConnectedConceptionEntitiesPairView(String relationKind,String relationEntityUID,int relationEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
        this.relationEntityIntegratedInfoViewHeightOffset = relationEntityIntegratedInfoViewHeightOffset;

        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        HorizontalLayout fromConceptionEntityInfoLayout = new HorizontalLayout();
        fromConceptionEntityInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label fromConceptionEntityText = new Label("FROM 概念实体");
        fromConceptionEntityText.addClassNames("text-xs","font-medium","text-secondary");
        fromConceptionEntityInfoLayout.add(fromConceptionEntityText);
        this.fromConceptionEntityOperateLayout = new HorizontalLayout();
        this.fromConceptionEntityOperateLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.fromConceptionEntityOperateLayout.setSpacing(false);
        fromConceptionEntityInfoLayout.add(this.fromConceptionEntityOperateLayout);
        titleLayout.add(fromConceptionEntityInfoLayout);

        Icon divIcon = VaadinIcon.ANGLE_DOUBLE_RIGHT.create();
        divIcon.setSize("16px");
        titleLayout.add(divIcon);

        HorizontalLayout toConceptionEntityInfoLayout = new HorizontalLayout();
        toConceptionEntityInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label toConceptionEntityText = new Label("TO 概念实体");
        toConceptionEntityText.addClassNames("text-xs","font-medium","text-secondary");
        toConceptionEntityInfoLayout.add(toConceptionEntityText);
        this.toConceptionEntityOperateLayout = new HorizontalLayout();
        this.toConceptionEntityOperateLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.toConceptionEntityOperateLayout.setSpacing(false);
        toConceptionEntityInfoLayout.add(this.toConceptionEntityOperateLayout);
        titleLayout.add(toConceptionEntityInfoLayout);

        secondaryTitleComponentsList.add(titleLayout);

        Button addToProcessingDataListButton = new Button("加入待处理数据列表");
        addToProcessingDataListButton.setIcon(VaadinIcon.INBOX.create());
        addToProcessingDataListButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addToProcessingDataListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                addRelationEntityToProcessingList();
            }
        });

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                relationConceptionEntitiesPairChart.reload();
            }
        });

        actionComponentsList.add(addToProcessingDataListButton);
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.INFO_CIRCLE_O.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关系实体概要: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        relationConceptionEntitiesPairChart = new RelationConceptionEntitiesPairChart();
        relationEntitiesDetailLayout.add(relationConceptionEntitiesPairChart);
        relationConceptionEntitiesPairChart.setContainerRelationEntityConnectedConceptionEntitiesPairView(this);

        VerticalLayout selectedEntityInfoContainerLayout = new VerticalLayout();
        selectedEntityInfoContainerLayout.setSpacing(false);
        selectedEntityInfoContainerLayout.setMargin(false);
        selectedEntityInfoContainerLayout.getStyle().set("padding-right","0px");
        selectedEntityInfoContainerLayout.setWidth(330, Unit.PIXELS);
        selectedEntityInfoContainerLayout.getStyle()
                .set("border-left", "1px solid var(--lumo-contrast-20pct)");
        relationEntitiesDetailLayout.add(selectedEntityInfoContainerLayout);
        relationEntitiesDetailLayout.setFlexGrow(1,relationConceptionEntitiesPairChart);

        this.entitySyntheticAbstractInfoView = new EntitySyntheticAbstractInfoView(330);
        selectedEntityInfoContainerLayout.add(this.entitySyntheticAbstractInfoView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderView();
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            relationConceptionEntitiesPairChart.setHeight((event.getHeight()-this.relationEntityIntegratedInfoViewHeightOffset)-120, Unit.PIXELS);
            entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(event.getHeight()-this.relationEntityIntegratedInfoViewHeightOffset-320);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            relationConceptionEntitiesPairChart.setHeight((browserHeight-this.relationEntityIntegratedInfoViewHeightOffset+40)-120,Unit.PIXELS);
            entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(browserHeight-this.relationEntityIntegratedInfoViewHeightOffset-320);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderView(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        RelationKind targetRelation = coreRealm.getRelationKind(this.relationKind);

        if(targetRelation != null){
            RelationEntity targetRelationEntity = targetRelation.getEntityByUID(this.relationEntityUID);
            if(targetRelationEntity != null){
                String relationFromConceptionEntityUID = targetRelationEntity.getFromConceptionEntityUID();
                List<String> fromConceptionKinds = targetRelationEntity.getFromConceptionEntityKinds();
                String relationToConceptionEntityUID = targetRelationEntity.getToConceptionEntityUID();
                List<String> toConceptionKinds = targetRelationEntity.getToConceptionEntityKinds();

                this.fromConceptionEntityKind = fromConceptionKinds.get(0);
                this.fromConceptionEntityUID = relationFromConceptionEntityUID;
                this.toConceptionEntityKind = toConceptionKinds.get(0);
                this.toConceptionEntityUID = relationToConceptionEntityUID;

                Icon fromConceptionKindIcon = VaadinIcon.CUBE.create();
                fromConceptionKindIcon.setSize("12px");
                fromConceptionKindIcon.getStyle().set("padding-right","3px");
                Icon fromConceptionEntityIcon = VaadinIcon.KEY_O.create();
                fromConceptionEntityIcon.setSize("18px");
                fromConceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");

                String fromConceptionKindsTxt = "";
                if(fromConceptionKinds !=null &&fromConceptionKinds.size()>0){
                    for(int i=0; i<fromConceptionKinds.size(); i++){
                        fromConceptionKindsTxt = fromConceptionKindsTxt+fromConceptionKinds.get(i);
                        if(i<fromConceptionKinds.size()-1){
                            fromConceptionKindsTxt = fromConceptionKindsTxt+" | ";
                        }
                    }
                }
                Label fromConceptionKindsLabel = new Label(fromConceptionKindsTxt);
                fromConceptionKindsLabel.addClassNames("text-xs","font-medium");
                Label fromConceptionEntityUIDLabel = new Label(fromConceptionEntityUID);
                fromConceptionEntityUIDLabel.addClassNames("text-xs","font-medium");
                Icon fromDivIcon = VaadinIcon.ITALIC.create();
                fromDivIcon.setSize("12px");
                fromDivIcon.getStyle().set("padding-left","5px");

                Button showFromEntityDetailButton = new Button();
                showFromEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                showFromEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
                showFromEntityDetailButton.setIcon(VaadinIcon.EYE.create());
                Tooltips.getCurrent().setTooltip(showFromEntityDetailButton, "显示 FROM 概念实体详情");
                showFromEntityDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        renderConceptionEntityUI(fromConceptionEntityKind,fromConceptionEntityUID);
                    }
                });
                this.fromConceptionEntityOperateLayout.add(fromConceptionKindIcon,fromConceptionKindsLabel,fromDivIcon,fromConceptionEntityIcon,fromConceptionEntityUIDLabel,showFromEntityDetailButton);

                Icon toConceptionKindIcon = VaadinIcon.CUBE.create();
                toConceptionKindIcon.setSize("12px");
                toConceptionKindIcon.getStyle().set("padding-right","3px");
                Icon toConceptionEntityIcon = VaadinIcon.KEY_O.create();
                toConceptionEntityIcon.setSize("18px");
                toConceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");

                String toConceptionKindsTxt = "";
                if(toConceptionKinds !=null &&toConceptionKinds.size()>0){
                    for(int i=0; i<toConceptionKinds.size(); i++){
                        toConceptionKindsTxt = toConceptionKindsTxt+toConceptionKinds.get(i);
                        if(i<toConceptionKinds.size()-1){
                            toConceptionKindsTxt = toConceptionKindsTxt+" | ";
                        }
                    }
                }
                Label toConceptionKindsLabel = new Label(toConceptionKindsTxt);
                toConceptionKindsLabel.addClassNames("text-xs","font-medium");
                Label toConceptionEntityUIDLabel = new Label(toConceptionEntityUID);
                toConceptionEntityUIDLabel.addClassNames("text-xs","font-medium");
                Icon toDivIcon = VaadinIcon.ITALIC.create();
                toDivIcon.setSize("12px");
                toDivIcon.getStyle().set("padding-left","5px");

                Button showToEntityDetailButton = new Button();
                showToEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                showToEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
                showToEntityDetailButton.setIcon(VaadinIcon.EYE.create());
                Tooltips.getCurrent().setTooltip(showToEntityDetailButton, "显示 TO 概念实体详情");
                showToEntityDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        renderConceptionEntityUI(toConceptionEntityKind,toConceptionEntityUID);
                    }
                });
                this.toConceptionEntityOperateLayout.add(toConceptionKindIcon,toConceptionKindsLabel,toDivIcon,toConceptionEntityIcon,toConceptionEntityUIDLabel,showToEntityDetailButton);
                this.relationConceptionEntitiesPairChart.setDate(this.relationKind,this.relationEntityUID,this.fromConceptionEntityUID,fromConceptionKinds,this.toConceptionEntityUID,toConceptionKinds);
            }else{
                CommonUIOperationUtil.showPopupNotification("关系类型 "+ relationKind +" 中不存在 UID 为"+ relationEntityUID +" 的关系实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("关系类型 "+ relationKind +" 不存在", NotificationVariant.LUMO_ERROR);
        }
    }

    private void renderConceptionEntityUI(String conceptionKindName,String conceptionEntityUID){
        ConceptionEntityDetailView conceptionEntityDetailView = new ConceptionEntityDetailView(conceptionKindName,conceptionEntityUID);

        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(conceptionKindIcon);
        Label conceptionKindNameLabel = new Label(conceptionKindName);
        actionComponentList.add(conceptionKindNameLabel);
        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("12px");
        divIcon.getStyle().set("padding-left","5px");
        actionComponentList.add(divIcon);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        actionComponentList.add(conceptionEntityIcon);

        Label conceptionEntityUIDLabel = new Label(conceptionEntityUID);
        actionComponentList.add(conceptionEntityUIDLabel);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailView);
        conceptionEntityDetailView.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    public void renderSelectedConceptionEntityAbstractInfo(String entityType,String entityUID){
        entitySyntheticAbstractInfoView.renderConceptionEntitySyntheticAbstractInfo(entityType,entityUID);
    }

    public void clearConceptionEntityAbstractInfo(){
        entitySyntheticAbstractInfoView.cleanAbstractInfo();
    }

    public void renderSelectedRelationEntityAbstractInfo(String entityType,String entityUID){
        entitySyntheticAbstractInfoView.renderRelationEntitySyntheticAbstractInfo(entityType,entityUID);
    }

    public void clearRelationEntityAbstractInfo(){
        entitySyntheticAbstractInfoView.cleanAbstractInfo();
    }

    private void addRelationEntityToProcessingList(){
        RelationEntityValue relationEntityValue = new RelationEntityValue();
        relationEntityValue.setRelationEntityUID(this.relationEntityUID);
        AddRelationEntityToProcessingListView addRelationEntityToProcessingListView = new AddRelationEntityToProcessingListView(this.relationKind,relationEntityValue);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INBOX),"待处理数据列表添加概念实例",null,true,600,320,false);
        fixSizeWindow.setWindowContent(addRelationEntityToProcessingListView);
        fixSizeWindow.setModel(true);
        addRelationEntityToProcessingListView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
