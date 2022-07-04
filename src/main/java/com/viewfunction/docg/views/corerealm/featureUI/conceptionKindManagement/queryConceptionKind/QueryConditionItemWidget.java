package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;

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
        this.setPadding(true);
        this.setWidth(350, Unit.PIXELS);

        HorizontalLayout attributeMetaLayout = new HorizontalLayout();
        attributeMetaLayout.setSpacing(false);
        attributeMetaLayout.setMargin(false);
        attributeMetaLayout.setPadding(false);
        attributeMetaLayout.setWidth(100, Unit.PERCENTAGE);
        add(attributeMetaLayout);

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

        propertyTypeIcon.setSize("10px");
        attributeNameInfoContainer.add(propertyTypeIcon);
        Label attributeNameLabel = new Label(attributeName);
        attributeNameInfoContainer.add(attributeNameLabel);
        attributeNameInfoContainer.setFlexGrow(1,attributeNameLabel);

        Label attributeTypeLabel = new Label(attributeDataType.toString());
        attributeTypeLabel.addClassNames("text-tertiary");

        attributeTypeLabel.getStyle().set("font-size","0.6rem")
                .set("color","var(--lumo-contrast-70pct)");


        attributeMetaInfoContainer.add(attributeTypeLabel);


        attributeMetaLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeMetaInfoContainer);

        HorizontalLayout controlButtonsContainer = new HorizontalLayout();
        controlButtonsContainer.setPadding(false);
        controlButtonsContainer.setMargin(false);
        controlButtonsContainer.setSpacing(false);

        Button linkButton = new Button();
        linkButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        linkButton.setIcon(VaadinIcon.EYE.create());
        controlButtonsContainer.add(linkButton);
        Button linkButton2 = new Button();
        linkButton2.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        linkButton2.setIcon(VaadinIcon.EYE.create());
        controlButtonsContainer.add(linkButton2);
        Button linkButton3 = new Button();
        linkButton3.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        linkButton3.setIcon(VaadinIcon.EYE.create());
        controlButtonsContainer.add(linkButton3);
        Button linkButton4 = new Button();
        linkButton4.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
        linkButton4.setIcon(VaadinIcon.EYE.create());
        controlButtonsContainer.add(linkButton4);

        controlButtonsContainer.setVerticalComponentAlignment(Alignment.START,linkButton,linkButton2,linkButton3,linkButton4);

        attributeMetaLayout.add(controlButtonsContainer);
        attributeMetaLayout.setVerticalComponentAlignment(Alignment.START,controlButtonsContainer);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(100,Unit.PERCENTAGE);
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);
    }


}
