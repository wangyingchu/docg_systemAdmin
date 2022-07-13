package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.timepicker.TimePickerVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.data.validator.*;
import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import dev.mett.vaadin.tooltip.Tooltips;

import java.math.BigDecimal;

public class QueryConditionItemWidget extends VerticalLayout {

    private Label propertyNameLabel;

    private boolean reverseCondition = false;
    private boolean isFirstQueryCondition = false;
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


    private AttributeDataType attributeDataType;
    private String attributeName;
    private Icon plusIcon;
    private Icon multiIcon;
    private Icon notIcon;
    private ConceptionKindQueryCriteriaView containerDataInstanceQueryCriteriaView;
    private Label isDefaultLabel;
    private Label joinTypeLabel;
    private Label isConvertedLabel;
    private Component singleQueryValueTextField;
    private Component multiValuePropertyInput;
    private Component betweenQueryFromValueTextField;
    private Component betweenQueryToValueTextField;
    private Binder<String> binder;

    public QueryConditionItemWidget(String attributeName, AttributeDataType attributeDataType,Binder<String> binder){
        this.attributeName = attributeName;
        this.attributeDataType = attributeDataType;
        this.binder = binder;
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

        HorizontalLayout conditionStatusContainer = new HorizontalLayout();
        conditionStatusContainer.setPadding(false);
        conditionStatusContainer.setMargin(false);
        conditionStatusContainer.setSpacing(false);
        attributeMetaInfoContainer.add(conditionStatusContainer);

        Label attributeTypeLabel = new Label(attributeDataType.toString());
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-70pct)").set("padding-left","20px");
        conditionStatusContainer.add(attributeTypeLabel);

        isDefaultLabel = new Label("D");
        isDefaultLabel.addClassNames("text-tertiary");
        isDefaultLabel.getStyle().set("font-size","0.5rem").set("color","var(--lumo-contrast-50pct)").set("padding-left","15px");
        conditionStatusContainer.add(isDefaultLabel);

        joinTypeLabel = new Label("AND");
        joinTypeLabel.addClassNames("text-tertiary");
        joinTypeLabel.getStyle().set("font-size","0.5rem").set("color","var(--lumo-contrast-50pct)").set("padding-left","15px");
        conditionStatusContainer.add(joinTypeLabel);

        isConvertedLabel = new Label("| NOT");
        isConvertedLabel.addClassNames("text-tertiary");
        isConvertedLabel.getStyle().set("font-size","0.5rem").set("color","var(--lumo-contrast-50pct)").set("padding-left","5px");
        conditionStatusContainer.add(isConvertedLabel);

