package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.queryDataSlice;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.dataCompute.dataComputeServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.analysis.query.filteringItem.FilteringItem;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceDetailInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.eventHandling.DataSliceQueriedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class DataSliceQueryCriteriaView extends VerticalLayout {
    private ComboBox<DataSlicePropertyDefinitionVO> queryCriteriaFilterSelect;
    private VerticalLayout criteriaItemsContainer;
    private Registration listener;
    private List<String> resultAttributesList;
    private QueryParameters queryParameters;
    private Binder<String> queryConditionDataBinder;
    private boolean defaultQueryConditionIsSet = true;
    private boolean otherQueryConditionsAreSet = false;
    private DataSliceMetaInfo dataSliceMetaInfo;

    private class DataSlicePropertyDefinitionVO{
        private String propertyName;
        private DataSlicePropertyType dataSlicePropertyType;
        private boolean isPrimaryKey;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public DataSlicePropertyType getDataSlicePropertyType() {
            return dataSlicePropertyType;
        }

        public void setDataSlicePropertyType(DataSlicePropertyType dataSlicePropertyType) {
            this.dataSlicePropertyType = dataSlicePropertyType;
        }

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            isPrimaryKey = primaryKey;
        }
    }

    public DataSliceQueryCriteriaView(DataSliceMetaInfo dataSliceMetaInfo) {
        this.dataSliceMetaInfo = dataSliceMetaInfo;
        this.resultAttributesList = new ArrayList<>();
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SEARCH),"查询条件");
        add(filterTitle1);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        VerticalLayout filterDropdownSelectorContainerLayout = new VerticalLayout();
        filterDropdownSelectorContainerLayout.setPadding(false);
        filterDropdownSelectorContainerLayout.setSpacing(false);
        filterDropdownSelectorContainerLayout.setMargin(false);
        add(filterDropdownSelectorContainerLayout);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定查询条件或显示属性");
        filterDropdownSelectorContainerLayout.add(infoTitle2);

        HorizontalLayout buttonSpaceDivLayout = new HorizontalLayout();
        buttonSpaceDivLayout.setWidth(99, Unit.PERCENTAGE);

        queryCriteriaFilterSelect = new ComboBox();
        queryCriteriaFilterSelect.setPageSize(30);
        queryCriteriaFilterSelect.setPlaceholder("选择查询条件或显示属性");
        queryCriteriaFilterSelect.setMinWidth(220,Unit.PIXELS);
        queryCriteriaFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);

        queryCriteriaFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<DataSlicePropertyDefinitionVO>() {
            @Override
            public String apply(DataSlicePropertyDefinitionVO DataSlicePropertyDefinitionVO) {
                String itemLabelValue = DataSlicePropertyDefinitionVO.getPropertyName()+ " ("+
                        DataSlicePropertyDefinitionVO.getDataSlicePropertyType()+")";
                return itemLabelValue;
            }
        });

        queryCriteriaFilterSelect.addValueChangeListener(new HasValue.
                ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<DataSlicePropertyDefinitionVO>,
                DataSlicePropertyDefinitionVO>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<DataSlicePropertyDefinitionVO>,
                    DataSlicePropertyDefinitionVO> comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent) {
                DataSlicePropertyDefinitionVO changedItem = comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent.getValue();
                if(changedItem != null){
                    queryCriteriaFilterSelect.setValue(null);
                    String selectedAttribute = changedItem.getPropertyName();
                    addQueryConditionItem(selectedAttribute,changedItem.getDataSlicePropertyType());


                }
            }
        });
        queryCriteriaFilterSelect.setRenderer(createRenderer());
        queryCriteriaFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");

        buttonSpaceDivLayout.add(queryCriteriaFilterSelect);
        buttonSpaceDivLayout.setFlexGrow(1,queryCriteriaFilterSelect);
        buttonSpaceDivLayout.setVerticalComponentAlignment(Alignment.CENTER,queryCriteriaFilterSelect);
        filterDropdownSelectorContainerLayout.add(buttonSpaceDivLayout);

        criteriaItemsContainer = new VerticalLayout();
        criteriaItemsContainer.setMargin(false);
        criteriaItemsContainer.setSpacing(false);
        criteriaItemsContainer.setPadding(false);
        criteriaItemsContainer.setWidth(100,Unit.PERCENTAGE);

        Scroller queryConditionItemsScroller = new Scroller(criteriaItemsContainer);
        queryConditionItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        //scroller.getStyle().set("padding", "var(--lumo-space-m)");
        add(queryConditionItemsScroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setMargin(false);
        buttonsContainerLayout.setSpacing(false);
        buttonsContainerLayout.setPadding(false);
        add(buttonsContainerLayout);

        Button executeQueryButton = new Button("查询数据记录");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryDataSliceRecords();
            }
        });
        buttonsContainerLayout.add(executeQueryButton);

        Button resultSetConfigButton = new Button("设置查询结果集参数",new Icon(VaadinIcon.COG_O));
        resultSetConfigButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        resultSetConfigButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderQueryResultSetConfigUI();
            }
        });
        resultSetConfigButton.getStyle()
                .set("padding-left","20px");
        buttonsContainerLayout.add(resultSetConfigButton);
        buttonsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,resultSetConfigButton);

        this.queryParameters = new QueryParameters();
        this.queryConditionDataBinder = new Binder<>();
    }

    private void queryDataSliceRecords(){
        queryParameters.getAndFilteringItemsList().clear();
        queryParameters.getOrFilteringItemsList().clear();
        queryParameters.setDefaultFilteringItem(null);

        setDefaultQueryConditionIsSet(true);
        setOtherQueryConditionsAreSet(false);

        criteriaItemsContainer.getChildren().forEach(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                DataSliceQueryConditionItemWidget currentQueryConditionItemWidget = (DataSliceQueryConditionItemWidget)component;

                FilteringItem currentFilteringItem = currentQueryConditionItemWidget.getFilteringItem();
                if(currentQueryConditionItemWidget.isDefaultQueryConditionItem() & currentFilteringItem == null){
                    setDefaultQueryConditionIsSet(false);
                }
                if(currentQueryConditionItemWidget.isDefaultQueryConditionItem()){
                    queryParameters.setDefaultFilteringItem(currentFilteringItem);
                }else{
                    if(currentFilteringItem != null){
                        QueryParameters.FilteringLogic currentFilteringLogic = currentQueryConditionItemWidget.getFilteringLogic();
                        queryParameters.addFilteringItem(currentFilteringItem, currentFilteringLogic);
                        setOtherQueryConditionsAreSet(true);
                    }else{
                        currentQueryConditionItemWidget.resetQueryConditionItem();
                    }
                }
            }
        });
        if(!defaultQueryConditionIsSet & otherQueryConditionsAreSet){
            CommonUIOperationUtil.showPopupNotification("请设定默认查询条件的属性过滤值", NotificationVariant.LUMO_ERROR);
        }else{
            DataSliceQueriedEvent dataSliceQueriedEvent = new DataSliceQueriedEvent();
            dataSliceQueriedEvent.setDataSliceName(this.dataSliceMetaInfo.getDataSliceName());
            dataSliceQueriedEvent.setQueryParameters(this.queryParameters);
            ResourceHolder.getApplicationBlackboard().fire(dataSliceQueriedEvent);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            criteriaItemsContainer.setHeight(event.getHeight()-250,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            criteriaItemsContainer.setHeight(browserHeight-250,Unit.PIXELS);
        }));
        loadQueryCriteriaComboBox();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void loadQueryCriteriaComboBox() {
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        try {
            DataSliceDetailInfo dataSliceDetailInfo = targetComputeGrid.getDataSliceDetail(this.dataSliceMetaInfo.getDataSliceName());
            if (dataSliceDetailInfo != null) {
                Map<String, DataSlicePropertyType> dataSlicePropertiesMap = dataSliceDetailInfo.getPropertiesDefinition();

                Set<String> primaryKeyPropertiesNames = dataSliceDetailInfo.getPrimaryKeyPropertiesNames();
                List<DataSlicePropertyDefinitionVO> dataSlicePropertyDefinitionVOList = new ArrayList<>();
                Set<String> propertyNameSet = dataSlicePropertiesMap.keySet();
                for (String currentName : propertyNameSet) {
                    DataSlicePropertyDefinitionVO currentDataSlicePropertyDefinitionVO = new DataSlicePropertyDefinitionVO();
                    currentDataSlicePropertyDefinitionVO.setPropertyName(currentName);
                    currentDataSlicePropertyDefinitionVO.setDataSlicePropertyType(dataSlicePropertiesMap.get(currentName));
                    if (primaryKeyPropertiesNames.contains(currentName)) {
                        currentDataSlicePropertyDefinitionVO.setPrimaryKey(true);
                    } else {
                        currentDataSlicePropertyDefinitionVO.setPrimaryKey(false);
                    }
                    dataSlicePropertyDefinitionVOList.add(currentDataSlicePropertyDefinitionVO);
                }

                queryCriteriaFilterSelect.setItems(dataSlicePropertyDefinitionVOList);
            }
        } catch (ComputeGridException e) {
            throw new RuntimeException(e);
        }
    }

    private Renderer<DataSlicePropertyDefinitionVO> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    ${item.attributeName}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeDataType}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<DataSlicePropertyDefinitionVO>of(tpl.toString())
                .withProperty("attributeName", DataSlicePropertyDefinitionVO::getPropertyName)
                .withProperty("attributeDataType", DataSlicePropertyDefinitionVO::getDataSlicePropertyType);
    }

    public void removeCriteriaFilterItem(DataSliceQueryConditionItemWidget queryConditionItemWidget){
        String removedAttributeName = queryConditionItemWidget.getPropertyName();
        boolean isDefaultCondition = queryConditionItemWidget.isDefaultQueryConditionItem();
        resultAttributesList.remove(removedAttributeName);
        criteriaItemsContainer.remove(queryConditionItemWidget);
        if(isDefaultCondition){
            boolean hasSecondItem = criteriaItemsContainer.getChildren().findFirst().isPresent();
            if(hasSecondItem){
                Component currentNewDefaultItem = criteriaItemsContainer.getChildren().findFirst().get();
                ((DataSliceQueryConditionItemWidget)currentNewDefaultItem).setAsDefaultQueryConditionItem();
            }
        }
    }

    public void addQueryConditionItem(String attributeName, DataSlicePropertyType attributeDataType){
        if(!resultAttributesList.contains(attributeName)){
            resultAttributesList.add(attributeName);
            DataSliceQueryConditionItemWidget dataSliceQueryConditionItemWidget = new DataSliceQueryConditionItemWidget(attributeName,attributeDataType,this.queryConditionDataBinder);
            dataSliceQueryConditionItemWidget.setContainerDataSliceQueryCriteriaView(this);
            if(resultAttributesList.size()==1){
                //this one is the default query condition
                dataSliceQueryConditionItemWidget.setAsDefaultQueryConditionItem();
            }
            criteriaItemsContainer.add(dataSliceQueryConditionItemWidget);
        }
    }

    private void setDefaultQueryConditionIsSet(boolean defaultQueryConditionIsSet) {
        this.defaultQueryConditionIsSet = defaultQueryConditionIsSet;
    }

    private void setOtherQueryConditionsAreSet(boolean otherQueryConditionsAreSet) {
        this.otherQueryConditionsAreSet = otherQueryConditionsAreSet;
    }

    private void renderQueryResultSetConfigUI(){
        DataSliceQueryResultSetConfigView dataSliceQueryResultSetConfigView = new DataSliceQueryResultSetConfigView(this.queryParameters);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"查询结果集参数",null,true,350,500,false);
        fixSizeWindow.setWindowContent(dataSliceQueryResultSetConfigView);
        fixSizeWindow.setModel(true);
        dataSliceQueryResultSetConfigView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }
}
