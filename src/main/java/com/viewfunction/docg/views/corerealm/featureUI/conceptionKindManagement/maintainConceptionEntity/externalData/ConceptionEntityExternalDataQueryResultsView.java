package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityExternalAttributesValueRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ConceptionEntityExternalDataQueryResultsView extends VerticalLayout {

    private String conceptionKindName;
    private String conceptionEntityUID;
    private AttributesViewKind attributesViewKind;
    private Registration listener;
    private Grid<Map<String,Object>> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
    private MenuBar queryResultOperationMenuBar;
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
        divIcon.setVisible(false);

        queryResultOperationMenuBar = new MenuBar();
        queryResultOperationMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        queryResultOperationMenuBar.getStyle().set("top","-5px").set("position","relative");
        queryResultOperationMenuBar.setEnabled(false);
        queryResultOperationMenuBar.setVisible(false);

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

        queryResultGrid = new Grid<>();
        queryResultGrid.setWidth(100, Unit.PERCENTAGE);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

        for (AttributeKind currentAttributeKind : this.attributesViewKind.getContainsAttributeKinds()) {
            String columnKey = "idx_" + currentAttributeKind.getAttributeKindUID();
            String columnName = currentAttributeKind.getAttributeKindName();
            MapValueProvider currentMapValueProvider =new MapValueProvider();
            currentMapValueProvider.setValueKey(columnName);
            queryResultGrid.addColumn(currentMapValueProvider).setHeader(columnName).setKey(columnKey).setResizable(true);
            GridColumnHeader gridColumnHeader_idx = new GridColumnHeader(LineAwesomeIconsSvg.CIRCLE.create(),columnName);
            queryResultGrid.getColumnByKey(columnKey).setHeader(gridColumnHeader_idx).setSortable(false);
        }
        add(queryResultGrid);
    }

    public void queryExternalValueAttributesViewData(QueryParameters queryParameters){
        queryResultOperationMenuBar.setEnabled(false);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKind.getAttributesViewKindUID());
        if(targetConceptionKind != null && targetAttributesViewKind != null){
            ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);

            if(targetConceptionEntity != null){
                try {
                    ConceptionEntityExternalAttributesValueRetrieveResult conceptionEntityExternalAttributesValueRetrieveResult
                            = targetConceptionEntity.getEntityExternalAttributesValues(targetAttributesViewKind,queryParameters);
                    if(conceptionEntityExternalAttributesValueRetrieveResult != null ) {
                        showPopupNotification(conceptionEntityExternalAttributesValueRetrieveResult, NotificationVariant.LUMO_SUCCESS);
                        Date startDateTime = conceptionEntityExternalAttributesValueRetrieveResult.getStartTime();
                        ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                        String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                        startTimeDisplayItem.updateDisplayValue(startTimeStr);
                        Date finishDateTime = conceptionEntityExternalAttributesValueRetrieveResult.getFinishTime();
                        ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                        String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                        finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                        dataCountDisplayItem.updateDisplayValue("" + numberFormat.format(conceptionEntityExternalAttributesValueRetrieveResult.getResultRowsCount()));

                        List<Map<String,Object>> externalDataRecords = conceptionEntityExternalAttributesValueRetrieveResult.getExternalAttributesValue();
                        queryResultGrid.setItems(externalDataRecords);
                        queryResultOperationMenuBar.setEnabled(true);
                    }
                } catch (CoreRealmServiceEntityExploreException e) {
                    Notification notification = new Notification();
                    Div text = new Div(new Text("概念实体 "+this.conceptionEntityUID+" 外部属性视图数据查询操作错误: " + e.getCause().getMessage()));
                    Button closeButton = new Button(new Icon("lumo", "cross"));
                    closeButton.addClickListener(event -> {
                        notification.close();
                    });

                    HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                    layout.setWidth(100, Unit.PERCENTAGE);
                    layout.setFlexGrow(1,text);
                    notification.add(layout);
                    notification.setPosition(Notification.Position.BOTTOM_START);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                    throw new RuntimeException(e);
                }
            }
        }
        coreRealm.closeGlobalSession();
    }

    private class MapValueProvider implements ValueProvider<Map<String,Object>,Object>{
        private String valueKey;

        public void setValueKey(String valueKey) {
            this.valueKey = valueKey;
        }

        public String getValueKey() {
            return valueKey;
        }

        @Override
        public Object apply(Map<String, Object> stringObjectMap) {
            Object resultObject = stringObjectMap.getOrDefault(valueKey, null);
            return resultObject;
        }
    }

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
        super.onDetach(detachEvent);
    }

    private void showPopupNotification(ConceptionEntityExternalAttributesValueRetrieveResult conceptionEntityExternalAttributesValueRetrieveResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念实体 "+this.conceptionEntityUID+" 外部属性视图数据查询操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("查询返回数据数: "+conceptionEntityExternalAttributesValueRetrieveResult.getResultRowsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+conceptionEntityExternalAttributesValueRetrieveResult.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+conceptionEntityExternalAttributesValueRetrieveResult.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }

    private void exportCSVQueryResult(){
        /*
        DownloadCSVFormatQueryResultsView downloadCSVFormatQueryResultsView = new DownloadCSVFormatQueryResultsView(this.conceptionKindName,this.lastConceptionEntitiesAttributesRetrieveResult,this.lastQueryAttributesList,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 CSV 格式概念类型实体数据查询结果",null,true,550,290,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadCSVFormatQueryResultsView);
        fixSizeWindow.setModel(true);
        downloadCSVFormatQueryResultsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
        */
    }

    private void exportExcelQueryResult(){
        /*
        DownloadExcelFormatQueryResultsView downloadExcelFormatQueryResultsView = new DownloadExcelFormatQueryResultsView(this.conceptionKindName,this.lastConceptionEntitiesAttributesRetrieveResult,this.lastQueryAttributesList,500);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.DOWNLOAD),"导出 EXCEL 格式概念类型实体数据查询结果",null,true,550,290,false);
        fixSizeWindow.disableCloseButton();
        fixSizeWindow.setWindowContent(downloadExcelFormatQueryResultsView);
        fixSizeWindow.setModel(true);
        downloadExcelFormatQueryResultsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
        */
    }
}
