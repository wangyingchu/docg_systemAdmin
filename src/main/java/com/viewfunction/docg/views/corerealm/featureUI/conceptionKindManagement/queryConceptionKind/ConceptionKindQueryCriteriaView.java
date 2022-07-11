package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;

public class ConceptionKindQueryCriteriaView extends VerticalLayout {
    private String conceptionKindName;
    private ComboBox<KindEntityAttributeRuntimeStatistics> queryCriteriaFilterSelect;
    private VerticalLayout criteriaItemsContainer;
    private Registration listener;
    private List<String> resultAttributesList;
    private QueryParameters queryParameters;
    public ConceptionKindQueryCriteriaView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
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

        resultAttributesList = new ArrayList<>();

        queryCriteriaFilterSelect = new ComboBox();
        queryCriteriaFilterSelect.setPageSize(30);
        queryCriteriaFilterSelect.setPlaceholder("选择查询条件或显示属性");
        queryCriteriaFilterSelect.setMinWidth(220,Unit.PIXELS);
        queryCriteriaFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);

        queryCriteriaFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });

        ConceptionKindQueryCriteriaView containerConceptionKindQueryCriteriaView = this;
        queryCriteriaFilterSelect.addValueChangeListener(new HasValue.
                ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                KindEntityAttributeRuntimeStatistics>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                    KindEntityAttributeRuntimeStatistics> comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent) {
                KindEntityAttributeRuntimeStatistics changedItem = comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent.getValue();
                if(changedItem != null){
                    queryCriteriaFilterSelect.setValue(null);
                    String selectedAttribute = changedItem.getAttributeName();
                    addQueryConditionItem(selectedAttribute,changedItem.getAttributeDataType());
                }
            }
        });
        queryCriteriaFilterSelect.setRenderer(createRenderer());
        queryCriteriaFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");

        buttonSpaceDivLayout.add(queryCriteriaFilterSelect);
        buttonSpaceDivLayout.setFlexGrow(1,queryCriteriaFilterSelect);

        Button addCustomQueryCriteriaButton = new Button();
        Tooltips.getCurrent().setTooltip(addCustomQueryCriteriaButton, "添加自定义查询条件/显示属性");
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addCustomQueryCriteriaButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addCustomQueryCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddCustomQueryCriteriaUI();
            }
        });

        buttonSpaceDivLayout.add(addCustomQueryCriteriaButton);
        buttonSpaceDivLayout.setVerticalComponentAlignment(Alignment.CENTER,queryCriteriaFilterSelect);
        buttonSpaceDivLayout.setVerticalComponentAlignment(Alignment.CENTER,addCustomQueryCriteriaButton);

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

        Button executeQueryButton = new Button("查询概念实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryConceptionEntities();
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
                .set("font-size","0.7rem")
                .set("padding-left","20px");
        buttonsContainerLayout.add(resultSetConfigButton);
        buttonsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,resultSetConfigButton);

        this.queryParameters = new QueryParameters();
    }

    private void queryConceptionEntities(){
        ConceptionKindQueriedEvent conceptionKindQueriedEvent = new ConceptionKindQueriedEvent();
        conceptionKindQueriedEvent.setConceptionKindName(this.conceptionKindName);
        conceptionKindQueriedEvent.setResultAttributesList(this.resultAttributesList);
        conceptionKindQueriedEvent.setQueryParameters(this.queryParameters);
        ResourceHolder.getApplicationBlackboard().fire(conceptionKindQueriedEvent);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            criteriaItemsContainer.setHeight(event.getHeight()-280,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            criteriaItemsContainer.setHeight(browserHeight-280,Unit.PIXELS);
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
        int entityAttributesDistributionStatisticSampleRatio = 20000;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList =
                targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        coreRealm.closeGlobalSession();
        queryCriteriaFilterSelect.setItems(kindEntityAttributeRuntimeStatisticsList);
    }

    private Renderer<KindEntityAttributeRuntimeStatistics> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    ${item.attributeName}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeDataType}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindEntityAttributeRuntimeStatistics>of(tpl.toString())
                .withProperty("attributeName", KindEntityAttributeRuntimeStatistics::getAttributeName)
                .withProperty("attributeDataType", KindEntityAttributeRuntimeStatistics::getAttributeDataType);
    }

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

    private void renderQueryResultSetConfigUI(){
        QueryResultSetConfigView queryResultSetConfigView = new QueryResultSetConfigView(this.queryParameters);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"查询结果集参数",null,true,350,580,false);
        fixSizeWindow.setWindowContent(queryResultSetConfigView);
        fixSizeWindow.setModel(true);
        queryResultSetConfigView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderAddCustomQueryCriteriaUI(){
        AddCustomQueryCriteriaUI addCustomQueryCriteriaUI = new AddCustomQueryCriteriaUI();
        addCustomQueryCriteriaUI.setConceptionKindQueryCriteriaView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"添加自定义查询/显示属性",null,true,470,180,false);
        fixSizeWindow.setWindowContent(addCustomQueryCriteriaUI);
        fixSizeWindow.setModel(true);
        addCustomQueryCriteriaUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void addQueryConditionItem(String attributeName, AttributeDataType attributeDataType){
        if(!resultAttributesList.contains(attributeName)){
            resultAttributesList.add(attributeName);
            QueryConditionItemWidget queryConditionItemWidget = new QueryConditionItemWidget(attributeName,attributeDataType);
            queryConditionItemWidget.setContainerDataInstanceQueryCriteriaView(this);
            if(resultAttributesList.size()==1){
                //this one is the default query condition
                queryConditionItemWidget.setAsDefaultQueryConditionItem();
            }
            criteriaItemsContainer.add(queryConditionItemWidget);
        }
    }
}
