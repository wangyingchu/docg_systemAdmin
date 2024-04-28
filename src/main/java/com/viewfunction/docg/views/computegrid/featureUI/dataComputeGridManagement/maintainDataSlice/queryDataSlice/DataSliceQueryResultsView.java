package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.queryDataSlice;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.dataCompute.computeServiceCore.internal.ignite.ComputeGridObserver;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.DataSliceQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.ConceptionKindQueryResultsView;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DataSliceQueryResultsView extends VerticalLayout implements DataSliceQueriedEvent.DataSliceQueriedListener {

    private DataSliceMetaInfo dataSliceMetaInfo;
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
        event.getDataSliceName();
        event.getResultPropertiesList();
        event.getQueryParameters();

        ComputeGridObserver currentComputeGridObserver = ComputeGridObserver.getObserverInstance();
       // currentComputeGridObserver.



        System.out.println(event);
        System.out.println(event);
        System.out.println(event);
    }
}
