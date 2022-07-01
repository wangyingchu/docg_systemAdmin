package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

public class ConceptionKindQueryCriteriaView extends VerticalLayout {
    private String conceptionKindName;
    private ComboBox queryCriteriaFilterSelect;
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

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"添加查询条件或显示属性");
        filterDropdownSelectorContainerLayout.add(infoTitle2);

        queryCriteriaFilterSelect = new ComboBox();
        queryCriteriaFilterSelect.setPageSize(30);
        queryCriteriaFilterSelect.setPlaceholder("查询条件/显示属性");
        queryCriteriaFilterSelect.setWidth(98, Unit.PERCENTAGE);
        //queryCriteriaFilterSelect.setEmptySelectionAllowed(true);

        queryCriteriaFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);

        filterDropdownSelectorContainerLayout.add(queryCriteriaFilterSelect);

        Button executeQueryButton = new Button("查询概念实体");
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryConceptionEntities();
            }
        });
        add(executeQueryButton);
    }

    private void queryConceptionEntities(){
        ConceptionKindQueriedEvent conceptionKindQueriedEvent = new ConceptionKindQueriedEvent();
        conceptionKindQueriedEvent.setConceptionKindName(this.conceptionKindName);
        ResourceHolder.getApplicationBlackboard().fire(conceptionKindQueriedEvent);
    }
}
