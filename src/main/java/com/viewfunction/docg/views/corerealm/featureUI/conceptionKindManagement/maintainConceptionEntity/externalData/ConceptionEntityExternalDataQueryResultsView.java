package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityDeletedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis.ConceptionEntitiesGeospatialInfoAnalysisView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.AddConceptionEntityToProcessingListView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.DeleteConceptionEntityView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.ConceptionKindQueryResultsView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.DownloadArrowFormatQueryResultsView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.DownloadCSVFormatQueryResultsView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.DownloadExcelFormatQueryResultsView;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ConceptionEntityExternalDataQueryResultsView extends VerticalLayout {

    private String conceptionKindName;
    private String conceptionEntityUID;
    private AttributesViewKind attributesViewKind;
    private Registration listener;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
    private final String _rowIndexPropertyName = "ROW_INDEX";
    private MenuBar queryResultOperationMenuBar;
    private List<String> currentRowKeyList;
    private ConceptionEntitiesAttributesRetrieveResult lastConceptionEntitiesAttributesRetrieveResult;
    private  List<String> lastQueryAttributesList;
    private NumberFormat numberFormat;

    private int conceptionEntityExternalDataViewHeightOffset;

    public ConceptionEntityExternalDataQueryResultsView(String conceptionKindName,String conceptionEntityUID,AttributesViewKind attributesViewKind,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.attributesViewKind = attributesViewKind;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;
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

        HorizontalLayout actionButtonLayout = new HorizontalLayout();
        toolbarLayout.add(actionButtonLayout);

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("12px");
        divIcon.getStyle().set("top","-5px").set("position","relative");
        actionButtonLayout.add(divIcon);
        actionButtonLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        queryResultOperationMenuBar = new MenuBar();
        queryResultOperationMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        queryResultOperationMenuBar.getStyle().set("top","-5px").set("position","relative");
        queryResultOperationMenuBar.setEnabled(false);

        actionButtonLayout.add(queryResultOperationMenuBar);
        actionButtonLayout.setVerticalComponentAlignment(Alignment.START,queryResultOperationMenuBar);

        Icon analysisMenuIcon = VaadinIcon.AUTOMATION.create();
        analysisMenuIcon.setSize("16px");
        MenuItem analysisMenuItem = queryResultOperationMenuBar.addItem(analysisMenuIcon);
        analysisMenuItem.add("查询结果数据处理");
        Icon downArrowIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon.setSize("12px");
        analysisMenuItem.add(downArrowIcon);
        analysisMenuItem.getStyle().set("font-size","1em");

        SubMenu subMenu = analysisMenuItem.getSubMenu();
        MenuItem exportDataSubMenu = subMenu.addItem(VaadinIcon.DOWNLOAD.create());
        exportDataSubMenu.add(" 数据导出");
        MenuItem exportCSVItem = exportDataSubMenu.getSubMenu().addItem("导出 CSV 格式查询结果");
        exportCSVItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                exportCSVQueryResult();
            }
        });

        MenuItem exportExcelItem = exportDataSubMenu.getSubMenu().addItem("导出 EXCEL 格式查询结果");
        exportExcelItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                exportExcelQueryResult();
            }
        });

        MenuItem analyzeDataSubMenu = subMenu.addItem(LineAwesomeIconsSvg.BONG_SOLID.create());
        analyzeDataSubMenu.add(" 数据分析");

        MenuItem displayGISInfo = analyzeDataSubMenu.getSubMenu().addItem(VaadinIcon.GLOBE.create());
        displayGISInfo.add("随机抽样显示地理空间属性");

        MenuItem globalGISInfo = displayGISInfo.getSubMenu().addItem(VaadinIcon.GLOBE_WIRE.create());
        globalGISInfo.add("全球坐标系地理空间信息");

        MenuItem countryGISInfo = displayGISInfo.getSubMenu().addItem(VaadinIcon.LOCATION_ARROW_CIRCLE.create());
        countryGISInfo.add("国家坐标系地理空间信息");

        MenuItem localGISInfo = displayGISInfo.getSubMenu().addItem(VaadinIcon.HOME.create());
        localGISInfo.add("本地坐标系地理空间信息");

        globalGISInfo.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                showEntitiesGISInfo(GeospatialScaleCalculable.SpatialScaleLevel.Global);
            }
        });

        countryGISInfo.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                showEntitiesGISInfo(GeospatialScaleCalculable.SpatialScaleLevel.Country);
            }
        });

        localGISInfo.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                showEntitiesGISInfo(GeospatialScaleCalculable.SpatialScaleLevel.Local);
            }
        });

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
        queryResultGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_0").setFlexGrow(0).setWidth("120px").setResizable(false);


        queryResultGrid.addColumn(ConceptionEntityValue::getConceptionEntityUID).setHeader(" EntityUID").setKey("idx_1").setFlexGrow(1).setWidth("150px").setResizable(false);




        for (AttributeKind currentAttributeKind : this.attributesViewKind.getContainsAttributeKinds()) {
            queryResultGrid.addColumn(ConceptionEntityValue::getConceptionEntityUID).setHeader(" "+currentAttributeKind.getAttributeKindName()).setKey("idx_"+currentAttributeKind.getAttributeKindUID()).setResizable(true);
        }






        GridColumnHeader gridColumnHeader_idx = new GridColumnHeader(VaadinIcon.LIST_OL,"");
        queryResultGrid.getColumnByKey("idx").setHeader(gridColumnHeader_idx).setSortable(false);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.WRENCH,"操作");
        queryResultGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        queryResultGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx0).setSortable(false).setResizable(true);
        add(queryResultGrid);

        this.currentRowKeyList = new ArrayList<>();
    }



    public void queryExternalValueAttributesViewData(QueryParameters queryParameters){}




    /*
    @Override
    public void receivedConceptionKindQueriedEvent(ConceptionKindQueriedEvent event) {
        for(String currentExistingRowKey:this.currentRowKeyList){
            queryResultGrid.removeColumnByKey(currentExistingRowKey);
        }
        queryResultGrid.setItems(new ArrayList<>());
        this.currentRowKeyList.clear();
        this.lastQueryAttributesList = null;
        String conceptionKindName = event.getConceptionKindName();
        List<String> resultAttributesList = event.getResultAttributesList();
        QueryParameters eventQueryParameters = event.getQueryParameters();
        if(conceptionKindName.equals(this.conceptionKindName)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKindName);
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

                this.lastQueryAttributesList = attributesList;

                ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult =
                        targetConception.getSingleValueEntityAttributesByAttributeNames(attributesList,queryParameters);
                if(conceptionEntitiesAttributesRetrieveResult != null && conceptionEntitiesAttributesRetrieveResult.getOperationStatistics() != null){
                    showPopupNotification(conceptionEntitiesAttributesRetrieveResult, NotificationVariant.LUMO_SUCCESS);
                    Date startDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                    String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    Date finishDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getFinishTime();
                    ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                    String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                    dataCountDisplayItem.updateDisplayValue(""+   numberFormat.format(conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount()));

                    List<ConceptionEntityValue> conceptionEntityValueList = conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();
                    for(int i=0 ; i<conceptionEntityValueList.size();i++){
                        ConceptionEntityValue currentConceptionEntityValue = conceptionEntityValueList.get(i);
                        currentConceptionEntityValue.getEntityAttributesValue().put(_rowIndexPropertyName,i+1);
                    }
                    if (attributesList != null && attributesList.size() > 0) {
                        for (String currentProperty : attributesList) {
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
                    queryResultOperationMenuBar.setEnabled(true);

                    lastConceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }else{
            lastConceptionEntitiesAttributesRetrieveResult = null;
        }
    }
*/
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryResultGrid.setHeight(event.getHeight()- conceptionEntityExternalDataViewHeightOffset + 5,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryResultGrid.setHeight(browserHeight- conceptionEntityExternalDataViewHeightOffset + 5,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        super.onDetach(detachEvent);
    }

    /*
    @Override
    public void receivedConceptionEntityDeletedEvent(ConceptionEntityDeletedEvent event) {
        List<String> deletedEntityAllConceptionKinds = event.getEntityAllConceptionKindNames();
        if(deletedEntityAllConceptionKinds.contains(conceptionKindName)){
            String conceptionEntityUID = event.getConceptionEntityUID();
            ListDataProvider dataProvider=(ListDataProvider)queryResultGrid.getDataProvider();
            ConceptionEntityValue conceptionEntityValueToDelete = null;
            Collection<ConceptionEntityValue> conceptionEntityValueList = dataProvider.getItems();
            for(ConceptionEntityValue currentConceptionEntityValue:conceptionEntityValueList){
                if(currentConceptionEntityValue.getConceptionEntityUID().equals(conceptionEntityUID)){
                    conceptionEntityValueToDelete = currentConceptionEntityValue;
                    break;
                }
            }
            if(conceptionEntityValueToDelete != null){
                dataProvider.getItems().remove(conceptionEntityValueToDelete);
                dataProvider.refreshAll();
            }
        }
    }
    */
    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntityValue,HorizontalLayout>{
        @Override
        public HorizontalLayout apply(ConceptionEntityValue conceptionEntityValue) {
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
                        //renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });

            Button addToProcessListButton = new Button();
            addToProcessListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addToProcessListButton.setIcon(VaadinIcon.INBOX.create());
            addToProcessListButton.setTooltipText("加入待处理数据列表");
            actionButtonContainerLayout.add(addToProcessListButton);
            addToProcessListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        //addConceptionEntityToProcessingList(conceptionEntityValue);
                    }
                }
            });

            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR );
            deleteButton.setIcon(VaadinIcon.TRASH.create());
            deleteButton.setTooltipText("删除概念实体");
            actionButtonContainerLayout.add(deleteButton);
            deleteButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        //deleteConceptionEntity(conceptionEntityValue);
                    }
                }
            });
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



    private void exportCSVQueryResult(){
        DownloadCSVFormatQueryResultsView downloadCSVFormatQueryResultsView = new DownloadCSVFormatQueryResultsView(this.conceptionKindName,this.lastConceptionEntitiesAttributesRetrieveResult,this.lastQueryAttributesList,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 CSV 格式概念类型实体数据查询结果",null,true,550,290,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadCSVFormatQueryResultsView);
        fixSizeWindow.setModel(true);
        downloadCSVFormatQueryResultsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void exportArrowQueryResult(){
        DownloadArrowFormatQueryResultsView downloadArrowFormatQueryResultsView = new DownloadArrowFormatQueryResultsView(this.conceptionKindName,this.lastConceptionEntitiesAttributesRetrieveResult,this.lastQueryAttributesList,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 ARROW 格式概念类型实体数据查询结果",null,true,550,290,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadArrowFormatQueryResultsView);
        fixSizeWindow.setModel(true);
        downloadArrowFormatQueryResultsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void exportExcelQueryResult(){
        DownloadExcelFormatQueryResultsView downloadExcelFormatQueryResultsView = new DownloadExcelFormatQueryResultsView(this.conceptionKindName,this.lastConceptionEntitiesAttributesRetrieveResult,this.lastQueryAttributesList,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 EXCEL 格式概念类型实体数据查询结果",null,true,550,290,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadExcelFormatQueryResultsView);
        fixSizeWindow.setModel(true);
        downloadExcelFormatQueryResultsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void showEntitiesGISInfo(GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel){
        Icon windowTitleIcon = null;
        String windowTitleTxt = this.conceptionKindName + " 概念实体 ("+ this.numberFormat.format(this.lastConceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues().size())+") ";
        switch(spatialScaleLevel){
            case Global :
                windowTitleIcon = VaadinIcon.GLOBE_WIRE.create();
                windowTitleTxt = windowTitleTxt + "全球坐标系空间数据展示";
                break;
            case Country:
                windowTitleIcon = VaadinIcon.LOCATION_ARROW_CIRCLE.create();
                windowTitleTxt = windowTitleTxt + "国家坐标系空间数据展示";
                break;
            case Local:
                windowTitleIcon = VaadinIcon.HOME.create();
                windowTitleTxt = windowTitleTxt + "本地坐标系空间数据展示";
                break;
        }

        FullScreenWindow fullScreenWindow = new FullScreenWindow(windowTitleIcon,windowTitleTxt,null,null,true);
        ConceptionEntitiesGeospatialInfoAnalysisView conceptionEntitiesGeospatialInfoAnalysisView = new ConceptionEntitiesGeospatialInfoAnalysisView(this.conceptionKindName,spatialScaleLevel,this.lastConceptionEntitiesAttributesRetrieveResult);
        fullScreenWindow.setWindowContent(conceptionEntitiesGeospatialInfoAnalysisView);
        fullScreenWindow.show();
    }

}
