package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

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

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;

public class AddConceptionEntityView extends VerticalLayout {

    private String conceptionKindName;
    private VerticalLayout criteriaItemsContainer;
    private List<String> resultAttributesList;
    private ComboBox<KindEntityAttributeRuntimeStatistics> kindExistingAttributeFilterSelect;

    public AddConceptionEntityView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setMargin(false);
        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        VerticalLayout filterDropdownSelectorContainerLayout = new VerticalLayout();
        filterDropdownSelectorContainerLayout.setPadding(false);
        filterDropdownSelectorContainerLayout.setSpacing(false);
        filterDropdownSelectorContainerLayout.setMargin(false);
        add(filterDropdownSelectorContainerLayout);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定实体属性");
        filterDropdownSelectorContainerLayout.add(infoTitle2);

        HorizontalLayout buttonSpaceDivLayout = new HorizontalLayout();
        buttonSpaceDivLayout.setWidth(99, Unit.PERCENTAGE);

        resultAttributesList = new ArrayList<>();

        kindExistingAttributeFilterSelect = new ComboBox();
        kindExistingAttributeFilterSelect.setPageSize(30);
        kindExistingAttributeFilterSelect.setPlaceholder("选择要添加的实体属性");
        kindExistingAttributeFilterSelect.setMinWidth(220,Unit.PIXELS);
        kindExistingAttributeFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        buttonSpaceDivLayout.add(kindExistingAttributeFilterSelect);
        buttonSpaceDivLayout.setFlexGrow(1, kindExistingAttributeFilterSelect);

        kindExistingAttributeFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });

        kindExistingAttributeFilterSelect.addValueChangeListener(new HasValue.
                ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                KindEntityAttributeRuntimeStatistics>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                    KindEntityAttributeRuntimeStatistics> comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent) {
                KindEntityAttributeRuntimeStatistics changedItem = comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent.getValue();
                if(changedItem != null){
                    kindExistingAttributeFilterSelect.setValue(null);
                    String selectedAttribute = changedItem.getAttributeName();
                    addQueryConditionItem(selectedAttribute,changedItem.getAttributeDataType());
                }
            }
        });
        kindExistingAttributeFilterSelect.setRenderer(createRenderer());
        kindExistingAttributeFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");








        Button addCustomQueryCriteriaButton = new Button();
        Tooltips.getCurrent().setTooltip(addCustomQueryCriteriaButton, "添加自定义查询条件/显示属性");
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addCustomQueryCriteriaButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addCustomQueryCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddCustomQueryCriteriaUI();
            }
        });

        buttonSpaceDivLayout.add(addCustomQueryCriteriaButton);
        buttonSpaceDivLayout.setVerticalComponentAlignment(Alignment.CENTER, kindExistingAttributeFilterSelect);
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


    }


    public void addQueryConditionItem(String attributeName, AttributeDataType attributeDataType){
        if(!resultAttributesList.contains(attributeName)){
            resultAttributesList.add(attributeName);

            //QueryConditionItemWidget queryConditionItemWidget = new QueryConditionItemWidget(attributeName,attributeDataType,this.queryConditionDataBinder);
            //queryConditionItemWidget.setContainerDataInstanceQueryCriteriaView(this);
           // if(resultAttributesList.size()==1){
                //this one is the default query condition
             //   queryConditionItemWidget.setAsDefaultQueryConditionItem();
           // }
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setAttributeName(attributeName);
            attributeValue.setAttributeDataType(attributeDataType);
            attributeValue.setAttributeValue("");


            AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.conceptionKindName,null,attributeValue, com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget.KindType.ConceptionKind);
            attributeEditorItemWidget.setWidth(100,Unit.PERCENTAGE);

            criteriaItemsContainer.add(attributeEditorItemWidget);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        /*
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            criteriaItemsContainer.setHeight(event.getHeight()-280,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            criteriaItemsContainer.setHeight(browserHeight-280,Unit.PIXELS);
        }));

         */
        loadQueryCriteriaComboBox();
    }

    private void loadQueryCriteriaComboBox(){
        int entityAttributesDistributionStatisticSampleRatio = 20000;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList =
                targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        coreRealm.closeGlobalSession();
        kindExistingAttributeFilterSelect.setItems(kindEntityAttributeRuntimeStatisticsList);
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
}
