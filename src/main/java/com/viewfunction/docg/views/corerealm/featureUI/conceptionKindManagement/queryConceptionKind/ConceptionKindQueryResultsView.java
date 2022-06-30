package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ConceptionKindQueryResultsView extends VerticalLayout implements
        ConceptionKindQueriedEvent.ConceptionKindQueriedListener {
    private String conceptionKindName;
    private Registration listener;
    private Grid<Map<String, Object>> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
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


        //queryResultGrid.addColumn("EntityStatisticsInfo::getEntityKindName").setHeader("概念类型名称").setKey("idx_0");
        add(queryResultGrid);
    }

    @Override
    public void receivedConceptionKindQueriedEvent(ConceptionKindQueriedEvent event) {
        String conceptionKindName = event.getConceptionKindName();
        if(conceptionKindName.equals(this.conceptionKindName)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKindName);
            QueryParameters queryParameters = new QueryParameters();
            try {
                List<String> attributesList = new ArrayList<>();
                attributesList.add(RealmConstant._createDateProperty);
                attributesList.add(RealmConstant._lastModifyDateProperty);
                attributesList.add(RealmConstant._creatorIdProperty);
                attributesList.add(RealmConstant._dataOriginProperty);
                ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult =
                        targetConception.getSingleValueEntityAttributesByAttributeNames(attributesList,queryParameters);
                if(conceptionEntitiesAttributesRetrieveResult != null && conceptionEntitiesAttributesRetrieveResult.getOperationStatistics() != null){
                    Date startDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                    String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    Date finishDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                    String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                    dataCountDisplayItem.updateDisplayValue(""+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount());
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





    public class MyDataProvider<T> extends ListDataProvider<T> {

        public MyDataProvider(Collection<T> items) {
            super(items);
        }

        @Override
        public String getId(T item) {
            Objects.requireNonNull(item,
                    "Cannot provide an id for a null item.");
            if (item instanceof Map<?, ?>) {
                if (((Map<String, ?>) item).get("id") != null)
                    return ((Map<String, ?>) item).get("id").toString();
                else
                    return item.toString();
            } else {
                return item.toString();
            }
        }

    }








}
/*   https://cookbook.vaadin.com/grid-with-map
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

@Route("grid-with-map")
@CssImport(value = "./recipe/mapingridandbinder/mapingridandbinder.css", themeFor = "vaadin-grid")
public class MapInGridAndBinder extends VerticalLayout {

    public MapInGridAndBinder() {
        setHeight("500px");
        IntegerField field = new IntegerField("Columns ");
        field.setMin(5);
        field.setMax(25);
        field.setHasControls(true);
        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();
        add(field, container);

        field.addValueChangeListener(event -> {
            Integer value = event.getValue();
            container.removeAll();
            Grid<Map<String, Integer>> grid = createGrid(value);
            grid.setSizeFull();
            container.add(grid);
        });

        field.setValue(10);
    }

    private Grid<Map<String, Integer>> createGrid(int columns) {
        // Instantiate Grid with Map type
        Grid<Map<String, Integer>> grid = new Grid<>();

        List<Map<String, Integer>> items = createData(columns);
        MyDataProvider<Map<String, Integer>> dataProvider = new MyDataProvider<>(
                items);
        grid.setItems(dataProvider);

        // Instantiate Binder with Map type
        Binder<Map<String, Integer>> binder = new Binder<>();

        grid.addColumn(map -> map.get("id")).setHeader("Id");

        // Generate requested amount of columns and setup Binder for edit fields
        for (int i = 0; i < columns; i++) {
            final int index = i;
            IntegerField integerField = new IntegerField();
            binder.forField(integerField).bind(map -> map.get("col" + index),
                    (map, value) -> map.put("col" + index, value));
            grid.addColumn(map -> map.get("col" + index))
                    .setEditorComponent(integerField).setHeader("Col " + index);
            // Use small variant for the field in order to edit row look nicer
            integerField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
            integerField.addKeyDownListener(Key.ENTER, event -> {
                grid.getEditor().closeEditor();
            });
            integerField.addKeyDownListener(Key.ESCAPE, event -> {
                grid.getEditor().closeEditor();
            });
        }
        grid.getEditor().setBinder(binder);

        // Add column for the sum
        grid.addColumn(map -> map.get("sum")).setHeader("Sum")
                .setClassNameGenerator(item -> "sum");

        // Tip: Lets use item click listener as toggle for editing
        grid.addItemClickListener(event -> {
            Map<String, Integer> item = event.getItem();
            // If editor is open already, lets close it
            if (grid.getEditor().isOpen()
                    && grid.getEditor().getItem().equals(item)) {
                grid.getEditor().closeEditor();
            } else {
                grid.getEditor().editItem(item);
                // Tip: Find the field from the colum and focus it for better UX
                IntegerField field = (IntegerField) event.getColumn()
                        .getEditorComponent();
                field.focus();
            }
        });

        // Update sum when editor is closed
        grid.getEditor().addCloseListener(event -> {
            Map<String, Integer> item = event.getItem();
            int sum = 0;
            for (int i = 0; i < columns; i++) {
                sum += item.get("col" + i);
            }
            item.put("sum", sum);
            // Item refrsh is needed in order to new sum be visible
            grid.getDataProvider().refreshItem(item);
        });

        // Tip: Grid uses these values for padding, lets set them smaller value
        // so that editor will look nicer.
        grid.getStyle().set("--lumo-space-xs", "1px");
        grid.getStyle().set("--lumo-space-m", "1px");
        return grid;
    }

    // Generate some mock data
    private List<Map<String, Integer>> createData(int columns) {
        Random random = new Random();
        List<Map<String, Integer>> items = new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            final Map<String, Integer> values = new HashMap<>();
            values.put("id", j);
            int sum = 0;
            for (int i = 0; i < columns; i++) {
                int number = random.nextInt(10000);
                values.put("col" + i, number);
                sum = sum + number;
            }
            values.put("sum", sum);
            items.add(values);
        }
        return items;
    }

    // HashMap identity does not play nice with Grid by default, thus
    // we use option to override getId method of DataProvider to return value
    // of "id" key as identity.
    public class MyDataProvider<T> extends ListDataProvider<T> {

        public MyDataProvider(Collection<T> items) {
            super(items);
        }

        @Override
        public String getId(T item) {
            Objects.requireNonNull(item,
                    "Cannot provide an id for a null item.");
            if (item instanceof Map<?, ?>) {
                if (((Map<String, ?>) item).get("id") != null)
                    return ((Map<String, ?>) item).get("id").toString();
                else
                    return item.toString();
            } else {
                return item.toString();
            }
        }

    }
}
* */