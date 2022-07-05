package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import dev.mett.vaadin.tooltip.Tooltips;

public class QueryConditionItemWidget extends VerticalLayout {

    private final String PropertyTypeClassification_BOOLEAN = "BOOLEAN";
    private final String PropertyTypeClassification_INT = "INT";
    private final String PropertyTypeClassification_SHORT = "SHORT";
    private final String PropertyTypeClassification_LONG = "LONG";
    private final String PropertyTypeClassification_FLOAT = "FLOAT";
    private final String PropertyTypeClassification_DOUBLE = "DOUBLE";
    private final String PropertyTypeClassification_DATE = "DATE";
    private final String PropertyTypeClassification_TIMESTAMP = "TIMESTAMP";
    private final String PropertyTypeClassification_STRING = "STRING";
    private final String PropertyTypeClassification_BINARY = "BINARY";
    private final String PropertyTypeClassification_BYTE = "BYTE";
    private final String PropertyTypeClassification_DECIMAL = "DECIMAL";

    private Label propertyNameLabel;

    private boolean reverseCondition=false;
    private boolean isFirstQueryCondition=false;
    private String filteringLogic_AND="AND";
    private String filteringLogic_OR="OR";
    private String filteringLogic;
    private Button filteringLogicNotButton;
    private Button filteringLogicOrButton;
    private Button filteringLogicAndButton;
    private Button clearFilteringLogicButton;
    private ComboBox filteringItemTypeSelection;
    private HorizontalLayout conditionValueInputElementsLayout;

    private final String FilteringItemType_Equal="Equal";
    private final String FilteringItemType_NotEqual="Not Equal";
    private final String FilteringItemType_SimilarTo="Similar To";
    private final String FilteringItemType_RegularMatch="Regular Match";
    private final String FilteringItemType_Between= "Between";
    private final String FilteringItemType_GreatThan="Great Than";
    private final String FilteringItemType_GreatThanEqual="Great Than Equal";
    private final String FilteringItemType_LessThan="Less Than";
    private final String FilteringItemType_LessThanEqual="Less Than Equal";
    private final String FilteringItemType_InValue="In Value";
    private final String FilteringItemType_NullValue="Null Value";

    private final String SimilarToMatchingType_BeginWith="Begin With";
    private final String SimilarToMatchingType_EndWith="End With";
    private final String SimilarToMatchingType_Contain="Contain";

    private String currentSelectedFilteringItemType;
    //private AbstractComponent singleQueryValueTextField;
    //private MultiValuePropertyInput multiValuePropertyInput;
    //private AbstractComponent betweenQueryFromValueTextField;
    //private AbstractComponent betweenQueryToValueTextField;
    private ComboBox similarToMatchingTypeSelector;
    private TextField similarToConditionValueTextField;
    private boolean isFromBaseDataset;

