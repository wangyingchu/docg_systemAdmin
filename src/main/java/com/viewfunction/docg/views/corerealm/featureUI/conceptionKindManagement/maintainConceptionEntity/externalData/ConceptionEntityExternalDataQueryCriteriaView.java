package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData;

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
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.filteringItem.FilteringItem;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.KindQueryCriteriaView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.QueryConditionItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.QueryResultSetConfigView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConceptionEntityExternalDataQueryCriteriaView extends VerticalLayout implements KindQueryCriteriaView {

    private String conceptionKindName;
    private ComboBox<AttributeKind> queryCriteriaFilterSelect;
    private VerticalLayout criteriaItemsContainer;
    private Registration listener;
    private List<String> resultAttributesList;
    private QueryParameters queryParameters;
    private Binder<String> queryConditionDataBinder;
    private boolean defaultQueryConditionIsSet = true;
    private boolean otherQueryConditionsAreSet = false;
    private AttributesViewKind attributesViewKind;
    private int conceptionEntityExternalDataViewHeightOffset;
    private Button resultSetConfigButton;
    private Popover resultSetConfigButtonPopover;
    private ExternalValueAttributeDataAccessView containerExternalValueAttributeDataAccessView;

    public ConceptionEntityExternalDataQueryCriteriaView(String conceptionKindName, AttributesViewKind attributesViewKind,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKindName = conceptionKindName;
        this.attributesViewKind = attributesViewKind;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;

        VerticalLayout filterDropdownSelectorContainerLayout = new VerticalLayout();
        filterDropdownSelectorContainerLayout.setPadding(false);
        filterDropdownSelectorContainerLayout.setSpacing(false);
        filterDropdownSelectorContainerLayout.setMargin(false);
        add(filterDropdownSelectorContainerLayout);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定查询条件");
        filterDropdownSelectorContainerLayout.add(infoTitle2);

        HorizontalLayout buttonSpaceDivLayout = new HorizontalLayout();
        buttonSpaceDivLayout.setWidth(99, Unit.PERCENTAGE);

        resultAttributesList = new ArrayList<>();

        queryCriteriaFilterSelect = new ComboBox();
        queryCriteriaFilterSelect.setPageSize(30);
        queryCriteriaFilterSelect.setPlaceholder("选择查询条件");
        queryCriteriaFilterSelect.setMinWidth(220,Unit.PIXELS);
        queryCriteriaFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);

        queryCriteriaFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<AttributeKind>() {
            @Override
            public String apply(AttributeKind attributeKind) {
                String itemLabelValue = attributeKind.getAttributeKindName()+ " ("+
                        attributeKind.getAttributeDataType().toString()+")";
                return itemLabelValue;
            }
        });

        queryCriteriaFilterSelect.addValueChangeListener(new HasValue.
                ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<AttributeKind>,
                AttributeKind>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<AttributeKind>,
                    AttributeKind> comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent) {
                AttributeKind changedItem = comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent.getValue();
                if(changedItem != null){
                    queryCriteriaFilterSelect.setValue(null);
                    String selectedAttribute = changedItem.getAttributeKindName();
                    addQueryConditionItem(selectedAttribute,changedItem.getAttributeDataType());
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

        Button executeQueryButton = new Button("查询外部属性");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryConceptionEntities();
            }
        });
        buttonsContainerLayout.add(executeQueryButton);

        resultSetConfigButton = new Button("设置查询结果集参数",new Icon(VaadinIcon.COG_O));
        resultSetConfigButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        resultSetConfigButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                QueryResultSetConfigView queryResultSetConfigView = new QueryResultSetConfigView(queryParameters);
                queryResultSetConfigView.setContainerPopover(resultSetConfigButtonPopover);
                resultSetConfigButtonPopover.removeAll();
                resultSetConfigButtonPopover.add(queryResultSetConfigView);
                resultSetConfigButtonPopover.open();
            }
        });
        resultSetConfigButton.getStyle()
                .set("padding-left","20px");
        buttonsContainerLayout.add(resultSetConfigButton);
        buttonsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,resultSetConfigButton);

        this.queryParameters = new QueryParameters();
        this.queryConditionDataBinder = new Binder<>();

        resultSetConfigButtonPopover = new Popover();
        resultSetConfigButtonPopover.setTarget(resultSetConfigButton);
        resultSetConfigButtonPopover.setWidth("350px");
        resultSetConfigButtonPopover.setHeight("440px");
        resultSetConfigButtonPopover.addThemeVariants(PopoverVariant.ARROW);
        resultSetConfigButtonPopover.setPosition(PopoverPosition.TOP_START);
        resultSetConfigButtonPopover.setAutofocus(true);
        resultSetConfigButtonPopover.setModal(true,true);
    }

    private void queryConceptionEntities(){
        queryParameters.getAndFilteringItemsList().clear();
        queryParameters.getOrFilteringItemsList().clear();
        queryParameters.setDefaultFilteringItem(null);
        setDefaultQueryConditionIsSet(true);
        setOtherQueryConditionsAreSet(false);

        criteriaItemsContainer.getChildren().forEach(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                QueryConditionItemWidget currentQueryConditionItemWidget = (QueryConditionItemWidget)component;
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
            if(containerExternalValueAttributeDataAccessView != null){
                containerExternalValueAttributeDataAccessView.queryExternalValueAttributesViewData(this.queryParameters);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            criteriaItemsContainer.setMaxHeight(event.getHeight() - conceptionEntityExternalDataViewHeightOffset - 70 ,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            criteriaItemsContainer.setMaxHeight(browserHeight - conceptionEntityExternalDataViewHeightOffset - 70,Unit.PIXELS);
        }));
        loadQueryCriteriaComboBox();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void loadQueryCriteriaComboBox(){
        List<AttributeKind> attributeKindList = this.attributesViewKind.getContainsAttributeKinds();
        queryCriteriaFilterSelect.setItems(attributeKindList);
    }

    private Renderer<AttributeKind> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    ${item.attributeName}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeDataType}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<AttributeKind>of(tpl.toString())
                .withProperty("attributeName", AttributeKind::getAttributeKindName)
                .withProperty("attributeDataType", AttributeKind::getAttributeDataType);
    }

    @Override
    public void removeCriteriaFilterItem(QueryConditionItemWidget queryConditionItemWidget){
        String removedAttributeName = queryConditionItemWidget.getAttributeName();
        boolean isDefaultCondition = queryConditionItemWidget.isDefaultQueryConditionItem();
        resultAttributesList.remove(removedAttributeName);
        criteriaItemsContainer.remove(queryConditionItemWidget);
        if(isDefaultCondition){
            boolean hasSecondItem = criteriaItemsContainer.getChildren().findFirst().isPresent();
            if(hasSecondItem){
                Component currentNewDefaultItem = criteriaItemsContainer.getChildren().findFirst().get();
                ((QueryConditionItemWidget)currentNewDefaultItem).setAsDefaultQueryConditionItem();
            }
        }
    }

    @Override
    public void addQueryConditionItem(String attributeName, AttributeDataType attributeDataType){
        if(!resultAttributesList.contains(attributeName)){
            resultAttributesList.add(attributeName);
            QueryConditionItemWidget queryConditionItemWidget = new QueryConditionItemWidget(attributeName,attributeDataType,this.queryConditionDataBinder);
            queryConditionItemWidget.setContainerDataInstanceQueryCriteriaView(this);
            if(resultAttributesList.size()==1){
                //this one is the default query condition
                queryConditionItemWidget.setAsDefaultQueryConditionItem();
            }
            criteriaItemsContainer.add(queryConditionItemWidget);
        }
    }

    private void setDefaultQueryConditionIsSet(boolean defaultQueryConditionIsSet) {
        this.defaultQueryConditionIsSet = defaultQueryConditionIsSet;
    }

    private void setOtherQueryConditionsAreSet(boolean otherQueryConditionsAreSet) {
        this.otherQueryConditionsAreSet = otherQueryConditionsAreSet;
    }

    public void setContainerExternalValueAttributeDataAccessView(ExternalValueAttributeDataAccessView containerExternalValueAttributeDataAccessView) {
        this.containerExternalValueAttributeDataAccessView = containerExternalValueAttributeDataAccessView;
    }
}
