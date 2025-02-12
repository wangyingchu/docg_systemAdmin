package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.EntitySyntheticAbstractInfoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionKindSampleUI extends VerticalLayout {

    private String conceptionKind;
    private int sampleCount;
    private IntegerField entitiesSampleCountField;
    private HorizontalLayout mainLayout;
    private NativeLabel resultNumberValue;
    private ConceptionEntitiesListView conceptionEntitiesListView;
    private ConceptionEntitiesRelationsChart conceptionEntitiesRelationsChart;
    private EntitySyntheticAbstractInfoView entitySyntheticAbstractInfoView;
    private String lastSelectedConceptionEntityUID;

    public ConceptionKindSampleUI(String conceptionKind, int sampleCount) {
        this.conceptionKind = conceptionKind;
        this.sampleCount = sampleCount;

        List<Component> actionElementsList = new ArrayList<>();

        NativeLabel currentDisplayCountInfoMessage = new NativeLabel("当前采样数量:");
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","5px");
        currentDisplayCountInfoMessage.addClassNames("text-tertiary");
        actionElementsList.add(currentDisplayCountInfoMessage);

        this.entitiesSampleCountField = new IntegerField();
        this.entitiesSampleCountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        this.entitiesSampleCountField.setMin(1);
        this.entitiesSampleCountField.setStep(1);
        this.entitiesSampleCountField.setValue(this.sampleCount);
        actionElementsList.add(this.entitiesSampleCountField);

        Button resampleButton = new Button("重新采样");
        resampleButton.setIcon(VaadinIcon.REFRESH.create());
        resampleButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        actionElementsList.add(resampleButton);
        resampleButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConceptionEntitiesSample();
            }
        });

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.CONTROLLER.create(), "数据采样设置",actionElementsList,null);
        add(secondaryTitleActionBar);

        mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setWidthFull();
        add(mainLayout);

        VerticalLayout leftSideContainer = new VerticalLayout();
        leftSideContainer.setWidth(260,Unit.PIXELS);
        leftSideContainer.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainer.setSpacing(true);
        leftSideContainer.setMargin(false);
        leftSideContainer.setPadding(false);
        mainLayout.add(leftSideContainer);

        HorizontalLayout rightSideContainer = new HorizontalLayout();
        rightSideContainer.setWidthFull();
        rightSideContainer.setSpacing(false);
        rightSideContainer.setMargin(false);
        rightSideContainer.setPadding(false);
        mainLayout.add(rightSideContainer);

        this.resultNumberValue = new NativeLabel("-");
        this.resultNumberValue.addClassNames("text-xs","font-bold");
        this.resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.CUBES_SOLID.create(),"概念类型实体采样结果",this.resultNumberValue);
        filterTitle.getStyle().set("padding-left","10px");
        leftSideContainer.add(filterTitle);
        this.conceptionEntitiesListView = new ConceptionEntitiesListView(false);
        this.conceptionEntitiesListView.setWidth(250,Unit.PIXELS);
        leftSideContainer.add(this.conceptionEntitiesListView);

        ConceptionEntitiesListView.SelectConceptionEntityListener selectConceptionEntityListener = new ConceptionEntitiesListView.SelectConceptionEntityListener() {
            @Override
            public void onSelectConceptionEntity(ConceptionEntity conceptionEntity) {
                if(conceptionEntity != null){
                    conceptionEntitiesRelationsChart.unSelectConceptionEntity(lastSelectedConceptionEntityUID);
                    lastSelectedConceptionEntityUID = conceptionEntity.getConceptionEntityUID();
                    entitySyntheticAbstractInfoView.renderConceptionEntitySyntheticAbstractInfo(conceptionEntity.getConceptionKindName(),conceptionEntity.getConceptionEntityUID());
                    conceptionEntitiesRelationsChart.selectConceptionEntity(conceptionEntity.getConceptionEntityUID());
                }
            }

            @Override
            public void onUnSelectConceptionEntity(ConceptionEntity conceptionEntity) {
                if(conceptionEntity != null){
                    entitySyntheticAbstractInfoView.cleanAbstractInfo();
                    if(conceptionEntity!= null){
                        conceptionEntitiesRelationsChart.unSelectConceptionEntity(conceptionEntity.getConceptionEntityUID());
                    }
                    lastSelectedConceptionEntityUID = null;
                }
            }
        };
        this.conceptionEntitiesListView.setSelectConceptionEntityListener(selectConceptionEntityListener);

        this.conceptionEntitiesRelationsChart = new ConceptionEntitiesRelationsChart();
        this.conceptionEntitiesRelationsChart.setContainerConceptionKindSampleUI(this);
        rightSideContainer.add(this.conceptionEntitiesRelationsChart);

        this.entitySyntheticAbstractInfoView = new EntitySyntheticAbstractInfoView(330);
        this.entitySyntheticAbstractInfoView.getStyle().set("padding-left", "8px");
        this.entitySyntheticAbstractInfoView.setWidth(350,Unit.PIXELS);
        this.entitySyntheticAbstractInfoView.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");
        rightSideContainer.add(this.entitySyntheticAbstractInfoView);
        rightSideContainer.setFlexGrow(1,this.conceptionEntitiesRelationsChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.conceptionEntitiesListView.setHeight(receiver.getBodyClientHeight()-145, Unit.PIXELS);
            this.entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(receiver.getBodyClientHeight()-330);
            this.conceptionEntitiesRelationsChart.setWidth(receiver.getBodyClientWidth()-700, Unit.PIXELS);
        }));
        doConceptionEntitiesSample();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        this.conceptionEntitiesRelationsChart.clearData();
        super.onDetach(detachEvent);
    }

    private void doConceptionEntitiesSample(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            try {
                int realSampleCount = entitiesSampleCountField.getValue();
                int executeSampleCount = realSampleCount == this.sampleCount ? this.sampleCount : realSampleCount;
                Set<ConceptionEntity> conceptionEntitySet = targetConceptionKind.getRandomEntities(executeSampleCount);
                int resultConceptionEntitiesCount = conceptionEntitySet.size();
                this.resultNumberValue.setText(""+resultConceptionEntitiesCount);
                this.conceptionEntitiesListView.renderConceptionEntitiesList(conceptionEntitySet);
                this.conceptionEntitiesRelationsChart.clearData();
                this.conceptionEntitiesRelationsChart.renderConceptionEntitiesList(conceptionEntitySet);
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void renderSelectConceptionEntityLogic(String entityType, String entityUID){
        lastSelectedConceptionEntityUID = entityUID;
        entitySyntheticAbstractInfoView.renderConceptionEntitySyntheticAbstractInfo(entityType,entityUID);
        conceptionEntitiesListView.selectConceptionEntity(entityUID);
    }

    public void renderUnselectConceptionEntityLogic(){
        lastSelectedConceptionEntityUID = null;
        entitySyntheticAbstractInfoView.cleanAbstractInfo();
        conceptionEntitiesListView.unSelectConceptionEntity();
    }

    public void renderSelectRelationEntityLogic(String entityType, String entityUID){
        entitySyntheticAbstractInfoView.renderRelationEntitySyntheticAbstractInfo(entityType,entityUID);
        conceptionEntitiesListView.unSelectConceptionEntity();
    }

    public void renderUnselectRelationEntityLogic(){
        entitySyntheticAbstractInfoView.cleanAbstractInfo();
    }
}