    private AttributeDataType attributeDataType;
    private Icon plusIcon;
    private Icon multiIcon;
    public QueryConditionItemWidget(String attributeName, AttributeDataType attributeDataType){
        this.attributeDataType = attributeDataType;
        this.setPadding(true);
        this.setMargin(false);
        this.setSpacing(false);
        this.setWidth(340, Unit.PIXELS);

        HorizontalLayout attributeMetaLayout = new HorizontalLayout();
        attributeMetaLayout.setSpacing(false);
        attributeMetaLayout.setMargin(false);
        attributeMetaLayout.setPadding(false);
        attributeMetaLayout.setWidth(320, Unit.PIXELS);

        Scroller queryConditionItemScroller = new Scroller(attributeMetaLayout);
        add(queryConditionItemScroller);

        VerticalLayout attributeMetaInfoContainer = new VerticalLayout();
        attributeMetaInfoContainer.setSpacing(false);
        attributeMetaInfoContainer.setMargin(false);
        attributeMetaInfoContainer.setPadding(false);

        attributeMetaLayout.add(attributeMetaInfoContainer);

        HorizontalLayout attributeNameInfoContainer = new HorizontalLayout();
        attributeNameInfoContainer.setWidth(100,Unit.PERCENTAGE);
        attributeMetaInfoContainer.add(attributeNameInfoContainer);

        Icon propertyTypeIcon = null;
        if(attributeName.startsWith(RealmConstant.RealmInnerTypePerFix) ||
                attributeName.equals(RealmConstant._createDateProperty) ||
                attributeName.equals(RealmConstant._lastModifyDateProperty) ||
                attributeName.equals(RealmConstant._creatorIdProperty)||
                attributeName.equals(RealmConstant._dataOriginProperty)
        ){
            propertyTypeIcon = VaadinIcon.ELLIPSIS_CIRCLE_O.create();
        }else{
            propertyTypeIcon = VaadinIcon.ELLIPSIS_CIRCLE.create();
        }

        propertyTypeIcon.setSize("12px");
        attributeNameInfoContainer.add(propertyTypeIcon);
        attributeNameInfoContainer.setVerticalComponentAlignment(Alignment.STRETCH,propertyTypeIcon);

        Label attributeNameLabel = new Label(attributeName);
        attributeNameLabel.getStyle().set("font-size","0.75rem").set("font-weight","bold").set("padding-right","5px");
        attributeNameInfoContainer.add(attributeNameLabel);
        attributeNameInfoContainer.setFlexGrow(1,attributeNameLabel);

        Label attributeTypeLabel = new Label(attributeDataType.toString());
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-70pct)").set("padding-left","20px");
        attributeMetaInfoContainer.add(attributeTypeLabel);

        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        filteringLogicAndButton = new Button();
        filteringLogicAndButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_SMALL);
        plusIcon = VaadinIcon.PLUS.create();
        plusIcon.setSize("26px");
        filteringLogicAndButton.setIcon(plusIcon);
        Tooltips.getCurrent().setTooltip(filteringLogicAndButton, "与逻辑过滤");
        filteringLogicAndButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                setFilteringLogic(filteringLogic_AND);
            }
        });
        controlButtonsContainer.add(filteringLogicAndButton);

        filteringLogicOrButton = new Button();
        filteringLogicOrButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        multiIcon = VaadinIcon.CLOSE.create();
        multiIcon.setSize("16px");
        filteringLogicOrButton.setIcon(multiIcon);
        Tooltips.getCurrent().setTooltip(filteringLogicOrButton, "或逻辑过滤");
        filteringLogicOrButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                setFilteringLogic(filteringLogic_OR);
            }
        });
        controlButtonsContainer.add(filteringLogicOrButton);

        filteringLogicNotButton = new Button();
        filteringLogicNotButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        filteringLogicNotButton.setIcon(VaadinIcon.BAN.create());
        Tooltips.getCurrent().setTooltip(filteringLogicNotButton, "非逻辑过滤");
        controlButtonsContainer.add(filteringLogicNotButton);
        clearFilteringLogicButton = new Button();
        clearFilteringLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        clearFilteringLogicButton.setIcon(VaadinIcon.ERASER.create());
        Tooltips.getCurrent().setTooltip(clearFilteringLogicButton, "撤销此过滤（显示）条件");
        controlButtonsContainer.add(clearFilteringLogicButton);

        controlButtonsContainer.setVerticalComponentAlignment(Alignment.START,filteringLogicAndButton,filteringLogicOrButton,filteringLogicNotButton,clearFilteringLogicButton);
        attributeMetaLayout.add(controlButtonsContainer);
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.START,controlButtonsContainer);

        HorizontalLayout filterItemsContainer = new HorizontalLayout();
        filterItemsContainer.setPadding(false);
        filterItemsContainer.setMargin(false);
        filterItemsContainer.setSpacing(false);
        add(filterItemsContainer);

        Icon filterItemsIcon = VaadinIcon.FILTER.create();
        filterItemsContainer.add(filterItemsIcon);
        filterItemsIcon.setSize("10px");
        filterItemsIcon.getStyle().set("padding-right","3px");
        filterItemsContainer.setVerticalComponentAlignment(Alignment.CENTER,filterItemsIcon);

        this.filteringItemTypeSelection = new ComboBox();
        this.filteringItemTypeSelection.setPlaceholder("属性过滤条件");
        this.filteringItemTypeSelection.setRequiredIndicatorVisible(false);
        this.filteringItemTypeSelection.setWidth(95,Unit.PIXELS);
        this.filteringItemTypeSelection.getStyle()
                .set("--vaadin-combo-box-overlay-width", "180px")
                .set("font-size","0.65rem")
                .set("padding-right","5px");
        this.filteringItemTypeSelection.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        this.filteringItemTypeSelection.setPageSize(11);
        this.filteringItemTypeSelection.setAllowCustomValue(false);
        this.filteringItemTypeSelection.setPreventInvalidInput(true);

        this.filteringItemTypeSelection.addValueChangeListener(new HasValue.ValueChangeListener<
                AbstractField.ComponentValueChangeEvent<ComboBox<String>,String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>,String> valueChangeEvent) {
                String changedItem = valueChangeEvent.getValue();
                if(changedItem != null){
                    TextField textField = new TextField();
                    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                    textField.setWidth(210,Unit.PIXELS);
                    conditionValueInputElementsLayout.add(textField);
                }else{
                    conditionValueInputElementsLayout.removeAll();
                }
            }
        });

        /*
        this.filteringItemTypeSelection.addValueChangeListener(new HasValue.ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getValue() != null) {
                    String filteringItemType = valueChangeEvent.getValue().toString();
                    renderFilteringItemInputElements(filteringItemType);
                }else{
                    cleanFilteringItemInputElements();
                }
            }
        });
        */
        filterItemsContainer.add(this.filteringItemTypeSelection);

        this.conditionValueInputElementsLayout = new HorizontalLayout();
        filterItemsContainer.add(this.conditionValueInputElementsLayout);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(100,Unit.PERCENTAGE);
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("padding-top","3px");
        add(spaceDivLayout2);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        setQueryConditionSelectionByDataType();
    }

    private void setQueryConditionSelectionByDataType(){
        if(this.attributeDataType !=null) {
            String propertyDataType = this.attributeDataType.toString();
            switch (propertyDataType) {
                case PropertyTypeClassification_STRING:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_SimilarTo,
                            FilteringItemType_RegularMatch,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_BOOLEAN:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_DATE:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_TIMESTAMP:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_INT:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_LONG:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_DOUBLE:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_FLOAT:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_SHORT:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_DECIMAL:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_BYTE:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_Between,
                            FilteringItemType_GreatThan,
                            FilteringItemType_GreatThanEqual,
                            FilteringItemType_LessThan,
                            FilteringItemType_LessThanEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case PropertyTypeClassification_BINARY:
                    break;
            }
        }
    }

    private void setFilteringLogic(String filteringLogicValue){
        this.filteringLogic = filteringLogicValue;
        this.filteringLogicOrButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicAndButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicOrButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.filteringLogicAndButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        if(this.filteringLogic.equals(filteringLogic_OR)){
            multiIcon.setSize("22px");
            plusIcon.setSize("20px");
            this.filteringLogicOrButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            this.filteringLogicAndButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        }
        if(this.filteringLogic.equals(filteringLogic_AND)){
            plusIcon.setSize("26px");
            multiIcon.setSize("16px");
            this.filteringLogicAndButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            this.filteringLogicOrButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        }
    }
}
