package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.queryRelationKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.RelationEntityDeletedEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.AddRelationEntityToProcessingListView;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.DeleteRelationEntityView;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class RelationKindQueryResultsView extends VerticalLayout implements
        RelationKindQueriedEvent.RelationKindQueriedListener,
        RelationEntityDeletedEvent.RelationEntityDeletedListener {
    private String relationKindName;
    private Registration listener;
    private Grid<RelationEntityValue> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
    private final String _rowIndexPropertyName = "ROW_INDEX";
    private NumberFormat numberFormat;
    private List<String> currentRowKeyList;
    public RelationKindQueryResultsView(String relationKindName){
        this.relationKindName = relationKindName;
        this.setPadding(true);
        this.setSpacing(true);

        HorizontalLayout titleLayout = new HorizontalLayout();
        add(titleLayout);
        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.HARDDRIVE_O),"查询结果");
        titleLayout.add(filterTitle2);
        startTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询开始时间","-");
        finishTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询结束时间","-");
        dataCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"结果集数据量","-");
        numberFormat = NumberFormat.getInstance();

        queryResultGrid = new Grid<>();
        queryResultGrid.setWidth(100, Unit.PERCENTAGE);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        queryResultGrid.addColumn(new ValueProvider<RelationEntityValue, Object>() {
            @Override
            public Object apply(RelationEntityValue relationEntityValue) {
                return relationEntityValue.getEntityAttributesValue().get(_rowIndexPropertyName);
            }
        }).setHeader("").setHeader("IDX").setKey("idx").setFlexGrow(0).setWidth("75px").setResizable(false);
        queryResultGrid.addComponentColumn(new RelationEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_0").setFlexGrow(0).setWidth("120px").setResizable(false);
        queryResultGrid.addColumn(RelationEntityValue::getRelationEntityUID).setHeader(" EntityUID").setKey("idx_1").setFlexGrow(1).setWidth("150px").setResizable(false);
        queryResultGrid.addColumn(RelationEntityValue::getFromConceptionEntityUID).setHeader(" FromEntityUID").setKey("idx_2").setFlexGrow(1).setWidth("150px").setResizable(false);
        queryResultGrid.addColumn(RelationEntityValue::getToConceptionEntityUID).setHeader(" ToEntityUID").setKey("idx_3").setFlexGrow(1).setWidth("150px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx = new GridColumnHeader(VaadinIcon.LIST_OL,"");
        queryResultGrid.getColumnByKey("idx").setHeader(gridColumnHeader_idx).setSortable(false);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        queryResultGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.KEY_O,"关系实体UID");
        queryResultGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx0).setSortable(false).setResizable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.KEY_O,"From 概念实体UID");
        queryResultGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false).setResizable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.KEY_O,"To 概念实体UID");
        queryResultGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(false).setResizable(true);
        add(queryResultGrid);

        queryResultGrid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<RelationEntityValue>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<RelationEntityValue> relationEntityValueItemDoubleClickEvent) {
                RelationEntityValue targetRelationEntityValue = relationEntityValueItemDoubleClickEvent.getItem();
                if(targetRelationEntityValue!= null){
                    renderRelationEntityUI(targetRelationEntityValue);
                }
            }
        });

        this.currentRowKeyList = new ArrayList<>();
    }

    @Override
    public void receivedRelationKindQueriedEvent(RelationKindQueriedEvent event) {
        for(String currentExistingRowKey:this.currentRowKeyList){
            queryResultGrid.removeColumnByKey(currentExistingRowKey);
        }
        queryResultGrid.setItems(new ArrayList<>());
        this.currentRowKeyList.clear();
        String relationKindName = event.getRelationKindName();
        List<String> resultAttributesList = event.getResultAttributesList();
        QueryParameters eventQueryParameters = event.getQueryParameters();
        if(relationKindName.equals(this.relationKindName)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            RelationKind targetRelation = coreRealm.getRelationKind(relationKindName);
            QueryParameters queryParameters = eventQueryParameters != null ? eventQueryParameters : new QueryParameters();
            try {
                List<String> attributesList = new ArrayList<>();
                if(resultAttributesList != null && resultAttributesList.size() > 0){
                    attributesList.addAll(resultAttributesList);
                }else{
                    attributesList.add(RealmConstant._createDateProperty);
                    attributesList.add(RealmConstant._lastModifyDateProperty);
                    attributesList.add(RealmConstant._creatorIdProperty);
                    attributesList.add(RealmConstant._dataOriginProperty);
                }

                RelationEntitiesAttributesRetrieveResult relationEntitiesAttributesRetrieveResult =
                        targetRelation.getEntityAttributesByAttributeNames(attributesList,queryParameters);
                if(relationEntitiesAttributesRetrieveResult != null && relationEntitiesAttributesRetrieveResult.getOperationStatistics() != null){
                    showPopupNotification(relationEntitiesAttributesRetrieveResult, NotificationVariant.LUMO_SUCCESS);
                    Date startDateTime = relationEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                    String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    Date finishDateTime = relationEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                    String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                    dataCountDisplayItem.updateDisplayValue(""+numberFormat.format(relationEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount()));

                    List<RelationEntityValue> relationEntityValueList = relationEntitiesAttributesRetrieveResult.getRelationEntityValues();
                    for(int i=0 ; i<relationEntityValueList.size();i++){
                        RelationEntityValue currentRelationEntityValue = relationEntityValueList.get(i);
                        currentRelationEntityValue.getEntityAttributesValue().put(_rowIndexPropertyName,i+1);
                    }
                    if (resultAttributesList != null && resultAttributesList.size() > 0) {
                        for (String currentProperty : resultAttributesList) {
                            if (!currentProperty.equals(_rowIndexPropertyName)) {
                                queryResultGrid.addColumn(new ValueProvider<RelationEntityValue, Object>() {
                                    @Override
                                    public Object apply(RelationEntityValue relationEntityValue) {
                                        return relationEntityValue.getEntityAttributesValue().get(currentProperty);
                                    }
                                }).setHeader(" " + currentProperty).setKey(currentProperty + "_KEY");
                                queryResultGrid.getColumnByKey(currentProperty + "_KEY").setSortable(true).setResizable(true);
                            }

                            this.currentRowKeyList.add(currentProperty + "_KEY");
                        }
                    }
                    queryResultGrid.setItems(relationEntityValueList);
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryResultGrid.setHeight(event.getHeight()-140,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryResultGrid.setHeight(browserHeight-140,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedRelationEntityDeletedEvent(RelationEntityDeletedEvent event) {
        String relationKindName = event.getRelationKindName();
        if(relationKindName.equals(relationKindName)){
            String relationEntityUID = event.getRelationEntityUID();
            ListDataProvider dataProvider=(ListDataProvider)queryResultGrid.getDataProvider();
            RelationEntityValue relationEntityValueToDelete = null;
            Collection<RelationEntityValue> relationEntityValueList = dataProvider.getItems();
            for(RelationEntityValue currentRelationEntityValue:relationEntityValueList){
                if(currentRelationEntityValue.getRelationEntityUID().equals(relationEntityUID)){
                    relationEntityValueToDelete = currentRelationEntityValue;
                    break;
                }
            }
            if(relationEntityValueToDelete != null){
                dataProvider.getItems().remove(relationEntityValueToDelete);
                dataProvider.refreshAll();
            }
        }
    }

    private class RelationEntityActionButtonsValueProvider implements ValueProvider<RelationEntityValue,HorizontalLayout>{
        @Override
        public HorizontalLayout apply(RelationEntityValue relationEntityValue) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(showDetailButton, "显示关系实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntityValue != null){
                        renderRelationEntityUI(relationEntityValue);
                    }
                }
            });

            Button addToProcessListButton = new Button();
            addToProcessListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addToProcessListButton.setIcon(VaadinIcon.INBOX.create());
            Tooltips.getCurrent().setTooltip(addToProcessListButton, "加入待处理数据列表");
            actionButtonContainerLayout.add(addToProcessListButton);
            addToProcessListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntityValue != null){
                        addRelationEntityToProcessingList(relationEntityValue);
                    }
                }
            });

            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR );
            deleteButton.setIcon(VaadinIcon.UNLINK.create());
            Tooltips.getCurrent().setTooltip(deleteButton, "删除关系实体");
            actionButtonContainerLayout.add(deleteButton);
            deleteButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(relationEntityValue != null){
                        deleteRelationEntity(relationEntityValue);
                    }
                }
            });
            return actionButtonContainerLayout;
        }
    }

    private void showPopupNotification(RelationEntitiesAttributesRetrieveResult relationEntitiesAttributesRetrieveResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("关系类型 "+ relationKindName +" 实例查询操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("查询返回实体数: "+relationEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+relationEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+relationEntitiesAttributesRetrieveResult.getOperationStatistics().getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }

    private void renderRelationEntityUI(RelationEntityValue relationEntityValue){
        RelationEntityDetailUI relationEntityDetailUI = new RelationEntityDetailUI(relationKindName,relationEntityValue.getRelationEntityUID());

        List<Component> actionComponentList = new ArrayList<>();

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        actionComponentList.add(footPrintStartIcon);
        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("12px");
        relationKindIcon.getStyle().set("padding-right","3px");
        actionComponentList.add(relationKindIcon);
        Label relationKindNameLabel = new Label(relationKindName);
        actionComponentList.add(relationKindNameLabel);
        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("12px");
        divIcon.getStyle().set("padding-left","5px");
        actionComponentList.add(divIcon);
        Icon relationEntityIcon = VaadinIcon.KEY_O.create();
        relationEntityIcon.setSize("18px");
        relationEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        actionComponentList.add(relationEntityIcon);

        Label relationEntityUIDLabel = new Label(relationEntityValue.getRelationEntityUID());
        actionComponentList.add(relationEntityUIDLabel);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationEntityDetailUI);
        relationEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void addRelationEntityToProcessingList(RelationEntityValue relationEntityValue){
        AddRelationEntityToProcessingListView addRelationEntityToProcessingListView = new AddRelationEntityToProcessingListView(relationKindName,relationEntityValue);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INBOX),"待处理数据列表添加概念实例",null,true,600,300,false);
        fixSizeWindow.setWindowContent(addRelationEntityToProcessingListView);
        fixSizeWindow.setModel(true);
        addRelationEntityToProcessingListView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void deleteRelationEntity(RelationEntityValue relationEntityValue){
        DeleteRelationEntityView deleteRelationEntityView = new DeleteRelationEntityView(relationKindName,relationEntityValue);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除关系实体",null,true,600,210,false);
        fixSizeWindow.setWindowContent(deleteRelationEntityView);
        fixSizeWindow.setModel(true);
        deleteRelationEntityView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