        conditionStatusContainer.setVerticalComponentAlignment(Alignment.CENTER,isDefaultLabel,joinTypeLabel,isConvertedLabel);
        isConvertedLabel.setVisible(reverseCondition);
        isDefaultLabel.setVisible(isFirstQueryCondition);

        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        filteringLogicAndButton = new Button();
        filteringLogicAndButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_SMALL);
        plusIcon = VaadinIcon.PLUS.create();
        plusIcon.setSize("24px");
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
        filteringLogicNotButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        notIcon = VaadinIcon.BAN.create();
        notIcon.setSize("20px");
        filteringLogicNotButton.setIcon(notIcon);
        Tooltips.getCurrent().setTooltip(filteringLogicNotButton, "非逻辑过滤");
        filteringLogicNotButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                setReverseConditionLogic();
            }
        });
        controlButtonsContainer.add(filteringLogicNotButton);

        clearFilteringLogicButton = new Button();
        clearFilteringLogicButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        clearFilteringLogicButton.setIcon(VaadinIcon.ERASER.create());
        Tooltips.getCurrent().setTooltip(clearFilteringLogicButton, "撤销此过滤（显示）条件");
        clearFilteringLogicButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                removeCurrentConditionLogic();
            }
        });
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
                cleanFilteringItemInputElements();
                String changedItem = valueChangeEvent.getValue();
                if(changedItem != null){
                    renderFilteringItemInputElements(changedItem);
                }
            }
        });
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
        if(this.getAttributeDataType() !=null) {
            switch (this.getAttributeDataType()) {
                case STRING:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_SimilarTo,
                            FilteringItemType_RegularMatch,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case BOOLEAN:
                    this.filteringItemTypeSelection.setItems(
                            FilteringItemType_Equal,
                            FilteringItemType_NotEqual,
                            FilteringItemType_InValue,
                            FilteringItemType_NullValue);
                    break;
                case DATE:
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
                case TIMESTAMP:
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
                case DATETIME:
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
                case TIME:
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
                case INT:
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
                case LONG:
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
                case DOUBLE:
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
                case FLOAT:
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
                case SHORT:
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
                case DECIMAL:
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
                case BYTE:
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
                case BINARY:
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
            multiIcon.setSize("20px");
            plusIcon.setSize("20px");
            this.filteringLogicOrButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            this.filteringLogicAndButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            this.joinTypeLabel.setText("OR");
        }
        if(this.filteringLogic.equals(filteringLogic_AND)){
            plusIcon.setSize("24px");
            multiIcon.setSize("16px");
            this.filteringLogicAndButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            this.filteringLogicOrButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            this.joinTypeLabel.setText("AND");
        }
    }

    private void setReverseConditionLogic(){
        this.reverseCondition = !this.reverseCondition;
        this.isConvertedLabel.setVisible(this.reverseCondition);
        this.filteringLogicNotButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicNotButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        if(this.reverseCondition){
            this.filteringLogicNotButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        }else{
            this.filteringLogicNotButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        }
    }

    private void cleanFilteringItemInputElements(){
        //this.currentSelectedFilteringItemType = null;
        this.conditionValueInputElementsLayout.removeAll();
    }

    private void removeCurrentConditionLogic(){
        if(this.getContainerDataInstanceQueryCriteriaView() != null){
            this.getContainerDataInstanceQueryCriteriaView().removeCriteriaFilterItem(this);
        }
    }

    public ConceptionKindQueryCriteriaView getContainerDataInstanceQueryCriteriaView() {
        return containerDataInstanceQueryCriteriaView;
    }

    public void setContainerDataInstanceQueryCriteriaView(ConceptionKindQueryCriteriaView containerDataInstanceQueryCriteriaView) {
        this.containerDataInstanceQueryCriteriaView = containerDataInstanceQueryCriteriaView;
    }

    public AttributeDataType getAttributeDataType() {
        return attributeDataType;
    }

    public String getAttributeName(){
        return this.attributeName;
    }

    public void setAsDefaultQueryConditionItem(){
        this.plusIcon.setSize("20px");
        this.multiIcon.setSize("16px");
        this.filteringLogicOrButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicAndButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicOrButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.filteringLogicAndButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.filteringLogicOrButton.setEnabled(false);
        this.filteringLogicAndButton.setEnabled(false);
        this.isFirstQueryCondition = true;
        this.isDefaultLabel.setVisible(true);
        this.joinTypeLabel.setVisible(false);
    }

    public boolean isDefaultQueryConditionItem(){
        return this.isFirstQueryCondition;
    }

    private void renderFilteringItemInputElements(String filteringItemType){
        this.currentSelectedFilteringItemType = filteringItemType;
        this.conditionValueInputElementsLayout.removeAll();
        if(this.singleQueryValueTextField != null){
            this.singleQueryValueTextField = null;
        }
        this.multiValuePropertyInput = null;
        if(this.betweenQueryFromValueTextField != null){
            this.betweenQueryFromValueTextField = null;
        }
        if(this.similarToConditionValueTextField != null){
            this.similarToConditionValueTextField = null;
        }
        if(this.similarToMatchingTypeSelector != null){
            this.similarToMatchingTypeSelector = null;
        }
        switch(filteringItemType){
            case FilteringItemType_Equal:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_NotEqual:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_RegularMatch:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_GreatThan:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_GreatThanEqual:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_LessThan:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_LessThanEqual:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                this.singleQueryValueTextField = generateSingleQueryValueTextField(210);
                this.conditionValueInputElementsLayout.add(this.singleQueryValueTextField);
                break;
            case FilteringItemType_SimilarTo:
                //this.filteringItemTypeSelection.setWidth(105,Unit.PIXELS);
                HorizontalLayout fieldLayout = generateSimilarToQueryValueInputElements();
                this.conditionValueInputElementsLayout.add(fieldLayout);
                break;
            case FilteringItemType_Between:
                //this.filteringItemTypeSelection.setWidth(95,Unit.PIXELS);
                HorizontalLayout betweenFieldLayout = generateBetweenQueryValueInputElements();
                this.conditionValueInputElementsLayout.add(betweenFieldLayout);
                break;
            case FilteringItemType_InValue:
                //this.filteringItemTypeSelection.setWidth(95,Unit.PIXELS);
                this.multiValuePropertyInput = generateInValueQueryValueInputElements();
                this.conditionValueInputElementsLayout.add(this.multiValuePropertyInput);
                break;
            case FilteringItemType_NullValue:
                //this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
                break;
        }
    }

    public Component generateSingleQueryValueTextField(int textFieldWidth) {
        Component currentConditionValueEditor = null;
        switch(this.getAttributeDataType()){
            case INT:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor).withConverter(new StringToIntegerConverter("该项属性值必须为INT类型"))
                        .withValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null, null))
                        .bind(new ValueProvider<String, Integer>() {
                            @Override
                            public Integer apply(String s) {
                                return new Integer(s);
                            }
                        }, new Setter<String, Integer>() {
                            @Override
                            public void accept(String s, Integer intValue) {}
                        });
                ((TextField) currentConditionValueEditor).setValue("0");
                break;
            case BYTE:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DATE:
                currentConditionValueEditor = new DatePicker();
                ((DatePicker)currentConditionValueEditor).addThemeVariants(DatePickerVariant.LUMO_SMALL);
                ((DatePicker)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((DatePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case LONG:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToLongConverter("该项属性值必须为LONG类型"))
                        .withValidator(new LongRangeValidator("该项属性值必须为LONG类型", null, null))
                        .bind(new ValueProvider<String, Long>() {
                            @Override
                            public Long apply(String s) {
                                return new Long(s);
                            }
                        }, new Setter<String, Long>() {
                            @Override
                            public void accept(String s, Long longValue) {}
                        });
                ((TextField) currentConditionValueEditor).setValue("0");
                break;
            case FLOAT:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToFloatConverter("该项属性值必须为FLOAT类型"))
                        .withValidator(new FloatRangeValidator("该项属性值必须为FLOAT类型", null, null))
                        .bind(new ValueProvider<String, Float>() {
                            @Override
                            public Float apply(String s) {
                                return new Float(s);
                            }
                        }, new Setter<String, Float>() {
                            @Override
                            public void accept(String s, Float floatValue) {}
                        });
                ((TextField) currentConditionValueEditor).setValue("0.0");
                break;
            case SHORT:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToIntegerConverter("该项属性值必须为SHORT类型"))
                        .withValidator(new IntegerRangeValidator("该项属性值必须为SHORT类型", null, null))
                        .bind(new ValueProvider<String, Integer>() {
                            @Override
                            public Integer apply(String s) {
                                return new Integer(s);
                            }
                        }, new Setter<String, Integer>() {
                            @Override
                            public void accept(String s, Integer shortValue) {}
                        });
                ((TextField) currentConditionValueEditor).setValue("0");
                break;
            case BINARY:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DOUBLE:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToDoubleConverter("该项属性值必须为DOUBLE类型"))
                        .withValidator(new DoubleRangeValidator("该项属性值必须为DOUBLE类型", null, null))
                        .bind(new ValueProvider<String, Double>() {
                            @Override
                            public Double apply(String s) {
                                return new Double(s);
                            }
                        }, new Setter<String, Double>() {
                            @Override
                            public void accept(String s, Double doubleValue) {}
                        });
                ((TextField) currentConditionValueEditor).setValue("0.0");
                break;
            case STRING:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case BOOLEAN:
                currentConditionValueEditor = new ComboBox();
                ((ComboBox) currentConditionValueEditor).addThemeVariants(ComboBoxVariant.LUMO_SMALL);
                ((ComboBox) currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((ComboBox) currentConditionValueEditor).setPreventInvalidInput(true);
                ((ComboBox) currentConditionValueEditor).setAllowCustomValue(false);
                ((ComboBox) currentConditionValueEditor).setItems("true","false");
                ((ComboBox) currentConditionValueEditor).setValue("true");
                break;
            case DECIMAL:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToBigDecimalConverter("该项属性值必须为DECIMAL类型"))
                        .withValidator(new BigDecimalRangeValidator("该项属性值必须为DECIMAL类型", null, null))
                        .bind(new ValueProvider<String, BigDecimal>() {
                            @Override
                            public BigDecimal apply(String s) {
                                return new BigDecimal(s);
                            }
                        }, new Setter<String, BigDecimal>() {
                            @Override
                            public void accept(String s, BigDecimal bigDecimalValue) {}
                        });
                ((TextField) currentConditionValueEditor).setValue("0.0");
                break;
            case TIMESTAMP:
                currentConditionValueEditor = new TextField();
                ((TextField)currentConditionValueEditor).addThemeVariants(TextFieldVariant.LUMO_SMALL);
                ((TextField)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TextField)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                binder.forField((TextField)currentConditionValueEditor)
                        .withConverter(
                                new StringToDateConverter())
                        .withValidator((Validator)new DateTimeRangeValidator("该项属性值必须为TIMESTAMP类型",null,null) )
                        .bind(new ValueProvider<String, Double>() {
                            @Override
                            public Double apply(String s) {
                                return new Double(s);
                            }
                        }, new Setter<String, Double>() {
                            @Override
                            public void accept(String s, Double doubleValue) {}
                        });
                break;
            case TIME:
                currentConditionValueEditor = new TimePicker();
                ((TimePicker)currentConditionValueEditor).addThemeVariants(TimePickerVariant.LUMO_SMALL);
                ((TimePicker)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((TimePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
                break;
            case DATETIME:
                currentConditionValueEditor = new DateTimePicker();
                ((DateTimePicker)currentConditionValueEditor).addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
                ((DateTimePicker)currentConditionValueEditor).setWidth(textFieldWidth,Unit.PIXELS);
                ((DateTimePicker)currentConditionValueEditor).getStyle().set("font-size","1.0rem");
        }
        return currentConditionValueEditor;
    }

    public HorizontalLayout generateBetweenQueryValueInputElements(){
        HorizontalLayout containerHorizontalLayout = new HorizontalLayout();
        containerHorizontalLayout.setSpacing(false);
        this.betweenQueryFromValueTextField = generateSingleQueryValueTextField(100);
        containerHorizontalLayout.add(this.betweenQueryFromValueTextField);
        Icon divIcon = VaadinIcon.MINUS.create();
        divIcon.setSize("10px");
        divIcon.getStyle().set("padding-left","3px").set("padding-right","3px");
        containerHorizontalLayout.add(divIcon);
        containerHorizontalLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);
        this.betweenQueryToValueTextField = generateSingleQueryValueTextField(100);
        containerHorizontalLayout.add(this.betweenQueryToValueTextField);
        return containerHorizontalLayout;
    }

    public HorizontalLayout generateSimilarToQueryValueInputElements(){
        HorizontalLayout containerHorizontalLayout=new HorizontalLayout();
        containerHorizontalLayout.setSpacing(false);
        this.similarToMatchingTypeSelector = new ComboBox();
        this.similarToMatchingTypeSelector.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        this.similarToMatchingTypeSelector.setWidth(65,Unit.PIXELS);
        this.similarToMatchingTypeSelector.getStyle()
                .set("--vaadin-combo-box-overlay-width", "130px")
                .set("font-size","0.65rem")
                .set("padding-right","5px");
        this.similarToMatchingTypeSelector.setAllowCustomValue(false);
        this.similarToMatchingTypeSelector.setItems(
                SimilarToMatchingType_BeginWith,
                SimilarToMatchingType_EndWith,
                SimilarToMatchingType_Contain);
        this.similarToMatchingTypeSelector.setValue(SimilarToMatchingType_BeginWith);
        containerHorizontalLayout.add(this.similarToMatchingTypeSelector);
        this.similarToConditionValueTextField = new TextField();
        this.similarToConditionValueTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        this.similarToConditionValueTextField.setWidth(140,Unit.PIXELS);
        this.similarToConditionValueTextField.getStyle().set("font-size","1.0rem");
        containerHorizontalLayout.add(this.similarToConditionValueTextField);
        return containerHorizontalLayout;
    }

    public Component generateInValueQueryValueInputElements(){
        MultiValuePropertyInputWidget multiValuePropertyInput =new MultiValuePropertyInputWidget(215);
        multiValuePropertyInput.setQueryConditionItemWidget(this);
        return multiValuePropertyInput;
    }
}
