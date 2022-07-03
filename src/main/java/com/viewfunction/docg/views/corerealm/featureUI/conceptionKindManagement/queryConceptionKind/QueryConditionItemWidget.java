package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;

public class QueryConditionItemWidget extends VerticalLayout {

    private final String PropertyTypeClassification_BOOLEAN = "BOOLEAN";
    private final String PropertyTypeClassification_INT = "INT";
    private final String PropertyTypeClassification_SHORT = "SHORT";
    private final String PropertyTypeClassification_LONG = "LONG";
    private final String PropertyTypeClassification_FLOAT = "FLOAT";
    private final String PropertyTypeClassification_DOUBLE = "DOUBLE";
    private final String PropertyTypeClassification_DATE = "DATE";
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

    public QueryConditionItemWidget(String attributeName, AttributeDataType attributeDataType){
        this.setWidth(320, Unit.PIXELS);

        add(new Label(attributeName));

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(100,Unit.PERCENTAGE);
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);
    }


}
