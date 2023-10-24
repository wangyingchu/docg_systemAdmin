package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.filteringItem.FilteringItem;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.AddCustomQueryCriteriaUI;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.KindQueryCriteriaView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.QueryConditionItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery.QueryResultSetConfigView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AttributesQueryCriteriaView extends VerticalLayout implements KindQueryCriteriaView {
    private VerticalLayout criteriaItemsContainer;
    private List<String> resultAttributesList;
    private QueryParameters queryParameters;
    private Binder<String> queryConditionDataBinder;
    private boolean defaultQueryConditionIsSet = true;
    private boolean otherQueryConditionsAreSet = false;
    private Dialog containerDialog;
    public AttributesQueryCriteriaView(){
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"设定查询条件属性:");
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

        HorizontalLayout buttonSpaceDivLayout = new HorizontalLayout();
        buttonSpaceDivLayout.setWidth(99, Unit.PERCENTAGE);

        resultAttributesList = new ArrayList<>();
        Button addCustomQueryCriteriaButton = new Button("添加自定义查询条件/显示属性",VaadinIcon.KEYBOARD_O.create());

        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        addCustomQueryCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddCustomQueryCriteriaUI();
            }
        });

        buttonSpaceDivLayout.add(addCustomQueryCriteriaButton);
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

        Button executeQueryButton = new Button("确定");
        executeQueryButton.setIcon(new Icon(VaadinIcon.CHECK));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(getContainerDialog() != null){
                    getContainerDialog().close();
                }
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

    private void renderAddCustomQueryCriteriaUI(){
        AddCustomQueryCriteriaUI addCustomQueryCriteriaUI = new AddCustomQueryCriteriaUI();
        addCustomQueryCriteriaUI.setKindQueryCriteriaView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"添加自定义查询/显示属性",null,true,470,150,false);
        fixSizeWindow.setWindowContent(addCustomQueryCriteriaUI);
        fixSizeWindow.setModel(true);
        addCustomQueryCriteriaUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderQueryResultSetConfigUI(){
        QueryResultSetConfigView queryResultSetConfigView = new QueryResultSetConfigView(this.queryParameters);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"查询结果集参数",null,true,350,500,false);
        fixSizeWindow.setWindowContent(queryResultSetConfigView);
        fixSizeWindow.setModel(true);
        queryResultSetConfigView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void setViewHeight(int heightValue){
        criteriaItemsContainer.setHeight(heightValue-100,Unit.PIXELS);
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void addQueryConditionItem(String attributeName, AttributeDataType attributeDataType) {
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

    @Override
    public void removeCriteriaFilterItem(QueryConditionItemWidget queryConditionItemWidget) {
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

    private void setDefaultQueryConditionIsSet(boolean defaultQueryConditionIsSet) {
        this.defaultQueryConditionIsSet = defaultQueryConditionIsSet;
    }

    private void setOtherQueryConditionsAreSet(boolean otherQueryConditionsAreSet) {
        this.otherQueryConditionsAreSet = otherQueryConditionsAreSet;
    }

    public List<String> getResultAttributesList(){
        return resultAttributesList;
    }
    public QueryParameters getQueryParameters(){
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
            return null;
        }else{
            return queryParameters;
        }
    }
    public boolean isCorrectQueryCriteria(){
        getQueryParameters();
        if(!defaultQueryConditionIsSet & otherQueryConditionsAreSet){
            return false;
        }else{
            return true;
        }
    }
}
