package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ConceptionEntitiesGeospatialInfoAnalysisView extends VerticalLayout {
    private Registration listener;
    private IntegerField entitiesSampleCountField;
    private ConceptionEntitiesGeospatialScaleMapInfoChart conceptionEntitiesGeospatialScaleMapInfoChart;
    private int entitiesSampleCount = 100;
    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;
    private NativeLabel resultNumberValue;
    private Grid<ConceptionEntity> displayedConceptionEntitiesGrid;
    private HorizontalLayout doesNotContainsSpatialInfoMessage;
    private HorizontalLayout mainLayout;

    public ConceptionEntitiesGeospatialInfoAnalysisView(String kindName, GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel,
                                                        ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult) {
        this.conceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;
        List<Component> actionElementsList = new ArrayList<>();

        NativeLabel currentDisplayCountInfoMessage = new NativeLabel("当前采样数量:");
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","5px");
        currentDisplayCountInfoMessage.addClassNames("text-tertiary");
        actionElementsList.add(currentDisplayCountInfoMessage);

        this.entitiesSampleCountField = new IntegerField();
        this.entitiesSampleCountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        this.entitiesSampleCountField.setMin(1);
        this.entitiesSampleCountField.setStep(1);
        this.entitiesSampleCountField.setValue(this.entitiesSampleCount);
        actionElementsList.add(this.entitiesSampleCountField);

        Button resampleButton = new Button("重新采样");
        resampleButton.setIcon(VaadinIcon.REFRESH.create());
        resampleButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        actionElementsList.add(resampleButton);
        resampleButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshMapSpatialInfo();
            }
        });

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.CONTROLLER.create(), "数据采样设置",actionElementsList,null);
        add(secondaryTitleActionBar);

        doesNotContainsSpatialInfoMessage = new HorizontalLayout();
        doesNotContainsSpatialInfoMessage.setSpacing(true);
        doesNotContainsSpatialInfoMessage.setPadding(true);
        doesNotContainsSpatialInfoMessage.setMargin(true);
        doesNotContainsSpatialInfoMessage.setWidth(100,Unit.PERCENTAGE);
        doesNotContainsSpatialInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前采样概念实体中不包含指定类型的地理空间信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsSpatialInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsSpatialInfoMessage);

        mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setWidthFull();
        add(mainLayout);

        VerticalLayout leftSideContainer = new VerticalLayout();
        leftSideContainer.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainer.setWidth(400,Unit.PIXELS);
        leftSideContainer.setSpacing(true);
        leftSideContainer.setMargin(false);
        leftSideContainer.setPadding(false);
        mainLayout.add(leftSideContainer);

        VerticalLayout rightSideContainer = new VerticalLayout();
        rightSideContainer.setSpacing(false);
        rightSideContainer.setMargin(false);
        rightSideContainer.setPadding(false);
        mainLayout.add(rightSideContainer);

        this.resultNumberValue = new NativeLabel("100");
        this.resultNumberValue.addClassNames("text-xs","font-bold");
        this.resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(FontAwesome.Solid.MAP.create(),"含地理空间信息概念实体采样结果",resultNumberValue);
        filterTitle.getStyle().set("padding-left","10px");
        leftSideContainer.add(filterTitle);

        this.displayedConceptionEntitiesGrid = new Grid<>();
        this.displayedConceptionEntitiesGrid.setWidth(100,Unit.PERCENTAGE);
        this.displayedConceptionEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_COMPACT);
        this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionKindName).setHeader("").setKey("idx_0").setFlexGrow(1).setResizable(false);
        this.displayedConceptionEntitiesGrid.addColumn(ConceptionEntity::getConceptionEntityUID).setHeader("").setKey("idx_1").setFlexGrow(0).setWidth("100px").setResizable(false);
        this.displayedConceptionEntitiesGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("60px").setResizable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.CUBE,"概念类型");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        this.displayedConceptionEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false);
        this.displayedConceptionEntitiesGrid.addSelectionListener(new SelectionListener<Grid<ConceptionEntity>, ConceptionEntity>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ConceptionEntity>, ConceptionEntity> selectionEvent) {
                Set<ConceptionEntity> selectedEntities = selectionEvent.getAllSelectedItems();
                if(!selectedEntities.isEmpty()){
                    conceptionEntitiesGeospatialScaleMapInfoChart.flyToPointedConceptionEntities(selectedEntities);
                }
            }
        });

        leftSideContainer.add(this.displayedConceptionEntitiesGrid);

        this.conceptionEntitiesGeospatialScaleMapInfoChart = new ConceptionEntitiesGeospatialScaleMapInfoChart(kindName,spatialScaleLevel,conceptionEntitiesAttributesRetrieveResult);
        rightSideContainer.add(this.conceptionEntitiesGeospatialScaleMapInfoChart);

        List<ConceptionEntity> displayedConceptionEntities = this.conceptionEntitiesGeospatialScaleMapInfoChart.renderMapAndSpatialInfo(getRandomEntitiesUID(entitiesSampleCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues()));
        if(displayedConceptionEntities == null || displayedConceptionEntities.size() == 0){
            mainLayout.setVisible(false);
            doesNotContainsSpatialInfoMessage.setVisible(true);
        }else{
            mainLayout.setVisible(true);
            doesNotContainsSpatialInfoMessage.setVisible(false);
        }
        this.resultNumberValue.setText(""+displayedConceptionEntities.size());
        this.displayedConceptionEntitiesGrid.setItems(displayedConceptionEntities);
    }

    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntity,HorizontalLayout>{
        @Override
        public HorizontalLayout apply(ConceptionEntity conceptionEntityValue) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            showDetailButton.setTooltipText("显示概念实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });
            return actionButtonContainerLayout;
        }
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.conceptionEntitiesGeospatialScaleMapInfoChart.setHeight(receiver.getBodyClientHeight()-120, Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void refreshMapSpatialInfo(){
        if(this.entitiesSampleCountField.getValue() == null || this.entitiesSampleCountField.isInvalid()){
            this.entitiesSampleCountField.setValue(this.entitiesSampleCount);
        }
        int currentSampleCount = this.entitiesSampleCountField.getValue();
        List<String> conceptionEntitiesUIDList = getRandomEntitiesUID(currentSampleCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues());
        this.conceptionEntitiesGeospatialScaleMapInfoChart.clearMap();
        if(conceptionEntitiesUIDList != null){
            List<String> targetEntitiesUIDList = getRandomEntitiesUID(currentSampleCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues());
            List<ConceptionEntity> displayedConceptionEntities = this.conceptionEntitiesGeospatialScaleMapInfoChart.renderMapAndSpatialInfo(targetEntitiesUIDList);
            if(displayedConceptionEntities == null || displayedConceptionEntities.size() == 0){
                mainLayout.setVisible(false);
                doesNotContainsSpatialInfoMessage.setVisible(true);
            }else{
                mainLayout.setVisible(true);
                doesNotContainsSpatialInfoMessage.setVisible(false);
            }
            this.resultNumberValue.setText(""+displayedConceptionEntities.size());
            this.displayedConceptionEntitiesGrid.setItems(displayedConceptionEntities);
        }
    }

    private List<String> getRandomEntitiesUID(int targetEntitiesCount,List<ConceptionEntityValue> conceptionEntityValueList){
        List<String> allEntitiesUIDList = new ArrayList<>();
        conceptionEntityValueList.forEach(new SerializableConsumer<ConceptionEntityValue>() {
            @Override
            public void accept(ConceptionEntityValue conceptionEntityValue) {
                allEntitiesUIDList.add(conceptionEntityValue.getConceptionEntityUID());
            }
        });

        int realSampleCount = 0;
        if(conceptionEntityValueList.size() >= targetEntitiesCount){
            realSampleCount = targetEntitiesCount;
        }else{
            realSampleCount = conceptionEntityValueList.size();
        }
        Collections.shuffle(allEntitiesUIDList);
        return allEntitiesUIDList.subList(0, realSampleCount);
    }

    private void renderConceptionEntityUI(ConceptionEntity conceptionEntityValue){
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionEntityValue.getConceptionKindName(),conceptionEntityValue.getConceptionEntityUID());

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionEntityValue.getConceptionKindName());
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityValue.getConceptionEntityUID());
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }
}
