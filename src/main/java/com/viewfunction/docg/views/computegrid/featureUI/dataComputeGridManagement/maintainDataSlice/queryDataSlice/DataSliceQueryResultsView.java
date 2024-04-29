package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.queryDataSlice;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.dataCompute.applicationCapacity.dataCompute.dataComputeUnit.dataService.DataServiceInvoker;
import com.viewfunction.docg.dataCompute.applicationCapacity.dataCompute.dataComputeUnit.dataService.DataSlice;
import com.viewfunction.docg.dataCompute.computeServiceCore.internal.ignite.exception.ComputeGridNotActiveException;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceQueryResult;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.eventHandling.DataSliceQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataSliceQueryResultsView extends VerticalLayout implements DataSliceQueriedEvent.DataSliceQueriedListener {

    private DataSliceMetaInfo dataSliceMetaInfo;
    private Registration listener;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
    private final String _rowIndexPropertyName = "ROW_INDEX";
    private List<String> currentRowKeyList;
    private  List<String> lastQueryAttributesList;
    private NumberFormat numberFormat;

    public DataSliceQueryResultsView(DataSliceMetaInfo dataSliceMetaInfo){
        this.dataSliceMetaInfo = dataSliceMetaInfo;
        this.setPadding(true);
        this.setSpacing(true);

        HorizontalLayout toolbarLayout = new HorizontalLayout();
        add(toolbarLayout);
        HorizontalLayout titleLayout = new HorizontalLayout();
        toolbarLayout.add(titleLayout);
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
        queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
            @Override
            public Object apply(ConceptionEntityValue conceptionEntityValue) {
                return conceptionEntityValue.getEntityAttributesValue().get(_rowIndexPropertyName);
            }
        }).setHeader("").setHeader("IDX").setKey("idx").setFlexGrow(0).setWidth("75px").setResizable(false);
        //queryResultGrid.addComponentColumn(new ConceptionKindQueryResultsView.ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_0").setFlexGrow(0).setWidth("120px").setResizable(false);
        queryResultGrid.addColumn(ConceptionEntityValue::getConceptionEntityUID).setHeader(" EntityUID").setKey("idx_1").setFlexGrow(1).setWidth("150px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx = new GridColumnHeader(VaadinIcon.LIST_OL,"");
        queryResultGrid.getColumnByKey("idx").setHeader(gridColumnHeader_idx).setSortable(false);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        //queryResultGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        queryResultGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx0).setSortable(false).setResizable(true);
        add(queryResultGrid);

        queryResultGrid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<ConceptionEntityValue>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<ConceptionEntityValue> conceptionEntityValueItemDoubleClickEvent) {
                ConceptionEntityValue targetConceptionEntityValue = conceptionEntityValueItemDoubleClickEvent.getItem();
                if(targetConceptionEntityValue!= null){
                    //renderConceptionEntityUI(targetConceptionEntityValue);
                }
            }
        });

        this.currentRowKeyList = new ArrayList<>();
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
    public void receivedDataSliceQueriedEvent(DataSliceQueriedEvent event) {
        for(String currentExistingRowKey:this.currentRowKeyList){
            queryResultGrid.removeColumnByKey(currentExistingRowKey);
        }
        queryResultGrid.setItems(new ArrayList<>());
        this.currentRowKeyList.clear();
        this.lastQueryAttributesList = null;
        try(DataServiceInvoker dataServiceInvoker = DataServiceInvoker.getInvokerInstance()){

            DataSlice targetDataSlice = dataServiceInvoker.getDataSlice(event.getDataSliceName());
            if(targetDataSlice != null){
                //DataSliceQueryResult dataSliceQueryResult = targetDataSlice.queryDataRecords(event.getQueryParameters());

                DataSliceMetaInfo dataSliceMetaInfo = targetDataSlice.getDataSliceMetaInfo();

                DataSliceQueryResult dataSliceQueryResult = targetDataSlice.queryDataRecords("SELECT * FROM "+event.getDataSliceName());
                if(dataSliceQueryResult != null){
                    List<Map<String, Object>> recordsList = dataSliceQueryResult.getResultRecords();
                    showPopupNotification(dataSliceQueryResult, NotificationVariant.LUMO_SUCCESS);
                    Date startDateTime = dataSliceQueryResult.getStartTime();
                    ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                    String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    Date finishDateTime = dataSliceQueryResult.getFinishTime();
                    ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                    String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                    dataCountDisplayItem.updateDisplayValue(""+   numberFormat.format(recordsList.size()));
                }
            }
        } catch (ComputeGridNotActiveException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showPopupNotification(DataSliceQueryResult dataSliceQueryResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念类型 "+" 实例查询操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("查询返回实体数: "+dataSliceQueryResult.getResultRecords().size())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+dataSliceQueryResult.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+dataSliceQueryResult.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }
}
