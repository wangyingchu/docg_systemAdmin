package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import dev.mett.vaadin.tooltip.Tooltips;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ConceptionKindQueryResultsView extends VerticalLayout implements
        ConceptionKindQueriedEvent.ConceptionKindQueriedListener {
    private String conceptionKindName;
    private Registration listener;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
    private final String _rowIndexPropertyName = "ROW_INDEX";

    private List<String> currentRowKeyList;
    public ConceptionKindQueryResultsView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setPadding(true);
        this.setSpacing(true);

        HorizontalLayout titleLayout = new HorizontalLayout();
        add(titleLayout);
        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.HARDDRIVE_O),"查询结果");
        titleLayout.add(filterTitle2);
        startTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询开始时间","-");
        finishTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询结束时间","-");
        dataCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"结果集数据量","-");

        queryResultGrid = new Grid<>();
        queryResultGrid.setWidth(100,Unit.PERCENTAGE);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
            @Override
            public Object apply(ConceptionEntityValue conceptionEntityValue) {
                return conceptionEntityValue.getEntityAttributesValue().get(_rowIndexPropertyName);
            }
        }).setHeader("").setHeader("IDX").setKey("idx").setFlexGrow(0).setWidth("60px").setResizable(false);
        queryResultGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_0").setFlexGrow(0).setWidth("110px").setResizable(false);
        queryResultGrid.addColumn(ConceptionEntityValue::getConceptionEntityUID).setHeader(" EntityUID").setKey("idx_1").setFlexGrow(1).setWidth("150px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx = new GridColumnHeader(VaadinIcon.LIST_OL,"");
        queryResultGrid.getColumnByKey("idx").setHeader(gridColumnHeader_idx).setSortable(false);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        queryResultGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        queryResultGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx0).setSortable(false).setResizable(true);
        add(queryResultGrid);

        this.currentRowKeyList = new ArrayList<>();
    }

    @Override
    public void receivedConceptionKindQueriedEvent(ConceptionKindQueriedEvent event) {
        for(String currentExistingRowKey:this.currentRowKeyList){
            queryResultGrid.removeColumnByKey(currentExistingRowKey);
        }
        queryResultGrid.setItems(new ArrayList<>());
        this.currentRowKeyList.clear();
        String conceptionKindName = event.getConceptionKindName();
        List<String> resultAttributesList = event.getResultAttributesList();
        if(conceptionKindName.equals(this.conceptionKindName)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKindName);
            QueryParameters queryParameters = new QueryParameters();
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

                ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult =
                        targetConception.getSingleValueEntityAttributesByAttributeNames(attributesList,queryParameters);
                if(conceptionEntitiesAttributesRetrieveResult != null && conceptionEntitiesAttributesRetrieveResult.getOperationStatistics() != null){
                    showPopupNotification(conceptionEntitiesAttributesRetrieveResult,NotificationVariant.LUMO_SUCCESS);
                    Date startDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                    String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    Date finishDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                    String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                    dataCountDisplayItem.updateDisplayValue(""+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount());

                    List<ConceptionEntityValue> conceptionEntityValueList = conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();
                    for(int i=0 ; i<conceptionEntityValueList.size();i++){
                        ConceptionEntityValue currentConceptionEntityValue = conceptionEntityValueList.get(i);
                        currentConceptionEntityValue.getEntityAttributesValue().put(_rowIndexPropertyName,i+1);
                    }
                    if (resultAttributesList != null && resultAttributesList.size() > 0) {
                        for (String currentProperty : resultAttributesList) {
                            if (!currentProperty.equals(_rowIndexPropertyName)) {
                            queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
                                    @Override
                                    public Object apply(ConceptionEntityValue conceptionEntityValue) {
                                        return conceptionEntityValue.getEntityAttributesValue().get(currentProperty);
                                    }
                                }).setHeader(" " + currentProperty).setKey(currentProperty + "_KEY");
                                queryResultGrid.getColumnByKey(currentProperty + "_KEY").setSortable(true).setResizable(true);
                            }

                            this.currentRowKeyList.add(currentProperty + "_KEY");
                        }
                    }
                    queryResultGrid.setItems(conceptionEntityValueList);
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

    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntityValue,HorizontalLayout>{

        @Override
        public HorizontalLayout apply(ConceptionEntityValue o) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button linkButton = new Button();
            linkButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            linkButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            linkButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(linkButton, "显示概念实体详情");
            actionButtonContainerLayout.add(linkButton);

            Button infoButton = new Button();
            infoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            infoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            infoButton.setIcon(VaadinIcon.INBOX.create());
            Tooltips.getCurrent().setTooltip(infoButton, "加入待处理数据列表");
            actionButtonContainerLayout.add(infoButton);

            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR );
            deleteButton.setIcon(VaadinIcon.TRASH.create());
            Tooltips.getCurrent().setTooltip(deleteButton, "删除概念实体");
            actionButtonContainerLayout.add(deleteButton);

            return actionButtonContainerLayout;
        }
    }

    private void showPopupNotification(ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念类型 "+conceptionKindName+" 实例查询操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("查询返回实体数: "+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }
}